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
import java.util.List;
import javax.servlet.jsp.*;
import org.dbforms.util.*;
import org.dbforms.event.eventtype.EventType;
import org.dbforms.event.WebEvent;
import javax.servlet.http.*;



/****
 *
 * <p>This tag renders a html SELECT element including embedding OPTION elements.</p>
 *
 * @author Joachim Peer <j.peer@gmx.net>
 * @author Philip Grunikiewicz<grunikiewicz.philip@hydro.qc.ca>
 */
public class DbSelectTag extends DbBaseHandlerTag implements DataContainer,
      javax.servlet.jsp.tagext.TryCatchFinally
{
   private List  embeddedData  = null;
   private String  selectedIndex;
   private String  customEntry;
   private String  size;

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
    * Return the size of this field (synonym for <code>getCols()</code>).
    */
   public String getSize()
   {
      return size;
   }


   /**
    * Set the size of this field (synonym for <code>setCols()</code>).
    * @param size The new size
    */
   public void setSize(String size)
   {
      this.size = size;
   }


   /**
      This method is a "hookup" for EmbeddedData - Tags which can assign the lines of data they loaded
      (by querying a database, or by rendering data-subelements, etc. etc.) and make the data
      available to this tag.
      [this method is defined in Interface DataContainer]
    */
   public void setEmbeddedData(List embeddedData)
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
      return EVAL_BODY_BUFFERED;
   }


   private String generateSelectHeader() throws javax.servlet.jsp.JspException
   {
      // This method have been 
      StringBuffer tagBuf = new StringBuffer();

      tagBuf.append("<select name=\"");

      tagBuf.append(getFormFieldName());
      tagBuf.append("\"");

      if (size != null)
      {
         tagBuf.append(" size=\"");
         tagBuf.append(size);
         tagBuf.append("\"");
      }

      if (getAccessKey() != null)
      {
         tagBuf.append(" accesskey=\"");
         tagBuf.append(getAccessKey());
         tagBuf.append("\"");
      }

      if (getTabIndex() != null)
      {
         tagBuf.append(" tabindex=\"");
         tagBuf.append(getTabIndex());
         tagBuf.append("\"");
      }

      tagBuf.append(prepareStyles());
      tagBuf.append(prepareEventHandlers());
      tagBuf.append(">");

      return tagBuf.toString();
   }


   private String generateTagString(String value, String description, 
                                    boolean selected)
   {
      StringBuffer tagBuf = new StringBuffer();
      tagBuf.append("<option value=\"");
      tagBuf.append(escapeHtml(value));
      tagBuf.append("\"");

      if (selected)
      {
         tagBuf.append(" selected=\"selected\" ");
      }


      //20021203-HKK: Removed unneeded blank
      tagBuf.append(">");
      tagBuf.append(escapeHtml(description));


      // 20021025-HKK: Appended </option>
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
      List               errors = (List) request.getAttribute("errors");
      WebEvent           we     = getParentForm().getWebEvent();

      StringBuffer       tagBuf          = new StringBuffer();
      StringBuffer       selectedOptions = new StringBuffer();

      String             currentValue = getFormFieldValue();

      if (embeddedData != null)
      {
         // PG, 2001-12-14
         // Is their a custom entry? Display first...
         String ce = null;

         if (((ce = this.getCustomEntry()) != null) && (ce.trim().length() > 0))
         {
            boolean isSelected = false;
            String  aKey = org.dbforms.util.ParseUtil.getEmbeddedStringWithoutDots(
                                    ce, 0, ',');
            String  aValue = org.dbforms.util.ParseUtil.getEmbeddedStringWithoutDots(
                                      ce, 1, ',');

            // Check if we are in redisplayFieldsOnError mode and errors have occured
            // If so, only set to selected if currentRow is equal to custom row.
            if ((getParentForm().hasRedisplayFieldsOnErrorSet() && (errors != null)
                         && (errors.size() > 0))
                      || ((we != null)
                         && (we.getType().equals(EventType.EVENT_NAVIGATION_RELOAD))))
            {
               isSelected = (currentValue.equals(aKey));
            }
            else
            {
               isSelected = "true".equals(
                                     org.dbforms.util.ParseUtil.getEmbeddedStringWithoutDots(
                                              ce, 2, ','));
            }

            if (isSelected)
            {
               selectedOptions.append("-").append(aKey);
            }

            tagBuf.append(generateTagString(aKey, aValue, isSelected));
         }

         int embeddedDataSize = embeddedData.size();

         for (int i = 0; i < embeddedDataSize; i++)
         {
            KeyValuePair aKeyValuePair = (KeyValuePair) embeddedData.get(i);
            String       aKey   = aKeyValuePair.getKey();
            String       aValue = aKeyValuePair.getValue();

            // select, if datadriven and data matches with current value OR if explicitly set by user
            boolean isSelected = aKey.equals(currentValue);

            if (isSelected)
            {
               selectedOptions.append("-").append(aKey);
            }

            tagBuf.append(generateTagString(aKey, aValue, isSelected));
         }
      }

      tagBuf.append("</select>");

      //
      // For read-only mode.  Add unique generic javascript function.
      // Reset to the default selected values (in case of multiselection)
      // using javascript function on client side.
      //
      if (hasReadOnlySet() || getParentForm().hasReadOnlySet())
      {
         selectedOptions.append("-");

         String onChange = "resetSelect(this,'" + selectedOptions.toString()
                           + "');";
         setOnChange(onChange + ((getOnChange() != null) ? getOnChange() : ""));

         if (!getParentForm().existJavascriptFunction("resetSelect"))
         {
            StringBuffer buf = new StringBuffer();
            buf.append("\nfunction resetSelect(sel,x){\n");
            buf.append("   for(i=0;i<sel.length;i++){\n");
            buf.append("      var tmp = sel.options[i].value;\n");
            buf.append("      if(x.indexOf(('-'+tmp+'-'))!=-1){\n");
            buf.append("         sel.options[i].selected=true;\n");
            buf.append("      }else{;\n");
            buf.append("         sel.options[i].selected=false;\n");
            buf.append("      }\n");
            buf.append("   }\n");
            buf.append("}\n");

            getParentForm().addJavascriptFunction("resetSelect", buf);
         }
      }


      // For generation Javascript Validation.  Need original and modified fields name
      getParentForm().addChildName(getName(), getFormFieldName());

      try
      {
         pageContext.getOut().write(generateSelectHeader());
         pageContext.getOut().write(tagBuf.toString());
         writeOutSpecialValues();
      }
      catch (java.io.IOException ioe)
      {
         throw new JspException("IO Error: " + ioe.getMessage());
      }

      return EVAL_PAGE;
   }

   public String getFormattedFieldValue() {
      String res = getFieldValue();
      if (res == null)
         res =  ParseUtil.getParameter((HttpServletRequest) this.pageContext.getRequest(), getFormFieldName());
      return res;
   }

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   protected String typicalDefaultValue()
   {
      String val;
      // Lets check if the selectedIndex parameter has been input
      if (((val = this.getSelectedIndex()) != null)
                && (val.trim().length() != 0))
      {
         return val;
      }
      // No selectedIndex - business as usual...
      return (super.typicalDefaultValue());
   }


   /**
    * Gets the customEntry
    * @return Returns a String
    */
   public String getCustomEntry()
   {
      return customEntry;
   }


   /**
    * Sets the customEntry
    * @param customEntry The customEntry to set
    */
   public void setCustomEntry(String customEntry)
   {
      this.customEntry = customEntry;
   }


   /**
    * DOCUMENT ME!
    */
   public void doFinally()
   {
      embeddedData  = null;
      selectedIndex = null;
      customEntry   = null;
      size          = null;
      super.doFinally();
   }
   
   /**
    * @see javax.servlet.jsp.tagext.TryCatchFinally#doCatch(java.lang.Throwable)
    */
   public void doCatch(Throwable t) throws Throwable
   {
      throw t;
   }

}