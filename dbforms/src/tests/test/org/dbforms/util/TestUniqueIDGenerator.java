package org.dbforms.util;
import junit.framework.*;
/**
 *  Description of the Class
 *
 * @author     epugh
 * @created    May 3, 2002
 */
public class TestUniqueIDGenerator extends TestCase {


    public TestUniqueIDGenerator( String name ) throws Exception{
        super( name );

    }
    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception{
        super.tearDown();

    }

    public void testGetUniqueID() throws Exception{
        String id1 = UniqueIDGenerator.getUniqueID();
        assertTrue("Make sure we got a unique id.",id1 != null);
        String id2 = UniqueIDGenerator.getUniqueID();
        assertTrue("Make sure both id's are different",!id1.equals(id2));
    }

}


