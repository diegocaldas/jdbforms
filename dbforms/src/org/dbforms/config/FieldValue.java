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

import java.util.Locale;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;

import org.apache.log4j.Category;

import org.dbforms.util.UniqueIDGenerator;
import org.dbforms.util.TimeUtil;
import org.dbforms.util.Util;

// imports for return objects
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Time;
import org.dbforms.util.FileHolder;

/**
 * This helper-class was originally used to maintain the mapped values between
 * Main-Form and Sub-Form in the taglib package. in meantime it is used as
 * holder of data in many places. <br>
 * It also performs operations that involve "fields" and associated "values"
 * (i.e. building blocks of SQL SELECT statements, etc)
 * 
 * Now it's handles all the stuf of parsing the resended strings back into java objects.
 * 
 * 
 * @author Joe Peer
 * @author Philip Grunikiewicz
 * @author Henner Kollmann
 */
public class FieldValue implements Cloneable {
   /** logging category for this class */
   private Category logCat = Category.getInstance(this.getClass().getName());

   /** field object */
   private Field field;

   /** a value a field is associated with */
   private String fieldValue;

   /** old value */
   private String oldValue;

   /**
    * Field sor direction. Can be Field.ORDER_ASCENDING or
    * Field.ORDER_DESCENDING.
    */
   private boolean sortDirection = Constants.ORDER_ASCENDING;

   /**
    * If FieldValue is used in a "childFieldValue", it signalizes if it should
    * be rendered as hidden tag or not (if "stroke out")
    */
   private boolean renderHiddenHtmlTag = true;

   /** if used in an argument for searching: AND || OR! */
   private int searchMode;

   /** if used in an argument for searching: SHARP || WEAK! */
   private int searchAlgorithm;

   /**
    * if used in a filter argument: the type of filter comparison operator (see
    * FILTER_ definitions above)
    */
   private int operator;

   /** specifies whether to OR all values or AND them... */
   private boolean logicalOR = false;

   /** holds the FileHolder object */
   private FileHolder fileHolder;

   /** holds the current locale */
   private Locale locale;

   /** holds the format object */
   private String pattern;

   public static FieldValue createFieldValueForSorting(Field field, boolean sortDirection) {
      FieldValue fv = new FieldValue(field);
      fv.setSortDirection(sortDirection);
      return fv;
   }

   public static FieldValue createFieldValueForSearching(Field field, String fieldValue, Locale locale, int operator, int searchMode, int searchAlgorithm, boolean logicalOR) {

      FieldValue fv = new FieldValue(field, fieldValue);
      fv.setLocale(locale);
      fv.setOperator(operator);
      fv.setSearchMode(searchMode);
      fv.setSearchAlgorithm(searchAlgorithm);
      fv.setLogicalOR(logicalOR);
      return fv;
   }

   /**
    * Creates a new FieldValue object.
    * 
    * @param field      the name of the field
    * @param fieldValue the string representation of the value of the field
    */
   public FieldValue(Field field, String fieldValue) {
      this(field);
      this.fieldValue = fieldValue;
   }

   /**
    * Creates a new FieldValue object.
    * 
    * @param field      the name of the field
    * @param fieldValue the string representation of the value of the field
    */
   private FieldValue(Field field) {
      this.field = field;
   }

   /**
    * Gets the operator attribute of the FieldValue object
    * 
    * @return The operator value
    */
   public int getOperator() {
      return operator;
   }

   /**
    * sets the operator
    * 
    * @param i
    */
   public void setOperator(int i) {
      operator = i;
   }

   /**
    * Sets the field attribute of the FieldValue object
    * 
    * @param field The new field value
    */
   public void setField(Field field) {
      this.field = field;
   }

   /**
    * Gets the field attribute of the FieldValue object
    * 
    * @return The field value
    */
   public Field getField() {
      return field;
   }

   /**
    * Sets the fieldValue attribute of the FieldValue object
    * 
    * @param fieldValue The new fieldValue value
    */
   public void setFieldValue(String fieldValue) {
      this.fieldValue = fieldValue;
   }

