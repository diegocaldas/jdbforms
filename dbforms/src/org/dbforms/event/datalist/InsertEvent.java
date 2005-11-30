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
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.Field;
import org.dbforms.config.FieldValues;
import org.dbforms.config.GrantedPrivileges;
import org.dbforms.config.MultipleValidationException;

import org.dbforms.event.AbstractValidationEvent;
import org.dbforms.event.datalist.dao.DataSourceFactory;
import org.dbforms.event.datalist.dao.DataSourceSessionList;
import org.dbforms.interfaces.DbEventInterceptorData;
import org.dbforms.interfaces.IDbEventInterceptor;

import org.dbforms.util.MessageResourcesInternal;
import org.dbforms.util.StringUtil;
import org.dbforms.util.Util;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Vector;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;



/**
 * This event prepares and performs a SQL-Insert operation. <br>
 * Works with new factory classes
 *
 * @author Henner Kollmann
 */
public class InsertEvent extends AbstractValidationEvent {

	private static Log logCat = LogFactory.getLog(InsertEvent.class.getName()); // logging category for this class
   
	/**
    * Creates a new InsertEvent object.
    *
    * @param tableId the table identifier
    * @param keyId the key
    * @param request the request object
    * @param config the configuration object
    */
   public InsertEvent(Integer tableId, String keyId,
      HttpServletRequest request, DbFormsConfig config) {
      super(tableId.intValue(), keyId, request, config);
   }


   /**
    * Constructor. <br>
    * Insert actionbutton-strings is as follows: ac_insert_12_root_3 which is
    * equivalent to: ac_insert  : insert action event 12         : table id
    * root       : key 3          : button count used to identify individual
    * insert buttons
    *
    * @param action the action string
    * @param request the request object
    * @param config the config object
    */
   public InsertEvent(String action, HttpServletRequest request,
      DbFormsConfig config) {
      super(StringUtil.getEmbeddedStringAsInteger(action, 2, '_'),
         StringUtil.getEmbeddedString(action, 3, '_'), request, config);
   }

   /**
    * Get the FieldValues object representing the collection of FieldValue
    * objects builded from the request parameters
    *
    * @return the FieldValues object representing the collection of FieldValue
    *         objects builded from the request parameters
    */

   // must be public because protected will break cactus testing!
   public FieldValues getFieldValues() {
      return getFieldValues(true);
   }


   /**
    * Process this event.
    *
    * @param con the jdbc connection object
    *
    * @throws SQLException if any data access error occurs
    * @throws MultipleValidationException if any validation error occurs
    */
   public void processEvent(Connection con) throws SQLException, MultipleValidationException {
      // Applying given security contraints (as defined in dbforms-config xml file)
      // part 1: check if requested privilge is granted for role
      if (!hasUserPrivileg(GrantedPrivileges.PRIVILEG_INSERT)) {
         String s = MessageResourcesInternal.getMessage("dbforms.events.insert.nogrant",
               getRequest().getLocale(), new String[] {
                  getTable().getName()
               });
         throw new SQLException(s);
      }

      FieldValues fieldValues = getFieldValues();

      if (fieldValues.size() == 0) {
         throw new SQLException("no parameters");
      }

      DbEventInterceptorData interceptorData = new DbEventInterceptorData(getRequest(),
            getConfig(), con, getTable());
      interceptorData.setAttribute(DbEventInterceptorData.FIELDVALUES,
         fieldValues);

      // process the interceptors associated to this table
      int operation = getTable().processInterceptors(IDbEventInterceptor.PRE_INSERT,
            interceptorData);

      if ((operation == IDbEventInterceptor.GRANT_OPERATION)
               && (fieldValues.size() > 0)) {
         // End of interceptor processing
         if (!checkSufficentValues(fieldValues)) {
            throw new SQLException("unsufficent parameters");
         }

         // INSERT operation;
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
         int i = qry.doInsert(interceptorData, fieldValues);
         interceptorData.setAttribute(DbEventInterceptorData.ROWSAFFECTED, new Integer(i));

         if (own) {
            qry.close();
         } else {
            ds.remove(getTable(), getRequest());
         }

         // Show the last record inserted
         String firstPosition = getTable().getPositionString(fieldValues);
         // CAPIO - must encode the position as it gets decoded when read.
         try {
         	firstPosition = Util.encode(firstPosition, getRequest().getCharacterEncoding());
		 } catch (UnsupportedEncodingException ex) {
		    logCat.error(ex);
			throw new SQLException(ex.getMessage());
		 }
		 getRequest().setAttribute("firstpos_" + getTable().getId(),
            firstPosition);

         // finally, we process interceptor again (post-insert)
         // process the interceptors associated to this table
         getTable().processInterceptors(IDbEventInterceptor.POST_INSERT,
            interceptorData);
      }
   }


   /**
    * Check a list of conditions on the the input FieldValues object:
    *
    * <ul>
    * <li>
    * it must contains the same number of fields as the current main Table
    * </li>
    * <li>
    * any autoInc field must NOT have a related FieldValue object (generated by
    * a request parameter) because that field value must be calculated by the
    * underlying RDBMS
    * </li>
    * </ul>
    *
    *
    * @param fieldValues the FieldValues object containing the Field elements
    *        to check
    *
    * @return true  if the  all the above conditions are true, false otherwise
    *
    * @throws SQLException if any check condition fails
    */
   private boolean checkSufficentValues(FieldValues fieldValues)
      throws SQLException {
      Vector fields = getTable().getFields();

      for (int i = 0; i < fields.size(); i++) {
         Field field = (Field) fields.elementAt(i);

         // if a field is a key and if it is NOT automatically generated,
         // then it should be provided by the user
         if (!field.hasAutoIncSet() && field.hasIsKeySet()) {
            if (fieldValues.get(field.getName()) == null) {
               throw new SQLException("Field " + field.getName()
                  + " is missing");
            }
         }
         // in opposite, if a field is automatically generated by the RDBMS, we need to
         else if (field.hasAutoIncSet()) {
            if (fieldValues.get(field.getName()) != null) {
               throw new SQLException("Field " + field.getName()
                  + " should be calculated by RDBMS, remove it from the form");
            }
         }

         // in future we could do some other checks like NOT-NULL conditions,etc.
      }

      return true;
   }
}
