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

package org.dbforms.event.datalist.dao;


import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.Types;
import java.sql.Clob;

import java.util.Vector;
import java.util.Enumeration;

import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.Field;
import org.dbforms.config.Table;
import org.dbforms.util.FieldTypes;
import org.dbforms.util.Constants;
import org.dbforms.util.FieldValue;
import org.dbforms.util.FileHolder;
import org.dbforms.util.ResultSetVector;
import org.dbforms.util.SqlUtil;
import org.dbforms.util.FieldValues;
import org.dbforms.util.UniqueIDGenerator;
import org.dbforms.util.Util;



/**
 * Special implementation of DataSource. This is the default class and deals
 * with JDBC Connections.
 * 
 * @author hkk
 */
public class DataSourceJDBC extends DataSource
{
   private String        query;
   private Connection    con;
   private boolean       ownCon = false;
   private ResultSet     rs;
   private Statement     stmt;
   private Vector        data;
   private Vector        keys;
   private int           colCount;
   private String        whereClause;
   private String        dbConnectionName;
   private String        tableList;
   private FieldValue[]  filterConstraint;
   private FieldValue[]  orderConstraint;
   private DbFormsConfig config;
   private boolean calledFromFinalize = false;

   /**
    * Creates a new DataSourceJDBC object.
    *
    * @param table the inout table
    */
   public DataSourceJDBC(Table table)
   {
      super(table);
      data = new Vector();
      keys = new Vector();
   }


   /**
    * Finalize this object.
    *
    * @throws Throwable in any error occurs
    */
   protected void finalize() throws Throwable
   {
	  getLogCat().info("finalize called");
	  // 20030725-HKK:
	  // To overcome a bug in the firebird jdbc driver. 
	  // This drivers makes an error if you call stmt.close in finalize.
	  // After this error no more connections are possible!
	  calledFromFinalize = true;      
      close();
   }


   /**
    * Set the conenction name.
    * 
    * @param config teh configuration object
    * @param dbConnectionName the name of the database connection
    */
   public void setConnection(DbFormsConfig config, String dbConnectionName)
   {
      close();
      this.config           = config;
      this.dbConnectionName = dbConnectionName;
   }


   /**
    * Set the connection object.
    * 
    * @param con the connection object
    */
   public void setConnection(Connection con)
   {
      close();
      this.con = con;
   }


   /**
    * Set the tableList and whererClause attributes used
    * to build the SQL Select condition.
    * 
    * @param tableList the table list string
    * @param whereClause the SQL where clause string
    */
   public void setSelect(String tableList, String whereClause)
   {
      this.tableList   = tableList;
      this.whereClause = whereClause;
   }


   /**
    * Set the filterConstraint and orderConstraint used
    * to build the SQL Select condition.
    * 
    * @param filterConstraint FieldValue array used to build a cumulation of rules for filtering
	*                         fields.
    * @param orderConstraint  FieldValue array used to build a cumulation of rules for ordering
	*                         (sorting) and restricting fields.
    */
   public void setSelect(FieldValue[] filterConstraint, 
                         FieldValue[] orderConstraint)
   {
      this.filterConstraint = filterConstraint;
      this.orderConstraint  = orderConstraint;
   }


   /**
    * Release all the resources holded by this datasource.
    * <br>
    * Clean the underlying data and keys vectors, then
    * close the JDBC resultSet, statement and connection objects. 
    */
   public void close()
   {
      if (data != null)
      {
         data.clear();
      }

      if (keys != null)
      {
         keys.clear();
      }

      if (rs != null)
      {
         try
         {
            rs.close();
         }
         catch (SQLException e)
         {
				SqlUtil.logSqlException(e);
         }
         rs = null;
      }

      if (stmt != null)
      {
         try
         {
            if (!calledFromFinalize) 
               stmt.close();
         }
         catch (SQLException e)
         {
				SqlUtil.logSqlException(e);
         }
         stmt = null;
      }

      if (ownCon)
      {
         try
         {
            con.close();
         }
         catch (SQLException e)
         {
				SqlUtil.logSqlException(e);
         }
         con = null;
      }

      ownCon = false;
   }


   /**
    * Open this datasource and initialize its resources.
    * 
    * @throws SQLException if any error occurs
    */
   protected void open() throws SQLException
   {
      if (rs == null)
      {
         ownCon = true;
         con    = SqlUtil.getConnection(config, dbConnectionName);

         if (Util.isNull(whereClause))
         {
            query = getTable()
                       .getSelectQuery(getTable().getFields(), 
                                       filterConstraint, 
                                       orderConstraint, 
                                       Constants.COMPARE_NONE);
                                       
            stmt = con.prepareStatement(query);
            rs   = getTable()
                      .getDoSelectResultSet(filterConstraint, 
                                            orderConstraint, 
                                            Constants.COMPARE_NONE, 
                                            (PreparedStatement) stmt);
         }
         else
         {
            query = getTable()
                       .getFreeFormSelectQuery(getTable().getFields(), 
                                               whereClause, tableList);
            stmt = con.createStatement();
            rs   = stmt.executeQuery(query);
         }

         ResultSetMetaData rsmd = rs.getMetaData();
         colCount = rsmd.getColumnCount();
      }
   }


