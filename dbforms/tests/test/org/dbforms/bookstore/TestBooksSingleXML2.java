// package
// This class was generated by MaxQ (maxq.tigris.org)
package org.dbforms.bookstore;

// imports
import java.util.List;
import java.util.ArrayList;
import HTTPClient.NVPair;

// definition of test class
public class TestBooksSingleXML2 extends AbstractTestBase
{
	// Test method generated from the MaxQ Java generator
	public TestBooksSingleXML2(String name)
	{
		super(name);
	}
	
	public void testBooksSingle() throws Exception
	{
		List list;
		System.out.println("Testing URL: " + replaceURL("http://localhost/bookstore/tests/testBOOKSSingleXML2.jsp"));
		get("http://localhost/bookstore/tests/testBOOKSSingleXML2.jsp");
		System.out.println("Response code: " + getResponse().getStatusCode());
		assertEquals("Assert number 6 failed", 200, getResponse().getStatusCode());
		printResponse();
		assertTrue(responseContains("Die Insel des vorigen Tages"));
		assertTrue(responseContains("Das Foucaltsche Pendel"));

		list = new ArrayList();
		list.add(new NVPair("invtable", "1"));
		list.add(new NVPair("invname_7", "null"));
		list.add(new NVPair("autoupdate_7", "false"));
		list.add(new NVPair("fu_7", "/tests/testBOOKSSingleXML2.jsp"));
		list.add(new NVPair("source", "/bookstore/tests/testBOOKSSingleXML2.jsp"));
		list.add(new NVPair("customEvent", ""));
		list.add(new NVPair("firstpos_7", "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
		list.add(new NVPair("lastpos_7", "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
		list.add(new NVPair("ac_next_7", "next"));
		list.add(new NVPair("k_7_0@root", "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
		list.add(new NVPair("k_7_7@root", "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
		System.out.println(
			"Testing URL: "
				+ replaceURL("http://localhost/bookstore/servlet/control?invtable=1&invname_7=null&autoupdate_7=false&fu_7=/tests/testBOOKSSingleXML2.jsp&source=/bookstore/tests/testBOOKSSingleXML2.jsp&customEvent=&firstpos_7=0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages&lastpos_7=0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel&ac_next_7=next&k_7_0@root=0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages&k_7_7@root=0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
		post("http://localhost/bookstore/servlet/control", list);
		System.out.println("Response code: " + getResponse().getStatusCode());
		assertEquals("Assert number 7 failed", 200, getResponse().getStatusCode());
		printResponse();
		assertTrue(responseContains("Hijacking through the Galaxy 1"));
		assertTrue(responseContains("Hijacking through the Galaxy 2"));

		list = new ArrayList();
		list.add(new NVPair("invtable", "1"));
		list.add(new NVPair("invname_7", "null"));
		list.add(new NVPair("autoupdate_7", "false"));
		list.add(new NVPair("fu_7", "/tests/testBOOKSSingleXML2.jsp"));
		list.add(new NVPair("source", "/bookstore/tests/testBOOKSSingleXML2.jsp"));
		list.add(new NVPair("customEvent", ""));
		list.add(new NVPair("firstpos_7", "0%3A1%3A3-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+1"));
		list.add(new NVPair("lastpos_7", "0%3A1%3A4-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+2"));
		list.add(new NVPair("ac_next_7", "next"));
		list.add(new NVPair("k_7_0@root", "0%3A1%3A3-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+1"));
		list.add(new NVPair("k_7_7@root", "0%3A1%3A4-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+2"));
		System.out.println(
			"Testing URL: "
				+ replaceURL("http://localhost/bookstore/servlet/control?invtable=1&invname_7=null&autoupdate_7=false&fu_7=/tests/testBOOKSSingleXML2.jsp&source=/bookstore/tests/testBOOKSSingleXML2.jsp&customEvent=&firstpos_7=0%3A1%3A3-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+1&lastpos_7=0%3A1%3A4-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+2&ac_next_7=next&k_7_0@root=0%3A1%3A3-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+1&k_7_7@root=0%3A1%3A4-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+2"));
		post("http://localhost/bookstore/servlet/control", list);
		System.out.println("Response code: " + getResponse().getStatusCode());
		assertEquals("Assert number 8 failed", 200, getResponse().getStatusCode());
		printResponse();
		assertTrue(responseContains("Hijacking through the Galaxy 3"));
		assertTrue(responseContains("Hijacking through the Galaxy 4"));

		list = new ArrayList();
		list.add(new NVPair("invtable", "1"));
		list.add(new NVPair("invname_7", "null"));
		list.add(new NVPair("autoupdate_7", "false"));
		list.add(new NVPair("fu_7", "/tests/testBOOKSSingleXML2.jsp"));
		list.add(new NVPair("source", "/bookstore/tests/testBOOKSSingleXML2.jsp"));
		list.add(new NVPair("customEvent", ""));
		list.add(new NVPair("firstpos_7", "0%3A1%3A5-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+3"));
		list.add(new NVPair("lastpos_7", "0%3A1%3A6-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+4"));
		list.add(new NVPair("ac_next_7", "next"));
		list.add(new NVPair("k_7_0@root", "0%3A1%3A5-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+3"));
		list.add(new NVPair("k_7_7@root", "0%3A1%3A6-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+4"));
		System.out.println(
			"Testing URL: "
				+ replaceURL("http://localhost/bookstore/servlet/control?invtable=1&invname_7=null&autoupdate_7=false&fu_7=/tests/testBOOKSSingleXML2.jsp&source=/bookstore/tests/testBOOKSSingleXML2.jsp&customEvent=&firstpos_7=0%3A1%3A5-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+3&lastpos_7=0%3A1%3A6-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+4&ac_next_7=next&k_7_0@root=0%3A1%3A5-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+3&k_7_7@root=0%3A1%3A6-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+4"));
		post("http://localhost/bookstore/servlet/control", list);
		System.out.println("Response code: " + getResponse().getStatusCode());
		assertEquals("Assert number 9 failed", 200, getResponse().getStatusCode());
		printResponse();
		assertTrue(responseContains("Test null value"));
		assertTrue(responseContains("Luca's favorite thing to eat is  \"delicious Italian pasta\""));

		list = new ArrayList();
		list.add(new NVPair("invtable", "1"));
		list.add(new NVPair("invname_7", "null"));
		list.add(new NVPair("autoupdate_7", "false"));
		list.add(new NVPair("fu_7", "/tests/testBOOKSSingleXML2.jsp"));
		list.add(new NVPair("source", "/bookstore/tests/testBOOKSSingleXML2.jsp"));
		list.add(new NVPair("customEvent", ""));
		list.add(new NVPair("firstpos_7", "0%3A1%3A7-2%3A0%3A-3%3A15%3ATest+null+value"));
		list.add(
			new NVPair(
				"lastpos_7",
				"0%3A1%3A8-2%3A1%3A2-3%3A58%3ALuca%27s+favorite+thing+to+eat+is++%22delicious+Italian+pasta%22"));
		list.add(new NVPair("ac_next_7", "next"));
		list.add(new NVPair("k_7_0@root", "0%3A1%3A7-2%3A0%3A-3%3A15%3ATest+null+value"));
		list.add(
			new NVPair(
				"k_7_7@root",
				"0%3A1%3A8-2%3A1%3A2-3%3A58%3ALuca%27s+favorite+thing+to+eat+is++%22delicious+Italian+pasta%22"));
		System.out.println(
			"Testing URL: "
				+ replaceURL("http://localhost/bookstore/servlet/control?invtable=1&invname_7=null&autoupdate_7=false&fu_7=/tests/testBOOKSSingleXML2.jsp&source=/bookstore/tests/testBOOKSSingleXML2.jsp&customEvent=&firstpos_7=0%3A1%3A7-2%3A0%3A-3%3A15%3ATest+null+value&lastpos_7=0%3A1%3A8-2%3A1%3A2-3%3A58%3ALuca%27s+favorite+thing+to+eat+is++%22delicious+Italian+pasta%22&ac_next_7=next&k_7_0@root=0%3A1%3A7-2%3A0%3A-3%3A15%3ATest+null+value&k_7_7@root=0%3A1%3A8-2%3A1%3A2-3%3A58%3ALuca%27s+favorite+thing+to+eat+is++%22delicious+Italian+pasta%22"));
		post("http://localhost/bookstore/servlet/control", list);
		System.out.println("Response code: " + getResponse().getStatusCode());
		assertEquals("Assert number 10 failed", 200, getResponse().getStatusCode());
		printResponse();
		assertTrue(responseContains("Hijacking through the Galaxy 6"));

		list = new ArrayList();
		list.add(new NVPair("invtable", "1"));
		list.add(new NVPair("invname_7", "null"));
		list.add(new NVPair("autoupdate_7", "false"));
		list.add(new NVPair("fu_7", "/tests/testBOOKSSingleXML2.jsp"));
		list.add(new NVPair("source", "/bookstore/tests/testBOOKSSingleXML2.jsp"));
		list.add(new NVPair("customEvent", ""));
		list.add(new NVPair("firstpos_7", "0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6"));
		list.add(new NVPair("lastpos_7", "0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6"));
		list.add(new NVPair("ac_next_7", "next"));
		list.add(new NVPair("k_7_0@root", "0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6"));
		System.out.println(
			"Testing URL: "
				+ replaceURL("http://localhost/bookstore/servlet/control?invtable=1&invname_7=null&autoupdate_7=false&fu_7=/tests/testBOOKSSingleXML2.jsp&source=/bookstore/tests/testBOOKSSingleXML2.jsp&customEvent=&firstpos_7=0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6&lastpos_7=0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6&ac_next_7=next&k_7_0@root=0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6"));
		post("http://localhost/bookstore/servlet/control", list);
		System.out.println("Response code: " + getResponse().getStatusCode());
		assertEquals("Assert number 11 failed", 200, getResponse().getStatusCode());
		printResponse();
		assertTrue(responseContains("Luca's favorite thing to eat is  \"delicious Italian pasta\""));
		assertTrue(responseContains("Hijacking through the Galaxy 6"));

	}

	/****************/

}
