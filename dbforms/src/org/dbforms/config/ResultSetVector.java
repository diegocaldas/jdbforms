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
package org.dbforms.config;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.util.IEscaper;
import org.dbforms.util.Util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;



/**
 * <p>
 * In version 0.5, this class held the actual data of a ResultSet (SELECT from a
 * table). The main weakness of this class was that it used too much memory and
 * processor time:
 *
 * <ul>
 * <li>1. every piece of data gots stored in an array (having a table with a
 * million datasets will mean running into trouble because all the memory gets
 * allocated at one time.)</li>
 * <li>2. every piece of data gots converted into a String and trim()ed</li>
 * </ul>
 * </p>
 *
 * <p>
 * since version 0.7 DbForms queries only those record from the database the
 * user really wants to see. this way you can query from a table with millions
 * of records and you will still have no memory problems, [exception: you choose
 * count="" in DbForms-tag :=) -> see org.dbforms.taglib.DbFormTag]
 * </p>
 *
 * @author Joe Peer
 */
public class ResultSetVector {
   private static Log logCat                = LogFactory.getLog(ResultSetVector.class
         .getName());
   private Hashtable  selectFieldsHashtable;
   private Vector     selectFields = new Vector();
   private Vector     objectVector = new Vector();
   private boolean    firstPage    = false;
   private boolean    lastPage     = false;
   private Table      table        = null;
   private Map                attributes = new HashMap();

   // logging category for this class
   private int pointer = 0;

   /**
    * Creates a new, empty ResultSetVector object.
    */
   public ResultSetVector() {
   }


   /**
    * Creates a new ResultSetVector object with the given FieldList
    *
    * @param selectFields
    *            The FieldList to use
    */
   public ResultSetVector(Table table) {
      this.table = table;

      if (table != null) {
         setupSelectFieldsHashtable(table.getFields());
         setupSelectFieldsHashtable(table.getCalcFields());
      }
   }


   /**
    * Creates a new ResultSetVector object.
    *
    * @param table
    *            DOCUMENT ME!
    * @param selectedFields
    *            DOCUMENT ME!
    */
   public ResultSetVector(Table table, Vector selectedFields) {
      this.table = table;
      setupSelectFieldsHashtable(selectedFields);
   }

