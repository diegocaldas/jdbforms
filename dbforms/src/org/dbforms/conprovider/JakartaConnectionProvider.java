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
import org.apache.commons.dbcp.BasicDataSource;
import org.dbforms.util.Util;



/**
 *  Connection provider for Apache Jakarta commons-dbcp.
 *  <br>
 *  See <code>http://jakarta.apache.org/commons/components.html</code>
 *  for further informations.
 */
public class JakartaConnectionProvider extends ConnectionProvider
{
    protected final static String PROPS_VALIDATION_QUERY = "validationQuery";
    protected final static String PROPS_MAX_ACTIVE       = "maxActive";
    protected final static String PROPS_MAX_IDLE         = "maxIdle";
    protected final static String PROPS_MAX_WAIT         = "maxWait";
    protected final static String PROPS_USE_LOG          = "useLog";


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
        dataSource.setDriverClassName(prefs.getJdbcDriver());
        dataSource.setUrl(prefs.getJdbcURL());
        dataSource.setUsername(prefs.getUser());
        dataSource.setPassword(prefs.getPassword());

        // set the dataSource properties;
        if ((props = prefs.getProperties()) != null)
        {
            for (Enumeration e = props.propertyNames(); e.hasMoreElements();)
            {
                String key = (String) e.nextElement();
                dataSource.addConnectionProperty(key, props.getProperty(key));
                cat.info("::init - dataSource property [" + key + "] = [" + props.getProperty(key) + "]");
            }
        }

        // now set the connection pool custom properties;
        // if the connectionPool properties object is null,
        // instance a new properties object anyway, to use default values;
        if ((props = prefs.getPoolProperties()) == null)
            props = new Properties();

        String validationQuery = props.getProperty(PROPS_VALIDATION_QUERY, null);
        if (!Util.isNull(validationQuery))
          dataSource.setValidationQuery(validationQuery.trim());

        dataSource.setMaxActive (Integer.parseInt(props.getProperty(PROPS_MAX_ACTIVE, "20")));
        dataSource.setMaxIdle   (Integer.parseInt(props.getProperty(PROPS_MAX_IDLE,   "5")));
        dataSource.setMaxWait   (Long.parseLong  (props.getProperty(PROPS_MAX_WAIT,   "-1")));

        // if PROPS_LOG == true, use log4j category to log the datasource info;
        String useLog = props.getProperty(PROPS_USE_LOG, "false");
        if (!Util.isNull(useLog) && "true".equals(useLog.trim()))
        {
            cat.info("::init - dataSource log activated");
            dataSource.setLogWriter(new Log4jPrintWriter(cat, cat.getPriority()));
        }
    }
}
