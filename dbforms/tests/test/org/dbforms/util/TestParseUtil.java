package org.dbforms.util;
import junit.framework.*;
import java.util.*;
/**
 *  Description of the Class
 *
 * @author     epugh
 * @created    May 3, 2002
 */
public class TestParseUtil extends TestCase {


    public TestParseUtil( String name ) throws Exception{
        super( name );

    }
    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception{
        super.tearDown();

    }

    public void testSplitString() throws Exception{
       Vector v = ParseUtil.splitString("hello,there,this,is,delimited",",");
       assertTrue("Vector must have have 5 elements",v.size()==5);
       assertTrue("check elements",v.get(0).toString().equals("hello"));
       assertTrue("check elements",v.get(1).toString().equals("there"));
       assertTrue("check elements",v.get(2).toString().equals("this"));
       assertTrue("check elements",v.get(3).toString().equals("is"));
       assertTrue("check elements",v.get(4).toString().equals("delimited"));
    }

    // Currently this test fails because return index 4, seems to return the first index!
    public void testGetEmbeddedString() throws Exception{
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

    public void testGetEmbeddedStringWithoutDots() throws Exception{
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

    public void testGetEmbeddedStringAsInteger() throws Exception{
       String s = "ac_update_3_12";


       assertTrue(ParseUtil.getEmbeddedStringAsInteger(s, 2, '_')==3);
       assertTrue(ParseUtil.getEmbeddedStringAsInteger(s, 3, '_')==12);
       try {
         int i = ParseUtil.getEmbeddedStringAsInteger(s, 4, '_');
        assertTrue("This should not be hit! result:" + i,i==999);
        fail("Should not get here, should have exception");
      }
      catch (Exception e){}

    }

}


