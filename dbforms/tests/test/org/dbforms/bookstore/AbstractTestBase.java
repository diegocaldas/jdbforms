package org.dbforms.bookstore;

// imports
import com.bitmechanic.maxq.HttpTestCase;
import com.bitmechanic.maxq.EditorPane;

// definition of test class
public abstract class AbstractTestBase extends HttpTestCase
{
	public AbstractTestBase(String name)
	{
		super(name);
	}

	protected void saveHTML(String testName) throws Exception
	{
		String rsp = new String(getResponse().getData());
		String filename = this.getClass().getName() + "." + this.getName() + "." + testName + ".html";
		EditorPane.saveHTML(filename, rsp, "temp/");
	}



}
