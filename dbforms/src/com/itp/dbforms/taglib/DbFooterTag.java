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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.itp.dbforms.taglib;

import java.util.*;
import java.sql.*;
import java.io.*;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.itp.dbforms.*;

import org.apache.log4j.Category;

/****
 *
 * this tag renders a Footer-tag. it is supposed to be nested within a DbFormTag.
 * because this tag is nested within a DbFormTag it is invoked every time the parent dbFormTag gets
 * evaluated, but it gets only rendered at the end of the last evalation-loop.
 *
 * @author Joachim Peer <joepeer@excite.com>
 */

public class DbFooterTag extends BodyTagSupport {

  static Category logCat = Category.getInstance(DbFooterTag.class.getName()); // logging category for this class

  public int doStartTag() {

    //DbFormTag parent = (DbFormTag) getParent(); // parent Tag in which this tag is embedded in
    DbFormTag myParent = (DbFormTag) findAncestorWithClass(this, DbFormTag.class);
	  int pCount = myParent.getCount();							// pCount==-1 => endless form, else max nr. of eval.loops is pCount
	  int pCurrent = myParent.getCurrentCount();		// how many times renderd

		logCat.info("we are talking about="+myParent.getTableName());
		logCat.info("pcount="+pCount);
		logCat.info("pcurrent="+pCurrent);

	  if(	(pCount!=-1 && pCurrent == pCount) ||   							// if the max-count is reached
	  		(myParent.getResultSetVector() == null) ||
	  	 	(pCurrent == myParent.getResultSetVector().size()) ||		// or if end of resultsetvector is reached #checkme: can we dispose this condition?
	  	 	(myParent.getResultSetVector().size()==0) ||						// or if there is no resultSet data
	  	 	(myParent.getResultSetVector().size()==myParent.getResultSetVector().getPointer())  // or if end of resultsetvector is reached
	  	) {

				logCat.info("setting footerreached to true");

     		myParent.setFooterReached(true); // tell parent form that there are no more loops do go and the only thing remaining to be rendered is this footerTag and its subelements
     		return EVAL_BODY_TAG;
		 	}
      else
        return SKIP_BODY;
  }


  public int doAfterBody() throws JspException {
    return SKIP_BODY; // a footer gets onle 1 time rendered
  }


  public int doEndTag() throws JspException {

		//DbFormTag myParent = (DbFormTag) getParent(); // parent Tag in which this tag is embedded in
		DbFormTag myParent = (DbFormTag) findAncestorWithClass(this, DbFormTag.class);

		JspWriter out = pageContext.getOut();

		// field values that have not been rendered by html tags but that is determinated by field
		// mapping between main- and subform are rendered now:
		try {
			if(myParent.isSubForm()) {
				myParent.appendToChildElementOutput(myParent.produceLinkedTags()); // print hidden-fields for missing insert-fields we can determinated
			}

		  if(bodyContent != null)
		    bodyContent.writeOut(bodyContent.getEnclosingWriter());
  	  }
		catch(java.io.IOException e) {
			throw new JspException("IO Error: " + e.getMessage());
		}

		return EVAL_PAGE;
  }

}