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

package org.dbforms;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.dbforms.util.*;
import org.dbforms.event.*;
import org.apache.log4j.Category;
import org.apache.commons.validator.ValidatorResources;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorResults;
import org.dbforms.validation.ValidatorConstants;



/****
 * <p>
 * This servlets is the Controller component in the Model-View-Controller - architecture
 * used by the dbforms-framework. Every request goes through this component and its event
 * dispatching facilities.
 * </p>
 *
 * @author Joe Peer <joepeer@excite.com>
 */
public class Controller extends HttpServlet
{
    static Category logCat = Category.getInstance(Controller.class.getName());

    // logging category for this class
    private DbFormsConfig config;
    private int maxUploadSize = 102400; // 100KB default upload size

    /**
     * Initialize this servlet.
     */
    public void init() throws ServletException
    {
        // take Config-Object from application context - this object should have been
        // initalized by Config-Servlet on Webapp/server-startup!
        config = (DbFormsConfig) getServletContext().getAttribute(DbFormsConfig.CONFIG);

        // if existing and valid, override default maxUploadSize, which determinates how
        // big the http/multipart uploads may get
        String maxUploadSizeStr = this.getServletConfig().getInitParameter("maxUploadSize");

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
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
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
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        process(request, response);
    }


    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        // create RFC-1867 data wrapper for the case the form was sent in multipart mode
        // this is needed because common HttpServletRequestImpl - classes do not support it
        // the whole application has to access Request via the "ParseUtil" class
        // this is also true for jsp files written by the user
        // #fixme taglib needed for convenient access to ParseUtil wrapper methods
        // Bradley's multiple connection support [fossato <fossato@pow2.com> 2002/11/04]:
        // the connections HashTable;
        Hashtable connections = new Hashtable();
        String contentType = request.getContentType();
        String formValidatorName = request.getParameter(ValidatorConstants.FORM_VALIDATOR_NAME);

        processLocale(request); // Verify if Locale have been setted in session with "LOCALE_KEY"
                                // if not, take the request.getLocale() as default and put it in session

        // Verify if Locale have been setted in session with "LOCALE_KEY"
        // if not, take the request.getLocale() as default and put it in session
        if ((contentType != null) && contentType.startsWith("multipart"))
        {
            try
            {
                logCat.debug("before new multipartRequest");

                MultipartRequest multipartRequest = new MultipartRequest(request, maxUploadSize);
                logCat.debug("after new multipartRequest");
                request.setAttribute("multipartRequest", multipartRequest);
            }
            catch (IOException ioe)
            {
                logCat.debug("Check if uploaded file(s) exceeded allowed size.");
                sendErrorMessage("Check if uploaded file(s) exceeded allowed size.", request, response);

                return;
            }
        }

        // Bradley's multiple connection support [fossato <fossato@pow2.com> 2002/11/04]:
        // moved the connection "initialization" into the try - catch block;
        //
        // previous code was:
        // Connection con = config.getDbConnection().getConnection();
        // logCat.debug("Created new connection - " + con);
        Connection con = null;

