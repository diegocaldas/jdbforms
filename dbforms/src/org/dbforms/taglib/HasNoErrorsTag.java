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

/*
 * Grunikiewicz.philip@hydro.qc.ca
 * 2001-12-18
 * If their are no errors on this page and it is a follow up page, redirect...
 */

package org.dbforms.taglib;

import org.dbforms.util.*;
import java.io.IOException;
import javax.servlet.http.*;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.log4j.Category;

import java.util.*;

/**
 */

public class HasNoErrorsTag extends TagSupport {

	// logging category for this class
	static Category logCat = Category.getInstance(HasNoErrorsTag.class.getName());

	// ----------------------------------------------------------- Properties

	/**
	 * Name of the request scope attribute containing our error messages,
	 * if any.
	 */
	private String name = "errors";
	private String redirect = null;

	public String getName() {
		return (this.name);
	}

	public void setName(String name) {
		this.name = name;
	}

	// ------------------------------------------------------- Public Methods

	/**
	 * Render the specified error messages if there are any.
	 *
	 * @exception JspException if a JSP exception has occurred
	 */
	public int doStartTag() throws JspException {

		HttpServletRequest localRequest = (HttpServletRequest) pageContext.getRequest();
		HttpServletResponse localResponse =
			(HttpServletResponse) pageContext.getResponse();

		Vector errors =
			(Vector) pageContext.getAttribute(name, PageContext.REQUEST_SCOPE);

		// No errors?
		if (errors == null || errors.size() == 0) {
			String thisPage = localRequest.getRequestURI();
			String previousPage = ParseUtil.getParameter(localRequest, "source");

			if (previousPage != null && previousPage.equals(thisPage)) {
				try {

					localRequest.getRequestDispatcher(getRedirect()).forward(
						localRequest,
						localResponse);
				} catch (javax.servlet.ServletException se) {
					throw new JspException("Servlet Exception: " + se.getMessage());
				} catch (IOException ioe) {
					throw new JspException("IO Error: " + ioe.getMessage());
				}
			}

		}

		// Continue processing this page
		return (EVAL_BODY_INCLUDE);

	}

	/**
	 * Release any acquired resources.
	 */
	public void release() {

		super.release();

	}

	/**
	 * Gets the redirect
	 * @return Returns a String
	 */
	public String getRedirect() {
		return redirect;
	}
	/**
	 * Sets the redirect
	 * @param redirect The redirect to set
	 */
	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

}