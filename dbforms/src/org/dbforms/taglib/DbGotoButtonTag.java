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
import org.dbforms.event.eventtype.EventType;
import org.dbforms.util.Util;
/****
 *
 * <p>this tag renders a Goto-button.
 *
 * @author Joachim Peer 
 */
public class DbGotoButtonTag extends DbBaseButtonTag implements javax.servlet.jsp.tagext.TryCatchFinally {
   private String destination;
   private String destTable;
   private String destPos;
   private String keyToDestPos;
   private String keyToKeyToDestPos;
   private String singleRow = "false";
   private String childField;
   private String parentField;

   public void doFinally() {
      destination = null;
      destTable = null;
      destPos = null;
      keyToDestPos = null;
      keyToKeyToDestPos = null;
      singleRow = "false";
      super.doFinally();
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
    * @param destination DOCUMENT ME!
    */
   public void setDestination(String destination) {
      this.destination = destination;
   }

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getDestination() {
      return destination;
   }

   /**
    * DOCUMENT ME!
    *
    * @param destTable DOCUMENT ME!
    */
   public void setDestTable(String destTable) {
      this.destTable = destTable;
   }

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getDestTable() {
      return destTable;
   }

   /**
    * DOCUMENT ME!
    *
    * @param destPos DOCUMENT ME!
    */
   public void setDestPos(String destPos) {
      this.destPos = destPos;
   }

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getDestPos() {
      return destPos;
   }

   /**
    * DOCUMENT ME!
    *
    * @param keyToDestPos DOCUMENT ME!
    */
   public void setKeyToDestPos(String keyToDestPos) {
      this.keyToDestPos = keyToDestPos;
   }

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getKeyToDestPos() {
      return keyToDestPos;
   }

   /**
    * DOCUMENT ME!
    *
    * @param keyToKeyToDestPos DOCUMENT ME!
    */
   public void setKeyToKeyToDestPos(String keyToKeyToDestPos) {
      this.keyToKeyToDestPos = keyToKeyToDestPos;
   }

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getKeyToKeyToDestPos() {
      return keyToKeyToDestPos;
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

      try {
         String tagName = EventType.EVENT_NAVIGATION_TRANSFER_GOTO + ((getTable() != null) ? getTable().getId() : -1) + "_" + Integer.toString(getUniqueID());

         StringBuffer tagBuf = new StringBuffer();

         // mask destination as "fu" (FollowUp), so that we can use standard-event dispatching facilities
         // from Controller and dont have to invent something new!
         // #checkme: should we rename destination to followUp ?
         if (destination != null) {
            tagBuf.append(getDataTag(tagName, "fu", destination));
         }

         if (destTable != null) {
            tagBuf.append(getDataTag(tagName, "destTable", destTable));
         }

         // 2004-01-13 HKK: Fix encoding
         tagBuf.append(getDataTag(tagName, "destPos", Util.encode(destPos, pageContext.getRequest().getCharacterEncoding())));
         tagBuf.append(getDataTag(tagName, "keyToDestPos", Util.encode(keyToDestPos, pageContext.getRequest().getCharacterEncoding())));
         tagBuf.append(getDataTag(tagName, "keyToKeyToDestPos", Util.encode(keyToKeyToDestPos, pageContext.getRequest().getCharacterEncoding())));

         // 2004-01-13 HKK: Add support for childField/parentField
         if ((getConfig().getTableByName(destTable) != getTable()) && (getParentForm().getTable() != null)) {
            tagBuf.append(getDataTag(tagName, "srcTable", getParentForm().getTable().getName()));
            tagBuf.append(getDataTag(tagName, "childField", Util.encode(childField, pageContext.getRequest().getCharacterEncoding())));
            tagBuf.append(getDataTag(tagName, "parentField", Util.encode(parentField, pageContext.getRequest().getCharacterEncoding())));
         }

         tagBuf.append(getDataTag(tagName, "singleRow", getSingleRow()));

         tagBuf.append(getButtonBegin());
         tagBuf.append(" name=\"");
         tagBuf.append(tagName);
         tagBuf.append(getButtonEnd());

         pageContext.getOut().write(tagBuf.toString());
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
    * @return the attribute
    */
   public String getSingleRow() {
      return singleRow;
   }

   /**
    * @param string
    */
   public void setSingleRow(String string) {
      singleRow = string;
   }

   /**
    * @return
    */
   public String getChildField() {
      return childField;
   }

   /**
    * @return
    */
   public String getParentField() {
      return parentField;
   }

   /**
    * @param string
    */
   public void setChildField(String string) {
      childField = string;
   }

   /**
    * @param string
    */
   public void setParentField(String string) {
      parentField = string;
   }

}
