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
import com.oreilly.servlet.multipart.*;
import org.apache.log4j.Category;

/****
 *
 * <p>This event prepares and performs a SQL-Insert operation</p>
 *
 * @author Joe Peer <j.peer@gmx.net>
 */

public class InsertEvent extends DatabaseEvent {

  static Category logCat = Category.getInstance(InsertEvent.class.getName()); // logging category for this class

  private Table table;
  private String idStr;


  /**
  insert actionbutton-strings looks like that: ac_insert_12
  this means "table 12 will be affected by an insert event"
  */


  public InsertEvent(String str, HttpServletRequest request, DbFormsConfig config) {
		this.request = request;
		this.config = config;

		int firstUnderscore = str.indexOf('_');
		int secondUnderscore = str.indexOf('_', firstUnderscore+1);
		int thirdUnderscore = str.indexOf('_', secondUnderscore+1);
		String tableName = str.substring(secondUnderscore+1, thirdUnderscore);
		this.tableId = Integer.parseInt(tableName);
		this.table = config.getTable(tableId);

		this.idStr = str.substring(thirdUnderscore+1);

		logCat.info("parsing insertevent");
		logCat.info("tableName="+tableName);
		logCat.info("tableId="+tableId);
		logCat.info("idStr="+idStr); // ie. "root", "1@root"
	}

	private Hashtable getFieldValues() {
		Hashtable result = new Hashtable();

		String paramStub =  "f_"+tableId+"_ins"+idStr+"_";
		Vector params = ParseUtil.getParametersStartingWith(request, paramStub);
		Enumeration enum = params.elements();
		while(enum.hasMoreElements()) {
			String param = (String) enum.nextElement();
			String value = ParseUtil.getParameter(request,param);

			logCat.info("insertevent::getFieldValues - param="+param+" value="+value);

			Integer iiFieldId = new Integer( param.substring(paramStub.length()) );
			result.put(iiFieldId, value);
		}

		return result;
	}

    /**
    for use in ConditionChecker only
    "associative" -> this hashtable works like "associative arrays" in PERL or PHP
    */
	private Hashtable getAssociativeFieldValues(Hashtable scalarFieldValues) {

		Hashtable result = new Hashtable();

		Enumeration scalars = scalarFieldValues.keys();
		while(scalars.hasMoreElements()) {
		  Integer fieldIndex = (Integer) scalars.nextElement();
		  String fieldName = table.getField(fieldIndex.intValue()).getName();

		  result.put(fieldName, scalarFieldValues.get(fieldIndex)); // building "associative array"
		}

		return result;
	}


  private boolean checkSufficentValues(Hashtable fieldValues) throws SQLException {
		Vector fields = table.getFields();
		for(int i=0; i<fields.size(); i++) {
			Field field = (Field) fields.elementAt(i);

			// if a field is a key and if it is NOT automatically generated, then it should be provided by the user
			if(!field.getIsAutoInc() && field.isKey()) {

				if(fieldValues.get(new Integer(i))==null) throw new SQLException ("Field " + field.getName() + " is missing");

			}

			// in opposite, if a field is automatically generated by the RDBMS, we need to
			else if(field.getIsAutoInc()) {

				if(fieldValues.get(new Integer(i))!=null) throw new SQLException ("Field " + field.getName() + " should be calculated by RDBMS, remove it from the form");

			} // in future we could do some other checks like NOT-NULL conditions,etc.

		}

		return true;
	}

