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

/****
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
 *
 * @author Joe Peer <j.peer@gmx.net>
 *
 * @version 0.5
 * @version 0.8.3 Kevin Dangoor <kdangoor@webelite.com> added Username and Passwort properties
 *
 */

public class DbConnection {

   static Category logCat = Category.getInstance(DbConnection.class.getName()); // logging category for this class

	private String name;
	private String isJndi;
	private boolean jndi;
	private String conClass;
	private String username;
	private String password;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setIsJndi(String isJndi) {
		this.isJndi = isJndi;
		jndi = "true".equalsIgnoreCase(isJndi);
	}

	public String getisJndi() {
		return isJndi;
	}

	public void setConClass(String conClass) {
		this.conClass = conClass;
	}

	public String getConClass() {
		return conClass;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String newuser) {
		this.username = newuser;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String newpass) {
		this.password = newpass;
	}

	public Connection getConnection() {

		logCat.debug("returning a connection:"+this.toString());


	  // access Connection via Application Server's JNDI table
	  if(jndi) {

	  try {
				Context ctx = new InitialContext();
				DataSource ds = (DataSource) ctx.lookup(name);
				Connection con = ds.getConnection();
				return con;
			} catch(NamingException ne) {
				ne.printStackTrace();
   	    return null;
	  	} catch(Exception e) {
				e.printStackTrace();
   	    return null;
	  	}

		// access connection directly from db or from a connectionpool-manager like "Poolman"
		} else {

	    try {
 	  		Class.forName(conClass).newInstance();
			if (username != null) {
				return DriverManager.getConnection(name, username, password);
			}
	  		return DriverManager.getConnection(name);
	  	} catch(Exception e) {
				e.printStackTrace();
   	    return null;
	  	}

		}
	}

  public String toString() {
	 StringBuffer buf = new StringBuffer("DbConnection = ");
	 buf.append("name="+name);
	 buf.append(",jndi="+isJndi);
	 buf.append(",conClass="+conClass);
	 buf.append(",username="+username);
	 buf.append(",password="+password);
	 return buf.toString();

  }  
}