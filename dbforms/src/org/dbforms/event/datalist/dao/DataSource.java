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
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.Category;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.Field;
import org.dbforms.config.Table;
import org.dbforms.util.FieldValue;
import org.dbforms.util.FieldTypes;
import org.dbforms.util.FieldValues;
import org.dbforms.util.FileHolder;
import org.dbforms.util.ResultSetVector;
import org.dbforms.util.Util;



/**
 * Abstract base class for DataSource.
 * 
 * @author hkk
 */
public abstract class DataSource
{
   /** DOCUMENT ME! */
   protected Category logCat = Category.getInstance(this.getClass().getName());
   private Table      table;

   /**
    * Creates a new DataSource object.
    * 
    * @param table table for the DataSource
    */
   public DataSource(Table table)
   {
      this.table = table;
   }

   /**
    * gets the Table for the DataSource
    * 
    * @return Table
    */
   public Table getTable()
   {
      return table;
   }


   /**
    * set the connection parameter for the DataSouce virtual method, if you
    * need connection data you must override the method
    * 
    * @param config             DbFormConfig object
    * @param dbConnectionName    name of the connection to use
    */
   public void setConnection(DbFormsConfig config, String dbConnectionName)
   {
   }


   /**
    * set the connection parameter for the DataSouce virtual method, if you
    * need connection data you must override the method
    * 
    * @param con this costructor gets an SQL Connection object
    */
   public void setConnection(Connection con)
   {
   }


   /**
    * Sets the select data for this dataSource for free form selects. default
    * methods just raises an exception
    * 
    * @param tableList      the list of tables involved into the query
    * @param whereClause    free-form whereClause to be appended to query
    * 
    * @throws SQLException
    */
   public void setSelect(String tableList, String whereClause)
                  throws SQLException
   {
      throw new SQLException("Free form select not implemented");
   }


   /**
    * Sets the select data for this dataSource
    * 
    * @param filterConstraint FieldValue array used to restrict a set in a
    *        resultset
    * @param orderConstraint FieldValue array used to build a cumulation of
    *        rules for ordering (sorting)
    * @param maxRows        max rows to fetcht
    */
   public abstract void setSelect(FieldValue[] filterConstraint, 
                                  FieldValue[] orderConstraint);


   /**
    * performs an insert into the DataSource
    * 
    * @param fieldValues FieldValues to insert
    * 
    * @throws SQLException
    */
   public void doInsert(FieldValues fieldValues) throws SQLException
   {
   }


   /**
    * performs an update into the DataSource
    * 
    * @param fieldValues    FieldValues to update
    * @param keyValuesStr   keyValueStr to the row to update key format:
    *        FieldID ":" Length ":" Value example: if key id = 121 and field
    *        id=2 then keyValueStr contains "2:3:121" if the key consists of
    *        more than one fields, the key values are seperated through "-"
    *        example: value of field 1=12, value of field 3=1992, then we'll
    *        get "1:2:12-3:4:1992"
    * 
    * @throws SQLException
    */
   public void doUpdate(FieldValues fieldValues, String keyValuesStr)
                 throws SQLException
   {
   }


   /**
    * performs an delete in the DataSource
    * 
    * @param keyValuesStr   keyValueStr to the row to update key format:
    *        FieldID ":" Length ":" Value example: if key id = 121 and field
    *        id=2 then keyValueStr contains "2:3:121" if the key consists of
    *        more than one fields, the key values are seperated through "-"
    *        example: value of field 1=12, value of field 3=1992, then we'll
    *        get "1:2:12-3:4:1992"
    * 
    * @throws SQLException
    */
   public void doDelete(String keyValuesStr) throws SQLException
   {
   }


   /**
    * should close all open datasets
    */
   public void close()
   {
   }


   /**
    * Will be called to open all datasets
    * 
    * @throws SQLException
    */
   protected abstract void open() throws SQLException;


