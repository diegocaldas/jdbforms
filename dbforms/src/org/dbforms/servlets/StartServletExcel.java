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

package org.dbforms.servlets;

import net.sf.jasperreports.engine.JRDataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.DbFormsConfigRegistry;
import org.dbforms.config.ResultSetVector;
import org.dbforms.config.Table;

import org.dbforms.event.WebEvent;

import org.dbforms.servlets.reports.ExcelReportWriter;
import org.dbforms.servlets.reports.JRDataSourceIter;
import org.dbforms.servlets.reports.JRDataSourceRSV;

import org.dbforms.taglib.DbFormTag;

import org.dbforms.util.MessageResourcesInternal;
import org.dbforms.util.PageContextBuffer;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.Util;
import org.dbforms.util.external.FileUtil;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;



/**
 * This servlet generates a Microsoft Excel xls file using POI. Data is read
 * from the current dbForm, a Collection or a ResultSetVector  The template
 * file is in the reports directory with a .xr extension it consists of one
 * line of text, which is list of comma separated field names  usage: with a
 * simple goto button: &lt;db:gotoButton destTable="web_parts" destination="
 * /reports/Artikel"/&gt; or for one record: &lt;db:gotoButton
 * destTable="web_parts" keyToDestPos="currentRow"
 * destination="/reports/Artikel" /&gt; Servlet mapping must be set to handle
 * all /reports by this servlet!!! &lt;servlet/&gt;
 * &lt;servlet-name/&gt;startreport&lt;/servlet-name/&gt;
 * &lt;display-name/&gt;startreport&lt;/display-name/&gt;
 * &lt;servlet-class/&gt;org.dbforms.StartReportServlet&lt;/servlet-class/&gt;
 * &lt;/servlet&gt; &lt;servlet-mapping/&gt;
 * &lt;servlet-name/&gt;startreport&lt;/servlet-name/&gt;
 * &lt;url-pattern/&gt;/reports/&lt;/url-pattern/&gt; &lt;/servlet-mapping&gt;
 * Parameters filename=xyz.xls           name the output file
 * sheetname=first_page       name the worksheet  Support for grabbing data
 * from a Collection or an existing ResultSetVector set session variable
 * "jasper.input" to use a Collection object set session variable "jasper.rsv"
 * to use a ResultSetVector object ex &ltc:set var="jasper.rsv"
 * value="${rsv_xxxxx}" scope="session" /&gt Note: Setting column headings
 * does not work.
 *
 * @author Neal Katz
 */
public class StartServletExcel extends HttpServlet {
   private static Log logCat = LogFactory.getLog(StartServletExcel.class);

   /** DOCUMENT ME! */
   public static final String FILENAMEPARAM = "filename";

   /** DOCUMENT ME! */
   public static final String SHEETNAMEPARAM = "sheetname";

   //	public static final String REPORTNAMEPARAM = "reportname";

   /** DOCUMENT ME! */
   public static final String REPORTTYPEPARAM = "reporttype";

   /** DOCUMENT ME! */
   public static final String REPORTCONFIGDIR = "reportdirs";

   /** DOCUMENT ME! */
   public static final String mimeType = "application/msexcel";

   /** DOCUMENT ME! */
   public static final String extension  = ".xr";
   private String[]           reportdirs;

   /**
    * Basic servlet method, answers requests from the browser.
    *
    * @param request HTTPServletRequest
    * @param response HTTPServletResponse
    *
    * @throws ServletException if there is a servlet problem.
    * @throws IOException if there is an I/O problem.
    */
   public void doGet(HttpServletRequest  request,
                     HttpServletResponse response)
              throws ServletException, IOException {
      process(request, response);
   }


   /**
    * Basic servlet method, answers requests fromt the browser.
    *
    * @param request HTTPServletRequest
    * @param response HTTPServletResponse
    *
    * @throws ServletException if there is a servlet problem.
    * @throws IOException if there is an I/O problem.
    */
   public void doPost(HttpServletRequest  request,
                      HttpServletResponse response)
               throws ServletException, IOException {
      process(request, response);
   }


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param response DOCUMENT ME!
    * @param e DOCUMENT ME!
    */
   public static void handleException(HttpServletRequest  request,
                                      HttpServletResponse response,
                                      Exception           e) {
      sendErrorMessage(request, response,
                       MessageResourcesInternal.getMessage("dbforms.reports.exception",
                                                           request.getLocale(),
                                                           new String[] {
                                                              e.getMessage()
                                                           }));
   }


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param response DOCUMENT ME!
    */
   public static void handleNoData(HttpServletRequest  request,
                                   HttpServletResponse response) {
      sendErrorMessage(request, response,
                       MessageResourcesInternal.getMessage("dbforms.reports.nodata",
                                                           request.getLocale()));
   }


   /**
    * Initialize this servlet.
    *
    * @exception ServletException if we cannot configure ourselves correctly
    */
   public void init() throws ServletException {
      String value = getServletConfig()
                        .getInitParameter(REPORTCONFIGDIR);

      if (value == null) {
         value = "WEB-INF/reports/";
      }

      reportdirs = StringUtils.split(value, ',');
   }


