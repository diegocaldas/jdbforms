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
import java.sql.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import org.apache.log4j.Category;

import org.dbforms.util.*;

public class StaticDataItem extends TagSupport {

  static Category logCat = Category.getInstance(StaticDataItem.class.getName()); // logging category for this class

  private String key;
  private String value;


  public String getKey() {
	  return key;
  }

  public void setKey(String key) {
	  this.key = key;
  }

  public String getValue() {
	  return value;
  }

  public void setValue(String value) {
	  this.value = value;
  }


  public int doStartTag() throws JspException {

	if(getParent()!=null && getParent() instanceof StaticData)
	  ((StaticData) getParent()).getData().addElement(new KeyValuePair(key, value));
	else
	  throw new JspException("StaticDataItem element must be placed inside a StaticData element!");

	return EVAL_BODY_INCLUDE;
  }


  public void release() {
	 this.key = null;
	 this.value = null;
  }
}