   /**
    * Gets the fieldValue attribute of the FieldValue object
    * 
    * @return The fieldValue value
    */
   public String getFieldValue() {
      return fieldValue;
   }

   /**
    * Sets the sortDirection attribute of the FieldValue object
    * 
    * @param sortDirection The new sortDirection value
    */
   private void setSortDirection(boolean sortDirection) {
      this.sortDirection = sortDirection;
   }

   /**
    * Gets the sortDirection attribute of the FieldValue object
    * 
    * @return The sortDirection value
    */
   public boolean getSortDirection() {
      return sortDirection;
   }

   /**
    * Sets the renderHiddenHtmlTag attribute of the FieldValue object
    * 
    * @param renderHiddenHtmlTag The new renderHiddenHtmlTag value
    */
   public void setRenderHiddenHtmlTag(boolean renderHiddenHtmlTag) {
      this.renderHiddenHtmlTag = renderHiddenHtmlTag;
   }

   /**
    * Gets the renderHiddenHtmlTag attribute of the FieldValue object
    * 
    * @return The renderHiddenHtmlTag value
    */
   public boolean getRenderHiddenHtmlTag() {
      return renderHiddenHtmlTag;
   }

   /**
    * Sets the searchMode attribute of the FieldValue object
    * 
    * @param searchMode The new searchMode value
    */
   public void setSearchMode(int searchMode) {
      this.searchMode = searchMode;
   }

   /**
    * Gets the searchMode attribute of the FieldValue object
    * 
    * @return The searchMode value
    */
   public int getSearchMode() {
      return searchMode;
   }

   /**
    * Sets the searchAlgorithm attribute of the FieldValue object
    * 
    * @param searchAlgorithm The new searchAlgorithm value
    */
   public void setSearchAlgorithm(int searchAlgorithm) {
      this.searchAlgorithm = searchAlgorithm;
   }

   /**
    * Gets the searchAlgorithm attribute of the FieldValue object
    * 
    * @return The searchAlgorithm value
    */
   public int getSearchAlgorithm() {
      return searchAlgorithm;
   }

   /**
    * Get the logicalOR attribute of this FieldValue object.
    * 
    * @return the logicalOR attribute of this FieldValue object
    */
   public boolean getLogicalOR() {
      return logicalOR;
   }

   /**
    * Set the logicalOR attribute of this FieldValue object.
    * 
    * @param newLogicalOR the logicalOR attribute value to set
    */
   public void setLogicalOR(boolean newLogicalOR) {
      logicalOR = newLogicalOR;
   }

   /**
    * Return the fileHolder object for this field.
    * 
    * @return the fileHolder object of this field
    */
   public FileHolder getFileHolder() {
      return fileHolder;
   }

   /**
    * Sets the fileHolder.
    * 
    * @param fileHolder The fileHolder to set
    */
   public void setFileHolder(FileHolder fileHolder) {
      this.fileHolder = fileHolder;
   }

   /**
    * Get the oldValue of this Field object.
    * 
    * @return the oldValue of this Field object
    */
   public String getOldValue() {
      return oldValue;
   }

   /**
    * Set the oldValue for this Field object.
    * 
    * @param string the oldValue for this Field object
    */
   public void setOldValue(String string) {
      oldValue = string;
   }

   /**
    * @return
    */
   public Locale getLocale() {
      return locale;
   }

   /**
    * @param locale
    */
   public void setLocale(Locale locale) {
      this.locale = locale;
   }

   /**
    * @param string
    */
   public void setPattern(String pattern) {
      this.pattern = pattern;
   }

   /**
    * @return
    */
   public String getPattern() {
      return pattern;
   }

   /**
    * parses the given fieldValue according to the given type and creates an 
    * java object of the needed type. 
    * during the parsing the given locale/format settings will be used.
    * 
    * @return Object of given type;
    * 
    */
   public Object getFieldValueAsObject() {
      if (getField() == null) {
         return null;
      }
      return getFieldValueAsObject(getField().getType());
   }

