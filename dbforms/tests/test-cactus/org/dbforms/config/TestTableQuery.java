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

package org.dbforms.config;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.JspTestCase;
import org.apache.cactus.WebResponse;
import java.text.SimpleDateFormat;
import org.dbforms.servlets.ConfigServlet;
import org.dbforms.taglib.DbFormTag;



/**
 * Tests of the <code>DbFormsConfig</code> class.
 * 
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 */
public class TestTableQuery extends JspTestCase
{
   private DbFormTag tag;

   /**
    * Defines the testcase name for JUnit.
    * 
    * @param theName the testcase's name.
    */
   public TestTableQuery(String theName)
   {
      super(theName);
   }

   /**
    * Start the tests.
    * 
    * @param theArgs the arguments. Not used
    */
   public static void main(String[] theArgs)
   {
      junit.swingui.TestRunner.main(
               new String[] { TestTableQuery.class.getName() });
   }


   /**
    * DOCUMENT ME!
    * 
    * @return a test suite (<code>TestSuite</code>) that includes all methods
    *         starting with "test"
    */
   public static Test suite()
   {
      // All methods starting with "test" will be executed in the test suite.
      return new TestSuite(TestTableQuery.class);
   }


   /**
    * In addition to creating the tag instance and adding the pageContext to
    * it, this method creates a BodyContent object and passes it to the tag.
    * 
    * @throws Exception DOCUMENT ME!
    */
   public void setUp() throws Exception
   {
      config.setInitParameter("dbformsConfig", "/WEB-INF/dbforms-config.xml");

      DbFormsConfig dbFormsConfig = null;

      try
      {
         dbFormsConfig = DbFormsConfigRegistry.instance().lookup();
      }
      catch (Exception e)
      {
      }

      if (dbFormsConfig == null)
      {
         config.setInitParameter("dbformsConfig", "/WEB-INF/dbforms-config.xml");

         ConfigServlet configServlet = new ConfigServlet();
         configServlet.init(config);
      }

      this.tag = new DbFormTag();
      this.tag.setPageContext(this.pageContext);
   }


   /**
    * DOCUMENT ME!
    * 
    * @throws Exception DOCUMENT ME!
    */
   public void testTable() throws Exception
   {
      this.tag.setTableName("BOOK");
      this.tag.setFilter("BOOK_ID=5,AUTHOR_ID=2");
      this.tag.doStartTag();
      assertEquals(1, this.tag.getResultSetVector().size());
   }


   /**
    * DOCUMENT ME!
    * 
    * @throws Exception DOCUMENT ME!
    */
   public void testQuery() throws Exception
   {
      this.tag.setTableName("BOOKLISTPERAUTHOR");
      this.tag.setFilter("BOOK_ID=5,AUTHOR_ID=2");
      this.tag.doStartTag();
      assertEquals(1, this.tag.getResultSetVector().size());
   }


   /**
    * DOCUMENT ME!
    */
   public void tearDown()
   {
   }
}