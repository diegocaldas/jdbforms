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
package org.dbforms.event.classic;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Category;

import org.dbforms.util.FieldTypes;
import org.dbforms.util.FieldValue;
import org.dbforms.util.FileHolder;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.ResultSetVector;
import org.dbforms.util.SqlUtil;
import org.dbforms.util.Util;
import org.dbforms.util.UniqueIDGenerator;
import org.dbforms.util.FieldValues;
import org.dbforms.util.Constants;
import org.dbforms.util.MessageResourcesInternal;

import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.DbFormsConfigRegistry;
import org.dbforms.config.Field;
import org.dbforms.config.GrantedPrivileges;

import org.dbforms.event.DbEventInterceptor;
import org.dbforms.event.MultipleValidationException;
import org.dbforms.event.ValidationEvent;

/****
 * 
 * @deprecated
 * 
 *  This event prepares and performs a SQL-Insert operation.
 *
 * @author Joe Peer
 */
public class InsertEvent extends ValidationEvent
{
   /** logging category for this class */
   static Category logCat = Category.getInstance(InsertEvent.class.getName());

   /**
    *  Insert actionbutton-strings is as follows: ac_insert_12_root_3
    *  which is equivalent to:
    *
    *       ac_insert  : insert action event
    *       12         : table id
    *       root       : key
    *       3          : button count used to identify individual insert buttons
    */
   public InsertEvent(String action, HttpServletRequest request,
      DbFormsConfig config)
   {
      super(ParseUtil.getEmbeddedStringAsInteger(action, 2, '_'),
         ParseUtil.getEmbeddedString(action, 3, '_'), request, config);
   }

   /**
    *  Get the hash table containing the form field names and values taken
    *  from the request object.
    *  <br>
    *  Example of a request parameter:<br>
    *  <code>
    *    name  = f_0_insroot_6
    *    value = foo-bar
    *  </code>
    *
    * @return the hash map containing the names and values taken from
    *         the request object
    */
   public FieldValues getFieldValues()
   {
      ;

      return getFieldValues(true);
   }


