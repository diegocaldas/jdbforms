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


public class DbLabelTag extends TagSupport  {

  static Category logCat = Category.getInstance(DbLabelTag.class.getName()); // logging category for this class

  protected static final String NO_DATA = "[No Data]";

  protected DbFormsConfig config;
  protected String fieldName;
  protected Field field;

	protected DbFormTag parentForm;


  public void setFieldName(String fieldName) {
	this.fieldName=fieldName;
	this.field = parentForm.getTable().getFieldByName(fieldName);
  }

  public String getFieldName() {
	return fieldName;
  }


  public int doEndTag() throws javax.servlet.jsp.JspException {
	try {

		String fieldValue = NO_DATA;
		if(!ResultSetVector.isEmptyOrNull(parentForm.getResultSetVector())) {
			String[] currentRow = parentForm.getResultSetVector().getCurrentRow();
			fieldValue = currentRow[field.getId()];
		}
		pageContext.getOut().write(fieldValue);

	} 	catch(java.io.IOException ioe) {
		throw new JspException("IO Error: "+ioe.getMessage());
	}	catch(Exception e) {
		throw new JspException("Error: "+e.getMessage());
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