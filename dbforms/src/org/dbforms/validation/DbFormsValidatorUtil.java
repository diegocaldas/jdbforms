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
import java.util.Locale;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.Collections;
import java.util.Enumeration;

import org.dbforms.config.DbFormsErrors;
import org.dbforms.util.MessageResources;
import org.dbforms.util.Util;
import org.apache.commons.validator.Arg;
import org.apache.commons.validator.Var;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.Form;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorResources;
import org.apache.commons.validator.ValidatorUtil;
import org.apache.log4j.Category;



/**********************************************************************************************
 *         This class generate error string from ResourceBundle if enable,
 *  else generate error in DbForms-Error.xml standard.
 *
 *  @author Eric Beaumier
 **********************************************************************************************/
public class DbFormsValidatorUtil
{
   static Category logCat = Category.getInstance(DbFormsValidatorUtil.class
         .getName());

   /**********************************************************************************************
    *  Get the error message from ResourceBundle if present, else generate a DbForms-Error.xml standard.
    *
    * @param  <code>String</code> : Type of validation (required, range, email,...).
    * @param  <code>ValidatorAction</code> : ValidatorAction to retrieve validation definition.
    * @param  <code>Locale</code> : Locale object to map with the good ResourceBundle
    * @param  <code>Field</code> : Field validator information. (retrieve <arg_> tag from validation.xml).
    * @param  <code>DbFormsErrors</code> : DbForms Error class to retrieve error message in DbForm-Errors.xml format.
    *
   * @return        <code>String</code> : Message resolve.
    **********************************************************************************************/
   public static String getMessage(String type, ValidatorAction va,
      Locale locale, Field field, DbFormsErrors errors)
   {
      String   result = null;
      String[] arg = getArgs(va.getName(), locale, field);
      String   msg = ((field.getMsg(va.getName()) != null)
         ? field.getMsg(va.getName()) : va.getMsg());

      if (msg == null)
      {
         msg = "errors." + va.getName();
      }

      //**************************************************
      // Try to resolve message with Application Resource
      //**************************************************
      result = MessageResources.getMessage(msg, locale, arg);

      if (result != null)
      {
         return result;
      }

      //**************************************************	
      // ELSE Generate DbForms-Error.xml standard	
      //**************************************************
      result = msg + ":";

      for (int i = 0; i < arg.length; i++)
      {
         if (arg[i] != null)
         {
            result += (arg[i] + ",");
         }
      }

      // 2003-01-30 HKK: Removed trailing ,
      result = result.substring(0, result.length() - 1);

      // 2002-09-12 HKK: Catch errors while getXMLErrorMessage!
      try
      {
         result = errors.getXMLErrorMessage(result);
      }
      catch (Exception e)
      {
         logCat.error("Not in proper format - do not try to convert!");
      }

      return result;
   }


   /**********************************************************************************************
    *  Retrieve <arg> parameter from validation.xml.
    *
    * @param  <code>String</code> : Type of validation (required, range, email,...).
    * @param  <code>Locale</code> : Locale object to map with the good ResourceBundle
    * @param  <code>Field</code> : Field validator information. (retrieve <arg_> tag from validation.xml).
    *
    *
   * @return        <code>String</code> : Message resolve.
    **********************************************************************************************/
   public static String[] getArgs(String actionName, Locale locale, Field field)
   {
      Arg    arg0 = field.getArg0(actionName);
      Arg    arg1 = field.getArg1(actionName);
      Arg    arg2 = field.getArg2(actionName);
      Arg    arg3 = field.getArg3(actionName);

      String sArg0 = null;
      String sArg1 = null;
      String sArg2 = null;
      String sArg3 = null;

      if (arg0 != null)
      {
         if (arg0.getResource())
         {
            sArg0 = MessageResources.getMessage(arg0.getKey(), locale);

            if (sArg0 == null)
            {
               sArg0 = arg0.getKey();
            }
         }
         else
         {
            sArg0 = arg0.getKey();
         }
      }
      else
      {
         sArg0 = field.getKey(); // If no <arg0> define
      }

      if (arg1 != null)
      {
         if (arg1.getResource())
         {
            sArg1 = MessageResources.getMessage(arg1.getKey(), locale);

            if (sArg1 == null)
            {
               sArg1 = arg1.getKey();
            }
         }
         else
         {
            sArg1 = arg1.getKey();
         }
      }

      if (arg2 != null)
      {
         if (arg2.getResource())
         {
            sArg2 = MessageResources.getMessage(arg2.getKey(), locale);

            if (sArg2 == null)
            {
               sArg2 = arg2.getKey();
            }
         }
         else
         {
            sArg2 = arg2.getKey();
         }
      }

      if (arg3 != null)
      {
         if (arg3.getResource())
         {
            sArg3 = MessageResources.getMessage(arg3.getKey(), locale);

            if (sArg3 == null)
            {
               sArg3 = arg3.getKey();
            }
         }
         else
         {
            sArg3 = arg3.getKey();
         }
      }

      return new String[]
      {
         sArg0,
         sArg1,
         sArg2,
         sArg3
      };
   }


