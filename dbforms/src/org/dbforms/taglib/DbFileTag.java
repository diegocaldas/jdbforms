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
public class DbFileTag extends DbBaseInputTag
   implements javax.servlet.jsp.tagext.TryCatchFinally {
   private static Log logCat = LogFactory.getLog(DbFileTag.class.getName());
   private String     accept;

   /**
    * DOCUMENT ME!
    *
    * @param accept DOCUMENT ME!
    */
   public void setAccept(String accept) {
      this.accept = accept;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getAccept() {
      return accept;
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
         StringBuffer tagBuf = new StringBuffer();

         if (hasReadOnlySet() || getParentForm()
                                          .hasReadOnlySet()) {
            // if read-only, remove the browse button (for netscape problem)
            tagBuf.append("<input type=\"text\"");
         } else {
            tagBuf.append("<input type=\"file\"");
         }

         tagBuf.append(prepareName());

         if (accept != null) {
            tagBuf.append(" accept=\"");
            tagBuf.append(accept);
            tagBuf.append("\"");
         }

         tagBuf.append(prepareSize());
         tagBuf.append(prepareKeys());
         tagBuf.append(prepareStyles());
         tagBuf.append(prepareEventHandlers());
         tagBuf.append("/>");

         // Writes out the old field value
         // Joe Peer: this is deadly for FileTags
         //writeOutSpecialValues();
         pageContext.getOut()
                    .write(tagBuf.toString());
      } catch (java.io.IOException ioe) {
         throw new JspException("IO Error: " + ioe.getMessage());
      }

      return EVAL_PAGE;
   }


   /**
    * DOCUMENT ME!
    */
   public void doFinally() {
      accept = null;
      super.doFinally();
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws javax.servlet.jsp.JspException DOCUMENT ME!
    * @throws JspException DOCUMENT ME!
    */
   public int doStartTag() throws javax.servlet.jsp.JspException {
      super.doStartTag();

      if (!getParentForm()
                    .hasMultipartSet()) {
         logCat.warn("DbFileTag is used but DbFormTag.multipart is not set (FALSE)");
         throw new JspException("DbFileTag is used but DbFormTag.multipart is not set (it is set to \"FALSE\"). you must set it to \"TRUE\" to enable file uploads!");
      }

      return SKIP_BODY;
   }
}
