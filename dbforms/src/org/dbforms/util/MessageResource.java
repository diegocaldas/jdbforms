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
package org.dbforms.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;



/**
 * base class for handling message resources
 *
 * @author Henner Kollmann
 */
public class MessageResource {
   /**
    * Use of HashMap for allowing null value (ReourceBundle) and avoiding to
    * call getBundle each time if resources file is not present.
    */
   private HashMap hashResources = new HashMap();
   private String  subClass = null;

   /**
    * Creates a new MessageResource object.
    *
    * @param subClass DOCUMENT ME!
    */
   public MessageResource(String subClass) {
      this.subClass = subClass;
   }

   /**
    * Retrieve message from ResourceBundle.  If the ResourceBundle is not yet
    * cached, cache it and retreive message.
    *
    * @param msgmsg </code> : Message key to lookup.
    * @param locloc </code> : Locale object to map message with good
    *        ResourceBundle.
    *
    * @return <code>String</code> : Message resolve, null if not found.
    */
   public String getMessage(String msg,
                            Locale loc) {
      if (subClass == null) {
         return null;
      }

      if (loc == null) {
         return null;
      }

      ResourceBundle rb = null;

      // Faster than String (immuable) concatenation
      String key = new StringBuffer().append(loc.getLanguage())
                                     .append("_")
                                     .append(loc.getCountry())
                                     .append("_")
                                     .append(loc.getVariant())
                                     .toString();

      if (hashResources.containsKey(key)) {
         rb = (ResourceBundle) hashResources.get(key);
      } else {
         try {
            rb = ResourceBundle.getBundle(subClass, loc);
         } catch (Exception e) {
            rb = null;
         }

         // Put the ResourceBundle or null value in HashMap with the key
         hashResources.put(key, rb);
      }

      String s = null;

      if (rb != null) {
         try {
            s = rb.getString(msg);
         } catch (Exception e) {
            s = null;
         }
      }

      return s;
   }


   /**
    * Retrieve message from ResourceBundle and replace parameter "{x}" with
    * values in parms array.
    *
    * @param msgmsg </code> : Message key to lookup.
    * @param locloc </code> : Locale object to map message with good
    *        ResourceBundle.
    * @param parmsparms[] </code> : Parameters to replace "{x}" in message .
    *
    * @return <code>String</code> : Message resolve with parameter replaced,
    *         null if message key not found.
    */
   public String getMessage(String   msg,
                            Locale   loc,
                            String[] parms) {
      String result = getMessage(msg, loc);

      if (result == null) {
         return null;
      }

      String search = null;

      for (int i = 0; i < parms.length; i++) {
         search = "{" + i + "}";
         result = replaceAll(result, search, parms[i]);
      }

      return result;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getSubClass() {
      return subClass;
   }


   /**
    * Replace all expression {...} by the appropriate string.
    *
    * @param strstr </code> : Original string.
    * @param searchsearch </code> : Expression to search.
    * @param replacereplace </code> : Replacement string.
    *
    * @return <code>String</code> : The string with all expression replaced.
    */
   private String replaceAll(String str,
                             String search,
                             String replace) {
      StringBuffer result = null;
      int          oldpos = 0;

      do {
         int pos = str.indexOf(search, oldpos);

         if (pos < 0) {
            break;
         }

         if (result == null) {
            result = new StringBuffer();
         }

         result.append(str.substring(oldpos, pos));

         result.append(replace);

         pos += search.length();
         oldpos = pos;
      } while (true);

      if (oldpos == 0) {
         return str;
      } else {
         result.append(str.substring(oldpos));

         return new String(result);
      }
   }
}
