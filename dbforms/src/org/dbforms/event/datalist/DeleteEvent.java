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

import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.http.*;
import org.dbforms.*;
import org.dbforms.util.*;
import org.apache.log4j.Category;
import org.dbforms.event.*;
import org.dbforms.event.datalist.dao.*;

/****
 *
 * <p>This event prepares and performs a SQL-Delete operation</p>
 * 
 * <p>Works with new factory classes</p>
 *
 * @author Henner Kollmann <Henner.Kollmann@gmx.de>
 */
public class DeleteEvent extends DatabaseEvent {
   static Category logCat = Category.getInstance(DeleteEvent.class.getName());

   /**
    * Creates a new DeleteEvent object.
    *
    * @param tableId DOCUMENT ME!
    * @param keyId DOCUMENT ME!
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    */
   //public DeleteEvent(int tableId, String keyId, HttpServletRequest request, DbFormsConfig config)
   public DeleteEvent(Integer tableId, String keyId, HttpServletRequest request, DbFormsConfig config) {
      super(tableId.intValue(), keyId, request, config);
   }

   /**
    * Creates a new DeleteEvent object.
    *
    * @param action DOCUMENT ME!
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    */
   public DeleteEvent(String action, HttpServletRequest request, DbFormsConfig config) {
      super(ParseUtil.getEmbeddedStringAsInteger(action, 2, '_'), ParseUtil.getEmbeddedString(action, 3, '_'), request, config);
   }

   public FieldValues getFieldValues() {
      return getFieldValues(false);
   }

   /**
    * DOCUMENT ME!
    *
    * @param con DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    * @throws MultipleValidationException DOCUMENT ME!
    */
   public void processEvent(Connection con) throws SQLException, MultipleValidationException {
      // Apply given security contraints (as defined in dbforms-config.xml)
      if (!hasUserPrivileg(GrantedPrivileges.PRIVILEG_DELETE)) {
         throw new SQLException("Sorry, deleting data from table " + table.getName() + " is not granted for this session.");
      }
      // which values do we find in request
      FieldValues fieldValues = getFieldValues();
      if (fieldValues.size() == 0) {
         return;
      }
      // part 2: check if there are interceptors to be processed (as definied by
      // "interceptor" element embedded in table element in dbforms-config xml file)
      if (table.hasInterceptors()) {
         try {
            Hashtable associativeArray = getAssociativeFieldValues(fieldValues);
            // process the interceptors associated to this table
            table.processInterceptors(DbEventInterceptor.PRE_DELETE, request, associativeArray, config, con);
            // synchronize data which may be changed by interceptor:
            table.synchronizeData(fieldValues, associativeArray);
         } catch (SQLException sqle) {
            // PG = 2001-12-04
            // No need to add extra comments, just re-throw exceptions as SqlExceptions
            throw new SQLException(sqle.getMessage());
         } catch (MultipleValidationException mve) {
            // PG, 2001-12-14
            // Support for multiple error messages in one interceptor
            throw new MultipleValidationException(mve.getMessages());
         }
      }

      // in order to process an update, we need the key of the dataset to update
      //
      String keyValuesStr = getKeyValues();
      if (Util.isNull(keyValuesStr)) {
         logCat.error("At least one key is required per table, check your dbforms-config.xml");
         return;
      }
      DataSourceFactory qry = new DataSourceFactory(con, table);
      qry.doDelete(keyValuesStr);
      qry.close();

      // finally, we process interceptor again (post-delete)
      if (table.hasInterceptors()) {
         try {
            // process the interceptors associated to this table
            table.processInterceptors(DbEventInterceptor.POST_DELETE, request, null, config, con);
         } catch (SQLException sqle) {
            // PG = 2001-12-04
            // No need to add extra comments, just re-throw exceptions as SqlExceptions
            throw new SQLException(sqle.getMessage());
         }
      }

      // End of interceptor processing
   }
}