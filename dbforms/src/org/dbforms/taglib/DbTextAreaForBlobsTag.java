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

import javax.servlet.jsp.*;



/**
 * <p>
 * This tag renders a HTML TextArea - Element
 * </p>
 * this tag renders a dabase-datadriven textArea, which is an active element -
 * the user can change data
 *
 * @author Joachim Peer
 */
public class DbTextAreaForBlobsTag extends DbTextAreaTag
   implements javax.servlet.jsp.tagext.TryCatchFinally {
   // logging category for this class
   private String suffix;

   /**
    * DOCUMENT ME!
    *
    * @param suffix DOCUMENT ME!
    */
   public void setSuffix(String suffix) {
      this.suffix = suffix;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getSuffix() {
      return suffix;
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
   public int doEndTag() throws javax.servlet.jsp.JspException {
      try {
         super.doEndTag();

         StringBuffer suffixBuf = new StringBuffer("<input type=\"hidden\" name=\"");
         suffixBuf.append("suffix_" + getFormFieldName());
         suffixBuf.append("\" value=\"");
         suffixBuf.append(suffix);
         suffixBuf.append("\"/>");

         pageContext.getOut()
                    .write(suffixBuf.toString());

         StringBuffer fileNameBuf = new StringBuffer("<input type=\"hidden\" name=\"");
         fileNameBuf.append("fn_" + getFormFieldName());
         fileNameBuf.append("\" value=\"");
         fileNameBuf.append(getFormFieldValue());
         fileNameBuf.append("\"/>");

         pageContext.getOut()
                    .write(fileNameBuf.toString());
      } catch (java.io.IOException e) {
         throw new JspException("IO Error: " + e.getMessage());
      }

      return EVAL_PAGE;
   }


   /**
    * DOCUMENT ME!
    */
   public void doFinally() {
      suffix = null;
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
      super.doStartTag();

      return EVAL_BODY_BUFFERED;
   }
}
