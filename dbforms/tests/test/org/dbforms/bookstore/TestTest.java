package org.dbforms.bookstore;

// imports


// definition of test class
public class TestTest extends AbstractTestBase
{

	// Test method generated from the MaxQ Java generator
	public TestTest(String name)
	{
		super(name);
	}


	public void testSimpleJSPPage() throws Exception
	{
		System.out.println("testTest");
		System.out.println("Testing URL: " + replaceURL("http://bookstore/dbforms/tests/test.jsp"));
		get("http://localhost/bookstore/tests/test.jsp");
		System.out.println("Response code: " + getResponse().getStatusCode());
		assertEquals("Assert number 1 failed", 200, getResponse().getStatusCode());
	}
}
