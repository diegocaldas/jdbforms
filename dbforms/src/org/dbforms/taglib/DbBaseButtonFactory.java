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
import org.dbforms.util.PageContextBuffer;
import javax.servlet.Servlet;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.log4j.Category;



/**
 * 
 * Factory class to render buttons.
 * Can be used to include a button into another tag. 
 * see DbFilterTag for example 
 * 
 * @author Henner Kollmann <Henner.Kollmann@gmx.de>
 */
public class DbBaseButtonFactory
{
   private DbBaseButtonTag   btn;
   private PageContextBuffer pageContext;
   static Category           logCat = Category.getInstance(
                                               DbBaseButtonFactory.class.getName());

   /**
    * Creates a new DbBaseButtonFactory object.
    *
    * @param parentContext parentContext to send to new tag
    * @param parent        parent tag to send to new tag
    * @param clazz         the button class to generate 
    *
    * @throws JspException exception
    */
   public DbBaseButtonFactory(PageContext parentContext, BodyTagSupport parent, 
                              Class clazz) throws JspException
   {
      try
      {
         btn         = (DbBaseButtonTag) clazz.newInstance();
         pageContext = new PageContextBuffer();
         pageContext.initialize((Servlet) parentContext.getPage(), 
                                parentContext.getRequest(), 
                                parentContext.getResponse(), null, true, 0, 
                                true);
         btn.setPageContext(pageContext);
         btn.setParent(parent);
      }
      catch (Exception e)
      {
         throw new JspException(e);
      }
   }

   /**
    * gets the generated button tag
    *
    * @return the button tag
    */
   public DbBaseButtonTag getButton()
   {
      return btn;
   }


   /**
    * renders the generated button tag into StringBuffer
    *
    * @return the StringBuffer
    *
    * @throws JspException thrown exception
    */
   public StringBuffer render() throws JspException
   {
      if (btn.doStartTag() != BodyTagSupport.SKIP_BODY)
      {
         btn.doEndTag();
      }

      return pageContext.getBuffer();
   }
}