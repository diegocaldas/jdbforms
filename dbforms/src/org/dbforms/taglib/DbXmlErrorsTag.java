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
/*
 * Grunikiewicz.philip@hydro.qc.ca
 * 2001-12-18
 *
 * Custom tag that renders error messages if an appropriate request attribute
 * has been created.
 *
 * Error messages are retrieved via an xml file
 */
package org.dbforms.taglib;

import java.util.Enumeration;
import java.util.Vector;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.dbforms.config.DbFormsErrors;



/**
 * DOCUMENT ME!
 *
 * @version $Revision$
 * @author $author$
 */
public class DbXmlErrorsTag extends TagSupportWithScriptHandler  
		implements javax.servlet.jsp.tagext.TryCatchFinally
{
   // ----------------------------------------------------------- Properties

   /**
    * Name of the request scope attribute containing our error messages,
    * if any.
    */
   private String name = "errors";
   private String      caption = "Error:";
   private DbFormsErrors errors;

	public void doFinally()
	{
		name  = "errors";
		caption = "Error:";
		errors = null;
	}

	public void doCatch(Throwable t) throws Throwable
	{
		throw t;
	}

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getName()
   {
      return (this.name);
   }


   /**
    * DOCUMENT ME!
    *
    * @param name DOCUMENT ME!
    */
   public void setName(String name)
   {
      this.name = name;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getCaption()
   {
      return (this.caption);
   }


   /**
    * DOCUMENT ME!
    *
    * @param caption DOCUMENT ME!
    */
   public void setCaption(String caption)
   {
      this.caption = caption;
   }


   // ------------------------------------------------------- Public Methods

   /**
    * Render the specified error messages if there are any.
    *
    * @exception JspException if a JSP exception has occurred
    */
   public int doStartTag() throws JspException
   {
      Vector transformedErrors = new Vector();

      Vector originalErrors = (Vector) pageContext.getAttribute(name,
            PageContext.REQUEST_SCOPE);

      if (errors == null)
      {
         throw new JspException(
            "XML error handler is disabled, please supply xml error file on startup or use error tag instead!");
      }

      if ((originalErrors != null) && (originalErrors.size() > 0))
      {
         Enumeration enum = originalErrors.elements();

         while (enum.hasMoreElements())
         {
            Exception ex = (Exception) enum.nextElement();

            String    result = errors.getXMLErrorMessage(ex.getMessage());

            // ignore empty messages
            if (result != null)
            {
               transformedErrors.add(result);
            }
         }

         StringBuffer results = new StringBuffer();
         results.append(caption);
         results.append("<ul>");

         for (int i = 0; i < transformedErrors.size(); i++)
         {
            results.append("<li>");
            results.append(transformedErrors.elementAt(i));
            results.append("</li>");
         }

         results.append("</ul>");

         // Print the results to our output writer
         JspWriter writer = pageContext.getOut();

         try
         {
            writer.print(results.toString());
         }
         catch (IOException e)
         {
            throw new JspException(e.toString());
         }
      }

      // Continue processing this page
      return EVAL_BODY_INCLUDE;
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
