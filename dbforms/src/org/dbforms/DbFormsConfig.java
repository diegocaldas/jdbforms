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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.itp.dbforms;

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.itp.dbforms.util.DbConnection;

import org.apache.log4j.Category;

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

public class DbFormsConfig {

  static Category logCat = Category.getInstance(DbFormsConfig.class.getName()); // logging category for this class

  public static final String CONFIG = "dbformsConfig";

  private Vector tables;
  private Hashtable tableNameHash; // for quicker lookup by name
  private DbConnection dbConnection;
  private ServletConfig servletConfig;

  public DbFormsConfig() {
		tables = new Vector();
		tableNameHash = new Hashtable();
  }

  public void addTable(Table table) {
		logCat.info("add table called");
		table.setId(tables.size());
		table.initDefaultOrder();
		tables.addElement(table);
		tableNameHash.put(table.getName(), table);
  }

	public Table getTable(int index) {
		return (Table) tables.elementAt(index);
	}

	public Table getTableByName(String name) {
		return (Table) tableNameHash.get(name);
	}


	public void setDbConnection(DbConnection dbConnection) {
		this.dbConnection = dbConnection;
		logCat.info("***** DBCONNECTION = "+dbConnection.toString()+"******");
	}

	public DbConnection getDbConnection() {
		return dbConnection;
	}


	public void setServletConfig(ServletConfig servletConfig) {
		this.servletConfig = servletConfig;
	}

	/**
	get access to configuration of config servlet
	*/
	public ServletConfig getServletConfig() {
		return servletConfig;
	}

	/**
	get access to servlet context in order to interoperate with
	other components of the web application
	*/
	public ServletContext getServletContext() {
		return servletConfig.getServletContext();
	}

  public String traverse() {
    StringBuffer buf = new StringBuffer();
    for(int i=0; i<tables.size(); i++) {
      Table t = (Table) tables.elementAt(i);
      buf.append("table:\n");
      buf.append(t.traverse());
		}
		return buf.toString();
  }



}