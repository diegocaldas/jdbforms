/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * DbForms - a Rapid Application Development Framework
 * Copyright (C) 2001 Joachim Peer <joepeer@excite.com>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 */

package org.dbforms.servlets;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Category;
import org.dbforms.util.MultipartRequest;
import org.dbforms.util.MessageResources;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.Util;
import org.dbforms.util.SqlUtil;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.MultipleValidationException;
import org.dbforms.config.Table;
import org.dbforms.event.EventEngine;
import org.dbforms.event.DatabaseEvent;
import org.dbforms.event.WebEvent;
import org.dbforms.validation.ValidatorConstants;



/**
 * This servlets is the Controller component in the Model-View-Controller -
 * architecture used by the dbforms-framework. Every request goes through this
 * component and its event dispatching facilities.
 * 
 * @author Joe Peer
 */
public class Controller extends HttpServlet
{
   /** logging category for this class */
   private static Category logCat = Category.getInstance(
                                             Controller.class.getName());

   /** config object */
   private DbFormsConfig config;

   /** 100KB default upload size */
   private int maxUploadSize = 102400;

   /**
    * Initialize this servlet.
    * 
    * @exception ServletException if the initialization fails
    */
   public void init() throws ServletException
   {
      // take Config-Object from application context - this object should have been
      // initalized by Config-Servlet on Webapp/server-startup!
      config = (DbFormsConfig) getServletContext()
                                  .getAttribute(DbFormsConfig.CONFIG);

      // if existing and valid, override default maxUploadSize, which determinates how
      // big the http/multipart uploads may get
      String maxUploadSizeStr = this.getServletConfig()
                                    .getInitParameter("maxUploadSize");

      if (maxUploadSizeStr != null)
      {
         try
         {
            this.maxUploadSize = Integer.parseInt(maxUploadSizeStr);
         }
         catch (NumberFormatException nfe)
         {
            logCat.error("maxUploadSize not a valid number => using default.");
         }
      }
   }


   /**
    * Process an HTTP "GET" request.
    * 
    * @param request The servlet request we are processing
    * @param response The servlet response we are creating
    * 
    * @exception IOException if an input/output error occurs
    * @exception ServletException if a servlet exception occurs
    */
   public void doGet(HttpServletRequest request, HttpServletResponse response)
              throws IOException, ServletException
   {
      process(request, response);
   }


   /**
    * Process an HTTP "POST" request.
    * 
    * @param request The servlet request we are processing
    * @param response The servlet response we are creating
    * 
    * @exception IOException if an input/output error occurs
    * @exception ServletException if a servlet exception occurs
    */
   public void doPost(HttpServletRequest request, HttpServletResponse response)
               throws IOException, ServletException
   {
      process(request, response);
   }


