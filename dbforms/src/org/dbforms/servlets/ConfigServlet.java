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
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.BasicConfigurator;
import org.apache.commons.validator.ValidatorResources;
import org.apache.commons.validator.ValidatorResourcesInitializer;
import org.apache.commons.lang.StringUtils;
import org.dbforms.validation.ValidatorConstants;
import org.dbforms.util.MessageResources;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.DbFormsConfigRegistry;
import org.dbforms.config.DbFormsErrors;
import org.dbforms.event.DatabaseEventFactoryImpl;
import org.dbforms.util.SingletonClassFactoryCreate;
import org.dbforms.event.NavEventFactoryImpl;



/**
 * This Servlet runs at application startup and reads the XML configuration in
 * dbforms-config.xml, populates a DbFormsConfig - Object and stores it in
 * application context.
 *
 * @author Joe Peer
 */
public class ConfigServlet extends HttpServlet
{
   /** DOCUMENT ME! */
   private Logger logCat;
   private Logger digesterLogger;

   // ----------------------------------------------------- Instance Variables

   /** The context-relative path to our configuration and errors resources. */
   protected String config = "/WEB-INF/dbforms-config.xml";

   /** DOCUMENT ME! */
   protected String errors = "/WEB-INF/dbforms-errors.xml";

   /** DOCUMENT ME! */
   protected String validation = "/WEB-INF/validation.xml";

   /** DOCUMENT ME! */
   protected String validator_rules = "/WEB-INF/validator-rules.xml";

   // ---------------------------------------------------- HttpServlet Methods

   /**
    * Gracefully shut down this controller servlet, releasing any resources
    * that were allocated at initialization.
    */
   public void destroy()
   {
      log("finalizing");
   }


