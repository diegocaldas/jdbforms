/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * DbForms - a Rapid Application Development Framework
 * Copyright (C) 2001 Joachim Peer <j.peer@gmx.net> et al.
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.dbforms.taglib;

import java.util.*;
import java.io.*;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import org.dbforms.*;
import org.dbforms.util.*;

import org.apache.log4j.Category;

/****
 *
 * <p>the 3 examles below produce all the same result</p>
 *
 * <p><linkURL href="customer.jsp" table="customer" position="1:2:12-3:4:1992" /></p>
 *
 * <p><linkURL href="customer.jsp" table="customer" position="<%= currentKey %>" /></p>
 *
 * <p><linkURL href="customer.jsp" table="customer" />
 * <ul>  <position field="id" value="103" /><br/>
 *   <position field="cust_lang" value="2" /></ul>
 * </link>
 * </p>
 *
 * <p>result (off course without the line feeds)</p>
 *
 *
 * <pre>/servlet/control?
 * ac_goto_x=t&
 * data_ac_goto_x_fu=/customer.jsp&
 * data_ac_goto_x_destTable=17&
 * data_ac_goto_x_destPos=103~2</pre>
 *
 *
 * <p>Use it like this:</p>
 *
 *
 * <pre><a href="<linkURL href="customer.jsp" tableName="customer" position="103~2" />"> some text </a></pre>
 *
 * @author Joachim Peer <j.peer@gmx.net>
 */


public class DbLinkURLTag extends BodyTagSupport {

    static Category logCat = Category.getInstance(DbLinkURLTag.class.getName()); // logging category for this class

	private DbFormsConfig config;
	private Table table;
	private Hashtable positionFv; // fields and their values, provided by embedded DbLinkPositionItem-Elements

	// -- properties
	private String href;
	private String tableName;
	private String position;

	private DbFormTag parentForm;

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	/**
	to be called by DbLinkPositonItems
	*/
	public void addPositionPart(Field field, String value) {
	    if(positionFv==null)
	    	positionFv=new Hashtable();

	    positionFv.put(field, value);
	}

	public Table getTable() {
		return table;
	}

	public int doStartTag() throws javax.servlet.jsp.JspException {
		// determinate table
		if(this.tableName!=null) {
			this.table = config.getTableByName(tableName);
		} else if(this.parentForm!=null) { // we must try if we get info from parentForm
			this.table = parentForm.getTable();
		} else {
			throw new IllegalArgumentException("no table specified. either you define expliclty the attribute \"tableName\" or you put this tag inside a db:form/db-element!");
		}

		if(position==null) // if position was not set explicitly,
			return EVAL_BODY_TAG;  // we have to evaluate body and hopefully find DbLinkPositionItems there
		else
			return SKIP_BODY; // if position was provided we don't need to look into body
	}

	public int doBodyEndTag() throws javax.servlet.jsp.JspException {
		return SKIP_BODY;
	}

	public int doEndTag() throws javax.servlet.jsp.JspException {

		try {

			// determinate position inside table (key)
			if(this.position==null) { // not explic. def. by attribute
			  if(positionFv!=null) { // but (maybe) defined by sub-elements (DbLinkPositionItem)
				position = table.getKeyPositionString(positionFv);
			  }
			}

			// build tag
			StringBuffer tagBuf = new StringBuffer(200);
			tagBuf.append("servlet/control?ac_goto_x=t&dataac_goto_x_fu=");
			tagBuf.append(href);
			tagBuf.append("&dataac_goto_x_destTable="); // table is required. we force to define a valid table. because we do not want the developer to use this tag instead of normal <a href="">-tags to arbitrary (static) ressources, as this would slow down the application.
			tagBuf.append(table.getId());
			if(position!=null) { // position within table is not required. if no position was provided/determinated, dbForm will navigate to the first row
				tagBuf.append("&dataac_goto_x_destPos=");
				tagBuf.append(position);
			}

			pageContext.getOut().write(tagBuf.toString());

		} 	catch(java.io.IOException ioe) {
			throw new JspException("IO Error: "+ioe.getMessage());
		}	catch(Exception e) {
			throw new JspException("Error: "+e.getMessage());
		}

		return EVAL_PAGE;
	}

  public void setPageContext(final javax.servlet.jsp.PageContext pageContext)  {
    super.setPageContext(pageContext);
	this.config = (DbFormsConfig) pageContext.getServletContext().getAttribute(DbFormsConfig.CONFIG);
  }

  public void setParent(final javax.servlet.jsp.tagext.Tag parent) {
    super.setParent(parent);
    this.parentForm = (DbFormTag) findAncestorWithClass(this, DbFormTag.class); // may be null!
  }

}