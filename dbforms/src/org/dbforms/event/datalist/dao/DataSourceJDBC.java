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

import java.sql.*;
import java.util.*;
import java.io.*;
import org.apache.log4j.Category;
import org.dbforms.Table;
import org.dbforms.DbFormsConfig;
import org.dbforms.FieldValue;
import org.dbforms.Field;
import org.dbforms.util.FieldTypes;
import org.dbforms.util.Constants;
import org.dbforms.util.ResultSetVector;
import org.dbforms.util.SqlUtil;
import org.dbforms.util.FieldValues;
import org.dbforms.util.FileHolder;
import org.dbforms.util.UniqueIDGenerator;
import org.dbforms.util.Util;

/**
 * 
 * Special implementation of DataSource. This is the default class and deals with JDBC Connections.
 * 
 * 
 * @author hkk
 */
public class DataSourceJDBC extends DataSource {
   static Category logCat =
      Category.getInstance(DataSourceJDBC.class.getName());
   // logging category for this class

   private String query;
   private Connection con;
   private boolean ownCon = false;
   private ResultSet rs;
   private Statement stmt;
   private Vector data;
   private Vector keys;
   private int colCount;
   private String whereClause;
   private String dbConnectionName;
   private String tableList;
   private FieldValue[] filterConstraint;
   private FieldValue[] orderConstraint;
   private DbFormsConfig config;

   public DataSourceJDBC(Table table) {
      super(table);
      data = new Vector();
      keys = new Vector();
   }

   public void setConnection(DbFormsConfig config, String dbConnectionName) {
      try {
         close();
      } catch (SQLException e) {
         SqlUtil.logSqlException(e);
      }
      this.config = config;
      this.dbConnectionName = dbConnectionName;
   }

   public void setConnection(Connection con) {
      try {
         close();
      } catch (SQLException e) {
         SqlUtil.logSqlException(e);
      }
      this.con = con;
   }

   public void setSelect(String tableList, String whereClause) {
      this.tableList = tableList;
      this.whereClause = whereClause;
   }

   public void setSelect(
      FieldValue[] filterConstraint,
      FieldValue[] orderConstraint) {
      this.filterConstraint = filterConstraint;
      this.orderConstraint = orderConstraint;
   }

   public void close() throws SQLException {
      if (data != null)
         data.clear();
      if (keys != null)
         keys.clear();
      if (rs != null) {
         rs.close();
         rs = null;
      }
      if (stmt != null) {
         stmt.close();
         stmt = null;
      }
      if (ownCon) {
         con.close();
         con = null;
      }
      ownCon = false;
   }

   protected void open() throws SQLException {
      if (rs == null) {
 		 ownCon = true;
		 con = SqlUtil.getConnection(config, dbConnectionName);
         if (Util.isNull(whereClause)) {
            query =
               getTable().getSelectQuery(
				getTable().getFields(),
				  filterConstraint,
                  orderConstraint,
                  Constants.COMPARE_NONE);
            stmt = con.prepareStatement(query);
            rs =
				getTable().getDoSelectResultSet(
				  filterConstraint,
                  orderConstraint,
                  Constants.COMPARE_NONE,
                  (PreparedStatement) stmt);
         } else {
            query =
				getTable().getFreeFormSelectQuery(
				getTable().getFields(),
				  whereClause,
				  tableList
                  );
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
         }
         ResultSetMetaData rsmd = rs.getMetaData();
         colCount = rsmd.getColumnCount();
      }
   }

   private Object[] getCurrentRowAsObject() throws SQLException {
      Object[] objectRow = new Object[colCount];
      for (int i = 0; i < colCount; i++)
         objectRow[i] = rs.getObject(i + 1);
      return objectRow;
   }

   private String[] getCurrentRow() throws SQLException {
      String[] objectRow = new String[colCount];
      for (int i = 0; i < colCount; i++)
         objectRow[i] = rs.getString(i + 1);
      return objectRow;
   }

   protected int findStartRow(String startRow) throws SQLException {
      int result = 0;
      String s;
      if (startRow != null) {
         for (int i = 0; i < keys.size(); i++) {
            if (startRow.equals((String) keys.elementAt(i))) {
               result = i;
               break;
            }
         }
         if (result == 0) {
            while (rs.next()) {
               data.add(getCurrentRowAsObject());
               s = getTable().getKeyPositionString(getCurrentRow());
               keys.add(s);
               if (startRow.equals(s)) {
                  result = data.size() - 1;
                  break;
               }
            }
         }
      }
      return result;
   }

