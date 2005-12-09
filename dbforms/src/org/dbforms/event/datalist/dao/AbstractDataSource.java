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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.config.DbFormsConfigRegistry;
import org.dbforms.config.Field;
import org.dbforms.config.FieldTypes;
import org.dbforms.config.FieldValue;
import org.dbforms.config.FieldValues;
import org.dbforms.config.ResultSetVector;
import org.dbforms.config.Table;
import org.dbforms.interfaces.DbEventInterceptorData;

import org.dbforms.util.FileHolder;
import org.dbforms.util.Util;

import java.io.File;
import java.io.IOException;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Iterator;

/**
 * Abstract base class for DataSource.
 * 
 * @author hkk
 */
public abstract class AbstractDataSource {
	/** log4j category class */
	private static Log logCat = LogFactory.getLog(AbstractDataSource.class);

	private Table table;

	/**
	 * Creates a new DataSource object.
	 */
	public AbstractDataSource() {
	}

	/**
	 * Sets the select data for this dataSource
	 * 
	 * @param filterConstraint
	 *            FieldValue array used to restrict a set in a resultset
	 * @param orderConstraint
	 *            FieldValue array used to build a cumulation of rules for
	 *            ordering (sorting)
	 * @param sqlFilter
	 *            sql condition to add to where clause
	 * @param sqlFilterParams
	 *            list of FieldValues to fill the sqlFilter with
	 */
	public abstract void setSelect(FieldValue[] filterConstraint,
			FieldValue[] orderConstraint, String sqlFilter,
			FieldValue[] sqlFilterParams);

	/**
	 * get count rows from position
	 * 
	 * @param position
	 *            keyValueStr to the row<br>
	 *            key format: FieldID ":" Length ":" Value<br>
	 *            example: if key id = 121 and field id=2 then keyValueStr
	 *            contains "2:3:121"<br>
	 *            If the key consists of more than one fields, the key values
	 *            are seperated through "-"<br>
	 *            example: value of field 1=12, value of field 3=1992, then
	 *            we'll get "1:2:12-3:4:1992"
	 * @param count
	 *            count of rows to fetch
	 * 
	 * @return the fetched ResultSetVector
	 * 
	 * @throws SQLException
	 */
	public ResultSetVector getCurrent(DbEventInterceptorData interceptorData,
			String position, int count) throws SQLException {
		try {
			open();

			int start = findStartRow(position);

			if (count == 0) {
				count = size() - start;
			}

			return getResultSetVector(interceptorData, start, count);
		} catch (Exception e) {
			logCat.error("getCurrent", e);
			close();
			// Valid result is required!!!
			return new ResultSetVector(getTable());
		}
	}

	/**
	 * get count rows from first row
	 * 
	 * @param count
	 *            count of rows to fetch
	 * 
	 * @return the fetched ResultSetVector
	 * 
	 * @throws SQLException
	 */
	public ResultSetVector getFirst(DbEventInterceptorData interceptorData,
			int count) throws SQLException {
		try {
			open();

			if (count == 0) {
				count = size();
			}

			return getResultSetVector(interceptorData, 0, count);
		} catch (Exception e) {
			logCat.error("getFirst", e);
			close();

			// Valid result is required!!!
			return new ResultSetVector(getTable());
		}
	}

	/**
	 * get count rows from last row
	 * 
	 * @param count
	 *            count of rows to fetch
	 * 
	 * @return the fetched ResultSetVector
	 * 
	 * @throws SQLException
	 */
	public ResultSetVector getLast(DbEventInterceptorData interceptorData,
			int count) throws SQLException {
		try {
			open();

			return getResultSetVector(interceptorData, size() - 1, -count);
		} catch (Exception e) {
			logCat.error("getLast", e);
			close();

			// Valid result is required!!!
			return new ResultSetVector(getTable());
		}
	}

