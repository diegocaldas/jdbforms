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

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.config.Field;
import org.dbforms.config.FieldValue;
import org.dbforms.config.ResultSetVector;

import org.dbforms.util.Util;

import javax.servlet.jsp.JspException;



/**
 * Special class for TextFormatting
 *
 * @author hkk
 */
public class TextFormatTag extends DbBaseHandlerTag
   implements javax.servlet.jsp.tagext.TryCatchFinally {
   private static Log logCat      = LogFactory.getLog(TextFormatTag.class);
   private Object     fieldObject; // Holds the object to retrieve.
   private String contextVar;
   private String pattern;
   private String type;
   private String value;

   /**
    * DOCUMENT ME!
    *
    * @param contextVar DOCUMENT ME!
    */
   public void setContextVar(String contextVar) {
      this.contextVar = contextVar;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getContextVar() {
      return contextVar;
   }


   /**
    * DOCUMENT ME!
    *
    * @return
    */
   public Object getFieldObject() {
      return fieldObject;
   }


   /**
    * DOCUMENT ME!
    *
    * @param pattern The pattern to set.
    */
   public void setPattern(String pattern) {
      this.pattern = pattern;
   }


   /**
    * DOCUMENT ME!
    *
    * @return Returns the pattern.
    */
   public String getPattern() {
      return pattern;
   }


   /**
    * DOCUMENT ME!
    *
    * @param type The type to set.
    */
   public void setType(String type) {
      this.type = type;
   }


   /**
    * DOCUMENT ME!
    *
    * @return Returns the type.
    */
   public String getType() {
      return type;
   }


   /**
    * DOCUMENT ME!
    *
    * @param value The value to set.
    */
   public void setValue(String value) {
      this.value = value;
   }


   /**
    * DOCUMENT ME!
    *
    * @return Returns the value.
    */
   public String getValue() {
      return value;
   }


   /**
    * @see javax.servlet.jsp.tagext.TryCatchFinally#doCatch(java.lang.Throwable)
    */
   public void doCatch(Throwable t) throws Throwable {
      throw t;
   }


   /**
    * Description of the Method
    *
    * @return Description of the Return Value
    *
    * @exception javax.servlet.jsp.JspException Description of the Exception
    * @throws JspException DOCUMENT ME!
    */
   public int doEndTag() throws javax.servlet.jsp.JspException {
      if (Util.isNull(getContextVar()) && Util.isNull(getValue())) {
         throw new JspException("either var or value must be setted!");
      }

      Field field = new Field();

      if (!Util.isNull(getValue())) {
         if (Util.isNull(getType())) {
            throw new JspException("value setted - type must be setted  too!");
         }

         field.setFieldType(getType());

         FieldValue fv = new FieldValue(field, getValue());
         fv.setPattern(getPattern());
         fieldObject = fv.getFieldValueAsObject();
      } else {
         String          search = getContextVar();
         int             pos = search.indexOf(".");

         ResultSetVector rsv = null;

         if (getParentForm() != null) {
            rsv = getParentForm()
                     .getResultSetVector();
         }

         if (pos == -1) {
            if (rsv != null) {
               fieldObject = rsv.getAttribute(search);
            }

            if (fieldObject == null) {
               // simple type, 'search' is an object in the session
               fieldObject = pageContext.findAttribute(search);
            }
         } else {
            try {
               // complex, 'search' is really a bean
               String search_bean = search.substring(0, pos);
               search = search.substring(pos + 1);

               Object bean = null;

               if (rsv != null) {
                  bean = rsv.getAttribute(search_bean);
               }

               if (bean == null) {
                  // simple type, 'search' is an object in the session
                  bean = pageContext.findAttribute(search_bean);
               }

               if (bean != null) {
                  logCat.debug("calling PropertyUtils.getProperty "
                               + search_bean + " " + search);
                  fieldObject = PropertyUtils.getProperty(bean, search);
               }
            } catch (Exception e) {
               throw new JspException(e.getMessage());
            }
         }

         if (fieldObject == null) {
            throw new JspException("object not found in context!");
         }

         field.setTypeByObject(fieldObject);
      }

      this.setField(field);

      String fieldValue = getFormattedFieldValue();
      fieldValue = escapeHTML(fieldValue);

      try {
         pageContext.getOut()
                    .write(fieldValue);
      } catch (java.io.IOException ioe) {
         // better to KNOW what happended !
         throw new JspException("IO Error: " + ioe.getMessage());
      }

      return EVAL_PAGE;
   }


   /**
    * DOCUMENT ME!
    */
   public void doFinally() {
      pattern    = null;
      type       = null;
      value      = null;
      contextVar = null;
      super.doFinally();
   }


   /**
    * DOCUMENT ME!
    *
    * @param fieldObject DOCUMENT ME!
    */
   protected void setFieldObject(Object fieldObject) {
      this.fieldObject = fieldObject;
   }
}
