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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 */

/*
 * the idea for this class was taken from the class ErrorsTag-class of the Apache Struts Project
 */
package org.dbforms.taglib;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.log4j.Category;
import java.util.*;



/**
 * Custom tag that renders error messages if an appropriate request attribute
 * has been created.
 *
 * <p>This idea for this class came from a - much more powerful - class done
 * by Craig R. McClanahan for the Apache struts framework.</p>
 *
 * @author Joe Peer
 */
public class ErrorsTag extends TagSupport
{
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

    /** DOCUMENT ME! */
    protected String caption = "Error:";

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName()
    {
        return (this.name);
    }


    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getCaption()
    {
        return (this.caption);
    }


    /**
     * DOCUMENT ME!
     *
     * @param caption DOCUMENT ME!
     */
    public void setCaption(String caption)
    {
        this.caption = caption;
    }


    // ------------------------------------------------------- Public Methods

    /**
     * Render the specified error messages if there are any.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException
    {
        Vector originalErrors = (Vector) pageContext.getAttribute(name, PageContext.REQUEST_SCOPE);

        if ((originalErrors != null) && (originalErrors.size() > 0))
        {
            // Extract out only user defined text
            Vector errors = this.extractUserDefinedErrors(originalErrors);

            StringBuffer results = new StringBuffer();
            results.append(caption);

            results.append("<ul>");

            for (int i = 0; i < errors.size(); i++)
            {
                results.append("<li>");
                results.append(errors.elementAt(i));
            }

            results.append("</ul>");

            // Print the results to our output writer
            JspWriter writer = pageContext.getOut();

            try
            {
                writer.print(results.toString());
            }
            catch (IOException e)
            {
                throw new JspException(e.toString());
            }
        }

        // Continue processing this page
        return EVAL_BODY_INCLUDE;
    }

    /** DOCUMENT ME! */
    public java.lang.String messagePrefix;

    /**
     * philip.grunikiewicz@hydro.qc.ca
     * 2001-05-10
     *
     * Iterate through the errors and extract only user-specified text
     */
    public Vector extractUserDefinedErrors(Vector errors)
    {
        Vector newErrors = new Vector();
        String message = null;
        int index = 0;

        //Get user defined delimiter from messagePrefix attribute
        String delimiter = this.getMessagePrefix();

        if (errors != null)
        {
            Enumeration enum = errors.elements();

            while (enum.hasMoreElements())
            {
                message = ((Exception) enum.nextElement()).getMessage();

                //Check for delimiter
                if ((delimiter != null) && ((index = message.indexOf(delimiter)) != -1))
                {
                    // Add only what is needed
                    message = message.substring(index + delimiter.length());
                }

                newErrors.add(message);
            }
        }

        return newErrors;
    }


    /**
     * Insert the method's description here.
     * Creation date: (2001-05-10 12:57:08)
     * @return java.lang.String
     */
    public java.lang.String getMessagePrefix()
    {
        return messagePrefix;
    }


    /**
     * Insert the method's description here.
     * Creation date: (2001-05-10 12:57:08)
     * @param newMessagePrefix java.lang.String
     */
    public void setMessagePrefix(java.lang.String newMessagePrefix)
    {
        messagePrefix = newMessagePrefix;
    }
}