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
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import org.dbforms.*;
import org.apache.log4j.Category;



/**
 * #fixme docu to come
 *
 * @author  Joe Peer
 * @created  06 August 2002
 */
public class DbBlobURLTag extends BodyTagSupport
{
   // logging category for this class
   static Category logCat = Category.getInstance(DbBlobURLTag.class.getName());

   /** DOCUMENT ME! */
   protected DbFormsConfig config;

   /** DOCUMENT ME! */
   protected String fieldName;

   /** DOCUMENT ME! */
   protected String defaultValue;

   /** DOCUMENT ME! */
   protected Field field;

   /** DOCUMENT ME! */
   protected DbFormTag parentForm;

   /**
    *  Gets the defaultValue attribute of the DbBlobURLTag object
    *
    * @return  The defaultValue value
    */
   public String getDefaultValue()
   {
      return defaultValue;
   }


   /**
    *  Sets the defaultValue attribute of the DbBlobURLTag object
    *
    * @param  defaultValue The new defaultValue value
    */
   public void setDefaultValue(String defaultValue)
   {
      this.defaultValue = defaultValue;
   }


   /**
    *  Sets the fieldName attribute of the DbBlobURLTag object
    *
    * @param  fieldName The new fieldName value
    */
   public void setFieldName(String fieldName)
   {
      this.fieldName    = fieldName;
      this.field        = parentForm.getTable().getFieldByName(fieldName);
   }


   /**
    *  Gets the fieldName attribute of the DbBlobURLTag object
    *
    * @return  The fieldName value
    */
   public String getFieldName()
   {
      return fieldName;
   }


   // --------------------------------------------------------- Public Methods
   // DbForms specific

   /**
    *  Sets the pageContext attribute of the DbBlobURLTag object
    *
    * @param  pageContext The new pageContext value
    */
   public void setPageContext(final javax.servlet.jsp.PageContext pageContext)
   {
      super.setPageContext(pageContext);
      config = (DbFormsConfig) pageContext.getServletContext().getAttribute(DbFormsConfig.CONFIG);
   }


   /**
    *  Sets the parent attribute of the DbBlobURLTag object
    *
    * @param  parent The new parent value
    */
   public void setParent(final javax.servlet.jsp.tagext.Tag parent)
   {
      super.setParent(parent);

      //parentForm = (DbFormTag) getParent().getParent(); // between this form and its parent lies a DbHeader/Body/Footer-Tag!
      parentForm = (DbFormTag) findAncestorWithClass(this, DbFormTag.class);
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
         StringBuffer tagBuf = new StringBuffer(((HttpServletRequest) pageContext
               .getRequest()).getContextPath());

         tagBuf.append("/servlet/file?tf=").append(getTableFieldCode())
               .append("&keyval=").append(getKeyVal());

         // append the defaultValue parameter;
         if (defaultValue != null)
         {
            tagBuf.append("&defaultValue=" + defaultValue);

            //logCat.info("::doEndTag - defaultValue set to [" + defaultValue + "]");
         }

         pageContext.getOut().write(tagBuf.toString());
      }
      catch (java.io.IOException ioe)
      {
         throw new JspException("IO Error: " + ioe.getMessage());
      }

      return EVAL_PAGE;
   }


   // ------------------------------------------------------ Protected Methods
   // DbForms specific

   /**
    *  Generates the decoded name.
    *
    * @return  The tableFieldCode value
    */
   protected String getTableFieldCode()
   {
      StringBuffer buf = new StringBuffer();

      buf.append(parentForm.getTable().getId());
      buf.append("_");
      buf.append(field.getId());

      return buf.toString();
   }


   /**
    *  Gets the keyVal attribute of the DbBlobURLTag object
    *
    * @return  The keyVal value
    */
   protected String getKeyVal()
   {
      return parentForm.getTable().getKeyPositionString(parentForm
         .getResultSetVector());
   }
}
