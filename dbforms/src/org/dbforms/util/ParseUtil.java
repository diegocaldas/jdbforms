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
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.log4j.Category;



/****
 * <p>
 * this utility-class provides convenience methods for parsing strings and generating certain
 * data structures
 * </p>
 *
 * @author Joe Peer <j.peer@gmx.net>
 */
public class ParseUtil
{
   static Category logCat = Category.getInstance(ParseUtil.class.getName());

   // logging category for this class
   // ------------------ methods directly related to HttpServletRequest operations

   /**
    * Returns the names of all the parameters as an Enumeration of
    * Strings.  It returns an empty Enumeration if there are no parameters.
    *
    * @return the names of all the parameters as an Enumeration of Strings.
    */
   public static Enumeration getParameterNames(HttpServletRequest request)
   {
      MultipartRequest multipartRequest = (MultipartRequest) request
         .getAttribute("multipartRequest");

      return (multipartRequest == null) ? request.getParameterNames()
                                        : multipartRequest.getParameterNames();
   }


   /**
    * Returns the value of the named parameter as a String, or the default if
    * the parameter was not sent or was sent without a value.  The value
    * is guaranteed to be in its normal, decoded form.  If the parameter
    * has multiple values, only the first (!!!!!) one is returned (for backward
    * compatibility).  For parameters with multiple values, it's possible the
    * first "value" may be null.
    *
    * @param name the parameter name.
    * @return the parameter value.
    */
   public static String getParameter(ServletRequest request, String name,
      String def)
   {
      String t = ((HttpServletRequest) request).getQueryString();
      String s = getParameter(request, name);

      if (Util.isNull(s))
      {
         s = def;
      }

      return s;
   }


   /**
    * Returns the value of the named parameter as a String, or null if
    * the parameter was not sent or was sent without a value.  The value
    * is guaranteed to be in its normal, decoded form.  If the parameter
    * has multiple values, only the first (!!!) one is returned (for backward
    * compatibility).  For parameters with multiple values, it's possible the
    * first "value" may be null.
    *
    * @param name the parameter name.
    * @return the parameter value.
    */
   public static String getParameter(ServletRequest request, String name)
   {
      MultipartRequest multipartRequest = (MultipartRequest) request
         .getAttribute("multipartRequest");

      //patch by borghi
      if (request.getAttribute(name) != null)
      {
         return request.getAttribute(name).toString();
      }
      else
      {
         //end patch		
         return (multipartRequest == null) ? request.getParameter(name)
                                           : multipartRequest.getParameter(name);
      }
   }


   /**
    * Returns the values of the named parameter as a String array, or null if
    * the parameter was not sent.  The array has one entry for each parameter
    * field sent.  If any field was sent without a value that entry is stored
    * in the array as a null.  The values are guaranteed to be in their
    * normal, decoded form.  A single value is returned as a one-element array.
    *
    * @param name the parameter name.
    * @return the parameter values.
    */
   public static String[] getParameterValues(ServletRequest request, String name)
   {
      MultipartRequest multipartRequest = (MultipartRequest) request
         .getAttribute("multipartRequest");

      return (multipartRequest == null) ? request.getParameterValues(name)
                                        : multipartRequest.getParameterValues(name);
   }


   //------------- request operations on higher level (not related to standard api)

   /**
    * Returns the names of all the uploaded files as an Enumeration of
    * Strings.  It returns an empty Enumeration if there are no uploaded
    * files.  Each file name is the name specified by the form, not by
    * the user.
    *
    * @return the names of all the uploaded files as an Enumeration of Strings.
    */
   public static Enumeration getFileNames(ServletRequest request)
   {
      MultipartRequest multipartRequest = (MultipartRequest) request
         .getAttribute("multipartRequest");

      return (multipartRequest == null) ? null : multipartRequest.getFileNames();
   }


   /**
    * Returns a InputStream object for the specified file pending around in the current request (it's still in a [File]Part!)
    *
    * @param inputstream of the file name.
    * @return a Fileinputstream object for the named file.
    */
   public static InputStream getFileInputStream(ServletRequest request,
      String name)
   {
      MultipartRequest multipartRequest = (MultipartRequest) request
         .getAttribute("multipartRequest");

      return (multipartRequest == null) ? null
                                        : multipartRequest.getFileInputStream(name);
   }


