package org.dbforms;
import junit.framework.*;
import org.dbforms.util.FieldTypes;
/**
 *  Description of the Class
 *
 * @author     epugh
 * @created    May 3, 2002
 */
public class TestField extends TestCase {


    public TestField( String name ) throws Exception{
        super( name );

    }
    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception{
        super.tearDown();

    }

    public void testGetSetName() throws Exception{
        Field f = new Field();
        f.setName("FIELDNAME");
        assertTrue("Field name should be FIELDNAME",f.getName().equals("FIELDNAME"));
    }
    public void testGetSetId() throws Exception{
        Field f = new Field();
        f.setId(10);
        assertTrue("Field id should be 10",f.getId() == 10);
    }

    public void testGetSetAutoInc() throws Exception{
        Field f = new Field();
        f.setAutoInc("true");
        assertTrue("Field should be autoinc",f.getIsAutoInc());
    }

    public void testGetSetAutoInc2() throws Exception{
        Field f = new Field();
        f.setAutoInc("YES");
        assertTrue("Field should be autoinc",f.getIsAutoInc());
    }

    public void testSortable() throws Exception{
        Field f = new Field();
        f.setSortable("true");
        assertTrue("Should be True",f.isFieldSortable());
        f.setSortable("TRUE");
        assertTrue("Should be True",f.isFieldSortable());
        f.setSortable("yes");
        assertTrue("Should be True",f.isFieldSortable());
        f.setSortable("FALSE");
        assertTrue("Should be false",!f.isFieldSortable());
        f.setSortable("NO");
        assertTrue("Should be false",!f.isFieldSortable());
    }

    public void testType() throws Exception{
        Field f = new Field();
        f.setType("int");
        assertTrue("Should be FieldTypes.INTEGER",f.getType()==FieldTypes.INTEGER);
        f.setType("smallint");
        assertTrue("Should be FieldTypes.INTEGER",f.getType()==FieldTypes.INTEGER);
        f.setType("tinyint");
        assertTrue("Should be FieldTypes.INTEGER",f.getType()==FieldTypes.INTEGER);
        f.setType("TINYINT");
        assertTrue("Should be FieldTypes.INTEGER",f.getType()==FieldTypes.INTEGER);

        f.setType("NUMERIC");
        assertTrue("Should be FieldTypes.NUMERIC",f.getType()==FieldTypes.NUMERIC);
        f.setType("NUMBER");
        assertTrue("Should be FieldTypes.NUMERIC",f.getType()==FieldTypes.NUMERIC);

        f.setType("char");
        assertTrue("Should be FieldTypes.CHAR",f.getType()==FieldTypes.CHAR);
        f.setType("varchar");
        assertTrue("Should be FieldTypes.CHAR",f.getType()==FieldTypes.CHAR);
        f.setType("nvarchar");
        assertTrue("Should be FieldTypes.CHAR",f.getType()==FieldTypes.CHAR);
        f.setType("longchar");
        assertTrue("Should be FieldTypes.CHAR",f.getType()==FieldTypes.CHAR);

        f.setType("diskblob");
        assertTrue("Should be FieldTypes.DISKBLOB",f.getType()==FieldTypes.DISKBLOB);
    }
}


