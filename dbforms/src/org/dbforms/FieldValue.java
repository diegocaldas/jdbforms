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

package org.dbforms;

/****
 * <p>
 *  This helper-class was originally used to maintain the mapped values
 *  between Main-Form and Sub-Form in the taglib package. in meantime
 *  it is used as holder of data in many places.
 *
 *  it also performs operations that involve "fields" and associated "values"
 *  (i.e. building blocks of SQL SELECT statements, etc)
 * </p>
 *
 *
 * @author Joe Peer <joepeer@excite.com>
 * @author Philip Grunikiewicz<grunikiewicz.philip@hydro.qc.ca> (-> introduced better support of filtering)
 */


import java.sql.*;
import java.util.*;
import org.dbforms.util.*;
import org.dbforms.taglib.DbBaseHandlerTag;
import org.apache.log4j.Category;

public class FieldValue implements Cloneable {

    static Category logCat = Category.getInstance(FieldValue.class.getName()); // logging category for this class

    //--------- constants -------------------------------------------------------------

    public static int COMPARE_NONE  = 0;
    public static int COMPARE_INCLUSIVE  = 1;
    public static int COMPARE_EXCLUSIVE  = 2;

    // 20020703-HKK: Make const public static final to use it outside this class!
    public static final int SEARCH_ALGO_SHARP = 0;
    public static final int SEARCH_ALGO_WEAK = 1;

    // 20020703-HKK: Extending search algorithm with WEAK_START, WEAK_END, WEAK_START_END
    //               results in like '%search', 'search%', '%search%'
    public static final int SEARCH_ALGO_WEAK_START      = 2;
    public static final int SEARCH_ALGO_WEAK_END        = 3;
    public static final int SEARCH_ALGO_WEAK_START_END  = 4;

    public static final int FILTER_EQUAL                = 0;
    public static final int FILTER_GREATER_THEN         = 1;
    public static final int FILTER_GREATER_THEN_EQUAL   = 3;
    public static final int FILTER_SMALLER_THEN         = 2;
    public static final int FILTER_SMALLER_THEN_EQUAL   = 4;
    public static final int FILTER_LIKE                 = 5;
    public static final int FILTER_NOT_EQUAL            = 6;
    public static final int FILTER_NULL                 = 7;
    public static final int FILTER_NOT_NULL             = 8;

  //--------- properties ------------------------------------------------------------

    private Field field;
    private String fieldValue; // a value a field is associated with
    private boolean sortDirection; // Field.ORDER_ASCENDING or Field.ORDER_DESCENDING
    private boolean renderHiddenHtmlTag; // if FieldValue is used in a "childFieldValue", it signalizes if it should be rendered as hidden tag or not (if "stroke out")
    private int searchMode; // if used in an argument for searching: AND || OR!
    private int searchAlgorithm; // if used in an argument for searching: SHARP || WEAK!
    private int operator; // if used in a filter argument: the type of filter comparison operator (see FILTER_* definitions above)
    private boolean logicalOR = false;  // specifies whether to OR all values or AND them...

    //--------- contructors -----------------------------------------------------------

  // Empty constructor
    public FieldValue() {}

  /**
   * constructor
   *
   * @param field
   * @param fieldValue
   * @param renderHiddenHtmlTag
   * @author
   */
    public FieldValue(Field field, String fieldValue, boolean renderHiddenHtmlTag) {
        this.field = field;
        this.fieldValue = fieldValue;
        this.renderHiddenHtmlTag = renderHiddenHtmlTag;
    }




  /**
   * constructor
   *
   * @param field
   * @param fieldValue
   * @param renderHiddenHtmlTag
   * @param operator
   */
    public FieldValue(Field field, String fieldValue, boolean renderHiddenHtmlTag, int operator) {
        this.field = field;
        this.fieldValue = fieldValue;
        this.renderHiddenHtmlTag = renderHiddenHtmlTag;
        this.operator = operator;
    }



  /**
   * constructor
   *
   * @param field
   * @param fieldValue
   * @param renderHiddenHtmlTag
   * @param operator
   */
    public FieldValue(Field field, String fieldValue, boolean renderHiddenHtmlTag, int operator, boolean isLogicalOR) {
        this.field = field;
        this.fieldValue = fieldValue;
        this.renderHiddenHtmlTag = renderHiddenHtmlTag;
        this.operator = operator;
        this.logicalOR = isLogicalOR;
    }


    public int getOperator() {
        return operator;
    }




    //--------- poperty getters and setters -------------------------------------------

    public void setField(Field field) {
        this.field = field;
    }

