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
import java.net.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.digester.Digester;
//import org.apache.struts.taglib.form.Constants;
import org.apache.struts.util.BeanUtils;
import org.apache.struts.util.GenericDataSource;
import org.apache.struts.util.MessageResources;
import org.xml.sax.SAXException;

import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.BasicConfigurator;



/****
 * <p>
 * This Servlet runs at application startup and reads the XML configuration in
 * dbforms-config.xml, populates a DbFormsConfig - Object and stores it in application
 * context.
 * </p>
 *
 *
 * @author Joe Peer <joepeer@excite.com>
 */

public class ConfigServlet extends HttpServlet {

 Category logCat;

	// ----------------------------------------------------- Instance Variables



	/**
	 * The context-relative path to our configuration and errors resources.
	 */
	protected String config = "/WEB-INF/dbforms-config.xml";
	protected String errors = "/WEB-INF/dbforms-errors.xml";


	// ---------------------------------------------------- HttpServlet Methods


	/**
	 * Gracefully shut down this controller servlet, releasing any resources
	 * that were allocated at initialization.
	 */
	public void destroy() {


	    log("finalizing");

	}


	/**
	 * Initialize this servlet.
	 *
	 * @exception ServletException if we cannot configure ourselves correctly
	 */
	public void init() throws ServletException {
		
		try {
			initLogging();
			
			// Setup digester debug level
			int digesterDebugLevel = 1;
			
			String digesterDebugLevelInput = this.getServletConfig().getInitParameter("digesterDebugLevel");
			if (digesterDebugLevelInput != null)
			{
				// Transform input into an integer
				digesterDebugLevel = Integer.parseInt(digesterDebugLevelInput);
			}
				
			initXMLConfig(digesterDebugLevel);
			initXMLErrors(digesterDebugLevel);
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Initialize Logging for this web application
	 * a url/path to a log4j properties file should be defined by the servlet init parameter "log4j.configuration"
	 */
	
	public void initLogging()
    {
        String  configurationStr = this.getServletConfig().getInitParameter("log4j.configuration");
        boolean usingURL         = true;

        if (configurationStr != null)
            {
                try
                    {
                        // Assuming that a webapp relative path starts with "/WEB-INF/"...
                        // is the log4j.configuration parameter value a relative URL ?
                        // (example: "/WEB-INF/log4j.configuration")
                        // [Fossato <fossato@pow2.com>, 20011123]
                        if (configurationStr.startsWith("/WEB-INF/"))
                            {
                                // get the web application context prefix;
                                String prefix = this.getServletContext().getRealPath("/");
                                configurationStr = (prefix + configurationStr);

                                // you can configure log4J using also a simple
                                // file path string... example:
                                //PropertyConfigurator.configure(configurationStr);
                            }

                        System.out.println("ConfigServlet::initLogging - log4j configuration str = ["
                                           + configurationStr + "]");
                                                        
                        URL configURL = new URL(configurationStr);
                        PropertyConfigurator.configure(configURL);                      
                    }
                catch (MalformedURLException mue)
                    {
                        PropertyConfigurator.configure(configurationStr);
                        usingURL = false;
                    }

                logCat = Category.getInstance(ConfigServlet.class.getName());
                // logging category for this class
                logCat.info("### LOGGING INITALIZED, USING URL: " + usingURL + " ###");
            }
        else
            {

                BasicConfigurator.configure();
                logCat = Category.getInstance(ConfigServlet.class.getName());
                // logging category for this class
                logCat.info("### LOGGING INITALIZED, USING BASIC CONFIGURATION.");
                logCat.info("### You can use init-parameter \"log4j.configuration\" in web.xml for defining individual properties, if you want. Check DbForms manual!");
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
	public void doGet(HttpServletRequest request,
		      HttpServletResponse response)
	throws IOException, ServletException {

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
	public void doPost(HttpServletRequest request,
		       HttpServletResponse response)
	throws IOException, ServletException {

	process(request, response);

	}


	// --------------------------------------------------------- Public Methods







	// ------------------------------------------------------ Protected Methods




	/**
	 * Construct and return a digester that uses the new configuration
	 * file format.
	 */
	protected Digester initDigester(int detail, DbFormsConfig dbFormsConfig) {
		

	// Initialize a new Digester instance
	Digester digester = new Digester();
	digester.push(dbFormsConfig);
	digester.setDebug(detail);
	digester.setValidating(false);

	// Configure the processing rules


		// parse "DateFormatter" 

		digester.addCallMethod("dbforms-config/date-format", "setDateFormatter", 0);

		// parse "Table" - object + add it to parent

		digester.addObjectCreate("dbforms-config/table","org.dbforms.Table");
		digester.addSetProperties("dbforms-config/table");
		digester.addSetNext("dbforms-config/table", "addTable", "org.dbforms.Table");


		// parse "Field" - object + add it to parent (which is "Table")

		digester.addObjectCreate("dbforms-config/table/field","org.dbforms.Field");
		digester.addSetProperties("dbforms-config/table/field");
		digester.addSetNext("dbforms-config/table/field", "addField", "org.dbforms.Field");

		// parse "GrantedPrivileges" - object + add it to parent (which is "Table")

		digester.addObjectCreate("dbforms-config/table/granted-privileges","org.dbforms.GrantedPrivileges");
		digester.addSetProperties("dbforms-config/table/granted-privileges");
		digester.addSetNext("dbforms-config/table/granted-privileges", "setGrantedPrivileges", "org.dbforms.GrantedPrivileges");

		// parse "Condition" - object + add it to parent (which is "Table")

		digester.addObjectCreate("dbforms-config/table/interceptor","org.dbforms.Interceptor");
		digester.addSetProperties("dbforms-config/table/interceptor");
		digester.addSetNext("dbforms-config/table/interceptor", "addInterceptor", "org.dbforms.Interceptor");


		// parse "DbConnecion" - object

		digester.addObjectCreate("dbforms-config/dbconnection","org.dbforms.util.DbConnection");
		digester.addSetProperties("dbforms-config/dbconnection");
		digester.addSetNext("dbforms-config/dbconnection", "setDbConnection", "org.dbforms.util.DbConnection");



		return digester;

	}


	/**
	 * Construct and return a digester that uses the new errors
	 * file format.
	 */
	protected Digester initErrorsDigester(int detail, DbFormsErrors dbFormsErrors) {

	// Initialize a new Digester instance
    logCat.info("initialize Errors Digester.");

	Digester digester = new Digester();
	digester.push(dbFormsErrors);
	digester.setDebug(detail);
	digester.setValidating(false);

	// Configure the processing rules


		// parse "Error" - object 

		digester.addObjectCreate("dbforms-errors/error","org.dbforms.Error");
		digester.addSetProperties("dbforms-errors/error");
		digester.addSetNext("dbforms-errors/error", "addError", "org.dbforms.Error");
		
		
		// parse "Message" - object + add it to parent (which is "Error")

		digester.addObjectCreate("dbforms-errors/error/message","org.dbforms.Message");
		digester.addSetNext("dbforms-errors/error/message", "addMessage", "org.dbforms.Message");
		digester.addSetProperties("dbforms-errors/error/message");
		digester.addCallMethod("dbforms-errors/error/message", "setMessage", 0);
		
		return digester;
	}


	/**
	 * Initialize the mapping information for this application.
	 *
	 * @exception IOException if an input/output error is encountered
	 * @exception ServletException if we cannot initialize these resources
	 */
	protected void initXMLErrors(int digesterDebugLevel) throws IOException, ServletException {

	    logCat.info("initialize XML Errors.");


		// Look to see if developer has specified his/her own errors filename & location
		String value = getServletConfig().getInitParameter(DbFormsErrors.ERRORS);
		if (value != null)
		    errors = value;

		// Acquire an input stream to our errors resource
		InputStream input = getServletContext().getResourceAsStream(errors);
		if (input == null)
		{
			// File not available, log warning
		    logCat.warn("XML Errors file not found, XML error handler disabled!");
		    return;
		}

		// Build a digester to process our errors resource

		DbFormsErrors dbFormsErrors = new DbFormsErrors();
		Digester digester = null;
		digester = initErrorsDigester(digesterDebugLevel,dbFormsErrors);

		 // store a reference to ServletErrors (for interoperation with other parts of the Web-App!)
		dbFormsErrors.setServletConfig(getServletConfig());

		// store this errors object in the servlet context ("application")
		getServletContext().setAttribute(DbFormsErrors.ERRORS, dbFormsErrors);

		// Parse the input stream to configure our mappings
		try {
		    digester.parse(input);
		    input.close();
		} catch (SAXException e) {
		    throw new ServletException(e.toString());
		}

		logCat.info("DbForms Error: " + dbFormsErrors);
	}


	/**
	 * Initialize the mapping information for this application.
	 *
	 * @exception IOException if an input/output error is encountered
	 * @exception ServletException if we cannot initialize these resources
	 */
	protected void initXMLConfig(int digesterDebugLevel) throws IOException, ServletException {


		// Initialize the context-relative path to our configuration resources
		String value = getServletConfig().getInitParameter(DbFormsConfig.CONFIG);
		if (value != null)
		    config = value;
		    

		// Acquire an input stream to our configuration resource
		InputStream input = getServletContext().getResourceAsStream(config);
		if (input == null)
		    throw new UnavailableException("configMissing");

		// Build a digester to process our configuration resource

		DbFormsConfig dbFormsConfig = new DbFormsConfig();
		Digester digester = null;
		digester = initDigester(digesterDebugLevel,dbFormsConfig);

		 // store a reference to ServletConfig (for interoperation with other parts of the Web-App!)
		dbFormsConfig.setServletConfig(getServletConfig());

		// store this config object in servlet context ("application")
		getServletContext().setAttribute(DbFormsConfig.CONFIG, dbFormsConfig);

		// Parse the input stream to configure our mappings
		try {
		    digester.parse(input);
		    input.close();
		} catch (SAXException e) {
		    throw new ServletException(e.toString());
		}



	}




	/**
	 * Process an HTTP request.
	 *
	 * @param request The servlet request we are processing
	 * @param response The servlet response we are creating
	 *
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet exception occurs
	 */
	protected void process(HttpServletRequest request,
			   HttpServletResponse response)
	throws IOException, ServletException {

		PrintWriter out = response.getWriter();
		DbFormsConfig dbFormsConfig = (DbFormsConfig) getServletContext().getAttribute("xmlconfig");
		out.println(dbFormsConfig.traverse());

	}

}