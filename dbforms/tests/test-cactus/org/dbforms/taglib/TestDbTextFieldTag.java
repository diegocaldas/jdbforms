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


import javax.servlet.jsp.tagext.BodyTag;

import java.util.Locale;
import java.util.Date;

import org.dbforms.config.DbFormsConfigRegistry;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.FieldValues;
import org.dbforms.config.FieldValue;
import org.dbforms.config.Table;
import org.dbforms.event.eventtype.EventType;
import org.dbforms.event.DatabaseEvent;
import org.dbforms.event.datalist.DeleteEvent;
import org.dbforms.servlets.ConfigServlet;
import org.dbforms.util.MessageResources;
import org.dbforms.util.ParseUtil;

/**
 * Tests of the <code>TestDbTextFieldTag</code> class.
 *
 *
 */
public class TestDbTextFieldTag extends JspTestCase {
   private DbTextFieldTag doubleTag;
   private DbTextFieldTag timeTag;
   private DbFormTag form;

   private static DbFormsConfig dbconfig;

   public TestDbTextFieldTag(String name) throws Exception {
      super(name);

   }
   /**
    * In addition to creating the tag instance and adding the pageContext to
    * it, this method creates a BodyContent object and passes it to the tag.
    */
   public void setUp() throws Exception {
      initConfig();

      String s = ParseUtil.getParameter(request, "lang");
      assertTrue(s != null);
      Locale locale = new Locale(s);
      MessageResources.setLocale(request, locale);

      form = new DbFormTag();
      form.setPageContext(this.pageContext);
      form.setTableName("TIMEPLAN");
      form.setMaxRows("*");

      doubleTag = new DbTextFieldTag();
      doubleTag.setPageContext(this.pageContext);
      doubleTag.setParent(form);
      doubleTag.setFieldName("D");

      timeTag = new DbTextFieldTag();
      timeTag.setPageContext(this.pageContext);
      timeTag.setParent(form);
      timeTag.setFieldName("TIME");

      form.doStartTag();



      Table table = dbconfig.getTableByName("TIMEPLAN");
      DatabaseEvent dbEvent = new DeleteEvent(new Integer(table.getId()), "null", request, dbconfig);
      // Set type to delete so that all fieldvalues will be parsed!!
      dbEvent.setType(EventType.EVENT_DATABASE_DELETE);
      FieldValues fv = dbEvent.getFieldValues();

      FieldValue f = fv.get("TIME");
      Date testDate = (Date) f.getFieldValueAsObject();
      assertTrue(testDate instanceof java.sql.Date);
      assertTrue(testDate.getTime() == ((Date)timeTag.getFieldObject()).getTime());

    
      f = fv.get("D");
      Double testNumber = (Double) f.getFieldValueAsObject();
      assertTrue(testNumber instanceof Double);
      assertTrue(testNumber.equals(doubleTag.getFieldObject()));

      int result = doubleTag.doEndTag();
      assertEquals(BodyTag.EVAL_PAGE, result);

      result = timeTag.doEndTag();
      assertEquals(BodyTag.EVAL_PAGE, result);

      form.doEndTag();
      form.doFinally();

   }

   //-------------------------------------------------------------------------

   public void beginOutputDE(WebRequest theRequest) throws Exception {
      theRequest.addParameter("lang", "de");
      theRequest.addParameter("f_8_null_1", "2,3");
      theRequest.addParameter("of_8_null_1", "2,3");
      theRequest.addParameter("pf_8_null_1", "#,##0.###");
      theRequest.addParameter("f_8_null_0", "01.01.1900");
      theRequest.addParameter("of_8_null_0", "01.01.1900");
      theRequest.addParameter("pf_8_null_0", "dd.MM.yyyy");
   }

   public void testOutputDE() throws Exception {}

   public void endOutputDE(WebResponse theResponse) throws Exception {
      String s = theResponse.getText();
      boolean res = s.indexOf("value=\"2,3\"") > -1;
      assertTrue("not found: " + "value=\"2,3\"", res);
      res = s.indexOf("value=\"01.01.1900\"") > -1;
      assertTrue("not found: " + "value=\"01.01.1900\"", res);
   }

   public void beginOutputEN(WebRequest theRequest) throws Exception {
      theRequest.addParameter("lang", "en");
      theRequest.addParameter("f_8_null_1", "2.3");
      theRequest.addParameter("of_8_null_1", "2.3");
      theRequest.addParameter("pf_8_null_1", "#,##0.###");
      theRequest.addParameter("f_8_null_0", "Jan 1, 1900");
      theRequest.addParameter("of_8_null_0", "Jan 1, 1900");
      theRequest.addParameter("pf_8_null_0", "MMM d, yyyy");
   }


   public void testOutputEN() throws Exception { }

   public void endOutputEN(WebResponse theResponse) throws Exception {
      String s = theResponse.getText();
      boolean res = s.indexOf("value=\"2.3\"") > -1;
      assertTrue("not found: " + "value=\"2.3\"", res);
      res = s.indexOf("value=\"Jan 1, 1900\"") > -1;
      assertTrue("not found : " + "value=\"Jan 1, 1900\"", res);
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
}