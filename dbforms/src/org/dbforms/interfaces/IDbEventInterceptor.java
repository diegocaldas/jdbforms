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

package org.dbforms.interfaces;

import java.util.Map;

import org.dbforms.config.MultipleValidationException;
import org.dbforms.config.ValidationException;



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
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public interface IDbEventInterceptor {
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
   public static final int PRE_ADDROW = 8;

   /** DOCUMENT ME! */
   public static final int POST_ADDROW = 9;

   /** The constant defined for granting the operation */
   public static final int GRANT_OPERATION = 0;

   /** The constant defined for not granting the operation */
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
   public void setParameterMap(Map params);


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public Map getParameterMap();


   /**
    * DOCUMENT ME!
    *
    * @param data DOCUMENT ME!
    */
   public void postAddRow(DbEventInterceptorData data);


   /**
    * DOCUMENT ME!
    *
    * @param data DOCUMENT ME!
    */
   public void postDelete(DbEventInterceptorData data);


   /**
    * DOCUMENT ME!
    *
    * @param data DOCUMENT ME!
    */
   public void postInsert(DbEventInterceptorData data);


   /**
    * DOCUMENT ME!
    *
    * @param data DOCUMENT ME!
    */
   public void postSelect(DbEventInterceptorData data);


   /**
    * DOCUMENT ME!
    *
    * @param data DOCUMENT ME!
    */
   public void postUpdate(DbEventInterceptorData data);


   /**
    * DOCUMENT ME!
    *
    * @param data DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws ValidationException DOCUMENT ME!
    * @throws MultipleValidationException DOCUMENT ME!
    */
   public int preAddRow(DbEventInterceptorData data)
                 throws ValidationException, MultipleValidationException;


   /**
    * DOCUMENT ME!
    *
    * @param data DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws ValidationException DOCUMENT ME!
    * @throws MultipleValidationException DOCUMENT ME!
    */
   public int preDelete(DbEventInterceptorData data)
                 throws ValidationException, MultipleValidationException;


   /**
    * DOCUMENT ME!
    *
    * @param data DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws ValidationException DOCUMENT ME!
    * @throws MultipleValidationException DOCUMENT ME!
    */
   public int preInsert(DbEventInterceptorData data)
                 throws ValidationException, MultipleValidationException;


   /**
    * DOCUMENT ME!
    *
    * @param data DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws ValidationException DOCUMENT ME!
    * @throws MultipleValidationException DOCUMENT ME!
    */
   public int preSelect(DbEventInterceptorData data)
                 throws ValidationException, MultipleValidationException;


   /**
    * DOCUMENT ME!
    *
    * @param data DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws ValidationException DOCUMENT ME!
    * @throws MultipleValidationException DOCUMENT ME!
    */
   public int preUpdate(DbEventInterceptorData data)
                 throws ValidationException, MultipleValidationException;
}
