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



/**
 *  Simple Connection provider.
 *  <br>
 *  provides non-pooled connections.
 */
public class SimpleConnectionProvider extends ConnectionProvider
{
    /**
     *  Default constructor.
     *
     * @exception  Exception Description of the Exception
     * @throws  Exception because of the <code>throws Exception</code> clause
     *          of the  <code>init</code> method.
     */
    public SimpleConnectionProvider() throws Exception
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
        Properties props = prefs.getProperties();
        Connection con = null;

        // uses custom jdbc properties;
        if ((props != null) && !props.isEmpty())
        {
            props.put("user", prefs.getUser());
            props.put("password", prefs.getPassword());
            con = DriverManager.getConnection(prefs.getJdbcURL(), props);
        }

        // "plain" flavour;
        else
        {
            con = DriverManager.getConnection(prefs.getJdbcURL(), prefs.getUser(), prefs.getPassword());
        }

        return con;
    }


    /**
     *  Initialize the ConnectionProvider.
     *
     * @throws  Exception if any error occurs
     */
    protected void init() throws Exception
    {
        Class.forName(prefs.getJdbcDriver()).newInstance();
    }
}