   /**
    * Initialize this servlet.
    *
    * @exception ServletException if we cannot configure ourselves correctly
    */
   public void init() throws ServletException
   {
      try
      {
         initLogging();

         initXMLConfig();
         initXMLErrors();
         initXMLValidator();
         initApplicationResources();
         initLocaleKey();
      }
      catch (IOException ioe)
      {
         ioe.printStackTrace();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }


   /**
    * Initialize Logging for this web application a url/path to a log4j
    * properties file should be defined by the servlet init parameter
    * "log4j.configuration"
    */
   public void initLogging()
   {
      String  configurationStr = this.getServletConfig()
                                     .getInitParameter("log4j.configuration");
      boolean usingURL = true;

      if (configurationStr != null)
      {
         try
         {
            //Works fine with Tomcat 4.1.27 and Weblogic
            InputStream fis = getServletContext()
                                 .getResourceAsStream(configurationStr);

            if (fis != null)
            {
               Properties log4jProperties = new Properties();
               log4jProperties.load(fis);
               PropertyConfigurator.configure(log4jProperties);
            }
            else
            {
               System.err.println(
                        "ConfigServlet::initLogging - log4j.configuration not found!");
            }
         }
         catch (IOException e)
         {
            System.err.println(
                     "ConfigServlet::initLogging - log4j.properties not found!");

            PropertyConfigurator.configure(configurationStr);
            usingURL = false;
         }

         logCat = Logger.getLogger(ConfigServlet.class.getName());
         // logging category for this class
         logCat.info("### LOGGING INITALIZED, USING URL: " + usingURL + " ###" + configurationStr);
      }
      else
      {
         BasicConfigurator.configure();
         logCat = Logger.getLogger(ConfigServlet.class.getName());
         // logging category for this class
         logCat.info("### LOGGING INITALIZED, USING BASIC CONFIGURATION.");
         logCat.info(
                  "### You can use init-parameter \"log4j.configuration\" in web.xml for defining individual properties, if you want. Check DbForms manual!");
      }
	  digesterLogger = Logger.getLogger(Digester.class.getName());

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


   // --------------------------------------------------------- Public Methods
   // ------------------------------------------------------ Protected Methods

   /**
    * Construct and return a digester that uses the new configuration file
    * format.
    *
    * @param detail DOCUMENT ME!
    * @param dbFormsConfig DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   protected Digester initDigester(DbFormsConfig dbFormsConfig)
   {
      // Initialize a new Digester instance
      Digester digester = new Digester();
      digester.setLogger(
               new org.apache.commons.logging.impl.Log4JLogger(digesterLogger));
      digester.push(dbFormsConfig);
      digester.setNamespaceAware(true);
      digester.setValidating(false);


      // Configure the processing rules
      // parse "DateFormatter"
      digester.addCallMethod("dbforms-config/date-format", "setDateFormatter", 0);


      // parse "Table" - object + add it to parent
      digester.addObjectCreate("dbforms-config/table", 
                               "org.dbforms.config.Table");
      digester.addSetProperties("dbforms-config/table");
      digester.addSetNext("dbforms-config/table", "addTable", 
                          "org.dbforms.config.Table");


      // parse "Field" - object + add it to parent (which is "Table")
      digester.addObjectCreate("dbforms-config/table/field", 
                               "org.dbforms.config.Field");
      digester.addSetProperties("dbforms-config/table/field");
      digester.addSetNext("dbforms-config/table/field", "addField", 
                          "org.dbforms.config.Field");


      // parse "Foreign-Key" - object + add it to parent (which is "Table")
      digester.addObjectCreate("dbforms-config/table/foreign-key", 
                               "org.dbforms.config.ForeignKey");
      digester.addSetProperties("dbforms-config/table/foreign-key");
      digester.addSetNext("dbforms-config/table/foreign-key", "addForeignKey", 
                          "org.dbforms.config.ForeignKey");


      // parse "Reference" - object + add it to parent (which is "ForeignKey")
      digester.addObjectCreate("dbforms-config/table/foreign-key/reference", 
                               "org.dbforms.config.Reference");
      digester.addSetProperties("dbforms-config/table/foreign-key/reference");
      digester.addSetNext("dbforms-config/table/foreign-key/reference", 
                          "addReference", "org.dbforms.config.Reference");


      // parse "GrantedPrivileges" - object + add it to parent (which is "Table")
      digester.addObjectCreate("dbforms-config/table/granted-privileges", 
                               "org.dbforms.config.GrantedPrivileges");
      digester.addSetProperties("dbforms-config/table/granted-privileges");
      digester.addSetNext("dbforms-config/table/granted-privileges", 
                          "setGrantedPrivileges", 
                          "org.dbforms.config.GrantedPrivileges");


      // parse "Interceptor" - object + add it to parent (which is "Table")
      digester.addObjectCreate("dbforms-config/table/interceptor", 
                               "org.dbforms.config.Interceptor");
      digester.addSetProperties("dbforms-config/table/interceptor");
      digester.addSetNext("dbforms-config/table/interceptor", "addInterceptor", 
                          "org.dbforms.config.Interceptor");


      // register custom database or navigation events (parent is "Table");
      // 1) for every "events" element, instance a new TableEvents object;
      // 2) set the TableEvents reference into the Table object
      // 3) for every "event" element, instance a new EventInfo object and set its properties ("type" and "id")
      // 4) register the EventInfo object into the TableEvents via TableEvents.addEventInfo()
      // 5) for every event's property attribute, instance a new Property object
      //    and and set its properties ("name" and "value")
      // 6) register the Property object into the EventInfo object
      digester.addObjectCreate("dbforms-config/table/events", 
                               "org.dbforms.config.TableEvents");
      digester.addSetNext("dbforms-config/table/events", "setTableEvents", 
                          "org.dbforms.config.TableEvents");
      digester.addObjectCreate("dbforms-config/table/events/event", 
                               "org.dbforms.config.EventInfo");
      digester.addSetProperties("dbforms-config/table/events/event");
      digester.addSetNext("dbforms-config/table/events/event", "addEventInfo", 
                          "org.dbforms.config.EventInfo");
      digester.addObjectCreate("dbforms-config/table/events/event/property", 
                               "org.dbforms.config.DbConnectionProperty");
      digester.addSetProperties("dbforms-config/table/events/event/property");
      digester.addSetNext("dbforms-config/table/events/event/property", 
                          "addProperty", 
                          "org.dbforms.config.DbConnectionProperty");


      // parse "Query" - object + add it to parent
      digester.addObjectCreate("dbforms-config/query", 
                               "org.dbforms.config.Query");
      digester.addSetProperties("dbforms-config/query");
      digester.addSetNext("dbforms-config/query", "addTable", 
                          "org.dbforms.config.Table");


      // parse "Field" - object + add it to parent (which is "Query")
      digester.addObjectCreate("dbforms-config/query/field", 
                               "org.dbforms.config.Field");
      digester.addSetProperties("dbforms-config/query/field");
      digester.addSetNext("dbforms-config/query/field", "addField", 
                          "org.dbforms.config.Field");


      // parse "search" - object + add it to parent (which is "Query")
      digester.addObjectCreate("dbforms-config/query/search", 
                               "org.dbforms.config.Field");
      digester.addSetProperties("dbforms-config/query/search");
      digester.addSetNext("dbforms-config/query/search", "addSearchField", 
                          "org.dbforms.config.Field");


      // parse "Foreign-Key" - object + add it to parent (which is "Table")
      digester.addObjectCreate("dbforms-config/query/foreign-key", 
                               "org.dbforms.config.ForeignKey");
      digester.addSetProperties("dbforms-config/query/foreign-key");
      digester.addSetNext("dbforms-config/query/foreign-key", "addForeignKey", 
                          "org.dbforms.config.ForeignKey");


      // parse "Reference" - object + add it to parent (which is "ForeignKey")
      digester.addObjectCreate("dbforms-config/query/foreign-key/reference", 
                               "org.dbforms.config.Reference");
      digester.addSetProperties("dbforms-config/query/foreign-key/reference");
      digester.addSetNext("dbforms-config/query/foreign-key/reference", 
                          "addReference", "org.dbforms.config.Reference");


      // parse "GrantedPrivileges" - object + add it to parent (which is "Query")
      digester.addObjectCreate("dbforms-config/query/granted-privileges", 
                               "org.dbforms.config.GrantedPrivileges");
      digester.addSetProperties("dbforms-config/query/granted-privileges");
      digester.addSetNext("dbforms-config/query/granted-privileges", 
                          "setGrantedPrivileges", 
                          "org.dbforms.config.GrantedPrivileges");


      // parse "Condition" - object + add it to parent (which is "Query")
      digester.addObjectCreate("dbforms-config/query/interceptor", 
                               "org.dbforms.config.Interceptor");
      digester.addSetProperties("dbforms-config/query/interceptor");
      digester.addSetNext("dbforms-config/query/interceptor", "addInterceptor", 
                          "org.dbforms.config.Interceptor");


      // register custom database or navigation events (parent is "Query");
      // 1) for every "events" element, instance a new TableEvents object;
      // 2) set the TableEvents reference into the Table object
      // 3) for every "event" element, instance a new EventInfo object and set its properties ("type" and "id")
      // 4) register the EventInfo object into the TableEvents via TableEvents.addEventInfo()
      // 5) for every event's property attribute, instance a new Property object
      //    and and set its properties ("name" and "value")
      // 6) register the Property object into the EventInfo object
      digester.addObjectCreate("dbforms-config/query/events", 
                               "org.dbforms.config.TableEvents");
      digester.addSetNext("dbforms-config/query/events", "setTableEvents", 
                          "org.dbforms.config.TableEvents");
      digester.addObjectCreate("dbforms-config/query/events/event", 
                               "org.dbforms.config.EventInfo");
      digester.addSetProperties("dbforms-config/query/events/event");
      digester.addSetNext("dbforms-config/query/events/event", "addEventInfo", 
                          "org.dbforms.config.EventInfo");
      digester.addObjectCreate("dbforms-config/query/events/event/property", 
                               "org.dbforms.config.DbConnectionProperty");
      digester.addSetProperties("dbforms-config/query/events/event/property");
      digester.addSetNext("dbforms-config/query/events/event/property", 
                          "addProperty", 
                          "org.dbforms.config.DbConnectionProperty");


      // parse "DbConnecion" - object
      digester.addObjectCreate("dbforms-config/dbconnection", 
                               "org.dbforms.config.DbConnection");
      digester.addSetProperties("dbforms-config/dbconnection");
      digester.addSetNext("dbforms-config/dbconnection", "addDbConnection", 
                          "org.dbforms.config.DbConnection");


      // parse "property" - object + add it to parent (which is "DbConnection")
      digester.addObjectCreate("dbforms-config/dbconnection/property", 
                               "org.dbforms.config.DbConnectionProperty");
      digester.addSetProperties("dbforms-config/dbconnection/property");
      digester.addSetNext("dbforms-config/dbconnection/property", "addProperty", 
                          "org.dbforms.config.DbConnectionProperty");


      // parse "pool-property" - object + add it to parent (which is "DbConnection")
      digester.addObjectCreate("dbforms-config/dbconnection/pool-property", 
                               "org.dbforms.config.DbConnectionProperty");
      digester.addSetProperties("dbforms-config/dbconnection/pool-property");
      digester.addSetNext("dbforms-config/dbconnection/pool-property", 
                          "addPoolProperty", 
                          "org.dbforms.config.DbConnectionProperty");


      // parse "database-events/database-event" - object and register them into the DatabaseEventsFactory
      digester.addFactoryCreate("dbforms-config/events/database-events", 
                                new SingletonClassFactoryCreate(
                                         DatabaseEventFactoryImpl.class.getName()));
      digester.addObjectCreate(
               "dbforms-config/events/database-events/database-event", 
               "org.dbforms.config.EventInfo");
      digester.addSetProperties(
               "dbforms-config/events/database-events/database-event");
      digester.addSetNext(
               "dbforms-config/events/database-events/database-event", 
               "addEventInfo", "org.dbforms.config.EventInfo");


      // parse "database-events/navigation-event" - object and register them into the NavigationEventsFactory
      digester.addFactoryCreate("dbforms-config/events/navigation-events", 
                                new SingletonClassFactoryCreate(
                                         NavEventFactoryImpl.class.getName()));
      digester.addObjectCreate(
               "dbforms-config/events/navigation-events/navigation-event", 
               "org.dbforms.config.EventInfo");
      digester.addSetProperties(
               "dbforms-config/events/navigation-events/navigation-event");
      digester.addSetNext(
               "dbforms-config/events/navigation-events/navigation-event", 
               "addEventInfo", "org.dbforms.config.EventInfo");

      return digester;
   }


   /**
    * Construct and return a digester that uses the new errors file format.
    *
    * @param detail DOCUMENT ME!
    * @param dbFormsErrors DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   protected Digester initErrorsDigester(DbFormsErrors dbFormsErrors)
   {
      // Initialize a new Digester instance
      logCat.info("initialize Errors Digester.");

      Digester digester = new Digester();
      digester.setLogger(
               new org.apache.commons.logging.impl.Log4JLogger(digesterLogger));
      digester.push(dbFormsErrors);
      digester.setValidating(false);


      // Configure the processing rules
      // parse "Error" - object
      digester.addObjectCreate("dbforms-errors/error", 
                               "org.dbforms.config.Error");
      digester.addSetProperties("dbforms-errors/error");
      digester.addSetNext("dbforms-errors/error", "addError", 
                          "org.dbforms.config.Error");


      // parse "Message" - object + add it to parent (which is "Error")
      digester.addObjectCreate("dbforms-errors/error/message", 
                               "org.dbforms.config.Message");
      digester.addSetNext("dbforms-errors/error/message", "addMessage", 
                          "org.dbforms.config.Message");
      digester.addSetProperties("dbforms-errors/error/message");
      digester.addCallMethod("dbforms-errors/error/message", "setMessage", 0);

      return digester;
   }


   /**
    * Initialize the mapping information for this application.
    *
    * @param digesterDebugLevel DOCUMENT ME!
    *
    * @exception IOException if an input/output error is encountered
    * @exception ServletException if we cannot initialize these resources
    */
   protected void initXMLErrors() throws IOException, ServletException
   {
      logCat.info("initialize XML Errors.");

      // Look to see if developer has specified his/her own errors filename & location
      String value = getServletConfig().getInitParameter(DbFormsErrors.ERRORS);

      if (value != null)
      {
         errors = value;
      }

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
      Digester      digester = null;
      digester = initErrorsDigester(dbFormsErrors);


      // store a reference to ServletErrors (for interoperation with other parts of the Web-App!)
      dbFormsErrors.setServletConfig(getServletConfig());


      // store this errors object in the servlet context ("application")
      getServletContext().setAttribute(DbFormsErrors.ERRORS, dbFormsErrors);

      // Parse the input stream to configure our mappings
      try
      {
         digester.parse(input);
         input.close();
      }
      catch (SAXException e)
      {
         throw new ServletException(e.toString());
      }

      logCat.info("DbForms Error: " + dbFormsErrors);
   }


   /**
    * Initialize the mapping information for this application.
    *
    * @param digesterDebugLevel DOCUMENT ME!
    *
    * @exception IOException if an input/output error is encountered
    * @exception ServletException if we cannot initialize these resources
    */
   protected void initXMLConfig() throws IOException, ServletException
   {
      // Initialize the context-relative path to our configuration resources
      String value = getServletConfig().getInitParameter(DbFormsConfig.CONFIG);

      if (value != null)
      {
         config = value;
      }

      String[] s = StringUtils.split(config, ",");

      for (int i = 0; i < s.length; i++)
         initXMLConfigFile(s[i]);
   }


   /**
    * DOCUMENT ME!
    *
    * @param config DOCUMENT ME!
    * @param digesterDebugLevel DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    * @throws ServletException DOCUMENT ME!
    */
   protected void initXMLConfigFile(String config) throws IOException, 
                                                          ServletException
   {
      // Build a digester to process our configuration resource
      String realPath = getServletContext().getRealPath("/");

      // Acquire an input stream to our configuration resource
      InputStream input = getServletContext().getResourceAsStream(config);

      if (input == null)
      {
         throw new UnavailableException("configMissing");
      }

      // register the config object into the DbFormsConfigRegistry
      // as the default config (fossato, 2002.12.02)
      DbFormsConfigRegistry registry      = DbFormsConfigRegistry.instance();
      DbFormsConfig         dbFormsConfig = null;

      try
      {
         dbFormsConfig = registry.lookup();
      }
      catch (Exception e)
      {
         logCat.error("initXMLConfigFile", e);
      }

      if (dbFormsConfig == null)
      {
         dbFormsConfig = new DbFormsConfig(realPath);


         // store a reference to ServletConfig (for interoperation with other parts of the Web-App!)
         dbFormsConfig.setServletConfig(getServletConfig());


         // ---------------------------------------------------------------
         registry.setServletContext(getServletContext());
         registry.register(dbFormsConfig);
      }

      // ---------------------------------------------------------------
      Digester digester = initDigester(dbFormsConfig);

      // Parse the input stream to configure our mappings
      try
      {
         digester.parse(input);
         input.close();
      }
      catch (SAXException e)
      {
         logCat.error("::initXMLConfig - SaxException", e);
         throw new ServletException(e.toString());
      }
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
                                        String validator_rules)
                                 throws ServletException
   {
      // Acquire an input stream validator_rules
      InputStream inputValidatorRules = getServletContext()
                                           .getResourceAsStream(validator_rules);

      if (inputValidatorRules == null)
      {
         // File not available, log warning
         logCat.warn(
                  "XML Validator rule file not found, XML Validator handler disabled!");

         return;
      }

      //
      // Initialize ValidatorResources
      //
      try
      {
         ValidatorResourcesInitializer.initialize(resources, 
                                                  inputValidatorRules);
      }
      catch (IOException e)
      {
         logCat.warn(
                  "XML Validator Exception ValidatorResourcesInitializer.initialize  : "
                  + e.getMessage());
         throw new ServletException(e.toString());
      }
      finally
      {
         if (inputValidatorRules != null)
         {
            try
            {
               inputValidatorRules.close();
            }
            catch (Exception e)
            {
               logCat.error("initXMLValidatorRules", e);
            }
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
                                             String validation)
                                      throws  ServletException
   {
      //
      // LOAD Validation & Validator_rules files
      //
      // Acquire an input stream validation
      InputStream inputValidation = getServletContext()
                                       .getResourceAsStream(validation);

      if (inputValidation == null)
      {
         // File not available, log warning
         logCat.warn(
                  "XML Validation file not found, XML Validator handler disabled!");

         return;
      }

      try
      {
         ValidatorResourcesInitializer.initialize(resources, inputValidation);
      }
      catch (IOException e)
      {
         logCat.warn(
                  "XML Validator Exception ValidatorResourcesInitializer.initialize  : "
                  + e.getMessage());
         throw new ServletException(e.toString());
      }
      finally
      {
         if (inputValidation != null)
         {
            try
            {
               inputValidation.close();
            }
            catch (Exception e)
            {
               logCat.error("initXMLValidatorValidation", e);
            }
         }
      }
   }


   /**
    * Initialize the ValidatorResources information for this application.
    *
    * @exception IOException if an input/output error is encountered
    * @exception ServletException if we cannot initialize these resources
    */
   protected void initXMLValidator() throws ServletException
   {
      // Map the commons-logging used by commons-validator to Log4J logger
      System.setProperty("org.apache.commons.logging.Log", 
                         "org.apache.commons.logging.impl.Log4JCategoryLog");

      ValidatorResources resources = new ValidatorResources();
      logCat.info("initialize XML Validator.");

      String value;
      value = getServletConfig()
                 .getInitParameter(ValidatorConstants.VALIDATOR_RULES);

      if (value != null)
      {
         validator_rules = value;
      }

      initXMLValidatorRules(resources, validator_rules);

      value = getServletConfig()
                 .getInitParameter(ValidatorConstants.VALIDATION);

      if (value != null)
      {
         validation = value;
      }

      String[] s = StringUtils.split(validation, ",");

      for (int i = 0; i < s.length; i++)
         initXMLValidatorValidation(resources, s[i]);


      // store this errors object in the servlet context ("application")
      getServletContext().setAttribute(ValidatorConstants.VALIDATOR, resources);

      logCat.info(" DbForms Validator : Loaded ");
   }


   /**
    * Initialize the SubClass information use by the ResourceBundle for this
    * application. ATTENTION: Here the "application" it's use as Class name,
    * not like path/file coordonnates. (see java.util.ResourceBundle)
    *
    * @exception IOException if an input/output error is encountered
    * @exception ServletException if we cannot initialize these resources
    */
   protected void initApplicationResources() 
   {
      logCat.info("initialize Application Resources.");

      String value = getServletConfig()
                        .getInitParameter(ValidatorConstants.RESOURCE_BUNDLE);

      if (value == null)
      {
         logCat.warn(
                  " Application Resources file not setted in Web.xml, ApplicationResources handler disabled!");

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
   protected void initLocaleKey()
   {
      logCat.info("initialize Locale Key for session attribute.");

      String value = getServletConfig().getInitParameter("localeKey");

      if (value == null)
      {
         logCat.warn(" Locale Key not setted, use \""
                     + MessageResources.LOCALE_KEY
                     + "\" as key to access the Locale in session scope.");

         return;
      }

      MessageResources.LOCALE_KEY = value.trim();

      logCat.info(" Locale Key setted with \"" + MessageResources.LOCALE_KEY
                  + "\" as key to access the Locale in session scope.");
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
                   throws IOException
   {
      PrintWriter out = response.getWriter();

      try
      {
         DbFormsConfig dbFormsConfig = DbFormsConfigRegistry.instance()
                                                            .lookup();
         out.println(dbFormsConfig.toString());
      }
      catch (Exception e)
      {
         throw new IOException(e.getMessage());
      }
   }
}