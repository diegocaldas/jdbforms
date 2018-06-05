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

import org.dbforms.config.GrantedPrivileges;
import org.dbforms.config.ResultSetVector;

import org.dbforms.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;



/**
 * <p>
 * This tag renders a Delete Button
 * </p>
 *
 * @author Joachim Peer
 */
public class DbDeleteButtonTag extends AbstractDbBaseButtonTag
   implements javax.servlet.jsp.tagext.TryCatchFinally {
   private static Log logCat   = LogFactory.getLog(DbDeleteButtonTag.class);

   private String associatedRadio;
   private String confirmMessage = null;

   /**
    * DOCUMENT ME!
    *
    * @param associatedRadio DOCUMENT ME!
    */
   public void setAssociatedRadio(String associatedRadio) {
      this.associatedRadio = associatedRadio;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getAssociatedRadio() {
      return associatedRadio;
   }


   /**
    * DOCUMENT ME!
    *
    * @param confirmMessage DOCUMENT ME!
    */
   public void setConfirmMessage(String confirmMessage) {
      this.confirmMessage = confirmMessage;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getConfirmMessage() {
      return confirmMessage;
   }


   /**
    * DOCUMENT ME!
    */
   public void doFinally() {
      confirmMessage  = null;
      associatedRadio = null;
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

      /*
       * 2005-12-12
       * Philip Grunikiewicz
       * 
       * Check table priviledges, if user is not allowed to delete table - don't show button
       */
      if ((getTable() != null) && !getTable().hasUserPrivileg((HttpServletRequest)this.pageContext.getRequest(), GrantedPrivileges.PRIVILEG_DELETE)){
    	  return SKIP_BODY; 
      }

      if (getConfirmMessage() != null) {
         String onclick = (getOnClick() != null) ? getOnClick()
                                                 : "";

         if (onclick.lastIndexOf(";") != (onclick.length() - 1)) {
            onclick += ";"; // be sure javascript end with ";"
         }

         String message = getConfirmMessage();

         if (getParentForm()
                      .hasCaptionResourceSet()) {
            try {
               message = MessageResources.getMessage((HttpServletRequest) pageContext
                                                     .getRequest(),
                                                     getConfirmMessage());
            } catch (Exception e) {
               logCat.debug("confirm(" + getCaption() + ") Exception : "
                            + e.getMessage());
            }
         }

         setOnClick(onclick + "return confirm('" + message + "');");
      }

      if (getParentForm()
                   .isFooterReached()
                && ResultSetVector.isNull(getParentForm().getResultSetVector())) {
         // 20030521 HKK: Bug fixing, thanks to Michael Slack! 
         return SKIP_BODY;
      }

      try {
         // first, determinate the name of the button tag
         StringBuffer tagNameBuf = new StringBuffer("ac_delete");

         if (associatedRadio != null) {
            tagNameBuf.append("ar");
         }

         tagNameBuf.append("_");
         tagNameBuf.append(getTable().getId());

         if (associatedRadio == null) {
            tagNameBuf.append("_");
            tagNameBuf.append(getParentForm().getPositionPath());
         }

         // PG - Render the name unique
         tagNameBuf.append("_");
         tagNameBuf.append(getUniqueID());

         String tagName = tagNameBuf.toString();

         // then render it and its associtated data-tags
         StringBuffer tagBuf = new StringBuffer();

         if (associatedRadio != null) {
            tagBuf.append(getDataTag(tagName, "arname", associatedRadio));
         }

         if (getFollowUp() != null) {
            tagBuf.append(getDataTag(tagName, "fu", getFollowUp()));
         }

         if (getFollowUpOnError() != null) {
            tagBuf.append(getDataTag(tagName, "fue", getFollowUpOnError()));
         }

         tagBuf.append(getButtonBegin());
         tagBuf.append(" name=\"");
         tagBuf.append(tagName);
         tagBuf.append(getButtonEnd());

         pageContext.getOut()
                    .write(tagBuf.toString());
      } catch (java.io.IOException ioe) {
         throw new JspException("IO Error: " + ioe.getMessage());
      }

      if (getChoosenFlavor() == FLAVOR_MODERN) {
         return EVAL_BODY_BUFFERED;
      } else {
         return SKIP_BODY;
      }
   }
}
