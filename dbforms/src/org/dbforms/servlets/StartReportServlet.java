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

import org.apache.log4j.Category;
import org.apache.commons.lang.StringUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import org.dbforms.servlets.reports.JRDataSourceRSV;
import org.dbforms.servlets.reports.ReportParameter;
import org.dbforms.event.WebEvent;
import org.dbforms.util.PageContextBuffer;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.Util;
import org.dbforms.util.MessageResourcesInternal;
import org.dbforms.util.external.FileUtil;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.ResultSetVector;
import org.dbforms.config.SqlUtil;
import org.dbforms.taglib.DbFormTag;
import dori.jasper.engine.JasperCompileManager;
import dori.jasper.engine.JasperFillManager;
import dori.jasper.engine.JasperPrint;
import dori.jasper.engine.JRDataSource;
import dori.jasper.engine.JRException;
import dori.jasper.engine.JRExporterParameter;
import dori.jasper.engine.JRExporter;
import dori.jasper.engine.export.JRPdfExporter;
import dori.jasper.engine.export.JRXlsExporter;



/**
 * This servlet starts a JasperReport. Data is read from the current dbForm.
 * Servlet is looking for the report xml file in WEB-INF/custom/reports,
 * WEB-INF/reports. If report xml is newer then jasper file report will be
 * recompiled. Data is send to report as JRDataSourceRSV which is a Wrapper
 * around an ResultSetVector. To enable subreports, message resources etc an
 * ReportParameter is send to the JasperReport.  usage: with a simple goto
 * button: &lt;db:gotoButton destTable="web_parts" destination="
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
 * 
 * @author Henner Kollmann
 */
public class StartReportServlet extends HttpServlet
{
   /** DOCUMENT ME! */
   private static Category logCat = Category.getInstance("StartReportServlet");

   /** DOCUMENT ME! */
   public static final String REPORTNAMEPARAM = "reportname";

   /** DOCUMENT ME! */
   public static final String REPORTTYPEPARAM = "reporttype";

   /** DOCUMENT ME! */
   public static final String REPORTCONFIGDIR = "reportdirs";
   private String[]           reportdirs;

   /**
    * Initialize this servlet.
    * 
    * @exception ServletException if we cannot configure ourselves correctly
    */
   public void init() throws ServletException
   {
      String value = getServletConfig().getInitParameter(REPORTCONFIGDIR);

      if (value == null)
      {
         value = "WEB-INF/reports/";
      }

      reportdirs = StringUtils.split(value, ",");
   }


