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

package org.dbforms.util;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.log4j.Category;
import org.dbforms.config.Field;



/**
 * This helper-class was originally used to maintain the mapped values between
 * Main-Form and Sub-Form in the taglib package. in meantime it is used as
 * holder of data in many places. <br>
 * It also performs operations that involve "fields" and associated "values"
 * (i.e. building blocks of SQL SELECT statements, etc)
 * 
 * @author Joe Peer
 * @author Philip Grunikiewicz
 */
public class FieldValue implements Cloneable
{
   /** logging category for this class */
   private static Category logCat = Category.getInstance(
                                             FieldValue.class.getName());

   /** DOCUMENT ME! */
   /** field object */
   private Field field;

   /** a value a field is associated with */
   private String fieldValue;

   private String oldValue;
   
   /**
    * Field sor direction. Can be Field.ORDER_ASCENDING or
    * Field.ORDER_DESCENDING.
    */
   private boolean sortDirection;

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

   /**
    * Creates a new FieldValue object.
    */
   public FieldValue()
   {
   }


   /**
    * Creates a new FieldValue object.
    * 
    * @param field DOCUMENT ME!
    * @param fieldValue DOCUMENT ME!
    */
   public FieldValue(Field field, String fieldValue)
   {
      this.field      = field;
      this.fieldValue = fieldValue;
   }


   /**
    * Constructor.
    * 
    * @param field the field object
    * @param fieldValue the field value
    * @param operator the type of filter comparison operator
    */
   public FieldValue(Field field, String fieldValue, int operator)
   {
      this(field, fieldValue);
      this.operator = operator;
   }


   /**
    * Constructor
    * 
    * @param field the field object
    * @param fieldValue the field value
    * @param operator the type of filter comparison operator
    * @param isLogicalOR specifies whether to OR all values or AND them
    */
   public FieldValue(Field field, String fieldValue, int operator, 
                     boolean isLogicalOR)
   {
      this(field, fieldValue, operator);
      this.logicalOR = isLogicalOR;
   }

   /**
    * Gets the operator attribute of the FieldValue object
    * 
    * @return The operator value
    */
   public int getOperator()
   {
      return operator;
   }


   /**
    * Sets the field attribute of the FieldValue object
    * 
    * @param field The new field value
    */
   public void setField(Field field)
   {
      this.field = field;
   }


   /**
    * Gets the field attribute of the FieldValue object
    * 
    * @return The field value
    */
   public Field getField()
   {
      return field;
   }


   /**
    * Sets the fieldValue attribute of the FieldValue object
    * 
    * @param fieldValue The new fieldValue value
    */
   public void setFieldValue(String fieldValue)
   {
      this.fieldValue = fieldValue;
   }


   /**
    * Gets the fieldValue attribute of the FieldValue object
    * 
    * @return The fieldValue value
    */
   public String getFieldValue()
   {
      return fieldValue;
   }


   /**
    * Sets the sortDirection attribute of the FieldValue object
    * 
    * @param sortDirection The new sortDirection value
    */
   public void setSortDirection(boolean sortDirection)
   {
      this.sortDirection = sortDirection;
   }


   /**
    * Gets the sortDirection attribute of the FieldValue object
    * 
    * @return The sortDirection value
    */
   public boolean getSortDirection()
   {
      return sortDirection;
   }


   /**
    * Sets the renderHiddenHtmlTag attribute of the FieldValue object
    * 
    * @param renderHiddenHtmlTag The new renderHiddenHtmlTag value
    */
   public void setRenderHiddenHtmlTag(boolean renderHiddenHtmlTag)
   {
      this.renderHiddenHtmlTag = renderHiddenHtmlTag;
   }


   /**
    * Gets the renderHiddenHtmlTag attribute of the FieldValue object
    * 
    * @return The renderHiddenHtmlTag value
    */
   public boolean getRenderHiddenHtmlTag()
   {
      return renderHiddenHtmlTag;
   }


