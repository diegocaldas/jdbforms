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

package org.dbforms.event;

import javax.servlet.http.HttpServletRequest;
import java.util.Hashtable;
import java.sql.Connection;
import org.dbforms.DbFormsConfig;
import org.apache.log4j.Category;

/**
convenience class
*/

public class DbEventInterceptorSupport implements DbEventInterceptor {

  static Category logCat = Category.getInstance(DbEventInterceptorSupport.class.getName()); // logging category for this class

  public int preInsert(HttpServletRequest request, Hashtable fieldValues, DbFormsConfig config, Connection con)
  throws ValidationException {
	return GRANT_OPERATION;
  }

  public void postInsert(HttpServletRequest request, DbFormsConfig config, Connection con) {}

  public int preUpdate(HttpServletRequest request, java.util.Hashtable fieldValues, DbFormsConfig config, Connection con)
  throws ValidationException {
	return GRANT_OPERATION;
  }

  public void postUpdate(HttpServletRequest request, DbFormsConfig config, Connection con) {}

  public int preDelete(HttpServletRequest request, Hashtable fieldValues, DbFormsConfig config, Connection con)
  throws ValidationException {
	 return GRANT_OPERATION;
  }

  public void postDelete(HttpServletRequest request, DbFormsConfig config, Connection con) {}

  public int preSelect(HttpServletRequest request, DbFormsConfig config, Connection con)
  throws ValidationException {
	 return GRANT_OPERATION;
  }

  public void postSelect(HttpServletRequest request, DbFormsConfig config, Connection con) {}

}