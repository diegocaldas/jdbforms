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
package org.dbforms.util.external;


import java.util.Enumeration;

import org.dbforms.util.FieldValue;
import org.dbforms.util.FieldValues;


/**
 * Data Dumper debugger class.
 * <br>
 * Useful to dump DbForms structures.
 *
 * @author Luca Fossato
 */
public class DumperUtil
{
  /**
   *  Dump the input FieldValue array
   °  TO BE FINISHED !!!!
   * 
   * @param fieldValues the fieldValues array
   * @return the string representation of the FieldValues object
   */
  public static String dumpFieldValues(FieldValue[] fieldValues)
  {
    StringBuffer sb = new StringBuffer();
    sb.append("FieldValue array size: ")
      .append(fieldValues.length)
      .append("; elements are:\n");

    for (int i = 0; i < fieldValues.length; i++)
    {
      FieldValue fieldValue = fieldValues[i];
      sb.append(dumpFieldValue(fieldValue));
    }

    // TO FINISH !!!
    return null;
  }


  /**
   *  Dump the input FieldValues object
   *
   * @param fValues the FieldValues object to dump
   * @return the string representation of the FieldValues object
   */
  public static String dumpFieldValues(FieldValues fValues)
  {
    StringBuffer sb = new StringBuffer();
    sb.append("FieldValues size: ")
      .append(fValues.size())
      .append("; elements are:\n");

    Enumeration keys = fValues.keys();

    while (keys.hasMoreElements())
    {
      String key = (String) keys.nextElement();
      FieldValue fValue = fValues.get(key);
      String fieldName;
      sb.append(dumpFieldValue(fValue));
    }

    return sb.toString();
  }


  
  
  /**
   *  PRIVATE METHODS HERE
   */
  
  
  /**
   *  Dump the input FieldValue object
   * 
   * @param fieldValue the FieldValue object to dump
   * @return the StringBuffer with the FieldValue data
   */
  private static StringBuffer dumpFieldValue(FieldValue fieldValue)
  {
    StringBuffer sb  = new StringBuffer();
    String fieldName = fieldValue.getField().getName();

    sb.append(addSpaces(2))
      .append("field [")
      .append(fieldName)
      .append("] has value, oldvalue [")
      .append(fieldValue.getFieldValue())
      .append(", ")
      .append(fieldValue.getOldValue())
      .append("]\n");
      
    return sb;
  }


  /**
   *  Return a String containin blank chars.
   *
   * @param len number of space characters to add
   * @return a String containin blank chars whose length is equal
   *         to $len
   */
  private static String addSpaces(int len)
  {
    String res = "";

    for (int i = 0; i < len; i++)
    {
      res += " ";
    }

    return res;
  }
}
