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

package org.dbforms.util;
import java.util.Vector;
import org.apache.log4j.Category;



/**
 * DOCUMENT ME!
 *
 * @version $Revision$
 * @author $author$
 */
public class XMLErrorsUtil
{
   // logging category for this class
   private static Category logCat = Category.getInstance(XMLErrorsUtil.class.getName());

   /** DOCUMENT ME! */
   public static final char PARAMETER_DELIMITER = '%';

   /**
    * <p>
    * Method for parsing substring embedded by constant delimeters
    * </p>
    * 
    * <p>
    * consider the following string s: English-001:param1, param2
    * </p>
    * 
    * <p>
    * <pre>
    *  </pre>
    * </p>
    * @param str DOCUMENT ME!
    * @param afterDelims DOCUMENT ME!
    * @param delim DOCUMENT ME!
    * 
    * @return DOCUMENT ME!
    */
   public static String getEmbeddedStringForErrors(String str, int afterDelims, 
                                                   char delim)
   {
      int lastIndex = 0;

      for (int i = 0; i < afterDelims; i++)
      {
         lastIndex = str.indexOf(delim, lastIndex) + 1; // search end of cutting
      }

      int nextIndex = str.indexOf(delim, lastIndex); // end of cutting

      if (nextIndex == -1)
      {
         nextIndex = str.length();
      }

      return str.substring(lastIndex, nextIndex);
   }


   /**
    * DOCUMENT ME!
    *
    * @param message DOCUMENT ME!
    * @param dbfErrors DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public static String getXMLErrorMessage(String message, 
                                           DbFormsErrors dbfErrors)
   {
      String                         langCode   = null;
      String                         language   = null;
      String                         errorCode  = null;
      String                         paramList  = null;
      org.dbforms.config.error.Error anError    = null;
      String                         xmlMessage = null;

      // If message is empty, return immediately
      if ((message == null) || (message.trim().length() == 0))
      {
         return null;
      }

      // Extract Language , Error Code ID, and parameters
      // ie: English-001:FieldName,200
      // ie: ORA-00001:Oracle Message
      try
      {
         langCode  = getEmbeddedStringForErrors(message, 0, ':');
         language  = getEmbeddedStringForErrors(langCode, 0, '-');
         errorCode = getEmbeddedStringForErrors(langCode, 1, '-');
         paramList = getEmbeddedStringForErrors(message, 1, ':');
      }
      catch (Exception e)
      {
         //Not in proper format - do not try to convert!
      }

      // Reference to listing of predefined errors (xml file)
      if (errorCode != null)
      {
         // Error does not contain an id, ignore!				
         if (errorCode.trim().length() == 0)
         {
            return "";
         }

         anError = dbfErrors.getErrorById(errorCode);
      }

      // Get message associated to language
      if (anError != null)
      {
         xmlMessage = anError.getMessage(language);

         if (xmlMessage != null)
         {
            // Replace placeholder with supplied parameters
            xmlMessage = insertParametersInString(xmlMessage, paramList);
         }
         else
         {
            xmlMessage = "No message defined! - check dbForms-error.xml";
         }

         return xmlMessage;
      }
      else
      {
         // An error has occured, 
         // however a custom error messages is not available		
         return message;
      }
   }


   /**
    * Grunikiewicz.philip  2001-12-04 Replace placeholders by parameters Make
    * sure that parameters required = parameters provided!
    * @param xmlMessage DOCUMENT ME!
    * @param paramList DOCUMENT ME!
    * 
    * @return DOCUMENT ME!
    */
   public static String insertParametersInString(String xmlMessage, 
                                                 String paramList)
   {
      // Check if their are parameters expected
      int pos = xmlMessage.indexOf(PARAMETER_DELIMITER);

      if ((pos < 0) || (paramList == null))
      {
         return xmlMessage; // No parameters found!
      }

      // Sort parameter list
      Vector v     = ParseUtil.splitString(paramList, ",");
      int    count = 0;

      while (pos >= 0)
      {
         // Replace
         String prefix = xmlMessage.substring(0, pos);
         String suffix = xmlMessage.substring(pos + 1);
         xmlMessage = prefix + (String) v.elementAt(count);
         xmlMessage += suffix;
         pos = xmlMessage.indexOf(PARAMETER_DELIMITER);
         count++;
      }

      return xmlMessage;
   }
}