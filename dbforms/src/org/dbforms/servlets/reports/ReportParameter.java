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

package org.dbforms.servlets.reports;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.config.Field;


import org.dbforms.taglib.TextFormatterUtil;
import org.dbforms.util.MessageResources;
import org.dbforms.util.ParseUtil;

import java.io.File;
import java.util.Map;

/**
 * Helper class send as parameter to JasperReports. So it is not neccesary to
 * send all the stuff in different parameters
 */
import java.sql.Connection;

import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class ReportParameter {
   private static Log         logCat     = LogFactory.getLog(ReportParameter.class);
   private Connection         connection;
   private HttpServletRequest request;
   private Locale             locale;
   private ServletContext     context;
   private String             reportPath;
   private Map attributes;			     		

   /**
    * Creates a new ReportParameter object.
    *
    * @param context DOCUMENT ME!
    * @param request DOCUMENT ME!
    * @param connection DOCUMENT ME!
    * @param reportPath DOCUMENT ME!
    */
   public ReportParameter(ServletContext     context,
                          HttpServletRequest request,
						  Map attributes,
                          Connection         connection,
                          String             reportPath) {
      this.context    = context;
      this.request    = request;
      this.connection = connection;
      this.reportPath = reportPath;
      this.attributes = attributes;
      this.locale     = MessageResources.getLocale(request);
   }

   /**
    * Returns the connection.
    *
    * @return Connection
    */
   public Connection getConnection() {
      return connection;
   }


   /**
    * DOCUMENT ME!
    *
    * @return
    */
   public String getContextPath() {
      return context.getRealPath("") + File.separator;
   }


   /**
    * Returns a message
    *
    * @param msg DOCUMENT ME!
    *
    * @return String
    */
   public String getMessage(String msg) {
      return MessageResources.getMessage(request, msg);
   }


   /**
    * Returns a request parameter
    *
    * @param param DOCUMENT ME!
    *
    * @return String
    */
   public String getParameter(String param) {
      return ParseUtil.getParameter(request, param);
   }

   /**
    * Returns a request parameter
    *
    * @param param DOCUMENT ME!
    *
    * @return String
    */
   public Object getAttribute(String key) {
      return attributes.get(key);
   }

   /**
    * Returns the reportPath.
    *
    * @return String
    */
   public String getReportPath() {
      return reportPath;
   }


   /**
    * Returns a formatted string with the same formatting as used inside
    * dbforms
    *
    * @param obj The object to format
    * @param pattern to use as pattern for numeric and date fields
    *
    * @return The string representation
    */
   public String getStringValue(Object obj,
                                String pattern) {
      try {
         Field field = new Field();
         field.setTypeByObject(obj);
         TextFormatterUtil f = new TextFormatterUtil(field, locale, pattern, obj);
         return f.getFormattedFieldValue();
      } catch (Exception e) {
         logCat.error(e);

         return e.getMessage();
      }
   }


   /**
    * Returns a formatted string with the same formatting as used inside
    * dbforms
    *
    * @param obj The object to format
    *
    * @return The string representation
    */
   public String getStringValue(Object obj) {
      return getStringValue(obj, null);
   }
}
