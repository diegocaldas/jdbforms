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

import org.dbforms.config.Field;
import org.dbforms.config.FieldValue;

import javax.servlet.jsp.JspException;



/**
 * Special class for TextFormatting
 *
 * @author hkk
 */
public class TextFormatTag extends DbBaseHandlerTag {
   private Object obj;
   private String pattern;
   private String type;
   private String value;

   /**
    * @param pattern
    *            The pattern to set.
    */
   public void setPattern(String pattern) {
      this.pattern = pattern;
   }


   /**
    * @return Returns the pattern.
    */
   public String getPattern() {
      return pattern;
   }


   /**
    * @param type The type to set.
    */
   public void setType(String type) {
      this.type = type;
   }


   /**
    * @return Returns the type.
    */
   public String getType() {
      return type;
   }


   /**
    * @param value
    *            The value to set.
    */
   public void setValue(String value) {
      this.value = value;
   }


   /**
    * @return Returns the value.
    */
   public String getValue() {
      return value;
   }


   /**
    * Description of the Method
    *     * @return Description of the Return Value
    *
    * @exception javax.servlet.jsp.JspException
    *                Description of the Exception
    */
   public int doEndTag() throws javax.servlet.jsp.JspException {
      Field field = new Field();
      field.setFieldType(getType());

      FieldValue fv = new FieldValue(field, getValue());
      fv.setPattern(getPattern());
      obj = fv.getFieldValueAsObject();
      setField(field);

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
    *
    * @return DOCUMENT ME!
    */
   protected Object getFieldObject() {
      return obj;
   }
}
