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

/*
 * the idea for this class was taken from the class ErrorsTag-class of the Apache Struts Project
 */

package com.itp.dbforms.taglib;


import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Category;

/**
 * Custom tag that renders error messages if an appropriate request attribute
 * has been created.
 *
 * <p>This idea for this class came from a - much more powerful - class done
 * by Craig R. McClanahan for the Apache struts framework.</p>
 *
 * @author Joe Peer
 */

public class ErrorsTag extends TagSupport {

    static Category logCat = Category.getInstance(ErrorsTag.class.getName()); // logging category for this class

    // ----------------------------------------------------------- Properties


    /**
     * The default locale on our server.
     */
    protected static Locale defaultLocale = Locale.getDefault();


    /**
     * Name of the request scope attribute containing our error messages,
     * if any.
     */
    protected String name = "errors";
    protected String caption = "Error:";

    public String getName() {
			return (this.name);
    }

    public void setName(String name) {
			this.name = name;
    }

    public String getCaption() {
			return (this.caption);
    }

    public void setCaption(String caption) {
			this.caption = caption;
    }


    // ------------------------------------------------------- Public Methods


    /**
     * Render the specified error messages if there are any.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

	     Vector errors = (Vector) pageContext.getAttribute
                (name, PageContext.REQUEST_SCOPE);


			if(errors!=null && errors.size()>0) {

				StringBuffer results = new StringBuffer();
				results.append(caption);

				results.append("<ul>");
				for(int i=0; i<errors.size(); i++) {
					Exception anEx = (Exception) errors.elementAt(i);
				  results.append("<li>");
				  results.append(anEx.toString());
				}
				results.append("</ul>");

				// Print the results to our output writer
				JspWriter writer = pageContext.getOut();
				try {
			    writer.print(results.toString());
				} catch (IOException e) {
				    throw new JspException(e.toString());
				}

			}

			// Continue processing this page
			return (EVAL_BODY_INCLUDE);

    }


    /**
     * Release any acquired resources.
     */
    public void release() {

	super.release();

    }


}
