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
public class TestBooksSingleUpdate extends AbstractHttpTestCase {
   // Test method generated from the MaxQ Java generator
   public TestBooksSingleUpdate(String name) {
      super(name);
   }

   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testBooksSingleInsert() throws Exception {
      List list;
      get("http://localhost/bookstore/tests/testBOOKSSingle.jsp");

      list = new ArrayList();
      list.add(new StaticData("invtable", "1"));
      list.add(new StaticData("invname_1", ""));
      list.add(new StaticData("autoupdate_1", "false"));
      list.add(new StaticData("fu_1", "/tests/testBOOKSSingle.jsp"));
      list.add(new StaticData("source", "/bookstore/tests/testBOOKSSingle.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_1",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new StaticData("lastpos_1",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new StaticData("f_1_0@root_1", "42-42-42"));
      list.add(new StaticData("of_1_0@root_1", "42-42-42"));
      list.add(new StaticData("f_1_0@root_3", "Die Insel des vorigen Tages"));
      list.add(new StaticData("of_1_0@root_3", "Die Insel des vorigen Tages"));
      list.add(new StaticData("ac_new_1_21", "New"));
      list.add(new StaticData("k_1_0@root",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      post("http://localhost/bookstore/servlet/control", list);
      assertTrue(responseContains("<input type=\"text\" name=\"f_1_insroot_1\" value=\"\" />"));

      list = new ArrayList();
      list.add(new StaticData("invtable", "1"));
      list.add(new StaticData("invname_1", ""));
      list.add(new StaticData("autoupdate_1", "false"));
      list.add(new StaticData("fu_1", "/tests/testBOOKSSingle.jsp"));
      list.add(new StaticData("source", "/bookstore/tests/testBOOKSSingle.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("f_1_insroot_0", "999"));
      list.add(new StaticData("of_1_insroot_0", ""));
      list.add(new StaticData("f_1_insroot_1", "4711"));
      list.add(new StaticData("of_1_insroot_1", ""));
      list.add(new StaticData("f_1_insroot_2", "1"));
      list.add(new StaticData("of_1_insroot_2", ""));
      list.add(new StaticData("f_1_insroot_3", "test"));
      list.add(new StaticData("of_1_insroot_3", ""));
      list.add(new StaticData("ac_insert_1_root_22", "Insert"));
      list.add(new StaticData("k_1_0@root", "null"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("<input type=\"text\" name=\"f_1_0@root_0\" value=\"999\" />"));
      assertTrue(responseContains("<input type=\"text\" name=\"f_1_0@root_1\" value=\"4711\" />"));

      list = new ArrayList();
      list.add(new StaticData("invtable", "1"));
      list.add(new StaticData("invname_1", ""));
      list.add(new StaticData("autoupdate_1", "false"));
      list.add(new StaticData("fu_1", "/tests/testBOOKSSingle.jsp"));
      list.add(new StaticData("source", "/bookstore/tests/testBOOKSSingle.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_1", "0%3A1%3A0-2%3A1%3A1"));
      list.add(new StaticData("lastpos_1", "0%3A1%3A0-2%3A1%3A1"));
      list.add(new StaticData("f_1_0@root_0", "999"));
      list.add(new StaticData("of_1_0@root_0", "999"));
      list.add(new StaticData("f_1_0@root_1", "4711"));
      list.add(new StaticData("of_1_0@root_1", "4711"));
      list.add(new StaticData("f_1_0@root_2", "1"));
      list.add(new StaticData("of_1_0@root_2", "1"));
      list.add(new StaticData("pf_1_0@root_2", "#,##0"));
      list.add(new StaticData("f_1_0@root_3", "test"));
      list.add(new StaticData("of_1_0@root_3", "test"));
      list.add(new StaticData("ac_delete_1_0@root_8", "Delete"));
      list.add(new StaticData("k_1_0@root", "0%3A3%3A999-2%3A1%3A1"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("<input type=\"text\" name=\"f_1_0@root_1\" value=\"3-423-12445-4\" />"));
      assertTrue(responseContains("<input type=\"text\" name=\"f_1_0@root_3\" value=\"Die Insel des vorigen Tages\" />"));
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testBooksSingleUpdate() throws Exception {
      List list;
      get("http://localhost/bookstore/tests/testBOOKSSingle.jsp");
      assertTrue(responseContains("<input type=\"text\" name=\"f_1_0@root_1\" value=\"3-423-12445-4\" />"));

      list = new ArrayList();
      list.add(new StaticData("invtable", "1"));
      list.add(new StaticData("invname_1", ""));
      list.add(new StaticData("autoupdate_1", "false"));
      list.add(new StaticData("fu_1", "/tests/testBOOKSSingle.jsp"));
      list.add(new StaticData("source", "/bookstore/tests/testBOOKSSingle.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_1",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new StaticData("lastpos_1",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new StaticData("f_1_0@root_1", "42-42-42"));
      list.add(new StaticData("of_1_0@root_1", "3-423-12445-4"));
      list.add(new StaticData("f_1_0@root_2", "1"));
      list.add(new StaticData("of_1_0@root_2", "1"));
      list.add(new StaticData("f_1_0@root_3", "Die Insel des vorigen Tages"));
      list.add(new StaticData("of_1_0@root_3", "Die Insel des vorigen Tages"));
      list.add(new StaticData("ac_update_1_0@root_1", "Save"));
      list.add(new StaticData("k_1_0@root",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      post("http://localhost/bookstore/servlet/control;jsessionid=2911D29F8DBDDAC2420A02EA92936B56",
           list);
      printResponse();
      assertTrue(responseContains("<input type=\"text\" name=\"f_1_0@root_1\" value=\"42-42-42\" />"));

      list = new ArrayList();
      list.add(new StaticData("invtable", "1"));
      list.add(new StaticData("invname_1", ""));
      list.add(new StaticData("autoupdate_1", "false"));
      list.add(new StaticData("fu_1", "/tests/testBOOKSSingle.jsp"));
      list.add(new StaticData("source", "/bookstore/tests/testBOOKSSingle.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_1",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new StaticData("lastpos_1",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new StaticData("f_1_0@root_1", "3-423-12445-4"));
      list.add(new StaticData("of_1_0@root_1", "42-42-42"));
      list.add(new StaticData("f_1_0@root_2", "1"));
      list.add(new StaticData("of_1_0@root_2", "1"));
      list.add(new StaticData("f_1_0@root_3", "Die Insel des vorigen Tages"));
      list.add(new StaticData("of_1_0@root_3", "Die Insel des vorigen Tages"));
      list.add(new StaticData("ac_update_1_0@root_1", "Save"));
      list.add(new StaticData("k_1_0@root",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      post("http://localhost/bookstore/servlet/control;jsessionid=2911D29F8DBDDAC2420A02EA92936B56",
           list);
      printResponse();
      assertTrue(responseContains("<input type=\"text\" name=\"f_1_0@root_1\" value=\"3-423-12445-4\" />"));
   }
}
