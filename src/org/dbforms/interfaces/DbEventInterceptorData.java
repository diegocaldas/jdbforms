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
package org.dbforms.interfaces;

import java.sql.Connection;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.Table;

/**
 * Class to transport the data in the interceptors.
 * 
 * @author hkk
 */
public class DbEventInterceptorData {

	/** filled with FieldValues array during insert, delete and update events */
	public static final String FIELDVALUES = "fieldValues";

	/** filled with KeyValues string during delete and update events */
	public static final String KEYVALUES = "keyValues";

	/** filled with current ResultsetVector during AddRow and Select events */
	public static final String RESULTSET = "ResultSetVector";

	/** filled with objectrow which should be added during AddRow events */
	public static final String OBJECTROW = "ObjectRow";

	/** filled with the currently used connection name in all events */
	public static final String CONNECTIONNAME = "connectionName";

	/** filled with the pageContext during select events */
	public static final String PAGECONTEXT = "pageContext";

	/** filled with the current form during select events */
	public static final String FORMTAG = "formTag";

	/** filled with affected rows during insert/update/delete event */
	public static final String ROWSAFFECTED = "rowsAffected";

	private HttpServletRequest request;

	private DbFormsConfig config;

	private Connection connection;

	private Table table;

	private Map attributes = new HashMap();

	private ISqlFilter sqlFilterTag;

	/**
	 * Creates a new DbEventInterceptorData object.
	 * 
	 * @param request
	 *            DOCUMENT ME!
	 * @param config
	 *            DOCUMENT ME!
	 * @param connection
	 *            DOCUMENT ME!
	 */
	public DbEventInterceptorData(HttpServletRequest request,
			DbFormsConfig config, Connection connection, Table table) {
		this.request = request;
		this.config = config;
		this.connection = connection;
		this.table = table;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Map getAttributesMap() {
		return attributes;
	}

	/**
	 * reads a value from the attributes list
	 * 
	 * @param key
	 *            key to read
	 * 
	 * @return The value. If not found null
	 */
	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	/**
	 * Stores a value in the attributes list
	 * 
	 * @param key
	 *            key for the value
	 * @param value
	 *            value to store
	 */
	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}

	/**
	 * Removes a value in the attributes list
	 * 
	 * @param key
	 *            key for the value
	 */
	public void removeAttribute(String key) {
		attributes.remove(key);
	}

	/**
	 * @return Returns the config.
	 */
	public DbFormsConfig getConfig() {
		return config;
	}

	/**
	 * @return Returns the connection.
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * @return Returns the request.
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @return Returns the table.
	 */
	public Table getTable() {
		return table;
	}

	public ISqlFilter getSqlFilterTag() {
		return sqlFilterTag;
	}

	public void setSqlFilterTag(ISqlFilter sqlFilterTag) {
		this.sqlFilterTag = sqlFilterTag;
	}
}
