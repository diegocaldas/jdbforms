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
import org.CVS.*;
import org.apache.log4j.Category;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.dbforms.*;
import org.dbforms.util.*;
import org.dbforms.util.external.*;
import org.dbforms.servlets.reports.*;
import org.dbforms.config.*;
import org.dbforms.event.*;
import org.dbforms.taglib.DbFormTag;
import dori.jasper.engine.*;
import dori.jasper.engine.export.*;



/**
 *
 * This servlet starts a JasperReport.</br>
 * Data is read from the current dbForm.</br>
 * Servlet is looking for the report xml file in
 * WEB-INF/custom/reports, WEB-INF/reports.</br>
 *
 * If report xml is newer then jasper file report will be recompiled.</br>
 *
 * Data is send to report as JRDataSourceRSV which is a Wrapper around an
 * ResultSetVector.</br>
 *
 * To enable subreports, message resources etc an ReportParameter is send to the
 * JasperReport. </br>
 *
 * usage:</br>
 * with a simple goto button:</br>
 *        &lt;db:gotoButton</br>
 *               destTable="web_parts"</br>
 *                          destination=" /reports/Artikel"/&gt;</br>
 *
 *
 * or for one record:</br>
 *              &lt;db:gotoButton</br>
 *          destTable="web_parts"</br>
 *         keyToDestPos="currentRow"</br>
 *              destination="/reports/Artikel"</br>
 *                    /&gt;</br>
 *
 * Servlet mapping must be set to handle all /reports by this servlet!!!</br>
 *         &lt;servlet/&gt;</br>
 *            &lt;servlet-name/&gt;startreport&lt;/servlet-name/&gt;</br>
 *            &lt;display-name/&gt;startreport&lt;/display-name/&gt;</br>
 *            &lt;servlet-class/&gt;org.dbforms.StartReportServlet&lt;/servlet-class/&gt;</br>
 *       &lt;/servlet&gt;</br>
 *         &lt;servlet-mapping/&gt;</br>
 *             &lt;servlet-name/&gt;startreport&lt;/servlet-name/&gt;</br>
 *            &lt;url-pattern/&gt;/reports/*&lt;/url-pattern/&gt;</br>
 *       &lt;/servlet-mapping&gt;</br>
 *
 *
 *
 *
 * @author Henner Kollmann
 */
public class StartReportServlet extends HttpServlet
{
   /** DOCUMENT ME! */
   protected static Category logCat = Category.getInstance("StartReportServlet");

   /** DOCUMENT ME! */
   public static String REPORTNAMEPARAM = "reportname";

   /** DOCUMENT ME! */
   public static String REPORTTYPEPARAM = "reporttype";

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
    *
    */
   private void process(HttpServletRequest request, HttpServletResponse response)
   {
      // create report name
      try
      {
         String       reportFile = getReportFileFullName(request.getPathInfo(),
               getServletContext(), request, response);
         JRDataSource dataSource = getDataFromForm(getServletContext(),
               request, response);
         process(reportFile, dataSource, getServletContext(), request, response);
      }
      catch (Exception e)
      {
         logCat.error(e);
      }
   }


