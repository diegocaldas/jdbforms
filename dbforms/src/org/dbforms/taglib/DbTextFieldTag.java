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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.dbforms.taglib;

import java.util.*;
import java.sql.*;
import java.io.*;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import org.dbforms.*;

import org.apache.log4j.Category;

/****
 *
 * <p>This tag renders a HTML TextArea - Element</p>
 *
 * this tag renders a dabase-datadriven textArea, which is an active element - the user
 * can change data
 *
 * @author Joachim Peer <j.peer@gmx.net>
 */


public class DbTextFieldTag extends DbBaseInputTag  {

  static Category logCat = Category.getInstance(DbTextFieldTag.class.getName()); // logging category for this class

  public int doStartTag() throws javax.servlet.jsp.JspException {
	return SKIP_BODY;
  }

/* ===========================================================================
 * grunikiewicz.philip@hydro.qc.ca
 * 2001-05-23
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
 *	From within a given JSP page, a developer is required to set the SOLD field to 'true' when the user clicks
 *	the updateButton.  The SOLD field should not to be displayed on screen.
 *
 *		<db:textField fieldName="SOLD" hidden="true" overrideValue="true"/>
 *		<db:updateButton caption="OK"/>
 *
*/

public int doEndTag() throws javax.servlet.jsp.JspException {

	try {

		/* Does the developer require the field to be hidden or displayed? */
		String value =
			("true".equals(this.getHidden()))
				? "<input type=\"hidden\" name=\""
				: "<input type=\"text\" name=\"";

		StringBuffer tagBuf = new StringBuffer(value);
		//if(parsedSearchMode == DbBaseHandlerTag.SEARCHMODE_NONE) {
		tagBuf.append(getFormFieldName());
		//} else {
		//tagBuf.append(getSearchFieldName());
		//}
		tagBuf.append("\" value=\"");
		//if(parsedSearchMode == DbBaseHandlerTag.SEARCHMODE_NONE) {

		/* If the overrideValue attribute has been set, use its value instead of the one
			retrieved from the database.  This mechanism can be used to set an initial default
			value for a given field. */

		if (this.getOverrideValue() != null) {
			tagBuf.append(this.getOverrideValue());
		} else {
			tagBuf.append(getFormFieldValue());
		}
		//}
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
		tagBuf.append(">");

		//if(parsedSearchMode != DbBaseHandlerTag.SEARCHMODE_NONE) {
		//tagBuf.append(getSearchModeTag());
		//}

		pageContext.getOut().write(tagBuf.toString());
	} catch (java.io.IOException ioe) {
		throw new JspException("IO Error: " + ioe.getMessage());
	}

	return EVAL_PAGE;
}

	private java.lang.String hidden = "false";
	private java.lang.String overrideValue = null;

/**
 * grunikiewicz.philip@hydro.qc.ca
 * 2001-05-23
 * @param newHidden java.lang.String
 *
 * Determines if the text field should be hidden or displayed
 * @return java.lang.String
 */
public java.lang.String getHidden() {
	return hidden;
}

/**
 * grunikiewicz.philip@hydro.qc.ca
 * 2001-05-23
 *
 * Defines the text field's default value. When this attribute is set, the value retrieved
 * from the database is ignored.
 *
 * @return java.lang.String
 */
public java.lang.String getOverrideValue() {
	return overrideValue;
}

/**
 * grunikiewicz.philip@hydro.qc.ca
 * 2001-05-23
 * @param newHidden java.lang.String
 *
 * Determines if the text field should be hidden or displayed
 */
public void setHidden(java.lang.String newHidden) {
	hidden = newHidden;
}

/**
 * grunikiewicz.philip@hydro.qc.ca
 * 2001-05-23
 *
 * Defines the text field's default value. When this attribute is set, the value retrieved
 * from the database is ignored.
 *
 * @param newOverrideValue java.lang.String
 */
public void setOverrideValue(java.lang.String newOverrideValue) {
	overrideValue = newOverrideValue;
}
}