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

        s = Util.encode(s, null);

        assertTrue("Encoded String:" + s, s.equals(ENCODED));


        s = null;


        s = Util.encode(s, null);

        assertTrue("S must be null", s == null);



    }

   public void testDecode()
       throws Exception {

       String SOURCE = "http://www.sun.com?some bogus=value";
       String ENCODED = "http%3A%2F%2Fwww.sun.com%3Fsome+bogus%3Dvalue";

       String s = ENCODED;
       s = Util.decode(s, null);
       assertTrue("Encoded String:" + s, s.equals(SOURCE));


       s = null;
       s = Util.encode(s, null);
       assertTrue("S must be null", s == null);
   }
   
   
   public void testEncodeDecode() throws Exception {
      String s = "0%3A7%3A%8A%E1%92%86%82%C9%82%A8%82%A9%82%C8%82%A2-1%3A8%3A%82%AA%82%F1%82%BF%82%E3%82%A4%82%AA%82%C8%82%A2";
      s = Util.decode(s, "Shift-JIS");
      System.out.println(s.length() + " " + s);
      s = "0%3A7%3A%E7%9C%BC%E4%B8%AD%E3%81%AB%E3%81%8A%E3%81%8B%E3%81%AA%E3%81%84-1%3A8%3A%E3%81%8C%E3%82%93%E3%81%A1%E3%82%85%E3%81%86%E3%81%8C%E3%81%AA%E3%81%84";
      s = Util.decode(s, "UTF-8");
      System.out.println(s.length() + " " + s);
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
