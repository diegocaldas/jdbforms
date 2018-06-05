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


import java.util.StringTokenizer;
import java.util.Vector;




/**
 * This utility-class provides convenience methods for parsing strings and
 * generating certain data structures
 *
 * @author Joe Peer
 */
public class StringUtil {

	/**
	    * Method for parsing substring embedded by constant delimeters.
	    * <br>
	    * consider the following string s: ac_update_3_12
	    * <br>
	    * <pre>
	    *  getEmbeddedString(s, 0, '_') ==> "ac"
	    *  getEmbeddedString(s, 1, '_') ==> "update"
	    *  getEmbeddedString(s, 2, '_') ==> "3"
	    *  getEmbeddedString(s, 3, '_') ==> "12"
	    *  getEmbeddedString(s, 3, '_') ==> will throw a Runtime Exception
	    * </pre>
	    *
	    * @param str the string to parse
	    * @param afterDelims the delimiter occurence where to start to parse
	    * @param delim the delimiter string
	    * @return the substring contained between the <code>afterDelims</code> delimiter occurence
	    *         and the next one
	    */
	   public static String getEmbeddedString(String str,
	                                          int    afterDelims,
	                                          char   delim) {
	      int lastIndex = 0;
	
	      for (int i = 0; i < afterDelims; i++) {
	         lastIndex = str.indexOf(delim, lastIndex) + 1; // search end of cutting
	      }
	
	      int nextIndex = str.indexOf(delim, lastIndex); // end of cutting
	
	      if (nextIndex == -1) {
	         // new: 18.1.2001: support for IMAGE buttons #checkme: can we swith this off?
	         int dotIndex = str.lastIndexOf('.');
	         nextIndex = (dotIndex == -1) ? str.length()
	                                      : dotIndex;
	      }
	
	      return str.substring(lastIndex, nextIndex);
	   }

	/**
	    *  Get the int value of the substring contained between
	    *  the <code>afterDelims</code> delimiter occurence and the next one
	    *
	    * @param str the string to parse
	    * @param afterDelims the delimiter occurence where to start to parse
	    * @param delim the delimiter string
	    * @return the int value of the substring contained between
	    *         the <code>afterDelims</code> delimiter occurence and the next one
	    */
	   public static int getEmbeddedStringAsInteger(String str,
	                                                int    afterDelims,
	                                                char   delim) {
	      return Integer.parseInt(getEmbeddedString(str, afterDelims, delim));
	   }

	/**
	    * Method for parsing substring embedded by constant delimeters
	    * <br>
	    * Because getEmbeddedString() support "." for image button, this method do
	    * the the same, but ignore dots. It's a patch and must be revised in the
	    * next cleanup... #checkme
	    * <br>
	    * consider the following string s: English-001:param1, param2
	    *
	    * <pre>
	    *  getEmbeddedString(s, 0, '_') ==> ""
	    *  getEmbeddedString(s, 1, '_') ==> ""
	    *  getEmbeddedString(s, 2, '_') ==> ""
	    *  getEmbeddedString(s, 3, '_') ==> ""
	    *  getEmbeddedString(s, 3, '_') ==> will throw a Runtime Exception
	    * </pre>
	    *
	    * @param str the string to parse
	    * @param afterDelims the delimiter occurence where to start to parse
	    * @param delim the delimiter string
	    *
	    * @return the substring contained between the <code>afterDelims</code> delimiter occurence
	    *         and the next one
	    */
	   public static String getEmbeddedStringWithoutDots(String str,
	                                                     int    afterDelims,
	                                                     char   delim) {
	      int lastIndex = 0;
	
	      for (int i = 0; i < afterDelims; i++) {
	         lastIndex = str.indexOf(delim, lastIndex) + 1; // search end of cutting
	      }
	
	      int nextIndex = str.indexOf(delim, lastIndex); // end of cutting
	
	      if (nextIndex == -1) {
	         nextIndex = str.length();
	      }
	
	      return str.substring(lastIndex, nextIndex);
	   }

	/**
	    * Get a Vector object containing all the tokens
	    * related to the input string, splitted using the input delimiter.
	    *
	    * @param str the string to split
	    * @param delimeter the delimiter string
	    *
	    * @return a Vector object containing all the tokens
	    *         related to the input string
	    */
	   public static Vector splitString(String str,
	                                    String delimeter) {
	      Vector          result = new Vector();
	      StringTokenizer st = new StringTokenizer(str, delimeter);
	
	      while (st.hasMoreTokens())
	         result.addElement(st.nextToken());
	
	      return result;
	   }
   
}
