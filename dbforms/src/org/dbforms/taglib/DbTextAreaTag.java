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

public class DbTextAreaTag extends DbBaseInputTag  {

  static Category logCat = Category.getInstance(DbTextAreaTag.class.getName()); // logging category for this class

	protected String wrap;
	protected String renderBody;

	public void setWrap(String wrap) {
		this.wrap = wrap;
	}

	public String getWrap() {
		return wrap;
	}

	public void setrenderBody(String renderBody) {
		this.renderBody = renderBody;
	}

	public String getrenderBody() {
		return renderBody;
	}


  public int doStartTag() throws javax.servlet.jsp.JspException {

	HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
	Vector errors = (Vector) request.getAttribute("errors");




		StringBuffer tagBuf = new StringBuffer("<textarea name=\"");
		tagBuf.append(getFormFieldName());
		tagBuf.append("\" ");

		if (cols != null) {
			tagBuf.append(" cols=\"");
			tagBuf.append(cols);
			tagBuf.append("\"");
		}

		if (wrap != null) {
			tagBuf.append(" wrap=\"");
			tagBuf.append(wrap);
			tagBuf.append("\"");
		}

		if (rows != null) {
			tagBuf.append(" rows=\"");
			tagBuf.append(rows);
			tagBuf.append("\"");
		}

		if (accessKey != null) {
			tagBuf.append(" accesskey=\"");
			tagBuf.append(accessKey);
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

		/* If the overrideValue attribute has been set, use its value instead of the one
			retrieved from the database.  This mechanism can be used to set an initial default
			value for a given field. */

		if(!"true".equals(renderBody)) {

			if (this.getOverrideValue() != null)
			{
				//If the redisplayFieldsOnError attribute is set and we are in error mode, forget override!
				if ("true".equals(parentForm.getRedisplayFieldsOnError()) && errors != null && errors.size() > 0)
				{
					tagBuf.append(getFormFieldValue());

				}
				else
				{
					tagBuf.append(this.getOverrideValue());

				}
			}
			else
			{
				tagBuf.append(getFormFieldValue());
			}

		}


	    try {
			pageContext.getOut().write(tagBuf.toString());

		}
		catch(java.io.IOException e) {
			throw new JspException("IO Error: " + e.getMessage());
		}


		if("true".equals(renderBody))
			return EVAL_BODY_TAG;
		else
			return SKIP_BODY;
  }

	public int doEndTag() throws javax.servlet.jsp.JspException {

	    try {
		  	if("true".equals(renderBody) && bodyContent != null) {
					bodyContent.writeOut(bodyContent.getEnclosingWriter());
					bodyContent.clearBody(); // workaround for duplicate rows in JRun 3.1
		    }

			pageContext.getOut().write("</textarea>");

		}
		catch(java.io.IOException e) {
			throw new JspException("IO Error: " + e.getMessage());
		}
		return EVAL_PAGE;
	}



	protected java.lang.String overrideValue;

/**
 * Insert the method's description here.
 * Creation date: (2001-06-27 17:44:16)
 * @return java.lang.String
 */
public java.lang.String getOverrideValue() {
	return overrideValue;
}

/**
 * Insert the method's description here.
 * Creation date: (2001-06-27 17:44:16)
 * @param newOverrideValue java.lang.String
 */
public void setOverrideValue(java.lang.String newOverrideValue) {
	overrideValue = newOverrideValue;
}
}