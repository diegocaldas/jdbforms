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

import javax.servlet.jsp.JspException;



/**
 * <p>
 * This tag renders a HTML TextArea - Element
 * </p>
 * this tag renders a dabase-datadriven textArea, which is an active element -
 * the user can change data
 *
 * @author Joachim Peer
 */
public class DbTextFieldTag extends DbBaseInputTag
   implements javax.servlet.jsp.tagext.TryCatchFinally {
   private java.lang.String password = "false";

   /**
    * Determines if the text field should be a password text field (display '')
    *
    * @param pwd DOCUMENT ME!
    */
   public void setPassword(String pwd) {
      this.password = pwd;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getPassword() {
      return this.password;
   }


   /**
    * @see javax.servlet.jsp.tagext.TryCatchFinally#doCatch(java.lang.Throwable)
    */
   public void doCatch(Throwable t) throws Throwable {
      throw t;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws javax.servlet.jsp.JspException DOCUMENT ME!
    * @throws JspException DOCUMENT ME!
    */
   public int doEndTag() throws JspException {
      try {
         /* Does the developer require the field to be hidden, displayed or displayed as password? */
         StringBuffer tagBuf = new StringBuffer("<input ");

         if ("true".equals(this.getPassword())) {
            tagBuf.append("type=\"password\" ");
         } else {
            tagBuf.append(prepareType());
         }

         tagBuf.append(prepareName());
         tagBuf.append(prepareValue());
         tagBuf.append(prepareSize());
         tagBuf.append(prepareKeys());
         tagBuf.append(prepareStyles());
         tagBuf.append(prepareEventHandlers());
         tagBuf.append("/>");

         pageContext.getOut()
                    .write(tagBuf.toString());

         // Writes out the old field value
         writeOutSpecialValues();

         // For generation Javascript Validation.  Need all original and modified fields name
         getParentForm()
            .addChildName(getName(), getFormFieldName());
      } catch (java.io.IOException ioe) {
         throw new JspException("IO Error: " + ioe.getMessage());
      }

      return EVAL_PAGE;
   }


   /**
    * DOCUMENT ME!
    */
   public void doFinally() {
      password = "false";
      super.doFinally();
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws javax.servlet.jsp.JspException DOCUMENT ME!
    */
   public int doStartTag() throws JspException {
      super.doStartTag();

      return SKIP_BODY;
   }
}
