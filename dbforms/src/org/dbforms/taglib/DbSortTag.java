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

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import org.dbforms.*;
import org.dbforms.util.*;

import org.apache.log4j.Category;

/****
 *
 * this tag renders a dabase-datadriven LABEL, which is apassive element (it can't be changed by
 * the user) - it is predestinated for use with read-only data (i.e. primary keys you don't want
 * the user to change, etc)
 *
 * @author Joachim Peer <j.peer@gmx.net>
 */


public class DbSortTag extends TagSupport  {

  static Category logCat = Category.getInstance(DbSortTag.class.getName()); // logging category for this class

  private DbFormsConfig config;
  private String fieldName;
  private Field field;

	private DbFormTag parentForm;


  public void setFieldName(String fieldName) {
    this.fieldName=fieldName;
		this.field = parentForm.getTable().getFieldByName(fieldName);
   }

  public String getFieldName() {
    return fieldName;
  }

/*
  public int doStartTag() throws javax.servlet.jsp.JspException {
    return SKIP_BODY;
  }*/


  public int doEndTag() throws javax.servlet.jsp.JspException {
		try {

			int tableId = parentForm.getTable().getId();
			int fieldId = field.getId();


			if(!field.isKey() && !field.isFieldSortable())
				logCat.warn("you should declare "+field.getName()+" as key or as sortable in your config file, if you use it as ordering field!");

			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

			StringBuffer paramNameBuf = new StringBuffer("sort_");
			paramNameBuf.append(tableId);
			paramNameBuf.append("_");
			paramNameBuf.append(fieldId);
			String paramName = paramNameBuf.toString();
			String oldValue = ParseUtil.getParameter(request, paramName);

			StringBuffer tagBuf = new StringBuffer();
			tagBuf.append("<select name=\"");
			tagBuf.append(paramName);
			tagBuf.append("\" size=\"0\" onChange=\"javascript:document.dbform.submit()\" >");

			// ---- ascending ----
			tagBuf.append("<option value=\"asc\"");
			if("asc".equals(oldValue)) {
				tagBuf.append(" selected ");
			}
			tagBuf.append(">Ascending");

			// ---- descending ----
			tagBuf.append("<option value=\"desc\"");
			if("desc".equals(oldValue)) {
				tagBuf.append(" selected ");
			}
			tagBuf.append(">Descending");

			// ---- no sorting ----
			tagBuf.append("<option value=\"none\" ");
			if(oldValue==null || "none".equals(oldValue)) {
				tagBuf.append(" selected ");
			}
			tagBuf.append(">None");

			tagBuf.append("</select>");

			pageContext.getOut().write(tagBuf.toString());

		} catch(java.io.IOException ioe) {
      throw new JspException("IO Error: "+ioe.getMessage());
		}
		return EVAL_PAGE;
  }


  public void setPageContext(final javax.servlet.jsp.PageContext pageContext)  {
    super.setPageContext(pageContext);
		this.config = (DbFormsConfig) pageContext.getServletContext().getAttribute(DbFormsConfig.CONFIG);
	}

  public void setParent(final javax.servlet.jsp.tagext.Tag parent) {
    super.setParent(parent);
    this.parentForm = (DbFormTag) findAncestorWithClass(this, DbFormTag.class);
  }


}


