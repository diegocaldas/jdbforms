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



class ExtensionFilter implements FilenameFilter {
   String[] extensions;

   /**
    * Creates a new ExtensionFilter object.
    *
    * @param extensions DOCUMENT ME!
    */
   public ExtensionFilter(String[] extensions) {
      this.extensions = extensions;
   }

   /**
    * DOCUMENT ME!
    *
    * @param dir DOCUMENT ME!
    * @param name DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public boolean accept(File   dir,
                         String name) {
      for (int i = 0; i < extensions.length; i++) {
         String anExtension = extensions[i];

         if ("*".equals(anExtension) || name.endsWith(anExtension)) {
            return true;
         }
      }

      return false;
   }
}
