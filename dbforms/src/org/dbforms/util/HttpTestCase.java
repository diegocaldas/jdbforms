/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * DbForms - a Rapid Application Development Framework
 * Copyright (C) 2001 Joachim Peer <joepeer@excite.com>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 */
package org.dbforms.util;

// imports
import junit.framework.TestCase;

import java.util.List;
import java.util.Iterator;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PostMethodWebRequest;

// definition of test class
public abstract class HttpTestCase extends TestCase {
	private String urlSearch;
	private String urlReplace;
	private String paramSearch;
	private String paramReplace;
	private WebConversation wc = new WebConversation();
	private WebResponse resp = null;

	public HttpTestCase(String name) {
		super(name);
		String context = System.getProperty("cactus.contextURL");
		if (!Util.isNull(context)) {
			println("change context to: " + context);
			urlSearch    = "http://localhost/bookstore";
			urlReplace   = context;
			paramSearch  = "/bookstore/";
			paramReplace = "/dbforms-cactus/";
		}
	}



	private String replaceURL(String url) {
		return replace(url, urlSearch, urlReplace);
	}

 
    private String replaceParam(String param) {
    	return replace(param, paramSearch, paramReplace); 
    }	
    
    private String replace(String from, String search, String replace) {
		if (search == null || replace == null)
			return from;
		else {
			int pos = from.indexOf(search);
			if (pos == -1)
				return from;
			else {
				String str =
				from.substring(0, pos)
						+ replace
						+ from.substring(pos + search.length());
				return str;
			}
		}
    }	
    	
	protected void println(String s) {
		System.out.println(s);
	}

	protected boolean responseContains(String text) throws Exception {
		if (resp == null || resp.getText() == null)
			return false;
		return resp.getText().indexOf(text) != -1;
	}

	protected void printResponse() throws Exception {
		println(resp.getText());
	}

	private int getStatusCode() {
		return resp.getResponseCode();
	}

	public void get(String url) throws Exception {
		get(url, null);
	}

	public void get(String url, List args) throws Exception {
		url = replaceURL(url);
		println("=========================");
		println("url" + " = " + url);
		println("=========================");
		WebRequest request = new GetMethodWebRequest(url);
		doIt(request, args);
	}

	public void post(String url) throws Exception {
		post(url, null);
	}

	public void post(String url, List args) throws Exception {
		url = replaceURL(url);
		println("=========================");
		println("url" + " = " + url);
		println("=========================");
		WebRequest request = new PostMethodWebRequest(url);
		doIt(request, args);
	}

	private void doIt(WebRequest request, List args) throws Exception {
		resp = null;
		if (args != null) {
			println("parameters");
			println("=========================");
			Iterator iter = args.iterator();
			while (iter.hasNext()) {
				KeyValuePair pair = (KeyValuePair) iter.next();
				println(pair.getKey() + " = " + pair.getValue());
				request.setParameter(pair.getKey(), replaceParam(pair.getValue()));
			}
			println("=========================");
		}
		wc.setExceptionsThrownOnErrorStatus(false);
		resp = wc.getResponse(request);
		println("Response code: " + getStatusCode());
		println("=========================");
		assertEquals("Page not found: ", 200, getStatusCode());
	}

	protected WebResponse getResponse() {
		return resp;
	}
}
