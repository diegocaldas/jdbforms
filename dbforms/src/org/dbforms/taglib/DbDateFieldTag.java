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

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.*;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import org.dbforms.*;

import org.apache.log4j.Category;

import javax.servlet.http.*;

/****
 *
 * <p>This tag renders a HTML TextArea - Element</p>
 *
 * this tag renders a dabase-datadriven textArea, which is an active element - the user
 * can change data
 *
 * @author Joachim Peer <j.peer@gmx.net>
 */

public class DbDateFieldTag extends DbBaseInputTag {

	static Category logCat = Category.getInstance(DbDateFieldTag.class.getName());
	// logging category for this class

	public int doStartTag() throws javax.servlet.jsp.JspException {

		// Use format defined in config file
		if (this.format == null) {
			this.format = DbFormsConfig.getDateFormatter();
		}

		return SKIP_BODY;
	}

	/* ===========================================================================
	 * grunikiewicz.philip@hydro.qc.ca
	 * 2001-06-26
	 *
	 * Added two new attributes: hidden and overrideValue
	 *
	 *	hidden : 		Determines if the text field should be hidden or displayed.
	 *	overrideValue : Defines the text field's default value. When this attribute is set, the value retrieved
	 * 					from the database is ignored.
	 *
	 * These attributes where added to allow a developer to:
	 *		- set a field's default value (Insert mode)
	 *		- override a field's value with a developer defined value (Update mode)
	 *		- hide a field, set its value (Insert and Update mode)
	 *
	 *	Example:
	 *
	 *	See DbTextField
	 *
	*/

	public int doEndTag() throws javax.servlet.jsp.JspException {

		HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
		Vector errors = (Vector) request.getAttribute("errors");

		try {

			/* Does the developer require the field to be hidden or displayed? */
			String value =
				("true".equals(this.getHidden()))
					? "<input type=\"hidden\" name=\""
					: "<input type=\"text\" name=\"";

			StringBuffer tagBuf = new StringBuffer(value);
			tagBuf.append(getFormFieldName());
			tagBuf.append("\" value=\"");

			/* If the overrideValue attribute has been set, use its value instead of the one
				retrieved from the database.  This mechanism can be used to set an initial default
				value for a given field. */

			if (this.getOverrideValue() != null) {
				//If the redisplayFieldsOnError attribute is set and we are in error mode, forget override!
				if ("true".equals(parentForm.getRedisplayFieldsOnError())
					&& errors != null
					&& errors.size() > 0) {
					tagBuf.append(getFormFieldValue());

				} else {
					tagBuf.append(this.getOverrideValue());

				}
			} else {
				tagBuf.append(getFormFieldValue());
			}

			tagBuf.append("\" ");

			if (accessKey != null) {
				tagBuf.append(" accesskey=\"");
				tagBuf.append(accessKey);
				tagBuf.append("\"");
			}

			if (maxlength != null) {
				tagBuf.append(" maxlength=\"");
				tagBuf.append(maxlength);
				tagBuf.append("\"");
			}

			if (cols != null) {
				tagBuf.append(" size=\"");
				tagBuf.append(cols);
				tagBuf.append("\"");
			}

			if (tabIndex != null) {
				tagBuf.append(" tabindex=\"");
				tagBuf.append(tabIndex);
				tagBuf.append("\"");
			}

			tagBuf.append(prepareStyles());
			tagBuf.append(prepareEventHandlers());
			tagBuf.append("/>");
			
			//Setup validation parameters
			tagBuf.append(prepareValidation());


			pageContext.getOut().write(tagBuf.toString());
		} catch (java.io.IOException ioe) {
			throw new JspException("IO Error: " + ioe.getMessage());
		}

		return EVAL_PAGE;
	}

	private java.lang.String hidden = "false";
	private java.lang.String overrideValue;

	/**
	 * Insert the method's description here.
	 * Creation date: (2001-06-26 16:19:01)
	 * @return java.lang.String
	 */
	public java.lang.String getHidden() {
		return hidden;
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (2001-06-26 16:19:25)
	 * @return java.lang.String
	 */
	public java.lang.String getOverrideValue() {
		return overrideValue;
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (2001-06-26 16:19:01)
	 * @param newHidden java.lang.String
	 */
	public void setHidden(java.lang.String newHidden) {
		hidden = newHidden;
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (2001-06-26 16:19:25)
	 * @param newOverrideValue java.lang.String
	 */
	public void setOverrideValue(java.lang.String newOverrideValue) {
		overrideValue = newOverrideValue;
	}
}