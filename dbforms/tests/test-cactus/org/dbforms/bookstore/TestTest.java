package org.dbforms.bookstore;

import org.dbforms.util.HttpTestCase;

// imports


// definition of test class
public class TestTest extends HttpTestCase
{

	// Test method generated from the MaxQ Java generator
	public TestTest(String name)
	{
		super(name);
	}


	public void testSimpleJSPPage() throws Exception
	{
		println("testTest");
		get("http://localhost/bookstore/tests/test.jsp");
	}
}
