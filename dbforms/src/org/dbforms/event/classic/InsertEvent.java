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
import java.util.Iterator;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Category;
import org.dbforms.util.FileHolder;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.Util;
import org.dbforms.util.UniqueIDGenerator;
import org.dbforms.util.MessageResourcesInternal;
import org.dbforms.config.Constants;
import org.dbforms.config.DbEventInterceptor;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.DbFormsConfigRegistry;
import org.dbforms.config.Field;
import org.dbforms.config.FieldTypes;
import org.dbforms.config.FieldValue;
import org.dbforms.config.FieldValues;
import org.dbforms.config.JDBCDataHelper;
import org.dbforms.config.ResultSetVector;
import org.dbforms.config.GrantedPrivileges;
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
      return getFieldValues(true);
   }


   /**
    *  Process this event.
    *
    * @param con the jdbc connection object
    * @throws SQLException if any data access error occurs
    * @throws MultipleValidationException if any validation error occurs
    */
   public void processEvent(Connection con) throws SQLException
   {
      // Applying given security contraints (as defined in dbforms-config xml file)
      // part 1: check if requested privilge is granted for role
      if (!hasUserPrivileg(GrantedPrivileges.PRIVILEG_INSERT))
      {
         String s = MessageResourcesInternal.getMessage(
                             "dbforms.events.insert.nogrant", 
                             getRequest().getLocale(), 
                             new String[] 
         {
            getTable().getName()
         });
         throw new SQLException(s);
      }

      FieldValues fieldValues = getFieldValues();

      if (fieldValues.size() == 0)
      {
         throw new SQLException("no parameters");
      }

      // process the interceptors associated to this table
      int operation = getTable().processInterceptors(DbEventInterceptor.PRE_INSERT, 
                                            getRequest(), fieldValues, getConfig(), 
                                            con);

      if ((operation == DbEventInterceptor.GRANT_OPERATION)
                && (fieldValues.size() > 0))
      {
         // End of interceptor processing
         if (!checkSufficentValues(fieldValues))
         {
            throw new SQLException("unsufficent parameters");
         }

         PreparedStatement ps = con.prepareStatement(getTable().getInsertStatement(
                                                              fieldValues));

         // now we provide the values;
         // every key is the parameter name from of the form page;
         Iterator enum = fieldValues.elements();
         int      col = 1;

         while (enum.hasNext())
         {
            FieldValue fv = (FieldValue) enum.next();

            if (fv != null)
            {
               Field curField = fv.getField();
               logCat.debug("Retrieved curField:" + curField.getName()
                            + " type:" + curField.getType());

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
                  FileHolder fileHolder = fv.getFileHolder();
                  String     fileName = fileHolder.getFileName();

                  if (curField.hasEncodedSet())
                  {
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
                  value = fv.getFieldValueAsObject();
               }

               logCat.info("PRE_INSERT: field=" + curField.getName() + " col="
                           + col + " value=" + value + " type=" + fieldType);
               JDBCDataHelper.fillWithData(ps, fv.getField().getEscaper(), col, value, fieldType, getTable().getBlobHandlingStrategy());
               col++;
            }
         }


         // execute the query & throws an exception if something goes wrong
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
                  FileHolder fileHolder = ParseUtil.getFileHolder(getRequest(), 
                                                                  "f_"
                                                                  + getTable().getId()
                                                                  + "_ins"
                                                                  + getKeyId() + "_"
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

         //Patch insert nav by Stefano Borghi
         //Show the last record inserted

         /** 
          * @todo Will not work if key field is autoinc!!
          */
         String       firstPosition = null;
         Vector       key     = getTable().getKey();
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

         ResultSetVector resultSetVector = getTable().doConstrainedSelect(
         getTable().getFields(), fvEqual, 
                                                    null, null, null, 
                                                    Constants.COMPARE_NONE, 1, 
                                                    con);

         if (resultSetVector != null)
         {
            resultSetVector.setPointer(0);
            firstPosition = getTable().getPositionString(resultSetVector);
         }

         getRequest().setAttribute("firstpos_" + getTable().getId(), firstPosition);

         // finally, we process interceptor again (post-insert)
         // process the interceptors associated to this table
         getTable().processInterceptors(DbEventInterceptor.POST_INSERT, getRequest(), fieldValues, 
                                   getConfig(), con);
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
      Vector fields = getTable().getFields();

      for (int i = 0; i < fields.size(); i++)
      {
         Field field = (Field) fields.elementAt(i);

         // if a field is a key and if it is NOT automatically generated,
         // then it should be provided by the user
         if (!field.hasAutoIncSet() && field.hasIsKeySet())
         {
            if (fieldValues.get(field.getName()) == null)
            {
               throw new SQLException("Field " + field.getName()
                                      + " is missing");
            }
         }

         // in opposite, if a field is automatically generated by the RDBMS, we need to
         else if (field.hasAutoIncSet())
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