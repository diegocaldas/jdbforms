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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.event.AbstractWebEvent;

import org.dbforms.util.Util;

import javax.servlet.jsp.JspException;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class IsWebEvent extends AbstractDbBaseHandlerTag
   implements javax.servlet.jsp.tagext.TryCatchFinally {
   // logging category for this class
   private static Log logCat = LogFactory.getLog(IsWebEvent.class.getName());
   private String     event;
   private String     value;

   /**
    * Creates a new IsWebEvent object.
    */
   public IsWebEvent() {
      value = "true";
      event = null;
   }

   /**
    * DOCUMENT ME!
    *
    * @param str DOCUMENT ME!
    */
   public final void setEvent(String str) {
      event = str;
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
    * @see javax.servlet.jsp.tagext.TryCatchFinally#doCatch(java.lang.Throwable)
    */
   public void doCatch(Throwable t) throws Throwable {
      throw t;
   }


   /**
    * DOCUMENT ME!
    */
   public void doFinally() {
      value = "true";
      event = null;
      super.doFinally();
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws JspException DOCUMENT ME!
    */
   public int doStartTag() throws JspException {
      AbstractWebEvent we = getParentForm()
                       .getWebEvent();

      if ((we == null) || (event == null) || (value == null)) {
         logCat.debug("Can't do IsWebEvent with  webEvent: " + we + "  event: "
                      + event + "   value: " + value);

         return SKIP_BODY;
      }

      String  className = we.getType();

      boolean eventNameMatch = className.toUpperCase()
                                        .indexOf(event.toUpperCase()) != -1;

      if (logCat.isDebugEnabled()) {
         logCat.debug(" IsLocalWebEvent webEvent className: " + className
                      + "    event: " + event + "  value: " + value);
      }

      return (Util.getTrue(value) == eventNameMatch) ? EVAL_BODY_INCLUDE
                                                     : SKIP_BODY;
   }
}
