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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 */

package org.dbforms.taglib;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import org.dbforms.*;
import org.apache.log4j.Category;

/**
 * #fixme docu to come
 *
 * @author Joe Peer
 */

public class DbBlobURLTag extends BodyTagSupport {

	static Category logCat = Category.getInstance(DbBlobURLTag.class.getName()); // logging category for this class

	// ----------------------------------------------------- Instance Variables


// DbForms specific

	protected DbFormsConfig config;
  	protected String fieldName;
  	protected Field field;

		protected DbFormTag parentForm;

	  public void setFieldName(String fieldName) {
	    this.fieldName=fieldName;
			this.field = parentForm.getTable().getFieldByName(fieldName);

			//if(parentForm.isSubForm()) {
				// tell parent that _this_ class will generate the html tag, not DbBodyTag!
			//	parentForm.strikeOut(this.field);
			//}
	  }

	  public String getFieldName() {
	    return fieldName;
	  }


	// --------------------------------------------------------- Public Methods


	// DbForms specific

	  public void setPageContext(final javax.servlet.jsp.PageContext pageContext)  {
	    super.setPageContext(pageContext);
	    config = (DbFormsConfig) pageContext.getServletContext().getAttribute(DbFormsConfig.CONFIG);
	  }

	  public void setParent(final javax.servlet.jsp.tagext.Tag parent) {
	    super.setParent(parent);
	    //parentForm = (DbFormTag) getParent().getParent(); // between this form and its parent lies a DbHeader/Body/Footer-Tag!
	    parentForm = (DbFormTag) findAncestorWithClass(this, DbFormTag.class);
	  }

	/**
	 * Release any acquired resources.
	 */
	public void release() {
			super.release();
	}

  public int doEndTag() throws javax.servlet.jsp.JspException {

		try {



  	  StringBuffer tagBuf = new StringBuffer(((HttpServletRequest) pageContext.getRequest()).getContextPath());
  	  tagBuf.append("/servlet/file?tf=");
	  tagBuf.append(getTableFieldCode());
			tagBuf.append("&keyval=");
			tagBuf.append(getKeyVal());
		  pageContext.getOut().write(tagBuf.toString());
		} catch(java.io.IOException ioe) {
			throw new JspException("IO Error: "+ioe.getMessage());
		}

		return EVAL_PAGE;
  }  


	// ------------------------------------------------------ Protected Methods



// DbForms specific




  /**
  generates the decoded name .
  */
	protected String getTableFieldCode() {
		StringBuffer buf = new StringBuffer();
	buf.append(parentForm.getTable().getId());
	buf.append("_");
	buf.append(field.getId());
		return buf.toString();
	}

	protected String getKeyVal() {
		return parentForm.getTable().getKeyPositionString(parentForm.getResultSetVector());
	}


}