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

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Category;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.DbFormsConfigRegistry;
import org.dbforms.util.KeyValuePair;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.Util;

/**
 * Map a placeholder (?) in sql code to an input tag. 
 * Used as nested tag inside filterCondition.
 * Implements DataContainer interface to use the nested tags queryData, staticData ...
 * 
 * @author Sergio Moretti <s.moretti@nsi-mail.it>
 * 
 * @version $Revision$
 */
public class DbFilterValueTag extends BodyTagSupport implements DataContainer
{
    // types value write in request's parameter ..._valuetype_<valueId> 
    protected static String FLT_VALUETYPE_DATE = "date";
    protected static String FLT_VALUETYPE_NUMERIC = "numeric";
    protected static String FLT_VALUETYPE_TEXT = "text";
    protected static String FLT_VALUETYPE_TIMESTAMP = "timestamp";
    // this is not a value type, but it tells that this value object is mapped to a select element
    protected static String FLT_VALUETYPE_SELECT = "select";

    static Category logCat =
        Category.getInstance(DbFilterValueTag.class.getName());

    /**
     * retrieve the format object to use to convert date or timestamp to string
     * @param type attribute of DbFilterValue object
     * @return format object
     */
    protected static DateFormat getDateFormat(String type)
    {
        DateFormat format = null;
        if (FLT_VALUETYPE_DATE.equalsIgnoreCase(type))
        {
            // get date format from config
            DbFormsConfig config;
            try
            {
                config = DbFormsConfigRegistry.instance().lookup();
                format = config.getDateFormatter();
            }
            catch (Exception e)
            {
                logCat.error(e);
            }
        }
        else if (FLT_VALUETYPE_TIMESTAMP.equalsIgnoreCase(type))
        {
            // TODO better handling of  timestamp format
            format = new SimpleDateFormat();
        }
        return format;
    }

    /**
     * check that value is conform to its type, if this is not true, return null.
     * For date and timestamp, transform input parsed with appropriate format object to a standard date or timestap string:
     * DATE'yyyy-MM-dd'
     * TIMESTAMP'yyyy-MM-dd HH:mm:ss.S'
     *   
     * @param value from user input
     * @param type of value object
     * @return string containing value, maybe rewrited to a more suitable form
     */
    protected static String parseValue(String value, String type)
    {
        String retval = value;
        // type is null or type is text, do nothing
        if (type == null || FLT_VALUETYPE_TEXT.equalsIgnoreCase(type))
        {
        }
        // if type is numeric, check if value is a valid number
        else if (FLT_VALUETYPE_NUMERIC.equalsIgnoreCase(type))
        {
            BigDecimal valueNum = null;
            try
            {
                valueNum = new BigDecimal(value);
            }
            catch (NumberFormatException e)
            {
                retval = null;
            }
        }
        // if type is date or timestamp, transform user input in a sql temporal literal
        else if (
            FLT_VALUETYPE_DATE.equalsIgnoreCase(type)
                || FLT_VALUETYPE_TIMESTAMP.equalsIgnoreCase(type))
        {
            // retrieve date or timestamp format object
            DateFormat format = getDateFormat(type);
            try
            {
                if (FLT_VALUETYPE_DATE.equalsIgnoreCase(type))
                {
                    Date date = new Date(format.parse(value).getTime());
                    // use the java.sql.Date toString method to convert a date in the 
                    // standard format 'yyyy-MM-dd'
                    retval = "DATE'" + date.toString() + "'";
                }
                else
                {
                    Timestamp tstamp =
                        new Timestamp(format.parse(value).getTime());
                    // use the java.sql.Timestamp toString method to convert a timestamp
                    // in the standard format 'yyyy-MM-dd HH:mm:ss.S' 
                    retval = "TIMESTAMP'" + tstamp.toString() + "'";
                }
            }
            catch (ParseException e)
            {
                logCat.warn(e);
                retval = null;
            }
            catch (Exception e)
            {
                logCat.error(e);
                retval = null;
            }
        }
        else
            // type unknown;
            retval = null;
        return retval;
    }

    /**
     * read from request all values associated to the condition identified with <tableId>, <conditionId>.
     * It try to read the value with identifier 0, if succeded go on with identifier 1, and so on.
     *    
     * @param tableId identify filter in request's parameters
     * @param conditionId identify condition in request's parameter
     * @param request
     * @return list of all values readed from request
     */
    protected static ArrayList readValuesFromRequest(
        int tableId,
        int conditionId,
        HttpServletRequest request)
    {
        ArrayList values = new ArrayList();
        for (int valueId = 0; true; ++valueId)
        {
            // read from parameter's request the value and the type having this id
            String paramValue =
                DbFilterConditionTag.getConditionName(tableId, conditionId)
                    + DbFilterTag.FLT_VALUE
                    + valueId;
            String paramType =
                DbFilterConditionTag.getConditionName(tableId, conditionId)
                    + DbFilterTag.FLT_VALUETYPE
                    + valueId;
            String value = ParseUtil.getParameter(request, paramValue);
            String valueType = ParseUtil.getParameter(request, paramType);
            if (value != null)
            {
                // check if value is a valid instance of type
                String realvalue = parseValue(value, valueType);
                if (realvalue == null)
                {
                    // error in parsing value, abort
                    logCat.debug(
                        "error in parsing filter type : "
                            + valueType
                            + ", value : "
                            + value);
                    return null;
                }
                // add value, possibly converted, to list
                values.add(realvalue);
            }
            else
                // didn't find any parameter with this id, so we have finished
                break;
        }
        return values;
    }