   /**********************************************************************************************
    *  J A V S C R I P T   G E N E R A T I O N.
    *
    * @param  <code>formName</code> : Name to match with <form> in validation.xml.
    * @param  <code>Locale</code> : Locale object to map with the ResourceBundle
    * @param  <code>Hashtable</code> : Hashtable of field name in table and generated name from DbForms (ex: "field1" : "f_4_3@root_4").
    * @param  <code>ValidatorResources</code> : ValidatorResources use by DbForms
    * @param  <code>javascriptValidationSrcFile</code> : Validation file name to include in src of script.  <SCRIPT src="..."></SCRIPT>
    * @param  <code>DbFormsErrors</code> : Class to resolve DbForms-Error format (DbForms-Errors.xml).
    *
   * @return        <code>StringBuffer</code> : The complete javascript validation section to include in response.
    **********************************************************************************************/
   public static StringBuffer getJavascript(List formNames, Locale locale,
      Hashtable fieldsName, ValidatorResources resources,
      String javascriptValidationSrcFile, DbFormsErrors errors)
   {
      StringBuffer results            = new StringBuffer(); // Final result.   All the Javascript validation code
      StringBuffer jsFunctions        = new StringBuffer(); // Only the validateFunctions() definition from ValidatorAction
      boolean      bJavascriptSrcFile = (javascriptValidationSrcFile != null);

      Form         form     = null;
      String       formName = "";

      // Iterate through all forms in formName array
      List lActions       = new ArrayList();
      List lActionMethods = new ArrayList();

      for (Iterator formIterator = formNames.iterator();
               formIterator.hasNext();)
      {
         formName = (String) formIterator.next();

         if ((form = resources.get(locale, formName)) != null)
         {
            // Get List of actions for this Form
            for (Iterator i = form.getFields().iterator(); i.hasNext();)
            {
               Field field = (Field) i.next();

               // Select only fields inside the <db:form >...</db:form>
               //if(!fieldsName.containsKey(field.getProperty())) continue;
               if (!fieldsName.containsValue(field.getProperty()))
               {
                  continue;
               }

               for (Iterator x = field.getDependencies().iterator();
                        x.hasNext();)
               {
                  Object o = x.next();

                  if ((o != null) && !lActionMethods.contains(o))
                  {
                     lActionMethods.add(o);
                  }
               }
            }
         }
      }

      // Create list of ValidatorActions based on lActionMethods
      for (Iterator i = lActionMethods.iterator(); i.hasNext();)
      {
         String          actionName = (String) i.next();

         ValidatorAction va = resources.getValidatorAction(actionName);

         String          javascript = va.getJavascript();

         if ((javascript != null) && (javascript.length() > 0))
         {
            lActions.add(va);
         }
         else
         {
            i.remove();
         }
      }

      Collections.sort(lActions,
         new Comparator()
         {
            public int compare(Object o1, Object o2)
            {
               ValidatorAction va1 = (ValidatorAction) o1;
               ValidatorAction va2 = (ValidatorAction) o2;

               if (((va1.getDepends() == null)
                        || (va1.getDepends().length() == 0))
                        && ((va2.getDepends() == null)
                        || (va2.getDepends().length() == 0)))
               {
                  return 0;
               }
               else if (((va1.getDepends() != null)
                        && (va1.getDepends().length() > 0))
                        && ((va2.getDepends() == null)
                        || (va2.getDepends().length() == 0)))
               {
                  return 1;
               }
               else if (((va1.getDepends() == null)
                        || (va1.getDepends().length() == 0))
                        && ((va2.getDepends() != null)
                        && (va2.getDepends().length() > 0)))
               {
                  return -1;
               }
               else
               {
                  return va1.getDependencies().size()
                  - va2.getDependencies().size();
               }
            }
         });

      String methods = null;

      for (Iterator i = lActions.iterator(); i.hasNext();)
      {
         ValidatorAction va = (ValidatorAction) i.next();

         if (methods == null)
         {
            methods = va.getMethod() + "(form)";
         }
         else
         {
            methods += (" && " + va.getMethod() + "(form)");
         }
      }

      results.append(getJavascriptBegin(formName, methods));

      for (Iterator i = lActions.iterator(); i.hasNext();)
      {
         ValidatorAction va           = (ValidatorAction) i.next();
         String          jscriptVar   = null;
         String          functionName = null;

         if ((va.getJsFunctionName() != null)
                  && (va.getJsFunctionName().length() > 0))
         {
            functionName = va.getJsFunctionName();
         }
         else
         {
            functionName = va.getName();
         }

         if (!bJavascriptSrcFile)
         {
            jsFunctions.append(va.getJavascript());
            jsFunctions.append("\n\n");
         }

         results.append("	 function " + functionName + " () { \n");

         for (Iterator formIterator = formNames.iterator();
                  formIterator.hasNext();)
         {
            formName = (String) formIterator.next();

            if ((form = resources.get(locale, formName)) != null)
            {
               for (Iterator x = form.getFields().iterator(); x.hasNext();)
               {
                  Field field = (Field) x.next();

                  // Skip indexed fields for now until there is 
                  // a good way to handle error messages (and the length of the list (could retrieve from scope?))
                  if (!field.isIndexed() && field.isDependency(va.getName()))
                  {
                     //String message = DbFormsValidatorUtil.getMessage(messages, locale, va, field);
                     //message = (message != null ? message : "");
                     //jscriptVar = getNextVar(jscriptVar);
                     String      message = getMessage(functionName, va, locale,
                           field, errors);
                     Enumeration enum = fieldsName.keys();

                     while (enum.hasMoreElements())
                     {
                        String fieldName = (String) enum.nextElement();
                        String val = (String) fieldsName.get(fieldName);

                        if (field.getKey().equals(val))
                        {
                           jscriptVar = getNextVar(jscriptVar);

                           if (fieldName.indexOf("insroot") != -1)
                           {
                              // Valide only Insert Mode
                              results.append("\t    if(")
                                     .append(ValidatorConstants.JS_UPDATE_VALIDATION_MODE)
                                     .append("==false) ");
                           }
                           else
                           {
                              // Valide only Update Mode
                              results.append("\t    if(")
                                     .append(ValidatorConstants.JS_UPDATE_VALIDATION_MODE)
                                     .append("==true) ");
                           }

                           results.append(" this." + jscriptVar
                              + " = new Array(\"" + fieldName + "\", \""
                              + message + "\", ");
                           results.append("new Function (\"varName\", \"");

                           Map hVars = field.getVars();

                           // Loop through the field's variables.
                           for (Iterator iVars = hVars.keySet().iterator();
                                    iVars.hasNext();)
                           {
                              String varKey   = (String) iVars.next();
                              Var    var      = (Var) hVars.get(varKey);
                              String varValue = var.getValue();
                              String jsType   = var.getJsType();

                              if (Var.JSTYPE_INT.equalsIgnoreCase(jsType))
                              {
                                 results.append("this." + varKey + "="
                                    + ValidatorUtil.replace(varValue, "\\",
                                       "\\\\") + "; ");
                              }
                              else if (Var.JSTYPE_REGEXP.equalsIgnoreCase(
                                          jsType))
                              {
                                 results.append("this." + varKey + "=/"
                                    + ValidatorUtil.replace(varValue, "\\",
                                       "\\\\") + "/; ");
                              }
                              else if (Var.JSTYPE_STRING.equalsIgnoreCase(
                                          jsType))
                              {
                                 results.append("this." + varKey + "='"
                                    + ValidatorUtil.replace(varValue, "\\",
                                       "\\\\") + "'; ");
                              }

                              // So everyone using the latest format doesn't need to change their xml files immediately.
                              else if ("mask".equalsIgnoreCase(varKey))
                              {
                                 results.append("this." + varKey + "=/"
                                    + ValidatorUtil.replace(varValue, "\\",
                                       "\\\\") + "/; ");
                              }
                              else
                              {
                                 results.append("this." + varKey + "='"
                                    + ValidatorUtil.replace(varValue, "\\",
                                       "\\\\") + "'; ");
                              }
                           }

                           results.append(" return this[varName];\"));\n");
                        }
                     }
                  }
               }
            }
         }

         results.append("	 } \n\n");
      }

      // 2003-01-30 HKK: if form not found do not generte trailing script code!!!        
      if (!bJavascriptSrcFile)
      {
         results.append(jsFunctions.toString());
      }

      results.append(getJavascriptEnd());

      if (bJavascriptSrcFile)
      {
         results.append("\n<SCRIPT language=\"javascript\" src=\""
            + javascriptValidationSrcFile + "\"></SCRIPT> \n");
      }

      return results;
   }


