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
import java.util.Hashtable;
import java.util.Vector;
import java.util.Locale;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.ValidatorResources;
import org.apache.commons.validator.Validator;

import org.dbforms.validation.ValidatorConstants;

import org.dbforms.config.MultipleValidationException;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.error.DbFormsErrors;
import org.dbforms.util.MessageResources;



/**
 * 
 *
 * abstract base class for all database operations which need validation, e.g. InsertEvent and UpdateEvent
 *
 *
 * @author hkk
 * 
 * @todo ATTENTION: this class must be tested, seems not to work!!!!!!
 */
public abstract class ValidationEvent extends DatabaseEvent
{
   /**
    * Creates a new ValidationEvent object.
    *
    * @param tableId DOCUMENT ME!
    * @param keyId DOCUMENT ME!
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    */
   public ValidationEvent(int tableId, String keyId,
      HttpServletRequest request, DbFormsConfig config)
   {
      super(tableId, keyId, request, config);
   }

   /**
    *  DO the validation of <FORM> with Commons-Validator.
    *
    * @param  formValidatorName The form name to retreive in validation.xml
    * @param  request The servlet request we are processing
    * @param  e the web event
    * @exception  MultipleValidationException The Vector of errors throwed with this exception
    */
   public void doValidation(String formValidatorName, ServletContext context,
      HttpServletRequest request) throws MultipleValidationException
   {
      Hashtable fieldValues = null;
      fieldValues = getAssociativeFieldValues(getFieldValues());

      // If no data to validate, return
      if (fieldValues.size() == 0)
      {
         return;
      }

      // Retreive ValidatorResources from Application context (loaded with ConfigServlet)
      ValidatorResources vr = (ValidatorResources) context.getAttribute(ValidatorConstants.VALIDATOR);

      if (vr == null)
      {
         return;
      }

      Validator     validator    = new Validator(vr, formValidatorName.trim());
      Vector        errors       = new Vector();
      DbFormsErrors dbFormErrors = (DbFormsErrors) context.getAttribute(DbFormsErrors.ERRORS);
      Locale        locale       = MessageResources.getLocale(request);

      // Add these resources to perform validation
      validator.addResource(Validator.BEAN_KEY, fieldValues);

      // The values
      validator.addResource("java.util.Vector", errors);
      validator.addResource(Validator.LOCALE_KEY, locale);

      // Vector of errors to populate
      validator.addResource("org.dbforms.config.error.DbFormsErrors", dbFormErrors);

      try
      {
         validator.validate();
      }
      catch (Exception ex)
      {
         logCat.error("\n!!! doValidation error for : " + formValidatorName
            + "  !!!\n" + ex);
      }

      // If error(s) found, throw Exception
      if (errors.size() > 0)
      {
         throw new MultipleValidationException(errors);
      }
   }
}
