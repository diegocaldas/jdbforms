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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * <p>
 * This tag renders a HTML TextArea - Element
 * </p>
 * this tag renders a dabase-datadriven textArea, which is an active element -
 * the user can change data
 * 
 * @author Joachim Peer
 */
public class DbDateFieldTag extends AbstractDbBaseInputTag implements javax.servlet.jsp.tagext.TryCatchFinally {
   /** Holds value of property jsCalendarDateFormat. */
   private String jsCalendarDateFormat;

   /** Holds value of property useJsCalendar. */
   private String useJsCalendar;

   /** Determinate whether the classic javascript calendar should be used */
   private String classicCalendar = "true";

   /**
    * Setter for property jsCalendarDateFormat.
    * 
    * @param jsCalendarDateFormat
    *           New value of property jsCalendarDateFormat.
    */
   public void setJsCalendarDateFormat(String jsCalendarDateFormat) {
      this.jsCalendarDateFormat = jsCalendarDateFormat;
   }

   /**
    * Getter for property jsCalendarDateFormat.
    * 
    * @return Value of property jsCalendarDateFormat.
    */
   public String getJsCalendarDateFormat() {
      return jsCalendarDateFormat;
   }

   /**
    * Setter for property useJsCalendar.
    * 
    * @param useJsCalendar
    *           New value of property useJsCalendar.
    */
   public void setUseJsCalendar(String useJsCalendar) {
      this.useJsCalendar = useJsCalendar;
   }

   /**
    * Getter for property useJsCalendar.
    * 
    * @return Value of property useJsCalendar.
    */
   public String getUseJsCalendar() {
      return useJsCalendar;
   }

   private void processNewCalendar(HttpServletRequest request, StringBuffer tagBuf) throws JspException {
      if (!hasHiddenSet() && ("true".equals(this.getUseJsCalendar()))) {
         tagBuf.append(" <a href=\"javascript:void(0);\" ").append(" onclick=\"");
         // if date format is not explicitely set for this calendar,
         // use date format for this form field.
         if ((jsCalendarDateFormat == null) && (getFormatter() instanceof java.text.SimpleDateFormat)) {
            java.text.SimpleDateFormat mysdf = (java.text.SimpleDateFormat) getFormatter();
            jsCalendarDateFormat = parseDateFormatPattern(mysdf.toPattern());
         }
         tagBuf.append("return showCalendar('")
               .append(getFormFieldName())
               .append("', '")
               .append(jsCalendarDateFormat)
               .append("', '24', true);\">")
               .append("<img src=\"")
               .append(request.getContextPath())
               .append("/dbformslib/jscal2/calendar.gif\"  ") 
               .append(" border=\"0\"  alt=\"Click on the Calendar to activate the Pop-Up Calendar Window.\">")
               .append("</img>")
               .append("</a>");
      }
   }

   /**
    * process the display of the classic javascript calendar
    * 
    * @param request
    *           the request to process
    * @param tagBuf
    *           the buffer containing the rendered html nicolas parise
    *           2006-02-11
    */
   private void processClassicCalendar(HttpServletRequest request, StringBuffer tagBuf) throws JspException {
      // if property useJSCalendar is set to 'true' we will now add a little
      // image that can be clicked to popup a small JavaScript Calendar
      // written by Robert W. Husted to edit the field:
      if (!hasHiddenSet() && ("true".equals(this.getUseJsCalendar()))) {
         tagBuf.append(" <a href=\"javascript:doNothing()\" ").append(" onclick=\"");

         // if date format is not explicitely set for this calendar,
         // use date format for this form field.
         if ((jsCalendarDateFormat == null) && (getFormatter() instanceof java.text.SimpleDateFormat)) {
            java.text.SimpleDateFormat mysdf = (java.text.SimpleDateFormat) getFormatter();
            jsCalendarDateFormat = mysdf.toPattern();

            // 2 digit date format pattern is 'dd' in Java, 'DD' in
            // JavaScript calendar
            if (jsCalendarDateFormat.indexOf("dd") >= 0) {
               jsCalendarDateFormat = jsCalendarDateFormat.replace('d', 'D');
            }
         }
         //       JS Date Format set ?
         if (jsCalendarDateFormat != null)  {
            tagBuf.append("calDateFormat='" + jsCalendarDateFormat + "';");
         }

         tagBuf.append("setDateField(document.dbform['")
         .append(getFormFieldName())
         .append("']);")
         .append(" top.newWin = window.open('")
         .append(request.getContextPath())
         .append("/dbformslib/jscal/calendar.html','cal','width=270,height=280')\">")
         .append("<img src=\"")
         .append(request.getContextPath())
         .append("/dbformslib/jscal/calendar.gif\"  ") 
         .append(" border=\"0\"  alt=\"Click on the Calendar to activate the Pop-Up Calendar Window.\">")
         .append("</img>")
         .append("</a>");
      }

      // For generation Javascript Validation. Need all original and modified
      // fields name
      getParentForm().addChildName(getName(), getFormFieldName());
   }

