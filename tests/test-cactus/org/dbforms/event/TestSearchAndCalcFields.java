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

package org.dbforms.event;

import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.DbFormsConfigRegistry;
import org.dbforms.config.Interceptor;
import org.dbforms.config.ResultSetVector;

import org.dbforms.servlets.ConfigServlet;

import org.dbforms.taglib.DbFormTag;
import org.dbforms.util.Util;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class TestSearchAndCalcFields extends org.apache.cactus.JspTestCase {
   private DbFormTag tag;

   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void setUp() throws Exception {
      super.setUp();

      config.setInitParameter("dbformsConfig", "/WEB-INF/dbforms-config.xml");

      DbFormsConfig dbFormsConfig = null;

      try {
         dbFormsConfig = DbFormsConfigRegistry.instance()
                                              .lookup();
      } catch (Exception e) {
         ;
      }

      if (dbFormsConfig == null) {
         config.setInitParameter("dbformsConfig", "/WEB-INF/dbforms-config.xml");

         ConfigServlet configServlet = new ConfigServlet();
         configServlet.init(config);
      }

      this.tag = new DbFormTag();
      tag.setTableName("BOOKLISTPERAUTHOR");
      tag.getTable().getInterceptors().clear();
      this.tag.setPageContext(this.pageContext);
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testResultSetVector() throws Exception {
      this.tag.setTableName("BOOKLISTPERAUTHOR");
      this.tag.doStartTag();
      ResultSetVector rsv = this.tag.getResultSetVector(); 
      assertEquals(9, rsv.size());
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testResultSetVectorWithCalcField() throws Exception {
      this.tag.setTableName("BOOKLISTPERAUTHOR");
      Interceptor interceptor = new Interceptor();
      interceptor.setClassName("org.dbforms.event.InterceptorTest");
      tag.getTable().addInterceptor(interceptor);
      this.tag.doStartTag();
      ResultSetVector rsv = this.tag.getResultSetVector(); 
      assertEquals(9, rsv.size());
      rsv.moveFirst();
      int colISBN = rsv.getFieldIndex("ISBN");
      int colTITEL = rsv.getFieldIndex("TITLE");
      int colSBN_TITEL = rsv.getFieldIndex("ISBN_TITLE");
      for (int i = 0; i < rsv.size(); i++) {
         String s1 = Util.isNull(rsv.getField(colISBN))?"null":rsv.getField(colISBN);
         String s2 = rsv.getField(colTITEL);
         String s3 = rsv.getField(colSBN_TITEL);
         assertTrue("row " + i, s3.equals(InterceptorTest.createTestText(s1, s2)));         
         rsv.moveNext();
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testResultSetVectorWithSearchField() throws Exception {
      this.tag.setTableName("BOOKLISTPERAUTHOR");
      this.tag.setFilter("AUTHOR_ID=2");
      this.tag.doStartTag();
      assertEquals(6, this.tag.getResultSetVector().size());
      
   }

}
