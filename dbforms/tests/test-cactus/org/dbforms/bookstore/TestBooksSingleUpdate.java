// package
// This class was generated by MaxQ (maxq.tigris.org)
package org.dbforms.bookstore;
// imports
import java.util.ArrayList;
import java.util.List;

import org.dbforms.util.HttpTestCase;

import org.dbforms.util.KeyValuePair;

// definition of test class
public class TestBooksSingleUpdate extends HttpTestCase
{
	// Test method generated from the MaxQ Java generator
	public TestBooksSingleUpdate(String name)
	{
		super(name);
	}

   public void testBooksSingleUpdate() throws Exception {
      List list;
      get("http://localhost/bookstore/tests/testBOOKSSingle.jsp");
      assertTrue(responseContains("<input type=\"text\" name=\"f_1_0@root_1\" value=\"3-423-12445-4\" />"));
      
      list = new ArrayList();
      list.add(new KeyValuePair("invtable", "1"));
      list.add(new KeyValuePair("invname_1", ""));
      list.add(new KeyValuePair("autoupdate_1", "false"));
      list.add(new KeyValuePair("fu_1", "/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("source", "/bookstore/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("customEvent", ""));
      list.add(new KeyValuePair("firstpos_1", "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new KeyValuePair("lastpos_1", "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new KeyValuePair("f_1_0@root_0", "1"));
      list.add(new KeyValuePair("of_1_0@root_0", "1"));
      list.add(new KeyValuePair("f_1_0@root_1", "42-42-42"));
      list.add(new KeyValuePair("of_1_0@root_1", "3-423-12445-4"));
      list.add(new KeyValuePair("f_1_0@root_2", "1"));
      list.add(new KeyValuePair("of_1_0@root_2", "1"));
      list.add(new KeyValuePair("f_1_0@root_3", "Die Insel des vorigen Tages"));
      list.add(new KeyValuePair("of_1_0@root_3", "Die Insel des vorigen Tages"));
      list.add(new KeyValuePair("ac_update_1_0@root_1", "Save"));
      list.add(new KeyValuePair("k_1_0@root", "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      post("http://localhost/bookstore/servlet/control;jsessionid=2911D29F8DBDDAC2420A02EA92936B56", list);
      printResponse();
      assertTrue(responseContains("<input type=\"text\" name=\"f_1_0@root_1\" value=\"42-42-42\" />"));

      list = new ArrayList();
      list.add(new KeyValuePair("invtable", "1"));
      list.add(new KeyValuePair("invname_1", ""));
      list.add(new KeyValuePair("autoupdate_1", "false"));
      list.add(new KeyValuePair("fu_1", "/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("source", "/bookstore/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("customEvent", ""));
      list.add(new KeyValuePair("firstpos_1", "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new KeyValuePair("lastpos_1", "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new KeyValuePair("f_1_0@root_0", "1"));
      list.add(new KeyValuePair("of_1_0@root_0", "1"));
      list.add(new KeyValuePair("f_1_0@root_1", "3-423-12445-4"));
      list.add(new KeyValuePair("of_1_0@root_1", "42-42-42"));
      list.add(new KeyValuePair("f_1_0@root_2", "1"));
      list.add(new KeyValuePair("of_1_0@root_2", "1"));
      list.add(new KeyValuePair("f_1_0@root_3", "Die Insel des vorigen Tages"));
      list.add(new KeyValuePair("of_1_0@root_3", "Die Insel des vorigen Tages"));
      list.add(new KeyValuePair("ac_update_1_0@root_1", "Save"));
      list.add(new KeyValuePair("k_1_0@root", "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      post("http://localhost/bookstore/servlet/control;jsessionid=2911D29F8DBDDAC2420A02EA92936B56", list);
      printResponse();
      assertTrue(responseContains("<input type=\"text\" name=\"f_1_0@root_1\" value=\"3-423-12445-4\" />"));

   }

   public void testBooksSingleInsert() throws Exception {
      List list;
      get("http://localhost/bookstore/tests/testBOOKSSingle.jsp");

      list = new ArrayList();
      list.add(new KeyValuePair("invtable", "1"));
      list.add(new KeyValuePair("invname_1", ""));
      list.add(new KeyValuePair("autoupdate_1", "false"));
      list.add(new KeyValuePair("fu_1", "/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("source", "/bookstore/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("customEvent", ""));
      list.add(new KeyValuePair("firstpos_1", "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new KeyValuePair("lastpos_1", "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new KeyValuePair("f_1_0@root_0", "1"));
      list.add(new KeyValuePair("of_1_0@root_0", "1"));
      list.add(new KeyValuePair("f_1_0@root_1", "42-42-42"));
      list.add(new KeyValuePair("of_1_0@root_1", "42-42-42"));
      list.add(new KeyValuePair("f_1_0@root_3", "Die Insel des vorigen Tages"));
      list.add(new KeyValuePair("of_1_0@root_3", "Die Insel des vorigen Tages"));
      list.add(new KeyValuePair("ac_new_1_21", "New"));
      list.add(new KeyValuePair("k_1_0@root", "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      post("http://localhost/bookstore/servlet/control", list);
      assertTrue(responseContains("<input type=\"text\" name=\"f_1_insroot_1\" value=\"\" />"));

      list = new ArrayList();
      list.add(new KeyValuePair("invtable", "1"));
      list.add(new KeyValuePair("invname_1", ""));
      list.add(new KeyValuePair("autoupdate_1", "false"));
      list.add(new KeyValuePair("fu_1", "/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("source", "/bookstore/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("customEvent", ""));
      list.add(new KeyValuePair("f_1_insroot_0", "0"));
      list.add(new KeyValuePair("of_1_insroot_0", "0"));
      list.add(new KeyValuePair("f_1_insroot_1", "4711"));
      list.add(new KeyValuePair("of_1_insroot_1", ""));
      list.add(new KeyValuePair("f_1_insroot_2", "1"));
      list.add(new KeyValuePair("of_1_insroot_2", ""));
      list.add(new KeyValuePair("f_1_insroot_3", "test"));
      list.add(new KeyValuePair("of_1_insroot_3", ""));
      list.add(new KeyValuePair("ac_insert_1_root_22", "Insert"));
      list.add(new KeyValuePair("k_1_0@root", "null"));
      post("http://localhost/bookstore/servlet/control", list);
      assertTrue(responseContains("<input type=\"text\" name=\"f_1_0@root_1\" value=\"4711\" />"));

      list = new ArrayList();
      list.add(new KeyValuePair("invtable", "1"));
      list.add(new KeyValuePair("invname_1", ""));
      list.add(new KeyValuePair("autoupdate_1", "false"));
      list.add(new KeyValuePair("fu_1", "/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("source", "/bookstore/tests/testBOOKSSingle.jsp"));
      list.add(new KeyValuePair("customEvent", ""));
      list.add(new KeyValuePair("firstpos_1", "0%3A1%3A0-2%3A1%3A1"));
      list.add(new KeyValuePair("lastpos_1", "0%3A1%3A0-2%3A1%3A1"));
      list.add(new KeyValuePair("f_1_0@root_0", "0"));
      list.add(new KeyValuePair("of_1_0@root_0", "0"));
      list.add(new KeyValuePair("pf_1_0@root_0", "#,##0"));
      list.add(new KeyValuePair("f_1_0@root_1", "4711"));
      list.add(new KeyValuePair("of_1_0@root_1", "4711"));
      list.add(new KeyValuePair("f_1_0@root_2", "1"));
      list.add(new KeyValuePair("of_1_0@root_2", "1"));
      list.add(new KeyValuePair("pf_1_0@root_2", "#,##0"));
      list.add(new KeyValuePair("f_1_0@root_3", "test"));
      list.add(new KeyValuePair("of_1_0@root_3", "test"));
      list.add(new KeyValuePair("ac_delete_1_0@root_8", "Delete"));
      list.add(new KeyValuePair("k_1_0@root", "0%3A1%3A0-2%3A1%3A1"));
      post("http://localhost/bookstore/servlet/control", list);
      assertTrue(responseContains("<input type=\"text\" name=\"f_1_0@root_1\" value=\"3-423-12445-4\" />"));
      assertTrue(responseContains("<input type=\"text\" name=\"f_1_0@root_3\" value=\"Die Insel des vorigen Tages\" />"));
      
   }


}