   public Object getFieldValueAsObject(int fieldType) {
      Object res = null;
      switch (fieldType) {

         case FieldTypes.INTEGER :
            res = parseINTEGER();
            break;

         case FieldTypes.DOUBLE :
            res = parseDOUBLE();
            break;

         case FieldTypes.FLOAT :
            res = parseFLOAT();
            break;

         case FieldTypes.NUMERIC :
            res = parseNUMERIC();
            break;

         case FieldTypes.CHAR :
            res = getFieldValue();
            break;

         case FieldTypes.DATE :
            res = parseDATE();
            break;

         case FieldTypes.TIME :
            res = parseTIME();
            break;

         case FieldTypes.TIMESTAMP :
            res = parseTIMESTAMP();
            break;

         case FieldTypes.BLOB :
            res = getFileHolder();
            break;

         case FieldTypes.DISKBLOB :
            res = parseDISKBLOB();
            break;

         default :
            break;
      }
      return res;
   }

   private BigDecimal parseNUMERIC() {
      String valueStr = getFieldValue().trim();
      if (Util.isNull(valueStr)) {
         return null;
      }
      if (getLocale() != null) {
         try {
            DecimalFormat f = (DecimalFormat) getField().getFormat(pattern, getLocale());
            return new BigDecimal(f.parse(valueStr).doubleValue());
         } catch (Exception e) {
            logCat.error(e.getMessage() + " <" + valueStr + "/" + pattern + ">");
         }
      }
      try {
         return new BigDecimal(valueStr);
      } catch (Exception e) {
         logCat.error(e.getMessage() + " <" + valueStr + ">");
      }
      return null;
   }

   private Integer parseINTEGER() {
      String valueStr = getFieldValue().trim();
      if (Util.isNull(valueStr)) {
         return null;
      }
      if (getLocale() != null) {
         try {
            DecimalFormat f = (DecimalFormat) getField().getFormat(pattern, getLocale());
            return new Integer(f.parse(valueStr).intValue());
         } catch (Exception e) {
            logCat.error(e.getMessage() + " <" + valueStr + "/" + pattern + ">");
         }
      }
      try {
         return new Integer(valueStr);
      } catch (Exception e) {
         logCat.error(e.getMessage() + " <" + valueStr + ">");
      }
      return null;
   }

   private Float parseFLOAT() {
      String valueStr = getFieldValue().trim();
      if (Util.isNull(valueStr)) {
         return null;
      }
      if (getLocale() != null) {
         try {
            DecimalFormat f = (DecimalFormat) getField().getFormat(pattern, getLocale());
            return new Float(f.parse(valueStr).floatValue());
         } catch (Exception e) {
            logCat.error(e.getMessage() + " <" + valueStr + "/" + pattern + ">");
         }
      }
      try {
         return new Float(valueStr);
      } catch (Exception e) {
         logCat.error(e.getMessage() + " <" + valueStr + ">");
      }
      return null;
   }

   private Double parseDOUBLE() {
      String valueStr = getFieldValue().trim();
      if (Util.isNull(valueStr)) {
         return null;
      }
      if (getLocale() != null) {
         try {
            DecimalFormat f = (DecimalFormat) getField().getFormat(pattern, getLocale());
            return new Double(f.parse(valueStr).doubleValue());
         } catch (Exception e) {
            logCat.error(e.getMessage() + " <" + valueStr + "/" + pattern + ">");
         }
      }
      try {
         return new Double(valueStr);
      } catch (Exception e) {
         logCat.error(e.getMessage() + " <" + valueStr + ">");
      }
      return null;
   }

   private Time parseTIME() {
      String valueStr = getFieldValue().trim();
      if (Util.isNull(valueStr)) {
         return null;
      }
      if (getLocale() != null) {
         try {
            SimpleDateFormat f = (SimpleDateFormat) getField().getFormat(pattern, getLocale());
            Calendar result = TimeUtil.parseDate(f, valueStr);
            result.set(Calendar.DAY_OF_MONTH, 0);
            result.set(Calendar.MONTH, 0);
            result.set(Calendar.YEAR, 0);
            return new Time(result.getTime().getTime());
         } catch (Exception e) {
            logCat.error(e.getMessage() + " <" + valueStr + "/" + pattern + ">");
         }
      }
      try {
         return Time.valueOf(valueStr);
      } catch (Exception e) {
         logCat.error(e.getMessage() + " <" + valueStr + ">");
      }
      return null;
   }

