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

/****
 *
 * <p>This tag renders a HTML TextArea - Element</p>
 *
 * this tag renders a dabase-datadriven textArea, which is an active element - the user
 * can change data
 *
 * @author Joachim Peer <j.peer@gmx.net>
 */


public class DbFileTag extends DbBaseInputTag  {

  static Category logCat = Category.getInstance(DbFileTag.class.getName()); // logging category for this class

	private String accept;

	public void setAccept(String accept) {
		this.accept = accept;
	}

	public String getAccept() {
		return accept;
	}

  public int doStartTag() throws javax.servlet.jsp.JspException {

	if( !parentForm.hasMultipartCapability() ) {
	  logCat.warn("DbFileTag is used but DbFormTag.multipart is not set (FALSE)");
	  throw new JspException("DbFileTag is used but DbFormTag.multipart is not set (it is set to \"FALSE\"). you must set it to \"TRUE\" to enable file uploads!");
	}

	return SKIP_BODY;
  }  

  public int doEndTag() throws javax.servlet.jsp.JspException {


		try {

  	    StringBuffer tagBuf = new StringBuffer("<input type=\"file\" name=\"");
	  tagBuf.append(getFormFieldName());
	  tagBuf.append("\" ");

			if (accept != null) {
			    tagBuf.append(" accept=\"");
			    tagBuf.append(accept);
			    tagBuf.append("\"");
			}

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