    /**
     * Allows an additional (independant) entry into the select list
     */
    protected String customEntry;
    /**
     * contains list of elements to show as options when type is select, (DataContainer interface)
     */
    protected Vector embeddedData;
    /** 
     * Holds value of property jsCalendarDateFormat. 
     */
    protected String jsCalendarDateFormat;
    /**
     * label showed before input tag
     */
    protected String label;
    /**
     * parent DbCFilterConditionTag
     */
    protected DbFilterConditionTag parentCondition;
    /**
     * currently selected index, valid only when type = select
     */
    protected String selectedIndex;
    /**
     * html input's attribute size
     */
    protected String size;
    /**
     * css class to be applied to input element
     */
    protected String styleClass;
    /**
     * type of input
     */
    protected String type;
    /** 
     * Holds value of property useJsCalendar. 
     */
    protected String useJsCalendar;
    /**
     * current value, readed from request 
     */
    protected String value;
    /**
     * identifier of this value object
     */
    protected int valueId;

    /** 
     * initialize  environment
     * 
     * @see javax.servlet.jsp.tagext.Tag#doStartTag()
     */
    public int doStartTag() throws JspException
    {
        init();
        return EVAL_BODY_BUFFERED;
    }

    /**
     * generate option element for select. borrowed from @see DbSearchComboTag#generateTagString
     * 
     * @param value
     * @param description 
     * @param selected
     * @return string containing an html option element
     */
    protected String generateTagString(
        String value,
        String description,
        boolean selected)
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
     * @return
     */
    public String getJsCalendarDateFormat()
    {
        return jsCalendarDateFormat;
    }

    /**
     * @return
     */
    public String getLabel()
    {
        return label;
    }

    /**
     * @return
     */
    public String getUseJsCalendar()
    {
        return useJsCalendar;
    }

    protected String getValue()
    {
        return value;
    }

    protected String getValueName()
    {
        return parentCondition.getConditionName()
            + DbFilterTag.FLT_VALUE
            + valueId;
    }

    protected String getValueType()
    {
        return parentCondition.getConditionName()
            + DbFilterTag.FLT_VALUETYPE
            + valueId;
    }

    protected void init()
    {
        if (type == null)
            type = "text";
        if (styleClass == null)
            styleClass = "";
        parentCondition = (DbFilterConditionTag) getParent();
        valueId = parentCondition.addValue(this);
        value =
            ParseUtil.getParameter(
                (HttpServletRequest) pageContext.getRequest(),
                getValueName());
        if (value == null || parseValue(value, type) == null)
            value = "";
        // the type attribute can be read either from request, with FLT_VALUETYPE, or directly from page
        embeddedData = null;
    }

    protected StringBuffer render()
        throws JspException
    {
        StringBuffer buf = new StringBuffer();
        if (label != null)
            buf.append("<b>" + label + "</b>\n");
        // TODO better handling of timestamp
        if (type.equalsIgnoreCase(FLT_VALUETYPE_TEXT)
            || type.equalsIgnoreCase(FLT_VALUETYPE_NUMERIC))
        {
            renderTextElement(buf);
        }
        else if (
            type.equalsIgnoreCase(FLT_VALUETYPE_DATE)
                || type.equalsIgnoreCase(FLT_VALUETYPE_TIMESTAMP))
        {
            renderDateElement(buf);
        }
        else if (FLT_VALUETYPE_SELECT.equalsIgnoreCase(type) && embeddedData != null)
        {
            renderSelectElement(buf);
        }
        else
            throw new JspException("type not correct");
        return buf;
    }

