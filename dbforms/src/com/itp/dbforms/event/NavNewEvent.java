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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.itp.dbforms.event;

import com.itp.dbforms.*;
import com.itp.dbforms.util.*;
import javax.servlet.http.*;
import java.sql.*;
import org.apache.log4j.Category;

/****
 *
 * <p>This event signalizes to the framework that the user wants to initializa a new dataset</p>
 * #fixme: lousy description
 *
 * @author Joe Peer <joepeer@excite.com>
 */

public class NavNewEvent extends NavigationEvent {

    static Category logCat = Category.getInstance(NavNewEvent.class.getName()); // logging category for this class

	public NavNewEvent(String action, HttpServletRequest request, DbFormsConfig config) {
		this.config = config;
		tableId = ParseUtil.getEmbeddedStringAsInteger(action, 2, '_');
		this.table = config.getTable(tableId);
	}

    // for call from localevent
	public NavNewEvent(Table table, DbFormsConfig config) {
	  this.table = table;
	  this.tableId = table.getId();
	  this.config = config;
	}

	public ResultSetVector processEvent(FieldValue[] childFieldValues, FieldValue[] orderConstraint, int count, String firstPost, String lastPos, Connection con) {
	//	rsv.setPointer( rsv.size() ); //#fixme - this is not well thought through
		logCat.info("processed NavNewEvent");
		return null;
	}


}