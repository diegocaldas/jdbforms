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

import org.dbforms.interfaces.ICustomFormat;

import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;


import org.dbforms.util.MessageResources;
import org.dbforms.util.ReflectionUtil;
import org.dbforms.util.Util;

/**
 * DOCUMENT ME!
 *
 * @author Neal Katz &lt;db:setCustomFormater name="foo" class="" arg=""
 *         reset="" &gt; arg and reset are optional
 */
public class SetCustomFormatterTag extends AbstractScriptHandlerTag
   implements javax.servlet.jsp.tagext.TryCatchFinally {
   
   private static final String sessionKey = "dbforms.org.tag.CustomFormatterTag";
   
   Object              arg       = null;
   String              className = null;
   String              name      = null;


   public static String sprintf(String customFormatter, PageContext pageContext, String s) {
		if (!Util.isNull(customFormatter)) {
			HashMap hm = (HashMap) pageContext
					.getAttribute(SetCustomFormatterTag.sessionKey);
			if (hm != null) {
				Object obj = hm.get(customFormatter);
				if (obj instanceof ICustomFormat) {
					ICustomFormat cf = (ICustomFormat) obj;
					Locale locale = MessageResources
							.getLocale((HttpServletRequest) pageContext
									.getRequest());
					cf.setLocale(locale);
					s = cf.sprintf(s);
				}
			}
		}
		return s;
	   
   }
   
   /**
    * optional argument passed to CustomFormatter instance init()
    *
    * @param obj
    */
   public void setArg(Object obj) {
      this.arg = obj; 
   }


   /**
    * classname of a class implementing the CustomFormatter Interface
    *
    * @param className
    */
   public void setClassName(String className) {
      this.className = className;
   }


   /**
    * name to use, other tags will use this as the value for the
    * customFormatter attribute
    *
    * @param name
    */
   public void setName(String name) {
      this.name = name;
   }

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public int doEndTag() throws JspException {
      if ((name != null) && (name.length() > 0)) {
         HashMap     hm = (HashMap) pageContext.getAttribute(sessionKey);
         if (hm == null) {
            hm = new HashMap();
            pageContext.setAttribute(sessionKey, hm);
         }
         try {
            ICustomFormat cf = (ICustomFormat) hm.get(name);
            if (cf == null) {
               cf = (ICustomFormat) ReflectionUtil.newInstance(className);
               cf.setArg(arg.toString());
               hm.put(name, cf);
            }
         } catch (Exception e) {
            throw new JspException(e.getLocalizedMessage());
         }
      }
      return EVAL_PAGE;
   }


   /**
    * DOCUMENT ME!
    */
   public void doFinally() {
      this.arg       = null;
      this.className = null;
      this.name      = null;
      super.doFinally();
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
}
