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
 * This class represents a view tag in dbforms-config.xml (dbforms config xml file)
 * </p>
 *
 * <p>
 * it's derived from the table class and overloads the necessary methods.
 * </p>
 *
 * @author Henner Kollmann (Henner.Kollmann@gmx.de)
 */
import java.util.*;
import org.dbforms.util.*;


/**
 * DOCUMENT ME!
 *
 * @version $Revision$
 * @author $author$
 */
public class Query extends Table
{
    private String from;
    private String groupBy;
    private String where;
    private Vector searchfields; // the Field-Objects this table constists of
    private Hashtable searchNameHash;
    private static final int WHEREIDSTART = 1000;
    private String orderWithPos = "false";
    private String followAfterWhere = " AND ";
    private String distinct = "false";

    /**
     * Constructor for View.
     */
    public Query()
    {
        super();
        searchfields = new Vector();
        searchNameHash = new Hashtable();
    }

    /**
     * adds a Field-Object to this table
     * and puts it into othere datastructure for further references
     * (this method gets called from DbFormsConfig)
     */
    public void addSearchField(Field field)
    {
        field.setId(WHEREIDSTART + searchfields.size());
        searchfields.addElement(field);


        // for quicker lookup by name:
        searchNameHash.put(field.getName(), field);
    }


    /**
     * set from, if defined in dbforms-config-xml
     * (this method gets called from XML-digester)
    */
    public void setFrom(String value)
    {
        this.from = value;
    }


    /**
     * set groupBy, if defined in dbforms-config-xml
     * (this method gets called from XML-digester)
    */
    public void setGroupBy(String value)
    {
        this.groupBy = value;
    }


    /**
     * set whereClause, if defined in dbforms-config-xml
     * (this method gets called from XML-digester)
    */
    public void setWhere(String value)
    {
        this.where = value;
    }

    /**
     * set OrderWithPos, if defined in dbforms-config-xml
     * (this method gets called from XML-digester)
     * if set the ORDER BY statment will use position number instead of 
     * field names in ORDER BY
    */
    public void setOrderWithPos(String value)
    {
        this.orderWithPos = value;
    }

    /**
     * return OrderWithPos
     * OrderWithPos will be set if
     *    - groupBy is set
     *    - OrderWithPos is defined in dbforms-config.xml
    */
    public boolean needOrderWithPos()
    {
        return !Util.isNull(groupBy) || orderWithPos.equalsIgnoreCase("true") || orderWithPos.equalsIgnoreCase("yes");
    }


    /**
     *  Returns the search fields
     * 
     *  search fields are fields in the query which are only used in the where part, 
     *  not in the select part
     * 
     */
    public Vector getSearchFields()
    {
        return searchfields;
    }


    /**
     * returns the from part of a query. 
     * 
     * overloaded from Table
     * if from is defind in dbforms-config.xml use this, else method from Table
     */
    protected String getQueryFrom()
    {
        if (!Util.isNull(from))
        {
            return from;
        }
        else
        {
            return super.getQueryFrom();
        }
    }


    /**
     * returns the part of the orderby-clause represented by this FieldValue object.
     * FieldName [DESC]
     * (ASC will be not printed because it is defined DEFAULT in SQL
     * if there are RDBMS which do not tolerate this please let me know; then i'll
     * change it)
     * 
     * overloaded from Table
     * if from is defind in dbforms-config.xml use this, else method from Table
     */
    protected String getQueryOrderBy(FieldValue[] fvOrder)
    {
        if (!needOrderWithPos())
        {
            return super.getQueryOrderBy(fvOrder);
        }
        else
        {
            StringBuffer buf = new StringBuffer();

            if (fvOrder != null)
            {
                for (int i = 0; i < fvOrder.length; i++)
                {
                    buf.append(fvOrder[i].getField().getId() + 1);

                    if (fvOrder[i].getSortDirection() == FieldValue.ORDER_DESCENDING)
                    {
                        buf.append(" DESC");
                    }

                    if (i < (fvOrder.length - 1))
                    {
                        buf.append(",");
                    }
                }
            }

            return buf.toString();
        }
    }


