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
package org.dbforms.taglib.cewolf;

import javax.servlet.jsp.JspException;

import org.dbforms.taglib.AbstractDbBaseHandlerTag;
/**
 * 
 * This tag is a sub tag of DbTimeChart and describes a single series for the chart.
 * 
 * @author Henner Kollmann
 * 
 */
public class CewolfTimeSeriesFieldTag extends AbstractDbBaseHandlerTag
		implements javax.servlet.jsp.tagext.TryCatchFinally {

	private String title;
	private String fieldName;
	private String color;


	public void doFinally() {
		super.doFinally();
		title = null;
		fieldName = null;
		color = null;
	}

	public int doStartTag() throws JspException {
		if (getParent() != null
				&& getParent() instanceof CewolfTimeSeriesDataTag) {
			CewolfTimeSeriesDataTag p = (CewolfTimeSeriesDataTag) getParent();	
			p.addField(this);
		} else {
			throw new JspException(
					"TimeSeries element must be placed inside a TimeChart element!");
		}
		return EVAL_BODY_INCLUDE;
	}

	/**
	 * Returns the fieldName.
	 * 
	 * @return String
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Returns the title.
	 * 
	 * @return String
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the fieldName.
	 * 
	 * @param fieldName
	 *            The fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * Sets the title.
	 * 
	 * @param title
	 *            The title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getColor() {
		return color;
	}
}
