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
import org.dbforms.ForeignKey;
import org.dbforms.Reference;



public class XMLConfigGenerator implements PropertyNames  {
      
                                          // the following types are known inside class org.dbforms.Field:
static final String[] knownFieldTypes  = 
{   "tinyint","int","smallint","integer","bigint",  // mapped to INTEGER
    "float","real",				                              // mapped to FLOAT
    "double",					                               // mapped to DOUBLE
    "numeric","decimal","number",			// mapped to NUMERIC
    "char","varchar","longchar","nvarchar",      // mapped to CHAR
    "blob","image",					                // mapped to BLOB
    "diskblob",					                            // mapped to DISKBLOB
    "date",					                        	// mapped to DATE
    "timestamp"					                        // mapped to TIMESTAMP
};

static boolean fieldTypeIsKnown(String s) {
    int i;
    String sLower = s.toLowerCase();
    for (i=0; i< knownFieldTypes.length;i++) 
        if (s.startsWith( knownFieldTypes[i])) 
            return true;
    return false;
}

static private final int DBMS_MYSQL = 1;
static private final int DBMS_IBMDB2 = 2;
     

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

    public static HashMap getForeignKeyInformation(DatabaseMetaData dbmd,
                          boolean includeCatalog, String catalogSeparator,
                          boolean includeSchema,String schemaSeparator,
                          Vector knownTables) throws SQLException  {
      HashMap hm = new HashMap();
                                   // following method accepts no pattern, so we just pass nulls and read all
                                   // available information:
      ResultSet rsk = dbmd.getCrossReference(null, null, null, null,null,null ) ;
      
      while (rsk.next()) {
          String pCatalog = rsk.getString(1);
          String pSchema = rsk.getString(2);
          String pTable= rsk.getString(3);
          if ( ! knownTables.contains("" + pCatalog + "\t" + pSchema + "\t" + pTable)) continue; 
         
          String pColName = rsk.getString(4);
          
          String fCatalog = rsk.getString(5);
          String fSchema = rsk.getString(6);
          String fTable=rsk.getString(7);
          if ( ! knownTables.contains("" + fCatalog + "\t" + fSchema + "\t" + fTable)) continue;
          
          String fColName = rsk.getString(8);
          
          String fkName = rsk.getString(12);
 
          if (fkName == null)   // foreign key name not set, construct one....
              fkName = pCatalog + "::" + pSchema + "::" + pTable;  
          
          String hashKey = fCatalog + "\t" + fSchema + "\t" + fTable;
          
          HashMap tabForKeys = (HashMap)hm.get(hashKey);  // Hash with foreign key information for referencing table
          if (tabForKeys == null) {
              tabForKeys = new HashMap();
              hm.put(hashKey, tabForKeys);
          }
          ForeignKey fki = (ForeignKey)tabForKeys.get(fkName);
          if (fki == null) {
               fki = new ForeignKey();
               tabForKeys.put(fkName,fki);
               String fullTabName = "";
               if (includeCatalog) fullTabName = pCatalog + catalogSeparator;
               if (includeSchema) fullTabName +=  pSchema + schemaSeparator;
               fullTabName +=  pTable;
               fki.setForeignTable(fullTabName);
               fki.setName(fkName);              
          }
          fki.addReference(new Reference(fColName, pColName));
      }       
      return hm;      
  }
  
