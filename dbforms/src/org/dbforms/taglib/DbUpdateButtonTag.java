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

import org.dbforms.config.GrantedPrivileges;
import org.dbforms.config.ResultSetVector;

import org.dbforms.util.Util;

import org.dbforms.validation.ValidatorConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;



/**
 * <p>
 * this tag renders an update-button. #fixme - define abstract base class
 * [should be fixed in release 0.6]
 * </p>
 *
 * @author Joachim Peer
 */
public class DbUpdateButtonTag extends AbstractDbBaseButtonTag
   implements javax.servlet.jsp.tagext.TryCatchFinally {
   private String associatedRadio;
   private String showAlways = "false";

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
    * @param string
    */
   public void setShowAlways(String string) {
      showAlways = string;
   }


   /**
    * DOCUMENT ME!
    *
    * @return
    */
   public String getShowAlways() {
      return showAlways;
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
      associatedRadio = null;
      showAlways      = "false";
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
       * Check table priviledges, if user is not allowed to update table - don't show button
       */
      if (!getTable().hasUserPrivileg((HttpServletRequest)this.pageContext.getRequest(), GrantedPrivileges.PRIVILEG_UPDATE)){
    	  return SKIP_BODY; 
      }

      if (!Util.getTrue(showAlways)
                && getParentForm()
                            .isFooterReached()
                && ResultSetVector.isNull(getParentForm().getResultSetVector())) {
         // 20030521 HKK: Bug fixing, thanks to Michael Slack! 
         return SKIP_BODY;
      }

      try {
         // first, determinate the name of the button tag
         StringBuffer tagNameBuf = new StringBuffer("ac_update");

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
         tagNameBuf.append(Integer.toString(getUniqueID()));

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


   /**
    * returns the JavaScript validation flags. Will be put into the onClick
    * event of the main form Must be overloaded by update and delete button
    *
    * @return the java script validation vars.
    */
   protected String JsValidation() {
      return (ValidatorConstants.JS_CANCEL_VALIDATION + "=true;"
             + ValidatorConstants.JS_UPDATE_VALIDATION_MODE + "=true;");
   }
}
