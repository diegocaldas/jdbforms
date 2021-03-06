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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

/**
 * Helper classes for dealing with time values
 *
 */
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;



/**
 * DOCUMENT ME!
 *
 * @version $Revision$
 * @author $author$
 */
public class TimeUtil {
   private static Log    logCat     = LogFactory.getLog(TimeUtil.class.getName());
   static final int      SECSPERDAY = 24 * 60 * 60;
   private static String reISO8601  = "(\\d\\d\\d\\d)(-(\\d\\d)(-(\\d\\d))?)?"
                                      + "([T| ]?"
                                      + "(\\d\\d):(\\d\\d)(:((\\d\\d)(\\.(\\d+))?)?)?"
                                      + "(Z|([+-]\\d\\d:\\d\\d)|([A-Z]{3}))?)?";

   /**
    * Reformats seconds to time string with format: dd:hh:mm:ss
    *
    * @param seconds string to format
    *
    * @return String
    */
   public static final String seconds2String(String seconds) {
      if (Util.isNull(seconds)) {
         return "";
      } else {
         return seconds2String(Integer.valueOf(seconds));
      }
   }


   /**
    * Reformats seconds to time string with format: dd:hh:mm:ss
    *
    * @param seconds Integer to format
    *
    * @return String
    */
   public static final String seconds2String(Integer seconds) {
      return seconds2String(seconds.intValue());
   }


   /**
    * Reformats seconds to time string with format: dd:hh:mm:ss
    *
    * @param seconds string to format
    *
    * @return String
    */
   public static final String seconds2String(Long seconds) {
      return seconds2String(seconds.longValue());
   }


   /**
    * Reformats seconds to time string with format: dd:hh:mm:ss
    *
    * @param seconds string to format
    *
    * @return String
    */
   public static final String seconds2String(Number seconds) {
      return seconds2String(seconds.longValue());
   }


   /**
    * Reformats seconds to time string with format: dd:hh:mm:ss
    *
    * @param seconds string to format
    *
    * @return String
    */
   public static final String seconds2String(long seconds) {
      long   d;
      long   h;
      long   m;
      String zeit;
      d       = (seconds / SECSPERDAY);
      seconds = seconds - (d * SECSPERDAY);
      h       = seconds / (60 * 60);
      seconds = seconds - (h * 60 * 60);
      m       = seconds / 60;
      seconds = seconds - (m * 60);

      if (d > 0) {
         Object[] o = {
                         new Long(d),
                         new Long(h),
                         new Long(m),
                         new Long(seconds)
                      };
         zeit = Util.sprintf("%i:%02i:%02i:%02i", o);
      } else {
         Object[] o = {
                         new Long(h),
                         new Long(m),
                         new Long(seconds)
                      };
         zeit = Util.sprintf("%i:%02i:%02i", o);
      }

      return zeit;
   }

   /**
    * Reformats minutes to time string with format: dd:hh:mm
    *
    * @param seconds string to format
    *
    * @return String
    */
   public static final String minutes2String(long minutes) {
      long   d;
      long   h;
      String zeit;
      d       = (minutes / SECSPERDAY * 60);
      minutes = minutes - (d * SECSPERDAY * 60);
      h       = minutes / (60);
      minutes = minutes - (h * 60);

      if (d > 0) {
         Object[] o = {
                         new Long(d),
                         new Long(h),
                         new Long(minutes)
                      };
         zeit = Util.sprintf("%i:%02i:%02i", o);
      } else {
         Object[] o = {
                         new Long(h),
                         new Long(minutes),
                      };
         zeit = Util.sprintf("%i:%02i", o);
      }

      return zeit;
   }


