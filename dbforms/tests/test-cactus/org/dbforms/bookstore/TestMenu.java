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
public class TestMenu extends HttpTestCase {
   // Test method generated from the MaxQ Java generator
   public TestMenu(String name) {
      super(name);
   }

   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testTest() throws Exception {
      List list;
      get("http://localhost/bookstore/tests/testMenu.jsp");
      printResponse();

      list = new ArrayList();
      list.add(new KeyValuePair("source", "/bookstore/tests/testMenu.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_25_fu",
                                "/tests/testMenuPage1.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_25_singleRow", "false"));
      list.add(new KeyValuePair("ac_goto_-1_25", "test goto menu page 1"));
      list.add(new KeyValuePair("dataac_goto_-1_26_fu",
                                "/tests/testMenuPage2.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_26_singleRow", "false"));
      list.add(new KeyValuePair("dataac_goto_-1_27_fu",
                                "/tests/testPositionAuthorEdit.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_27_singleRow", "false"));
      list.add(new KeyValuePair("dataac_goto_-1_28_fu",
                                "/tests/testPositionAuthorEdit.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_28_destTable", "AUTHOR_VIEW"));
      list.add(new KeyValuePair("dataac_goto_-1_28_singleRow", "false"));
      list.add(new KeyValuePair("dataac_new_0_29_fu", "/tests/testMenuPage1.jsp"));
      list.add(new KeyValuePair("dataac_new_0_30_fu", "/tests/testMenuPage2.jsp"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("This is testMenuPage1"));

      list = new ArrayList();
      list.add(new KeyValuePair("source", "/bookstore/tests/testMenu.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_25_fu",
                                "/tests/testMenuPage1.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_25_singleRow", "false"));
      list.add(new KeyValuePair("dataac_goto_-1_26_fu",
                                "/tests/testMenuPage2.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_26_singleRow", "false"));
      list.add(new KeyValuePair("ac_goto_-1_26", "test goto menu page 2"));
      list.add(new KeyValuePair("dataac_goto_-1_27_fu",
                                "/tests/testPositionAuthorEdit.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_27_singleRow", "false"));
      list.add(new KeyValuePair("dataac_goto_-1_28_fu",
                                "/tests/testPositionAuthorEdit.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_28_destTable", "AUTHOR_VIEW"));
      list.add(new KeyValuePair("dataac_goto_-1_28_singleRow", "false"));
      list.add(new KeyValuePair("dataac_new_0_29_fu", "/tests/testMenuPage1.jsp"));
      list.add(new KeyValuePair("dataac_new_0_30_fu", "/tests/testMenuPage2.jsp"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("This is testMenuPage2"));

      list = new ArrayList();
      list.add(new KeyValuePair("source", "/bookstore/tests/testMenu.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_25_fu",
                                "/tests/testMenuPage1.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_25_singleRow", "false"));
      list.add(new KeyValuePair("dataac_goto_-1_26_fu",
                                "/tests/testMenuPage2.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_26_singleRow", "false"));
      list.add(new KeyValuePair("dataac_goto_-1_27_fu",
                                "/tests/testPositionAuthorEdit.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_27_singleRow", "false"));
      list.add(new KeyValuePair("dataac_goto_-1_28_fu",
                                "/tests/testPositionAuthorEdit.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_28_destTable", "AUTHOR_VIEW"));
      list.add(new KeyValuePair("dataac_goto_-1_28_singleRow", "false"));
      list.add(new KeyValuePair("dataac_new_0_29_fu", "/tests/testMenuPage1.jsp"));
      list.add(new KeyValuePair("ac_new_0_29", "testMenuPage1"));
      list.add(new KeyValuePair("dataac_new_0_30_fu", "/tests/testMenuPage2.jsp"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("This is testMenuPage1"));

      list = new ArrayList();
      list.add(new KeyValuePair("source", "/bookstore/tests/testMenu.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_25_fu",
                                "/tests/testMenuPage1.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_25_singleRow", "false"));
      list.add(new KeyValuePair("dataac_goto_-1_26_fu",
                                "/tests/testMenuPage2.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_26_singleRow", "false"));
      list.add(new KeyValuePair("dataac_goto_-1_27_fu",
                                "/tests/testPositionAuthorEdit.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_27_singleRow", "false"));
      list.add(new KeyValuePair("dataac_goto_-1_28_fu",
                                "/tests/testPositionAuthorEdit.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_28_destTable", "AUTHOR_VIEW"));
      list.add(new KeyValuePair("dataac_goto_-1_28_singleRow", "false"));
      list.add(new KeyValuePair("dataac_new_0_29_fu", "/tests/testMenuPage1.jsp"));
      list.add(new KeyValuePair("dataac_new_0_30_fu", "/tests/testMenuPage2.jsp"));
      list.add(new KeyValuePair("ac_new_0_30", "testMenuPage2"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("This is testMenuPage2"));

      list = new ArrayList();
      list.add(new KeyValuePair("source", "/bookstore/tests/testMenu.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_25_fu",
                                "/tests/testMenuPage1.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_25_singleRow", "false"));
      list.add(new KeyValuePair("dataac_goto_-1_26_fu",
                                "/tests/testMenuPage2.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_26_singleRow", "false"));
      list.add(new KeyValuePair("dataac_goto_-1_27_fu",
                                "/tests/testPositionAuthorEdit.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_27_singleRow", "false"));
      list.add(new KeyValuePair("ac_goto_-1_27",
                                "test goto testPositionAuthorEdit.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_28_fu",
                                "/tests/testPositionAuthorEdit.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_28_destTable", "AUTHOR_VIEW"));
      list.add(new KeyValuePair("dataac_goto_-1_28_singleRow", "false"));
      list.add(new KeyValuePair("dataac_new_0_29_fu", "/tests/testMenuPage1.jsp"));
      list.add(new KeyValuePair("dataac_new_0_30_fu", "/tests/testMenuPage2.jsp"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Edit Authors"));

      list = new ArrayList();
      list.add(new KeyValuePair("source", "/bookstore/tests/testMenu.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_25_fu",
                                "/tests/testMenuPage1.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_25_singleRow", "false"));
      list.add(new KeyValuePair("dataac_goto_-1_26_fu",
                                "/tests/testMenuPage2.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_26_singleRow", "false"));
      list.add(new KeyValuePair("dataac_goto_-1_27_fu",
                                "/tests/testPositionAuthorEdit.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_27_singleRow", "false"));
      list.add(new KeyValuePair("dataac_goto_-1_28_fu",
                                "/tests/testPositionAuthorEdit.jsp"));
      list.add(new KeyValuePair("dataac_goto_-1_28_destTable", "AUTHOR_VIEW"));
      list.add(new KeyValuePair("dataac_goto_-1_28_singleRow", "false"));
      list.add(new KeyValuePair("ac_goto_-1_28",
                                "test goto testPositionAuthorEdit.jsp with AUTHORVIEW"));
      list.add(new KeyValuePair("dataac_new_0_29_fu", "/tests/testMenuPage1.jsp"));
      list.add(new KeyValuePair("dataac_new_0_30_fu", "/tests/testMenuPage2.jsp"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("Edit Authors"));
   }
}