   /**
    * Process the incoming requests.
    * 
    * @param request  the request object
    * @param response  the response object
    * 
    * @throws IOException
    * @throws ServletException
    */
   private void process(HttpServletRequest request, 
                        HttpServletResponse response) throws IOException, 
                                                             ServletException
   {
      HttpSession session = request.getSession(true);
      logCat.debug("session timeout: " + session.getMaxInactiveInterval());

      // create RFC-1867 data wrapper for the case the form was sent in multipart mode
      // this is needed because common HttpServletRequestImpl - classes do not support it
      // the whole application has to access Request via the "ParseUtil" class
      // this is also true for jsp files written by the user
      // #fixme taglib needed for convenient access to ParseUtil wrapper methods
      Hashtable connections = new Hashtable();
      String    contentType = request.getContentType();


      // Verify if Locale have been setted in session with "LOCALE_KEY"
      // if not, take the request.getLocale() as default and put it in session
      // Verify if Locale have been setted in session with "LOCALE_KEY"
      // if not, take the request.getLocale() as default and put it in session
      processLocale(request);

      if ((contentType != null) && contentType.startsWith("multipart"))
      {
         try
         {
            MultipartRequest multipartRequest = new MultipartRequest(request, 
                                                                     maxUploadSize);
            request.setAttribute("multipartRequest", multipartRequest);
         }
         catch (IOException ioe)
         {
            logCat.error(
                     "::process - check if uploaded file(s) exceeded allowed size", 
                     ioe);
            sendErrorMessage("Check if uploaded file(s) exceeded allowed size.", 
                             response);

            return;
         }
      }

      Connection con    = null;
      WebEvent   e      = null;
      Vector     errors = new Vector();

      try
      {
         request.setAttribute("errors", errors);

         EventEngine engine = new EventEngine(request, config);
         e = engine.generatePrimaryEvent();

         // send as info to dbForms (=> Taglib)
         if (e != null)
         {
            request.setAttribute("webEvent", e);
         }

         con = getConnection(request, e.getTableId(), connections);

         // primary event can be any kind of event (database, navigation...)
         if (e instanceof DatabaseEvent)
         {
            try
            {
               // if hidden formValidatorName exist and it's an Update or Insert event,
               // doValidation with Commons-Validator
               String formValidatorName = request.getParameter(ValidatorConstants.FORM_VALIDATOR_NAME
                                                               + "_"
                                                               + e.getTableId());

               if (formValidatorName != null)
               {
                  ((DatabaseEvent) e).doValidation(formValidatorName, 
                                                   getServletContext(), request);
               }

               ((DatabaseEvent) e).processEvent(con);
            }
            catch (SQLException sqle)
            {
               logCat.error("::process - SQLException:", sqle);
               errors.addElement(sqle);
               cleanUpConnectionAfterException(con);
            }
            catch (MultipleValidationException mve)
            {
               processMultipleValidationException(con, errors, mve);
            }
         }

         //else
         //{
         // currently, we support db events ONLY
         // but in future there may be events with processEvent() method which do not need a jdbc con!
         // (you may think: "what about navigation events?" - well they are created by the
         // controller but they get executed in the referncing "DbFormTag" at the jsp -- that's why we
         // do not any further operations on them right here...we just put them into the request)
         //}
         // secondary Events are always database events
         // (in fact, they all are SQL UPDATEs)
         if (engine.getInvolvedTables() != null)
         {
            processInvolvedTables(request, connections, e, errors, engine);
         }
      }
      finally
      {
         // close all the connections stored into the connections hash table;
         closeConnections(connections);

         if (e != null)
         {
            // PG  - if form contained errors, use followupOnError (if available!)
            String followUpError = e.getFollowUpOnError();

            if ((errors.size() != 0) && (followUpError != null)
                      && (followUpError.trim().length() > 0))
            {
               request.getRequestDispatcher(followUpError)
                      .forward(request, response);
            }
            else
            {
               request.getRequestDispatcher(e.getFollowUp())
                      .forward(request, response);
            }
         }
      }
   }


   /**
    * PRIVATE METHODS here
    */
   /**
    *  Process tables related to the main event table.
    *  <br>
    *  For every table related to the parent one, generate secundary (update) events 
    *  for that table and execute them.
    *  
    * @param request      the request object
    * @param connections  the connections hashTable
    * @param e            the main webEvent object
    * @param errors       the errors vector
    * @param engine       the eventEngine reference
    */
   private void processInvolvedTables(HttpServletRequest request, 
                                      Hashtable connections, WebEvent e, 
                                      Vector errors, EventEngine engine)
   {
      Connection con;

      // may be null if empty form!
      Enumeration tableEnum = engine.getInvolvedTables().elements();

      while (tableEnum.hasMoreElements())
      {
         Table       t         = (Table) tableEnum.nextElement();
         Enumeration eventEnum = engine.generateSecundaryEvents(e);

         // scan all the secundary events for the current secundary table;
         while (eventEnum.hasMoreElements())
         {
            DatabaseEvent dbE = (DatabaseEvent) eventEnum.nextElement();

            // 2003-02-03 HKK: do not do the work twice - without this every event 
            // would be generated for each table and event
            if (t.getId() == dbE.getTableId())
            {
               con = getConnection(request, dbE.getTableId(), connections);

               // 2003-02-03 HKK: do not do the work twice!!!
               String formValidatorName = request.getParameter(ValidatorConstants.FORM_VALIDATOR_NAME
                                                               + "_"
                                                               + dbE.getTableId());

               try
               {
                  // if hidden formValidatorName exist and it's an Update or Insert event,
                  // doValidation with Commons-Validator
                  if (formValidatorName != null)
                  {
                     dbE.doValidation(formValidatorName, getServletContext(), 
                                      request);
                  }

                  dbE.processEvent(con);
               }
               catch (SQLException sqle2)
               {
                  SqlUtil.logSqlException(sqle2, 
                                          "::process - exception while process secundary events");
                  errors.addElement(sqle2);
                  cleanUpConnectionAfterException(con);
               }
               catch (MultipleValidationException mve)
               {
                  processMultipleValidationException(con, errors, mve);
               }
            }
         }
      }
   }