   /**
    * Sets the searchMode attribute of the FieldValue object
    * 
    * @param searchMode The new searchMode value
    */
   public void setSearchMode(int searchMode)
   {
      this.searchMode = searchMode;
   }


   /**
    * Gets the searchMode attribute of the FieldValue object
    * 
    * @return The searchMode value
    */
   public int getSearchMode()
   {
      return searchMode;
   }


   /**
    * Sets the searchAlgorithm attribute of the FieldValue object
    * 
    * @param searchAlgorithm The new searchAlgorithm value
    */
   public void setSearchAlgorithm(int searchAlgorithm)
   {
      this.searchAlgorithm = searchAlgorithm;
   }


   /**
    * Gets the searchAlgorithm attribute of the FieldValue object
    * 
    * @return The searchAlgorithm value
    */
   public int getSearchAlgorithm()
   {
      return searchAlgorithm;
   }


   /**
    * Get the logicalOR attribute of this FieldValue object.
    * 
    * @return the logicalOR attribute of this FieldValue object
    */
   public boolean isLogicalOR()
   {
      return logicalOR;
   }


   /**
    * Set the logicalOR attribute of this FieldValue object.
    * 
    * @param newLogicalOR the logicalOR attribute value to set
    */
   public void setLogicalOR(boolean newLogicalOR)
   {
      logicalOR = newLogicalOR;
   }


   /**
    * Build the WHERE clause string using the input field values.
    * 
    * @param fv the array of FieldValue objects
    * 
    * @return the WHERE clause string
    */
   public static String getWhereClause(FieldValue[] fv)
   {
      StringBuffer buf = new StringBuffer();

      if ((fv != null) && (fv.length > 0))
      {
          // SM 2003-08-08: added brackets for each and-ed condition
          buf.append(" ( ");
         for (int i = 0; i < fv.length; i++)
         {
            // depending on the value of isLogicalOR in FieldValue,
            // prefix the filter definition with either OR or AND
            // (Skip first entry!)
            if (i != 0)
            {
               if (fv[i].isLogicalOR())
               {
                  buf.append(" OR ");
               }
               else
               {
                  buf.append(" ) AND ( ");
               }
            }

            // 20021104-HKK: Ceckk for expression.
            Field f = fv[i].getField();

            if ((f.getExpression() != null) && (f.getExpression().length() > 0))
            {
               buf.append(f.getExpression());
            }
            else
            {
               buf.append(f.getName());
            }

            // Check what type of operator is required
            switch (fv[i].getOperator())
            {
               case Constants.FILTER_EQUAL:
                  buf.append(" = ");
                  buf.append(" ? ");

                  break;

               case Constants.FILTER_NOT_EQUAL:
                  buf.append(" <> ");
                  buf.append(" ? ");

                  break;

               case Constants.FILTER_GREATER_THEN:
                  buf.append(" > ");
                  buf.append(" ? ");

                  break;

               case Constants.FILTER_SMALLER_THEN:
                  buf.append(" < ");
                  buf.append(" ? ");

                  break;

               case Constants.FILTER_GREATER_THEN_EQUAL:
                  buf.append(" >= ");
                  buf.append(" ? ");

                  break;

               case Constants.FILTER_SMALLER_THEN_EQUAL:
                  buf.append(" <= ");
                  buf.append(" ? ");

                  break;

               case Constants.FILTER_LIKE:
                  buf.append(" LIKE ");
                  buf.append(" ? ");

                  break;

               case Constants.FILTER_NULL:
                  buf.append(" IS NULL ");

                  break;

               case Constants.FILTER_NOT_NULL:
                  buf.append(" IS NOT NULL ");

                  break;
            }
         }
         buf.append(" ) ");
      }
      return buf.toString();
   }


