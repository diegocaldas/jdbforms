/*
 *  $Header$
 *  $Revision$
 *  $Date$
 *
 *  DbForms - a Rapid Application Development Framework
 *  Copyright (C) 2001 Joachim Peer <joepeer@excite.com>
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 */

package org.dbforms.util;

import junit.framework.*;

/**
 *  Test the Util.java class
 *
 * @author     epugh
 * @created    May 3, 2002
 */

public class TestTimeUtil extends TestCase {

   public void testTimeUtil() {
      int k = 4 + 47 * 60 + 3 * 60 * 60 + 1 * 24 * 60 * 60;
      assertEquals("1:03:47:04", TimeUtil.seconds2String(k));

      String format = "dd.MM.yyyy HH:mm";
      assertEquals("12.12.2002 00:00:00", TimeUtil.parseDate(format, "12.12.2002").toLocaleString());
      assertEquals("12.12.2002 12:30:00", TimeUtil.parseDate(format, "12.12.2002 12:30").toLocaleString());
      System.out.println(TimeUtil.parseDate(format, "12:30").toLocaleString());
      System.out.println(TimeUtil.parseDate(format, "12. 12:30").toLocaleString());
      System.out.println(TimeUtil.parseDate(format, "12.06 12:30").toLocaleString());
      System.out.println(TimeUtil.parseDate(format, "12.").toLocaleString());
      System.out.println(TimeUtil.parseDate(format, "12.06").toLocaleString());
      System.out.println(TimeUtil.parseDate(format, "12.06.03").toLocaleString());
      System.out.println(TimeUtil.parseDate(format, "12. 12:").toLocaleString());
      System.out.println(TimeUtil.findEndOfDay(TimeUtil.parseDate(format, "12. 12:")).toLocaleString());
   }
}
