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

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.log4j.Category;

/**
 * Renders an dbforms style tag
 * @author Joe Peer <joepeer@wap-force.net>
 */

public class TemplateBasedirTag extends TagSupport {

  static Category logCat = Category.getInstance(TemplateBasedirTag.class.getName()); // logging category for this class

	private String baseDir;


  // --------------------- getters and setters ---------------------------------------------------------


  public int doStartTag() throws JspException {
    return SKIP_BODY;
  }


  public int doEndTag() throws JspException {

		try {

			StringBuffer buf = new StringBuffer();
			buf.append(baseDir);
			pageContext.getOut().flush();
      pageContext.getOut().write( buf.toString() );

		} catch(IOException ioe) {
			throw new JspException("Problem including template end - "+ioe.toString());
		}

		return EVAL_PAGE;
  }



  public void setPageContext(final javax.servlet.jsp.PageContext pageContext)  {
	  super.setPageContext(pageContext);
		this.baseDir = (String) pageContext.getRequest().getAttribute("baseDir");
	}

}