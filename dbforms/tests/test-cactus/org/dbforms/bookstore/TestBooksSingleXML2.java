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
import org.dbforms.util.KeyValuePair;

import java.util.ArrayList;

// imports
import java.util.List;



// definition of test class
public class TestBooksSingleXML2 extends HttpTestCase {
   // Test method generated from the MaxQ Java generator
   public TestBooksSingleXML2(String name) {
      super(name);
   }

   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testBooksSingle() throws Exception {
      List list;
      get("http://localhost/bookstore/tests/testBOOKSSingleXML2.jsp");
      printResponse();
      assertTrue(responseContains("Die Insel des vorigen Tages"));
      assertTrue(responseContains("Das Foucaltsche Pendel"));

      list = new ArrayList();
      list.clear();
      list.add(new KeyValuePair("invtable", "7"));
      list.add(new KeyValuePair("invname_7", ""));
      list.add(new KeyValuePair("autoupdate_7", "false"));
      list.add(new KeyValuePair("fu_7", "/tests/testBOOKSSingleXML2.jsp"));
      list.add(new KeyValuePair("lang", "de"));
      list.add(new KeyValuePair("source", "/bookstore/tests/testBOOKSSingleXML2.jsp"));
      list.add(new KeyValuePair("customEvent", ""));
      list.add(new KeyValuePair("firstpos_7", "0%3A1%3A1-2%3A1%3A1"));
      list.add(new KeyValuePair("lastpos_7", "0%3A1%3A2-2%3A1%3A1"));
      list.add(new KeyValuePair("ac_next_7_3", "next"));
      list.add(new KeyValuePair("k_7_0@root", "0%3A1%3A1-2%3A1%3A1"));
      list.add(new KeyValuePair("k_7_1@root", "0%3A1%3A2-2%3A1%3A1"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Hijacking through the Galaxy 1"));
      assertTrue(responseContains("Hijacking through the Galaxy 2"));

      list.clear();
      list.add(new KeyValuePair("invtable", "7"));
      list.add(new KeyValuePair("invname_7", ""));
      list.add(new KeyValuePair("autoupdate_7", "false"));
      list.add(new KeyValuePair("fu_7", "/tests/testBOOKSSingleXML2.jsp"));
      list.add(new KeyValuePair("lang", "de"));
      list.add(new KeyValuePair("source", "/bookstore/tests/testBOOKSSingleXML2.jsp"));
      list.add(new KeyValuePair("customEvent", ""));
      list.add(new KeyValuePair("firstpos_7", "0%3A1%3A3-2%3A1%3A2"));
      list.add(new KeyValuePair("lastpos_7", "0%3A1%3A4-2%3A1%3A2"));
      list.add(new KeyValuePair("ac_next_7_7", "next"));
      list.add(new KeyValuePair("k_7_0@root", "0%3A1%3A3-2%3A1%3A2"));
      list.add(new KeyValuePair("k_7_1@root", "0%3A1%3A4-2%3A1%3A2"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Hijacking through the Galaxy 3"));
      assertTrue(responseContains("Hijacking through the Galaxy 4"));

      list.clear();
      list.add(new KeyValuePair("invtable", "7"));
      list.add(new KeyValuePair("invname_7", ""));
      list.add(new KeyValuePair("autoupdate_7", "false"));
      list.add(new KeyValuePair("fu_7", "/tests/testBOOKSSingleXML2.jsp"));
      list.add(new KeyValuePair("lang", "de"));
      list.add(new KeyValuePair("source", "/bookstore/tests/testBOOKSSingleXML2.jsp"));
      list.add(new KeyValuePair("customEvent", ""));
      list.add(new KeyValuePair("firstpos_7", "0%3A1%3A5-2%3A1%3A2"));
      list.add(new KeyValuePair("lastpos_7", "0%3A1%3A6-2%3A1%3A2"));
      list.add(new KeyValuePair("ac_next_7_11", "next"));
      list.add(new KeyValuePair("k_7_0@root", "0%3A1%3A5-2%3A1%3A2"));
      list.add(new KeyValuePair("k_7_1@root", "0%3A1%3A6-2%3A1%3A2"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Test null value"));
      assertTrue(responseContains("Luca's favorite thing to eat is  &quot;delicious Italian pasta&quot;"));

      list.clear();
      list.add(new KeyValuePair("invtable", "7"));
      list.add(new KeyValuePair("invname_7", ""));
      list.add(new KeyValuePair("autoupdate_7", "false"));
      list.add(new KeyValuePair("fu_7", "/tests/testBOOKSSingleXML2.jsp"));
      list.add(new KeyValuePair("lang", "de"));
      list.add(new KeyValuePair("source", "/bookstore/tests/testBOOKSSingleXML2.jsp"));
      list.add(new KeyValuePair("customEvent", ""));
      list.add(new KeyValuePair("firstpos_7", "0%3A1%3A7-2%3A0%3A"));
      list.add(new KeyValuePair("lastpos_7", "0%3A1%3A8-2%3A1%3A2"));
      list.add(new KeyValuePair("ac_next_7_15", "next"));
      list.add(new KeyValuePair("k_7_0@root", "0%3A1%3A7-2%3A0%3A"));
      list.add(new KeyValuePair("k_7_1@root", "0%3A1%3A8-2%3A1%3A2"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Hijacking through the Galaxy 6"));


      list.clear();
      list.add(new KeyValuePair("invtable", "7"));
      list.add(new KeyValuePair("invname_7", ""));
      list.add(new KeyValuePair("autoupdate_7", "false"));
      list.add(new KeyValuePair("fu_7", "/tests/testBOOKSSingleXML2.jsp"));
      list.add(new KeyValuePair("lang", "de"));
      list.add(new KeyValuePair("source", "/bookstore/tests/testBOOKSSingleXML2.jsp"));
      list.add(new KeyValuePair("customEvent", ""));
      list.add(new KeyValuePair("ac_next_7_15", "next"));
      list.add(new KeyValuePair("firstpos_7", "0%3A1%3A9-2%3A1%3A2"));
      list.add(new KeyValuePair("lastpos_7", "0%3A1%3A9-2%3A1%3A2"));
      list.add(new KeyValuePair("k_7_0@root", "0%3A1%3A9-2%3A1%3A2"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Luca's favorite thing to eat is  &quot;delicious Italian pasta&quot;"));
      assertTrue(responseContains("Hijacking through the Galaxy 6"));
   }

   /** ************* */
}
