/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * DbForms - a Rapid Application Development Framework
 * Copyright (C) 2001 Joachim Peer <joepeer@excite.com>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 */
package org.dbforms.util;

import java.io.*;



/**
 *  Description of the Class
 *
 * @author     epugh
 * @created    May 3, 2002
 */
public class TestFileHolder extends AbstractTestCase {
   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testFileHolder() throws Exception {
      File testFile = new File(FileHolder.class.getResource("TestFileHolderFile.txt").getPath());
      assertTrue("Make sure test file exists:" + testFile, testFile.canRead());

      BufferedInputStream bis = new BufferedInputStream(new FileInputStream(testFile));
      FileHolder          fh = new FileHolder(testFile.getName(), "string",
                                              bis, true, 10000);
      assertEquals(fh.getFileName(), testFile.getName());
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testWritingFileOut() throws Exception {
      File testFile = new File(FileHolder.class.getResource("TestFileHolderFile.txt").getPath());
      assertTrue("Make sure test file exists:" + testFile, testFile.canRead());

      BufferedInputStream bis = new BufferedInputStream(new FileInputStream(testFile));
      FileHolder          fh = new FileHolder(testFile.getName(), "string",
                                              bis, true, 10000);
      File                outputFile = new File(testFile.getParentFile(),
                                                "TestFileHolderFile_output.txt");

      if (outputFile.exists()) {
         outputFile.delete();
      }

      assertTrue("Make sure the output file does not exist yet.",
                 !outputFile.exists());
      fh.writeBufferToFile(outputFile);
      assertTrue("Make sure output file exists.", outputFile.exists());
   }
}
