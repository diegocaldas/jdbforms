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

import java.util.*;
import java.sql.*;
import java.io.*;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import org.dbforms.*;
import org.dbforms.util.*;

import org.apache.log4j.Category;

/****
 *
 * <p>this tag renders a Body-tag. it is supposed to be nested within a DbFormTag.
 * because this tag is nested within a DbFormTag it is invoked every time the parent dbFormTag gets
 * evaluated, AND it gets rendered in every evalation-loop (if there exists data to be rendered)</p>
 *
 * @author Joachim Peer <j.peer@gmx.net>
 */


public class DbBodyTag extends BodyTagSupport {

    static Category logCat = Category.getInstance(DbBodyTag.class.getName()); // logging category for this class

	private String allowNew = "true"; // by default this is "true" - if so, the body is rendered at least 1 time, even if there are no data rows in the table. this enables the user to insert a new data row. - to disable this feature, allowNew has to be set to "false"

	public void setAllowNew(String allowNew) {
		this.allowNew = allowNew;
	}

	public String getAllowNew() {
		return allowNew;
	}

  public int doStartTag() {

	  //DbFormTag myParent = (DbFormTag) getParent(); // parent Tag in which this tag is embedded in
	  // between this form and its parent lies a DbHeader/Body/Footer-Tag and maybe other tags (styling, logic, etc.)
	  DbFormTag myParent = (DbFormTag) findAncestorWithClass(this, DbFormTag.class);

		// the body may be rendered under the following circumstances:
		// - resultSetVector > 0 => render a row
		// - resultSetVector == 0 && allowNew==true => insert a new row

	  if(myParent.getResultSetVector()==null || myParent.getResultSetVector().size()==0) {
			myParent.setFooterReached(true);
			if("false".equals(allowNew)) return SKIP_BODY;
		}

		myParent.updatePositionPath();

		// to enable access from jsp we provide a variable
		// #fixme: we need a more convenient data access structure
		//
		// #fixme: this is a CRAZY WEIRED ODD SQUARE WORKAROUND
		//

		ResultSetVector rsv = myParent.getResultSetVector();

		if(!ResultSetVector.isEmptyOrNull(rsv)) {
			if(rsv.getPointer() < rsv.size()-1 ) {
				rsv.increasePointer(); // teleport us to future...
				pageContext.setAttribute("currentRow_"+myParent.getTableName(), rsv.getCurrentRowAsHashtable());
				pageContext.setAttribute("position_"+myParent.getTableName(), myParent.getTable().getPositionString(rsv) );
				rsv.declinePointer(); // ...and back to present ;=)
			}
		}
	 	return EVAL_BODY_TAG;
  }


  public int doAfterBody() throws JspException {

    //DbFormTag myParent = (DbFormTag) getParent(); // parent Tag in which this tag is embedded in
    DbFormTag myParent = (DbFormTag) findAncestorWithClass(this, DbFormTag.class);

		JspWriter out = pageContext.getOut();

    //try {
			// each rendering loop represents one row of data.
			// for every row we need to print some data needed by the controller servlet in order to
			// correctly dispatching and eventually modifying our data.
			//
			// now the key of the current dataset is printed out (always)
			// this key will be used by actions such as delete or update.
			String curKeyString = myParent.getTable().getKeyPositionString(myParent.getResultSetVector());

			myParent.appendToChildElementOutput("<input type=\"hidden\" name=\"k_"+myParent.getTable().getId()+"_"+myParent.getPositionPath()+"\" value=\""+curKeyString+"\">");
		//} catch(IOException ioe) {
		//	throw new JspException(ioe.toString());
		//}

		myParent.increaseCurrentCount();


		if(!ResultSetVector.isEmptyOrNull(myParent.getResultSetVector()))
			myParent.getResultSetVector().increasePointer();

		return SKIP_BODY;
  }

  public int doEndTag() throws JspException {

    try {
	  	if(bodyContent != null) {
				bodyContent.writeOut(bodyContent.getEnclosingWriter());
				bodyContent.clearBody(); // workaround for duplicate rows in JRun 3.1
	    }
	}
	catch(java.io.IOException e) {
		throw new JspException("IO Error: " + e.getMessage());
	}
	return EVAL_PAGE;
  }

}