   /**
    *  Process this event.
    *
    * @param con the jdbc connection object
    * @throws SQLException if any data access error occurs
    * @throws MultipleValidationException if any validation error occurs
    */
   public void processEvent(Connection con)
      throws SQLException, MultipleValidationException
   {
      // Applying given security contraints (as defined in dbforms-config xml file)
      // part 1: check if requested privilge is granted for role
      if (!hasUserPrivileg(GrantedPrivileges.PRIVILEG_INSERT))
      {
		 String s = MessageResourcesInternal.getMessage("dbforms.events.insert.nogrant", 
																		request.getLocale(),
																		new String[]{table.getName()} 
																		);
		 throw new SQLException(s);
      }

      FieldValues fieldValues = getFieldValues();

      if (fieldValues.size() == 0)
      {
         throw new SQLException("no parameters");
      }

      // part 2: check if there are interceptors to be processed (as definied by
      // "interceptor" element embedded in table element in dbforms-config xml file)
      if (table.hasInterceptors())
      {
         try
         {
            Hashtable associativeArray = getAssociativeFieldValues(fieldValues);

            // process the interceptors associated to this table
            table.processInterceptors(DbEventInterceptor.PRE_INSERT, request,
               associativeArray, config, con);

            // synchronize data which may be changed by interceptor:
            table.synchronizeData(fieldValues, associativeArray);
         }

         // better to log exceptions generated by method errors (fossato, 2002.12.04);
         catch (SQLException sqle)
         {
            // PG = 2001-12-04
            // No need to add extra comments, just re-throw exception
            SqlUtil.logSqlException(sqle);
            throw sqle;
         }
         catch (MultipleValidationException mve)
         {
            // PG, 2001-12-14
            // Support for multiple error messages in one interceptor
            logCat.error("::processEvent - MultipleValidationException", mve);
            throw mve;
         }
      }

      // End of interceptor processing
      if (!checkSufficentValues(fieldValues))
      {
         throw new SQLException("unsufficent parameters");
      }

      PreparedStatement ps = con.prepareStatement(table.getInsertStatement(
               fieldValues));

      // now we provide the values;
      // every key is the parameter name from of the form page;
      Enumeration enum = fieldValues.keys();
      int         col = 1;

      while (enum.hasMoreElements())
      {
         String fieldName = (String) enum.nextElement();
         Field  curField = table.getFieldByName(fieldName);

         if (curField != null)
         {
            //logCat.debug("Retrieved curField:" + curField.getName() + " type:" + curField.getFieldType());
            int    fieldType = curField.getType();
            Object value = null;

            if (fieldType == FieldTypes.BLOB)
            {
               // in case of a BLOB we supply the FileHolder object to SqlUtils for further operations
               value = ParseUtil.getFileHolder(request,
                     "f_" + tableId + "_ins" + keyId + "_" + curField.getId());
            }
            else if (fieldType == FieldTypes.DISKBLOB)
            {
               // check if we need to store it encoded or not
               if ("true".equals(curField.getEncoding()))
               {
                  FileHolder fileHolder = ParseUtil.getFileHolder(request,
                        "f_" + tableId + "_ins" + keyId + "_"
                        + curField.getId());

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
                  value = fieldValues.get(curField.getName()).getFieldValue();
               }
            }
            else
            {
               // in case of simple db types we just supply a string representing the value of the fields
               value = fieldValues.get(curField.getName()).getFieldValue();
            }

            logCat.info("PRE_INSERT: field=" + curField.getName() + " col="
               + col + " value=" + value + " type=" + fieldType);
            SqlUtil.fillPreparedStatement(ps, col, value, fieldType);
            col++;
         }
      }

      // execute the query & throws an exception if something goes wrong
      ps.executeUpdate();
      ps.close(); // #JP Jun 27, 2001
      enum = fieldValues.keys();

      while (enum.hasMoreElements())
      {
         String fieldName = (String) enum.nextElement();
         Field  curField = table.getFieldByName(fieldName);

         if (curField != null)
         {
            int    fieldType = curField.getType();

				String directory = null;
				try {
					directory = Util.replaceRealPath(curField.getDirectory(), 
																  DbFormsConfigRegistry.instance().lookup());
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}

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
               FileHolder fileHolder = ParseUtil.getFileHolder(request,
                     "f_" + tableId + "_ins" + keyId + "_" + curField.getId());

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
                        + fileHolder.getFileName() + "' to dir '" + directory
                        + "'");
                  }
               }
               else
               {
                  logCat.info("uh! empty fileHolder");
               }
            }
         }
      }

      //Patch insert nav by Stefano Borghi
      //Show the last record inserted
      /** 
       * @todo Will not work if key field is autoinc!!
       */
      String       firstPosition = null;
      Vector       key     = table.getKey();
      FieldValue[] fvEqual = new FieldValue[key.size()];

      for (int i = 0; i < key.size(); i++)
      {
         Field      field     = (Field) key.elementAt(i);
         String     fieldName = field.getName();
         FieldValue fv        = fieldValues.get(fieldName);
         String     value     = null;

         if (fv != null)
         {
            value = fv.getFieldValue();
         }

         FieldValue keyFieldValue = new FieldValue(field, value);
         fvEqual[i] = keyFieldValue;
      }

	  /* 
	   * @todo nullify sqlFilter, check if is logically correct
	   */ 
      ResultSetVector resultSetVector = table.doConstrainedSelect(table
            .getFields(), fvEqual, null, "", Constants.COMPARE_NONE, 1, con);

      if (resultSetVector != null)
      {
         resultSetVector.setPointer(0);
         firstPosition = table.getPositionString(resultSetVector);
      }

      request.setAttribute("firstpos_" + tableId, firstPosition);

      //end patch
      // finally, we process interceptor again (post-insert)
      if (table.hasInterceptors())
      {
         try
         {
            // process the interceptors associated to this table
            table.processInterceptors(DbEventInterceptor.POST_INSERT, request,
               null, config, con);
         }
         catch (SQLException sqle)
         {
            // PG = 2001-12-04
            // No need to add extra comments, just re-throw exceptions as SqlExceptions
            throw new SQLException(sqle.getMessage());
         }
      }
   }


   /**
    *  Check if the input hash table has got sufficent parameters.
    *
    * @param fieldValues the hash map containing the names and values taken from
    *                    the request object
    * @return true  if the hash table has got sufficent parameters,
    *         false otherwise
    * @throws SQLException  if any data access error occurs
    */
   private boolean checkSufficentValues(FieldValues fieldValues)
      throws SQLException
   {
      Vector fields = table.getFields();

      for (int i = 0; i < fields.size(); i++)
      {
         Field field = (Field) fields.elementAt(i);

         // if a field is a key and if it is NOT automatically generated,
         // then it should be provided by the user
         if (!field.getIsAutoInc() && field.isKey())
         {
            if (fieldValues.get(field.getName()) == null)
            {
               throw new SQLException("Field " + field.getName()
                  + " is missing");
            }
         }

         // in opposite, if a field is automatically generated by the RDBMS, we need to
         else if (field.getIsAutoInc())
         {
            if (fieldValues.get(field.getName()) != null)
            {
               throw new SQLException("Field " + field.getName()
                  + " should be calculated by RDBMS, remove it from the form");
            }
         }

         // in future we could do some other checks like NOT-NULL conditions,etc.
      }

      return true;
   }
}
