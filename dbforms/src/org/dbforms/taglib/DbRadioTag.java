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

import org.dbforms.interfaces.IDataContainer;
import org.dbforms.interfaces.StaticData;
import org.dbforms.util.*;

import java.util.List;

import javax.servlet.jsp.*;



/**
 * <p>
 * This tag renders a html RADIO element or a whole group of them
 * </p>
 *
 * @author Joachim Peer
 */
public class DbRadioTag extends AbstractDbBaseHandlerTag implements IDataContainer,
                                                            javax.servlet.jsp.tagext.TryCatchFinally {
   private static Log logCat = LogFactory.getLog(DbRadioTag.class.getName()); // logging category for this class
   private List       embeddedData  = null;
   private String     growDirection; // only needed if we have a whole "group" of DbRadioTags; default = null == horizontal
   private String     growSize      = "0"; // limit the number of elements per row (growDirection="horizontal")

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
      this.growSize = growSize;
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

      // current Value from Database; or if no data: explicitly set by user; or ""
      String currentValue = getFormFieldValue();

      if (Util.isNull(currentValue)) {
         currentValue = getDefaultValue();
      }

      if (embeddedData != null) {
         int embeddedDataSize = embeddedData.size();

         // If radio is in read-only, retrieve selectedIndex and set the onclick of all radio with
         // "document.formName['radioName'][selectedIndex].checked=true"
         //
         if (hasReadOnlySet() || getParentForm()
                                          .hasReadOnlySet()) {
            // First pass to retreive radio selectedIndex, because in Javascript it use only this index (Netscape 4.x)
            for (int i = 0; i < embeddedDataSize; i++) {
               StaticData aKeyValuePair = (StaticData) embeddedData.get(i);
               String       aKey = aKeyValuePair.getKey();

               if (aKey.equals(currentValue)) {
                  String onclick = (getOnClick() != null) ? getOnClick()
                                                          : "";

                  if (onclick.lastIndexOf(";") != (onclick.length() - 1)) {
                     onclick += ";"; // be sure javascript end with ";"
                  }

                  setOnClick("document.dbform['" + getFormFieldName() + "']["
                             + i + "].checked=true;" + onclick);

                  break;
               }
            }
         }

         int maxSize = growSize();

         tagBuf.append("<table  BORDER=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr valign=\"top\">");

         for (int i = 0; i < embeddedDataSize; i++) {
            StaticData aKeyValuePair = (StaticData) embeddedData.get(i);
            String       aKey   = aKeyValuePair.getKey();
            String       aValue = aKeyValuePair.getValue();

            // select, if datadriven and data matches with current value OR if explicitly set by user
            boolean isSelected = aKey.equals(currentValue);

            if ("horizontal".equals(getGrowDirection())
                      && (maxSize != 0)
                      && ((i % maxSize) == 0)
                      && (i != 0)) {
               tagBuf.append("</tr><tr valign=\"top\">");
            }

            if ("vertical".equals(getGrowDirection()) && (i != 0)) {
               tagBuf.append("</tr><tr valign=\"top\">");
            }

            tagBuf.append("<td ");
            tagBuf.append(prepareStyles());
            tagBuf.append(">")
                  .append(generateTagString(aKey, aValue, isSelected))
                  .append("</td>");
         }

         tagBuf.append("</tr></table>");
      }

      // For generation Javascript Validation.  Need all original and modified fields name
      getParentForm()
         .addChildName(getName(), getFormFieldName());

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
      growDirection = null;
      growSize      = "0";
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


   private String generateTagString(String  value,
                                    String  description,
                                    boolean selected) {
      StringBuffer tagBuf = new StringBuffer();
      tagBuf.append("<input type=\"radio\" name=\"");
      tagBuf.append(getFormFieldName());
      tagBuf.append("\" value=\"");
      tagBuf.append(value);
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
      tagBuf.append(">\n");
      tagBuf.append(description);
      tagBuf.append("</input>");

      return tagBuf.toString();
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   private int growSize() {
      int res = 0;

      try {
         res = Integer.parseInt(growSize);
      } catch (NumberFormatException nfe) {
         logCat.warn(" setGrowSize(" + growSize + ") NumberFormatException : "
                     + nfe.getMessage());
      }

      return res;
   }
}
