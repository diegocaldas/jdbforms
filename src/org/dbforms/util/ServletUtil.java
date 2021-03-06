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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;



/**
 *  Servlet Utility class
 *
 * @author  Luca Fossato
 * @created  10 June 2002
 */
public class ServletUtil {
   /** Log4j category. */
   private static Log cat = LogFactory.getLog(ServletUtil.class);

   /**
    *  Dumps all the incoming HttpServletRequest informations.
    *
    * @param  req the input HttpServletRequest object to dump
    * @return  a string with dumped information
    */
   public static String dumpRequest(HttpServletRequest req) {
      return dumpRequest(req, "\n");
   }


   /**
    *  Dumps all the incoming HttpServletRequest informations.
    *
    * @param  req the input HttpServletRequest object to dump
    * @return  a string with dumped information
    */
   public static String dumpRequest(HttpServletRequest req,
                                    String             returnToken) {
      String s = null;

      try {
         s = dump(req, returnToken);
      } catch (Exception e) {
         cat.error("::dumpRequest - exception: ", e);
      }

      return s;
   }


   /**
    *  Get the length of the element having the longer name
    *
    * @param paramNames the enumeration element
    * @return the length of the element having the longer name
    */
   private static int getElementNameMaxLength(Iterator paramNames) {
      int len = 0;

      while (paramNames.hasNext()) {
         String paramName = (String) paramNames.next();
         int    tmpLen = paramName.length();

         if (tmpLen > len) {
            len = tmpLen;
         }
      }

      return len;
   }


   /**
    *  Return a String containin blank chars.
    *  Its length is equal to ($spacesToAdd - $fromString.length)
    *
    * @param spacesToAdd number of space characters to add
    * @param fromString  the string where to start to add chars
    * @return a String containin blank chars whose length is equal
    *         to ($spacesToAdd - $fromString.length)
    */
   private static String addSpaces(int    spacesToAdd,
                                   String fromString) {
      int    len = spacesToAdd;
      String res = "";

      if (!Util.isNull(fromString)) {
         len = (spacesToAdd - fromString.length());

         for (int i = 0; i < len; i++) {
            res += " ";
         }
      }

      return res;
   }


   /**
    *  Dump the incoming HttpServletRequest object
    *
    * @param  req the HttpServletRequest object to dump
    * @return  Description of the Return Value
    * @exception  ServletException if any error occurs
    * @exception  IOException      if any error occurs
    */
   private static String dump(HttpServletRequest req,
                              String             returnToken)
                       throws ServletException, IOException {
      StringBuffer sb = new StringBuffer();
      String       s = null;

      sb.append("HTTP Snooper Servlet")
        .append(returnToken)
        .append(returnToken);
      sb.append("Request URL:")
        .append(returnToken);
      sb.append(" http://" + req.getServerName() + ":" + req.getServerPort()
                + req.getRequestURI() + returnToken)
        .append(returnToken);

      sb.append("Request Info:")
        .append(returnToken);
      sb.append(" Request Method: " + req.getMethod())
        .append(returnToken);
      sb.append(" Request URI: " + req.getRequestURI())
        .append(returnToken);
      sb.append(" Request Protocol: " + req.getProtocol())
        .append(returnToken);
      sb.append(" Servlet Path: " + req.getServletPath())
        .append(returnToken);
      sb.append(" Path Info: " + req.getPathInfo())
        .append(returnToken);
      sb.append(" Path Translated: " + req.getPathTranslated())
        .append(returnToken);
      sb.append(" Content Length: " + req.getContentLength())
        .append(returnToken);
      sb.append(" Content Type: " + req.getContentType())
        .append(returnToken);

      String queryString = req.getQueryString();
      sb.append(" QueryString: " + queryString)
        .append(returnToken);

      if (queryString != null) {
         Map ht = req.getParameterMap();
         sb.append(geKeyValuesData(ht, returnToken))
           .append(returnToken);
      }

      sb.append(" Server Name: " + req.getServerName())
        .append(returnToken);
      sb.append(" Server Port: " + req.getServerPort())
        .append(returnToken);
      sb.append(" Remote User: " + req.getRemoteUser())
        .append(returnToken);
      sb.append(" Remote Host: " + req.getRemoteHost())
        .append(returnToken);
      sb.append(" Remote Address: " + req.getRemoteAddr())
        .append(returnToken);
      sb.append(" Authentication Scheme: " + req.getAuthType())
        .append("")
        .append(returnToken);

      // return parameters (name/value pairs)
      int         maxParamNameLength = getElementNameMaxLength(req.getParameterMap().keySet().iterator());
      Enumeration params = req.getParameterNames();

      if (params.hasMoreElements()) {
         sb.append(returnToken)
           .append("Parameters:")
           .append(returnToken);

         while (params.hasMoreElements()) {
            String name = (String) params.nextElement();
            sb.append("  ")
              .append(name)
              .append(addSpaces(maxParamNameLength, name))
              .append(" = ");

            String[] values = req.getParameterValues(name);

            for (int x = 0; x < values.length; x++) {
               if (x > 0) {
                  sb.append(", ");
               }

               sb.append(values[x]);
            }

            sb.append(returnToken);
         }

         sb.append(returnToken);
      }

      // return HTTP Headers
      Enumeration e = req.getHeaderNames();

      if (e.hasMoreElements()) {
         sb.append("Request Headers:")
           .append(returnToken);

         while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            sb.append("  " + name + ": " + req.getHeader(name))
              .append(returnToken);
         }

         sb.append(returnToken);
      }

      sb.append(returnToken);
      s = sb.toString();

      return s;
   }


   /**
    *  Get the data from the input hashMap where keys are string
    *  and key values are string arrays.
    *
    * @param ht  the hash table
    * @param returnToken the return token, i.e.: "\n" or "<br>\n"
    */
   private static StringBuffer geKeyValuesData(Map    ht,
                                               String returnToken) {
      StringBuffer sb   = new StringBuffer();
      Iterator     keys = ht.keySet()
                            .iterator();
      int          maxKeyNameLength = getElementNameMaxLength(keys);

      while (keys.hasNext()) {
         String   key    = (String) keys.next();
         String[] values = (String[]) ht.get(key);

         for (int i = 0; i < values.length; i++) {
            sb.append("  {")
              .append(key)
              .append(" [")
              .append(i)
              .append("]}")
              .append(addSpaces(maxKeyNameLength, key))
              .append(" = ")
              .append(values[i])
              .append(returnToken);
         }
      }

      return sb;
   }
}