   private Object[] getCurrentRowAsObject() throws SQLException
   {
      Object[] objectRow = new Object[colCount];

      for (int i = 0; i < colCount; i++)
      {
         if (rs.getMetaData().getColumnType(i + 1) == Types.CLOB)
         {
            Clob tmpObj = (Clob) rs.getObject(i + 1);
            if (tmpObj != null)  
            	objectRow[i] = tmpObj.getSubString((long) 1, (int) tmpObj.length());
            else
					objectRow[i] = null;
         }
         else
         {
            Object tmpObj = rs.getObject(i + 1);
            objectRow[i] = tmpObj;
         }
      }

      return objectRow;
   }


   private String[] getCurrentRow() throws SQLException
   {
      String[] objectRow = new String[colCount];

      for (int i = 0; i < colCount; i++)
         if (rs.getMetaData().getColumnType(i + 1) == Types.CLOB)
         {
            Clob tmpObj = (Clob) rs.getObject(i + 1);
            objectRow[i] = tmpObj.getSubString((long) 1, (int) tmpObj.length());
         }
         else
         {
            objectRow[i] = rs.getString(i + 1);
         }

      return objectRow;
   }


   /**
    * Find the first row of the internal data vector. 
    * 
    * @param startRow the string identifying the initial row
    * 
    * @return the start row position
    * 
    * @throws SQLException if any error occurs
    */
   protected int findStartRow(String startRow) throws SQLException
   {
      int    result = 0;
      String s;

      if (startRow != null)
      {
         for (int i = 0; i < keys.size(); i++)
         {
            if (startRow.equals((String) keys.elementAt(i)))
            {
               result = i;
               break;
            }
         }

         if (result == 0)
         {
            while (rs.next())
            {
               data.add(getCurrentRowAsObject());
               s = getTable().getKeyPositionString(getCurrentRow());
               keys.add(s);

               if (startRow.equals(s))
               {
                  result = data.size() - 1;
                  break;
               }
            }
         }
      }

      return result;
   }


   /**
    * Get the requested row  as array of objects.
    *
    * @param i the row number
    * 
    * @return the requested row  as array of objects
    * 
    * @throws SQLException if any error occurs
    */
   protected final Object[] getRow(int i) throws SQLException
   {
      Object[] result = null;

      if (i >= 0)
      {
         if (i < data.size())
         {
            result = (Object[]) data.elementAt(i);
         }
         else
         {
            while (rs.next())
            {
               result = getCurrentRowAsObject();
               data.add(result);
               keys.add(getTable().getKeyPositionString(getCurrentRow()));

               if (i < data.size())
               {
                  break;
               }
            }
         }
      }

      return result;
   }


   /**
    * Get the size of the data vector.
    * 
    * @return the size of the data vector
    * 
    * @throws SQLException if any error occurs
    */
   protected int size() throws SQLException
   {
      // Workaround for bug in firebird driver: After reaching next the next call 
      // to next will start at the beginning of the resultset.
      // rs.next will return true, fetching data will get an NullPointerException. 
      // Catch this error and do an break!   
      while (rs.next())
      {
         try
         {
            data.add(getCurrentRowAsObject());
            keys.add(getTable().getKeyPositionString(getCurrentRow()));
         }
         catch (Exception e)
         {
			getLogCat().error(e.getMessage());
            break;
         }
      }

      return data.size();
   }


