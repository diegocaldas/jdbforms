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
package org.dbforms.config;

import java.io.InputStream;
import java.io.IOException;
import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.dbforms.event.DatabaseEventFactoryImpl;
import org.dbforms.event.NavEventFactoryImpl;
import org.dbforms.util.SingletonClassFactoryCreate;
import org.xml.sax.SAXException;

/**
 * Loads the configuration. 
 * With this class you can load the configuration data from 
 * an standalone application, e.g.:
 * 
 * 
 *           dbConfig = new DbFormsConfig(realPath);
 *           FileInputStream fis = new FileInputStream(fn);
 *           ConfigLoader loader = new ConfigLoader()
 *           loader.loadConfig(fis, dbConfig);
 * 
 * @author Ivan F. Martinez 
 */
public class ConfigLoader {

   public static final String BASE_CLASS_TABLE = "org.dbforms.config.Table";
   public static final String BASE_CLASS_FIELD = "org.dbforms.config.Field";
   public static final String BASE_CLASS_QUERY = "org.dbforms.config.Query";
   public static final String BASE_CLASS_FOREIGNKEY =
      "org.dbforms.config.ForeignKey";
   public static final String BASE_CLASS_REFERENCE =
      "org.dbforms.config.Reference";

   public static final String DEFAULT_CONFIG = "/WEB-INF/dbforms-config.xml";
   public static final String DEFAULT_ERRORS = "/WEB-INF/dbforms-errors.xml";
   public static final String DEFAULT_VALIDATION = "/WEB-INF/validation.xml";
   public static final String DEFAULT_RULES = "/WEB-INF/validator-rules.xml";

   /** The context-relative path to our configuration and errors resources. */
   private String config = DEFAULT_CONFIG;

   /** DOCUMENT ME! */
   private String errors = DEFAULT_ERRORS;

   /** DOCUMENT ME! */
   private String validation = DEFAULT_VALIDATION;

   /** DOCUMENT ME! */
   private String validator_rules = DEFAULT_RULES;
   /**
    * className for Table
    */
   private String tableClassName = BASE_CLASS_TABLE;
   /**
    * className for Field
    */
   private String fieldClassName = BASE_CLASS_FIELD;
   /**
    * className for Query
    */
   private String queryClassName = BASE_CLASS_QUERY;
   /**
    * className for ForeignKey
    */
   private String foreignKeyClassName = BASE_CLASS_FOREIGNKEY;
   /**
    * className for Reference
    */
   private String referenceClassName = BASE_CLASS_REFERENCE;

   private Logger logCat = Logger.getLogger(ConfigLoader.class);
   private Logger digesterLogger = Logger.getLogger(Digester.class);

   public ConfigLoader() {
   }

