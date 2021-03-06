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

/*
 * $Header$
 * $Revision$
 * $Date$
 *
 */
package org.dbforms.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.RequestDispatcher;

/**
 * 
 * read only PageContext.
 * 
 * Used to generate a form via doStart internally to use the results.
 * 
 * See StartReportServlet.
 * 
 */
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

/**
 * DOCUMENT ME!
 * 
 * @author $author$
 * @version $Revision$
 */
public class PageContextBuffer extends PageContext {
	private Exception exception;

	/**
	 * Provides programmatic access to the ExpressionEvaluator. The JSP
	 * Container must return a valid instance of an ExpressionEvaluator that can
	 * parse EL expressions.
	 */

	// The expression evaluator, for evaluating EL expressions.
	private org.apache.commons.el.ExpressionEvaluatorImpl elExprEval = new org.apache.commons.el.ExpressionEvaluatorImpl();

	private Hashtable nametable;

	private HttpSession session;

	private JspWriterBuffer out;

	private Servlet servlet;

	private ServletConfig servletConfig;

	private ServletContext servletContext;

	private ServletRequest request;

	private ServletResponse response;

	private String errorPageURL;

	// The variable resolver, for evaluating EL expressions.
	private org.apache.commons.el.VariableResolverImpl variableResolver = new org.apache.commons.el.VariableResolverImpl(
			this);

	private boolean needsSession;

