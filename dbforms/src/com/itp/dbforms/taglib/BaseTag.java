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
 *
 *
 * NOTE:
 * substantial parts of this class where taken from the class BaseTag of the
 * Apache Struts Project
 */


package com.itp.dbforms.taglib;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.log4j.Category;

/**
 * Renders an HTML <base> element with an href
 * attribute pointing to the absolute location of the enclosing JSP page. The presence
 * of this tag allows the browser to resolve relative URL's to images,
 * CSS stylesheets  and other resources in a manner independent of the URL
 * used to call the ControllerServlet.  There are no attributes associated with
 * this tag.
 *
 * author Luis Arias <luis@elysia.com>
 * @author Joe Peer <joepeer@wap-force.net> (changed class for use in DbForms-Framework)
 */

public class BaseTag extends TagSupport {

  static Category logCat = Category.getInstance(BaseTag.class.getName()); // logging category for this class

  /**
   * Process the start of this tag.
   *
   * @exception JspException if a JSP exception has occurred
   */
  public int doStartTag() throws JspException {
    HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
    StringBuffer buf = new StringBuffer("<base href=\"");
    buf.append(request.getScheme());
    buf.append("://");
    buf.append(request.getServerName());

    int port = request.getServerPort();
    if(port!=80) {
	    buf.append(":");
	    buf.append(port);
  	}
    buf.append(request.getRequestURI());
    buf.append("\">");
    JspWriter out = pageContext.getOut();
    try {
        out.write(buf.toString());
    }
    catch (IOException e) {
        throw new JspException(e.toString());
    }
    return EVAL_BODY_INCLUDE;
  }
}