   /**
    * Returns a FileHolder object for the specified file pending around in the current request
    *
    * @param name the file name.
    * @return a FileHolder object for the named file.
    */
   public static FileHolder getFileHolder(ServletRequest request, String name)
   {
      MultipartRequest multipartRequest = (MultipartRequest) request
         .getAttribute("multipartRequest");

      return (multipartRequest == null) ? null
                                        : multipartRequest.getFileHolder(name);
   }


   /**
   */
   public static Vector getParametersStartingWith(ServletRequest request,
      String str)
   {
      MultipartRequest multipartRequest = (MultipartRequest) request
         .getAttribute("multipartRequest");

      Vector           result = new Vector();
      Enumeration      enum   = (multipartRequest == null)
         ? request.getParameterNames() : multipartRequest.getParameterNames();

      while (enum.hasMoreElements())
      {
         String param = (String) enum.nextElement();

         if (param.startsWith(str))
         {
            result.addElement(param);
         }
      }

      return result;
   }


   /**
   */
   public static String getFirstParameterStartingWith(ServletRequest request,
      String str)
   {
      MultipartRequest multipartRequest = (MultipartRequest) request
         .getAttribute("multipartRequest");

      Enumeration      enum = (multipartRequest == null)
         ? request.getParameterNames() : multipartRequest.getParameterNames();

      while (enum.hasMoreElements())
      {
         String param = (String) enum.nextElement();

         if (param.startsWith(str))
         {
            return param;
         }
      }

      return null;
   }


   /**
    * DOCUMENT ME!
    *
    * @param str DOCUMENT ME!
    * @param delimeter DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public static Vector splitString(String str, String delimeter)
   {
      Vector          result = new Vector();

      StringTokenizer st = new StringTokenizer(str, delimeter);

      while (st.hasMoreTokens())
         result.addElement(st.nextToken());

      return result;
   }


   /**
   <p>Method for parsing substring embedded by constant delimeters</p>

   <p>consider the following string s: ac_update_3_12</p>

   <p><pre>
   getEmbeddedString(s, 0, '_') ==> "ac"
   getEmbeddedString(s, 1, '_') ==> "update"
   getEmbeddedString(s, 2, '_') ==> "3"
   getEmbeddedString(s, 3, '_') ==> "12"
   getEmbeddedString(s, 3, '_') ==> will throw a Runtime Exception
   </pre></p>
   */
   public static String getEmbeddedString(String str, int afterDelims,
      char delim)
   {
      int lastIndex = 0;

      for (int i = 0; i < afterDelims; i++)
      {
         lastIndex = str.indexOf(delim, lastIndex) + 1; // search end of cutting
      }

      int nextIndex = str.indexOf(delim, lastIndex); // end of cutting

      if (nextIndex == -1)
      {
         // new: 18.1.2001: support for IMAGE buttons #checkme: can we swith this off?
         int dotIndex = str.lastIndexOf('.');
         nextIndex = (dotIndex == -1) ? str.length() : dotIndex;
      }

      return str.substring(lastIndex, nextIndex);
   }


   /**
   <p>Method for parsing substring embedded by constant delimeters</p>

   <p>Because getEmbeddedString() support "." for image button, this
      method do the the same, but ignore dots. It's a patch and must
      be revised in the next cleanup... #checkme</p>

   <p>consider the following string s: English-001:param1, param2</p>

   <p><pre>
   </pre></p>
   */
   public static String getEmbeddedStringWithoutDots(String str,
      int afterDelims, char delim)
   {
      int lastIndex = 0;

      for (int i = 0; i < afterDelims; i++)
      {
         lastIndex = str.indexOf(delim, lastIndex) + 1; // search end of cutting
      }

      int nextIndex = str.indexOf(delim, lastIndex); // end of cutting

      if (nextIndex == -1)
      {
         nextIndex = str.length();
      }

      return str.substring(lastIndex, nextIndex);
   }


   /**
    * DOCUMENT ME!
    *
    * @param str DOCUMENT ME!
    * @param afterDelims DOCUMENT ME!
    * @param delim DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public static int getEmbeddedStringAsInteger(String str, int afterDelims,
      char delim)
   {
      return Integer.parseInt(getEmbeddedString(str, afterDelims, delim));
   }


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public static boolean hasSearchFields(ServletRequest request)
   {
      Vector searchFieldNames = getParametersStartingWith(request, "search_");

      return ((searchFieldNames == null) || (searchFieldNames.size() == 0));
   }
}
