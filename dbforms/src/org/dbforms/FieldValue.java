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



/**
 * DOCUMENT ME!
 *
 * @version $Revision$
 * @author $author$
 */
public class FieldValue implements Cloneable
{
    static Category logCat = Category.getInstance(FieldValue.class.getName()); // logging category for this class

    /** DOCUMENT ME! */
    public static int COMPARE_NONE = 0;

    /** DOCUMENT ME! */
    public static int COMPARE_INCLUSIVE = 1;

    /** DOCUMENT ME! */
    public static int COMPARE_EXCLUSIVE = 2;

    /** DOCUMENT ME! */
    public static final int SEARCH_ALGO_SHARP = 0;

    /** DOCUMENT ME! */
    public static final int SEARCH_ALGO_WEAK = 1;

    /** DOCUMENT ME! */
    public static final int SEARCH_ALGO_WEAK_START = 2;

    /** DOCUMENT ME! */
    public static final int SEARCH_ALGO_WEAK_END = 3;

    /** DOCUMENT ME! */
    public static final int SEARCH_ALGO_WEAK_START_END = 4;

    /** DOCUMENT ME! */
    public static final int SEARCH_ALGO_EXTENDED = 5;

    /** DOCUMENT ME! */
    public static final int FILTER_EQUAL = 0;

    /** DOCUMENT ME! */
    public static final int FILTER_GREATER_THEN = 1;

    /** DOCUMENT ME! */
    public static final int FILTER_GREATER_THEN_EQUAL = 3;

    /** DOCUMENT ME! */
    public static final int FILTER_SMALLER_THEN = 2;

    /** DOCUMENT ME! */
    public static final int FILTER_SMALLER_THEN_EQUAL = 4;

    /** DOCUMENT ME! */
    public static final int FILTER_LIKE = 5;

    /** DOCUMENT ME! */
    public static final int FILTER_NOT_EQUAL = 6;

    /** DOCUMENT ME! */
    public static final int FILTER_NULL = 7;

    /** DOCUMENT ME! */
    public static final int FILTER_NOT_NULL = 8;

    /** DOCUMENT ME! */
    public static final boolean ORDER_ASCENDING = false;

    /** DOCUMENT ME! */
    public static final boolean ORDER_DESCENDING = true;

    //--------- properties ------------------------------------------------------------
    private Field field;
    private String fieldValue; // a value a field is associated with
    private boolean sortDirection; // Field.ORDER_ASCENDING or Field.ORDER_DESCENDING
    private boolean renderHiddenHtmlTag;

    // if FieldValue is used in a "childFieldValue", it signalizes if it should be rendered as hidden tag or not (if "stroke out")
    private int searchMode; // if used in an argument for searching: AND || OR!
    private int searchAlgorithm; // if used in an argument for searching: SHARP || WEAK!
    private int operator;

    // if used in a filter argument: the type of filter comparison operator (see FILTER_* definitions above)
    private boolean logicalOR = false; // specifies whether to OR all values or AND them...

    /**
     * Creates a new FieldValue object.
     */
    public FieldValue()
    {
    }


