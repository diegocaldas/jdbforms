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

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.Calendar;
import java.util.Date;



/**
 * Simple export of data as excel file notes: All strings are exported as
 * UTF-16 Setting Column Headers does not work. I have not figured out how to
 * do it with POI. Help would be appreciated.
 *
 * @author Neal Katz
 */
public class ExcelReportWriter {
   private static Log    logCat       = LogFactory.getLog(ExcelReportWriter.class);
   ByteArrayOutputStream bos;
   HSSFSheet             sheet        = null;
   HSSFWorkbook          wb;
   String                sheetName;
   private String[]      headerFields;

   /**
                                                                                                                            *
                                                                                                                            */
   public ExcelReportWriter() {
      bos = new ByteArrayOutputStream();
      wb  = new HSSFWorkbook();
   }

   /**
    * DOCUMENT ME!
    *
    * @param hFields
    */
   public void setHeaders(String[] hFields) {
      headerFields = new String[hFields.length];
      System.arraycopy(hFields, 0, headerFields, 0, hFields.length);
   }


   /**
    * DOCUMENT ME!
    *
    * @return
    */
   public ByteArrayOutputStream getOutput() {
      return this.bos;
   }


   /**
    * DOCUMENT ME!
    *
    * @param string
    */
   public void setSheetName(String string) {
      sheetName = string;
   }


   /**
    * DOCUMENT ME!
    *
    * @return
    */
   public String getSheetName() {
      return sheetName;
   }


   /**
    * DOCUMENT ME!
    *
    * @param reportFields
    * @param dataSource
    */
   public void fillReport(String[]     reportFields,
                          JRDataSource dataSource)
                   throws JRException, IOException {
      try {
         sheet = wb.createSheet(getSheetName());

         int          i;
         String[]     fields  = new String[reportFields.length];
         CSVJRField[] cfields = new CSVJRField[reportFields.length];

         for (i = 0; i != reportFields.length; i++) {
            fields[i]  = reportFields[i].trim();
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
}
