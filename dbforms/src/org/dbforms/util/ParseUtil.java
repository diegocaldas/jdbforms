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

import java.io.InputStream;

import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;



/**
 * This utility-class provides convenience methods for parsing strings and
 * generating certain data structures
 *
 * @author Joe Peer
 */
public class ParseUtil {
   /**
    * Method for parsing substring embedded by constant delimeters.
    * <br>
    * consider the following string s: ac_update_3_12
    * <br>
    * <pre>
    *  getEmbeddedString(s, 0, '_') ==> "ac"
    *  getEmbeddedString(s, 1, '_') ==> "update"
    *  getEmbeddedString(s, 2, '_') ==> "3"
    *  getEmbeddedString(s, 3, '_') ==> "12"
    *  getEmbeddedString(s, 3, '_') ==> will throw a Runtime Exception
    * </pre>
    *
    * @param str the string to parse
    * @param afterDelims the delimiter occurence where to start to parse
    * @param delim the delimiter string
    * @return the substring contained between the <code>afterDelims</code> delimiter occurence
    *         and the next one
    */
   public static String getEmbeddedString(String str,
                                          int    afterDelims,
                                          char   delim) {
      int lastIndex = 0;

      for (int i = 0; i < afterDelims; i++) {
         lastIndex = str.indexOf(delim, lastIndex) + 1; // search end of cutting
      }

      int nextIndex = str.indexOf(delim, lastIndex); // end of cutting

      if (nextIndex == -1) {
         // new: 18.1.2001: support for IMAGE buttons #checkme: can we swith this off?
         int dotIndex = str.lastIndexOf('.');
         nextIndex = (dotIndex == -1) ? str.length()
                                      : dotIndex;
      }

      return str.substring(lastIndex, nextIndex);
   }


   /**
    *  Get the int value of the substring contained between
    *  the <code>afterDelims</code> delimiter occurence and the next one
    *
    * @param str the string to parse
    * @param afterDelims the delimiter occurence where to start to parse
    * @param delim the delimiter string
    * @return the int value of the substring contained between
    *         the <code>afterDelims</code> delimiter occurence and the next one
    */
   public static int getEmbeddedStringAsInteger(String str,
                                                int    afterDelims,
                                                char   delim) {
      return Integer.parseInt(getEmbeddedString(str, afterDelims, delim));
   }


   /**
    * Method for parsing substring embedded by constant delimeters
    * <br>
    * Because getEmbeddedString() support "." for image button, this method do
    * the the same, but ignore dots. It's a patch and must be revised in the
    * next cleanup... #checkme
    * <br>
    * consider the following string s: English-001:param1, param2
    *
    * <pre>
    *  getEmbeddedString(s, 0, '_') ==> ""
    *  getEmbeddedString(s, 1, '_') ==> ""
    *  getEmbeddedString(s, 2, '_') ==> ""
    *  getEmbeddedString(s, 3, '_') ==> ""
    *  getEmbeddedString(s, 3, '_') ==> will throw a Runtime Exception
    * </pre>
    *
    * @param str the string to parse
    * @param afterDelims the delimiter occurence where to start to parse
    * @param delim the delimiter string
    *
    * @return the substring contained between the <code>afterDelims</code> delimiter occurence
    *         and the next one
    */
   public static String getEmbeddedStringWithoutDots(String str,
                                                     int    afterDelims,
                                                     char   delim) {
      int lastIndex = 0;

      for (int i = 0; i < afterDelims; i++) {
         lastIndex = str.indexOf(delim, lastIndex) + 1; // search end of cutting
      }

      int nextIndex = str.indexOf(delim, lastIndex); // end of cutting

      if (nextIndex == -1) {
         nextIndex = str.length();
      }

      return str.substring(lastIndex, nextIndex);
   }


   /**
    * Returns a FileHolder object for the specified file pending around in the
    * current request
    *
    * @param request the request object
    * @param name the file name.
    *
    * @return a FileHolder object for the named file.
    */
   public static FileHolder getFileHolder(HttpServletRequest request,
                                          String             name) {
      MultipartRequest multipartRequest = (MultipartRequest) request
                                          .getAttribute("multipartRequest");

      return (multipartRequest == null) ? null
                                        : multipartRequest.getFileHolder(name);
   }


   /**
    * Returns a InputStream object for the specified file pending around in the
    * current request (it's still in a [File]Part!)
    *
    * @param request of the file name.
    * @param name the file name
    *
    * @return a Fileinputstream object for the named file.
    */
   public static InputStream getFileInputStream(HttpServletRequest request,
                                                String             name) {
      MultipartRequest multipartRequest = (MultipartRequest) request
                                          .getAttribute("multipartRequest");

      return (multipartRequest == null) ? null
                                        : multipartRequest.getFileInputStream(name);
   }


   /**
    * Returns the names of all the uploaded files as an Enumeration of Strings.
    * It returns an empty Enumeration if there are no uploaded files.  Each
    * file name is the name specified by the form, not by the user.
    *
    * @param request the request object
    *
    * @return the names of all the uploaded files as an Enumeration of Strings.
    */
   public static Enumeration getFileNames(HttpServletRequest request) {
      MultipartRequest multipartRequest = (MultipartRequest) request
                                          .getAttribute("multipartRequest");

      return (multipartRequest == null) ? null
                                        : multipartRequest.getFileNames();
   }