   /**
    * Construct and return a digester that uses the new configuration file
    * format.
    *
    * @param detail DOCUMENT ME!
    * @param dbFormsConfig DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   protected Digester initDigester(DbFormsConfig dbFormsConfig) {
      // Initialize a new Digester instance
      Digester digester = new Digester();
      digester.setLogger(
         new org.apache.commons.logging.impl.Log4JLogger(digesterLogger));
      digester.push(dbFormsConfig);
      digester.setNamespaceAware(true);
      digester.setValidating(false);

      // Configure the processing rules
      // parse "DOMFactoryClass"
      digester.addCallMethod(
         "dbforms-config/DOMFactoryClass",
         "setDOMFactoryClass",
         0);
      digester.addCallMethod(
         "dbforms-config/DefaultEscaperClass",
         "setDefaultEscaperClass",
         0);

      // parse "Table" - object + add it to parent
      digester.addObjectCreate("dbforms-config/table", tableClassName);
      digester.addSetProperties("dbforms-config/table");
      digester.addSetNext("dbforms-config/table", "addTable", BASE_CLASS_TABLE);

      // parse "Field" - object + add it to parent (which is "Table")
      digester.addObjectCreate("dbforms-config/table/field", fieldClassName);
      digester.addSetProperties("dbforms-config/table/field");
      digester.addSetNext(
         "dbforms-config/table/field",
         "addField",
         BASE_CLASS_FIELD);

      // parse "Foreign-Key" - object + add it to parent (which is "Table")
      digester.addObjectCreate(
         "dbforms-config/table/foreign-key",
         foreignKeyClassName);
      digester.addSetProperties("dbforms-config/table/foreign-key");
      digester.addSetNext(
         "dbforms-config/table/foreign-key",
         "addForeignKey",
         BASE_CLASS_FOREIGNKEY);

      // parse "Reference" - object + add it to parent (which is "ForeignKey")
      digester.addObjectCreate(
         "dbforms-config/table/foreign-key/reference",
         referenceClassName);
      digester.addSetProperties("dbforms-config/table/foreign-key/reference");
      digester.addSetNext(
         "dbforms-config/table/foreign-key/reference",
         "addReference",
         BASE_CLASS_REFERENCE);

      // parse "GrantedPrivileges" - object + add it to parent (which is "Table")
      digester.addObjectCreate(
         "dbforms-config/table/granted-privileges",
         "org.dbforms.config.GrantedPrivileges");
      digester.addSetProperties("dbforms-config/table/granted-privileges");
      digester.addSetNext(
         "dbforms-config/table/granted-privileges",
         "setGrantedPrivileges",
         "org.dbforms.config.GrantedPrivileges");

      // parse "Interceptor" - object + add it to parent (which is "Table")
      digester.addObjectCreate(
         "dbforms-config/table/interceptor",
         "org.dbforms.config.Interceptor");
      digester.addSetProperties("dbforms-config/table/interceptor");
      digester.addSetNext(
         "dbforms-config/table/interceptor",
         "addInterceptor",
         "org.dbforms.config.Interceptor");

      // J.Peer 03/2004: parse interceptor "param"s + add it to parent (which is "Interceptor")
      digester.addCallMethod(
         "dbforms-config/table/interceptor/param",
         "addParam",
         2);
      digester.addCallParam(
         "dbforms-config/table/interceptor/param",
         0,
         "name");
      digester.addCallParam(
         "dbforms-config/table/interceptor/param",
         1,
         "value");

      // register custom database or navigation events (parent is "Table");
      // 1) for every "events" element, instance a new TableEvents object;
      // 2) set the TableEvents reference into the Table object
      // 3) for every "event" element, instance a new EventInfo object and set its properties ("type" and "id")
      // 4) register the EventInfo object into the TableEvents via TableEvents.addEventInfo()
      // 5) for every event's property attribute, instance a new Property object
      //    and and set its properties ("name" and "value")
      // 6) register the Property object into the EventInfo object
      digester.addObjectCreate(
         "dbforms-config/table/events",
         "org.dbforms.config.TableEvents");
      digester.addSetNext(
         "dbforms-config/table/events",
         "setTableEvents",
         "org.dbforms.config.TableEvents");
      digester.addObjectCreate(
         "dbforms-config/table/events/event",
         "org.dbforms.config.EventInfo");
      digester.addSetProperties("dbforms-config/table/events/event");
      digester.addSetNext(
         "dbforms-config/table/events/event",
         "addEventInfo",
         "org.dbforms.config.EventInfo");
      digester.addObjectCreate(
         "dbforms-config/table/events/event/property",
         "org.dbforms.config.DbConnectionProperty");
      digester.addSetProperties("dbforms-config/table/events/event/property");
      digester.addSetNext(
         "dbforms-config/table/events/event/property",
         "addProperty",
         "org.dbforms.config.DbConnectionProperty");

      // parse "Query" - object + add it to parent
      digester.addObjectCreate("dbforms-config/query", queryClassName);
      digester.addSetProperties("dbforms-config/query");
      digester.addSetNext("dbforms-config/query", "addTable", BASE_CLASS_TABLE);

      // parse "Field" - object + add it to parent (which is "Query")
      digester.addObjectCreate("dbforms-config/query/field", fieldClassName);
      digester.addSetProperties("dbforms-config/query/field");
      digester.addSetNext(
         "dbforms-config/query/field",
         "addField",
         BASE_CLASS_FIELD);

      // parse "search" - object + add it to parent (which is "Query")
      digester.addObjectCreate("dbforms-config/query/search", fieldClassName);
      digester.addSetProperties("dbforms-config/query/search");
      digester.addSetNext(
         "dbforms-config/query/search",
         "addSearchField",
         BASE_CLASS_FIELD);

      // parse "Foreign-Key" - object + add it to parent (which is "Table")
      digester.addObjectCreate(
         "dbforms-config/query/foreign-key",
         foreignKeyClassName);
      digester.addSetProperties("dbforms-config/query/foreign-key");
      digester.addSetNext(
         "dbforms-config/query/foreign-key",
         "addForeignKey",
         BASE_CLASS_FOREIGNKEY);

      // parse "Reference" - object + add it to parent (which is "ForeignKey")
      digester.addObjectCreate(
         "dbforms-config/query/foreign-key/reference",
         referenceClassName);
      digester.addSetProperties("dbforms-config/query/foreign-key/reference");
      digester.addSetNext(
         "dbforms-config/query/foreign-key/reference",
         "addReference",
         BASE_CLASS_REFERENCE);

      // parse "GrantedPrivileges" - object + add it to parent (which is "Query")
      digester.addObjectCreate(
         "dbforms-config/query/granted-privileges",
         "org.dbforms.config.GrantedPrivileges");
      digester.addSetProperties("dbforms-config/query/granted-privileges");
      digester.addSetNext(
         "dbforms-config/query/granted-privileges",
         "setGrantedPrivileges",
         "org.dbforms.config.GrantedPrivileges");

      // parse "Condition" - object + add it to parent (which is "Query")
      digester.addObjectCreate(
         "dbforms-config/query/interceptor",
         "org.dbforms.config.Interceptor");
      digester.addSetProperties("dbforms-config/query/interceptor");
      digester.addSetNext(
         "dbforms-config/query/interceptor",
         "addInterceptor",
         "org.dbforms.config.Interceptor");

      // J.Peer 03/2004: parse interceptor "param"s + add it to parent (which is "Interceptor")
      digester.addCallMethod(
         "dbforms-config/query/interceptor/param",
         "addParam",
         2);
      digester.addCallParam(
         "dbforms-config/query/interceptor/param",
         0,
         "name");
      digester.addCallParam(
         "dbforms-config/query/interceptor/param",
         1,
         "value");

      // register custom database or navigation events (parent is "Query");
      // 1) for every "events" element, instance a new TableEvents object;
      // 2) set the TableEvents reference into the Table object
      // 3) for every "event" element, instance a new EventInfo object and set its properties ("type" and "id")
      // 4) register the EventInfo object into the TableEvents via TableEvents.addEventInfo()
      // 5) for every event's property attribute, instance a new Property object
      //    and and set its properties ("name" and "value")
      // 6) register the Property object into the EventInfo object
      digester.addObjectCreate(
         "dbforms-config/query/events",
         "org.dbforms.config.TableEvents");
      digester.addSetNext(
         "dbforms-config/query/events",
         "setTableEvents",
         "org.dbforms.config.TableEvents");
      digester.addObjectCreate(
         "dbforms-config/query/events/event",
         "org.dbforms.config.EventInfo");
      digester.addSetProperties("dbforms-config/query/events/event");
      digester.addSetNext(
         "dbforms-config/query/events/event",
         "addEventInfo",
         "org.dbforms.config.EventInfo");
      digester.addObjectCreate(
         "dbforms-config/query/events/event/property",
         "org.dbforms.config.DbConnectionProperty");
      digester.addSetProperties("dbforms-config/query/events/event/property");
      digester.addSetNext(
         "dbforms-config/query/events/event/property",
         "addProperty",
         "org.dbforms.config.DbConnectionProperty");

      // parse "DbConnecion" - object
      digester.addObjectCreate(
         "dbforms-config/dbconnection",
         "org.dbforms.config.DbConnection");
      digester.addSetProperties("dbforms-config/dbconnection");
      digester.addSetNext(
         "dbforms-config/dbconnection",
         "addDbConnection",
         "org.dbforms.config.DbConnection");

      // parse "property" - object + add it to parent (which is "DbConnection")
      digester.addObjectCreate(
         "dbforms-config/dbconnection/property",
         "org.dbforms.config.DbConnectionProperty");
      digester.addSetProperties("dbforms-config/dbconnection/property");
      digester.addSetNext(
         "dbforms-config/dbconnection/property",
         "addProperty",
         "org.dbforms.config.DbConnectionProperty");

      // parse "pool-property" - object + add it to parent (which is "DbConnection")
      digester.addObjectCreate(
         "dbforms-config/dbconnection/pool-property",
         "org.dbforms.config.DbConnectionProperty");
      digester.addSetProperties("dbforms-config/dbconnection/pool-property");
      digester.addSetNext(
         "dbforms-config/dbconnection/pool-property",
         "addPoolProperty",
         "org.dbforms.config.DbConnectionProperty");

      // parse "Interceptor" - object + add it to parent (which is "DbConfig")
      digester.addObjectCreate(
         "dbforms-config/interceptors/interceptor",
         "org.dbforms.config.Interceptor");
      digester.addSetProperties("dbforms-config/interceptors/interceptor");
      digester.addSetNext(
         "dbforms-config/interceptors/interceptor",
         "addInterceptor",
         "org.dbforms.config.Interceptor");

      // J.Peer 03/2004: parse interceptor "param"s + add it to parent (which is "Interceptor")
      digester.addCallMethod(
         "dbforms-config/interceptors/interceptor/param",
         "addParam",
         2);
      digester.addCallParam(
         "dbforms-config/interceptors/interceptor/param",
         0,
         "name");
      digester.addCallParam(
         "dbforms-config/interceptors/interceptor/param",
         1,
         "value");
      // parse "database-events/database-event" - object and register them into the DatabaseEventsFactory
      digester.addFactoryCreate(
         "dbforms-config/events/database-events",
         new SingletonClassFactoryCreate(
            DatabaseEventFactoryImpl.class.getName()));
      digester.addObjectCreate(
         "dbforms-config/events/database-events/database-event",
         "org.dbforms.config.EventInfo");
      digester.addSetProperties(
         "dbforms-config/events/database-events/database-event");
      digester.addSetNext(
         "dbforms-config/events/database-events/database-event",
         "addEventInfo",
         "org.dbforms.config.EventInfo");

      // parse "database-events/navigation-event" - object and register them into the NavigationEventsFactory
      digester.addFactoryCreate(
         "dbforms-config/events/navigation-events",
         new SingletonClassFactoryCreate(NavEventFactoryImpl.class.getName()));
      digester.addObjectCreate(
         "dbforms-config/events/navigation-events/navigation-event",
         "org.dbforms.config.EventInfo");
      digester.addSetProperties(
         "dbforms-config/events/navigation-events/navigation-event");
      digester.addSetNext(
         "dbforms-config/events/navigation-events/navigation-event",
         "addEventInfo",
         "org.dbforms.config.EventInfo");

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
   protected Digester initErrorsDigester(DbFormsErrors dbFormsErrors) {
      // Initialize a new Digester instance
      logCat.info("initialize Errors Digester.");

      Digester digester = new Digester();
      digester.setLogger(
         new org.apache.commons.logging.impl.Log4JLogger(digesterLogger));
      digester.push(dbFormsErrors);
      digester.setValidating(false);

      // Configure the processing rules
      // parse "Error" - object
      digester.addObjectCreate(
         "dbforms-errors/error",
         "org.dbforms.config.Error");
      digester.addSetProperties("dbforms-errors/error");
      digester.addSetNext(
         "dbforms-errors/error",
         "addError",
         "org.dbforms.config.Error");

      // parse "Message" - object + add it to parent (which is "Error")
      digester.addObjectCreate(
         "dbforms-errors/error/message",
         "org.dbforms.config.Message");
      digester.addSetNext(
         "dbforms-errors/error/message",
         "addMessage",
         "org.dbforms.config.Message");
      digester.addSetProperties("dbforms-errors/error/message");
      digester.addCallMethod("dbforms-errors/error/message", "setMessage", 0);

      return digester;
   }

   /**
    * Load Config from InputStream
    *
    * @param input InputStream to load Config
    * @param dbFormsErrors object to load the Config
    */
   public void loadConfig(InputStream input, DbFormsConfig dbFormsConfig)
      throws IOException, SAXException {
      Digester digester = initDigester(dbFormsConfig);
      digester.parse(input);
   }

