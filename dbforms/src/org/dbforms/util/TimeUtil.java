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
 *  @author  frederic
 */
import java.text.*;
import java.util.*;
import org.apache.regexp.*;
import org.apache.log4j.Category;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class TimeUtil
{
   static Category  logCat     = Category.getInstance(TimeUtil.class.getName());
   static final int SECSPERDAY = 24 * 60 * 60;

   /**
    * Reformats seconds to time string with format: dd:hh:mm:ss
    *
    * @param seconds
    * @return String
    */
   public static final String Seconds2String(String seconds)
   {
      return Seconds2String(Integer.valueOf(seconds));
   }


   /**
    * Reformats seconds to time string with format: dd:hh:mm:ss
    *
    * @param seconds
    * @return String
    */
   public static final String Seconds2String(Integer seconds)
   {
      return Seconds2String(seconds.intValue());
   }


   /**
    * Reformats seconds to time string with format: dd:hh:mm:ss
    *
    * @param seconds
    * @return String
    */
   public static final String Seconds2String(long seconds)
   {
      long   d;
      long   h;
      long   m;
      String zeit;
      d          = (seconds / SECSPERDAY);
      seconds    = seconds - (d * SECSPERDAY);
      h          = seconds / (60 * 60);
      seconds    = seconds - (h * 60 * 60);
      m          = seconds / 60;
      seconds    = seconds - (m * 60);

      if (d > 0)
      {
         Object[] o = 
         {
            new Long(d),
            new Long(h),
            new Long(m),
            new Long(seconds)
         };
         zeit = StringUtil.sprintf("%i:%02i:%02i:%02i", o);
      }
      else
      {
         Object[] o = 
         {
            new Long(h),
            new Long(m),
            new Long(seconds)
         };
         zeit = StringUtil.sprintf("%i:%02i:%02i", o);
      }

      return zeit;
   }


   private static void splitDate(String format, StringBuffer sDate,
      StringBuffer sTime)
   {
      sDate.setLength(0);
      sTime.setLength(0);

      int i = format.lastIndexOf(':');

      if (i > -1)
      {
         i = format.lastIndexOf(' ', i);

         if (i > -1)
         {
            sDate.append(format.substring(0, i));
            sTime.append(format.substring(i + 1));
         }
         else
         {
            sTime.append(format);
         }
      }
      else
      {
         sDate.append(format);
      }
   }


   private static long saveParseDate(Locale loc, String format, String s)
   {
      long d = 0;

      if (!Util.isNull(format) && !Util.isNull(s))
      {
         SimpleDateFormat sdf;
         sdf = new SimpleDateFormat(format, loc);
         sdf.setLenient(false);

         try
         {
            sdf.parse(s);
         }
         catch (Exception e)
         {
            ;
         }

         Calendar cal = sdf.getCalendar();
         Calendar now = Calendar.getInstance();

         if (!cal.isSet(Calendar.DAY_OF_MONTH))
         {
            cal.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
         }

         if (!cal.isSet(Calendar.MONTH))
         {
            cal.set(Calendar.MONTH, now.get(Calendar.MONTH));
         }

         if (!cal.isSet(Calendar.YEAR))
         {
            cal.set(Calendar.YEAR, now.get(Calendar.YEAR));
         }

         if (cal.get(Calendar.YEAR) < 30)
         {
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 2000);
         }
         else if (cal.get(Calendar.YEAR) < 100)
         {
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1900);
         }

         d = cal.getTime().getTime();
      }
      else
      {
         Calendar now = Calendar.getInstance();
         now.set(Calendar.HOUR_OF_DAY, 0);
         now.set(Calendar.MINUTE, 0);
         now.set(Calendar.SECOND, 0);
         d = now.getTime().getTime();
      }

      return d;
   }


   private static long saveParseTime(Locale loc, String format, String s)
   {
      long d = 0;

      if (!Util.isNull(format) && !Util.isNull(s))
      {
         SimpleDateFormat sdf;
         sdf = new SimpleDateFormat(format, loc);
         sdf.setLenient(false);

         try
         {
            sdf.parse(s);
         }
         catch (Exception e)
         {
            ;
         }

         Calendar cal = sdf.getCalendar();

         if (!cal.isSet(Calendar.HOUR_OF_DAY)
                  && !cal.isSet(Calendar.HOUR_OF_DAY))
         {
            cal.set(Calendar.HOUR_OF_DAY, 0);
         }

         if (!cal.isSet(Calendar.MINUTE))
         {
            cal.set(Calendar.MINUTE, 0);
         }

         if (!cal.isSet(Calendar.SECOND))
         {
            cal.set(Calendar.SECOND, 0);
         }

         d = cal.getTime().getTime() + cal.getTimeZone().getRawOffset();
      }

      return d;
   }


   /**
    * Tries to parse a String into a Date value.
    * String mustn't a full date, parts are enough.
    * Parsing will set missing parts to default values
    *
    * @param format   java format string for date/time
    * @return         the parsed date
    */
   public static Date parseDate(String format, String s)
   {
      return parseDate(Locale.getDefault(), format, s);
   }


   /**
    * Tries to parse a String into a Date value.
    * String mustn't a full date, parts are enough.
    * Parsing will set missing parts to default values
    *
    * @param loc      locale to use
    * @param format   java format string for date/time
    * @param s        string to be parsed
    * @return         the parsed date
    */
   public static Date parseDate(Locale loc, String format, String s)
   {
      StringBuffer sDate = new StringBuffer();
      StringBuffer sTime = new StringBuffer();
      StringBuffer fDate = new StringBuffer();
      StringBuffer fTime = new StringBuffer();
      splitDate(s, sDate, sTime);
      splitDate(format, fDate, fTime);

      long dDate = saveParseDate(loc, fDate.toString(), sDate.toString());
      long dTime = saveParseTime(loc, fTime.toString(), sTime.toString());
      Date d     = new Date(dDate + dTime);
      logCat.info("parsed " + s + " to " + d);

      return d;
   }

   /**
    * Parses an ISO8601 date format string
    *
    * @param s        string to be parsed
    * @return         the parsed date
    */
   private static class ISO8601
   {
      private int    year;
		private int    month;
		private int    day;
		private int    hour;
		private int    min;
		private int    sec;
		private int    frac;
		private String tz;
   }

   private static String reISO8601 = "(\\d\\d\\d\\d)(-(\\d\\d)(-(\\d\\d))?)?"
      + "([T| ]?" + "(\\d\\d):(\\d\\d)(:((\\d\\d)(\\.(\\d+))?)?)?"
      + "(Z|([+-]\\d\\d:\\d\\d)|([A-Z]{3}))?)?";

   private static int toInt(String x)
   {
      if (x == null)
      {
         return 0;
      }

      try
      {
         return Integer.parseInt(x);
      }
      catch (NumberFormatException e)
      {
         return 0;
      }
   }


   private static ISO8601 parseISO8601(String s)
   {
      // ISO 8601 datetime: http://www.w3.org/TR/NOTE-datetime
      // e.g. 1997-07-16T19:20:30.45+01:00
      // additions: "T" can be a space, TZ can be a three-char code, TZ can be missing
      try
      {
         RE re = new RE(reISO8601);

         if (re.match(s))
         {
            ISO8601 iso = new ISO8601();
            iso.year     = toInt(re.getParen(1));
            iso.month    = toInt(re.getParen(3));
            iso.day      = toInt(re.getParen(5));
            iso.hour     = toInt(re.getParen(7));
            iso.min      = toInt(re.getParen(8));
            iso.sec      = toInt(re.getParen(11));
            iso.frac     = toInt(re.getParen(13));
            iso.tz       = re.getParen(14);

            return iso;
         }
      }
       // try
      catch (RESyntaxException ree)
      {
         ree.printStackTrace();
      }

      return null;
   }


   /**
    * Parses an ISO8601 date format string
    *
    * @param s        string to be parsed
    * @return         the parsed date
    */
   public static Date parseISO8601Date(String s)
   {
      ISO8601 iso = parseISO8601(s);

      if (iso != null)
      {
         TimeZone tz = null;

         // see if setting tz first fixes tz bug
         if ((iso.tz != null) && !(iso.tz.length() == 0))
         {
            if (iso.tz.equals("Z"))
            {
               tz = TimeZone.getTimeZone("GMT");
            }
            else if (iso.tz.length() == 3)
            {
               tz = TimeZone.getTimeZone(iso.tz);
            }
            else
            {
               tz = TimeZone.getTimeZone("GMT" + iso.tz);
            }
         }

         Calendar cal = new GregorianCalendar(iso.year, iso.month - 1, iso.day,
               iso.hour, iso.min, iso.sec);

         if (tz != null)
         {
            cal.setTimeZone(tz);
         }

         return cal.getTime();
      }
       // if iso

      return null;
   }


   /**
    * finds the end of the given day
    *
    * @param d        date of which end should be find
    * @return         end of the day
    */
   public static Date findEndOfDay(Date d)
   {
      Calendar cal = Calendar.getInstance();
      cal.setTime(d);
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);
      cal.add(Calendar.DAY_OF_MONTH, 1);

      return cal.getTime();
   }


   /**
    * main test routine
    */
   public static void main(String[] args)
   {
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
      System.out.println(parseDate(format, "12.06.03").toLocaleString());
      System.out.println(parseDate(format, "12. 12:").toLocaleString());
      System.out.println(findEndOfDay(parseDate(format, "12. 12:"))
                            .toLocaleString());
   }
}
