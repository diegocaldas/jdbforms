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
import java.io.*;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.log4j.Category;



/**
 * Renders an dbforms style tag
 * @author Joe Peer <joepeer@wap-force.net>
 */
public class TemplateBasedirTag extends BodyTagSupport
{
   static Category logCat  = Category.getInstance(TemplateBasedirTag.class
         .getName()); // logging category for this class
   private String  baseDir;

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws JspException DOCUMENT ME!
    */
   public int doStartTag() throws JspException
   {
      return SKIP_BODY;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws JspException DOCUMENT ME!
    */
   public int doEndTag() throws JspException
   {
      try
      {
         StringBuffer buf = new StringBuffer();
         buf.append(baseDir);
         pageContext.getOut().flush();
         pageContext.getOut().write(buf.toString());
      }
      catch (IOException ioe)
      {
         throw new JspException("Problem including template end - "
            + ioe.toString());
      }

      return EVAL_PAGE;
   }


   /**
    * DOCUMENT ME!
    *
    * @param pageContext DOCUMENT ME!
    */
   public void setPageContext(final javax.servlet.jsp.PageContext pageContext)
   {
      super.setPageContext(pageContext);
      this.baseDir = (String) pageContext.getRequest().getAttribute("baseDir");
   }
}
