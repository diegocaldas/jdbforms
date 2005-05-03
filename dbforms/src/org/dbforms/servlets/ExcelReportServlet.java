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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.dbforms.servlets.reports.LineReportServletAbstract;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.MessageResourcesInternal;
import org.dbforms.util.MessageResources;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * This servlet generates a Microsoft Excel xls file using POI. Data is read
 * from the current dbForm, a Collection or a ResultSetVector The template file
 * is in the reports directory with a .xr extension it consists of one line of
 * text, which is list of comma separated field names usage: with a simple goto
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
 * Parameters filename=xyz.xls name the output file sheetname=first_page name
 * the worksheet Support for grabbing data from a Collection or an existing
 * ResultSetVector set session variable "jasper.input" to use a Collection
 * object set session variable "jasper.rsv" to use a ResultSetVector object ex
 * &ltc:set var="jasper.rsv" value="${rsv_xxxxx}" scope="session" /&gt Note:
 * Setting column headings does not work.
 * 
 * @author Neal Katz
 */
public class ExcelReportServlet extends LineReportServletAbstract {

	private static final String SHEETNAMEPARAM = "sheetname";

	private HSSFWorkbook wb;

	private HSSFSheet sheet;

	private short rowCnt = 0;

	protected String getMimeType() {
		return "application/msexcel";
	}

	protected String getFileExtension() {
		return ".xls";
	}

	protected void openStream(OutputStream out)  throws Exception  {
	}

	protected void closeStream(OutputStream out) throws Exception {
		wb.write(out);
	}

	
	protected void writeHeader(String[] header) throws Exception {
		HSSFRow row = sheet.createRow(rowCnt++);
		for (int i = 0; i < header.length; i++) {
			HSSFCell cell = row.createCell((short) i);
			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(header[i]);
		}
	}

	protected void writeData(Object[] data) throws Exception {
		HSSFRow row = sheet.createRow( rowCnt++);
		for (int i = 0; i < data.length; i++) {
			if (data[i] != null) {
				// for null values we just skip the cell
				HSSFCell cell = row.createCell((short) i);
				if (data[i] instanceof Number) {
					cell.setCellValue(((Number) data[i]).doubleValue());
				} else if (data[i] instanceof Date) {
					cell.setCellValue((Date) data[i]);
				} else if (data[i] instanceof Calendar) {
					cell.setCellValue((Calendar) data[i]);
				} else {
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(data[i].toString());
				}
			}
		}
	}

	protected void process(HttpServletRequest request,
			HttpServletResponse response) {
		String sheetname = ParseUtil.getParameter(request, SHEETNAMEPARAM,
				MessageResourcesInternal.getMessage("dbforms.new_worksheet",
						MessageResources.getLocale(request)));
		wb = new HSSFWorkbook();
		sheet = wb.createSheet(sheetname);
		rowCnt = 0;
		super.process(request, response);
	}

}
