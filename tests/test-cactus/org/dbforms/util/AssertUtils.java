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

import junit.framework.Assert;



/**
 * Tests of the <code>DbFormTag</code> class.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh </a>
 *
 */
public class AssertUtils {
   /**
    * DOCUMENT ME!
    *
    * @param sourceString DOCUMENT ME!
    * @param targetString DOCUMENT ME!
    */
   public static void assertContains(String sourceString,
                                     String targetString) {
      Assert.assertTrue("Make sure string contains another string.\nString to Find:\n"
                        + sourceString + "\n\nString to Search:\n"
                        + targetString, targetString.indexOf(sourceString) >= 0);
   }
}
