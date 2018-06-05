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

import org.dbforms.config.Constants;
import org.dbforms.config.DbFormsConfigRegistry;
import org.dbforms.config.Field;
import org.dbforms.config.FieldTypes;
import org.dbforms.config.FieldValue;
import org.dbforms.config.FieldValues;
import org.dbforms.config.JDBCDataHelper;
import org.dbforms.config.ResultSetVector;
import org.dbforms.interfaces.DbEventInterceptorData;

import org.dbforms.util.FileHolder;
import org.dbforms.util.UniqueIDGenerator;
import org.dbforms.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import java.io.Serializable;

/**
 * Special implementation of DataSource. This is the default class and deals
 * with JDBC Connections.
 * 
 * @author hkk
 */
public class DataSourceJDBC extends AbstractDataSource implements Serializable {
	private static Log logCat = LogFactory.getLog(DataSourceJDBC.class);

	private Connection connection;

	private List data;

	private Map keys;

	private ResultSet rs;

	private Statement stmt;

	private String connectionName;

	private String query;

	private String sqlFilter;

	private String tableList;

	private String whereClause;

	private FieldValue[] filterConstraint;

	private FieldValue[] orderConstraint;

	private FieldValue[] sqlFilterParams;

	private boolean calcRowCount = false;

	private boolean fetchedAll = false;

	private int colCount;

	private int rowCount = 0;