   //*********************************************************************************************
   // ***
   // ***  P R I V A T E    U S E 
   // ***
   //*********************************************************************************************
   private static String getJavascriptBegin(String formName, String methods)
   {
      StringBuffer sb   = new StringBuffer();
      String       name = Character.toUpperCase(formName.charAt(0))
         + formName.substring(1, formName.length());

      sb.append("<script language=\"Javascript1.1\"> \n");

      sb.append("<!-- Begin \n");
      sb.append("\n	 var " + ValidatorConstants.JS_CANCEL_VALIDATION
         + " = false;");
      sb.append("\n	 var " + ValidatorConstants.JS_UPDATE_VALIDATION_MODE
         + " = true; \n\n");

      sb.append("	 function validate" + name + "(form) {  \n");

      sb.append("	      if (!" + ValidatorConstants.JS_CANCEL_VALIDATION
         + ") \n");
      sb.append("	 	return true; \n");
      sb.append("	      else \n");

      // Always return true if there aren't any Javascript validation methods
      if (Util.isNull(methods))
      {
         sb.append("	 	return true; \n");
      }
      else
      {
         sb.append("	 	return " + methods + "; \n");
      }

      sb.append("	 } \n\n");

      return sb.toString();
   }


   private static String getJavascriptEnd()
   {
      StringBuffer sb = new StringBuffer();

      sb.append("\n");
      sb.append("//  End -->\n");
      sb.append("</script>\n\n");

      return sb.toString();
   }


   private static String getNextVar(String input)
   {
      if (input == null)
      {
         return "aa";
      }

      input = input.toLowerCase();

      for (int i = input.length(); i > 0; i--)
      {
         int  pos = i - 1;

         char c = input.charAt(pos);
         c++;

         if (c <= 'z')
         {
            if (i == 0)
            {
               return c + input.substring(pos, input.length());
            }
            else if (i == input.length())
            {
               return input.substring(0, pos) + c;
            }
            else
            {
               return input.substring(0, pos) + c
               + input.substring(pos, input.length() - 1);
            }
         }
         else
         {
            input = replaceChar(input, pos, 'a');
         }
      }

      return null;
   }


   private static String replaceChar(String input, int pos, char c)
   {
      if (pos == 0)
      {
         return c + input.substring(pos, input.length());
      }
      else if (pos == input.length())
      {
         return input.substring(0, pos) + c;
      }
      else
      {
         return input.substring(0, pos) + c
         + input.substring(pos, input.length() - 1);
      }
   }
}
