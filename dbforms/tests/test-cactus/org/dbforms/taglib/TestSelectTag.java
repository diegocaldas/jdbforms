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
import org.apache.cactus.WebRequest;

import com.meterware.httpunit.WebResponse;

import java.util.Locale;

import org.dbforms.config.DbFormsConfigRegistry;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.servlets.ConfigServlet;
import org.dbforms.util.MessageResources;
import org.dbforms.util.ParseUtil;

/**
 * Tests of the <code>TestDbTextFieldTag</code> class.
 *
 *
 */
public class TestSelectTag extends JspTestCase {
   private static DbFormsConfig dbconfig;
   StaticDataItem staticDataItem1;
   StaticDataItem staticDataItem2;
   StaticData staticData;
   DbSelectTag selectTag;

   private DbFormTag form;
   public TestSelectTag(String name) throws Exception {
      super(name);

   }
   /**
    * In addition to creating the tag instance and adding the pageContext to
    * it, this method creates a BodyContent object and passes it to the tag.
    */
   public void setUp() throws Exception {
	super.setUp();	

   	initConfig();

      form = new DbFormTag();
      form.setPageContext(this.pageContext);
      form.setTableName("BOOK");
      form.setMaxRows("*");

      selectTag = new DbSelectTag();
      selectTag.setPageContext(this.pageContext);
      selectTag.setParent(form);
      selectTag.setFieldName("AUTHOR_ID");

      staticData = new StaticData();
      staticData.setPageContext(this.pageContext);
      staticData.setParent(selectTag);
      staticData.setName("AUTHOR_ID");

      staticDataItem1 = new StaticDataItem();
      staticDataItem1.setPageContext(this.pageContext);
      staticDataItem1.setParent(staticData);
      staticDataItem1.setKey("1");
      staticDataItem1.setValue("Eco");

      staticDataItem2 = new StaticDataItem();
      staticDataItem2.setPageContext(this.pageContext);
      staticDataItem2.setParent(staticData);
      staticDataItem2.setKey("2");
      staticDataItem2.setValue("Douglas");

      String s = ParseUtil.getParameter(request, "lang");
      MessageResources.setLocale(request, new Locale(s));
   }

   //-------------------------------------------------------------------------

   public void beginStaticDE(WebRequest theRequest) throws Exception {
      theRequest.addParameter("lang", Locale.GERMAN.toString());
   }

   public void testStaticDE() throws Exception {
      Locale locale = MessageResources.getLocale(request);
      assertTrue("no german locale", locale.equals(Locale.GERMAN));
      doTheTest();
   }

   public void endStaticDE(WebResponse theResponse) throws Exception {
      String s = theResponse.getText();
      System.out.println(s);
   }


	public void beginStaticJPN(WebRequest theRequest) throws Exception {
		theRequest.addParameter("lang", Locale.JAPANESE.toString());
	}

	public void testStaticJPN() throws Exception {
		Locale locale = MessageResources.getLocale(request);
		assertTrue("no japanese locale", locale.equals(Locale.JAPANESE));
		doTheTest();
	}

	public void endStaticJPN(WebResponse theResponse) throws Exception {
		String s = theResponse.getText();
		System.out.println(s);
	}

   private void initConfig() throws Exception {
      if (dbconfig == null) {
         DbFormsConfigRegistry.instance().register(null);
         config.setInitParameter("dbformsConfig", "/WEB-INF/dbforms-config.xml");
         config.setInitParameter("log4j.configuration", "/WEB-INF/log4j.properties");
         ConfigServlet configServlet = new ConfigServlet();
         configServlet.init(config);
         dbconfig = DbFormsConfigRegistry.instance().lookup();
         if (dbconfig == null)
            throw new NullPointerException("not able to create dbconfig object!");
      }
   }

   private void doTheTest() throws Exception {
      form.doStartTag();
      selectTag.doStartTag();
      staticData.doStartTag();
      staticDataItem1.doStartTag();
      staticDataItem1.doEndTag();
      staticDataItem1.doFinally();
      staticDataItem2.doStartTag();
      staticDataItem2.doEndTag();
      staticDataItem2.doFinally();
      staticData.doEndTag();
      staticData.doFinally();
      selectTag.doEndTag();
      selectTag.doFinally();
      form.doEndTag();
      form.doFinally();

   }
}
