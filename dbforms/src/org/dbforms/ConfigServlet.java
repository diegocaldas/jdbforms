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

package com.itp.dbforms;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.digester.Digester;  // thanks to everybody involved for this great tool!
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
     * The context-relative path to our configuration resource.
     */
    protected String config = "/WEB-INF/dbforms-config.xml";


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
			initXMLConfig();
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
    public void initLogging() {
		String configurationStr = this.getServletConfig().getInitParameter("log4j.configuration");
		boolean usingURL = true;

		if(configurationStr!=null) {
			try {
				URL configURL = new URL(configurationStr);
				PropertyConfigurator.configure(configURL);
			} catch(MalformedURLException mue) {
			    PropertyConfigurator.configure(configurationStr);
			    usingURL = false;
			}

			logCat = Category.getInstance(ConfigServlet.class.getName()); // logging category for this class
			logCat.info("### LOGGING INITALIZED, USING URL: "+usingURL+" ###");
		} else {

		    BasicConfigurator.configure();
		    logCat = Category.getInstance(ConfigServlet.class.getName()); // logging category for this class
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


		// parse "Table" - object + add it to parent

        digester.addObjectCreate("dbforms-config/table","com.itp.dbforms.Table");
        digester.addSetProperties("dbforms-config/table");
        digester.addSetNext("dbforms-config/table", "addTable", "com.itp.dbforms.Table");


		// parse "Field" - object + add it to parent (which is "Table")

        digester.addObjectCreate("dbforms-config/table/field","com.itp.dbforms.Field");
        digester.addSetProperties("dbforms-config/table/field");
        digester.addSetNext("dbforms-config/table/field", "addField", "com.itp.dbforms.Field");

		// parse "GrantedPrivileges" - object + add it to parent (which is "Table")

        digester.addObjectCreate("dbforms-config/table/granted-privileges","com.itp.dbforms.GrantedPrivileges");
        digester.addSetProperties("dbforms-config/table/granted-privileges");
        digester.addSetNext("dbforms-config/table/granted-privileges", "setGrantedPrivileges", "com.itp.dbforms.GrantedPrivileges");

		// parse "Condition" - object + add it to parent (which is "Table")

        digester.addObjectCreate("dbforms-config/table/interceptor","com.itp.dbforms.Interceptor");
        digester.addSetProperties("dbforms-config/table/interceptor");
        digester.addSetNext("dbforms-config/table/interceptor", "addInterceptor", "com.itp.dbforms.Interceptor");


		// parse "DbConnecion" - object

        digester.addObjectCreate("dbforms-config/dbconnection","com.itp.dbforms.util.DbConnection");
        digester.addSetProperties("dbforms-config/dbconnection");
        digester.addSetNext("dbforms-config/dbconnection", "setDbConnection", "com.itp.dbforms.util.DbConnection");



		return digester;

    }





    /**
     * Initialize the mapping information for this application.
     *
     * @exception IOException if an input/output error is encountered
     * @exception ServletException if we cannot initialize these resources
     */
    protected void initXMLConfig() throws IOException, ServletException {


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
		digester = initDigester(1,dbFormsConfig);

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
