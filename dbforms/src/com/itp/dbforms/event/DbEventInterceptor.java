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

package com.itp.dbforms.event;


/****
 * <p>
 * This interface intercepts Database Operations DbForms is about to perform
 * </p>
 *
 *    <p>As the names indicate </p>
 *    <li>the preXxx() methods get called before the respective database operation is performed,
 *    <li>the postXxx() methods get called after the operation was finished.
 *
 * @author Joe Peer <joepeer@excite.com>
 */


import javax.servlet.http.HttpServletRequest;
import java.util.Hashtable;
import java.sql.Connection;
import com.itp.dbforms.DbFormsConfig;

public interface DbEventInterceptor {

  public static final int PRE_INSERT = 0;
  public static final int POST_INSERT = 1;
  public static final int PRE_UPDATE = 2;
  public static final int POST_UPDATE = 3;
  public static final int PRE_DELETE = 4;
  public static final int POST_DELETE = 5;
  public static final int PRE_SELECT = 6;
  public static final int POST_SELECT = 7;

  public static final int GRANT_OPERATION = 0;
  public static final int DENY_OPERATION = 1;

  public int preInsert(HttpServletRequest request, Hashtable fieldValues, DbFormsConfig config, Connection con)
  throws ValidationException;

  public void postInsert(HttpServletRequest request, DbFormsConfig config, Connection con);

  public int preUpdate(HttpServletRequest request, java.util.Hashtable fieldValues, DbFormsConfig config, Connection con)
  throws ValidationException;;

  public void postUpdate(HttpServletRequest request, DbFormsConfig config, Connection con);

  public int preDelete(HttpServletRequest request, Hashtable fieldValues,DbFormsConfig config, Connection con)
  throws ValidationException;;

  public void postDelete(HttpServletRequest request, DbFormsConfig config, Connection con);

  public int preSelect(HttpServletRequest request, DbFormsConfig config, Connection con)
  throws ValidationException;;

  public void postSelect(HttpServletRequest request, DbFormsConfig config, Connection con);

}