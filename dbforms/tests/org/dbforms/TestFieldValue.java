package org.dbforms;
import junit.framework.*;
import org.dbforms.util.FieldTypes;
/**
 *  Description of the Class
 *
 * @author     epugh
 * @created    May 3, 2002
 */
public class TestFieldValue extends TestCase {

    Field fAuthorId = null;
    Field fBookId = null;
    FieldValue fvLogicalOR = null;
    FieldValue fvNotLogicalOR = null;
    FieldValue fvLogicalORLikeFilter = null;

    public TestFieldValue( String name ) throws Exception{
        super( name );

    }
    public void setUp() throws Exception {
        super.setUp();
        fAuthorId = new Field();
        fAuthorId.setName("AUTHOR_ID");
        fBookId = new Field();
        fBookId.setName("BOOK_ID");

        fvLogicalOR = new FieldValue(fAuthorId,"10",false, FieldValue.FILTER_EQUAL, true);
        fvNotLogicalOR = new FieldValue(fBookId,"10",false, FieldValue.FILTER_EQUAL, false);
        fvLogicalORLikeFilter = new FieldValue(fAuthorId,"10",false, FieldValue.FILTER_LIKE, true);

    }

    public void tearDown() throws Exception{
        super.tearDown();

    }

    public void testCon() throws Exception{
        FieldValue fv = new FieldValue(fAuthorId,"10",false);
        assertTrue("make sure we get our field back.",fv.getField().equals(fAuthorId));
        assertTrue("make sure we get our field value.",fv.getFieldValue().equals("10"));
    }

    public void testLogicalOr() throws Exception{
        FieldValue fv = new FieldValue(fAuthorId,"10",false);
        assertTrue("Make sure this is not a logical OR.",!fv.isLogicalOR());
        fv.setLogicalOR(true);
        assertTrue("make sure this is a logical OR.",fv.isLogicalOR());
    }

    public void testOrderCluse() throws Exception{
        FieldValue fv = new FieldValue(fAuthorId,"10",false);
        assertTrue("Make sure this is ordered by AUTHOR_ID",fv.getOrderClause().equals("AUTHOR_ID"));
        fv.setSortDirection(Field.ORDER_DESCENDING);
        assertTrue("Make sure this is ordered by AUTHOR_ID DESC",fv.getOrderClause().equals("AUTHOR_ID DESC"));

    }

    public void testGetWhereClause() throws Exception{
        FieldValue[] fvs = {fvLogicalOR,fvNotLogicalOR};
        assertTrue("Test Where clause equals:AUTHOR_ID =  ?  AND BOOK_ID =  ?",FieldValue.getWhereClause(fvs).indexOf("AUTHOR_ID =  ?  AND BOOK_ID =  ?")>=0);
        FieldValue[]fvs2 = {fvLogicalOR,fvLogicalOR};
        assertTrue("Test Where cluase equals:" + FieldValue.getWhereClause(fvs2),FieldValue.getWhereClause(fvs2).indexOf("AUTHOR_ID =  ?  OR AUTHOR_ID =  ?")>=0);

        FieldValue[]fvs3 = {fvLogicalORLikeFilter,fvLogicalOR};
        assertTrue("Test Where cluase equals:" + FieldValue.getWhereClause(fvs3),FieldValue.getWhereClause(fvs3).indexOf("AUTHOR_ID like  ?  OR AUTHOR_ID =  ?")>=0);

    }

    public void testClose() throws Exception{
        FieldValue fv = new FieldValue(fAuthorId,"10",false);

        FieldValue fvClone = (FieldValue)fv.clone();
        assertTrue("Make sure toStrings match",fv.toString().equals(fvClone.toString()));
    }

    public void testInvert() throws Exception{
        FieldValue fvLogicalOROrig = (FieldValue)fvLogicalOR.clone();
        FieldValue fvNotLogicalOROrig = (FieldValue)fvNotLogicalOR.clone();
        FieldValue fvLogicalORLikeFilterOrig = (FieldValue)fvLogicalORLikeFilter.clone();

        FieldValue[] fvs = {fvLogicalOR,fvNotLogicalOR,fvLogicalORLikeFilter};

        FieldValue.invert(fvs);
        assertTrue("fv was flipped",fvLogicalOR.getSortDirection() == (!fvLogicalOROrig.getSortDirection()));
        assertTrue("fv was flipped",fvNotLogicalOR.getSortDirection() == (!fvNotLogicalOROrig.getSortDirection()));
        assertTrue("fv was flipped",fvLogicalORLikeFilter.getSortDirection() == (!fvLogicalORLikeFilterOrig.getSortDirection()));


    }

}


