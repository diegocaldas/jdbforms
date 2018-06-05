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

import java.text.SimpleDateFormat;

import java.util.Calendar;



/**
 *  Test the Util.java class
 *
 * @author     epugh
 * @created    May 3, 2002
 */
public class TestTimeUtil extends AbstractTestCase {
   private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

   /**
    * DOCUMENT ME!
    */
   public void testSecondsToString() {
      int k = 4 + (47 * 60) + (3 * 60 * 60) + (1 * 24 * 60 * 60);
      assertEquals("1:03:47:04", TimeUtil.seconds2String(k));
   }


   /**
    * DOCUMENT ME!
    */
   public void testTimeUtil() {
      Calendar now;
      now = Calendar.getInstance();
      now.set(Calendar.HOUR_OF_DAY, 12);
      now.set(Calendar.MINUTE, 30);
      now.set(Calendar.SECOND, 0);
      now.set(Calendar.DAY_OF_MONTH, 12);
      now.set(Calendar.MONTH, 11);
      now.set(Calendar.YEAR, 2002);
      assertTrue(check(now, "12.12.2002 12:30:00"));

      now = Calendar.getInstance();
      now.set(Calendar.HOUR_OF_DAY, 0);
      now.set(Calendar.MINUTE, 0);
      now.set(Calendar.SECOND, 0);
      now.set(Calendar.DAY_OF_MONTH, 12);
      now.set(Calendar.MONTH, 11);
      now.set(Calendar.YEAR, 2002);
      assertTrue(check(now, "12.12.2002 00:00:00"));

      now = Calendar.getInstance();
      now.set(Calendar.HOUR_OF_DAY, 12);
      now.set(Calendar.MINUTE, 30);
      now.set(Calendar.SECOND, 0);
      now.set(Calendar.DAY_OF_MONTH, 25);
      now.set(Calendar.MONTH, 9);
      now.set(Calendar.YEAR, 2003);
      assertTrue(check(now, "25.10.2003 12:30:00"));

      now = Calendar.getInstance();
      now.set(Calendar.HOUR_OF_DAY, 12);
      now.set(Calendar.MINUTE, 30);
      now.set(Calendar.SECOND, 0);
      now.set(Calendar.DAY_OF_MONTH, 27);
      now.set(Calendar.MONTH, 9);
      now.set(Calendar.YEAR, 2003);
      assertTrue(check(now, "27.10.2003 12:30:00"));

      now = Calendar.getInstance();
      now.set(Calendar.HOUR_OF_DAY, 12);
      now.set(Calendar.MINUTE, 30);
      now.set(Calendar.SECOND, 0);
      now.set(Calendar.DAY_OF_MONTH, 26);
      now.set(Calendar.MONTH, 9);
      now.set(Calendar.YEAR, 2003);

      // here we have bug in the java daylight switching code!!!!
      assertFalse(check(now, "26.10.2003 12:30:00"));

      now = Calendar.getInstance();
      now.set(Calendar.HOUR_OF_DAY, 12);
      now.set(Calendar.MINUTE, 30);
      now.set(Calendar.SECOND, 0);
      now.set(Calendar.DAY_OF_MONTH, 12);
      now.set(Calendar.MONTH, 5);
      assertTrue(check(now, "12.06 12:30"));

      now = Calendar.getInstance();
      now.set(Calendar.HOUR_OF_DAY, 12);
      now.set(Calendar.MINUTE, 30);
      now.set(Calendar.SECOND, 0);
      now.set(Calendar.DAY_OF_MONTH, 12);
      now.set(Calendar.MONTH, 11);
      assertTrue(check(now, "12.12 12:30"));

      now = Calendar.getInstance();
      now.set(Calendar.HOUR_OF_DAY, 12);
      now.set(Calendar.MINUTE, 30);
      now.set(Calendar.SECOND, 0);
      now.set(Calendar.DAY_OF_MONTH, 12);
      assertTrue(check(now, "12. 12:30"));

      now = Calendar.getInstance();
      now.set(Calendar.HOUR_OF_DAY, 0);
      now.set(Calendar.MINUTE, 0);
      now.set(Calendar.SECOND, 0);
      now.set(Calendar.DAY_OF_MONTH, 12);
      assertTrue(check(now, "12."));

      now = Calendar.getInstance();
      now.set(Calendar.HOUR_OF_DAY, 0);
      now.set(Calendar.MINUTE, 0);
      now.set(Calendar.SECOND, 0);
      now.set(Calendar.DAY_OF_MONTH, 12);
      now.set(Calendar.MONTH, 5);
      assertTrue(check(now, "12.06"));

      now = Calendar.getInstance();
      now.set(Calendar.HOUR_OF_DAY, 0);
      now.set(Calendar.MINUTE, 0);
      now.set(Calendar.SECOND, 0);
      now.set(Calendar.DAY_OF_MONTH, 12);
      now.set(Calendar.MONTH, 5);
      now.set(Calendar.YEAR, 2003);
      assertTrue(check(now, "12.06.03"));

      now = Calendar.getInstance();
      now.set(Calendar.HOUR_OF_DAY, 0);
      now.set(Calendar.MINUTE, 0);
      now.set(Calendar.SECOND, 0);
      now.set(Calendar.DAY_OF_MONTH, 12);
      now.set(Calendar.MONTH, 11);
      now.set(Calendar.YEAR, 2003);
      assertTrue(check(now, "12.12.03"));

      now = Calendar.getInstance();
      now.set(Calendar.HOUR_OF_DAY, 12);
      now.set(Calendar.MINUTE, 0);
      now.set(Calendar.SECOND, 0);
      now.set(Calendar.DAY_OF_MONTH, 12);
      assertTrue(check(now, "12. 12:"));

      now = Calendar.getInstance();
      now.set(Calendar.HOUR_OF_DAY, 12);
      now.set(Calendar.MINUTE, 30);
      now.set(Calendar.SECOND, 0);
      assertTrue(check(now, "12:30"));

      now = Calendar.getInstance();
      now.set(Calendar.HOUR_OF_DAY, 12);
      now.set(Calendar.MINUTE, 30);
      now.set(Calendar.SECOND, 0);
      now.set(Calendar.DAY_OF_MONTH, 12);
      now.set(Calendar.MONTH, 11);
      now.set(Calendar.YEAR, 2002);
      assertTrue(check(now, "2002-12-12 12:30:00"));
   }


   private boolean check(Calendar now,
                         String   s) {
      String s1 = format.format(now.getTime());
      System.out.println("parsing results");
      System.out.println("===============");

      String s2 = format.format(TimeUtil.parseDate(format, s).getTime());
      System.out.println(s);
      System.out.println(s1);
      System.out.println(s2);

      return s1.equals(s2);
   }
}