	/**
	 * get count next rows from position
	 * 
	 * @param position
	 *            keyValueStr to the row<br>
	 *            key format: FieldID ":" Length ":" Value<br>
	 *            example: if key id = 121 and field id=2 then keyValueStr
	 *            contains "2:3:121"<br>
	 *            If the key consists of more than one fields, the key values
	 *            are seperated through "-"<br>
	 *            example: value of field 1=12, value of field 3=1992, then
	 *            we'll get "1:2:12-3:4:1992"
	 * @param count
	 *            count of rows to fetch
	 * 
	 * @return the fetched ResultSetVector
	 * 
	 * @throws SQLException
	 */
	public ResultSetVector getNext(DbEventInterceptorData interceptorData,
			String position, int count) throws SQLException {
		try {
			open();

			int start = findStartRow(position) + 1;

			if (count == 0) {
				count = size() - start;
			}

			return getResultSetVector(interceptorData, start, count);
		} catch (Exception e) {
			logCat.error("getNext", e);
			close();

			// Valid result is required!!!
			return new ResultSetVector(getTable());
		}
	}

	/**
	 * get count rows backwards from position
	 * 
	 * @param position
	 *            keyValueStr to the row<br>
	 *            key format: FieldID ":" Length ":" Value<br>
	 *            example: if key id = 121 and field id=2 then keyValueStr
	 *            contains "2:3:121"<br>
	 *            If the key consists of more than one fields, the key values
	 *            are seperated through "-"<br>
	 *            example: value of field 1=12, value of field 3=1992, then
	 *            we'll get "1:2:12-3:4:1992"
	 * @param count
	 *            count of rows to fetch
	 * 
	 * @return the fetched ResultSetVector
	 * 
	 * @throws SQLException
	 */
	public ResultSetVector getPrev(DbEventInterceptorData interceptorData,
			String position, int count) throws SQLException {
		try {
			open();

			int start = findStartRow(position) - 1;

			if (count == 0) {
				count = start;
			}

			return getResultSetVector(interceptorData, start, -count);
		} catch (Exception e) {
			logCat.error("getPrev", e);
			close();

			// Valid result is required!!!
			return new ResultSetVector(getTable());
		}
	}

	/**
	 * Sets the select data for this dataSource for free form selects. default
	 * methods just raises an exception
	 * 
	 * @param tableList
	 *            the list of tables involved into the query
	 * @param whereClause
	 *            free-form whereClause to be appended to query
	 * 
	 * @throws SQLException
	 */
	public void setSelect(String tableList, String whereClause)
			throws SQLException {
		throw new SQLException("Free form select not implemented");
	}

	/**
	 * gets the Table for the DataSource
	 * 
	 * @return Table
	 */
	public Table getTable() {
		return table;
	}

	/**
	 * performs an delete in the DataSource
	 * 
	 * @param con
	 *            DOCUMENT ME!
	 * @param keyValuesStr
	 *            keyValueStr to the row to update<br>
	 *            key format: FieldID ":" Length ":" Value<br>
	 *            example: if key id = 121 and field id=2 then keyValueStr
	 *            contains "2:3:121"<br>
	 *            If the key consists of more than one fields, the key values
	 *            are seperated through "-"<br>
	 *            example: value of field 1=12, value of field 3=1992, then
	 *            we'll get "1:2:12-3:4:1992"
	 * 
	 * @throws SQLException
	 *             if any error occurs
	 */
	public int doDelete(DbEventInterceptorData interceptorData,
			String keyValuesStr) throws SQLException {
		return 0;
	}

	/**
	 * performs an insert into the DataSource
	 * 
	 * @param con
	 *            DOCUMENT ME!
	 * @param fieldValues
	 *            FieldValues to insert
	 * 
	 * @throws SQLException
	 */
	public int doInsert(DbEventInterceptorData interceptorData,
			FieldValues fieldValues) throws SQLException {
		return 0;
	}

