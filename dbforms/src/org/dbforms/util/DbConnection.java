/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * DbForms - a Rapid Application Development Framework
 * Copyright (C) 2001 Joachim Peer <j.peer@gmx.net> et al.
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

import com.pow2.dao.ConnectionFactory;
import com.pow2.dao.ConnectionProviderPrefs;

/**
 * <p>
 * this class represents datastructures like to following examples:
 * </p>
 * <pre>
 *
 *  &lt;dbconnection
 *   	name = "jdbc/dbformstest"
 *		isJndi = "true"
 *  /&gt;
 *  </pre>
 *<p>(in the example above dbforms asumes that the jndi-entry "jdbc/dbformstest" is correctly configured in
 *the application-server's database configuration [i.e. date-sources.xml])</p>
 *
 * <p> or:</p>
 * <pre>
 *  &lt;dbconnection
 *  	name = "jdbc:poolman://dbformstest"
 *		isJndi = "false"
 *  	class = "com.codestudio.sql.PoolMan"
 *  /&gt;
 *</pre>
 *<p>
 *	(in the example above dbforms asumes that the connectionpool-entry "dbformstest" is correctly configured in
 *	the associated connection pool properties file)
 *
 *	as these examples show, the configuration of datasources is beyond the scope of dbforms. that is
 *	a task of the underlying applicationserver/jsp-engine!
 * </p>
 *
 * @author  Joe Peer <j.peer@gmx.net>
 * @created  25 agosto 2002
 * @version  0.5
 * @version  0.8.3 Kevin Dangoor <kdangoor@webelite.com> added Username and Passwort properties
 */

public class DbConnection {
	private static final String CONNECTION_FACTORY_CLASS = "com.pow2.dao.ConnectionFactory";

	static Category logCat = Category.getInstance(DbConnection.class.getName());

	private ConnectionFactory connectionFactory = ConnectionFactory.instance();
	private String id;
	private String name;
	private String isJndi = "false";
	private boolean jndi = false;
	private boolean defaultConnection = false;
	private String conClass;
	private String username;
	private String password;
	private Properties properties;
	private boolean isPropSetup = false;
	private String connectionProviderClass;
	private String connectionPoolURL;
	private String isPow2 = "false";
	private boolean pow2 = false;
	private boolean isFactorySetup = false;

	public DbConnection() {
		properties = new java.util.Properties();
	}

	/**
	 *  Adds a new proptery - used while parsing XML file
	 */
	public void addProperty(DbConnectionProperty prop) {
		properties.put(prop.getName(), prop.getValue());
	}

	/**
	 *  Sets the connectionProviderClass attribute of the DbConnection object
	 *
	 * @param  cpc The new connectionProviderClass value
	 */
	public void setConnectionProviderClass(String cpc) {
		connectionProviderClass = cpc;
	}

	/**
	 *  Sets the defaultConnection attribute of the DbConnection object
	 *
	 * @param  defaultConnection The new defaultConnection value
	 */
	public void setDefaultConnection(boolean defaultConnection) {
		this.defaultConnection = defaultConnection;
	}

	/**
	 *  Gets the connectionProviderClass attribute of the DbConnection object
	 *
	 * @return  The connectionProviderClass value
	 */
	public String getConnectionProviderClass() {
		return connectionProviderClass;
	}

	/**
	 *  Sets the connectionPoolURL attribute of the DbConnection object
	 *
	 * @param  url The new connectionPoolURL value
	 */
	public void setConnectionPoolURL(String url) {
		connectionPoolURL = url;
	}

	/**
	 *  Gets the connectionPoolURL attribute of the DbConnection object
	 *
	 * @return  The connectionPoolURL value
	 */
	public String getConnectionPoolURL() {
		return connectionPoolURL;
	}

	/**
	 *  Sets the isPow2 attribute of the DbConnection object
	 *
	 * @param  isPow2 The new isPow2 value
	 */
	public void setIsPow2(String isPow2) {
		this.isPow2 = isPow2;

		pow2 = new Boolean(isPow2).booleanValue();
	}

	/**
	 *  Sets the id attribute of the DbConnection object
	 *
	 * @param  id The new id value
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 *  Gets the id attribute of the DbConnection object
	 *
	 * @return  The id value
	 */
	public String getId() {
		return id;
	}

	/**
	 *  Gets the defaultConnection attribute of the DbConnection object
	 *
	 * @return  The defaultConnection value
	 */
	public boolean isDefaultConnection() {
		return defaultConnection;
	}

