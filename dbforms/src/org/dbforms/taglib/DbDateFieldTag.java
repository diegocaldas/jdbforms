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
import java.text.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import org.dbforms.util.ParseUtil;
import org.dbforms.config.*;
import org.dbforms.event.ReloadEvent;
import org.dbforms.event.WebEvent;
import org.apache.log4j.Category;
import javax.servlet.http.*;



/****
 *
 * <p>This tag renders a HTML TextArea - Element</p>
 *
 * this tag renders a dabase-datadriven textArea, which is an active element - the user
 * can change data
 *
 * @author Joachim Peer <j.peer@gmx.net>
 */
public class DbDateFieldTag extends DbBaseInputTag
{
   static Category logCat = Category.getInstance(DbDateFieldTag.class.getName());

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws javax.servlet.jsp.JspException DOCUMENT ME!
    */
   public int doStartTag() throws JspException
   {
      super.doStartTag();

      // Use format defined in config file
      if (this.format == null)
      {
         try {
				this.format = DbFormsConfigRegistry.instance().lookup().getDateFormatter();
         } catch (Exception e) {
            throw new JspException(e.getMessage());
         }
         
      }

      return SKIP_BODY;
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
      HttpServletRequest request = (HttpServletRequest) this.pageContext
         .getRequest();
      Vector             errors = (Vector) request.getAttribute("errors");
      WebEvent           we     = (WebEvent) request.getAttribute("webEvent");

      try
      {
         /* Does the developer require the field to be hidden or displayed? */
         String       value = ("true".equals(this.getHidden()))
            ? "<input type=\"hidden\" name=\"" : "<input type=\"text\" name=\"";

         StringBuffer tagBuf = new StringBuffer(value);
         tagBuf.append(getFormFieldName());
         tagBuf.append("\" value=\"");

         /* If the overrideValue attribute has been set, use its value instead of the one
         retrieved from the database.  This mechanism can be used to set an initial default
         value for a given field. */
         if (this.getOverrideValue() != null)
         {
            //If the redisplayFieldsOnError attribute is set and we are in error mode, forget override!
            if (("true".equals(parentForm.getRedisplayFieldsOnError())
                     && (errors != null) && (errors.size() > 0))
                     || (we instanceof ReloadEvent))
            {
               tagBuf.append(getFormFieldValue());
            }
            else
            {
               tagBuf.append(this.getOverrideValue());
            }
         }
         else
         {
            if (we instanceof ReloadEvent)
            {
               String oldValue = ParseUtil.getParameter(request,
                     getFormFieldName());

               if (oldValue != null)
               {
                  tagBuf.append(oldValue);
               }
               else
               {
                  tagBuf.append(getFormFieldValue());
               }
            }
            else
            {
               tagBuf.append(getFormFieldValue());
            }
         }

         tagBuf.append("\" ");

         if (accessKey != null)
         {
            tagBuf.append(" accesskey=\"");
            tagBuf.append(accessKey);
            tagBuf.append("\"");
         }

         if (maxlength != null)
         {
            tagBuf.append(" maxlength=\"");
            tagBuf.append(maxlength);
            tagBuf.append("\"");
         }

         if (cols != null)
         {
            tagBuf.append(" size=\"");
            tagBuf.append(cols);
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
         tagBuf.append("/>");

         // if property useJSCalendar is set to 'true' we will now add a little
         // image that can be clicked to popup a small JavaScript Calendar
         // written by Robert W. Husted to edit the field:
         if (!("true".equals(this.getHidden()))
                  && ("true".equals(this.getUseJsCalendar())))
         {
            tagBuf.append(" <A HREF=\"javascript:doNothing()\" ").append(" onclick=\"");

            // if date format is not explicitely set for this calendar,
            // use date format for this form field. 
            if ((jsCalendarDateFormat == null)
                     && (this.format instanceof java.text.SimpleDateFormat))
            {
               java.text.SimpleDateFormat mysdf = (java.text.SimpleDateFormat) format;
               jsCalendarDateFormat = mysdf.toPattern();

               // 2 digit date format pattern is 'dd' in Java, 'DD' in
               // JavaScript calendar
               if (jsCalendarDateFormat.indexOf("dd") >= 0)
               {
                  jsCalendarDateFormat = jsCalendarDateFormat.replace('d', 'D');
               }
            }

            if (jsCalendarDateFormat != null) // JS Date Format set ?
            {
               tagBuf.append("calDateFormat='" + jsCalendarDateFormat + "';");
            }

            tagBuf.append("setDateField(document.dbform['")
                  .append(getFormFieldName()).append("']);")
                  .append(" top.newWin = window.open('")
                  .append(request.getContextPath())
                  .append("/dbformslib/jscal/calendar.html','cal','WIDTH=270,HEIGHT=280')\">")
                  .append("<IMG SRC=\"").append(request.getContextPath())
                  .append("/dbformslib/jscal/calendar.gif\" WIDTH=\"32\" HEIGHT=\"32\" ")
                  .append(" BORDER=0  alt=\"Click on the Calendar to activate the Pop-Up Calendar Window.\">")
                  .append("</a>");
         }

         // For generation Javascript Validation.  Need all original and modified fields name
         parentForm.addChildName(getFieldName(), getFormFieldName());

         pageContext.getOut().write(tagBuf.toString());
      }
      catch (java.io.IOException ioe)
      {
         throw new JspException("IO Error: " + ioe.getMessage());
      }

      return EVAL_PAGE;
   }

   private java.lang.String hidden        = "false";
   private java.lang.String overrideValue;

   /** Holds value of property useJsCalendar. */
   private String useJsCalendar;

   /** Holds value of property jsCalendarDateFormat. */
   private String jsCalendarDateFormat;

   /**
    * Insert the method's description here.
    * Creation date: (2001-06-26 16:19:01)
    * @return java.lang.String
    */
   public java.lang.String getHidden()
   {
      return hidden;
   }


   /**
    * Insert the method's description here.
    * Creation date: (2001-06-26 16:19:25)
    * @return java.lang.String
    */
   public java.lang.String getOverrideValue()
   {
      return overrideValue;
   }


   /**
    * Insert the method's description here.
    * Creation date: (2001-06-26 16:19:01)
    * @param newHidden java.lang.String
    */
   public void setHidden(java.lang.String newHidden)
   {
      hidden = newHidden;
   }


   /**
    * Insert the method's description here.
    * Creation date: (2001-06-26 16:19:25)
    * @param newOverrideValue java.lang.String
    */
   public void setOverrideValue(java.lang.String newOverrideValue)
   {
      overrideValue = newOverrideValue;
   }


   /** Getter for property useJsCalendar.
    * @return Value of property useJsCalendar.
    */
   public String getUseJsCalendar()
   {
      return useJsCalendar;
   }


   /** Setter for property useJsCalendar.
    * @param useJsCalendar New value of property useJsCalendar.
    */
   public void setUseJsCalendar(String useJsCalendar)
   {
      this.useJsCalendar = useJsCalendar;
   }


   /** Getter for property jsCalendarDateFormat.
    * @return Value of property jsCalendarDateFormat.
    */
   public String getJsCalendarDateFormat()
   {
      return jsCalendarDateFormat;
   }


   /** Setter for property jsCalendarDateFormat.
    * @param jsCalendarDateFormat New value of property jsCalendarDateFormat.
    */
   public void setJsCalendarDateFormat(String jsCalendarDateFormat)
   {
      this.jsCalendarDateFormat = jsCalendarDateFormat;
   }
}
