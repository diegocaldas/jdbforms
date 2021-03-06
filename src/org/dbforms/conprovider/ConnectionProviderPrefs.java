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

package org.dbforms.conprovider;

import java.util.Properties;

import javax.servlet.ServletContext;



/**
 * Preferences class for Connection Providers.
 *
 * @author Luca Fossato
 */
public class ConnectionProviderPrefs {
   /** Holds connection pool custom properties */
   private Properties poolProperties;

   /** Holds jdbc properties */
   private Properties     properties;
   private ServletContext servletContext;

   /** connection pool url */
   private String connectionPoolURL;

   /** connection provider class name */
   private String connectionProviderClass;

   /** the JDBC driver class name */
   private String jdbcDriver;

   /** the JDBC URL string */
   private String jdbcURL;

   /** database user password */
   private String password;

   /** database user name */
   private String user;

   /**
    * Sets the connectionPoolURL attribute of the ConnectionProviderPrefs
    * object
    *
    * @param connectionPoolURL The new connectionPoolURL value
    */
   public void setConnectionPoolURL(String connectionPoolURL) {
      this.connectionPoolURL = connectionPoolURL;
   }


   /**
    * Gets the connectionPoolURL attribute of the ConnectionProviderPrefs
    * object
    *
    * @return The connectionPoolURL value
    */
   public String getConnectionPoolURL() {
      return connectionPoolURL;
   }


   /**
    * Sets the connectionProviderClass attribute of the ConnectionProviderPrefs
    * object
    *
    * @param connectionProviderClass The new connectionProviderClass value
    */
   public void setConnectionProviderClass(String connectionProviderClass) {
      this.connectionProviderClass = connectionProviderClass;
   }


   /**
    * Gets the connectionProviderClass attribute of the ConnectionProviderPrefs
    * object
    *
    * @return The connectionProviderClass value
    */
   public String getConnectionProviderClass() {
      return connectionProviderClass;
   }


   /**
    * Sets the jdbcDriver attribute of the ConnectionProviderPrefs object
    *
    * @param jdbcDriver The new jdbcDriver value
    */
   public void setJdbcDriver(String jdbcDriver) {
      this.jdbcDriver = jdbcDriver;
   }


   /**
    * Gets the jdbcDriver attribute of the ConnectionProviderPrefs object
    *
    * @return The jdbcDriver value
    */
   public String getJdbcDriver() {
      return jdbcDriver;
   }


   /**
    * Sets the jdbcURL attribute of the ConnectionProviderPrefs object
    *
    * @param jdbcURL The new jdbcURL value
    */
   public void setJdbcURL(String jdbcURL) {
      this.jdbcURL = jdbcURL;
   }


   /**
    * Gets the jdbcURL attribute of the ConnectionProviderPrefs object
    *
    * @return The jdbcURL value
    */
   public String getJdbcURL() {
      return jdbcURL;
   }


   /**
    * Sets the password attribute of the ConnectionProviderPrefs object
    *
    * @param password The new password value
    */
   public void setPassword(String password) {
      this.password = password;
   }


   /**
    * Gets the password attribute of the ConnectionProviderPrefs object
    *
    * @return The password value
    */
   public String getPassword() {
      return password;
   }


   /**
    * Sets the connection pool custom properties.
    *
    * @param poolProperties The pool properties to set
    */
   public void setPoolProperties(Properties poolProperties) {
      this.poolProperties = poolProperties;
   }


   /**
    * Returns the connection pool custom properties.
    *
    * @return Properties
    */
   public Properties getPoolProperties() {
      return poolProperties;
   }


   /**
    * Sets the properties.
    *
    * @param properties The properties to set
    */
   public void setProperties(Properties properties) {
      this.properties = properties;
   }


   /**
    * Returns the properties.
    *
    * @return Properties
    */
   public Properties getProperties() {
      return properties;
   }


   /**
    * DOCUMENT ME!
    *
    * @param context
    */
   public void setServletContext(ServletContext context) {
      servletContext = context;
   }


   /**
    * DOCUMENT ME!
    *
    * @return
    */
   public ServletContext getServletContext() {
      return servletContext;
   }


   /**
    * Sets the user attribute of the ConnectionProviderPrefs object
    *
    * @param user The new user value
    */
   public void setUser(String user) {
      this.user = user;
   }


   /**
    * Gets the user attribute of the ConnectionProviderPrefs object
    *
    * @return The user value
    */
   public String getUser() {
      return user;
   }
}
