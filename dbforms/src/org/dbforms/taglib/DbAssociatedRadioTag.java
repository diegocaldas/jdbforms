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

import org.apache.log4j.Category;

/****
 *
 * <p>This tag enables the end-user to define a row by selecting the radio-button rendered by this tag</p>
 *
 * <summary><p>This tag enables the end-user to define a row by selecting the radio-button
 * rendered by this tag</p></summary>
 * <tagclass>org.dbforms.taglib.DbAssociatedRadioTag</tagclass>
 * <bodycontent>empty</bodycontent>
 *
 * <p>example: imagine a table customer, which should be listed. the user should
 * be able to delete a customer.</p>
 *
 * in that case the application developer has to alternatives:
 * <li>to put a "deleteButton" into the body -> this button gets rendered for every row<br>
 *   if the user klicks the button the associated data row gets deleted.
 *   the disadvantage of this method is that multiple buttons must be rendered, which takes away
 *   lots of space  and makes layouting more difficult</li>
 * <li>to put an "associatedRadio" into the body and the "deleteButton" on the footer (or header)<br>
 *   the radio element gets rendered for every row, the deleteButton just once. if the user wants
 *   to delete a row, he/she has to select the radioButton (to mark the row he/she wants to be deleted)
 *   and then to press the deleteButton.
 * </li>
 * <p>the more buttons you have the better this method is!!</p>
 * <p>
 * nota bene: you have to tell the delete (or insert, update...) - button that there is an associated
 * radio button that marks the row the action should be applied to, by defining the "associatedRadio"
 * attribute of that respective button
 * <p>
 *
 *
 * @author Joachim Peer <j.peer@gmx.net>
 */

public class DbAssociatedRadioTag extends TagSupport  {

  static Category logCat = Category.getInstance(DbAssociatedRadioTag.class.getName()); // logging category for this class

	private DbFormTag parentForm;
  private String name;

  public void setName(String name) {
	this.name=name;
  }  

  public String getName() {
	return name;
  }  


  public int doEndTag() throws javax.servlet.jsp.JspException {

		try {

	  StringBuffer tagBuf = new StringBuffer("<input type=\"radio\" name=\"");
	  tagBuf.append(name);
	  tagBuf.append("\" value=\"");
	  tagBuf.append(parentForm.getTable().getId());
	  tagBuf.append("_");
			tagBuf.append(parentForm.getPositionPath());
	  tagBuf.append("\">");

		  pageContext.getOut().write(tagBuf.toString());
		} catch(java.io.IOException ioe) {
	  throw new JspException("IO Error: "+ioe.getMessage());
		}

		return EVAL_PAGE;
  }  


  public void setParent(final javax.servlet.jsp.tagext.Tag parent) {
	super.setParent(parent);
	parentForm = (DbFormTag) findAncestorWithClass(this, DbFormTag.class);
  }  

}