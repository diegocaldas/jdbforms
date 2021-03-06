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

import org.dbforms.util.Util;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;



/**
 * <p>
 * This class represents the query tag in dbforms-config.xml (dbforms config
 * xml file)
 * </p>
 *
 * <p>
 * it's derived from the table class and overloads the necessary methods.
 * </p>
 *
 * @author Henner Kollmann
 */
public class Query extends Table {
   /** log4j category */
   private static Log       logCat           = LogFactory.getLog(Query.class);
   private String           distinct         = "false";
   private String           followAfterWhere = "AND";
   private String           from;
   private String           groupBy;
   private String           having;
   private String           orderWithPos     = "false";
   private String           where;
   private Hashtable        searchNameHash  = new Hashtable();
   private Vector           searchfields = new Vector(); // the Field-Objects this table constists of


   /**
    * DOCUMENT ME!
    *
    * @param value the value to set
    */
   public void setDistinct(String value) {
      distinct = value;
   }


   /**
    * returns the Field-Objet with specified id overloaded from Table Specials:
    * 1. if fieldId is in range from search fields, get from search fields 2.
    * if has fields try to find in fields 3. if not has fields try to find in
    * parent table
    *
    * @param fieldId The id of the field to be returned
    *
    * @return the field
    */
   public Field getField(int fieldId) {
      Field f = null;
      if (checkFieldId(SEARCH_FIELD, fieldId)) {
         f = (Field) searchfields.elementAt(decodeFieldId(SEARCH_FIELD, fieldId));
      } else {
         try {
            f = super.getField(fieldId);
         } catch (RuntimeException e) {
            f = null;
         }

         if ((f == null) && !Util.isNull(from)) {
            if (getConfig() != null) {
               Table t = getConfig().getTableByName(from);

               if (t != null) {
                  f = t.getField(fieldId);
               }
            }
         }
      }

      return f;
   }


   /**
    * returns the field-objects as specified by name (or null if no field with
    * the specified name exists in this table) overloaded from Table Specials:
    * 1. Try to find in fields 2. Try to find in search fields 3. Try to find
    * in parent table
    *
    * @param name The name of the field
    *
    * @return the field
    */
   public Field getFieldByName(String name) {
      Field f = super.getFieldByName(name);

      if (f == null) {
         f = (Field) searchNameHash.get(name);
      }

      if ((f == null) && !Util.isNull(from)) {
         if (getConfig() != null) {
            Table t = getConfig().getTableByName(from);

            if (t != null) {
               f = t.getFieldByName(name);
            }
         }
      }

      return f;
   }
   

   /**
    * returns the vector of fields this table constists of overloaded from
    * Table Specials: if view has field defined, use this otherwise use fields
    * from parent table
    *
    * @return the fields
    */
   public Vector getFields() {
      // In this case there are no fields listed. So use the fieldlist of the
      // parent table!
      Vector f = super.getFields();

      if ((f == null) || ((f.isEmpty()) && !Util.isNull(from))) {
         if (getConfig() != null) {
            Table t = getConfig().getTableByName(from);

            if (t != null) {
               f = t.getFields();
            }
         }
      }

      return f;
   }


   /**
    * Sets the followAfterWhere.
    *
    * @param followAfterWhere The followAfterWhere to set
    */
   public void setFollowAfterWhere(String followAfterWhere) {
      this.followAfterWhere = followAfterWhere;
   }


   /**
    * set from, if defined in dbforms-config-xml (this method gets called from
    * XML-digester)
    *
    * @param value sql from
    */
   public void setFrom(String value) {
      this.from = value;
   }


   /**
    * set groupBy, if defined in dbforms-config-xml (this method gets called
    * from XML-digester)
    *
    * @param value sql group by
    */
   public void setGroupBy(String value) {
      this.groupBy = value;
   }


