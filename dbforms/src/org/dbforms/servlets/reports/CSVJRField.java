/*
 * Created on May 27, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
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