    protected void renderDateElement(StringBuffer buf)
    {
        renderTextElement(buf);
        // if property useJSCalendar is set to 'true' we will now add a little
        // image that can be clicked to popup a small JavaScript Calendar
        // written by Robert W. Husted to edit the field:
        if ("true".equals(useJsCalendar))
        {
            buf.append(" <a href=\"javascript:doNothing()\" ").append(
                " onclick=\"");

            if (jsCalendarDateFormat == null)
            {
                // get date format from config
                SimpleDateFormat format =
                    (SimpleDateFormat) getDateFormat(type);
                if (format != null)
                    jsCalendarDateFormat = format.toPattern();
            }
            if (jsCalendarDateFormat != null) // JS Date Format set ?
            {
                buf.append("calDateFormat='" + jsCalendarDateFormat + "';");
            }

            buf
                .append("setDateField(document.dbform['")
                .append(getValueName())
                .append("']);")
                .append(" top.newWin = window.open('")
                .append(
                    ((HttpServletRequest) pageContext.getRequest())
                        .getContextPath())
                .append("/dbformslib/jscal/calendar.html','cal','WIDTH=270,HEIGHT=280')\">")
                .append("<IMG SRC=\"")
                .append(
                    ((HttpServletRequest) pageContext.getRequest())
                        .getContextPath())
                .append("/dbformslib/jscal/calendar.gif\" WIDTH=\"32\" HEIGHT=\"32\" ")
                .append(" BORDER=0  alt=\"Click on the Calendar to activate the Pop-Up Calendar Window.\">")
                .append("</a>");
        }
    }

    protected void renderSelectElement(StringBuffer buf)
    {
        String sizestr = "";
        if (size != null)
            sizestr = "size=\"" + size + "\" ";
        buf.append(
            "<select name=\""
                + getValueName()
                + "\" "
                + sizestr
                + " class=\""
                + styleClass
                + "\">\n");

        if ((customEntry != null) && (customEntry.trim().length() > 0))
        {
            String aKey =
                org.dbforms.util.ParseUtil.getEmbeddedStringWithoutDots(
                    customEntry,
                    0,
                    ',');
            String aValue =
                org.dbforms.util.ParseUtil.getEmbeddedStringWithoutDots(
                    customEntry,
                    1,
                    ',');
            boolean isSelected = false;

            if ((selectedIndex == null)
                || (selectedIndex.trim().length() == 0))
            {
                isSelected =
                    "true".equals(
                        org
                            .dbforms
                            .util
                            .ParseUtil
                            .getEmbeddedStringWithoutDots(
                            customEntry,
                            2,
                            ','));
            }
            buf.append(generateTagString(aKey, aValue, isSelected));
        }

        int embeddedDataSize = embeddedData.size();
        for (int i = 0; i < embeddedDataSize; i++)
        {
            KeyValuePair aKeyValuePair =
                (KeyValuePair) embeddedData.elementAt(i);
            String aKey = aKeyValuePair.getKey();
            String aValue = aKeyValuePair.getValue();

            // select, if datadriven and data matches with current value OR if explicitly set by user
            boolean isSelected = aKey.equals(value);
            buf.append(generateTagString(aKey, aValue, isSelected));
        }
        buf.append("</select>\n");
    }

    protected void renderTextElement(StringBuffer buf)
    {
        String sizestr = "";
        if (size != null)
            sizestr = "size=\"" + size + "\" ";
        buf.append(
            "<input type=\"text\" name=\""
                + getValueName()
                + "\" value=\""
                + value
                + "\""
                + sizestr
                + " class=\""
                + styleClass
                + "\"/>\n");
        buf.append(
            "<input type=\"hidden\" name=\""
                + getValueType()
                + "\" value=\""
                + type.toLowerCase()
                + "\"/>\n");
    }

    /**
     * Allows an additional (independant) entry into the select list
     * 
     * @param string
     */
    public void setCustomEntry(String string)
    {
        customEntry = string;
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
     * property jsCalendarDateFormat.
     *  
     * @param string
     */
    public void setJsCalendarDateFormat(String string)
    {
        jsCalendarDateFormat = string;
    }

    /**
     * property label showed before input tag
     * 
     * @param string
     */
    public void setLabel(String string)
    {
        label = string;
    }

    /**
     * property currently selected index, valid only when type = select
     * 
     * @param string
     */
    public void setSelectedIndex(String string)
    {
        selectedIndex = string;
    }

    /**
     * property html input's attribute size
     * 
     * @param string
     */
    public void setSize(String string)
    {
        size = string;
    }

    /**
     * type of the input element that will be rendered, possible values are:
     * <dl>
     * <dt>text
     * <dd>text input
     * <dt>date
     * <dd>input text for date type, a validation of the value will be done, and it supports the jscal object
     * <dt>timestamp
     * <dd>input text for timestamp type, a validation of the value will be done, and it supports the jscal object (it doesn't fit very well, anyway ...)
     * <dt>numeric
     * <dd>input text for number, a validation of the value will be done
     * <dt>select
     * <dd>render an html select element, filled with nested tags like queryData, staticData and so on.
     * </dl>
     * 
     * @param string
     */
    public void setType(String string)
    {
        type = string;
    }

    /**
     * @param string
     */
    public void setUseJsCalendar(String string)
    {
        useJsCalendar = string;
    }

    /**
     * css class to be applied to input element
     * 
     * @param string
     */
    public void setStyleClass(String string)
    {
        styleClass = string;
    }
}