   /**
    * generates a report.
    *
    * @param reportFileFullName filename of report to process
    *        reportHTTPServletRequest generated                by
    *        getReportFile! getReportFile should be called before fetching
    *        data, so that error handling of report not found e.g. could be
    *        processed first!
    * @param dataSource data for the report
    * @param context ServletContext
    * @param request HTTPServletRequest
    * @param response HTTPServletResponse
    */
   public static void processReport(String              reportFileFullName,
                                    JRDataSource        dataSource,
                                    ServletContext      context,
                                    HttpServletRequest  request,
                                    HttpServletResponse response) {
      ByteArrayOutputStream baos    = null;
      String                fields  = null;
      String                headers = null;

      // read in template file, 1st line is headers, 2 is name of fields
      // if only one line, use that as fields, and assume no headers
      reportFileFullName = reportFileFullName + extension;

      File f = new File(reportFileFullName);

      try {
         BufferedReader in = new BufferedReader(new FileReader(f));

         try {
            String line1 = in.readLine();
            String line2 = in.readLine();

            if ((line2 == null) || (line2.length() == 0)) {
               fields = line1;
            } else {
               headers = line1;
               fields  = line2;
            }
         } finally {
            in.close();
         }
      } catch (FileNotFoundException e1) {
         logCat.error(e1);
      } catch (IOException e) {
         logCat.error(e);
      }

      if (fields == null) {
         logCat.error("no fields found");
         handleEmptyResponse(request, response);

         return;
      }

      String[] reportFields = fields.split(",");
      String[] headerFields;

      if (headers != null) {
         headerFields = headers.split(",");
      } else {
         headerFields = reportFields;
      }

      if (reportFields.length != headerFields.length) {
         logCat.error("reportFields.length != headerFields.length");
         headerFields = reportFields;
      }

      if (reportFields.length == 0) {
         logCat.error("no fields found");
         handleEmptyResponse(request, response);

         return;
      }

      try {
         String sheetname = ParseUtil.getParameter(request, SHEETNAMEPARAM,
                                                   "new worksheet");

         if (Util.isNull(reportFileFullName)) {
            return;
         }

         ExcelReportWriter cw = new ExcelReportWriter();

         // set headers, 1st row will be name of fields
         cw.setHeaders(headerFields);
         cw.setSheetName(sheetname);

         // fill report by grabbing data from dataSource
         try {
            cw.fillReport(reportFields, dataSource);
         } catch (Exception e) {
            logCat.error("processReport: calling cw.fillReport()" + e);
            e.printStackTrace();
         }

         baos = cw.getOutput();

         if ((baos != null) && (baos.size() > 0)) {
            String filename = ParseUtil.getParameter(request, FILENAMEPARAM,
                                                     "output.xls");

            // create header
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control",
                               "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            response.setHeader("Content-disposition",
                               "inline; filename=" + filename);
            response.setContentType(mimeType);

            // Send the output stream to the client
            response.setContentLength(baos.size());

            ServletOutputStream outputStream = response.getOutputStream();
            baos.writeTo(outputStream);
            outputStream.flush();
            outputStream.close();
         } else {
            handleEmptyResponse(request, response);

            return;
         }
      } catch (Exception e) {
         handleException(request, response, e);

         return;
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param reportFileName DOCUMENT ME!
    * @param context DOCUMENT ME!
    * @param request DOCUMENT ME!
    * @param response DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getReportFileFullName(String              reportFileName,
                                       ServletContext      context,
                                       HttpServletRequest  request,
                                       HttpServletResponse response) {
      String reportFile = null;

      try {
         boolean found = false;

         for (int i = 0; i < reportdirs.length; i++) {
            reportFile = context.getRealPath(reportdirs[i] + reportFileName);

            if (FileUtil.fileExists(reportFile + ".xr")) {
               found = true;

               break;
            }
         }

         if (!found) {
            handleNoReport(request, response);
            reportFile = null;
         }
      } catch (Exception e) {
         handleException(request, response, e);
      }

      return reportFile;
   }


   /**
    * DOCUMENT ME!
    *
    * @param request
    * @param response
    */
   private static void handleEmptyResponse(HttpServletRequest  request,
                                           HttpServletResponse response) {
      sendErrorMessage(request, response,
                       MessageResourcesInternal.getMessage("dbforms.reports.nooutput",
                                                           request.getLocale()));
   }


   /**
    * DOCUMENT ME!
    *
    * @param request
    * @param response
    */
   private static void handleNoReport(HttpServletRequest  request,
                                      HttpServletResponse response) {
      sendErrorMessage(request, response,
                       MessageResourcesInternal.getMessage("dbforms.reports.noreport",
                                                           request.getLocale(),
                                                           new String[] {
                                                              request
                                                              .getPathInfo()
                                                           }));
   }


   /**
    * generates a report from request. Tries to get data from DbForms.
    *
    * @param request HTTPServletRequest
    * @param response HTTPServletResponse
    */
   private void process(HttpServletRequest  request,
                        HttpServletResponse response) {
      // create report name
      try {
         String reportFile = getReportFileFullName(request.getPathInfo(),
                                                   getServletContext(),
                                                   request, response);
         JRDataSource dataSource = getDataForReport(getServletContext(),
                                                    request, response);

         if (!response.isCommitted()) {
            processReport(reportFile, dataSource, getServletContext(), request,
                          response);
         }
      } catch (Exception e) {
         logCat.error("process", e);
         handleException(request, response, e);
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param request
    * @param response
    * @param message
    */
   private static void sendErrorMessage(HttpServletRequest  request,
                                        HttpServletResponse response,
                                        String              message) {
      try {
         Vector errors = (Vector) request.getAttribute("errors");
         errors.add(new Exception(message));

         String fue         = ParseUtil.getParameter(request, "source");
         String contextPath = request.getContextPath();

         if (!Util.isNull(fue)) {
            fue = fue.substring(contextPath.length());
         }

         if (Util.isNull(fue)) {
            sendErrorMessageText(response, message);
         } else {
            request.getRequestDispatcher(fue)
                   .forward(request, response);
         }
      } catch (Exception ex) {
         logCat.error("sendErrorMessage", ex);
         sendErrorMessageText(response, message);
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param response
    * @param message
    */
   private static void sendErrorMessageText(HttpServletResponse response,
                                            String              message) {
      try {
         PrintWriter out = response.getWriter();
         response.setContentType("text/html");
         out.println("<html><body><h1>ERROR</h1><p>");
         out.println(message);
         out.println("</p></body></html>");
         out.flush();
         out.close();
      } catch (IOException ioe2) {
         logCat.error("sendErrorMessageText", ioe2);
      }
   }


   /**
    * get a JRDataSource for report data. Source can be a Collection, an rsv,
    * or dbform if session variable "jasper.input" is set, it must point to a
    * Collection object if session variable "jasper.rsv" is set, it must point
    * to a ResultSetVector object otherwise the enclosing dbform is used for
    * data
    *
    * @param context
    * @param request
    * @param response
    *
    * @return
    */
   private JRDataSource getDataForReport(ServletContext      context,
                                         HttpServletRequest  request,
                                         HttpServletResponse response) {
      JRDataSource    dataSource = null;
      Table           table = null;
      ResultSetVector rsv   = null;

      try {
         PageContext pageContext = new PageContextBuffer();
         pageContext.initialize(this, request, response, null, true, 0, true);

         // is the datasource a Collection ?
         Object input = pageContext.findAttribute("jasper.input");

         if ((input != null) && (input instanceof Collection)) {
            Iterator iter = ((Collection) input).iterator();
            dataSource = new JRDataSourceIter(iter, pageContext);

            return dataSource;
         }

         // Create form to get the resultsetvector
         WebEvent webEvent = (WebEvent) request.getAttribute("webEvent");
         table = webEvent.getTable();

         if (table == null) {
            logCat.error("table==null");
         }

         // check if we are using a ResultSetVector passed by user
         rsv = (ResultSetVector) pageContext.findAttribute("jasper.rsv");

         if (rsv != null) {
            logCat.info("get resultsetvector rsv= " + rsv.size());

            if (rsv.size() == 0) {
               handleNoData(request, response);
            } else {
               dataSource = new JRDataSourceRSV(rsv, pageContext);
            }

            return dataSource;
         }

         if ((webEvent != null) && (table != null) && (table.getId() != -1)) {
            // Generate DataSource for JasperReports from call to DbForm
            DbFormsConfig config = null;

            try {
               config = DbFormsConfigRegistry.instance()
                                             .lookup();
            } catch (Exception e) {
               logCat.error(e);
               throw new ServletException(e);
            }

            String tableName = config.getTable(webEvent.getTable().getId())
                                     .getName();

            // Simulate call to DbFormTag to get resultsetvector
            DbFormTag form = new DbFormTag();
            form.setPageContext(pageContext);
            form.setTableName(tableName);

            String maxRows = ParseUtil.getParameter(request, "MaxRows", "*");
            form.setMaxRows(maxRows);
            form.setFollowUp("");
            form.setAutoUpdate("false");

            // set the source attribute to the requestURI.
            // So the form will think that the source is equal to the target and will use order constraints
            // Source must be saved and restored - we need it e.g. in error processing again!
            String saveSource = (String) request.getAttribute("source");
            String refSource = request.getRequestURI();

            if (request.getQueryString() != null) {
               refSource += ("?" + request.getQueryString());
            }

            request.setAttribute("source", refSource);
            form.doStartTag();
            request.setAttribute("source", saveSource);

            rsv = form.getResultSetVector();
            logCat.info("get resultsetvector rsv= " + rsv.size());

            if (rsv.size() == 0) {
               handleNoData(request, response);
            } else {
               dataSource = new JRDataSourceRSV(rsv, pageContext);
            }

            form.doFinally();
         }
      } catch (Exception e) {
         logCat.error(e);
      }

      return dataSource;
   }
}
