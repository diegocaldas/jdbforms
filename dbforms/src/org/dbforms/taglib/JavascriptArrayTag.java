/*
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
import javax.servlet.http.*;

/****
 *
 * <p>This tag renders a javascript array with Embeded data.  Only Value is generated. </p>
 *
 * @author Eric Beaumier
 */

public class JavascriptArrayTag extends BodyTagSupport implements DataContainer   {

	static Category logCat = Category.getInstance(JavascriptArrayTag.class.getName()); // logging category for this class

	private Vector embeddedData=null;

	private String name = null;
	
	public void setName(String name){
		this.name = name;
	}
	
  /**
  This method is a "hookup" for EmbeddedData - Tags which can assign the lines of data they loaded
  (by querying a database, or by rendering data-subelements, etc. etc.) and make the data
  available to this tag.
  [this method is defined in Interface DataContainer]
  */
  	public void setEmbeddedData(Vector embeddedData) {
		this.embeddedData = embeddedData;
	}  

	public int doStartTag() throws javax.servlet.jsp.JspException {
			
		return EVAL_BODY_TAG;

	}


  public int doEndTag() throws javax.servlet.jsp.JspException {
  		
 	
		HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();

		StringBuffer tagBuf = new StringBuffer();

		if(embeddedData==null) { // no embedded data is nested in this tag
			logCat.warn("No EmbeddedData provide for javascriptArray TagLib "+name);			
			return EVAL_PAGE;
	
		} else {
			
			tagBuf.append("\n<SCRIPT language=\"javascript\">\n");
			tagBuf.append("   var "+name+" = new Array();\n");
			
			int embeddedDataSize = embeddedData.size();
			for(int i=0; i<embeddedDataSize; i++) {

				KeyValuePair aKeyValuePair = (KeyValuePair) embeddedData.elementAt(i);
				String aKey = aKeyValuePair.getKey();
				tagBuf.append("   ").append(name).append("[").append(i).append("] = new Array('").append(aKey).append("'");

				String aValue = aKeyValuePair.getValue();

				StringTokenizer st = new StringTokenizer(aValue, ",");
				while (st.hasMoreTokens())
					tagBuf.append(",'").append(st.nextToken()).append("'");
				
				
				tagBuf.append(");\n");
			}
			tagBuf.append("</SCRIPT>\n");
		}
		
		try {
		  pageContext.getOut().write(tagBuf.toString());
		} catch(java.io.IOException ioe) {
			throw new JspException("IO Error: "+ioe.getMessage());
		}

		return EVAL_PAGE;
  }  

}