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


import java.util.Enumeration;
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

      Enumeration      e = (multipartRequest == null)
                              ? request.getParameterNames()
                              : multipartRequest.getParameterNames();

      while (e.hasMoreElements()) {
         String param = (String) e.nextElement();

         if (param.startsWith(str)) {
            return param;
         }
      }

      e = request.getAttributeNames();

      while (e.hasMoreElements()) {
         String param = (String) e.nextElement();

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
      Enumeration      e   = (multipartRequest == null)
                                ? request.getParameterNames()
                                : multipartRequest.getParameterNames();

      while (e.hasMoreElements()) {
         String param = (String) e.nextElement();

         if (param.startsWith(str)) {
            if (!result.contains(param)) {
               result.addElement(param);
            }
         }
      }

      e = request.getAttributeNames();

      while (e.hasMoreElements()) {
         String param = (String) e.nextElement();

         if (param.startsWith(str)) {
            if (!result.contains(param)) {
               result.addElement(param);
            }
         }
      }

      return result;
   }
}
