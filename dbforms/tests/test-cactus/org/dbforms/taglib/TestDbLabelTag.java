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
import com.meterware.httpunit.WebResponse;

import javax.servlet.jsp.tagext.BodyTag;

import java.util.Locale;

import org.dbforms.config.DbFormsConfigRegistry;
import org.dbforms.servlets.ConfigServlet;
import org.dbforms.util.MessageResources;

/**
 * Tests of the <code>DbLabelTag</code> class.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 *
 */
public class TestDbLabelTag extends JspTestCase {
   private DbLabelTag doubleTag;
   private DbLabelTag timeTag;
   private DbLabelTag nullTag;
   private DbLabelTag nullTagWithNoData;
   private DbFormTag form;

   /**
    * In addition to creating the tag instance and adding the pageContext to
    * it, this method creates a BodyContent object and passes it to the tag.
    */
   public void setUp() throws Exception {
      DbFormsConfigRegistry.instance().register(null);
      config.setInitParameter("dbformsConfig", "/WEB-INF/dbforms-config.xml");
      config.setInitParameter("log4j.configuration", "/WEB-INF/log4j.properties");
      ConfigServlet configServlet = new ConfigServlet();
      configServlet.init(config);

      form = new DbFormTag();
      form.setPageContext(this.pageContext);
      form.setTableName("TIMEPLAN");
      form.setMaxRows("*");

      doubleTag = new DbLabelTag();
      doubleTag.setPageContext(this.pageContext);
      doubleTag.setParent(form);
      doubleTag.setFieldName("D");

      timeTag = new DbLabelTag();
      timeTag.setPageContext(this.pageContext);
      timeTag.setParent(form);
      timeTag.setFieldName("TIME");

      nullTag = new DbLabelTag();
      nullTag.setPageContext(this.pageContext);
      nullTag.setParent(form);
      nullTag.setFieldName("REMARK");
      nullTag.setNullFieldValue("[]");

      nullTagWithNoData = new DbLabelTag();
      nullTagWithNoData.setPageContext(this.pageContext);
      nullTagWithNoData.setParent(form);
      nullTagWithNoData.setFieldName("REMARK");
   }

   //-------------------------------------------------------------------------

   public void testOutputDE() throws Exception {
      MessageResources.setLocale(request, Locale.GERMAN);
      form.doStartTag();
      int result = doubleTag.doEndTag();
      assertEquals(BodyTag.EVAL_PAGE, result);
      result = timeTag.doEndTag();
      assertEquals(BodyTag.EVAL_PAGE, result);
      result = nullTag.doEndTag();
      assertEquals(BodyTag.EVAL_PAGE, result);
      result = nullTagWithNoData.doEndTag();
      assertEquals(BodyTag.EVAL_PAGE, result);
      form.doEndTag();

   }

   public void endOutputDE(WebResponse theResponse) throws Exception {
      String s = theResponse.getText();
      boolean res = s.indexOf("2,3") > -1;
      assertTrue("wrong number",res);
      res = s.indexOf("01.01.1900") > -1;
      assertTrue("wrong date", res);
      res = s.indexOf("[]") > -1;
      assertTrue("wrong setted null field", res);
      res = s.indexOf("[NULL]") > -1;
      assertTrue("wrong null field", res);
   }

   public void testOutputEN() throws Exception {
      MessageResources.setLocale(request, Locale.ENGLISH);
      form.doStartTag();
      int result = doubleTag.doEndTag();
      assertEquals(BodyTag.EVAL_PAGE, result);
      result = timeTag.doEndTag();
      assertEquals(BodyTag.EVAL_PAGE, result);
      result = nullTag.doEndTag();
      assertEquals(BodyTag.EVAL_PAGE, result);
      result = nullTagWithNoData.doEndTag();
      assertEquals(BodyTag.EVAL_PAGE, result);
      form.doEndTag();
   }

   public void endOutputEN(WebResponse theResponse) throws Exception {
      String s = theResponse.getText();
      boolean res = s.indexOf("2.3") > -1;
      assertTrue("wrong number", res);
      res = s.indexOf("1/1/00") > -1;
      assertTrue("wrong date", res);
      res = s.indexOf("[]") > -1;
      assertTrue("wrong setted null field", res);
      res = s.indexOf("[No Data]") > -1;
      assertTrue("wrong null field", res);
   }

	public void testOutputJPN() throws Exception {
		MessageResources.setLocale(request, Locale.JAPANESE);
		form.doStartTag();
		int result = doubleTag.doEndTag();
		assertEquals(BodyTag.EVAL_PAGE, result);
		result = timeTag.doEndTag();
		assertEquals(BodyTag.EVAL_PAGE, result);
      result = nullTag.doEndTag();
      assertEquals(BodyTag.EVAL_PAGE, result);
      result = nullTagWithNoData.doEndTag();
      assertEquals(BodyTag.EVAL_PAGE, result);
		form.doEndTag();

	}

	public void endOutputJPN(WebResponse theResponse) throws Exception {
      String s = theResponse.getText();
      boolean res = s.indexOf("2.3") > -1;
      assertTrue("wrong number", res);
      res = s.indexOf("1900/01/01") > -1;
      assertTrue("wrong date", res);
      res = s.indexOf("[]") > -1;
      assertTrue("wrong setted null field", res);
/* can not be tested! We have no japanese resource bundle! 
      res = s.indexOf("[No Data]") > -1;
      assertTrue("wrong null field", res);
*/      
	}
}