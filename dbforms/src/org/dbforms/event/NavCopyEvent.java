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
package org.dbforms.event;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.Table;
import org.dbforms.util.FieldValue;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.ResultSetVector;

/**
 *
 * <p>This event signalizes to the framework that the user wants to initialize a new dataset 
 * with values coming from current row</p>
 * #fixme: lousy description
 *
 * @author Stefano Borghi <s.borghi@nsi-mail.it>
 *
 * @version $Revision$
 * 
 */
public class NavCopyEvent extends NavigationEvent {

	/**
	 * Creates a new NavCopyEvent object.
	 *
	 * @param action DOCUMENT ME!
	 * @param request DOCUMENT ME!
	 * @param config DOCUMENT ME!
	 */
	public NavCopyEvent(
		String action,
		HttpServletRequest request,
		DbFormsConfig config) {
		super(action, request, config);
		this.config = config;
		tableId = ParseUtil.getEmbeddedStringAsInteger(action, 2, '_');
		this.table = config.getTable(tableId);
	}

	/**
	 * Creates a new NavCopyEvent object.
	 *
	 * @param table DOCUMENT ME!
	 * @param config DOCUMENT ME!
	 */
	public NavCopyEvent(
		Table table,
		HttpServletRequest request,
		DbFormsConfig config) {
		super(table, request, config);
		this.table = table;
		this.tableId = table.getId();
		this.config = config;
	}

	/* (non-Javadoc)
	 * @see org.dbforms.event.NavigationEvent#processEvent(org.dbforms.util.FieldValue[], org.dbforms.util.FieldValue[], int, java.lang.String, java.lang.String, java.sql.Connection, java.lang.String)
	 */
	public ResultSetVector processEvent(
		FieldValue[] childFieldValues,
		FieldValue[] orderConstraint,
		int count,
		String firstPost,
		String lastPos,
		Connection con,
		String dbConnectionName)
		throws SQLException {
		logCat.info("processed NavCopyEvent");
		return null;
	}

}
