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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 */
package org.dbforms.config;

/****
 * <p>
 * This interface intercepts Database Operations DbForms is about to perform
 * </p>
 *
 *    <p>As the names indicate </p>
 *    <li>the preXxx() methods get called before the respective database operation is performed,
 *    <li>the postXxx() methods get called after the operation was finished.
 *
 * @author Joe Peer <j.peer@gmx.net>
 */
import javax.servlet.http.HttpServletRequest;
import java.util.Hashtable;
import java.sql.Connection;




/**
 * DOCUMENT ME!
 *
 * @version $Revision$
 * @author $author$
 */
public interface DbEventInterceptor
{
   /** DOCUMENT ME! */
   public static final int PRE_INSERT = 0;

   /** DOCUMENT ME! */
   public static final int POST_INSERT = 1;

   /** DOCUMENT ME! */
   public static final int PRE_UPDATE = 2;

   /** DOCUMENT ME! */
   public static final int POST_UPDATE = 3;

   /** DOCUMENT ME! */
   public static final int PRE_DELETE = 4;

   /** DOCUMENT ME! */
   public static final int POST_DELETE = 5;

   /** DOCUMENT ME! */
   public static final int PRE_SELECT = 6;

   /** DOCUMENT ME! */
   public static final int POST_SELECT = 7;

   /** DOCUMENT ME! */
   public static final int GRANT_OPERATION = 0;

   /** DOCUMENT ME! */
   public static final int DENY_OPERATION = 1;

   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param fieldValues DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws ValidationException DOCUMENT ME!
    */
   int preInsert(HttpServletRequest request, Hashtable fieldValues,
      DbFormsConfig config, Connection con) throws ValidationException;


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    */
   void postInsert(HttpServletRequest request, DbFormsConfig config,
      Connection con);


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param fieldValues DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws ValidationException DOCUMENT ME!
    */
   int preUpdate(HttpServletRequest request,
      java.util.Hashtable fieldValues, DbFormsConfig config, Connection con)
      throws ValidationException;
   ;

   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    */
   void postUpdate(HttpServletRequest request, DbFormsConfig config,
      Connection con);


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param fieldValues DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws ValidationException DOCUMENT ME!
    */
   int preDelete(HttpServletRequest request, Hashtable fieldValues,
      DbFormsConfig config, Connection con) throws ValidationException;
   ;

   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    */
   void postDelete(HttpServletRequest request, DbFormsConfig config,
      Connection con);


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws ValidationException DOCUMENT ME!
    */
   int preSelect(HttpServletRequest request, DbFormsConfig config,
      Connection con) throws ValidationException;
   ;

   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    */
   void postSelect(HttpServletRequest request, DbFormsConfig config,
      Connection con);
}
