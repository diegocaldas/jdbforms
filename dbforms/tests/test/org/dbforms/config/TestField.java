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
import org.dbforms.util.AbstractTestCase;



/**
 *  Description of the Class
 *
 * @author     epugh
 * @created    May 3, 2002
 */
public class TestField extends AbstractTestCase
{
   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testGetSetName() throws Exception
   {
      Field f = new Field();
      f.setName("FIELDNAME");
      assertTrue("Field name should be FIELDNAME", 
                 f.getName().equals("FIELDNAME"));
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testGetSetId() throws Exception
   {
      Field f = new Field();
      f.setId(10);
      assertTrue("Field id should be 10", f.getId() == 10);
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testGetSetAutoInc() throws Exception
   {
      Field f = new Field();
      f.setAutoInc("true");
      assertTrue("Field should be autoinc", f.isAutoInc());
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testGetSetAutoInc2() throws Exception
   {
      Field f = new Field();
      f.setAutoInc("YES");
      assertTrue("Field should be autoinc", f.isAutoInc());
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testSortable() throws Exception
   {
      Field f = new Field();
      f.setSortable("true");
      assertTrue("Should be True", f.isSortable());
      f.setSortable("TRUE");
      assertTrue("Should be True", f.isSortable());
      f.setSortable("yes");
      assertTrue("Should be True", f.isSortable());
      f.setSortable("FALSE");
      assertTrue("Should be false", !f.isSortable());
      f.setSortable("NO");
      assertTrue("Should be false", !f.isSortable());
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testType() throws Exception
   {
      Field f = new Field();
      f.setFieldType("int");
      assertTrue("Should be FieldTypes.INTEGER", 
                 f.getType() == FieldTypes.INTEGER);
      f.setFieldType("smallint");
      assertTrue("Should be FieldTypes.INTEGER", 
                 f.getType() == FieldTypes.INTEGER);
      f.setFieldType("tinyint");
      assertTrue("Should be FieldTypes.INTEGER", 
                 f.getType() == FieldTypes.INTEGER);
      f.setFieldType("TINYINT");
      assertTrue("Should be FieldTypes.INTEGER", 
                 f.getType() == FieldTypes.INTEGER);

      f.setFieldType("NUMERIC");
      assertTrue("Should be FieldTypes.NUMERIC", 
                 f.getType() == FieldTypes.NUMERIC);
      f.setFieldType("NUMBER");
      assertTrue("Should be FieldTypes.NUMERIC", 
                 f.getType() == FieldTypes.NUMERIC);

      f.setFieldType("char");
      assertTrue("Should be FieldTypes.CHAR", f.getType() == FieldTypes.CHAR);
      f.setFieldType("varchar");
      assertTrue("Should be FieldTypes.CHAR", f.getType() == FieldTypes.CHAR);
      f.setFieldType("nvarchar");
      assertTrue("Should be FieldTypes.CHAR", f.getType() == FieldTypes.CHAR);
      f.setFieldType("longchar");
      assertTrue("Should be FieldTypes.CHAR", f.getType() == FieldTypes.CHAR);
      f.setFieldType("text");
      assertTrue("Should be FieldTypes.CHAR", f.getType() == FieldTypes.CHAR);

      f.setFieldType("date");
      assertTrue("Should be FieldTypes.DATE", f.getType() == FieldTypes.DATE);

      f.setFieldType("timestamp");
      assertTrue("Should be FieldTypes.TIMESTAMP", 
                 f.getType() == FieldTypes.TIMESTAMP);

      f.setFieldType("time");
      assertTrue("Should be FieldTypes.TIME", f.getType() == FieldTypes.TIME);

      f.setFieldType("double");
      assertTrue("Should be FieldTypes.DOUBLE", 
                 f.getType() == FieldTypes.DOUBLE);
      f.setFieldType("float");
      assertTrue("Should be FieldTypes.DOUBLE", 
                 f.getType() == FieldTypes.DOUBLE);

      f.setFieldType("real");
      assertTrue("Should be FieldTypes.FLOAT", f.getType() == FieldTypes.FLOAT);

      f.setFieldType("blob");
      assertTrue("Should be FieldTypes.BLOB", f.getType() == FieldTypes.BLOB);
      f.setFieldType("image");
      assertTrue("Should be FieldTypes.BLOB", f.getType() == FieldTypes.BLOB);

      f.setFieldType("diskblob");
      assertTrue("Should be FieldTypes.DISKBLOB", 
                 f.getType() == FieldTypes.DISKBLOB);
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testType2() throws Exception
   {
      Field f = new Field();
      f.setTypeByObject(java.lang.Integer.class);
      assertTrue("Should be FieldTypes.INTEGER", 
                 f.getType() == FieldTypes.INTEGER);
      f.setTypeByObject(java.lang.Long.class);
      assertTrue("Should be FieldTypes.INTEGER", 
                 f.getType() == FieldTypes.INTEGER);
      f.setTypeByObject(java.lang.String.class);
      assertTrue("Should be FieldTypes.CHAR", f.getType() == FieldTypes.CHAR);
      f.setTypeByObject(java.math.BigDecimal.class);
      assertTrue("Should be FieldTypes.NUMERIC", 
                 f.getType() == FieldTypes.NUMERIC);
      f.setTypeByObject(java.sql.Date.class);
      assertTrue("Should be FieldTypes.DATE", f.getType() == FieldTypes.DATE);
      f.setTypeByObject(java.sql.Time.class);
      assertTrue("Should be FieldTypes.TIME", f.getType() == FieldTypes.TIME);
      f.setTypeByObject(java.sql.Timestamp.class);
      assertTrue("Should be FieldTypes.TIMESTAMP", 
                 f.getType() == FieldTypes.TIMESTAMP);
      f.setTypeByObject(java.lang.Double.class);
      assertTrue("Should be FieldTypes.DOUBLE", 
                 f.getType() == FieldTypes.DOUBLE);
      f.setTypeByObject(java.lang.Float.class);
      assertTrue("Should be FieldTypes.FLOAT", f.getType() == FieldTypes.FLOAT);
   }
}