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
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.log4j.Category;

/**
 * Renders an dbforms style tag
 * @author Joe Peer <joepeer@wap-force.net>
 */

public class StyleTag extends BodyTagSupport {

    static Category logCat = Category.getInstance(StyleTag.class.getName()); // logging category for this class
	private Hashtable params;


	//private String templateBegin, templateEnd;
	private String templateBase;
	private String templateBaseDir;


  // --------------------- properties ------------------------------------------------------------------

	private String template;
	private String paramList;
	private String part;

  // --------------------- getters and setters ---------------------------------------------------------

	public void setTemplate(String template) {

		this.template = template;
		this.templateBaseDir = templateBase + "/" + template + "/";
		//this.templateBegin = templateBaseDir + template + "_begin.jsp";
		//this.templateEnd =  templateBaseDir + template + "_end.jsp";

	}

	public String getTemplate() {
		return template;
	}

	public void setParamList(String paramList) {
		this.paramList = paramList;
		this.params = parseParams(paramList);
	}

	public String getParamList() {
	  return paramList;
	}


	public void setPart(String part) {
	  this.part = part;
  	}

  	public String getPart() {
	  return part;
    }

  /**
   * this method splits a string of the form
   * "param1 = 'foo1', param2 = foo2, param3=foo3"
   * into a hashtable containing following key/value pairs:
   * { ("param1"/"foo1"), ("param2"/"foo2"), ("param3"/"foo3") }
   *
   * #fixme: primitive algorithm! breaks if params contains komma - signs. fix it
   *
   */
	private Hashtable parseParams(String s) {
		Hashtable result = new Hashtable();

		// break into main (key/value)- tokens
		StringTokenizer st = new StringTokenizer(s, ",");
		while(st.hasMoreTokens()) {
			String token = st.nextToken();  // a key-value pair in its orignal string-shape

			int equalSignIndex = token.indexOf('=');

			// peeling out the key
			String key = token.substring(0,equalSignIndex).trim();

			// peeling out the value (which may or not be embedded in single quotes)
			String value = token.substring(equalSignIndex+1).trim();
			if(value.charAt(0) == '\'' && value.charAt(value.length()-1) == '\'') // get out of any single quotes
			  value = value.substring(1,value.length() - 1);

			result.put(key, value);
		}

		return result;
	}



	/**

	*/
  public int doStartTag() throws JspException {
/*
		try {
			if(params!=null) pageContext.getRequest().setAttribute("styleparams", params);
			pageContext.getRequest().setAttribute("baseDir", templateBaseDir);


      //pageContext.forward(templateBegin);

      HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
      HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

      request.getRequestDispatcher(templateBegin).include(request, response);

      //pageContext.getOut().flush();
		} catch(IOException ioe) {
			throw new JspException("Problem 1including template begin - "+ioe.toString());
		} catch(ServletException se) {}
*/
    return EVAL_BODY_TAG;
  }


  public int doAfterBody() throws JspException {
    return SKIP_BODY; // gets only rendered 1 time
  }


  public int doEndTag() throws JspException {

	try {
		if(bodyContent != null) {
			bodyContent.writeOut(bodyContent.getEnclosingWriter());
		}

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		if(params!=null) request.setAttribute("styleparams", params);
		request.setAttribute("baseDir", templateBaseDir);

     	pageContext.include(templateBaseDir + template+"_"+part+".jsp");

	} catch(IOException ioe) {
	  throw new JspException("Problem 2 including template end - "+ioe.toString());
	} catch(ServletException se) {}

	return EVAL_PAGE;
  }


  public void setPageContext(final javax.servlet.jsp.PageContext pageContext)  {
    super.setPageContext(pageContext);



  	templateBase = pageContext.getServletContext().getInitParameter("templateBase");

		if(templateBase==null)
			templateBase = "templates";

	}

}