    public Field getField() {
        return field;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setSortDirection(boolean sortDirection) {
        this.sortDirection = sortDirection;

    }

    public boolean getSortDirection() {
        return sortDirection;
    }

    public void setRenderHiddenHtmlTag(boolean renderHiddenHtmlTag) {
        this.renderHiddenHtmlTag = renderHiddenHtmlTag;
    }

    public boolean getRenderHiddenHtmlTag() {
        return renderHiddenHtmlTag;
    }

    public void setSearchMode(int searchMode) {
        this.searchMode = searchMode;

    }

    public int getSearchMode() {
        return searchMode;
    }

    public void setSearchAlgorithm(int searchAlgorithm) {
        this.searchAlgorithm = searchAlgorithm;
    }

    public int getSearchAlgorithm() {
        return searchAlgorithm;
    }

  //---------------------------------------------------------------------------
  // this class serves as helper for building and populating queries:
  //
  //---------------------------------------------------------------------------

  /**
   * situation: we have an array of fieldvalues (== fields + actual value) and want
   * to build a where - clause [that should restrict the resultset in matching to
   * the fileds]
   *
   * @param FieldValue[] fv
   * @returns _part_ of a WHERE-clause

  public static String getWhereEqualsClause(FieldValue[] fv) {

        StringBuffer buf = new StringBuffer();
        if(fv != null && fv.length > 0) {

            for(int i=0; i<fv.length; i++) {
                buf.append(fv[i].getField().getName());

                buf.append(" = ");
                buf.append(" ? ");

                if(i < fv.length-1)
                    buf.append(" AND ");
            }
        }
        return buf.toString();

    }
   */

  /**
   * situation: we have an array of fieldvalues (== fields + actual value) and want
   * to build a where - clause [that should restrict the resultset in matching to
   * the fields]
   *
   * @param FieldValue[] fv
   * @returns _part_ of a WHERE-clause
   * @author Philip Grunikiewicz<grunikiewicz.philip@hydro.qc.ca> (-> introduced better support of filtering)
   */
  public static String getWhereClause(FieldValue[] fv) {

        StringBuffer buf = new StringBuffer();
        if(fv != null && fv.length > 0) {

            for(int i=0; i<fv.length; i++) {


                // Depending on the value of isLogicalOR in FieldValue, prefix the filter definition
                // with either OR or AND (Skip first entry!)
                if(i != 0)
                {
                    if(fv[i].isLogicalOR())
                        buf.append(" OR ");
                    else
                        buf.append(" AND ");
                }

                buf.append(fv[i].getField().getName());
                // Check what type of operator is required
                switch(fv[i].getOperator()) {
                    case FieldValue.FILTER_EQUAL:               buf.append(" = "); break;
                    case FieldValue.FILTER_NOT_EQUAL:           buf.append(" <> "); break;
                    case FieldValue.FILTER_GREATER_THEN:        buf.append(" > "); break;
                    case FieldValue.FILTER_SMALLER_THEN:        buf.append(" < "); break;
                    case FieldValue.FILTER_GREATER_THEN_EQUAL:  buf.append(" >= "); break;
                    case FieldValue.FILTER_SMALLER_THEN_EQUAL:  buf.append(" <= "); break;
                    case FieldValue.FILTER_LIKE:                buf.append(" like "); break;

                }
                buf.append(" ? ");
            }
        }
        return buf.toString();
    }



  /**
   * situation: we have an array of fieldvalues (== fields + actual value ) with search
   * information and we want
   * to build a where - clause [that should restrict the resultset in matching to
   * the search fieleds]
   *
   * convention:    index 0-n => AND
   *                index (n+1)-m => OR
   *
   * examples
   *
   *            (A = 'meier' AND X = 'joseph') AND (AGE = '10')
   *            (A = 'meier' ) AND (X = 'joseph' OR AGE = '10')
   *            (X = 'joseph' OR AGE = '10')
   *            (A = 'meier' AND X = 'joseph')
   *
   * for comparing to code:
   *
   *   §1     §2        §3      §2          §4    §5   §6      §2      §7
   *   (   A = 'smith' AND   X LIKE 'jose%' )    AND    (  AGE = '10'   )
   *
   * @param FieldValue[] fv
   * @returns _part_ of a WHERE-clause
   */
  public static String getWhereEqualsSearchClause(FieldValue[] fv) {
        StringBuffer buf = new StringBuffer();
        if(fv != null && fv.length > 0) {
            int mode, oldMode=-1;
            for(int i=0; i<fv.length; i++) {
                    mode = fv[i].getSearchMode();
                    if(oldMode!=mode) {
                            oldMode = mode;
                            buf.append("("); // §1, §6
                    }
                    // §2, i.e "A = 'smith'" or "X LIKE 'jose%'"
                    buf.append(fv[i].getField().getName());
                    // 20020927-HKK: Check what type of operator is required
                      switch(fv[i].getOperator()){
                          case FieldValue.FILTER_EQUAL:
                            buf.append(" = ");
                        buf.append(" ? ");
                            break;
                          case FieldValue.FILTER_NOT_EQUAL:
                            buf.append(" <> ");
                        buf.append(" ? ");
                            break;
                          case FieldValue.FILTER_GREATER_THEN:
                            buf.append(" > ");
                    buf.append(" ? ");
                            break;
                          case FieldValue.FILTER_SMALLER_THEN:
                            buf.append(" < ");
                    buf.append(" ? ");
                            break;
                          case FieldValue.FILTER_GREATER_THEN_EQUAL:
                            buf.append(" >= ");
                    buf.append(" ? ");
                            break;
                          case FieldValue.FILTER_SMALLER_THEN_EQUAL:
                            buf.append(" <= ");
                                buf.append(" ? ");
                            break;
                          case FieldValue.FILTER_LIKE:
                            buf.append(" LIKE ");
                    buf.append(" ? ");
                            break;
                          case FieldValue.FILTER_NULL:
                            buf.append(" IS NULL ");
                            break;
                          case FieldValue.FILTER_NOT_NULL:
                            buf.append(" IS NOT NULL ");
                            break;
                      }
                    if(i < fv.length-1 && fv[i+1].getSearchMode()==mode)
                            buf.append( mode == DbBaseHandlerTag.SEARCHMODE_AND ? "AND " : "OR " ); // §3
                    else {
                    //if(i==fv.length-1 || fv[i+1].getSearchMode()!=mode) {
                            buf.append(")");      // §4, §7
                            if(i!=fv.length-1) {
                              buf.append(" OR ");   // §5 #checkme
                            }
                    }
            }
        }
        return buf.toString();
}

  /**
   * situation: we have built a query (involving the getWhereEqualsClause() method)
   * and now we want to prepare the statemtent - provide actual values for the
   * the '?' placeholders
   *
   * @param FieldValue[] fv
   * @param PreparedStatement ps
   * @param int startCol - index we're supposed to start placing values with
   * @param return - the index of the first NOT POPULATED placeholder
   */
  public static int populateWhereEqualsClause(FieldValue[] fv, PreparedStatement ps, int curCol)
  throws SQLException {

        if(fv != null && fv.length > 0) {
            for(int i=0; i<fv.length; i++) {
                fillPreparedStatement(fv[i],ps,curCol);
                curCol++;
            }
        }

        return curCol;
    }


  /**
   * situation: we have an array of fieldvalues which represents actual values of
   * order-determinating-fields. we want to build a part of the WHERE clause which restricts
   * the query to rows coming AFTER the row containing the actual data.
   *
   * shortly described the following rule is applied:
   *
   * +--------------------------------------------------------------------------------------------------+
   * |  RULE = R1 AND R2 AND ... AND Rn                                                                 |
   * |  Ri = fi OpA(i) fi* OR  f(i-1) OpB(i-1) f(i-1)* OR f(i-2) OpB(i-2) f(i-2)* OR ... OR f1 OpB f1*  |
   * +--------------------------------------------------------------------------------------------------+
   *
   * For background info email joepeer@wap-force.net
   *
   * IMPORTANT NOTE: the indizes of the fv-array indicate implicitly the order-priority of the fields
   * example: if we have ORDER BY id,name,age -> then fv[0] should contain field id, fv[1] should contain field name, fv[2] should contain field age
   *
   * @param FieldValue[] fv
   * @returns _part_ of a WHERE-clause
   */
  public static String getWhereAfterClause(FieldValue[] fv, int compareMode) {

        String conj, disj, opA1, opA2, opB1, opB2;

        if(compareMode == FieldValue.COMPARE_INCLUSIVE) {
            opA1 = ">=";
            opA2 = "<=";
            opB1 = ">";
            opB2 = "<";
            conj = " AND ";
            disj = " OR ";
        } else  { // COMPARE_INCLUSIVE
            opA1 = ">";
            opA2 = "<";
            opB1 = ">=";
            opB2 = "<=";
            conj = " OR ";
            disj = " AND ";
        }

        StringBuffer buf = new StringBuffer();
        if(fv != null && fv.length > 0) {

            // generate the Ri's
            for(int i=0; i<fv.length; i++) {

                // generate a "fi OpA(i) fi*"
                buf.append("(");
                buf.append(fv[i].getField().getName());
                buf.append(fv[i].getSortDirection()==Field.ORDER_ASCENDING ? opA1 : opA2); // OpA
                buf.append(" ? ");

              // generate the "f(i-1) OpB(i-1) f(i-1)* OR f(i-2) OpB(i-2) f(i-2)* OR ... OR f1 OpB f1*"
                if(i>0) {

                    for(int j=i-1; j>=0; j--)   {

                        buf.append(disj);
                        buf.append(fv[j].getField().getName());
                        buf.append(fv[j].getSortDirection()==Field.ORDER_ASCENDING ? opB1 : opB2); // OpB
                        buf.append(" ? ");
                    }
                }
                buf.append(" ) ");

                if(i < fv.length-1)
                    buf.append(conj); // link the R's together (conjunction)

            }

        }
        return buf.toString();
    }


  /**
  situation: we have built a query (involving the getWhereEqualsClause() method)
  and now we want to prepare the statemtent - provide actual values for the
  the '?' placeholders

  @param FieldValue[] fv
  @param PreparedStatement ps
  @param int startCol - index we're supposed to start placing values with
  @param return - the index of the first NOT POPULATED placeholder
  */
  public static int populateWhereAfterClause(FieldValue[] fv, PreparedStatement ps, int curCol)
  throws SQLException {

        if(fv != null && fv.length > 0) {
            // populate the Ri's
            for(int i=0; i<fv.length; i++) {
                // populate a "fi OpA(i) fi*"

                logCat.info("setting col "+curCol);
                fillPreparedStatement(fv[i], ps, curCol);
                curCol++;
              // populate the "f(i-1) OpB(i-1) f(i-1)* OR f(i-2) OpB(i-2) f(i-2)* OR ... OR f1 OpB f1*"
                if(i>0) {
                    for(int j=i-1; j>=0; j--)   {
                        logCat.info("setting col "+curCol);
                        fillPreparedStatement(fv[j], ps, curCol);
                        curCol++;
                    }
                }
            }
        }

        return curCol;
    }

  /**
   * inverts the sorting direction of all FieldValue objects in the given array
   * [ASC-->DESC et vice versa]
   */
    public static void invert(FieldValue[] fv) {
        for(int i=0; i<fv.length; i++)
          fv[i].setSortDirection(!fv[i].getSortDirection());
    }

    /**
     * returns the part of the orderby-clause represented by this FieldValue object.
     * FieldName [DESC]
     * (ASC will be not printed because it is defined DEFAULT in SQL
     * if there are RDBMS which do not tolerate this please let me know; then i'll
     * change it)
     */
    public String getOrderClause() {
        StringBuffer buf = new StringBuffer();
      buf.append(field.getName());
        if(sortDirection == Field.ORDER_DESCENDING)
            buf.append(" DESC");

        return buf.toString();
    }

    private static void fillPreparedStatement(FieldValue cur, PreparedStatement ps, int curCol)
    throws SQLException{
        Field curField = cur.getField();
        String valueStr = cur.getFieldValue();

        logCat.info("setting "+cur.getField().getName()+" to value "+valueStr+" of type "+curField.getType());

                // 20020703-HKK: Extending search algorithm with WEAK_START, WEAK_END, WEAK_START_END
                //               results in like '%search', 'search%', '%search%'
                switch (cur.getSearchAlgorithm()) {
                    case FieldValue.SEARCH_ALGO_WEAK_START:
                        valueStr = '%' + valueStr;
                        break;
                    case FieldValue.SEARCH_ALGO_WEAK_END:
                        valueStr = valueStr + '%';
                        break;
                    case FieldValue.SEARCH_ALGO_WEAK_START_END:
                        valueStr = '%' + valueStr + '%';
                        break;
                }
                switch (cur.getOperator()) {
                    case FieldValue.FILTER_NULL:
                        break;
                      case FieldValue.FILTER_NOT_NULL:
                        break;
                    default:
                        SqlUtil.fillPreparedStatement(ps, curCol, valueStr, curField.getType());
                }
    }


    /**
     transfer values from asscociative (name-orientated, user friendly) hashtable [param 'assocFv']
     into plain fieldValues objects [param 'fv']

    public static void synchronizeData(FieldValue[] fv, Hashtable assocFv) {

        for(int i=0; i<fv.length; i++) {
          String fieldName = fv[i].getField().getName();
          String valueFromHash = (String) assocFv.get(fieldName);
          fv[i].setFieldValue(valueFromHash);
        }

    }

*/

  public Object clone() {
    try {
        return super.clone(); // shallow copy ;=)
    } catch ( CloneNotSupportedException e ) {
      // should not happen
    }
        return null;
  }


  public String toString() {
    StringBuffer buf = new StringBuffer();

    if(field!=null) {
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
 * Insert the method's description here.
 * Creation date: (2001-09-11 15:39:36)
 * @return boolean
 */
public boolean isLogicalOR() {
    return logicalOR;
}

/**
 * Insert the method's description here.
 * Creation date: (2001-09-11 15:39:36)
 * @param newLogicalOR boolean
 */
public void setLogicalOR(boolean newLogicalOR) {
    logicalOR = newLogicalOR;
}
}