    /**
     * returns the select part of a query
     * overloaded from Table
     * if from is defind in dbforms-config.xml use this, else method from Table
     * 
     * extends field names with expressions: 
     * expression as name
     */
    protected String getQuerySelect(Vector fieldsToSelect)
    {
        StringBuffer buf = new StringBuffer();
        int fieldsToSelectSize = fieldsToSelect.size();

        for (int i = 0; i < fieldsToSelectSize; i++)
        {
            Field f = (Field) fieldsToSelect.elementAt(i);

            // if field has an expression use it!
            if ((f.getExpression() != null) && (f.getExpression().length() > 0))
            {
                buf.append(f.getExpression());
                buf.append(" ");
            }

            buf.append(f.getName());
            buf.append(", ");
        }

        if (buf.length() > 1)
        {
            buf.deleteCharAt(buf.length() - 2);
        }

        return buf.toString();
    }


    /**
     * Prepares the Querystring for the select statement
     * if the statement is for a sub-form (=> doConstrainedSelect),
     * we set some place holders for correct mapping
     *
     * @param fieldsToSelect - vector of fields to be selected
     * @param fvEqual - fieldValues representing values we are looking for
     *    @param fvOrder - fieldValues representing needs for order clauses
     *    @param compareMode - and / or
     *
     * overloaded from Table
     * if from is defind in dbforms-config.xml use this, else method from Table
     * 
     * extends select query with:
     *    group by
     *    where clause special from select fields
     * 
     */
    protected String getSelectQuery(Vector fieldsToSelect, FieldValue[] fvEqual, FieldValue[] fvOrder, int compareMode)
    {
        StringBuffer buf = new StringBuffer();
        String s;
        boolean HatSchonWhere = false;
        boolean HatSchonFollowAfterWhere = false;
        Vector mode_having = new Vector();
        Vector mode_where = new Vector();

        // Split fields in where and having part
        if (fvEqual != null)
        {
            for (int i = 0; i < fvEqual.length; i++)
            {
                if (fvEqual[i].getField().getId() >= WHEREIDSTART)
                {
                    mode_where.add(fvEqual[i]);
                }
                else
                {
                    mode_having.add(fvEqual[i]);
                }
            }
        }

        FieldValue[] fvHaving = new FieldValue[mode_having.size()];

        for (int i = 0; i < mode_having.size(); i++)
        {
            fvHaving[i] = (FieldValue) mode_having.elementAt(i);
        }

        FieldValue[] fvWhere = new FieldValue[mode_where.size()];

        for (int i = 0; i < mode_where.size(); i++)
        {
            fvWhere[i] = (FieldValue) mode_where.elementAt(i);
        }

        buf.append("SELECT ");
        if ( distinct.equalsIgnoreCase("true") || distinct.equalsIgnoreCase("yes") ) {
           buf.append(" DISTINCT ");
        }
        buf.append(getQuerySelect(fieldsToSelect));
        buf.append(" FROM ");
        buf.append(getQueryFrom());
        s = getQueryWhere(fvWhere, null, compareMode);

        if ((s.length() > 0) || !Util.isNull(where) )
        {
            HatSchonWhere = true;
            buf.append(" WHERE ");

            if (!Util.isNull(where))
            {
                buf.append(where);
            }

            if (s.length() > 0)
            {
                buf.append(" ");

                if (!Util.isNull(where))
                {
                    HatSchonFollowAfterWhere = true;
                    buf.append(getFollowAfterWhere());
                }

                buf.append(" ");
                buf.append("(");
                buf.append(s);
                buf.append(")");
            }
        }

        if (!Util.isNull(groupBy))
        {
            buf.append(" GROUP BY ");
            buf.append(groupBy);
        }

        s = getQueryWhere(fvHaving, fvOrder, compareMode);

        if (s.length() > 0)
        {
            if (!Util.isNull(groupBy))
            {
                buf.append(" HAVING ( ");
            }
            else if (!HatSchonWhere)
            {
                buf.append(" WHERE ( ");
            }
            else
            {
                if (!Util.isNull(where) && !HatSchonFollowAfterWhere)
                {
                    buf.append(" ");
                    buf.append(getFollowAfterWhere());
                    buf.append(" ");
                }

                buf.append(" ( ");
            }

            buf.append(s);
            buf.append(")");
        }

        s = getQueryOrderBy(fvOrder);

        if (s.length() > 0)
        {
            buf.append(" ORDER BY ");
            buf.append(s);
        }

        logCat.info("doSelect:" + buf.toString());

        return buf.toString();
    }


