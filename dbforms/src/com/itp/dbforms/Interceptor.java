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

package com.itp.dbforms;

import java.util.*;
import javax.servlet.http.HttpServletRequest;

import com.itp.dbforms.util.*;
import org.apache.log4j.Category;

/****
 * <p>
 * This class holds XML config data defining interceptors for tables.
 * compare com.itp.dbforms.event.DbEventInterceptor
 * </p>
 *
 * @auther Joachim Peer <joepeer@wap-force.net>
 */

public class Interceptor {

  static Category logCat = Category.getInstance(Interceptor.class.getName()); // logging category for this class

  private String className;


  public void setClassName(String className) {
	  this.className = className;
  }

  public String getClassName() {
	  return className;
  }


}