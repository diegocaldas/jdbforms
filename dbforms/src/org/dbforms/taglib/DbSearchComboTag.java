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

import org.dbforms.config.*;
import org.dbforms.interfaces.IDataContainer;
import org.dbforms.interfaces.StaticData;

import org.dbforms.util.*;

import java.util.List;

import javax.servlet.http.*;
import javax.servlet.jsp.*;



/**
 * <p>
 * renders a select field for searching with special default search modes.
 * </p>
 *
 * <p>
 * example:
 * </p>
 * &lt;input type="hidden" name="searchalgo_0_1" value="weakEnd"/&gt; &lt;input
 * type="hidden" name="searchmode_0_1" value="AND"/&gt; &lt;select
 * name="search_0_1"/&gt; &lt;/select&gt; searchalgo and searchmode are set by
 * parameter.
 *
 * @author Henner Kollmann
 */
public class DbSearchComboTag extends DbSearchTag implements IDataContainer,
                                                             javax.servlet.jsp.tagext.TryCatchFinally {
   private List   embeddedData  = null;
   private String compareWith   = "key";
   private String customEntry;
   private String selectedIndex;
   private String size          = "1";

   /**
    * Creates a new DbSearchComboTag object.
    */
   public DbSearchComboTag() {
      setSearchAlgo("sharp");
   }

   /**
    * DOCUMENT ME!
    *
    * @param string
    */
   public void setCompareWith(String string) {
      compareWith = string;
   }


   /**
    * DOCUMENT ME!
    *
    * @return
    */
   public String getCompareWith() {
      return compareWith;
   }


   /**
    * DOCUMENT ME!
    *
    * @param customEntry DOCUMENT ME!
    */
   public void setCustomEntry(String customEntry) {
      this.customEntry = customEntry;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getCustomEntry() {
      return customEntry;
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
    * @param selectedIndex DOCUMENT ME!
    */
   public void setSelectedIndex(String selectedIndex) {
      this.selectedIndex = selectedIndex;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getSelectedIndex() {
      return selectedIndex;
   }


   /**
    * DOCUMENT ME!
    *
    * @param string
    */
   public void setSize(String string) {
      size = string;
   }


   // ------------------------------------------------------ Protected Methods

   /**
    * DOCUMENT ME!
    *
    * @return
    */
   public String getSize() {
      return size;
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
      HttpServletRequest request = (HttpServletRequest) this.pageContext
                                   .getRequest();

      Field              field = getField();

      StringBuffer       tagBuf = new StringBuffer();

      String             oldValue = ParseUtil.getParameter(request,
                                                           field
                                                           .getSearchFieldName());

      if (!Util.isNull(oldValue)) {
         selectedIndex = oldValue;
      }

      boolean isSelected = false;

      if (embeddedData != null) { // no embedded data is nested in this tag

         if (!Util.isNull(customEntry)) {
            String aKey = org.dbforms.util.StringUtil
                          .getEmbeddedStringWithoutDots(customEntry, 0, ',');
            String aValue = org.dbforms.util.StringUtil
                            .getEmbeddedStringWithoutDots(customEntry, 1, ',');

         	// is captionResource is activated, retrieve the value from the MessageResources bundle
            if (getParentForm().hasCaptionResourceSet()) {
              aValue = MessageResources.getMessage(request,aValue);
            }

            
            if (Util.isNull(selectedIndex)) {
               isSelected = Util.getTrue(org.dbforms.util.StringUtil.getEmbeddedStringWithoutDots(customEntry,
                                                                                                 2,
                                                                                                 ',').trim());
            }

            tagBuf.append(generateTagString(aKey, aValue, isSelected));
         }

         int embeddedDataSize  = embeddedData.size();
         int selectedIndexList = 0;

         try {
            selectedIndexList = Integer.parseInt(selectedIndex);
         } catch (Exception e) {
            selectedIndexList = 0;
         }

         for (int i = 0; i < embeddedDataSize; i++) {
            StaticData aKeyValuePair = (StaticData) embeddedData.get(i);
            String       aKey   = aKeyValuePair.getKey();
            String       aValue = aKeyValuePair.getValue();

            // select, if datadriven and data matches with current value OR if explicitly set by user
            if (Util.isNull(selectedIndex) && !isSelected) {
               isSelected = i == 0;
            } else {
               if ("value".equalsIgnoreCase(getCompareWith())) {
                  isSelected = aValue.equals(selectedIndex);
               } else if ("list".equalsIgnoreCase(getCompareWith())) {
                  isSelected = selectedIndexList == i;
               } else {
                  isSelected = aKey.equals(selectedIndex);
               }
            }

            tagBuf.append(generateTagString(aKey, aValue, isSelected));
         }
      }

      tagBuf.append("</select>");

      try {
         pageContext.getOut()
                    .write(renderPatternHtmlInputField());
         pageContext.getOut()
                    .write(RenderHiddenFields(field));
         pageContext.getOut()
                    .write(generateSelectHeader(field));
         pageContext.getOut()
                    .write(tagBuf.toString());
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
      selectedIndex = null;
      customEntry   = null;
      size          = "1";
      compareWith   = "key";
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
      return EVAL_BODY_INCLUDE;
   }


   private String generateSelectHeader(Field f)
                                throws javax.servlet.jsp.JspException {
      // This method have been 
      StringBuffer tagBuf = new StringBuffer();
      tagBuf.append("<select name=\"");
      tagBuf.append(f.getSearchFieldName());
      tagBuf.append("\"");

      if (size != null) {
         tagBuf.append(" size=\"");
         tagBuf.append(size);
         tagBuf.append("\"");
      }

      tagBuf.append(prepareStyles());
      tagBuf.append(prepareEventHandlers());
      tagBuf.append(">");

      return tagBuf.toString();
   }


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
}
