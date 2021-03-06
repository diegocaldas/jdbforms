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
 * the idea for this class was taken from the class ErrorsTag-class of the Apache Struts Project
 */
package org.dbforms.taglib;

import org.dbforms.util.MessageResources;

import java.io.IOException;

import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;



/**
 * Custom tag that renders error messages if an appropriate request attribute
 * has been created.
 *
 * <p>
 * This idea for this class came from a - much more powerful - class done by
 * Craig R. McClanahan for the Apache struts framework.
 * </p>
 *
 * @author Joe Peer
 */
public class ErrorsTag extends AbstractScriptHandlerTag
   implements javax.servlet.jsp.tagext.TryCatchFinally {
   private String caption       = "Error:";
   private String messagePrefix;
   private String name = "errors";

   /**
    * DOCUMENT ME!
    *
    * @param caption DOCUMENT ME!
    */
   public void setCaption(String caption) {
      this.caption = caption;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getCaption() {
      return (this.caption);
   }


   /**
    * Insert the method's description here. Creation date: (2001-05-10
    * 12:57:08)
    *
    * @param newMessagePrefix java.lang.String
    */
   public void setMessagePrefix(java.lang.String newMessagePrefix) {
      messagePrefix = newMessagePrefix;
   }


   /**
    * Insert the method's description here. Creation date: (2001-05-10
    * 12:57:08)
    *
    * @return java.lang.String
    */
   public java.lang.String getMessagePrefix() {
      return messagePrefix;
   }


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
      return (this.name);
   }


   /**
    * DOCUMENT ME!
    */
   public void doFinally() {
      messagePrefix = null;
      name          = "errors";
      caption       = "Error:";
   }


   // ------------------------------------------------------- Public Methods

   /**
    * Render the specified error messages if there are any.
    *
    * @return DOCUMENT ME!
    *
    * @exception JspException if a JSP exception has occurred
    */
   public int doStartTag() throws JspException {
      Vector             originalErrors = (Vector) pageContext.getAttribute(getName(),
                                                                            PageContext.REQUEST_SCOPE);
      HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

      if ((originalErrors != null) && (originalErrors.size() > 0)) {
         // Extract out only user defined text
         Vector       errors = this.extractUserDefinedErrors(originalErrors);

         StringBuffer results = new StringBuffer();
         results.append(caption);

         results.append("<ul>");

         for (int i = 0; i < errors.size(); i++) {
            results.append("<li>");
            results.append(MessageResources.getMessage(request,
                                                       (String) errors
                                                       .elementAt(i)));
            results.append("</li>");
         }

         results.append("</ul>");

         // Print the results to our output writer
         JspWriter writer = pageContext.getOut();

         try {
            writer.print(results.toString());
         } catch (IOException e) {
            throw new JspException(e.toString());
         }
      }

      // Continue processing this page
      return EVAL_BODY_INCLUDE;
   }


   /**
    * philip.grunikiewicz 2001-05-10 Iterate through the errors and extract
    * only user-specified text
    *
    * @param errors DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public Vector extractUserDefinedErrors(Vector errors) {
      Vector newErrors = new Vector();
      String message = null;
      int    index   = 0;

      //Get user defined delimiter from messagePrefix attribute
      String delimiter = this.getMessagePrefix();

      if (errors != null) {
         Enumeration e = errors.elements();

         while (e.hasMoreElements()) {
            message = ((Exception) e.nextElement()).getMessage();

            //Check for delimiter
            if ((delimiter != null)
                      && ((index = message.indexOf(delimiter)) != -1)) {
               // Add only what is needed
               message = message.substring(index + delimiter.length());
            }

            newErrors.add(message);
         }
      }

      return newErrors;
   }
}