   /**
    * situation: we have an array of fieldvalues (== fields + actual value )
    * with search information and we want to build a where - clause [that
    * should restrict the resultset in matching to the search fields].
    * <pre>
    * convention:    index 0-n => AND
    *                index (n+1)-m => OR
    * examples
    *            (A = 'meier' AND X = 'joseph') AND (AGE = '10')
    *            (A = 'meier' ) AND (X = 'joseph' OR AGE = '10')
    *            (X = 'joseph' OR AGE = '10')
    *            (A = 'meier' AND X = 'joseph')
    * for comparing to code:
    *   §1     §2        §3      §2          §4    §5   §6      §2      §7
    *   (   A = 'smith' AND   X LIKE 'jose%' )    AND    (  AGE = '10'   )
    * </pre>
    * 
    * @param fv Description of the Parameter
    * 
    * @return _part_ of a WHERE-clause
    */
   public static String getWhereEqualsSearchClause(FieldValue[] fv)
   {
      StringBuffer buf = new StringBuffer();

      if ((fv != null) && (fv.length > 0))
      {
         int mode;
         int oldMode = -1;

         for (int i = 0; i < fv.length; i++)
         {
            mode = fv[i].getSearchMode();

            if (oldMode != mode)
            {
               oldMode = mode;
               buf.append("(");

               // §1, §6
            }

            // §2, i.e "A = 'smith'" or "X LIKE 'jose%'"
            // 20021104-HKK: Ceckk for expression.
            Field f = fv[i].getField();

            if ((f.getExpression() != null) && (f.getExpression().length() > 0))
            {
               buf.append(f.getExpression());
            }
            else
            {
               buf.append(f.getName());
            }

            // 20020927-HKK: Check what type of operator is required
            switch (fv[i].getOperator())
            {
               case Constants.FILTER_EQUAL:
                  buf.append(" = ");
                  buf.append(" ? ");

                  break;

               case Constants.FILTER_NOT_EQUAL:
                  buf.append(" <> ");
                  buf.append(" ? ");

                  break;

               case Constants.FILTER_GREATER_THEN:
                  buf.append(" > ");
                  buf.append(" ? ");

                  break;

               case Constants.FILTER_SMALLER_THEN:
                  buf.append(" < ");
                  buf.append(" ? ");

                  break;

               case Constants.FILTER_GREATER_THEN_EQUAL:
                  buf.append(" >= ");
                  buf.append(" ? ");

                  break;

               case Constants.FILTER_SMALLER_THEN_EQUAL:
                  buf.append(" <= ");
                  buf.append(" ? ");

                  break;

               case Constants.FILTER_LIKE:
                  buf.append(" LIKE ");
                  buf.append(" ? ");

                  break;

               case Constants.FILTER_NULL:
                  buf.append(" IS NULL ");

                  break;

               case Constants.FILTER_NOT_NULL:
                  buf.append(" IS NOT NULL ");

                  break;
            }

            if ((i < (fv.length - 1)) && (fv[i + 1].getSearchMode() == mode))
            {
               buf.append((mode == Constants.SEARCHMODE_AND) ? "AND " : "OR ");

               // §3
            }
            else
            {
               //if(i==fv.length-1 || fv[i+1].getSearchMode()!=mode) {
               buf.append(")");

               // §4, §7
               if (i != (fv.length - 1))
               {
                  buf.append(" OR ");

                  // §5 #checkme
               }
            }
         }
      }

      return buf.toString();
   }


   /**
    * situation: we have built a query (involving the getWhereEqualsClause()
    * method) and now we want to prepare the statemtent - provide actual
    * values for the the '?' placeholders
    * 
    * @param fv the array of FieldValue objects
    * @param ps the PreparedStatement object
    * @param curCol the current PreparedStatement column; points to a
    *        PreparedStatement xxx value
    * 
    * @return the current column value
    * 
    * @exception SQLException if any error occurs
    */
   public static int populateWhereEqualsClause(FieldValue[] fv, 
                                               PreparedStatement ps, int curCol)
                                        throws SQLException
   {
      if ((fv != null) && (fv.length > 0))
      {
         for (int i = 0; i < fv.length; i++)
         {
            curCol = fillPreparedStatement(fv[i], ps, curCol);
         }
      }

      return curCol;
   }


