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
import java.util.*;



/**
 *  Description of the Class
 *
 * @author     epugh
 * @created    May 3, 2002
 */
public class TestParseUtil extends TestCase
{
    /**
     * Creates a new TestParseUtil object.
     *
     * @param name DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public TestParseUtil(String name) throws Exception
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
    public void testSplitString() throws Exception
    {
        Vector v = ParseUtil.splitString("hello,there,this,is,delimited", ",");
        assertTrue("Vector must have have 5 elements", v.size() == 5);
        assertTrue("check elements", v.get(0).toString().equals("hello"));
        assertTrue("check elements", v.get(1).toString().equals("there"));
        assertTrue("check elements", v.get(2).toString().equals("this"));
        assertTrue("check elements", v.get(3).toString().equals("is"));
        assertTrue("check elements", v.get(4).toString().equals("delimited"));
    }


    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void testGetEmbeddedString() throws Exception
    {
        String s = "ac_update_3_12";

        assertTrue(ParseUtil.getEmbeddedString(s, 0, '_').equals("ac"));
        assertTrue(ParseUtil.getEmbeddedString(s, 1, '_').equals("update"));
        assertTrue(ParseUtil.getEmbeddedString(s, 2, '_').equals("3"));
        assertTrue(ParseUtil.getEmbeddedString(s, 3, '_').equals("12"));

        /*
        try {
          String result = ParseUtil.getEmbeddedString(s, 4, '_');
         assertTrue("This should not be hit! result:" + result,result.equals("dodo"));
         fail("Should not get here, should have exception");
        }
        catch (Exception e){}
        */
    }


    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void testGetEmbeddedStringWithoutDots() throws Exception
    {
        String s = "ac_update_3_12";

        assertTrue(ParseUtil.getEmbeddedStringWithoutDots(s, 0, '_').equals("ac"));
        assertTrue(ParseUtil.getEmbeddedStringWithoutDots(s, 1, '_').equals("update"));
        assertTrue(ParseUtil.getEmbeddedStringWithoutDots(s, 2, '_').equals("3"));
        assertTrue(ParseUtil.getEmbeddedStringWithoutDots(s, 3, '_').equals("12"));

        /*
        try {
          String result = ParseUtil.getEmbeddedStringWithoutDots(s, 4, '_');
         assertTrue("This should not be hit!  Should throw a runtime exception. result:" + result,result.equals("dodo"));
         fail("Should not get here, should have exception");
        }
        catch (Exception e){}
        */
    }


    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void testGetEmbeddedStringAsInteger() throws Exception
    {
        String s = "ac_update_3_12";

        assertTrue(ParseUtil.getEmbeddedStringAsInteger(s, 2, '_') == 3);
        assertTrue(ParseUtil.getEmbeddedStringAsInteger(s, 3, '_') == 12);

        try
        {
            int i = ParseUtil.getEmbeddedStringAsInteger(s, 4, '_');
            assertTrue("This should not be hit! result:" + i, i == 999);
            fail("Should not get here, should have exception");
        }
        catch (Exception e)
        {
        }
    }
}