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

import javax.sql.DataSource;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/*************************************************************
 * Grunikiewicz.philip@hydro.qc.ca
 * 2001-12-18
 *
 * Obtain a connection (from the connection pool) using same settings defined
 * in dbForms-config.xml file
 *
 * ***************************************************************/
public class DbGetConnection
	extends DbBaseHandlerTag
	implements javax.servlet.jsp.tagext.TryCatchFinally {

	private String dbConnectionName;

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws JspException DOCUMENT ME!
	 * @throws IllegalArgumentException DOCUMENT ME!
	 */
	public int doStartTag() throws JspException {
		try {
			// get the connection and place it in attribute;
			DataSource ds = getConfig().getDbConnection(dbConnectionName);
			String s = getId();
			pageContext.setAttribute(getId(), ds, PageContext.PAGE_SCOPE);
		} catch (Exception e) {
			throw new JspException("Database error" + e.getMessage());
		}
		return SKIP_BODY;
	}

	/**
	* DOCUMENT ME!
	*
	* @param name DOCUMENT ME!
	*/
	public void setDbConnectionName(String name) {
		dbConnectionName = name;
	}

	/**
	* DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getDbConnectionName() {
		return dbConnectionName;
	}

	/**
	* DOCUMENT ME!
	*/
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

}
