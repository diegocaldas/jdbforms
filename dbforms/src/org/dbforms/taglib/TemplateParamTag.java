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

import java.io.*;

import java.util.*;

import javax.servlet.jsp.JspException;



/**
 * Renders an dbforms style tag
 *
 * @author Joe Peer
 */
public class TemplateParamTag extends TagSupportWithScriptHandler
   implements javax.servlet.jsp.tagext.TryCatchFinally {
   private Hashtable sp;
   private String    baseDir;
   private String    defaultValue; // properties set by JSP container
   private String    dir; // properties set by JSP container
   private String    name; // properties set by JSP container

   /**
    * DOCUMENT ME!
    *
    * @param defaultValue DOCUMENT ME!
    */
   public void setDefaultValue(String defaultValue) {
      this.defaultValue = defaultValue;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getDefaultValue() {
      return defaultValue;
   }


   /**
    * DOCUMENT ME!
    *
    * @param dir DOCUMENT ME!
    */
   public void setDir(String dir) {
      if (dir.equals(".")) {
         this.dir = "";
      } else {
         this.dir = dir;
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getDir() {
      return dir;
   }


   /**
    * DOCUMENT ME!
    *
    * @param name DOCUMENT ME!
    */
   public void setName(String name) {
      this.name = name;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getName() {
      return name;
   }


   /**
    * DOCUMENT ME!
    *
    * @param pageContext DOCUMENT ME!
    */
   public void setPageContext(final javax.servlet.jsp.PageContext pageContext) {
      super.setPageContext(pageContext);
      this.sp = (java.util.Hashtable) pageContext.getRequest()
                                                 .getAttribute("styleparams");
      this.baseDir = (String) pageContext.getRequest()
                                         .getAttribute("baseDir");
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
      try {
         StringBuffer buf = new StringBuffer();

         // determinate dir
         if (dir != null) {
            buf.append(baseDir);
            buf.append(dir);

            if (dir.length() > 0) {
               buf.append("/");
            }
         }

         // determinate param value
         if (sp == null) {
            if (defaultValue != null) {
               buf.append(defaultValue);
            }
         } else {
            String paramValue = (String) sp.get(name);

            if (paramValue != null) {
               buf.append(paramValue);
            } else {
               if (defaultValue != null) {
                  buf.append(defaultValue);
               }
            }
         }

         pageContext.getOut()
                    .flush();
         pageContext.getOut()
                    .write(buf.toString());
      } catch (IOException ioe) {
         throw new JspException("Problem including template end - "
                                + ioe.toString());
      }

      return EVAL_PAGE;
   }


   /**
    * DOCUMENT ME!
    */
   public void doFinally() {
      name         = null;
      defaultValue = null;
      dir          = null;
      baseDir      = null;
      sp           = null;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws JspException DOCUMENT ME!
    */
   public int doStartTag() throws JspException {
      return SKIP_BODY;
   }
}
