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

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.servlets.reports.JRDataSourceAbstract;
import org.dbforms.servlets.reports.ReportServletAbstract;
import org.dbforms.servlets.reports.ReportWriter;

import org.dbforms.util.Util;
import org.dbforms.util.external.FileUtil;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * This servlet generates a comma separated values file (CSV). Data is read from
 * the current dbForm, a Collection or a ResultSetVector The template file is in
 * the reports directory with a .cr extension it consists of two lines of text,
 * the first is a list of stirngs to use as header the second a list of comma
 * separated field names. both lines must have the same number of elements.
 * Normally the output file will use the first row as the headers. But if no
 * header line is provided, then no headers are output and every row is data.
 * usage: with a simple goto button: &lt;db:gotoButton destTable="web_parts"
 * destination=" /reports/Artikel"/&gt; or for one record: &lt;db:gotoButton
 * destTable="web_parts" keyToDestPos="currentRow"
 * destination="/reports/Artikel" /&gt; Servlet mapping must be set to handle
 * all /reports by this servlet!!! &lt;servlet/&gt;
 * &lt;servlet-name/&gt;startreport&lt;/servlet-name/&gt;
 * &lt;display-name/&gt;startreport&lt;/display-name/&gt;
 * &lt;servlet-class/&gt;org.dbforms.StartReportServlet&lt;/servlet-class/&gt;
 * &lt;/servlet&gt; &lt;servlet-mapping/&gt;
 * &lt;servlet-name/&gt;startreport&lt;/servlet-name/&gt;
 * &lt;url-pattern/&gt;/reports/&lt;/url-pattern/&gt; &lt;/servlet-mapping&gt;
 * web.xml optional parameters reportdirs list of directories to search for
 * report file reportMimeType mime type to send to browser Parameters
 * filename=xyz.csv name the output file Support for grabbing data from a
 * Collection or an existing ResultSetVector set session variable "jasper.input"
 * to use a Collection object set session variable "jasper.rsv" to use a
 * ResultSetVector object ex &ltc:set var="jasper.rsv" value="${rsv_xxxxx}"
 * scope="session" /&gt
 * 
 * @author Neal Katz
 */
public abstract class LineReportServletAbstract extends ReportServletAbstract {
	private static Log logCat = LogFactory
			.getLog(LineReportServletAbstract.class);

	protected abstract String getMimeType();

	protected abstract String getFileExtension();

	protected abstract void writeData(Object[] data) throws Exception;
	protected abstract void openStream(OutputStream out) throws Exception;
	protected abstract void closeStream(OutputStream out) throws Exception;

	private static final String REPORTMIMETYPEPARAM = "reportMimeType";

	private String mimeType = getMimeType();

	private int rownum = 0;

	public void init() throws ServletException {
		super.init();
		String value = getServletConfig().getInitParameter(REPORTMIMETYPEPARAM);
		if (!Util.isNull(value)) {
			mimeType = value;
		}
	}

	protected String getReportFileExtension() {
		return "xr";
	}

	protected void writeHeader(String[] header) throws Exception {
		writeData(header);
	}

	private Object getFieldValue(HttpServletRequest request,
			JRDataSourceAbstract dataSource, String search) {
		Object o = null;
		search = search.replaceAll("__", ".");

		if (search.startsWith("context.")) {
			search = search.substring(search.indexOf("context."));
			logCat.debug("Trying to find data for page context value: "
					+ search);
			o = getPageContextValue(request, search);
		} else if (search.startsWith("internal.")) {
			search = search.substring(search.indexOf("internal."));
			logCat.debug("Trying to find data for internal value: " + search);
			o = getInternalValue(search);
		} else if (search.startsWith("session.")) {
			search = search.substring(search.indexOf("session."));
			logCat.debug("Trying to find data for session value: " + search);
			o = getSessionValue(request.getSession(), search);
		} else {
			logCat.debug("Trying to find data for field named: " + search);
			o = dataSource.getFieldValue(search);
		}
		return o;

	}

	private Object getInternalValue(String search) {
		Object obj = null;
		try {
			logCat.debug("Trying to find data for internal var : " + search);
			if (search.equalsIgnoreCase("rownum")) {
				return new Integer(rownum);
			}
		} catch (Exception e) {
			logCat.error("getInternalValue: " + e);
		}

		return obj;
	}

