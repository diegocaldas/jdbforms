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

import org.dbforms.util.Formatter;
import org.dbforms.util.MessageResources;

import java.io.IOException;

import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;



/**
 * DOCUMENT ME!
 *
 * @author neal katz  Format an arbitray string with a register Custom
 *         Formatter  &lt;:customFormat customFormatter="xx" value="123456789"
 *         /&gt; &lt;db:customFormat
 *         customFormatter="xx"&t;123456789&lt;/db:customFormat&gt;
 */
public class CustomFormatTag extends BodyTagSupport
   implements javax.servlet.jsp.tagext.TryCatchFinally {
   private String customFormatter = null;
   private String value = null;

   /**
    * DOCUMENT ME!
    *
    * @param string DOCUMENT ME!
    */
   public void setCustomFormatter(String string) {
      customFormatter = string;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getCustomFormatter() {
      return customFormatter;
   }


   /**
    * DOCUMENT ME!
    *
    * @param string DOCUMENT ME!
    */
   public void setValue(String string) {
      value = string;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getValue() {
      return value;
   }


   /**
    * DOCUMENT ME!
    *
    * @param s DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String customFormat(String s) {
      if ((customFormatter != null) && (customFormatter.length() > 0)) {
         HttpSession session = pageContext.getSession();
         HashMap     hm = (HashMap) session.getAttribute(SetCustomFormatterTag.sessionKey);

         if (hm != null) {
            Object obj = hm.get(customFormatter);

            if (obj instanceof Formatter) {
               Formatter cf = (Formatter) obj;
               Object[]  o = new Object[] {
                                s,
                                null,
                                this
                             };
               Locale locale = MessageResources.getLocale((HttpServletRequest) pageContext
                                                          .getRequest());
               cf.setLocale(locale);
               s = cf.sprintf(o);
            }
         }
      }

      return s;
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
      // if no value attribute specified, use the body
      if ((value == null)
                && (bodyContent != null)
                && (bodyContent.getString() != null)) {
         value = bodyContent.getString()
                            .trim();
      }

      try {
         pageContext.getOut()
                    .write(customFormat(value));
      } catch (IOException e) {
         throw new JspException("IO Error: " + e.getMessage());
      }

      return EVAL_PAGE;
   }


   /**
    * DOCUMENT ME!
    */
   public void doFinally() {
      customFormatter = null;
      value           = null;
   }
}
