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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.*;

import org.dbforms.*;
import org.dbforms.util.*;
import org.dbforms.validation.ValidatorConstants;
import org.apache.log4j.Category;

/****
 *
 * <p>This tag renders a Delete Button</p>
 *
 *
 *
 * @author Joachim Peer <j.peer@gmx.net>
 */


public class DbDeleteButtonTag extends DbBaseButtonTag  {

  static Category logCat = Category.getInstance(DbDeleteButtonTag.class.getName()); // logging category for this class
  
  	private static int uniqueID;
  	private String confirmMessage = null;

	static {
		uniqueID=1;
	}

  private String associatedRadio;


  public void setAssociatedRadio(String associatedRadio) {
	this.associatedRadio=associatedRadio;
   }   

  public String getAssociatedRadio() {
	return associatedRadio;
  }  

  public void setConfirmMessage(String confirmMessage) {
	this.confirmMessage=confirmMessage;
   }   

  public String getConfirmMessage() {
	return confirmMessage;
  }  


  public int doStartTag() throws javax.servlet.jsp.JspException {
  	
  	DbDeleteButtonTag.uniqueID++; // make sure that we don't mix up buttons
	
	// ValidatorConstants.JS_CANCEL_SUBMIT is the javascript variable boolean to verify 
	// if we do the javascript validation before subMit <FORM>
	if( parentForm.getFormValidatorName()!=null && 
		parentForm.getFormValidatorName().length() > 0 &&
		parentForm.getJavascriptValidation().equals("true") ){
			String onclick = (getOnClick()!=null)? getOnClick():"";
			if(onclick.lastIndexOf(";")!=onclick.length()-1) onclick+=";"; // be sure javascript end with ";"
			setOnClick( onclick + ValidatorConstants.JS_CANCEL_VALIDATION+"=true;");
	}
	
	if(getConfirmMessage()!=null){
		String onclick = (getOnClick()!=null)? getOnClick():"";
		if(onclick.lastIndexOf(";")!=onclick.length()-1) onclick+=";"; // be sure javascript end with ";"

		String message = getConfirmMessage();
		if(parentForm.getCaptionResource().equals("true")){
			try{	
				HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
				message = MessageResources.getMessage(getConfirmMessage(), request.getLocale(), getConfirmMessage());
			}catch(Exception e){
				logCat.debug("confirm("+caption+") Exception : "+e.getMessage());
			}
		}
		setOnClick(onclick+"return confirm('"+message+"');");
	}
	
	if(parentForm.getFooterReached() && ResultSetVector.isEmptyOrNull(parentForm.getResultSetVector()) ) return EVAL_PAGE;

		
		try {

				// first, determinate the name of the button tag
				StringBuffer tagNameBuf = new StringBuffer("ac_delete");
				if(associatedRadio!=null) {
					tagNameBuf.append("ar");
				}
				tagNameBuf.append("_");
				tagNameBuf.append(table.getId());
		  if(associatedRadio==null) {
		  	tagNameBuf.append("_");
		  	tagNameBuf.append(parentForm.getPositionPath());
				}
				
				// PG - Render the name unique
				tagNameBuf.append("_");
				tagNameBuf.append(uniqueID);				
				
				String tagName = tagNameBuf.toString();


				// then render it and its associtated data-tags
			  StringBuffer tagBuf = new StringBuffer();

				if(associatedRadio != null) {
					tagBuf.append( getDataTag(tagName, "arname", associatedRadio) );
				}

				if(followUp != null) {
					tagBuf.append( getDataTag(tagName, "fu", followUp) );
				}
				
				if(followUpOnError != null) {
					tagBuf.append( getDataTag(tagName, "fue", followUpOnError) );
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



}