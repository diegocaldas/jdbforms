/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * DbForms - a Rapid Application Development Framework
 * Copyright (C) 2001 Joachim Peer <joepeer@excite.com>
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

package com.itp.dbforms.taglib;

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

public class TemplateParamTag extends TagSupport {

    static Category logCat = Category.getInstance(TemplateParamTag.class.getName()); // logging category for this class

	private String name, defaultValue, dir; // properties set by JSP container

	private String baseDir;
	private Hashtable sp;

  // --------------------- getters and setters ---------------------------------------------------------

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDefaultValue() {
	  return defaultValue;
	}

	public void setDir(String dir) {
		if(dir.equals("."))
			this.dir = "";
		else
			this.dir = dir;
	}

	public String getDir() {
	  return dir;
	}



  public int doStartTag() throws JspException {
    return SKIP_BODY;
  }


  public int doEndTag() throws JspException {

		try {

			StringBuffer buf = new StringBuffer();

			// determinate dir
			if(dir!=null) {
		    buf.append(baseDir);
				buf.append(dir);
				if(dir.length()>0) buf.append("/");
			}



			// determinate param value
		  if(sp==null) {
				if(defaultValue != null) buf.append(defaultValue);

			} else {
		  	String paramValue = (String) sp.get(name);
		  	if(paramValue != null) {
					buf.append(paramValue);
				} else {
					if(defaultValue != null) buf.append(defaultValue);
				}
			}

			pageContext.getOut().flush();
      pageContext.getOut().write( buf.toString() );

		} catch(IOException ioe) {
			throw new JspException("Problem including template end - "+ioe.toString());
		}

		return EVAL_PAGE;
  }



  public void setPageContext(final javax.servlet.jsp.PageContext pageContext)  {
	  super.setPageContext(pageContext);
		this.sp = (java.util.Hashtable) pageContext.getRequest().getAttribute("styleparams");
		this.baseDir = (String) pageContext.getRequest().getAttribute("baseDir");
	}

}