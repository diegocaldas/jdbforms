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
import java.text.Format;
import javax.servlet.jsp.*;
import javax.servlet.http.*;
import javax.servlet.jsp.tagext.*;
import org.dbforms.*;
import org.dbforms.config.*;
import org.dbforms.util.*;
import org.CVS.*;
import org.apache.log4j.Category;



/**
 * this tag renders a dabase-datadriven LABEL, which is apassive element (it can't be changed by
 * the user) - it is predestinated for use with read-only data (i.e. primary keys you don't want
 * the user to change, etc)
 *
 * @author  Joachim Peer <j.peer@gmx.net>
 * @created  29 agosto 2002
 */
public class DbLabelTag extends TagSupport
{
   // logging category for this class
   static Category logCat = Category.getInstance(DbLabelTag.class.getName());

   /**  Description of the Field */
   protected static final String NO_DATA = "[No Data]";

   /**  Description of the Field */
   protected DbFormsConfig config;

   /**  Description of the Field */
   protected String fieldName;

   /**  Description of the Field */
   protected Field field;

   /**  Description of the Field */
   protected String nullFieldValue;

   /**
    *  PG, 2001-12-14
    *  The maximum number of characters to be displayed.
    */
   protected String maxlength = null;

   /**  parent form pointer */
   protected DbFormTag parentForm;

   /** format object used to format this tag's value; */
   protected Format format = null;

   /**
    *  Sets the fieldName attribute of the DbLabelTag object
    *
    * @param  fieldName The new fieldName value
    */
   public void setFieldName(String fieldName)
   {
      this.fieldName    = fieldName;
      this.field        = parentForm.getTable().getFieldByName(fieldName);
   }


   /**
    *  Gets the fieldName attribute of the DbLabelTag object
    *
    * @return  The fieldName value
    */
   public String getFieldName()
   {
      return fieldName;
   }


   /**
    *  Sets the nullFieldValue attribute of the DbLabelTag object
    *
    * @param  nullFieldValue The new nullFieldValue value
    */
   public void setNullFieldValue(String nullFieldValue)
   {
      this.nullFieldValue = nullFieldValue;

      // Resolve message if captionResource=true in the Form Tag
      if (parentForm.getCaptionResource().equals("true"))
      {
         Locale locale = MessageResources.getLocale((HttpServletRequest) pageContext
               .getRequest());
         this.nullFieldValue = MessageResources.getMessage(nullFieldValue,
               locale, nullFieldValue);
      }
   }


   /**
    *  Gets the nullFieldValue attribute of the DbLabelTag object
    *
    * @return  The nullFieldValue value
    */
   public String getNullFieldValue()
   {
      return nullFieldValue;
   }


   /**
    *  Description of the Method
    *
    * @return  Description of the Return Value
    * @exception  javax.servlet.jsp.JspException Description of the Exception
    */
   public int doEndTag() throws javax.servlet.jsp.JspException
   {
      try
      {
         String fieldValue = (nullFieldValue != null) ? nullFieldValue : NO_DATA;

         if (!Util.isNull(parentForm.getResultSetVector()))
         {
            Object fieldValueObj = parentForm.getResultSetVector()
                                             .getCurrentRowAsObjects()[field
               .getId()];

            if (fieldValueObj == null)
            {
               fieldValue = (nullFieldValue != null) ? nullFieldValue : "";
            }
            else
            {
               // Fossato, 20002-08-29
               // uses the format class to format this tag's value;
               fieldValue = (format != null) ? format.format(fieldValueObj)
                                             : fieldValueObj.toString();
            }
         }

         // PG, 2001-12-14
         // If maxlength was input, trim display
         String size = null;

         if (((size = this.getMaxlength()) != null)
                  && (size.trim().length() > 0))
         {
            //convert to int
            int count = Integer.parseInt(size);

            // Trim and add trim indicator (...)
            if (count < fieldValue.length())
            {
               fieldValue = fieldValue.substring(0, count);
               fieldValue += "...";
            }
         }

         pageContext.getOut().write(fieldValue);
      }
      catch (java.io.IOException ioe)
      {
         // better to KNOW what happended !
         logCat.error("::doEndTag - IO Error", ioe);
         throw new JspException("IO Error: " + ioe.getMessage());
      }
      catch (Exception e)
      {
         // better to KNOW what happended !
         logCat.error("::doEndTag - general exception", e);
         throw new JspException("Error: " + e.getMessage());
      }

      return EVAL_PAGE;
   }


   /**
    *  Sets the pageContext attribute of the DbLabelTag object
    *
    * @param  pageContext The new pageContext value
    */
   public void setPageContext(final javax.servlet.jsp.PageContext pageContext)
   {
      super.setPageContext(pageContext);
      this.config = (DbFormsConfig) pageContext.getServletContext()
                                               .getAttribute(DbFormsConfig.CONFIG);
   }


   /**
    *  Sets the parent attribute of the DbLabelTag object
    *
    * @param  parent The new parent value
    */
   public void setParent(final javax.servlet.jsp.tagext.Tag parent)
   {
      super.setParent(parent);
      this.parentForm = (DbFormTag) findAncestorWithClass(this, DbFormTag.class);
   }


   /**
    * Gets the maxlength
    *
    * @return  Returns a String
    */
   public String getMaxlength()
   {
      return maxlength;
   }


   /**
    * Sets the maxlength
    *
    * @param  maxlength The maxlength to set
    */
   public void setMaxlength(String maxlength)
   {
      this.maxlength = maxlength;
   }


   /**
    *  Sets the format attribute of the DbLabelTag object
    *
    * @param  format The new format value
    */
   public void setFormat(Format format)
   {
      this.format = format;
   }


   /**
    *  Gets the format attribute of the DbLabelTag object
    *
    * @return  The format value
    */
   public Format getFormat()
   {
      return format;
   }
}
