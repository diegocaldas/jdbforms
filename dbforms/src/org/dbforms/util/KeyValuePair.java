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

/**
 *  this class was introduced in DbForms 0.9
 *  it is used for holding simple data
 *  for example in EmbeddedData - Classes
 */
package org.dbforms.util;

/**
 *  Description of the Class
 *
 * @author  foxat
 * @created  3 dicembre 2002
 */
public class KeyValuePair
{
   private String key;
   private String value;

   /**
    *  Creates a new KeyValuePair object.
    */
   public KeyValuePair()
   {
   }


   /**
    * Creates a new KeyValuePair object.
    *
    * @param  key  the key value
    * @param  value the value related to the key
    */
   public KeyValuePair(String key, String value)
   {
      this.key      = key;
      this.value    = value;
   }

   /**
    *  Gets the key attribute of the KeyValuePair object
    *
    * @return  The key value
    */
   public String getKey()
   {
      return key;
   }


   /**
    *  Gets the value attribute of the KeyValuePair object
    *
    * @return  The value value
    */
   public String getValue()
   {
      return value;
   }


   /**
    *  Sets the key attribute of the KeyValuePair object
    *
    * @param  key The new key value
    */
   public void setKey(String key)
   {
      this.key = key;
   }


   /**
    *  Sets the value attribute of the KeyValuePair object
    *
    * @param  value The new value value
    */
   public void setValue(String value)
   {
      this.value = value;
   }
}
