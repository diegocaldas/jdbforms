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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.DbFormsConfigRegistry;

import org.dbforms.event.AbstractWebEvent;

import org.dbforms.servlets.reports.AbstractJRDataSource;
import org.dbforms.servlets.reports.ReportParameter;
import org.dbforms.servlets.reports.AbstractReportServlet;
import org.dbforms.servlets.reports.ReportWriter;

import org.dbforms.util.ParseUtil;
import org.dbforms.util.SqlUtil;
import org.dbforms.util.external.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;

import java.sql.Connection;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * This servlet starts a JasperReport.
 *
 * @author Henner Kollmann
 */
public class JasperReportServlet extends AbstractReportServlet {
   /** DOCUMENT ME! */
   private static Log logCat = LogFactory.getLog(JasperReportServlet.class
                                                 .getName());
   private static final String REPORTFILEEXTENSION = ".jrxml";


   /** DOCUMENT ME! */
   private static final String REPORTTYPEPARAM = "reporttype";

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   protected String getReportFileExtension() {
      return REPORTFILEEXTENSION;
   }


   /**
    * DOCUMENT ME!
    *
    * @param context DOCUMENT ME!
    * @param reportFile DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   protected void compileReport(ServletContext context,
                                String         reportFile)
                         throws Exception {
      logCat.info("=== user dir " + FileUtil.dirname(reportFile));
      System.setProperty("user.dir", FileUtil.dirname(reportFile));

      File   dir  = FileUtil.getFile(FileUtil.dirname(reportFile));
      File[] list = dir.listFiles();

      for (int i = 0; i < list.length; i++) {
         String s   = FileUtil.removeExtension(list[i].getPath());
         String ext = "." + FileUtil.getExtension(list[i].getPath());

         if (s.startsWith(reportFile) && (ext.equals(getReportFileExtension()))) {
            File xmlFile    = list[i];
            File jasperFile = FileUtil.getFile(s + ".jasper");

            if (!jasperFile.exists()
                      || (xmlFile.lastModified() > jasperFile.lastModified())) {
               compileJasper(context, xmlFile.getPath());
            }
         }
      }
   }


   /**
    * generates a report.
    *
    * @param reportFileFullName filename of report to process
    *        reportHTTPServletRequest generated by getReportFile!
    *        getReportFile should be called before fetching data, so that
    *        error handling of report not found e.g. could be processed first!
    * @param dataSource data for the report
    * @param context ServletContext
    * @param request HTTPServletRequest
    * @param response HTTPServletResponse
    *
    * @return DOCUMENT ME!
    */
   protected ReportWriter processReport(String               reportFileFullName,
                                        AbstractJRDataSource dataSource,
                                        ServletContext       context,
                                        HttpServletRequest   request,
                                        HttpServletResponse  response) {
      /*
       * Grunikiewicz.philip@hydro.qc.ca 2004-01-14
       *
       * In order to correct problem with displaying dynamically generated PDF
       * files in IE, I followed the suggestions provided in the iText library
       * FAQ. This simply meant using a ByteArrayOutputStream and not a byte
       * array as previously coded.
       */
      ReportWriter res = new ReportWriter();

      try {
         // generate parameter map
         DbFormsConfig config = null;

         try {
            config = DbFormsConfigRegistry.instance()
                                          .lookup();
         } catch (Exception e) {
            logCat.error("processReport", e);
            throw new ServletException(e);
         }

         try {
            // Fill the report with data
            JasperPrint jPrint   = null;
            AbstractWebEvent    webEvent = (AbstractWebEvent) request.getAttribute("webEvent");
            Connection  con      = null;

            if ((webEvent != null) && (webEvent.getTable() == null)) {
               con = config.getConnection();
            } else {
               con = config.getConnection(getConnectionName(request));
            }

            try {
               ReportParameter repParam = new ReportParameter(context, request,
                                                              dataSource
                                                              .getAttributes(),
                                                              con,
                                                              FileUtil.dirname(reportFileFullName)
                                                              + File.separator);
               Map             map = new HashMap();
               map.put("PARAM", repParam);
               jPrint = JasperFillManager.fillReport(reportFileFullName
                                                     + ".jasper", map,
                                                     dataSource);
            } catch (Exception e) {
               logCat.error("processReport", e);
            } finally {
               SqlUtil.closeConnection(con);
            }

            if ((jPrint == null) || (jPrint.getPages()
                                                 .size() == 0)) {
               return null;
            } else {
               String outputFormat = ParseUtil.getParameter(request,
                                                            JasperReportServlet.REPORTTYPEPARAM,
                                                            "PDF");

               if ("PDF".equals(outputFormat)) {
                  res.mimeType = "application/pdf";
                  res.data     = exportToPDF(jPrint);
                  res.fileName = FileUtil.filename(reportFileFullName) + ".pdf";
               } else if ("XLS".equals(outputFormat)) {
                  res.mimeType = "application/msexcel";
                  res.data     = exportToXLS(jPrint);
                  res.fileName = FileUtil.filename(reportFileFullName) + ".xls";
               } else if ("CSV".equalsIgnoreCase(outputFormat)) {
                  res.mimeType = "text/comma-separated-values";
                  res.data     = exportToCSV(jPrint);
                  res.fileName = FileUtil.filename(reportFileFullName) + ".csv";
               }
               jPrint       = null;

               return res;
            }
         } catch (JRException e) {
            logCat.error("processReport", e);
            handleException(request, response, e);

            return null;
         }
      } catch (Exception e) {
         handleException(request, response, e);

         return null;
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   private static String getConnectionName(HttpServletRequest request) {
      AbstractWebEvent webEvent = (AbstractWebEvent) request.getAttribute("webEvent");
      String   res = null;

      if ((webEvent != null) && (webEvent.getTable()
                                               .getId() != -1)) {
         res = ParseUtil.getParameter(request,
                                      "invname_" + webEvent.getTable().getId());
      }

      return res;
   }


   private void compileJasper(ServletContext context,
                              String         reportFile)
                       throws Exception {
      logCat.info("=== start to compile " + reportFile);

      // Tomcat specific!! Other jsp engine may handle this different!!
      String classpath = (String) context.getAttribute("org.apache.catalina.jsp_classpath");
      logCat.info("=== used classpath " + classpath);
      System.setProperty("jasper.reports.compile.class.path", classpath);

      try {
         JasperCompileManager.compileReportToFile(reportFile);
      } catch (Exception e) {
         logCat.error("compile", e);
         throw e;
      }
   }


   private ByteArrayOutputStream exportToCSV(JasperPrint jasperPrint)
                                      throws JRException {
      ByteArrayOutputStream baos     = new ByteArrayOutputStream();
      JRExporter            exporter = new JRCsvExporter();
      exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
      exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
      exporter.exportReport();

      return baos;
   }


   private ByteArrayOutputStream exportToPDF(JasperPrint jasperPrint)
                                      throws JRException {
      ByteArrayOutputStream baos     = new ByteArrayOutputStream();
      JRExporter            exporter = new JRPdfExporter();
      exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
      exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
      exporter.exportReport();

      return baos;
   }


   private ByteArrayOutputStream exportToXLS(JasperPrint jasperPrint)
                                      throws JRException {
      ByteArrayOutputStream baos     = new ByteArrayOutputStream();
      JRExporter            exporter = new JRXlsExporter();
      exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
      exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
      exporter.exportReport();

      return baos;
   }
}
