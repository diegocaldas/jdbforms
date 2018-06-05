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

import org.dbforms.config.Constants;
import org.dbforms.config.DbFormsConfigRegistry;
import org.dbforms.config.Field;
import org.dbforms.config.FieldValue;
import org.dbforms.config.FieldValues;
import org.dbforms.config.Table;
import org.dbforms.interfaces.IDataContainer;
import org.dbforms.interfaces.StaticData;

import org.dbforms.util.MessageResources;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.StringUtil;
import org.dbforms.util.Util;

import java.text.SimpleDateFormat;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;



/**
 * Map a placeholder (?) in sql code to an input tag. Used as nested tag inside
 * filterCondition. Implements DataContainer interface to use the nested tags
 * queryData, staticData ...
 *
 * @author Sergio Moretti
 * @version $Revision$
 */
public class DbFilterValueTag extends AbstractDbBaseHandlerTag implements IDataContainer,
                                                                  javax.servlet.jsp.tagext.TryCatchFinally {
   /** DOCUMENT ME! */
   private static String FLT_VALUETYPE_DATE = "date";

   /** DOCUMENT ME! */
   private static String FLT_VALUETYPE_NUMERIC = "numeric";

   /** DOCUMENT ME! */
   private static String FLT_VALUETYPE_SELECT = "select";

   /** DOCUMENT ME! */
   private static String FLT_VALUETYPE_TEXT = "text";

   /** Reuse the previous filter value from the list */
   private static String FLT_VALUETYPE_REPEAT = "repeat_previous";

   /** DOCUMENT ME! */
   private static String FLT_VALUETYPE_TIMESTAMP = "timestamp";
   private static Log    logCat = LogFactory.getLog(DbFilterValueTag.class
                                                    .getName());

   /** contain the state of this tag object */
   private transient State state;

   /**
                                                                                  *
                                                                                  */
   public DbFilterValueTag() {
      super();
      state = new State();
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
    * This method is a "hookup" for EmbeddedData - Tags which can assign the
    * lines of data they loaded (by querying a database, or by rendering
    * data-subelements, etc. etc.) and make the data available to this tag.
    * [this method is defined in Interface DataContainer]
    *
    * @param embeddedData DOCUMENT ME!
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
    * DOCUMENT ME!
    *
    * @return
    */
   public String getJsCalendarDateFormat() {
      return state.jsCalendarDateFormat;
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
    * DOCUMENT ME!
    *
    * @return
    */
   public String getLabel() {
      return state.label;
   }


   /**
    * DOCUMENT ME!
    *
    * @param value DOCUMENT ME!
    */
   public void setSearchAlgo(String value) {
      state.searchAlgo = value;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getSearchAlgo() {
      return state.searchAlgo;
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
    * DOCUMENT ME!
    *
    * @param pg DOCUMENT ME!
    * @param parent DOCUMENT ME!
    * @param state
    */
   public void setState(PageContext          pg,
                        DbFilterConditionTag parent,
                        State                state) {
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
    *
    * <dl>
    * <dt>
    * text
    * </dt>
    * <dd>
    * text input
    * </dd>
    * <dt>
    * date
    * </dt>
    * <dd>
    * input text for date type, a validation of the value will be done, and it
    * supports the jscal object
    * </dd>
    * <dt>
    * timestamp
    * </dt>
    * <dd>
    * input text for timestamp type, a validation of the value will be done,
    * and it supports the jscal object (it doesn't fit very well, anyway ...)
    * </dd>
    * <dt>
    * numeric
    * </dt>
    * <dd>
    * input text for number, a validation of the value will be done
    * </dd>
    * <dt>
    * select
    * </dt>
    * <dd>
    * render an html select element, filled with nested tags like queryData,
    * staticData and so on.
    * </dd>
    * </dl>
    *
    *
    * @param string
    */
   public void setType(String string) {
      state.type = string;
   }


   /**
    * DOCUMENT ME!
    *
    * @param string
    */
   public void setUseJsCalendar(String string) {
      state.useJsCalendar = string;
   }


   /**
    * DOCUMENT ME!
    *
    * @return
    */
   public String getUseJsCalendar() {
      return state.useJsCalendar;
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
    * initialize environment
    *
    * @see javax.servlet.jsp.tagext.Tag#doStartTag()
    */
   public int doStartTag() throws JspException {
      init();

      return EVAL_BODY_BUFFERED;
   }


   /**
    * read from request all values associated to the condition identified with
    * &lt;tableId&gt;, &lt;conditionId&gt;. It try to read the value with
    * identifier 0, if succeded go on with identifier 1, and so on.
    *
    * @param request
    * @param tableId identify filter in request's parameters
    * @param conditionId identify condition in request's parameter
    *
    * @return list of all values readed from request
    */
   protected static FieldValue[] readValuesFromRequest(HttpServletRequest request,
                                                       int                tableId,
                                                       int                conditionId) {
      FieldValues values = new FieldValues();

      for (int valueId = 0; true; ++valueId) {
         // read from parameter's request the value and the type having this
         // id
         String paramValue = DbFilterConditionTag.getConditionName(tableId,
                                                                   conditionId)
                             + DbFilterTag.FLT_VALUE + valueId;
         String paramType = DbFilterConditionTag.getConditionName(tableId,
                                                                  conditionId)
                            + DbFilterTag.FLT_VALUETYPE + valueId;
         String searchAlgoType = DbFilterConditionTag.getConditionName(tableId,
                                                                       conditionId)
                                 + DbFilterTag.FLT_SEARCHALGO + valueId;

         String valueType = ParseUtil.getParameter(request, paramType);
         String value     = "";
         String aSearchAlgorithm = "";


         /* Null filterValue types are always 'text' */
         valueType = Util.isNull(valueType) ? FLT_VALUETYPE_TEXT
                 : valueType;

         /* DJH - Handle case of using "get previous" parameter type
          * NOTE! This does not handle the case of two previous parameter types back to back... */
         if (valueType.equalsIgnoreCase(DbFilterValueTag.FLT_VALUETYPE_REPEAT) && valueId > 0) {
             String prev_paramValue = DbFilterConditionTag.getConditionName(tableId, conditionId) + DbFilterTag.FLT_VALUE + (valueId - 1);
             String prev_paramType = DbFilterConditionTag.getConditionName(tableId, conditionId) + DbFilterTag.FLT_VALUETYPE + (valueId - 1);
             String prev_searchAlgoType = DbFilterConditionTag.getConditionName(tableId, conditionId) + DbFilterTag.FLT_SEARCHALGO + (valueId - 1);
             
             /* Get the previous parameter valueType */
             valueType = ParseUtil.getParameter(request, prev_paramType);
             value     = ParseUtil.getParameter(request, prev_paramValue);
             aSearchAlgorithm = ParseUtil.getParameter(request, prev_searchAlgoType);

         } else { /* Get parameter value as normal (i.e. current) */
             value     = ParseUtil.getParameter(request, paramValue);
             aSearchAlgorithm = ParseUtil.getParameter(request, searchAlgoType);
         }

         int    algorithm = Constants.SEARCH_ALGO_SHARP;

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

         /* Null filterValue types are always 'text' */
         valueType = Util.isNull(valueType) ? FLT_VALUETYPE_TEXT
                                            : valueType;

         if (value != null) {
            // add value, possibly converted, to list
            Field f = new Field();
            f.setName(paramValue);
            f.setId(valueId);
            f.setFieldType(valueType);

            Table table = null;

            try {
               table = DbFormsConfigRegistry.instance()
                                            .lookup()
                                            .getTable(tableId);
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
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   protected Object getFieldObject() {
      FieldValue fv = new FieldValue(getField(), state.value);
      fv.setLocale(MessageResources.getLocale((HttpServletRequest) pageContext
                                              .getRequest()));

      return fv.getFieldValueAsObject();
   }


   /**
    * DOCUMENT ME!
    *
    * @return
    */
   protected State getState() {
      return state;
   }


   /**
    * render output of this value object. This is called only if its parent's
    * condition is selected
    *
    * @return
    *
    * @throws JspException
    */
   protected StringBuffer render() throws JspException {
      StringBuffer buf = new StringBuffer();

      Field        f = new Field();
      f.setName(state.label);
      f.setId(state.valueId);
      f.setFieldType(state.type);
      f.setTable(getParentForm().getTable());
      setField(f);

      if (state.label != null) {
    	  if (getParentForm().hasCaptionResourceSet()) {
    		  try {
    			  String message = MessageResources.getMessage((HttpServletRequest)pageContext.getRequest(), state.label);
    			  if (message != null) {
    				  state.label = message;
    			  }
    		  } catch (Exception e) {
    			  logCat.debug("setCaption(" + state.label + ") Exception : "
    					  + e.getMessage());
    		  }
    	  }
         buf.append("<b>" + state.label + "</b>\n");
      }

      if (state.type.equalsIgnoreCase(FLT_VALUETYPE_TEXT)
                || state.type.equalsIgnoreCase(FLT_VALUETYPE_NUMERIC)) {
         renderTextElement(buf);
      } else if (state.type.equalsIgnoreCase(FLT_VALUETYPE_DATE)
                       || state.type.equalsIgnoreCase(FLT_VALUETYPE_TIMESTAMP)) {
         renderDateElement(buf);
      } else if (FLT_VALUETYPE_SELECT.equalsIgnoreCase(state.type)
                       && (state.embeddedData != null)) {
         renderSelectElement(buf);
      } else if (state.type.equalsIgnoreCase(FLT_VALUETYPE_REPEAT)) {
          renderHiddenElement(buf);
      } else {
         throw new JspException("type not correct");
      }

      return buf;
   }


   private String getSearchAlgoType() {
      return ((DbFilterConditionTag) getParent()).getConditionName()
             + DbFilterTag.FLT_SEARCHALGO + state.valueId;
   }


   private String getValueName() {
      return ((DbFilterConditionTag) getParent()).getConditionName()
             + DbFilterTag.FLT_VALUE + state.valueId;
   }


   private String getValueType() {
      return ((DbFilterConditionTag) getParent()).getConditionName()
             + DbFilterTag.FLT_VALUETYPE + state.valueId;
   }


   /**
    * generate option element for select. borrowed from
    *
    * @param value
    * @param description
    * @param selected
    *
    * @return string containing an html option element
    *
    * @see DbSearchComboTag#generateTagString
    */
   private String generateTagString(String  value,
                                    String  description,
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
    * initialize tag's state before start using it
    */
   private void init() {
      // state object is createad in constructor (for the first use) and in
      // doEndTag next
      if (state.type == null) {
         state.type = "text";
      }

      if (state.styleClass == null) {
         state.styleClass = "";
      }

      state.valueId = ((DbFilterConditionTag) getParent()).addValue(this);
      state.value   = ParseUtil.getParameter((HttpServletRequest) pageContext
                                             .getRequest(), getValueName());

      if (state.value == null) {
         state.value = "";
      }

      // the type attribute can be read either from request, with
      // FLT_VALUETYPE, or directly from page
      state.embeddedData = null;
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
         buf.append(" <a href=\"javascript:doNothing()\" ")
            .append(" onclick=\"");

         setPattern(state.jsCalendarDateFormat);
         state.jsCalendarDateFormat = ((SimpleDateFormat) getFormatter())
                                      .toPattern();

         if (state.jsCalendarDateFormat != null) // JS Date Format set ?
          {
            buf.append("calDateFormat='" + state.jsCalendarDateFormat + "';");
         }

         buf.append("setDateField(document.dbform['")
            .append(getValueName())
            .append("']);")
            .append(" top.newWin = window.open('")
            .append(((HttpServletRequest) pageContext.getRequest())
                    .getContextPath())
            .append("/dbformslib/jscal/calendar.html','cal','width=270,height=280')\">")
            .append("<img src=\"")
            .append(((HttpServletRequest) pageContext.getRequest())
                    .getContextPath())
            .append("/dbformslib/jscal/calendar.gif\" width=\"32\" height=\"32\" ")
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

      buf.append("<select name=\"" + getValueName() + "\" " + sizestr
                 + " class=\"" + state.styleClass + "\">\n");

      if ((state.customEntry != null)
                && (state.customEntry.trim()
                                           .length() > 0)) {
         String aKey = org.dbforms.util.StringUtil.getEmbeddedStringWithoutDots(state.customEntry,
                                                                               0,
                                                                               ',');
         String aValue = org.dbforms.util.StringUtil
                         .getEmbeddedStringWithoutDots(state.customEntry, 1, ',');
                   
    	 // is captionResource is activated, retrieve the value from the MessageResources bundle
         if (getParentForm().hasCaptionResourceSet()) {
        	 aValue = MessageResources.getMessage((HttpServletRequest)pageContext.getRequest(),aValue);
         }
        	 
         boolean isSelected = false;

         if ((state.selectedIndex == null)
                   || (state.selectedIndex.trim()
                                                .length() == 0)) {
            isSelected = "true".equals(StringUtil.getEmbeddedStringWithoutDots(state.customEntry,
                                                                              2,
                                                                              ','));
         }

         buf.append(generateTagString(aKey, aValue, isSelected));
      }

      int embeddedDataSize = state.embeddedData.size();

      for (int i = 0; i < embeddedDataSize; i++) {
         StaticData aKeyValuePair = (StaticData) state.embeddedData.get(i);
         String       aKey   = aKeyValuePair.getKey();
         String       aValue = aKeyValuePair.getValue();

         // select, if datadriven and data matches with current value OR if
         // explicitly set by user
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

      buf.append("<input type=\"text\" name=\"" + getValueName()
                 + "\" value=\"" + this.getFormattedFieldValue() + "\""
                 + sizestr + " class=\"" + state.styleClass + "\"/>\n");
      buf.append("<input type=\"hidden\" name=\"" + getValueType()
                 + "\" value=\"" + state.type.toLowerCase() + "\"/>\n");

      if (!Util.isNull(state.searchAlgo)) {
         buf.append("<input type=\"hidden\" name=\"" + getSearchAlgoType()
                    + "\" value=\"" + state.searchAlgo + "\"/>\n");
      }
   }

   /**
    * render input's type "repeat_previous"
    *
    * @param buf
    */
   private void renderHiddenElement(StringBuffer buf) {
      String sizestr = "";

      if (state.size != null) {
         sizestr = "size=\"" + state.size + "\" ";
      }

      buf.append("<input type=\"hidden\" name=\"" + getValueName()
                 + "\" value=\"" + this.getFormattedFieldValue() + "\""
                 + sizestr + " class=\"" + state.styleClass + "\"/>\n");
      buf.append("<input type=\"hidden\" name=\"" + getValueType()
                 + "\" value=\"" + state.type.toLowerCase() + "\"/>\n");

      if (!Util.isNull(state.searchAlgo)) {
         buf.append("<input type=\"hidden\" name=\"" + getSearchAlgoType()
                    + "\" value=\"" + state.searchAlgo + "\"/>\n");
      }
   }

   /**
    * tag's state holder. Used a separate class to hold tag's state to
    * workaround to Tag pooling, in which an tag object is reused, but we have
    * the need to store informations about all child tags in the parent, so we
    * store the state, and apply it to a dummy tag when needed.
    *
    * @author Sergio Moretti
    */
   protected static class State {
      /**
       * contains list of elements to show as options when type is select,
       * (DataContainer interface)
       */
      protected List embeddedData = null;

      /** Allows an additional (independant) entry into the select list */
      protected String customEntry = null;

      /** Holds value of property jsCalendarDateFormat. */
      protected String jsCalendarDateFormat = null;

      /** label showed before input tag */
      protected String label = null;

      /*
       * holds the searchAlgo
       */
      protected String searchAlgo = null;

      /** currently selected index, valid only when type = select */
      protected String selectedIndex = null;

      /** html input's attribute size */
      protected String size = null;

      /** css class to be applied to input element */
      protected String styleClass = null;

      /** type of input */
      protected String type = null;

      /** Holds value of property useJsCalendar. */
      protected String useJsCalendar = null;

      /** current value, readed from request */
      protected String value = null;

      /** identifier of this value object */
      protected int valueId = -1;
   }
}
