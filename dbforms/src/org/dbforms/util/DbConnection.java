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

package org.dbforms.util;

import java.util.*;
import java.sql.*;
import javax.sql.*;
import javax.naming.*;

import org.apache.log4j.Category;

import org.dbforms.conprovider.ConnectionFactory;
import org.dbforms.conprovider.ConnectionProviderPrefs;



/**
 * This class represents datastructures like to following examples:
 * <br>
 * <pre>
 *
 *  &lt;dbconnection
 *           name   = "jdbc/dbformstest"
 *           isJndi = "true"
 *  /&gt;
 * </pre>
 * <p>
 *   (in the example above dbforms asumes that the jndi-entry "jdbc/dbformstest"
 *   is correctly configured in the application-server's database configuration
 *   [i.e. date-sources.xml])
 * </p>
 * <p>
 *   or:
 * </p>
 * <pre>
 *  &lt;dbconnection
 *          name   = "jdbc:poolman://dbformstest"
 *          isJndi = "false"
 *          class  = "com.codestudio.sql.PoolMan"
 *  /&gt;
 * </pre>
 * <p>
 *   (in the example above dbforms asumes that the connectionpool-entry "dbformstest"
 *   is correctly configured in the associated connection pool properties file).
 * </p>
 * <p>
 *   As these examples show, the configuration of datasources is beyond the scope of dbforms.
 *   That is a task of the underlying applicationserver/jsp-engine!
 * </p>
 *
 * @author  Joe Peer <j.peer@gmx.net>
 * @created  25 agosto 2002
 * @version  0.5
 * @version  0.8.3 Kevin Dangoor <kdangoor@webelite.com> added Username and Passwort properties
 */
public class DbConnection
{
    /** log4j category */
    static Category logCat = Category.getInstance(DbConnection.class.getName());

    /** connection factory instance */
    private ConnectionFactory connectionFactory = ConnectionFactory.instance();

    /** connection id */
    private String id;

    /** connection name */
    private String name;

    /** JNDI flag */
    private String isJndi = "false";

    /** JNDI flag. Sei it to true to get connection objects from a JNDI service */
    private boolean jndi = false;

    /** default connection flag */
    private boolean defaultConnection = false;

    /** JDBC drivermanager class */
    private String conClass;

    /** database user name */
    private String username;

    /** database password */
    private String password;

    /** JDBC properties */
    private Properties properties;

    /** connection pool properties */
    private Properties poolProperties;

    /** */
    private boolean isPropSetup = false;

    /** connection provider class */
    private String connectionProviderClass;

    /** connection provider URL */
    private String connectionPoolURL;

    /** connection factory flag */
    private String isPow2 = "false";

    /**
     * connection factory flag. Set it to true to use connection factory
     * to get JDBC connection objects
     */
    private boolean pow2 = false;

    /**
     *  connection factory configuration flag. If true,
     *  the connection factory is already configured
     */
    private boolean isFactorySetup = false;


    /**
     *  Constructor.
     */
    public DbConnection()
    {
        properties = new java.util.Properties();
        poolProperties = new java.util.Properties();
    }

    /**
     *  Adds a new property - used while parsing XML file
     *
     * @param  prop The feature to be added to the Property attribute
     */
    public void addProperty(DbConnectionProperty prop)
    {
        properties.put(prop.getName(), prop.getValue());
    }


    /**
     *  Adds a new pool property - used while parsing XML file
     *
     * @param  prop The feature to be added to the PoolProperty attribute
     */
    public void addPoolProperty(DbConnectionProperty prop)
    {
        poolProperties.put(prop.getName(), prop.getValue());
    }


    /**
     *  Sets the connectionProviderClass attribute of the DbConnection object
     *
     * @param  cpc The new connectionProviderClass value
     */
    public void setConnectionProviderClass(String cpc)
    {
        connectionProviderClass = cpc;
    }


    /**
     *  Sets the defaultConnection attribute of the DbConnection object
     *
     * @param  defaultConnection The new defaultConnection value
     */
    public void setDefaultConnection(boolean defaultConnection)
    {
        this.defaultConnection = defaultConnection;
    }


