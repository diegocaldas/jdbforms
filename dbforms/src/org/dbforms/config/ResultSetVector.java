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

import org.dbforms.util.Escaper;
import org.dbforms.util.Util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;



/**
 * <p>
 * In version 0.5, this class held the actual data of a ResultSet (SELECT from
 * a table). The main weakness of this class was that it used too much memory
 * and processor time:
 *
 * <ul>
 * <li>
 * 1. every piece of data gots stored in an array (having a table with a
 * million datasets will mean running into trouble because all the memory gets
 * allocated at one time.)
 * </li>
 * <li>
 * 2. every piece of data gots converted into a String and trim()ed
 * </li>
 * </ul>
 * </p>
 *
 * <p>
 * since version 0.7 DbForms queries only those record from the database the
 * user really wants to see. this way you can query from a table with millions
 * of records and you will still have no memory problems, [exception: you
 * choose count="" in DbForms-tag :=) -> see org.dbforms.taglib.DbFormTag]
 * </p>
 *
 * @author Joe Peer
 */
public class ResultSetVector {
   private static Log logCat = LogFactory.getLog(ResultSetVector.class.getName());
   private Hashtable  selectFieldsHashtable;
   private Vector     objectVector = new Vector();
   private Vector     selectFields;
   private Vector     stringVector = new Vector();
   private boolean    firstPage    = false;
   private boolean    lastPage     = false;

   // logging category for this class
   private int pointer = 0;

   /**
    * Creates a new ResultSetVector object.
    */
   public ResultSetVector() {
   }


   /**
    * Creates a new ResultSetVector object with the given FieldList
    *
    * @param selectFields The FieldList to use
    */
   public ResultSetVector(Vector selectFields) {
      setupSelectFieldsHashtable(selectFields);
   }


   /**
    * Creates a new ResultSetVector object.
    *
    * @param escaper DOCUMENT ME!
    * @param rs DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public ResultSetVector(Escaper   escaper,
                          ResultSet rs) throws SQLException {
      setupData(escaper, rs);
   }


   /**
    * Creates a new ResultSetVector object with the given FieldList and DataSet
    * .
    *
    * @param selectFields The FieldList to use
    * @param rs data to fill the ResutlSetVector with
    *
    * @throws java.sql.SQLException thrown exception
    */
   public ResultSetVector(Vector    selectFields,
                          ResultSet rs) throws java.sql.SQLException {
      setupSelectFieldsHashtable(selectFields);
      setupData(null, rs);
   }

