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
import java.io.Serializable;
import java.util.Locale;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ValidatorAction;
import javax.servlet.ServletContext;
import org.dbforms.DbFormsErrors;
import java.util.Vector;
import java.util.Hashtable;
import org.dbforms.event.ValidationException;
import org.apache.log4j.Category;



/*********************************************************************************************
 * <p>This class performs validations.  
 * The parameters of methods must match the "methodParams" in "validator-rules.xml" file. </p>
 *
 * @author Eric Beaumier  
*********************************************************************************************/
public class DbFormsValidator implements Serializable
{
    static Category logCat = Category.getInstance(DbFormsValidator.class.getName());
    private static final String REQUIRED = "required";
    private static final String MASK = "mask";
    private static final String RANGE = "range";
    private static final String MINLENGTH = "minlength";
    private static final String MAXLENGTH = "maxlength";
    private static final String BYTE = "byte";
    private static final String SHORT = "short";
    private static final String LONG = "long";
    private static final String INTEGER = "integer";
    private static final String FLOAT = "float";
    private static final String DOUBLE = "double";
    private static final String DATE = "date";
    private static final String CREDITCARD = "creditcard";
    private static final String EMAIL = "email";

    /*********************************************************************************************
     * <p>Checks if the field isn't null and length of the field is greater than zero not 
     * including whitespace.</p>
     *
     * @param         bean                 The bean validation is being performed on.
     * @param         va                         The <code>ValidatorAction</code> used to retrieve validator information.
     * @param         field                 The <code>Field</code> object associated with the current field being validated.
     * @param         errors                 The <code>Vector</code> object used by DBForms to add errors to, if any validation errors occur.
     * @param         locale                 The <code>Locale</code> object of the Request.
          * @param   dbFormsErrors  DbForms Error class to retrieve error message in DbForm-Errors.xml format. 
     ********************************************************************************************/
    public static boolean validateRequired(Object bean, ValidatorAction va, Field field, Vector errors, Locale locale, DbFormsErrors dbFormsErrors)
    {
        Hashtable hash = (Hashtable) bean;

        String value = null;

        if ((field.getProperty() != null) && (field.getProperty().length() > 0))
        {
            value = (String) hash.get(field.getProperty());
        }

        if (GenericValidator.isBlankOrNull(value))
        {
            errors.add(new ValidationException(DbFormsErrorMessage(REQUIRED, va, field, locale, dbFormsErrors)));

            return false;
        }
        else
        {
            return true;
        }
    }


    /*********************************************************************************************
     * <p>Checks if the field matches the regular expression in the field's mask attribute.</p>
     *
     * @param         bean                 The bean validation is being performed on.
     * @param         va                         The <code>ValidatorAction</code> used to retrieve validator information.
     * @param         field                 The <code>Field</code> object associated with the current field being validated.
     * @param         errors                 The <code>Vector</code> object used by DBForms to add errors to if any validation errors occur.
     * @param         locale                 The <code>Locale</code> object of the Request.
          * @param   dbFormsErrors  DbForms Error class to retrieve error message in DbForm-Errors.xml format. 
     ********************************************************************************************/
    public static boolean validateMask(Object bean, ValidatorAction va, Field field, Vector errors, Locale locale, DbFormsErrors dbFormsErrors)
    {
        Hashtable hash = (Hashtable) bean;

        String mask = field.getVarValue("mask");

        if ((field.getProperty() != null) && (field.getProperty().length() > 0))
        {
            String value = (String) hash.get(field.getProperty());

            try
            {
                if (!GenericValidator.isBlankOrNull(value) && !GenericValidator.matchRegexp(value, mask))
                {
                    errors.add(new ValidationException(DbFormsErrorMessage(MASK, va, field, locale, dbFormsErrors)));

                    return false;
                }
                else
                {
                    return true;
                }
            }
            catch (Exception e)
            {
                logCat.error("Validator::validateMask() - " + e.getMessage());
            }
        }

        return true;
    }


