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
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Category;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.Util;

/**
 * Holds an sql condition that has to be nested inside a DbFilterTag.
 * A condition is specified as sql code in the body of the tag.
 * The character ? is a placeholder for user's input substitution.
 * Every char ? found in sql code is replaced with value evalutated from
 * corresponding filterValue tag nested. So there must be as ? as filterValue tags.
 * 
 * @author Sergio Moretti <s.moretti@nsi-mail.it>
 * 
 * @version $Revision$
 */
public class DbFilterConditionTag extends BodyTagSupport
{
    protected static Category logCat =
        Category.getInstance(DbFilterConditionTag.class.getName());

    protected String filterCondition;
    protected String label;
    protected ArrayList values;
    protected DbFilterTag parentFilter;
    protected int conditionId;

    /**
     * add a value to the value's list. called from nested DbFilterValueTag objs.
     * 
     * @param value
     * @return index of the newly added object
     */
    protected int addValue(DbFilterValueTag value)
    {
        values.add(value);
        return values.size() - 1;
    }

    /**
     * initialize environment and process body only if this condition is the currently selected.
     * 
     * @see javax.servlet.jsp.tagext.Tag#doStartTag()
     */
    public int doStartTag() throws JspException
    {
        init();
        String sel =
            ParseUtil.getParameter(
                (HttpServletRequest) pageContext.getRequest(),
                parentFilter.getFilterName() + DbFilterTag.FLT_SEL);
        // process body only if this condition is active
        if (sel != null)
        {
            int selId = Integer.parseInt(sel);
            if (selId == conditionId)
                return EVAL_BODY_BUFFERED;
        }
        return SKIP_BODY;
    }

    /**
     * @return
     */
    public String getLabel()
    {
        return label;
    }

    /**
     * initialize class's attributes
     *
     */
    protected void init()
    {
        values = new ArrayList();
        parentFilter = (DbFilterTag) getParent();
        conditionId = parentFilter.addCondition(this);
        filterCondition = null;
        if (label == null)
            label = Integer.toString(conditionId);
    }

    /**
     * render output, called from parent DbFilterCondition obj.
     * 
     * @return string containing html code for this obj
     * @throws JspException
     */
    protected StringBuffer render() throws JspException
    {
        StringBuffer buf = new StringBuffer();
        buf.append(
            "<input type=\"hidden\" name=\""
                + parentFilter.getFilterName()
                + DbFilterTag.FLT_COND
                + conditionId
                + "\" value=\""
                + filterCondition
                + "\" />\n");
        for (Iterator i = values.iterator(); i.hasNext();)
        {
            DbFilterValueTag value = (DbFilterValueTag) i.next();
            buf.append(value.render());
        }
        return buf;
    }

    /**
     * @param string
     */
    public void setLabel(String string)
    {
        label = string;
    }

    /**
     * read filterCondition from body
     * 
     * @see javax.servlet.jsp.tagext.IterationTag#doAfterBody()
     */
    public int doAfterBody() throws JspException
    {
        if (this.bodyContent != null)
        {
            filterCondition = bodyContent.getString().trim();
        }
        return SKIP_BODY;
    }

    /**
     * generate condition from request. Called from nested DbFilterTag object
     * 
     * @param request
     * @param tableId
     * @param conditionId
     * @return string containing sql condition code
     */
    protected static String generateFilterCondition(
        HttpServletRequest request,
        int tableId,
        int conditionId)
    {
        // read raw condition from request
        String filterCondition =
            ParseUtil.getParameter(
                request,
                DbFilterTag.getFilterName(tableId)
                    + DbFilterTag.FLT_COND
                    + conditionId);
        if (Util.isNull(filterCondition))
            return null;
        // build up the list of the values of the nested filterValue's parameters 
        ArrayList values =
            DbFilterValueTag.readValuesFromRequest(
                tableId,
                conditionId,
                request);
        // if list is null, then a parse error is occurred, abort 
        if (values == null)
            return null;
        logCat.debug(
            "init parse filterCondition : "
                + filterCondition
                + ", values : "
                + values);
        // TODO search for \? to let use the ? char in condition
        // substitute ? with corresponding value in list 
        int p1 = 0;
        int p2 = filterCondition.indexOf('?', p1);
        StringBuffer buf = new StringBuffer();
        int cnt = 0;
        while (p2 > -1)
        {
            // add the string before the next ?
            buf.append(filterCondition.substring(p1, p2));
            // if values are exausted, then abort
            if (cnt >= values.size())
            {
                logCat.error(
                    "reference to a missing filterValue in " + filterCondition);
                return null;
            }
            // retrieve value
            String value = (String) values.get(cnt);
            if (value == null)
            {
                value = "";
            }
            // add value to string gbuffer
            buf.append(value);
            // restart search from next char after ? 
            p1 = p2 + 1;
            p2 = filterCondition.indexOf('?', p1);
            cnt++;
        }
        // add remaining part of string
        buf.append(filterCondition.substring(p1));
        filterCondition = buf.toString();
        logCat.debug("end parse filterCondition : " + filterCondition);
        return filterCondition;
    }

    /**
     * condition prefix for request parameters
     * 
     * @return
     */
    protected String getConditionName()
    {
        return getConditionName(parentFilter.getTableId(), conditionId);
    }

    /**
     * condition prefix for request parameter
     * 
     * @param tableId
     * @param conditionId
     * @return
     */
    protected static String getConditionName(int tableId, int conditionId)
    {
        return DbFilterTag.getFilterName(tableId)
            + DbFilterTag.FLT_COND
            + conditionId;
    }
}