   /**
    * Basic servlet method, answers requests from the browser.
    * 
    * @param request HTTPServletRequest
    * @param response HTTPServletResponse
    * 
    * @throws ServletException if there is a servlet problem.
    * @throws IOException if there is an I/O problem.
    */
   public void doGet(HttpServletRequest request, HttpServletResponse response)
              throws ServletException, IOException
   {
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
   public void doPost(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException
   {
      process(request, response);
   }


   /**
    * generates a report from request. Tries to get data from DbForms.
    * 
    * @param request HTTPServletRequest
    * @param response HTTPServletResponse
    */
   private void process(HttpServletRequest request, 
                        HttpServletResponse response)
   {
      // create report name
      try
      {
         String reportFile = getReportFileFullName(request.getPathInfo(), 
                                                   getServletContext(), request, 
                                                   response);

         if (!Util.isNull(reportFile))
         {
            logCat.info("=== user dir " + FileUtil.dirname(reportFile));
            System.setProperty("user.dir", FileUtil.dirname(reportFile));
            checkIfNeedToCompile(getServletContext(), reportFile);

            JRDataSource dataSource = getDataFromForm(getServletContext(), 
                                                      request, response);

            if (!response.isCommitted())
            {
               processReport(reportFile, dataSource, getServletContext(), 
                             request, response);
            }
         }
      }
      catch (Exception e)
      {
         logCat.error(e);
         handleException(request, response, e);
      }
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
   public static void processReport(String reportFileFullName, 
                                    JRDataSource dataSource, 
                                    ServletContext context, 
                                    HttpServletRequest request, 
                                    HttpServletResponse response)
   {
      try
      {
         if (Util.isNull(reportFileFullName))
         {
            return;
         }

         // generate parameter map
         DbFormsConfig   config = (DbFormsConfig) context.getAttribute(
                                           DbFormsConfig.CONFIG);
         ReportParameter repParam = new ReportParameter(request, 
                                                        SqlUtil.getConnection(
                                                                 config, 
                                                                 getConnectionName(
                                                                          request)), 
                                                        FileUtil.dirname(
                                                                 reportFileFullName)
                                                        + File.separator, 
                                                        context.getRealPath("")
                                                        + File.separator);
         Map map = new HashMap();
         map.put("PARAM", repParam);

         byte[] bytes = null;

         try
         {
            // Fill the report with data
            JasperPrint jPrint;

            try
            {
               if (dataSource == null)
               {
                  jPrint = JasperFillManager.fillReport(reportFileFullName
                                                        + ".jasper", map, 
                                                        repParam.getConnection());
               }
               else
               {
                  jPrint = JasperFillManager.fillReport(reportFileFullName
                                                        + ".jasper", map, 
                                                        dataSource);
               }
            }
            finally
            {
               repParam.getConnection().close();
            }

            if (jPrint.getPages().size() == 0)
            {
               handleNoData(request, response);

               return;
            }
            else
            {
               String outputFormat = ParseUtil.getParameter(request, 
                                                            StartReportServlet.REPORTTYPEPARAM, 
                                                            "PDF");

               // create the output stream
               if ("PDF".equals(outputFormat))
               {
                  response.setContentType("application/pdf");
                  bytes = exportToPDF(jPrint);
               }
               else if ("XLS".equals(outputFormat))
               {
                  response.setContentType("application/msexcel");
                  bytes = exportToXLS(jPrint);
               }
            }
         }
         catch (JRException e)
         {
            logCat.error("jasper error: " + e.getMessage());
            handleException(request, response, e);

            return;
         }

         if ((bytes != null) && (bytes.length > 0))
         {
            // Send the output stream to the client
            response.setContentLength(bytes.length);

            ServletOutputStream ouputStream = response.getOutputStream();
            ouputStream.write(bytes, 0, bytes.length);
            ouputStream.flush();
            ouputStream.close();
         }
         else
         {
            handleEmptyResponse(request, response);

            return;
         }
      }
      catch (Exception e)
      {
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
   public String getReportFileFullName(String reportFileName, 
                                       ServletContext context, 
                                       HttpServletRequest request, 
                                       HttpServletResponse response)
   {
      String reportFile = null;

      try
      {
         boolean found = false;

         for (int i = 0; i < reportdirs.length; i++)
         {
            reportFile = context.getRealPath(reportdirs[i] + reportFileName);

            if (FileUtil.fileExists(reportFile + ".xml"))
            {
               found = true;

               break;
            }
         }

         if (!found)
         {
            handleNoReport(request, response);
            reportFile = null;
         }
      }
      catch (Exception e)
      {
         handleException(request, response, e);
      }

      return reportFile;
   }


   /**
    * DOCUMENT ME!
    * 
    * @param request DOCUMENT ME!
    * 
    * @return DOCUMENT ME!
    */
   public static String getConnectionName(HttpServletRequest request)
   {
      WebEvent webEvent = (WebEvent) request.getAttribute("webEvent");
      String   res = null;

      if ((webEvent != null) && (webEvent.getTableId() != -1))
      {
         res = ParseUtil.getParameter(request, 
                                      "invname_" + webEvent.getTableId());
      }

      return res;
   }


   /**
    * DOCUMENT ME!
    * 
    * @param request DOCUMENT ME!
    * @param response DOCUMENT ME!
    * @param e DOCUMENT ME!
    */
   public static void handleException(HttpServletRequest request, 
                                      HttpServletResponse response, Exception e)
   {
      sendErrorMessage(request, response, 
                       MessageResourcesInternal.getMessage(
                                "dbforms.reports.exception", 
                                request.getLocale(), 
                                new String[] 
      {
         e.toString()
      }));
   }


   /**
    * DOCUMENT ME!
    * 
    * @param request DOCUMENT ME!
    * @param response DOCUMENT ME!
    */
   public static void handleNoData(HttpServletRequest request, 
                                   HttpServletResponse response)
   {
      sendErrorMessage(request, response, 
                       MessageResourcesInternal.getMessage(
                                "dbforms.reports.nodata", request.getLocale()));
   }


   private static void sendErrorMessageText(HttpServletResponse response, 
                                            String message)
   {
      try
      {
         PrintWriter out = response.getWriter();
         response.setContentType("text/html");
         out.println("<html><body><h1>ERROR</h1><p>");
         out.println(message);
         out.println("</p></body></html>");
         out.flush();
         out.close();
      }
      catch (IOException ioe2)
      {
         logCat.error("!!!senderror message crashed!!!" + ioe2.getMessage());
      }
   }


   private static void sendErrorMessage(HttpServletRequest request, 
                                        HttpServletResponse response, 
                                        String message)
   {
      try
      {
         Vector errors = (Vector) request.getAttribute("errors");
         errors.add(new Exception(message));

         String fue         = ParseUtil.getParameter(request, "source");
         String contextPath = request.getContextPath();
         fue = fue.substring(contextPath.length());

         if (Util.isNull(fue))
         {
            sendErrorMessageText(response, message);
         }
         else
         {
            request.getRequestDispatcher(fue).forward(request, response);
         }
      }
      catch (Exception ex)
      {
         sendErrorMessageText(response, message);
      }
   }


   private static void handleNoReport(HttpServletRequest request, 
                                      HttpServletResponse response)
   {
      sendErrorMessage(request, response, 
                       MessageResourcesInternal.getMessage(
                                "dbforms.reports.noreport", request.getLocale(), 
                                new String[] 
      {
         request.getPathInfo()
      }));
   }


   private static void handleEmptyResponse(HttpServletRequest request, 
                                           HttpServletResponse response)
   {
      sendErrorMessage(request, response, 
                       MessageResourcesInternal.getMessage(
                                "dbforms.reports.nooutput", request.getLocale()));
   }


   private static byte[] exportToPDF(JasperPrint jasperPrint)
                              throws JRException
   {
      ByteArrayOutputStream baos     = new ByteArrayOutputStream();
      JRExporter            exporter = new JRPdfExporter();
      exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
      exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
      exporter.exportReport();

      return baos.toByteArray();
   }


   private static byte[] exportToXLS(JasperPrint jasperPrint)
                              throws JRException
   {
      ByteArrayOutputStream baos     = new ByteArrayOutputStream();
      JRExporter            exporter = new JRXlsExporter();
      exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
      exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
      exporter.exportReport();

      return baos.toByteArray();
   }


   private static void compileJasper(ServletContext context, String reportFile)
                              throws Exception
   {
      logCat.info("=== start to compile " + reportFile);

      // Tomcat specific!! Other jsp engine may handle this different!!
      String classpath = (String) context.getAttribute(
                                  "org.apache.catalina.jsp_classpath");
      logCat.info("=== used classpath " + classpath);
      System.setProperty("jasper.reports.compile.class.path", classpath);

      try
      {
         JasperCompileManager.compileReportToFile(reportFile);
      }
      catch (Exception e)
      {
         logCat.error("compile error " + e.getMessage());
         throw e;
      }
   }


   private static void checkIfNeedToCompile(ServletContext context, 
                                            String reportFile)
                                     throws Exception
   {
      File   dir  = FileUtil.getFile(FileUtil.dirname(reportFile));
      File[] list = dir.listFiles();

      for (int i = 0; i < list.length; i++)
      {
         String s   = FileUtil.removeExtension(list[i].getPath());
         String ext = FileUtil.getExtension(list[i].getPath());

         if (s.startsWith(reportFile) && (ext.equals("xml")))
         {
            File xmlFile    = list[i];
            File jasperFile = FileUtil.getFile(s + ".jasper");

            if (!jasperFile.exists()
                      || (xmlFile.lastModified() > jasperFile.lastModified()))
            {
               compileJasper(context, xmlFile.getPath());
            }
         }
      }
   }


   private JRDataSource getDataFromForm(ServletContext context, 
                                        HttpServletRequest request, 
                                        HttpServletResponse response)
   {
      JRDataSource dataSource = null;

      // Create form to get the resultsetvector
      try
      {
         WebEvent webEvent = (WebEvent) request.getAttribute("webEvent");

         if ((webEvent != null) && (webEvent.getTableId() != -1))
         {
            // Generate DataSource for JasperReports from call to DbForm
            DbFormsConfig config = (DbFormsConfig) context.getAttribute(
                                            DbFormsConfig.CONFIG);
            String        tableName = config.getTable(webEvent.getTableId())
                                            .getName();

            // Simulate call to DbFormTag to get resultsetvector
            PageContext pageContext = new PageContextBuffer();
            pageContext.initialize(this, request, response, null, true, 0, true);

            DbFormTag form = new DbFormTag();
            form.setPageContext(pageContext);
            form.setTableName(tableName);

            String maxRows = ParseUtil.getParameter(request, "MaxRows", "*");
            form.setMaxRows(maxRows);
            form.setFollowUp("");
            form.setAutoUpdate("false");

            // set the source attribute to the requestURI.
            // So the form will think that the source is equal to the target and will use order constraints
            String saveSource = ParseUtil.getParameter(request, "source");
            request.setAttribute("source", request.getRequestURI());
            form.doStartTag();
            request.setAttribute("source", saveSource);

            ResultSetVector rsv = form.getResultSetVector();
            logCat.info("get resultsetvector rsv= " + rsv.size());

            if (rsv.size() == 0)
            {
               handleNoData(request, response);
            }
            else
            {
               dataSource = new JRDataSourceRSV(rsv);
            }

            form.doFinally();
         }
      }
      catch (Exception e)
      {
         logCat.error(e);
      }

      return dataSource;
   }
}