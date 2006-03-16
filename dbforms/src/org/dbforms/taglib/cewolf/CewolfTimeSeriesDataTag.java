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

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.Map;

import java.awt.Paint;

import javax.servlet.jsp.JspException;

import de.laures.cewolf.taglib.DataAware;
import de.laures.cewolf.taglib.tags.AbstractChartTag;
import de.laures.cewolf.taglib.util.PageUtils;
import de.laures.cewolf.ChartPostProcessor;
import de.laures.cewolf.DatasetProducer;
import de.laures.cewolf.taglib.util.ColorHelper;

import org.jfree.data.xy.XYDataset;

import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.time.TimeSeriesCollection;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.taglib.AbstractDbBaseHandlerTag;
import org.dbforms.util.ReflectionUtil;
import org.dbforms.util.Util;
import org.dbforms.config.ResultSetVector;
/**
 * 
 * This tag defines TimeSeries for cewolf
 * 
 * @author Henner Kollmann
 * 
 */

public class CewolfTimeSeriesDataTag extends AbstractDbBaseHandlerTag implements
		javax.servlet.jsp.tagext.TryCatchFinally {

	private String timeField;

	private String rendererclass;

	private Vector fieldList = new Vector();

	private Log logCat = LogFactory.getLog(this.getClass().getName());

	private static class CewolfTimeSeriesData implements java.io.Serializable {

		private String title;
		private String fieldName;
		private Paint color;

		public CewolfTimeSeriesData(CewolfTimeSeriesFieldTag tag) {
			title = tag.getTitle();
			fieldName = tag.getFieldName();
			color = ColorHelper.getColor(tag.getColor());
		}

		public Paint getColor() {
			return color;
		}

		public String getFieldName() {
			return fieldName;
		}

		public String getTitle() {
			return title;
		}

	}

	private class MyChartPostProcessor implements ChartPostProcessor {

		public void processChart(Object chart, Map args) {
		    XYItemRenderer rend = null;
			XYPlot plot = ((JFreeChart) chart).getXYPlot();

			XYDataset ds = (XYDataset) args.get("dataset");
		    int rendererIndex = plot.getIndexOf(plot.getRendererForDataset(ds));

			String rendClass = (String) args.get("renderer");
			if (!Util.isNull(rendClass)) {
				try {
					rend = (XYItemRenderer) ReflectionUtil.newInstance(rendClass);
					plot.setRenderer(rendererIndex, rend);
				} catch (Exception e) {
					logCat.error("::getEvent - cannot create the new renderer ["
							+ rendClass + "]", e);
				}
			}

			if (rend == null) {
				rend = plot.getRenderer(rendererIndex);
			}
			
			for (int i = 0; i < ds.getSeriesCount(); i++) {
				Object o = ds.getSeriesKey(i);
				CewolfTimeSeriesData series = (CewolfTimeSeriesData) args.get(o);
				rend.setSeriesPaint(i, series.getColor());
			}
		}
	};

	public void addField(CewolfTimeSeriesFieldTag field) {
		fieldList.add(new CewolfTimeSeriesData(field));
	}

	public int doEndTag() throws JspException {

		ResultSetVector rsv = getParentForm().getResultSetVector();

		TimeSeriesCollection ds = new DbFormsTimeSeriesCollection(rsv);
		HashMap map = new HashMap();

		Iterator iterator = fieldList.iterator();
		while (iterator.hasNext()) {
			CewolfTimeSeriesData field = (CewolfTimeSeriesData) iterator.next();
			String title = field.getTitle();
			if (Util.isNull(title)) {
				title = field.getFieldName();
			}
			ds.addSeries(new TimeSeries(title, Millisecond.class));
			map.put(title, field);
		}

		if (!ResultSetVector.isNull(rsv)) {
			logCat.debug("rsv.size() = " + String.valueOf(rsv.size()));
			rsv.moveFirst();
			for (int i = 0; i < rsv.size(); i++) {
				Object o = rsv.getFieldAsObject(getTimeField());
				if (o != null) {
					Millisecond t = new Millisecond((Date) o);
					for (int j = 0; j < ds.getSeriesCount(); j++) {
						CewolfTimeSeriesData field = (CewolfTimeSeriesData) fieldList
								.elementAt(j);
						Double d = (Double) rsv.getFieldAsObject(field
								.getFieldName());
						ds.getSeries(j).add(new TimeSeriesDataItem(t, d));
					}
				}
				rsv.moveNext();
			}
		}
		map.put("renderer", getRendererclass());
		map.put("dataset", ds);

		AbstractChartTag rt = (AbstractChartTag) PageUtils.findRoot(this, pageContext);
		rt.addChartPostProcessor(new MyChartPostProcessor(), map);

		DatasetProducer dataProducer = new DbFormsDatasetProducer(ds);
		DataAware dw = (DataAware) findAncestorWithClass(this, DataAware.class);
		dw.setDataProductionConfig(dataProducer, new HashMap(), false);
		return SKIP_BODY;
	}

	/**
	 * @return Returns the timeField.
	 */
	public String getTimeField() {
		return timeField;
	}

	/**
	 * @param timeField
	 *            The timeField to set.
	 */
	public void setTimeField(String timeField) {
		this.timeField = timeField;
	}

	public void doFinally() {
		super.doFinally();
		timeField = null;
		fieldList.clear();
	}

	public String getRendererclass() {
		return rendererclass;
	}

	public void setRendererclass(String rendererclass) {
		this.rendererclass = rendererclass;
	}

}