   /**
    *  Process the input MultipleValidationException object.
    * 
    * @param con    the connection object to close
    * @param errors the errors vector to fill
    * @param mve    the MultipleValidationException to process
    */
   private void processMultipleValidationException(Connection con, 
                                                   Vector errors, 
                                                   MultipleValidationException mve)
   {
      java.util.Vector v = null;

      logCat.error("::processMultipleValidationException - exception", mve);

      if ((v = mve.getMessages()) != null)
      {
         Enumeration enum = v.elements();

         while (enum.hasMoreElements())
         {
            errors.addElement(enum.nextElement());
         }
      }

      cleanUpConnectionAfterException(con);
   }


   /**
    * Send error messages to the servlet's output stream
    * 
    * @param message the message to display
    * @param request the request object
    * @param response the response object
    */
   private void sendErrorMessage(String message, HttpServletResponse response)
   {
      try
      {
         PrintWriter out = response.getWriter();

         response.setContentType("text/html");
         out.println("<html><body><h1>ERROR:</h1><p>");
         out.println(message);
         out.println("</p></body></html>");
      }
      catch (IOException ioe)
      {
         logCat.error("::sendErrorMessage - senderror message crashed", ioe);
      }
   }


   /**
    * In our development, we sometimes set the connection object to 
    * autoCommit = false in the interceptor (Pre... methods). 
    * This allows us to have dbForms do part of
    * the required transaction (other parts are done via jdbc calls). If the
    * database throws an exception, then we need to make sure that the
    * connection is reinitialized (rollbacked) before it is sent back into the
    * connection pool.
    * 
    * Grunikiewicz.philip&at;hydro.qc.ca 2001-10-29 
    * 
    * @param con the connection object
    */
   private void cleanUpConnectionAfterException(Connection con)
   {
      try
      {
         // Do only if autoCommit is disabled
         if ((con != null) && (!con.getAutoCommit()))
         {
            con.rollback();
            con.setAutoCommit(true);
         }
      }
      catch (java.sql.SQLException e)
      {
         SqlUtil.logSqlException(e);
      }
   }


   /**
    * Set the default locale
    * 
    * @param request the request object
    */
   private void processLocale(HttpServletRequest request)
   {
      HttpSession session = request.getSession();

      if (session.getAttribute(MessageResources.LOCALE_KEY) == null)
      {
         session.setAttribute(MessageResources.LOCALE_KEY, request.getLocale());
      }
   }


   /**
    * Gets the connection object. Holds a list of all used connections. So they
    * stay open between calls an can be reused
    * 
    * @param request          the request object
    * @param tableId          the table identifier
    * @param connectionsTable the connections hash table
    * 
    * @return The connection object
    */
   private Connection getConnection(HttpServletRequest request, int tableId, 
                                    Hashtable connectionsTable)
   {
      String     connectionName = null;
      Connection con = null;

      // get the connection name from the request;
      if (tableId != -1)
      {
         connectionName = ParseUtil.getParameter(request, "invname_" + tableId);
      }
      connectionName = Util.isNull(connectionName)?"default":connectionName; 
      if ((con = (Connection) connectionsTable.get(connectionName)) == null)
      {
         con = config.getConnection(connectionName);
         connectionsTable.put(connectionName, con);
      }

      return con;
   }


   /**
    * Close all the connections stored into the the connections HashTable.
    * 
    * @param connectionsTable the connections HashTable
    */
   private void closeConnections(Hashtable connectionsTable)
   {
      Enumeration cons = connectionsTable.keys();

      while (cons.hasMoreElements())
      {
         String     dbConnectionName = (String) cons.nextElement();
         Connection con = (Connection) connectionsTable.get(dbConnectionName);

         try
         {
            // Do only if autoCommit is disabled
            if ((con != null) && (!con.getAutoCommit()))
            {
               con.commit();
               con.setAutoCommit(true);
            }
         }
         catch (java.sql.SQLException e)
         {
            SqlUtil.logSqlException(e);
         }

         SqlUtil.closeConnection(con);
      }
   }
}