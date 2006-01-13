/*
 * $Header$
 * $Revision$
 * $Date$
 *
 */
package org.dbforms.taglib.cewolf;

import javax.servlet.jsp.JspException;

import org.dbforms.taglib.AbstractDbBaseHandlerTag;
import org.dbforms.util.Util;

import java.awt.Color;

/**
 * 
 * This tag is a sub tag of DbTimeChart and describes a series for the chart.
 * 
 * @author Henner Kollmann
 * 
 */
public class CewolfTimeSeriesFieldTag extends AbstractDbBaseHandlerTag
		implements javax.servlet.jsp.tagext.TryCatchFinally {

	private String title;
	private String fieldName;
	private String color;
	private String showValueTicks;
	private String commentColor;
	private String commentFieldName;


	public void doFinally() {
		super.doFinally();
		title = null;
		fieldName = null;
		color = null;
		showValueTicks = null;
		commentColor = null;
		commentFieldName = null;
	}

	public int doStartTag() throws JspException {
		if (getParent() != null
				&& getParent() instanceof CewolfTimeSeriesDataTag) {
			CewolfTimeSeriesDataTag p = (CewolfTimeSeriesDataTag) getParent();	
			p.addField(new CewolfTimeSeriesDataTag.CewolfTimeSeriesData(this));
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

	/**
	 * @return
	 */
	public Color Color() {
		return String2Color(color);
	}

	/**
	 * @return
	 */
	public Color CommentColor() {
		return String2Color(commentColor);
	}

	public void setShowValueTicks(String showValueTicks) {
		this.showValueTicks = showValueTicks;
	}

	/**
	 * @return
	 */
	public boolean hasShowValueTicks() {
		return "true".equalsIgnoreCase(showValueTicks);
	}

	private Color String2Color(String color) {
		Color res = null;
		if (!Util.isNull(color)) {
			String tok = color.trim();
			try {
				int radix = 10;
				if (tok.charAt(0) == '#') {
					radix = 16;
					tok = tok.substring(1, tok.length());
				}
				res = new Color(Integer.parseInt(tok, radix));
			} catch (NumberFormatException exp) {
			}
		}
		return res;
	}

	/**
	 * @return
	 */
	public String getCommentFieldName() {
		return commentFieldName;
	}

	/**
	 * @param string
	 */
	public void setCommentColor(String string) {
		commentColor = string;
	}

	/**
	 * @param string
	 */
	public void setCommentFieldName(String string) {
		commentFieldName = string;
	}

	public String getColor() {
		return color;
	}

	public String getCommentColor() {
		return commentColor;
	}

	public String getShowValueTicks() {
		return showValueTicks;
	}

}
