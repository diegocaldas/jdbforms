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
 *
 * NOTE:
 * Many parts of this class where taken from the Apache Jakarta-Struts Project
 *
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

/*
 * <p>
 *  renders a position to use with keyToDestPos.
 *  This is the same string wich would be used as value in the DbassociatedRadioTag.
 * </p>
 *
 * @author Henner Kollmann, 20021012-HKK
 */
public class DbPosTag extends TagSupport  {
    
  static Category logCat = Category.getInstance(DbSortTag.class.getName()); // logging category for this class

  private DbFormsConfig config;
  private DbFormTag parentForm;

  public int doEndTag() throws javax.servlet.jsp.JspException {
    try {

            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
            int tableId = parentForm.getTable().getId();
            Vector v = parentForm.getTable().getFields();

            StringBuffer tagBuf = new StringBuffer();
	    tagBuf.append(parentForm.getTable().getId());
	    tagBuf.append("_");
	    tagBuf.append(parentForm.getPositionPath());
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