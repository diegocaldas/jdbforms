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

package org.dbforms.taglib;

import org.dbforms.util.MessageResources;

import java.util.Locale;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;



/**
 * 2002-09-23 HKK: Extented to support parameters
 */
public class MessageTag extends AbstractTagSupportWithScriptHandler
   implements javax.servlet.jsp.tagext.TryCatchFinally {
   private String key   = null;
   private String param = null;

   /**
    * DOCUMENT ME!
    *
    * @param newKey DOCUMENT ME!
    */
   public void setKey(String newKey) {
      key = newKey;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getKey() {
      return key;
   }


   /**
    * DOCUMENT ME!
    *
    * @param newParam DOCUMENT ME!
    */
   public void setParam(String newParam) {
      param = newParam;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getParam() {
      return param;
   }


   /**
    * DOCUMENT ME!
    *
    * @param t DOCUMENT ME!
    *
    * @throws Throwable DOCUMENT ME!
    */
   public void doCatch(Throwable t) throws Throwable {
      throw t;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws JspException DOCUMENT ME!
    */
   public int doEndTag() throws JspException {
      if (getKey() != null) {
         Locale locale = MessageResources.getLocale((HttpServletRequest) pageContext
                                                    .getRequest());
         String message;

         if ((param == null) || (param.length() == 0)) {
            message = MessageResources.getMessage(getKey(), locale);
         } else {
            message = MessageResources.getMessage(getKey(), locale,
                                                  splitString(param, ","));
         }

         try {
            if (message != null) {
               pageContext.getOut()
                          .write(message);
            } else {
               pageContext.getOut()
                          .write(getKey());

               if (param != null) {
                  pageContext.getOut()
                             .write("&nbsp;");
                  pageContext.getOut()
                             .write(param);
               }
            }
         } catch (java.io.IOException ioe) {
            throw new JspException("IO Error: " + ioe.getMessage());
         }
      }

      return EVAL_PAGE;
   }


   /**
    * DOCUMENT ME!
    */
   public void doFinally() {
      key   = null;
      param = null;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws javax.servlet.jsp.JspException DOCUMENT ME!
    */
   public int doStartTag() throws javax.servlet.jsp.JspException {
      return SKIP_BODY;
   }


   private String[] splitString(String str,
                                String delimeter) {
      StringTokenizer st     = new StringTokenizer(str, delimeter);
      int             i      = 0;
      String[]        result = new String[st.countTokens()];

      while (st.hasMoreTokens()) {
         result[i] = st.nextToken();
         i++;
      }

      return result;
   }
}
