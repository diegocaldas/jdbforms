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

package com.itp.dbforms.taglib;

import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.itp.dbforms.util.*;
import org.apache.log4j.Category;

/****
 *
 * external data to be nested into radio, checkbox or select - tag!
 * (useful only in conjunction with radio, checkbox or select - tag)
 *
 * <tagclass>com.itp.dbforms.taglib.QueryData</tagclass>
 * <bodycontent>empty</bodycontent>
 *
 *
 * <p>this tag provides data to radio, checkbox or select - tags. it may be used for
 * cross-references to other tables.</p>
 *
 * <p>this tag provides similar functionlaity to "TabData", but as it allows to
 * formulate free querys including all SQL statements your RDBMS supports you have much
 * more flexibility using this tag.</p>
 *
 * <p>query building convention: first column is the "key" column for the radio/check/select
 * elements, all other colums are just "data" columns visible to the user
 * <br/>example: SELECT DISTINCT customer.id, customer.name, customer.adress, debitors.debit FROM customer INNER JOIN id ON  (SELECT id FROM debitors WHERE debit>100000) ORDER BY debit DESC</p>
 * - "id" will be threaten as key-value in select box, "name and address will be shown in select box
 *
 *
 * @author Joachim Peer <joepeer@excite.com>
 */




public class QueryData extends EmbeddedData {

    static Category logCat = Category.getInstance(QueryData.class.getName()); // logging category for this class

	private String query;

	public void setQuery(String query) {
		this.query = query;
	}

	public String getQuery() {
		return query;
	}



	/**
	returns Hashtable with data. Its keys represent the "value"-fields for the DataContainer-Tag, its values
	represent the visible fields for the Multitags.
	(DataContainer are: select, radio, checkbox and a special flavour of Label).
  */
	protected Vector fetchData(Connection con)
	throws SQLException {

	  logCat.info("about to execute user defined query:"+query);
	  PreparedStatement ps = con.prepareStatement(query);

	  ResultSetVector rsv = new ResultSetVector(ps.executeQuery());

		Vector result = new Vector();

		// transforming the resultsetVector into a hashtable
		for(int i=0; i<rsv.size(); i++) {
			String[] currentRow = (String[]) rsv.elementAt(i);

			String htKey = currentRow[0];
			StringBuffer htValueBuf = new StringBuffer();
			for(int j=1; j<currentRow.length; j++) {
				htValueBuf.append(currentRow[j]);
				if(j<currentRow.length-1) htValueBuf.append(", ");  //#checkme: this could be more generic
		  }


		  result.addElement(new KeyValuePair(htKey, htValueBuf.toString())); // add current row, now well formatted, to result
		}

		return result;
	}

}