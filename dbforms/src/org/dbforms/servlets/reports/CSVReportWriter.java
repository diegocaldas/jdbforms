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

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import org.apache.log4j.Category;

import dori.jasper.engine.JRDataSource;
import dori.jasper.engine.JRException;

/**
 * @author Neal Katz
 *
 */
public class CSVReportWriter {
	private static Category logCat =
		Category.getInstance(CSVReportWriter.class);

	private final static char Q = '\"';
	private boolean useHeaders = false;
	private String[] headerFields;
	ByteArrayOutputStream bos;
	PrintWriter pw;

	/**
	 * 
	 */
	public CSVReportWriter() {
		bos = new ByteArrayOutputStream();
		Locale.setDefault(new Locale("th_TH"));
	
		try {
			OutputStreamWriter osw = new OutputStreamWriter(bos, "UTF8");
			BufferedWriter bw = new BufferedWriter(osw);
			pw = new PrintWriter(bw);
		} catch (UnsupportedEncodingException e) {
			logCat.error(e);
			pw = new PrintWriter(bos);
		}
		
	}

	/**
	 * @param reportFields
	 * @param dataSource
	 */
	public void fillReport(String[] reportFields, JRDataSource dataSource)
		throws JRException {
		try {
			int i;
			String fields[] = new String[reportFields.length];
			CSVJRField cfields[] = new CSVJRField[reportFields.length];
			for (i = 0; i != reportFields.length; i++) {
				fields[i] = reportFields[i].trim();
				cfields[i] = new CSVJRField(fields[i], fields[i]);
				if (this.useHeaders) {
					// print out the name of the fields as first row's data
					if (i > 0) {
						pw.print(',');
					}
					pw.print(Q + clean(headerFields[i]) + Q);
				}
			}
			if (this.useHeaders) {
				pw.println("");
			}
			while (dataSource.next()) {
				for (i = 0; i != fields.length; i++) {
					Object obj = dataSource.getFieldValue(cfields[i]);
					String s = "" + obj;
					if (i > 0) {
						pw.print(',');
					}
					pw.print(Q + clean(s) + Q);
				}
				pw.println("");
			}
			pw.flush();
			pw.close();
		} catch (JRException e) {
			logCat.error("e");
			throw e;
		}
	}

	/**
	 * @param s
	 * @return
	 */
	private String clean(String s) {
		s = s.replaceAll("\"", "\\\"");
		return s;
	}

	/**
	 * @param hFields
	 */
	public void setHeaders(String[] hFields) {
		headerFields = new String[hFields.length];
		System.arraycopy(hFields, 0, headerFields, 0, hFields.length);
		useHeaders = true;
	}

	/**
	 * @return
	 */
	public ByteArrayOutputStream getOutput() {
		return this.bos;
	}
}
