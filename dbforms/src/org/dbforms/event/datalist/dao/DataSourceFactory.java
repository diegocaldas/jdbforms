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
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Category;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.Table;
import org.dbforms.util.FieldValue;
import org.dbforms.util.ResultSetVector;
import org.dbforms.util.ReflectionUtil;
import org.dbforms.util.FieldValues;
import org.dbforms.util.Util;



/**
 *
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
   static Category logCat = Category.getInstance(DataSourceFactory.class
         .getName());

   /**
    * Creates a new DataSourceFactory object.
    *
    * @param table DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public DataSourceFactory(Table table) throws SQLException
   {
      String dataAccessClass = table.getDataAccessClass();

      if (Util.isNull(dataAccessClass))
      {
         dataAccessClass = "org.dbforms.event.datalist.dao.DataSourceJDBC";
      }

      try
      {
         Object[] constructorArgs      = new Object[]
            {
               table
            };
         Class[]  constructorArgsTypes = new Class[]
            {
               Table.class
            };
         dataHandler = (DataSource) ReflectionUtil.newInstance(dataAccessClass,
               constructorArgsTypes, constructorArgs);
      }
      catch (Exception e)
      {
         logCat.error(e);
      }
   }


   /**
    * Creates a new DataSourceFactory object.
    *
    * @param con DOCUMENT ME!
    * @param table DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public DataSourceFactory(Connection con, Table table)
      throws SQLException
   {
      this(table);
      dataHandler.setConnection(con);
   }


   /**
    * Creates a new DataSourceFactory object.
    *
    * @param config DOCUMENT ME!
    * @param dbConnectionName DOCUMENT ME!
    * @param table DOCUMENT ME!
    * @param filterConstraint DOCUMENT ME!
    * @param orderConstraint DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public DataSourceFactory(DbFormsConfig config, String dbConnectionName,
      Table table, FieldValue[] filterConstraint, FieldValue[] orderConstraint)
      throws SQLException
   {
      this(table);
      dataHandler.setConnection(config, dbConnectionName);
      dataHandler.setSelect(filterConstraint, orderConstraint);
   }


   /**
    * Creates a new DataSourceFactory object.
    *
    * @param config DOCUMENT ME!
    * @param dbConnectionName DOCUMENT ME!
    * @param table DOCUMENT ME!
    * @param tableList DOCUMENT ME!
    * @param whereClause DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public DataSourceFactory(DbFormsConfig config, String dbConnectionName,
      Table table, String tableList, String whereClause)
      throws SQLException
   {
      this(table);
      dataHandler.setConnection(config, dbConnectionName);
      dataHandler.setSelect(tableList, whereClause);
   }

   /**
    * DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public void close() throws SQLException
   {
      dataHandler.close();
   }


   /**
    * DOCUMENT ME!
    *
    * @param position DOCUMENT ME!
    * @param count DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public ResultSetVector getNext(String position, int count)
      throws SQLException
   {
      return dataHandler.getNext(position, count);
   }


   /**
    * DOCUMENT ME!
    *
    * @param position DOCUMENT ME!
    * @param count DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public ResultSetVector getPrev(String position, int count)
      throws SQLException
   {
      return dataHandler.getPrev(position, count);
   }


   /**
    * DOCUMENT ME!
    *
    * @param count DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public ResultSetVector getFirst(int count) throws SQLException
   {
      return dataHandler.getFirst(count);
   }


   /**
    * DOCUMENT ME!
    *
    * @param count DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public ResultSetVector getLast(int count) throws SQLException
   {
      return dataHandler.getLast(count);
   }


   /**
    * DOCUMENT ME!
    *
    * @param position DOCUMENT ME!
    * @param count DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public ResultSetVector getCurrent(String position, int count)
      throws SQLException
   {
      return dataHandler.getCurrent(position, count);
   }


   /**
    * DOCUMENT ME!
    *
    * @param fieldValues DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public void doInsert(FieldValues fieldValues) throws SQLException
   {
      dataHandler.doInsert(fieldValues);
   }


   /**
    * DOCUMENT ME!
    *
    * @param fieldValues DOCUMENT ME!
    * @param keyValuesStr DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public void doUpdate(FieldValues fieldValues, String keyValuesStr)
      throws SQLException
   {
      dataHandler.doUpdate(fieldValues, keyValuesStr);
   }


   /**
    * DOCUMENT ME!
    *
    * @param keyValuesStr DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public void doDelete(String keyValuesStr) throws SQLException
   {
      dataHandler.doDelete(keyValuesStr);
   }
}
