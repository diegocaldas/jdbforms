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

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.config.ResultSetVector;
import java.util.Map;

/**
 * use a ResultSetVector as data source.
 */
public final class JRDataSourceRSV extends JRDataSourceAbstract  {
	private static Log logCat = LogFactory.getLog(JRDataSourceRSV.class
			.getName());

	private ResultSetVector rsv;
	/**
	 * Constructor for JRDataSourceRSV.
	 * 
	 * @param rsv
	 *            DOCUMENT ME!
	 * @param pageContext
	 *            DOCUMENT ME!
	 */
	public JRDataSourceRSV(Map attributes, ResultSetVector rsv) {
		super(attributes);
		this.rsv = rsv;
		this.rsv.moveFirst();
		// Set the pointer to one place before the first record
		// Retriving data for JRDataSource will start with a moveNext!
		this.rsv.movePrevious();
	}

	/**
	 * @see net.sf.jasperreports.engine.JRDataSource#next()
	 */
	public boolean next() throws JRException {
		if (ResultSetVector.isNull(rsv)) {
			return false;
		}
		if (rsv.isLast()) {
			return false;
		}
		rsv.moveNext();
		return true;
	}

	public Object getFieldValue(String fieldName) {
		Object o = null;
		try {
			o = rsv.getFieldAsObject(fieldName);
		} catch (Exception e) {
			logCat.error("getFieldValue", e);
		}
		return o;
	}

}
