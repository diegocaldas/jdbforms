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

import org.apache.cactus.JspTestCase;

import org.dbforms.config.DbFormsConfigRegistry;

import org.dbforms.servlets.ConfigServlet;

import java.sql.Connection;

import javax.servlet.jsp.tagext.BodyTag;



/**
 * Tests of the <code>BaseTag</code> class.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh </a>
 *
 */
public class TestDbGetConnectionTag extends JspTestCase {
   private DbGetConnectionTag tag;

   /**
    * In addition to creating the tag instance and adding the pageContext to
    * it, this method creates a BodyContent object and passes it to the tag.
    */
   public void setUp() throws Exception {
      super.setUp();

      DbFormsConfigRegistry.instance()
                           .register(null);

      config.setInitParameter("dbformsConfig", "/WEB-INF/dbforms-config.xml");
      config.setInitParameter("log4j.configuration", "/WEB-INF/log4j.properties");

      ConfigServlet configServlet = new ConfigServlet();
      configServlet.init(config);
      tag = new DbGetConnectionTag();
      tag.setId("con");
      tag.setPageContext(this.pageContext);
   }


   //-------------------------------------------------------------------------
   public void testDbConnectionTag() throws Exception {
      //none of the other life cycle methods need to be implemented, so they
      //do not need to be called.
      int result = this.tag.doStartTag();
      assertEquals(BodyTag.EVAL_BODY_INCLUDE, result);

      Connection con = (Connection) this.pageContext.getAttribute("con");
      assertTrue("con != null", con != null);
      assertTrue("con is opened", !con.isClosed());
      result = this.tag.doEndTag();
      assertEquals(BodyTag.EVAL_PAGE, result);
      assertTrue("con is closed", con.isClosed());
      con = (Connection) this.pageContext.getAttribute("con");
      assertTrue("con == null", con == null);
   }
}
