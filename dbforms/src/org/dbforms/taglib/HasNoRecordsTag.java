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


import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Category;

import org.dbforms.util.Util;
import org.dbforms.util.DbFormsErrors;



/**********************************************************
 *
 * Grunikiewicz.philip@hydro.qc.ca
 * 2001-12-18
 *
 * Display a custom message if the developer has set a limit on the
 * number of rows to display
 *
 ***********************************************************/
public class HasNoRecordsTag extends DbBaseHandlerTag
{
   // logging category for this class
   static Category       logCat  = Category.getInstance(HasNoRecordsTag.class
         .getName());
   private String        message = null;
   private DbFormsErrors errors;

   /**
    * Render the specified error messages if there are any.
    *
    * @exception JspException if a JSP exception has occurred
    */
   public int doStartTag() throws JspException
   {
      if (Util.isNull(getParentForm().getResultSetVector()))
      {
         return EVAL_BODY_BUFFERED;
      }
      else
      {
         return SKIP_BODY;
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws javax.servlet.jsp.JspException DOCUMENT ME!
    * @throws JspException DOCUMENT ME!
    */
   public int doEndTag() throws javax.servlet.jsp.JspException
   {
      // Get result set vector from parent and calculate size
      try
      {
         int rsvSize = getParentForm().getResultSetVector().size();

         if (rsvSize == 0)
         {
            if (bodyContent != null)
            {
               bodyContent.writeOut(bodyContent.getEnclosingWriter());
               bodyContent.clearBody();
            }

            String message = org.dbforms.util.XMLErrorsUtil.getXMLErrorMessage(getMessage(),
                  errors);

            if (!Util.isNull(message))
            {
               // Print the results to our output writer
               JspWriter writer = pageContext.getOut();

               try
               {
                  writer.print(message);
               }
               catch (IOException e)
               {
                  throw new JspException(e.toString());
               }
            }
         }
      }
      catch (java.io.IOException e)
      {
         throw new JspException("IO Error: " + e.getMessage());
      }

      return EVAL_PAGE;
   }


   /**
    * Gets the message
    * @return Returns a String
    */
   public String getMessage()
   {
      return message;
   }


   /**
    * Sets the message
    * @param message The message to set
    */
   public void setMessage(String message)
   {
      this.message = message;
   }


   /**
    * DOCUMENT ME!
    *
    * @param pageContext DOCUMENT ME!
    */
   public void setPageContext(final javax.servlet.jsp.PageContext pageContext)
   {
      super.setPageContext(pageContext);
      this.errors = (DbFormsErrors) pageContext.getServletContext()
                                               .getAttribute(DbFormsErrors.ERRORS);
   }
}
