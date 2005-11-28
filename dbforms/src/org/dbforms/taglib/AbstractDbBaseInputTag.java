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

import org.dbforms.event.WebEvent;
import org.dbforms.event.eventtype.EventType;

import org.dbforms.util.Util;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;



/**
 * Abstract base class for the various input tags. original author Craig R.
 * McClanahan original author Don Clasen,
 *
 * @author Joachim Peer (modified this class for DbForms-Project)
 */
public abstract class AbstractDbBaseInputTag extends AbstractDbBaseHandlerTag {
   /**
    * The number of character columns for this field, or negative for no limit.
    */
   private String cols = null;

   /** DOCUMENT ME! */
   private java.lang.String hidden = "false";

   /** The maximum number of characters allowed, or negative for no limit. */
   private String maxlength = null;

   /** DOCUMENT ME! */
   private java.lang.String overrideValue;

   /** The number of rows for this field, or negative for no limit. */
   private String rows = null;

   /**
    * Set the number of columns for this field.
    *
    * @param cols The new number of columns
    */
   public void setCols(String cols) {
      this.cols = cols;
   }


   // ------------------------------------------------------------- Properties

   /**
    * Return the number of columns for this field.
    *
    * @return DOCUMENT ME!
    */
   public String getCols() {
      return (this.cols);
   }


   /**
    * Insert the method's description here. Creation date: (2001-06-26
    * 16:19:01)
    *
    * @param newHidden java.lang.String
    */
   public void setHidden(java.lang.String newHidden) {
      hidden = newHidden;
   }


   /**
    * Set the maximum length allowed.
    *
    * @param maxlength The new maximum length
    */
   public void setMaxlength(String maxlength) {
      this.maxlength = maxlength;
   }


   /**
    * Return the maximum length allowed.
    *
    * @return DOCUMENT ME!
    */
   public String getMaxlength() {
      return (this.maxlength);
   }


   /**
    * Insert the method's description here. Creation date: (2001-06-27
    * 17:44:16)
    *
    * @param newOverrideValue java.lang.String
    */
   public void setOverrideValue(java.lang.String newOverrideValue) {
      overrideValue = newOverrideValue;
   }


   /**
    * Insert the method's description here. Creation date: (2001-06-27
    * 17:44:16)
    *
    * @return java.lang.String
    */
   public java.lang.String getOverrideValue() {
      return overrideValue;
   }


   /**
    * Set the number of rows for this field.
    *
    * @param rows The new number of rows
    */
   public void setRows(String rows) {
      this.rows = rows;
   }


   /**
    * Return the number of rows for this field.
    *
    * @return DOCUMENT ME!
    */
   public String getRows() {
      return (this.rows);
   }


   /**
    * Set the size of this field (synonym for <code>setCols()</code>).
    *
    * @param size The new size
    */
   public void setSize(String size) {
      setCols(size);
   }


   /**
    * Return the size of this field (synonym for <code>getCols()</code>).
    *
    * @return DOCUMENT ME!
    */
   public String getSize() {
      return (getCols());
   }


   /**
    * Process the end of this tag.  The default implementation does nothing.
    *
    * @return DOCUMENT ME!
    *
    * @exception JspException if a JSP exception has occurred
    */
   public int doEndTag() throws JspException {
      return EVAL_PAGE;
   }


   /**
    * DOCUMENT ME!
    */
   public void doFinally() {
      cols      = null;
      maxlength = null;
      rows      = null;
      super.doFinally();
   }


   // --------------------------------------------------------- Public Methods

   /**
    * Process the start of this tag.  The default implementation does nothing.
    *
    * @return DOCUMENT ME!
    *
    * @exception JspException if a JSP exception has occurred
    */
   public int doStartTag() throws JspException {
      if (hasReadOnlySet() || getParentForm()
                                       .hasReadOnlySet()) {
         String onFocus = (getOnFocus() != null) ? getOnFocus()
                                                 : "";

         if (onFocus.lastIndexOf(";") != (onFocus.length() - 1)) {
            onFocus += ";"; // be sure javascript end with ";"
         }

         setOnFocus(onFocus + "this.blur();");
      }

      return EVAL_BODY_BUFFERED;
   }


   /**
    * Insert the method's description here. Creation date: (2001-06-26
    * 16:19:01)
    *
    * @return java.lang.String
    */
   public boolean hasHiddenSet() {
      return Util.getTrue(hidden);
   }


   /**
    * gets the formfield value
    *
    * @return String
    */
   protected String getFormFieldValue() {
      String res;

      /* If the overrideValue attribute has been set, use its value instead of the one
         retrieved from the database.  This mechanism can be used to set an initial default
         value for a given field. */
      HttpServletRequest request = (HttpServletRequest) this.pageContext
                                   .getRequest();
      Vector             errors = (Vector) request.getAttribute("errors");
      WebEvent           we     = getParentForm()
                                     .getWebEvent();

      if ((this.getOverrideValue() != null)
                && !((getParentForm()
                               .hasRedisplayFieldsOnErrorSet()
                && (errors != null) && (errors.size() > 0))
                || ((we != null)
                && (we.getType().equals(EventType.EVENT_NAVIGATION_RELOAD))))) {
         res = getOverrideValue();
      } else //If the redisplayFieldsOnError attribute is set and we are in error mode, forget override!
       {
         res = super.getFormFieldValue();
      }

      return res;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   protected String prepareKeys() {
      StringBuffer tagBuf = new StringBuffer();

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

      return tagBuf.toString();
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   protected String prepareName() {
      StringBuffer tagBuf = new StringBuffer();
      tagBuf.append("name=\"");
      tagBuf.append(getFormFieldName());
      tagBuf.append("\"");

      return tagBuf.toString();
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   protected String prepareSize() {
      StringBuffer tagBuf = new StringBuffer();

      if (getMaxlength() != null) {
         tagBuf.append(" maxlength=\"");
         tagBuf.append(getMaxlength());
         tagBuf.append("\"");
      }

      if (getCols() != null) {
         tagBuf.append(" size=\"");
         tagBuf.append(getCols());
         tagBuf.append("\"");
      }

      return tagBuf.toString();
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   protected String prepareType() {
      return hasHiddenSet() ? "type=\"hidden\" "
                            : "type=\"text\" ";
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   protected String prepareValue() {
      StringBuffer tagBuf = new StringBuffer();
      tagBuf.append(" value=\"");
      tagBuf.append(escapeHTML(getFormFieldValue()));
      tagBuf.append("\" ");

      return tagBuf.toString();
   }


   /**
    * writes out all hidden fields for the input fields
    */
   protected void writeOutSpecialValues() throws JspException {
      super.writeOutSpecialValues();

      try {
         pageContext.getOut()
                    .write(renderPatternHtmlInputField());
      } catch (java.io.IOException ioe) {
         throw new JspException("IO Error: " + ioe.getMessage());
      }
   }
}
