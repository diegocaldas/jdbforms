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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Category;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.Util;

/**
 * custom tag that build up a set of sql filters.
 * Create a set of sql filter conditions, letting user select which one will be applied.
 * A filter tag contains one or more filterCondition tag. Each filterCondition represent a sql condition and is identified by its label.
 * In the body of the filterCondition tag there is the piece of SQL code that we want to insert in the where clause, 
 * the character ? act like a placeholder, so a ? in the sql code will be substituted with the some user input. 
 * To tell the system what type of user input we want, the last tag is used, the filterValue tag. 
 * Each ? found in body will be subsituted by its corresponding filterValue tag. 
 * With the "type" attribute of this tag you can select the input more. 
 * Selecting "text", a filterValue will render an html input tag, with "select" you'll have an html select, and so on.
 * An example is like this:
 * <pre>
 * &lt;db:filter>
 *     &lt;db:filterCondition label="author name like">
 *         NAME LIKE '%?%'
 *         &lt;db:filterValue type="timestamp" useJsCalendar="true" />
 *     &lt;/db:filterCondition>
 *     &lt;db:filterCondition label="ID > V1 AND ID &lt; V2">
 *         AUTHOR_ID >= ? AND AUTHOR_ID &lt;= ?
 *         &lt;db:filterValue label="V1" type="numeric"/>
 *         &lt;db:filterValue label="V2" type="numeric"/>
 *     &lt;/db:filterCondition>
 *     &lt;db:filterCondition label="author = ">
 *         NAME = '?'
 *         &lt;db:filterValue type="select">
 *             &lt;db:queryData name="q1" query="select distinct name as n1, name as n2 from author where AUTHOR_ID &lt; 100 order by name"/>
 *         &lt;/db:filterValue>
 *     &lt;/db:filterCondition>
 *     &lt;db:filterCondition label="now is after date">
 *         CURRENT_DATE > ?
 *         &lt;db:filterValue type="date" useJsCalendar="true" />
 *     &lt;/db:filterCondition>
 *     &lt;db:filterCondition label="filter without user input">
 *         AUTHOR_ID > 10
 *     &lt;/db:filterCondition>
 * &lt;/db:filter>
 * </pre>
 * 
 * This structure will be rendered as a html select element to select the condition the you want to apply. 
 * On the onchange event there is a submit, so the page reload with the input elements of the condition that you have selected. 
 * After all input elements, there are two buttons, one to apply the condition, one to unset the current applied condition. 
 * <p>
 * Internals:
 * <dl>Request parameters:
 * <dt>filter_[tableId] 
 * <dd>prefix that all the parameters created by this tag have.
 * <dt>filter_[tableId]_sel
 * <dd>index of the currently selected condition
 * <dt>filter_[tableId]_cond_[condId]
 * <dd>text of the currently selected condition
 * <dt>filter_[tableId]_cond_[condId]_value_[valueId]
 * <dd>current value of the input tag corresponding to the filterValue tag identified by [valueId]
 * <dt>filter_[tableId]_cond_[condId]_valuetype_[valueId]
 * <dd>type of the value identified by [valueId] 
 * <dt>filter_[tableId]_set
 * <dd>name of the submit button to set/unset the filter. If this parameter is present in the request,
 * I know that a filter set/unset operation is requested. This is needed because I have to nullify the 
 * current position to permit to the goto event to go to the first row (and not to the current row, which maybe will
 * be out the filtered rowset).  
 * this custom tag generate
 * </dl>
 * <p> 
 * Reading data from request, and update corrispondently the sqlFilter attribute of DbFormTag
 * is done in the static method generateSqlFilter, which produce in output a valid filter string.
 * This method is called in DbFormTag's method doStartTag, setting with it the sqlFilter attribute value.
 * The only other changes needed in DbFormTag's doStartTag is the nullifing of the firstPosition and 
 * lastPosition variables that normally contain the current position in the case of applying of a filter 
 * (.i.e. when user press the set button, and so the filter_<tableId>_set parameter is found in request).
 * This is needed because here we must force the goto event to move to the first avalilable row. 
 * <p>   
 * TODO can I extends this tag from TagSupportWithScriptHandler to inherit attributes?
 * TODO I have to find a way to apply filter only when "set" button is pressed (problem with goto to current position that is anymore visible)
 * TODO add internationalization support
 *  
 * @author Sergio Moretti <s.moretti@nsi-mail.it>
 * 
 * @version $Revision$
 */
