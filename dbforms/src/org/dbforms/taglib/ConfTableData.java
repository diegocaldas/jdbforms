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
package org.dbforms.taglib;

import java.util.Vector;

import java.sql.Connection;
import java.sql.SQLException;


import org.dbforms.config.FieldValue;
import org.dbforms.config.ResultSetVector;
import org.dbforms.config.Table;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.DbFormsConfigRegistry;

import org.dbforms.event.datalist.dao.DataSourceFactory;

import org.apache.log4j.Category;



/****
 *
 * external data to be nested into radio, checkbox or select - tag!
 * (useful only in conjunction with radio, checkbox or select - tag)
 *
 * <tagclass>org.dbforms.taglib.ConfTableData</tagclass>
 * <bodycontent>empty</bodycontent>
 *
 *
 * <p>this tag provides data to radio, checkbox or select - tags. it may be used for
 * cross-references to other tables.</p>
 *
 * <p>
 * this tag provides similar functionality to "TabData", but as it allows to
 * use the table data given in the conf file and use a filter clause like in dbform tag
 * </p>
 *
 * @author Henner Kollmann
 */
public class ConfTableData extends EmbeddedData
{
   static Category logCat = Category.getInstance(ConfTableData.class.getName());

	// logging category for this class
	private String foreignTable;
	private String visibleFields;
	private String storeField;
	private String filter;
	private String orderBy;

	/**
	 * DOCUMENT ME!
	 *
	 * @param foreignTable DOCUMENT ME!
	 */
	public void setForeignTable(String foreignTable)
	{
		this.foreignTable = foreignTable;
	}


	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getForeignTable()
	{
		return foreignTable;
	}


	/**
	 * DOCUMENT ME!
	 *
	 * @param visibleFields DOCUMENT ME!
	 */
	public void setVisibleFields(String visibleFields)
	{
		this.visibleFields = visibleFields;
	}


	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getVisibleFields()
	{
		return visibleFields;
	}


	/**
	 * DOCUMENT ME!
	 *
	 * @param storeField DOCUMENT ME!
	 */
	public void setStoreField(String storeField)
	{
		this.storeField = storeField;
	}


	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getStoreField()
	{
		return storeField;
	}


	/**
	 * DOCUMENT ME!
	 *
	 * @param orderBy DOCUMENT ME!
	 */
	public void setOrderBy(String orderBy)
	{
		this.orderBy = orderBy;
	}


	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getOrderBy()
	{
		return orderBy;
	}

	/**
	 * @return the filter
	 */
	public String getFilter()
	{
		return filter;
	}

	/**
	 * @param string
	 */
	public void setFilter(String string)
	{
		filter = string;
	}

	/**
	returns Hashtable with data. Its keys represent the "value"-fields for the DataContainer-Tag, its values
	represent the visible fields for the Multitags.
	(DataContainer are: select, radio, checkbox and a special flavour of Label).
	*/
	protected Vector fetchData(Connection con) throws SQLException
	{
		try {
			DbFormsConfig config = DbFormsConfigRegistry.instance().lookup();
			Table table = config.getTableByName(getForeignTable());
			FieldValue[] orderConstraint = table.createOrderFieldValues(getOrderBy(), null, false);
			FieldValue[] childFieldValues = table.getFilterFieldArray(getFilter(), getParentForm().getLocale());
			DataSourceFactory qry = new DataSourceFactory(null, con, table);
			qry.setSelect(childFieldValues, orderConstraint, null, null);
			ResultSetVector rsv = qry.getCurrent(null, 0);
			return formatEmbeddedResultRows(rsv);
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
		
	}


}
