// package
// This class was generated by MaxQ (maxq.tigris.org)
package org.dbforms.bookstore;

import org.dbforms.util.HttpTestCase;

// imports

// definition of test class
public class TestBooksListXML extends HttpTestCase {

	// Test method generated from the MaxQ Java generator
	public TestBooksListXML(String name) {
		super(name);
	}

	/**
	 * Start the tests.
	 * 
	 * @param theArgs
	 *            the arguments. Not used
	 */
	public static void main(String[] theArgs) {
		junit.textui.TestRunner.main(new String[] { TestBooksListXML.class
				.getName() });
	}

	public void testBooksList() throws Exception {
		println("testBooksList");
		get("http://localhost/bookstore/tests/testBOOKSListXML.jsp");
		printResponse();
		assertTrue(responseContains("Die Insel des vorigen Tages"));
		assertTrue(responseContains("Das Foucaltsche Pendel"));
		assertTrue(responseContains("Hijacking through the Galaxy 1"));
		assertTrue(responseContains("Hijacking through the Galaxy 2"));
		assertTrue(responseContains("Hijacking through the Galaxy 3"));
		assertTrue(responseContains("Hijacking through the Galaxy 4"));
		assertTrue(responseContains("Test null value"));
		assertTrue(responseContains("Luca's favorite thing to eat is  &quot;delicious Italian pasta&quot;"));
		assertTrue(responseContains("Hijacking through the Galaxy 6"));
	}

}
