package org.dbforms;
import junit.framework.*;
import org.dbforms.util.FieldTypes;
/**
 *  Description of the Class
 *
 * @author     epugh
 * @created    May 3, 2002
 */
public class TestInterceptor extends TestCase {



    public TestInterceptor( String name ) throws Exception{
        super( name );

    }
    public void setUp() throws Exception {
        super.setUp();


    }

    public void tearDown() throws Exception{
        super.tearDown();

    }

    public void testGetSetClassName() throws Exception{
        Interceptor i = new Interceptor();
        i.setClassName("java.util.List");
        assertTrue("Class name should be java.util.List",i.getClassName().equals("java.util.List"));
    }


}


