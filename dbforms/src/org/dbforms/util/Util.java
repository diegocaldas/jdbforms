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

import java.text.Format;
import java.net.URLEncoder;
import java.net.URLDecoder;
import org.dbforms.util.external.PrintfFormat;
import java.io.UnsupportedEncodingException;

/**
 * Simple general utility class
 */
public class Util {

	private static final String REALPATH = "$(SERVLETCONTEXT_REALPATH)";

	/**
	 * Test if the input string is null or empty (does not contain any
	 * character)
	 * 
	 * @param s
	 *            the string value to test
	 * 
	 * @return true if the input string is null or empty, false otherwise
	 */
	public static final boolean isNull(String s) {
		return ((s == null) || (s.trim().length() == 0));
	}

	/**
	 * Replaces the occurens from REALPATH in s with realpath.
	 * 
	 * @param s
	 *            the string containing the REALPATH token
	 * @param realpath
	 *            the value used to replace the REALPATH token
	 * 
	 * @return the input string, with the REALPATH token replaced with the
	 *         realpath value
	 */
	public static final String replaceRealPath(String s, String realpath) {
		if (!isNull(s) && !isNull(realpath)) {
			// 20030604-HKK: Bugfixing for different engine, e.g. cactus. Path
			// maybe without trailing '/'!!!
			if (realpath.charAt(realpath.length() - 1) != '/') {
				realpath = realpath + '/';
			}
			int i = s.indexOf(REALPATH);
			while (i >= 0) {
				StringBuffer buf = new StringBuffer();
				buf.append(s.substring(0, i));
				buf.append(realpath);
				buf.append(s.substring(i + REALPATH.length() + 1));
				s = buf.toString();
				i = s.indexOf(REALPATH);
			}
		}

		return s;
	}

	/**
	 * Encode a string with desired character encoding.
	 * 
	 * @param s
	 *            the string to encode
	 * @param enc
	 *            the desired encoding
	 * 
	 * @return the encoded string
	 * @throws UnsupportedEncodingException
	 *             DOCUMENT ME!
	 */
	public static final String encode(String s, String enc)
			throws UnsupportedEncodingException {
		if (!Util.isNull(s)) {
			try {
				s = encCheck(s, enc);
			} catch (NoSuchMethodError nsme) {
				s = URLEncoder.encode(s);
			}
		}

		return s;
	}

	/**
	 * Decodes a string
	 * 
	 * @param s
	 *            the string to encode
	 * 
	 * @return the encoded string
	 */
	public static final String decode(String s, String enc)
			throws UnsupportedEncodingException {
		if (!Util.isNull(s)) {
			try {
				s = decCheck(s, enc);
			} catch (NoSuchMethodError nsme) {
				s = URLDecoder.decode(s);
			}
		}

		return s;
	}

	/**
	 * returns a formated string
	 * 
	 * @param format
	 *            format string
	 * @param o
	 *            objects to use to format
	 * 
	 * @return String
	 */
	public static String sprintf(String format, Object[] o) {
		PrintfFormat printfFormat = new PrintfFormat(format); // create
		// instance of
		// PrintfFormat
		// class

		return printfFormat.sprintf(o);
	}

	/**
	 * needed by Util.encode(s,enc) to check if URLEncoder.encode(String s,
	 * String enc) is available (from jdk 1.4)
	 * 
	 * @param s
	 *            the string to encode
	 * @param enc
	 *            the desired encoding
	 * 
	 * @return the encoded string
	 * 
	 * @throws UnsupportedEncodingException
	 *             DOCUMENT ME!
	 * @throws NoSuchMethodException
	 *             to signal that jdk 1.3 is being used
	 */
	private static final String decCheck(String s, String enc)
			throws UnsupportedEncodingException, NoSuchMethodError {
		if (isNull(enc)) {
			enc = "UTF-8";
		}
		return URLDecoder.decode(s, enc);
	}

	/**
	 * needed by Util.encode(s,enc) to check if URLEncoder.encode(String s,
	 * String enc) is available (from jdk 1.4)
	 * 
	 * @param s
	 *            the string to encode
	 * @param enc
	 *            the desired encoding
	 * 
	 * @return the encoded string
	 * 
	 * @throws UnsupportedEncodingException
	 *             DOCUMENT ME!
	 * @throws NoSuchMethodException
	 *             to signal that jdk 1.3 is being used
	 */
	private static final String encCheck(String s, String enc)
			throws UnsupportedEncodingException, NoSuchMethodError {
		if (isNull(enc)) {
			enc = "UTF-8";
		}
		s = URLEncoder.encode(s, enc);
		return s;
	}

	public static String getPattern(Format f) {
		if (f instanceof java.text.DecimalFormat)
			return ((java.text.DecimalFormat) f).toPattern();
		else if (f instanceof java.text.SimpleDateFormat)
			return ((java.text.SimpleDateFormat) f).toPattern();
		else
			return null;
	}
	
	public static boolean getTrue(String value) {
		return "true".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value);
	}

	public static boolean getFalse(String value) {
		return !Util.getTrue(value);
	}

}