	/**
	 * Creates a new DataSourceJDBC object.
	 */
	public DataSourceJDBC() {
		data = new ArrayList();
		keys = new HashMap();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param calcRowCount
	 *            The calcRowCount to set.
	 */
	public void setCalcRowCount(boolean calcRowCount) {
		this.calcRowCount = calcRowCount;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return Returns the calcRowCount.
	 */
	public boolean isCalcRowCount() {
		return calcRowCount;
	}

	/**
	 * Set the tableList and whererClause attributes used to build the SQL
	 * Select condition.
	 * 
	 * @param tableList
	 *            the table list string
	 * @param whereClause
	 *            the SQL where clause string
	 */
	public void setSelect(String tableList, String whereClause) {
		this.tableList = tableList;
		this.whereClause = whereClause;
	}

	/**
	 * Set the filterConstraint and orderConstraint used to build the SQL Select
	 * condition.
	 * 
	 * @param filterConstraint
	 *            FieldValue array used to build a cumulation of rules for
	 *            filtering fields.
	 * @param orderConstraint
	 *            FieldValue array used to build a cumulation of rules for
	 *            ordering (sorting) and restricting fields.
	 * @param sqlFilter
	 *            sql condition to add to where clause
	 * @param sqlFilterParams
	 *            list of FieldValues to fill the sqlFilter with
	 */
	public void setSelect(FieldValue[] filterConstraint,
			FieldValue[] orderConstraint, String sqlFilter,
			FieldValue[] sqlFilterParams) {
		this.filterConstraint = filterConstraint;
		this.orderConstraint = orderConstraint;
		this.sqlFilter = sqlFilter;
		this.sqlFilterParams = sqlFilterParams;
	}

	/**
	 * performs a delete in the DataSource
	 * 
	 * @param interceptorData
	 *            DOCUMENT ME!
	 * @param keyValuesStr
	 *            keyValueStr to the row to update <br>
	 *            key format: FieldID ":" Length ":" Value <br>
	 *            example: if key id = 121 and field id=2 then keyValueStr
	 *            contains "2:3:121" <br>
	 *            If the key consists of more than one fields, the key values
	 *            are seperated through "-" <br>
	 *            example: value of field 1=12, value of field 3=1992, then
	 *            we'll get "1:2:12-3:4:1992"
	 * 
	 * @throws SQLException
	 */
	public int doDelete(DbEventInterceptorData interceptorData,
			String keyValuesStr) throws SQLException {
		int res = 0;
		FieldValues fieldValues = null;

		// get current blob files from database
		if (getTable().containsDiskblob()) {
			ResultSet diskblobs = null;
			StringBuffer queryBuf = new StringBuffer();
			queryBuf.append(getTable().getDisblobSelectStatement());
			queryBuf.append(" WHERE ");
			queryBuf.append(getTable().getWhereClauseForKeyFields(keyValuesStr));

			PreparedStatement diskblobsPs = interceptorData.getConnection()
					.prepareStatement(queryBuf.toString());

			try {
				getTable().populateWhereClauseWithKeyFields(keyValuesStr,
						diskblobsPs, 1);

				diskblobs = diskblobsPs.executeQuery();

				try {
					ResultSetVector rsv = new ResultSetVector(getTable(),
							getTable().getDiskblobs());
					rsv.addResultSet(interceptorData, diskblobs);

					if (!ResultSetVector.isNull(rsv)) {
						rsv.moveFirst();
						fieldValues = rsv.getCurrentRowAsFieldValues();
					}
				} finally {
					diskblobs.close();
				}
			} finally {
				diskblobsPs.close();
			}
		}

		// 20021031-HKK: Build in table!!
		PreparedStatement ps = interceptorData.getConnection()
				.prepareStatement(getTable().getDeleteStatement(keyValuesStr));

		try {
			// now we provide the values
			// of the key-fields, so that the WHERE clause matches the right
			// dataset!
			getTable().populateWhereClauseWithKeyFields(keyValuesStr, ps, 1);

			// finally execute the query
			res = ps.executeUpdate();

			if (fieldValues != null) {
				deleteBlobFilesFromDisk(fieldValues);
			}
		} finally {
			ps.close();
		}
		return res;
	}

	/**
	 * Performs an insert into the DataSource
	 * 
	 * @param interceptorData
	 *            DOCUMENT ME!
	 * @param fieldValues
	 *            FieldValues to insert
	 * 
	 * @throws SQLException
	 */
	public int doInsert(DbEventInterceptorData interceptorData,
			FieldValues fieldValues) throws SQLException {
		String query = getTable().getInsertStatement(fieldValues);

		PreparedStatement ps = interceptorData.getConnection()
				.prepareStatement(query);

		int res = 0;
		try {
			// execute the query & throws an exception if something goes wrong
			fillWithData(ps, fieldValues);
			res = ps.executeUpdate();
		} finally {
			ps.close();
		}

		// now handle blob files
		saveBlobFilesToDisk(fieldValues);
		return res;
	}

	/**
	 * Performs an update into the DataSource
	 * 
	 * @param interceptorData
	 *            DOCUMENT ME!
	 * @param fieldValues
	 *            FieldValues to update
	 * @param keyValuesStr
	 *            keyValueStr to the row to update <br>
	 *            key format: FieldID ":" Length ":" Value <br>
	 *            example: if key id = 121 and field id=2 then keyValueStr
	 *            contains "2:3:121" <br>
	 *            If the key consists of more than one fields, the key values
	 *            are seperated through "-" <br>
	 *            example: value of field 1=12, value of field 3=1992, then
	 *            we'll get "1:2:12-3:4:1992"
	 * 
	 * @throws SQLException
	 */
	public int doUpdate(DbEventInterceptorData interceptorData,
			FieldValues fieldValues, String keyValuesStr) throws SQLException {
		int res = 0;

		String query = getTable().getUpdateStatement(fieldValues, keyValuesStr);
		PreparedStatement ps = interceptorData.getConnection()
				.prepareStatement(query);
		try {
			int col = fillWithData(ps, fieldValues);
			getTable().populateWhereClauseWithKeyFields(keyValuesStr, ps, col);

			// we are now ready to execute the query
			res = ps.executeUpdate();
			logCat.info("update rows: " + String.valueOf(res));

		} finally {
			ps.close();
		}

		// now handle blob files
		saveBlobFilesToDisk(fieldValues);
		return res;
	}

	/**
	 * set the connection parameter for the DataSouce. virtual method, if you
	 * need the connection data you must override the method In this special
	 * case we need our own connection to save it in the session.
	 * 
	 * @param con
	 *            the JDBC Connection object
	 * @param dbConnectionName
	 *            name of the used db connection. Can be used to get an own db
	 *            connection, e.g. to hold it during the session (see
	 *            DataSourceJDBC for example!)
	 */
	protected void setConnection(Connection con, String dbConnectionName) {
		close();

		// To prevent empty connection name. We always need our own connection!
		connectionName = Util.isNull(dbConnectionName) ? "default"
				: dbConnectionName;
	}

	/**
	 * Get the requested row as array of objects.
	 * 
	 * @param i
	 *            the row number
	 * 
	 * @return the requested row as array of objects
	 * 
	 * @throws SQLException
	 *             if any error occurs
	 */
	protected final Object[] getRow(int i) throws SQLException {
		Object[] result = null;

		if (i >= 0) {
			if (i < data.size()) {
				result = (Object[]) data.get(i);
			} else {
				if (!fetchedAll) {
					while (rs.next()) {
						addRow();
						if (i < data.size()) {
							result = (Object[]) data.get(i);
							break;
						}
					}
					checkResultSetEnd();
				}
			}
		}

		return result;
	}

	/**
	 * Release all the resources holded by this datasource. <br>
	 * Clean the underlying data and keys vectors, then close the JDBC
	 * resultSet, statement and connection objects.
	 */
	protected final void close() {
		if (data != null) {
			data.clear();
		}

		if (keys != null) {
			keys.clear();
		}

		closeConnection();

		// reset fetched all flag. So DataSource can be reopened after close!
		fetchedAll = false;
	}

	/**
	 * Find the first row of the internal data vector.
	 * 
	 * @param startRow
	 *            the string identifying the initial row
	 * 
	 * @return the start row position
	 * 
	 * @throws SQLException
	 *             if any error occurs
	 */
	protected final int findStartRow(String startRow) throws SQLException {
		int result = 0;
		boolean found = false;

		if (startRow != null) {
			Integer i = (Integer) keys.get(startRow);

			if (i != null) {
				result = i.intValue();
				found = true;
			}

			if (!found && !fetchedAll) {
				while (rs.next()) {
					String key = addRow();

					if (startRow.equals(key)) {
						result = data.size() - 1;

						break;
					}
				}

				checkResultSetEnd();
			}
		}

		return result;
	}

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
	protected final boolean hasMore(int i) throws SQLException {
		return !fetchedAll || (i < data.size());
	}

	/**
	 * Open this datasource and initialize its resources.
	 * 
	 * @throws SQLException
	 *             if any error occurs
	 */
	protected void open() throws SQLException {
		if (!fetchedAll && (rs == null)) {
			if ((connection == null) || connection.isClosed()) {
				try {
					this.connection = DbFormsConfigRegistry.instance().lookup()
							.getConnection(connectionName);
				} catch (Exception e) {
					logCat.error("open", e);
				}
			}

			if (connection == null) {
				throw new SQLException("no connection found!");
			}

			if (Util.isNull(whereClause)) {
				query = getTable().getSelectQuery(getTable().getFields(),
						filterConstraint, orderConstraint, sqlFilter,
						Constants.COMPARE_NONE);
				stmt = connection.prepareStatement(query);

				if (stmt == null) {
					throw new SQLException("no statement: " + query);
				}

				// 20040730-HKK: To workaround a bug inside mysql driver
				// stmt.setFetchSize(Integer.MIN_VALUE);
				rs = getTable().getDoSelectResultSet(filterConstraint,
						orderConstraint, sqlFilterParams,
						Constants.COMPARE_NONE, (PreparedStatement) stmt);
			} else {
				query = getTable().getFreeFormSelectQuery(
						getTable().getFields(), whereClause, tableList);
				stmt = connection.createStatement();

				if (stmt == null) {
					throw new SQLException("no statement");
				}

				rs = stmt.executeQuery(query);
			}

			ResultSetMetaData rsmd = rs.getMetaData();
			colCount = rsmd.getColumnCount();

			if (isCalcRowCount()) {
				Field f = new Field();
				f.setName("count(*) cnt");
				Vector v = new Vector();
				v.add(f);
				// v.addAll(getTable().getFields());
				ResultSet prs = null;
				if (Util.isNull(whereClause)) {
					String pquery = getTable().getSelectQuery(v,
                            filterConstraint, null, sqlFilter,
                            Constants.COMPARE_NONE); // DJH - Don't need ordering when doing row count.
					//String pquery = getTable().getSelectQuery(v,
					//		filterConstraint, orderConstraint, sqlFilter,
					//		Constants.COMPARE_NONE);
					PreparedStatement pstmt = connection
							.prepareStatement(pquery);

					if (pstmt == null) {
						throw new SQLException("no statement: " + pquery);
					}

					prs = getTable().getDoSelectResultSet(filterConstraint,
							orderConstraint, sqlFilterParams,
							Constants.COMPARE_NONE, pstmt);
				} else {
					String pquery = getTable().getFreeFormSelectQuery(v,
							whereClause, tableList);
					Statement pstmt = connection.createStatement();

					if (pstmt == null) {
						throw new SQLException("no statement");
					}

					prs = pstmt.executeQuery(pquery);
				}
				prs.next();
				rowCount = prs.getInt(prs.findColumn("cnt"));
			}
		}
	}

	/**
	 * Get the size of the data vector.
	 * 
	 * @return the size of the data vector
	 * 
	 * @throws SQLException
	 *             if any error occurs
	 */
	protected final int size() throws SQLException {
		// Workaround for bug in firebird driver: After reaching next the next
		// call
		// to next will start at the beginning of the resultset.
		// rs.next will return true, fetching data will get an
		// NullPointerException.
		// Catch this error and do an break!
		if (!fetchedAll) {
			while (rs.next()) {
				try {
					addRow();
				} catch (Exception e) {
					logCat.error("size", e);
					break;
				}
			}
			closeConnection();
		}
		return data.size();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return Returns the rowCount.
	 */
	protected int getRowCount() {
		return rowCount;
	}

	private String addRow() throws SQLException {
		Integer j = new Integer(data.size());
		Object[] objectRow = new Object[colCount];
		String[] stringRow = new String[colCount];

		for (int i = 0; i < colCount; i++) {
			objectRow[i] = JDBCDataHelper.getData(rs, getTable().getField(i)
					.getEscaper(), i + 1);
			stringRow[i] = (objectRow[i] != null) ? objectRow[i].toString()
					: null;
		}

		data.add(objectRow);

		String key = getTable().getKeyPositionString(stringRow);
		keys.put(key, j);

		return key;
	}

	private void checkResultSetEnd() throws SQLException {
		if (rs.next()) {
			addRow();
		} else {
			closeConnection();
		}
	}

	private void closeConnection() {
		fetchedAll = true;

		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				logCat.info("closeConnection", e);
			}
			rs = null;
		}

		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				logCat.info("closeConnection", e);
			}
			stmt = null;
		}

