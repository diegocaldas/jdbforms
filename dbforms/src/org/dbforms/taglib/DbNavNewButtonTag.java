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

import org.dbforms.event.eventtype.EventType;

import org.dbforms.util.Util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;



/**
 * this tag renders an "new"-button.
 *
 * @author Joachim Peer
 */
public class DbNavNewButtonTag extends AbstractDbBaseButtonTag
   implements javax.servlet.jsp.tagext.TryCatchFinally {
   private String destTable;

   /** Holds value of property showAlwaysInFooter. */
   private String showAlwaysInFooter = "true";

   /**
    * Sets the destTable
    *
    * @param destTable The destTable to set
    */
   public void setDestTable(String destTable) {
      this.destTable = destTable;
   }


   /**
    * Gets the destTable
    *
    * @return Returns a String
    */
   public String getDestTable() {
      return destTable;
   }


   /**
    * Setter for property showAlwaysInFooter.
    *
    * @param showAlwaysInFooter New value of property showAlwaysInFooter.
    */
   public void setShowAlwaysInFooter(String showAlwaysInFooter) {
      this.showAlwaysInFooter = showAlwaysInFooter;
   }


   /**
    * Getter for property showAlwaysInFooter.
    *
    * @return Value of property showAlwaysInFooter.
    */
   public String getShowAlwaysInFooter() {
      return this.showAlwaysInFooter;
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
      destTable          = null;
      showAlwaysInFooter = "true";
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
       * Check table priviledges, if user is not allowed to insert into table - don't show button
       */
      if ((getTable() != null) && !getTable().hasUserPrivileg((HttpServletRequest)this.pageContext.getRequest(), GrantedPrivileges.PRIVILEG_INSERT)){
    	  return SKIP_BODY; 
      }

      if (getParentForm()
                   .isFooterReached()
                && ResultSetVector.isNull(getParentForm().getResultSetVector())
                && !Util.getTrue(showAlwaysInFooter)) {
         // 20030521 HKK: Bug fixing, thanks to Michael Slack! 
         return SKIP_BODY;
      }

      try {
         StringBuffer tagBuf = new StringBuffer();

         // Allow navNewButton to specify a table for insertion other than the
         // parent table. (Contrib. John Madsen)		  
         int tableId = ((destTable != null) && (destTable.length() != 0))
                       ? getConfig()
                            .getTableByName(destTable)
                            .getId()
                       : getTable()
                            .getId();

         String tagName = EventType.EVENT_NAVIGATION_TRANSFER_NEW + tableId
                          + "_" + Integer.toString(getUniqueID());

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
