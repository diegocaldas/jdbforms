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
package org.dbforms.config;

import java.util.Hashtable;

import org.apache.log4j.Category;



/************************************************************************
 *
 * This class represents an error tag in dbforms-errors.xml
 * 
 * @author unknown
 *
 ************************************************************************/
public class Error
{
   private static Category logCat = Category.getInstance(Error.class.getName());

   // logging category for this class
   //------------------------ Properties ---------------------------------------------------------
   private String    id; // the id of this error
   private String    type; // the type of error
   private Hashtable messages;

   /**
    * Creates a new Error object.
    */
   public Error()
   {
      messages = new Hashtable();
   }

   /**
    * DOCUMENT ME!
    *
    * @param id DOCUMENT ME!
    */
   public void setId(String id)
   {
      this.id = id;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getId()
   {
      return id;
   }


   /**
    * DOCUMENT ME!
    *
    * @param msgs DOCUMENT ME!
    */
   public void setMessages(Hashtable msgs)
   {
      this.messages = msgs;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public Hashtable getMessages()
   {
      return messages;
   }


   /**
    * DOCUMENT ME!
    *
    * @param language DOCUMENT ME!
    * @param message DOCUMENT ME!
    */
   public void setMessage(String language, String message)
   {
      this.messages.put(language, message);
   }


   /**
    * DOCUMENT ME!
    *
    * @param language DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getMessage(String language)
   {
      return (String) messages.get(language);
   }


   /**
    * DOCUMENT ME!
    *
    * @param t DOCUMENT ME!
    */
   public void setType(String t)
   {
      this.type = t;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getType()
   {
      return type;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String toString()
   {
      StringBuffer buf = new StringBuffer();
      buf.append("id=");
      buf.append(this.getId());
      buf.append(" type=");
      buf.append(this.getType());

      return buf.toString();
   }


   /**
   * adds a Message-Object to this error
   * and puts it into the datastructure for further references
   * (this method gets called from DbFormsError)
   * 
   * @param message message to add
   */
   public void addMessage(Message message)
   {
      messages.put(message.getLanguage(), message.getMessage());
      logCat.info("Language " + message.getLanguage() + " Message "
         + message.getMessage());
   }
}
