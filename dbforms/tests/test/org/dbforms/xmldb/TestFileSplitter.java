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

package org.dbforms.xmldb;

import junit.framework.*;

import org.dbforms.util.Util;

import java.io.*;



/**
 *  Description of the Class
 *
 * @author     epugh
 * @created    May 3, 2002
 */
public class TestFileSplitter extends TestCase {
   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testConstructorWithStrings() throws Exception {
      String srcFile = TestFileSplitter.class.getResource("fileA.txt")
                                             .getPath();
      assertTrue(!Util.isNull(srcFile));
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testSplitFile() throws Exception {
      File srcFile = new File(FileSplitter.class.getResource("fileA.txt").getPath());
      File destDir = srcFile.getParentFile();

      assertTrue("File Must Exist:" + srcFile, srcFile.exists());
      assertTrue("File Must be readable:" + srcFile, srcFile.canRead());
      assertTrue("Dir must Exist and write",
                 destDir.exists() && destDir.canWrite());

      FileSplitter fp = new FileSplitter(srcFile, destDir);
      fp.splitFile();

      File fileSplitterResultA = new File(destDir, "FileSplitterResultA.txt");
      assertTrue("File Must Exist:" + fileSplitterResultA,
                 fileSplitterResultA.exists());

      File fileSplitterResultB = new File(destDir, "FileSplitterResultB.txt");
      assertTrue("File Must Exist:" + fileSplitterResultB,
                 fileSplitterResultB.exists());
   }
}
