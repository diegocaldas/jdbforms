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

package interceptors;

import org.dbforms.config.DbEventInterceptorData;
import org.dbforms.config.MultipleValidationException;
import org.dbforms.config.ValidationException;

import org.dbforms.event.DbEventInterceptorSupport;



/**
 * DOCUMENT ME!
 *
 * @author hkk To change this generated comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class InterceptorTest extends DbEventInterceptorSupport {
   /** DOCUMENT ME! */
   public static boolean preInsertCalled  = false;

   /** DOCUMENT ME! */
   public static boolean preSelectCalled  = false;

   /** DOCUMENT ME! */
   public static boolean preUpdateCalled  = false;

   /** DOCUMENT ME! */
   public static boolean preDeleteCalled  = false;

   /** DOCUMENT ME! */
   public static boolean preAddRowCalled  = false;

   /** DOCUMENT ME! */
   public static boolean postUpdateCalled = false;

   /** DOCUMENT ME! */
   public static boolean postSelectCalled = false;

   /** DOCUMENT ME! */
   public static boolean postInsertCalled = false;

   /** DOCUMENT ME! */
   public static boolean postDeleteCalled = false;

   /** DOCUMENT ME! */
   public static boolean postAddRowCalled = false;

   /**
    * DOCUMENT ME!
    *
    * @param data DOCUMENT ME!
    */
   public void postAddRow(DbEventInterceptorData data) {
      postAddRowCalled = true;
   }


   /**
    * DOCUMENT ME!
    *
    * @param data DOCUMENT ME!
    */
   public void postDelete(DbEventInterceptorData data) {
      postDeleteCalled = true;
   }


   /**
    * DOCUMENT ME!
    *
    * @param data DOCUMENT ME!
    */
   public void postInsert(DbEventInterceptorData data) {
      postInsertCalled = true;
   }


   /**
    * DOCUMENT ME!
    *
    * @param data DOCUMENT ME!
    */
   public void postSelect(DbEventInterceptorData data) {
      postSelectCalled = true;
   }


   /**
    * DOCUMENT ME!
    *
    * @param data DOCUMENT ME!
    */
   public void postUpdate(DbEventInterceptorData data) {
      postUpdateCalled = true;
   }


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
                 throws ValidationException, MultipleValidationException {
      preAddRowCalled = true;

      return GRANT_OPERATION;
   }


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
                 throws ValidationException, MultipleValidationException {
      preDeleteCalled = true;

      return GRANT_OPERATION;
   }


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
                 throws ValidationException, MultipleValidationException {
      preInsertCalled = true;

      return GRANT_OPERATION;
   }


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
                 throws ValidationException, MultipleValidationException {
      preSelectCalled = true;

      return GRANT_OPERATION;
   }


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
                 throws ValidationException, MultipleValidationException {
      preUpdateCalled = true;

      return GRANT_OPERATION;
   }
}
