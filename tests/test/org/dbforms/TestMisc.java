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

package org.dbforms;

import java.util.Calendar;



/**
 *  This is a class to test elements of DbForms that don't have a class them selves.  For instance, jdk1.4 and 1.3 workarounds.
 *
 * @author     epugh
 */
public class TestMisc extends org.dbforms.util.AbstractTestCase {
   /**
    * This is a testcase for checking if the jdk1.4 method
    * cal.getTimeInMillis() is approximate to the jdk1.3
    * workaround cal.getDate().getTime()
    *
    */
   public void testTimeInMillisApproximate() throws Exception {
      System.out.println("IF YOU ARE ON JDK1.4, PLEASE UNCOMMENT THE CONTENTS OF testJDK to TEST 1.4 versus 1.3 method.");

      Calendar cal         = Calendar.getInstance();
      long     jdk14Method = cal.getTimeInMillis();
      long     jdk13Method = cal.getTime()
                                .getTime();
      assertTrue("jdk14Method == jdk13Method", (jdk14Method == jdk13Method));
   }
}
