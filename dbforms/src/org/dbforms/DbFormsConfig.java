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

package org.dbforms;

import java.util.*;
import java.text.SimpleDateFormat;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.Category;

import org.dbforms.util.DbConnection;
import org.dbforms.util.Util;



/****
 * <p>
 * This class gets populated with data from the dbforms-config.xml file by the ConfigServlet.
 * This class is a kind of "single point of entry" for configuration data: it contains the
 * definitions of tables, fields, fieldtypes, keys etc. and even the definitions of data
 * sources (see dbforms-config.xml) Many components of the dbforms-frameworks use the data
 * stored in this class.
 * </p>
 *
 * @author Joe Peer <joepeer@excite.com>
 */
public class DbFormsConfig
{
    static Category logCat = Category.getInstance(DbFormsConfig.class.getName());

    /** DOCUMENT ME! */
    public static final String CONFIG = "dbformsConfig";
    private static SimpleDateFormat sdf;
    private Vector tables;

    /** for quicker lookup by name */
    private Hashtable tableNameHash;

    /** the default db connection */
    private DbConnection defaultDbConnection;

    /** contains connection put by addDbConnection */
    private ArrayList dbConnectionsList;
    private Hashtable dbConnectionsHash;
    private String realPath;

    //private DbConnection dbConnection;
    private ServletConfig servletConfig;


    /**
     * Creates a new DbFormsConfig object.
     */
    public DbFormsConfig(String realPath)
    {
        setRealPath(realPath);
        tables = new Vector();
        tableNameHash = new Hashtable();
        dbConnectionsHash = new Hashtable();
        dbConnectionsList = new ArrayList();
    }


    /**
     * DOCUMENT ME!
     *
     * @param table DOCUMENT ME!
     */
    public void addTable(Table table)
    {
        logCat.info("add table called");
        table.setId(tables.size());
        table.setConfig(this);
        table.initDefaultOrder();
        tables.addElement(table);
        tableNameHash.put(table.getName(), table);
    }


    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Table getTable(int index)
    {
		try {
			return (Table) tables.elementAt(index);
		} catch(Exception e) {
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
    public Table getTableByName(String name)
    {
		try {
	        return (Table) tableNameHash.get(name);
		} catch(Exception e) {
			return null;
		}
    }


    /**
     * DOCUMENT ME!
     *
     * @param dbConnection DOCUMENT ME!
     */
    public void addDbConnection(DbConnection dbConnection)
    {
        dbConnection.setName(Util.replaceRealPath(dbConnection.getName(), realPath));
        dbConnectionsList.add(dbConnection);

        //if ((dbConnection.getId() != null) && (dbConnection.getId().trim().length() > 0))
        if (!Util.isNull(dbConnection.getId()))
        {
            dbConnectionsHash.put(dbConnection.getId(), dbConnection);
        }

        // if a default connection does not exist yet,
        // use the input connection as the default one;
        if ((dbConnection.isDefaultConnection() && ((defaultDbConnection == null) || !defaultDbConnection.isDefaultConnection())) || (defaultDbConnection == null))
        {
            defaultDbConnection = dbConnection;
            dbConnection.setDefaultConnection(true);
        }

        logCat.info("::addDbConnection - added the dbConnection [" + dbConnection + "]");
    }


    /**
     * DOCUMENT ME!
     *
     * @param dbConnectionName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DbConnection getDbConnection(String dbConnectionName)
    {
        DbConnection connection = null;

        if ((dbConnectionName == null) || (dbConnectionName.trim().length() == 0))
        {
            return defaultDbConnection;
        }

        try
        {
            connection = (DbConnection) dbConnectionsList.get(Integer.parseInt(dbConnectionName));
        }
        catch (Exception ex)
        {
        }
        finally
        {
            if (connection != null)
            {
                return connection;
            }
        }

        connection = (DbConnection) dbConnectionsHash.get(dbConnectionName);

        if (connection == null)
        {
            connection = defaultDbConnection;
        }

        return connection;
    }


    /**
     * DOCUMENT ME!
     *
     * @param servletConfig DOCUMENT ME!
     */
    public void setServletConfig(ServletConfig servletConfig)
    {
        this.servletConfig = servletConfig;
    }


    /**
    get access to configuration of config servlet
    */
    public ServletConfig getServletConfig()
    {
        return servletConfig;
    }


    /**
     *  Get access to servlet context in order to interoperate with
     *  other components of the web application
     */
    public ServletContext getServletContext()
    {
        return servletConfig.getServletContext();
    }


    /**
     *  DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static SimpleDateFormat getDateFormatter()
    {
        if (sdf == null)
        {
            sdf = new SimpleDateFormat();
        }

        return sdf;
    }


    /**
     * DOCUMENT ME!
     *
     * @param pattern DOCUMENT ME!
     */
    public static void setDateFormatter(String pattern)
    {
        sdf = new SimpleDateFormat(pattern);
    }


    /**
     * DOCUMENT ME!
     *
     * @param tableID DOCUMENT ME!
     * @param fieldID DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFieldName(int tableID, int fieldID)
    {
        Table t = (Table) tables.elementAt(tableID);

        return (t.getFieldName(fieldID));
    }


    /**
     *  Returns the realPath.
     *
     * @return the realPath
     */
    public String getRealPath()
    {
        return realPath;
    }


    /**
     *  Sets the realPath.
     *
     * @param realPath The realPath to set
     */
    public void setRealPath(String realPath)
    {
        this.realPath = realPath.replace('\\', '/');
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String traverse()
    {
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < tables.size(); i++)
        {
            Table t = (Table) tables.elementAt(i);
            buf.append("table:\n");
            buf.append(t.traverse());
        }

        return buf.toString();
    }
}
