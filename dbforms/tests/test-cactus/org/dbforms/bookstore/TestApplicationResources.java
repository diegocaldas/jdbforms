package org.dbforms.bookstore;

import org.dbforms.util.HttpTestCase;

// imports


// definition of test class
public class TestApplicationResources extends HttpTestCase
{

	// Test method generated from the MaxQ Java generator
	public TestApplicationResources(String name)
	{
		super(name);
	}


	public void testSimpleJSPPage() throws Exception
	{
		println("testTest");
		get("http://localhost/bookstore/tests/testApplicationResources.jsp");
      printResponse();
      assertTrue(responseContains("test.books.struts"));
      assertTrue(responseContains("test.books.dbforms"));
      assertTrue(responseContains("test.author.hk"));
      assertTrue(responseContains("test.author.jm"));
	}
}
