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
 * <p>This event scrolls the current ResultSet to the next row of data</p>
 *
 * @author Joe Peer <j.peer@gmx.net>
 */

public class NavNextEvent extends NavigationEvent {

    static Category logCat = Category.getInstance(NavNextEvent.class.getName()); // logging category for this class

	private int stepWidth = 1;

	public NavNextEvent(String action, HttpServletRequest request, DbFormsConfig config) {
		this.config = config;
		tableId = ParseUtil.getEmbeddedStringAsInteger(action, 2, '_');
		this.table = config.getTable(tableId);

		String stepWidthStr = ParseUtil.getParameter(request,"data"+action+"_sw");
		if(stepWidthStr!=null)
			stepWidth = Integer.parseInt(stepWidthStr);
	}

    // for call from localevent
	public NavNextEvent(Table table, DbFormsConfig config) {
	  this.table = table;
	  this.tableId = table.getId();
	  this.config = config;
	}

	public ResultSetVector processEvent(FieldValue[] childFieldValues, FieldValue[] orderConstraint, int count, String firstPosition, String lastPosition, Connection con)
	throws SQLException {
		logCat.info("==>NavNextEvent");

		// select in given order everyting thats greater than lastpos
		table.fillWithValues(orderConstraint, lastPosition);
		return table.doConstrainedSelect(table.getFields(), childFieldValues, orderConstraint, FieldValue.COMPARE_EXCLUSIVE, count, con);
	}

}