   /**
    * Load Errors Config from InputStream
    *
    * @param input InputStream to load Errors Config
    * @param dbFormsErrors object to load the Errors config
    */
   public void loadErrors(InputStream input, DbFormsErrors dbFormsErrors)
      throws IOException, SAXException {
      Digester digester = initErrorsDigester(dbFormsErrors);
      // Parse the input stream to configure our mappings
      digester.parse(input);
   }

   public void setFieldClassName(String className) {
      if (className != null) {
         fieldClassName = className;
      } else {
         fieldClassName = BASE_CLASS_FIELD;
      }
   }

   public void setTableClassName(String className) {
      if (className != null) {
         tableClassName = className;
      } else {
         tableClassName = BASE_CLASS_TABLE;
      }
   }

   public void setQueryClassName(String className) {
      if (className != null) {
         queryClassName = className;
      } else {
         queryClassName = BASE_CLASS_QUERY;
      }
   }

   public void setForeignKeyClassName(String className) {
      if (className != null) {
         foreignKeyClassName = className;
      } else {
         foreignKeyClassName = BASE_CLASS_FOREIGNKEY;
      }
   }

   public void setReferenceClassName(String className) {
      if (className != null) {
         referenceClassName = className;
      } else {
         referenceClassName = BASE_CLASS_REFERENCE;
      }
   }

   public void setConfig(String config) {
      if (config != null) {
         this.config = config;
      } else {
         this.config = DEFAULT_CONFIG;
      }
   }

   public String getConfig() {
      return config;
   }

   public void setErrors(String errors) {
      if (errors != null) {
         this.errors = errors;
      } else {
         this.errors = DEFAULT_ERRORS;
      }
   }

   public String getErrors() {
      return errors;
   }

   public void setValidation(String validation) {
      if (validation != null) {
         this.validation = validation;
      } else {
         this.validation = DEFAULT_VALIDATION;
      }
   }

   public String getValidation() {
      return validation;
   }

   public void setValidatorRules(String validator_rules) {
      if (validator_rules != null) {
         this.validator_rules = validator_rules;
      } else {
         this.validator_rules = DEFAULT_RULES;
      }
   }

   public String getValidatorRules() {
      return validator_rules;
   }

}
