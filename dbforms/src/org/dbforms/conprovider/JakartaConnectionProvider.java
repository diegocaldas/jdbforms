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
import java.util.Enumeration;
import java.util.Properties;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbcp.BasicDataSource;
import org.dbforms.util.Util;



/**
 *  Connection provider for Apache Jakarta commons-dbcp.
 *  <br>
 *  See <code>http://jakarta.apache.org/commons/dbcp.html</code>
 *  for further informations.
 * 
 * @author Luca Fossato
 * 
 */
public class JakartaConnectionProvider extends ConnectionProvider
{
   /**
    *  The SQL query that will be used to validate connections from this pool
    *  before returning them to the caller.
    */
   protected static final String CP_PROPS_VALIDATION_QUERY = "validationQuery";

   /**
    *  the maximum number of active connections that can be allocated from this pool
    *  at the same time, or zero for no limit.
    */
   protected static final String CP_PROPS_MAX_ACTIVE = "maxActive";

   /**
    *  The maximum number of active connections that can remain idle in the pool,
    *  without extra ones being released, or zero for no limit.
    */
   protected static final String CP_PROPS_MAX_IDLE = "maxIdle";

   /**
    *  The maximum number of milliseconds that the pool will wait
    *  (when there are no available connections) for a connection to be returned
    *  before throwing an exception, or -1 to wait indefinitely.
    */
   protected static final String CP_PROPS_MAX_WAIT = "maxWait";

   /** log the dataSource object statements using log4j category class */
   protected static final String CP_PROPS_USE_LOG = "useLog";

   /** Commons-dbcp dataSource * */
   private BasicDataSource dataSource = null;

   /**
    *  Default constructor.
    *
    * @exception  Exception Description of the Exception
    * @throws  Exception because of the <code>throws Exception</code> clause
    *                   of the  <code>init</code> method.
    */
   public JakartaConnectionProvider() throws Exception
   {
      super();
   }

   /**
    *  Get a JDBC Connection
    *
    * @return  a JDBC Connection
    * @exception  SQLException Description of the Exception
    */
   protected Connection getConnection() throws SQLException
   {
      getLog4JCategory().debug("::getConnection - MaxActive = " + dataSource.getMaxActive());
      getLog4JCategory().debug("::getConnection - NumActive = " + dataSource.getNumActive());
	  getLog4JCategory().debug("::getConnection - NumIdle   = " + dataSource.getNumIdle());
	  return dataSource.getConnection();
   }


   /**
    *  Initialize the Jakarta Commons connection pool.
    *
    * @throws  Exception if any error occurs
    */
   protected void init() throws Exception
   {
      Properties props = null;

      dataSource = new BasicDataSource();
      dataSource.setDriverClassName(getPrefs().getJdbcDriver());
      dataSource.setUrl(getPrefs().getJdbcURL());
      dataSource.setUsername(getPrefs().getUser());
      dataSource.setPassword(getPrefs().getPassword());

      // set the dataSource properties;
      if ((props = getPrefs().getProperties()) != null)
      {
         for (Enumeration e = props.propertyNames(); e.hasMoreElements();)
         {
            String key = (String) e.nextElement();
            dataSource.addConnectionProperty(key, props.getProperty(key));
            getLog4JCategory().info("::init - dataSource property [" + key + "] = ["
               + props.getProperty(key) + "]");
         }
      }

      // now set the connection pool custom properties;
      // if the connectionPool properties object is null,
      // instance a new properties object anyway, to use default values;
      if ((props = getPrefs().getPoolProperties()) == null)
      {
         props = new Properties();
      }

      String validationQuery = props.getProperty(CP_PROPS_VALIDATION_QUERY, null);

      if (!Util.isNull(validationQuery))
      {
         dataSource.setValidationQuery(validationQuery.trim());
      }

      dataSource.setMaxActive(Integer.parseInt(props.getProperty(
               CP_PROPS_MAX_ACTIVE, "20")));
      dataSource.setMaxIdle(Integer.parseInt(props.getProperty(
               CP_PROPS_MAX_IDLE, "5")));
      dataSource.setMaxWait(Long.parseLong(props.getProperty(
               CP_PROPS_MAX_WAIT, "-1")));

      // if PROPS_LOG == true, use log4j category to log the datasource info;
      String useLog = props.getProperty(CP_PROPS_USE_LOG, "false");

      if (!Util.isNull(useLog) && "true".equals(useLog.trim()))
      {
         getLog4JCategory().info("::init - dataSource log activated");
         dataSource.setLogWriter(new Log4jPrintWriter(getLog4JCategory(), getLog4JCategory().getPriority()));
      }
   }
}
