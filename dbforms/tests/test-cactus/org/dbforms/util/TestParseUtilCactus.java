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
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.apache.cactus.*;
import java.util.*;



/**
 * Tests of the <code>ParseUtil</code> class.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 *
 */
public class TestParseUtilCactus extends ServletTestCase
{
    /**
     * Defines the testcase name for JUnit.
     *
     * @param theName the testcase's name.
     */
    public TestParseUtilCactus(String theName)
    {
        super(theName);
    }

    /**
     * Start the tests.
     *
     * @param theArgs the arguments. Not used
     */
    public static void main(String[] theArgs)
    {
        junit.swingui.TestRunner.main(new String[] 
        {
            TestParseUtilCactus.class.getName()
        });
    }


    /**
     * @return a test suite (<code>TestSuite</code>) that includes all methods
     *         starting with "test"
     */
    public static Test suite()
    {
        // All methods starting with "test" will be executed in the test suite.
        return new TestSuite(TestParseUtilCactus.class);
    }


    /**
     * In addition to creating the tag instance and adding the pageContext to
     * it, this method creates a BodyContent object and passes it to the tag.
     */
    public void setUp() throws Exception
    {
    }


    /**
     * DOCUMENT ME!
     *
     * @param theRequest DOCUMENT ME!
     */
    public void beginGetParameterNames(WebRequest theRequest)
    {
        theRequest.addParameter("name", "value");
    }


    /**
     * Execute the test.  Doesn't test though for multipart posts.
     *
     * @throws Exception
     */
    public void testGetParameterNames() throws Exception
    {
        ArrayList al = new ArrayList();
        Enumeration e = ParseUtil.getParameterNames(request);

        for (; e.hasMoreElements();)
        {
            al.add(e.nextElement());
        }

        String name = "";

        for (Iterator i = al.iterator(); i.hasNext();)
        {
            String test = i.next().toString();

            if (test.equals("name"))
            {
                name = test;
            }
        }

        assertTrue("Should have more then one parameter:" + al.size(), al.size() > 0);
        assertTrue("Should have parameter with name of  \"name\"", name.equals("name"));
    }


    /**
     * DOCUMENT ME!
     *
     * @param theRequest DOCUMENT ME!
     */
    public void beginGetParameter(WebRequest theRequest)
    {
        theRequest.addParameter("name", "value");
        theRequest.addParameter("name2", "value2");
        theRequest.addParameter("name3", "value3");
    }


    /**
     * Execute the test.  Doesn't test though for multipart posts.
     *
     * @throws Exception
     */
    public void testGetParameter() throws Exception
    {
        assertTrue("Should find specific param", ParseUtil.getParameter(request, "name2").equals("value2"));
        assertTrue("Should find specific param", ParseUtil.getParameter(request, "name3").equals("value3"));
        assertTrue("Should NOT find specific param", ParseUtil.getParameter(request, "name4") == null);
    }


    /**
     * DOCUMENT ME!
     *
     * @param theRequest DOCUMENT ME!
     */
    public void beginGetParametersStartingWith(WebRequest theRequest)
    {
        theRequest.addParameter("name", "value");
        theRequest.addParameter("name2", "value2");
        theRequest.addParameter("name3", "value3");
        theRequest.addParameter("bob", "value");
        theRequest.addParameter("bob2", "value2");
        theRequest.addParameter("bob3", "value3");
    }


    /**
     * Execute the test.  Doesn't test though for multipart posts.
     *
     * @throws Exception
     */
    public void testGetParametersStartingWith() throws Exception
    {
        assertTrue("Should find 3 parameters starting with name", ParseUtil.getParametersStartingWith(request, "name").size() == 3);
        assertTrue("Should find 3 parameters starting with bob", ParseUtil.getParametersStartingWith(request, "bob").size() == 3);
        assertTrue("Should find 0 parameters starting with john", ParseUtil.getParametersStartingWith(request, "john").size() == 0);
    }


    /**
     * DOCUMENT ME!
     *
     * @param theRequest DOCUMENT ME!
     */
    public void beginGetFirstParameterStartingWith(WebRequest theRequest)
    {
//		theRequest.addParameter("name2", "value2");
// 20030604-HKK: current cactus implementation do not garanty the sequence of the list!!!
        theRequest.addParameter("name", "value");
//        theRequest.addParameter("name3", "value3");
//        theRequest.addParameter("bob", "value");
//        theRequest.addParameter("bob2", "value2");
        theRequest.addParameter("bob3", "value3");
    }


    /**
     * Execute the test.  Doesn't test though for multipart posts.
     *
     * @throws Exception
     */
    public void testGetFirstParameterStartingWith() throws Exception
    {
        String param = ParseUtil.getFirstParameterStartingWith(request, "name");
        assertTrue("Should find first parameters starting with name:" + param, param.equals("name"));
    }


    /**
     * DOCUMENT ME!
     */
    public void tearDown()
    {
    }
}