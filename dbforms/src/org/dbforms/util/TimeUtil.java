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
/**
 *  Helper classes for dealing with time values
 * 
 * 
 * 
 *  @author  frederic
*/
import java.text.*;
import java.util.*;

public class TimeUtil {
   
   static final int SECSPERDAY = 24 * 60 * 60;

/**
 * Reformats seconds to time string with format: dd:hh:mm:ss
 * 
 * @param seconds
 * @return String
 */
   public final static String Seconds2String(String seconds) {
      return Seconds2String(Integer.valueOf(seconds));
   }

/**
 * Reformats seconds to time string with format: dd:hh:mm:ss
 * 
 * @param seconds
 * @return String
 */
   public final static String Seconds2String(Integer seconds) {
      return Seconds2String(seconds.intValue());
   }

/**
 * Reformats seconds to time string with format: dd:hh:mm:ss
 * 
 * @param seconds
 * @return String
 */
   public final static String Seconds2String(int seconds) {
      int d, h, m;
      String zeit;

      d = (seconds / SECSPERDAY);
      seconds = seconds - d * SECSPERDAY;
      h = seconds / (60 * 60);
      seconds = seconds - h * 60 * 60;
      m = seconds / 60;
      seconds = seconds - m * 60;
      if (d > 0) {
      	Object [] o = {new Integer(d),new Integer(h),new Integer(m) ,new Integer(seconds)};
         zeit = StringUtil.sprintf("%i:%02i:%02i:%02i", o);
      } else { 
         Object [] o = {new Integer(h),new Integer(m) ,new Integer(seconds)};
         zeit = StringUtil.sprintf("%i:%02i:%02i", o);
      }
      return zeit;
   }

	private static void splitDate(String s, StringBuffer sDate,	StringBuffer sTime) {
		sDate.setLength(0);
		sTime.setLength(0);
		int i = s.lastIndexOf(':');
		if (i > -1) {
			i = s.lastIndexOf(' ', i);
			if (i > -1) {
				sDate.append(s.substring(0, i));
				sTime.append(s.substring(i + 1));
			} else {
				sTime.append(s);
			}
		} else {
			sDate.append(s);
		}
	}

	private static long saveParseDate(Locale loc, String format, String s) {
		long d = 0;
		if (!Util.isNull(format) && !Util.isNull(s)) {
			SimpleDateFormat sdf;
			sdf = new SimpleDateFormat(format, loc);
			sdf.setLenient(false);
			try {
				sdf.parse(s);
			} catch (Exception e) {
			}
			Calendar cal = sdf.getCalendar();
			Calendar now = Calendar.getInstance();
			if (!cal.isSet(Calendar.DAY_OF_MONTH))
				cal.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
			if (!cal.isSet(Calendar.MONTH))
				cal.set(Calendar.MONTH, now.get(Calendar.MONTH));
			if (!cal.isSet(Calendar.YEAR))
				cal.set(Calendar.YEAR, now.get(Calendar.YEAR));
			d = cal.getTimeInMillis();
		} else {
			Calendar now = Calendar.getInstance();
			now.set(Calendar.HOUR_OF_DAY, 0);
			now.set(Calendar.MINUTE, 0);
			now.set(Calendar.SECOND, 0);
			d = now.getTimeInMillis();
		}
		return d;
	}

	private static long saveParseTime(Locale loc, String format, String s) {
		long d = 0;
		if (!Util.isNull(format) && !Util.isNull(s)) {
			SimpleDateFormat sdf;
			sdf = new SimpleDateFormat(format, loc);
			sdf.setLenient(false);
			try {
				sdf.parse(s);
			} catch (Exception e) {
			}
			Calendar cal = sdf.getCalendar();
			if (!cal.isSet(Calendar.HOUR_OF_DAY) && !cal.isSet(Calendar.HOUR_OF_DAY))
				cal.set(Calendar.HOUR_OF_DAY, 0);
			if (!cal.isSet(Calendar.MINUTE))
				cal.set(Calendar.MINUTE, 0);
			if (!cal.isSet(Calendar.SECOND))
				cal.set(Calendar.SECOND, 0);
			d = cal.getTimeInMillis() + cal.getTimeZone().getRawOffset();
		}
		return d;
	}

	public static Date parseDate(String format, String s) {
		return parseDate(Locale.getDefault(), format, s);
	}

	public static Date parseDate(Locale loc, String format, String s) {
		StringBuffer sDate = new StringBuffer();
		StringBuffer sTime = new StringBuffer();
		StringBuffer fDate = new StringBuffer();
		StringBuffer fTime = new StringBuffer();
		splitDate(s, sDate, sTime);
		splitDate(format, fDate, fTime);
		long dDate = saveParseDate(loc, fDate.toString(), sDate.toString());
		long dTime = saveParseTime(loc, fTime.toString(), sTime.toString());
		return new Date(dDate + dTime);
	}


   public static void main(String args[]) {
      int k;
      k = 100024;
      System.out.println(Seconds2String(k));

   	String format = "dd.MM.yyyy HH:mm";
   	System.out.println(parseDate(format, "12.12.2002").toLocaleString());
   	System.out.println(parseDate(format, "12.12.2002 12:30").toLocaleString());
   	System.out.println(parseDate(format, "12:30").toLocaleString());
   	System.out.println(parseDate(format, "12. 12:30").toLocaleString());
   	System.out.println(parseDate(format, "12.06 12:30").toLocaleString());
   	System.out.println(parseDate(format, "12.").toLocaleString());
   	System.out.println(parseDate(format, "12.06").toLocaleString());
   	System.out.println(parseDate(format, "12. 12:").toLocaleString());
   }

}
