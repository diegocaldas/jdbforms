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
import java.util.Hashtable;
import java.util.Enumeration;



/**
 * New class to deal with a list of FieldValues. Delagate pattern: Delegate
 * everything to an Hashtable Do the necessary type transformations.
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
    * DOCUMENT ME!
    * 
    * @param key DOCUMENT ME!
    * 
    * @return DOCUMENT ME!
    */
   public FieldValue get(String key)
   {
      return (FieldValue) ht.get(key);
   }


   /**
    * DOCUMENT ME!
    * 
    * @param key DOCUMENT ME!
    * @param value DOCUMENT ME!
    */
   public void put(String key, FieldValue value)
   {
      ht.put(key, value);
   }


   /**
    * DOCUMENT ME!
    */
   public void clear()
   {
      ht.clear();
   }


   /**
    * DOCUMENT ME!
    * 
    * @return DOCUMENT ME!
    */
   public Enumeration keys()
   {
      return ht.keys();
   }


   /**
    * DOCUMENT ME!
    * 
    * @return DOCUMENT ME!
    */
   public int size()
   {
      return ht.size();
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public FieldValue[] toArr()
   {
      FieldValue[] result = new FieldValue[size()];
      Enumeration  enum = ht.elements();
      int          cnt  = 0;
      while (enum.hasMoreElements())
      {
         result[cnt] = (FieldValue) enum.nextElement();
         cnt++;
      }
      return result;
   }
}