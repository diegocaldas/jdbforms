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

/*
 * the idea for this class was taken from the class ErrorsTag-class of the Apache Struts Project
 */
package org.dbforms.taglib;

import org.dbforms.util.Util;

import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;



/**
 * Custom tag that evaluates its body if an Error has occured.
 *
 * <p>
 * Based on ErrorTag and IsWebEvent tag, allows for conditional coding if an
 * error event occured.
 * </p>
 *
 * @author Neal Katz
 */
public class HasErrorTag extends BodyTagSupport
   implements javax.servlet.jsp.tagext.TryCatchFinally {
   //   private String messagePrefix;
   private String name  = "errors";
   private String value = "true";

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
      return (this.name);
   }


   /**
    * DOCUMENT ME!
    *
    * @param string
    */
   public void setValue(String string) {
      value = string;
   }


   /**
    * DOCUMENT ME!
    *
    * @return
    */
   public String getValue() {
      return value;
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
    */
   public void doFinally() {
      name  = "errors";
      value = "true";
   }


   // ------------------------------------------------------- Public Methods

   /**
    * Render the specified error messages if there are any.
    *
    * @return DOCUMENT ME!
    *
    * @exception JspException if a JSP exception has occurred
    */
   public int doStartTag() throws JspException {
      Vector             originalErrors = (Vector) pageContext.getAttribute(getName(),
                                                                            PageContext.REQUEST_SCOPE);
      boolean            haveError = false;

      haveError = (((originalErrors != null) && (originalErrors.size() > 0)));

      return (Util.getTrue(value) == haveError) ? EVAL_BODY_INCLUDE
                                                : SKIP_BODY;
   }
}
