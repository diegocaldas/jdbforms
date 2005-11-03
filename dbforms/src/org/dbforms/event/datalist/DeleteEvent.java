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

import org.dbforms.config.DbEventInterceptor;
import org.dbforms.config.DbEventInterceptorData;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.FieldValues;
import org.dbforms.config.GrantedPrivileges;
import org.dbforms.config.MultipleValidationException;

import org.dbforms.event.DatabaseEvent;
import org.dbforms.event.datalist.dao.DataSourceFactory;
import org.dbforms.event.datalist.dao.DataSourceSessionList;

import org.dbforms.util.MessageResourcesInternal;
import org.dbforms.util.StringUtil;
import org.dbforms.util.Util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;



/**
 * This event prepares and performs a SQL-Delete operation. <br>
 * Works with new factory classes.
 *
 * @author Henner Kollmann
 */
public class DeleteEvent extends DatabaseEvent {
   private static Log logCat = LogFactory.getLog(DeleteEvent.class.getName());

   /**
    * Creates a new DeleteEvent object.
    *
    * @param tableId the table id
    * @param keyId the key id
    * @param request the request object
    * @param config the configuration object
    */
   public DeleteEvent(Integer tableId, String keyId,
      HttpServletRequest request, DbFormsConfig config) {
      super(tableId.intValue(), keyId, request, config);
   }


   /**
    * Creates a new DeleteEvent object.
    *
    * @param action the action string
    * @param request the request object
    * @param config the configuration object
    */
   public DeleteEvent(String action, HttpServletRequest request,
      DbFormsConfig config) {
      super(StringUtil.getEmbeddedStringAsInteger(action, 2, '_'),
         StringUtil.getEmbeddedString(action, 3, '_'), request, config);
   }

   /**
    * Get the FieldValues attribute.
    *
    * @return the FieldValues attribute
    */
   public FieldValues getFieldValues() {
      return getFieldValues(true);
   }


   /**
    * Process this event.
    *
    * @param con the connection object
    *
    * @throws SQLException if any SQL error occurs
    * @throws MultipleValidationException if any validation error occurs
    */
   public void processEvent(Connection con) throws SQLException, MultipleValidationException {
      // Apply given security contraints (as defined in dbforms-config.xml)
      if (!hasUserPrivileg(GrantedPrivileges.PRIVILEG_DELETE)) {
         String s = MessageResourcesInternal.getMessage("dbforms.events.delete.nogrant",
               getRequest().getLocale(), new String[] {
                  getTable().getName()
               });
         throw new SQLException(s);
      }

      // in order to process an update, we need the key of the dataset to update;
      String keyValuesStr = getKeyValues();

      if (Util.isNull(keyValuesStr)) {
         logCat.error(
            "::processEvent - At least one key is required per table, check your dbforms-config.xml");

         return;
      }

      // which values do we find in request
      FieldValues            fieldValues     = getFieldValues();
      DbEventInterceptorData interceptorData = new DbEventInterceptorData(getRequest(),
            getConfig(), con, getTable());
      interceptorData.setAttribute(DbEventInterceptorData.FIELDVALUES,
         fieldValues);
      interceptorData.setAttribute(DbEventInterceptorData.KEYVALUES,
         keyValuesStr);

      // part 2: check if there are interceptors to be processed (as definied by
      // "interceptor" element embedded in table element in dbforms-config xml file)
      // process the interceptors associated to this table
      int operation = getTable().processInterceptors(DbEventInterceptor.PRE_DELETE,
            interceptorData);

      if (operation == DbEventInterceptor.GRANT_OPERATION) {
         // DELETE operation;
         DataSourceSessionList ds  = DataSourceSessionList.getInstance(getRequest());
         DataSourceFactory     qry = ds.get(getTable(), getRequest());
         boolean               own = false;

         if (qry == null) {
            qry    = new DataSourceFactory((String) interceptorData
                  .getAttribute(DbEventInterceptorData.CONNECTIONNAME),
                  interceptorData.getConnection(), getTable());
            own    = true;
         }

         getRequest().setAttribute("forceUpdate", "true");
         int i = qry.doDelete(interceptorData, keyValuesStr);
         interceptorData.setAttribute(DbEventInterceptorData.ROWSAFFECTED, new Integer(i));

         if (own) {
            qry.close();
         } else {
            ds.remove(getTable(), getRequest());
         }

         // finally, we process interceptor again (post-delete)
         // process the interceptors associated to this table
         getTable().processInterceptors(DbEventInterceptor.POST_DELETE,
            interceptorData);
      }

      // End of interceptor processing
   }
}
