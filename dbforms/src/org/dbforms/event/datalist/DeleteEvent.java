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
import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import org.apache.log4j.Category;
import org.dbforms.config.DbEventInterceptor;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.FieldValues;
import org.dbforms.config.GrantedPrivileges;
import org.dbforms.config.SqlUtil;
import org.dbforms.event.DatabaseEvent;
import org.dbforms.event.datalist.dao.DataSourceList;
import org.dbforms.event.datalist.dao.DataSourceFactory;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.Util;
import org.dbforms.util.MessageResourcesInternal;



/**
 * This event prepares and performs a SQL-Delete operation.
 * <br>
 * Works with new factory classes.
 *
 * @author Henner Kollmann <Henner.Kollmann@gmx.de>
 */
public class DeleteEvent extends DatabaseEvent
{
   private static Category logCat = Category.getInstance(
                                             DeleteEvent.class.getName());

   /**
    * Creates a new DeleteEvent object.
    *
    * @param tableId the table id
    * @param keyId   the key id
    * @param request the request object
    * @param config  the configuration object
    */
   public DeleteEvent(Integer tableId, String keyId, HttpServletRequest request, 
                      DbFormsConfig config)
   {
      super(tableId.intValue(), keyId, request, config);
   }


   /**
    * Creates a new DeleteEvent object.
    *
    * @param action  the action string
    * @param request the request object
    * @param config  the configuration object
    */
   public DeleteEvent(String action, HttpServletRequest request, 
                      DbFormsConfig config)
   {
      super(ParseUtil.getEmbeddedStringAsInteger(action, 2, '_'), 
            ParseUtil.getEmbeddedString(action, 3, '_'), request, config);
   }

   /**
    * Get the FieldValues attribute.
    *
    * @return the FieldValues attribute
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
    * @throws SQLException  if any SQL error occurs
    * @throws MultipleValidationException if any validation error occurs
    */
   public void processEvent(Connection con) throws SQLException 
   {
      // Apply given security contraints (as defined in dbforms-config.xml)
      if (!hasUserPrivileg(GrantedPrivileges.PRIVILEG_DELETE))
      {
         String s = MessageResourcesInternal.getMessage(
                             "dbforms.events.delete.nogrant", 
                             request.getLocale(), 
                             new String[] 
         {
            table.getName()
         });
         throw new SQLException(s);
      }

      // which values do we find in request
      FieldValues fieldValues = getFieldValues();

      int operation = DbEventInterceptor.GRANT_OPERATION;

      // part 2: check if there are interceptors to be processed (as definied by
      // "interceptor" element embedded in table element in dbforms-config xml file)
      try
      {
         Hashtable associativeArray = getAssociativeFieldValues(fieldValues);


         // process the interceptors associated to this table
         operation = table.processInterceptors(DbEventInterceptor.PRE_DELETE, 
                                               request, associativeArray, 
                                               config, con);


         // synchronize data which may be changed by interceptor:
         table.synchronizeData(fieldValues, associativeArray);
      }
      catch (SQLException sqle)
      {
         SqlUtil.logSqlException(sqle, 
                                 "::processEvent - SQL exception during PRE_DELETE interceptors procession");
         throw sqle;
      }

      if (operation != DbEventInterceptor.IGNORE_OPERATION)
      {
         // in order to process an update, we need the key of the dataset to update;
         String keyValuesStr = getKeyValues();

         if (Util.isNull(keyValuesStr))
         {
            logCat.error(
                     "::processEvent - At least one key is required per table, check your dbforms-config.xml");

            return;
         }

         // DELETE operation;
         boolean           mustClose = false;
         DataSourceList    ds  = DataSourceList.getInstance(request);
         DataSourceFactory qry = ds.get(table, request);

         if (qry == null)
         {
            qry       = new DataSourceFactory(table);
            mustClose = true;
         }

         qry.doDelete(con, keyValuesStr);

         if (mustClose)
         {
            qry.close();
         }
      }

      // finally, we process interceptor again (post-delete)
      try
      {
         // process the interceptors associated to this table
         table.processInterceptors(DbEventInterceptor.POST_DELETE, request, 
                                   null, config, con);
      }
      catch (SQLException sqle)
      {
         SqlUtil.logSqlException(sqle, 
                                 "::processEvent - SQL exception during POST_DELETE interceptors procession");
         throw sqle;
      }

      // End of interceptor processing
   }
}