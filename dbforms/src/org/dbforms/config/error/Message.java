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
package org.dbforms.config.error;
import java.util.*;
import org.dbforms.util.*;
import org.apache.log4j.Category;



/****
 * <p>
 * This class represents a Message tag in dbforms-error.xml
 * </p>
 *
 */
public class Message
{
   static Category logCat = Category.getInstance(Message.class.getName()); // logging category for this class

   //------------------------ Properties ---------------------------------------------------------
   private String language; // Code used to specify a language for this message

   // May be converted to a real Locale object if need be...
   private String message; // the message-name, as provided in xml-error file

   /**
    * DOCUMENT ME!
    *
    * @param message DOCUMENT ME!
    */
   public void setMessage(String message)
   {
      this.message = message;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getMessage()
   {
      return message;
   }


   /**
    * DOCUMENT ME!
    *
    * @param language DOCUMENT ME!
    */
   public void setLanguage(String language)
   {
      this.language = language;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getLanguage()
   {
      return language;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String toString()
   {
      StringBuffer buf = new StringBuffer();
      buf.append("language=");
      buf.append(language);
      buf.append(" message=");
      buf.append(getMessage());

      return buf.toString();
   }
}
