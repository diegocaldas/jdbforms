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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.jsp.JspException;

import de.laures.cewolf.taglib.DataAware;
import de.laures.cewolf.DatasetProducer;

import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.OHLCDataItem;
import org.jfree.data.xy.OHLCDataset;

import org.dbforms.taglib.AbstractDbBaseHandlerTag;
import org.dbforms.config.ResultSetVector;
import org.dbforms.util.Util;

/**
 * 
 * This tag defines a HighLow Series for use inside a cewolf timeseries
 * 
 * @author Henner Kollmann
 * 
 */
public class CewolfHighLowSeriesDataTag extends AbstractDbBaseHandlerTag {

	private String timeField;
	private String openField;
	private String highField;
	private String lowField;
	private String closeField;
	private String title;

	public int doEndTag() throws JspException {
		String s = getTitle();
		if (Util.isNull(s)) {
			s = getTimeField();
		}
		CewolfOHLCDataset ds = new CewolfOHLCDataset(s);
		ResultSetVector rsv = getParentForm().getResultSetVector();

		for (int i = 0; i < rsv.size(); i++) {
			Date d = (Date) rsv.getFieldAsObject(i, getTimeField());
			Double dd = (Double) rsv.getFieldAsObject(i, getHighField());
			double h = (dd != null) ? dd.doubleValue() : 0;
			dd = (Double) rsv.getFieldAsObject(i, getOpenField());
			double o = (dd != null) ? dd.doubleValue() : h;
			dd = (Double) rsv.getFieldAsObject(i, getLowField());
			double l = (dd != null) ? dd.doubleValue() : 0;
			dd = (Double) rsv.getFieldAsObject(i, getCloseField());
			double c = (dd != null) ? dd.doubleValue() : l;
			ds.add(0, new OHLCDataItem(d, o, h, l, c, 0));
		}

		DatasetProducer dataProducer = new DbFormsDatasetProducer(ds);
		DataAware dw = (DataAware) findAncestorWithClass(this, DataAware.class);
		dw.setDataProductionConfig(dataProducer, new HashMap(), false);

		return SKIP_BODY;
	}

	public void doFinally() {
		super.doFinally();
	}

	public String getCloseField() {
		return closeField;
	}

	public void setCloseField(String closeField) {
		this.closeField = closeField;
	}

	public String getHighField() {
		return highField;
	}

	public void setHighField(String highField) {
		this.highField = highField;
	}

	public String getLowField() {
		return lowField;
	}

	public void setLowField(String lowField) {
		this.lowField = lowField;
	}

	public String getOpenField() {
		return openField;
	}

	public void setOpenField(String openField) {
		this.openField = openField;
	}

	public String getTimeField() {
		return timeField;
	}

