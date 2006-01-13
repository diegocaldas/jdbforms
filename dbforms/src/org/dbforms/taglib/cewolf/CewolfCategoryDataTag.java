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

import java.util.HashMap;

import javax.servlet.jsp.JspException;

import de.laures.cewolf.taglib.DataAware;
import de.laures.cewolf.DatasetProducer;

import org.jfree.data.category.DefaultCategoryDataset;

import org.dbforms.taglib.AbstractDbBaseHandlerTag;
import org.dbforms.util.CewolfDatasetProducer;
import org.dbforms.config.ResultSetVector;

/** 
 * Tag &lt;producer&gt; which defines a DatasetProducer.
 * @see DataTag
 * @author  Henner Kollmann 
 */
public class CewolfCategoryDataTag extends AbstractDbBaseHandlerTag  {
	private String categoryField;
	private String dataField;
	private String seriesField;
	

    public String getSeriesField() {
		return seriesField;
	}


	public void setSeriesField(String seriesField) {
		this.seriesField = seriesField;
	}


	public int doEndTag() throws JspException {
    	DefaultCategoryDataset ds = new DefaultCategoryDataset();
    	ResultSetVector rsv = getParentForm().getResultSetVector();
    	for (int i = 0; i < rsv.size(); i++) {
        	Comparable c = (Comparable) rsv.getFieldAsObject(i, getCategoryField());
    		if (c == null)
     		   c = new String();	
        	Comparable s = (Comparable) rsv.getFieldAsObject(i, getSeriesField());
    		Number n = (Number) rsv.getFieldAsObject(i, getDataField());
    		if (s == null)
    		   s = new String();	
    		ds.addValue(n, s, c);
    	}
    	DatasetProducer dataProducer = new CewolfDatasetProducer(ds);
        DataAware dw = (DataAware) findAncestorWithClass(this, DataAware.class);
        dw.setDataProductionConfig(dataProducer, new HashMap(), false);
        return SKIP_BODY;
    }


	/**
	 * @return Returns the categoryField.
	 */
	public String getCategoryField() {
		return categoryField;
	}
	/**
	 * @param categoryField The categoryField to set.
	 */
	public void setCategoryField(String categoryField) {
		this.categoryField = categoryField;
	}
	/**
	 * @return Returns the dataField.
	 */
	public String getDataField() {
		return dataField;
	}
	/**
	 * @param dataField The dataField to set.
	 */
	public void setDataField(String dataField) {
		this.dataField = dataField;
	}

	public void doFinally() {
		super.doFinally();
		categoryField = null;
		dataField = null;
		seriesField = null;
	}
	
}
