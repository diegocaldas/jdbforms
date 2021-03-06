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

import org.dbforms.util.external.PrintfFormat;

import java.io.UnsupportedEncodingException;

import java.net.URLDecoder;
import java.net.URLEncoder;

import java.text.Format;

import javax.servlet.http.HttpServletRequest;



/**
 * Simple general utility class
 */
public class Util {
   /**
    * Test if the input string is null or empty (does not contain any
    * character)
    *
    * @param s
    *            the string value to test
    *
    * @return true if the input string is null or empty, false otherwise
    */
   public static final boolean isNull(String s) {
      return ((s == null) || (s.trim()
                               .length() == 0));
   }


   /**
    * Decodes a string
    *
    * @param s
    *            the string to encode
    *
    * @return the encoded string
    */
   public static final String decode(String s,
                                     String enc)
                              throws UnsupportedEncodingException {
      if (!Util.isNull(s)) {
         try {
            s = decCheck(s, enc);
         } catch (NoSuchMethodError nsme) {
            s = URLDecoder.decode(s);
         }
      }

      return s;
   }


   /**
    * Encode a string with desired character encoding.
    *
    * @param s
    *            the string to encode
    * @param enc
    *            the desired encoding
    *
    * @return the encoded string
    * @throws UnsupportedEncodingException
    *             DOCUMENT ME!
    */
   public static final String encode(String s,
                                     String enc)
                              throws UnsupportedEncodingException {
      if (!Util.isNull(s)) {
         try {
            s = encCheck(s, enc);
         } catch (NoSuchMethodError nsme) {
            s = URLEncoder.encode(s);
         }
      }

      return s;
   }


   /**
    * DOCUMENT ME!
    *
    * @param value DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public static boolean getFalse(String value) {
      return !Util.getTrue(value);
   }


   /**
    * DOCUMENT ME!
    *
    * @param f DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public static String getPattern(Format f) {
      if (f instanceof java.text.DecimalFormat) {
         return ((java.text.DecimalFormat) f).toPattern();
      } else if (f instanceof java.text.SimpleDateFormat) {
         return ((java.text.SimpleDateFormat) f).toPattern();
      } else {
         return null;
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param value DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public static boolean getTrue(String value) {
      return "true".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value);
   }


   /**
    * returns a formated string
    *
    * @param format
    *            format string
    * @param o
    *            objects to use to format
    *
    * @return String
    */
   public static String sprintf(String   format,
                                Object[] o) {
      PrintfFormat printfFormat = new PrintfFormat(format); // create

      // instance of
      // PrintfFormat
      // class
      return printfFormat.sprintf(o);
   }


   /**
    * needed by Util.encode(s,enc) to check if URLEncoder.encode(String s,
    * String enc) is available (from jdk 1.4)
    *
    * @param s
    *            the string to encode
    * @param enc
    *            the desired encoding
    *
    * @return the encoded string
    *
    * @throws UnsupportedEncodingException
    *             DOCUMENT ME!
    * @throws NoSuchMethodException
    *             to signal that jdk 1.3 is being used
    */
   private static final String decCheck(String s,
                                        String enc)
                                 throws UnsupportedEncodingException, 
                                        NoSuchMethodError {
      if (isNull(enc)) {
         enc = "UTF-8";
      }

      return URLDecoder.decode(s, enc);
   }


   /**
    * needed by Util.encode(s,enc) to check if URLEncoder.encode(String s,
    * String enc) is available (from jdk 1.4)
    *
    * @param s
    *            the string to encode
    * @param enc
    *            the desired encoding
    *
    * @return the encoded string
    *
    * @throws UnsupportedEncodingException
    *             DOCUMENT ME!
    * @throws NoSuchMethodException
    *             to signal that jdk 1.3 is being used
    */
   private static final String encCheck(String s,
                                        String enc)
                                 throws UnsupportedEncodingException, 
                                        NoSuchMethodError {
      if (isNull(enc)) {
         enc = "UTF-8";
      }

      s = URLEncoder.encode(s, enc);

      return s;
   }

	public static String getBaseURL(HttpServletRequest request) {
		StringBuffer buf = new StringBuffer();
		buf.append(request.getScheme());
		buf.append("://");
		buf.append(request.getServerName());

		int port = request.getServerPort();
		if((port!=80) && (port !=443)) {
			buf.append(":");
			buf.append(String.valueOf(port));
		}
		buf.append(request.getContextPath());
		if (buf.charAt(buf.length() - 1) != '/') { 
		   buf.append("/");
		}
		return buf.toString();
	}

	public static String getRequestURL(HttpServletRequest request) {
		StringBuffer buf = new StringBuffer();
		buf.append(request.getScheme());
		buf.append("://");
		buf.append(request.getServerName());
		int port = request.getServerPort();
		if ((port != 80) && (port != 443)) {
			buf.append(":");
			buf.append(port);
		}
		buf.append(request.getRequestURI());
		String query = request.getQueryString();
		if (!isNull(query)) {
			buf.append("?");
			buf.append(query);
		}
		return buf.toString();
	}

}