   /**
    * Checks if the given ResultSetVector is null
    *
    * @param rsv
    *            ResultSetVector to check
    *
    * @return true if ResultSetVector is null
    */
   public static final boolean isNull(ResultSetVector rsv) {
      return ((rsv == null) || (rsv.size() == 0));
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String[] getCurrentRow() {
      Object[] obj = getCurrentRowAsObjects();
      String[] res = null;

      if (obj != null) {
         res = new String[obj.length];

         for (int i = 0; i < obj.length; i++) {
            res[i] = getField(i);
         }
      }

      return res;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws IllegalArgumentException
    *             DOCUMENT ME!
    */
   public FieldValues getCurrentRowAsFieldValues() {
      if (selectFields == null) {
         throw new IllegalArgumentException(
            "no field vector was provided to this result");
      }

      String[] rowData = getCurrentRow();

      if (rowData == null) {
         return null;
      }

      FieldValues fvHT = new FieldValues();

      for (int i = 0; i < selectFields.size(); i++) {
         Field      f  = (Field) selectFields.elementAt(i);
         FieldValue fv = new FieldValue(f, rowData[i]);
         fvHT.put(fv);
      }

      return fvHT;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws IllegalArgumentException
    *             DOCUMENT ME!
    */
   public Map getCurrentRowAsMap() {
      if (selectFields == null) {
         throw new IllegalArgumentException(
            "no field vector was provided to this result");
      }

      String[] rowData = getCurrentRow();

      if (rowData == null) {
         return null;
      }

      Hashtable ht = new Hashtable();

      for (int i = 0; i < selectFields.size(); i++) {
         Field f = (Field) selectFields.elementAt(i);
         ht.put(f.getName(), rowData[i]);
      }

      return ht;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public Object[] getCurrentRowAsObjects() {
      if (isPointerLegal(pointer)) {
         return (Object[]) objectVector.elementAt(pointer);
      } else {
         return null;
      }
   }


   /**
    * returns the fieldValues String representation given by index i
    *
    * @param i Index into the objectArray
    *
    * @return the object
    */
   public String getField(int i) {
      Object obj = getFieldAsObject(i);

      return (obj != null) ? obj.toString() : "";
   }


   /**
    * returns the fieldValues string representation given by it's name
    *
    * @param fieldName name of the field
    *
    * @return the object
    */
   public String getField(String fieldName) {
      int fieldIndex = getFieldIndex(fieldName);

      if (fieldIndex < 0) {
         return null;
      }

      return getField(fieldIndex);
   }


   /**
    * returns the index of a given fieldName
    *
    * @param  fieldName name of the field
    *
    * @return the index of the field in the data arrays
    */
   public int getFieldIndex(String fieldName) {
      int res = -1;

      if (!Util.isNull(fieldName)) {
         Field f = (Field) selectFieldsHashtable.get(fieldName);

         if (f != null) {
            res = selectFields.indexOf(f);
         }
      }

      return res;
   }


   /**
    * returns the fieldValues Object given by index i
    *
    * @param i Index into the objectArray
    *
    * @return the object
    */
   public Object getFieldAsObject(int i) {
      if (isPointerLegal(pointer)) {
         try {
            return ((Object[]) objectVector.elementAt(pointer))[i];
         } catch (Exception e) {
            return null;
         }
      } else {
         return null;
      }
   }


   /**
    * returns the fieldValues Object given by it's name
    *
    * @param fieldName name of the field
    *
    * @return the object
    */
   public Object getFieldAsObject(String fieldName) {
      int fieldIndex = getFieldIndex(fieldName);

      if (fieldIndex < 0) {
         return null;
      }

      return getFieldAsObject(fieldIndex);
   }


   /**
    * gets the Field object to a given fieldName
    *
    * @param fieldName name of the field
    *
    * @return the Field object of the given name
    *         null if not found
    */
   public Field getFieldDescription(String fieldName) {
      return (Field) selectFieldsHashtable.get(fieldName);
   }


   /**
    * DOCUMENT ME!
    *
    * @param b
    *            value to set
    */
   public void setFirstPage(boolean b) {
      firstPage = b;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public boolean isFirstPage() {
      return firstPage;
   }


   /**
    * DOCUMENT ME!
    *
    * @param b
    *            value to set
    */
   public void setLastPage(boolean b) {
      lastPage = b;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public boolean isLastPage() {
      return lastPage;
   }


   /**
    * moves to the first record
    */
   public void moveFirst() {
      this.pointer = 0;
   }


   /**
    * moves to the last record
    */
   public void moveLast() {
      this.pointer = size() - 1;
   }


   /**
    * Return true if the current record is the last record
    *
    * @return true if the last record is reached
    */
   public boolean isLast() {
      return (pointer == (size() - 1));
   }

   /**
    * Return true if the current record is the first record
    *
    * @return true if the first record is reached
    */
   public boolean isFirst() {
      return (pointer == 0);
   }

   private boolean isPointerLegal(int p) {
      return ((p >= 0) && (p < size()));
   }


   /**
    * DOCUMENT ME!
    *
    * @param rs
    *            DOCUMENT ME!
    *
    * @throws SQLException
    *             DOCUMENT ME!
    */
   public void addResultSet(DbEventInterceptorData interceptorData, ResultSet rs)
      throws SQLException {
      ResultSetMetaData rsmd    = rs.getMetaData();
      int               columns = rsmd.getColumnCount();
      IEscaper          escaper = null;

      try { // #JP Jun 27, 2001

         while (rs.next()) {
            Object[] objectRow = new Object[columns];

            for (int i = 0; i < columns; i++) {
               Field curField = (Field) selectFields.elementAt(i);

               if (curField != null) {
                  escaper = curField.getEscaper();
               }

               if (table != null) {
                  escaper = (escaper == null) ? escaper : table.getEscaper();
               }

               if (escaper == null) {
                  try {
                     escaper = DbFormsConfigRegistry.instance().lookup()
                                                    .getEscaper();
                  } catch (Exception e) {
                     logCat.error("cannot create the new default escaper", e);
                  }
               }

               objectRow[i] = JDBCDataHelper.getData(rs, escaper, i + 1);
            }

            addRow(interceptorData, objectRow);
         }
      } finally {
         rs.close();
      }
   }


   /**
    * adds a row to the ResultSetVector
    *
    * @param objectRow
    *            row to add
    */
   public void addRow(DbEventInterceptorData interceptorData, Object[] objectRow) {
      if (objectRow != null) {
         boolean  doit   = true;
         Object[] newRow = new Object[selectFields.size()];

         for (int i = 0; i < objectRow.length; i++) {
            newRow[i] = objectRow[i];
         }

         if ((interceptorData != null) && (interceptorData.getTable() != null)) {
            interceptorData.setAttribute(DbEventInterceptorData.RESULTSET, this);
            interceptorData.setAttribute(DbEventInterceptorData.OBJECTROW,
               newRow);

            int res = DbEventInterceptor.GRANT_OPERATION;

            try {
               res = interceptorData.getTable().processInterceptors(DbEventInterceptor.PRE_ADDROW,
                     interceptorData);
            } catch (SQLException e) {
               ;
            }

            doit = (res == DbEventInterceptor.GRANT_OPERATION);
         }

         if (doit) {
            objectVector.addElement(newRow);

            if ((interceptorData != null)
                     && (interceptorData.getTable() != null)) {
               try {
                  interceptorData.getTable().processInterceptors(DbEventInterceptor.POST_ADDROW,
                     interceptorData);
               } catch (SQLException e) {
                  ;
               }
            }
         }
      }
   }


   /**
    * moves to the previous record
    *
    * @return true if beginning is reached
    */
   public boolean movePrevious() {
      pointer--;

      return (pointer < 0);
   }


   /**
    * DOCUMENT ME!
    */
   public void flip() {
      int vSize = this.size();

      if (vSize > 1) {
         logCat.info("flipping " + vSize + " elements!");

         for (int i = 1; i < vSize; i++) {
            Object o = objectVector.elementAt(i);
            objectVector.remove(i);
            objectVector.insertElementAt(o, 0);
         }
      }
   }


   /**
    * moves to the next record
    *
    * @return true if end is reached
    */
   public boolean moveNext() {
      pointer++;

      return (pointer >= size());
   }


   /**
    * implements size()
    *
    * @return the sizeof the vector
    */
   public int size() {
      return objectVector.size();
   }


   private void setupSelectFieldsHashtable(Vector paramSelectFields) {
      this.selectFields.addAll(paramSelectFields);
      selectFieldsHashtable = new Hashtable();

      for (int i = 0; i < selectFields.size(); i++) {
         Field f = (Field) selectFields.elementAt(i);
         selectFieldsHashtable.put(f.getName(), f);
      }
   }
   
   /**
    * reads a value from the attributes list
    *
    * @param key key to read
    *
    * @return The value. If not found null
    */
   public Object getAttribute(String key) {
      return attributes.get(key);
   }


   /**
    * Stores a value in the attributes list
    *
    * @param key key for the value
    * @param value value to store
    */
   public void setAttribute(String key, Object value) {
      attributes.put(key, value);
   }

}
