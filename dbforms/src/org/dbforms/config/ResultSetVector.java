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
import java.util.Vector;
import java.util.Hashtable;
import java.util.Map;
import java.sql.Types;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Clob;
import org.apache.log4j.Category;



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
public class ResultSetVector
{
   private static Category logCat = Category.getInstance(
                                             ResultSetVector.class.getName());

   // logging category for this class
   private int       pointer               = 0;
   private Vector    selectFields;
   private Hashtable selectFieldsHashtable;
   private Vector    stringVector;
   private Vector    objectVector;
   private boolean   firstPage = false;
   private boolean   lastPage  = false;

   /**
    * Creates a new ResultSetVector object.
    */
   public ResultSetVector()
   {
      super();
      objectVector = new Vector();
      stringVector = new Vector();
   }


   /**
    * Creates a new ResultSetVector object.
    * 
    * @param rs data to fill the ResutlSetVector with
    * 
    * @throws java.sql.SQLException thrown exception
    */
   public ResultSetVector(ResultSet rs) throws java.sql.SQLException
   {
      this();

      ResultSetMetaData rsmd    = rs.getMetaData();
      int               columns = rsmd.getColumnCount();

      try
      { // #JP Jun 27, 2001

         while (rs.next())
         {
            Object[] objectRow = new Object[columns];

            for (int i = 0; i < columns; i++)
            {
               if (rs.getMetaData().getColumnType(i + 1) == Types.CLOB)
               {
                  Clob tmpObj = (Clob) rs.getObject(i + 1);

                  if (tmpObj != null)
                  {
                     objectRow[i] = tmpObj.getSubString((long) 1, 
                                                        (int) tmpObj.length());
                  }
                  else
                  {
                     objectRow[i] = null;
                  }
               }
               else
               {
                  Object tmpObj = rs.getObject(i + 1);
                  objectRow[i] = tmpObj;
               }
            }

            addRow(objectRow);
         }
      }
      finally
      {
         rs.close();
      }
   }


