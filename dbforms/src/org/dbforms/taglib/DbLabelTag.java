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
import org.apache.log4j.Category;
import org.dbforms.util.Util;



/**
 * this tag renders a dabase-datadriven LABEL, which is apassive element (it can't be changed by
 * the user) - it is predestinated for use with read-only data (i.e. primary keys you don't want
 * the user to change, etc)
 *
 * @author  Joachim Peer <j.peer@gmx.net>
 * @created  29 agosto 2002
 */
public class DbLabelTag extends DbBaseHandlerTag
      implements javax.servlet.jsp.tagext.TryCatchFinally
{
   // logging category for this class
   static Category logCat = Category.getInstance(DbLabelTag.class.getName());

	public void doFinally()
	{
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
    *  Description of the Method
    *
    * @return  Description of the Return Value
    * @exception  javax.servlet.jsp.JspException Description of the Exception
    */
   public int doEndTag() throws javax.servlet.jsp.JspException
   {
      try
      {
         String fieldValue = getFormattedFieldValue();

         // PG, 2001-12-14
         // If maxlength was input, trim display
         String size = null;

         if (((size = this.getMaxlength()) != null)
                   && (size.trim().length() > 0))
         {
            //convert to int
            int count = Integer.parseInt(size);

            // Trim and add trim indicator (...)
            if (count < fieldValue.length())
            {
               fieldValue = fieldValue.substring(0, count);
               fieldValue += "...";
            }
         }

         // SM 2003-08-05
         // if styleClass is present, render a SPAN with text included
         String s = prepareStyles();

         if (Util.isNull(s))
         {
            pageContext.getOut().write(fieldValue.toString());
         }
         else
         {
            pageContext.getOut()
                       .write("<span " + s + "\">" + fieldValue + "</span>");
         }
      }
      catch (java.io.IOException ioe)
      {
         // better to KNOW what happended !
         logCat.error("::doEndTag - IO Error", ioe);
         throw new JspException("IO Error: " + ioe.getMessage());
      }
      catch (Exception e)
      {
         // better to KNOW what happended !
         logCat.error("::doEndTag - general exception", e);
         throw new JspException("Error: " + e.getMessage());
      }

      return EVAL_PAGE;
   }
}