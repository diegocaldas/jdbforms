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

import java.net.URLEncoder;
import java.io.*;
import org.dbforms.DbFormsConfig;


/**
 * Simple general utility class
 */
public class Util
{

	private static final String REALPATH = "$(SERVLETCONTEXT_REALPATH)";

    /**
     * Test if the input string is null or empty (does not contain any character)
     *
     * @param s the string value to test
     * @return true if the input string is null or empty, false otherwise
     */
    public final static boolean isNull(String s)
    {
        return ((s == null) || (s.trim().length() == 0)) ? true : false;
    }
    
	/** 
     *  
	 * replaces the occurens from REALPATH in s with realpath
	 * 
	 */
    public final static String replaceRealPath(String s, String realpath) {
    	if (!isNull(realpath) ) {
           int i = s.indexOf(REALPATH);
           if (i > 0) {
              StringBuffer buf = new StringBuffer();
              buf.append(s.substring(0,i));
              buf.append(realpath);
              buf.append(s.substring(i + REALPATH.length() + 1));
              s = buf.toString();
           }	
        }	
        return s;
    }

	/** 
    *  
	 * replaces the occurens from REALPATH in s with config.getRealPath()
	 * 
	 */
    public final static String replaceRealPath(String s, DbFormsConfig config) {
		return replaceRealPath(s, config.getRealPath());
    }

	/** 
    *  
	 * encodes a string with "ISO8859-1". This is the default in the servlet engine (tomcat)
	 * hope that's the same in the other ones...
	 * 
	 */
    public final static String encode(String s) throws UnsupportedEncodingException {
			if (!Util.isNull(s)) {
				s = URLEncoder.encode(s, "ISO8859-1");			
			}
			return s;
    }


}