   /**
    * Checks if the given ResultSetVector is null
    *
    * @param rsv ResultSetVector to check
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
   public boolean isCurrentPointerLegal() {
      return ((pointer >= 0) && (pointer < size()));
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String[] getCurrentRow() {
      if (isPointerLegal(pointer)) {
         return (String[]) stringVector.elementAt(pointer);
      } else {
         return null;
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws IllegalArgumentException DOCUMENT ME!
    */
   public FieldValues getCurrentRowAsFieldValues() {
      if (selectFields == null) {
         throw new IllegalArgumentException("no field vector was provided to this result");
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
    * @throws IllegalArgumentException DOCUMENT ME!
    */
   public Map getCurrentRowAsMap() {
      if (selectFields == null) {
         throw new IllegalArgumentException("no field vector was provided to this result");
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
    * DOCUMENT ME!
    *
    * @param i DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getField(int i) {
      if (isPointerLegal(pointer)) {
         try {
            return ((String[]) stringVector.elementAt(pointer))[i];
         } catch (Exception e) {
            return null;
         }
      } else {
         return null;
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param fieldName DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getField(String fieldName) {
      if (Util.isNull(fieldName)) {
         return null;
      }

      Field f = (Field) selectFieldsHashtable.get(fieldName);

      if (f == null) {
         return null;
      }

      int fieldIndex = selectFields.indexOf(f);

      return getField(fieldIndex);
   }


   /**
    * DOCUMENT ME!
    *
    * @param i DOCUMENT ME!
    *
    * @return DOCUMENT ME!
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
    * DOCUMENT ME!
    *
    * @param fieldName DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public Object getFieldAsObject(String fieldName) {
      if (Util.isNull(fieldName)) {
         return null;
      }

      Field f = (Field) selectFieldsHashtable.get(fieldName);

      if (f == null) {
         return null;
      }

      int fieldIndex = selectFields.indexOf(f);

      return getFieldAsObject(fieldIndex);
   }


   /**
    * DOCUMENT ME!
    *
    * @param fieldName DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public Field getFieldDescription(String fieldName) {
      if (isPointerLegal(pointer)) {
         return (Field) selectFieldsHashtable.get(fieldName);
      } else {
         return null;
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param b value to set
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
    * @param b value to set
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
    * DOCUMENT ME!
    *
    * @param pointer DOCUMENT ME!
    */
   public void setPointer(int pointer) {
      this.pointer = pointer;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public int getPointer() {
      return pointer;
   }


   /**
    * DOCUMENT ME!
    *
    * @param p DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public boolean isPointerLegal(int p) {
      return ((p >= 0) && (p < size()));
   }


   /**
    * adds a row to the ResultSetVector
    *
    * @param objectRow row to add
    */
   public void addRow(Object[] objectRow) {
      if (objectRow != null) {
         int      columns   = objectRow.length;
         String[] stringRow = new String[columns];

         for (int i = 0; i < columns; i++) {
            if (objectRow[i] != null) {
               stringRow[i] = objectRow[i].toString();
            } else {
               stringRow[i] = "";
            }
         }

         stringVector.addElement(stringRow);
         objectVector.addElement(objectRow);
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public int declinePointer() {
      pointer--;

      if (pointer >= 0) {
         return pointer;
      } else {
         return -1;
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param stepWidth DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public int declinePointerBy(int stepWidth) {
      pointer -= stepWidth;

      if (pointer >= 0) {
         return pointer;
      } else {
         pointer = this.size() - 1;

         return -1;
      }
   }


   /**
    * DOCUMENT ME!
    */
   public void flip() {
      int vSize = this.size();

      if (vSize > 1) {
         logCat.info("flipping " + vSize + " elements!");

         for (int i = 1; i < vSize; i++) {
            Object o = stringVector.elementAt(i);

            //logCat.debug("o="+o);
            stringVector.remove(i);
            stringVector.insertElementAt(o, 0);

            // we must flip the duplicate vector, too
            o = objectVector.elementAt(i);
            objectVector.remove(i);
            objectVector.insertElementAt(o, 0);
         }
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public int increasePointer() {
      pointer++;

      if (pointer < this.size()) {
         return pointer;
      } else {
         return -1;
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param stepWidth DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public int increasePointerBy(int stepWidth) {
      pointer += stepWidth;

      if (pointer < this.size()) {
         return pointer;
      } else {
         pointer = this.size() - 1;

         return -1;
      }
   }


   /**
    * implements size()
    *
    * @return the sizeof the vector
    */
   public int size() {
      return stringVector.size();
   }


   private void setupData(Escaper   escaper,
                          ResultSet rs) throws SQLException {
      ResultSetMetaData rsmd    = rs.getMetaData();
      int               columns = rsmd.getColumnCount();

      try { // #JP Jun 27, 2001

         while (rs.next()) {
            Object[] objectRow = new Object[columns];

            for (int i = 0; i < columns; i++) {
               if (escaper == null) {
                  Field curField = (Field) selectFields.elementAt(i);

                  if (curField != null) {
                     escaper = curField.getEscaper();
                  }
               }

               if (escaper == null) {
                  try {
                     escaper = DbFormsConfigRegistry.instance()
                                                    .lookup()
                                                    .getEscaper();
                  } catch (Exception e) {
                     logCat.error("cannot create the new default escaper", e);
                  }
               }

               objectRow[i] = JDBCDataHelper.getData(rs, escaper, i + 1);
            }

            addRow(objectRow);
         }
      } finally {
         rs.close();
      }
   }


   private void setupSelectFieldsHashtable(Vector selectFields) {
      this.selectFields = selectFields;

      if (this.selectFields == null) {
         logCat.warn("selectField is null");

         return;
      }

      selectFieldsHashtable = new Hashtable();

      for (int i = 0; i < selectFields.size(); i++) {
         Field f = (Field) selectFields.elementAt(i);
         selectFieldsHashtable.put(f.getName(), f);
      }
   }
}
