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

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.io.IOException;
import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import javax.servlet.http.Cookie;
import javax.servlet.ServletInputStream;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

import com.meterware.httpunit.WebForm;
/**
 * 
 * Wraps a com.meterware.httpunit.WebForm object into a HttpServletRequset.
 * Only fields of the form are wrapped into parameters,
 * 
 * Needed for test of server side responses
 * 
 * @author hkk
 *
 */


public class WebFormWrapper implements HttpServletRequest {


   private Locale locale;
   private WebForm form;
   private Hashtable parameterNames;
   private SessionWrapper session = new SessionWrapper();
   
   private class SessionWrapper implements HttpSession {
      private Hashtable attributes;
      private SessionWrapper() {
         attributes = new Hashtable();
      }
      
      public long getCreationTime() {
          return 0;
      }


      public String getId() {
          return this.getId();
      }


      public long getLastAccessedTime() {
          return 0;
      }


      public ServletContext getServletContext() {
          return null;
      }


      public void setMaxInactiveInterval(int interval) {
      }


      public int getMaxInactiveInterval() {
          return 0;
      }


      public HttpSessionContext getSessionContext() {
          return null;
      }


      public Object getAttribute(String name) {
          return attributes.get(name);
      }


      public Object getValue(String name) {
          return attributes.get(name);
      }


      public Enumeration getAttributeNames() {
         return attributes.keys();
      }


      public String[] getValueNames() {
          return null;
      }


      public void setAttribute(String name, Object value) {
         attributes.put(name, value);
      }


      public void putValue(String name, Object value) {
         attributes.put(name, value);
      }


      public void removeAttribute(String name) {
          attributes.remove(name);
      }


      public void removeValue(String name) {
         attributes.remove(name);
      }


      public void invalidate() {
      }


      public boolean isNew() {
          return false;
      }


      
   }
   
   public WebFormWrapper(WebForm form, Locale locale) {
      this.locale = locale;
      this.form = form;
   }
   
   public String getAuthType() {
      return null;
   }

   public Cookie[] getCookies() {
      return null;
   }

   public long getDateHeader(String name) {
      return 0;
   }

   public String getHeader(String name) {
      return null;
   }

   public Enumeration getHeaders(String name) {
      return null;
   }

   public Enumeration getHeaderNames() {
      return null;
   }

   public int getIntHeader(String name) {
      return 0;
   }

   public String getMethod() {
      return null;
   }

   public String getPathInfo() {
      return null;
   }

   public String getPathTranslated() {
      return null;
   }

   public String getContextPath() {
      return null;
   }

   public String getQueryString() {
      return null;
   }

   public String getRemoteUser() {
      return null;
   }

   public boolean isUserInRole(String role) {
      return false;
   }

   public java.security.Principal getUserPrincipal() {
      return null;
   }

   public String getRequestedSessionId() {
      return null;
   }

   public String getRequestURI() {
      return null;
   }
   public StringBuffer getRequestURL() {
      return null;
   }

   public String getServletPath() {
      return null;
   }

   public HttpSession getSession(boolean create) {
      if (create) 
         session = new SessionWrapper();
      return session;
   }

   public HttpSession getSession() {
      return session;
   }

   public boolean isRequestedSessionIdValid() {
      return false;
   }

   public boolean isRequestedSessionIdFromCookie() {
      return false;
   }

   public boolean isRequestedSessionIdFromURL() {
      return false;
   }


   public Object getAttribute(String name) {
      return null;
   }

   public Enumeration getAttributeNames() {
      return null;
   }

   public String getCharacterEncoding() {
      return null;
   }

   public void setCharacterEncoding(String enc) throws java.io.UnsupportedEncodingException {}

   public int getContentLength() {
      return 0;
   }

   public String getContentType() {
      return null;
   }

   public ServletInputStream getInputStream() throws IOException {
      return null;
   }

   public String getParameter(String name) {
      return form.getParameterValue(name);
   }

   public Map getParameterMap() {
      return null;
   }

   public Enumeration getParameterNames() {
      if (parameterNames == null) {
         parameterNames = new Hashtable();
         String [] params = form.getParameterNames();
         for (int i = 0; i < params.length; i++) {
            parameterNames.put(params[i], getParameter(params[i]));
         }
      }
      return parameterNames.keys();
   }

   public String[] getParameterValues(String name) {
      return null;
   }

   public String getProtocol() {
      return null;
   }

   public String getScheme() {
      return null;
   }

   public String getServerName() {
      return null;
   }

   public int getServerPort() {
      return 0;
   }

   public BufferedReader getReader() throws IOException {
      return null;
   }

   public String getRemoteAddr() {
      return null;
   }

   public String getRemoteHost() {
      return null;
   }

   public void setAttribute(String name, Object o) {}

   public void removeAttribute(String name) {}

   public Locale getLocale() {
      return locale;
   }

   public Enumeration getLocales() {
      return null;
   }

   public boolean isSecure() {
      return false;
   }

   public RequestDispatcher getRequestDispatcher(String path) {
      return null;
   }

   public String getRealPath(String path) {
      return null;
   }

   public boolean isRequestedSessionIdFromUrl() {
      return false;
   }

}
