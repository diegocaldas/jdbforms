/*
 * $Header$
 * $Revision$
 * $Date$
 *
 */

package org.dbforms.util;
/**
 * 
 * read only PageContext
 * 
 */

import javax.servlet.*;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import java.util.Hashtable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.Enumeration;

public class PageContextDummy extends PageContext {

	private Servlet servlet;
	private ServletRequest request;
	private ServletResponse response;
	private HttpSession session;
	private String errorPageURL;
	private boolean needsSession;
	private int bufferSize;
	private boolean autoFlush;
	
	private ServletConfig servletConfig;
	private ServletContext servletContext;

	private Hashtable nametable;
	private JspWriter out;
	private Exception exception;
	
	public PageContextDummy() {}
	
	public void initialize(Servlet servlet, 
			ServletRequest request, 
			ServletResponse response, 
			String errorPageURL, 
			boolean needsSession, 
			int bufferSize, 
			boolean autoFlush) {

		this.servlet = servlet;
		this.request = request;
		this.response = response;
		this.errorPageURL = errorPageURL;
		this.needsSession = needsSession;
		this.bufferSize = bufferSize;
		this.autoFlush = autoFlush;
		
		if (needsSession) {
			session = ( (HttpServletRequest) request).getSession();
		}

		nametable = new Hashtable();
		out = new JspWriterDummy(bufferSize, autoFlush, response);
		exception = null;
		
		servletConfig = servlet.getServletConfig();
		servletContext = servletConfig.getServletContext();
	}

	public void release() {
		this.servlet = null;
		this.request = null;
		this.response = null;
		this.errorPageURL = null;
		
		nametable = null;
		out = null;
		exception = null;
		
		servletConfig = null;
		servletContext = null;
	}

	public void setAttribute(String name, Object attribute) {
	
		if (name == null || attribute == null)
			throw new NullPointerException();

		nametable.put(name, attribute);
	}

	public void setAttribute(String name, Object attribute, int scope) {
	
		if (name == null || attribute == null)
			throw new NullPointerException();
			
		switch (scope) {
			case APPLICATION_SCOPE:
				break;
			case PAGE_SCOPE:
				setAttribute(name, attribute);
				break;
			case REQUEST_SCOPE:
				break;
			case SESSION_SCOPE:
				if (!needsSession)
					throw new IllegalArgumentException(
					"Invalid scope - Page does not participate in a session");
				break;
			default:
				throw new IllegalArgumentException("Invalid scope");
		}
	}

	public Object getAttribute(String name) {
		
		if (name == null)
			throw new IllegalArgumentException();

		return (nametable.get(name));
	}
	
	public Object getAttribute(String name, int scope) {
	
		Object obj = null;
		
		if (name == null)
			throw new NullPointerException();
			
		switch (scope) {
			case PAGE_SCOPE:
				obj = getAttribute(name);
				break;
			case REQUEST_SCOPE:
				obj = request.getAttribute(name);
				break;
			case SESSION_SCOPE:
				if (needsSession)
					/* Throws IllegalStateException */
					obj = session.getAttribute(name);
				else
					throw new IllegalArgumentException(
					"Invalid scope - Page does not participate in a session");
				break;
			case APPLICATION_SCOPE:
				obj = servletContext.getAttribute(name);
				break;
			default:
				throw new IllegalArgumentException("Invalid scope");
		}

		return obj;
	}

	public Object findAttribute(String name) {

		Object obj = null;
		
		/* Search order - page, request, session (if supported), appl */
		if ( (obj = getAttribute(name, PAGE_SCOPE)) != null )
			return obj;
			
		if ( (obj = getAttribute(name, REQUEST_SCOPE)) != null )
			return obj;
			
		if (needsSession) {
			if ( (obj = getAttribute(name, SESSION_SCOPE)) != null )
				return obj;
		}
		
		if ( (obj = getAttribute(name, APPLICATION_SCOPE)) != null )
			return obj;
		
		return obj;
	}

	public void removeAttribute(String name) {
		nametable.remove(name);
	}

	public void removeAttribute(String name, int scope) {
		
		switch (scope) {
			case PAGE_SCOPE:
				break;
			case REQUEST_SCOPE:
				throw new IllegalArgumentException(
				"Invalid scope - Can't remove attribute from REQUEST_SCOPE");
			case SESSION_SCOPE:
				if (!needsSession) 
					throw new IllegalArgumentException(
					"Invalid Scope - Page does not participate in an HTTP session");
				break;
			case APPLICATION_SCOPE:
				break;
			default:
				throw new IllegalArgumentException("Invalid Scope");
		}
	}

	public int getAttributesScope(String name) {
		
		int scope = 0;
		
		/* Search order - page, request, session (if supported), appl */
		if (getAttribute(name, PAGE_SCOPE) != null )
			scope = PAGE_SCOPE;
			
		if (getAttribute(name, REQUEST_SCOPE) != null )
			scope =  REQUEST_SCOPE;
			
		if (needsSession) {
			if (getAttribute(name, SESSION_SCOPE) != null )
				scope = SESSION_SCOPE;
		}
		
		if (getAttribute(name, APPLICATION_SCOPE) != null )
			scope = APPLICATION_SCOPE;

		return scope;
	
	}

