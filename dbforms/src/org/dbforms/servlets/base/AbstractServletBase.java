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

package org.dbforms.servlets.base;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbforms.servlets.Controller;
import org.dbforms.util.MessageResources;
import org.dbforms.util.MultipartRequest;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.Util;

/**
 * This is the abstract base class for generating reports.
 * 
 * @author Henner Kollmann
 */
public abstract class AbstractServletBase extends HttpServlet {
   /** logging category for this class */
   private static Log logCat        = LogFactory.getLog(Controller.class.getName());

   /** 100KB default upload size */
   private int        maxUploadSize = 102400;

   /**
    * Basic servlet method, answers requests from the browser.
    * 
    * @param request
    *           HTTPServletRequest
    * @param response
    *           HTTPServletResponse
    * 
    * @throws ServletException
    *            if there is a servlet problem.
    * @throws IOException
    *            if there is an I/O problem.
    */
   public final void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      process_internal(request, response);
   }

   /**
    * Basic servlet method, answers requests fromt the browser.
    * 
    * @param request
    *           HTTPServletRequest
    * @param response
    *           HTTPServletResponse
    * 
    * @throws ServletException
    *            if there is a servlet problem.
    * @throws IOException
    *            if there is an I/O problem.
    */
   public final void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      process_internal(request, response);
   }

   /**
    * generates a report from request. Tries to get data from DbForms.
    * 
    * @param request
    *           HTTPServletRequest
    * @param response
    *           HTTPServletResponse
    */
   private final void process_internal(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
      // Verify if Locale have been setted in session with "LOCALE_KEY"
      // if not, take the request.getLocale() as default and put it in session
      // Verify if Locale have been setted in session with "LOCALE_KEY"
      // if not, take the request.getLocale() as default and put it in session
      processLocale(request);

      // create RFC-1867 data wrapper for the case the form was sent in
      // multipart mode
      // this is needed because common HttpServletRequestImpl - classes do not
      // support it
      // the whole application has to access Request via the "ParseUtil" class
      // this is also true for jsp files written by the user
      // #fixme taglib needed for convenient access to ParseUtil wrapper methods
      String contentType = request.getContentType();

      if (!Util.isNull(contentType) && contentType.startsWith("multipart")) {
         try {
            MultipartRequest multipartRequest = new MultipartRequest(request, maxUploadSize);
            request.setAttribute("multipartRequest", multipartRequest);
         } catch (IOException ioe) {
            logCat.error("::process - check if uploaded file(s) exceeded allowed size", ioe);
            sendErrorMessage("Check if uploaded file(s) exceeded allowed size.", response);
            return;
         }
      }
      process(request, response);
   }

   /**
    * generates a report from request. Tries to get data from DbForms.
    * 
    * @param request
    *           HTTPServletRequest
    * @param response
    *           HTTPServletResponse
    */
   abstract protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;


   /**
    * Initialize this servlet.
    * 
    * @exception ServletException
    *               if the initialization fails
    */
   public void init() throws ServletException {
      // if existing and valid, override default maxUploadSize, which determinates
      // how
      // big the http/multipart uploads may get
      String maxUploadSizeStr = this.getServletConfig().getInitParameter("maxUploadSize");
      if (!Util.isNull(maxUploadSizeStr)) {
         try {
            this.maxUploadSize = Integer.parseInt(maxUploadSizeStr);
         } catch (NumberFormatException nfe) {
            logCat.error("maxUploadSize not a valid number => using default.");
         }
      }
   }

   /**
    * Set the default locale
    * 
    * @param request
    *           the request object
    */
   private void processLocale(HttpServletRequest request) {
      String lang = ParseUtil.getParameter(request, "lang");
      String country = ParseUtil.getParameter(request, "country", "");
      Locale locale = null;

      if (!Util.isNull(lang)) {
         locale = new Locale(lang, country);
      } else if (MessageResources.getLocale(request) == null) {
         MessageResources.setLocale(request, request.getLocale());
      }

      if (locale != null) {
         MessageResources.setLocale(request, locale);
      }
   }

   /**
    * Send error messages to the servlet's output stream
    * 
    * @param message
    *           the message to display
    * @param response
    *           the response object
    */
   protected void sendErrorMessage(String message, HttpServletResponse response) {
      try {
         PrintWriter out = response.getWriter();

         response.setContentType("text/html");
         out.println("<html><body><h1>ERROR:</h1><p>");
         out.println(message);
         out.println("</p></body></html>");
      } catch (IOException ioe) {
         logCat.error("::sendErrorMessage - senderror message crashed", ioe);
      }
   }

}
