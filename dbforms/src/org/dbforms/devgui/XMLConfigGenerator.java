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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.dbforms.devgui;


import java.sql.*;
import java.io.*;
import java.util.*;



public class XMLConfigGenerator {

  protected static Connection createConnection(String jdbcDriver, String jdbcURL ,String  username,String  password)
  throws SQLException, ClassNotFoundException, InstantiationException, IOException, IllegalAccessException {

	  Class.forName(jdbcDriver).newInstance();

	  return DriverManager.getConnection(jdbcURL, username, password);
  }

  // nope, i won't use DOM for that task ;-)
  public static String createXMLOutput(ProjectData projectData) throws Exception {

	  String jdbcDriver = projectData.getProperty("jdbcDriver");
	  String jdbcURL = projectData.getProperty("jdbcURL");
	  String username = projectData.getProperty("username");
	  String password = projectData.getProperty("password");

	  String catalog = projectData.getProperty("catalog");
	  String schemaPattern = projectData.getProperty("schemaPattern");
	  String tableNamePattern = projectData.getProperty("tableNamePattern");

	  if(catalog != null && catalog.trim().equalsIgnoreCase("$null"))
	  	catalog = null;

	  if(schemaPattern != null && schemaPattern.trim().equalsIgnoreCase("$null"))
	  	schemaPattern = null;

	  if(tableNamePattern != null && tableNamePattern.trim().equalsIgnoreCase("$null"))
	  	tableNamePattern = null;

	  System.out.println(": Retrieving metadata using the following properties ");
	  System.out.println("-----------------------------------------------------");
	  System.out.println("jdbcDriver="+jdbcDriver);
	  System.out.println("jdbcURL="+jdbcURL);
	  System.out.println("username="+username);
	  System.out.println("password="+password);
	  System.out.println("catalog="+catalog);
	  System.out.println("schemaPattern="+schemaPattern);
	  System.out.println("tableNamePattern="+schemaPattern);

	StringBuffer result = new StringBuffer();

    Connection con = null;
	try {

		con = createConnection(jdbcDriver, jdbcURL, username, password);

		result.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n\n<dbforms-config>\n");

		if(con==null) {System.exit(1);}

		DatabaseMetaData dbmd = con.getMetaData();
		String[] types = {"TABLE", "VIEW"};
		ResultSet tablesRS = dbmd.getTables(catalog, schemaPattern, tableNamePattern, types);

		while(tablesRS.next()) {

		  String tableName = tablesRS.getString(3);

		  result.append("\t<table name=\"");
		  result.append(tableName);
		  result.append("\">\n");

		  ResultSet rsKeys = dbmd.getPrimaryKeys(catalog, schemaPattern, tableName);
		  Vector keys = new Vector();
		  while(rsKeys.next()) {
		      String columnName = rsKeys.getString(4);
			  keys.addElement(columnName);
		  }
		  //rsKeys.close();

		  ResultSet rsFields = dbmd.getColumns(catalog, schemaPattern, tableName, null);
		  while(rsFields.next()) {

		      String columnName = rsFields.getString(4);
		   	  short dataType = rsFields.getShort(5);
		   	  String typeName = rsFields.getString(6);
		   	  int columnSize = rsFields.getInt(7);
		   	  String isNullable = rsFields.getString(18);

			  result.append("\t\t<field name=\"");
			  result.append(columnName);
			  result.append("\" fieldType=\"");
			  result.append(typeName.toLowerCase());
			  result.append("\" size=\"");
			  result.append(columnSize);
			  result.append("\"");
			  if(keys.contains(columnName)) {
			    result.append(" isKey=\"true\"");
			  }
			  result.append("/>\n");
		  }
		  //rsFields.close();

		  result.append("\n\t\t<!-- add \"granted-privileges\" element for security constraints -->\n\n\t</table>\n\n");
		}
		tablesRS.close();

		result.append("\t<!-- ========== Connection =================================== -->\n");
		result.append("\t<!--\n");
		result.append("\tuncomment this if you have access to JNDI of an application server (see users guide for more info)\n");
		result.append("\t<dbconnection\n");
		result.append("\t\tname = \"jdbc/dbformstest\"\n");
		result.append("\t\tisJndi = \"true\"\n");
		result.append("\t/>\n");
		result.append("\t-->\n\n");

		result.append("\t<dbconnection\n");
		result.append("\t\tname   = \""+jdbcURL+"\"\n");
		result.append("\t\tisJndi = \"false\"\n");
		result.append("\t\tconClass  = \""+jdbcDriver+"\"\n");
		result.append("\t\tusername = \""+username+"\"\n");
		result.append("\t\tpassword  = \""+password+"\"\n");
		result.append("\t/>\n");
		result.append("</dbforms-config>");

		System.out.println("finished");

	} catch(Exception e) {
		e.printStackTrace();
		throw new Exception(e.getMessage()+" in XMLConfigGenerator");
	} finally {
		try {
			if(con!=null) con.close();
		}	catch(SQLException sqle) {
			throw new SQLException("could not close Connection - " + sqle.getMessage());
		}
	}

	return result.toString();
  }

}