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

import java.util.Map;
import org.dbforms.config.ResultSetVector;

/**
 * This is the context of the dbform tag. Used as TEI class element.
 * In the pagecontext is holded a map named dbform with name of form as 
 * key and this context as object.
 *
 * @author  Henner Kollmann
 */
public class DbFormContext {
	Map currentRow;
	String position;
	Map searchFieldNames;
	Map searchFieldModeNames;
	Map searchFieldAlgorithmNames;
	ResultSetVector rsv;

	public DbFormContext(
		Map searchFieldNames,
		Map searchFieldModeNames,
		Map searchFieldAlgorithmNames,
		ResultSetVector rsv) {
		this.searchFieldNames = searchFieldNames;
		this.searchFieldModeNames = searchFieldModeNames;
		this.searchFieldAlgorithmNames = searchFieldAlgorithmNames;
		this.rsv = rsv;
	}

	/**
	 * @return
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * @return
	 */
	public ResultSetVector getRsv() {
		return rsv;
	}

	/**
	 * @return
	 */
	public Map getSearchFieldAlgorithmNames() {
		return searchFieldAlgorithmNames;
	}

	/**
	 * @return
	 */
	public Map getSearchFieldModeNames() {
		return searchFieldModeNames;
	}

	/**
	 * @return
	 */
	public Map getSearchFieldNames() {
		return searchFieldNames;
	}

	/**
	 * @return
	 */
	public Map getCurrentRow() {
		return currentRow;
	}

	/**
	 * @param map
	 */
	public void setCurrentRow(Map map) {
		currentRow = map;
	}

	/**
	 * @param string
	 */
	public void setPosition(String string) {
		position = string;
	}

}