    /**
     *  Gets the connectionProviderClass attribute of the DbConnection object
     *
     * @return  The connectionProviderClass value
     */
    public String getConnectionProviderClass()
    {
        return connectionProviderClass;
    }


    /**
     *  Sets the connectionPoolURL attribute of the DbConnection object
     *
     * @param  url The new connectionPoolURL value
     */
    public void setConnectionPoolURL(String url)
    {
        connectionPoolURL = url;
    }


    /**
     *  Gets the connectionPoolURL attribute of the DbConnection object
     *
     * @return  The connectionPoolURL value
     */
    public String getConnectionPoolURL()
    {
        return connectionPoolURL;
    }


    /**
     *  Sets the isPow2 attribute of the DbConnection object
     *
     * @param  isPow2 The new isPow2 value
     */
    public void setIsPow2(String isPow2)
    {
        this.isPow2 = isPow2;

        pow2 = new Boolean(isPow2).booleanValue();
    }


    /**
     *  Sets the id attribute of the DbConnection object
     *
     * @param  id The new id value
     */
    public void setId(String id)
    {
        this.id = id;
    }


    /**
     *  Gets the id attribute of the DbConnection object
     *
     * @return  The id value
     */
    public String getId()
    {
        return id;
    }


    /**
     *  Gets the defaultConnection attribute of the DbConnection object
     *
     * @return  The defaultConnection value
     */
    public boolean isDefaultConnection()
    {
        return defaultConnection;
    }


    /**
     *  Sets the name attribute of the DbConnection object
     *
     * @param  name The new name value
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     *  Gets the name attribute of the DbConnection object
     *
     * @return  The name value
     */
    public String getName()
    {
        return name;
    }


    /**
     *  Sets the isJndi attribute of the DbConnection object
     *
     * @param  isJndi The new isJndi value
     */
    public void setIsJndi(String isJndi)
    {
        this.isJndi = isJndi;
        jndi = "true".equalsIgnoreCase(isJndi);
    }


    /**
     *  Description of the Method
     *
     * @return  Description of the Return Value
     */
    public String getisJndi()
    {
        return isJndi;
    }


    /**
     *  Sets the conClass attribute of the DbConnection object
     *
     * @param  conClass The new conClass value
     */
    public void setConClass(String conClass)
    {
        this.conClass = conClass;
    }


    /**
     *  Gets the conClass attribute of the DbConnection object
     *
     * @return  The conClass value
     */
    public String getConClass()
    {
        return conClass;
    }


    /**
     *  Gets the username attribute of the DbConnection object
     *
     * @return  The username value
     */
    public String getUsername()
    {
        return username;
    }


    /**
     *  Sets the username attribute of the DbConnection object
     *
     * @param  newuser The new username value
     */
    public void setUsername(String newuser)
    {
        this.username = newuser;
    }


    /**
     *  Gets the password attribute of the DbConnection object
     *
     * @return  The password value
     */
    public String getPassword()
    {
        return password;
    }


    /**
     *  Sets the password attribute of the DbConnection object
     *
     * @param  newpass The new password value
     */
    public void setPassword(String newpass)
    {
        this.password = newpass;
    }


    /**
     *  Gets a JDBC connection object.
     *
     * @return  the connection object, or null if any error occurs
     */
    public Connection getConnection()
    {
        Connection con = null;

        // get connection via Application Server's JNDI table;
        // name attribute is used as JNDI lookup string;
        if (jndi)
          con = getConnectionFromJNDI(name);

        // get connection using the connection factory library
        // by Luca Fossato <fossato@pow2.com>
        else if (pow2)
          con = getConnectionFromFactory();

        // get connection from DriverManager
        else
          con = getConnectionFromDriverManager();

        return con;
    }


