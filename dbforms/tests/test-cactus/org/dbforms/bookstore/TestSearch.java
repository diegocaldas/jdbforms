// package
// This class was generated by MaxQ (maxq.tigris.org)
package org.dbforms.bookstore;

// imports
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

import org.dbforms.util.KeyValuePair;

import org.dbforms.util.HttpTestCase;
import org.dbforms.util.MessageResourcesInternal;
import org.dbforms.util.MessageResources;

// definition of test class
public class TestSearch extends HttpTestCase {
	private static String nodata = null;

	// Test method generated from the MaxQ Java generator
	public TestSearch(String name) {
		super(name);
		if (nodata == null) {
			MessageResources.setSubClass("resources");
			nodata = MessageResourcesInternal.getMessage("dbforms.nodata",
					Locale.getDefault());
		}
	}

	public void testSearching() throws Exception {
		List list;

		get("http://localhost/bookstore/tests/testSEARCH.jsp");
		printResponse();
		assertTrue(responseContains("<h1>Search Page</h1>"));

		list = new ArrayList();
		list.add(new KeyValuePair("invtable", "1"));
		list.add(new KeyValuePair("invname_1", ""));
		list.add(new KeyValuePair("autoupdate_1", "false"));
		list.add(new KeyValuePair("fu_1", "/tests/testSEARCHRESULTS.jsp"));
		list.add(new KeyValuePair("source", "/bookstore/tests/testSEARCH.jsp"));
		list.add(new KeyValuePair("customEvent", ""));
		list.add(new KeyValuePair("firstpos_1", "0%3A1%3A1-2%3A1%3A1"));
		list.add(new KeyValuePair("lastpos_1", "0%3A1%3A9-2%3A1%3A2"));
		list.add(new KeyValuePair("dataac_goto_1_8_fu",
				"/tests/testSEARCHRESULTS.jsp"));
		list.add(new KeyValuePair("dataac_goto_1_8_singleRow", "false"));
		list.add(new KeyValuePair("search_1_3", "Hij%"));
		list.add(new KeyValuePair("searchalgo_1_3", "weak"));
		list.add(new KeyValuePair("k_1_0@root", "0%3A1%3A1-2%3A1%3A1"));
		list.add(new KeyValuePair("k_1_1@root", "0%3A1%3A2-2%3A1%3A1"));
		list.add(new KeyValuePair("k_1_2@root", "0%3A1%3A3-2%3A1%3A2"));
		list.add(new KeyValuePair("k_1_3@root", "0%3A1%3A4-2%3A1%3A2"));
		list.add(new KeyValuePair("k_1_4@root", "0%3A1%3A5-2%3A1%3A2"));
		list.add(new KeyValuePair("k_1_5@root", "0%3A1%3A6-2%3A1%3A2"));
		list.add(new KeyValuePair("k_1_6@root", "0%3A1%3A7-2%3A0%3A"));
		list.add(new KeyValuePair("k_1_7@root", "0%3A1%3A8-2%3A1%3A2"));
		list.add(new KeyValuePair("k_1_8@root", "0%3A1%3A9-2%3A1%3A2"));
		post("http://localhost/bookstore/servlet/control", list);
		printResponse();
		assertTrue(responseContains("<h1>Search Page</h1>"));

		list = new ArrayList();
		list.add(new KeyValuePair("invtable", "1"));
		list.add(new KeyValuePair("invname_1", ""));
		list.add(new KeyValuePair("autoupdate_1", "false"));
		list.add(new KeyValuePair("fu_1", "/tests/testSEARCHRESULTS.jsp"));
		list.add(new KeyValuePair("source", "/bookstore/tests/testSEARCH.jsp"));
		list.add(new KeyValuePair("customEvent", ""));
		list.add(new KeyValuePair("firstpos_1", "0%3A1%3A3-2%3A1%3A2"));
		list.add(new KeyValuePair("lastpos_1", "0%3A1%3A9-2%3A1%3A2"));
		list.add(new KeyValuePair("dataac_goto_1_9_fu",
				"/tests/testSEARCHRESULTS.jsp"));
		list.add(new KeyValuePair("dataac_goto_1_9_singleRow", "false"));
		list.add(new KeyValuePair("ac_goto_1_9", "Search!"));
		list.add(new KeyValuePair("search_1_3", "Hij%"));
		list.add(new KeyValuePair("k_1_0@root", "0%3A1%3A3-2%3A1%3A2"));
		list.add(new KeyValuePair("k_1_1@root", "0%3A1%3A4-2%3A1%3A2"));
		list.add(new KeyValuePair("k_1_2@root", "0%3A1%3A5-2%3A1%3A2"));
		list.add(new KeyValuePair("k_1_3@root", "0%3A1%3A6-2%3A1%3A2"));
		list.add(new KeyValuePair("k_1_4@root", "0%3A1%3A9-2%3A1%3A2"));
		post("http://localhost/bookstore/servlet/control", list);
		printResponse();
		assertTrue(responseContains(nodata));

		list = new ArrayList();
		list.add(new KeyValuePair("invtable", "1"));
		list.add(new KeyValuePair("invname_1", ""));
		list.add(new KeyValuePair("autoupdate_1", "false"));
		list.add(new KeyValuePair("fu_1", "/tests/testSEARCHRESULTS.jsp"));
		list.add(new KeyValuePair("source", "/bookstore/tests/testSEARCH.jsp"));
		list.add(new KeyValuePair("customEvent", ""));
		list.add(new KeyValuePair("firstpos_1", "0%3A1%3A3-2%3A1%3A2"));
		list.add(new KeyValuePair("lastpos_1", "0%3A1%3A9-2%3A1%3A2"));
		list.add(new KeyValuePair("dataac_goto_1_9_fu",
				"/tests/testSEARCHRESULTS.jsp"));
		list.add(new KeyValuePair("dataac_goto_1_9_singleRow", "false"));
		list.add(new KeyValuePair("ac_goto_1_9", "Search!"));
		list.add(new KeyValuePair("search_1_3", "Hij%"));
		list.add(new KeyValuePair("searchalgo_1_3", "weak"));
		list.add(new KeyValuePair("k_1_0@root", "0%3A1%3A3-2%3A1%3A2"));
		list.add(new KeyValuePair("k_1_1@root", "0%3A1%3A4-2%3A1%3A2"));
		list.add(new KeyValuePair("k_1_2@root", "0%3A1%3A5-2%3A1%3A2"));
		list.add(new KeyValuePair("k_1_3@root", "0%3A1%3A6-2%3A1%3A2"));
		list.add(new KeyValuePair("k_1_4@root", "0%3A1%3A9-2%3A1%3A2"));
		post("http://localhost/bookstore/servlet/control", list);
		printResponse();
		assertTrue(responseContains("Hijacking through the Galaxy 1"));
		assertTrue(responseContains("Hijacking through the Galaxy 2"));
		assertTrue(responseContains("Hijacking through the Galaxy 3"));
		assertTrue(responseContains("Hijacking through the Galaxy 4"));
		assertTrue(responseContains("Hijacking through the Galaxy 6"));

	}

}