		if (connection != null) {
			try {
				if (!connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				logCat.info("closeConnection", e);
			}
			connection = null;
		}
	}

	// ------------------------------ DAO methods
	// ---------------------------------
	private int fillWithData(PreparedStatement ps, FieldValues fieldValues)
			throws SQLException {
		// now we provide the values;
		// every key is the parameter name from of the form page;
		Iterator e = fieldValues.keys();
		int col = 1;

		while (e.hasNext()) {
			String fieldName = (String) e.next();
			Field curField = getTable().getFieldByName(fieldName);

			if ((curField != null) && !getTable().isCalcField(curField.getId()) && Util.isNull(curField.getExpression())) {
				FieldValue fv = fieldValues.get(fieldName);

				logCat.debug("Retrieved curField:" + curField.getName()
						+ " type:" + curField.getType());

				int fieldType = curField.getType();
				Object value = null;

				if (fieldType == FieldTypes.BLOB) {
					// in case of a BLOB we supply the FileHolder object to
					// SqlUtils for further operations
					if (fv.getFileHolder() == null) { // if the blob field is
						// updated from within
						// textarea
						value = fv.getFieldValue();
					} else { // if we have a file upload
						value = fv.getFileHolder();
					}
				} else if (fieldType == FieldTypes.DISKBLOB) {
					FileHolder fileHolder = fv.getFileHolder();

					// encode fileName
					String fileName = fileHolder.getFileName();

					// check if we need to store it encoded or not
					if (curField.hasEncodedSet()) {
						int dotIndex = fileName.lastIndexOf('.');
						String suffix = (dotIndex != -1) ? fileName
								.substring(dotIndex) : "";
						fileHolder.setFileName(UniqueIDGenerator.getUniqueID()
								+ suffix);

						// a diskblob gets stored to db as an ordinary string
						// (it's only the reference!)
						value = fileHolder.getFileName();
					} else {
						// a diskblob gets stored to db as an ordinary string
						// (it's only the reference!)
						value = fileName;
					}
				} else {
					// in case of simple db types we just supply a string
					// representing the value of the fields
					value = fv.getFieldValueAsObject();
				}

				logCat.info("field=" + curField.getName() + " col=" + col
						+ " value=" + value + " type=" + fieldType);
				JDBCDataHelper.fillWithData(ps, curField.getEscaper(), col,
						value, fieldType, getTable().getBlobHandlingStrategy());
				col++;
			}
		}

		return col;
	}
}
