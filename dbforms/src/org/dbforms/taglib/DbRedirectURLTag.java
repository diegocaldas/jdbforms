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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

/****
 *
 * <p>the 3 examples below produce all the same result</p>
 *
 * <p><linkURL href="customer.jsp" table="customer" position="1:2:12-3:4:1992" /></p>
 *
 * <p><linkURL href="customer.jsp" table="customer" position="<%= currentKey %>" /></p>
 *
 * <p><linkURL href="customer.jsp" table="customer" />
 * <ul>  <position fieldName="id" value="103" /><br/>
 *   <position fieldName="cust_lang" value="2" /></ul>
 * </link>
 * </p>
 *
 * <p>result (off course without the line feeds)</p>
 *
 *
 * <pre>/servlet/control?
 * ac_goto_x=t&
 * data_ac_goto_x_fu=/customer.jsp&
 * data_ac_goto_x_destTable=17&
 * data_ac_goto_x_destPos=103~2</pre>
 *
 *
 * <p>Use it like this:</p>
 *
 *
 * <pre><a href="<linkURL href="customer.jsp" tableName="customer" position="103~2" />"> some text </a></pre>
 *
 * @author Neal Katz , based on work from Joachim Peer <j.peer@gmx.net>
 */
public class DbRedirectURLTag
	extends DbLinkURLTag
	implements javax.servlet.jsp.tagext.TryCatchFinally {
	private static Category logCat =
		Category.getInstance(DbRedirectURLTag.class.getName());
	// logging category for this class

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws JspException  thrown when error occurs in processing the body of
	 *                       this method
	 * @throws IllegalArgumentException thrown when some parameters are missing.
	 */
	public int doStartTag() throws javax.servlet.jsp.JspException {
		return super.doStartTag();
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws JspException  thrown when error occurs in processing the body of
	 *                       this method
	 */
	public int doBodyEndTag() throws javax.servlet.jsp.JspException {
		return super.doBodyEndTag();
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws JspException  thrown when error occurs in processing the body of
	 *                       this method
	 */
	public int doEndTag() throws javax.servlet.jsp.JspException {
		try {
			HttpServletResponse response =
				(HttpServletResponse) pageContext.getResponse();
			String s = makeUrl();
			s = response.encodeURL(s);
			response.sendRedirect(s);
		} catch (java.io.IOException ioe) {
			throw new JspException("IO Error: " + ioe.getMessage());
		} catch (Exception e) {
			throw new JspException("Error: " + e.getMessage());
		}
		return SKIP_PAGE;
	}

	/**
	 * DOCUMENT ME!
	 */
	public void doFinally() {
		logCat.info("doFinally called");
		super.doFinally();
	}

	/**
	 * @see javax.servlet.jsp.tagext.TryCatchFinally#doCatch(java.lang.Throwable)
	 */
	public void doCatch(Throwable t) throws Throwable {
		throw t;
	}

}
