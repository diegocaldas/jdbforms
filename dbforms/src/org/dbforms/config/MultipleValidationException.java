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

import java.util.Vector;




/**
 *  This exception may be thrown by user code
 *  in classes implementing the interceptor interface
 *  Allows developers to define more then one error - to be
 *  used in conjunction with the XML errors mechanism
 */
public class MultipleValidationException extends ValidationException
{
   private Vector messages = null;

	public MultipleValidationException(String message)
	{
		super();
		addMessage(message);
	}
   /**
    * Creates a new MultipleValidationException object.
    *
    * @param messages DOCUMENT ME!
    */
   public MultipleValidationException(Vector messages)
   {
	  super();
      this.setMessages(messages);
   }

   /**
    * Gets the messages
    * @return Returns a Vector
    */
   public Vector getMessages()
   {
      return messages;
   }


   /**
    * Sets the messages
    * @param messages The messages to set
    */
   public void setMessages(Vector messages)
   {
      this.messages = messages;
   }
   
   public void addMessage(String message) {
      if (messages == null) 
         messages = new Vector();
      messages.add(message);
   }

   /**
    *  Returns the detail message string of this throwable.
    *  <br>
    *  Override the <code>getMessage</code> method of the <code>Throwable</code>
    *  class.
    *  <br>
    *  Note: the Throwable class' <code>toString</code> method calls
    *  <code>getLocalizedMessage()</code> to get the string representation of the
    *  detaile error message. The original <code>getLocalizedMessage()</code>
    *  implementation calls <code>getMessage()</code>.
    *  <br>
    *  Overriding <code>getMessage</code> to get the multiple messages from this
    *  exception class let Log4j category classes log all the error messages.
    *  (fossato, 2002.11.29)
    *
    * @return  the detail message string of this <tt>Throwable</tt> instance
    */
   public String getMessage()
   {
      StringBuffer sb = new StringBuffer();

      if (messages != null)
      {
         for (int i = 0; i < messages.size(); i++)
         {
            Object o = messages.elementAt(i);

            if (o != null)
            {
               sb.append(o.toString()).append("\n");
            }
         }
      }

      return sb.toString();
   }
}
