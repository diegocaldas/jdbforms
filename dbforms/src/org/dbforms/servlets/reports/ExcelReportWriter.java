/*
 * Created on May 27, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.dbforms.servlets.reports;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Category;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import dori.jasper.engine.JRDataSource;
import dori.jasper.engine.JRException;

/** Simple export of data as excel file
 * notes:
 * All strings are exported as UTF-16
 * Setting Column Headers does not work. I have not figured out
 * how to do it with POI. Help would be appreciated.
 * @author Neal Katz
 *
 */
public class ExcelReportWriter {
	private static Category logCat =
		Category.getInstance(ExcelReportWriter.class);

	private boolean useHeaders = false;
	private String[] headerFields;
	ByteArrayOutputStream bos;
	HSSFWorkbook wb;
	HSSFSheet sheet = null;
	String sheetName;

	/**
	 * 
	 */
	public ExcelReportWriter() {
		bos = new ByteArrayOutputStream();
		wb = new HSSFWorkbook();
	}

	/**
	 * @param reportFields
	 * @param dataSource
	 */
	public void fillReport(String[] reportFields, JRDataSource dataSource)
		throws JRException, IOException {
		try {
			sheet = wb.createSheet(getSheetName());
			int i;
			String fields[] = new String[reportFields.length];
			CSVJRField cfields[] = new CSVJRField[reportFields.length];
			for (i = 0; i != reportFields.length; i++) {
				fields[i] = reportFields[i].trim();
				cfields[i] = new CSVJRField(fields[i], fields[i]);
				//sheet.setDisplayRowColHeadings(this.useHeaders);
				//				if (this.useHeaders) {
				//					// print out the name of the fields as first row's data
				//					if (i > 0) {
				//						pw.print(',');
				//					}
				//					pw.print(Q + clean(headerFields[i]) + Q);
				//				}
			}
			int rowCnt = 0;
			//			if (useHeaders) {
			//				sheet.setDisplayRowColHeadings(true);
			//				HSSFRow row = sheet.createRow((short) rowCnt++);
			//				for (i = 0; i != headerFields.length; i++) {					
			//					HSSFCell cell = row.createCell((short) i);
			//					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			//					cell.setCellValue(headerFields[i]);
			//				}
			//			}
			while (dataSource.next()) {
				HSSFRow row = sheet.createRow((short) rowCnt++);
				for (i = 0; i != fields.length; i++) {
					Object obj = dataSource.getFieldValue(cfields[i]);
					if (obj != null) {
						// for null values we just skip the cell
						HSSFCell cell = row.createCell((short) i);
						if (obj instanceof Number) {
							cell.setCellValue(((Number) obj).doubleValue());
						} else if (obj instanceof Date) {
							cell.setCellValue((Date) obj);
						} else if (obj instanceof Calendar) {
							cell.setCellValue((Calendar) obj);
						} else {
							cell.setEncoding(HSSFCell.ENCODING_UTF_16);
							cell.setCellValue(obj.toString());
						}
					}
				}
			}
			wb.write(bos);

		} catch (JRException e) {
			logCat.error("e");
			throw e;
		} catch (IOException e) {
			logCat.error("e");
			throw e;
		}
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
	/**
	 * @return
	 */
	public String getSheetName() {
		return sheetName;
	}

	/**
	 * @param string
	 */
	public void setSheetName(String string) {
		sheetName = string;
	}

}
