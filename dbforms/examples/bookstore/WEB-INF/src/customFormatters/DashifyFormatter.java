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

import java.util.Locale;
import org.dbforms.util.ICustomFormat;



/**
 * @author Neal Katz
 *
 * the fmtArg is a string. the string will be inserted between every digit in
 * the input when used any # is replaced by input. ex. string="--" ,
 * input="1234", output="1--2--3--4--5"
 */
public class DashifyFormatter implements ICustomFormat {
   Locale locale  = null;
   String dashStr = null;

   public void setArg(String fmtArg) throws IllegalArgumentException {
      dashStr = fmtArg;
   }


   public void setLocale(Locale locale) {
      this.locale = locale;
   }


   public Locale getLocale() {
      return locale;
   }


   public String sprintf(String s) {
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
