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
 * <p>This event prepares and performs a SQL-Update operation</p>
 *
 * @author Joe Peer <j.peer@gmx.net>
 */

public class UpdateEvent extends DatabaseEvent {

  static Category logCat = Category.getInstance(UpdateEvent.class.getName()); // logging category for this class

  private Table table;
  private String keyId;

  /**

  */
	public String getKeyId() {
		return keyId;
	}


  public UpdateEvent(int tableId, String keyId, HttpServletRequest request, DbFormsConfig config) {
		logCat.info("new UpdateEvent with tableid="+tableId+" keyId="+keyId);
		this.request = request;
		this.config = config;
		this.tableId = tableId;
		this.table=config.getTable(tableId);
		this.keyId = keyId;
	}

	public UpdateEvent(String action, HttpServletRequest request, DbFormsConfig config) {
		this.request = request;
		this.config = config;
		this.tableId = ParseUtil.getEmbeddedStringAsInteger(action, 2, '_');
		this.table=config.getTable(tableId);
		this.keyId = ParseUtil.getEmbeddedString(action, 3, '_');
	}

	public Hashtable getFieldValues() {
		Hashtable result = new Hashtable();

		String paramStub =  "f_"+tableId+"_"+keyId+"_";
		Vector params = ParseUtil.getParametersStartingWith(request, paramStub);
		Enumeration enum = params.elements();
		while(enum.hasMoreElements()) {
			String param = (String) enum.nextElement();
			String value = ParseUtil.getParameter(request,param);

			Integer iiFieldId = new Integer( param.substring(paramStub.length()) );
			result.put(iiFieldId, value);
		}

		return result;
	}

