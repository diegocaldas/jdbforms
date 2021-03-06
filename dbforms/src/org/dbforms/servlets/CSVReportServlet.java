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

import org.dbforms.servlets.reports.AbstractLineReportServlet;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

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
public class CSVReportServlet extends AbstractLineReportServlet {

	private static final char Q = '\"';

	private static final String DEFAULTMIMETYPE = "text/comma-separated-values;charset=ASCII";

	private String clean(String s) {
		s = s.replaceAll("\"", "\\\"");
		return s;
	}

	protected String getMimeType() {
		return DEFAULTMIMETYPE;
	}

	protected String getFileExtension() {
		return ".csv";
	}

	private PrintWriter pw;

	protected void openStream(OutputStream out)  throws Exception {
		OutputStreamWriter osw;
		try {
			osw = new OutputStreamWriter(out, "UTF8");
		} catch (Exception e) {
			osw = new OutputStreamWriter(out);
		}
		pw = new PrintWriter(osw);
	}

	protected void closeStream(OutputStream out)  throws Exception {
		pw.flush();
		pw.close();
	}

	protected void writeData(Object[] data) throws Exception {
		for (int i = 0; i < data.length; i++) {
			if (i > 0) {
				pw.print(',');
			}
			if (data[i] != null) {
				pw.print(Q + clean(data[i].toString()) + Q);
			} else {
				pw.print(Q + "" + Q);
			}
		}
		pw.println();
	}

}
