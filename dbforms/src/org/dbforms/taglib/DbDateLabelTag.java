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

import org.dbforms.util.Util;

import javax.servlet.jsp.JspException;



/**
 * Philip Grunikiewicz 2001-05-14 This class inherits from DbLabelTag.  It
 * allows a developer to specify the displayed date format.
 */
public class DbDateLabelTag extends DbLabelTag
   implements javax.servlet.jsp.tagext.TryCatchFinally {
   /**
    * @see javax.servlet.jsp.tagext.TryCatchFinally#doCatch(java.lang.Throwable)
    */
   public void doCatch(Throwable t) throws Throwable {
      throw t;
   }


   /**
    * Philip Grunikiewicz 2001-05-14  If user has specified a date format - use
    * it!
    *
    * @return DOCUMENT ME!
    */
   public int doEndTag() throws javax.servlet.jsp.JspException {
      try {
         String fieldValue = getFormattedFieldValue();

         // SM 2003-08-05
         // if styleClass is present, render a SPAN with text included
         fieldValue = escapeHTML(fieldValue);

         String s = prepareStyles();

         if (Util.isNull(s)) {
            pageContext.getOut()
                       .write(fieldValue);
         } else {
            pageContext.getOut()
                       .write("<span " + s + "\">" + fieldValue + "</span>");
         }
      } catch (Exception e) {
         throw new JspException("Error: " + e.getMessage());
      }

      return EVAL_PAGE;
   }


   /**
    * DOCUMENT ME!
    */
   public void doFinally() {
      super.doFinally();
   }
}