	/**
	 *  Sets the name attribute of the DbConnection object
	 *
	 * @param  name The new name value
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 *  Gets the name attribute of the DbConnection object
	 *
	 * @return  The name value
	 */
	public String getName() {
		return name;
	}

	/**
	 *  Sets the isJndi attribute of the DbConnection object
	 *
	 * @param  isJndi The new isJndi value
	 */
	public void setIsJndi(String isJndi) {
		this.isJndi = isJndi;
		jndi = "true".equalsIgnoreCase(isJndi);
	}

	/**
	 *  Description of the Method
	 *
	 * @return  Description of the Return Value
	 */
	public String getisJndi() {
		return isJndi;
	}

	/**
	 *  Sets the conClass attribute of the DbConnection object
	 *
	 * @param  conClass The new conClass value
	 */
	public void setConClass(String conClass) {
		this.conClass = conClass;
	}

	/**
	 *  Gets the conClass attribute of the DbConnection object
	 *
	 * @return  The conClass value
	 */
	public String getConClass() {
		return conClass;
	}

	/**
	 *  Gets the username attribute of the DbConnection object
	 *
	 * @return  The username value
	 */
	public String getUsername() {
		return username;
	}

	/**
	 *  Sets the username attribute of the DbConnection object
	 *
	 * @param  newuser The new username value
	 */
	public void setUsername(String newuser) {
		this.username = newuser;
	}

	/**
	 *  Gets the password attribute of the DbConnection object
	 *
	 * @return  The password value
	 */
	public String getPassword() {
		return password;
	}

	/**
	 *  Sets the password attribute of the DbConnection object
	 *
	 * @param  newpass The new password value
	 */
	public void setPassword(String newpass) {
		this.password = newpass;
	}

	/**
	 *  Gets the connection attribute of the DbConnection object
	 *
	 * @return  The connection value
	 */
	public Connection getConnection() {
		Connection con = null;

		logCat.debug("returning a connection:" + this.toString());

		// access Connection via Application Server's JNDI table
		if (jndi) {
			try {
				Context ctx = new InitialContext();
				DataSource ds = (DataSource) ctx.lookup(name);
				con = ds.getConnection();
			} catch (NamingException ne) {
				ne.printStackTrace();
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
   	// access the connection using the pow2 library by Luca Fossato.
		} else if (pow2) {
			try {
				if (!isFactorySetup) {
					setupConnectionFactory();
				}
				con = connectionFactory.getConnection();
			} catch (Exception se) {
				logCat.error("::getConnection - cannot retrieve " + "a connection from the " + "connectionFactory", se);
				return null;
			}
		// access connection directly from db or from a connectionpool-manager like "Poolman"
		} else
			try {
				Class.forName(conClass).newInstance();
				if (!properties.isEmpty()) {
               if (!isPropSetup) {
                  properties.put("user", getUsername());
                  properties.put("password", getPassword());
                  isPropSetup = true;
               }
					con = DriverManager.getConnection(name, properties);
				} else if (username != null) {
					con = DriverManager.getConnection(name, username, password);
				} else {
					con = DriverManager.getConnection(name);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		return con;
	}

	/**
	 *  Set up the ConnectionFactory
	 *
	 * @exception  Exception Description of the Exception
	 */
	public void setupConnectionFactory() throws Exception {
		ConnectionProviderPrefs prefs = new ConnectionProviderPrefs();

		prefs.setConnectionProviderClass(connectionProviderClass);
		prefs.setConnectionPoolURL(connectionPoolURL);
		prefs.setJdbcDriver(conClass);
		prefs.setJdbcURL(name);
		prefs.setUser(username);
		prefs.setPassword(password);
 		prefs.setProperties(properties);
		connectionFactory.setProvider(prefs);
		isFactorySetup = true;
	}

	/**
	 *  Description of the Method
	 *
	 * @return  Description of the Return Value
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer("DbConnection = ");
		buf.append("id=" + id);
		buf.append(",name=" + name);
		buf.append(",jndi=" + isJndi);
		buf.append(",conClass=" + conClass);
		buf.append(",username=" + username);
		buf.append(",default=" + defaultConnection);
		if (pow2) {
			buf.append(",connectionProviderClass" + connectionProviderClass);
			buf.append(",connectionPoolURL" + connectionPoolURL);
		}
		if (!properties.isEmpty())
   		buf.append(properties);
		//buf.append(",password="+password);  Not such a good idea!
		return buf.toString();
	}
}
