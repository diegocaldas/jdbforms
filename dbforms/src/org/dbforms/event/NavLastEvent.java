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

import org.dbforms.*;
import org.dbforms.util.*;
import javax.servlet.http.*;
import java.sql.*;
import org.apache.log4j.Category;

/****
 *
 * <p>This event scrolls the current ResultSet to its last row of data</p>
 *
 * @author Joe Peer <j.peer@gmx.net>
 */

public class NavLastEvent extends NavigationEvent {

  static Category logCat = Category.getInstance(NavLastEvent.class.getName()); // logging category for this class

	public NavLastEvent(String action, HttpServletRequest request, DbFormsConfig config) {
		this.config = config;
		tableId = ParseUtil.getEmbeddedStringAsInteger(action, 2, '_');
		this.table = config.getTable(tableId);
	}

	// for call from localevent
	public NavLastEvent(Table table, DbFormsConfig config) {
	  this.table = table;
	  this.tableId = table.getId();
	  this.config = config;
	}

	public ResultSetVector processEvent(FieldValue[] childFieldValues, FieldValue[] orderConstraint, int count, String firstPost, String lastPos, Connection con)
	throws SQLException {
		// select from table in inverted order
		logCat.info("==>NavLastEvent");
		FieldValue.invert(orderConstraint);
		ResultSetVector resultSetVector = table.doConstrainedSelect(table.getFields(), childFieldValues, orderConstraint, FieldValue.COMPARE_NONE, count, con);
		FieldValue.invert(orderConstraint);
		resultSetVector.flip();
		return resultSetVector;
	}

}