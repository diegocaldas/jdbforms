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

package org.dbforms.devgui;


import java.sql.*;
import java.io.*;
import java.util.*;



public class XMLConfigGenerator implements PropertyNames  {

/* changes 2002-03-04 dikr:
 * - 
 * - changed big loop while reading meta data into two smaller ones
 * - fixed bug that could make problems if several tables in different
 *    schemas have same name
 * - added several options the user now has in ConfigFilePanel (see comments
 *    there)
 */
    
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

          
                                         // create boolean variables out of String properties:
          
          boolean includeCatalog = projectData.getProperty(INCLUDE_CATALOGNAME).
                                                            equalsIgnoreCase(TRUESTRING);
          boolean includeSchema = projectData.getProperty(INCLUDE_SCHEMANAME).
                                                            equalsIgnoreCase(TRUESTRING);          
          boolean useAutoCommitMode = projectData.getProperty(AUTOCOMMIT_MODE).
                                                            equalsIgnoreCase(TRUESTRING);

                                        // create array of  table types that have to be examined...
          
          Vector typesVec = new Vector();
          if (projectData.getProperty(EXAMINE_TABLES).equalsIgnoreCase(TRUESTRING))
               typesVec.add("TABLE");
          if (projectData.getProperty(EXAMINE_VIEWS).equalsIgnoreCase(TRUESTRING))
               typesVec.add("VIEW");
          if (projectData.getProperty(EXAMINE_SYSTABS).equalsIgnoreCase(TRUESTRING))
               typesVec.add("SYSTEM TABLE");  
          String[] types = new String[typesVec.size()];
          for (int jj=0;jj < typesVec.size();jj++) {
               types[jj] = (String)typesVec.get(jj);
          }

                            // set values for catalog, schema and table name according to properties:
          
          String catalog = projectData.getProperty(CATALOG_SELECTION).equalsIgnoreCase(ALL) ?
                                            null : projectData.getProperty(CATALOG);
                         
          String schemaPattern = projectData.getProperty(SCHEMA_SELECTION).equalsIgnoreCase(ALL) ?
                                            null : projectData.getProperty(SCHEMA);
                                 
          String tableNamePattern  = projectData.getProperty(TABLE_SELECTION).equalsIgnoreCase(ALL) ?
                                            null : projectData.getProperty(TABLE_NAME_PATTERN);
                
 
	  System.out.println(": Retrieving metadata using the following properties ");
	  System.out.println("-----------------------------------------------------");
	  System.out.println("jdbcDriver="+jdbcDriver);
	  System.out.println("jdbcURL="+jdbcURL);
	  System.out.println("username="+username);
	  System.out.println("password="+password);
	  System.out.println("catalog="+catalog);
	  System.out.println("schemaPattern="+schemaPattern);
	  System.out.println("tableNamePattern="+tableNamePattern);

          
          
        StringBuffer result = new StringBuffer();

        boolean showWarning                = false;
        int            catalogNameFailure   = 0;
        int            schemaNameFailure   = 0;
        StringBuffer warningMessage    = new StringBuffer("<html><ul>");
        
