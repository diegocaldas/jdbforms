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
public class TestFieldValue extends TestCase
{
    Field fAuthorId = null;
    Field fBookId = null;
    FieldValue fvLogicalOR = null;
    FieldValue fvNotLogicalOR = null;
    FieldValue fvLogicalORLikeFilter = null;

    /**
     * Creates a new TestFieldValue object.
     *
     * @param name DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public TestFieldValue(String name) throws Exception
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
        fAuthorId = new Field();
        fAuthorId.setName("AUTHOR_ID");
        fBookId = new Field();
        fBookId.setName("BOOK_ID");

        fvLogicalOR = new FieldValue(fAuthorId, "10", false, FieldValue.FILTER_EQUAL, true);
        fvNotLogicalOR = new FieldValue(fBookId, "10", false, FieldValue.FILTER_EQUAL, false);
        fvLogicalORLikeFilter = new FieldValue(fAuthorId, "10", false, FieldValue.FILTER_LIKE, true);
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
    public void testCon() throws Exception
    {
        FieldValue fv = new FieldValue(fAuthorId, "10", false);
        assertTrue("make sure we get our field back.", fv.getField().equals(fAuthorId));
        assertTrue("make sure we get our field value.", fv.getFieldValue().equals("10"));
    }


    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void testLogicalOr() throws Exception
    {
        FieldValue fv = new FieldValue(fAuthorId, "10", false);
        assertTrue("Make sure this is not a logical OR.", !fv.isLogicalOR());
        fv.setLogicalOR(true);
        assertTrue("make sure this is a logical OR.", fv.isLogicalOR());
    }


    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void testGetWhereClause() throws Exception
    {
        FieldValue[] fvs = { fvLogicalOR, fvNotLogicalOR };
        assertTrue("Test fvs Where clause equals:AUTHOR_ID =  ?  AND BOOK_ID =  ?:" + FieldValue.getWhereClause(fvs), FieldValue.getWhereClause(fvs).indexOf("AUTHOR_ID =  ?  AND BOOK_ID =  ?") >= 0);

        FieldValue[] fvs2 = { fvLogicalOR, fvLogicalOR };
        assertTrue("Test fvs2 Where clause equals:" + FieldValue.getWhereClause(fvs2), FieldValue.getWhereClause(fvs2).indexOf("AUTHOR_ID =  ?  OR AUTHOR_ID =  ?") >= 0);

        FieldValue[] fvs3 = { fvLogicalORLikeFilter, fvLogicalOR };
        assertTrue("Test fvs3 Where clause equals:" + FieldValue.getWhereClause(fvs3), FieldValue.getWhereClause(fvs3).indexOf("AUTHOR_ID LIKE  ?  OR AUTHOR_ID =  ?") >= 0);
    }


    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void testClose() throws Exception
    {
        FieldValue fv = new FieldValue(fAuthorId, "10", false);

        FieldValue fvClone = (FieldValue) fv.clone();
        assertTrue("Make sure toStrings match", fv.toString().equals(fvClone.toString()));
    }


    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void testInvert() throws Exception
    {
        FieldValue fvLogicalOROrig = (FieldValue) fvLogicalOR.clone();
        FieldValue fvNotLogicalOROrig = (FieldValue) fvNotLogicalOR.clone();
        FieldValue fvLogicalORLikeFilterOrig = (FieldValue) fvLogicalORLikeFilter.clone();

        FieldValue[] fvs = { fvLogicalOR, fvNotLogicalOR, fvLogicalORLikeFilter };

        FieldValue.invert(fvs);
        assertTrue("fv was flipped", fvLogicalOR.getSortDirection() == (!fvLogicalOROrig.getSortDirection()));
        assertTrue("fv was flipped", fvNotLogicalOR.getSortDirection() == (!fvNotLogicalOROrig.getSortDirection()));
        assertTrue("fv was flipped", fvLogicalORLikeFilter.getSortDirection() == (!fvLogicalORLikeFilterOrig.getSortDirection()));
    }
}