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

package org.dbforms.event.datalist.dao;
import org.apache.log4j.Category;
import java.util.Vector;
import java.sql.SQLException;

import java.net.URI;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.OutputStream;

import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMWriter;

import org.apache.xpath.domapi.XPathEvaluatorImpl;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;



import org.dbforms.config.Constants;
import org.dbforms.config.DbFormsConfigRegistry;
import org.dbforms.config.Field;
import org.dbforms.config.FieldValue;
import org.dbforms.config.Table;
import org.dbforms.util.Util;

/**
 * Special implementation of DataSource. This class deals with xml data
 * 
 * @author hkk
 */
public class DataSourceXML extends DataSource {
   private Category logCat = Category.getInstance(this.getClass().getName());
   private FieldValue[] filterConstraint;
   private FieldValue[] orderConstraint;
   private FieldValue[] sqlFilterParams;
   private String sqlFilter;
   private XMLDataResult data;
   private String[] keys;
   private Object[][] dataObject;

	private static DocumentBuilderFactory dfactory         = null;
	private static DocumentBuilder        builder          = null;
	private static XPathEvaluatorImpl     evaluator        = null;  
	private static DOMWriter 			  writer           = null;

   /**
    * Contructor
    * 
    * @param table to set
    */
   public DataSourceXML(Table table) {
      super(table);
      if (dfactory == null) {
         try {
            dfactory = DocumentBuilderFactory.newInstance();
            dfactory.setValidating(false);
            dfactory.setNamespaceAware(false);
            builder = dfactory.newDocumentBuilder();
            evaluator = new XPathEvaluatorImpl();
		    writer = new org.apache.xml.serialize.DOMWriterImpl();
			writer.setEncoding("ISO-8859-1");
			writer.setNewLine("CR-LF");
         } catch (Exception e) {
            logCat.error(e);
         }
      }
   }

   /**
    * Set the filterConstraint and orderConstraint used to build the SQL Select
    * condition.
    * 
    * @param filterConstraint FieldValue array used to build a cumulation of
    *        rules for filtering fields.
    * @param orderConstraint  FieldValue array used to build a cumulation of
    *        rules for ordering (sorting) and restricting fields.
    * @param sqlFilter       sql condition to add to where clause
    * @param sqlFilterParams list of FieldValues to fill the sqlFilter with
    */
   public void setSelect(
      FieldValue[] filterConstraint,
      FieldValue[] orderConstraint,
      String sqlFilter,
      FieldValue[] sqlFilterParams) {
      this.filterConstraint = filterConstraint;
      this.orderConstraint = orderConstraint;
      this.sqlFilter = sqlFilter;
      this.sqlFilterParams = sqlFilterParams;
   }

   /**
    * Will be called to open all datasets
    * 
    * @throws SQLException
    */
   protected final void open() throws SQLException {
      try {
         URI url = getURI();
         data =
            new XMLDataResult(evaluator, read(url), url.getQuery());
      } catch (Exception e) {
         logCat.error(e);
      }
      keys = new String[size()];
      dataObject = new Object[size()][];
   }

   /**
    * should close all open datasets
    */
   protected final void close() {
      if (data.hasChanged()) {
         try {
			URI url = getURI();
            write(url, data.getDocument());
         } catch (Exception e) {
            logCat.error(e);
         }
      }
   }

   private URI getURI() throws Exception {
      return getURI(getXPath());
   }   	
   
   private URI getURI(String qry) {
      URI url = null;

      // Check if we got a full URI
      try {
         url = new URI(qry);
      } catch (Exception e) {
         logCat.error(e);
      }

      // No valid URI given, put query into to query part of the 
      // URI object
      if (url == null) {
         try {
            url = new URI(null, null, null, qry, null);
         } catch (Exception e) {
            logCat.error(e);
         }
      }
      return url;
   }

   /**
    * Must return the size of the whole resultset with all data fetch
    * 
    * @return size of whole resultset
    * 
    * @throws SQLException
    */
   protected final int size() throws SQLException {
      int res = 0;

      if (data != null) {
         res = data.size();
      }

      return res;
   }

   /**
    * return true if there are more records to fetch then the given record
    * number
    * 
    * @param i index of last fetched row.
    * 
    * @return true if there are more records to fetch then the given record
    *         number
    * 
    * @throws SQLException
    */
   protected boolean hasMore(int i) throws SQLException {
      return (i < size());
   }

   /**
    * maps the startRow to the internal index
    * 
    * @param startRow  keyValueStr to the row<br>
    *        key format: FieldID ":" Length ":" Value<br>
    *        example: if key id = 121 and field id=2 then keyValueStr contains "2:3:121"<br>
    *        If the key consists of more than one fields, the key values  are
    *        seperated through "-"<br>
    *        example: value of field 1=12, value of field 3=1992, then we'll
    *        get "1:2:12-3:4:1992"
    * 
    * @return the index of the row, 0 as first row if not found
    * 
    * @throws SQLException
    */
   protected final int findStartRow(String startRow) throws SQLException {
      for (int i = 0; i < size(); i++) {
         if (getKey(i).equals(startRow)) {
            return i;
         }
      }

      return 0;
   }

