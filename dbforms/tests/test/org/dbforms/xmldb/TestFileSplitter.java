package org.dbforms.xmldb;
import junit.framework.*;
import java.io.*;
/**
 *  Description of the Class
 *
 * @author     epugh
 * @created    May 3, 2002
 */
public class TestFileSplitter extends TestCase {


    public TestFileSplitter( String name ) throws Exception{
        super( name );

    }
    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception{
        super.tearDown();

    }

    public void testSplitFile() throws Exception{
        File srcFile = new File(TestFileSplitter.class.getResource("fileA.txt").getPath());
        File destDir = srcFile.getParentFile();

        assertTrue("File Must Exist:" + srcFile,srcFile.exists() );
        assertTrue("File Must be readable:" + srcFile,srcFile.canRead() );
        assertTrue("Dir must Exist and write",destDir.exists() && destDir.canWrite());

        FileSplitter fp = new FileSplitter(srcFile,destDir);
        fp.splitFile();

        File fileSplitterResultA = new File(destDir.toString() + "\\FileSplitterResultA.txt");
        assertTrue("File Must Exist:" + fileSplitterResultA,fileSplitterResultA.exists() );

        File fileSplitterResultB = new File(destDir.toString() + "\\FileSplitterResultB.xml");
        assertTrue("File Must Exist:" + fileSplitterResultB,fileSplitterResultB.exists() );
    }


    public void testConstructorWithStrings() throws Exception{
        String srcFile = TestFileSplitter.class.getResource("fileA.txt").getPath();
        String destDir = new File(srcFile).getParent();

        FileSplitter fp = new FileSplitter(srcFile,destDir);
    }


}


