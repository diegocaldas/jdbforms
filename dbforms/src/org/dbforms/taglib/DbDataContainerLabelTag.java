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
import org.dbforms.config.*;
import org.dbforms.util.*;
import org.CVS.*;
import org.apache.log4j.Category;



/****
 *
 * this tag renders a dabase-datadriven LABEL, which is apassive element (it can't be changed by
 * the user) - it is predestinated for use with read-only data (i.e. primary keys you don't want
 * the user to change, etc)
 *
 * so far it is equivalent to DbLabelTag. But this tag may have a body containing any kind of
 * EmbeddedData - tag.
 * i put this feature into a seperate class for performance reasons (we do not want the overhead
 * of pushing and poping the jsp writer to and off the stack
 *
 * @author Joachim Peer <j.peer@gmx.net>
 */
public class DbDataContainerLabelTag extends BodyTagSupport
   implements DataContainer
{
   static Category logCat = Category.getInstance(DbDataContainerLabelTag.class
         .getName());

   // logging category for this class
   private Vector        embeddedData   = null;
   private DbFormsConfig config;
   private String        fieldName;
   private Field         field;
   private DbFormTag     parentForm;
   private String        nullFieldValue = null;

   /**
   * PG, 2001-12-14
   * The maximum number of characters to be displayed.
   */
   private String maxlength = null;

   /**
    * DOCUMENT ME!
    *
    * @param fieldName DOCUMENT ME!
    */
   public void setFieldName(String fieldName)
   {
      this.fieldName    = fieldName;
      this.field        = parentForm.getTable().getFieldByName(fieldName);
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getFieldName()
   {
      return fieldName;
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
    * @param nullFieldValue DOCUMENT ME!
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
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getNullFieldValue()
   {
      return nullFieldValue;
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
      try
      {
         String fieldValue = "[no data]";

         // "fieldValue" is the variable actually printed out
         if (!Util.isNull(parentForm.getResultSetVector()))
         {
            //String[] currentRow = parentForm.getResultSetVector().getCurrentRow();
            //fieldValue = currentRow[field.getId()];
            Object fieldValueObj = parentForm.getResultSetVector()
                                             .getCurrentRowAsObjects()[field
               .getId()];

            if (fieldValueObj == null)
            {
               fieldValue = (nullFieldValue != null) ? nullFieldValue : "";
            }
            else
            {
               fieldValue = fieldValueObj.toString();
            }

            if (embeddedData != null)
            { //  embedded data is nested in this tag

               boolean found             = false;
               int     embeddedDataSize  = embeddedData.size();
               int     i                 = 0;
               String  embeddedDataValue = null;

               while (!found && (i < embeddedDataSize))
               {
                  KeyValuePair aKeyValuePair = (KeyValuePair) embeddedData
                     .elementAt(i);

                  if (aKeyValuePair.getKey().equals(fieldValue))
                  {
                     embeddedDataValue    = aKeyValuePair.getValue();
                     found                = true;
                  }

                  i++;
               }

               if (embeddedDataValue != null)
               {
                  fieldValue = embeddedDataValue;

                  // we'll print out embedded value associated with the current value
               }
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
         logCat.error(ioe);
         throw new JspException("IO Error: " + ioe.getMessage());
      }
      catch (Exception e)
      {
         logCat.error(e);
         throw new JspException("Error: " + e.getMessage());
      }

      return EVAL_PAGE;
   }


   /**
    * DOCUMENT ME!
    *
    * @param pageContext DOCUMENT ME!
    */
   public void setPageContext(final javax.servlet.jsp.PageContext pageContext)
   {
      super.setPageContext(pageContext);
      this.config = (DbFormsConfig) pageContext.getServletContext()
                                               .getAttribute(DbFormsConfig.CONFIG);
   }


   /**
    * DOCUMENT ME!
    *
    * @param parent DOCUMENT ME!
    */
   public void setParent(final javax.servlet.jsp.tagext.Tag parent)
   {
      super.setParent(parent);
      this.parentForm = (DbFormTag) findAncestorWithClass(this, DbFormTag.class);
   }


   /**
    * Gets the maxlength
    * @return Returns a String
    */
   public String getMaxlength()
   {
      return maxlength;
   }


   /**
    * Sets the maxlength
    * @param maxlength The maxlength to set
    */
   public void setMaxlength(String maxlength)
   {
      this.maxlength = maxlength;
   }
}
