// package
// This class was generated by MaxQ (maxq.tigris.org)
package org.dbforms.bookstore;

// imports

// definition of test class
public class TestBooksListXML extends AbstractTestBase
{

	// Test method generated from the MaxQ Java generator
	public TestBooksListXML(String name)
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
		junit.textui.TestRunner.main(new String[] { TestBooksListXML.class.getName()});
	}

	public void testBooksList() throws Exception
	{
		System.out.println("testBooksList");
		System.out.println("Testing URL: " + replaceURL("http://localhost/bookstore/tests/testBOOKSListXML.jsp"));
		get("http://localhost/bookstore/tests/testBOOKSListXML.jsp");
		System.out.println("Response code: " + getResponse().getStatusCode());
		assertEquals("Assert number 1 failed", 200, getResponse().getStatusCode());
		printResponse();
		assertTrue(responseContains("Die Insel des vorigen Tages"));
		assertTrue(responseContains("Das Foucaltsche Pendel"));
		assertTrue(responseContains("Hijacking through the Galaxy 1"));
		assertTrue(responseContains("Hijacking through the Galaxy 2"));
		assertTrue(responseContains("Hijacking through the Galaxy 3"));
		assertTrue(responseContains("Hijacking through the Galaxy 4"));
		assertTrue(responseContains("Test null value"));
		assertTrue(responseContains("Luca's favorite thing to eat is  \"delicious Italian pasta\""));
		assertTrue(responseContains("Hijacking through the Galaxy 6"));
	}

}