   /**
    * Creates a new ResultSetVector object with the given FieldList
    * 
    * @param selectFields The FieldList to use
    */
   public ResultSetVector(Vector selectFields)
   {
      this();
      this.selectFields = selectFields;
      setupSelectFieldsHashtable();
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
   public ResultSetVector(Vector selectFields, ResultSet rs)
                   throws java.sql.SQLException
   {
      this(rs);
      this.selectFields = selectFields;
      setupSelectFieldsHashtable();
   }

   /**
    * Checks if the given ResultSetVector is null
    * 
    * @param rsv ResultSetVector to check
    * 
    * @return true if ResultSetVector is null
    */
   public static final boolean isNull(ResultSetVector rsv)
   {
      return ((rsv == null) || (rsv.size() == 0));
   }


   private void setupSelectFieldsHashtable()
   {
      if (selectFields == null)
      {
         logCat.warn("selectField is null");

         return;
      }

      selectFieldsHashtable = new Hashtable();

      for (int i = 0; i < selectFields.size(); i++)
      {
         Field f = (Field) selectFields.elementAt(i);
         selectFieldsHashtable.put(f.getName(), f);
      }
   }


   /**
    * adds a row to the ResultSetVector
    * 
    * @param objectRow row to add
    */
   public void addRow(Object[] objectRow)
   {
      if (objectRow != null)
      {
         int      columns   = objectRow.length;
         String[] stringRow = new String[columns];

         for (int i = 0; i < columns; i++)
         {
            if (objectRow[i] != null)
            {
               stringRow[i] = objectRow[i].toString();
            }
            else
            {
               stringRow[i] = "";
            }
         }

         stringVector.addElement(stringRow);
         objectVector.addElement(objectRow);
      }
   }


   /**
    * implements size()
    * 
    * @return the sizeof the vector
    */
   public int size()
   {
      return stringVector.size();
   }


   /**
    * DOCUMENT ME!
    * 
    * @param pointer DOCUMENT ME!
    */
   public void setPointer(int pointer)
   {
      this.pointer = pointer;
   }


   /**
    * DOCUMENT ME!
    * 
    * @return DOCUMENT ME!
    */
   public int getPointer()
   {
      return pointer;
   }


   /**
    * DOCUMENT ME!
    * 
    * @return DOCUMENT ME!
    */
   public int increasePointer()
   {
      pointer++;

      if (pointer < this.size())
      {
         return pointer;
      }
      else
      {
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
   public int increasePointerBy(int stepWidth)
   {
      pointer += stepWidth;

      if (pointer < this.size())
      {
         return pointer;
      }
      else
      {
         pointer = this.size() - 1;

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
   public int declinePointerBy(int stepWidth)
   {
      pointer -= stepWidth;

      if (pointer >= 0)
      {
         return pointer;
      }
      else
      {
         pointer = this.size() - 1;

         return -1;
      }
   }


   /**
    * DOCUMENT ME!
    * 
    * @return DOCUMENT ME!
    */
   public int declinePointer()
   {
      pointer--;

      if (pointer >= 0)
      {
         return pointer;
      }
      else
      {
         return -1;
      }
   }


   /**
    * DOCUMENT ME!
    * 
    * @param p DOCUMENT ME!
    * 
    * @return DOCUMENT ME!
    */
   public boolean isPointerLegal(int p)
   {
      return ((p >= 0) && (p < size()));
   }


   /**
    * DOCUMENT ME!
    * 
    * @return DOCUMENT ME!
    */
   public boolean isCurrentPointerLegal()
   {
      return ((pointer >= 0) && (pointer < size()));
   }


   /**
    * DOCUMENT ME!
    * 
    * @return DOCUMENT ME!
    */
   public String[] getCurrentRow()
   {
      if (isPointerLegal(pointer))
      {
         return (String[]) stringVector.elementAt(pointer);
      }
      else
      {
         return null;
      }
   }


   /**
    * DOCUMENT ME!
    * 
    * @return DOCUMENT ME!
    */
   public Object[] getCurrentRowAsObjects()
   {
      if (isPointerLegal(pointer))
      {
         return (Object[]) objectVector.elementAt(pointer);
      }
      else
      {
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
   public String getField(int i)
   {
      if (isPointerLegal(pointer))
      {
         try
         {
            return ((String[]) stringVector.elementAt(pointer))[i];
         }
         catch (Exception e)
         {
            return null;
         }
      }
      else
      {
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
   public String getField(String fieldName)
   {
      Field f = (Field) selectFieldsHashtable.get(fieldName);

      if (f == null)
      {
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
   public Object getFieldAsObject(int i)
   {
      if (isPointerLegal(pointer))
      {
         try
         {
            return ((Object[]) objectVector.elementAt(pointer))[i];
         }
         catch (Exception e)
         {
            return null;
         }
      }
      else
      {
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
   public Object getFieldAsObject(String fieldName)
   {
      Field f = (Field) selectFieldsHashtable.get(fieldName);

      if (f == null)
      {
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
   public Field getFieldDescription(String fieldName)
   {
      if (isPointerLegal(pointer))
      {
         return (Field) selectFieldsHashtable.get(fieldName);
      }
      else
      {
         return null;
      }
   }


   /**
    * DOCUMENT ME!
    */
   public void flip()
   {
      int vSize = this.size();

      if (vSize > 1)
      {
         logCat.info("flipping " + vSize + " elements!");

         for (int i = 1; i < vSize; i++)
         {
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
    * 
    * @throws IllegalArgumentException DOCUMENT ME!
    */
   public Map getCurrentRowAsMap()
   {
      if (selectFields == null)
      {
         throw new IllegalArgumentException(
                  "no field vector was provided to this result");
      }

      String[] rowData = getCurrentRow();

      if (rowData == null)
      {
         return null;
      }

      Hashtable ht = new Hashtable();

      for (int i = 0; i < selectFields.size(); i++)
      {
         Field f = (Field) selectFields.elementAt(i);
         ht.put(f.getName(), rowData[i]);
      }

      return ht;
   }


   /**
    * DOCUMENT ME!
    * 
    * @return DOCUMENT ME!
    * 
    * @throws IllegalArgumentException DOCUMENT ME!
    */
   public FieldValues getCurrentRowAsFieldValues()
   {
      if (selectFields == null)
      {
         throw new IllegalArgumentException(
                  "no field vector was provided to this result");
      }

      String[] rowData = getCurrentRow();

      if (rowData == null)
      {
         return null;
      }

      FieldValues fvHT = new FieldValues();

      for (int i = 0; i < selectFields.size(); i++)
      {
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
    */
   public boolean isFirstPage()
   {
      return firstPage;
   }


   /**
    * DOCUMENT ME!
    * 
    * @return DOCUMENT ME!
    */
   public boolean isLastPage()
   {
      return lastPage;
   }


   /**
    * DOCUMENT ME!
    * 
    * @param b value to set
    */
   public void setFirstPage(boolean b)
   {
      firstPage = b;
   }


   /**
    * DOCUMENT ME!
    * 
    * @param b value to set
    */
   public void setLastPage(boolean b)
   {
      lastPage = b;
   }
}