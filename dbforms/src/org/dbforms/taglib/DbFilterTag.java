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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.config.FieldValue;

import org.dbforms.util.MessageResources;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.Util;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
 * custom tag that build up a set of sql filters. Create a set of sql filter
 * conditions, letting user select which one will be applied. A filter tag
 * contains one or more filterCondition tag. Each filterCondition represent a
 * sql condition and is identified by its label. In the body of the
 * filterCondition tag there is the piece of SQL code that we want to insert in
 * the where clause, the character ? act like a placeholder, so a ? in the sql
 * code will be substituted with the some user input. To tell the system what
 * type of user input we want, the last tag is used, the filterValue tag. Each ?
 * found in body will be subsituted by its corresponding filterValue tag. With
 * the "type" attribute of this tag you can select the input more. Selecting
 * "text", a filterValue will render an html input tag, with "select" you'll
 * have an html select, and so on. An example is like this:
 * 
 * <pre>
 *  &lt;db:filter&gt;
 *      &lt;db:filterCondition label=&quot;author name like&quot;&gt;
 *          NAME LIKE '%?%'
 *          &lt;db:filterValue type=&quot;timestamp&quot; useJsCalendar=&quot;true&quot; /&gt;
 *      &lt;/db:filterCondition&gt;
 *      &lt;db:filterCondition label=&quot;ID &gt; V1 AND ID &lt; V2&quot;&gt;
 *          AUTHOR_ID &gt;= ? AND AUTHOR_ID &lt;= ?
 *          &lt;db:filterValue label=&quot;V1&quot; type=&quot;numeric&quot;/&gt;
 *          &lt;db:filterValue label=&quot;V2&quot; type=&quot;numeric&quot;/&gt;
 *      &lt;/db:filterCondition&gt;
 *      &lt;db:filterCondition label=&quot;author = &quot;&gt;
 *          NAME = '?'
 *          &lt;db:filterValue type=&quot;select&quot;&gt;
 *              &lt;db:queryData name=&quot;q1&quot; query=&quot;select distinct name as n1, name as n2 from author where AUTHOR_ID &lt; 100 order by name&quot;/&gt;
 *          &lt;/db:filterValue&gt;
 *      &lt;/db:filterCondition&gt;
 *      &lt;db:filterCondition label=&quot;now is after date&quot;&gt;
 *          CURRENT_DATE &gt; ?
 *          &lt;db:filterValue type=&quot;date&quot; useJsCalendar=&quot;true&quot; /&gt;
 *      &lt;/db:filterCondition&gt;
 *      &lt;db:filterCondition label=&quot;filter without user input&quot;&gt;
 *          AUTHOR_ID &gt; 10
 *      &lt;/db:filterCondition&gt;
 *  &lt;/db:filter&gt;
 * </pre>
 * 
 * This structure will be rendered as a html select element to select the
 * condition the you want to apply. On the onchange event there is a submit, so
 * the page reload with the input elements of the condition that you have
 * selected. After all input elements, there are two buttons, one to apply the
 * condition, one to unset the current applied condition.
 * 
 * <p>
 * Internals:
 * 
 * <dl>
 * <dt> filter_[tableId] </dt>
 * <dd> prefix that all the parameters created by this tag have. </dd>
 * <dt> filter_[tableId]_sel </dt>
 * <dd> index of the currently selected condition </dd>
 * <dt> filter_[tableId]_cond_[condId] </dt>
 * <dd> text of the currently selected condition </dd>
 * <dt> filter_[tableId]_cond_[condId]_value_[valueId] </dt>
 * <dd> current value of the input tag corresponding to the filterValue tag
 * identified by [valueId] </dd>
 * <dt> filter_[tableId]_cond_[condId]_valuetype_[valueId] </dt>
 * <dd> type of the value identified by [valueId] </dd>
 * </dl>
 * </p>
 * 
 * <p>
 * Reading data from request, and update corrispondently the sqlFilter attribute
 * of DbFormTag is done in the static method generateSqlFilter, which produce in
 * output a valid filter string. This method is called in DbFormTag's method
 * doStartTag, setting with it the sqlFilter attribute value. The only other
 * changes needed in DbFormTag's doStartTag is the nullifing of the
 * firstPosition and lastPosition variables that normally contain the current
 * position in the case of applying of a filter (.i.e. when user press the set
 * button, and so the filter_&lt;tableId&gt;_set parameter is found in request).
 * This is needed because here we must force the goto event to move to the first
 * avalilable row.
 * </p>
 * 
 * <p>
 * </p>
 * 
 * @author Sergio Moretti
 * @version $Revision$
 */
