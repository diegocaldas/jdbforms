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



/**
 *  Connection provider for Protomatter Connection pool.
 *  <br>
 *  See <code>http://protomatter.sourceforge.net/</code> for further informations.
 */
public class ProtomatterConnectionProvider extends ConnectionProvider
{
    /** connection pool driver class */
    private static final String POOL_DRIVER = "com.protomatter.jdbc.pool.JdbcConnectionPoolDriver";

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
        return DriverManager.getConnection(prefs.getConnectionPoolURL(), prefs.getUser(), prefs.getPassword());
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
        args.put("jdbc.driver", prefs.getJdbcDriver());

        // the URL to connect the underlyng driver with the server
        args.put("jdbc.URL", prefs.getJdbcURL());

        // these are properties that get passed to DriverManager.getConnection(...)
        Properties jdbcProperties = new Properties();

        jdbcProperties.put("user", prefs.getUser());
        jdbcProperties.put("password", prefs.getPassword());

        // set the connection properties;
        if ((props = prefs.getProperties()) != null)
        {
            for (Enumeration e = props.propertyNames(); e.hasMoreElements();)
            {
                String key = (String) e.nextElement();
                jdbcProperties.put(key, props.getProperty(key));
                cat.info("::init - JDBC property [" + key + "] = [" + props.getProperty(key) + "]");
            }
        }

        args.put("jdbc.properties", jdbcProperties);

        // the initial size of the pool.
        args.put("pool.initialSize", new Integer(5));

        // the maximum size the pool can grow to.
        args.put("pool.maxSize", new Integer(10));

        // each time the pool grows, it grows by this many connections
        args.put("pool.growBlock", new Integer(2));

        // between successive connections, wait this many milliseconds.
        args.put("pool.createWaitTime", new Integer(1000));

        // finally create the pool and we're ready to go!
        Class.forName(POOL_DRIVER).newInstance();
        connectionPool = new JdbcConnectionPool(getLastToken(prefs.getConnectionPoolURL(), ":"), args);
    }
}
