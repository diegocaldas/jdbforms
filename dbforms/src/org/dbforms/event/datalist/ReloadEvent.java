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
import org.dbforms.config.FieldValue;
import org.dbforms.config.ResultSetVector;
import org.dbforms.config.Table;

import org.dbforms.event.AbstractNavigationEvent;
import org.dbforms.event.datalist.dao.DataSourceFactory;
import org.dbforms.event.datalist.dao.DataSourceSessionList;
import org.dbforms.event.eventtype.EventType;
import org.dbforms.interfaces.DbEventInterceptorData;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

/**
 * This event reloads the current dataset and moves to the first row of data
 * Works with new factory classes.
 * 
 * @author Henner Kollmann
 */
public class ReloadEvent extends AbstractNavigationEvent {
	private static Log logCat = LogFactory.getLog(ReloadEvent.class.getName()); // logging

	// category
	// for
	// this
	// class
	private boolean isForce = false;

	private boolean isInsert = false;

	/**
	 * Creates a new ReloadEvent object.
	 * 
	 * @param action
	 *            the action string
	 * @param request
	 *            the request object
	 * @param config
	 *            the configuration object
	 */
	public ReloadEvent(String action, HttpServletRequest request,
			DbFormsConfig config) {
		super(action, request, config);
		isForce = action.indexOf("_force_") > 0;
		isInsert = !isForce && (action.indexOf("_ins_") > 0);
	}

	/**
	 * Creates a new ReloadEvent object.
	 * 
	 * @param table
	 *            the input table object
	 * @param request
	 *            the request object
	 * @param config
	 *            the configuration object
	 */
	public ReloadEvent(Table table, HttpServletRequest request,
			DbFormsConfig config) {
		super(table, request, config);
	}

	/**
	 * Process the current event.
	 * 
	 * @param filterFieldValues
	 *            FieldValue array used to restrict a set of data
	 * @param orderConstraint
	 *            FieldValue array used to build a cumulation of rules for
	 *            ordering (sorting) and restricting fields to the actual block
	 *            of data
	 * @param sqlFilter
	 *            DOCUMENT ME!
	 * @param sqlFilterParams
	 *            DOCUMENT ME!
	 * @param count
	 *            record count
	 * @param firstPosition
	 *            a string identifying the first resultset position
	 * @param lastPosition
	 *            a string identifying the last resultset position
	 * @param dbConnectionName
	 *            name of the used db connection. Can be used to get an own db
	 *            connection, e.g. to hold it during the session (see
	 *            DataSourceJDBC for example!)
	 * @param con
	 *            the JDBC Connection object
	 * 
	 * @return a ResultSetVector object
	 * 
	 * @exception SQLException
	 *                if any error occurs
	 */
	public ResultSetVector processEvent(FieldValue[] filterFieldValues,
			FieldValue[] orderConstraint, String sqlFilter,
			FieldValue[] sqlFilterParams, int count, String firstPosition,
			String lastPosition, DbEventInterceptorData interceptorData)
			throws SQLException {
		if (isInsert) {
			return null;
		}
		logCat.info("==>NavCurrentEvent.processEvent");

		DataSourceSessionList ds = DataSourceSessionList
				.getInstance(getRequest());
		DataSourceFactory qry = null;
		String position = null;

		if (isForce) {
			setType(EventType.EVENT_NAVIGATION_FORCERELOAD);
			ds.remove(getTable(), getRequest());
		} else {
			qry = ds.get(getTable(), getRequest());
		}

		if (qry == null) {
			qry = new DataSourceFactory((String) interceptorData
					.getAttribute(DbEventInterceptorData.CONNECTIONNAME),
					interceptorData.getConnection(), getTable());
			qry.setSelect(filterFieldValues, orderConstraint, sqlFilter,
					sqlFilterParams);
			ds.put(getTable(), getRequest(), qry);
		}

		position = (count == 0) ? null : getTable().getKeyPositionString(
				getTable().getFieldValues(lastPosition));

		ResultSetVector res = qry.getCurrent(interceptorData, position, count);

		if (ResultSetVector.isNull(res)) {
			res = qry.getLast(interceptorData, count);
		}

		return res;
	}
}