    /********************************************************************************************
     * <p>Checks if the field can safely be converted to a byte primitive.</p>
     *
     * @param         bean                 The bean validation is being performed on.
     * @param         va                         The <code>ValidatorAction</code> used to retrieve validator information.
     * @param         field                 The <code>Field</code> object associated with the current field being validated.
     * @param         errors                 The <code>Vector</code> object used by DBForms to add errors to if any validation errors occur.
     * @param         locale                 The <code>Locale</code> object of the Request.
          * @param   dbFormsErrors  DbForms Error class to retrieve error message in DbForm-Errors.xml format. 
     ********************************************************************************************/
    public static boolean validateByte(Object bean, ValidatorAction va, Field field, Vector errors, Locale locale, DbFormsErrors dbFormsErrors)
    {
        Hashtable hash = (Hashtable) bean;
        String value = (String) hash.get(field.getProperty());

        if (!GenericValidator.isBlankOrNull(value) && !GenericValidator.isByte(value))
        {
            errors.add(new ValidationException(DbFormsErrorMessage(BYTE, va, field, locale, dbFormsErrors)));

            return false;
        }
        else
        {
            return true;
        }
    }


    /********************************************************************************************
     * <p>Checks if the field can safely be converted to a short primitive.</p>
     *
     * @param         bean                 The bean validation is being performed on.
     * @param         va                         The <code>ValidatorAction</code> used to retrieve validator information.
     * @param         field                 The <code>Field</code> object associated with the current field being validated.
     * @param         errors                 The <code>Vector</code> object used by DBForms to add errors to if any validation errors occur.
     * @param         locale                 The <code>Locale</code> object of the Request.
          * @param   dbFormsErrors  DbForms Error class to retrieve error message in DbForm-Errors.xml format. 
     ********************************************************************************************/
    public static boolean validateShort(Object bean, ValidatorAction va, Field field, Vector errors, Locale locale, DbFormsErrors dbFormsErrors)
    {
        Hashtable hash = (Hashtable) bean;
        String value = (String) hash.get(field.getProperty());

        if (!GenericValidator.isBlankOrNull(value) && !GenericValidator.isShort(value))
        {
            errors.add(new ValidationException(DbFormsErrorMessage(SHORT, va, field, locale, dbFormsErrors)));

            return false;
        }
        else
        {
            return true;
        }
    }


    /********************************************************************************************
     * <p>Checks if the field can safely be converted to an int primitive.</p>
     *
     * @param         bean                 The bean validation is being performed on.
     * @param         va                         The <code>ValidatorAction</code> used to retrieve validator information.
     * @param         field                 The <code>Field</code> object associated with the current field being validated.
     * @param         errors                 The <code>Vector</code> object used by DBForms to add errors to if any validation errors occur.
     * @param         locale                 The <code>Locale</code> object of the Request.
          * @param   dbFormsErrors  DbForms Error class to retrieve error message in DbForm-Errors.xml format. 
     ********************************************************************************************/
    public static boolean validateInteger(Object bean, ValidatorAction va, Field field, Vector errors, Locale locale, DbFormsErrors dbFormsErrors)
    {
        Hashtable hash = (Hashtable) bean;
        String value = (String) hash.get(field.getProperty());

        if (!GenericValidator.isBlankOrNull(value) && !GenericValidator.isInt(value))
        {
            errors.add(new ValidationException(DbFormsErrorMessage(INTEGER, va, field, locale, dbFormsErrors)));

            return false;
        }
        else
        {
            return true;
        }
    }


    /********************************************************************************************
     * <p>Checks if the field can safely be converted to a long primitive.</p>
     *
     * @param         bean                 The bean validation is being performed on.
     * @param         va                         The <code>ValidatorAction</code> used to retrieve validator information.
     * @param         field                 The <code>Field</code> object associated with the current field being validated.
     * @param         errors                 The <code>Vector</code> object used by DBForms to add errors to if any validation errors occur.
     * @param         locale                 The <code>Locale</code> object of the Request.
          * @param   dbFormsErrors  DbForms Error class to retrieve error message in DbForm-Errors.xml format. 
     ********************************************************************************************/
    public static boolean validateLong(Object bean, ValidatorAction va, Field field, Vector errors, Locale locale, DbFormsErrors dbFormsErrors)
    {
        Hashtable hash = (Hashtable) bean;
        String value = (String) hash.get(field.getProperty());

        if (!GenericValidator.isBlankOrNull(value) && !GenericValidator.isLong(value))
        {
            errors.add(new ValidationException(DbFormsErrorMessage(LONG, va, field, locale, dbFormsErrors)));

            return false;
        }
        else
        {
            return true;
        }
    }


