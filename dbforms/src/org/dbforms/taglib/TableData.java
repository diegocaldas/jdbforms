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

package org.dbforms.taglib;

import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import org.dbforms.util.*;
import org.apache.log4j.Category;

/****
 *
 * external data to be nested into radio, checkbox or select - tag!
 * (useful only in conjunction with radio, checkbox or select - tag)
 *
 * <tagclass>org.dbforms.taglib.TableData</tagclass>
 * <bodycontent>empty</bodycontent>
 *
 *
 * <p>this tag provides data to radio, checkbox or select - tags. it may be used for
 * cross-references to other tables.</p>
 *
 * @author Joachim Peer <j.peer@gmx.net>
 */

public class TableData extends EmbeddedData {

	static Category logCat = Category.getInstance(TableData.class.getName());
	// logging category for this class

	private String foreignTable;
	private String visibleFields;
	private String storeField;
	private String orderBy;

	public void setForeignTable(String foreignTable) {
		this.foreignTable = foreignTable;
	}

	public String getForeignTable() {
		return foreignTable;
	}

	public void setVisibleFields(String visibleFields) {
		this.visibleFields = visibleFields;
	}

	public String getVisibleFields() {
		return visibleFields;
	}

	public void setStoreField(String storeField) {
		this.storeField = storeField;
	}

	public String getStoreField() {
		return storeField;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
		logCat.info("setOrderBy(\"" + orderBy + "\")");
	}

	public String getOrderBy() {
		return orderBy;
	}

	/**
	returns Hashtable with data. Its keys represent the "value"-fields for the DataContainer-Tag, its values
	represent the visible fields for the Multitags.
	(DataContainer are: select, radio, checkbox and a special flavour of Label).
	*/
	protected Vector fetchData(Connection con) throws SQLException {

		Vector vf = ParseUtil.splitString(visibleFields, ",;~");

		StringBuffer queryBuf = new StringBuffer();

		queryBuf.append("SELECT ");
		queryBuf.append(storeField);
		queryBuf.append(", ");

		for (int i = 0; i < vf.size(); i++) {
			queryBuf.append((String) vf.elementAt(i));
			if (i < vf.size() - 1)
				queryBuf.append(", ");
		}
		queryBuf.append(" FROM ");
		queryBuf.append(foreignTable);

		if (orderBy != null) {
			queryBuf.append(" ORDER BY ");
			queryBuf.append(orderBy);
		}

		logCat.info("about to execute:" + queryBuf.toString());
		PreparedStatement ps = con.prepareStatement(queryBuf.toString());
		ResultSetVector rsv = new ResultSetVector(ps.executeQuery());
		ps.close(); // #JP Jun 27, 2001

		Vector result = new Vector();

		// transforming the resultsetVector into a hashtable
		for (int i = 0; i < rsv.size(); i++) {
			String[] currentRow = (String[]) rsv.elementAt(i);

			String htKey = currentRow[0];
			StringBuffer htValueBuf = new StringBuffer();
			for (int j = 1; j < currentRow.length; j++) {
				htValueBuf.append(currentRow[j]);
				if (j < currentRow.length - 1)
					htValueBuf.append(", "); //#checkme: this could be more generic
			}
			String htValue = htValueBuf.toString();

			result.addElement(new KeyValuePair(htKey, htValue));
			// add current row, now well formatted, to result
		}

		return result;
	}

}