  public static String getForeignKeyTags(HashMap hm, String catalog,String schema,String table ) {
       String hashKey = catalog + "\t" +schema + "\t" + table;
       HashMap keyInfo = (HashMap)hm.get(hashKey);
       if (keyInfo == null) return "";
       
       StringBuffer sb = new StringBuffer("");
       Collection col = keyInfo.values();
       Iterator forKeyIt = col.iterator();
       while (forKeyIt.hasNext()) {
           ForeignKey fk = (ForeignKey)forKeyIt.next();
           java.util.Vector v = fk.getReferencesVector();
           sb.append("\n\t\t<foreign-key  name=\"").append(fk.getName()).append("\"" ).
               append("\n\t\t      foreignTable=\"").append(fk.getForeignTable()).append("\"" ).
               append("\n\t\t      displayType=\"").append(v.size() == 1 ? "select" : "none").append("\"").               
               append(">\n");
           
           for (int jj = 0; jj < v.size();jj++ ) {
              Reference ref = (Reference)v.get(jj);
              sb.append("\t\t     <reference local=\"").append(ref.getLocal()).append("\"").
                append("\n\t\t              foreign=\"").append(ref.getForeign()).append("\"/>\n");           
           }
           sb.append("\t\t</foreign-key>\n");
       }
       return sb.toString();                       
   }
       