	/**
	 * performs an update into the DataSource
	 * 
	 * @param con
	 *            DOCUMENT ME!
	 * @param fieldValues
	 *            FieldValues to update
	 * @param keyValuesStr
	 *            keyValueStr to the row to update<br>
	 *            key format: FieldID ":" Length ":" Value<br>
	 *            example: if key id = 121 and field id=2 then keyValueStr
	 *            contains "2:3:121"<br>
	 *            If the key consists of more than one fields, the key values
	 *            are seperated through "-"<br>
	 *            example: value of field 1=12, value of field 3=1992, then
	 *            we'll get "1:2:12-3:4:1992"
	 * 
	 * @throws SQLException
	 *             if any error occurs
	 */
	public int doUpdate(DbEventInterceptorData interceptorData,
			FieldValues fieldValues, String keyValuesStr) throws SQLException {
		return 0;
	}

	/**
	 * set the connection parameter for the DataSouce. virtual method, if you
	 * need the connection data you must override this method
	 * 
	 * @param con
	 *            the JDBC Connection object
	 * @param dbConnectionName
	 *            name of the used db connection. Can be used to get an own db
	 *            connection, e.g. to hold it during the session (see
	 *            DataSourceJDBC for example!)
	 */
	protected void setConnection(Connection con, String dbConnectionName) {
	}

	/**
	 * should retrieve the row at an special index as an Object[]
	 * 
	 * @param i
	 *            index of row to fetch
	 * 
	 * @return Object[] of the fetched row
	 * 
	 * @throws SQLException
	 */
	protected abstract Object[] getRow(int i) throws SQLException;

	/**
	 * should close all open datasets
	 */
	protected abstract void close();

	/**
	 * maps the startRow to the internal index
	 * 
	 * @param startRow
	 *            keyValueStr to the row<br>
	 *            key format: FieldID ":" Length ":" Value<br>
	 *            example: if key id = 121 and field id=2 then keyValueStr
	 *            contains "2:3:121"<br>
	 *            If the key consists of more than one fields, the key values
	 *            are seperated through "-"<br>
	 *            example: value of field 1=12, value of field 3=1992, then
	 *            we'll get "1:2:12-3:4:1992"
	 * 
	 * @return the index of the row, 0 as first row if not found
	 * 
	 * @throws SQLException
	 */
	protected abstract int findStartRow(String startRow) throws SQLException;

	/**
	 * return true if there are more records to fetch then the given record
	 * number
	 * 
	 * @param i
	 *            index of last fetched row.
	 * 
	 * @return true if there are more records to fetch then the given record
	 *         number
	 * 
	 * @throws SQLException
	 */
	protected abstract boolean hasMore(int i) throws SQLException;

	/**
	 * Will be called to open all datasets
	 * 
	 * @throws SQLException
	 */
	protected abstract void open() throws SQLException;

	/**
	 * Must return the size of the whole resultset with all data fetch
	 * 
	 * @return size of whole resultset
	 * 
	 * @throws SQLException
	 */
	protected abstract int size() throws SQLException;

	/**
	 * @param table
	 *            The table to set.
	 */
	protected void setTable(Table table) {
		this.table = table;
	}