   private Date parseDATE() {
      String valueStr = getFieldValue().trim();
      if (Util.isNull(valueStr)) {
         return null;
      }
      if (getLocale() != null) {
         try {
            SimpleDateFormat f = (SimpleDateFormat) getField().getFormat(pattern, getLocale());
            Calendar result = TimeUtil.parseDate(f, valueStr);
            result.set(Calendar.HOUR_OF_DAY, 0);
            result.set(Calendar.MINUTE, 0);
            result.set(Calendar.SECOND, 0);
            return new Date(result.getTime().getTime());
         } catch (Exception e) {
            logCat.error(e.getMessage() + " <" + valueStr + "/" + pattern + ">");
         }
      }
      try {
         return Date.valueOf(valueStr);
      } catch (Exception e) {
         logCat.error(e.getMessage() + " <" + valueStr + ">");
      }
      return null;
   }

   private Timestamp parseTIMESTAMP() {
      String valueStr = getFieldValue().trim();
      if (Util.isNull(valueStr)) {
         return null;
      }
      if (getLocale() != null) {
         try {
            SimpleDateFormat f = (SimpleDateFormat) getField().getFormat(pattern, getLocale());
            Calendar result = TimeUtil.parseDate(f, valueStr);
            return new Timestamp(result.getTime().getTime());
         } catch (Exception e) {
            logCat.error(e.getMessage() + " <" + valueStr + "/" + pattern + ">");
         }
      }
      try {
         return Timestamp.valueOf(valueStr);
      } catch (Exception e) {
         logCat.error(e.getMessage() + " <" + valueStr + ">");
      }
      return null;
   }

   private String parseDISKBLOB() {
      if (getField().isEncoded()) {
         String fileName = getFileHolder().getFileName();
         int dotIndex = fileName.lastIndexOf('.');
         String suffix = (dotIndex != -1) ? fileName.substring(dotIndex) : "";
         fileHolder.setFileName(UniqueIDGenerator.getUniqueID() + suffix);
      }
      return fileHolder.getFileName();
   }

   /**
    * Clone this object.
    * 
    * @return a cloned FieldValue object
    */
   public Object clone() {
      // shallow copy ;=)
      try {
         FieldValue fv = (FieldValue) super.clone();
         return fv;
      } catch (CloneNotSupportedException e) {
         // should not happen
         logCat.error("::clone - exception", e);
      }

      return null;
   }

   /**
    * Get the String representation of this object.
    * 
    * @return the String representation of this object
    * 
    * @todo toString: to be finished
    */
   public String toString() {
      StringBuffer buf = new StringBuffer();

      String fieldName = getField().getName();
      buf.append("  ").append("field [").append(fieldName).append("] has value, oldvalue [").append(getFieldValue()).append(", ").append(getOldValue()).append("]\n");
      return buf.toString();
   }

   /**
    * Inverts the sorting direction of all FieldValue objects in the given
    * array [ASC-->DESC et vice versa]
    * 
    * @param fv the array of FieldValue objects
    */
   public static void invert(FieldValue[] fv) {
      for (int i = 0; i < fv.length; i++)
         fv[i].setSortDirection(!fv[i].getSortDirection());
   }

   /**
    *  Dump the input FieldValue array
    * 
    * @param fieldValues the fieldValues array
    * @return the string representation of the FieldValues object
    */
   public static String toString(FieldValue[] fieldValues) {
      StringBuffer sb = new StringBuffer();
      sb.append("FieldValue array size: ").append(fieldValues.length).append("; elements are:\n");

      for (int i = 0; i < fieldValues.length; i++) {
         FieldValue fieldValue = fieldValues[i];
         sb.append(fieldValue.toString());
      }
      return sb.toString();
   }

   /**
    * test if given FieldValue[] is empty
    * @param arr FieldValue[] to test
    * @return true if array is null or empty
    */
   public static final boolean isNull(FieldValue[] arr) {
      return ((arr == null) || (arr.length == 0));
   }

}