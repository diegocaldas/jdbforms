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
import java.io.IOException;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import org.dbforms.event.ReloadEvent;
import org.dbforms.event.WebEvent;
import org.dbforms.util.ParseUtil;



/**
 * Abstract base class for the various input tags. original author Craig R.
 * McClanahan original author Don Clasen,
 * 
 * @author Joachim Peer (modified this class for DbForms-Project)
 */
public abstract class DbBaseInputTag extends DbBaseHandlerTag
{
   // logging category for this class
   // ----------------------------------------------------- Instance Variables

   /**
    * The number of character columns for this field, or negative for no limit.
    */
   protected String cols = null;

   /** The maximum number of characters allowed, or negative for no limit. */
   protected String maxlength = null;

   /** The number of rows for this field, or negative for no limit. */
   protected String rows = null;

   // ------------------------------------------------------------- Properties

   /**
    * Return the number of columns for this field.
    * 
    * @return DOCUMENT ME!
    */
   public String getCols()
   {
      return (this.cols);
   }


   /**
    * Set the number of columns for this field.
    * 
    * @param cols The new number of columns
    */
   public void setCols(String cols)
   {
      this.cols = cols;
   }


   /**
    * Return the maximum length allowed.
    * 
    * @return DOCUMENT ME!
    */
   public String getMaxlength()
   {
      return (this.maxlength);
   }


   /**
    * Set the maximum length allowed.
    * 
    * @param maxlength The new maximum length
    */
   public void setMaxlength(String maxlength)
   {
      this.maxlength = maxlength;
   }


   /**
    * Return the number of rows for this field.
    * 
    * @return DOCUMENT ME!
    */
   public String getRows()
   {
      return (this.rows);
   }


   /**
    * Set the number of rows for this field.
    * 
    * @param rows The new number of rows
    */
   public void setRows(String rows)
   {
      this.rows = rows;
   }


   /**
    * Return the size of this field (synonym for <code>getCols()</code>).
    * 
    * @return DOCUMENT ME!
    */
   public String getSize()
   {
      return (getCols());
   }


   /**
    * Set the size of this field (synonym for <code>setCols()</code>).
    * 
    * @param size The new size
    */
   public void setSize(String size)
   {
      setCols(size);
   }


   // --------------------------------------------------------- Public Methods

   /**
    * Process the start of this tag.  The default implementation does nothing.
    * 
    * @return DOCUMENT ME!
    * 
    * @exception JspException if a JSP exception has occurred
    */
   public int doStartTag() throws JspException
   {
      if (getReadOnly().equals("true")
                || parentForm.getReadOnly().equals("true"))
      {
         String onFocus = (getOnFocus() != null) ? getOnFocus() : "";

         if (onFocus.lastIndexOf(";") != (onFocus.length() - 1))
         {
            onFocus += ";"; // be sure javascript end with ";"
         }

         setOnFocus(onFocus + "this.blur();");
      }

      return EVAL_BODY_BUFFERED;
   }


   /**
    * Process the end of this tag.  The default implementation does nothing.
    * 
    * @return DOCUMENT ME!
    * 
    * @exception JspException if a JSP exception has occurred
    */
   public int doEndTag() throws JspException
   {
      return EVAL_PAGE;
   }

   /** DOCUMENT ME! */
   protected java.lang.String overrideValue;

   /**
    * Insert the method's description here. Creation date: (2001-06-27
    * 17:44:16)
    * 
    * @return java.lang.String
    */
   public java.lang.String getOverrideValue()
   {
      return overrideValue;
   }


   /**
    * Insert the method's description here. Creation date: (2001-06-27
    * 17:44:16)
    * 
    * @param newOverrideValue java.lang.String
    */
   public void setOverrideValue(java.lang.String newOverrideValue)
   {
      overrideValue = newOverrideValue;
   }

   /** DOCUMENT ME! */
   protected java.lang.String hidden = "false";

   /**
    * Insert the method's description here. Creation date: (2001-06-26
    * 16:19:01)
    * 
    * @return java.lang.String
    */
   public java.lang.String getHidden()
   {
      return hidden;
   }


   /**
    * Insert the method's description here. Creation date: (2001-06-26
    * 16:19:01)
    * 
    * @param newHidden java.lang.String
    */
   public void setHidden(java.lang.String newHidden)
   {
      hidden = newHidden;
   }


   /**
    * gets the formfield value
    * 
    * @return String
    */
   protected String getFormFieldValue()
   {
      String res;

      /* If the overrideValue attribute has been set, use its value instead of the one
      retrieved from the database.  This mechanism can be used to set an initial default
      value for a given field. */
      HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
      Vector             errors = (Vector) request.getAttribute("errors");
      WebEvent           we     = (WebEvent) request.getAttribute("webEvent");

      if (this.getOverrideValue() != null)
      {
         //If the redisplayFieldsOnError attribute is set and we are in error mode, forget override!
         if (("true".equals(parentForm.getRedisplayFieldsOnError())
                      && (errors != null) && (errors.size() > 0))
                   || (we instanceof ReloadEvent))
         {
            res = super.getFormFieldValue();
         }
         else
         {
            res = getOverrideValue();
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
               res = oldValue;
            }
            else
            {
               res = super.getFormFieldValue();
            }
         }
         else
         {
            res = super.getFormFieldValue();
         }
      }

      return res;
   }


   /**
    * generates the decoded name for the old value of the html-widget.
    * 
    * @return String
    */
   protected String getFormFieldNameOld()
   {
      return "o" + getFormFieldName();
   }


   /**
    * writes out the field value in hidden field _old
    */
   protected void writeOutOldValue() throws JspException
   {
      try
      {
	      StringBuffer tagBuf = new StringBuffer();
	      tagBuf.append("<input type=\"hidden\" name=\"");
	      tagBuf.append(getFormFieldNameOld());
			tagBuf.append("\" value=\"");
			tagBuf.append(getFormFieldValue());
			tagBuf.append("\" />");
			pageContext.getOut().write(tagBuf.toString());
      }	
		catch (java.io.IOException ioe)
		{
			throw new JspException("IO Error: " + ioe.getMessage());
		}

   }
}