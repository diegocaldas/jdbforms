package org.dbforms;
import junit.framework.*;
import org.dbforms.util.FieldTypes;
/**
 *  Description of the Class
 *
 * @author     epugh
 * @created    May 3, 2002
 */
public class TestError extends TestCase {

    private Error e = null;

    public TestError( String name ) throws Exception{
        super( name );

    }
    public void setUp() throws Exception {
        super.setUp();
        e = new Error();

    }

    public void tearDown() throws Exception{
        super.tearDown();

    }

    public void testGetSetID() throws Exception{

        e.setId("ERR_01");
        assertTrue("ID should be ERR_01",e.getId().equals("ERR_01"));
    }

    public void testSetMessage() throws Exception{
        e.setMessage("EN","EN Error");
        e.setMessage("FR","FR Error");
        assertTrue("Make sure we get back appropriate message for lang",e.getMessage("FR").equals("FR Error"));
        assertTrue("Make sure we get back appropriate message for lang",e.getMessage("EN").equals("EN Error"));

    }

    public void testAddMessage() throws Exception{
        e.setMessage("EN","EN Error");
        assertTrue("Make sure we don't have a message",e.getMessage("GB")==null);
        Message m = new Message();
        m.setMessage("GB Error");
        m.setLanguage("GB");
        e.addMessage(m);
        assertTrue("Make sure we get back appropriate message for lang",e.getMessage("GB").equals("GB Error"));

    }
}