    /**
     *  Gets the string representation of this object.
     *
     * @return  the string representation of this object
     */
    public String toString()
    {
        StringBuffer buf = new StringBuffer("DbConnection = ");

        buf.append("id="         + id)
           .append(", name="     + name)
           .append(", jndi="     + isJndi)
           .append(", conClass=" + conClass)
           .append(", username=" + username)
           .append(", default="  + defaultConnection);

        if (pow2)
            buf.append(", connectionProviderClass=" + connectionProviderClass)
               .append(", connectionPoolURL="       + connectionPoolURL);

        if (!properties.isEmpty())
            buf.append(", jdbc properties: ")
               .append(properties);


        if (!poolProperties.isEmpty())
            buf.append(", connection pool properties: ")
               .append(poolProperties);

        //buf.append(",password="+password);  Not such a good idea!
        return buf.toString();
    }




    /**
     *  PRIVATE METHODs here
     */


    /**
     *  Gets a JDBC connection object from a JNDI server.
     *
     * @param  lookupString the string used to lookup the
     *          datasource object from the JNDI server
     * @return  the JDBC connection object, or null if the lookup fails
     */
    private Connection getConnectionFromJNDI(String lookupString)
    {
        Connection con = null;

        // a useful source of examples for Tomcat 4.1:
        // http://jakarta.apache.org/tomcat/tomcat-4.1-doc/jndi-datasource-examples-howto.html
        try
        {
            Context ctx = new InitialContext();

            if (ctx != null)
            {
                DataSource ds = (DataSource) ctx.lookup(lookupString);

                if (ds != null)
                    con = ds.getConnection();
                else
                    logCat.error("::getConnectionFromJNDI - DataSource object is null");
            }
            else
            {
                logCat.error("::getConnectionFromJNDI - no context object avaiable");
            }
        }
        catch (NamingException ne)
        {
            logCat.error("::getConnectionFromJNDI - cannot retrieve a connection from JNDI:", ne);
        }
        catch (Exception e)
        {
            logCat.error("::getConnectionFromJNDI - exception:", e);
        }

        return con;
    }


    /**
     *  Gets a JDBC connection object from the connection factory.
     *
     * @return  the JDBC connection object, or null if any error occurs
     */
    private Connection getConnectionFromFactory()
    {
        Connection con = null;

        try
        {
            if (!isFactorySetup)
            {
                setupConnectionFactory();
            }

            con = connectionFactory.getConnection();
        }
        catch (Exception se)
        {
            logCat.error("::getConnectionFromFactory - cannot retrieve a connection from the connectionFactory", se);
        }

        return con;
    }


    /**
     *  Gets a JDBC connection object from the DriverManager class
     *  specified by the conClass member attribute.
     *
     * @return  the JDBC connection object, or null if any error occurs
     */
    private Connection getConnectionFromDriverManager()
    {
        Connection con = null;

        try
        {
            Class.forName(conClass).newInstance();

            if (!properties.isEmpty())
            {
                if (!isPropSetup)
                {
                    properties.put("user", getUsername());
                    properties.put("password", getPassword());
                    isPropSetup = true;
                }

                con = DriverManager.getConnection(name, properties);
            }
            else if (username != null)
            {
                con = DriverManager.getConnection(name, username, password);
            }
            else
            {
                con = DriverManager.getConnection(name);
            }
        }
        catch (Exception e)
        {
            logCat.error("::getConnectionFromDriverManager - cannot retrieve a connection from DriverManager", e);
        }

        return con;
    }


    /**
     *  Set up the ConnectionFactory
     *
     * @exception  Exception if any error occurs
     */
    private void setupConnectionFactory() throws Exception
    {
        ConnectionProviderPrefs prefs = new ConnectionProviderPrefs();

        prefs.setConnectionProviderClass(connectionProviderClass);
        prefs.setConnectionPoolURL(connectionPoolURL);
        prefs.setJdbcDriver(conClass);
        prefs.setJdbcURL(name);
        prefs.setUser(username);
        prefs.setPassword(password);
        prefs.setProperties(properties);
        prefs.setPoolProperties(poolProperties);
        connectionFactory.setProvider(prefs);
        isFactorySetup = true;
    }
}
