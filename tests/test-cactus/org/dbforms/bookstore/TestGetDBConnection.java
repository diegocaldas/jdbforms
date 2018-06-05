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
public class TestGetDBConnection extends AbstractHttpTestCase {
   // Test method generated from the MaxQ Java generator
   public TestGetDBConnection(String name) {
      super(name);
   }

   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testGetDBConnection() throws Exception {
      get("http://localhost/bookstore/tests/testGetDBConnection.jsp");

      printResponse();
      assertTrue(responseContains("Die Insel des vorigen Tages"));
      assertTrue(responseContains("Das Foucaltsche Pendel"));
      assertTrue(responseContains("Hijacking through the Galaxy 1"));
      assertTrue(responseContains("Hijacking through the Galaxy 2"));
      assertTrue(responseContains("Hijacking through the Galaxy 3"));
      assertTrue(responseContains("Hijacking through the Galaxy 4"));
      assertTrue(responseContains("Test null value"));
      assertTrue(responseContains("Luca&#039;s favorite thing to eat is  &#034;delicious Italian pasta&#034;"));
      assertTrue(responseContains("Hijacking through the Galaxy 6"));
   }

   /** ************* */
}
