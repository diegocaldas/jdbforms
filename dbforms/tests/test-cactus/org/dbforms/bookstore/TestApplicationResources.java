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
package org.dbforms.bookstore;

import org.dbforms.util.HttpTestCase;



// imports
// definition of test class
public class TestApplicationResources extends HttpTestCase {
   // Test method generated from the MaxQ Java generator
   public TestApplicationResources(String name) {
      super(name);
   }

   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testApplicationResources() throws Exception {
      println("testApplicationResources");
      get("http://localhost/bookstore/tests/testApplicationResources.jsp");
      printResponse();
      assertTrue("test.books.struts", responseContains("test.books.struts"));
      assertTrue("test.books.dbforms", responseContains("test.books.dbforms"));
      assertTrue("test.author.hk", responseContains("test.author.hk"));
      assertTrue("test.author.jm", responseContains("test.author.jm"));
   }
}
