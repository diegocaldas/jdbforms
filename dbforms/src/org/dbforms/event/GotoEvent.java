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
import java.util.*;
import org.apache.log4j.Category;

/****
 *
 * <p>This event forces the controller to forward the current request to a Request-Dispatcher
 * specified by the Application-Developer in a "org.dbforms.taglib.DbGotoButton".</p>
 *
 *
 * @author Joe Peer <j.peer@gmx.net>
 */

public class GotoEvent extends NavigationEvent {

    static Category logCat = Category.getInstance(GotoEvent.class.getName()); // logging category for this class

	private String position; // where to go in associated table
	private Table table;

 /**
 * <p>constructor - parses the event details</p>
 * <p>Depending on the way the attributes where provided by the developer,  different
 * ways are used for resolving the dispatcher the user wants to get called and the position
 * he wants the ResultSet to be scrolled to.
 * </p>
 */


	public GotoEvent(String action, HttpServletRequest request, DbFormsConfig config) {

		this.config = config;
		this.followUp = ParseUtil.getParameter(request, "data"+action+"_fu");

		logCat.info("gotoevent's followup = "+followUp);

		String destTable = ParseUtil.getParameter(request, "data"+action+"_destTable");
		if(destTable == null) {
			this.tableId = -1;
			return; // if the user wants a simple, dumb link and we want no form to be navigated through
		}

		//# fixme: decision for *1* of the 2 approaches should be met soon!! (either id- OR name-based lookup)
		this.table = config.getTableByName(destTable);
		if(table==null) {
		  this.table = config.getTable(Integer.parseInt(destTable));
		}
		this.tableId = table.getId();


		// the position to go to within the destination-jsp's-table	can be given
		// more or less directly

		String destPos = ParseUtil.getParameter(request,"data"+action+"_destPos");

		// the direct way - i.e. "1~23"
		if(destPos!=null) {

			this.position = destPos;

		} else {

			String keyToDestPos = ParseUtil.getParameter(request,"data"+action+"_keyToDestPos");
			// the 1-leveled indirect way: i.e. "k_1_1" whereby k_1_1 leads to "1:2:23"
			if(keyToDestPos!=null) {

				this.position = ParseUtil.getParameter(request,keyToDestPos);

			} else {

				String keyToKeyToDestPos = ParseUtil.getParameter(request,"data"+action+"_keyToKeyToDestPos");
				// the 2-leveled indirect way: i.e. "my_sel" wherby "mysel" leads to "1_1",
				// which leads to "1:2:23"
				if(keyToKeyToDestPos != null) {

					String widgetValue = ParseUtil.getParameter(request,keyToKeyToDestPos); // i.e. "1_1"

					this.position = (String) ParseUtil.getParameter(request,"k_"+widgetValue); // i.e. 1~23

					logCat.info("--->pos="+position);

				}
			}
		}
	}

	/**
	this constructer is not called by the controller but, actually, BY THE VIEW
	for example if the FormTag "gotoPrefix" attribute is set an a GotoEvent needs to be
	instanciated
	*/
	public GotoEvent(String position, Table table) {
		this.table = table;
		this.position = position;
	}


	public ResultSetVector processEvent(FieldValue[] childFieldValues, FieldValue[] orderConstraint, int count, String firstPosition, String lastPosition, Connection con)
	throws SQLException {
		if(position!=null)
			table.fillWithValues(orderConstraint, position);

		logCat.info("gotopos = "+position);

		return table.doConstrainedSelect(table.getFields(), childFieldValues, orderConstraint, position!=null ? FieldValue.COMPARE_INCLUSIVE : FieldValue.COMPARE_NONE, count, con);
	}

}