	public void processEvent(Connection con)
	throws SQLException, MultipleValidationException {

		// Apply given security contraints (as defined in dbforms-config.xml)
		if(!hasUserPrivileg(GrantedPrivileges.PRIVILEG_UPDATE))
			throw new SQLException("Sorry, updating table "+table.getName()+" is not granted for this session.");

		// which values do we find in request
		Hashtable fieldValues = getFieldValues();
		if(fieldValues.size()==0) return;

		// part 2: check if there are interceptors to be processed (as definied by
		// "interceptor" element embedded in table element in dbforms-config xml file)
		if(table.hasInterceptors()) {
			try {
				Hashtable associativeArray = getAssociativeFieldValues(fieldValues);
				// process the interceptors associated to this table
				table.processInterceptors(DbEventInterceptor.PRE_UPDATE, request, associativeArray, config, con);
				// synchronize data which may be changed by interceptor:
				table.synchronizeData(fieldValues, associativeArray);
			} catch(SQLException sqle) {
				// PG = 2001-12-04
				// No need to add extra comments, just re-throw exceptions as SqlExceptions
				throw new SQLException(sqle.getMessage());
			} catch(MultipleValidationException mve) {
		  		// PG, 2001-12-14
		  		// Support for multiple error messages in one interceptor
		  		throw new MultipleValidationException(mve.getMessages());
		  }
		}
		// End of interceptor processing




		// in order to process an update, we need the key of the dataset to update
		//
		// new since version 0.9:
		// key format: FieldID ":" Length ":" Value
		// example: if key id = 121 and field id=2 then keyValueStr contains "2:3:121"
		//
		// if the key consists of more than one fields, the key values are seperated through "-"
		// example: value of field 1=12, value of field 3=1992, then we'll get "1:2:12-3:4:1992"
		String keyValuesStr = ParseUtil.getParameter(request,"k_"+tableId+"_"+keyId);
		if(keyValuesStr==null || keyValuesStr.trim().length() == 0) 
		{
			logCat.error("At least one key is required per table, check your dbforms-config.xml");
			return;
		}

		// now we start building the UPDATE statement
		StringBuffer queryBuf = new StringBuffer();
		queryBuf.append("UPDATE ");
		queryBuf.append(table.getName());
		queryBuf.append(" SET ");

	// list the names of fields and the place holder for their new values
	// important: these are the fields which are sent throug the current request;
	// this list may be only a subset of the field list, it is not necessarily the complete field list of a table!
		Vector fields = table.getFields();
		Enumeration enum = fieldValues.keys();
		boolean kommaNeeded = false;
		while(enum.hasMoreElements()) {
			Integer iiFieldId = (Integer) enum.nextElement();
			String fieldName = ((Field) fields.elementAt(iiFieldId.intValue())).getName();

			if(kommaNeeded) queryBuf.append(", "); else kommaNeeded=true;
			queryBuf.append(fieldName);
			queryBuf.append("= ?");

		}
		queryBuf.append(" WHERE ");
		queryBuf.append(table.getWhereClauseForPS());


		logCat.info(queryBuf.toString());
		PreparedStatement ps = con.prepareStatement(queryBuf.toString());

		// now we provide the values
		// first, we provide the "new" values for fields
		enum = fieldValues.keys();
		int col=1;
		while(enum.hasMoreElements()) {
			Integer iiFieldId = (Integer) enum.nextElement();
			Field curField = (Field) fields.elementAt(iiFieldId.intValue());

			int fieldType = curField.getType();
			Object value = null;

			if(fieldType == FieldTypes.BLOB) {
				// in case of a BLOB we supply the FileHolder object to SqlUtils for further operations
				logCat.info("we are looking for fileholder with name: f_"+tableId+"_"+keyId+"_"+iiFieldId);
				value = ParseUtil.getFileHolder(request, "f_"+tableId+"_"+keyId+"_"+iiFieldId);
				logCat.info("and found a value="+value);


			} else if(fieldType == FieldTypes.DISKBLOB) {

				FileHolder fileHolder = ParseUtil.getFileHolder(request, "f_"+tableId+"_"+keyId+"_"+iiFieldId);
				String fileName = fileHolder.getFileName();

				// check if we need to store it encoded or not
				if("yes".equals(curField.getEncoding())) {

					// encode fileName

					int dotIndex = fileName.lastIndexOf('.');
					String suffix = (dotIndex != -1) ? fileName.substring(dotIndex) : "";
					fileHolder.setFileName(UniqueIDGenerator.getUniqueID()+suffix);

					// a diskblob gets stored to db as an ordinary string (it's only the reference!)
					value = fileHolder.getFileName();

				} else {

					// a diskblob gets stored to db as an ordinary string	 (it's only the reference!)
					value = fileName;
				}

			} else {

				// in case of simple db types we just supply a string representing the value of the fields
				value = (String) fieldValues.get(iiFieldId);
			}

			SqlUtil.fillPreparedStatement(ps, col, value, curField.getType());
			col++;
		}

		table.populateWhereClauseForPS(keyValuesStr, ps, col);

		// we are now ready to execute the query
		ps.executeUpdate();
		ps.close(); // #JP Jun 27, 2001


		// if something went wrong we do not reach this piece of code:
		// the story may continue for DISKBLOBs:
		// #checkme: we need some kind of ROLLBACK-mechanism:
		//  for the case that file upload physically failed we should rollback the logical db entry!
		//  but this depends on the capabilities of the system's database / jdbc-driver
		// #checkme: ho to find out if rollback possible, how to behave if not possible?

		enum = fieldValues.keys();
		while(enum.hasMoreElements()) {
			Integer iiFieldId = (Integer) enum.nextElement();

			Field curField = (Field) fields.elementAt(iiFieldId.intValue());
			int fieldType = curField.getType();
			String directory = curField.getDirectory();

			if(fieldType == FieldTypes.DISKBLOB) {

				// check if directory-attribute was provided
				if(directory==null)
					throw new IllegalArgumentException("directory-attribute needed for fields of type DISKBLOB");

			    // instanciate file object for that dir
			    File dir = new File(directory);

			    // Check saveDirectory is truly a directory
			    if (!dir.isDirectory())
			      throw new IllegalArgumentException("Not a directory: " + directory);

			    // Check saveDirectory is writable
			    if (!dir.canWrite())
			      throw new IllegalArgumentException("Not writable: " + directory);

				// dir is ok so lets store the filepart
				FileHolder fileHolder = ParseUtil.getFileHolder(request, "f_"+tableId+"_"+keyId+"_"+iiFieldId);
				if(fileHolder!=null) {
				  try {

					fileHolder.writeBufferToFile(dir);


					//filePart.getInputStream().close();
					logCat.info("fin + closedy");
				  } catch(IOException ioe) {
					//#checkme: this would be a good place for rollback in database!!
					throw new SQLException("could not store file '"+fileHolder.getFileName()+"' to dir '"+directory+"'");
				  }

				} else logCat.info("uh! empty fileHolder");
			}
		}


		// finally, we process interceptor again (post-update)
		if(table.hasInterceptors()) {
			try {
				// process the interceptors associated to this table
				table.processInterceptors(DbEventInterceptor.POST_UPDATE, request, null, config, con);
			} catch(SQLException sqle) {
				// PG = 2001-12-04
				// No need to add extra comments, just re-throw exceptions as SqlExceptions
				throw new SQLException(sqle.getMessage());
			}
		}
		// End of interceptor processing

	}

	/**
	for use in ConditionChecker only
	"associative" -> this hashtable works like "associative arrays" in PERL or PHP
	*/
	public Hashtable getAssociativeFieldValues(Hashtable scalarFieldValues) {

		Hashtable result = new Hashtable();

		Enumeration scalars = scalarFieldValues.keys();
		while(scalars.hasMoreElements()) {
		  Integer fieldIndex = (Integer) scalars.nextElement();
		  String fieldName = table.getField(fieldIndex.intValue()).getName();

		  result.put(fieldName, scalarFieldValues.get(fieldIndex)); // building "associative array"
		}

		return result;
	}

}