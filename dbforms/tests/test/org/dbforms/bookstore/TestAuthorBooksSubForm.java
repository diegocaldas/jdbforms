// package
// This class was generated by MaxQ (maxq.tigris.org)
package org.dbforms.bookstore;
// imports
import java.util.List;
import java.util.ArrayList;

import HTTPClient.NVPair;

// definition of test class
public class TestAuthorBooksSubForm extends AbstractTestBase
{
	// Test method generated from the MaxQ Java generator
	public TestAuthorBooksSubForm(String name)
	{
		super(name);
	}

	/**
	 * Start the tests.
	 *
	 * @param theArgs the arguments. Not used
	 */
	public static void main(String[] theArgs)
	{
		junit.textui.TestRunner.main(new String[] { TestAuthorBooksSubForm.class.getName()});
	}

	public void testNavNewMain() throws Exception
	{
		List list;

		System.out.println("Testing URL: " + replaceURL("http://localhost/bookstore/tests/testAuthorBooksSubForm.jsp"));
		get("http://localhost/bookstore/tests/testAuthorBooksSubForm.jsp");
		System.out.println("Response code: " + getResponse().getStatusCode());
		assertEquals("Assert number 1 failed", 200, getResponse().getStatusCode());
		printResponse();

		list = new ArrayList();
		list.add(new NVPair("invtable", "0"));
		list.add(new NVPair("invname_0", "null"));
		list.add(new NVPair("autoupdate_0", "false"));
		list.add(new NVPair("fu_0", "/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("source", "/bookstore/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("customEvent", ""));
		list.add(new NVPair("firstpos_0", "0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11"));
		list.add(new NVPair("lastpos_0", "0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11"));
		list.add(new NVPair("f_0_0@root_1", "Eco, Umberto"));
		list.add(new NVPair("f_0_0@root_2", "organisation 11"));
		list.add(new NVPair("ac_next_0", ">  Next"));
		list.add(new NVPair("invtable", "1"));
		list.add(new NVPair("invname_1", "null"));
		list.add(new NVPair("autoupdate_1", "false"));
		list.add(new NVPair("fu_1", "/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("source", "/bookstore/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("customEvent", ""));
		list.add(new NVPair("firstpos_1", "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
		list.add(new NVPair("lastpos_1", "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
		list.add(new NVPair("f_1_0@0@root_1", "3-423-12445-4"));
		list.add(new NVPair("f_1_0@0@root_3", "Die Insel des vorigen Tages"));
		list.add(new NVPair("f_1_1@0@root_1", "3-423-12445-5"));
		list.add(new NVPair("f_1_1@0@root_3", "Das Foucaltsche Pendel"));
		list.add(new NVPair("k_1_0@0@root", "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
		list.add(new NVPair("f_1_ins0@root_2", "1"));
		list.add(new NVPair("k_1_1@0@root", "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
		list.add(new NVPair("f_1_ins0@root_2", "1"));
		list.add(new NVPair("k_0_0@root", "0%3A1%3A1"));
		System.out.println(
			"Testing URL: "
				+ replaceURL("http://localhost/bookstore/servlet/control?invtable=0&invname_0=null&autoupdate_0=false&fu_0=/tests/testAuthorBooksSubForm.jsp&source=/bookstore/tests/testAuthorBooksSubForm.jsp&customEvent=&firstpos_0=0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11&lastpos_0=0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11&f_0_0@root_1=Eco, Umberto&f_0_0@root_2=organisation 11&ac_next_0=>  Next&invtable=1&invname_1=null&autoupdate_1=false&fu_1=/tests/testAuthorBooksSubForm.jsp&source=/bookstore/tests/testAuthorBooksSubForm.jsp&customEvent=&firstpos_1=0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages&lastpos_1=0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel&f_1_0@0@root_1=3-423-12445-4&f_1_0@0@root_3=Die Insel des vorigen Tages&f_1_1@0@root_1=3-423-12445-5&f_1_1@0@root_3=Das Foucaltsche Pendel&k_1_0@0@root=0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages&f_1_ins0@root_2=1&k_1_1@0@root=0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel&f_1_ins0@root_2=1&k_0_0@root=0%3A1%3A1"));
		post("http://localhost/bookstore/servlet/control", list);
		System.out.println("Response code: " + getResponse().getStatusCode());
		assertEquals("Assert number 2 failed", 200, getResponse().getStatusCode());
		printResponse();

		list = new ArrayList();
		list.add(new NVPair("invtable", "0"));
		list.add(new NVPair("invname_0", "null"));
		list.add(new NVPair("autoupdate_0", "false"));
		list.add(new NVPair("fu_0", "/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("source", "/bookstore/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("customEvent", ""));
		list.add(new NVPair("firstpos_0", "0%3A1%3A2-1%3A13%3ADouglas%2C+Adam-2%3A14%3Aorganisation+2"));
		list.add(new NVPair("lastpos_0", "0%3A1%3A2-1%3A13%3ADouglas%2C+Adam-2%3A14%3Aorganisation+2"));
		list.add(new NVPair("f_0_0@root_1", "Douglas, Adam"));
		list.add(new NVPair("f_0_0@root_2", "organisation 2"));
		list.add(new NVPair("ac_next_0", ">  Next"));
		list.add(new NVPair("invtable", "1"));
		list.add(new NVPair("invname_1", "null"));
		list.add(new NVPair("autoupdate_1", "false"));
		list.add(new NVPair("fu_1", "/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("source", "/bookstore/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("customEvent", ""));
		list.add(new NVPair("firstpos_1", "0%3A1%3A3-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+1"));
		list.add(new NVPair("lastpos_1", "0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6"));
		list.add(new NVPair("f_1_0@0@root_1", "42-1"));
		list.add(new NVPair("f_1_0@0@root_3", "Hijacking through the Galaxy 1"));
		list.add(new NVPair("f_1_1@0@root_1", "42-2"));
		list.add(new NVPair("f_1_1@0@root_3", "Hijacking through the Galaxy 2"));
		list.add(new NVPair("f_1_2@0@root_1", "42-3"));
		list.add(new NVPair("f_1_2@0@root_3", "Hijacking through the Galaxy 3"));
		list.add(new NVPair("f_1_3@0@root_1", "42-4"));
		list.add(new NVPair("f_1_3@0@root_3", "Hijacking through the Galaxy 4"));
		list.add(new NVPair("f_1_4@0@root_1", "42-5"));
		list.add(new NVPair("f_1_4@0@root_3", "Luca\'s favorite thing to eat is  "));
		list.add(new NVPair("f_1_5@0@root_1", "42-6"));
		list.add(new NVPair("f_1_5@0@root_3", "Hijacking through the Galaxy 6"));
		list.add(new NVPair("k_1_0@0@root", "0%3A1%3A3-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+1"));
		list.add(new NVPair("f_1_ins0@root_2", "2"));
		list.add(new NVPair("k_1_1@0@root", "0%3A1%3A4-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+2"));
		list.add(new NVPair("f_1_ins0@root_2", "2"));
		list.add(new NVPair("k_1_2@0@root", "0%3A1%3A5-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+3"));
		list.add(new NVPair("f_1_ins0@root_2", "2"));
		list.add(new NVPair("k_1_3@0@root", "0%3A1%3A6-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+4"));
		list.add(new NVPair("f_1_ins0@root_2", "2"));
		list.add(
			new NVPair(
				"k_1_4@0@root",
				"0%3A1%3A8-2%3A1%3A2-3%3A58%3ALuca%27s+favorite+thing+to+eat+is++%22delicious+Italian+pasta%22"));
		list.add(new NVPair("f_1_ins0@root_2", "2"));
		list.add(new NVPair("k_1_5@0@root", "0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6"));
		list.add(new NVPair("f_1_ins0@root_2", "2"));
		list.add(new NVPair("k_0_0@root", "0%3A1%3A2"));
		System.out.println(
			"Testing URL: "
				+ replaceURL("http://localhost/bookstore/servlet/control?invtable=0&invname_0=null&autoupdate_0=false&fu_0=/tests/testAuthorBooksSubForm.jsp&source=/bookstore/tests/testAuthorBooksSubForm.jsp&customEvent=&firstpos_0=0%3A1%3A2-1%3A13%3ADouglas%2C+Adam-2%3A14%3Aorganisation+2&lastpos_0=0%3A1%3A2-1%3A13%3ADouglas%2C+Adam-2%3A14%3Aorganisation+2&f_0_0@root_1=Douglas, Adam&f_0_0@root_2=organisation 2&ac_next_0=>  Next&invtable=1&invname_1=null&autoupdate_1=false&fu_1=/tests/testAuthorBooksSubForm.jsp&source=/bookstore/tests/testAuthorBooksSubForm.jsp&customEvent=&firstpos_1=0%3A1%3A3-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+1&lastpos_1=0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6&f_1_0@0@root_1=42-1&f_1_0@0@root_3=Hijacking through the Galaxy 1&f_1_1@0@root_1=42-2&f_1_1@0@root_3=Hijacking through the Galaxy 2&f_1_2@0@root_1=42-3&f_1_2@0@root_3=Hijacking through the Galaxy 3&f_1_3@0@root_1=42-4&f_1_3@0@root_3=Hijacking through the Galaxy 4&f_1_4@0@root_1=42-5&f_1_4@0@root_3=Luca\'s favorite thing to eat is  &f_1_5@0@root_1=42-6&f_1_5@0@root_3=Hijacking through the Galaxy 6&k_1_0@0@root=0%3A1%3A3-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+1&f_1_ins0@root_2=2&k_1_1@0@root=0%3A1%3A4-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+2&f_1_ins0@root_2=2&k_1_2@0@root=0%3A1%3A5-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+3&f_1_ins0@root_2=2&k_1_3@0@root=0%3A1%3A6-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+4&f_1_ins0@root_2=2&k_1_4@0@root=0%3A1%3A8-2%3A1%3A2-3%3A58%3ALuca%27s+favorite+thing+to+eat+is++%22delicious+Italian+pasta%22&f_1_ins0@root_2=2&k_1_5@0@root=0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6&f_1_ins0@root_2=2&k_0_0@root=0%3A1%3A2"));
		post("http://localhost/bookstore/servlet/control", list);
		System.out.println("Response code: " + getResponse().getStatusCode());
		assertEquals("Assert number 3 failed", 200, getResponse().getStatusCode());
		printResponse();
		assertTrue(responseContains("<input type=\"submit\"  value=\"New\"  style=\"width:100\" name=\"ac_new_0"));
		assertTrue(responseContains("<input type=\"submit\"  value=\"Copy\"  style=\"width:100\" name=\"ac_copy_0"));

		list = new ArrayList();
		list.add(new NVPair("invtable", "0"));
		list.add(new NVPair("invname_0", "null"));
		list.add(new NVPair("autoupdate_0", "false"));
		list.add(new NVPair("fu_0", "/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("source", "/bookstore/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("customEvent", ""));
		list.add(new NVPair("firstpos_0", "0%3A1%3A2-1%3A13%3ADouglas%2C+Adam-2%3A14%3Aorganisation+2"));
		list.add(new NVPair("lastpos_0", "0%3A1%3A2-1%3A13%3ADouglas%2C+Adam-2%3A14%3Aorganisation+2"));
		list.add(new NVPair("f_0_0@root_1", "Douglas, Adam"));
		list.add(new NVPair("f_0_0@root_2", "organisation 2"));
		list.add(new NVPair("ac_new_0", "New"));
		list.add(new NVPair("invtable", "1"));
		list.add(new NVPair("invname_1", "null"));
		list.add(new NVPair("autoupdate_1", "false"));
		list.add(new NVPair("fu_1", "/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("source", "/bookstore/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("customEvent", ""));
		list.add(new NVPair("firstpos_1", "0%3A1%3A3-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+1"));
		list.add(new NVPair("lastpos_1", "0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6"));
		list.add(new NVPair("f_1_0@0@root_1", "42-1"));
		list.add(new NVPair("f_1_0@0@root_3", "Hijacking through the Galaxy 1"));
		list.add(new NVPair("f_1_1@0@root_1", "42-2"));
		list.add(new NVPair("f_1_1@0@root_3", "Hijacking through the Galaxy 2"));
		list.add(new NVPair("f_1_2@0@root_1", "42-3"));
		list.add(new NVPair("f_1_2@0@root_3", "Hijacking through the Galaxy 3"));
		list.add(new NVPair("f_1_3@0@root_1", "42-4"));
		list.add(new NVPair("f_1_3@0@root_3", "Hijacking through the Galaxy 4"));
		list.add(new NVPair("f_1_4@0@root_1", "42-5"));
		list.add(new NVPair("f_1_4@0@root_3", "Luca\'s favorite thing to eat is  "));
		list.add(new NVPair("f_1_5@0@root_1", "42-6"));
		list.add(new NVPair("f_1_5@0@root_3", "Hijacking through the Galaxy 6"));
		list.add(new NVPair("k_1_0@0@root", "0%3A1%3A3-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+1"));
		list.add(new NVPair("f_1_ins0@root_2", "2"));
		list.add(new NVPair("k_1_1@0@root", "0%3A1%3A4-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+2"));
		list.add(new NVPair("f_1_ins0@root_2", "2"));
		list.add(new NVPair("k_1_2@0@root", "0%3A1%3A5-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+3"));
		list.add(new NVPair("f_1_ins0@root_2", "2"));
		list.add(new NVPair("k_1_3@0@root", "0%3A1%3A6-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+4"));
		list.add(new NVPair("f_1_ins0@root_2", "2"));
		list.add(
			new NVPair(
				"k_1_4@0@root",
				"0%3A1%3A8-2%3A1%3A2-3%3A58%3ALuca%27s+favorite+thing+to+eat+is++%22delicious+Italian+pasta%22"));
		list.add(new NVPair("f_1_ins0@root_2", "2"));
		list.add(new NVPair("k_1_5@0@root", "0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6"));
		list.add(new NVPair("f_1_ins0@root_2", "2"));
		list.add(new NVPair("k_0_0@root", "0%3A1%3A2"));
		System.out.println(
			"Testing URL: "
				+ replaceURL("http://localhost/bookstore/servlet/control?invtable=0&invname_0=null&autoupdate_0=false&fu_0=/tests/testAuthorBooksSubForm.jsp&source=/bookstore/tests/testAuthorBooksSubForm.jsp&customEvent=&firstpos_0=0%3A1%3A2-1%3A13%3ADouglas%2C+Adam-2%3A14%3Aorganisation+2&lastpos_0=0%3A1%3A2-1%3A13%3ADouglas%2C+Adam-2%3A14%3Aorganisation+2&f_0_0@root_1=Douglas, Adam&f_0_0@root_2=organisation 2&ac_new_0=New&invtable=1&invname_1=null&autoupdate_1=false&fu_1=/tests/testAuthorBooksSubForm.jsp&source=/bookstore/tests/testAuthorBooksSubForm.jsp&customEvent=&firstpos_1=0%3A1%3A3-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+1&lastpos_1=0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6&f_1_0@0@root_1=42-1&f_1_0@0@root_3=Hijacking through the Galaxy 1&f_1_1@0@root_1=42-2&f_1_1@0@root_3=Hijacking through the Galaxy 2&f_1_2@0@root_1=42-3&f_1_2@0@root_3=Hijacking through the Galaxy 3&f_1_3@0@root_1=42-4&f_1_3@0@root_3=Hijacking through the Galaxy 4&f_1_4@0@root_1=42-5&f_1_4@0@root_3=Luca\'s favorite thing to eat is  &f_1_5@0@root_1=42-6&f_1_5@0@root_3=Hijacking through the Galaxy 6&k_1_0@0@root=0%3A1%3A3-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+1&f_1_ins0@root_2=2&k_1_1@0@root=0%3A1%3A4-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+2&f_1_ins0@root_2=2&k_1_2@0@root=0%3A1%3A5-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+3&f_1_ins0@root_2=2&k_1_3@0@root=0%3A1%3A6-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+4&f_1_ins0@root_2=2&k_1_4@0@root=0%3A1%3A8-2%3A1%3A2-3%3A58%3ALuca%27s+favorite+thing+to+eat+is++%22delicious+Italian+pasta%22&f_1_ins0@root_2=2&k_1_5@0@root=0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6&f_1_ins0@root_2=2&k_0_0@root=0%3A1%3A2"));
		post("http://localhost/bookstore/servlet/control", list);
		System.out.println("Response code: " + getResponse().getStatusCode());
		assertEquals("Assert number 4 failed", 200, getResponse().getStatusCode());
		printResponse();
		assertFalse(responseContains("<input type=\"submit\"  value=\"New\"  style=\"width:100\" name=\"ac_new_0"));
		assertFalse(responseContains("<input type=\"submit\"  value=\"Copy\"  style=\"width:100\" name=\"ac_copy_0"));
		assertTrue(responseContains("<td style=\"width:100px\">[No Data]&nbsp;</td>"));
		assertTrue(
			responseContains("<input type=\"text\" name=\"f_0_insroot_1\" value=\"\"  size=\"25\"/>"));
		assertTrue(responseContains("<input type=\"text\" name=\"f_0_insroot_2\" value=\"\"  size=\"25\"/>"));

	}

	public void testNavCopyMain() throws Exception
	{

		List list;
		System.out.println("Testing URL: " + replaceURL("http://localhost/bookstore/tests/testAuthorBooksSubForm.jsp"));
		get("http://localhost/bookstore/tests/testAuthorBooksSubForm.jsp");
		System.out.println("Response code: " + getResponse().getStatusCode());
		assertEquals("Assert number 6 failed", 200, getResponse().getStatusCode());
		printResponse();
		assertTrue(responseContains("<input type=\"submit\"  value=\"New\"  style=\"width:100\" name=\"ac_new_0"));
		assertTrue(responseContains("<input type=\"submit\"  value=\"Copy\"  style=\"width:100\" name=\"ac_copy_0"));

		list = new ArrayList();
		list.add(new NVPair("invtable", "0"));
		list.add(new NVPair("invname_0", "null"));
		list.add(new NVPair("autoupdate_0", "false"));
		list.add(new NVPair("fu_0", "/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("source", "/bookstore/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("customEvent", ""));
		list.add(new NVPair("firstpos_0", "0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11"));
		list.add(new NVPair("lastpos_0", "0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11"));
		list.add(new NVPair("f_0_0@root_1", "Eco, Umberto"));
		list.add(new NVPair("f_0_0@root_2", "organisation 11"));
		list.add(new NVPair("ac_copy_0", "Copy"));
		list.add(new NVPair("invtable", "1"));
		list.add(new NVPair("invname_1", "null"));
		list.add(new NVPair("autoupdate_1", "false"));
		list.add(new NVPair("fu_1", "/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("source", "/bookstore/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("customEvent", ""));
		list.add(new NVPair("firstpos_1", "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
		list.add(new NVPair("lastpos_1", "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
		list.add(new NVPair("f_1_0@0@root_1", "3-423-12445-4"));
		list.add(new NVPair("f_1_0@0@root_3", "Die Insel des vorigen Tages"));
		list.add(new NVPair("f_1_1@0@root_1", "3-423-12445-5"));
		list.add(new NVPair("f_1_1@0@root_3", "Das Foucaltsche Pendel"));
		list.add(new NVPair("k_1_0@0@root", "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
		list.add(new NVPair("f_1_ins0@root_2", "1"));
		list.add(new NVPair("k_1_1@0@root", "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
		list.add(new NVPair("f_1_ins0@root_2", "1"));
		list.add(new NVPair("k_0_0@root", "0%3A1%3A1"));
		System.out.println(
			"Testing URL: "
				+ replaceURL("http://localhost/bookstore/servlet/control?invtable=0&invname_0=null&autoupdate_0=false&fu_0=/tests/testAuthorBooksSubForm.jsp&source=/bookstore/tests/testAuthorBooksSubForm.jsp&customEvent=&firstpos_0=0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11&lastpos_0=0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11&f_0_0@root_1=Eco, Umberto&f_0_0@root_2=organisation 11&ac_copy_0=Copy&invtable=1&invname_1=null&autoupdate_1=false&fu_1=/tests/testAuthorBooksSubForm.jsp&source=/bookstore/tests/testAuthorBooksSubForm.jsp&customEvent=&firstpos_1=0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages&lastpos_1=0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel&f_1_0@0@root_1=3-423-12445-4&f_1_0@0@root_3=Die Insel des vorigen Tages&f_1_1@0@root_1=3-423-12445-5&f_1_1@0@root_3=Das Foucaltsche Pendel&k_1_0@0@root=0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages&f_1_ins0@root_2=1&k_1_1@0@root=0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel&f_1_ins0@root_2=1&k_0_0@root=0%3A1%3A1"));
		post("http://localhost/bookstore/servlet/control", list);
		System.out.println("Response code: " + getResponse().getStatusCode());
		assertEquals("Assert number 7 failed", 200, getResponse().getStatusCode());
		printResponse();
		assertFalse(responseContains("<input type=\"submit\"  value=\"New\"  style=\"width:100\" name=\"ac_new_0"));
		assertFalse(responseContains("<input type=\"submit\"  value=\"Copy\"  style=\"width:100\" name=\"ac_copy_0"));
		assertTrue(responseContains("<td style=\"width:100px\">[No Data]&nbsp;</td>"));
		assertTrue(
			responseContains("<input type=\"text\" name=\"f_0_insroot_1\" value=\"Eco, Umberto\"  size=\"25\"/>"));
		assertTrue(responseContains("<input type=\"text\" name=\"f_0_insroot_2\" value=\"organisation 11\"  size=\"25\"/>"));

	}

	public void testNavNewSub() throws Exception
	{
		List list;

		System.out.println("Testing URL: " + replaceURL("http://localhost/bookstore/tests/testAuthorBooksSubForm.jsp"));
		get("http://localhost/bookstore/tests/testAuthorBooksSubForm.jsp");
		System.out.println("Response code: " + getResponse().getStatusCode());
		assertEquals("Assert number 8 failed", 200, getResponse().getStatusCode());
		printResponse();
		assertTrue(responseContains("<input type=\"submit\"  value=\"New\"  style=\"width:100\" name=\"ac_new_1"));
		assertTrue(responseContains("<input type=\"submit\"  value=\"Copy\"  style=\"width:100\" name=\"ac_copy_1"));

		list = new ArrayList();
		list.add(new NVPair("invtable", "0"));
		list.add(new NVPair("invname_0", "null"));
		list.add(new NVPair("autoupdate_0", "false"));
		list.add(new NVPair("fu_0", "/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("source", "/bookstore/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("customEvent", ""));
		list.add(new NVPair("firstpos_0", "0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11"));
		list.add(new NVPair("lastpos_0", "0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11"));
		list.add(new NVPair("f_0_0@root_1", "Eco, Umberto"));
		list.add(new NVPair("f_0_0@root_2", "organisation 11"));
		list.add(new NVPair("invtable", "1"));
		list.add(new NVPair("invname_1", "null"));
		list.add(new NVPair("autoupdate_1", "false"));
		list.add(new NVPair("fu_1", "/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("source", "/bookstore/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("customEvent", ""));
		list.add(new NVPair("firstpos_1", "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
		list.add(new NVPair("lastpos_1", "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
		list.add(new NVPair("f_1_0@0@root_1", "3-423-12445-4"));
		list.add(new NVPair("f_1_0@0@root_3", "Die Insel des vorigen Tages"));
		list.add(new NVPair("f_1_1@0@root_1", "3-423-12445-5"));
		list.add(new NVPair("f_1_1@0@root_3", "Das Foucaltsche Pendel"));
		list.add(new NVPair("ac_new_1", "New"));
		list.add(new NVPair("k_1_0@0@root", "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
		list.add(new NVPair("f_1_ins0@root_2", "1"));
		list.add(new NVPair("k_1_1@0@root", "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
		list.add(new NVPair("f_1_ins0@root_2", "1"));
		list.add(new NVPair("k_0_0@root", "0%3A1%3A1"));
		System.out.println(
			"Testing URL: "
				+ replaceURL("http://localhost/bookstore/servlet/control?invtable=0&invname_0=null&autoupdate_0=false&fu_0=/tests/testAuthorBooksSubForm.jsp&source=/bookstore/tests/testAuthorBooksSubForm.jsp&customEvent=&firstpos_0=0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11&lastpos_0=0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11&f_0_0@root_1=Eco, Umberto&f_0_0@root_2=organisation 11&invtable=1&invname_1=null&autoupdate_1=false&fu_1=/tests/testAuthorBooksSubForm.jsp&source=/bookstore/tests/testAuthorBooksSubForm.jsp&customEvent=&firstpos_1=0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages&lastpos_1=0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel&f_1_0@0@root_1=3-423-12445-4&f_1_0@0@root_3=Die Insel des vorigen Tages&f_1_1@0@root_1=3-423-12445-5&f_1_1@0@root_3=Das Foucaltsche Pendel&ac_new_1=New&k_1_0@0@root=0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages&f_1_ins0@root_2=1&k_1_1@0@root=0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel&f_1_ins0@root_2=1&k_0_0@root=0%3A1%3A1"));
		post("http://localhost/bookstore/servlet/control", list);
		System.out.println("Response code: " + getResponse().getStatusCode());
		assertEquals("Assert number 9 failed", 200, getResponse().getStatusCode());
		printResponse();
		assertTrue(responseContains("<input type=\"submit\"  value=\"New\"  style=\"width:100\" name=\"ac_new_1"));
		assertTrue(responseContains("<input type=\"submit\"  value=\"Copy\"  style=\"width:100\" name=\"ac_copy_1"));

	}

	public void testNavCopySub() throws Exception
	{

		List list;

		System.out.println("Testing URL: " + replaceURL("http://localhost/bookstore/tests/testAuthorBooksSubForm.jsp"));
		get("http://localhost/bookstore/tests/testAuthorBooksSubForm.jsp");
		System.out.println("Response code: " + getResponse().getStatusCode());
		assertEquals("Assert number 10 failed", 200, getResponse().getStatusCode());
		printResponse();
		assertTrue(responseContains("<input type=\"submit\"  value=\"New\"  style=\"width:100\" name=\"ac_new_1"));
		assertTrue(responseContains("<input type=\"submit\"  value=\"Copy\"  style=\"width:100\" name=\"ac_copy_1"));

		list = new ArrayList();
		list.add(new NVPair("invtable", "0"));
		list.add(new NVPair("invname_0", "null"));
		list.add(new NVPair("autoupdate_0", "false"));
		list.add(new NVPair("fu_0", "/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("source", "/bookstore/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("customEvent", ""));
		list.add(new NVPair("firstpos_0", "0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11"));
		list.add(new NVPair("lastpos_0", "0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11"));
		list.add(new NVPair("f_0_0@root_1", "Eco, Umberto"));
		list.add(new NVPair("f_0_0@root_2", "organisation 11"));
		list.add(new NVPair("invtable", "1"));
		list.add(new NVPair("invname_1", "null"));
		list.add(new NVPair("autoupdate_1", "false"));
		list.add(new NVPair("fu_1", "/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("source", "/bookstore/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("customEvent", ""));
		list.add(new NVPair("firstpos_1", "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
		list.add(new NVPair("lastpos_1", "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
		list.add(new NVPair("f_1_0@0@root_1", "3-423-12445-4"));
		list.add(new NVPair("f_1_0@0@root_3", "Die Insel des vorigen Tages"));
		list.add(new NVPair("f_1_1@0@root_1", "3-423-12445-5"));
		list.add(new NVPair("f_1_1@0@root_3", "Das Foucaltsche Pendel"));
		list.add(new NVPair("ac_copy_1", "Copy"));
		list.add(new NVPair("k_1_0@0@root", "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
		list.add(new NVPair("f_1_ins0@root_2", "1"));
		list.add(new NVPair("k_1_1@0@root", "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
		list.add(new NVPair("f_1_ins0@root_2", "1"));
		list.add(new NVPair("k_0_0@root", "0%3A1%3A1"));
		System.out.println(
			"Testing URL: "
				+ replaceURL("http://localhost/bookstore/servlet/control?invtable=0&invname_0=null&autoupdate_0=false&fu_0=/tests/testAuthorBooksSubForm.jsp&source=/bookstore/tests/testAuthorBooksSubForm.jsp&customEvent=&firstpos_0=0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11&lastpos_0=0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11&f_0_0@root_1=Eco, Umberto&f_0_0@root_2=organisation 11&invtable=1&invname_1=null&autoupdate_1=false&fu_1=/tests/testAuthorBooksSubForm.jsp&source=/bookstore/tests/testAuthorBooksSubForm.jsp&customEvent=&firstpos_1=0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages&lastpos_1=0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel&f_1_0@0@root_1=3-423-12445-4&f_1_0@0@root_3=Die Insel des vorigen Tages&f_1_1@0@root_1=3-423-12445-5&f_1_1@0@root_3=Das Foucaltsche Pendel&ac_copy_1=Copy&k_1_0@0@root=0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages&f_1_ins0@root_2=1&k_1_1@0@root=0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel&f_1_ins0@root_2=1&k_0_0@root=0%3A1%3A1"));
		post("http://localhost/bookstore/servlet/control", list);
		System.out.println("Response code: " + getResponse().getStatusCode());
		assertEquals("Assert number 11 failed", 200, getResponse().getStatusCode());
		printResponse();
		assertTrue(responseContains("<td>[No Data]&nbsp;</td>"));
		assertTrue(responseContains("<input type=\"text\" name=\"f_1_ins0@root_1\" value=\"3-423-12445-4\" />"));
		assertTrue(responseContains("<input type=\"text\" name=\"f_1_ins0@root_3\" value=\"Die Insel des vorigen Tages\" />"));
		assertTrue(responseContains("<input type=\"submit\"  value=\"New\"  style=\"width:100\" name=\"ac_new_1"));
		assertTrue(responseContains("<input type=\"submit\"  value=\"Copy\"  style=\"width:100\" name=\"ac_copy_1"));

	}

	public void testNavNewInSubForm() throws Exception
	{

		List list;

		System.out.println("Testing URL: " + replaceURL("http://localhost/bookstore/tests/testAuthorBooksSubForm.jsp"));
		get("http://localhost/bookstore/tests/testAuthorBooksSubForm.jsp");
		System.out.println("Response code: " + getResponse().getStatusCode());
		assertEquals("Assert number 3 failed", 200, getResponse().getStatusCode());
		assertTrue(responseContains("<input type=\"submit\"  value=\"New\"  style=\"width:100\" name=\"ac_new_1"));
		assertTrue(responseContains("<input type=\"submit\"  value=\"Copy\"  style=\"width:100\" name=\"ac_copy_1"));

		list = new ArrayList();
		list.add(new NVPair("invtable", "0"));
		list.add(new NVPair("invname_0", "null"));
		list.add(new NVPair("autoupdate_0", "false"));
		list.add(new NVPair("fu_0", "/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("source", "/bookstore/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("customEvent", ""));
		list.add(new NVPair("firstpos_0", "0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11"));
		list.add(new NVPair("lastpos_0", "0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11"));
		list.add(new NVPair("f_0_0@root_1", "Eco, Umberto"));
		list.add(new NVPair("f_0_0@root_2", "organisation 11"));
		list.add(new NVPair("invtable", "1"));
		list.add(new NVPair("invname_1", "null"));
		list.add(new NVPair("autoupdate_1", "false"));
		list.add(new NVPair("fu_1", "/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("source", "/bookstore/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("customEvent", ""));
		list.add(new NVPair("firstpos_1", "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
		list.add(new NVPair("lastpos_1", "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
		list.add(new NVPair("f_1_0@0@root_1", "3-423-12445-4"));
		list.add(new NVPair("f_1_0@0@root_3", "Die Insel des vorigen Tages"));
		list.add(new NVPair("f_1_1@0@root_1", "3-423-12445-5"));
		list.add(new NVPair("f_1_1@0@root_3", "Das Foucaltsche Pendel"));
		list.add(new NVPair("ac_new_1", "New"));
		list.add(new NVPair("k_1_0@0@root", "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
		list.add(new NVPair("f_1_ins0@root_2", "1"));
		list.add(new NVPair("k_1_1@0@root", "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
		list.add(new NVPair("f_1_ins0@root_2", "1"));
		list.add(new NVPair("k_0_0@root", "0%3A1%3A1"));
		System.out.println(
			"Testing URL: "
				+ replaceURL("http://localhost/bookstore/servlet/control?invtable=0&invname_0=null&autoupdate_0=false&fu_0=/tests/testAuthorBooksSubForm.jsp&source=/bookstore/tests/testAuthorBooksSubForm.jsp&customEvent=&firstpos_0=0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11&lastpos_0=0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11&f_0_0@root_1=Eco, Umberto&f_0_0@root_2=organisation 11&invtable=1&invname_1=null&autoupdate_1=false&fu_1=/tests/testAuthorBooksSubForm.jsp&source=/bookstore/tests/testAuthorBooksSubForm.jsp&customEvent=&firstpos_1=0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages&lastpos_1=0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel&f_1_0@0@root_1=3-423-12445-4&f_1_0@0@root_3=Die Insel des vorigen Tages&f_1_1@0@root_1=3-423-12445-5&f_1_1@0@root_3=Das Foucaltsche Pendel&ac_new_1=New&k_1_0@0@root=0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages&f_1_ins0@root_2=1&k_1_1@0@root=0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel&f_1_ins0@root_2=1&k_0_0@root=0%3A1%3A1"));
		post("http://localhost/bookstore/servlet/control", list);
		System.out.println("Response code: " + getResponse().getStatusCode());
		assertEquals("Assert number 4 failed", 200, getResponse().getStatusCode());
		printResponse();
		assertTrue(responseContains("<input type=\"submit\"  value=\"New\"  style=\"width:100\" name=\"ac_new_1"));
		assertTrue(responseContains("<input type=\"submit\"  value=\"Copy\"  style=\"width:100\" name=\"ac_copy_1"));

		list = new ArrayList();
		list.add(new NVPair("invtable", "0"));
		list.add(new NVPair("invname_0", "null"));
		list.add(new NVPair("autoupdate_0", "false"));
		list.add(new NVPair("fu_0", "/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("source", "/bookstore/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("customEvent", ""));
		list.add(new NVPair("firstpos_0", "0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11"));
		list.add(new NVPair("lastpos_0", "0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11"));
		list.add(new NVPair("f_0_0@root_1", "Eco, Umberto"));
		list.add(new NVPair("f_0_0@root_2", "organisation 11"));
		list.add(new NVPair("ac_next_0", ">  Next"));
		list.add(new NVPair("invtable", "1"));
		list.add(new NVPair("invname_1", "null"));
		list.add(new NVPair("autoupdate_1", "false"));
		list.add(new NVPair("fu_1", "/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("source", "/bookstore/tests/testAuthorBooksSubForm.jsp"));
		list.add(new NVPair("customEvent", ""));
		list.add(new NVPair("f_1_ins0@root_1", ""));
		list.add(new NVPair("f_1_ins0@root_3", ""));
		list.add(new NVPair("k_1_0@0@root", "null"));
		list.add(new NVPair("f_1_ins0@root_2", "1"));
		list.add(new NVPair("k_0_0@root", "0%3A1%3A1"));
		System.out.println(
			"Testing URL: "
				+ replaceURL("http://localhost/bookstore/servlet/control?invtable=0&invname_0=null&autoupdate_0=false&fu_0=/tests/testAuthorBooksSubForm.jsp&source=/bookstore/tests/testAuthorBooksSubForm.jsp&customEvent=&firstpos_0=0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11&lastpos_0=0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11&f_0_0@root_1=Eco, Umberto&f_0_0@root_2=organisation 11&ac_next_0=>  Next&invtable=1&invname_1=null&autoupdate_1=false&fu_1=/tests/testAuthorBooksSubForm.jsp&source=/bookstore/tests/testAuthorBooksSubForm.jsp&customEvent=&f_1_ins0@root_1=&f_1_ins0@root_3=&k_1_0@0@root=null&f_1_ins0@root_2=1&k_0_0@root=0%3A1%3A1"));
		post("http://localhost/bookstore/servlet/control", list);
		System.out.println("Response code: " + getResponse().getStatusCode());
		assertEquals("Assert number 5 failed", 200, getResponse().getStatusCode());
		printResponse();
		assertTrue(responseContains("<input type=\"text\" name=\"f_0_0@root_1\" value=\"Douglas, Adam\"  size=\"25\"/>"));
		assertTrue(responseContains("<input type=\"text\" name=\"f_1_0@0@root_1\" value=\"42-1\" />"));
	}

}
