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
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.apache.log4j.Category;
import org.dbforms.config.Field;
import org.dbforms.config.Table;
import org.dbforms.config.FieldValue;
import org.dbforms.config.FieldValues;
import org.dbforms.config.Constants;
import org.dbforms.config.DbFormsConfigRegistry;
import org.dbforms.util.KeyValuePair;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.Util;
import org.dbforms.util.MessageResources;

/**
 * Map a placeholder (?) in sql code to an input tag. 
 * Used as nested tag inside filterCondition.
 * Implements DataContainer interface to use the nested tags queryData, staticData ...
 * 
 * @author Sergio Moretti <s.moretti@nsi-mail.it>
 * 
 * @version $Revision$
 */
public class DbFilterValueTag
	extends DbBaseHandlerTag
	implements DataContainer, javax.servlet.jsp.tagext.TryCatchFinally {
	/**
	 * tag's state holder.
	 * Used a separate class to hold tag's state to workaround to Tag pooling, in which
	 * an tag object is reused, but we have the need to store informations about all 
	 * child tags in the parent, so we store the state, and apply it to a dummy tag when needed.
	 *  
	 * @author Sergio Moretti
	 */
	protected class State {
		/**
		 * Allows an additional (independant) entry into the select list
		 */
		protected String customEntry = null;

		/**
		 * contains list of elements to show as options when type is select, (DataContainer interface)
		 */
		protected List embeddedData = null;

		/** 
		 * Holds value of property jsCalendarDateFormat. 
		 */
		protected String jsCalendarDateFormat = null;

		/**
		 * label showed before input tag
		 */
		protected String label = null;

		/**
		 * currently selected index, valid only when type = select
		 */
		protected String selectedIndex = null;

		/**
		 * html input's attribute size
		 */
		protected String size = null;

		/**
		 * css class to be applied to input element
		 */
		protected String styleClass = null;

		/**
		 * type of input
		 */
		protected String type = null;

		/** 
		 * Holds value of property useJsCalendar. 
		 */
		protected String useJsCalendar = null;

		/**
		 * current value, readed from request 
		 */
		protected String value = null;

		/**
		 * identifier of this value object
		 */
		protected int valueId = -1;

		/*
		 * holds the searchAlgo 
		 */
		protected String searchAlgo = null;

	}

	/** DOCUMENT ME! */
	protected static String FLT_VALUETYPE_DATE = "date";

	/** DOCUMENT ME! */
	protected static String FLT_VALUETYPE_NUMERIC = "numeric";

	/** DOCUMENT ME! */
	protected static String FLT_VALUETYPE_SELECT = "select";

	/** DOCUMENT ME! */
	protected static String FLT_VALUETYPE_TEXT = "text";

	/** DOCUMENT ME! */
	protected static String FLT_VALUETYPE_TIMESTAMP = "timestamp";

	static Category logCat =
		Category.getInstance(DbFilterValueTag.class.getName());

	/**
	 * read from request all values associated to the condition identified with <tableId>, <conditionId>.
	 * It try to read the value with identifier 0, if succeded go on with identifier 1, and so on.
	 *    
	 * @param tableId identify filter in request's parameters
	 * @param conditionId identify condition in request's parameter
	 * @param request
	 * @return list of all values readed from request
	 */
	protected static FieldValue[] readValuesFromRequest(
		HttpServletRequest request,
		int tableId,
		int conditionId) {
		FieldValues values = new FieldValues();

		for (int valueId = 0; true; ++valueId) {
			// read from parameter's request the value and the type having this id
			String paramValue =
				DbFilterConditionTag.getConditionName(tableId, conditionId)
					+ DbFilterTag.FLT_VALUE
					+ valueId;
			String paramType =
				DbFilterConditionTag.getConditionName(tableId, conditionId)
					+ DbFilterTag.FLT_VALUETYPE
					+ valueId;
			String searchAlgoType =
				DbFilterConditionTag.getConditionName(tableId, conditionId)
					+ DbFilterTag.FLT_SEARCHALGO
					+ valueId;

			String value = ParseUtil.getParameter(request, paramValue);
			String valueType = ParseUtil.getParameter(request, paramType);
			
			String aSearchAlgorithm = ParseUtil.getParameter(request, searchAlgoType);
			int algorithm = Constants.SEARCH_ALGO_SHARP;
			if (!Util.isNull(aSearchAlgorithm)) {
			   if (aSearchAlgorithm.startsWith("weakStartEnd")) {
				  algorithm = Constants.SEARCH_ALGO_WEAK_START_END;
			   } else if (aSearchAlgorithm.startsWith("weakStart")) {
				  algorithm = Constants.SEARCH_ALGO_WEAK_START;
			   } else if (aSearchAlgorithm.startsWith("weakEnd")) {
				  algorithm = Constants.SEARCH_ALGO_WEAK_END;
			   } else if (aSearchAlgorithm.startsWith("weak")) {
				  algorithm = Constants.SEARCH_ALGO_WEAK;
			   }
			}
			valueType = Util.isNull(valueType) ? FLT_VALUETYPE_TEXT : valueType;
			if (value != null) {
				// add value, possibly converted, to list
				Field f = new Field();
				f.setName(paramValue);
				f.setId(valueId);
				f.setFieldType(valueType);
            Table table = null;
            try {
               table = DbFormsConfigRegistry.instance().lookup().getTable(tableId);
            } catch (Exception e) {
               logCat.error("readValuesFromRequest", e);
            }
            f.setTable(table);
				FieldValue fv = new FieldValue(f, value);
				fv.setLocale(MessageResources.getLocale(request));
				fv.setSearchAlgorithm(algorithm);
				values.put(fv);
			} else {
				// didn't find any parameter with this id, so we have finished
				break;
			}
		}

		return values.toArray();
	}

	/**
	 * contain the state of this tag object
	 */
	private State state;

	/**
	 * 
	 */
	public DbFilterValueTag() {
		super();
		state = new State();
	}

	/** 
	 * initialize  environment
	 * 
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {
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
	private String generateTagString(
		String value,
		String description,
		boolean selected) {
		StringBuffer tagBuf = new StringBuffer();
		tagBuf.append("<option value=\"");
		tagBuf.append(value);
		tagBuf.append("\"");

		if (selected) {
			tagBuf.append(" selected=\"selected\"");
		}

		tagBuf.append(">");
		tagBuf.append(description.trim());
		tagBuf.append("</option>");

		return tagBuf.toString();
	}

	/**
	 * @return
	 */
	public String getJsCalendarDateFormat() {
		return state.jsCalendarDateFormat;
	}

	/**
	 * @return
	 */
	public String getLabel() {
		return state.label;
	}

	/**
	 * @return
	 */
	protected State getState() {
		return state;
	}

	/**
	 * @return
	 */
	public String getUseJsCalendar() {
		return state.useJsCalendar;
	}

	public String getSearchAlgo() {
		return state.searchAlgo;
	}

	private String getValueName() {
		return ((DbFilterConditionTag) getParent()).getConditionName()
			+ DbFilterTag.FLT_VALUE
			+ state.valueId;
	}

	private String getValueType() {
		return ((DbFilterConditionTag) getParent()).getConditionName()
			+ DbFilterTag.FLT_VALUETYPE
			+ state.valueId;
	}

	private String getSearchAlgoType() {
		return ((DbFilterConditionTag) getParent()).getConditionName()
			+ DbFilterTag.FLT_SEARCHALGO
			+ state.valueId;

	}

	/**
	 * initialize tag's state before start using it
	 */
	private void init() {
		// state object is createad in constructor (for the first use) and in doEndTag next
		if (state.type == null) {
			state.type = "text";
		}

		if (state.styleClass == null) {
			state.styleClass = "";
		}

		state.valueId = ((DbFilterConditionTag) getParent()).addValue(this);
		state.value =
			ParseUtil.getParameter(
				(HttpServletRequest) pageContext.getRequest(),
				getValueName());

		if (state.value == null) {
			state.value = "";
		}

		// the type attribute can be read either from request, with FLT_VALUETYPE, or directly from page
		state.embeddedData = null;
	}

	protected Object getFieldObject() {
		FieldValue fv = new FieldValue(getField(), state.value);
		fv.setLocale(
			MessageResources.getLocale(
				(HttpServletRequest) pageContext.getRequest()));
		return fv.getFieldValueAsObject();
	}

	/**
	 * render output of this value object. This is called only if its parent's condition is selected
	 * 
	 * @return
	 * @throws JspException
	 */
	protected StringBuffer render() throws JspException {
		StringBuffer buf = new StringBuffer();

		Field f = new Field();
		f.setName(state.label);
		f.setId(state.valueId);
		f.setFieldType(state.type);
      f.setTable(getParentForm().getTable());
		setField(f);

		if (state.label != null) {
			buf.append("<b>" + state.label + "</b>\n");
		}

		if (state.type.equalsIgnoreCase(FLT_VALUETYPE_TEXT)
			|| state.type.equalsIgnoreCase(FLT_VALUETYPE_NUMERIC)) {
			renderTextElement(buf);
		} else if (
			state.type.equalsIgnoreCase(FLT_VALUETYPE_DATE)
				|| state.type.equalsIgnoreCase(FLT_VALUETYPE_TIMESTAMP)) {
			renderDateElement(buf);
		} else if (
			FLT_VALUETYPE_SELECT.equalsIgnoreCase(state.type)
				&& (state.embeddedData != null)) {
			renderSelectElement(buf);
		} else {
			throw new JspException("type not correct");
		}

		return buf;
	}

	/**
	 * render input's type "date"
	 * 
	 * @param buf
	 */
	private void renderDateElement(StringBuffer buf) {
		renderTextElement(buf);

		// if property useJSCalendar is set to 'true' we will now add a little
		// image that can be clicked to popup a small JavaScript Calendar
		// written by Robert W. Husted to edit the field:
		if ("true".equals(state.useJsCalendar)) {
			buf.append(" <a href=\"javascript:doNothing()\" ").append(
				" onclick=\"");

			setPattern(state.jsCalendarDateFormat);
			state.jsCalendarDateFormat =
				((SimpleDateFormat) getFieldFormat()).toPattern();

			if (state.jsCalendarDateFormat != null) // JS Date Format set ?
				{
				buf.append(
					"calDateFormat='" + state.jsCalendarDateFormat + "';");
			}

			buf
				.append("setDateField(document.dbform['")
				.append(getValueName())
				.append("']);")
				.append(" top.newWin = window.open('")
				.append(
					((HttpServletRequest) pageContext.getRequest())
						.getContextPath())
				.append("/jscal/calendar.html','cal','width=270,height=280')\">")
				.append("<img src=\"")
				.append(
					((HttpServletRequest) pageContext.getRequest())
						.getContextPath())
				.append("/jscal/calendar.gif\" width=\"32\" height=\"32\" ")
				.append(" border=0  alt=\"Click on the Calendar to activate the Pop-Up Calendar Window.\">")
				.append("</img>")
				.append("</a>");
		}
	}

	/**
	 * render input's type "select"
	 * 
	 * @param buf
	 */
	private void renderSelectElement(StringBuffer buf) {
		String sizestr = "";

		if (state.size != null) {
			sizestr = "size=\"" + state.size + "\" ";
		}

		buf.append(
			"<select name=\""
				+ getValueName()
				+ "\" "
				+ sizestr
				+ " class=\""
				+ state.styleClass
				+ "\">\n");

		if ((state.customEntry != null)
			&& (state.customEntry.trim().length() > 0)) {
			String aKey =
				org.dbforms.util.ParseUtil.getEmbeddedStringWithoutDots(
					state.customEntry,
					0,
					',');
			String aValue =
				org.dbforms.util.ParseUtil.getEmbeddedStringWithoutDots(
					state.customEntry,
					1,
					',');
			boolean isSelected = false;

			if ((state.selectedIndex == null)
				|| (state.selectedIndex.trim().length() == 0)) {
				isSelected =
					"true".equals(
						ParseUtil.getEmbeddedStringWithoutDots(
							state.customEntry,
							2,
							','));
			}

			buf.append(generateTagString(aKey, aValue, isSelected));
		}

		int embeddedDataSize = state.embeddedData.size();

		for (int i = 0; i < embeddedDataSize; i++) {
			KeyValuePair aKeyValuePair =
				(KeyValuePair) state.embeddedData.get(i);
			String aKey = aKeyValuePair.getKey();
			String aValue = aKeyValuePair.getValue();

			// select, if datadriven and data matches with current value OR if explicitly set by user
			boolean isSelected = aKey.equals(state.value);
			buf.append(generateTagString(aKey, aValue, isSelected));
		}

		buf.append("</select>\n");
	}

	/**
	 * render input's type "text"
	 * 
	 * @param buf
	 */
	private void renderTextElement(StringBuffer buf) {
		String sizestr = "";

		if (state.size != null) {
			sizestr = "size=\"" + state.size + "\" ";
		}

		buf.append(
			"<input type=\"text\" name=\""
				+ getValueName()
				+ "\" value=\""
				+ this.getFormattedFieldValue()
				+ "\""
				+ sizestr
				+ " class=\""
				+ state.styleClass
				+ "\"/>\n");
		buf.append(
			"<input type=\"hidden\" name=\""
				+ getValueType()
				+ "\" value=\""
				+ state.type.toLowerCase()
				+ "\"/>\n");
		if (!Util.isNull(state.searchAlgo)) {
			buf.append(
				"<input type=\"hidden\" name=\""
					+ getSearchAlgoType()
					+ "\" value=\""
					+ state.searchAlgo
					+ "\"/>\n");
		}
	}

	/**
	 * Allows an additional (independant) entry into the select list
	 * 
	 * @param string
	 */
	public void setCustomEntry(String string) {
		state.customEntry = string;
	}

	/**
	This method is a "hookup" for EmbeddedData - Tags which can assign the lines of data they loaded
	(by querying a database, or by rendering data-subelements, etc. etc.) and make the data
	available to this tag.
	[this method is defined in Interface DataContainer]
	*/
	public void setEmbeddedData(List embeddedData) {
		state.embeddedData = embeddedData;
	}

	/**
	 * property jsCalendarDateFormat.
	 *  
	 * @param string
	 */
	public void setJsCalendarDateFormat(String string) {
		state.jsCalendarDateFormat = string;
	}

	/**
	 * property label showed before input tag
	 * 
	 * @param string
	 */
	public void setLabel(String string) {
		state.label = string;
	}

	/**
	 * property currently selected index, valid only when type = select
	 * 
	 * @param string
	 */
	public void setSelectedIndex(String string) {
		state.selectedIndex = string;
	}

	/**
	 * property html input's attribute size
	 * 
	 * @param string
	 */
	public void setSize(String string) {
		state.size = string;
	}

	/**
	 * @param state
	 */
	public void setState(
		PageContext pg,
		DbFilterConditionTag parent,
		State state) {
		setPageContext(pg);
		setParent(parent);
		this.state = state;
	}

	/**
	 * css class to be applied to input element
	 * 
	 * @param string
	 */
	public void setStyleClass(String string) {
		state.styleClass = string;
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
	public void setType(String string) {
		state.type = string;
	}

	/**
	 * @param string
	 */
	public void setUseJsCalendar(String string) {
		state.useJsCalendar = string;
	}

	public void setSearchAlgo(String value) {
		state.searchAlgo = value;
	}

	/**
	 * reset tag state
	 * 
	 * @see javax.servlet.jsp.tagext.TryCatchFinally#doFinally()
	 */
	public void doFinally() {
		state = new State();
		super.doFinally();
	}

	/**
	 * @see javax.servlet.jsp.tagext.TryCatchFinally#doCatch(java.lang.Throwable)
	 */
	public void doCatch(Throwable t) throws Throwable {
		throw t;
	}

}