   protected final Object[] getRow(int i) throws SQLException {
      Object[] result = null;
      if (i >= 0) {
         if (i < data.size()) {
            result = (Object[]) data.elementAt(i);
         } else {
            while (rs.next()) {
               result = getCurrentRowAsObject();
               data.add(result);
               keys.add(getTable().getKeyPositionString(getCurrentRow()));
               if (i < data.size()) {
                  break;
               }
            }
         }
      }
      return result;
   }

   protected int size() throws SQLException {
      while (rs.next()) {
         data.add(getCurrentRowAsObject());
         keys.add(getTable().getKeyPositionString(getCurrentRow()));
      }
      return data.size();
   }


   //------------------------------ DAO methods ---------------------------------
   private int fillWithData(PreparedStatement ps, FieldValues fieldValues)
      throws SQLException {
      // now we provide the values;
      // every key is the parameter name from of the form page;
      Enumeration enum = fieldValues.keys();
      int col = 1;
      while (enum.hasMoreElements()) {
         String fieldName = (String) enum.nextElement();
         Field curField = getTable().getFieldByName(fieldName);
         if (curField != null) {
            FieldValue fv = fieldValues.get(fieldName);
            //logCat.debug("Retrieved curField:" + curField.getName() + " type:" + curField.getFieldType());
            int fieldType = curField.getType();
            Object value = null;
            if (fieldType == FieldTypes.BLOB) {
               // in case of a BLOB we supply the FileHolder object to SqlUtils for further operations
               value = fv.getFileHolder();
            } else if (fieldType == FieldTypes.DISKBLOB) {
               // check if we need to store it encoded or not
               if ("true".equals(curField.getEncoding())) {
                  FileHolder fileHolder = fv.getFileHolder();
                  // encode fileName
                  String fileName = fileHolder.getFileName();
                  int dotIndex = fileName.lastIndexOf('.');
                  String suffix =
                     (dotIndex != -1) ? fileName.substring(dotIndex) : "";
                  fileHolder.setFileName(
                     UniqueIDGenerator.getUniqueID() + suffix);
                  // a diskblob gets stored to db as an ordinary string (it's only the reference!)
                  value = fileHolder.getFileName();
               } else {
                  // a diskblob gets stored to db as an ordinary string	 (it's only the reference!)
                  value = fv.getFieldValue();
               }
            } else {
               // in case of simple db types we just supply a string representing the value of the fields
               value = fv.getFieldValue();
            }
            logCat.info(
               "field="
                  + curField.getName()
                  + " col="
                  + col
                  + " value="
                  + value
                  + " type="
                  + fieldType);
            SqlUtil.fillPreparedStatement(ps, col, value, fieldType);
            col++;
         }
      }
      return col;
   }

   public void doInsert(FieldValues fieldValues) throws SQLException {
      PreparedStatement ps =
         con.prepareStatement(getTable().getInsertStatement(fieldValues));
      // execute the query & throws an exception if something goes wrong
      fillWithData(ps, fieldValues);
      ps.executeUpdate();
      ps.close();
      // now handle blob files
      saveBlobFilesToDisk(fieldValues);
   }

   public void doUpdate(FieldValues fieldValues, String keyValuesStr)
      throws SQLException {
      PreparedStatement ps =
         con.prepareStatement(getTable().getUpdateStatement(fieldValues));
      int col = fillWithData(ps, fieldValues);
		getTable().populateWhereClauseForPS(keyValuesStr, ps, col);
      // we are now ready to execute the query
      ps.executeUpdate();
      ps.close();
      // now handle blob files
      saveBlobFilesToDisk(fieldValues);
   }

   public void doDelete(String keyValuesStr) throws SQLException {
      FieldValues fieldValues = null;
      // get current blob files from database
      if (getTable().containsDiskblob()) {
         ResultSet diskblobs = null;
         StringBuffer queryBuf = new StringBuffer();
         queryBuf.append(getTable().getDisblobSelectStatement());
         queryBuf.append(" WHERE ");
         queryBuf.append(getTable().getWhereClauseForPS());
         PreparedStatement diskblobsPs =
            con.prepareStatement(queryBuf.toString());
			getTable().populateWhereClauseForPS(keyValuesStr, diskblobsPs, 1);
         diskblobs = diskblobsPs.executeQuery();
         ResultSetVector rsv =
            new ResultSetVector(getTable().getDiskblobs(), diskblobs);
         if (!Util.isNull(rsv)) {
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
         deleteBlobFilesFromDisk(fieldValues);
   }

}
