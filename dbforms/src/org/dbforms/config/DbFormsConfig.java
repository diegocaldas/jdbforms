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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.dom.DOMFactory;

import org.dbforms.util.Escaper;
import org.dbforms.util.ReflectionUtil;
import org.dbforms.util.Util;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import javax.sql.DataSource;



/**
 * <p>
 * This class gets populated with data from the dbforms-config.xml file by the
 * ConfigServlet. This class is a kind of "single point of entry" for
 * configuration data: it contains the definitions of tables, fields,
 * fieldtypes, keys etc. and even the definitions of data sources (see
 * dbforms-config.xml) Many components of the dbforms-frameworks use the data
 * stored in this class.
 * </p>
 *
 * @author Joe Peer
 */
public class DbFormsConfig {
   /** DOCUMENT ME! */
   public static final String CONFIG = "dbformsConfig";

   /** contains connection put by addDbConnection */
   private ArrayList dbConnectionsList = new ArrayList();

   /** the default db connection */
   private DbConnection defaultDbConnection;
   private Escaper      escaper           = null;
   private Hashtable    dbConnectionsHash = new Hashtable();

   /** for quicker lookup by name */
   private Hashtable     tableNameHash         = new Hashtable();
   private Log           logCat                = LogFactory.getLog(this.getClass().getName());
   private ServletConfig servletConfig;
   private String        defaultEscaperClass   = "org.dbforms.util.DefaultEscaperImpl";
   private String        defaultFormatterClass = "org.dbforms.util.DefaultFormatterImpl";
   private String        realPath;

   /** global application hookups */
   private Vector interceptors = new Vector();
   private Vector tables = new Vector();

   /**
    * Creates a new DbFormsConfig object.
    *
    * @param realPath local path to the application on local server
    */
   public DbFormsConfig(String realPath) {
      setRealPath(realPath);
   }

   /**
    * Just returns the default connection
    *
    * @return a JDBC connection object
    *
    * @throws IllegalArgumentException if any error occurs
    */
   public Connection getConnection()
                            throws IllegalArgumentException, SQLException {
      return getConnection(null);
   }


   /**
    * Get a connection using the connection name specified into the xml
    * configuration file.
    *
    * @param dbConnectionName the name of the DbConnection element
    *
    * @return a JDBC connection object
    *
    * @throws IllegalArgumentException if any error occurs
    */
   public Connection getConnection(String dbConnectionName)
                            throws IllegalArgumentException, SQLException {
      DataSource dbConnection = null;
      Connection con = null;

      //  get the DbConnection object having the input name;
      if ((dbConnection = getDataSource(dbConnectionName)) == null) {
         throw new IllegalArgumentException("No DbConnection object configured with name '"
                                            + dbConnectionName + "'");
      }

      // now try to get the JDBC connection from the retrieved DbConnection object;
      if ((con = dbConnection.getConnection()) == null) {
         throw new IllegalArgumentException("JDBC-Troubles:  was not able to create connection from "
                                            + dbConnection);
      }

      return con;
   }


   /**
    * DOCUMENT ME!
    *
    * @param string
    */
   public void setDOMFactoryClass(String string) {
      DOMFactory.setFactoryClass(string);
   }


   /**
    * DOCUMENT ME!
    *
    * @param dbConnectionName DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public DataSource getDataSource(String dbConnectionName) {
      DbConnection connection = null;

      if (!Util.isNull(dbConnectionName)) {
         try {
            connection = (DbConnection) dbConnectionsList.get(Integer.parseInt(dbConnectionName));
         } catch (Exception ex) {
            // wanted! logCat.error("getDbConnection", ex);
            connection = null;
         }

         if (connection == null) {
            connection = (DbConnection) dbConnectionsHash.get(dbConnectionName);
         }
      }

      if (connection == null) {
         connection = defaultDbConnection;
      }

      if ((connection != null)
                && !Util.isNull(connection.getContextDataSource())) {
         DataSource ds = (DataSource) getServletContext()
                                         .getAttribute(connection
                                                       .getContextDataSource());

         if (ds != null) {
            return ds;
         }
      }

      return connection;
   }


   /**
    * DOCUMENT ME!
    *
    * @param string
    */
   public void setDefaultEscaperClass(String string) {
      defaultEscaperClass = string;
   }