    /********************************************************************************************
     * <p>Checks if the field can safely be converted to a float primitive.</p>
     *
     * @param         bean                 The bean validation is being performed on.
     * @param         va                         The <code>ValidatorAction</code> used to retrieve validator information.
     * @param         field                 The <code>Field</code> object associated with the current field being validated.
     * @param         errors                 The <code>Vector</code> object used by DBForms to add errors to if any validation errors occur.
     * @param         locale                 The <code>Locale</code> object of the Request.
          * @param   dbFormsErrors  DbForms Error class to retrieve error message in DbForm-Errors.xml format. 
     ********************************************************************************************/
    public static boolean validateFloat(Object bean, ValidatorAction va, Field field, Vector errors, Locale locale, DbFormsErrors dbFormsErrors)
    {
        Hashtable hash = (Hashtable) bean;
        String value = (String) hash.get(field.getProperty());

        if (!GenericValidator.isBlankOrNull(value) && !GenericValidator.isFloat(value))
        {
            errors.add(new ValidationException(DbFormsErrorMessage(FLOAT, va, field, locale, dbFormsErrors)));

            return false;
        }
        else
        {
            return true;
        }
    }


    /********************************************************************************************
     * <p>Checks if the field can safely be converted to a double primitive.</p>
     *
     * @param         bean                 The bean validation is being performed on.
     * @param         va                         The <code>ValidatorAction</code> used to retrieve validator information.
     * @param         field                 The <code>Field</code> object associated with the current field being validated.
     * @param         errors                 The <code>Vector</code> object used by DBForms to add errors to if any validation errors occur.
     * @param         locale                 The <code>Locale</code> object of the Request.
          * @param   dbFormsErrors  DbForms Error class to retrieve error message in DbForm-Errors.xml format. 
     ********************************************************************************************/
    public static boolean validateDouble(Object bean, ValidatorAction va, Field field, Vector errors, Locale locale, DbFormsErrors dbFormsErrors)
    {
        Hashtable hash = (Hashtable) bean;
        String value = (String) hash.get(field.getProperty());

        if (!GenericValidator.isBlankOrNull(value) && !GenericValidator.isDouble(value))
        {
            errors.add(new ValidationException(DbFormsErrorMessage(DOUBLE, va, field, locale, dbFormsErrors)));

            return false;
        }
        else
        {
            return true;
        }
    }


    /********************************************************************************************
     * <p>Checks if the field is a valid date.  If the field has a datePattern variable, 
     * that will be used to format <code>java.text.SimpleDateFormat</code>.  If the field 
     * has a datePatternStrict variable, that will be used to format 
     * <code>java.text.SimpleDateFormat</code> and the length will be checked so '2/12/1999' 
     * will not pass validation with the format 'MM/dd/yyyy' because the month isn't two digits.
     * If no datePattern variable is specified, then the field gets the DateFormat.SHORT 
     * format for the locale.  The setLenient method is set to <code>false</code> for all 
     * variations.</p>
     *
     * @param         bean                 The bean validation is being performed on.
     * @param         va                         The <code>ValidatorAction</code> used to retrieve validator information.
     * @param         field                 The <code>Field</code> object associated with the current field being validated.
     * @param         errors                 The <code>Vector</code> object used by DBForms to add errors to if any validation errors occur.
     * @param         locale                 The <code>Locale</code> object of the Request.
          * @param   dbFormsErrors  DbForms Error class to retrieve error message in DbForm-Errors.xml format. 
     ********************************************************************************************/
    public static boolean validateDate(Object bean, ValidatorAction va, Field field, Vector errors, Locale locale, DbFormsErrors dbFormsErrors)
    {
        Hashtable hash = (Hashtable) bean;
        String value = (String) hash.get(field.getProperty());

        boolean bValid = true;

        String datePattern = field.getVarValue("datePattern");
        String datePatternStrict = field.getVarValue("datePatternStrict");

        if (!GenericValidator.isBlankOrNull(value))
        {
            try
            {
                if ((datePattern != null) && (datePattern.length() > 0))
                {
                    bValid = GenericValidator.isDate(value, datePattern, false);
                }
                else if ((datePatternStrict != null) && (datePatternStrict.length() > 0))
                {
                    bValid = GenericValidator.isDate(value, datePatternStrict, true);
                }
                else
                {
                    bValid = GenericValidator.isDate(value, locale);
                }
            }
            catch (Exception e)
            {
                errors.add(new ValidationException(DbFormsErrorMessage(DATE, va, field, locale, dbFormsErrors)));
                bValid = false;
            }
        }

        if (!bValid)
        {
            errors.add(new ValidationException(DbFormsErrorMessage(DATE, va, field, locale, dbFormsErrors)));
        }

        logCat.debug("VALIDATEDATE()  datePattern:" + datePattern + "  datePatternStrict:" + datePatternStrict + " value:" + value + "  return:" + bValid);

        return bValid;
    }


