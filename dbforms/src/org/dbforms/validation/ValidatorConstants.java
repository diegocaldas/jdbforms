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
package org.dbforms.validation;
import java.util.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import org.apache.commons.validator.ValidatorResources;
import org.apache.log4j.Category;



/****
 * <p>
 * This class is use for static reference primarily.
 * </p>
 *
 * @author Eric Beaumier
 */
public class ValidatorConstants
{
   /** DOCUMENT ME! */
   public static final String VALIDATION = "validation";

   /** DOCUMENT ME! */
   public static final String VALIDATOR_RULES = "validator-rules";

   /** DOCUMENT ME! */
   public static final String RESOURCE_BUNDLE = "resourceBundle";

   /** DOCUMENT ME! */
   public static final String VALIDATOR = "Validator";

   /** DOCUMENT ME! */
   public static final String FORM_VALIDATOR_NAME = "formValidatorName";

   /** DOCUMENT ME! */
   public static final String JS_CANCEL_VALIDATION = "bValidateForm";

   /** DOCUMENT ME! */
   public static final String JS_UPDATE_VALIDATION_MODE = "bValidateUpdate";

   /**
    * Creates a new ValidatorConstants object.
    */
   public ValidatorConstants()
   {
   }
}
