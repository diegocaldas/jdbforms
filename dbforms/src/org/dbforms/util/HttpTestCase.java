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

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

// imports
import junit.framework.TestCase;

import java.util.Iterator;
import java.util.List;



// definition of test class
public abstract class HttpTestCase extends TestCase {
   private String          paramReplace;
   private String          paramSearch;
   private String          urlReplace;
   private String          urlSearch;
   private WebConversation wc   = new WebConversation();
   private WebResponse     resp = null;

   /**
    * Creates a new HttpTestCase object.
    *
    * @param name DOCUMENT ME!
    */
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

   /**
    * DOCUMENT ME!
    *
    * @param url DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void get(String url) throws Exception {
      get(url, null);
   }


   /**
    * DOCUMENT ME!
    *
    * @param url DOCUMENT ME!
    * @param args DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void get(String url,
                   List   args) throws Exception {
      url = replaceURL(url);
      println("=========================");
      println("url" + " = " + url);
      println("=========================");

      WebRequest request = new GetMethodWebRequest(url);
      doIt(request, args);
   }


   /**
    * DOCUMENT ME!
    *
    * @param url DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void post(String url) throws Exception {
      post(url, null);
   }


   /**
    * DOCUMENT ME!
    *
    * @param url DOCUMENT ME!
    * @param args DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void post(String url,
                    List   args) throws Exception {
      url = replaceURL(url);
      println("=========================");
      println("url" + " = " + url);
      println("=========================");

      WebRequest request = new PostMethodWebRequest(url);
      doIt(request, args);
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   protected WebResponse getResponse() {
      return resp;
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   protected void printResponse() throws Exception {
      println(resp.getText());
   }


   /**
    * DOCUMENT ME!
    *
    * @param s DOCUMENT ME!
    */
   protected void println(String s) {
      System.out.println(s);
   }


   /**
    * DOCUMENT ME!
    *
    * @param text DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   protected boolean responseContains(String text) throws Exception {
      if ((resp == null) || (resp.getText() == null)) {
         return false;
      }

      return resp.getText()
                 .indexOf(text) != -1;
   }


   private int getStatusCode() {
      return resp.getResponseCode();
   }


   private void doIt(WebRequest request,
                     List       args) throws Exception {
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


   private String replace(String from,
                          String search,
                          String replace) {
      if ((search == null) || (replace == null)) {
         return from;
      } else {
         int pos = from.indexOf(search);

         if (pos == -1) {
            return from;
         } else {
            String str = from.substring(0, pos) + replace
                         + from.substring(pos + search.length());

            return str;
         }
      }
   }


   private String replaceParam(String param) {
      return replace(param, paramSearch, paramReplace);
   }


   private String replaceURL(String url) {
      return replace(url, urlSearch, urlReplace);
   }
}
