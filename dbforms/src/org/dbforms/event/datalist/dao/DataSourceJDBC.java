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

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.PreparedStatement;
import java.sql.Connection;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import org.dbforms.config.Constants;
import org.dbforms.config.DbFormsConfigRegistry;
import org.dbforms.config.Field;
import org.dbforms.config.FieldTypes;
import org.dbforms.config.FieldValue;
import org.dbforms.config.FieldValues;
import org.dbforms.config.JDBCDataHelper;
import org.dbforms.config.ResultSetVector;
import org.dbforms.config.Table;
import org.dbforms.util.FileHolder;
import org.dbforms.util.UniqueIDGenerator;
import org.dbforms.util.Util;

/**
 * Special implementation of DataSource. This is the default class and deals
 * with JDBC Connections.
 * 
 * @author hkk
 */
public class DataSourceJDBC extends DataSource {
	private String query;

	private Connection con;

	private String connectionName;

	private ResultSet rs;

	private Statement stmt;

	private List data;

	private Map keys;

	private int colCount;

	private String whereClause;

	private String tableList;

	private FieldValue[] filterConstraint;

	private FieldValue[] orderConstraint;

	private FieldValue[] sqlFilterParams;

	private String sqlFilter;

	private boolean fetchedAll = false;

	/**
	 * Creates a new DataSourceJDBC object.
	 * 
	 * @param table
	 *            the inout table
	 */
	public DataSourceJDBC(Table table) {
		super(table);
		data = new ArrayList();
		keys = new HashMap();
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
	public void setConnection(Connection con, String dbConnectionName) {
		close();

		// To prevent empty connection name. We always need our own connection!
		connectionName = Util.isNull(dbConnectionName) ? "default"
				: dbConnectionName;
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

	private void closeConnection() {
		fetchedAll = true;
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				getLogCat().info("closeConnection", e);
			}
			rs = null;
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				getLogCat().info("closeConnection", e);
			}
			stmt = null;
		}
		if (con != null) {
			try {
				if (!con.isClosed())
					con.close();
			} catch (SQLException e) {
				getLogCat().info("closeConnection", e);
			}
			con = null;
		}
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
	 * Open this datasource and initialize its resources.
	 * 
	 * @throws SQLException
	 *             if any error occurs
	 */
	protected final void open() throws SQLException {
		if (!fetchedAll && (rs == null)) {
			if ((con == null) || con.isClosed()) {
				try {
					this.con = DbFormsConfigRegistry.instance().lookup()
							.getConnection(connectionName);
				} catch (Exception e) {
					getLogCat().error("open", e);
				}
			}

			if (con == null) {
				throw new SQLException("no connection found!");
			}

			if (Util.isNull(whereClause)) {
				query = getTable().getSelectQuery(getTable().getFields(),
						filterConstraint, orderConstraint, sqlFilter,
						Constants.COMPARE_NONE);
				stmt = con.prepareStatement(query);
				if (stmt == null) {
					throw new SQLException("no statement: " + query);
				}
				// 20040730-HKK: To workaround a bug inside mysql driver
//                stmt.setFetchSize(Integer.MIN_VALUE); 
				rs = getTable().getDoSelectResultSet(filterConstraint,
						orderConstraint, sqlFilterParams,
						Constants.COMPARE_NONE, (PreparedStatement) stmt);
			} else {
				query = getTable().getFreeFormSelectQuery(
						getTable().getFields(), whereClause, tableList);
				stmt = con.createStatement();
				if (stmt == null) {
					throw new SQLException("no statement");
				}
				// 20040730-HKK: To workaround a bug inside mysql driver
//                stmt.setFetchSize(Integer.MIN_VALUE); 
				rs = stmt.executeQuery(query);
			}

			ResultSetMetaData rsmd = rs.getMetaData();
			colCount = rsmd.getColumnCount();
		}
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

	private void checkResultSetEnd() throws SQLException {
		if ((rs.getRow() != 0)) {
			// test if next record is avaiable...
			// rs.isLast is not allowed in all circumstances!
			if (rs.next()) {
				addRow();
			}
		}

		if ((rs.getRow() == 0)
		/* || rs.isLast() 20031510-HKK: removed because of Oracle problems */
		) {
			closeConnection();
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
					getLogCat().error("size", e);

					break;
				}
			}

			closeConnection();
		}