public class DbFilterTag extends AbstractDbBaseHandlerTag implements
		javax.servlet.jsp.tagext.TryCatchFinally {
	/** DOCUMENT ME! */
	protected static final String FLT_COND = "_cond_";

	/** DOCUMENT ME! */
	protected static final String FLT_PREFIX = "filter_";

	/** DOCUMENT ME! */
	protected static final String FLT_SEL = "_sel";

	/** DOCUMENT ME! */
	protected static final String FLT_SET = "_set";

	/** DOCUMENT ME! */
	protected static final String FLT_VALUE = "_value_";

	/** DOCUMENT ME! */
	protected static final String FLT_VALUETYPE = "_valuetype_";

	/** DOCUMENT ME! */
	protected static final String FLT_SEARCHALGO = "_searchalgo_";

	private static Log logCat = LogFactory.getLog(DbFilterTag.class.getName());

	/** list of conditions defined for this filter element */
	private ArrayList conds;

	/** used to override the label of the main select's first option element */
	private String disabledCaption;

	/** prefix for this filter of the request's parameters */
	private String filterName;

	/** caption of the SET button */
	private String setCaption;

	/** size attribute for select element */
	private String size;

	/** caption of the UNSET button */
	private String unsetCaption;

	/** CSS stylesheet class to be applied to the SET button */
	private String setStyleClass;

	/** CSS stylesheet class to be applied to the UNSET button */
	private String unsetStyleClass;

	/**
	 * return the currently setted filter condition, reading it from request.
	 * 
	 * @param request
	 * @param tableId
	 * 
	 * @return filter string
	 */
	public static String getSqlFilter(HttpServletRequest request, int tableId) {
		int conditionId = getCurrentCondition(request, tableId);

		if (conditionId > -1) {
			// if there's an active condition, build up it from request
			return DbFilterConditionTag.getSqlFilter(request, tableId,
					conditionId);
		}

		return null;
	}

	/**
	 * return the parametes of the currently setted filter condition, reading it
	 * from request.
	 * 
	 * @param request
	 * @param tableId
	 * 
	 * @return filter string
	 */
	public static FieldValue[] getSqlFilterParams(HttpServletRequest request,
			int tableId) {
		int conditionId = getCurrentCondition(request, tableId);

		if (conditionId > -1) {
			// if there's an active condition, build up it from request
			return DbFilterConditionTag.getSqlFilterParams(request, tableId,
					conditionId);
		}

		return null;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param string
	 */
	public void setDisabledCaption(String string) {
		disabledCaption = string;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param string
	 */
	public void setSetCaption(String string) {
		setCaption = string;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param string
	 */
	public void setSize(String string) {
		size = string;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param string
	 */
	public void setUnsetCaption(String string) {
		unsetCaption = string;
	}

	/**
	 * here we read information from nested tags and we render output to the
	 * page.
	 * 
	 * @see javax.servlet.jsp.tagext.IterationTag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		// retrieve the currently active condition from request
		int currentCondId = getCurrentCondition(
				(HttpServletRequest) pageContext.getRequest(), getTableId());
		DbFilterConditionTag currentCond = null;

		if (currentCondId > -1) {
			currentCond = new DbFilterConditionTag();

			// read the object's state stored in array and apply it in newly
			// created object
			currentCond.setState(pageContext, this,
					(DbFilterConditionTag.State) conds.get(currentCondId));
		}

		StringBuffer buf = render(currentCond);

		try {
			// clear body content.
			// It's meaningless for filter tag and should not be rendered!
			if (bodyContent != null) {
				bodyContent.clearBody();
			}

			JspWriter out = pageContext.getOut();
			out.write(buf.toString());
		} catch (IOException e) {
			throw new JspException(e.getMessage());
		}

		return SKIP_BODY;
	}

	/**
	 * reset tag state
	 * 
	 * @see javax.servlet.jsp.tagext.TryCatchFinally#doFinally()
	 */
	public void doFinally() {
		conds = null;
		disabledCaption = null;
		filterName = null;
		setCaption = null;
		size = null;
		unsetCaption = null;
		super.doFinally();
	}

	/**
	 * initialize environment
	 * 
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		init();

		return EVAL_BODY_INCLUDE;
	}

	/**
	 * filter prefix
	 * 
	 * @param tableId
	 * 
	 * @return
	 */
	protected static String getFilterName(int tableId) {
		return FLT_PREFIX + tableId;
	}

	/**
	 * filter's parameters prefix in request
	 * 
	 * @return
	 */
	protected String getFilterName() {
		return filterName;
	}

	/**
	 * return tableId of the parent dbform tag
	 * 
	 * @return
	 */
	protected int getTableId() {
		return getParentForm().getTable().getId();
	}

	/**
	 * add a condition object to the list. Called by nested DbFilterConditionTag
	 * 
	 * @param condition
	 *            to add
	 * 
	 * @return index of the newly added condition
	 */
	protected int addCondition(DbFilterConditionTag condition) {
		conds.add(condition.getState());

		return conds.size() - 1;
	}

	/**
	 * retrieve the currently active condition from request
	 * 
	 * @param request
	 * @param tableId
	 * 
	 * @return the condition id
	 */
	private static int getCurrentCondition(HttpServletRequest request,
			int tableId) {
		int curCondId = -1;

		// retrieve the current condition from parameters
		String param = ParseUtil.getParameter(request, getFilterName(tableId)
				+ FLT_SEL);

		if (!Util.isNull(param)) {
			// try to transform parameter string in integer
			try {
				curCondId = Integer.parseInt(param);
			} catch (NumberFormatException e) {
				logCat.error("getCurrentCondition", e);
			}
		}

		logCat.debug("setting current filter: " + curCondId);

		return curCondId;
	}

	/**
	 * initialize class fields.
	 */
	private void init() {
		conds = new ArrayList();
		filterName = getFilterName(getTableId());

		if (size == null) {
			size = "1";
		}

		if (disabledCaption == null) {
			disabledCaption = "";
		}

		if (setCaption == null) {
			setCaption = "set";
		}

		if (unsetCaption == null) {
			unsetCaption = "unset";
		}
		if (unsetStyleClass == null) {
			unsetStyleClass = getStyleClass();
		}

		if (setStyleClass == null) {
			setStyleClass = getStyleClass();
		}
	}

	/**
	 * render output
	 * 
	 * @param currentCond
	 * 
	 * @return
	 * 
	 * @throws JspException
	 */
	private StringBuffer render(DbFilterConditionTag currentCond)
			throws JspException {
		StringBuffer buf = new StringBuffer();

		// render main select
		buf.append("\n<select name=\"" + filterName + FLT_SEL + "\" class=\""
				+ getStyleClass() + "\" size=\"" + size
				+ "\" onchange=\"document.dbform.submit()\" >\n");

		int cnt = 0;
		buf
				.append("\t<option value=\"-1\" >" + disabledCaption
						+ "</option>\n");

		// render an option for each nested condition
		DbFilterConditionTag cond = new DbFilterConditionTag();
		Locale locale = MessageResources
				.getLocale((HttpServletRequest) pageContext.getRequest());

		for (Iterator i = conds.iterator(); i.hasNext();) {
			// read DbFilterConditionTag object's state stored in array and
			// apply to cond object
			cond.setState(this.pageContext, this,
					(DbFilterConditionTag.State) i.next());

			// select the currently active condition
			String selected = ((currentCond != null) && currentCond
					.equals(cond)) ? "selected" : "";

			// NAK Added support for localization of option label
			// If the caption is not null and the resources="true" attribute
			String label = cond.getLabel();

			if ((label != null) && getParentForm().hasCaptionResourceSet()) {
				try {
					String message = MessageResources.getMessage(label, locale);

					if (message != null) {
						label = message;
					}
				} catch (Exception e) {
					logCat.debug("setCaption(" + label + ") Exception : "
							+ e.getMessage());
				}
			}

			// render option
			buf.append("\t<option value=\"" + cnt + "\" " + selected + ">"
					+ label + "</option>\n");
			cnt++;
		}

		buf.append("</select>\n");

		if (currentCond != null) {
			// render the current condition
			buf.append(currentCond.render());

			if (!Util.isNull(setCaption)) {
				DbBaseHandlerFactory btn = new DbBaseHandlerFactory(
						this.pageContext, this, DbNavReloadButtonTag.class);
				((DbNavReloadButtonTag) btn.getTag()).setCaption(setCaption);
				((DbNavReloadButtonTag) btn.getTag()).setForceReload("true");
				((DbNavReloadButtonTag) btn.getTag())
						.setStyleClass(setStyleClass);
				buf.append(btn.render());
			}

			if (!Util.isNull(unsetCaption)) {
				DbBaseHandlerFactory btn = new DbBaseHandlerFactory(
						this.pageContext, this, DbNavReloadButtonTag.class);
				((DbNavReloadButtonTag) btn.getTag()).setCaption(unsetCaption);
				((DbNavReloadButtonTag) btn.getTag())
						.setOnClick("document.dbform." + filterName + FLT_SEL
								+ ".selectedIndex = -1;");
				((DbNavReloadButtonTag) btn.getTag()).setForceReload("true");
				((DbNavReloadButtonTag) btn.getTag())
						.setStyleClass(unsetStyleClass);
				buf.append(btn.render());
			}
		}

		return buf;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param string
	 */
	public void setSetStyleClass(String string) {
		setStyleClass = string;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param string
	 */
	public void setUnsetStyleClass(String string) {
		unsetStyleClass = string;
	}
}