   /**
    * finds the end of the given day
    *
    * @param d        date of which end should be find
    *
    * @return end of the day
    */
   public static Date findEndOfDay(Date d) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(d);
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);
      cal.add(Calendar.DAY_OF_MONTH, 1);

      return cal.getTime();
   }


   /**
    * Tries to parse a String into a Calendar objectvalue. String mustn't a full date,
    * parts are enough. Parsing will set missing parts to default values
    *
    * @param loc      locale to use
    * @param format   java format string for date/time
    * @param s        string to be parsed
    *
    * @return the parsed date
    */
   public static Calendar parseDate(final SimpleDateFormat format,
                                    String                 s) {
      StringBuffer sDate = new StringBuffer();
      StringBuffer sTime = new StringBuffer();
      StringBuffer fDate = new StringBuffer();
      StringBuffer fTime = new StringBuffer();

      splitDate(s, sDate, sTime);
      splitDate(format.toPattern(), fDate, fTime);

      SimpleDateFormat f     = (SimpleDateFormat) format.clone();
      Calendar         dDate = saveParseDate(f, fDate.toString(),
                                             sDate.toString());
      long             date = dDate.getTime()
                                   .getTime();
      f.setTimeZone(dDate.getTimeZone());

      long time   = saveParseTime(f, fTime.toString(), sTime.toString());
      long offset = dDate.getTimeZone()
                         .getRawOffset();

      if (!Util.isNull(sTime.toString())) {
         time = time + offset;
      }

      date = date + time;

      Calendar c = format.getCalendar();

      //20040304 JFM: replaced Calendar.setTimeInMillis(long) 
      Date dateAsDate = new Date(date);
      c.setTime(dateAsDate);

      logCat.info("parsed " + s + " to " + format.format(c.getTime()));

      return c;
   }


   /**
    * Parses an ISO8601 date format string
    *
    * @param s        string to be parsed
    *
    * @return the parsed date
    */
   public static Date parseISO8601Date(String s) {
      ISO8601 iso = parseISO8601(s);

      if (iso != null) {
         TimeZone tz = null;

         // see if setting tz first fixes tz bug
         if ((iso.tz != null) && !(iso.tz.length() == 0)) {
            if (iso.tz.equals("Z")) {
               tz = TimeZone.getTimeZone("GMT");
            } else if (iso.tz.length() == 3) {
               tz = TimeZone.getTimeZone(iso.tz);
            } else {
               tz = TimeZone.getTimeZone("GMT" + iso.tz);
            }
         }

         Calendar cal = new GregorianCalendar(iso.year, iso.month - 1, iso.day,
                                              iso.hour, iso.min, iso.sec);

         if (tz != null) {
            cal.setTimeZone(tz);
         }

         return cal.getTime();
      }

      // if iso
      return null;
   }


   private static ISO8601 parseISO8601(String s) {
      // ISO 8601 datetime: http://www.w3.org/TR/NOTE-datetime
      // e.g. 1997-07-16T19:20:30.45+01:00
      // additions: "T" can be a space, TZ can be a three-char code, TZ can be missing
      try {
         RE re = new RE(reISO8601);

         if (re.match(s)) {
            ISO8601 iso = new ISO8601();
            iso.year  = toInt(re.getParen(1));
            iso.month = toInt(re.getParen(3));
            iso.day   = toInt(re.getParen(5));
            iso.hour  = toInt(re.getParen(7));
            iso.min   = toInt(re.getParen(8));
            iso.sec   = toInt(re.getParen(11));
            iso.frac  = toInt(re.getParen(13));
            iso.tz    = re.getParen(14);

            return iso;
         }
      }
      // try
      catch (RESyntaxException ree) {
         ree.printStackTrace();
      }

      return null;
   }


   private static Calendar saveParseDate(final SimpleDateFormat format,
                                         String                 formatString,
                                         String                 s)
                                  throws NumberFormatException {
      Date     d   = null;
      Calendar cal;
      Calendar now = Calendar.getInstance();

      if (!Util.isNull(s)) {
         if (!Util.isNull(formatString)) {
            format.applyPattern(formatString);
         }

         try {
            d = format.parse(s);
         } catch (Exception e) {
            logCat.error(e);

            // Make parsing more tolerant - try sql standard format too
            SimpleDateFormat f = (SimpleDateFormat) format.clone();
            f.applyPattern("yyyy-MM-dd");

            try {
               d = f.parse(s);
            } catch (Exception ex) {
               logCat.error(ex);
            }
         }

         cal = format.getCalendar();

         if (d != null) {
            cal.setTime(d);
         } else {
            synchronized (cal) {
               try {
                  // HKK: I do not know why this is necessary, but if you do not wait
                  //      the calender HOUR_OF_DAY maybe unset...
                  cal.wait(5);
               } catch (Exception ex) {
                  logCat.error(ex);
               }
            }
         }

         if (!cal.isSet(Calendar.DAY_OF_MONTH)
                   && !cal.isSet(Calendar.MONTH)
                   && !cal.isSet(Calendar.YEAR)) {
            throw new NumberFormatException("wrong date format");
         }

         if (!cal.isSet(Calendar.DAY_OF_MONTH)) {
            cal.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
         }

         if (!cal.isSet(Calendar.MONTH)) {
            cal.set(Calendar.MONTH, now.get(Calendar.MONTH));
         }

         if (!cal.isSet(Calendar.YEAR)) {
            cal.set(Calendar.YEAR, now.get(Calendar.YEAR));
         }

         if (cal.get(Calendar.YEAR) < 30) {
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 2000);
         } else if (cal.get(Calendar.YEAR) < 100) {
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1900);
         }
      } else {
         cal = now;
      }

      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);

      return cal;
   }


   private static synchronized long saveParseTime(final SimpleDateFormat format,
                                                  String                 formatString,
                                                  String                 s) {
      Date d = null;

      if (!Util.isNull(s)) {
         if (!Util.isNull(formatString)) {
            format.applyPattern(formatString);
         }

         try {
            d = format.parse(s);
         } catch (Exception e) {
            logCat.error(e);

            // Make parsing more tolerant - try 24 hour formats too
            SimpleDateFormat f = (SimpleDateFormat) format.clone();
            f.applyPattern("HH:mm:ss");

            try {
               d = f.parse(s);
            } catch (Exception ex) {
               logCat.error(ex);
            }
         }

         Calendar cal = format.getCalendar();

         if (d != null) {
            cal.setTime(d);
         } else {
            synchronized (cal) {
               try {
                  // HKK: I do not know why this is necessary, but if you do not wait
                  //      the calender HOUR_OF_DAY maybe unset...
                  cal.wait(5);
               } catch (Exception ex) {
                  logCat.error(ex);
               }
            }
         }

         if (!cal.isSet(Calendar.HOUR_OF_DAY)
                   && !cal.isSet(Calendar.MINUTE)
                   && !cal.isSet(Calendar.SECOND)) {
            throw new NumberFormatException("wrong time format");
         }

         if (!cal.isSet(Calendar.HOUR_OF_DAY)) {
            cal.set(Calendar.HOUR_OF_DAY, 0);
         }

         if (!cal.isSet(Calendar.MINUTE)) {
            cal.set(Calendar.MINUTE, 0);
         }

         if (!cal.isSet(Calendar.SECOND)) {
            cal.set(Calendar.SECOND, 0);
         }

         return cal.getTime()
                   .getTime();
      }

      return 0;
   }


   public static void splitDate(final String format,
                                 StringBuffer sDate,
                                 StringBuffer sTime) {
      sDate.setLength(0);
      sTime.setLength(0);

      int i = format.lastIndexOf(':');

      if (i > -1) {
         i = format.lastIndexOf(' ', i);

         if (i > -1) {
            sDate.append(format.substring(0, i));
            sTime.append(format.substring(i + 1));
         } else {
            sTime.append(format);
         }
      } else {
         sDate.append(format);
      }
   }


   private static int toInt(String x) {
      if (x == null) {
         return 0;
      }

      try {
         return Integer.parseInt(x);
      } catch (NumberFormatException e) {
         return 0;
      }
   }

   /**
    * Parses an ISO8601 date format string
    *
    */
   private static class ISO8601 {
      public String tz;
      public int    day;
      public int    frac;
      public int    hour;
      public int    min;
      public int    month;
      public int    sec;
      public int    year;
   }
}