   /**
    * DOCUMENT ME!
    *
    * @return
    */
   public String getDefaultEscaperClass() {
      return defaultEscaperClass;
   }


   /**
    * DOCUMENT ME!
    *
    * @param string
    */
   public void setDefaultFormatterClass(String string) {
      defaultFormatterClass = string;
   }


   /**
    * DOCUMENT ME!
    *
    * @return
    */
   public String getDefaultFormatterClass() {
      return defaultFormatterClass;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public Escaper getEscaper() {
      if (escaper == null) {
         String s = getDefaultEscaperClass();

         if (!Util.isNull(s)) {
            try {
               escaper = (Escaper) ReflectionUtil.newInstance(s);
            } catch (Exception e) {
               logCat.error("cannot create the new escaper [" + s + "]", e);
            }
         }
      }

      return escaper;
   }


   /**
    * Get all the global interceptor objects
    *
    * @return a vector containing all the interceptor objects
    */
   public Vector getInterceptors() {
      return interceptors;
   }


   /**
    * Sets the realPath.
    *
    * @param realPath The realPath to set
    */
   public void setRealPath(String realPath) {
      if (!Util.isNull(realPath)) {
         realPath = realPath.replace('\\', '/');
      }

      this.realPath = realPath;
   }


   /**
    * Returns the realPath.
    *
    * @return the realPath
    */
   public String getRealPath() {
      return realPath;
   }


   /**
    * DOCUMENT ME!
    *
    * @param servletConfig DOCUMENT ME!
    */
   public void setServletConfig(ServletConfig servletConfig) {
      this.servletConfig = servletConfig;
   }


   /**
    * get access to configuration of config servlet
    *
    * @return the store config
    */
   public ServletConfig getServletConfig() {
      return servletConfig;
   }


   /**
    * Get access to servlet context in order to interoperate with other
    * components of the web application
    *
    * @return the stored context
    */
   public ServletContext getServletContext() {
      return servletConfig.getServletContext();
   }


   /**
    * DOCUMENT ME!
    *
    * @param index DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public Table getTable(int index) {
      try {
         return (Table) tables.elementAt(index);
      } catch (Exception e) {
         return null;
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param name DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public Table getTableByName(String name) {
      try {
         return (Table) tableNameHash.get(name);
      } catch (Exception e) {
         return null;
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param dbConnection DOCUMENT ME!
    */
   public void addDbConnection(DbConnection dbConnection) {
      dbConnection.setName(Util.replaceRealPath(dbConnection.getName(), realPath));
      dbConnectionsList.add(dbConnection);

      if (!Util.isNull(dbConnection.getId())) {
         dbConnectionsHash.put(dbConnection.getId(), dbConnection);
      }

      // if a default connection does not exist yet,
      // use the input connection as the default one;
      if ((dbConnection.isDefaultConnection()
                && ((defaultDbConnection == null)
                || !defaultDbConnection.isDefaultConnection()))
                || (defaultDbConnection == null)) {
         defaultDbConnection = dbConnection;
         dbConnection.setDefaultConnection(true);
      }

      logCat.info("::addDbConnection - added the dbConnection [" + dbConnection
                  + "]");
   }


   /**
    * Add an global interceptor
    *
    * @param interceptor the interceptor to add
    */
   public void addInterceptor(Interceptor interceptor) {
      interceptors.addElement(interceptor);
   }


   /**
    * DOCUMENT ME!
    *
    * @param table DOCUMENT ME!
    */
   public void addTable(Table table) {
      logCat.info("add table called");
      table.setId(tables.size());
      table.setConfig(this);
      table.initDefaultOrder();
      tables.addElement(table);
      tableNameHash.put(table.getName(), table);
   }


   /**
    * Check if this table has got interceptors.
    *
    * @return true if the table contains interceptors, false otherwise
    */
   public boolean hasInterceptors() {
      return (interceptors != null) && (interceptors.size() > 0);
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String toString() {
      StringBuffer buf = new StringBuffer();

      for (int i = 0; i < tables.size(); i++) {
         Table t = (Table) tables.elementAt(i);
         buf.append("table:\n");
         buf.append(t.toString());
      }

      return buf.toString();
   }
}
