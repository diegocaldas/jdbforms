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

import java.util.Hashtable;
import java.sql.SQLException;
import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;

import org.dbforms.util.Util;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.FieldValues;
import org.dbforms.config.GrantedPrivileges;
import org.dbforms.config.DbFormsConfig;
import org.apache.log4j.Category;
import org.dbforms.event.DatabaseEvent;
import org.dbforms.event.MultipleValidationException;
import org.dbforms.event.DbEventInterceptor;
import org.dbforms.event.datalist.dao.DataSourceFactory;



/**
 * This event prepares and performs a SQL-Update operation.
 * <br>
 * Works with new factory classes.
 *
 * @author Henner Kollmann <Henner.Kollmann@gmx.de>
 */
public class UpdateEvent extends DatabaseEvent
{
//	logging category for this class
   static Category logCat = Category.getInstance(UpdateEvent.class.getName()); 


   /**
    * Creates a new UpdateEvent object.
    *
    * @param tableId the table identifier
    * @param keyId   the key 
    * @param request the request object
    * @param config  the configuration object
    */
   public UpdateEvent(Integer            tableId, 
                      String             keyId,
      				  HttpServletRequest request, 
      				  DbFormsConfig      config)
   {
      super(tableId.intValue(), keyId, request, config);
   }


   /**
    * Creates a new UpdateEvent object.
    *
    * @param action  the action string
    * @param request the request object
    * @param config  the configuration object
    */
   public UpdateEvent(String             action, 
                      HttpServletRequest request,
      				  DbFormsConfig      config)
   {
      super(ParseUtil.getEmbeddedStringAsInteger(action, 2, '_'),
            ParseUtil.getEmbeddedString(action, 3, '_'), 
            request, 
            config);
   }


   /**
    *  Get the FieldValues object.
    *
    * @return the FieldValues object
    */
   public FieldValues getFieldValues()
   {
      return getFieldValues(false);
   }


   /**
    * Process this event.
    *
    * @param con the connection object
    *
    * @throws SQLException                if any SQL error occurs
    * @throws MultipleValidationException if any validation error occurs
    */
   public void processEvent(Connection con)
      throws SQLException, MultipleValidationException
   {
      // Apply given security contraints (as defined in dbforms-config.xml)
      if (!hasUserPrivileg(GrantedPrivileges.PRIVILEG_UPDATE))
      {
         throw new SQLException("Sorry, updating table " + table.getName()
                                + " is not granted for this session.");
      }

      // which values do we find in request
      FieldValues fieldValues = getFieldValues();

      if (fieldValues.size() == 0)
      {
         return;
      }

      // part 2: check if there are interceptors to be processed (as definied by
      // "interceptor" element embedded in table element in dbforms-config xml file)
      if (table.hasInterceptors())
      {
         try
         {
            Hashtable associativeArray = getAssociativeFieldValues(fieldValues);

            // process the interceptors associated to this table
            table.processInterceptors(DbEventInterceptor.PRE_UPDATE, 
                                      request,
               						  associativeArray, 
               						  config, 
               						  con);

            // synchronize data which may be changed by interceptor:
            table.synchronizeData(fieldValues, associativeArray);
         }
         
		 // [2003.07.10 - fossato] catch and throw without logging ? Is not useful...
		 //                        can we delethe the try statement ??
         //
         catch (SQLException sqle)
         {
            // PG = 2001-12-04
            // No need to add extra comments, just re-throw exceptions as SqlExceptions
            //throw new SQLException(sqle.getMessage());
            throw sqle;
         }
         catch (MultipleValidationException mve)
         {
            // PG, 2001-12-14
            // Support for multiple error messages in one interceptor
            //throw new MultipleValidationException(mve.getMessages());
            throw mve;
         }
      }

      // End of interceptor processing
      // in order to process an update, we need the key of the dataset to update
      String keyValuesStr = getKeyValues();

      if (Util.isNull(keyValuesStr))
      {
         logCat.error("At least one key is required per table, check your dbforms-config.xml");
         
         return;
      }

      // UPDATE operation;
      DataSourceFactory qry = new DataSourceFactory(con, table);
      qry.doUpdate(fieldValues, keyValuesStr);
      qry.close();

      // finally, we process interceptor again (post-update)
      if (table.hasInterceptors())
      {
         try
         {
            // process the interceptors associated to this table
            table.processInterceptors(DbEventInterceptor.POST_UPDATE, request, null, config, con);
         }
         catch (SQLException sqle)
         {
            // PG = 2001-12-04
            // No need to add extra comments, just re-throw exceptions as SqlExceptions
            //throw new SQLException(sqle.getMessage());
			throw sqle;
         }
      }

      // End of interceptor processing
   }
}
