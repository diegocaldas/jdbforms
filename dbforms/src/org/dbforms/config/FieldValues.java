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
import java.util.Iterator;

import org.dbforms.util.Util;



/**
 * New class to deal with a list of FieldValues. 
 * <br>
 * This class uses a delegate pattern: it delegates
 * everything to an Hashtable to do the necessary 
 * type transformations.
 * 
 * @author hkk
 */
public class FieldValues
{
   private Hashtable ht;


   /**
    * Creates a new FieldValues object.
    */
   public FieldValues()
   {
      ht = new Hashtable();
   }


   /**
    * Creates a new FieldValues object from an input array 
    * of FieldValue objects.
    * 
    * @param valueArr an array of FieldValue objects
    */
   public FieldValues(FieldValue[] valueArr)
   {
      ht = new Hashtable();

      for (int i = 0; i < valueArr.length; i++)
      {
         if (!Util.isNull(valueArr[i].getFieldValue()))
         {
            put(valueArr[i]);
         }
      }
   }


   /**
    * Get the FieldValue object having the input name.
    * 
    * @param name Dthe name of the FieldValue object to retrieve
    * @return the FieldValue object having the input name
    */
   public FieldValue get(String name)
   {
      return (FieldValue) ht.get(name);
   }


   /**
    * Put the input FieldName object into the
    * internal hash table.
    * 
    * @param value the FieldValue object to store
    */
   public void put(FieldValue value)
   {
      ht.put(value.getField().getName(), value);
   }


   /**
    *  Clear the internal hash table.
    */
   public void clear()
   {
      ht.clear();
   }


   /**
    * Get the Enumeration of the hash table keys.
    * 
    * @return the Enumeration of the hash table keys
    */
   public Iterator keys()
   {
      return ht.keySet().iterator();
   }

   /**
    * Get the Enumeration of the hash table keys.
    * 
    * @return the Enumeration of the hash table keys
    */
   public Iterator elements()
   {
      return ht.values().iterator();
   }


   /**
    * Get the size of the internal hash table.
    * 
    * @return the size of the internal hash table
    */
   public int size()
   {
      return ht.size();
   }


   /**
    *  Transform this object into a FieldValue array.
    * 
    * @return the FieldValue array representation of this object
    */
   public FieldValue[] toArr()
   {
      return (FieldValue[]) ht.values().toArray();
   }
   
	/**
	 *  Dump the input FieldValues object
	 *
	 * @param fValues the FieldValues object to dump
	 * @return the string representation of the FieldValues object
	 */
	public String toString()
	{
	  StringBuffer sb = new StringBuffer();
	  sb.append("FieldValues size: ")
		 .append(size())
		 .append("; elements are:\n");

	  Iterator keys = keys();
	  while (keys.hasNext())
	  {
		 String key = (String) keys.next();
		 FieldValue fValue = get(key);
       sb.append(key);
       sb.append(" - ");
       sb.append(fValue.toString());
       sb.append("\n");
	  }
	  return sb.toString();
	}
}