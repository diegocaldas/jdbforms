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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorResources;

import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.DbFormsErrors;
import org.dbforms.config.FieldValues;
import org.dbforms.config.MultipleValidationException;

import org.dbforms.util.MessageResources;

import org.dbforms.validation.ValidatorConstants;

import java.util.Locale;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;



/**
 * abstract base class for all database operations which need validation, e.g.
 * InsertEvent and UpdateEvent
 *
 * @author hkk
 */
public abstract class ValidationEvent extends DatabaseEvent {
   private static Log logCat = LogFactory.getLog(ValidationEvent.class.getName()); // logging category for this class

   /**
    * Creates a new ValidationEvent object.
    *
    * @param tableId DOCUMENT ME!
    * @param keyId DOCUMENT ME!
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    */
   public ValidationEvent(int tableId, String keyId,
      HttpServletRequest request, DbFormsConfig config) {
      super(tableId, keyId, request, config);
   }

   /**
    * DO the validation of the form with Commons-Validator.
    *
    * @param formValidatorName The form name to retreive in validation.xml
    * @param context The servlet request we are processing
    *
    * @exception MultipleValidationException The Vector of errors throwed with
    *            this exception
    */
   public void doValidation(String formValidatorName, ServletContext context)
      throws MultipleValidationException {
      FieldValues fieldValues = getFieldValues();

      // If no data to validate, return
      if (fieldValues.size() == 0) {
         return;
      }

      // Retreive ValidatorResources from Application context (loaded with ConfigServlet)
      ValidatorResources vr = (ValidatorResources) context.getAttribute(ValidatorConstants.VALIDATOR);

      if (vr == null) {
         return;
      }

      Validator     validator    = new Validator(vr, formValidatorName.trim());
      Vector        errors       = new Vector();
      DbFormsErrors dbFormErrors = (DbFormsErrors) context.getAttribute(DbFormsErrors.ERRORS);
      Locale        locale       = MessageResources.getLocale(getRequest());

      // Add these resources to perform validation
      validator.addResource(Validator.BEAN_KEY, fieldValues);

      // The values
      validator.addResource("java.util.Vector", errors);
      validator.addResource(Validator.LOCALE_KEY, locale);

      // Vector of errors to populate
      validator.addResource("org.dbforms.config.DbFormsErrors", dbFormErrors);

      try {
         validator.validate();
      } catch (Exception ex) {
         logCat.error("\n!!! doValidation error for : " + formValidatorName
            + "  !!!\n" + ex);
      }

      // If error(s) found, throw Exception
      if (errors.size() > 0) {
         throw new MultipleValidationException(errors);
      }
   }
}