   /**
    * DOCUMENT ME!
    * 
    * @return DOCUMENT ME!
    * 
    * @throws javax.servlet.jsp.JspException
    *            DOCUMENT ME!
    * @throws JspException
    *            DOCUMENT ME!
    */
   public int doEndTag() throws JspException {
      HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();

      try {

         StringBuffer tagBuf = new StringBuffer("<input ");
         tagBuf.append(prepareType());
         tagBuf.append(prepareName());
         tagBuf.append(prepareValue());
         tagBuf.append(prepareSize());
         tagBuf.append(prepareKeys());
         tagBuf.append(prepareStyles());
         tagBuf.append(prepareEventHandlers());
         tagBuf.append("/>");

         if ("true".equals(classicCalendar)) {
            processClassicCalendar(request, tagBuf);
         } else {
            processNewCalendar(request, tagBuf);

         }

         pageContext.getOut().write(tagBuf.toString());

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
      super.doFinally();
   }

   /**
    * DOCUMENT ME!
    * 
    * @return DOCUMENT ME!
    * 
    * @throws javax.servlet.jsp.JspException
    *            DOCUMENT ME!
    */
   public int doStartTag() throws JspException {
      super.doStartTag();

      return SKIP_BODY;
   }

   /**
    * Return the JavaScript Calendar pattern for the given Java DateFormat
    * pattern.
    * 
    * @author Malcolm A. Edgar
    * @param pattern
    *           the Java DateFormat pattern
    * @return JavaScript Calendar pattern
    */
   private String parseDateFormatPattern(String pattern) {
      StringBuffer jsPattern = new StringBuffer(20);
      int tokenStart = -1;
      int tokenEnd = -1;
      // boolean debug = false;

      for (int i = 0; i < pattern.length(); i++) {
         char aChar = pattern.charAt(i);

         // If character is in SimpleDateFormat pattern character set
         if ("GyMwWDdFEaHkKhmsSzZ".indexOf(aChar) == -1) {
            if (tokenStart > -1) {
               tokenEnd = i;
            }
         } else {
            if (tokenStart == -1) {
               tokenStart = i;
            }
         }

         if (tokenStart > -1) {

            if (tokenEnd == -1 && i == pattern.length() - 1) {
               tokenEnd = pattern.length();
            }

            if (tokenEnd > -1) {
               String token = pattern.substring(tokenStart, tokenEnd);

               if ("yyyy".equals(token)) {
                  jsPattern.append("%Y");
               } else if ("yy".equals(token)) {
                  jsPattern.append("%y");
               } else if ("MMMM".equals(token)) {
                  jsPattern.append("%B");
               } else if ("MMM".equals(token)) {
                  jsPattern.append("%b");
               } else if ("MM".equals(token)) {
                  jsPattern.append("%m");
               } else if ("dd".equals(token)) {
                  jsPattern.append("%d");
               } else if ("d".equals(token)) {
                  jsPattern.append("%e");
               } else if ("EEEE".equals(token)) {
                  jsPattern.append("%A");
               } else if ("EEE".equals(token)) {
                  jsPattern.append("%a");
               } else if ("EE".equals(token)) {
                  jsPattern.append("%a");
               } else if ("E".equals(token)) {
                  jsPattern.append("%a");
               } else if ("aaa".equals(token)) {
                  jsPattern.append("%p");
               } else if ("aa".equals(token)) {
                  jsPattern.append("%p");
               } else if ("a".equals(token)) {
                  jsPattern.append("%p");
               } else if ("HH".equals(token)) {
                  jsPattern.append("%H");
               } else if ("H".equals(token)) {
                  jsPattern.append("%H");
               } else if ("hh".equals(token)) {
                  jsPattern.append("%l");
               } else if ("h".equals(token)) {
                  jsPattern.append("%l");
               } else if ("mm".equals(token)) {
                  jsPattern.append("%M");
               } else if ("m".equals(token)) {
                  jsPattern.append("%M");
               } else if ("ss".equals(token)) {
                  jsPattern.append("%S");
               } else if ("s".equals(token)) {
                  jsPattern.append("%S");
               }

               tokenStart = -1;
               tokenEnd = -1;
            }
         }

         if (tokenStart == -1 && tokenEnd == -1) {
            if ("GyMwWDdFEaHkKhmsSzZ".indexOf(aChar) == -1) {
               jsPattern.append(aChar);
            }
         }
      }

      return jsPattern.toString();
   }

   public String getClassicCalendar() {
      return classicCalendar;
   }

   public void setClassicCalendar(String classicCalendar) {
      this.classicCalendar = classicCalendar;
   }
}
