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

import org.dbforms.interfaces.StaticData;
import org.dbforms.util.AbstractHttpTestCase;

// imports
import java.util.ArrayList;
import java.util.List;



// definition of test class
public class TestBooksSingleXML extends AbstractHttpTestCase {
   // Test method generated from the MaxQ Java generator
   public TestBooksSingleXML(String name) {
      super(name);
   }

   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testBooksSingle() throws Exception {
      List list;

      get("http://localhost/bookstore/tests/testBOOKSSingleXML.jsp");
      printResponse();
      assertTrue(responseContains("Die Insel des vorigen Tages"));

      list = new ArrayList();
      list.add(new StaticData("invtable", "7"));
      list.add(new StaticData("invname_7", "null"));
      list.add(new StaticData("autoupdate_7", "false"));
      list.add(new StaticData("fu_7", "/tests/testBOOKSSingleXML.jsp"));
      list.add(new StaticData("source",
                                "/bookstore/tests/testBOOKSSingleXML.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_7",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new StaticData("lastpos_7",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new StaticData("ac_next_7", "next"));
      list.add(new StaticData("k_7_0@root",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Das Foucaltsche Pendel"));

      list = new ArrayList();
      list.add(new StaticData("invtable", "7"));
      list.add(new StaticData("invname_7", "null"));
      list.add(new StaticData("autoupdate_7", "false"));
      list.add(new StaticData("fu_7", "/tests/testBOOKSSingleXML.jsp"));
      list.add(new StaticData("source",
                                "/bookstore/tests/testBOOKSSingleXML.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_7",
                                "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
      list.add(new StaticData("lastpos_7",
                                "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
      list.add(new StaticData("ac_next_7", "next"));
      list.add(new StaticData("k_7_0@root",
                                "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Hijacking through the Galaxy 1"));

      list = new ArrayList();
      list.add(new StaticData("invtable", "7"));
      list.add(new StaticData("invname_7", "null"));
      list.add(new StaticData("autoupdate_7", "false"));
      list.add(new StaticData("fu_7", "/tests/testBOOKSSingleXML.jsp"));
      list.add(new StaticData("source",
                                "/bookstore/tests/testBOOKSSingleXML.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_7",
                                "0%3A1%3A3-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+1"));
      list.add(new StaticData("lastpos_7",
                                "0%3A1%3A3-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+1"));
      list.add(new StaticData("ac_next_7", "next"));
      list.add(new StaticData("k_7_0@root",
                                "0%3A1%3A3-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+1"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Hijacking through the Galaxy 2"));

      list = new ArrayList();
      list.add(new StaticData("invtable", "7"));
      list.add(new StaticData("invname_7", "null"));
      list.add(new StaticData("autoupdate_7", "false"));
      list.add(new StaticData("fu_7", "/tests/testBOOKSSingleXML.jsp"));
      list.add(new StaticData("source",
                                "/bookstore/tests/testBOOKSSingleXML.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_7",
                                "0%3A1%3A4-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+2"));
      list.add(new StaticData("lastpos_7",
                                "0%3A1%3A4-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+2"));
      list.add(new StaticData("ac_next_7", "next"));
      list.add(new StaticData("k_7_0@root",
                                "0%3A1%3A4-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+2"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Hijacking through the Galaxy 3"));

      list = new ArrayList();
      list.add(new StaticData("invtable", "7"));
      list.add(new StaticData("invname_7", "null"));
      list.add(new StaticData("autoupdate_7", "false"));
      list.add(new StaticData("fu_7", "/tests/testBOOKSSingleXML.jsp"));
      list.add(new StaticData("source",
                                "/bookstore/tests/testBOOKSSingleXML.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_7",
                                "0%3A1%3A5-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+3"));
      list.add(new StaticData("lastpos_7",
                                "0%3A1%3A5-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+3"));
      list.add(new StaticData("ac_next_7", "next"));
      list.add(new StaticData("k_7_0@root",
                                "0%3A1%3A5-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+3"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Hijacking through the Galaxy 4"));

      list = new ArrayList();
      list.add(new StaticData("invtable", "7"));
      list.add(new StaticData("invname_7", "null"));
      list.add(new StaticData("autoupdate_7", "false"));
      list.add(new StaticData("fu_7", "/tests/testBOOKSSingleXML.jsp"));
      list.add(new StaticData("source",
                                "/bookstore/tests/testBOOKSSingleXML.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_7",
                                "0%3A1%3A6-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+4"));
      list.add(new StaticData("lastpos_7",
                                "0%3A1%3A6-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+4"));
      list.add(new StaticData("ac_next_7", "next"));
      list.add(new StaticData("k_7_0@root",
                                "0%3A1%3A6-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+4"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Test null value"));

      list = new ArrayList();
      list.add(new StaticData("invtable", "7"));
      list.add(new StaticData("invname_7", "null"));
      list.add(new StaticData("autoupdate_7", "false"));
      list.add(new StaticData("fu_7", "/tests/testBOOKSSingleXML.jsp"));
      list.add(new StaticData("source",
                                "/bookstore/tests/testBOOKSSingleXML.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_7",
                                "0%3A1%3A7-2%3A0%3A-3%3A15%3ATest+null+value"));
      list.add(new StaticData("lastpos_7",
                                "0%3A1%3A7-2%3A0%3A-3%3A15%3ATest+null+value"));
      list.add(new StaticData("ac_next_7", "next"));
      list.add(new StaticData("k_7_0@root",
                                "0%3A1%3A7-2%3A0%3A-3%3A15%3ATest+null+value"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Luca's favorite thing to eat is  &quot;delicious Italian pasta&quot;"));

      list = new ArrayList();
      list.add(new StaticData("invtable", "7"));
      list.add(new StaticData("invname_7", "null"));
      list.add(new StaticData("autoupdate_7", "false"));
      list.add(new StaticData("fu_7", "/tests/testBOOKSSingleXML.jsp"));
      list.add(new StaticData("source",
                                "/bookstore/tests/testBOOKSSingleXML.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_7",
                                "0%3A1%3A8-2%3A1%3A2-3%3A58%3ALuca%27s+favorite+thing+to+eat+is++%22delicious+Italian+pasta%22"));
      list.add(new StaticData("lastpos_7",
                                "0%3A1%3A8-2%3A1%3A2-3%3A58%3ALuca%27s+favorite+thing+to+eat+is++%22delicious+Italian+pasta%22"));
      list.add(new StaticData("ac_next_7", "next"));
      list.add(new StaticData("k_7_0@root",
                                "0%3A1%3A8-2%3A1%3A2-3%3A58%3ALuca%27s+favorite+thing+to+eat+is++%22delicious+Italian+pasta%22"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Hijacking through the Galaxy 6"));

      list = new ArrayList();
      list.add(new StaticData("invtable", "7"));
      list.add(new StaticData("invname_7", "null"));
      list.add(new StaticData("autoupdate_7", "false"));
      list.add(new StaticData("fu_7", "/tests/testBOOKSSingleXML.jsp"));
      list.add(new StaticData("source",
                                "/bookstore/tests/testBOOKSSingleXML.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_7",
                                "0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6"));
      list.add(new StaticData("lastpos_7",
                                "0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6"));
      list.add(new StaticData("ac_next_7", "next"));
      list.add(new StaticData("k_7_0@root",
                                "0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Hijacking through the Galaxy 6"));

      list = new ArrayList();
      list.add(new StaticData("invtable", "7"));
      list.add(new StaticData("invname_7", "null"));
      list.add(new StaticData("autoupdate_7", "false"));
      list.add(new StaticData("fu_7", "/tests/testBOOKSSingleXML.jsp"));
      list.add(new StaticData("source",
                                "/bookstore/tests/testBOOKSSingleXML.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_7",
                                "0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6"));
      list.add(new StaticData("lastpos_7",
                                "0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6"));
      list.add(new StaticData("ac_first_7", "first"));
      list.add(new StaticData("k_7_0@root",
                                "0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Die Insel des vorigen Tages"));

      list = new ArrayList();
      list.add(new StaticData("invtable", "7"));
      list.add(new StaticData("invname_7", "null"));
      list.add(new StaticData("autoupdate_7", "false"));
      list.add(new StaticData("fu_7", "/tests/testBOOKSSingleXML.jsp"));
      list.add(new StaticData("source",
                                "/bookstore/tests/testBOOKSSingleXML.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_7",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new StaticData("lastpos_7",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new StaticData("ac_last_7", "last"));
      list.add(new StaticData("k_7_0@root",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Hijacking through the Galaxy 6"));
   }

   /** ************* */
}
