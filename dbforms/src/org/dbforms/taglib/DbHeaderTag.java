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
import javax.servlet.jsp.tagext.*;

import org.apache.log4j.Category;



/****
 *
 * this tag renders a Header-tag. it is supposed to be nested within a DbFormTag.
 * because this tag is nested within a DbFormTag it is invoked every time the parent dbFormTag gets
 * evaluated, but it gets only rendered at the FIRST evalation-loop.
 *
 * @author Joachim Peer <j.peer@gmx.net>
 */
public class DbHeaderTag extends BodyTagSupport
{
   static Category logCat = Category.getInstance(DbHeaderTag.class.getName()); // logging category for this class

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public int doStartTag()
   {
      DbFormTag myParent = (DbFormTag) findAncestorWithClass(this,
            DbFormTag.class);

      if (myParent.getCurrentCount() == 0)
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
    * @throws JspException DOCUMENT ME!
    */
   public int doAfterBody() throws JspException
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
         if (bodyContent != null)
         {
            bodyContent.writeOut(bodyContent.getEnclosingWriter());
            bodyContent.clearBody(); // 2002116-HKK: workaround for duplicate rows in Tomcat 4.1
         }
      }
      catch (java.io.IOException e)
      {
         throw new JspException("IO Error: " + e.getMessage());
      }

      return EVAL_PAGE;
   }
}
