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
package org.dbforms.taglib;

import java.io.*;
import javax.servlet.jsp.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.dbforms.config.FieldTypes;
import org.dbforms.config.Table;
import org.dbforms.util.SqlUtil;
import org.apache.log4j.Category;

/**
 * #fixme docu to come
 * 
 * @author Joe Peer
 */
public class DbBlobContentTag extends DbBaseHandlerTag implements
		javax.servlet.jsp.tagext.TryCatchFinally {
	private Category logCat = Category.getInstance(this.getClass().getName());

	private String dbConnectionName;

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @throws javax.servlet.jsp.JspException
	 *             DOCUMENT ME!
	 * @throws IllegalArgumentException
	 *             DOCUMENT ME!
	 * @throws JspException
	 *             DOCUMENT ME!
	 */
	public int doEndTag() throws javax.servlet.jsp.JspException {
		try {
			if (getParentForm().getFooterReached()) {
				return EVAL_PAGE; // nothing to do when no data available..
			}

			StringBuffer queryBuf = new StringBuffer();
			queryBuf.append("SELECT ");
			queryBuf.append(getField().getName());
			queryBuf.append(" FROM ");
			queryBuf.append(getParentForm().getTable().getName());
			queryBuf.append(" WHERE ");
			queryBuf.append(getParentForm().getTable()
					.getWhereClauseForKeyFields());
			logCat.info("blobcontent query- " + queryBuf.toString());

			StringBuffer contentBuf = new StringBuffer();

			try {
				Connection con = getConfig().getConnection(dbConnectionName);
				PreparedStatement ps = con
						.prepareStatement(queryBuf.toString());
				getParentForm().getTable().populateWhereClauseWithKeyFields(
						getKeyVal(), ps, 1);

				ResultSet rs = ps.executeQuery();

				if (rs.next()) {
					InputStream is = null;
					String fileName = null;
					if (getField().getType() == FieldTypes.DISKBLOB) {
						fileName = rs.getString(1);
						is = SqlUtil.readDiskBlob(fileName, getField()
								.getDirectory(), null);
					} else {
						/*
						 * As the classic and new blob handling modes are
						 * distinguished by fileName, we use a small hack here
						 * to provide empty string or null as the fileName
						 * according to the blob handling strategy defined in
						 * the configuration file.
						 */
						fileName = (getField().getTable()
								.getBlobHandlingStrategy() == Table.BLOB_CLASSIC) ? null
								: "";
						is = SqlUtil.readDbFieldBlob(rs, fileName);
					}
					if (is != null) {
						BufferedReader br = new BufferedReader(
								new InputStreamReader(is));
						char[] c = new char[1024];
						int read;
						while ((read = br.read(c)) != -1) {
							contentBuf.append(c, 0, read);
						}
						is.close();
					}
				} else {
					logCat.info("fs- we have got no result" + queryBuf);
				}
				SqlUtil.closeConnection(con);
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

			pageContext.getOut().write(escapeHtml(contentBuf.toString()));
		} catch (java.io.IOException ioe) {
			throw new JspException("IO Error: " + ioe.getMessage());
		}

		return EVAL_PAGE;
	}

	public void doFinally() {
		dbConnectionName = null;
		super.doFinally();
	}

	/**
	 * @see javax.servlet.jsp.tagext.TryCatchFinally#doCatch(java.lang.Throwable)
	 */
	public void doCatch(Throwable t) throws Throwable {
		throw t;
	}

	// ------------------------------------------------------ Protected Methods
	// DbForms specific

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	private String getKeyVal() {
		return getParentForm().getTable().getKeyPositionString(
				getParentForm().getResultSetVector());
	}

	/**
	 * @return
	 */
	public String getDbConnectionName() {
		return dbConnectionName;
	}

	/**
	 * @param string
	 */
	public void setDbConnectionName(String string) {
		dbConnectionName = string;
	}

}