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


import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

import org.dbforms.*;
import org.dbforms.util.*;
import org.dbforms.taglib.*;
import org.dbforms.taglib.DbSortTag;
import org.dbforms.taglib.TagSupportWithScriptHandler;

import org.apache.log4j.Category;

/*
 * <p>renders a input field for searching with special default search modes.</p>
 * <p>example:</p>
        <input type="hidden" name="searchalgo_0_1" value="weakEnd"/>
        <input type="hidden" name="searchmode_0_1" value="AND"/>
        <input type="input" name="search_0_1"/>
 *
 *  searchalgo and searchmode are set by parameter.
 *
 * @author Henner Kollmann
 */
public class DbSearchTag extends TagSupportWithScriptHandler  {

  static Category logCat = Category.getInstance(DbSortTag.class.getName()); // logging category for this class

  protected DbFormTag parentForm;

  private String fieldName;
  private String searchAlgo = "sharp";
  private String searchMode = "and"; 
  private String defaultValue = null;

  public void setFieldName(String fieldName) {
	this.fieldName=fieldName;
   }   

  public String getFieldName() {
	return fieldName;
  }  

  public void setSearchAlgo(String searchAlgo) {
	this.searchAlgo=searchAlgo;
   }   

  public String getSearchAlgo() {
	return searchAlgo;
  }  

  public void setSearchMode(String searchMode) {
	this.searchMode=searchMode;
   }   

  public String getSearchMode() {
	return searchMode;
  }  

  public void setDefault(String value) {
	this.defaultValue = value;
   }   

  public String getDefault() {
	return defaultValue;
  }  


  protected String RenderHiddenFields(int tableId, int fieldId) {
			StringBuffer tagBuf = new StringBuffer();
         StringBuffer paramNameBufA = new StringBuffer();
			paramNameBufA.append("searchalgo_");
			paramNameBufA.append(tableId);
			paramNameBufA.append("_");
			paramNameBufA.append(fieldId);
			tagBuf.append("<input type=\"hidden\" name=\"");
			tagBuf.append(paramNameBufA.toString());
			tagBuf.append("\" value=\"");
			tagBuf.append(getSearchAlgo());
			tagBuf.append("\">\n");
			
			StringBuffer paramNameBufB = new StringBuffer();
			paramNameBufB.append("searchmode_");
			paramNameBufB.append(tableId);
			paramNameBufB.append("_");
			paramNameBufB.append(fieldId);
			tagBuf.append("<input type=\"hidden\" name=\"");
			tagBuf.append(paramNameBufB.toString());
			tagBuf.append("\" value=\"");
			tagBuf.append(getSearchMode());
			tagBuf.append("\">\n");
			return tagBuf.toString();
  }
  
  public int doEndTag() throws javax.servlet.jsp.JspException {
     try {

			int tableId = parentForm.getTable().getId();
         Field field = parentForm.getTable().getFieldByName(fieldName);
			int fieldId = field.getId();

/*
                   <input type="hidden" name="searchalgo_0_1" value="weakEnd"/>
                   <input type="hidden" name="searchmode_0_1" value="AND"/>
                   <input type="input" name="search_0_1"/>
*/
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			StringBuffer tagBuf = new StringBuffer();

			
			StringBuffer paramNameBuf = new StringBuffer();
			paramNameBuf.append("search_");
			paramNameBuf.append(tableId);
			paramNameBuf.append("_");
			paramNameBuf.append(fieldId);
			String oldValue = ParseUtil.getParameter(request, paramNameBuf.toString());
			tagBuf.append("<input type=\"input\" name=\"");
			tagBuf.append(paramNameBuf.toString());
			tagBuf.append("\" value=\"");
			if (oldValue != null) { 
            tagBuf.append(oldValue);
			} else if (defaultValue != null) {
            tagBuf.append(defaultValue);
			}   
			tagBuf.append("\"");
     		tagBuf.append(prepareStyles());
         tagBuf.append(prepareEventHandlers());
         tagBuf.append(">\n");

         pageContext.getOut().write(RenderHiddenFields(tableId, fieldId));			
         pageContext.getOut().write(tagBuf.toString());

		} catch(java.io.IOException ioe) {
	  		throw new JspException("IO Error: "+ioe.getMessage());
		}
		return EVAL_PAGE;
  }  

  public void setParent(final javax.servlet.jsp.tagext.Tag parent) {
	super.setParent(parent);
	this.parentForm = (DbFormTag) findAncestorWithClass(this, DbFormTag.class);
  }  

}