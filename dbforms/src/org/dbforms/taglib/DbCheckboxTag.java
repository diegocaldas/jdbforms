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
import java.sql.*;
import java.io.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import javax.servlet.http.*;
import org.dbforms.*;
import org.dbforms.util.*;
import org.dbforms.event.ReloadEvent;
import org.dbforms.event.WebEvent;
import org.apache.log4j.Category;



/****
 *
 * <p>This tag renders a html CHECKBOX element or a whole group of them</p>
 *
 * @author Joachim Peer <j.peer@gmx.net>
 */
public class DbCheckboxTag extends DbBaseHandlerTag implements DataContainer
{
   static Category logCat        = Category.getInstance(DbCheckboxTag.class
         .getName()); // logging category for this class
   private Vector  embeddedData  = null;
   private String  checked; // only needed if parentForm is in "insert-mode", otherwise the DbForms-Framework determinates whether a checkbox should be selected or not.
   private String  growDirection; // only needed if we habe a whole "group" of DbRadioTags; default = null == horizontal
   private String  growSize      = "0"; // only needed if we habe a whole "group" of DbRadioTags; default = 1
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

      tagBuf.append("<input type=\"checkbox\" name=\"");
      tagBuf.append(getFormFieldName());
      tagBuf.append("\" value =\"");
      tagBuf.append(value);
      tagBuf.append("\" ");

      if (selected)
      {
         tagBuf.append(" checked ");
      }

      if (accessKey != null)
      {
         tagBuf.append(" accesskey=\"");
         tagBuf.append(accessKey);
         tagBuf.append("\"");
      }

      if (tabIndex != null)
      {
         tagBuf.append(" tabindex=\"");
         tagBuf.append(tabIndex);
         tagBuf.append("\"");
      }

      tagBuf.append(prepareStyles());
      tagBuf.append(prepareEventHandlers());
      tagBuf.append(">");
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

      // Because it can generate more than one checkbox, and to avoid wrong concatenation
      // ex: onclick="this.checked=true; this.checked=false; this.checked=true ..."
      String onclick = (getOnClick() != null) ? getOnClick() : "";

      if (onclick.lastIndexOf(";") != (onclick.length() - 1))
      {
         onclick += ";"; // be sure javascript end with ";"
      }

      // For generation Javascript Validation.  Need all original and modified fields name
      parentForm.addChildName(getFieldName(), getFormFieldName());

      if (embeddedData == null)
      { // no embedded data is nested in this tag

         // select, if datadriven and data matches with current value OR if explicitly set by user
         boolean isSelected = ((!parentForm.getFooterReached()
            || we instanceof ReloadEvent) && (value != null)
            && value.equals(currentValue))
            || (parentForm.getFooterReached() && "true".equals(checked));

         if (getReadOnly().equals("true")
                  || parentForm.getReadOnly().equals("true"))
         {
            setOnClick("this.checked=" + isSelected + ";" + onclick);
         }

         tagBuf.append(generateTagString(value, "", isSelected));

         if (!Util.isNull(getNovalue()))
         {
            tagBuf.append("<input type=\"hidden\" name=\"");
            tagBuf.append(getFormFieldName());
            tagBuf.append("\" value =\"");
            tagBuf.append(getNovalue());
            tagBuf.append("\" ");
            tagBuf.append("/>");
         }
      }
      else
      {
         int embeddedDataSize = embeddedData.size();
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

            if (getReadOnly().equals("true")
                     || parentForm.getReadOnly().equals("true"))
            {
               setOnClick("this.checked=" + isSelected + ";" + onclick);
            }

            if ("horizontal".equals(getGrowDirection()) && (maxSize != 0)
                     && ((i % maxSize) == 0) && (i != 0))
            {
               tagBuf.append("</TR><TR valign=top>");
            }

            if ("vertical".equals(getGrowDirection()) && (i != 0))
            {
               tagBuf.append("</TR><TR valign=top>");
            }

            tagBuf.append("<TD>")
                  .append(generateTagString(aKey, aValue, isSelected)).append("&nbsp;</TD>");
         }

         tagBuf.append("</TR></TABLE>");
      }

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
   public String getNovalue()
   {
      return noValue;
   }


   /**
    * Sets the noValue.
    * @param noValue The noValue to set
    */
   public void setNovalue(String noValue)
   {
      this.noValue = noValue;
   }
}
