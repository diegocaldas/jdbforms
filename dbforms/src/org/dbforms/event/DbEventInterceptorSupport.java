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

package org.dbforms.event;

import org.dbforms.config.DbEventInterceptor;
import org.dbforms.config.DbEventInterceptorData;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.Field;
import org.dbforms.config.FieldValue;
import org.dbforms.config.FieldValues;
import org.dbforms.config.MultipleValidationException;
import org.dbforms.config.Table;
import org.dbforms.config.ValidationException;

import java.sql.Connection;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;



/**
 * convenience class
 *
 * @author Joe Peer
 */
public class DbEventInterceptorSupport implements DbEventInterceptor {
   private Map params;

   /* (non-Javadoc)
    * @see org.dbforms.config.DbEventInterceptor#setParameterMap(java.util.Map)
    */
   public void setParameterMap(Map params) {
      this.params = params;
   }


   /* (non-Javadoc)
    * @see org.dbforms.config.DbEventInterceptor#getParameterMap()
    */
   public Map getParameterMap() {
      return params;
   }


   /* (non-Javadoc)
    * @see org.dbforms.config.DbEventInterceptor#postAddRow(org.dbforms.config.DbEventInterceptorData)
    */
   public void postAddRow(DbEventInterceptorData data) {
   }


   /* (non-Javadoc)
    * @see org.dbforms.config.DbEventInterceptor#postDelete(org.dbforms.config.Table, org.dbforms.config.DbEventInterceptorData)
    */
   public void postDelete(DbEventInterceptorData data) {
      postDelete(data.getRequest(), data.getConfig(), data.getConnection());
   }


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    *
    * @deprecated
    */
   public void postDelete(HttpServletRequest request,
                          DbFormsConfig      config,
                          Connection         con) {
   }


   /* (non-Javadoc)
    * @see org.dbforms.config.DbEventInterceptor#postInsert(org.dbforms.config.Table, org.dbforms.config.DbEventInterceptorData)
    */
   public void postInsert(DbEventInterceptorData data) {
      postInsert(data.getRequest(), data.getConfig(), data.getConnection());
   }


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    *
    * @deprecated
    */
   public void postInsert(HttpServletRequest request,
                          DbFormsConfig      config,
                          Connection         con) {
   }


   /* (non-Javadoc)
    * @see org.dbforms.config.DbEventInterceptor#postSelect(org.dbforms.config.Table, org.dbforms.config.DbEventInterceptorData)
    */
   public void postSelect(DbEventInterceptorData data) {
      postSelect(data.getRequest(), data.getConfig(), data.getConnection());
   }


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    *
    * @deprecated
    */
   public void postSelect(HttpServletRequest request,
                          DbFormsConfig      config,
                          Connection         con) {
   }


   /* (non-Javadoc)
    * @see org.dbforms.config.DbEventInterceptor#postUpdate(org.dbforms.config.Table, org.dbforms.config.DbEventInterceptorData)
    */
   public void postUpdate(DbEventInterceptorData data) {
      postUpdate(data.getRequest(), data.getConfig(), data.getConnection());
   }


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    *
    * @deprecated
    */
   public void postUpdate(HttpServletRequest request,
                          DbFormsConfig      config,
                          Connection         con) {
   }


   /* (non-Javadoc)
    * @see org.dbforms.config.DbEventInterceptor#preAddRow(org.dbforms.config.DbEventInterceptorData)
    */
   public int preAddRow(DbEventInterceptorData data)
                 throws ValidationException, MultipleValidationException {
      return GRANT_OPERATION;
   }


   /* (non-Javadoc)
    * @see org.dbforms.config.DbEventInterceptor#preDelete(org.dbforms.config.Table, org.dbforms.config.DbEventInterceptorData)
    */
   public int preDelete(DbEventInterceptorData data)
                 throws ValidationException, MultipleValidationException {
      return preDelete(data.getRequest(), data.getTable(),
                       (FieldValues) data.getAttribute(DbEventInterceptorData.FIELDVALUES),
                       data.getConfig(), data.getConnection());
   }


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
    *
    * @deprecated
    */
   public int preDelete(HttpServletRequest request,
                        Table              table,
                        FieldValues        fieldValues,
                        DbFormsConfig      config,
                        Connection         con) throws ValidationException {
      return GRANT_OPERATION;
   }


   /* (non-Javadoc)
    * @see org.dbforms.config.DbEventInterceptor#preInsert(org.dbforms.config.Table, org.dbforms.config.DbEventInterceptorData)
    */
   public int preInsert(DbEventInterceptorData data)
                 throws ValidationException, MultipleValidationException {
      return preInsert(data.getRequest(), data.getTable(),
                       (FieldValues) data.getAttribute(DbEventInterceptorData.FIELDVALUES),
                       data.getConfig(), data.getConnection());
   }


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
    *
    * @deprecated
    */
   public int preInsert(HttpServletRequest request,
                        Table              table,
                        FieldValues        fieldValues,
                        DbFormsConfig      config,
                        Connection         con) throws ValidationException {
      return GRANT_OPERATION;
   }


   /* (non-Javadoc)
    * @see org.dbforms.config.DbEventInterceptor#preSelect(org.dbforms.config.Table, org.dbforms.config.DbEventInterceptorData)
    */
   public int preSelect(DbEventInterceptorData data)
                 throws ValidationException, MultipleValidationException {
      return preSelect(data.getRequest(), data.getConfig(), data.getConnection());
   }


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
    *
    * @deprecated
    */
   public int preSelect(HttpServletRequest request,
                        DbFormsConfig      config,
                        Connection         con) throws ValidationException {
      return GRANT_OPERATION;
   }


   /* (non-Javadoc)
    * @see org.dbforms.config.DbEventInterceptor#preUpdate(org.dbforms.config.Table, org.dbforms.config.DbEventInterceptorData)
    */
   public int preUpdate(DbEventInterceptorData data)
                 throws ValidationException, MultipleValidationException {
      return preUpdate(data.getRequest(), data.getTable(),
                       (FieldValues) data.getAttribute(DbEventInterceptorData.FIELDVALUES),
                       data.getConfig(), data.getConnection());
   }


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
    *
    * @deprecated
    */
   public int preUpdate(HttpServletRequest request,
                        Table              table,
                        FieldValues        fieldValues,
                        DbFormsConfig      config,
                        Connection         con) throws ValidationException {
      return GRANT_OPERATION;
   }


   /**
    * adds or replace a value in the fieldValues
    *
    * @param table wich should be used to lookup for the fieldName
    * @param fieldValues to add/replace value to
    * @param fieldName to add/replace value
    * @param value to add/replace
    */
   protected void setValue(Table       table,
                           FieldValues fieldValues,
                           String      fieldName,
                           String      value) {
      FieldValue fv = fieldValues.get(fieldName);
      Field      f = table.getFieldByName(fieldName);

      if (f != null) {
         if (fv == null) {
            fv = new FieldValue(f, value);
            fieldValues.put(fv);
         } else {
            fv.setFieldValue(value);
         }
      }
   }
}
