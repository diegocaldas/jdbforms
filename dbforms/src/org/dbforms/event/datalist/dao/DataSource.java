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
package org.dbforms.event.datalist.dao;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.Category;
import org.dbforms.Table;
import org.dbforms.Field;
import org.dbforms.FieldValue;
import org.dbforms.util.FileHolder;
import org.dbforms.util.FieldTypes;
import org.dbforms.util.FieldValues;
import org.dbforms.DbFormsConfig;
import org.dbforms.util.ResultSetVector;
import org.dbforms.util.FieldValues;
import org.dbforms.util.Util;

/**
 * 
 * Abstract base class for DataSource.
 * 
 * 
 * @author hkk
 */
public abstract class DataSource {
	private static Category logCat = Category.getInstance(DataSourceJDBC.class.getName()); // logging category for this class
	protected Table table;

	public DataSource(Table table) {
		this.table = table;
	};


	public abstract void setConnection(DbFormsConfig config, String dbConnectionName);
	public abstract void setConnection(Connection con);
	public abstract void setSelect(String tableList, String whereClause);
	public abstract void setSelect(FieldValue[] childFieldValues, FieldValue[] orderConstraint);
	
	public abstract void close() throws SQLException;
	public abstract ResultSetVector getNext(String position, int count) throws SQLException;
	public abstract ResultSetVector getPrev(String position, int count) throws SQLException;
	public abstract ResultSetVector getFirst(int count) throws SQLException;
	public abstract ResultSetVector getLast(int count) throws SQLException;
	public abstract ResultSetVector getCurrent(String position, int count) throws SQLException;
	public abstract void doInsert(FieldValues fieldValues) throws SQLException;
	public abstract void doUpdate(FieldValues fieldValues, String keyValuesStr) throws SQLException;
	public abstract void doDelete(String keyValuesStr) throws SQLException;

	// the story may continue for DISKBLOBs:
	// #checkme: we need some kind of ROLLBACK-mechanism:
	//  for the case that file upload physically failed we should rollback the logical db entry!
	//  but this depends on the capabilities of the system's database / jdbc-driver
	// #checkme: ho to find out if rollback possible, how to behave if not possible?
	protected void saveBlobFilesToDisk(FieldValues fieldValues) throws SQLException {
		Enumeration enum = fieldValues.keys();
		while (enum.hasMoreElements()) {
			String fieldName = (String) enum.nextElement();
			Field curField = table.getFieldByName(fieldName);
			if (curField != null) {
				int fieldType = curField.getType();
				String directory = curField.getDirectory();
				if (fieldType == FieldTypes.DISKBLOB) {
					// check if directory-attribute was provided
					if (directory == null) {
						throw new IllegalArgumentException("directory-attribute needed for fields of type DISKBLOB");
					}
					// instanciate file object for that dir
					File dir = new File(directory);
					// Check saveDirectory is truly a directory
					if (!dir.isDirectory()) {
						throw new IllegalArgumentException("Not a directory: " + directory);
					}
					// Check saveDirectory is writable
					if (!dir.canWrite()) {
						throw new IllegalArgumentException("Not writable: " + directory);
					}
					// dir is ok so lets store the filepart
					FileHolder fileHolder = fieldValues.get(fieldName).getFileHolder();
					if (fileHolder != null) {
						try {
							fileHolder.writeBufferToFile(dir);
							//filePart.getInputStream().close();
							logCat.info("fin + closedy");
						} catch (IOException ioe) {
							//#checkme: this would be a good place for rollback in database!!
							throw new SQLException(
								"could not store file '" + fileHolder.getFileName() + "' to dir '" + directory + "'");
						}
					} else {
						logCat.info("uh! empty fileHolder");
					}
				}
			}
		}
	}

	protected void deleteBlobFilesFromDisk(FieldValues fieldValues) throws SQLException {
		Enumeration enum = fieldValues.keys();
		while (enum.hasMoreElements()) {
			String fieldName = (String) enum.nextElement();
			Field curField = table.getFieldByName(fieldName);
			if (curField != null) {
				int fieldType = curField.getType();
				String directory = curField.getDirectory();
				if (fieldType == FieldTypes.DISKBLOB) {
					String fileName = fieldValues.get(fieldName).getFieldValue().trim(); // get a filename
					if (!Util.isNull(fileName)) { // may be SQL NULL, in that case we'll skip it
						String dir = curField.getDirectory();
						// remember: every field may have its own storing dir!
						File file = new File(dir, fileName);
						if (file.exists()) {
							file.delete();
							logCat.info("deleted file " + fileName + " from dir " + dir);
						} else {
							logCat.info("delete of file " + fileName + " from dir " + dir + " failed because file not found");
						}
					}
				}
			}
		}
	}


}
