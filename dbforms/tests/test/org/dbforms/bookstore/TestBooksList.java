// package
// This class was generated by MaxQ (maxq.tigris.org)
package org.dbforms.bookstore;

// imports
import junit.framework.Test;
import junit.framework.TestSuite;
import com.bitmechanic.maxq.HttpTestCase;
import com.bitmechanic.maxq.EditorPane;

// definition of test class
public class TestBooksList extends AbstractTestBase
{

	// Test method generated from the MaxQ Java generator
	public TestBooksList(String name)
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
		junit.textui.TestRunner.main(new String[] { TestBooksList.class.getName()});
	}

	public void testBooksList2() throws Exception
	{
		System.out.println("testBooksList");
		System.out.println("Testing URL: " + replaceURL("http://localhost/bookstore/tests/testBOOKSList.jsp"));
		get("http://localhost/bookstore/tests/testBOOKSList.jsp");
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