        try
        {
            Vector errors = new Vector();
            request.setAttribute("errors", errors);

            EventEngine engine = new EventEngine(request, config);
            WebEvent e = engine.generatePrimaryEvent();

            // ---- Bradley's multiple connection support [fossato <fossato@pow2.com> 2002/11/04] ------------------------
            String dbConnectionName = request.getParameter("invname_" + e.getTableId());
            DbConnection aDbConnection = config.getDbConnection(dbConnectionName);

            if (aDbConnection == null)
            {
                throw new IllegalArgumentException("No dbconnection configured with name '" + dbConnectionName + "'.");
            }

            con = aDbConnection.getConnection();

            if (dbConnectionName == null)
            {
                dbConnectionName = "default";
            }

            logCat.debug("Adding Connection '" + dbConnectionName + "' to connection cache.");
            connections.put(dbConnectionName, con);
            logCat.debug("Created new connection - " + con);

            // ---- Bradley's multiple connection support end -------------------------------------------------------------
            // primary event can be any kind of event (database, navigation...)
            if (e instanceof DatabaseEvent)
            {
                try
                {
                    // if hidden formValidatorName exist and it's an Update or Insert event,
                    // doValidation with Commons-Validator
                    if ((formValidatorName != null) && (e instanceof UpdateEvent || e instanceof InsertEvent))
                    {
                        doValidation(formValidatorName, e, request);
                    }

                    ((DatabaseEvent) e).processEvent(con);
                }
                catch (SQLException sqle)
                {
                    sqle.printStackTrace();
                    errors.addElement(sqle);
                    cleanUpConnectionAfterException(con);
                }
                catch (MultipleValidationException mve)
                {
                    java.util.Vector v = null;

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
            }
            else
            {
                // currently, we support db events ONLY
                // but in future there may be events with processEvent() method which do not need a jdbc con!
                // (you may think: "what about navigation events?" - well they are created by the
                // controller but they get executed in the referncing "DbFormTag" at the jsp -- that's why we
                // do not any further operations on them right here...we just put them into the request)
            }

            // secondary Events are always database events
            // (in fact, they all are SQL UPDATEs)
            if (engine.getInvolvedTables() != null)
            { // may be null if empty form!

                Enumeration tableEnum = engine.getInvolvedTables().elements();

                while (tableEnum.hasMoreElements())
                {
                    Table t = (Table) tableEnum.nextElement();

                    Enumeration eventEnum = engine.generateSecundaryEvents(e);

                    while (eventEnum.hasMoreElements())
                    {
                        DatabaseEvent dbE = (DatabaseEvent) eventEnum.nextElement();


                        // ---- Bradley's multiple connection support [fossato <fossato@pow2.com> 2002/11/04] ------------------------
                        dbConnectionName = request.getParameter("invname_" + dbE.getTableId());
                        aDbConnection = config.getDbConnection(dbConnectionName);

                        if (aDbConnection == null)
                        {
                            throw new IllegalArgumentException("No dbconnection configured with name '" + dbConnectionName + "'.");
                        }

                        if (dbConnectionName == null)
                        {
                            dbConnectionName = "default";
                        }

                        if (connections.get(dbConnectionName) == null)
                        {
                            con = aDbConnection.getConnection();
                            connections.put(dbConnectionName, con);
                        }
                        else
                        {
                            con = (Connection) connections.get(dbConnectionName);
                        }

                        // ---- Bradley's multiple connection support end ---------------------------------- ------------------------
                        try
                        {
                            // if hidden formValidatorName exist and it's an Update or Insert event, 
                            // doValidation with Commons-Validator
                            if ((formValidatorName != null) && (dbE instanceof UpdateEvent || dbE instanceof InsertEvent))
                            {
                                doValidation(formValidatorName, dbE, request);
                            }

                            dbE.processEvent(con);
                        }
                        catch (SQLException sqle2)
                        {
                            errors.addElement(sqle2);
                            cleanUpConnectionAfterException(con);
                        }
                        catch (MultipleValidationException mve)
                        {
                            java.util.Vector v = null;

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
                    }
                }
            }


            // send as info to dbForms (=> Taglib)
            //if(e instanceof NavigationEvent) {
            request.setAttribute("webEvent", e);

            //}
            // PG  - if form contained errors, use followupOnError (if available!)
            String fue = e.getFollowUpOnError();

            if ((errors.size() != 0) && (fue != null) && (fue.trim().length() > 0))
            {
                request.getRequestDispatcher(fue).forward(request, response);
            }
            else
            {
                request.getRequestDispatcher(e.getFollowUp()).forward(request, response);
            }
        }
        finally
        {
            // The connection should not be null - If it is, then you might have an infrastructure problem!
            // Be sure to look into this!  Hint: check out your pool manager's performance! 
            // ---- Bradley's multiple connection support [fossato <fossato@pow2.com> 2002/11/04] start -------------------
            //
            // must close all the connections stored into the the connections HashTable;
            Enumeration cons = connections.keys();

            while (cons.hasMoreElements())
            {
                String dbConnectionName = (String) cons.nextElement();
                con = (Connection) connections.get(dbConnectionName);

                if (con != null)
                {
                    try
                    {
                        logCat.debug("About to close connection - " + con);
                        con.close();
                        logCat.debug("Connection closed");
                    }
                    catch (SQLException sqle3)
                    {
                        sqle3.printStackTrace();
                    }
                }
            }

            // ---- Bradley's multiple connection support end --------------------------------------------------------------
        }
    }


    private void sendErrorMessage(String message, HttpServletRequest request, HttpServletResponse response)
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
            logCat.error("!!!senderror message crashed!!!" + ioe);
        }
    }


    /**
     Grunikiewicz.philip@hydro.qc.ca
     2001-10-29
     In our development, we sometimes set the connection object to autoCommit = false in the interceptor (Pre... methods).
     This allows us to have dbForms do part of the required transaction (other parts are done via jdbc calls).
     If the database throws an exception, then we need to make sure that the connection is reinitialized (rollbacked) before it
     is sent back into the connection pool.
     */
    public void cleanUpConnectionAfterException(Connection con)
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
            e.printStackTrace(System.out);
        }
    }


    /**
     *  DO the validation of <FORM> with Commons-Validator.
     *
     * @param formValidatorName  The form name to retreive in validation.xml
     * @param WebEvent The data (Hashtable) to perform validation
     * @param request The servlet request we are processing
     *
     * @exception MultipleValidationException The Vector of errors throwed with this exception
     *
     */
    private void doValidation(String formValidatorName, WebEvent e, HttpServletRequest request) throws MultipleValidationException
    {
        Hashtable fieldValues = null;

        if (e instanceof UpdateEvent)
        {
            UpdateEvent ue = (UpdateEvent) e;
            fieldValues = ue.getAssociativeFieldValues(ue.getFieldValues());
        }
        else
        {
            InsertEvent ie = (InsertEvent) e;
            fieldValues = ie.getAssociativeFieldValues(ie.getFieldValues());
        }

        // If no data to validate, return
        if (fieldValues.size() == 0)
        {
            return;
        }

        // Retreive ValidatorResources from Application context (loaded with ConfigServlet)
        ValidatorResources vr = (ValidatorResources) getServletContext().getAttribute(ValidatorConstants.VALIDATOR);

        if (vr == null)
        {
            return;
        }

        Validator validator = new Validator(vr, formValidatorName.trim());
        Vector errors = new Vector();
        DbFormsErrors dbFormErrors = (DbFormsErrors) getServletContext().getAttribute(DbFormsErrors.ERRORS);
        Locale locale = MessageResources.getLocale(request);


        // Add these resources to perform validation
        validator.addResource(Validator.BEAN_KEY, fieldValues); // The values
        validator.addResource("java.util.Vector", errors);
        validator.addResource(Validator.LOCALE_KEY, locale); // Vector of errors to populate
        validator.addResource("org.dbforms.DbFormsErrors", dbFormErrors); // Applicatiob context

        ValidatorResults hResults = null;

        try
        {
            hResults = validator.validate();
        }
        catch (Exception ex)
        {
            logCat.error("\n!!! doValidation error for : " + formValidatorName + "  !!!\n" + ex);
        }

        // If error(s) found, throw Exception		  
        if (errors.size() > 0)
        {
            throw new MultipleValidationException(errors);
        }
    }


    private void processLocale(HttpServletRequest request)
    {
        HttpSession session = request.getSession();

        if (session.getAttribute(MessageResources.LOCALE_KEY) == null)
        {
            session.setAttribute(MessageResources.LOCALE_KEY, request.getLocale());
        }
    }
}