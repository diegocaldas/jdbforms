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
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.HashMap;
import java.util.Enumeration;
import org.apache.log4j.Category;
import javax.servlet.http.*;



/**
 * DOCUMENT ME!
 *
 * @version $Revision$
 * @author $author$
 */
public class MessageResources
{
   /** DOCUMENT ME! */
   public static String LOCALE_KEY = "org.dbforms.LOCALE";
   static Category      logCat = Category.getInstance(MessageResources.class
         .getName());

   /*********************************************************************************************
    *  Use of HashMap for allowing null value (ReourceBundle)
    *  and avoiding to call getBundle each time if resources file is not present.
    ********************************************************************************************/
   private static HashMap hashResources = new HashMap();
   private static String  subClass = null;

   /*********************************************************************************************
    *  Set the ResourceBundle subclass used by the getBundle() method
    ********************************************************************************************/
   public static void setSubClass(String res)
   {
      subClass = res;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public static String getSubClass()
   {
      return subClass;
   }


   /*********************************************************************************************
    *  Get the message from ResourceBundle.  If not present, return the defaultMsg at
    *  the place of a null.
    *
    ********************************************************************************************/
   public static String getMessage(HttpServletRequest request, String msg)
   {
      return getMessage(msg, getLocale(request), msg);
   }


   /*********************************************************************************************
    *  Get the message from ResourceBundle.  If not present, return the defaultMsg at
    *  the place of a null.  To avoid to doing this condition everywhere in the code ...
    *
    * @param  <code>String</code> : Message key to lookup.
    * @param  <code>Locale</code> : Locale object to map message with good ResourceBundle.
    * @param  <code>String</code> : String to return if the lookup message key is not found.
    *
   * @return        <code>String</code> : Message resolve.
    ********************************************************************************************/
   public static String getMessage(String msg, Locale locale, String defaultMsg)
   {
      String result = getMessage(msg, locale);

      if (result == null)
      {
         return defaultMsg;
      }

      return result;
   }


   /********************************************************************************************
    *  Retrieve message from ResourceBundle.  If the ResourceBundle is not yet cached,
    *  cache it and retreive message.
    *
    *         @param  <code>String</code> : Message key to lookup.
    *         @param  <code>Locale</code> : Locale object to map message with good ResourceBundle.
    *
    *         @return        <code>String</code> : Message resolve, null if not found.
    ********************************************************************************************/
   public static String getMessage(String msg, Locale loc)
   {
      if (subClass == null)
      {
         return null;
      }

      if (loc == null)
      {
         return null;
      }

      ResourceBundle rb = null;

      // Faster than String (immuable) concatenation
      String key = new StringBuffer().append(loc.getLanguage()).append("_")
                                     .append(loc.getCountry()).append("_")
                                     .append(loc.getVariant()).toString();

      if (hashResources.containsKey(key))
      {
         rb = (ResourceBundle) hashResources.get(key);
      }
      else
      {
         try
         {
            rb = ResourceBundle.getBundle(subClass, loc);
         }
         catch (Exception e)
         {
            ;
         }

         // Put the ResourceBundle or null value in HashMap with the key
         hashResources.put(key, rb);
      }

      try
      {
         String s = rb.getString(msg);

         return s;
      }
      catch (Exception e)
      {
         logCat.debug("not found: " + msg);

         return null;
      }
   }


   /*********************************************************************************************
    *  Retrieve message from ResourceBundle and replace parameter "{x}" with values in parms array.
    *
    *         @param  <code>String</code> : Message key to lookup.
    *         @param  <code>Locale</code> : Locale object to map message with good ResourceBundle.
    *         @param  <code>String[]</code> : Parameters to replace "{x}" in message .
    *
    *         @return        <code>String</code> : Message resolve with parameter replaced, null if message key not found.
    ********************************************************************************************/
   public static String getMessage(String msg, Locale loc, String[] parms)
   {
      String result = getMessage(msg, loc);

      if (result == null)
      {
         return null;
      }

      String search = null;

      for (int i = 0; i < parms.length; i++)
      {
         search    = "{" + i + "}";
         result    = MessageResources.replaceAll(result, search, parms[i]);
      }

      return result;
   }


   /*********************************************************************************************
   *  Replace all expression {...} by the appropriate string.
   *
   * @param  <code>String</code> : Original string.
   * @param  <code>String</code> : Expression to search.
   * @param  <code>String</code> : Replacement string.
   *
   * @return        <code>String</code> : The string with all expression replaced.
   ********************************************************************************************/
   public static String replaceAll(String str, String search, String replace)
   {
      StringBuffer result = null;
      int          oldpos = 0;

      do
      {
         int pos = str.indexOf(search, oldpos);

         if (pos < 0)
         {
            break;
         }

         if (result == null)
         {
            result = new StringBuffer();
         }

         result.append(str.substring(oldpos, pos));

         result.append(replace);

         pos += search.length();
         oldpos = pos;
      }
      while (true);

      if (oldpos == 0)
      {
         return str;
      }
      else
      {
         result.append(str.substring(oldpos));

         return new String(result);
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public static Locale getLocale(HttpServletRequest request)
   {
      if (request == null)
      {
         return null;
      }

      HttpSession session = request.getSession();

      if (session.getAttribute(MessageResources.LOCALE_KEY) == null)
      {
         session.setAttribute(MessageResources.LOCALE_KEY, request.getLocale());

         return request.getLocale();
      }
      else
      {
         return (Locale) session.getAttribute(MessageResources.LOCALE_KEY);
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param locale DOCUMENT ME!
    */
   public static void setLocale(HttpServletRequest request, Locale locale)
   {
      HttpSession session = request.getSession();
      session.setAttribute(MessageResources.LOCALE_KEY, locale);
   }
}
