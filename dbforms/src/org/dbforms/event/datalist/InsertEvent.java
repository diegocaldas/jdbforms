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

package org.dbforms.event.datalist;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.dbforms.config.DbEventInterceptor;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.Field;
import org.dbforms.config.FieldValues;
import org.dbforms.config.GrantedPrivileges;
import org.dbforms.event.ValidationEvent;
import org.dbforms.event.datalist.dao.DataSourceList;
import org.dbforms.event.datalist.dao.DataSourceFactory;
import org.dbforms.util.MessageResourcesInternal;
import org.dbforms.util.ParseUtil;



/**
 * This event prepares and performs a SQL-Insert operation.
 * <br>
 * Works with new factory classes
 *
 * @author Henner Kollmann <Henner.Kollmann@gmx.de>
 */
public class InsertEvent extends ValidationEvent
{
   /**
    *  Constructor.
    *  <br>
    *  Insert actionbutton-strings is as follows: ac_insert_12_root_3
    *  which is equivalent to:
    *
    *       ac_insert  : insert action event
    *       12         : table id
    *       root       : key
    *       3          : button count used to identify individual insert buttons
    *
    * @param  action  the action string
    * @param  request the request object
    * @param  config  the config object
    */
   public InsertEvent(String action, HttpServletRequest request, 
                      DbFormsConfig config)
   {
      super(ParseUtil.getEmbeddedStringAsInteger(action, 2, '_'), 
            ParseUtil.getEmbeddedString(action, 3, '_'), request, config);
   }

   /**
    * Get the FieldValues object representing the collection
    * of FieldValue objects builded from the request parameters
    *
    * @return the FieldValues object representing the collection
    *         of FieldValue objects builded from the request parameters
    */
   public FieldValues getFieldValues()
   {
      return getFieldValues(true);
   }


   /**
    *  Process this event.
    *
    * @param  con the jdbc connection object
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
                             request.getLocale(), 
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

      // part 2: check if there are interceptors to be processed (as definied by
      // "interceptor" element embedded in table element in dbforms-config xml file)
      int operation = DbEventInterceptor.GRANT_OPERATION;


      // process the interceptors associated to this table
      operation = getTable()
                     .processInterceptors(DbEventInterceptor.PRE_INSERT, 
                                          request, fieldValues, getConfig(), 
                                          con);

      if ((operation != DbEventInterceptor.IGNORE_OPERATION)
                && (fieldValues.size() > 0))
      {
         // End of interceptor processing
         if (!checkSufficentValues(fieldValues))
         {
            throw new SQLException("unsufficent parameters");
         }

         // INSERT operation;
         boolean           mustClose = false;
         DataSourceList    ds  = DataSourceList.getInstance(request);
         DataSourceFactory qry = ds.get(getTable(), request);

         if (qry == null)
         {
            qry       = new DataSourceFactory(getTable());
            mustClose = true;
         }

         qry.doInsert(con, fieldValues);

         if (mustClose)
         {
            qry.close();
         }

         // Show the last record inserted
         String firstPosition = getTable().getPositionString(fieldValues);
         request.setAttribute("firstpos_" + getTable().getId(), firstPosition);
      }


      //end patch
      // finally, we process interceptor again (post-insert)
      // process the interceptors associated to this table
      getTable()
         .processInterceptors(DbEventInterceptor.POST_INSERT, request, null, 
                              getConfig(), con);
   }


   /**
    *  Check a list of conditions on the the input FieldValues object:
    *  <ul>
    *    <li>it must contains the same number of fields as the current main Table</li>
    *    <li>
    *      any autoInc field must NOT have a related FieldValue object (generated by a request
    *      parameter) because that field value must be calculated by the underlying RDBMS
    *    </li>
    *  </ul>
    *
    * @param fieldValues the FieldValues object containing the Field elements to check
    * @return true  if the  all the above conditions are true, false otherwise
    * @throws SQLException  if any check condition fails
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
         if (!field.isAutoInc() && field.getKey())
         {
            if (fieldValues.get(field.getName()) == null)
            {
               throw new SQLException("Field " + field.getName()
                                      + " is missing");
            }
         }

         // in opposite, if a field is automatically generated by the RDBMS, we need to
         else if (field.isAutoInc())
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