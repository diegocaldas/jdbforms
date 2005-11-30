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
package interceptors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.config.ConfigLoader;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.FieldValues;
import org.dbforms.config.Table;
import org.dbforms.config.ValidationException;

import org.dbforms.event.DbEventInterceptorSupport;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hkk
 * 
 * To change this generated comment go to Window>Preferences>Java>Code
 * Generation>Code and Comments
 */
public class BookstoreWithInterceptorTest extends DbEventInterceptorSupport {
	private static Log logCat = LogFactory.getLog(ConfigLoader.class);

	/**
	 * DOCUMENT ME!
	 * 
	 * @param request
	 *            DOCUMENT ME!
	 * @param table
	 *            DOCUMENT ME!
	 * @param fieldValues
	 *            DOCUMENT ME!
	 * @param config
	 *            DOCUMENT ME!
	 * @param con
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @throws ValidationException
	 *             DOCUMENT ME!
	 */
	public int preInsert(HttpServletRequest request, Table table,
			FieldValues fieldValues, DbFormsConfig config, Connection con)
			throws ValidationException {
		logCat.info("preInsert called");

		Statement stmt;
		ResultSet rs = null;
		long new_id = 0;
		String strSql = "";
		String strParentID = "AUTHOR_ID";
		String strID = "BOOK_ID";
		String strTbl = "BOOK";

		if (fieldValues.get(strID) == null) {
			try {
				stmt = con.createStatement();
				strSql = "select max(" + strID + ") from " + strTbl;

				if (fieldValues.get(strParentID) != null) {
					strSql = strSql + " where " + strParentID + "="
							+ fieldValues.get(strParentID);
				}

				rs = stmt.executeQuery(strSql);
				rs.next();
				new_id = rs.getLong(1) + 1;
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			if (new_id == 0) {
				throw new ValidationException("Error generating automatic IDs");
			}
			
			String test = fieldValues.get("ISBN").getFieldValue();
			if ("exception".equals(test)) {
				throw new ValidationException("test exception");
			}
			
			
			fieldValues.remove(strID);
			setValue(table, fieldValues, strID, Long.toString(new_id));
			setValue(table, fieldValues, strParentID, Long.toString(1));

			// Test: set title to fixed string!
			setValue(table, fieldValues, "TITLE",
					"fixed title in new interceptor");
		}

		return GRANT_OPERATION;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param request
	 *            DOCUMENT ME!
	 * @param config
	 *            DOCUMENT ME!
	 * @param con
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public int preSelect(HttpServletRequest request, DbFormsConfig config,
			Connection con) {
		logCat.info("preSelect called");

		return GRANT_OPERATION;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param request
	 *            DOCUMENT ME!
	 * @param table
	 *            DOCUMENT ME!
	 * @param fieldValues
	 *            DOCUMENT ME!
	 * @param config
	 *            DOCUMENT ME!
	 * @param con
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @throws ValidationException
	 *             DOCUMENT ME!
	 */
	public int preUpdate(HttpServletRequest request, Table table,
			FieldValues fieldValues, DbFormsConfig config, Connection con)
			throws ValidationException {
		logCat.info("preUpdate called");

		if ("42".equals(fieldValues.get("ISBN").getFieldValue())) {
			return IGNORE_OPERATION;
		}

		fieldValues.remove("ISBN");

		return GRANT_OPERATION;
	}
}