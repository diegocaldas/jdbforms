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
import java.util.*;
import java.sql.*;
import java.io.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import org.dbforms.*;
import org.dbforms.util.*;
import org.dbforms.validation.ValidatorConstants;
import org.apache.log4j.Category;



/****
 *
 * <p>this tag renders an "new"-button.
 *
 *
 * @author Joachim Peer <j.peer@gmx.net>
 */
public class DbNavNewButtonTag extends DbBaseButtonTag
{
    static Category logCat = Category.getInstance(DbNavNewButtonTag.class.getName()); // logging category for this class
    private String destTable;

    /** Holds value of property showAlwaysInFooter. */
    private String showAlwaysInFooter = "true";
    
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws javax.servlet.jsp.JspException DOCUMENT ME!
     * @throws JspException DOCUMENT ME!
     */
    public int doStartTag() throws javax.servlet.jsp.JspException
    {
        // ValidatorConstants.JS_CANCEL_SUBMIT is the javascript variable boolean to verify 
        // if we do the javascript validation before submit <FORM>
        if ((parentForm.getFormValidatorName() != null) && (parentForm.getFormValidatorName().length() > 0) && parentForm.getJavascriptValidation().equals("true"))
        {
            String onclick = (getOnClick() != null) ? getOnClick() : "";

            if (onclick.lastIndexOf(";") != (onclick.length() - 1))
            {
                onclick += ";"; // be sure javascript end with ";"
            }

            setOnClick(onclick + ValidatorConstants.JS_CANCEL_VALIDATION + "=false;");
        }

        if (parentForm.getFooterReached() && Util.isNull(parentForm.getResultSetVector()) && "false".equalsIgnoreCase(showAlwaysInFooter))
        {
			// 20030521 HKK: Bug fixing, thanks to Michael Slack! 
			return SKIP_BODY;
        }

        try
        {
            StringBuffer tagBuf = new StringBuffer();

            // Allow navNewButton to specify a table for insertion other than the
            // parent table. (Contrib. John Madsen)		  
            int tableId = ((destTable != null) && (destTable.length() != 0)) ? config.getTableByName(destTable).getId() : table.getId();

            String tagName = "ac_new_" + tableId;

            if (followUp != null)
            {
                tagBuf.append(getDataTag(tagName, "fu", followUp));
            }

            if (followUpOnError != null)
            {
                tagBuf.append(getDataTag(tagName, "fue", followUpOnError));
            }

            tagBuf.append(getButtonBegin());
            tagBuf.append(" name=\"");
            tagBuf.append(tagName);
            tagBuf.append("\">");

            pageContext.getOut().write(tagBuf.toString());
        }
        catch (java.io.IOException ioe)
        {
            throw new JspException("IO Error: " + ioe.getMessage());
        }

        if (choosenFlavor == FLAVOR_MODERN)
        {
            return EVAL_BODY_TAG;
        }
        else
        {
            return SKIP_BODY;
        }
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws javax.servlet.jsp.JspException DOCUMENT ME!
     * @throws JspException DOCUMENT ME!
     */
    public int doEndTag() throws javax.servlet.jsp.JspException
    {
        if (choosenFlavor == FLAVOR_MODERN)
        {
            try
            {
                if (bodyContent != null)
                {
                    bodyContent.writeOut(bodyContent.getEnclosingWriter());
                }

                pageContext.getOut().write("</button>");
            }
            catch (java.io.IOException ioe)
            {
                throw new JspException("IO Error: " + ioe.getMessage());
            }
        }

        return EVAL_PAGE;
    }


    /**
     * Gets the destTable
     * @return Returns a String
     */
    public String getDestTable()
    {
        return destTable;
    }


    /**
     * Sets the destTable
     * @param destTable The destTable to set
     */
    public void setDestTable(String destTable)
    {
        this.destTable = destTable;
    }
    
    /** Getter for property showAlwaysInFooter.
     * @return Value of property showAlwaysInFooter.
     *
     */
    public String getShowAlwaysInFooter() {
        return this.showAlwaysInFooter;
    }
    
    /** Setter for property showAlwaysInFooter.
     * @param showAlwaysInFooter New value of property showAlwaysInFooter.
     *
     */
    public void setShowAlwaysInFooter(String showAlwaysInFooter) {
        this.showAlwaysInFooter = showAlwaysInFooter;
    }
    
}