	Connection con = null;
	try {

		con = createConnection(jdbcDriver, jdbcURL, username, password);
                
               
		result.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n\n<dbforms-config>\n");

		DatabaseMetaData dbmd = con.getMetaData();
                
                                                        // if user wants to include catalog names in table names,
                                                        // try to check if DBMS supports this feature:
                if (includeCatalog) {
                    boolean supportsCatalogInDML = false;
                    try {                      // this is not mission critical, so own try block 
                        supportsCatalogInDML = dbmd.supportsCatalogsInDataManipulation();
                    } catch (Exception ignored){}
                    if (! supportsCatalogInDML) {
                        showWarning = true;
                        warningMessage.append("<li>Your database system does not seem to support use of  <br>" +
                                                                         "  catalog names in data manipulation statements. You should<br> " +
                                                                         "  better not include catalog names in table names.");
                    }
                }

                                                        // if user wants to includeschema names in table names,
                                                        // try to check if DBMS supports this feature:
                if (includeSchema) { 
                    boolean supportsSchemaInDML = false;
                    try {                      // this is not mission critical, so own try block 
                        supportsSchemaInDML = dbmd.supportsSchemasInDataManipulation();
                    } catch (Exception ignored){}
                    if (! supportsSchemaInDML) {
                        showWarning = true;
                        warningMessage.append("<li>Your database system does not seem to support use of <br>" +
                                                                         "  schema names in data manipulation statements. You should <br>" +
                                                                         "  better not include schema names in table names.");
                    }
                }                
                                   // user wants transaction mode, but it is not supported by 
                                   // dbms => warning and reset to autocommit mode:
                if ( (! useAutoCommitMode ) && (! dbmd.supportsTransactions())) {
                    showWarning = true;
                    warningMessage.append("<li>Transaction mode not supported by DBMS, connection is <br>" +
                                                                     "    automatically set to autocommit mode.");
                    useAutoCommitMode = true;
                }
                                  // select transaction mode if desired by user and supported:
                if (! useAutoCommitMode) con.setAutoCommit(false);

                                   // try to read catalog separator from DBMS, if needed:
                String catalogSeparator = ".";   // just in case reading value from DB fails
                if (includeCatalog) {
                    try {
                      catalogSeparator = dbmd.getCatalogSeparator();
                    } catch (SQLException ex) {
                       showWarning = true;
                       warningMessage.append("<li>Error reading catalog separator from database:   <br>" +
                                                                           ex.getMessage() + "<br>" +
                                                                      "  Using default '" + catalogSeparator + "' instead.");
                    }
                }
                
                String schemaSeparator= ".";    // isn't that default ? 
                

                                                                            // read table, catalog and schema names into vectors
                                                                            // to avoid big nested loop...
                Vector tableNames      = new Vector();
                Vector catalogNames  = new Vector();
                Vector schemaNames = new Vector();
              
                try {
                    ResultSet tablesRS = dbmd.getTables(catalog, schemaPattern, tableNamePattern, types);               

		    while(tablesRS.next()) {
                       catalogNames.add(tablesRS.getString(1));
                       schemaNames.add(tablesRS.getString(2));
                       tableNames.add(tablesRS.getString(3));
                    }
                    tablesRS.close();
                
                } catch (SQLException ex) {
                       showWarning = true;
                       warningMessage.append("<li>Error while trying to read table names with <br>" +
                                                                            "  catalog=" + catalog +
                                                                            ",<br>   schemapattern=" + schemaPattern +
                                                                            ",<br>   tableNamePattern=" + tableNamePattern + 
                                                                             "<br> from database.   <br>Error message:" +
                                                                           ex.getMessage() + "<br>");
                }
                
                
                if (! useAutoCommitMode) con.commit();
                
                if (tableNames.size() == 0) {
                    showWarning = true;
                    warningMessage.append("<li> No tables of type <br>(");
                    for (int i=0 ; i < types.length;i++) warningMessage.append("'").append(types[i]).append("' ");
                    warningMessage.append(") <br>found with catalog='" + catalog +"', schemapattern='" +
                                                                     schemaPattern + "',<br>tablename pattern='" + tableNamePattern +"'");
                }
                
                for (int i = 0; i < tableNames.size();i++ ) {
                   
                    String catalogName  =  (String)catalogNames.get(i);
                    String schemaName =  (String)schemaNames.get(i);
                    String tableName      =  (String)tableNames.get(i);

		  result.append("\t<table name=\"");
                  
                  
                                                        // prepend catalog and schema names to table names
                                                        // if desired by user and not empty:
                  if ( includeCatalog && (catalogName != null) && ( ! catalogName.equalsIgnoreCase("")) ) 
                       result.append(catalogName.trim()).append(catalogSeparator);
                  if (includeCatalog && ((catalogName == null) || catalogName.equalsIgnoreCase(""))) 
                       catalogNameFailure++;
                       
                  if (includeSchema && (schemaName != null) && ( ! schemaName.equalsIgnoreCase("")) )
                      result.append(schemaName.trim()).append(schemaSeparator);
                  if (includeSchema && ((schemaName == null) || schemaName.equalsIgnoreCase("")) )
                      schemaNameFailure++;
                  
		  result.append(tableName);
		  result.append("\">\n");

		  ResultSet rsKeys = dbmd.getPrimaryKeys(catalogName, schemaName, tableName);
		  Vector keys = new Vector();
		  while(rsKeys.next()) {
		      String columnName = rsKeys.getString(4);
		      keys.addElement(columnName);
		  }
		  rsKeys.close();

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
		  rsFields.close();
                  if (! useAutoCommitMode) con.commit();
		  result.append("\n\t\t<!-- add \"granted-privileges\" element for security constraints -->\n\n\t</table>\n\n");
		}
		
               if (catalogNameFailure > 0) {
                      showWarning = true;
                      warningMessage.append("<li> " + catalogNameFailure + " empty catalog names not " +
                                                                               "included in table name.");
                }

                if (schemaNameFailure > 0) {
                      showWarning = true;
                      warningMessage.append("<li> " + schemaNameFailure + " empty schema names not " +
                                                                               "included in table name.");
                }   
                  
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
                warningMessage.append("</ul></html>");
	} catch(Exception e) {
		e.printStackTrace();
		throw new Exception(e.getMessage()+" in XMLConfigGenerator");
	} finally {
		try {

                        if (showWarning)
                          javax.swing.JOptionPane.showMessageDialog(null, warningMessage,"Warning", 
                                                                                                          javax.swing.JOptionPane.WARNING_MESSAGE);
	               
			if(con!=null) con.close();
		}	catch(SQLException sqle) {
			throw new SQLException("could not close Connection - " + sqle.getMessage());
		}
	}

	return result.toString();
  }  

}