   /**
    * should retrieve the row at an special index as an Object[]
    * 
    * @param currRow index of row to fetch
    * 
    * @return Object[] of the fetched row
    * 
    * @throws SQLException
    */
   protected final Object[] getRow(int currRow) throws SQLException {
      return getValuesAsObject(currRow);
   }

   /**
    * gets the document from the remote system.
    * 
    * @param uri the uri to query
    * 
    * @return NODE the result
    * 
    * @throws Exception Exception during processing IO
    */
	protected Document read(URI uri) throws Exception
	{
		URLConnection con = uri.toURL().openConnection();
		con.connect();
		InputStream in  = con.getInputStream();
		InputSource src = new InputSource(in);
		Document    doc = builder.parse(src);
		return doc;
	}


   /**
    * saves the document to the remote system.
    * 
    * @throws Exception Exception during processing IO
    */
	protected void write(URI uri, Document doc) throws Exception
	{
		URLConnection con = uri.toURL().openConnection();
		con.connect();
		OutputStream out  = con.getOutputStream();
		writer.writeNode(out, doc);
	}


   private String insertParamsInSqlFilter() {
      /** substitute ? with corresponding value in list */
      int p1 = 0;
      int p2 = sqlFilter.indexOf('?', p1);
      StringBuffer buf = new StringBuffer();
      int cnt = 0;

      while (p2 > -1) {
         // add the string before the next ?
         buf.append(sqlFilter.substring(p1, p2));

         // if values are exausted, then abort
         if (cnt >= sqlFilterParams.length) {
            logCat.error("reference to a missing filterValue in " + sqlFilter);

            return null;
         }

         // retrieve value
         String value = (String) sqlFilterParams[cnt].getFieldValue();

         if (!Util.isNull(value)) {
            // add value to string gbuffer
            buf.append("\"");
            buf.append(value);
            buf.append("\"");
         }

         // restart search from next char after ? 
         p1 = p2 + 1;
         p2 = sqlFilter.indexOf('?', p1);
         cnt++;
      }

      // add remaining part of string
      buf.append(sqlFilter.substring(p1));

      return buf.toString();
   }

   private String parseFilterConstraint() {
      StringBuffer buf = new StringBuffer();

      if (!FieldValue.isNull(filterConstraint)) {
         for (int i = 0; i < filterConstraint.length; i++) {
            if (i != 0) {
               if (filterConstraint[i].isLogicalOR()) {
                  buf.append(" or ");
               } else {
                  buf.append(" and ");
               }
            }

            Field f = filterConstraint[i].getField();
            buf.append(
               Util.isNull(f.getExpression())
                  ? f.getName()
                  : f.getExpression());

            // Check what type of operator is required
            switch (filterConstraint[i].getOperator()) {
               case Constants.FILTER_EQUAL :
                  buf.append("=");

                  break;

               case Constants.FILTER_NOT_EQUAL :
                  buf.append("!=");

                  break;

               case Constants.FILTER_GREATER_THEN :
                  buf.append("&gt;");

                  break;

               case Constants.FILTER_SMALLER_THEN :
                  buf.append("&lt;");

                  break;

               case Constants.FILTER_GREATER_THEN_EQUAL :
                  buf.append("&gt;=");

                  break;

               case Constants.FILTER_SMALLER_THEN_EQUAL :
                  buf.append("&lt;=");

                  break;
            }

            buf.append("\"");
            buf.append(filterConstraint[i].getFieldValue());
            buf.append("\"");
         }
      }

      return buf.toString();
   }

   private String getFilePath() throws Exception {
      return Util.replaceRealPath(
         getTable().getAlias(),
         DbFormsConfigRegistry.instance().lookup().getRealPath());
   }

   private String getXPath() throws Exception {
      StringBuffer buf = new StringBuffer();
      buf.append(getFilePath());

      String filter = parseFilterConstraint();
      String sqlFilter = insertParamsInSqlFilter();

      if (!Util.isNull(filter) || !Util.isNull(sqlFilter)) {
         buf.append("[");
         buf.append(filter);

         if (!Util.isNull(filter)) {
            buf.append(" and ");
         }

         buf.append(sqlFilter);
         buf.append("]");
      }

      return buf.toString();
   }

   private String getKey(int index) throws SQLException {
      if (keys[index] == null) {
         Vector fields = getTable().getFields();
         String[] objectRow = new String[fields.size()];

         for (int i = 0; i < fields.size(); i++) {
            Field f = (Field) fields.elementAt(i);
            objectRow[i] =
               data.getString(
                  index,
                  Util.isNull(f.getExpression())
                     ? f.getName()
                     : f.getExpression(),
                  f.getType());
         }

         keys[index] = getTable().getKeyPositionString(objectRow);
      }

      return keys[index];
   }

   private Object[] getValuesAsObject(int index) throws SQLException {
      if ((index < 0) || (index >= dataObject.length)) {
         return null;
      }

      if (dataObject[index] == null) {
         Vector fields = getTable().getFields();
         Object[] objectRow = new Object[fields.size()];

         for (int i = 0; i < fields.size(); i++) {
            Field f = (Field) fields.elementAt(i);
            objectRow[i] =
               data.getItemValue(
                  index,
                  Util.isNull(f.getExpression())
                     ? f.getName()
                     : f.getExpression(),
                  f.getType());
         }

         dataObject[index] = objectRow;
      }

      return dataObject[index];
   }
}