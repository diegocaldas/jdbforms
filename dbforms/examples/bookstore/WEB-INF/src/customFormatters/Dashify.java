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
package customFormatters;

import org.dbforms.config.Field;

import org.dbforms.util.Formatter;

import java.util.Locale;

import javax.servlet.jsp.tagext.Tag;



/**
 * @author Neal Katz
 *
 * the fmtArg is a string. the string will be inserted between every digit in
 * the input when used any # is replaced by input. ex. string="--" ,
 * input="1234", output="1--2--3--4--5"
 */
public class Dashify implements Formatter {
   Locale locale  = null;
   String dashStr = null;

   /*
    * (non-Javadoc)
    *
    * @see org.dbforms.util.Formatter#setFormat(java.lang.String)
    */
   public void setFormat(String fmtArg) throws IllegalArgumentException {
      dashStr = fmtArg;
   }


   /*
    * (non-Javadoc) May be null
    *
    * @see org.dbforms.util.Formatter#setLocale(java.util.Locale)
    */
   public void setLocale(Locale locale) {
      this.locale = locale;
   }


   /*
    * (non-Javadoc)
    *
    * @see org.dbforms.util.Formatter#getLocale()
    */
   public Locale getLocale() {
      return locale;
   }


   /*
    * (non-Javadoc)
    *
    * @see org.dbforms.util.Formatter#sprintf(java.lang.Object[])
    */
   public String sprintf(Object[] o) {
      String s   = null;
      Field  f   = null;
      Tag    tag = null;

      if ((o.length >= 1) && (o[0] instanceof String)) {
         s = (String) o[0];
      }

      if ((o.length >= 2) && (o[1] instanceof Field)) {
         f = (Field) o[1];
      }

      if ((o.length >= 3) && (o[2] instanceof Tag)) {
         tag = (Tag) o[2];
      }

      String r = "";

      if (s != null) {
         for (int i = 0; i != s.length(); i++) {
            if (i > 0) {
               r += this.dashStr;
            }

            r += s.charAt(i);
         }
      }

      return r;
   }
}
