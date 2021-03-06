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

package org.dbforms.servlets;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.cactus.ServletTestCase;

import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.DbFormsConfigRegistry;
import org.dbforms.config.Table;



/**
 * Tests of the <code>DbFormTag</code> class.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh </a>
 *
 */
public class TestConfigServlet extends ServletTestCase {
   /**
    * Defines the testcase name for JUnit.
    *
    * @param theName
    *            the testcase's name.
    */
   public TestConfigServlet(String theName) {
      super(theName);
   }

   /**
    * Start the tests.
    *
    * @param theArgs
    *            the arguments. Not used
    */
   public static void main(String[] theArgs) {
      junit.swingui.TestRunner.main(new String[] {
                                       TestConfigServlet.class.getName()
                                    });
   }


   /**
    * @return a test suite (<code>TestSuite</code>) that includes all
    *         methods starting with "test"
    */
   public static Test suite() {
      // All methods starting with "test" will be executed in the test suite.
      return new TestSuite(TestConfigServlet.class);
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception
    *             DOCUMENT ME!
    */
   public void testInitConfigServlet() throws Exception {
      config.setInitParameter("dbformsConfig", "/WEB-INF/dbforms-config.xml");

      ConfigServlet configServlet = new ConfigServlet();
      configServlet.init(config);

      DbFormsConfig pconfig = DbFormsConfigRegistry.instance()
                                                  .lookup();

      Table         tblAuthor = pconfig.getTableByName("AUTHOR");
      assertTrue("Found tblAuthor", tblAuthor.getName().equals("AUTHOR"));
   }
}
