/*
 *  $Header$
 *  $Revision$
 *  $Date$
 *
 *  DbForms - a Rapid Application Development Framework
 *  Copyright (C) 2001 Joachim Peer <joepeer@excite.com>
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 */

package org.dbforms.util;




/**
 *  Test the Util.java class
 *
 * @author     epugh
 * @created    May 3, 2002
 */

public class TestUtil extends AbstractTestCase {


    /**
     * DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */

    public void testEncode()
        throws Exception {

        String SOURCE = "http://www.sun.com?some bogus=value";

        String ENCODED = "http%3A%2F%2Fwww.sun.com%3Fsome+bogus%3Dvalue";


        String s = SOURCE;

        s = Util.encode(s);

        assertTrue("Encoded String:" + s, s.equals(ENCODED));


        s = null;


        s = Util.encode(s);

        assertTrue("S must be null", s == null);



    }

   public void testDecode()
       throws Exception {

       String SOURCE = "http://www.sun.com?some bogus=value";
       String ENCODED = "http%3A%2F%2Fwww.sun.com%3Fsome+bogus%3Dvalue";

       String s = ENCODED;
       s = Util.decode(s);
       assertTrue("Encoded String:" + s, s.equals(SOURCE));


       s = null;
       s = Util.encode(s);
       assertTrue("S must be null", s == null);
   }


    /**
     *  A unit test for JUnit
     *
     * @exception  Exception  Description of the Exception
     */
    public void testIsNull()
        throws Exception {
        boolean result = Util.isNull((String)null);
        assertTrue("Must be null", result);
        result = Util.isNull("");
        assertTrue("Must be null", result);
        result = Util.isNull("not null");
        assertTrue("Must not be null", !result);

    }



    /**
     *  A unit test for JUnit
     *
     * @exception  Exception  Description of the Exception
     */
    public void testRealPath()
        throws Exception {

        String REALPATH = "c:/java/dbforms/" + "$(SERVLETCONTEXT_REALPATH)" + "/target";

        String TRANSLATED_PATH = "c:/java/dbforms/bob/target";

        String result = Util.replaceRealPath(REALPATH, "bob/");

        assertTrue("Make sure path is translated. " + result, result.equals(TRANSLATED_PATH));



    }


}