   /**
    * Must return the size of the whole resultset with all data fetch
    * 
    * @return size of whole resultset
    * 
    * @throws SQLException
    */
   protected abstract int size() throws SQLException;


   /**
    * should retrieve the row at an special index as an Object[]
    * 
    * @param i index of row to fetch
    * 
    * @return Object[] of the fetched row
    * 
    * @throws SQLException
    */
   protected abstract Object[] getRow(int i) throws SQLException;


   /**
    * maps the startRow to the internal index
    * 
    * @param startRow       keyValueStr to the row key format: FieldID ":"
    *        Length ":" Value example: if key id = 121 and field id=2 then
    *        keyValueStr contains "2:3:121" if the key consists of more than
    *        one fields, the key values are seperated through "-" example:
    *        value of field 1=12, value of field 3=1992, then we'll get
    *        "1:2:12-3:4:1992"
    * 
    * @return the index of the row, 0 as first row if not found
    * 
    * @throws SQLException
    */
   protected abstract int findStartRow(String startRow)
                                throws SQLException;


   /**
    * get count next rows from position
    * 
    * @param position       keyValueStr to the row key format: FieldID ":"
    *        Length ":" Value example: if key id = 121 and field id=2 then
    *        keyValueStr contains "2:3:121" if the key consists of more than
    *        one fields, the key values are seperated through "-" example:
    *        value of field 1=12, value of field 3=1992, then we'll get
    *        "1:2:12-3:4:1992"
    * @param count          count of rows to fetch
    * 
    * @return the fetched ResultSetVector
    * 
    * @throws SQLException
    */
   public ResultSetVector getNext(String position, int count)
                           throws SQLException
   {
      open();

      int start = findStartRow(position) + 1;

      if (count == 0)
      {
         count = size() - start;
      }

      return getResultSetVector(start, count);
   }


   /**
    * get count rows backwards from position
    * 
    * @param position       keyValueStr to the row key format: FieldID ":"
    *        Length ":" Value example: if key id = 121 and field id=2 then
    *        keyValueStr contains "2:3:121" if the key consists of more than
    *        one fields, the key values are seperated through "-" example:
    *        value of field 1=12, value of field 3=1992, then we'll get
    *        "1:2:12-3:4:1992"
    * @param count          count of rows to fetch
    * 
    * @return the fetched ResultSetVector
    * 
    * @throws SQLException
    */
   public ResultSetVector getPrev(String position, int count)
                           throws SQLException
   {
      open();

      int start = findStartRow(position) - 1;

      if (count == 0)
      {
         count = start;
      }

      return getResultSetVector(start, -count);
   }


   /**
    * get count rows from first row
    * 
    * @param count          count of rows to fetch
    * 
    * @return the fetched ResultSetVector
    * 
    * @throws SQLException
    */
   public ResultSetVector getFirst(int count) throws SQLException
   {
      open();

      if (count == 0)
      {
         count = size();
      }

      return getResultSetVector(0, count);
   }


   /**
    * get count rows from last row
    * 
    * @param count          count of rows to fetch
    * 
    * @return the fetched ResultSetVector
    * 
    * @throws SQLException
    */
   public ResultSetVector getLast(int count) throws SQLException
   {
      open();
      return getResultSetVector(size() - 1, -count);
   }


   /**
    * get count rows from position
    * 
    * @param position       keyValueStr to the row key format: FieldID ":"
    *        Length ":" Value example: if key id = 121 and field id=2 then
    *        keyValueStr contains "2:3:121" if the key consists of more than
    *        one fields, the key values are seperated through "-" example:
    *        value of field 1=12, value of field 3=1992, then we'll get
    *        "1:2:12-3:4:1992"
    * @param count          count of rows to fetch
    * 
    * @return the fetched ResultSetVector
    * 
    * @throws SQLException
    */
   public ResultSetVector getCurrent(String position, int count)
                              throws SQLException
   {
      open();

      int start = findStartRow(position);

      if (count == 0)
      {
         count = size() - start;
      }

      return getResultSetVector(start, count);
   }


