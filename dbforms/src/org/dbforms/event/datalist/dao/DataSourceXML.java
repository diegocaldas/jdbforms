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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.config.Constants;
import org.dbforms.config.DbFormsConfigRegistry;
import org.dbforms.config.Field;
import org.dbforms.config.FieldValue;
import org.dbforms.config.FieldValues;
import org.dbforms.config.Table;

import org.dbforms.dom.DOMFactory;

import org.dbforms.util.Util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.net.URL;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;



/**
 * Special implementation of DataSource. This class deals with xml data
 *
 * @author hkk
 */
public class DataSourceXML extends DataSource {
   private Hashtable     keys;
   private Log           logCat           = LogFactory.getLog(this.getClass().getName());
   private String        sqlFilter;
   private XMLDataResult data;
   private Object[][]    dataObject       = null;
   private FieldValue[]  filterConstraint;
   private FieldValue[]  orderConstraint;
   private FieldValue[]  sqlFilterParams;

   /**
    * Contructor
    *
    * @param table to set
    */
   public DataSourceXML(Table table) {
      super(table);
   }

   /**
    * Set the filterConstraint and orderConstraint used to build the SQL Select
    * condition.
    *
    * @param filterConstraint FieldValue array used to build a cumulation of
    *        rules for filtering fields.
    * @param orderConstraint FieldValue array used to build a cumulation of
    *        rules for ordering (sorting) and restricting fields.
    * @param sqlFilter sql condition to add to where clause
    * @param sqlFilterParams list of FieldValues to fill the sqlFilter with
    */
   public void setSelect(FieldValue[] filterConstraint,
                         FieldValue[] orderConstraint,
                         String       sqlFilter,
                         FieldValue[] sqlFilterParams) {
      this.filterConstraint = filterConstraint;
      this.orderConstraint  = orderConstraint;
      this.sqlFilter        = sqlFilter;
      this.sqlFilterParams  = sqlFilterParams;
   }


   /**
    * performs an update into the DataSource
    *
    * @param con DOCUMENT ME!
    * @param fieldValues FieldValues to update
    * @param keyValuesStr keyValueStr to the row to update<br>
    *        key format: FieldID ":" Length ":" Value<br>
    *        example: if key id = 121 and field id=2 then keyValueStr contains "2:3:121"<br>
    *        If the key consists of more than one fields, the key values are
    *        seperated through "-"<br>
    *        example: value of field 1=12, value of field 3=1992, then we'll
    *        get "1:2:12-3:4:1992"
    *
    * @throws SQLException if any error occurs
    */
   public void doUpdate(Connection  con,
                        FieldValues fieldValues,
                        String      keyValuesStr) throws SQLException {
      Integer row = (Integer) keys.get(keyValuesStr);

      if (row != null) {
         int      r    = row.intValue();
         Iterator iter = fieldValues.elements();

         while (iter.hasNext()) {
            FieldValue fv = (FieldValue) iter.next();
            Field      f = fv.getField();
            data.setItemValue(r,
                              Util.isNull(f.getExpression()) ? f.getName()
                                                             : f.getExpression(),
                              f.getType(), fv.getFieldValueAsObject());
         }

         dataObject[r] = null;
      }
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
      if ((currRow < 0) || (currRow >= size())) {
         return null;
      }

      if (dataObject[currRow] == null) {
         Vector   fields = getTable()
                              .getFields();
         Object[] objectRow = new Object[fields.size()];
         String[] stringRow = new String[fields.size()];

         for (int i = 0; i < fields.size(); i++) {
            Field f = (Field) fields.elementAt(i);
            objectRow[i] = data.getItemValue(currRow,
                                             Util.isNull(f.getExpression())
                                             ? f.getName()
                                             : f.getExpression(), f.getType());
            stringRow[i] = (objectRow[i] != null) ? objectRow[i].toString()
                                                  : null;
         }

         String key = getTable()
                         .getKeyPositionString(stringRow);
         keys.put(key, new Integer(currRow));
         dataObject[currRow] = objectRow;
      }

      return dataObject[currRow];
   }


   /**
    * should close all open datasets
    */
   protected final void close() {
      if ((data != null) && data.hasChanged()) {
         try {
            String url = getFilePath() + getQuery();
            write(url, data.getRoot());
         } catch (Exception e) {
            logCat.error(e);
         }
      }

      if (keys != null) {
         keys.clear();
      }

      dataObject = null;
   }