  // nope, i won't use DOM for that task ;-)
  public static String createXMLOutput(ProjectData projectData,
                              boolean createGuiMessagewindow) throws Exception {

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
          boolean useStdTypeNames = projectData.getProperty(WRITE_STD_TYPENAMES).
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
               
          String dateFormatTag =  projectData.getProperty(DATE_FORMAT).equalsIgnoreCase("") ? 
                                            "" : "\n\t<date-format>" + projectData.getProperty(DATE_FORMAT) +
                                                    "</date-format>\n\n" ;
                                            
	  System.out.println(": Retrieving metadata using the following properties ");
	  System.out.println("-----------------------------------------------------");
	  System.out.println("jdbcDriver="+jdbcDriver);
	  System.out.println("jdbcURL="+jdbcURL);
	  System.out.println("username="+username);
	  System.out.println("password=(hidden)");
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

                result.append(dateFormatTag);
                
		DatabaseMetaData dbmd = con.getMetaData();
                
                                                        // at leat until JDBC 2 there does not seem to be a standard
                                                        // way to determine, which column is automatically incremented
                                                        // by dbms. The following is a woraround to support some systems
                                                        // (contributed by Sebastian Bogaci)
                                                        // currently we are able to handle:
                                                        //    MySQL, DB2    : using dbms specific query
                                                        //    Sybase, (maybe MS-SQL): checking string representation of
                                                        //                                                       column type
                boolean checkForAutoIncFields = false;
                String      autoIncColumnsQuery = "";
                String      catalogPlaceholder       = ":catalog";
                String      schemaPlaceholder       = ":schema";
                String      tabnamePlaceholder    = ":tabname";
                int            dbms = 0;
                PreparedStatement spCall = null;
                try {                                // this is not mission critical, so own try block
                    String dbmsProductName = dbmd.getDatabaseProductName();
                    if (dbmsProductName != null) {
                      dbmsProductName = dbmsProductName.toLowerCase();
                      if (dbmsProductName.equals("mysql")) {
                        dbms = DBMS_MYSQL;
                        checkForAutoIncFields = true;
                        autoIncColumnsQuery  = "SHOW COLUMNS FROM :tabname LIKE ?";
                     } else if (dbmsProductName.startsWith("db2")) {
                        dbms = DBMS_IBMDB2;
                        checkForAutoIncFields = true;
                        autoIncColumnsQuery  = 
                             "SELECT identity FROM sysibm.syscolumns " +
                             "WHERE tbcreator=':schema' and  tbname = ':tabname' AND name = ?";
                      }
                    }
                } catch (SQLException ignored) {}
                
                
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
                Vector knownTables = new Vector();
                
                try {
                    ResultSet tablesRS = dbmd.getTables(catalog, schemaPattern, tableNamePattern, types);               

		    while(tablesRS.next()) {
                       catalogNames.add(tablesRS.getString(1));
                       schemaNames.add(tablesRS.getString(2));
                       tableNames.add(tablesRS.getString(3));
                       knownTables.add("" + tablesRS.getString(1) + "\t" + tablesRS.getString(2) + "\t" + tablesRS.getString(3));                      
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
                
                HashMap forKeys = getForeignKeyInformation(dbmd, includeCatalog, catalogSeparator,
                                                     includeSchema,schemaSeparator,knownTables) ;
                
                if (! useAutoCommitMode) con.commit();
                
                if (tableNames.size() == 0) {
                    showWarning = true;
                    warningMessage.append("<li> No tables of type <br>(");
                    for (int i=0 ; i < types.length;i++) warningMessage.append("'").append(types[i]).append("' ");
                    warningMessage.append(") <br>found with catalog='" + catalog +"', schemapattern='" +
                                                                     schemaPattern + "',<br>tablename pattern='" + tableNamePattern +"'");
                }
                
                boolean autoIncColumnsQueryAlwaysSucceeded = true; 
                
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

                                                                // read primary key into Vector keys:
		  ResultSet rsKeys = dbmd.getPrimaryKeys(catalogName, schemaName, tableName);
		  Vector keys = new Vector();
                  String  defaultVisibleFields = "";
                  boolean isFirst = true;
		  while(rsKeys.next()) {
		      String columnName = rsKeys.getString(4);
		      keys.addElement(columnName);
                      if (isFirst) defaultVisibleFields += columnName;
                      else defaultVisibleFields += "," +  columnName;
                      isFirst = false;
		  }
		  rsKeys.close();
                  
                  result.append(tableName).append("\"");
                  
                  if (defaultVisibleFields.length() > 0) 
                      result.append("\n\t            defaultVisibleFields=\"" + defaultVisibleFields + "\" ");
		  result.append(">\n");

                                                                // now try to get information about automatically 
                                                                // incemented fields. Unfortunaltely there is now 
                                                                // standard way to get this information in JDBC 2. 
                                                                // Is there one within JDBC 3 ? 
                                                                // 
                  
                  if (checkForAutoIncFields) {
                       
                       String sqlStmtPS = autoIncColumnsQuery;
                       
                                                                // We first construct a dbms specific query by substituting
                                                                // placeholders for catalog, schema and table name
                                                                // in a sql query. We do not use JDBC prepared queries
                                                                // with questionmarks because we do not know the order
                                                                // of these parameters and because '?' might not be allowed
                                                                // in any place
                     
                       int pos =  autoIncColumnsQuery.indexOf(tabnamePlaceholder);
                       if (pos >= 0) 
                           sqlStmtPS = autoIncColumnsQuery.substring(0,pos) + tableName +
                                                   autoIncColumnsQuery.substring(pos + tabnamePlaceholder.length());
                       
                       pos =  sqlStmtPS.indexOf(schemaPlaceholder);
                       if (pos >= 0) 
                           sqlStmtPS = sqlStmtPS.substring(0,pos) + schemaName +
                                                   sqlStmtPS.substring(pos + schemaPlaceholder.length());
                       
                       pos =  sqlStmtPS.indexOf(catalogPlaceholder);
                       if (pos >= 0) 
                           sqlStmtPS = sqlStmtPS.substring(0,pos) + catalogName +
                                                   sqlStmtPS.substring(pos + catalogPlaceholder.length());
                     
                                                                // now prepare statement having just one '?' for 
                                                                // column name left:
                       
                       try {                                 // if something goes wrong here (maybe wrong dbms version),
                                                                // the program should go on, just the detection of auto-incremented
                                                                // columns will not work...
                          spCall = con.prepareStatement(sqlStmtPS);
                       } catch (SQLException ex) {
                           System.err.println("Warning: Prepare of Statement \n'" + sqlStmtPS 
                              + "'\n  failed with message \n'" + ex.getMessage() 
                              +"'.\n No reason to panic, just detection auf auto-incremented \n " 
                              + " columns will not work. However, better send a mail to \n"
                              + " DbForms Mailing list to get this corrected" );
                       } 
                  }
                  
		  ResultSet rsFields = dbmd.getColumns(catalogName, schemaName, tableName, null);
		  while(rsFields.next()) {

		       String columnName = rsFields.getString(4);
		       short dataType = rsFields.getShort(5);
		       String typeName = rsFields.getString(6);
		       int columnSize = rsFields.getInt(7);
		       String isNullable = rsFields.getString(18);
                       int typeCode = rsFields.getInt(5);
                       
                                                             // if we want to check for autoincremented
                                                             // columns and have successfully prepared a
                                                             // dbms specific query, we can now do an execute
                                                             // for this query and evaluate results:    
                       boolean isAutoIncColumn = false;
                       if (checkForAutoIncFields && spCall != null) {
                            try {
                               spCall.setString(1, columnName);
                               ResultSet rssp = spCall.executeQuery();

                               switch (dbms) {
                                    case DBMS_MYSQL :      
                                       isAutoIncColumn =                                           
                                          (   rssp.next() &&
                                              rssp.getString(1).equalsIgnoreCase(columnName) &&
                                              rssp.getString(6).equalsIgnoreCase("auto_increment") );                                           
                                        break;
                                   case DBMS_IBMDB2 :
                                       isAutoIncColumn =                                           
                                          (  rssp.next() &&
                                              rssp.getString(1).equalsIgnoreCase("y") );
                                       break;
                              }
                              rssp.close();
                            } catch (SQLException ex) {    
                                          // We do not want to print out this message again and again for each column,
                                          // so we check if this error already occured and only print out message
                                          // the first time
                                if (autoIncColumnsQueryAlwaysSucceeded) {
                                      System.err.println("Warning: Reading of auto-incremented columns  \n"  
                                         + "\n  failed with message \n'" + ex.getMessage() 
                                         +"'.\n No reason to panic, just detection auf auto-incremented \n " 
                                         + " columns will not work. However, better send a mail to \n"
                                         + " DbForms Mailing list to get this corrected" );    
                                      autoIncColumnsQueryAlwaysSucceeded = false;
                                }
                            }
                        } else { // some DBMS (like Sybase) simply have a trailing
                                     // ' identity' in type name
                            isAutoIncColumn =  typeName.toLowerCase().endsWith(" identity");
                        }
                                                                // if type name is unknown and user selected to
                                                                // generate standard type names in this case, try
                                                                // to set typeName to standard type name
                          if ( useStdTypeNames && ( ! fieldTypeIsKnown(typeName)) ) {
                              switch (typeCode) {
                                  case java.sql.Types.BIGINT:
                                  case java.sql.Types.INTEGER:
                                  case java.sql.Types.SMALLINT:
                                  case java.sql.Types.TINYINT:          typeName = "integer"; break;
                                  case java.sql.Types.CHAR:
                                  case java.sql.Types.LONGVARCHAR:
                                  case java.sql.Types.VARCHAR:       typeName = "char";       break;
                                  case java.sql.Types.DECIMAL:        typeName = "decimal"; break;
                                  case java.sql.Types.NUMERIC:      typeName = "numeric"; break;
                                  case java.sql.Types.FLOAT:             typeName = "float"; break;
                                  case java.sql.Types.REAL:                typeName = "real"; break;
                                  case java.sql.Types.DATE:               typeName = "date"; break;
                                  case java.sql.Types.TIMESTAMP:  typeName = "timestamp"; break;
                                  case java.sql.Types.BLOB:               typeName="blob"; break;
                           }
                          }

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
                          if (isAutoIncColumn) result.append(" autoInc=\"true\"");
                          
			  result.append("/>\n");
		  }
		  rsFields.close();
                  if (! useAutoCommitMode) con.commit();
                  
                  result.append(getForeignKeyTags(forKeys,catalogName,schemaName, tableName) );
 
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
                          if (createGuiMessagewindow) 
                              javax.swing.JOptionPane.showMessageDialog(
                                  null, warningMessage,"Warning", 
                                  javax.swing.JOptionPane.WARNING_MESSAGE);
                          else 
                              System.err.println("Warning:\n " + warningMessage);
	               
			if(con!=null) con.close();
		}	catch(SQLException sqle) {
			throw new SQLException("could not close Connection - " + sqle.getMessage());
		}
	}

	return result.toString();
  }  

}