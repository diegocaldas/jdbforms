package org.dbforms;
import junit.framework.*;
import org.dbforms.util.FieldTypes;
/**
 *  Description of the Class
 *
 * @author     epugh
 * @created    May 3, 2002
 */
public class TestMessage extends TestCase {


    public TestMessage( String name ) throws Exception{
        super( name );

    }
    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception{
        super.tearDown();

    }

    public void testGetSetMessage() throws Exception{
        Message m = new Message();
        m.setMessage("This is a Message");
        assertTrue("This is a Message shoud be message",m.getMessage().equals("This is a Message"));
    }

    public void testGetSetLanguage() throws Exception{
        Message m  = new Message();
        m.setLanguage("EN");
        assertTrue("EN should be returned",m.getLanguage().equals("EN"));
    }

}


