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
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Category;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.SqlUtil;
import org.dbforms.util.FieldValues;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.Field;
import org.dbforms.config.GrantedPrivileges;
import org.dbforms.event.DatabaseEvent;
import org.dbforms.event.MultipleValidationException;
import org.dbforms.event.DbEventInterceptor;
import org.dbforms.event.datalist.dao.DataSourceFactory;



/**
 * This event prepares and performs a SQL-Insert operation.
 * <br>
 * Works with new factory classes
 *
 * @author Henner Kollmann <Henner.Kollmann@gmx.de>
 */
public class InsertEvent extends DatabaseEvent
{
   /** logging category for this class */
   static Category logCat = Category.getInstance(InsertEvent.class.getName());


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
   public InsertEvent(String             action, 
                      HttpServletRequest request,
                      DbFormsConfig      config)
   {
      super(ParseUtil.getEmbeddedStringAsInteger(action, 2, '_'),
            ParseUtil.getEmbeddedString(action, 3, '_'), 
            request, 
            config);
   }


   /**
    * Get the FieldValues attribute
    *
    * @return the FieldValues attribute
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
   public void processEvent(Connection con)
      throws SQLException, MultipleValidationException
   {
      // Applying given security contraints (as defined in dbforms-config xml file)
      // part 1: check if requested privilge is granted for role
      if (!hasUserPrivileg(GrantedPrivileges.PRIVILEG_INSERT))
      {
         throw new SQLException("Sorry, adding data to table "
                                + table.getName() 
                                + " is not granted for this session.");
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
            table.processInterceptors(DbEventInterceptor.PRE_INSERT, 
                                      request,
                                      associativeArray, 
                                      config, 
                                      con);

            // synchronize data which may be changed by interceptor:
            table.synchronizeData(fieldValues, associativeArray);
         }

         // better to log exceptions generated by method errors (fossato, 2002.12.04);
         catch (SQLException sqle)
         {
            SqlUtil.logSqlException(sqle, "SQL exception during PRE_INSERT interceptors procession");
            throw sqle;
         }
         catch (MultipleValidationException mve)
         {
            logCat.error("::processEvent - MVE exception during PRE_INSERT interceptors procession", mve);
            throw mve;
         }
      }

      // End of interceptor processing
      if (!checkSufficentValues(fieldValues))
      {
         throw new SQLException("unsufficent parameters");
      }

      // INSERT operation;
      DataSourceFactory qry = new DataSourceFactory(con, table);
      qry.doInsert(fieldValues);
      qry.close();

      // Show the last record inserted
      String firstPosition = table.getPositionString(fieldValues);
      request.setAttribute("firstpos_" + tableId, firstPosition);

      //end patch
      // finally, we process interceptor again (post-insert)
      if (table.hasInterceptors())
      {
         try
         {
            // process the interceptors associated to this table
            table.processInterceptors(DbEventInterceptor.POST_INSERT, 
                                      request,
                                      null, 
                                      config, 
                                      con);
         }
         catch (SQLException sqle)
         {
			SqlUtil.logSqlException(sqle, "SQL exception during POST_INSERT interceptors procession");
            throw sqle;
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
