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

package org.dbforms.devgui;

import java.io.*;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class FileUtility {
   /**
    * DOCUMENT ME!
    *
    * @param dir DOCUMENT ME!
    * @param extensions DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public static String[] getFileNamesInDirectory(File     dir,
                                                  String[] extensions) {
      if (extensions == null) {
         return dir.list();
      } else {
         return dir.list(new ExtensionFilter(extensions));
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param dir DOCUMENT ME!
    * @param extensions DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public static File[] getFilesInDirectory(File     dir,
                                            String[] extensions) {
      if (extensions == null) {
         return dir.listFiles();
      } else {
         return dir.listFiles(new ExtensionFilter(extensions));
      }
   }
}