	private Object getPageContextValue(HttpServletRequest request, String search) {
		Object obj = null;
		int pos;
		try {
			pos = search.indexOf(".");
			if (pos == -1) {
				// simple type, 'search' is an object in the session
				obj = request.getAttribute(search);
			} else {
				// complex, 'search' is really a bean
				String[] sa = search.split(".", 2);
				Object bean = request.getAttribute(sa[0]);
				if (obj != null) {
					logCat.debug("calling PropertyUtils.getProperty " + sa[0]
							+ " " + sa[1]);
					obj = PropertyUtils.getProperty(bean, sa[1]);
				}
			}
		} catch (Exception e) {
			logCat.error("getPageContextValue: " + e);
		}

		return obj;
	}

	private Object getSessionValue(HttpSession session, String search) {
		Object obj = null;
		int pos;
		try {
			pos = search.indexOf(".");
			if (pos == -1) {
				// simple type, 'search' is an object in the session
				obj = session.getAttribute(search);
			} else {
				// complex, 'search' is really a bean
				String[] sa = search.split(".", 2);
				Object bean = session.getAttribute(sa[0]);
				if (obj != null) {
					logCat.debug("calling PropertyUtils.getProperty " + sa[0]
							+ " " + sa[1]);
					obj = PropertyUtils.getProperty(bean, sa[1]);
				}
			}
		} catch (Exception e) {
			logCat.error("getSessionValue: " + e);
		}

		return obj;
	}

	private ReportWriter fillReport(HttpServletRequest request,
			String[] header, String[] fields, JRDataSourceAbstract dataSource)
			throws Exception {
		ReportWriter res = new ReportWriter();
		res.mimeType = mimeType;
		res.data = new ByteArrayOutputStream();
		res.fileName = getFileExtension();
		openStream(res.data);
		writeHeader(header);
		// Write out the data
		Object[] data = new Object[fields.length];
		while (dataSource.next()) {
			rownum++;
			for (int i = 0; i < fields.length; i++) {
				data[i] = getFieldValue(request, dataSource, fields[i]);
			}
			writeData(data);
		}
		closeStream(res.data);
		return res;
	}

	/**
	 * generates a report.
	 * 
	 * @param reportFileFullName
	 *            filename of report to process reportHTTPServletRequest
	 *            generated by getReportFile! getReportFile should be called
	 *            before fetching data, so that error handling of report not
	 *            found e.g. could be processed first!
	 * @param dataSource
	 *            data for the report
	 * @param context
	 *            ServletContext
	 * @param request
	 *            HTTPServletRequest
	 * @param response
	 *            HTTPServletResponse
	 */
	protected ReportWriter processReport(String reportFileFullName,
			JRDataSourceAbstract dataSource, ServletContext context,
			HttpServletRequest request, HttpServletResponse response) {

		try {
			File f = new File(reportFileFullName);
			BufferedReader in = new BufferedReader(new FileReader(f));
			String line1 = in.readLine();
			String line2 = in.readLine();
			String fields = null;
			String headers = null;
			if (Util.isNull(line2)) {
				fields = line1;
			} else {
				headers = line1;
				fields = line2;
			}
			if (Util.isNull(fields)) {
				logCat.error("no fields found");
				return null;
			}
			String[] reportFields = fields.split(",");
			String[] headerFields;
			if (headers != null) {
				headerFields = headers.split(",");
			} else {
				headerFields = new String[] {};
			}
			if (reportFields.length != headerFields.length) {
				logCat.error("reportFields.length != headerFields.length");
				headerFields = reportFields;
			}
			if (reportFields.length == 0) {
				logCat.error("no fields found");
				return null;
			}

			ReportWriter res = fillReport(request, headerFields, reportFields,
					dataSource);
			if (res != null) {
				res.fileName = FileUtil.basename(reportFileFullName,
						res.fileName);
			}
			return res;
		} catch (Exception e) {
			logCat.error("read report file", e);
			handleException(request, response, e);
			return null;
		}
	}

}