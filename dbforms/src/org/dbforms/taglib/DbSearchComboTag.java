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
import javax.servlet.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import javax.servlet.http.*;
import org.dbforms.*;
import org.dbforms.util.*;
import org.apache.log4j.Category;

/**
 * <p>renders a select field for searching with special default search modes.</p>
 * <p>example:</p>
        &lt;input type="hidden" name="searchalgo_0_1" value="weakEnd"/&gt;<br/>
        &lt;input type="hidden" name="searchmode_0_1" value="AND"/&gt;<br/>
        &lt;select name="search_0_1"/&gt;<br/>
        &lt;/select&gt;<br/>
 *
 *  searchalgo and searchmode are set by parameter.
 *
 * @author Henner Kollmann  (Henner.Kollmann@gmx.de)
 */
public class DbSearchComboTag extends DbSearchTag implements DataContainer
{
    static Category logCat = Category.getInstance(DbSearchComboTag.class.getName()); // logging category for this class
    private Vector embeddedData = null;
    private String selectedIndex;
    private String customEntry;
    private String size = "1";

    /**
     * Creates a new DbSearchComboTag object.
     */
    public DbSearchComboTag()
    {
        setSearchAlgo("sharp");
    }

    /**
     * DOCUMENT ME!
     *
     * @param selectedIndex DOCUMENT ME!
     */
    public void setSelectedIndex(String selectedIndex)
    {
        this.selectedIndex = selectedIndex;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSelectedIndex()
    {
        return selectedIndex;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getCustomEntry()
    {
        return customEntry;
    }


    /**
     * DOCUMENT ME!
     *
     * @param customEntry DOCUMENT ME!
     */
    public void setCustomEntry(String customEntry)
    {
        this.customEntry = customEntry;
    }


    /**
    This method is a "hookup" for EmbeddedData - Tags which can assign the lines of data they loaded
    (by querying a database, or by rendering data-subelements, etc. etc.) and make the data
    available to this tag.
    [this method is defined in Interface DataContainer]
    */
    public void setEmbeddedData(Vector embeddedData)
    {
        this.embeddedData = embeddedData;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws javax.servlet.jsp.JspException DOCUMENT ME!
     */
    public int doStartTag() throws javax.servlet.jsp.JspException
    {
        return EVAL_BODY_INCLUDE;
    }


    private String generateSelectHeader(int tableId, int fieldId) throws javax.servlet.jsp.JspException
    {
        // This method have been 
        StringBuffer tagBuf = new StringBuffer();
        tagBuf.append("<select name=\"");
        tagBuf.append("search_");
        tagBuf.append(tableId);
        tagBuf.append("_");
        tagBuf.append(fieldId);
        tagBuf.append("\"");

        if (size != null)
        {
            tagBuf.append(" size=\"");
            tagBuf.append(size);
            tagBuf.append("\"");
        }

        tagBuf.append(prepareStyles());
        tagBuf.append(prepareEventHandlers());
        tagBuf.append(">");

        return tagBuf.toString();
    }


    private String generateTagString(String value, String description, boolean selected)
    {
        StringBuffer tagBuf = new StringBuffer();
        tagBuf.append("<option value=\"");
        tagBuf.append(value);
        tagBuf.append("\"");

        if (selected)
        {
            tagBuf.append(" selected");
        }

        tagBuf.append(">");
        tagBuf.append(description.trim());
        tagBuf.append("</option>");

        return tagBuf.toString();
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
        HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();

        int tableId = parentForm.getTable().getId();
        Field field = parentForm.getTable().getFieldByName(getFieldName());
        int fieldId = field.getId();

        StringBuffer tagBuf = new StringBuffer();
        StringBuffer paramNameBuf = new StringBuffer();
        paramNameBuf.append("search_");
        paramNameBuf.append(tableId);
        paramNameBuf.append("_");
        paramNameBuf.append(fieldId);

        String oldValue = ParseUtil.getParameter(request, paramNameBuf.toString());

        if ((oldValue != null) && (oldValue.trim().length() > 0))
        {
            selectedIndex = oldValue;
        }

        if (embeddedData != null)
        { // no embedded data is nested in this tag

            if ((customEntry != null) && (customEntry.trim().length() > 0))
            {
                String aKey = org.dbforms.util.ParseUtil.getEmbeddedStringWithoutDots(customEntry, 0, ',');
                String aValue = org.dbforms.util.ParseUtil.getEmbeddedStringWithoutDots(customEntry, 1, ',');
                boolean isSelected = false;

                if ((selectedIndex == null) || (selectedIndex.trim().length() == 0))
                {
                    isSelected = "true".equals(org.dbforms.util.ParseUtil.getEmbeddedStringWithoutDots(customEntry, 2, ','));
                }

                tagBuf.append(generateTagString(aKey, aValue, isSelected));
            }

            int embeddedDataSize = embeddedData.size();

            for (int i = 0; i < embeddedDataSize; i++)
            {
                KeyValuePair aKeyValuePair = (KeyValuePair) embeddedData.elementAt(i);
                String aKey = aKeyValuePair.getKey();
                String aValue = aKeyValuePair.getValue();

                // select, if datadriven and data matches with current value OR if explicitly set by user
                boolean isSelected = aKey.equals(selectedIndex);
                tagBuf.append(generateTagString(aKey, aValue, isSelected));
            }
        }

        tagBuf.append("</select>");

        try
        {
            pageContext.getOut().write(RenderHiddenFields(tableId, fieldId));
            pageContext.getOut().write(generateSelectHeader(tableId, fieldId));
            pageContext.getOut().write(tagBuf.toString());
        }
        catch (java.io.IOException ioe)
        {
            throw new JspException("IO Error: " + ioe.getMessage());
        }

        return EVAL_PAGE;
    }

    // ------------------------------------------------------ Protected Methods
}