public class DbFilterTag extends BodyTagSupport
{
    protected static String FLT_COND = "_cond_";
    protected static String FLT_PREFIX = "filter_";
    protected static String FLT_SEL = "_sel";
    protected static String FLT_SET = "_set";
    protected static String FLT_VALUE = "_value_";
    protected static String FLT_VALUETYPE = "_valuetype_";

    static Category logCat = Category.getInstance(DbFilterTag.class.getName());

    /**
     * return the currently setted filter condition, reading it from request.
     * Used in DbFormTag's doStartTag to set the value of the sqlFilter attribute. 
     * 
     * @param request
     * @param tableId
     * @param sqlFilter
     * @return filter string
     */
    public static String generateSqlFilter(
        HttpServletRequest request,
        int tableId,
        String sqlFilter)
    {
        String filter = "";
        String filterCondition = null;
        // read from request the currently active condition
        int conditionId = getCurrentCondition(request, tableId);
        if (conditionId > -1)
        {
            // if there's an active condition, build up it from request
            filterCondition =
                DbFilterConditionTag.generateFilterCondition(
                    request,
                    tableId,
                    conditionId);
        }
        logCat.debug("filter condition from request : " + filterCondition);
        if (sqlFilter != null)
            // sqlFilter value is already defined, we AND it with out condition 
            if (filterCondition != null && filterCondition.length() > 0)
                filter =
                    " ( " + sqlFilter + " ) AND ( " + filterCondition + " ) ";
            else
                filter = sqlFilter;
        else
            filter = filterCondition != null ? filterCondition : "";
        logCat.debug("filter to apply : " + filter);
        return filter;
    }

    /**
     * retrieve the currently active condition from request
     * 
     * @param request
     * @param tableId
     * @return the condition id
     */
    protected static int getCurrentCondition(
        HttpServletRequest request,
        int tableId)
    {
        int curCondId = -1;
        // retrieve the current condition from parameters 
        String param =
            ParseUtil.getParameter(request, getFilterName(tableId) + FLT_SEL);
        if (param != null)
        {
            // try to transform parameter string in integer
            try
            {
                curCondId = Integer.parseInt(param);
            }
            catch (NumberFormatException e)
            {
            }
        }
        logCat.debug("setting current filter: " + curCondId);
        return curCondId;
    }

    /**
     * filter prefix
     * 
     * @param tableId
     * @return
     */
    protected static String getFilterName(int tableId)
    {
        return FLT_PREFIX + tableId;
    }

    /**
     * list of conditions defined for this filter element
     */
    protected ArrayList conds;
    /**
     * prefix for this filter of the request's parameters
     */
    protected String filterName;
    /**
     * reference to parent dbform
     */
    protected DbFormTag parentForm;
    /** 
     * size attribute for select element 
     */
    protected String size;
    /** 
     * class style to apply to select 
     */
    protected String styleClass;
    /**
     * used to override the label of the main select's first option element
     */
    protected String disabledCaption;
    /**
     * caption of the SET button
     */
    protected String setCaption;
    /**
     * caption of the UNSET button
     */
    protected String unsetCaption;

    /**
     * add a condition object to the list. Called by nested DbFilterConditionTag
     * 
     * @param condition to add
     * @return index of the newly added condition
     */
    protected int addCondition(DbFilterConditionTag condition)
    {
        conds.add(condition);
        return conds.size() - 1;
    }

