package org.dbforms.util;
import junit.framework.*;
import java.io.*;
import java.util.*;
/**
 *  Description of the Class
 *
 * @author     epugh
 * @created    May 3, 2002
 */
public class TestFileHolder extends TestCase {


    public TestFileHolder( String name ) throws Exception{
        super( name );

    }
    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception{
        super.tearDown();

    }

    public void testFileHolder() throws Exception{

        File testFile = new File(FileHolder.class.getResource("TestFileHolderFile.txt").getPath());
        assertTrue("Make sure test file exists:" + testFile,testFile.canRead());
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(testFile));
        FileHolder fh = new FileHolder(testFile.getName(),"string", bis, true, 10000);
        assertEquals(fh.getFileName(),testFile.getName());

    }

   public void testWritingFileOut() throws Exception{
        File testFile = new File(FileHolder.class.getResource("TestFileHolderFile.txt").getPath());
       assertTrue("Make sure test file exists:" + testFile,testFile.canRead());
       BufferedInputStream bis = new BufferedInputStream(new FileInputStream(testFile));
       FileHolder fh = new FileHolder(testFile.getName(),"string", bis, true, 10000);
       File outputFile = new File(testFile.getParentFile(),"TestFileHolderFile_output.txt");
       if (outputFile.exists()){
           outputFile.delete();
       }
       assertTrue("Make sure the output file does not exist yet.",!outputFile.exists());
       fh.writeBufferToFile(outputFile);
       assertTrue("Make sure output file exists.",outputFile.exists());
   }

}


