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

import java.util.Vector;
import java.sql.Connection;

import javax.servlet.http.*;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.*;
import org.apache.log4j.Category;



/**
 * DOCUMENT ME!
 *
 * @version $Revision$
 * @author $author$
 */
public class StaticData extends BodyTagSupport
{
   static Category logCat = Category.getInstance(StaticData.class.getName()); // logging category for this class

   /** DOCUMENT ME! */
   protected Vector data;

   /** DOCUMENT ME! */
   protected String name;

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws JspException DOCUMENT ME!
    */
   public int doStartTag() throws JspException
   {
      // was the data generated by another instance on the same page yet?
      data = (Vector) pageContext.getAttribute(name, PageContext.PAGE_SCOPE);

      // if not, we do it
      // (the embedded  staticDataItem's will fill the hashtable)
      if (data == null)
      {
         data = new Vector();

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
      // make parsed data available to other instances
      pageContext.setAttribute(name, data, PageContext.PAGE_SCOPE);

      return SKIP_BODY; // 1 iteration only
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
      ((DataContainer) getParent()).setEmbeddedData(data); // DbBaseMultiTag are: select, radio, checkbox!

      return EVAL_PAGE;
   }


   /**
   for use from parent element [radio, select, etc.]
   */
   protected Vector fetchData(Connection con)
   {
      return data;
   }


   /**
   for use from StaticDataItems
   */
   public Vector getData()
   {
      return data;
   }


   /**
   returns the unique name of the embedded data
   */
   public String getName()
   {
      return name;
   }


   /**
   set the name of the embedded data.
   every embedded data entity on a jsp page has to have a unique name. this name is used for
   storing (caching) and retrieving data in Page-Scope. this is useful if a tag gets evaluated
   many times -> we do not the tag get parsed more than once

   #fixme: encode name in order to avoid naming conficts!

   */
   public void setName(String name)
   {
      this.name = name;
   }
}
