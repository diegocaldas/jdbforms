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
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

import org.dbforms.util.*;
import org.apache.log4j.Category;



/****
 *
 * this tag renders a dabase-datadriven LABEL, which is apassive element (it can't be changed by
 * the user) - it is predestinated for use with read-only data (i.e. primary keys you don't want
 * the user to change, etc)
 *
 * @author Joachim Peer <j.peer@gmx.net>
 */
public class DbSortTag extends DbBaseHandlerTag
      implements javax.servlet.jsp.tagext.TryCatchFinally
{
   private Category   logCat     = Category.getInstance(this.getClass().getName()); // logging category for this class

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
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws javax.servlet.jsp.JspException DOCUMENT ME!
    * @throws JspException DOCUMENT ME!
    */
   public int doEndTag() throws javax.servlet.jsp.JspException
   {
      try
      {
         int tableId = getParentForm().getTable().getId();
         int fieldId = getField().getId();

         if (!getField().getKey() && !getField().isSortable())
         {
            logCat.warn("you should declare " + getField().getName()
               + " as key or as sortable in your config file, if you use it as ordering field!");
         }

         HttpServletRequest request = (HttpServletRequest) pageContext
            .getRequest();

         StringBuffer       paramNameBuf = new StringBuffer("sort_");
         paramNameBuf.append(tableId);
         paramNameBuf.append("_");
         paramNameBuf.append(fieldId);

         String       paramName = paramNameBuf.toString();
         String       oldValue = ParseUtil.getParameter(request, paramName);

         StringBuffer tagBuf = new StringBuffer();
         tagBuf.append("<select name=\"");
         tagBuf.append(paramName);
         tagBuf.append(
            "\" size=\"0\" onChange=\"javascript:document.dbform.submit()\" >");

         String strAsc  = "Ascending";
         String strDesc = "Descending";
         String strNone = "None";

         // Internationalization			
         if (getParentForm().hasCaptionResourceSet())
         {
            Locale reqLocale = MessageResources.getLocale(request);

            // get message resource or if null take the default (english)
            strAsc     = MessageResources.getMessage("dbforms.select.sort.ascending",
                  reqLocale, "Ascending");
            strDesc    = MessageResources.getMessage("dbforms.select.sort.descending",
                  reqLocale, "Descending");
            strNone    = MessageResources.getMessage("dbforms.select.sort.none",
                  reqLocale, "None");
         }

         // ---- ascending ----
         tagBuf.append("<option value=\"asc\"");

         if ("asc".equalsIgnoreCase(oldValue))
         {
            tagBuf.append(" selected ");
         }

         tagBuf.append(">").append(strAsc);

         // ---- descending ----
         tagBuf.append("<option value=\"desc\"");

         if ("desc".equalsIgnoreCase(oldValue))
         {
            tagBuf.append(" selected ");
         }

         tagBuf.append(">").append(strDesc);

         // ---- no sorting ----
         tagBuf.append("<option value=\"none\" ");

         if ((oldValue == null) || "none".equals(oldValue))
         {
            tagBuf.append(" selected ");
         }

         tagBuf.append(">").append(strNone);

         tagBuf.append("</select>");

         pageContext.getOut().write(tagBuf.toString());
      }
      catch (java.io.IOException ioe)
      {
         throw new JspException("IO Error: " + ioe.getMessage());
      }

      return EVAL_PAGE;
   }


}