	public void processEvent(Connection con)
	throws SQLException {

		// Applying given security contraints (as defined in dbforms-config xml file)
		// part 1: check if requested privilge is granted for role
		if(!hasUserPrivileg(GrantedPrivileges.PRIVILEG_INSERT))
			throw new SQLException("Sorry, adding data to table "+table.getName()+" is not granted for this session.");

		Hashtable fieldValues = getFieldValues();
		if(fieldValues.size()==0) throw new SQLException("no parameters");

		// part 2: check if there are interceptors to be processed (as definied by
		// "interceptor" element embedded in table element in dbforms-config xml file)
		if(table.hasInterceptors()) {
			try {
				Hashtable associativeArray = getAssociativeFieldValues(fieldValues);
				// process the interceptors associated to this table
				table.processInterceptors(DbEventInterceptor.PRE_INSERT, request, associativeArray, config, con);
				// synchronize data which may be changed by interceptor:
				table.synchronizeData(fieldValues, associativeArray);
			} catch(SQLException sqle) {
				throw new SQLException("Exception in interceptor: " + sqle.getMessage());
			}
		}
		// End of interceptor processing



		if(!checkSufficentValues(fieldValues)) throw new SQLException("unsufficent parameters");

		StringBuffer queryBuf = new StringBuffer();
		queryBuf.append("INSERT INTO ");
		queryBuf.append(table.getName());
		queryBuf.append(" (");

    // list the names of fields we'll include into the insert operation
		Vector fields = table.getFields();
		Enumeration enum = fieldValues.keys();
		while(enum.hasMoreElements()) {
			Integer iiFieldId = (Integer) enum.nextElement();
			String fieldName = ((Field) fields.elementAt(iiFieldId.intValue())).getName();
			queryBuf.append(fieldName);
			if(enum.hasMoreElements()) queryBuf.append(",");
		}

		// list the place-holders for the fields to include
		queryBuf.append(") VALUES (");
		for(int i=0; i<fieldValues.size(); i++) {
			if(i!=0) queryBuf.append(",");
			queryBuf.append("?");
		}
		queryBuf.append(")");


		logCat.info(queryBuf.toString());

		PreparedStatement ps = con.prepareStatement(queryBuf.toString());

		// now we provide the values
		enum = fieldValues.keys();
		int col=1;
		while(enum.hasMoreElements()) {
			Integer iiFieldId = (Integer) enum.nextElement();
			Field curField = (Field) fields.elementAt(iiFieldId.intValue());

			int fieldType = curField.getType();
			Object value = null;

			if(fieldType == FieldTypes.BLOB) {

				// in case of a BLOB we supply the FileHolder object to SqlUtils for further operations
		  	value = ParseUtil.getFileHolder(request, "f_"+tableId+"_ins"+idStr+"_"+iiFieldId);

			} else if(fieldType == FieldTypes.DISKBLOB) {

				// check if we need to store it encoded or not
				if("yes".equals(curField.getEncoding())) {
					FileHolder fileHolder = ParseUtil.getFileHolder(request, "f_"+tableId+"_ins"+idStr+"_"+iiFieldId);

					// encode fileName
					String fileName = fileHolder.getFileName();
					int dotIndex = fileName.lastIndexOf('.');
					String suffix = (dotIndex != -1) ? fileName.substring(dotIndex) : "";
					fileHolder.setFileName(UniqueIDGenerator.getUniqueID()+suffix);

					// a diskblob gets stored to db as an ordinary string (it's only the reference!)
					value = fileHolder.getFileName();

				} else {

					// a diskblob gets stored to db as an ordinary string	 (it's only the reference!)
					value = fieldValues.get(iiFieldId);
				}

			}	else {
				// in case of simple db types we just supply a string representing the value of the fields
				value = fieldValues.get(iiFieldId);
			}

			logCat.info("PRE_INSERT: field="+curField.getName()+" col="+col+" value="+value);

			SqlUtil.fillPreparedStatement(ps, col, value, fieldType);
			col++;
		}

		// execute the query & throws an exception if something goes wrong
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
				FileHolder fileHolder = ParseUtil.getFileHolder(request, "f_"+tableId+"_ins"+idStr+"_"+iiFieldId);
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


		// finally, we process interceptor again (post-insert)
		if(table.hasInterceptors()) {
			try {
				// process the interceptors associated to this table
				table.processInterceptors(DbEventInterceptor.POST_INSERT, request, null, config, con);
			} catch(SQLException sqle) {
				throw new SQLException("Exception in interceptor: " + sqle.getMessage());
			}
		}
		// End of interceptor processing

	}

}