   //------------------------------ DAO methods ---------------------------------
   private int fillWithData(PreparedStatement ps, 
                            FieldValues       fieldValues)
     throws SQLException
   {
      // now we provide the values;
      // every key is the parameter name from of the form page;
      Enumeration enum = fieldValues.keys();
      int         col = 1;

      while (enum.hasMoreElements())
      {
         String fieldName = (String) enum.nextElement();
         Field  curField = getTable().getFieldByName(fieldName);

         if (curField != null)
         {
            FieldValue fv = fieldValues.get(fieldName);

			getLogCat().debug("Retrieved curField:" + curField.getName() + " type:"
                         + curField.getType());

            int    fieldType = curField.getType();
            Object value = null;

            if (fieldType == FieldTypes.BLOB)
            {
               // in case of a BLOB we supply the FileHolder object to SqlUtils for further operations
               value = fv.getFileHolder();
            }
            else if (fieldType == FieldTypes.DISKBLOB)
            {
               // check if we need to store it encoded or not
               if ("true".equals(curField.getEncoding()))
               {
                  FileHolder fileHolder = fv.getFileHolder();

                  // encode fileName
                  String fileName = fileHolder.getFileName();
                  int    dotIndex = fileName.lastIndexOf('.');
                  String suffix   = (dotIndex != -1)
                                       ? fileName.substring(dotIndex) : "";
                  fileHolder.setFileName(UniqueIDGenerator.getUniqueID()
                                         + suffix);


                  // a diskblob gets stored to db as an ordinary string (it's only the reference!)
                  value = fileHolder.getFileName();
               }
               else
               {
                  // a diskblob gets stored to db as an ordinary string	 (it's only the reference!)
                  value = fv.getFieldValue();
               }
            }
            else
            {
               // in case of simple db types we just supply a string representing the value of the fields
               value = fv.getFieldValue();
            }

			getLogCat().info("field=" + curField.getName() + " col=" + col
                        + " value=" + value + " type=" + fieldType);
            SqlUtil.fillPreparedStatement(ps, col, value, fieldType);
            col++;
         }
      }

      return col;
   }


   /**
	* Performs an insert into the DataSource
	* 
	* @param fieldValues FieldValues to insert
	* 
	* @throws SQLException
	*/
   public void doInsert(FieldValues fieldValues) throws SQLException
   {
      PreparedStatement ps = 
         con.prepareStatement(getTable().getInsertStatement(fieldValues));

      // execute the query & throws an exception if something goes wrong
      fillWithData(ps, fieldValues);
      ps.executeUpdate();
      ps.close();

      // now handle blob files
      saveBlobFilesToDisk(fieldValues);
   }


  /**
   * Performs an update into the DataSource
   * 
   * @param fieldValues  FieldValues to update
   * @param keyValuesStr keyValueStr to the row to update<br>
   *        key format: FieldID ":" Length ":" Value<br>
   *        example: if key id = 121 and field id=2 then keyValueStr contains "2:3:121"<br>
   *        If the key consists of more than one fields, the key values are seperated through "-"<br>
   *        example: value of field 1=12, value of field 3=1992, then we'll
   *        get "1:2:12-3:4:1992"
   * 
   * @throws SQLException
   */
   public void doUpdate(FieldValues fieldValues, String keyValuesStr)
                 throws SQLException
   {
      PreparedStatement ps 
        = con.prepareStatement(getTable().getUpdateStatement(fieldValues));
      int col = fillWithData(ps, fieldValues);
      getTable().populateWhereClauseForPS(keyValuesStr, ps, col);

      // we are now ready to execute the query
      ps.executeUpdate();
      ps.close();

      // now handle blob files
      saveBlobFilesToDisk(fieldValues);
   }


  /**
   * performs a delete in the DataSource
   * 
   * @param keyValuesStr   keyValueStr to the row to update<br>
   *        key format: FieldID ":" Length ":" Value<br>
   *        example: if key id = 121 and field id=2 then keyValueStr contains "2:3:121"<br>
   *        If the key consists of more than one fields, the key values are seperated through "-"<br>
   *        example: value of field 1=12, value of field 3=1992, then we'll
   *        get "1:2:12-3:4:1992"
   * 
   * @throws SQLException
   */
   public void doDelete(String keyValuesStr) throws SQLException
   {
      FieldValues fieldValues = null;

      // get current blob files from database
      if (getTable().containsDiskblob())
      {
         ResultSet    diskblobs = null;
         StringBuffer queryBuf = new StringBuffer();
         queryBuf.append(getTable().getDisblobSelectStatement());
         queryBuf.append(" WHERE ");
         queryBuf.append(getTable().getWhereClauseForPS());

         PreparedStatement diskblobsPs = 
           con.prepareStatement(queryBuf.toString());
         getTable().populateWhereClauseForPS(keyValuesStr, diskblobsPs, 1);
         diskblobs = diskblobsPs.executeQuery();

         ResultSetVector rsv = new ResultSetVector(getTable().getDiskblobs(), diskblobs);

         if (!Util.isNull(rsv))
         {
            rsv.setPointer(0);
            fieldValues = rsv.getCurrentRowAsFieldValues();
         }
      }

      // 20021031-HKK: Build in table!!
      PreparedStatement ps = con.prepareStatement(getTable().getDeleteStatement());

      // now we provide the values
      // of the key-fields, so that the WHERE clause matches the right dataset!
      getTable().populateWhereClauseForPS(keyValuesStr, ps, 1);

      // finally execute the query
      ps.executeUpdate();

      if (fieldValues != null)
      {
         deleteBlobFilesFromDisk(fieldValues);
      }
   }
}