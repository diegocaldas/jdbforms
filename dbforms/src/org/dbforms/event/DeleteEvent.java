/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * DbForms - a Rapid Application Development Framework
 * Copyright (C) 2001 Joachim Peer <j.peer@gmx.net> et al.
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

package org.dbforms.event;

import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.http.*;
import org.dbforms.*;
import org.dbforms.util.*;

import org.apache.log4j.Category;

/****
 *
 * <p>This event prepares and performs a SQL-Delete operation</p>
 *
 * @author Joe Peer <j.peer@gmx.net>
 */

public class DeleteEvent extends DatabaseEvent {

	static Category logCat = Category.getInstance(DeleteEvent.class.getName());
	// logging category for this class

	private Table table;
	private String keyId;

	/**
	
	*/
	public String getKeyId() {
		return keyId;
	}

	public DeleteEvent(
		int tableId,
		String keyId,
		HttpServletRequest request,
		DbFormsConfig config) {
		logCat.info("new DeleteEvent with tableid=" + tableId + " keyId=" + keyId);
		this.request = request;
		this.config = config;
		this.tableId = tableId;
		this.table = config.getTable(tableId);
		this.keyId = keyId;
	}

	public DeleteEvent(
		String action,
		HttpServletRequest request,
		DbFormsConfig config) {
		this.request = request;
		this.config = config;
		this.tableId = ParseUtil.getEmbeddedStringAsInteger(action, 2, '_');
		this.table = config.getTable(tableId);
		this.keyId = ParseUtil.getEmbeddedString(action, 3, '_');
	}

	public void processEvent(Connection con)
		throws SQLException, MultipleValidationException {

		// in order to process an delete, we need the key of the dataset to delete
		//
		// new since version 0.9:
		// key format: FieldID ":" Length ":" Value
		// example: if key id = 121 and field id=2 then keyValueStr contains "2:3:121"
		//
		// if the key consists of more than one fields, the key values are seperated through "-"
		// example: value of field 1=12, value of field 3=1992, then we'll get "1:2:12-3:4:1992"
		String keyValuesStr =
			(String) ParseUtil.getParameter(request, "k_" + tableId + "_" + keyId);
		if (keyValuesStr == null || keyValuesStr.trim().length() == 0) {
			logCat.error(
				"At least one key is required per table, check your dbforms-config.xml");
			return;
		}

		// Apply given security contraints (as defined in dbforms-config.xml)
		if (!hasUserPrivileg(GrantedPrivileges.PRIVILEG_DELETE))
			throw new SQLException(
				"Sorry, deleting data from table "
					+ table.getName()
					+ " is not granted for this session.");

		// part 2: check if there are interceptors to be processed (as definied by
		// "interceptor" element embedded in table element in dbforms-config xml file)
		if (table.hasInterceptors()) {

			try {

				// part 2a: we need eventually information about the data to be deleted
				//#checkme: this means performance overhead!
				//can we go without this luxury? (=> USE CASES! where do we need info about data to delete?)
				StringBuffer queryBuf = new StringBuffer();
            queryBuf.append(table.getSelectStatement());
            queryBuf.append(" WHERE ");
				queryBuf.append(table.getWhereClauseForPS());

				logCat.info("doing interceptor before delete:" + queryBuf.toString());

				PreparedStatement ps = con.prepareStatement(queryBuf.toString());
				table.populateWhereClauseForPS(keyValuesStr, ps, 1);
				ResultSet rowToDelete = ps.executeQuery();
				Hashtable associativeArray = new Hashtable();
				if (rowToDelete.next()) {
					// yea, this code really sucks...
					// but i do not want the jdbc driver to fetch the row name (-> what do we if a
					// driver does not support column names...??-> this is the reason i do not to rely on this!)
					for (int i = 0; i < table.getFields().size(); i++) {
						Field f = (Field) table.getFields().elementAt(i);
						// get the name of the encoded key field
						String key = f.getName();
						String value = rowToDelete.getString(i + 1);
						if (value != null)
							associativeArray.put(key, value);
					}
				} else
					throw new SQLException(
						"Sorry, deleting data from table "
							+ table.getName()
							+ " is not granted this time. Your request could have been violating a condition or there is a weird database error. Contact system administrator if problem persists.");

				rowToDelete.close();
				ps.close(); // #JP Jun 27, 2001

				// part 2b: process the interceptors associated to this table
				table.processInterceptors(
					DbEventInterceptor.PRE_DELETE,
					request,
					associativeArray,
					config,
					con);

			} catch (SQLException sqle) {
				// PG, 2001-12-04
				// No need to add extra comments, just re-throw exceptions as SqlExceptions
				throw new SQLException(sqle.getMessage());
			} catch (MultipleValidationException mve) {
				// PG, 2001-12-14
				// Support for multiple error messages in one interceptor
				throw new MultipleValidationException(mve.getMessages());
			}

		}
		// End of interceptor processing

		// we check if the table the delete should be applied to contains field(s)
		// of the type "DISKBLOB"
		// if so, we have to select the filename+dirs from the db before we can delete
		ResultSet diskblobs = null;
		if (table.containsDiskblob()) {
         StringBuffer queryBuf = new StringBuffer();
         queryBuf.append(table.getDisblobSelectStatement());
			queryBuf.append(" WHERE ");
			queryBuf.append(table.getWhereClauseForPS());

			PreparedStatement diskblobsPs = con.prepareStatement(queryBuf.toString());
			table.populateWhereClauseForPS(keyValuesStr, diskblobsPs, 1);
			diskblobs = diskblobsPs.executeQuery();
		}
      
      // 20021031-HKK: Build in table!!
      PreparedStatement ps = con.prepareStatement(table.getDeleteStatement());

      // now we provide the values
      // of the key-fields, so that the WHERE clause matches the right dataset!
      table.populateWhereClauseForPS(keyValuesStr, ps, 1);
      // finally execute the query
      ps.executeUpdate();
      
		// if we came here, we can delete the diskblob files (if any)
		// #checkme: rollback if file problem (?? not sure!)

		if (diskblobs != null) { // if resultset exists

			if (diskblobs.next()) {
				// if a row in the resultset exists (can be only 1 row !)

				Vector diskblobFields = table.getDiskblobs();
				// get fields we're interested in
				for (int i = 0; i < diskblobFields.size(); i++) {
					Field aField = (Field) diskblobFields.elementAt(i);
					String fileName = diskblobs.getString(i + 1); // get a filename

					if (fileName != null) { // may be SQL NULL, in that case we'll skip it

						fileName = fileName.trim(); // remove whitespace
						if (fileName.length() > 0) {
							String dir = aField.getDirectory();
							// remember: every field may have its own storing dir!
							File file = new File(dir, fileName);

							if (file.exists()) {
								file.delete();
								logCat.info("deleted file " + fileName + " from dir " + dir);
							} else {
								logCat.info(
									"delete of file "
										+ fileName
										+ " from dir "
										+ dir
										+ " failed because file not found");
							}
						}

					}

				}

			}

		}

		// finally, we process interceptor again (post-delete)
		if (table.hasInterceptors()) {
			try {
				// process the interceptors associated to this table
				table.processInterceptors(
					DbEventInterceptor.POST_DELETE,
					request,
					null,
					config,
					con);
			} catch (SQLException sqle) {
				// PG = 2001-12-04
				// No need to add extra comments, just re-throw exceptions as SqlExceptions
				throw new SQLException(sqle.getMessage());
			}
		}
		// End of interceptor processing

	}

}