    /**
     * constructor
     *
     * @param field
     * @param fieldValue
     * @param renderHiddenHtmlTag
     * @author
     */
    public FieldValue(Field field, String fieldValue, boolean renderHiddenHtmlTag)
    {
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
    public FieldValue(Field field, String fieldValue, boolean renderHiddenHtmlTag, int operator)
    {
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
    public FieldValue(Field field, String fieldValue, boolean renderHiddenHtmlTag, int operator, boolean isLogicalOR)
    {
        this.field = field;
        this.fieldValue = fieldValue;
        this.renderHiddenHtmlTag = renderHiddenHtmlTag;
        this.operator = operator;
        this.logicalOR = isLogicalOR;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getOperator()
    {
        return operator;
    }


    /**
     * DOCUMENT ME!
     *
     * @param field DOCUMENT ME!
     */
    public void setField(Field field)
    {
        this.field = field;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Field getField()
    {
        return field;
    }


    /**
     * DOCUMENT ME!
     *
     * @param fieldValue DOCUMENT ME!
     */
    public void setFieldValue(String fieldValue)
    {
        this.fieldValue = fieldValue;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFieldValue()
    {
        return fieldValue;
    }


    /**
     * DOCUMENT ME!
     *
     * @param sortDirection DOCUMENT ME!
     */
    public void setSortDirection(boolean sortDirection)
    {
        this.sortDirection = sortDirection;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getSortDirection()
    {
        return sortDirection;
    }


    /**
     * DOCUMENT ME!
     *
     * @param renderHiddenHtmlTag DOCUMENT ME!
     */
    public void setRenderHiddenHtmlTag(boolean renderHiddenHtmlTag)
    {
        this.renderHiddenHtmlTag = renderHiddenHtmlTag;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getRenderHiddenHtmlTag()
    {
        return renderHiddenHtmlTag;
    }


    /**
     * DOCUMENT ME!
     *
     * @param searchMode DOCUMENT ME!
     */
    public void setSearchMode(int searchMode)
    {
        this.searchMode = searchMode;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSearchMode()
    {
        return searchMode;
    }


    /**
     * DOCUMENT ME!
     *
     * @param searchAlgorithm DOCUMENT ME!
     */
    public void setSearchAlgorithm(int searchAlgorithm)
    {
        this.searchAlgorithm = searchAlgorithm;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSearchAlgorithm()
    {
        return searchAlgorithm;
    }


    /**
     * DOCUMENT ME!
     *
     * @param fv DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getWhereClause(FieldValue[] fv)
    {
        StringBuffer buf = new StringBuffer();

        if ((fv != null) && (fv.length > 0))
        {
            for (int i = 0; i < fv.length; i++)
            {
                // Depending on the value of isLogicalOR in FieldValue, prefix the filter definition
                // with either OR or AND (Skip first entry!)
                if (i != 0)
                {
                    if (fv[i].isLogicalOR())
                    {
                        buf.append(" OR ");
                    }
                    else
                    {
                        buf.append(" AND ");
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
                    buf.append("("); // §1, §6
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

                if ((i < (fv.length - 1)) && (fv[i + 1].getSearchMode() == mode))
                {
                    buf.append((mode == DbBaseHandlerTag.SEARCHMODE_AND) ? "AND " : "OR "); // §3
                }
                else
                {
                    //if(i==fv.length-1 || fv[i+1].getSearchMode()!=mode) {
                    buf.append(")"); // §4, §7

                    if (i != (fv.length - 1))
                    {
                        buf.append(" OR "); // §5 #checkme
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
    public static int populateWhereEqualsClause(FieldValue[] fv, PreparedStatement ps, int curCol) throws SQLException
    {
        if ((fv != null) && (fv.length > 0))
        {
            for (int i = 0; i < fv.length; i++)
            {
                fillPreparedStatement(fv[i], ps, curCol);
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
    public static String getWhereAfterClause(FieldValue[] fv, int compareMode)
    {
        String conj;
        String disj;
        String opA1;
        String opA2;
        String opB1;
        String opB2;

        if (compareMode == FieldValue.COMPARE_INCLUSIVE)
        {
            opA1 = ">=";
            opA2 = "<=";
            opB1 = ">";
            opB2 = "<";
            conj = " AND ";
            disj = " OR ";
        }
        else
        { // COMPARE_INCLUSIVE
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
                buf.append((fv[i].getSortDirection() == ORDER_ASCENDING) ? opA1 : opA2); // OpA
                buf.append(" ? ");

                // generate the "f(i-1) OpB(i-1) f(i-1)* OR f(i-2) OpB(i-2) f(i-2)* OR ... OR f1 OpB f1*"
                if (i > 0)
                {
                    for (int j = i - 1; j >= 0; j--)
                    {
                        buf.append(disj);
                        buf.append(fv[j].getField().getName());
                        buf.append((fv[j].getSortDirection() == ORDER_ASCENDING) ? opB1 : opB2); // OpB
                        buf.append(" ? ");
                    }
                }

                buf.append(" ) ");

                if (i < (fv.length - 1))
                {
                    buf.append(conj); // link the R's together (conjunction)
                }
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
    public static int populateWhereAfterClause(FieldValue[] fv, PreparedStatement ps, int curCol) throws SQLException
    {
        if ((fv != null) && (fv.length > 0))
        {
            // populate the Ri's
            for (int i = 0; i < fv.length; i++)
            {
                // populate a "fi OpA(i) fi*"
                logCat.info("setting col " + curCol);
                fillPreparedStatement(fv[i], ps, curCol);
                curCol++;

                // populate the "f(i-1) OpB(i-1) f(i-1)* OR f(i-2) OpB(i-2) f(i-2)* OR ... OR f1 OpB f1*"
                if (i > 0)
                {
                    for (int j = i - 1; j >= 0; j--)
                    {
                        logCat.info("setting col " + curCol);
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
    public static void invert(FieldValue[] fv)
    {
        for (int i = 0; i < fv.length; i++)
            fv[i].setSortDirection(!fv[i].getSortDirection());
    }


    private static void fillPreparedStatement(FieldValue cur, PreparedStatement ps, int curCol) throws SQLException
    {
        Field curField = cur.getField();
        String valueStr = cur.getFieldValue();

        logCat.info("setting " + cur.getField().getName() + " to value " + valueStr + " of type " + curField.getType());

        // 20020703-HKK: Extending search algorithm with WEAK_START, WEAK_END, WEAK_START_END
        //               results in like '%search', 'search%', '%search%'
        switch (cur.getSearchAlgorithm())
        {
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

        switch (cur.getOperator())
        {
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
    public Object clone()
    {
        try
        {
            return super.clone(); // shallow copy ;=)
        }
        catch (CloneNotSupportedException e)
        {
            // should not happen
        }

        return null;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
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
     * Insert the method's description here.
     * Creation date: (2001-09-11 15:39:36)
     * @return boolean
     */
    public boolean isLogicalOR()
    {
        return logicalOR;
    }


    /**
     * Insert the method's description here.
     * Creation date: (2001-09-11 15:39:36)
     * @param newLogicalOR boolean
     */
    public void setLogicalOR(boolean newLogicalOR)
    {
        logicalOR = newLogicalOR;
    }
}