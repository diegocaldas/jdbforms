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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import org.dbforms.util.Util;



/**
 * #fixme docu to come
 *
 * @author Joe Peer
 *
 */
public class DbBlobURLTag extends AbstractDbBaseHandlerTag
   implements javax.servlet.jsp.tagext.TryCatchFinally {
   /** DOCUMENT ME! */
   protected String nameField;

   /**
    * DOCUMENT ME!
    *
    * @param nameField DOCUMENT ME!
    */
   public void setNameField(String nameField) {
      this.nameField = nameField;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getNameField() {
      return nameField;
   }



   // --------------------------------------------------------- Public Methods
   // DbForms specific

   /**
    * Description of the Method
    *
    * @return Description of the Return Value
    *
    * @exception javax.servlet.jsp.JspException Description of the Exception
    */
   public int doEndTag() throws javax.servlet.jsp.JspException {
      try {
         StringBuffer tagBuf = new StringBuffer(((HttpServletRequest) pageContext
                                                 .getRequest()).getContextPath());

         tagBuf.append("/servlet/file?tf=")
               .append(getTableFieldCode())
               .append("&keyval=")
               .append(getKeyVal());

         // JPEer 03/2004
         if (this.nameField != null) {
            tagBuf.append("&nf=");
            tagBuf.append(nameField);
         }

         // append the defaultValue parameter;
         if (getDefaultValue() != null) {
            tagBuf.append("&defaultValue=" + getDefaultValue());

            //logCat.info("::doEndTag - defaultValue set to [" + defaultValue + "]");
         }

		 // fix issue where DbBlobURLTag does not work properly when more 
		 // than 1 connection defined in dbforms-config.xml . Nic Parise 10/2005
		 String dbConnectionName = getParentForm().getDbConnectionName();         
		 if (!Util.isNull(dbConnectionName)) {
	       tagBuf.append("&invname_"+getParentForm().getTable().getId());
		   tagBuf.append("=").append(dbConnectionName);
		 }
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
      super.doFinally();
   }


   /**
    * Gets the keyVal attribute of the DbBlobURLTag object
    *
    * @return The keyVal value
    */
   protected String getKeyVal() {
      return getParentForm()
                .getTable()
                .getKeyPositionString(getParentForm().getResultSetVector());
   }


   // ------------------------------------------------------ Protected Methods
   // DbForms specific

   /**
    * Generates the decoded name.
    *
    * @return The tableFieldCode value
    */
   private String getTableFieldCode() {
      StringBuffer buf = new StringBuffer();

      buf.append(getParentForm().getTable().getId());
      buf.append("_");
      buf.append(getField().getId());

      return buf.toString();
   }
}