   /**
    * DOCUMENT ME!
    *
    * @param value the value to set
    */
   public void setHaving(String value) {
      having = value;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getHaving() {
      return having;
   }


   /**
    * returns the key of this table (consisting of Field-Objects representing
    * key-fields) overloaded from Table Specials: if key of view is not
    * defined (if view has not defined fields) use keys from parent table
    *
    * @return the keys
    */
   public Vector getKey() {
      Vector v = super.getKey();

      if (((v == null) || v.isEmpty()) && !Util.isNull(from)) {
         if (getConfig() != null) {
            Table t = getConfig().getTableByName(from);

            if (t != null) {
               v = t.getKey();
            }
         }
      }

      return v;
   }


   /**
    * DOCUMENT ME!
    *
    * @param core starting tag for the fields
    *
    * @return the hash table. Hashtables are build from fields + searchfields!
    */
   public Hashtable getNamesHashtable(String core) {
      Hashtable   result = super.getNamesHashtable(core);
      Enumeration e = getSearchFields()
                            .elements();

      while (e.hasMoreElements()) {
         Field f = (Field) e.nextElement();
         result.put(f.getName(), f.getFieldName(core));

         // in PHP slang we would call that an "associative array" :=)
      }

      return result;
   }


   /**
    * set OrderWithPos, if defined in dbforms-config-xml (this method gets
    * called from XML-digester) if set the ORDER BY statment will use position
    * number instead of field names in ORDER BY
    *
    * @param value sets orderWithPos
    */
   public void setOrderWithPos(String value) {
      this.orderWithPos = value;
   }


   /**
    * returns the from part of a query. overloaded from Table if from is defined
    * in dbforms-config.xml use this, else method from Table. Used in Delete/Insert/Update.
    *
    * @return sql from
    */
   public String getQueryFrom() {
      String res;

      // DJH - Allow use of the alias attribute (as gotten through super.getQueryFrom()) if Query element
      //       does not have a simple table in the from attribute. This is detected by
      //       there being spaces in the trimmed from attribute string. e.g. from="t1 join t2 on (t1.id = t2.id)"
      if (!Util.isNull(from) && from.trim().indexOf(" ") == -1) {
          res = from;
      } else {
         res = super.getQueryFrom();
      }
   	  return res;
   }

   /**
    * returns the from part of a query. overloaded from Table if from is defind
    * in dbforms-config.xml use this, else method from Table. Used in Select
    *
    * @return sql from
    */
   public String getSelectQueryFrom() {
      String res;

      if (!Util.isNull(from)) {
          res = from;
      } else {
         res = super.getQueryFrom();
      }
      return res;
   }

   /**
    * returns the select part of a query overloaded from Table extends
    * fieldnames with getting expression
    *
    * @param fieldsToSelect fieldlist
    *
    * @return sql select part
    */
   public String getQuerySelect(Vector fieldsToSelect) {
      if (fieldsToSelect != null) {
         StringBuffer buf                = new StringBuffer();
         int          fieldsToSelectSize = fieldsToSelect.size();

         for (int i = 0; i < fieldsToSelectSize; i++) {
            Field f = (Field) fieldsToSelect.elementAt(i);

            // if field has an expression use it!
            if (!Util.isNull(f.getExpression())) {
               buf.append(f.getExpression());
               buf.append(" ");
            }

            buf.append(f.getName());
            buf.append(", ");
         }

         if (buf.length() > 1) {
            buf.deleteCharAt(buf.length() - 2);
         }

         return buf.toString();
      }

      return "*";
   }


   /**
    * Returns the search fields search fields are fields in the query which are
    * only used in the where part, not in the select part
    *
    * @return search field list
    */
   public Vector getSearchFields() {
      return searchfields;
   }


   /**
    * Prepares the Querystring for the select statement Order of parts: 1.
    * where condition from config (no params!) 2. sqlFilter (fild in
    * getDoSelectResultSet!) 3. where condition generated from search fields
    * (fild in overloaded populateWhereEqualsClause) 4. where condition
    * generated from having / ordering fields (fild in overloaded
    * populateWhereEqualsClause) Retrieving the parameters in
    * getDoSelectResultSet() must match this order!
    *
    * @param fieldsToSelect vector of fields to be selected
    * @param fvEqual fieldValues representing values we are looking for
    * @param fvOrder fieldValues representing needs for order clauses
    * @param sqlFilter sql condition to and with the where clause
    * @param compareMode compare mode value for generating the order clause
    *
    * @return the query string
    */
   public String getSelectQuery(Vector       fieldsToSelect,
                                FieldValue[] fvEqual,
                                FieldValue[] fvOrder,
                                String       sqlFilter,
                                int          compareMode) {
      StringBuffer buf                      = new StringBuffer();
      String       s;
      boolean      hatSchonWhere            = false;
      boolean      hatSchonFollowAfterWhere = false;
      boolean      hatSchonHaving           = false;
      FieldValue[] fvHaving                 = getFieldValueHaving(fvEqual);
      FieldValue[] fvWhere                  = getFieldValueWhere(fvEqual);

      buf.append("SELECT ");

      if (hasDistinctSet()) {
         buf.append(" DISTINCT ");
      }

      buf.append(getQuerySelect(fieldsToSelect));
      buf.append(" FROM ");
      buf.append(getSelectQueryFrom());

      s = getQueryWhere(fvWhere, null, 0);

      if (!Util.isNull(s) || !Util.isNull(where) || !Util.isNull(sqlFilter)) {
         hatSchonWhere = true;
         buf.append(" WHERE ");

         // where condition part from config
         if (!Util.isNull(where)) {
            buf.append("( ");
            buf.append(where);
            buf.append(" ) ");
         }

         // where condition part from DbFormTag's sqlFilter attribute
         if (!Util.isNull(sqlFilter)) {
            if (!Util.isNull(where)) {
               hatSchonFollowAfterWhere = true;
               buf.append(" ");
               buf.append(followAfterWhere);
               buf.append(" ");
            }

            buf.append(" ( ");
            buf.append(sqlFilter);
            buf.append(" ) ");
         }

         // where condition part generated from searching / ordering
         if (!Util.isNull(s)) {
            if (!Util.isNull(sqlFilter)) {
               buf.append(" AND ");
            } else if (!Util.isNull(where)) {
               hatSchonFollowAfterWhere = true;
               buf.append(" ");
               buf.append(followAfterWhere);
               buf.append(" ");
            }

            // parents are inserted in getQueryWhere method
            buf.append(" ( ");
            buf.append(s);
            buf.append(" ) ");
         }
      }

      if (!Util.isNull(groupBy)) {
         buf.append(" GROUP BY ");
         buf.append(groupBy);
      }

      s = getQueryWhere(fvHaving, fvOrder, compareMode);

      if (!Util.isNull(s)) {
         if (!Util.isNull(groupBy)) {
            buf.append(" HAVING ( ");
            hatSchonHaving = true;
         } else if (!hatSchonWhere) {
            buf.append(" WHERE ( ");
         } else {
            if (!Util.isNull(where) && !hatSchonFollowAfterWhere && !Util.isNull(followAfterWhere)) {
               buf.append(" ");
               buf.append(followAfterWhere);
               buf.append(" (");
            } else {
               buf.append(" AND (");
            }
         }

         buf.append(s);
         buf.append(")");
      }

      if (!Util.isNull(groupBy) && !Util.isNull(getHaving())) {
         if (!hatSchonHaving) {
            buf.append(" HAVING ");
         } else {
            buf.append(" AND ");
         }

         buf.append("(");
         buf.append(getHaving());
         buf.append(") ");
      }

      s = getQueryOrderBy(fvOrder);

      if (s.length() > 0) {
         buf.append(" ORDER BY ");
         buf.append(s);
      }

      logCat.info("doSelect:" + buf.toString());

      return buf.toString();
   }


   /**
    * set whereClause, if defined in dbforms-config-xml (this method gets
    * called from XML-digester)
    *
    * @param value sql where
    */
   public void setWhere(String value) {
      this.where = value;
   }


   /**
    * adds a Field-Object to this table and puts it into othere datastructure
    * for further references (this method gets called from DbFormsConfig)
    *
    * @param field field to add
    *
    * @throws Exception DOCUMENT ME!
    */
   public void addSearchField(Field field) throws Exception {
      if (field.getType() == 0) {
          throw new Exception("Table " + getName() + " Field " + field.getName() + ": no type!");
      }

      field.setId(encodeFieldId(SEARCH_FIELD, searchfields.size()));
      field.setTable(this);
      searchfields.addElement(field);

      // for quicker lookup by name:
      searchNameHash.put(field.getName(), field);
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public boolean hasDistinctSet() {
      return Util.getTrue(distinct);
   }


   /**
    * return OrderWithPos OrderWithPos will be set if - groupBy is set -
    * OrderWithPos is defined in dbforms-config.xml
    *
    * @return orderWithPos
    */
   public boolean needOrderWithPos() {
      return !Util.isNull(groupBy) || Util.getTrue(orderWithPos);
   }


   /**
    * situation: we have built a query (involving the getWhereEqualsClause()
    * method) and now we want to prepare the statemtent - provide actual
    * values for the the '?' placeholders
    *
    * @param fvEqual the array of FieldValue objects
    * @param ps the PreparedStatement object
    * @param curCol the current PreparedStatement column; points to a
    *        PreparedStatement xxx value
    *
    * @return the current column value
    *
    * @exception SQLException if any error occurs
    */
   public int populateWhereEqualsClause(FieldValue[]      fvEqual,
                                        PreparedStatement ps,
                                        int               curCol)
                                 throws SQLException {
      curCol = super.populateWhereEqualsClause(getFieldValueWhere(fvEqual), ps,
                                               curCol);
      curCol = super.populateWhereEqualsClause(getFieldValueHaving(fvEqual),
                                               ps, curCol);

      return curCol;
   }


   /**
    * returns the part of the orderby-clause represented by this FieldValue
    * object. FieldName [DESC] (ASC will be not printed because it is defined
    * DEFAULT in SQL if there are RDBMS which do not tolerate this please let
    * me know; then i'll change it) overloaded from Table if from is defind in
    * dbforms-config.xml use this, else method from Table
    *
    * @param fvOrder order list
    *
    * @return sql order by
    */
   protected String getQueryOrderBy(FieldValue[] fvOrder) {
      String res;
   	  if (!needOrderWithPos()) {
         res = super.getQueryOrderBy(fvOrder);
      } else {
         StringBuffer buf = new StringBuffer();

         if (fvOrder != null) {
            for (int i = 0; i < fvOrder.length; i++) {
               buf.append(fvOrder[i].getField().getId() + 1);

               if (fvOrder[i].getSortDirection() == Constants.ORDER_DESCENDING) {
                  buf.append(" DESC");
               }

               if (i < (fvOrder.length - 1)) {
                  buf.append(",");
               }
            }
         }
         res =  buf.toString();
      }
   	  return res;
   }


   private FieldValue[] getFieldValueHaving(FieldValue[] fvEqual) {
      Vector mode_having = new Vector();

      // Split fields in where and having part
      if (fvEqual != null) {
         for (int i = 0; i < fvEqual.length; i++) {
            if (!checkFieldId(SEARCH_FIELD, fvEqual[i].getField().getId())) {
               mode_having.add(fvEqual[i]);
            }
         }
      }

      FieldValue[] fvHaving = new FieldValue[mode_having.size()];

      for (int i = 0; i < mode_having.size(); i++) {
         fvHaving[i] = (FieldValue) mode_having.elementAt(i);
      }

      return fvHaving;
   }


   private FieldValue[] getFieldValueWhere(FieldValue[] fvEqual) {
      Vector mode_where = new Vector();

      // Split fields in where and having part
      if (fvEqual != null) {
         for (int i = 0; i < fvEqual.length; i++) {
            if (checkFieldId(SEARCH_FIELD, fvEqual[i].getField().getId())) {
               mode_where.add(fvEqual[i]);
            }
         }
      }

      FieldValue[] fvWhere = new FieldValue[mode_where.size()];

      for (int i = 0; i < mode_where.size(); i++) {
         fvWhere[i] = (FieldValue) mode_where.elementAt(i);
      }

      return fvWhere;
   }
}
