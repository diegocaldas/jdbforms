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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.util.PageContextBuffer;

import javax.servlet.Servlet;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;



/**
 * Factory class to render buttons. Can be used to include a button into
 * another tag.  see DbFilterTag for example
 *
 * @author Henner Kollmann
 */
public class DbBaseHandlerFactory {
   private static Log        logCat      = LogFactory.getLog(DbBaseHandlerFactory.class);
   private DbBaseHandlerTag  tag;
   private PageContextBuffer pageContext;

   /**
    * Creates a new DbBaseButtonFactory object.
    *
    * @param parentContext parentContext to send to new tag
    * @param parent parent tag to send to new tag
    * @param clazz the button class to generate
    *
    * @throws JspException exception
    */
   public DbBaseHandlerFactory(PageContext    parentContext,
                               BodyTagSupport parent,
                               Class          clazz) throws JspException {
      try {
         tag         = (DbBaseHandlerTag) clazz.newInstance();
         pageContext = new PageContextBuffer();
         pageContext.initialize((Servlet) parentContext.getPage(),
                                parentContext.getRequest(),
                                parentContext.getResponse(), null, true, 0, true);
         tag.setPageContext(pageContext);
         tag.setParent(parent);
      } catch (Exception e) {
         throw new JspException(e);
      }
   }

   /**
    * gets the generated button tag
    *
    * @return the button tag
    */
   public DbBaseHandlerTag getTag() {
      return tag;
   }


   /**
    * renders the generated button tag into StringBuffer
    *
    * @return the StringBuffer
    *
    * @throws JspException thrown exception
    */
   public StringBuffer render() throws JspException {
      if (tag.doStartTag() != Tag.SKIP_BODY) {
         tag.doEndTag();
      }

      return pageContext.getBuffer();
   }
}