	/**
	 * deletes the blob files on disk
	 * 
	 * @param fieldValues
	 *            FieldValues to delete, called by
	 * 
	 * @throws SQLException
	 */
	protected void deleteBlobFilesFromDisk(FieldValues fieldValues)
			throws SQLException {
		Iterator iter = fieldValues.keys();

		while (iter.hasNext()) {
			String fieldName = (String) iter.next();
			Field curField = table.getFieldByName(fieldName);

			if (curField != null) {
				int fieldType = curField.getType();

				String directory = null;

				try {
					directory = DbFormsConfigRegistry.instance().lookup()
							.replaceRealPath(curField.getDirectory());
				} catch (Exception e) {
					logCat.error("deleteBlobFilesFromDisk", e);
					throw new SQLException(e.getMessage());
				}

				if (fieldType == FieldTypes.DISKBLOB) {
					String fileName = fieldValues.get(fieldName)
							.getFieldValue().trim();

					// get a filename
					if (!Util.isNull(fileName)) {
						// remember: every field may have its own storing dir!
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
	}

	/**
	 * save the blob files to disk
	 * 
	 * @param fieldValues
	 *            FieldValues to update
	 * 
	 * @throws SQLException
	 * @throws IllegalArgumentException
	 */
	protected void saveBlobFilesToDisk(FieldValues fieldValues)
			throws SQLException {
		Iterator iter = fieldValues.keys();

		while (iter.hasNext()) {
			String fieldName = (String) iter.next();
			Field curField = table.getFieldByName(fieldName);

			if (curField != null) {
				int fieldType = curField.getType();

				if (fieldType == FieldTypes.DISKBLOB) {
					String directory = curField.getDirectory();

					// check if directory-attribute was provided
					if (directory == null) {
						throw new IllegalArgumentException(
								"directory-attribute needed for fields of type DISKBLOB");
					}

					try {
						directory = DbFormsConfigRegistry.instance().lookup()
								.replaceRealPath(directory);
					} catch (Exception e) {
						logCat.error("saveBlobFilesToDisk", e);
					}

					// instanciate file object for that dir
					File dir = new File(directory);

					// Check saveDirectory is truly a directory
					if (!dir.isDirectory()) {
						throw new IllegalArgumentException("Not a directory: "
								+ directory);
					}

					// Check saveDirectory is writable
					if (!dir.canWrite()) {
						throw new IllegalArgumentException("Not writable: "
								+ directory);
					}

					// dir is ok so lets store the filepart
					FileHolder fileHolder = fieldValues.get(fieldName)
							.getFileHolder();

					if (fileHolder != null) {
						try {
							fileHolder.writeBufferToFile(dir);

							// filePart.getInputStream().close();
							logCat.info("fin + closedy");
						} catch (IOException ioe) {
							// #checkme: this would be a good place for rollback
							// in database!!
							logCat.error("saveBlobFilesToDisk", ioe);
							throw new SQLException("could not store file '"
									+ fileHolder.getFileName() + "' to dir '"
									+ directory + "'");
						}
					} else {
						logCat.info("uh! empty fileHolder");
					}
				}
			}
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param startRow
	 * @param count
	 * 
	 * @return the resultsetvector
	 * 
	 * @throws SQLException
	 */
	protected ResultSetVector getResultSetVector(
			DbEventInterceptorData interceptorData, int startRow, int count)
			throws SQLException {
		ResultSetVector result;
		int cCount = count;
		do {
			result = getResultSetVectorInternal(interceptorData, startRow,
					cCount);
			if ((count < 0) && result.isFirstPage())
				break;
			else if ((count > 0) && result.isLastPage())
				break;
			cCount = count + (count - result.size());
		} while (result.size() < Math.abs(count));
		return result;
	}

	private ResultSetVector getResultSetVectorInternal(
			DbEventInterceptorData interceptorData, int startRow, int count)
			throws SQLException {
		ResultSetVector result = null;
		result = new ResultSetVector(table);

		int begin = 0;
		int ende = 0;

		Object[] row;

		if (count > 0) {
			begin = startRow;

			for (ende = begin; ende < (startRow + count); ende++) {
				row = getRow(ende);

				if (row == null) {
					break;
				}

				result.addRow(interceptorData, row);
			}
		} else if (count < 0) {
			begin = startRow + count + 1;

			if (begin < 0) {
				begin = 0;
			}

			for (ende = begin; ende <= startRow; ende++) {
				row = getRow(ende);

				if (row == null) {
					break;
				}

				result.addRow(interceptorData, row);
			}
		}

		result.setFirstPage(!(begin > 0));
		result.setLastPage(!hasMore(ende));

		return result;
	}

}
