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

package org.dbforms.servlets.reports;

import dori.jasper.engine.JRField;

/**
 * @author Neal Katz
 *
 */
public class CSVJRField implements JRField {
	private Class valueClass;
	String name = null;
	String description = null;

	/**
	 * @param n
	 * @param d
	 */
	public CSVJRField(String n, String d) {
		this(n, d, String.class);
	}
	
	/**
	 * @param n
	 * @param d
	 * @param c
	 */
	CSVJRField(String n, String d, Class c) {
		name = n;
		description = d;
		valueClass = c;
	}

	/* (non-Javadoc)
	 * @see dori.jasper.engine.JRField#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see dori.jasper.engine.JRField#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see dori.jasper.engine.JRField#setDescription(java.lang.String)
	 */
	public void setDescription(String arg0) {
		description = arg0;
	}

	/* (non-Javadoc)
	 * @see dori.jasper.engine.JRField#getValueClass()
	 */
	public Class getValueClass() {
		return valueClass;
	}

	/* (non-Javadoc)
	 * @see dori.jasper.engine.JRField#getValueClassName()
	 */
	public String getValueClassName() {
		return valueClass.getName();
	}

}
