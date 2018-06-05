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

import org.dbforms.util.AbstractHttpTestCase;



// definition of test class
public class TestIsWebEvent extends AbstractHttpTestCase {
   // Test method generated from the MaxQ Java generator
   public TestIsWebEvent(String name) {
      super(name);
   }

   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testTestIsWebEvent() throws Exception {
      get("http://localhost/bookstore/tests/testIsWebEvent.jsp");
      printResponse();
      assertTrue("Hijacking through the Galaxy 6", responseContains("Hijacking through the Galaxy 6"));
      assertTrue("this should be printed", responseContains("this should be printed"));
      assertTrue("this should also be printed", responseContains("this should also be printed"));
      assertFalse("this should not be printed", responseContains("this should not be printed"));
   }

   /** ************* */
}
