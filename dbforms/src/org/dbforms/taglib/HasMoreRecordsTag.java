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

import org.dbforms.config.DbFormsErrors;

import org.dbforms.util.Util;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;



/**
 * Grunikiewicz.philip 2001-12-18 Display a custom message if the developer has
 * set a limit on the number of rows to display
 */
public class HasMoreRecordsTag extends AbstractDbBaseHandlerTag
   implements javax.servlet.jsp.tagext.TryCatchFinally {
   private transient DbFormsErrors errors;
   private String                  count   = null;
   private String                  message = null;

   /**
    * Sets the count
    *
    * @param count The count to set
    */
   public void setCount(String count) {
      this.count = count;
   }


   /**
    * Gets the count
    *
    * @return Returns a String
    */
   public String getCount() {
      return count;
   }


   /**
    * Gets the count as int
    *
    * @return DOCUMENT ME!
    */
   public int getCountAsInt() {
      return Integer.parseInt(getCount());
   }


   /**
    * Sets the message
    *
    * @param message The message to set
    */
   public void setMessage(String message) {
      this.message = message;
   }


   /**
    * Gets the message
    *
    * @return Returns a String
    */
   public String getMessage() {
      return message;
   }


   /**
    * DOCUMENT ME!
    *
    * @param pageContext DOCUMENT ME!
    */
   public void setPageContext(final javax.servlet.jsp.PageContext pageContext) {
      super.setPageContext(pageContext);
      this.errors = (DbFormsErrors) pageContext.getServletContext()
                                               .getAttribute(DbFormsErrors.ERRORS);
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
      // Get result set vector from parent and calculate size
      try {
         int rsvSize = getParentForm()
                          .getResultSetVector()
                          .size();

         if (rsvSize >= getCountAsInt()) {
            if (bodyContent != null) {
               bodyContent.writeOut(bodyContent.getEnclosingWriter());
               bodyContent.clearBody();
            }

            String pmessage = (errors != null)
                             ? errors.getXMLErrorMessage(getMessage())
                             : getMessage();

            if (!Util.isNull(pmessage)) {
               // Print the results to our output writer
               JspWriter writer = pageContext.getOut();

               try {
                  writer.print(pmessage);
               } catch (IOException e) {
                  throw new JspException(e.toString());
               }
            }
         }
      } catch (java.io.IOException e) {
         throw new JspException("IO Error: " + e.getMessage());
      }

      return EVAL_PAGE;
   }


   /**
    * DOCUMENT ME!
    */
   public void doFinally() {
      count   = null;
      message = null;
      errors  = null;
      super.doFinally();
   }


   /**
    * Render the specified error messages if there are any.
    *
    * @return DOCUMENT ME!
    *
    * @exception JspException if a JSP exception has occurred
    */
   public int doStartTag() throws JspException {
      int rsvSize = getParentForm()
                       .getResultSetVector()
                       .size();

      if (rsvSize >= getCountAsInt()) {
         return EVAL_BODY_BUFFERED;
      } else {
         return SKIP_BODY;
      }
   }
}
