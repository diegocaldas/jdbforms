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

package org.dbforms.taglib;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.log4j.Category;



/**
 * Abstract base class for the various input tags.
 *
 * original author Craig R. McClanahan
 * original author Don Clasen,
 * @author Joachim Peer (modified this class for DbForms-Project)
 */
public abstract class DbBaseInputTag extends DbBaseHandlerTag
{
    static Category logCat = Category.getInstance(DbBaseInputTag.class.getName());

    // logging category for this class
    // ----------------------------------------------------- Instance Variables

    /**
     * The number of character columns for this field, or negative
     * for no limit.
     */
    protected String cols = null;

    /**
     * The maximum number of characters allowed, or negative for no limit.
     */
    protected String maxlength = null;

    /**
     * The number of rows for this field, or negative for no limit.
     */
    protected String rows = null;

    // ------------------------------------------------------------- Properties

    /**
     * Return the number of columns for this field.
     */
    public String getCols()
    {
        return (this.cols);
    }


    /**
     * Set the number of columns for this field.
     *
     * @param cols The new number of columns
     */
    public void setCols(String cols)
    {
        this.cols = cols;
    }


    /**
     * Return the maximum length allowed.
     */
    public String getMaxlength()
    {
        return (this.maxlength);
    }


    /**
     * Set the maximum length allowed.
     *
     * @param maxlength The new maximum length
     */
    public void setMaxlength(String maxlength)
    {
        this.maxlength = maxlength;
    }


    /**
     * Return the number of rows for this field.
     */
    public String getRows()
    {
        return (this.rows);
    }


    /**
     * Set the number of rows for this field.
     *
     * @param rows The new number of rows
     */
    public void setRows(String rows)
    {
        this.rows = rows;
    }


    /**
     * Return the size of this field (synonym for <code>getCols()</code>).
     */
    public String getSize()
    {
        return (getCols());
    }


    /**
     * Set the size of this field (synonym for <code>setCols()</code>).
     *
     * @param size The new size
     */
    public void setSize(String size)
    {
        setCols(size);
    }


    // --------------------------------------------------------- Public Methods

    /**
     * Process the start of this tag.  The default implementation does nothing.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException
    {
        if (getReadOnly().equals("true") || parentForm.getReadOnly().equals("true"))
        {
            String onFocus = (getOnFocus() != null) ? getOnFocus() : "";

            if (onFocus.lastIndexOf(";") != (onFocus.length() - 1))
            {
                onFocus += ";"; // be sure javascript end with ";"
            }

            setOnFocus(onFocus + "this.blur();");
        }

        return EVAL_BODY_TAG;
    }


    /**
     * Process the end of this tag.  The default implementation does nothing.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException
    {
        return EVAL_PAGE;
    }
}