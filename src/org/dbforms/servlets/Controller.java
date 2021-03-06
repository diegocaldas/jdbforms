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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.servlets.base.AbstractServletBase;

import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.DbFormsConfigRegistry;
import org.dbforms.config.MultipleValidationException;
import org.dbforms.config.Table;

import org.dbforms.event.AbstractDatabaseEvent;
import org.dbforms.event.EventEngine;
import org.dbforms.event.AbstractWebEvent;

import org.dbforms.util.ParseUtil;
import org.dbforms.util.SqlUtil;
import org.dbforms.util.Util;

import org.dbforms.validation.ValidatorConstants;

import java.io.IOException;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



/**
 * This servlets is the Controller component in the Model-View-Controller -
 * architecture used by the dbforms-framework. Every request goes through this
 * component and its event dispatching facilities.
 *
 * @author Joe Peer
 */
public class Controller extends AbstractServletBase {

   /** logging category for this class */
   private static Log logCat        = LogFactory.getLog(Controller.class.getName());


   /**
    * Initialize this servlet.
    *
    * @exception ServletException if the initialization fails
    */
   public void init() throws ServletException {
      super.init();
   }


   /**
    * Gets the connection object. Holds a list of all used connections. So they
    * stay open between calls an can be reused
    *
    * @param request the request object
    * @param tableId the table identifier
    * @param connectionsTable the connections hash table
    *
    * @return The connection object
    */
   private Connection getConnection(DbFormsConfig      config,
                                    HttpServletRequest request,
                                    int                tableId,
                                    Hashtable          connectionsTable)
                             throws SQLException {
      String     connectionName = null;
      Connection con = null;

      // get the connection name from the request;
      if (tableId != -1) {
         connectionName = ParseUtil.getParameter(request, "invname_" + tableId);
      }

      connectionName = Util.isNull(connectionName) ? "default"
                                                   : connectionName;

      if ((con = (Connection) connectionsTable.get(connectionName)) == null) {
         con = config.getConnection(connectionName);
         connectionsTable.put(connectionName, con);
      }

      return con;
   }


   /**
    * In our development, we sometimes set the connection object to  autoCommit
    * = false in the interceptor (Pre... methods).  This allows us to have
    * dbForms do part of the required transaction (other parts are done via
    * jdbc calls). If the database throws an exception, then we need to make
    * sure that the connection is reinitialized (rollbacked) before it is sent
    * back into the connection pool.  Grunikiewicz.philip&at;hydro.qc.ca
    * 2001-10-29
    *
    * @param con the connection object
    */
   private void cleanUpConnectionAfterException(Connection con) {
      try {
         // Do only if autoCommit is disabled
         if ((con != null) && (!con.getAutoCommit())) {
            con.rollback();
            con.setAutoCommit(true);
         }
      } catch (java.sql.SQLException e) {
         SqlUtil.logSqlException(e);
      }
   }


   /**
    * Close all the connections stored into the the connections HashTable.
    *
    * @param connectionsTable the connections HashTable
    */
   private void closeConnections(Hashtable connectionsTable) {
      Enumeration cons = connectionsTable.keys();

      while (cons.hasMoreElements()) {
         String     dbConnectionName = (String) cons.nextElement();
         Connection con = (Connection) connectionsTable.get(dbConnectionName);

         try {
            // Do only if autoCommit is disabled
            if ((con != null) && (!con.getAutoCommit())) {
               con.commit();
               con.setAutoCommit(true);
            }
         } catch (java.sql.SQLException e) {
            SqlUtil.logSqlException(e);
         }

         SqlUtil.closeConnection(con);
      }
   }


