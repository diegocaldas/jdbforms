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


import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Category;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.Table;
import org.dbforms.util.FieldValue;
import org.dbforms.util.ResultSetVector;
import org.dbforms.util.ReflectionUtil;
import org.dbforms.util.FieldValues;
import org.dbforms.util.Util;



/**
 * Factory class to generate different DataSources.
 * datasource is attribute of table class and can be changed in dbforms-config.
 * Default class is
 *
 * @author hkk
 */
public class DataSourceFactory
{
   private DataSource dataHandler;

   // logging category for this class;
   private static Category logCat = Category.getInstance(DataSourceFactory.class.getName());

   /**
    * Creates a new DataSourceFactory object.
    * <br>
    * Set its DataSource object as dataHandler, using the dataAccess class name
    * for the given table. 
    *
    */
   private DataSourceFactory(Table table)
     //  throws SQLException
   {
      String dataAccessClass = table.getDataAccessClass();

      if (Util.isNull(dataAccessClass))
      {
         dataAccessClass = "org.dbforms.event.datalist.dao.DataSourceJDBC";
      }

      try
      {
         Object[] constructorArgs      = new Object[] {table };
         Class[]  constructorArgsTypes = new Class[]  { Table.class };
         dataHandler = (DataSource) ReflectionUtil.newInstance(dataAccessClass,
                                                               constructorArgsTypes, 
                                                               constructorArgs);
      }
      catch (Exception e)
      {
         logCat.error(e);
      }
   }


	public DataSourceFactory(Connection con, Table table)
	{
	   this(table);
	   dataHandler.setConnection(con);
	}

   /**
    * Creates a new DataSourceFactory object.
    *
    * @param config 		  the configuration object
    * @param dbConnectionName the name of the db connection
    * @param table 			  the input table
    * @param filterConstraint the filter constraint
    * @param orderConstraint  the order constraint
    *
    * @throws SQLException if any error occurs
    */
   public DataSourceFactory(Connection con, 
   							Table 	      table, 
      						FieldValue[]  filterConstraint, 
      						FieldValue[]  orderConstraint,
      						String sqlFilter)
      throws SQLException
   {
      this(con, table);
      dataHandler.setSelect(filterConstraint, orderConstraint, sqlFilter);
   }


   /**
    * Creates a new DataSourceFactory object.
    *
    * @param config           the configuration object
    * @param dbConnectionName the name of the db connection
    * @param table            the input table
    * @param tableList        the list of tables
    * @param whereClause      the SQL where clause
    *
    * @throws SQLException if any error occurs
    */
   public DataSourceFactory(Connection con, 
      						Table         table, 
      						String        tableList, 
      						String        whereClause)
      throws SQLException
   {
      this(con, table);
      dataHandler.setSelect(tableList, whereClause);
   }


   /**
    *  Close the underlying dataHandler.
    */
   public void close()
   {
      dataHandler.close();
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
   public ResultSetVector getNext(String position, int count)
      throws SQLException
   {
      return dataHandler.getNext(position, count);
   }


   /**
    * Return a resultSetVector object containing the previous <i>count</i> records
    * starting from the input position + <i>count</i> records.
    *
    * @param position the current table position
    * @param count number of records to fetch
    *
    * @return a resultSetVector object containing the previous <i>count</i> records
    *         starting from the input position
    *
    * @throws SQLException if any error occurs
    */
   public ResultSetVector getPrev(String position, int count)
      throws SQLException
   {
      return dataHandler.getPrev(position, count);
   }


   /**
	* Return a resultSetVector object containing the first <i>count</i> records.
	*
	* @param count number of records to fetch
	*
	* @return a resultSetVector object containing the first <i>count</i> records
	*
	* @throws SQLException if any error occurs
	*/
   public ResultSetVector getFirst(int count) throws SQLException
   {
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
   public ResultSetVector getLast(int count) throws SQLException
   {
      return dataHandler.getLast(count);
   }


   /**
	 * Return a resultSetVector object containing <i>count</i> records
	 * starting from the input position
	 *
	 * @param position the current table position
	 * @param count number of records to fetch
	 *
	 * @return a resultSetVector object containing the current <i>count</i> records
	 *         starting from the input position
	 *
	 * @throws SQLException if any error occurs
	 */
   public ResultSetVector getCurrent(String position, int count)
      throws SQLException
   {
      return dataHandler.getCurrent(position, count);
   }


   /**
    * Perform an insert operation into the underlying dataSource.
    *
    * @param fieldValues the field values to insert
    *
    * @throws SQLException if any error occurs
    */
   public void doInsert(FieldValues fieldValues) throws SQLException
   {
      dataHandler.doInsert(fieldValues);
   }


   /**
    * Perform an update operation into the underlying dataSource.
    *
    * @param fieldValues the field values to update
    * @param keyValuesStr the key value identifying the record to update
    *
    * @throws SQLException if any error occurs
    */
   public void doUpdate(FieldValues fieldValues, String keyValuesStr)
      throws SQLException
   {
      dataHandler.doUpdate(fieldValues, keyValuesStr);
   }


  /**
   * Perform a delete operation into the underlying dataSource.
   *
   * @param keyValuesStr the key value identifying the record to delete
   *
   * @throws SQLException if any error occurs
   */
   public void doDelete(String keyValuesStr) throws SQLException
   {
      dataHandler.doDelete(keyValuesStr);
   }
}
