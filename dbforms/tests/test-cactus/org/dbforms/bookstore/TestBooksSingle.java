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
import org.dbforms.util.KeyValuePair;

// imports
import java.util.ArrayList;
import java.util.List;



// definition of test class
public class TestBooksSingle extends AbstractHttpTestCase {
   // Test method generated from the MaxQ Java generator
   public TestBooksSingle(String name) {
      super(name);
   }

   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testBooksSingle() throws Exception {
      List list;

      get("http://localhost/bookstore/tests/testBOOKSSingle.jsp");
      printResponse();
      assertTrue(responseContains("Die Insel des vorigen Tages"));

      list = new ArrayList();
      list.add(new KeyValuePair("invtable", "1"));
      list.add(new KeyValuePair("invname_1", "null"));
      list.add(new KeyValuePair("autoupdate_1", "false"));
      list.add(new KeyValuePair("fu_1", "/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("source", "/bookstore/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("customEvent", ""));
      list.add(new KeyValuePair("firstpos_1",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new KeyValuePair("lastpos_1",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new KeyValuePair("ac_next_1", "next"));
      list.add(new KeyValuePair("k_1_0@root",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Das Foucaltsche Pendel"));

      list = new ArrayList();
      list.add(new KeyValuePair("invtable", "1"));
      list.add(new KeyValuePair("invname_1", "null"));
      list.add(new KeyValuePair("autoupdate_1", "false"));
      list.add(new KeyValuePair("fu_1", "/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("source", "/bookstore/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("customEvent", ""));
      list.add(new KeyValuePair("firstpos_1",
                                "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
      list.add(new KeyValuePair("lastpos_1",
                                "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
      list.add(new KeyValuePair("ac_next_1", "next"));
      list.add(new KeyValuePair("k_1_0@root",
                                "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Hijacking through the Galaxy 1"));

      list = new ArrayList();
      list.add(new KeyValuePair("invtable", "1"));
      list.add(new KeyValuePair("invname_1", "null"));
      list.add(new KeyValuePair("autoupdate_1", "false"));
      list.add(new KeyValuePair("fu_1", "/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("source", "/bookstore/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("customEvent", ""));
      list.add(new KeyValuePair("firstpos_1",
                                "0%3A1%3A3-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+1"));
      list.add(new KeyValuePair("lastpos_1",
                                "0%3A1%3A3-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+1"));
      list.add(new KeyValuePair("ac_next_1", "next"));
      list.add(new KeyValuePair("k_1_0@root",
                                "0%3A1%3A3-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+1"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Hijacking through the Galaxy 2"));

      list = new ArrayList();
      list.add(new KeyValuePair("invtable", "1"));
      list.add(new KeyValuePair("invname_1", "null"));
      list.add(new KeyValuePair("autoupdate_1", "false"));
      list.add(new KeyValuePair("fu_1", "/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("source", "/bookstore/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("customEvent", ""));
      list.add(new KeyValuePair("firstpos_1",
                                "0%3A1%3A4-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+2"));
      list.add(new KeyValuePair("lastpos_1",
                                "0%3A1%3A4-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+2"));
      list.add(new KeyValuePair("ac_next_1", "next"));
      list.add(new KeyValuePair("k_1_0@root",
                                "0%3A1%3A4-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+2"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Hijacking through the Galaxy 3"));

      list = new ArrayList();
      list.add(new KeyValuePair("invtable", "1"));
      list.add(new KeyValuePair("invname_1", "null"));
      list.add(new KeyValuePair("autoupdate_1", "false"));
      list.add(new KeyValuePair("fu_1", "/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("source", "/bookstore/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("customEvent", ""));
      list.add(new KeyValuePair("firstpos_1",
                                "0%3A1%3A5-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+3"));
      list.add(new KeyValuePair("lastpos_1",
                                "0%3A1%3A5-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+3"));
      list.add(new KeyValuePair("ac_next_1", "next"));
      list.add(new KeyValuePair("k_1_0@root",
                                "0%3A1%3A5-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+3"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Hijacking through the Galaxy 4"));

      list = new ArrayList();
      list.add(new KeyValuePair("invtable", "1"));
      list.add(new KeyValuePair("invname_1", "null"));
      list.add(new KeyValuePair("autoupdate_1", "false"));
      list.add(new KeyValuePair("fu_1", "/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("source", "/bookstore/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("customEvent", ""));
      list.add(new KeyValuePair("firstpos_1",
                                "0%3A1%3A6-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+4"));
      list.add(new KeyValuePair("lastpos_1",
                                "0%3A1%3A6-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+4"));
      list.add(new KeyValuePair("ac_next_1", "next"));
      list.add(new KeyValuePair("k_1_0@root",
                                "0%3A1%3A6-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+4"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Test null value"));

      list = new ArrayList();
      list.add(new KeyValuePair("invtable", "1"));
      list.add(new KeyValuePair("invname_1", "null"));
      list.add(new KeyValuePair("autoupdate_1", "false"));
      list.add(new KeyValuePair("fu_1", "/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("source", "/bookstore/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("customEvent", ""));
      list.add(new KeyValuePair("firstpos_1",
                                "0%3A1%3A7-2%3A0%3A-3%3A15%3ATest+null+value"));
      list.add(new KeyValuePair("lastpos_1",
                                "0%3A1%3A7-2%3A0%3A-3%3A15%3ATest+null+value"));
      list.add(new KeyValuePair("ac_next_1", "next"));
      list.add(new KeyValuePair("k_1_0@root",
                                "0%3A1%3A7-2%3A0%3A-3%3A15%3ATest+null+value"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Luca's favorite thing to eat is  &quot;delicious Italian pasta&quot;"));

      list = new ArrayList();
      list.add(new KeyValuePair("invtable", "1"));
      list.add(new KeyValuePair("invname_1", "null"));
      list.add(new KeyValuePair("autoupdate_1", "false"));
      list.add(new KeyValuePair("fu_1", "/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("source", "/bookstore/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("customEvent", ""));
      list.add(new KeyValuePair("firstpos_1",
                                "0%3A1%3A8-2%3A1%3A2-3%3A58%3ALuca%27s+favorite+thing+to+eat+is++%22delicious+Italian+pasta%22"));
      list.add(new KeyValuePair("lastpos_1",
                                "0%3A1%3A8-2%3A1%3A2-3%3A58%3ALuca%27s+favorite+thing+to+eat+is++%22delicious+Italian+pasta%22"));
      list.add(new KeyValuePair("ac_next_1", "next"));
      list.add(new KeyValuePair("k_1_0@root",
                                "0%3A1%3A8-2%3A1%3A2-3%3A58%3ALuca%27s+favorite+thing+to+eat+is++%22delicious+Italian+pasta%22"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Hijacking through the Galaxy 6"));

      list = new ArrayList();
      list.add(new KeyValuePair("invtable", "1"));
      list.add(new KeyValuePair("invname_1", "null"));
      list.add(new KeyValuePair("autoupdate_1", "false"));
      list.add(new KeyValuePair("fu_1", "/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("source", "/bookstore/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("customEvent", ""));
      list.add(new KeyValuePair("firstpos_1",
                                "0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6"));
      list.add(new KeyValuePair("lastpos_1",
                                "0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6"));
      list.add(new KeyValuePair("ac_next_1", "next"));
      list.add(new KeyValuePair("k_1_0@root",
                                "0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Hijacking through the Galaxy 6"));

      list = new ArrayList();
      list.add(new KeyValuePair("invtable", "1"));
      list.add(new KeyValuePair("invname_1", "null"));
      list.add(new KeyValuePair("autoupdate_1", "false"));
      list.add(new KeyValuePair("fu_1", "/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("source", "/bookstore/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("customEvent", ""));
      list.add(new KeyValuePair("firstpos_1",
                                "0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6"));
      list.add(new KeyValuePair("lastpos_1",
                                "0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6"));
      list.add(new KeyValuePair("ac_first_1", "first"));
      list.add(new KeyValuePair("k_1_0@root",
                                "0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Die Insel des vorigen Tages"));

      list = new ArrayList();
      list.add(new KeyValuePair("invtable", "1"));
      list.add(new KeyValuePair("invname_1", "null"));
      list.add(new KeyValuePair("autoupdate_1", "false"));
      list.add(new KeyValuePair("fu_1", "/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("source", "/bookstore/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("customEvent", ""));
      list.add(new KeyValuePair("firstpos_1",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new KeyValuePair("lastpos_1",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new KeyValuePair("ac_last_1", "last"));
      list.add(new KeyValuePair("k_1_0@root",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Hijacking through the Galaxy 6"));
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testGotoPrefix() throws Exception {
      get("http://localhost/bookstore/tests/testBOOKSSingle.jsp?fv_BOOK_ID=4&fv_AUTHOR_ID=2");
      printResponse();
      assertTrue(responseContains("Hijacking through the Galaxy 2"));
   }
}
