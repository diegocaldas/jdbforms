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


import org.dbforms.config.DbFormsErrors;
import org.dbforms.config.ResultSetVector;
import org.dbforms.util.Util;



/**********************************************************
 *
 * Grunikiewicz.philip@hydro.qc.ca
 * 2001-12-18
 *
 * Display a custom message if the developer has set a limit on the
 * number of rows to display
 *
 ***********************************************************/
public class HasRecordsTag extends DbBaseHandlerTag
      implements javax.servlet.jsp.tagext.TryCatchFinally
{
   private String        message = null;
   private DbFormsErrors errors;


	public void doFinally()
	{
		message = null;
		errors = null;
		super.doFinally();
	}
   
   
   /**
    * @see javax.servlet.jsp.tagext.TryCatchFinally#doCatch(java.lang.Throwable)
    */
   public void doCatch(Throwable t) throws Throwable
   {
      throw t;
   }


   /**
    * Render the specified error messages if there are any.
    *
    * @exception JspException if a JSP exception has occurred
    */
   public int doStartTag() throws JspException
   {
      if (!ResultSetVector.isNull(getParentForm().getResultSetVector()))
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
         if (rsvSize != 0)
         {
            if (bodyContent != null)
            {
               bodyContent.writeOut(bodyContent.getEnclosingWriter());
               bodyContent.clearBody();
            }
			String message = (errors != null)? errors.getXMLErrorMessage(getMessage()): getMessage();
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