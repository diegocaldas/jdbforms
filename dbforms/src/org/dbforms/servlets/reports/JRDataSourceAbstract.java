/*
 * $Header:
 * /cvsroot/jdbforms/dbforms/src/org/dbforms/servlets/reports/JRDataSourceRSV.java,v
 * 1.15 2004/10/11 08:55:02 hkollmann Exp $ $Revision$ $Date: 2004/10/11
 * 08:55:02 $
 * 
 * DbForms - a Rapid Application Development Framework Copyright (C) 2001
 * Joachim Peer <joepeer@excite.com>
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package org.dbforms.servlets.reports;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRDataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

public abstract class JRDataSourceAbstract implements JRDataSource {
	private static Log logCat = LogFactory.getLog(JRDataSourceAbstract.class
			.getName());
	private Map attributes;
	
	JRDataSourceAbstract(Map attributes) {
		this.attributes = attributes;
	}
	/**
	 * @see net.sf.jasperreports.engine.JRDataSource#next()
	 */
	public abstract boolean next() throws JRException;

	/**
	 * @see net.sf.jasperreports.engine.JRDataSource#getFieldValue(net.sf.jasperreports.engine.JRField)
	 *      Philip Grunikiewicz 2004-01-13 Because I had fields defined
	 *      (dbforms-config.xml) in mix case (ie: creditLimit) and in my XML
	 *      file, my field was in uppercase (ie: CREDITLIMIT), my field could
	 *      not be found. Added some logging to help out debugging this type of
	 *      problem.
	 */
	public Object getFieldValue(JRField field) throws JRException {
		String search = field.getName();
		logCat.debug("Trying to find data for field named: " + search);
		Object o = getFieldValue(field.getName());
		if (o == null) {
			logCat
					.debug("Field not found in dbforms-config, trying field renamed to uppercase: "
							+ search.toUpperCase());
			o = getFieldValue(search.toUpperCase());
		}
		if (o == null) {
			logCat
					.debug("Field not found in dbforms-config, trying field renamed to lowercase: "
							+ search.toLowerCase());
			o = getFieldValue(field.getName().toLowerCase());
		}

		/*
		 * // Try class conversation if the classes do not match! if ((o !=
		 * null) && (o.getClass() != field.getValueClass())) { try { Object[]
		 * constructorArgs = new Object[] { o.toString() }; Class[]
		 * constructorArgsTypes = new Class[] { String.class }; o =
		 * ReflectionUtil.newInstance(field.getValueClass(),
		 * constructorArgsTypes, constructorArgs); } catch (Exception e) { ; } }
		 */
		return o;
	}

	public abstract Object getFieldValue(String fieldName);

	/**
	 * @return Returns the attributes.
	 */
	public Map getAttributes() {
		return attributes;
	}
}