   /**
    * situation: we have an array of fieldvalues which represents actual values
    * of order-determinating-fields. we want to build a part of the WHERE
    * clause which restricts the query to rows coming AFTER the row containing
    * the actual data. <br>
    * shortly described the following rule is applied:
    * <pre>
    * +--------------------------------------------------------------------------------------------------+
    * |  RULE = R1 AND R2 AND ... AND Rn                                                                 |
    * |  Ri = fi OpA(i) fi* OR  f(i-1) OpB(i-1) f(i-1)* OR f(i-2) OpB(i-2) f(i-2)* OR ... OR f1 OpB f1*  |
    * +--------------------------------------------------------------------------------------------------+
    * For background info email joepeer@wap-force.net
    * </pre>
    * IMPORTANT NOTE: the indizes of the fv-array indicate implicitly the
    * order-priority of the fields. <br>
    * example: if we have ORDER BY id,name,age -> then fv[0] should contain
    * field id, fv[1] should contain field name, fv[2] should contain field
    * age
    * 
    * @param fv the array of FieldValue objects
    * @param compareMode the comparison mode
    * 
    * @return _part_ of a WHERE-clause
    */
   public static String getWhereAfterClause(FieldValue[] fv, int compareMode)
   {
      String conj;
      String disj;
      String opA1;
      String opA2;
      String opB1;
      String opB2;

      // COMPARE_INCLUSIVE
      if (compareMode == Constants.COMPARE_INCLUSIVE)
      {
         opA1 = ">=";
         opA2 = "<=";
         opB1 = ">";
         opB2 = "<";
         conj = " AND ";
         disj = " OR ";
      }
      else
      {
         opA1 = ">";
         opA2 = "<";
         opB1 = ">=";
         opB2 = "<=";
         conj = " OR ";
         disj = " AND ";
      }

      StringBuffer buf = new StringBuffer();

      if ((fv != null) && (fv.length > 0))
      {
         // generate the Ri's
         for (int i = 0; i < fv.length; i++)
         {
            // generate a "fi OpA(i) fi*"
            buf.append("(");
            buf.append(fv[i].getField().getName());
            buf.append((fv[i].getSortDirection() == Constants.ORDER_ASCENDING)
                          ? opA1 : opA2);


            // OpA
            buf.append(" ? ");

            // generate the "f(i-1) OpB(i-1) f(i-1)* OR f(i-2) OpB(i-2) f(i-2)* OR ... OR f1 OpB f1*"
            if (i > 0)
            {
               for (int j = i - 1; j >= 0; j--)
               {
                  buf.append(disj);
                  buf.append(fv[j].getField().getName());
                  buf.append((fv[j].getSortDirection() == Constants.ORDER_ASCENDING)
                                ? opB1 : opB2);


                  // OpB
                  buf.append(" ? ");
               }
            }

            buf.append(" ) ");

            if (i < (fv.length - 1))
            {
               buf.append(conj);

               // link the R's together (conjunction)
            }
         }
      }

      return buf.toString();
   }


   /**
    * situation: we have built a query (involving the getWhereEqualsClause()
    * method) and now we want to prepare the statemtent - provide actual
    * values for the the '?' placeholders.
    * 
    * @param fv the array of FieldValue objects
    * @param ps the PreparedStatement object
    * @param curCol the current PreparedStatement column; points to a
    *        PreparedStatement xxx value
    * 
    * @return the value of the current column
    * 
    * @exception SQLException if any error occurs
    */
   public static int populateWhereAfterClause(FieldValue[] fv, 
                                              PreparedStatement ps, int curCol)
                                       throws SQLException
   {
      if ((fv != null) && (fv.length > 0))
      {
         // populate the Ri's
         for (int i = 0; i < fv.length; i++)
         {
            // populate a "fi OpA(i) fi*"
            curCol = fillPreparedStatement(fv[i], ps, curCol);

            // populate the "f(i-1) OpB(i-1) f(i-1)* OR f(i-2) OpB(i-2) f(i-2)* OR ... OR f1 OpB f1*"
            if (i > 0)
            {
               for (int j = i - 1; j >= 0; j--)
               {
                  curCol = fillPreparedStatement(fv[j], ps, curCol);
               }
            }
         }
      }

      return curCol;
   }


