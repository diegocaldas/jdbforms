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

import java.util.Locale;



/**
 * Special class for TextFormatting
 *
 * @author hkk
 */
public class TextFormatter extends DbBaseInputTag {
   private Locale locale;
   private Object obj;

   /**
    * Creates a new TextFormatter object.
    *
    * @param field DOCUMENT ME!
    * @param locale DOCUMENT ME!
    * @param pattern DOCUMENT ME!
    * @param obj DOCUMENT ME!
    */
   public TextFormatter(Field  field,
                        Locale locale,
                        String pattern,
                        Object obj) {
      this.obj    = obj;
      this.locale = locale;
      setPattern(pattern);
      setField(field);
   }

   /**
    * DOCUMENT ME!
    *
    * @param parentForm DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getFormFieldName(DbFormTag parentForm) {
      setParent(parentForm);

      return super.getFormFieldName();
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getFormattedFieldValue() {
      return super.getFormattedFieldValue();
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String renderPatternHtmlInputField() {
      return super.renderPatternHtmlInputField();
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   protected Object getFieldObject() {
      return obj;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   protected Locale getLocale() {
      return locale;
   }
}