		return data.size();
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
		return !fetchedAll || (i < size());
	}

	//------------------------------ DAO methods
	// ---------------------------------
	private int fillWithData(PreparedStatement ps, FieldValues fieldValues)
			throws SQLException {
		// now we provide the values;
		// every key is the parameter name from of the form page;
		Iterator enum = fieldValues.keys();
		int col = 1;

		while (enum.hasNext()) {
			String fieldName = (String) enum.next();
			Field curField = getTable().getFieldByName(fieldName);

			if (curField != null) {
				FieldValue fv = fieldValues.get(fieldName);

				getLogCat().debug(
						"Retrieved curField:" + curField.getName() + " type:"
								+ curField.getType());

				int fieldType = curField.getType();
				Object value = null;

				if (fieldType == FieldTypes.BLOB) {
					// in case of a BLOB we supply the FileHolder object to
					// SqlUtils for further operations
					value = fv.getFileHolder();
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

				getLogCat().info(
						"field=" + curField.getName() + " col=" + col
								+ " value=" + value + " type=" + fieldType);
				JDBCDataHelper.fillWithData(ps, curField.getEscaper(), col,
						value, fieldType, getTable().getBlobHandlingStrategy());
				col++;
			}
		}

		return col;
	}

	/**
	 * Performs an insert into the DataSource
	 * 
	 * @param fieldValues
	 *            FieldValues to insert
	 * 
	 * @throws SQLException
	 */
	public void doInsert(Connection con, FieldValues fieldValues)
			throws SQLException {
		PreparedStatement ps = con.prepareStatement(getTable()
				.getInsertStatement(fieldValues));
		try {
			// execute the query & throws an exception if something goes wrong
			fillWithData(ps, fieldValues);
			ps.executeUpdate();
		} finally {
			ps.close();
		}

		// now handle blob files
		saveBlobFilesToDisk(fieldValues);
	}

	/**
	 * Performs an update into the DataSource
	 * 
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
	public void doUpdate(Connection con, FieldValues fieldValues,
			String keyValuesStr) throws SQLException {
		PreparedStatement ps = con.prepareStatement(getTable()
				.getUpdateStatement(fieldValues));
		try {
			int col = fillWithData(ps, fieldValues);
			getTable().populateWhereClauseWithKeyFields(keyValuesStr, ps, col);

			// we are now ready to execute the query
			ps.executeUpdate();
		} finally {
			ps.close();
		}

		// now handle blob files
		saveBlobFilesToDisk(fieldValues);
	}

	/**
	 * performs a delete in the DataSource
	 * 
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
	public void doDelete(Connection con, String keyValuesStr)
			throws SQLException {
		FieldValues fieldValues = null;

		// get current blob files from database
		if (getTable().containsDiskblob()) {
			ResultSet diskblobs = null;
			StringBuffer queryBuf = new StringBuffer();
			queryBuf.append(getTable().getDisblobSelectStatement());
			queryBuf.append(" WHERE ");
			queryBuf.append(getTable().getWhereClauseForKeyFields());

			PreparedStatement diskblobsPs = con.prepareStatement(queryBuf
					.toString());
			try {
				getTable().populateWhereClauseWithKeyFields(keyValuesStr,
						diskblobsPs, 1);
				try {
					diskblobs = diskblobsPs.executeQuery();
					ResultSetVector rsv = new ResultSetVector(getTable()
							.getDiskblobs(), diskblobs);

					if (!ResultSetVector.isNull(rsv)) {
						rsv.setPointer(0);
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
		PreparedStatement ps = con.prepareStatement(getTable()
				.getDeleteStatement());
		try {
			// now we provide the values
			// of the key-fields, so that the WHERE clause matches the right
			// dataset!
			getTable().populateWhereClauseWithKeyFields(keyValuesStr, ps, 1);

			// finally execute the query
			ps.executeUpdate();

			if (fieldValues != null) {
				deleteBlobFilesFromDisk(fieldValues);
			}
		} finally {
			ps.close();
		}
	}
}