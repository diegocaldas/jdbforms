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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.config.Constants;
import org.dbforms.config.DbEventInterceptor;
import org.dbforms.config.DbEventInterceptorData;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.FieldValues;
import org.dbforms.config.GrantedPrivileges;

import org.dbforms.event.ValidationEvent;
import org.dbforms.event.datalist.dao.DataSourceFactory;
import org.dbforms.event.datalist.dao.DataSourceSessionList;

import org.dbforms.util.MessageResourcesInternal;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.StringUtil;
import org.dbforms.util.Util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;



/**
 * This event prepares and performs a SQL-Update operation. <br>
 * Works with new factory classes.
 *
 * @author Henner Kollmann
 */
public class UpdateEvent extends ValidationEvent {
   // logging category for this class
   private static Log logCat = LogFactory.getLog(UpdateEvent.class.getName());

   /**
    * Creates a new UpdateEvent object.
    *
    * @param tableId the table identifier
    * @param keyId the key
    * @param request the request object
    * @param config the configuration object
    */
   public UpdateEvent(Integer            tableId,
                      String             keyId,
                      HttpServletRequest request,
                      DbFormsConfig      config) {
      super(tableId.intValue(), keyId, request, config);
   }


   /**
    * Creates a new UpdateEvent object.
    *
    * @param action the action string
    * @param request the request object
    * @param config the configuration object
    */
   public UpdateEvent(String             action,
                      HttpServletRequest request,
                      DbFormsConfig      config) {
      super(StringUtil.getEmbeddedStringAsInteger(action, 2, '_'),
            StringUtil.getEmbeddedString(action, 3, '_'), request, config);
   }

   /**
    * Get the FieldValues object.
    *
    * @return the FieldValues object
    */

   // must be public because protected will break cactus testing!
   public FieldValues getFieldValues() {
      String s = ParseUtil.getParameter(getRequest(),
                                        Constants.FIELDNAME_OVERRIDEFIELDTEST
                                        + getTable().getId());
      boolean flag = "true".equalsIgnoreCase(s);

      return getFieldValues(flag);
   }


   /**
    * Process this event.
    *
    * @param con the connection object
    *
    * @throws SQLException if any SQL error occurs
    * @throws MultipleValidationException if any validation error occurs
    */
   public void processEvent(Connection con) throws SQLException {
      // Apply given security contraints (as defined in dbforms-config.xml)
      if (!hasUserPrivileg(GrantedPrivileges.PRIVILEG_UPDATE)) {
         String s = MessageResourcesInternal.getMessage("dbforms.events.update.nogrant",
                                                        getRequest().getLocale(),
                                                        new String[] {
                                                           getTable()
                                                              .getName()
                                                        });
         throw new SQLException(s);
      }

      // End of interceptor processing
      // in order to process an update, we need the key of the dataset to update
      String keyValuesStr = getKeyValues();

      if (Util.isNull(keyValuesStr)) {
         logCat.error("::processEvent - at least one key is required per table, check your dbforms-config.xml");

         return;
      }

      // 2003-08-05-HKK: first check if update is necessary before check security
      // which values do we find in request
      FieldValues fieldValues = getFieldValues();

      if (fieldValues.size() == 0) {
         logCat.info("no parameters to update found");

         return;
      }

      DbEventInterceptorData interceptorData = new DbEventInterceptorData(getRequest(),
                                                               getConfig(), con, getTable());
      interceptorData.setAttribute(DbEventInterceptorData.FIELDVALUES, fieldValues);
      interceptorData.setAttribute(DbEventInterceptorData.KEYVALUES, keyValuesStr);

      // process the interceptors associated to this table
      int operation = getTable()
                         .processInterceptors(DbEventInterceptor.PRE_UPDATE,
                                              interceptorData);

      if ((operation == DbEventInterceptor.GRANT_OPERATION)
                && (fieldValues.size() > 0)) {
         // UPDATE operation;
         DataSourceSessionList    ds  = DataSourceSessionList.getInstance(getRequest());
         DataSourceFactory qry = ds.get(getTable(), getRequest());
         boolean           own = false;

         if (qry == null) {
            qry = new DataSourceFactory((String)interceptorData.getAttribute(DbEventInterceptorData.CONNECTIONNAME), interceptorData.getConnection(), getTable());
            own = true;
         }

         qry.doUpdate(interceptorData, fieldValues, keyValuesStr);

         if (own) {
            qry.close();
         } else {
            ds.remove(getTable(), getRequest());
         }

         // finally, we process interceptor again (post-update)
         // process the interceptors associated to this table
         getTable()
            .processInterceptors(DbEventInterceptor.POST_UPDATE, interceptorData);
      }

      // End of interceptor processing
   }
}
