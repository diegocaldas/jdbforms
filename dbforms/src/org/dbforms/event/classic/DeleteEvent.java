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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.config.*;

import org.dbforms.event.AbstractDatabaseEvent;
import org.dbforms.interfaces.DbEventInterceptorData;
import org.dbforms.interfaces.IDbEventInterceptor;

import org.dbforms.util.*;

import java.io.*;

import java.sql.*;

import java.util.*;

import javax.servlet.http.*;

/**
 * DOCUMENT ME!
 * 
 * @author Joe Peer
 * 
 * @deprecated
 * <p>
 */
public class DeleteEvent extends AbstractDatabaseEvent {
	// logging category for this class
	static Log logCat = LogFactory.getLog(DeleteEvent.class.getName());

	/**
	 * Creates a new DeleteEvent object.
	 * 
	 * @param tableId
	 *            DOCUMENT ME!
	 * @param keyId
	 *            DOCUMENT ME!
	 * @param request
	 *            DOCUMENT ME!
	 * @param config
	 *            DOCUMENT ME!
	 */
	public DeleteEvent(Integer tableId, String keyId,
			HttpServletRequest request, DbFormsConfig config) {
		super(tableId.intValue(), keyId, request, config);
	}

	/**
	 * Creates a new DeleteEvent object.
	 * 
	 * @param action
	 *            DOCUMENT ME!
	 * @param request
	 *            DOCUMENT ME!
	 * @param config
	 *            DOCUMENT ME!
	 */
	public DeleteEvent(String action, HttpServletRequest request,
			DbFormsConfig config) {
		super(StringUtil.getEmbeddedStringAsInteger(action, 2, '_'), StringUtil
				.getEmbeddedString(action, 3, '_'), request, config);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public FieldValues getFieldValues() {
		return getFieldValues(true);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param con
	 *            DOCUMENT ME!
	 * 
	 * @throws MultipleValidationException
	 *             DOCUMENT ME!
	 */
public void processEvent(Connection con) throws SQLException, MultipleValidationException {
      // Apply given security contraints (as defined in dbforms-config.xml)
      if (!hasUserPrivileg(GrantedPrivileges.PRIVILEG_DELETE)) {
         String s = MessageResourcesInternal.getMessage("dbforms.events.delete.nogrant",
                                                        getRequest().getLocale(),
                                                        new String[] {
                                                           getTable()
                                                              .getName()
                                                        });
         throw new MultipleValidationException(s);
      }

      // in order to process an delete, we need the key of the dataset to
      // delete
      //
      // new since version 0.9:
      // key format: FieldID ":" Length ":" Value
      // example: if key id = 121 and field id=2 then keyValueStr contains
      // "2:3:121"
      //
      // if the key consists of more than one fields, the key values are
      // seperated through "-"
      // example: value of field 1=12, value of field 3=1992, then we'll get
      // "1:2:12-3:4:1992"
      String keyValuesStr = getKeyValues();

      if (Util.isNull(keyValuesStr)) {
         logCat.error("At least one key is required per table, check your dbforms-config.xml");

         return;
      }

      // which values do we find in request
      FieldValues            fieldValues = getFieldValues();

      DbEventInterceptorData interceptorData = new DbEventInterceptorData(getRequest(),
                                                               getConfig(), con, getTable());
      interceptorData.setAttribute(DbEventInterceptorData.FIELDVALUES, fieldValues);
      interceptorData.setAttribute(DbEventInterceptorData.KEYVALUES, keyValuesStr);

      // part 2b: process the interceptors associated to this table
      int operation = getTable()
                         .processInterceptors(IDbEventInterceptor.PRE_DELETE,
                                              interceptorData);

      // End of interceptor processing
      if (operation == IDbEventInterceptor.GRANT_OPERATION) {
         // we check if the table the delete should be applied to contains
         // field(s)
         // of the type "DISKBLOB"
         // if so, we have to select the filename+dirs from the db before we
         // can delete
         ResultSet diskblobs = null;
         PreparedStatement diskblobsPs = null;
         if (getTable()
                      .containsDiskblob()) {
            StringBuffer queryBuf = new StringBuffer();
            queryBuf.append(getTable().getDisblobSelectStatement());
            queryBuf.append(" WHERE ");
            queryBuf.append(getTable().getWhereClauseForKeyFields(keyValuesStr));

            diskblobsPs = con.prepareStatement(queryBuf.toString());
            getTable()
               .populateWhereClauseWithKeyFields(keyValuesStr, diskblobsPs, 1);
            diskblobs = diskblobsPs.executeQuery();
         }

         // 20021031-HKK: build in table!!
         PreparedStatement ps = con.prepareStatement(getTable().getDeleteStatement(keyValuesStr));

         // now we provide the values
         // of the key-fields, so that the WHERE clause matches the right
         // dataset!
         getTable()
            .populateWhereClauseWithKeyFields(keyValuesStr, ps, 1);

         // finally execute the query
         ps.executeUpdate();
         ps.close();

         // if we came here, we can delete the diskblob files (if any)
         // #checkme: rollback if file problem (?? not sure!)
         if (diskblobs != null) { // if resultset exists

            if (diskblobs.next()) {
               // if a row in the resultset exists (can be only 1 row !)
               Vector diskblobFields = getTable()
                                          .getDiskblobs();

               // get fields we're interested in
               for (int i = 0; i < diskblobFields.size(); i++) {
                  Field  aField   = (Field) diskblobFields.elementAt(i);
                  String fileName = diskblobs.getString(i + 1);

                  // get a filename
                  if (fileName != null) {
                     // may be SQL NULL, in that case we'll skip it
                     fileName = fileName.trim(); // remove whitespace

                     if (fileName.length() > 0) {
                        String directory = null;

                        try {
                           directory = DbFormsConfigRegistry.instance().lookup().replaceRealPath(aField.getDirectory());
                        } catch (Exception e) {
                           throw new SQLException(e.getMessage());
                        }

                        // remember: every field may have its own
                        // storing dir!
                        File file = new File(directory, fileName);

                        if (file.exists()) {
                           file.delete();
                           logCat.info("deleted file " + fileName
                                       + " from dir " + directory);
                        } else {
                           logCat.info("delete of file " + fileName
                                       + " from dir " + directory
                                       + " failed because file not found");
                        }
                     }
                  }
               }
            }
            diskblobsPs.close();
            
         }

         // finally, we process interceptor again (post-delete)
         // process the interceptors associated to this table
         getTable()
            .processInterceptors(IDbEventInterceptor.POST_DELETE, interceptorData);
      }

      // End of interceptor processing
   }}
