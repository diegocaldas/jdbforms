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


	public void testApplicationResources() throws Exception
	{
		println("testApplicationResources");
		get("http://localhost/bookstore/tests/testApplicationResources.jsp");
      printResponse();
      assertTrue("test.books.struts", responseContains("test.books.struts"));
      assertTrue("test.books.dbforms", responseContains("test.books.dbforms"));
      assertTrue("test.author.hk", responseContains("test.author.hk"));
      assertTrue("test.author.jm", responseContains("test.author.jm"));
	}
}