    /********************************************************************************************
     * <p>Checks if a fields value is within a range (min &amp; max specified 
     * in the vars attribute).</p>
     *
     * @param         bean                 The bean validation is being performed on.
     * @param         va                         The <code>ValidatorAction</code> used to retrieve validator information.
     * @param         field                 The <code>Field</code> object associated with the current field being validated.
     * @param         errors                 The <code>Vector</code> object used by DBForms to add errors to if any validation errors occur.
     * @param         locale                 The <code>Locale</code> object of the Request.
          * @param   dbFormsErrors  DbForms Error class to retrieve error message in DbForm-Errors.xml format. 
     ********************************************************************************************/
    public static boolean validateRange(Object bean, ValidatorAction va, Field field, Vector errors, Locale locale, DbFormsErrors dbFormsErrors)
    {
        Hashtable hash = (Hashtable) bean;
        String value = (String) hash.get(field.getProperty());
        String sMin = field.getVarValue("min");
        String sMax = field.getVarValue("max");

        if (!GenericValidator.isBlankOrNull(value))
        {
            try
            {
                int iValue = Integer.parseInt(value);
                int min = Integer.parseInt(sMin);
                int max = Integer.parseInt(sMax);

                if (!GenericValidator.isInRange(iValue, min, max))
                {
                    errors.add(new ValidationException(DbFormsErrorMessage(RANGE, va, field, locale, dbFormsErrors)));

                    return false;
                }
            }
            catch (Exception e)
            {
                errors.add(new ValidationException(DbFormsErrorMessage(RANGE, va, field, locale, dbFormsErrors)));

                return false;
            }
        }

        return true;
    }


    /********************************************************************************************
     * <p>Checks if the field is a valid credit card number.</p>
     * <p>Translated to Java by Ted Husted (<a href="mailto:husted@apache.org">husted@apache.org</a>).<br>
     * &nbsp;&nbsp;&nbsp; Reference Sean M. Burke's script at http://www.ling.nwu.edu/~sburke/pub/luhn_lib.pl</p>
     *
     * @param         bean                 The bean validation is being performed on.
     * @param         va                         The <code>ValidatorAction</code> used to retrieve validator information.
     * @param         field                 The <code>Field</code> object associated with the current field being validated.
     * @param         errors                 The <code>Vector</code> object used by DBForms to add errors to if any validation errors occur.
     * @param         locale                 The <code>Locale</code> object of the Request.
          * @param   dbFormsErrors  DbForms Error class to retrieve error message in DbForm-Errors.xml format. 
     ********************************************************************************************/
    public static boolean validateCreditCard(Object bean, ValidatorAction va, Field field, Vector errors, Locale locale, DbFormsErrors dbFormsErrors)
    {
        Hashtable hash = (Hashtable) bean;

        if ((field.getProperty() != null) && (field.getProperty().length() > 0))
        {
            String value = (String) hash.get(field.getProperty());

            if (!GenericValidator.isBlankOrNull(value) && !GenericValidator.isCreditCard(value))
            {
                errors.add(new ValidationException(DbFormsErrorMessage(CREDITCARD, va, field, locale, dbFormsErrors)));

                return false;
            }
        }

        return true;
    }


    /********************************************************************************************
     * <p>Checks if a field has a valid e-mail address.</p>
     * <p>Based on a script by Sandeep V. Tamhankar (stamhankar@hotmail.com), 
     * http://javascript.internet.com</p>
     *
     * @param         bean                 The bean validation is being performed on.
     * @param         va                         The <code>ValidatorAction</code> used to retrieve validator information.
     * @param         field                 The <code>Field</code> object associated with the current field being validated.
     * @param         errors                 The <code>Vector</code> object used by DBForms to add errors to if any validation errors occur.
     * @param         locale                 The <code>Locale</code> object of the Request.
          * @param   dbFormsErrors  DbForms Error class to retrieve error message in DbForm-Errors.xml format. 
     ********************************************************************************************/
    public static boolean validateEmail(Object bean, ValidatorAction va, Field field, Vector errors, Locale locale, DbFormsErrors dbFormsErrors)
    {
        Hashtable hash = (Hashtable) bean;
        String value = (String) hash.get(field.getProperty());

        if (!GenericValidator.isBlankOrNull(value) && !GenericValidator.isEmail(value))
        {
            errors.add(new ValidationException(DbFormsErrorMessage(EMAIL, va, field, locale, dbFormsErrors)));

            return false;
        }
        else
        {
            return true;
        }
    }


