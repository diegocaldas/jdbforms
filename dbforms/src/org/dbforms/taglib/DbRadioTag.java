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
import java.util.*;
import javax.servlet.jsp.*;
import javax.servlet.http.*;

import org.dbforms.util.*;
import org.dbforms.event.ReloadEvent;
import org.dbforms.event.WebEvent;
import org.apache.log4j.Category;



/****
 *
 * <p>This tag renders a html RADIO element or a whole group of them</p>
 *
 * @author Joachim Peer <j.peer@gmx.net>
 */
public class DbRadioTag extends DbBaseHandlerTag implements DataContainer
{
   static Category logCat        = Category.getInstance(DbRadioTag.class
         .getName()); // logging category for this class
   private Vector  embeddedData  = null;
   private String  checked; // only needed if parentForm is in "insert-mode", otherwise the DbForms-Framework determinates whether a radio should be selected or not.
   private String  growDirection; // only needed if we have a whole "group" of DbRadioTags; default = null == horizontal
   private String  growSize      = "0"; // limit the number of elements per row (growDirection="horizontal")
   private String  noValue;

   /**
    * DOCUMENT ME!
    *
    * @param checked DOCUMENT ME!
    */
   public void setChecked(String checked)
   {
      this.checked = checked;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getChecked()
   {
      return checked;
   }


   /**
    * DOCUMENT ME!
    *
    * @param growDirection DOCUMENT ME!
    */
   public void setGrowDirection(String growDirection)
   {
      this.growDirection = growDirection;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getGrowDirection()
   {
      return growDirection;
   }


   /**
    * DOCUMENT ME!
    *
    * @param growSize DOCUMENT ME!
    */
   public void setGrowSize(String growSize)
   {
      try
      {
         int grow = Integer.parseInt(growSize);

         if (grow > 0)
         {
            this.growSize = growSize;
         }
         else
         {
            this.growSize = "0";
         }
      }
      catch (NumberFormatException nfe)
      {
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
   public String getGrowSize()
   {
      return growSize;
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


   private String generateTagString(String value, String description,
      boolean selected)
   {
      StringBuffer tagBuf = new StringBuffer();
      tagBuf.append("<input type=\"radio\" name=\"");
      tagBuf.append(getFormFieldName());
      tagBuf.append("\" value=\"");
      tagBuf.append(value);
      tagBuf.append("\" ");

      if (selected)
      {
         tagBuf.append(" checked ");
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
      tagBuf.append(">\n");
      tagBuf.append(description);

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
      StringBuffer       tagBuf  = new StringBuffer();
      HttpServletRequest request = (HttpServletRequest) this.pageContext
         .getRequest();
      WebEvent           we      = (WebEvent) request.getAttribute("webEvent");

      // current Value from Database; or if no data: explicitly set by user; or ""
      String currentValue = getFormFieldValue();

      if (Util.isNull(currentValue))
      {
         currentValue = value;
      }

      if (embeddedData == null)
      { // no embedded data is nested in this tag

         // select, if datadriven and data matches with current value OR if explicitly set by user
         boolean isSelected = ((!getParentForm().getFooterReached()
            || we instanceof ReloadEvent) && (value != null)
            && value.equals(currentValue))
            || (getParentForm().getFooterReached() && "true".equals(checked));

         tagBuf.append(generateTagString(value, "", isSelected));

         if (!Util.isNull(getNoValue()))
         {
            tagBuf.append("<input type=\"hidden\" name=\"");
            tagBuf.append(getFormFieldName());
            tagBuf.append("\" value =\"");
            tagBuf.append(getNoValue());
            tagBuf.append("\" ");
            tagBuf.append("/>");
         }
      }
      else
      {
         int embeddedDataSize = embeddedData.size();

         // If radio is in read-only, retrieve selectedIndex and set the onclick of all radio with
         // "document.formName['radioName'][selectedIndex].checked=true"
         //
         if (getReadOnly().equals("true")
                  || getParentForm().getReadOnly().equals("true"))
         {
            // First pass to retreive radio selectedIndex, because in Javascript it use only this index (Netscape 4.x)
            for (int i = 0; i < embeddedDataSize; i++)
            {
               KeyValuePair aKeyValuePair = (KeyValuePair) embeddedData
                  .elementAt(i);
               String       aKey   = aKeyValuePair.getKey();

               if (aKey.equals(currentValue))
               {
                  String onclick = (getOnClick() != null) ? getOnClick() : "";

                  if (onclick.lastIndexOf(";") != (onclick.length() - 1))
                  {
                     onclick += ";"; // be sure javascript end with ";"
                  }

                  setOnClick("document.dbform['" + getFormFieldName() + "']["
                     + i + "].checked=true;" + onclick);

                  break;
               }
            }
         }

         int maxSize = Integer.parseInt(getGrowSize());

         tagBuf.append(
            "<TABLE BORDER=0 cellspacing=0 cellpadding=0><TR valign=top>");

         for (int i = 0; i < embeddedDataSize; i++)
         {
            KeyValuePair aKeyValuePair = (KeyValuePair) embeddedData.elementAt(i);
            String       aKey   = aKeyValuePair.getKey();
            String       aValue = aKeyValuePair.getValue();

            // select, if datadriven and data matches with current value OR if explicitly set by user
            boolean isSelected = aKey.equals(currentValue);

            if ("horizontal".equals(getGrowDirection()) && (maxSize != 0)
                     && ((i % maxSize) == 0) && (i != 0))
            {
               tagBuf.append("</TR><TR valign=top>");
            }

            if ("vertical".equals(getGrowDirection()) && (i != 0))
            {
               tagBuf.append("</TR><TR valign=top>");
            }

            tagBuf.append("<TD ");
            tagBuf.append(prepareStyles());
            tagBuf.append(">")
                  .append(generateTagString(aKey, aValue, isSelected)).append("</TD>");
         }

         tagBuf.append("</TR></TABLE>");
      }

      // For generation Javascript Validation.  Need all original and modified fields name
		getParentForm().addChildName(getFieldName(), getFormFieldName());

      try
      {
         pageContext.getOut().write(tagBuf.toString());
      }
      catch (java.io.IOException ioe)
      {
         throw new JspException("IO Error: " + ioe.getMessage());
      }

      return EVAL_PAGE;
   }


   /**
    * Returns the noValue.
    * @return String
    */
   public String getNoValue()
   {
      return noValue;
   }


   /**
    * Sets the noValue.
    * @param noValue The noValue to set
    */
   public void setNoValue(String noValue)
   {
      this.noValue = noValue;
   }
}