    /**
     * here we read information from nested tags and we render output to the page. 
     * 
     * @see javax.servlet.jsp.tagext.IterationTag#doAfterBody()
     */
    public int doAfterBody() throws JspException
    {
        // retrieve the currently active condition from request
        int currentCondId =
            getCurrentCondition(
                (HttpServletRequest) pageContext.getRequest(),
                getTableId());
        DbFilterConditionTag currentCond = null;
        if (currentCondId > -1)
        {
            currentCond = (DbFilterConditionTag) conds.get(currentCondId);
        }
        try
        {
            JspWriter out = getPreviousOut();
            // render output
            out.println(render(currentCond));
            // TODO must bodyContent be cleared?
            if (bodyContent != null)
                bodyContent.clearBody();
        }
        catch (IOException e)
        {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }

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
     * filter's parameters prefix in request
     * @return
     */
    protected String getFilterName()
    {
        return filterName;
    }

    /**
     * return tableId of the parent dbform tag
     * 
     * @return
     */
    protected int getTableId()
    {
        return parentForm.getTable().getId();
    }

    /**
     * initialize class attributes.
     *
     */
    protected void init()
    {
        conds = new ArrayList();
        parentForm = (DbFormTag) findAncestorWithClass(this, DbFormTag.class);
        filterName = getFilterName(getTableId());
        if (styleClass == null)
            styleClass = "";
        if (size == null)
            size ="1";
        if (disabledCaption == null)
            disabledCaption = "";
        if (setCaption == null)
            setCaption = "set";
        if (unsetCaption == null)
            unsetCaption = "unset";
    }

    /**
     * render output
     * 
     * @param currentCond
     * @return
     * @throws JspException
     */
    protected StringBuffer render(DbFilterConditionTag currentCond)
        throws JspException
    {
        StringBuffer buf = new StringBuffer();
        // render main select
        buf.append(
            "\n<select name=\""
                + filterName
                + FLT_SEL
                + "\" class=\""
                + styleClass
                + "\" size=\"" + size
                + "\" onchange=\"document.dbform.submit()\" >\n");
        int cnt = 0;
        buf.append("\t<option value=\"-1\" >" + disabledCaption + "</option>\n");
        // render an option for each nested condition
        for (Iterator i = conds.iterator(); i.hasNext();)
        {
            DbFilterConditionTag cond = (DbFilterConditionTag) i.next();
            // select the currently active condition 
            String selected = currentCond == cond ? "selected" : "";
            // render option
            buf.append(
                "\t<option value=\""
                    + cnt
                    + "\" "
                    + selected
                    + ">"
                    + cond.getLabel()
                    + "</option>\n");
            cnt++;
        }
        buf.append("</select>\n");
        if (currentCond != null)
        {
            // render the current condition
            buf.append(currentCond.render());
            // render the buttons to set the filter
            buf.append(
                "<input type=\"submit\" value=\"" + setCaption + "\" name=\""
                    + filterName
                    + FLT_SET
                    + "\" />\n");
            buf.append(
                "<input type=\"submit\" value=\"" + unsetCaption + "\" name=\""
                    + filterName
                    + FLT_SET
                    + "\" onclick=\"document.dbform."
                    + filterName
                    + FLT_SEL
                    + ".selectedIndex = -1;\" />\n");
        }
        return buf;
    }
    
    /**
     * @param string
     */
    public void setSize(String string)
    {
        size = string;
    }

    /**
     * @param string
     */
    public void setStyleClass(String string)
    {
        styleClass = string;
    }
    
    /**
     * @param string
     */
    public void setDisabledCaption(String string)
    {
        disabledCaption = string;
    }

    /**
     * @param string
     */
    public void setSetCaption(String string)
    {
        setCaption = string;
    }

    /**
     * @param string
     */
    public void setUnsetCaption(String string)
    {
        unsetCaption = string;
    }

}
