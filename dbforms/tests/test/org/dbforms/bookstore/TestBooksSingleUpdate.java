// package
// This class was generated by MaxQ (maxq.tigris.org)
package org.dbforms.bookstore;
// imports
import java.util.ArrayList;
import java.util.List;
import HTTPClient.NVPair;

// definition of test class
public class TestBooksSingleUpdate extends AbstractTestBase
{
	// Test method generated from the MaxQ Java generator
	public TestBooksSingleUpdate(String name)
	{
		super(name);
	}

   public void testBooksSingleUpdate() throws Exception {
      List list;
      System.out.println("Testing URL: " + replaceURL("http://localhost/bookstore/tests/testBOOKSSingle.jsp"));
      get("http://localhost/bookstore/tests/testBOOKSSingle.jsp");
      System.out.println("Response code: " + getResponse().getStatusCode());
      assertEquals("Assert number 1 failed", 200, getResponse().getStatusCode());
      assertTrue(responseContains("<input type=\"text\" name=\"f_1_0@root_1\" value=\"3-423-12445-4\" />"));
      
      list = new ArrayList();
      list.add(new NVPair("invtable", "1"));
      list.add(new NVPair("invname_1", ""));
      list.add(new NVPair("autoupdate_1", "false"));
      list.add(new NVPair("fu_1", "/tests/testBOOKSSingle.jsp"));
      list.add(new NVPair("source", "/bookstore/tests/testBOOKSSingle.jsp"));
      list.add(new NVPair("customEvent", ""));
      list.add(new NVPair("firstpos_1", "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new NVPair("lastpos_1", "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new NVPair("f_1_0@root_0", "1"));
      list.add(new NVPair("of_1_0@root_0", "1"));
      list.add(new NVPair("f_1_0@root_1", "42-42-42"));
      list.add(new NVPair("of_1_0@root_1", "3-423-12445-4"));
      list.add(new NVPair("f_1_0@root_2", "1"));
      list.add(new NVPair("of_1_0@root_2", "1"));
      list.add(new NVPair("f_1_0@root_3", "Die Insel des vorigen Tages"));
      list.add(new NVPair("of_1_0@root_3", "Die Insel des vorigen Tages"));
      list.add(new NVPair("ac_update_1_0@root_1", "Save"));
      list.add(new NVPair("k_1_0@root", "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      System.out.println(
         "Testing URL: "
            + replaceURL("http://localhost/bookstore/servlet/control;jsessionid=2911D29F8DBDDAC2420A02EA92936B56?invtable=1&invname_1=&autoupdate_1=false&fu_1=/tests/testBOOKSSingle.jsp&source=/bookstore/tests/testBOOKSSingle.jsp&customEvent=&firstpos_1=0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages&lastpos_1=0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages&f_1_0@root_0=1&of_1_0@root_0=1&f_1_0@root_1=42-42-42&of_1_0@root_1=3-423-12445-4&f_1_0@root_3=Die Insel des vorigen Tages&of_1_0@root_3=Die Insel des vorigen Tages&ac_update_1_0@root_1=Save&k_1_0@root=0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      post("http://localhost/bookstore/servlet/control;jsessionid=2911D29F8DBDDAC2420A02EA92936B56", list);
      System.out.println("Response code: " + getResponse().getStatusCode());
      assertEquals("Assert number 2 failed", 200, getResponse().getStatusCode());
      printResponse();
      assertTrue(responseContains("<input type=\"text\" name=\"f_1_0@root_1\" value=\"42-42-42\" />"));

   }

   public void testBooksSingleInsert() throws Exception {
      List list;
      System.out.println("Testing URL: " + replaceURL("http://localhost/bookstore/tests/testBOOKSSingle.jsp"));
      get("http://localhost/bookstore/tests/testBOOKSSingle.jsp");
      System.out.println("Response code: " + getResponse().getStatusCode());
      assertEquals("Assert number 4 failed", 200, getResponse().getStatusCode());

      list = new ArrayList();
      list.add(new NVPair("invtable", "1"));
      list.add(new NVPair("invname_1", ""));
      list.add(new NVPair("autoupdate_1", "false"));
      list.add(new NVPair("fu_1", "/tests/testBOOKSSingle.jsp"));
      list.add(new NVPair("source", "/bookstore/tests/testBOOKSSingle.jsp"));
      list.add(new NVPair("customEvent", ""));
      list.add(new NVPair("firstpos_1", "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new NVPair("lastpos_1", "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new NVPair("f_1_0@root_0", "1"));
      list.add(new NVPair("of_1_0@root_0", "1"));
      list.add(new NVPair("f_1_0@root_1", "42-42-42"));
      list.add(new NVPair("of_1_0@root_1", "42-42-42"));
      list.add(new NVPair("f_1_0@root_3", "Die Insel des vorigen Tages"));
      list.add(new NVPair("of_1_0@root_3", "Die Insel des vorigen Tages"));
      list.add(new NVPair("ac_new_1_21", "New"));
      list.add(new NVPair("k_1_0@root", "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      System.out.println(
         "Testing URL: "
            + replaceURL("http://localhost/bookstore/servlet/control?invtable=1&invname_1=&autoupdate_1=false&fu_1=/tests/testBOOKSSingle.jsp&source=/bookstore/tests/testBOOKSSingle.jsp&customEvent=&firstpos_1=0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages&lastpos_1=0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages&f_1_0@root_0=1&of_1_0@root_0=1&f_1_0@root_1=42-42-42&of_1_0@root_1=42-42-42&f_1_0@root_3=Die Insel des vorigen Tages&of_1_0@root_3=Die Insel des vorigen Tages&ac_new_1_21=New&k_1_0@root=0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      post("http://localhost/bookstore/servlet/control", list);
      System.out.println("Response code: " + getResponse().getStatusCode());
      assertEquals("Assert number 5 failed", 200, getResponse().getStatusCode());
      assertTrue(responseContains("<input type=\"text\" name=\"f_1_insroot_1\" value=\"\" />"));

      list = new ArrayList();
      list.add(new NVPair("invtable", "1"));
      list.add(new NVPair("invname_1", ""));
      list.add(new NVPair("autoupdate_1", "false"));
      list.add(new NVPair("fu_1", "/tests/testBOOKSSingle.jsp"));
      list.add(new NVPair("source", "/bookstore/tests/testBOOKSSingle.jsp"));
      list.add(new NVPair("customEvent", ""));
      list.add(new NVPair("f_1_insroot_0", "0"));
      list.add(new NVPair("of_1_insroot_0", "0"));
      list.add(new NVPair("f_1_insroot_1", "4711"));
      list.add(new NVPair("of_1_insroot_1", ""));
      list.add(new NVPair("f_1_insroot_2", "1"));
      list.add(new NVPair("of_1_insroot_2", ""));
      list.add(new NVPair("f_1_insroot_3", "test"));
      list.add(new NVPair("of_1_insroot_3", ""));
      list.add(new NVPair("ac_insert_1_root_22", "Insert"));
      list.add(new NVPair("k_1_0@root", "null"));
      System.out.println(
         "Testing URL: "
            + replaceURL("http://localhost/bookstore/servlet/control?invtable=1&invname_1=&autoupdate_1=false&fu_1=/tests/testBOOKSSingle.jsp&source=/bookstore/tests/testBOOKSSingle.jsp&customEvent=&f_1_insroot_0=0&of_1_insroot_0=0&f_1_insroot_1=4711&of_1_insroot_1=&f_1_insroot_3=test&of_1_insroot_3=&ac_insert_1_root_22=Insert&k_1_0@root=null"));
      post("http://localhost/bookstore/servlet/control", list);
      System.out.println("Response code: " + getResponse().getStatusCode());
      assertEquals("Assert number 6 failed", 200, getResponse().getStatusCode());
      assertTrue(responseContains("<input type=\"text\" name=\"f_1_0@root_1\" value=\"4711\" />"));

   }


}
