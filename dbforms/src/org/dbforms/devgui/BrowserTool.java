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
 * #fixme: code is kindof dirty..
 */
public class BrowserTool {
   /**
    * DOCUMENT ME!
    *
    * @param anURL DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public static void openURL(String anURL) throws IOException {
      if (anURL.startsWith("http://") || anURL.startsWith("https://")) {
         // WIN32
         String osName = System.getProperties()
                               .getProperty("os.name")
                               .toLowerCase();
         System.out.println("os=" + osName);

         if (osName.startsWith("win")) {
            System.out.println("cmd /c start " + anURL);
            Runtime.getRuntime()
                   .exec("cmd /c start " + anURL);
         }
         // UNIX, LINUX, MAC
         else {
            System.out.println("netscape " + anURL);
            Runtime.getRuntime()
                   .exec("netscape " + anURL);
         }
      } else {
         throw new IOException("Protocol should be either http or https.");
      }
   }
}