   /**
    * Process the incoming requests.
    *
    * @param request the request object
    * @param response the response object
    *
    * @throws IOException
    * @throws ServletException
    */
   protected void process(HttpServletRequest  request,
                        HttpServletResponse response)
                 throws IOException, ServletException {

      HttpSession session = request.getSession(true);
      logCat.debug("session timeout: " + session.getMaxInactiveInterval());
      
      
      // take Config-Object from application context - this object should have been
      // initalized by Config-Servlet on Webapp/server-startup!
      DbFormsConfig config = null;

      try {
         config = DbFormsConfigRegistry.instance()
                                       .lookup();
      } catch (Exception e) {
         logCat.error(e);
         throw new ServletException(e);
      }


      Hashtable connections = new Hashtable();
      Connection con    = null;
      AbstractWebEvent   e      = null;
      Vector     errors = new Vector();

      try {
         request.setAttribute("errors", errors);

         EventEngine engine = new EventEngine(request, config);
         e = engine.generatePrimaryEvent();

         // send as info to dbForms (=> Taglib)
         if (e != null) {
            request.setAttribute("webEvent", e);
         }

         try {
            con = getConnection(config, request,
                                ((e == null) || (e.getTable() == null)) ? (-1)
                                                                        : e.getTable().getId(),
                                connections);

            // primary event can be any kind of event (database, navigation...)
            if (e instanceof AbstractDatabaseEvent) {
               try {
                  // if hidden formValidatorName exist and it's an Update or Insert event,
                  // doValidation with Commons-Validator
                  String formValidatorName = request.getParameter(ValidatorConstants.FORM_VALIDATOR_NAME
                                                                  + "_"
                                                                  + e.getTable().getId());

                  if (formValidatorName != null) {
                     ((AbstractDatabaseEvent) e).doValidation(formValidatorName,
                                                      getServletContext());
                  }

                  ((AbstractDatabaseEvent) e).processEvent(con);
               } catch (SQLException sqle) {
                  logCat.error("::process - SQLException:", sqle);
                  errors.addElement(sqle);
                  cleanUpConnectionAfterException(con);
               } catch (MultipleValidationException mve) {
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
            if (engine.getInvolvedTables() != null) {
               processInvolvedTables(config, request, connections, e, errors,
                                     engine);
            }
         } catch (SQLException exc) {
            throw new ServletException(exc);
         }
      } finally {
         // close all the connections stored into the connections hash table;
         closeConnections(connections);

         if (e != null) {
            // PG  - if form contained errors, use followupOnError (if available!)
            String followUp = null;

            if (errors.size() != 0) {
               followUp = e.getFollowUpOnError();
            }

            if (Util.isNull(followUp)) {
               followUp = e.getFollowUp();
            }

            logCat.info("*** e = " + e + "*** e.getFollowUp() = "
                        + e.getFollowUp());

            if (!Util.isNull(followUp)) {
               request.getRequestDispatcher(followUp)
                      .forward(request, response);
            }
         }
      }
   }


   /**
    * PRIVATE METHODS here
    *
    * @param request DOCUMENT ME!
    * @param connections DOCUMENT ME!
    * @param e DOCUMENT ME!
    * @param errors DOCUMENT ME!
    * @param engine DOCUMENT ME!
    */
   /**
    * Process tables related to the main event table. <br>
    * For every table related to the parent one, generate secundary (update)
    * events  for that table and execute them.
    *
    * @param request the request object
    * @param connections the connections hashTable
    * @param e the main webEvent object
    * @param errors the errors vector
    * @param engine the eventEngine reference
    */
   private void processInvolvedTables(DbFormsConfig      config,
                                      HttpServletRequest request,
                                      Hashtable          connections,
                                      AbstractWebEvent           e,
                                      Vector             errors,
                                      EventEngine        engine)
                               throws SQLException {
      Connection con;

      // may be null if empty form!
      Enumeration tableEnum = engine.getInvolvedTables()
                                    .elements();

      while (tableEnum.hasMoreElements()) {
         Table       t         = (Table) tableEnum.nextElement();
         Enumeration eventEnum = engine.generateSecundaryEvents(t, e);

         // scan all the secundary events for the current secundary table;
         while (eventEnum.hasMoreElements()) {
            AbstractDatabaseEvent dbE = (AbstractDatabaseEvent) eventEnum.nextElement();

            // 2003-02-03 HKK: do not do the work twice - without this every event 
            // would be generated for each table and event
            if (t.getId() == dbE.getTable()
                                      .getId()) {
               con = getConnection(config, request, dbE.getTable().getId(),
                                   connections);

               // 2003-02-03 HKK: do not do the work twice!!!
               String formValidatorName = request.getParameter(ValidatorConstants.FORM_VALIDATOR_NAME
                                                               + "_"
                                                               + dbE.getTable().getId());

               try {
                  // if hidden formValidatorName exist and it's an Update or Insert event,
                  // doValidation with Commons-Validator
                  if (formValidatorName != null) {
                     dbE.doValidation(formValidatorName, getServletContext());
                  }

                  dbE.processEvent(con);
               } catch (SQLException sqle2) {
                  SqlUtil.logSqlException(sqle2,
                                          "::process - exception while process secundary events");
                  errors.addElement(sqle2);
                  cleanUpConnectionAfterException(con);
               } catch (MultipleValidationException mve) {
                  processMultipleValidationException(con, errors, mve);
               }
            }
         }
      }
   }




   /**
    * Process the input MultipleValidationException object.
    *
    * @param con the connection object to close
    * @param errors the errors vector to fill
    * @param mve the MultipleValidationException to process
    */
   private void processMultipleValidationException(Connection                  con,
                                                   Vector                      errors,
                                                   MultipleValidationException mve) {
      java.util.Vector v = null;

      logCat.error("::processMultipleValidationException - exception", mve);

      if ((v = mve.getMessages()) != null) {
         Enumeration e = v.elements();

         while (e.hasMoreElements()) {
            errors.addElement(e.nextElement());
         }
      }

      cleanUpConnectionAfterException(con);
   }


}
