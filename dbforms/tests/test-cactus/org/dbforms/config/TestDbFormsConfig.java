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
import org.apache.cactus.ServletTestCase;
import java.text.SimpleDateFormat;
import org.dbforms.servlets.ConfigServlet;



/**
 * Tests of the <code>DbFormsConfig</code> class.
 * 
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 */
public class TestDbFormsConfig extends ServletTestCase
{
   private DbFormsConfig dbFormsConfig = null;

   /**
    * Defines the testcase name for JUnit.
    * 
    * @param theName the testcase's name.
    */
   public TestDbFormsConfig(String theName)
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
               new String[] { TestDbFormsConfig.class.getName() });
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
      return new TestSuite(TestDbFormsConfig.class);
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
      dbFormsConfig = null;
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
			dbFormsConfig = DbFormsConfigRegistry.instance().lookup();
      }
   }


   /**
    * DOCUMENT ME!
    * 
    * @throws Exception DOCUMENT ME!
    */
   public void testStandardDbFormsConfig() throws Exception
   {
      Table tblAuthor = dbFormsConfig.getTableByName("AUTHOR");
      assertTrue("Found tblAuthor", tblAuthor.getName().equals("AUTHOR"));
      assertTrue("Found tblBook", 
                 dbFormsConfig.getTableByName("BOOK").getName().equals("BOOK"));
      assertTrue("Make sure table names ARE casesensitve", 
                 dbFormsConfig.getTableByName("book") == null);
   }


   /**
    * DOCUMENT ME!
    * 
    * @throws Exception DOCUMENT ME!
    */
   public void testSimpleDateFormat() throws Exception
   {
      SimpleDateFormat sdf = dbFormsConfig.getDateFormatter();
      assertTrue("Default SDF is not null", sdf != null);

      dbFormsConfig.setDateFormatter("ddMMMyy");

      SimpleDateFormat newSDF = new SimpleDateFormat("ddMMMyy");
      assertTrue("New SDF doesn't match orginial:", 
                 !sdf.equals(dbFormsConfig.getDateFormatter()));
      assertTrue("New SDF does match format ddMMMyy:", 
                 newSDF.equals(dbFormsConfig.getDateFormatter()));
   }


   /**
    * DOCUMENT ME!
    * 
    * @throws Exception DOCUMENT ME!
    */
   public void testAddTable() throws Exception
   {
      Table newTable = new Table();
      newTable.setName("NEW_TABLE");
      dbFormsConfig.addTable(newTable);

      assertTrue("Found NEW_TABLE", 
                 dbFormsConfig.getTableByName("NEW_TABLE").getName()
                              .equals("NEW_TABLE"));
   }


   /**
    * DOCUMENT ME!
    */
   public void tearDown()
   {
   }
}