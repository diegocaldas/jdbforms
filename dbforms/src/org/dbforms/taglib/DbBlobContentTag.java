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

import java.io.*;
import java.util.*;
import java.net.*;
import java.sql.*;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import org.dbforms.*;
import org.dbforms.util.*; // for FieldTYPES def

import org.apache.log4j.Category;

/**
 * #fixme docu to come
 *
 * @author Joe Peer
 */

public class DbBlobContentTag extends BodyTagSupport {

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

		if(parentForm.getFooterReached()) return EVAL_PAGE; // nothing to do when no data available..

		StringBuffer queryBuf = new StringBuffer();
		queryBuf.append("SELECT ");
		queryBuf.append(fieldName);
		queryBuf.append(" FROM ");
		queryBuf.append(parentForm.getTable().getName());
		queryBuf.append(" WHERE ");
		queryBuf.append(parentForm.getTable().getWhereClauseForPS());

		logCat.info("blobcontent query- "+queryBuf.toString());

		StringBuffer contentBuf = new StringBuffer();

		Connection con = config.getDbConnection().getConnection();
		try {

			PreparedStatement ps = con.prepareStatement(queryBuf.toString());
			parentForm.getTable().populateWhereClauseForPS(getKeyVal(), ps, 1);

			ResultSet rs = ps.executeQuery();

					if(rs.next()) {


						if(field.getType() == FieldTypes.DISKBLOB) {

							String fileName = rs.getString(1);
							if(fileName!=null) fileName = fileName.trim();
							logCat.info("READING DISKBLOB field.getDirectory()="+field.getDirectory()+" "+"fileName="+fileName);

							if(fileName==null || field.getDirectory()==null || fileName.length()==0 || field.getDirectory().length()==0) {
								return EVAL_PAGE;
							}

							File file = new File(field.getDirectory(), fileName);

							if(file.exists()) {
								logCat.info("fs- file found "+file.getName());
								FileInputStream fis = new FileInputStream(file);
								BufferedReader br = new BufferedReader(new InputStreamReader(fis));

								char[] c = new char[1024];
								int read;
								while( (read = br.read(c)) != -1) {
									contentBuf.append(c,0,read);
								}

								fis.close();

							} else
								logCat.info("fs- file not found");
						}else {
							throw new IllegalArgumentException("DbBlobContentTag is currently only for DISKBLOBS - feel free to copy code from FileServlet.java to this place to bring this limitation to an end :=)");
						}

					}else{
						logCat.info("fs- we have got no result"+queryBuf);
					}

		}	catch(SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			try {
				con.close();
			} catch(SQLException sqle2) {
				sqle2.printStackTrace();
			}
		}


		pageContext.getOut().write(contentBuf.toString());

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
