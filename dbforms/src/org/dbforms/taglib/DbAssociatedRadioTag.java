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
 * This tag enables the end-user to define a row by selecting the radio-button
 * rendered by this tag
 * </p>
 *
 * <p>
 * This tag enables the end-user to define a row by selecting the radio-button
 * rendered by this tag
 * </p>
 * &lt;tagclass&gt;org.dbforms.taglib.DbAssociatedRadioTag&lt;/tagclass&gt;
 * &lt;bodycontent&gt;empty&lt;/bodycontent&gt;
 *
 * <p>
 * example: imagine a table customer, which should be listed. the user should
 * be able to delete a customer.
 * </p>
 * in that case the application developer has to alternatives: to put a
 * "deleteButton" into the body -> this button gets rendered for every row<br>
 * if the user klicks the button the associated data row gets deleted. the
 * disadvantage of this method is that multiple buttons must be rendered,
 * which takes away lots of space  and makes layouting more difficult to put
 * an "associatedRadio" into the body and the "deleteButton" on the footer (or header)<br>
 * the radio element gets rendered for every row, the deleteButton just once.
 * if the user wants to delete a row, he/she has to select the radioButton (to
 * mark the row he/she wants to be deleted) and then to press the
 * deleteButton.
 *
 * <p>
 * the more buttons you have the better this method is!!
 * </p>
 *
 * <p>
 * nota bene: you have to tell the delete (or insert, update...) - button that
 * there is an associated radio button that marks the row the action should be
 * applied to, by defining the "associatedRadio" attribute of that respective
 * button
 * </p>
 *
 * <p></p>
 *
 * @author Joachim Peer
 */
public class DbAssociatedRadioTag extends DbBaseHandlerTag
   implements javax.servlet.jsp.tagext.TryCatchFinally {
   private String name;

   /**
    * DOCUMENT ME!
    *
    * @param name DOCUMENT ME!
    */
   public void setName(String name) {
      this.name = name;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getName() {
      return name;
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
         StringBuffer tagBuf = new StringBuffer("<input type=\"radio\" name=\"");
         tagBuf.append(name);
         tagBuf.append("\" value=\"");
         tagBuf.append(getParentForm().getTable().getId());
         tagBuf.append("_");
         tagBuf.append(getParentForm().getPositionPath());

         // 20020705-HKK: Set defaultChecked for first row!  
         tagBuf.append("\"");

         if (getParentForm()
                      .getCurrentCount() == 0) {
            tagBuf.append(" checked=\"true\"");
         }

         tagBuf.append("/>");
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
      name = null;
      super.doFinally();
   }
}
