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
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.http.*;
import org.dbforms.util.*;
import org.apache.log4j.Category;
import org.dbforms.config.*;
import org.dbforms.event.*;



/****
 *
 * @deprecated
 *
 *
 * <p>This event prepares and performs a SQL-Update operation</p>
 *
 * @author Joe Peer
 */
public class UpdateEvent extends ValidationEvent
{
   static Category logCat = Category.getInstance(UpdateEvent.class.getName()); // logging category for this class

   /**
    * Creates a new UpdateEvent object.
    *
    * @param tableId DOCUMENT ME!
    * @param keyId DOCUMENT ME!
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    */
   public UpdateEvent(Integer tableId, String keyId, HttpServletRequest request, 
                      DbFormsConfig config)
   {
      super(tableId.intValue(), keyId, request, config);
   }


   /**
    * Creates a new UpdateEvent object.
    *
    * @param action DOCUMENT ME!
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    */
   public UpdateEvent(String action, HttpServletRequest request, 
                      DbFormsConfig config)
   {
      super(ParseUtil.getEmbeddedStringAsInteger(action, 2, '_'), 
            ParseUtil.getEmbeddedString(action, 3, '_'), request, config);
   }

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public FieldValues getFieldValues()
   {
      return getFieldValues(false);
   }


   /**
    * DOCUMENT ME!
    *
    * @param con DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    * @throws MultipleValidationException DOCUMENT ME!
    * @throws IllegalArgumentException DOCUMENT ME!
    */
   public void processEvent(Connection con) throws SQLException
   {
      // Apply given security contraints (as defined in dbforms-config.xml)
      if (!hasUserPrivileg(GrantedPrivileges.PRIVILEG_UPDATE))
      {
         String s = MessageResourcesInternal.getMessage(
                             "dbforms.events.update.nogrant", 
                             request.getLocale(), 
                             new String[] 
         {
            getTable().getName()
         });
         throw new SQLException(s);
      }

      // which values do we find in request
      FieldValues fieldValues = getFieldValues();

      if (fieldValues.size() == 0)
      {
         logCat.info("no parameters to update found");

         return;
      }

      // part 2: check if there are interceptors to be processed (as definied by
      // "interceptor" element embedded in table element in dbforms-config xml file)
      int operation = DbEventInterceptor.GRANT_OPERATION;


      // process the interceptors associated to this table
      getTable()
         .processInterceptors(DbEventInterceptor.PRE_UPDATE, request, 
                              fieldValues, getConfig(), con);

      if ((operation != DbEventInterceptor.IGNORE_OPERATION)
                && (fieldValues.size() > 0))
      {
         // End of interceptor processing
         // in order to process an update, we need the key of the dataset to update
         //
         // new since version 0.9:
         // key format: FieldID ":" Length ":" Value
         // example: if key id = 121 and field id=2 then keyValueStr contains "2:3:121"
         //
         // if the key consists of more than one fields, the key values are seperated through "-"
         // example: value of field 1=12, value of field 3=1992, then we'll get "1:2:12-3:4:1992"
         String keyValuesStr = getKeyValues();

         if ((keyValuesStr == null) || (keyValuesStr.trim().length() == 0))
         {
            logCat.error(
                     "At least one key is required per table, check your dbforms-config.xml");

            return;
         }

         // now we start building the UPDATE statement
         // 20021031-HKK: Moved into table
         PreparedStatement ps = con.prepareStatement(getTable()
                                                        .getUpdateStatement(fieldValues));

         // now we provide the values
         // first, we provide the "new" values for fields
         Iterator enum = fieldValues.elements();
         int      col = 1;

         while (enum.hasNext())
         {
            FieldValue fv = (FieldValue) enum.next();

            if (fv != null)
            {
               Field  curField  = fv.getField();
               int    fieldType = curField.getType();
               Object value     = null;

               if (fieldType == FieldTypes.BLOB)
               {
                  // in case of a BLOB we supply the FileHolder object to SqlUtils for further operations
                  logCat.info("we are looking for fileholder with name: f_"
                              + getTable().getId() + "_" + getKeyId() + "_"
                              + curField.getId());
                  value = fv.getFileHolder();
                  logCat.info("and found a value=" + value);
               }
               else if (fieldType == FieldTypes.DISKBLOB)
               {
                  FileHolder fileHolder = fv.getFileHolder();
                  String     fileName = fileHolder.getFileName();

                  // check if we need to store it encoded or not
                  if (curField.hasEncodedSet())
                  {
                     // encode fileName
                     int    dotIndex = fileName.lastIndexOf('.');
                     String suffix = (dotIndex != -1)
                                        ? fileName.substring(dotIndex) : "";
                     fileHolder.setFileName(UniqueIDGenerator.getUniqueID()
                                            + suffix);


                     // a diskblob gets stored to db as an ordinary string (it's only the reference!)
                     value = fileHolder.getFileName();
                  }
                  else
                  {
                     // a diskblob gets stored to db as an ordinary string	 (it's only the reference!)
                     value = fileName;
                  }
               }
               else
               {
                  // in case of simple db types we just supply a string representing the value of the fields
                  value = fv.getFieldValueAsObject();
               }

               JDBCDataHelper.fillWithData(ps, fv.getField().getEscaper(), col, value, fieldType, getTable().getBlobHandlingStrategy());
               col++;
            }
         }

         getTable().populateWhereClauseWithKeyFields(keyValuesStr, ps, col);


         // we are now ready to execute the query
         ps.executeUpdate();
         ps.close(); // #JP Jun 27, 2001

         enum = fieldValues.keys();

         while (enum.hasNext())
         {
            String fieldName = (String) enum.next();
            Field  curField = getTable().getFieldByName(fieldName);

            if (curField != null)
            {
               int    fieldType = curField.getType();

               String directory = null;

               try
               {
                  directory = Util.replaceRealPath(curField.getDirectory(), 
                                                   DbFormsConfigRegistry.instance()
                                                                        .lookup()
                                                                        .getRealPath());
               }
               catch (Exception e)
               {
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
                                                                  "f_"
                                                                  + getTable()
                                                                       .getId()
                                                                  + "_" + getKeyId()
                                                                  + "_"
                                                                  + curField.getId());

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


      // finally, we process interceptor again (post-update)
      // process the interceptors associated to this table
      getTable()
         .processInterceptors(DbEventInterceptor.POST_UPDATE, request, null, 
                              getConfig(), con);

      // End of interceptor processing
   }
}