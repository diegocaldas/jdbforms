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
import junit.framework.*;



/**
 *  Description of the Class
 *
 * @author     epugh
 * @created    May 3, 2002
 */
public class TestUniqueIDGenerator extends TestCase
{
    /**
     * Creates a new TestUniqueIDGenerator object.
     *
     * @param name DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public TestUniqueIDGenerator(String name) throws Exception
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
    public void testGetUniqueID() throws Exception
    {
        String id1 = UniqueIDGenerator.getUniqueID();
        assertTrue("Make sure we got a unique id.", id1 != null);

        String id2 = UniqueIDGenerator.getUniqueID();
        assertTrue("Make sure both id's are different.  id1:" + id1 + ", id2:" + id2, !id1.equals(id2));
    }
}