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
import java.net.URLEncoder;
import java.net.URLDecoder;
import org.dbforms.util.external.PrintfFormat;
import org.dbforms.config.DbFormsConfig;
import java.io.UnsupportedEncodingException;
import java.lang.NoSuchMethodException;



/**
 * Simple general utility class
 */
public class Util
{
   private static final String REALPATH = "$(SERVLETCONTEXT_REALPATH)";

   /**
    * Test if the input string is null or empty (does not contain any
    * character)
    * 
    * @param s the string value to test
    * 
    * @return true if the input string is null or empty, false otherwise
    */
   public static final boolean isNull(String s)
   {
      return ((s == null) || (s.trim().length() == 0));
   }


   /**
    * DOCUMENT ME!
    * 
    * @param rsv DOCUMENT ME!
    * 
    * @return DOCUMENT ME!
    */
   public static final boolean isNull(ResultSetVector rsv)
   {
      return ((rsv == null) || (rsv.size() == 0));
   }


   /**
    * Replaces the occurens from REALPATH in s with realpath.
    * 
    * @param s        the string containing the REALPATH token
    * @param realpath the value used to replace the REALPATH token
    * 
    * @return the input string, with the REALPATH token replaced with the
    *         realpath value
    */
   public static final String replaceRealPath(String s, String realpath)
   {
      if (!isNull(realpath))
      {
         // 20030604-HKK: Bugfixing for different engine, e.g. cactus. Path maybe without trailing '/'!!!
         if (realpath.charAt(realpath.length() - 1) != '/')
         {
            realpath = realpath + '/';
         }

         int i = s.indexOf(REALPATH);

         if (i >= 0)
         {
            StringBuffer buf = new StringBuffer();
            buf.append(s.substring(0, i));
            buf.append(realpath);
            buf.append(s.substring(i + REALPATH.length() + 1));
            s = buf.toString();
         }
      }

      return s;
   }


   /**
    * Replaces the occurens from REALPATH in s with config.getRealPath().
    * 
    * @param s       the string containing the REALPATH token
    * @param config  the config object
    * 
    * @return the input string, with the REALPATH token replaced with the
    *         realpath value taken from the config object
    */
   public static final String replaceRealPath(String s, DbFormsConfig config)
   {
      return replaceRealPath(s, config.getRealPath());
   }


   /**
    * Encode a string with desired character encoding to "ISO-8859-1"
    * 
    * @param s the string to encode
    * 
    * @return the encoded string
    * @throws UnsupportedEncodingException DOCUMENT ME!
    */
   public static final String encode(String s)
                              throws UnsupportedEncodingException
   {
      return encode(s, null);
   }


   /**
    * Encode a string with desired character encoding.  defaults to "ISO8859-1"
    * if param enc is null or jdk 1.3 is used
    * 
    * @param s the string to encode
    * @param enc the desired encoding
    * 
    * @return the encoded string
    * @throws UnsupportedEncodingException DOCUMENT ME!
    */
   public static final String encode(String s, String enc)
                              throws UnsupportedEncodingException
   {
      if (!Util.isNull(s))
      {
         try
         {
            s = Util.encCheck(s, enc);
         }
         catch (NoSuchMethodException nsme)
         {
            s = URLEncoder.encode(s);
         }
      }

      return s;
   }


   /**
    * needed by Util.encode(s,enc) to check if URLEncoder.encode(String s,
    * String enc) is available (from jdk 1.4)
    * 
    * @param s the string to encode
    * @param enc the desired encoding
    * 
    * @return the encoded string
    * 
    * @throws UnsupportedEncodingException DOCUMENT ME!
    * @throws NoSuchMethodException to signal that jdk 1.3 is being used
    */
   private static final String encCheck(String s, String enc)
                                 throws UnsupportedEncodingException, 
                                        NoSuchMethodException
   {
      if (Util.isNull(enc))
      {
         enc = "ISO-8859-1";
      }

      s = URLEncoder.encode(s, enc);

      return s;
   }


   /**
    * returns a formated string
    * 
    * @param format
    * @param o
    * 
    * @return String
    */
   public static String sprintf(String format, Object[] o)
   {
      PrintfFormat printfFormat = new PrintfFormat(format); // create instance of PrintfFormat class

      return printfFormat.sprintf(o);
   }


   /**
    * Decodes a string with "ISO8859-1". This is the default in the servlet
    * engine (tomcat); hope that's the same in the other ones...
    * 
    * @param s the string to encode
    * 
    * @return the encoded string
    */
   public static final String decode(String s)
   {
      if (!Util.isNull(s))
      {
         s = URLDecoder.decode(s);
      }

      return s;
   }


   /**
    * Dump the fieldValue objects contained into the input FieldValue array.
    * 
    * @param fv the FieldValue array to dump
    * 
    * @return the String object containing the dumped data, or null if the
    *         input array is null
    */
   public static final String dumpFieldValueArray(FieldValue[] fv)
   {
      String s = null;

      if (fv != null)
      {
         StringBuffer sb = new StringBuffer();

         for (int i = 0; i < fv.length; i++)
         {
            FieldValue f = fv[i];
            sb.append("  fv[").append(i).append("] = {").append(f.toString())
              .append("}\n");
         }

         s = sb.toString();
      }

      return s;
   }
}