    /********************************************************************************************
     * <p>Checks if the field's length is less than or equal to the maximum value.  A <code>Null</code> 
     * will be considered an error.</p>
     *
     * @param         bean                 The bean validation is being performed on.
     * @param         va                         The <code>ValidatorAction</code> used to retrieve validator information.
     * @param         field                 The <code>Field</code> object associated with the current field being validated.
     * @param         errors                 The <code>Vector</code> object used by DBForms to add errors to if any validation errors occur.
     * @param         locale                 The <code>Locale</code> object of the Request.
          * @param   dbFormsErrors  DbForms Error class to retrieve error message in DbForm-Errors.xml format. 
     ********************************************************************************************/
    public static boolean validateMaxLength(Object bean, ValidatorAction va, Field field, Vector errors, Locale locale, DbFormsErrors dbFormsErrors)
    {
        Hashtable hash = (Hashtable) bean;
        String value = (String) hash.get(field.getProperty());
        String sMaxLength = field.getVarValue("maxlength");

        if (value != null)
        {
            try
            {
                int max = Integer.parseInt(sMaxLength);

                if (!GenericValidator.maxLength(value, max))
                {
                    errors.add(new ValidationException(DbFormsErrorMessage(MAXLENGTH, va, field, locale, dbFormsErrors)));

                    return false;
                }
            }
            catch (Exception e)
            {
                errors.add(new ValidationException(DbFormsErrorMessage(MAXLENGTH, va, field, locale, dbFormsErrors)));

                return false;
            }
        }

        return true;
    }


    /********************************************************************************************
     * <p>Checks if the field's length is greater than or equal to the minimum value.  
     * A <code>Null</code> will be considered an error.</p>
     *
     * @param         bean                 The bean validation is being performed on.
     * @param         va                         The <code>ValidatorAction</code> used to retrieve validator information.
     * @param         field                 The <code>Field</code> object associated with the current field being validated.
     * @param         errors                 The <code>Vector</code> object used by DBForms to add errors to if any validation errors occur.
     * @param         locale                 The <code>Locale</code> object of the Request.
          * @param   dbFormsErrors  DbForms Error class to retrieve error message in DbForm-Errors.xml format. 
     ********************************************************************************************/
    public static boolean validateMinLength(Object bean, ValidatorAction va, Field field, Vector errors, Locale locale, DbFormsErrors dbFormsErrors)
    {
        Hashtable hash = (Hashtable) bean;
        String value = (String) hash.get(field.getProperty());
        String sMinLength = field.getVarValue("minlength");

        if (value != null)
        {
            try
            {
                int min = Integer.parseInt(sMinLength);

                if (!GenericValidator.minLength(value, min))
                {
                    errors.add(new ValidationException(DbFormsErrorMessage(MINLENGTH, va, field, locale, dbFormsErrors)));

                    return false;
                }
            }
            catch (Exception e)
            {
                errors.add(new ValidationException(DbFormsErrorMessage(MINLENGTH, va, field, locale, dbFormsErrors)));

                return false;
            }
        }

        return true;
    }


    //***************************************************************************************************
    //*** P R I V A T E 	
    //***************************************************************************************************

    /********************************************************************************************
    * <p>Generate error message with the ResourceBundle error format if enable or 
    * DBForms error standard format (dbforms_error.xml).
     *</p>
    *
    * @param         type                 Type name to retrieve <msg name="..."> and <arg name="..."> keys.
    * @param         va                         The <code>ValidatorAction</code> object associated with the current field.
    * @param         field                 The <code>Field</code> object associated with the current field being validated.
    * @param         locale                 The <code>Locale</code> object of the Request.
      * @param   dbFormsErrors  DbForms Error class to retrieve error message in DbForm-Errors.xml format. 
    ********************************************************************************************/
    private static String DbFormsErrorMessage(String type, ValidatorAction va, Field field, Locale locale, DbFormsErrors dbFormsErrors)
    {
        //2003-01-31 HKK: Removed field.getMsg. It's handled in DbFormsValidatorUtil.getMessage
        String message = DbFormsValidatorUtil.getMessage(type, va, locale, field, dbFormsErrors);
     	  return message;
    }
}