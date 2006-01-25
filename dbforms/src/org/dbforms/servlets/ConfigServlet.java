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
 * Foundation, Inc., 59 Temaple Place, Suite 330, Boston, MA  02111-1307 USA
 */

package org.dbforms.servlets;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.commons.validator.ValidatorResources;
import org.apache.commons.validator.ValidatorResourcesInitializer;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;

import org.dbforms.config.ConfigLoader;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.DbFormsConfigRegistry;
import org.dbforms.config.DbFormsErrors;

import org.dbforms.util.MessageResources;
import org.dbforms.util.Util;

import org.dbforms.validation.ValidatorConstants;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * This Servlet runs at application startup and reads the XML configuration in
 * dbforms-config.xml, populates a DbFormsConfig - Object and stores it in
 * application context.
 *
 * @author Joe Peer
 */
public class ConfigServlet extends HttpServlet {
   /** DOCUMENT ME! */
   private static Log logCat;

   // ----------------------------------------------------- Instance Variables
   private transient ConfigLoader loader = new ConfigLoader();

   // ---------------------------------------------------- HttpServlet Methods

   /**
    * Gracefully shut down this controller servlet, releasing any resources
    * that were allocated at initialization.
    */
   public void destroy() {
      log("finalizing");
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
   public void doGet(HttpServletRequest  request,
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
   public void doPost(HttpServletRequest  request,
                      HttpServletResponse response)
               throws IOException, ServletException {
      process(request, response);
   }


   /**
    * Initialize this servlet.
    *
    * @exception ServletException if we cannot configure ourselves correctly
    */
   public void init() throws ServletException {
      try {
         initLogging();

         loader.setFieldClassName(getServletConfig().getInitParameter("className.Field"));
         loader.setTableClassName(getServletConfig().getInitParameter("className.Table"));
         loader.setQueryClassName(getServletConfig().getInitParameter("className.Query"));
         loader.setForeignKeyClassName(getServletConfig().getInitParameter("className.ForeignKey"));
         loader.setReferenceClassName(getServletConfig().getInitParameter("className.Reference"));

         initXMLConfig();
         initXMLErrors();
         initXMLValidator();
         initApplicationResources();
         initLocaleKey();
      } catch (IOException ioe) {
         ioe.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }


   /**
    * Initialize Logging for this web application a url/path to a log4j
    * properties file should be defined by the servlet init parameter
    * "log4j.configuration"
    */
   public void initLogging() {
      String  configurationStr = this.getServletConfig()
                                     .getInitParameter("log4j.configuration");
      boolean usingURL = true;

      if (!Util.isNull(configurationStr)) {
         try {
            //Works fine with Tomcat 4.1.27 and Weblogic
            InputStream fis = getServletContext()
                                 .getResourceAsStream(configurationStr);

            if (fis != null) {
               try {
                  Properties log4jProperties = new Properties();
                  log4jProperties.load(fis);
                  LogManager.resetConfiguration();
                  PropertyConfigurator.configure(log4jProperties);
               } finally {
                  fis.close();
               }
            } else {
               System.err.println("ConfigServlet::initLogging - log4j.configuration not found!");
            }
         } catch (IOException e) {
            System.err.println("ConfigServlet::initLogging - log4j.properties not found!");

            PropertyConfigurator.configure(configurationStr);
            usingURL = false;
         }

         logCat = LogFactory.getLog(ConfigServlet.class.getName());
         // logging category for this class
         logCat.info("### LOGGING INITALIZED, USING URL: " + usingURL + " ###"
                     + configurationStr);
      }  else {
          logCat = LogFactory.getLog(ConfigServlet.class.getName());
          // logging category for this class
          logCat.info("### LOGGING INITALIZED, USING DEFAULT CONFIGURATION.");
       }
   }


   /**
    * Initialize the SubClass information use by the ResourceBundle for this
    * application. ATTENTION: Here the "application" it's use as Class name,
    * not like path/file coordonnates. (see java.util.ResourceBundle)
    *
    * @exception IOException if an input/output error is encountered
    * @exception ServletException if we cannot initialize these resources
    */
   protected void initApplicationResources() {
      logCat.info("initialize Application Resources.");

      String value = getServletConfig()
                        .getInitParameter(ValidatorConstants.RESOURCE_BUNDLE);

      if (value == null) {
         logCat.warn(" Application Resources file not setted in Web.xml, ApplicationResources handler disabled!");

         return;
      }

      MessageResources.setSubClass(value);

      logCat.info(" DbForms Application Resources : SubClass initialized ");
   }


   /**
    * Initialize the Locale key for Session scope. Usefull for sharing the same
    * Locale across different framework. Ex: By setting "localeKey" to
    * "org.apache.struts.action.LOCALE" you can share the same Locale in the
    * session scope with Struts.
    */
   protected void initLocaleKey() {
      logCat.info("initialize Locale Key for session attribute.");

      String value = getServletConfig()
                        .getInitParameter("localeKey");

      if (value == null) {
         logCat.warn(" Locale Key not setted, use \""
                     + MessageResources.LOCALE_KEY
                     + "\" as key to access the Locale in session scope.");
      } else {
         MessageResources.LOCALE_KEY = value.trim();
         logCat.info(" Locale Key setted with \"" + MessageResources.LOCALE_KEY
                     + "\" as key to access the Locale in session scope.");
      }
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
      loader.setConfig(value);
      String[] s = StringUtils.split(loader.getConfig(), ",");
      for (int i = 0; i < s.length; i++)
         initXMLConfigFile(s[i]);
   }


   /**
    * DOCUMENT ME!
    *
    * @param config DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    * @throws ServletException DOCUMENT ME!
    */
   protected void initXMLConfigFile(String config)
                             throws IOException, ServletException {

      InputStream input = getServletContext()
                             .getResourceAsStream(config);

      if (input == null) {
         throw new UnavailableException("configMissing");
      }

      try {
         // register the config object into the DbFormsConfigRegistry
         // as the default config (fossato, 2002.12.02)
         DbFormsConfigRegistry registry      = DbFormsConfigRegistry.instance();
         DbFormsConfig         dbFormsConfig = null;

         try {
            dbFormsConfig = registry.lookup();
         } catch (Exception e) {
            dbFormsConfig = null;
         }

         if (dbFormsConfig == null) {
            dbFormsConfig = new DbFormsConfig();

            // store a reference to ServletConfig (for interoperation with other parts of the Web-App!)
            dbFormsConfig.setServletConfig(getServletConfig());

            // ---------------------------------------------------------------
            registry.setServletContext(getServletContext());
            registry.register(dbFormsConfig);
         }

         // ---------------------------------------------------------------
         // Parse the input stream to configure our mappings
         try {
            loader.loadConfig(input, dbFormsConfig);
         } catch (SAXException e) {
            logCat.error("::initXMLConfig - SaxException", e);
            throw new ServletException(e.toString());
         }
      } finally {
         input.close();
      }
   }


   // --------------------------------------------------------- Public Methods
   // ------------------------------------------------------ Protected Methods

   /**
    * Initialize the mapping information for this application.
    *
    * @exception IOException if an input/output error is encountered
    * @exception ServletException if we cannot initialize these resources
    */
   protected void initXMLErrors() throws IOException, ServletException {
      logCat.info("initialize XML Errors.");

      // Look to see if developer has specified his/her own errors filename & location
      String value = getServletConfig()
                        .getInitParameter(DbFormsErrors.ERRORS);

      loader.setErrors(value);

      // Acquire an input stream to our errors resource
      InputStream input = getServletContext()
                             .getResourceAsStream(loader.getErrors());

      if (input == null) {
         // File not available, log warning
         logCat.warn("XML Errors file not found, XML error handler disabled!");

         return;
      }

      try {
         // Build a digester to process our errors resource
         DbFormsErrors dbFormsErrors = new DbFormsErrors();

         // store a reference to ServletErrors (for interoperation with other parts of the Web-App!)
         dbFormsErrors.setServletConfig(getServletConfig());

         // store this errors object in the servlet context ("application")
         getServletContext()
            .setAttribute(DbFormsErrors.ERRORS, dbFormsErrors);

         try {
            loader.loadErrors(input, dbFormsErrors);
         } catch (SAXException e) {
            throw new ServletException(e.toString());
         }

         logCat.info("DbForms Error: " + dbFormsErrors);
      } finally {
         input.close();
      }
   }


   /**
    * Initialize the ValidatorResources information for this application.
    *
    * @exception IOException if an input/output error is encountered
    * @exception ServletException if we cannot initialize these resources
    */
   protected void initXMLValidator() throws ServletException {
      // Map the commons-logging used by commons-validator to Log4J logger
/* 200050906-HKK Not neccesary to configure logging here. 
 *               Use the global logging configuration in init logging instead!!!   
	   try {
         System.setProperty("org.apache.commons.logging.Log",
                            "org.apache.commons.logging.impl.Log4JCategoryLog");
      } catch (java.security.AccessControlException e) {
         logCat.warn("Unable map commons-logging to Log4j, due to SecurityManager",
                     e);
      }
*/

      ValidatorResources resources = new ValidatorResources();
      logCat.info("initialize XML Validator.");

      String value;
      value = getServletConfig()
                 .getInitParameter(ValidatorConstants.VALIDATOR_RULES);

      loader.setValidatorRules(value);

      initXMLValidatorRules(resources, loader.getValidatorRules());

      value = getServletConfig()
                 .getInitParameter(ValidatorConstants.VALIDATION);

      loader.setValidation(value);

      String[] s = StringUtils.split(loader.getValidation(), ",");

      for (int i = 0; i < s.length; i++)
         initXMLValidatorValidation(resources, s[i]);

      // store this errors object in the servlet context ("application")
      getServletContext()
         .setAttribute(ValidatorConstants.VALIDATOR, resources);

      logCat.info(" DbForms Validator : Loaded ");
   }


   /**
    * DOCUMENT ME!
    *
    * @param resources DOCUMENT ME!
    * @param validator_rules DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    * @throws ServletException DOCUMENT ME!
    */
   protected void initXMLValidatorRules(ValidatorResources resources,
                                        String             validator_rules)
                                 throws ServletException {
      // Acquire an input stream validator_rules
      InputStream inputValidatorRules = getServletContext()
                                           .getResourceAsStream(validator_rules);

      if (inputValidatorRules == null) {
         // File not available, log warning
         logCat.warn("XML Validator rule file not found, XML Validator handler disabled!");

         return;
      }

      //
      // Initialize ValidatorResources
      //
      try {
         ValidatorResourcesInitializer.initialize(resources, inputValidatorRules);
      } catch (IOException e) {
         logCat.warn("XML Validator Exception ValidatorResourcesInitializer.initialize  : "
                     + e.getMessage());
         throw new ServletException(e.toString());
      } finally {
         try {
            inputValidatorRules.close();
         } catch (Exception e) {
            logCat.error("initXMLValidatorRules", e);
         }
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param resources DOCUMENT ME!
    * @param validation DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    * @throws ServletException DOCUMENT ME!
    */
   protected void initXMLValidatorValidation(ValidatorResources resources,
                                             String             validation)
                                      throws ServletException {
      //
      // LOAD Validation & Validator_rules files
      //
      // Acquire an input stream validation
      InputStream inputValidation = getServletContext()
                                       .getResourceAsStream(validation);

      if (inputValidation == null) {
         // File not available, log warning
         logCat.warn("XML Validation file not found, XML Validator handler disabled!");

         return;
      }

      try {
         ValidatorResourcesInitializer.initialize(resources, inputValidation);
      } catch (IOException e) {
         logCat.warn("XML Validator Exception ValidatorResourcesInitializer.initialize  : "
                     + e.getMessage());
         throw new ServletException(e.toString());
      } finally {
         try {
            inputValidation.close();
         } catch (Exception e) {
            logCat.error("initXMLValidatorValidation", e);
         }
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
   protected void process(HttpServletRequest  request,
                          HttpServletResponse response)
                   throws IOException {
      PrintWriter out = response.getWriter();

      try {
         DbFormsConfig dbFormsConfig = DbFormsConfigRegistry.instance()
                                                            .lookup();
         out.println(dbFormsConfig.toString());
      } catch (Exception e) {
         throw new IOException(e.getMessage());
      }
   }
}