	/**
	 * Creates a new PageContextDummy object.
	 */
	public PageContextBuffer() {
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param name
	 *            DOCUMENT ME!
	 * @param attribute
	 *            DOCUMENT ME!
	 */
	public void setAttribute(String name, Object attribute) {
		if ((name == null) || (attribute == null)) {
			throw new NullPointerException();
		}

		nametable.put(name, attribute);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param name
	 *            DOCUMENT ME!
	 * @param attribute
	 *            DOCUMENT ME!
	 * @param scope
	 *            DOCUMENT ME!
	 */
	public void setAttribute(String name, Object attribute, int scope) {
		if ((name == null) || (attribute == null)) {
			throw new NullPointerException();
		}

		switch (scope) {
		case APPLICATION_SCOPE:
			break;

		case PAGE_SCOPE:
			setAttribute(name, attribute);

			break;

		case REQUEST_SCOPE:
			break;

		case SESSION_SCOPE:

			if (!needsSession) {
				throw new IllegalArgumentException(
						"Invalid scope - Page does not participate in a session");
			}

			break;

		default:
			throw new IllegalArgumentException("Invalid scope");
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param name
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Object getAttribute(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}

		return (nametable.get(name));
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param name
	 *            DOCUMENT ME!
	 * @param scope
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Object getAttribute(String name, int scope) {
		Object obj = null;

		if (name == null) {
			throw new NullPointerException();
		}

		switch (scope) {
		case PAGE_SCOPE:
			obj = getAttribute(name);

			break;

		case REQUEST_SCOPE:
			obj = request.getAttribute(name);

			break;

		case SESSION_SCOPE:

			if (needsSession) {
				/* Throws IllegalStateException */
				obj = session.getAttribute(name);
			} else {
				throw new IllegalArgumentException(
						"Invalid scope - Page does not participate in a session");
			}

			break;

		case APPLICATION_SCOPE:
			obj = servletContext.getAttribute(name);

			break;

		default:
			throw new IllegalArgumentException("Invalid scope");
		}

		return obj;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param scope
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Enumeration getAttributeNamesInScope(int scope) {
		switch (scope) {
		case PAGE_SCOPE:
			return nametable.keys();

		case REQUEST_SCOPE:
			return request.getAttributeNames();

		case SESSION_SCOPE:

			if (!needsSession) {
				throw new IllegalArgumentException(
						"Invalid scope - Page does not participate in a session");
			}
			/* HttpSession should have get/setAttributeNames too :) */
			return session.getAttributeNames();

		case APPLICATION_SCOPE:
			return servletContext.getAttributeNames();

		default:
			throw new IllegalArgumentException("Invalid scope");
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param name
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public int getAttributesScope(String name) {
		int scope = 0;

		/* Search order - page, request, session (if supported), appl */
		if (getAttribute(name, PAGE_SCOPE) != null) {
			scope = PAGE_SCOPE;
		}

		if (getAttribute(name, REQUEST_SCOPE) != null) {
			scope = REQUEST_SCOPE;
		}

		if (needsSession) {
			if (getAttribute(name, SESSION_SCOPE) != null) {
				scope = SESSION_SCOPE;
			}
		}

		if (getAttribute(name, APPLICATION_SCOPE) != null) {
			scope = APPLICATION_SCOPE;
		}

		return scope;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public StringBuffer getBuffer() {
		return out.getBuffer();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Exception getException() {
		return exception;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public javax.servlet.jsp.el.ExpressionEvaluator getExpressionEvaluator() {
		return elExprEval;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public JspWriter getOut() {
		return out;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Object getPage() {
		return servlet;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public ServletRequest getRequest() {
		return request;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public ServletResponse getResponse() {
		return response;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String getResult() {
		return out.getResult();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public ServletConfig getServletConfig() {
		return servletConfig;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public ServletContext getServletContext() {
		return servletContext;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public HttpSession getSession() {
		return session;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public javax.servlet.jsp.el.VariableResolver getVariableResolver() {
		return variableResolver;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param name
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Object findAttribute(String name) {
		Object obj = null;

		/* Search order - page, request, session (if supported), appl */
		if ((obj = getAttribute(name, PAGE_SCOPE)) != null) {
			return obj;
		}

		if ((obj = getAttribute(name, REQUEST_SCOPE)) != null) {
			return obj;
		}

		if (needsSession) {
			if ((obj = getAttribute(name, SESSION_SCOPE)) != null) {
				return obj;
			}
		}

		if ((obj = getAttribute(name, APPLICATION_SCOPE)) != null) {
			return obj;
		}

		return obj;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param relativeURLPath
	 *            DOCUMENT ME!
	 * 
	 * @throws ServletException
	 *             DOCUMENT ME!
	 * @throws IOException
	 *             DOCUMENT ME!
	 */
	public void forward(String relativeURLPath) throws ServletException,
			IOException {
		if (relativeURLPath == null) {
			throw new ServletException(
					"[PageContext.forward()] Got 'null' URL - Probably caused "
							+ "by a non-existent Request Time Attribute Value.");
		}

		String path = decodePath(relativeURLPath);

		RequestDispatcher requestDispatcher = servletContext
				.getRequestDispatcher(path);

		requestDispatcher.forward(request, response);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param t
	 *            DOCUMENT ME!
	 * 
	 * @throws IOException
	 *             DOCUMENT ME!
	 * @throws ServletException
	 *             DOCUMENT ME!
	 */
	public void handlePageException(Throwable t) throws IOException,
			ServletException {
		// set the request attribute with the Throwable.
		request.setAttribute("javax.servlet.jsp.jspException", t);

		if ((errorPageURL != null) && !errorPageURL.equals("")) {
			try {
				forward(errorPageURL);
			} catch (IllegalStateException ise) {
				include(errorPageURL);
			}
		} else {
			// Otherwise throw the exception wrapped inside a ServletException.
			// Set the exception as the root cause in the ServletException
			// to get a stack trace for the real problem
			if (t instanceof IOException) {
				throw (IOException) t;
			}

			if (t instanceof ServletException) {
				throw (ServletException) t;
			}

			if (t instanceof RuntimeException) {
				throw (RuntimeException) t;
			}

			if (t instanceof JspException) {
				Throwable rootCause = ((JspException) t).getRootCause();

				if (rootCause != null) {
					throw new ServletException(t.getMessage(), rootCause);
				}
				throw new ServletException(t);
			}

			throw new ServletException(t);
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param e
	 *            DOCUMENT ME!
	 * 
	 * @throws ServletException
	 *             DOCUMENT ME!
	 * @throws IOException
	 *             DOCUMENT ME!
	 */
	public void handlePageException(Exception e) throws ServletException,
			IOException {
		// System.out.println("[PageContext] handling page exception");
		// e.printStackTrace();
		if (!errorPageURL.equals("")) {
			setAttribute(EXCEPTION, e, REQUEST_SCOPE);
			forward(errorPageURL);
		} else {
			System.out.println("Unhandled Page Exception:");
			e.printStackTrace();
			throw new ServletException("An unhandled Page Exception occurred");
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param relativeURLPath
	 *            DOCUMENT ME!
	 * 
	 * @throws ServletException
	 *             DOCUMENT ME!
	 * @throws IOException
	 *             DOCUMENT ME!
	 */
	public void include(String relativeURLPath) throws ServletException,
			IOException {
		if (relativeURLPath == null) {
			throw new ServletException(
					"[PageContext.include()] Got 'null' URL. Probably caused "
							+ " by a non-existent Request Time Attribute Value.");
		}

		out.flush();

		String path = decodePath(relativeURLPath);
		RequestDispatcher requestDispatcher = servletContext
				.getRequestDispatcher(path);

		if (requestDispatcher == null) {
			InputStream is = servletContext
					.getResourceAsStream(relativeURLPath);

			if (is == null) {
				throw new ServletException(
						"[PageContext.include()] Unable to obtain include resource "
								+ relativeURLPath);
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(is));

			try {
				int c;

				while ((c = in.read()) > 0) {
					out.write(c);
				}
			} finally {
				in.close();
			}
		} else {
			HttpServletRequest httpReq = (HttpServletRequest) request;

			/* Set additional attributes for include */
			String attrib;

			if ((attrib = httpReq.getRequestURI()) != null) {
				request.setAttribute("javax.servlet.include.request_uri",
						attrib);
			}

			if ((attrib = httpReq.getServletPath()) != null) {
				request.setAttribute("javax.servlet.include.servlet_path",
						attrib);
			}

			if ((attrib = httpReq.getPathInfo()) != null) {
				request.setAttribute("javax.servlet.include.path_info", attrib);
			}

			if ((attrib = httpReq.getQueryString()) != null) {
				request.setAttribute("javax.servlet.include.query_string",
						attrib);
			}

			requestDispatcher.include(request, response);
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param relativeURLPath
	 *            DOCUMENT ME!
	 * @param flush
	 *            DOCUMENT ME!
	 * 
	 * @throws ServletException
	 *             DOCUMENT ME!
	 * @throws IOException
	 *             DOCUMENT ME!
	 */
	public void include(String relativeURLPath, boolean flush)
			throws ServletException, IOException {
		include(relativeURLPath);
		out.flush();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param servlet
	 *            DOCUMENT ME!
	 * @param request
	 *            DOCUMENT ME!
	 * @param response
	 *            DOCUMENT ME!
	 * @param errorPageURL
	 *            DOCUMENT ME!
	 * @param needsSession
	 *            DOCUMENT ME!
	 * @param bufferSize
	 *            DOCUMENT ME!
	 * @param autoFlush
	 *            DOCUMENT ME!
	 */
	public void initialize(Servlet aservlet, ServletRequest arequest,
			ServletResponse aresponse, String aerrorPageURL,
			boolean aneedsSession, int bufferSize, boolean autoFlush) {
		this.servlet = aservlet;
		this.request = arequest;
		this.response = aresponse;
		this.errorPageURL = aerrorPageURL;
		this.needsSession = aneedsSession;

		if (needsSession) {
			session = ((HttpServletRequest) request).getSession();
		}

		nametable = new Hashtable();
		out = new JspWriterBuffer(bufferSize, autoFlush, response);
		exception = null;

		servletConfig = servlet.getServletConfig();
		servletContext = servletConfig.getServletContext();
	}

	/**
	 * DOCUMENT ME!
	 */
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

	/**
	 * DOCUMENT ME!
	 * 
	 * @param name
	 *            DOCUMENT ME!
	 */
	public void removeAttribute(String name) {
		nametable.remove(name);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param name
	 *            DOCUMENT ME!
	 * @param scope
	 *            DOCUMENT ME!
	 */
	public void removeAttribute(String name, int scope) {
		switch (scope) {
		case PAGE_SCOPE:
			break;

		case REQUEST_SCOPE:
			throw new IllegalArgumentException(
					"Invalid scope - Can't remove attribute from REQUEST_SCOPE");

		case SESSION_SCOPE:

			if (!needsSession) {
				throw new IllegalArgumentException(
						"Invalid Scope - Page does not participate in an HTTP session");
			}

			break;

		case APPLICATION_SCOPE:
			break;

		default:
			throw new IllegalArgumentException("Invalid Scope");
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

		// System.out.println("[PageContext] Decoded path : " + path + "");
		return path;
	}
}
