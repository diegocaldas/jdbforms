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

import java.sql.Connection;

import java.util.Map;

/**
 * <p>
 * This interface intercepts Database Operations DbForms is about to perform
 * </p>
 *
 * <p>
 * As the names indicate
 * </p>
 * - the preXxx() methods get called before the respective database operation
 * is performed, - the postXxx() methods get called after the operation was
 * finished.
 */
import javax.servlet.http.HttpServletRequest;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public interface DbEventInterceptor {
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
    * The constant defined for ignoring an operation after processing
    * interceptors.
    */
   public static final int IGNORE_OPERATION = 2;

   /**
    * DOCUMENT ME!
    *
    * @param params DOCUMENT ME!
    */
   public void setParams(Map params);


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public Map getParams();


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    */
   void postDelete(HttpServletRequest request,
                   DbFormsConfig      config,
                   Connection         con);


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    */
   void postInsert(HttpServletRequest request,
                   DbFormsConfig      config,
                   Connection         con);


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    */
   void postSelect(HttpServletRequest request,
                   DbFormsConfig      config,
                   Connection         con);


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    */
   void postUpdate(HttpServletRequest request,
                   DbFormsConfig      config,
                   Connection         con);


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param table DOCUMENT ME!
    * @param fieldValues DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws ValidationException DOCUMENT ME!
    */
   int preDelete(HttpServletRequest request,
                 Table              table,
                 FieldValues        fieldValues,
                 DbFormsConfig      config,
                 Connection         con)
          throws ValidationException, MultipleValidationException;


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param table DOCUMENT ME!
    * @param fieldValues DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws ValidationException DOCUMENT ME!
    */
   int preInsert(HttpServletRequest request,
                 Table              table,
                 FieldValues        fieldValues,
                 DbFormsConfig      config,
                 Connection         con)
          throws ValidationException, MultipleValidationException;


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
   int preSelect(HttpServletRequest request,
                 DbFormsConfig      config,
                 Connection         con)
          throws ValidationException, MultipleValidationException;


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param table DOCUMENT ME!
    * @param fieldValues DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws ValidationException DOCUMENT ME!
    */
   int preUpdate(HttpServletRequest request,
                 Table              table,
                 FieldValues        fieldValues,
                 DbFormsConfig      config,
                 Connection         con)
          throws ValidationException, MultipleValidationException;
}