	public void setTimeField(String timeField) {
		this.timeField = timeField;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private class CewolfOHLCDataset extends AbstractXYDataset implements
			OHLCDataset {

		private ArrayList data = new ArrayList();

		private String key;

		/**
		 * Creates a new dataset.
		 * 
		 * @param key
		 *            the series key.
		 * @param data
		 *            the data items.
		 */
		public CewolfOHLCDataset(String key) {
			this.key = key;
		}

		public void add(int series, OHLCDataItem item) {
			data.add(item);
		}

		/**
		 * Returns the series key.
		 * 
		 * @param series
		 *            the series index (ignored).
		 * 
		 * @return The series key.
		 */
		public Comparable getSeriesKey(int series) {
			return key;
		}

		/**
		 * Returns the x-value for a data item.
		 * 
		 * @param series
		 *            the series index (ignored).
		 * @param item
		 *            the item index (zero-based).
		 * 
		 * @return The x-value.
		 */
		public Number getX(int series, int item) {
			return new Long(getXDate(series, item).getTime());
		}

		/**
		 * Returns the x-value for a data item as a date.
		 * 
		 * @param series
		 *            the series index (ignored).
		 * @param item
		 *            the item index (zero-based).
		 * 
		 * @return The x-value as a date.
		 */
		public Date getXDate(int series, int item) {
			return ((OHLCDataItem) data.get(item)).getDate();
		}

		/**
		 * Returns the y-value.
		 * 
		 * @param series
		 *            the series index (ignored).
		 * @param item
		 *            the item index (zero-based).
		 * 
		 * @return The y value.
		 */
		public Number getY(int series, int item) {
			return getClose(series, item);
		}

		/**
		 * Returns the high value.
		 * 
		 * @param series
		 *            the series index (ignored).
		 * @param item
		 *            the item index (zero-based).
		 * 
		 * @return The high value.
		 */
		public Number getHigh(int series, int item) {
			return ((OHLCDataItem) data.get(item)).getHigh();
		}

		/**
		 * Returns the high-value (as a double primitive) for an item within a
		 * series.
		 * 
		 * @param series
		 *            the series (zero-based index).
		 * @param item
		 *            the item (zero-based index).
		 * 
		 * @return The high-value.
		 */
		public double getHighValue(int series, int item) {
			double result = Double.NaN;
			Number high = getHigh(series, item);
			if (high != null) {
				result = high.doubleValue();
			}
			return result;
		}

		/**
		 * Returns the low value.
		 * 
		 * @param series
		 *            the series index (ignored).
		 * @param item
		 *            the item index (zero-based).
		 * 
		 * @return The low value.
		 */
		public Number getLow(int series, int item) {
			return ((OHLCDataItem) data.get(item)).getLow();
		}

		/**
		 * Returns the low-value (as a double primitive) for an item within a
		 * series.
		 * 
		 * @param series
		 *            the series (zero-based index).
		 * @param item
		 *            the item (zero-based index).
		 * 
		 * @return The low-value.
		 */
		public double getLowValue(int series, int item) {
			double result = Double.NaN;
			Number low = getLow(series, item);
			if (low != null) {
				result = low.doubleValue();
			}
			return result;
		}

		/**
		 * Returns the open value.
		 * 
		 * @param series
		 *            the series index (ignored).
		 * @param item
		 *            the item index (zero-based).
		 * 
		 * @return The open value.
		 */
		public Number getOpen(int series, int item) {
			return ((OHLCDataItem) data.get(item)).getOpen();
		}

		/**
		 * Returns the open-value (as a double primitive) for an item within a
		 * series.
		 * 
		 * @param series
		 *            the series (zero-based index).
		 * @param item
		 *            the item (zero-based index).
		 * 
		 * @return The open-value.
		 */
		public double getOpenValue(int series, int item) {
			double result = Double.NaN;
			Number open = getOpen(series, item);
			if (open != null) {
				result = open.doubleValue();
			}
			return result;
		}

		/**
		 * Returns the close value.
		 * 
		 * @param series
		 *            the series index (ignored).
		 * @param item
		 *            the item index (zero-based).
		 * 
		 * @return The close value.
		 */
		public Number getClose(int series, int item) {
			return ((OHLCDataItem) data.get(item)).getClose();
		}

		/**
		 * Returns the close-value (as a double primitive) for an item within a
		 * series.
		 * 
		 * @param series
		 *            the series (zero-based index).
		 * @param item
		 *            the item (zero-based index).
		 * 
		 * @return The close-value.
		 */
		public double getCloseValue(int series, int item) {
			double result = Double.NaN;
			Number close = getClose(series, item);
			if (close != null) {
				result = close.doubleValue();
			}
			return result;
		}

		/**
		 * Returns the trading volume.
		 * 
		 * @param series
		 *            the series index (ignored).
		 * @param item
		 *            the item index (zero-based).
		 * 
		 * @return The trading volume.
		 */
		public Number getVolume(int series, int item) {
			return ((OHLCDataItem) data.get(item)).getVolume();
		}

		/**
		 * Returns the volume-value (as a double primitive) for an item within a
		 * series.
		 * 
		 * @param series
		 *            the series (zero-based index).
		 * @param item
		 *            the item (zero-based index).
		 * 
		 * @return The volume-value.
		 */
		public double getVolumeValue(int series, int item) {
			double result = Double.NaN;
			Number volume = getVolume(series, item);
			if (volume != null) {
				result = volume.doubleValue();
			}
			return result;
		}

		/**
		 * Returns the series count.
		 * 
		 * @return 1.
		 */
		public int getSeriesCount() {
			return 1;
		}

		/**
		 * Returns the item count for the specified series.
		 * 
		 * @param series
		 *            the series index (ignored).
		 * 
		 * @return The item count.
		 */
		public int getItemCount(int series) {
			return data.size();
		}

		/**
		 * Tests this instance for equality with an arbitrary object.
		 * 
		 * @param obj
		 *            the object (<code>null</code> permitted).
		 * 
		 * @return A boolean.
		 */
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof CewolfOHLCDataset)) {
				return false;
			}
			return this.data.equals(((CewolfOHLCDataset) obj).data);
		}

	}

}
