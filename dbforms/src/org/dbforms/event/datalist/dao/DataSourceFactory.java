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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.config.FieldValue;
import org.dbforms.config.FieldValues;
import org.dbforms.config.ResultSetVector;
import org.dbforms.config.Table;

import org.dbforms.util.ReflectionUtil;
import org.dbforms.util.Util;

import java.sql.Connection;
import java.sql.SQLException;



/**
 * Factory class to generate different DataSources. datasource is attribute of
 * table class and can be changed in dbforms-config. Default class is
 *
 * @author hkk
 */
public class DataSourceFactory {
   // logging category for this class;
   private static Log logCat = LogFactory.getLog(DataSourceFactory.class
                                                 .getName());
   private DataSource dataHandler;

   /**
    * Creates a new DataSourceFactory object. <br>
    * Set its DataSource object as dataHandler, using the dataAccess class
    * name for the given table.
    *
    * @param table DOCUMENT ME!
    */
   public DataSourceFactory(Table table) {
      String dataAccessClass = table.getDataAccessClass();

      if (Util.isNull(dataAccessClass)) {
         dataAccessClass = "org.dbforms.event.datalist.dao.DataSourceJDBC";
      }

      try {
         Object[] constructorArgs = new Object[] {
                                       table
                                    };
         Class[]  constructorArgsTypes = new Class[] {
                                            Table.class
                                         };
         dataHandler = (DataSource) ReflectionUtil.newInstance(dataAccessClass,
                                                               constructorArgsTypes,
                                                               constructorArgs);
      } catch (Exception e) {
         logCat.error(e);
      }
   }


   /**
    * Creates a new DataSourceFactory object.
    *
    * @param dbConnectionName name of the used db connection. Can be used to
    *        get an own db connection, e.g. to hold it during the  session
    *        (see DataSourceJDBC for example!)
    * @param con the JDBC Connection object
    * @param table the input table
    *
    * @throws SQLException if any error occurs
    */
   public DataSourceFactory(String     dbConnectionName,
                            Connection con,
                            Table      table) throws SQLException {
      this(table);
      dataHandler.setConnection(con, dbConnectionName);
   }

   /**
    * Return a resultSetVector object containing <i>count</i> records starting
    * from the input position
    *
    * @param position the current table position
    * @param count number of records to fetch
    *
    * @return a resultSetVector object containing the current <i>count</i>
    *         records starting from the input position
    *
    * @throws SQLException if any error occurs
    */
   public ResultSetVector getCurrent(String position,
                                     int    count) throws SQLException {
      return dataHandler.getCurrent(position, count);
   }


   /**
    * returns the internal DataSource element
    *
    * @return the DataSource element
    */
   public DataSource getDataHandler() {
      return dataHandler;
   }


   /**
    * Return a resultSetVector object containing the first <i>count</i>
    * records.
    *
    * @param count number of records to fetch
    *
    * @return a resultSetVector object containing the first <i>count</i>
    *         records
    *
    * @throws SQLException if any error occurs
    */
   public ResultSetVector getFirst(int count) throws SQLException {
      return dataHandler.getFirst(count);
   }


   /**
    * Return a resultSetVector object containing the last <i>count</i> records.
    *
    * @param count number of records to fetch
    *
    * @return a resultSetVector object containing the last <i>count</i> records
    *
    * @throws SQLException if any error occurs
    */
   public ResultSetVector getLast(int count) throws SQLException {
      return dataHandler.getLast(count);
   }


   /**
    * Return a resultSetVector object containing the next <i>count</i> records
    * starting from the input position + <i>count</i> records.
    *
    * @param position the current table position
    * @param count number of records to fetch
    *
    * @return a resultSetVector object containing the next <i>count</i> records
    *         starting from the input position
    *
    * @throws SQLException if any error occurs
    */
   public ResultSetVector getNext(String position,
                                  int    count) throws SQLException {
      return dataHandler.getNext(position, count);
   }


   /**
    * Return a resultSetVector object containing the previous <i>count</i>
    * records starting from the input position + <i>count</i> records.
    *
    * @param position the current table position
    * @param count number of records to fetch
    *
    * @return a resultSetVector object containing the previous <i>count</i>
    *         records starting from the input position
    *
    * @throws SQLException if any error occurs
    */
   public ResultSetVector getPrev(String position,
                                  int    count) throws SQLException {
      return dataHandler.getPrev(position, count);
   }


   /**
    * Sets the select data for this dataSource
    *
    * @param filterConstraint FieldValue array used to restrict a set in a
    *        resultset
    * @param orderConstraint FieldValue array used to build a cumulation of
    *        rules for ordering (sorting)
    * @param sqlFilter sql condition to add to where clause
    * @param sqlFilterParams DOCUMENT ME!
    */
   public void setSelect(FieldValue[] filterConstraint,
                         FieldValue[] orderConstraint,
                         String       sqlFilter,
                         FieldValue[] sqlFilterParams) {
      dataHandler.setSelect(filterConstraint, orderConstraint, sqlFilter,
                            sqlFilterParams);
   }


   /**
    * Sets the select data for this dataSource for free form selects. default
    * methods just raises an exception
    *
    * @param tableList the list of tables involved into the query
    * @param whereClause free-form whereClause to be appended to query
    *
    * @throws SQLException
    */
   public void setSelect(String tableList,
                         String whereClause) throws SQLException {
      dataHandler.setSelect(tableList, whereClause);
   }


   /**
    * Close the underlying dataHandler.
    */
   public void close() {
      dataHandler.close();
   }


   /**
    * Perform a delete operation into the underlying dataSource.
    *
    * @param con DOCUMENT ME!
    * @param keyValuesStr the key value identifying the record to delete
    *
    * @throws SQLException if any error occurs
    */
   public void doDelete(Connection con,
                        String     keyValuesStr) throws SQLException {
      dataHandler.doDelete(con, keyValuesStr);
   }


   /**
    * Perform an insert operation into the underlying dataSource.
    *
    * @param con DOCUMENT ME!
    * @param fieldValues the field values to insert
    *
    * @throws SQLException if any error occurs
    */
   public void doInsert(Connection  con,
                        FieldValues fieldValues) throws SQLException {
      dataHandler.doInsert(con, fieldValues);
   }


   /**
    * Perform an update operation into the underlying dataSource.
    *
    * @param con DOCUMENT ME!
    * @param fieldValues the field values to update
    * @param keyValuesStr the key value identifying the record to update
    *
    * @throws SQLException if any error occurs
    */
   public void doUpdate(Connection  con,
                        FieldValues fieldValues,
                        String      keyValuesStr) throws SQLException {
      dataHandler.doUpdate(con, fieldValues, keyValuesStr);
   }
}
