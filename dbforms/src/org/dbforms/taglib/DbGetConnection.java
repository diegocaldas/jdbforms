/*
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
import org.dbforms.*;

import org.apache.log4j.Category;

/*************************************************************
 * Grunikiewicz.philip@hydro.qc.ca
 * 2001-12-18
 * 
 * Obtain a connection (from the connection pool) using same settings defined 
 * in dbForms-config.xml file
 * 
 * ***************************************************************/

public class DbGetConnection extends BodyTagSupport {

	static Category logCat = Category.getInstance(DbGetConnection.class.getName());
	// logging category for this class

	private String id;
	private Connection con;

	public int doStartTag() throws JspException {

		try {

			// Place connection in attribute
			pageContext.setAttribute(this.getId(), con, PageContext.PAGE_SCOPE);

		} catch (Exception e) {
			throw new JspException("Database error" + e.toString());
		}

		return EVAL_BODY_TAG;
		
	}

	public int doAfterBody() {
		try {
			bodyContent.writeOut(bodyContent.getEnclosingWriter());
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return SKIP_BODY;
	}

	/**
	 * Gets the id
	 * @return Returns a String
	 */
	public String getId() {
		return id;
	}
	/**
	 * Sets the id
	 * @param id The id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	public void release() {
		super.release();

		// The connection should not be null - If it is, then you might have an infrastructure problem!
		// Be sure to look into this!  Hint: check out your pool manager's performance! 

		if (con != null) {
			try {
				logCat.debug("About to close connection - " + con);
				con.close();
				logCat.debug("Connection closed");
			} catch (java.sql.SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	public void setPageContext(PageContext pc) {
		super.setPageContext(pc);
		DbFormsConfig config =
			(DbFormsConfig) pageContext.getServletContext().getAttribute(
				DbFormsConfig.CONFIG);

		if (config == null)
			throw new IllegalArgumentException("Troubles with DbForms config xml file: can not find CONFIG object in application context! check system configuration! check if application crashes on start-up!");

		DbConnection aDbConnection = config.getDbConnection();

		if (aDbConnection == null)
			throw new IllegalArgumentException("Troubles in your DbForms config xml file: DbConnection not properly included - check manual!");

		con = aDbConnection.getConnection();
		logCat.debug("Created new connection - " + con);

		if (con == null)
			throw new IllegalArgumentException(
				"JDBC-Troubles: was not able to create connection, using the following DbConnection:"
					+ aDbConnection.toString());
	}

}
