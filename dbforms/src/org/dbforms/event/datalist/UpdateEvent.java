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

import org.dbforms.util.ParseUtil;
import org.dbforms.util.Util;
import org.dbforms.util.MessageResourcesInternal;

import org.dbforms.config.DbEventInterceptor;
import org.dbforms.config.FieldValues;
import org.dbforms.config.GrantedPrivileges;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.MultipleValidationException;
import org.dbforms.config.SqlUtil;

import org.apache.log4j.Category;

import org.dbforms.event.ValidationEvent;
import org.dbforms.event.datalist.dao.DataSourceList;
import org.dbforms.event.datalist.dao.DataSourceFactory;



/**
 * This event prepares and performs a SQL-Update operation.
 * <br>
 * Works with new factory classes.
 *
 * @author Henner Kollmann <Henner.Kollmann@gmx.de>
 */
public class UpdateEvent extends ValidationEvent
{
   // logging category for this class
   private static Category logCat = Category.getInstance(UpdateEvent.class.getName()); 


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
		
		// 2003-08-05-HKK: first check if update is necessary before check security
		// which values do we find in request
		FieldValues fieldValues = getFieldValues();

		if (fieldValues.size() == 0)
		{
			return;
		}

      // Apply given security contraints (as defined in dbforms-config.xml)
      if (!hasUserPrivileg(GrantedPrivileges.PRIVILEG_UPDATE))
      {
			String s = MessageResourcesInternal.getMessage("dbforms.events.update.nogrant", 
																		  request.getLocale(),
																		  new String[]{table.getName()} 
																		  );
			throw new SQLException(s);
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
         catch (SQLException sqle)
         {
			SqlUtil.logSqlException(sqle, "::processEvent - SQL exception during PRE_UPDATE interceptors procession");
            throw sqle;
         }
         catch (MultipleValidationException mve)
         {
			logCat.error("::processEvent - MVE exception during PRE_UPDATE interceptors procession", mve);
            throw mve;
         }
      }

      // End of interceptor processing
      // in order to process an update, we need the key of the dataset to update
      String keyValuesStr = getKeyValues();

      if (Util.isNull(keyValuesStr))
      {
         logCat.error("::processEvent - at least one key is required per table, check your dbforms-config.xml");
         
         return;
      }

      // UPDATE operation;
      boolean mustClose = false;
      DataSourceList    ds       = DataSourceList.getInstance(request);
      DataSourceFactory qry      = ds.get(table, request);
      if (qry == null)
      {
         qry = new DataSourceFactory(table);
         mustClose = true;
      }      
      qry.doUpdate(con, fieldValues, keyValuesStr);
      if (mustClose)
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
			SqlUtil.logSqlException(sqle, "::processEvent - SQL exception during POST_UPDATE interceptors procession");
			throw sqle;
         }
      }

      // End of interceptor processing
   }
}