   /**
    * generates a report.
    *
    * @param reportFile filename of report to process reportHTTPServletRequest
    *                generated                by getReportFile!
    * getReportFile should be called before fetching data, so that error
    * handling of report not found e.g. could be processed first!
    * @param dataSource data for the report
     * @param context ServletContext
     * @param request HTTPServletRequest
    * @param response HTTPServletResponse
    *
    */
   public static void process(String reportFileFullName,
      JRDataSource dataSource, ServletContext context,
      HttpServletRequest request, HttpServletResponse response)
   {
      try
      {
         if (Util.isNull(reportFileFullName))
         {
            return;
         }

         // generate parameter map
         DbFormsConfig   config   = (DbFormsConfig) context.getAttribute(DbFormsConfig.CONFIG);
         ReportParameter repParam = new ReportParameter(request,
               SqlUtil.getConnection(config, getConnectionName(request)),
               FileUtil.dirname(reportFileFullName) + File.separator);
         Map map = new HashMap();
         map.put("PARAM", repParam);

         byte[] bytes = null;

         try
         {
            // Fill the report with data
            JasperPrint jPrint;

            if (dataSource == null)
            {
               jPrint = JasperFillManager.fillReport(reportFileFullName
                     + ".jasper", map, repParam.getConnection());
            }
            else
            {
               jPrint = JasperFillManager.fillReport(reportFileFullName
                     + ".jasper", map, dataSource);
            }

            repParam.getConnection().close();

            if (jPrint.getPages().size() == 0)
            {
               handleNoData(request, response);

               return;
            }
            else
            {
               String outputFormat = ParseUtil.getParameter(request,
                     StartReportServlet.REPORTTYPEPARAM, "PDF");

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
   public static String getReportFileFullName(String reportFileName,
      ServletContext context, HttpServletRequest request,
      HttpServletResponse response)
   {
      String reportFile = null;

      try
      {
         boolean found = false;
         reportFile = context.getRealPath("WEB-INF/custom/reports/"
               + reportFileName);

         if (FileUtil.fileExists(reportFile + ".xml"))
         {
            found = true;
         }

         reportFile = context.getRealPath("WEB-INF/reports/" + reportFileName);

         if (FileUtil.fileExists(reportFile + ".xml"))
         {
            found = true;
         }

         if (found)
         {
            checkIfNeedToCompile(context, reportFile);
         }
         else
         {
            handleNoReport(request, response);
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
      String   res      = "default";
      WebEvent webEvent = (WebEvent) request.getAttribute("webEvent");

      if ((webEvent != null) && (webEvent.getTableId() != -1))
      {
         res = ParseUtil.getParameter(request,
               "invname_" + webEvent.getTableId(), res);
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
         "JasperReport Error<BR>" + e.toString() + "<BR>");
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
      sendErrorMessage(request, response, "no data found to print");
   }


   private static void sendErrorMessage(HttpServletRequest request,
      HttpServletResponse response, String message)
   {
      try
      {
         Vector errors = (Vector) request.getAttribute("errors");
         errors.add(new Exception(message));

         String fue         = ParseUtil.getParameter(request, "source");
         String contextPath = request.getContextPath();
         fue = fue.substring(contextPath.length());
         request.getRequestDispatcher(fue).forward(request, response);
      }
      catch (Exception ex)
      {
         try
         {
            PrintWriter out = response.getWriter();
            response.setContentType("text/html");
            out.println("<html><body><h1>ERROR</h1><p>");
            out.println(message);
            out.println("</p></body></html>");
         }
         catch (IOException ioe2)
         {
            logCat.error("!!!senderror message crashed!!!" + ioe2);
         }
      }
   }


   private static void handleNoReport(HttpServletRequest request,
      HttpServletResponse response)
   {
      sendErrorMessage(request, response,
         "report " + request.getPathInfo() + " not found");
   }


   private static void handleEmptyResponse(HttpServletRequest request,
      HttpServletResponse response)
   {
      sendErrorMessage(request, response,
         "JasperReport Error<BR>No output generated");
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
      throws JRException
   {
      logCat.debug("=== start to compile " + reportFile);

      // Tomcat specific!! Other jsp engine may handle this different!!
      String classpath = (String) context.getAttribute(
            "org.apache.catalina.jsp_classpath");
      logCat.debug("=== used classpath " + classpath);
      System.setProperty("jasper.reports.compile.class.path", classpath);

      try
      {
         JasperCompileManager.compileReportToFile(reportFile);
      }
      catch (JRException e)
      {
         logCat.error("compile error " + e.getMessage());
         throw e;
      }
   }


   private static void checkIfNeedToCompile(ServletContext context,
      String reportFile) throws JRException
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
      HttpServletRequest request, HttpServletResponse response)
   {
      JRDataSource dataSource = null;

      // Create form to get the resultsetvector
      try
      {
         WebEvent webEvent = (WebEvent) request.getAttribute("webEvent");

         if ((webEvent != null) && (webEvent.getTableId() != -1))
         {
            // Generate DataSource for JasperReports from call to DbForm
            DbFormsConfig config    = (DbFormsConfig) context.getAttribute(DbFormsConfig.CONFIG);
            String        tableName = config.getTable(webEvent.getTableId())
                                            .getName();

            // Simulate call to DbFormTag to get resultsetvector
            PageContext pageContext = new PageContextDummy();
            pageContext.initialize(this, request, response, null, true, 0, true);

            DbFormTag form = new DbFormTag();
            form.setPageContext(pageContext);
            form.setTableName(tableName);

            String MaxRows = ParseUtil.getParameter(request, "MaxRows", "*");
            form.setMaxRows(MaxRows);
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