   /**
    * Inverts the sorting direction of all FieldValue objects in the given
    * array [ASC-->DESC et vice versa]
    * 
    * @param fv the array of FieldValue objects
    */
   public static void invert(FieldValue[] fv)
   {
      for (int i = 0; i < fv.length; i++)
         fv[i].setSortDirection(!fv[i].getSortDirection());
   }


   /**
    * Fill the input PreparedStatement object
    * 
    * @param cur the FieldValue object
    * @param ps  the PreparedStatement object
    * @param curCol the current PreparedStatement column; points to a
    *        PreparedStatement xxx value
    * 
    * @return DOCUMENT ME!
    * 
    * @exception SQLException if any error occurs
    */
   private static int fillPreparedStatement(FieldValue cur, 
                                            PreparedStatement ps, int curCol)
                                     throws SQLException
   {
      Field  curField = cur.getField();
      String valueStr = cur.getFieldValue();

      logCat.info("setting col " + curCol + " with name "
                  + cur.getField().getName() + " to value " + valueStr
                  + " of type " + curField.getType() + " operator "
                  + cur.getOperator());

      // 20020703-HKK: Extending search algorithm with WEAK_START, WEAK_END, WEAK_START_END
      //               results in like '%search', 'search%', '%search%'
      switch (cur.getSearchAlgorithm())
      {
         case Constants.SEARCH_ALGO_WEAK_START:
            valueStr = '%' + valueStr;

            break;

         case Constants.SEARCH_ALGO_WEAK_END:
            valueStr = valueStr + '%';

            break;

         case Constants.SEARCH_ALGO_WEAK_START_END:
            valueStr = '%' + valueStr + '%';

            break;
      }

      switch (cur.getOperator())
      {
         case Constants.FILTER_NULL:
            break;

         case Constants.FILTER_NOT_NULL:
            break;

         default:
            SqlUtil.fillPreparedStatement(ps, curCol, valueStr, 
                                          curField.getType());
            curCol++;
      }

      return curCol;
   }


   /**
    * Clone this object.
    * 
    * @return a cloned FieldValue object
    */
   public Object clone()
   {
      // shallow copy ;=)
      try
      {
         return super.clone();
      }
      catch (CloneNotSupportedException e)
      {
         // should not happen
         logCat.error("::clone - exception", e);
      }

      return null;
   }


   /**
    * Get the String representation of this object.
    * 
    * @return the String representation of this object
    */
   public String toString()
   {
      StringBuffer buf = new StringBuffer();

      if (field != null)
      {
         buf.append("FieldName=");
         buf.append(field.getName());
      }

      buf.append(", fieldValue=");
      buf.append(fieldValue);
      buf.append(", sortDirection=");
      buf.append(sortDirection);
      buf.append(", renderHiddenHtmlTag=");
      buf.append(renderHiddenHtmlTag);
      buf.append(", searchMode=");
      buf.append(searchMode);
      buf.append(", searchAlgorithm=");
      buf.append(searchAlgorithm);

      return buf.toString();
   }


   /**
    * DOCUMENT ME!
    * 
    * @return FileHolder
    */
   public FileHolder getFileHolder()
   {
      return fileHolder;
   }


   /**
    * Sets the fileHolder.
    * 
    * @param fileHolder The fileHolder to set
    */
   public void setFileHolder(FileHolder fileHolder)
   {
      this.fileHolder = fileHolder;
   }
	/**
	 * @return
	 */
	public String getOldValue()
	{
		return oldValue;
	}

	/**
	 * @param string
	 */
	public void setOldValue(String string)
	{
		oldValue = string;
	}

}