	public Enumeration getAttributeNamesInScope(int scope) {

		int i;
		
		switch (scope) {
			case PAGE_SCOPE:
				return nametable.keys();
			case REQUEST_SCOPE:
				return request.getAttributeNames();
			case SESSION_SCOPE:
				if (!needsSession)
					throw new IllegalArgumentException(
					"Invalid scope - Page does not participate in a session");
				else {
					/* HttpSession should have get/setAttributeNames too :) */
					return session.getAttributeNames();
				}
			case APPLICATION_SCOPE:
				return servletContext.getAttributeNames();
			default:
				throw new IllegalArgumentException("Invalid scope");
		}
	}

	public JspWriter getOut() {
		return (JspWriter) out;
	}

	public HttpSession getSession() {
		return session;
	}

	public Object getPage() {
		return servlet;
	}

	public ServletRequest getRequest() {
		return request;
	}

	public ServletResponse getResponse() {
		return response;
	}

	public Exception getException() {
		return exception;
	}

	public ServletConfig getServletConfig() {
		return servletConfig;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void forward(String relativeURLPath)
			throws ServletException, IOException {
			
		if (relativeURLPath == null) {
			throw new ServletException(
				"[PageContext.forward()] Got 'null' URL - Probably caused "
				+ "by a non-existent Request Time Attribute Value.");
		}
		
		String path = decodePath(relativeURLPath);
		
		RequestDispatcher requestDispatcher = 
			servletContext.getRequestDispatcher(path);
		
		requestDispatcher.forward(request, response);
	}

	public void include(String relativeURLPath)
			throws ServletException, IOException {
	
		if (relativeURLPath == null) {
			throw new ServletException(
				"[PageContext.include()] Got 'null' URL. Probably caused "
				+ " by a non-existent Request Time Attribute Value.");
		}

		out.flush();

		String path = decodePath(relativeURLPath);
	
		RequestDispatcher requestDispatcher =
			servletContext.getRequestDispatcher(path);
		
		if (requestDispatcher == null) {

			InputStream is = 
				servletContext.getResourceAsStream(relativeURLPath);
			
			if (is == null) {
				throw new ServletException(
				"[PageContext.include()] Unable to obtain include resource " 
				+ relativeURLPath);
			}
			
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			
			int c;
			while ( (c = in.read()) > 0 ) {
				out.write(c);
			}
		} else {
		
			HttpServletRequest httpReq = (HttpServletRequest) request;
		
			/* Set additional attributes for include */
			String attrib;

			if ( (attrib = httpReq.getRequestURI()) != null ) 
				request.setAttribute(
					"javax.servlet.include.request_uri", attrib);
		
			if ( (attrib = httpReq.getServletPath()) != null )
				request.setAttribute(
					"javax.servlet.include.servlet_path", attrib);
		
			if ( (attrib = httpReq.getPathInfo()) != null )
				request.setAttribute(
					"javax.servlet.include.path_info", attrib);
		
			if ( (attrib = httpReq.getQueryString()) != null )
				request.setAttribute(
					"javax.servlet.include.query_string", attrib);

			requestDispatcher.include(request, response);
		}
	}

	private String decodePath(String relativeURLPath) {
		
		String path;
		
		if (relativeURLPath.startsWith("/")) {
			path = relativeURLPath;
		} else {
			path = ((HttpServletRequest) request).getServletPath();
			path = path.substring(0, path.lastIndexOf("/"));
			path = path + "/" + relativeURLPath;
		}

//		System.out.println("[PageContext] Decoded path : " + path + "");

		return path;
	}
	
	public void handlePageException(Throwable t)
		throws IOException, ServletException
	{
		// set the request attribute with the Throwable.
	request.setAttribute("javax.servlet.jsp.jspException", t);

	if (errorPageURL != null && !errorPageURL.equals("")) {
			try {
				forward(errorPageURL);
			} catch (IllegalStateException ise) {
				include(errorPageURL);
			}
	} else {
			// Otherwise throw the exception wrapped inside a ServletException.
		// Set the exception as the root cause in the ServletException
		// to get a stack trace for the real problem
		if (t instanceof IOException) throw (IOException)t;
		if (t instanceof ServletException) throw (ServletException)t;
			if (t instanceof RuntimeException) throw (RuntimeException)t;
			if (t instanceof JspException) {
				Throwable rootCause = ((JspException)t).getRootCause();
				if (rootCause != null) {
					throw new ServletException(t.getMessage(), rootCause);
				} else {
					throw new ServletException(t);
				}
			}
		throw new ServletException(t);
	}
	}


	public void handlePageException(Exception e)
			throws ServletException, IOException {
	
//		System.out.println("[PageContext] handling page exception");
//		e.printStackTrace();
		if (!errorPageURL.equals("")) {
			setAttribute(EXCEPTION, e, REQUEST_SCOPE);
			forward(errorPageURL);
		} else {
			System.out.println("Unhandled Page Exception:");
			e.printStackTrace();
			throw new ServletException("An unhandled Page Exception occurred");
		}
		
	}

}
