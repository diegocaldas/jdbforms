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
import junit.framework.*;
import org.dbforms.util.FieldTypes;



/**
 *  Description of the Class
 *
 * @author     epugh
 * @created    May 3, 2002
 */
public class TestField extends TestCase
{
    /**
     * Creates a new TestField object.
     *
     * @param name DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public TestField(String name) throws Exception
    {
        super(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void setUp() throws Exception
    {
        super.setUp();
    }


    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void tearDown() throws Exception
    {
        super.tearDown();
    }


    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void testGetSetName() throws Exception
    {
        Field f = new Field();
        f.setName("FIELDNAME");
        assertTrue("Field name should be FIELDNAME", f.getName().equals("FIELDNAME"));
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
        assertTrue("Field should be autoinc", f.getIsAutoInc());
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
        assertTrue("Field should be autoinc", f.getIsAutoInc());
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
        assertTrue("Should be True", f.isFieldSortable());
        f.setSortable("TRUE");
        assertTrue("Should be True", f.isFieldSortable());
        f.setSortable("yes");
        assertTrue("Should be True", f.isFieldSortable());
        f.setSortable("FALSE");
        assertTrue("Should be false", !f.isFieldSortable());
        f.setSortable("NO");
        assertTrue("Should be false", !f.isFieldSortable());
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
        assertTrue("Should be FieldTypes.INTEGER", f.getType() == FieldTypes.INTEGER);
        f.setFieldType("smallint");
        assertTrue("Should be FieldTypes.INTEGER", f.getType() == FieldTypes.INTEGER);
        f.setFieldType("tinyint");
        assertTrue("Should be FieldTypes.INTEGER", f.getType() == FieldTypes.INTEGER);
        f.setFieldType("TINYINT");
        assertTrue("Should be FieldTypes.INTEGER", f.getType() == FieldTypes.INTEGER);

        f.setFieldType("NUMERIC");
        assertTrue("Should be FieldTypes.NUMERIC", f.getType() == FieldTypes.NUMERIC);
        f.setFieldType("NUMBER");
        assertTrue("Should be FieldTypes.NUMERIC", f.getType() == FieldTypes.NUMERIC);

        f.setFieldType("char");
        assertTrue("Should be FieldTypes.CHAR", f.getType() == FieldTypes.CHAR);
        f.setFieldType("varchar");
        assertTrue("Should be FieldTypes.CHAR", f.getType() == FieldTypes.CHAR);
        f.setFieldType("nvarchar");
        assertTrue("Should be FieldTypes.CHAR", f.getType() == FieldTypes.CHAR);
        f.setFieldType("longchar");
        assertTrue("Should be FieldTypes.CHAR", f.getType() == FieldTypes.CHAR);

        f.setFieldType("diskblob");
        assertTrue("Should be FieldTypes.DISKBLOB", f.getType() == FieldTypes.DISKBLOB);
    }
}