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

import org.jfree.data.time.TimeSeriesCollection;
import org.dbforms.config.ResultSetVector;
/**
 * special TimeSeriesCollection used inside the framwork wich holds the whole
 * ResultSetVector too.
 * 
 * This class is used by the CewolfTimeSeriesDataTag to produce the data.
 * 
 * @author Henner Kollmann
 */


public class DbFormsTimeSeriesCollection extends TimeSeriesCollection {
	private ResultSetVector rsv;

	DbFormsTimeSeriesCollection(ResultSetVector rsv) {
		this.rsv = rsv;
	}

	public ResultSetVector getRsv() {
		return rsv;
	}

}
