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
import org.dbforms.validation.ValidatorConstants;
import org.apache.log4j.Category;

/****
 *
 * <p>this tag renders a Goto-button.
 *
 * @author Joachim Peer <j.peer@gmx.net>
 */

public class DbGotoButtonTag extends DbBaseButtonTag  {

  static Category logCat = Category.getInstance(DbGotoButtonTag.class.getName()); // logging category for this class

  private String destination;
  private String destTable;
  private String destPos;
  private String keyToDestPos;
  private String keyToKeyToDestPos;

	private static int uniqueID;

	static {
		uniqueID=1;
	}



  public void setDestination(String destination) {
	this.destination=destination;
   }   

  public String getDestination() {
	return destination;
  }  

	public void setDestTable(String destTable) {
		this.destTable = destTable;
	}

	public String getDestTable() {
		return destTable;
	}

	public void setDestPos(String destPos) {
		this.destPos = destPos;
	}

	public String getDestPos() {
		return destPos;
	}

	public void setKeyToDestPos(String keyToDestPos) {
		this.keyToDestPos = keyToDestPos;
	}

	public String getKeyToDestPos() {
		return keyToDestPos;
	}


	public void setKeyToKeyToDestPos(String keyToKeyToDestPos) {
		this.keyToKeyToDestPos = keyToKeyToDestPos;
	}

	public String getKeyToKeyToDestPos() {
		return keyToKeyToDestPos;
	}


  public int doStartTag() throws javax.servlet.jsp.JspException {

		DbGotoButtonTag.uniqueID++; // make sure that we don't mix up buttons
		
		// ValidatorConstants.JS_CANCEL_SUBMIT is the javascript variable boolean to verify
		// if we do the javascript validation before submit <FORM>
		if( parentForm.getFormValidatorName()!=null && 
			parentForm.getFormValidatorName().length() > 0 &&
			parentForm.getJavascriptValidation().equalsIgnoreCase("true") ) 
				setOnClick(  getOnClick() + ValidatorConstants.JS_CANCEL_VALIDATION+"=true;" );

		try {

			String tagName = "ac_goto_"+uniqueID;

			StringBuffer tagBuf = new StringBuffer();

			// mask destination as "fu" (FollowUp), so that we can use standard-event dispatching facilities
			// from Controller and dont have to invent something new!
			// #checkme: should we rename destination to followUp ?
			if(destination!=null) {
				tagBuf.append(getDataTag(tagName, "fu", destination));
			}
			
			if(destTable!=null) {
				tagBuf.append(getDataTag(tagName, "destTable", destTable));
			}

			if(destPos!=null) {
				tagBuf.append(getDataTag(tagName, "destPos", destPos));
			}

			if(keyToDestPos!=null) {
				tagBuf.append(getDataTag(tagName, "keyToDestPos", keyToDestPos));
			}

			if(keyToKeyToDestPos!=null) {
				tagBuf.append(getDataTag(tagName, "keyToKeyToDestPos", keyToKeyToDestPos));
			}

			tagBuf.append(getButtonBegin());
			tagBuf.append(" name=\"");
			tagBuf.append(tagName);
			tagBuf.append("\">");

		  pageContext.getOut().write(tagBuf.toString());

		} catch(java.io.IOException ioe) {
			throw new JspException("IO Error: "+ioe.getMessage());
		}

		if(choosenFlavor == FLAVOR_MODERN)
			return EVAL_BODY_TAG;
		else
		return SKIP_BODY;
  }  


  public int doEndTag() throws javax.servlet.jsp.JspException {

		if(choosenFlavor == FLAVOR_MODERN) {
			if(	parentForm.getFooterReached() && ResultSetVector.isEmptyOrNull(parentForm.getResultSetVector()) )
			return EVAL_PAGE;

			try {
				if(bodyContent != null)
					bodyContent.writeOut(bodyContent.getEnclosingWriter());

			  pageContext.getOut().write( "</button>" );
			} catch(java.io.IOException ioe) {
				throw new JspException("IO Error: "+ioe.getMessage());
			}
		}

		return EVAL_PAGE;
  }  


  public void release() {
		super.release();
		//DbGotoButtonTag.uniqueID=0; // nobody needs to know how many buttons where rendered in the lifetime of the app ;=) [release behavior is different implemented on tomcat, orion, etc so i will not play with the fire and remove this ;=)]
	}

}