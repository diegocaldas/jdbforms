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
 * DefaultEscaper
 * 
 */
public class DefaultEscaperImpl implements Escaper {
	public String escapeHTML(String s) {
		int i;
		StringBuffer v = new StringBuffer("");
		for (i = 0; i < s.length(); i++) {
			switch (s.charAt(i)) {
				case '"' :
					v.append("&quot;");
					break;
				case '<' :
					v.append("&lt;");
					break;
				case '>' :
					v.append("&gt;");
					break;
				default :
					v.append(s.charAt(i));
					break;
			}
		}
		return v.toString();
	}

	public String unescapeHTML(String s) {
		return s;
	}
	public String escapeJDBC(String s) {
		return s;
	}
	public String unescapeJDBC(String s) {
		return s;
	}

}