    /**
     * returns the vector of fields this table constists of
     * 
     * overloaded from Table
     * Specials:
     * if view has field defined, use this otherwise use fields from parent table
     * 
     */
    public Vector getFields()
    {
        // In this case there are no fields listed. So use the fieldlist of the parent table!
        Vector f = super.getFields();

        if ((f == null) || (f.isEmpty()) && !Util.isNull(from) )
        {
            Table t = config.getTableByName(from);
            f = t.getFields();
        }

        return f;
    }


    /**
     * returns the field-objects as specified by name (or null if no field with
     * the specified name exists in this table)
     * @param name The name of the field
     * 
     * overloaded from Table
     * Specials:
     *    1. Try to find in fields
     *    2. Try to find in search fields
     *    3. Try to find in parent table
     */
    public Field getFieldByName(String name)
    {
        Field f = super.getFieldByName(name);

        if (f == null)
        {
            f = (Field) searchNameHash.get(name);
        }

        if ((f == null) && !Util.isNull(from) )
        {
            f = config.getTableByName(from).getFieldByName(name);
        }

        return f;
    }


    /**
     * returns the Field-Objet with specified id
     *
     * @param fieldId The id of the field to be returned
     * 
     * overloaded from Table
     * Specials:
     *    1. if fieldId is in range from search fields, get from search fields
     *    2. if has fields try to find in fields
     *    3. if not has fields try to find in parent table
    */
    public Field getField(int fieldId)
    {
        Field f = null;

        if (fieldId >= WHEREIDSTART)
        {
            f = (Field) searchfields.elementAt(fieldId - WHEREIDSTART);
        }
        else
        {
            try
            {
                f = super.getField(fieldId);
            }
            catch (RuntimeException e)
            {
                f = null;
            }
            if ((f == null) && !Util.isNull(from))
            {
                f = config.getTableByName(from).getField(fieldId);
            }
        }

        return f;
    }


    /**
     * returns the key of this table (consisting of Field-Objects representing key-fields)
     * 
     * overloaded from Table
     * Specials:
     *    if key of view is not defined (if view has not defined fields) use keys from parent table
     */
    public Vector getKey()
    {
        Vector v = super.getKey();

        if ( ((v == null) || v.isEmpty()) && !Util.isNull(from) )
        {
            v = config.getTableByName(from).getKey();
        }

        return v;
    }


    /**
     * returns the hash table. Hashtables are build from fields + searchfields!
     */
    public Hashtable getNamesHashtable(String core)
    {
        Hashtable result = super.getNamesHashtable(core);
        Enumeration enum = getSearchFields().elements();

        while (enum.hasMoreElements())
        {
            Field f = (Field) enum.nextElement();
            String fieldName = f.getName();
            int fieldId = f.getId();
            StringBuffer sb = new StringBuffer(core);
            sb.append("_");
            sb.append(getId());
            sb.append("_");
            sb.append(fieldId);
            result.put(fieldName, sb.toString());

            // in PHP slang we would call that an "associative array" :=)
        }

        return result;
    }


    /**
     * Returns the followAfterWhere.
     * @return String
     */
    public String getFollowAfterWhere()
    {
        return followAfterWhere;
    }


    /**
     * Sets the followAfterWhere.
     * @param followAfterWhere The followAfterWhere to set
     */
    public void setFollowAfterWhere(String followAfterWhere)
    {
        this.followAfterWhere = followAfterWhere;
    }

   /**
    * Sets the distinct.
    * @param distinct The distinct to set
    */
   public void setDistinct(String distinct) {
      this.distinct = distinct;
   }

}