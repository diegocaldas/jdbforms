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

import org.dbforms.event.WebEvent;
import org.dbforms.event.eventtype.EventType;

import org.dbforms.util.*;

import java.util.*;

import javax.servlet.jsp.*;



/**
 * <p>
 * This tag renders a html CHECKBOX element or a whole group of them
 * </p>
 *
 * @author Joachim Peer
 */
public class DbCheckboxTag extends DbBaseHandlerTag implements DataContainer,
                                                               javax.servlet.jsp.tagext.TryCatchFinally {
   private static Log logCat        = LogFactory.getLog(DbCheckboxTag.class); // logging category for this class
   private List       embeddedData  = null;
   private String     checked; // only needed if parentForm is in "insert-mode", otherwise the DbForms-Framework determinates whether a checkbox should be selected or not.
   private String     growDirection; // only needed if we habe a whole "group" of DbRadioTags; default = null == horizontal
   private String     growSize      = "0"; // only needed if we habe a whole "group" of DbRadioTags; default = 1
   private String     noValue;
   private String     value;
   private String    force         = "false";

   /**
    * DOCUMENT ME!
    *
    * @param checked DOCUMENT ME!
    */
   public void setChecked(String checked) {
      this.checked = checked;
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
      this.embeddedData = embeddedData;
   }


   /**
    * DOCUMENT ME!
    *
    * @param b
    */
   public void setForce(String b) {
      force = b;
   }


   /**
    * DOCUMENT ME!
    *
    * @return
    */
   public String getForce() {
      return force;
   }


   /**
    * DOCUMENT ME!
    *
    * @param growDirection DOCUMENT ME!
    */
   public void setGrowDirection(String growDirection) {
      this.growDirection = growDirection;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getGrowDirection() {
      return growDirection;
   }


   /**
    * DOCUMENT ME!
    *
    * @param growSize DOCUMENT ME!
    */
   public void setGrowSize(String growSize) {
      try {
         int grow = Integer.parseInt(growSize);

         if (grow > 0) {
            this.growSize = growSize;
         } else {
            this.growSize = "0";
         }
      } catch (NumberFormatException nfe) {
         logCat.warn(" setGrowSize(" + growSize + ") NumberFormatException : "
                     + nfe.getMessage());
         this.growSize = "0";
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getGrowSize() {
      return growSize;
   }


   /**
    * Sets the noValue.
    *
    * @param noValue The noValue to set
    */
   public void setNovalue(String noValue) {
      this.noValue = noValue;
   }


   /**
    * Returns the noValue.
    *
    * @return String
    */
   public String getNovalue() {
      return noValue;
   }


   /**
    * DOCUMENT ME!
    *
    * @param string
    */
   public void setValue(String string) {
      value = string;
   }


   /**
    * DOCUMENT ME!
    *
    * @return
    */
   public String getValue() {
      return value;
   }


   /**
    * @see javax.servlet.jsp.tagext.TryCatchFinally#doCatch(java.lang.Throwable)
    */
   public void doCatch(Throwable t) throws Throwable {
      throw t;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws javax.servlet.jsp.JspException DOCUMENT ME!
    * @throws JspException DOCUMENT ME!
    */
   public int doEndTag() throws javax.servlet.jsp.JspException {
      StringBuffer tagBuf = new StringBuffer();
      WebEvent     we = getParentForm()
                           .getWebEvent();

      // current Value from Database; or if no data: explicitly set by user; or ""
      String currentValue = getFormFieldValue();

      // Because it can generate more than one checkbox, and to avoid wrong concatenation
      // ex: onclick="this.checked=true; this.checked=false; this.checked=true ..."
      String onclick = (getOnClick() != null) ? getOnClick()
                                              : "";

      if (onclick.lastIndexOf(";") != (onclick.length() - 1)) {
         onclick += ";"; // be sure javascript end with ";"
      }

      // For generation Javascript Validation.  Need all original and modified fields name
      getParentForm()
         .addChildName(getName(), getFormFieldName());

      if (embeddedData == null) { // no embedded data is nested in this tag

         // select, if datadriven and data matches with current value OR if explicitly set by user
         boolean isSelected = ((!getParentForm()
                                    .isFooterReached()
                              || ((we != null)
                              && we.getType()
                                   .equals(EventType.EVENT_NAVIGATION_RELOAD)))
                              && (getValue() != null)
                              && getValue()
                                    .equals(currentValue))
                              || (getParentForm()
                                     .isFooterReached() && hasCheckedSet());

         // nk, check if need to force it on. Useful w/ custom controller or javascript
         if (Util.getTrue(getForce())) {
            isSelected = hasCheckedSet();
         }

         if (hasReadOnlySet() || getParentForm()
                                          .hasReadOnlySet()) {
            setOnClick("this.checked=" + isSelected + ";" + onclick);
         }

         tagBuf.append(generateTagString(getValue(), "", isSelected));
      } else {
         int embeddedDataSize = embeddedData.size();
         int maxSize = Integer.parseInt(getGrowSize());

         tagBuf.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr valign=\"top\">");

         for (int i = 0; i < embeddedDataSize; i++) {
            KeyValuePair aKeyValuePair = (KeyValuePair) embeddedData.get(i);
            String       aKey   = aKeyValuePair.getKey();
            String       aValue = aKeyValuePair.getValue();

            // select, if datadriven and data matches with current value OR if explicitly set by user
            boolean isSelected = aKey.equals(currentValue);

            if (hasReadOnlySet() || getParentForm()
                                             .hasReadOnlySet()) {
               setOnClick("this.checked=" + isSelected + ";" + onclick);
            }

            if ("horizontal".equals(getGrowDirection())
                      && (maxSize != 0)
                      && ((i % maxSize) == 0)
                      && (i != 0)) {
               tagBuf.append("</tr><tr valign=\"top\">");
            }

            if ("vertical".equals(getGrowDirection()) && (i != 0)) {
               tagBuf.append("</tr><tr valign=\"top\">");
            }

            tagBuf.append("<td>")
                  .append(generateTagString(aKey, aValue, isSelected))
                  .append("&nbsp;</td>");
         }

         tagBuf.append("</tr></table>");
      }

      if (!Util.isNull(getNovalue())) {
         // Write noValue last. During parameter parsing the 
         // first written value will be returned.
         // This the setted value!!!
         tagBuf.append("<input type=\"hidden\" name=\"");
         tagBuf.append(getFormFieldName());
         tagBuf.append("\" value =\"");
         tagBuf.append(getNovalue());
         tagBuf.append("\" ");
         tagBuf.append("/>");
      }

      try {
         pageContext.getOut()
                    .write(tagBuf.toString());

         // Writes out the old field value
         writeOutSpecialValues();
      } catch (java.io.IOException ioe) {
         throw new JspException("IO Error: " + ioe.getMessage());
      }

      return EVAL_PAGE;
   }


   /**
    * DOCUMENT ME!
    */
   public void doFinally() {
      embeddedData  = null;
      checked       = null;
      growDirection = null;
      growSize      = "0";
      noValue       = null;
      value         = null;
      force         = "false";
      super.doFinally();
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws javax.servlet.jsp.JspException DOCUMENT ME!
    */
   public int doStartTag() throws javax.servlet.jsp.JspException {
      return EVAL_BODY_BUFFERED;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public boolean hasCheckedSet() {
      return Util.getTrue(checked);
   }


   private String generateTagString(String  avalue,
                                    String  description,
                                    boolean selected) {
      StringBuffer tagBuf = new StringBuffer();

      tagBuf.append("<input type=\"checkbox\" name=\"");
      tagBuf.append(getFormFieldName());
      tagBuf.append("\" value =\"");
      tagBuf.append(avalue);
      tagBuf.append("\" ");

      if (selected) {
         tagBuf.append(" checked=\"checked\" ");
      }

      if (getAccessKey() != null) {
         tagBuf.append(" accesskey=\"");
         tagBuf.append(getAccessKey());
         tagBuf.append("\"");
      }

      if (getTabIndex() != null) {
         tagBuf.append(" tabindex=\"");
         tagBuf.append(getTabIndex());
         tagBuf.append("\"");
      }

      tagBuf.append(prepareStyles());
      tagBuf.append(prepareEventHandlers());
      tagBuf.append(">");
      tagBuf.append(description);
      tagBuf.append("</input>");

      return tagBuf.toString();
   }
}