   private ResultSetVector getResultSetVector(int startRow, int count)
                                       throws SQLException
   {
      ResultSetVector result = null;
      result = new ResultSetVector(table.getFields());

      Object[] row;

      if (count > 0)
      {
         for (int i = startRow; i < (startRow + count); i++)
         {
            row = getRow(i);

            if (row == null)
            {
               break;
            }

            result.addRow(row);
         }
      }
      else if (count < 0)
      {
         int begin = startRow + count + 1;

         if (begin < 0)
         {
            begin = 0;
         }

         for (int i = begin; i <= startRow; i++)
         {
            row = getRow(i);

            if (row == null)
            {
               break;
            }

            result.addRow(row);
         }
      }

      return result;
   }


   /**
    * save the blob files to disk
    * 
    * @param fieldValues    FieldValues to update
    * 
    * @throws SQLException
    * @throws IllegalArgumentException
    */
   protected void saveBlobFilesToDisk(FieldValues fieldValues)
                               throws SQLException
   {
      Enumeration enum = fieldValues.keys();

      while (enum.hasMoreElements())
      {
         String fieldName = (String) enum.nextElement();
         Field  curField = table.getFieldByName(fieldName);

         if (curField != null)
         {
            int    fieldType = curField.getType();
            String directory = curField.getDirectory();

            if (fieldType == FieldTypes.DISKBLOB)
            {
               // check if directory-attribute was provided
               if (directory == null)
               {
                  throw new IllegalArgumentException(
                           "directory-attribute needed for fields of type DISKBLOB");
               }

               // instanciate file object for that dir
               File dir = new File(directory);

               // Check saveDirectory is truly a directory
               if (!dir.isDirectory())
               {
                  throw new IllegalArgumentException("Not a directory: "
                                                     + directory);
               }

               // Check saveDirectory is writable
               if (!dir.canWrite())
               {
                  throw new IllegalArgumentException("Not writable: "
                                                     + directory);
               }

               // dir is ok so lets store the filepart
               FileHolder fileHolder = fieldValues.get(fieldName)
                                                  .getFileHolder();

               if (fileHolder != null)
               {
                  try
                  {
                     fileHolder.writeBufferToFile(dir);


                     //filePart.getInputStream().close();
                     logCat.info("fin + closedy");
                  }
                  catch (IOException ioe)
                  {
                     //#checkme: this would be a good place for rollback in database!!
                     throw new SQLException("could not store file '"
                                            + fileHolder.getFileName()
                                            + "' to dir '" + directory + "'");
                  }
               }
               else
               {
                  logCat.info("uh! empty fileHolder");
               }
            }
         }
      }
   }


   /**
    * save the blob files to disk
    * 
    * @param fieldValues    FieldValues to delete, called by
    * 
    * @throws SQLException
    */
   protected void deleteBlobFilesFromDisk(FieldValues fieldValues)
                                   throws SQLException
   {
      Enumeration enum = fieldValues.keys();

      while (enum.hasMoreElements())
      {
         String fieldName = (String) enum.nextElement();
         Field  curField = table.getFieldByName(fieldName);

         if (curField != null)
         {
            int    fieldType = curField.getType();
            String directory = curField.getDirectory();

            if (fieldType == FieldTypes.DISKBLOB)
            {
               String fileName = fieldValues.get(fieldName).getFieldValue()
                                            .trim();

               // get a filename
               if (!Util.isNull(fileName))
               {
                  // may be SQL NULL, in that case we'll skip it
                  String dir = curField.getDirectory();

                  // remember: every field may have its own storing dir!
                  File file = new File(dir, fileName);

                  if (file.exists())
                  {
                     file.delete();
                     logCat.info("deleted file " + fileName + " from dir "
                                 + dir);
                  }
                  else
                  {
                     logCat.info("delete of file " + fileName + " from dir "
                                 + dir + " failed because file not found");
                  }
               }
            }
         }
      }
   }
}