   /**
    * maps the startRow to the internal index
    *
    * @param startRow keyValueStr to the row<br>
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
      Integer res = null;

      if (!Util.isNull(startRow)) {
         res = (Integer) keys.get(startRow);
      }

      return (res != null) ? res.intValue()
                           : 0;
   }


   /**
    * Will be called to open all datasets
    *
    * @throws SQLException
    */
   protected final void open() throws SQLException {
      if (dataObject == null) {
         try {
            String qry = getQuery();
            String url = getFilePath() + qry;

            try {
               URL u = new URL(url);
               qry = u.getQuery();
            } catch (Exception e) {
               logCat.info("open", e);
            }

            Document doc = read(url);

            if (doc != null) {
               Element elem = doc.getDocumentElement();
               data = new XMLDataResult(elem, qry);
            }
         } catch (Exception e) {
            logCat.error("open", e);
            throw new SQLException(e.getMessage());
         }

         keys       = new Hashtable();
         dataObject = new Object[size()][];
      }
   }


   /**
    * Must return the size of the whole resultset with all data fetch
    *
    * @return size of whole resultset
    *
    * @throws SQLException
    */
   protected final int size() throws SQLException {
      return (data != null) ? data.size()
                            : 0;
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
    * gets the document from the remote system.
    *
    * @param url the uri to query
    *
    * @return NODE the result
    *
    * @throws Exception Exception during processing IO
    */
   protected Document read(String url) throws Exception {
      return DOMFactory.instance()
                       .read(url);
   }


   /**
    * saves the document to the remote system.
    *
    * @param url DOCUMENT ME!
    * @param root DOCUMENT ME!
    *
    * @throws Exception Exception during processing IO
    */
   protected void write(String  url,
                        Element root) throws Exception {
      DOMFactory.instance()
                .write(url, root);
   }


   private String getFilePath() throws Exception {
      return Util.replaceRealPath(getTable().getAlias(),
                                  DbFormsConfigRegistry.instance().lookup().getRealPath());
   }


   private String getQuery() throws SQLException {
      StringBuffer buf = new StringBuffer();

      String       filter    = getWhereClause();
      String       sqlFilter = getSQLFilter();

      if (!Util.isNull(filter) || !Util.isNull(sqlFilter)) {
         buf.append("[");
         buf.append(filter);

         if (!Util.isNull(sqlFilter)) {
            if (!Util.isNull(filter)) {
               buf.append(" and ");
            }

            buf.append(sqlFilter);
         }

         buf.append("]");
      }

      return buf.toString();
   }


   private String getSQLFilter() {
      /** substitute ? with corresponding value in list */
      int          p1  = 0;
      int          p2  = sqlFilter.indexOf('?', p1);
      StringBuffer buf = new StringBuffer();
      int          cnt = 0;

      while (p2 > -1) {
         // add the string before the next ?
         buf.append(sqlFilter.substring(p1, p2));

         // if values are exausted, then abort
         if (cnt >= sqlFilterParams.length) {
            logCat.error("reference to a missing filterValue in " + sqlFilter);

            return null;
         }

         // retrieve value
         String value = sqlFilterParams[cnt].getFieldValue();

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


   private String getWhereClause() throws SQLException {
      StringBuffer buf = new StringBuffer();

      if (!FieldValue.isNull(filterConstraint)) {
         for (int i = 0; i < filterConstraint.length; i++) {
            if (i != 0) {
               if (filterConstraint[i].getLogicalOR()) {
                  buf.append(" or ");
               } else {
                  buf.append(" and ");
               }
            }

            Field f = filterConstraint[i].getField();
            buf.append(Util.isNull(f.getExpression()) ? f.getName()
                                                      : f.getExpression());

            // Check what type of operator is required
            switch (filterConstraint[i].getOperator()) {
               case Constants.FILTER_EQUAL:
                  buf.append("=");

                  break;

               case Constants.FILTER_NOT_EQUAL:
                  buf.append("!=");

                  break;

               case Constants.FILTER_GREATER_THEN:
                  buf.append("&gt;");

                  break;

               case Constants.FILTER_SMALLER_THEN:
                  buf.append("&lt;");

                  break;

               case Constants.FILTER_GREATER_THEN_EQUAL:
                  buf.append("&gt;=");

                  break;

               case Constants.FILTER_SMALLER_THEN_EQUAL:
                  buf.append("&lt;=");

                  break;
            }

            buf.append("\"");
            buf.append(filterConstraint[i].getFieldValueAsObject().toString());
            buf.append("\"");
         }
      }

      return buf.toString();
   }
}
