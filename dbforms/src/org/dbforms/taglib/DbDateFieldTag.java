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
import java.text.*;

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


public class DbDateFieldTag extends DbBaseInputTag  {

  static Category logCat = Category.getInstance(DbDateFieldTag.class.getName()); // logging category for this class



  public int doStartTag() throws javax.servlet.jsp.JspException {

	// a data format default to yyyy-MM-dd, if no other stlye is applied!
    // the string to use could be made configurable using web.xml
    // will be done if there's demand for it.

	if(this.format==null) {
	  this.format =	new SimpleDateFormat("yyyy-MM-dd");
	}

    return SKIP_BODY;
  }



  public int doEndTag() throws javax.servlet.jsp.JspException {

	try {

      StringBuffer tagBuf = new StringBuffer("<input type=\"text\" name=\"");
      tagBuf.append(getFormFieldName());
      tagBuf.append("\" value=\"");
	  tagBuf.append(getFormFieldValue());
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

	pageContext.getOut().write(tagBuf.toString());
		} catch(java.io.IOException ioe) {
			throw new JspException("IO Error: "+ioe.getMessage());
		}

		return EVAL_PAGE;
  }

}
