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
import java.util.*;
import java.sql.*;
import com.protomatter.jdbc.pool.JdbcConnectionPool;
import org.dbforms.util.Util;



/**
 *  Connection provider for Protomatter Connection pool.
 *  <br>
 *  See <code>http://protomatter.sourceforge.net/</code> for further informations.
 * 
 * @author Luca Fossato
 * 
 */
public class ProtomatterConnectionProvider extends ConnectionProvider
{
   /** the initial pool size (default is 0) */
   protected static final String CP_PROPS_INITIALSIZE = "pool.initialSize";

   /** the max pool size (default is -1). If the max pool size is -1, the pool grows infinitely. */
   protected static final String CP_PROPS_MAXSIZE = "pool.maxSize";

   /** the grow size (default is 1). When a new object is needed, this many are created. */
   protected static final String CP_PROPS_GROWBLOCK = "pool.growBlock";

   /**
    * the time (in ms) to sleep between pool object creates (default is 0).
    * This is useful for database connection pools where it's possible to overload the database
    * by trying to make too many connections too quickly.
    */
   protected static final String CP_PROPS_CREATEWAITTIME = "pool.createWaitTime";

   /**
    *  A SQL statement that is guaranteed to return at least 1 row.
    *  For Oracle, this is "select 1 from dual" and for Sybase it is "select 1".
    *  This statement is used as a means of checking that a connection is indeed working.
    */
   protected static final String CP_PROPS_VALIDITYCHECKSTATEMENT = "jdbc.validityCheckStatement";

   /**
    *  If this property is present, and the <code>pool.maidThreadCheckInterval</code>
    *  property is also present, then a thread will be created that looks for connections
    *  that have been idle for more than <code>pool.maxConnectionIdleTime</code> seconds.
    *  When this thread finds them, it closed the connection and logs
    *  a warning with a stack trace of when the connection was checked out of the pool.
    *  This is primarily here as a debugging aid for finding places where connections
    *  are not getting close, and should not be used in a production environment
    */
   protected static final String CP_PROPS_MAXCONNECTIONIDLETIME = "pool.maxConnectionIdleTime";

   /** this is the number of seconds between attempts by the maid thread (if present) to find idle connections. */
   protected static final String CP_PROPS_MAIDTHREADCHECKINTERVAL = "pool.maidThreadCheckInterval";

   /** connection pool driver class */
   private static final String CP_DRIVER = "com.protomatter.jdbc.pool.JdbcConnectionPoolDriver";

   /** Protomatter connectionPool * */
   private static JdbcConnectionPool connectionPool = null;

   /**
    *  Default constructor.
    *
    * @exception  Exception Description of the Exception
    * @throws  Exception because of the <code>throws Exception</code> clause
    *                   of the  <code>init</code> method.
    */
   public ProtomatterConnectionProvider() throws Exception
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
      return DriverManager.getConnection(getPrefs().getConnectionPoolURL(),
         getPrefs().getUser(), getPrefs().getPassword());
   }


   /**
    *  Initialize the Protomatter connection pool.
    *
    * @throws  Exception if any error occurs
    */
   protected void init() throws Exception
   {
      Properties props = null;

      // initialization params are kept in a Hashtable
      Hashtable args = new Hashtable();

      // the underlying driver
      args.put("jdbc.driver", getPrefs().getJdbcDriver());

      // the URL to connect the underlyng driver with the server
      args.put("jdbc.URL", getPrefs().getJdbcURL());

      // these are properties that get passed to DriverManager.getConnection(...)
      Properties jdbcProperties = new Properties();
      jdbcProperties.put("user", getPrefs().getUser());
      jdbcProperties.put("password", getPrefs().getPassword());

      // set the jdbc connection properties;
      if ((props = getPrefs().getProperties()) != null)
      {
         for (Enumeration e = props.propertyNames(); e.hasMoreElements();)
         {
            String key = (String) e.nextElement();
            jdbcProperties.put(key, props.getProperty(key));
            getLog4JCategory().info("::init - JDBC property [" + key + "] = ["
               + props.getProperty(key) + "]");
         }
      }

      args.put("jdbc.properties", jdbcProperties);

      // now set the connection pool custom properties;
      // if the connectionPool properties object is null,
      // instance a new properties object anyway, to use default values;
      if ((props = getPrefs().getPoolProperties()) == null)
      {
         props = new Properties();
      }

      // use defaults values as specified into the documentation;
      setIntegerArg(args, props, CP_PROPS_INITIALSIZE, "0");
      setIntegerArg(args, props, CP_PROPS_MAXSIZE, "-1");
      setIntegerArg(args, props, CP_PROPS_GROWBLOCK, "1");
      setIntegerArg(args, props, CP_PROPS_CREATEWAITTIME, "0");
      setArg(args, props, CP_PROPS_VALIDITYCHECKSTATEMENT, null);
      setIntegerArg(args, props, CP_PROPS_MAXCONNECTIONIDLETIME, null);
      setIntegerArg(args, props, CP_PROPS_MAIDTHREADCHECKINTERVAL, null);

      // finally create the pool and we're ready to go!
      Class.forName(CP_DRIVER).newInstance();
      connectionPool = new JdbcConnectionPool(getLastToken(
               getPrefs().getConnectionPoolURL(), ":"), args);
   }


   /**
    *  Set a new Integer value into the input hashTable.
    *
    * @param  args     the hashTable where to put the value
    * @param  props    the properties object where to retrieve the value;
    * @param  key      the key value used to retrieve the value from the props object.
    *                  The same key is used to store the retrieved value into the args hashMap
    * @param  defValue the default value to use if the input key does not retrieve a valid value from the
    *                  props object
    */
   private final void setIntegerArg(Hashtable args, Properties props,
      String key, String defValue)
   {
      String value = props.getProperty(key, defValue);

      if (!Util.isNull(value))
      {
         args.put(key, new Integer(value));
         getLog4JCategory().info("::setIntegerArg - [" + key + "] = [" + value + "]");
      }
   }


   /**
    *  Set a new String value into the input hashTable.
    *
    * @param  args     the hashTable where to put the value
    * @param  props    the properties object where to retrieve the value;
    * @param  key      the key value used to retrieve the value from the props object.
    *                  The same key is used to store the retrieved value into the args hashMap
    * @param  defValue the default value to use if the input key does not retrieve a valid value from the
    *                  props object
    */
   private final void setArg(Hashtable args, Properties props, String key,
      String defValue)
   {
      String value = props.getProperty(key, defValue);

      if (!Util.isNull(value))
      {
         args.put(key, value);
         getLog4JCategory().info("::setArg - [" + key + "] = [" + value + "]");
      }
   }
}