   /**
    * Get the name of the parameter starting with the input string.
    *
    * @param request the request object
    * @param str the string to check for
    *
    * @return the name of the parameter starting with the input string
    */
   public static String getFirstParameterStartingWith(HttpServletRequest request,
                                                      String             str) {
      MultipartRequest multipartRequest = (MultipartRequest) request
                                          .getAttribute("multipartRequest");

      Enumeration      enum = (multipartRequest == null)
                              ? request.getParameterNames()
                              : multipartRequest.getParameterNames();

      while (enum.hasMoreElements()) {
         String param = (String) enum.nextElement();

         if (param.startsWith(str)) {
            return param;
         }
      }

      enum = request.getAttributeNames();

      while (enum.hasMoreElements()) {
         String param = (String) enum.nextElement();

         if (param.startsWith(str)) {
            return param;
         }
      }

      return null;
   }


   /**
    * Returns the value of the named parameter as a String, or the default if
    * the parameter was not sent or was sent without a value.  The value is
    * guaranteed to be in its normal, decoded form.  If the parameter has
    * multiple values, only the first (!!!) one is returned
    * For parameters with multiple values, it's possible the
    * first "value" may be null.
    *
    * @param request the request object
    * @param name    the parameter name
    * @param def     the default value
    *
    * @return the parameter value.
    */
   public static String getParameter(HttpServletRequest request,
                                     String             name,
                                     String             def) {
      String s = getParameter(request, name);

      if (Util.isNull(s)) {
         s = def;
      }

      return s;
   }


   /**
    * Returns the value of the named parameter as a String, or null if the
    * parameter was not sent or was sent without a value.  The value is
    * guaranteed to be in its normal, decoded form.  If the parameter has
    * multiple values, only the first (!!!) one is returned
    * For parameters with multiple values, it's possible the
    * first "value" may be null.
    *
    * @param request the request object
    * @param name the parameter name.
    *
    * @return the parameter value.
    */
   public static String getParameter(HttpServletRequest request,
                                     String             name) {
      String           res;
      MultipartRequest multipartRequest = (MultipartRequest) request
                                          .getAttribute("multipartRequest");

      //patch by borghi
      if (request.getAttribute(name) != null) {
         res = request.getAttribute(name)
                      .toString();
      } else {
         //end patch
         res = (multipartRequest == null) ? request.getParameter(name)
                                          : multipartRequest.getParameter(name);
      }

      return res;
   }


   /**
    * Returns the names of all the parameters as an Enumeration of Strings.  It
    * returns an empty Enumeration if there are no parameters.
    *
    * @param request the request object
    *
    * @return the names of all the parameters as an Enumeration of Strings.
    */
   public static Enumeration getParameterNames(HttpServletRequest request) {
      MultipartRequest multipartRequest = (MultipartRequest) request
                                          .getAttribute("multipartRequest");

      return (multipartRequest == null) ? request.getParameterNames()
                                        : multipartRequest.getParameterNames();
   }


   /**
    * Returns the values of the named parameter as a String array, or null if
    * the parameter was not sent.  The array has one entry for each parameter
    * field sent.  If any field was sent without a value that entry is stored
    * in the array as a null.  The values are guaranteed to be in their
    * normal, decoded form.  A single value is returned as a one-element
    * array.
    *
    * @param request the request object
    * @param name the parameter name.
    *
    * @return the parameter values.
    */
   public static String[] getParameterValues(HttpServletRequest request,
                                             String             name) {
      MultipartRequest multipartRequest = (MultipartRequest) request
                                          .getAttribute("multipartRequest");

      return (multipartRequest == null) ? request.getParameterValues(name)
                                        : multipartRequest.getParameterValues(name);
   }


   /**
     * Get a Vector object containing all the request parameters
     * starting with the input string.
     *
     * @param request the request object
     * @param str the string to check for
     *
     * @return a Vector object containing all the request parameters
     *         starting with the input string.
     */
   public static Vector getParametersStartingWith(HttpServletRequest request,
                                                  String             str) {
      MultipartRequest multipartRequest = (MultipartRequest) request
                                          .getAttribute("multipartRequest");
      Vector           result = new Vector();
      Enumeration      enum   = (multipartRequest == null)
                                ? request.getParameterNames()
                                : multipartRequest.getParameterNames();

      while (enum.hasMoreElements()) {
         String param = (String) enum.nextElement();

         if (param.startsWith(str)) {
            if (!result.contains(param)) {
               result.addElement(param);
            }
         }
      }

      enum = request.getAttributeNames();

      while (enum.hasMoreElements()) {
         String param = (String) enum.nextElement();

         if (param.startsWith(str)) {
            if (!result.contains(param)) {
               result.addElement(param);
            }
         }
      }

      return result;
   }


   /**
    * Get a Vector object containing all the tokens
    * related to the input string, splitted using the input delimiter.
    *
    * @param str the string to split
    * @param delimeter the delimiter string
    *
    * @return a Vector object containing all the tokens
    *         related to the input string
    */
   public static Vector splitString(String str,
                                    String delimeter) {
      Vector          result = new Vector();
      StringTokenizer st = new StringTokenizer(str, delimeter);

      while (st.hasMoreTokens())
         result.addElement(st.nextToken());

      return result;
   }
}
