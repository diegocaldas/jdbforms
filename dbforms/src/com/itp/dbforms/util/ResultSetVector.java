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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.itp.dbforms.util;

import java.util.*;
import java.sql.*;
import com.itp.dbforms.Field;
import org.apache.log4j.Category;

/****
 *
 * <p>In version 0.5, this class holds the actual data of a ResultSet (SELECT from a table).
 * the goal for the release 1.0 is to abstract these methods into an _interface_ which can
 * be implemented by various - more efficient - classes. </p>
 *
 * <p>The main weakness of this class is that it uses too much memory and processor time:
 * <ul>
 * <li>1. every piece of data gets stored in an array (having a table with a million datasets
 * will mean running into trouble because all the memory gets allocated at one time.)
 * <li>2. every piece of data gets converted into a String and trim()ed
 * </ul>
 * </p>
 *
 * @author Joe Peer <joepeer@excite.com>
 */

public class ResultSetVector extends Vector {

  static Category logCat = Category.getInstance(ResultSetVector.class.getName()); // logging category for this class

  private int pointer=0;
  private Vector selectFields;

  public ResultSetVector() {
    super();
  }

  public ResultSetVector(ResultSet rs) throws java.sql.SQLException {

    ResultSetMetaData rsmd = rs.getMetaData();
    int columns = rsmd.getColumnCount();

    while(rs.next()) {

    	String[] tmpString = new String[columns];
    	for(int i=0; i<columns; i++) {
		  String tmp = rs.getString(i+1);
    	  if(tmp!=null) tmpString[i] = tmp.trim();
    	  else tmpString[i] = "";
    	}

    	this.addElement(tmpString);
    }

  }


  public ResultSetVector(Vector selectFields, ResultSet rs) throws java.sql.SQLException {
    this(rs);
    this.selectFields = selectFields;
  }


  public void setPointer(int pointer) {
	this.pointer = pointer;
  }

  public int getPointer() {
	return pointer;
  }

  public int increasePointer() {

	pointer++;

	if(pointer<this.size())
	  return pointer;
	else
	  return -1;
  }
/*
  public int increasePointerBy(int stepWidth) {
	pointer += stepWidth;
	if(pointer < this.size()) {

		return pointer;
	}
	else
	  return -1;
  }

  public int declinePointerBy(int stepWidth) {
		pointer -= stepWidth;

		if(pointer >= 0) {
			 return pointer;
		}
		else
		   return -1;
  }
*/

  public int increasePointerBy(int stepWidth) {
		pointer += stepWidth;
		if(pointer < this.size()) {
			return pointer;
		}
		else {
			pointer = this.size()-1;
		  return -1;
		}
  }

  public int declinePointerBy(int stepWidth) {
		pointer -= stepWidth;
		if(pointer >= 0) {
			 return pointer;
		}
		else {
			 pointer = this.size()-1;
		   return -1;
	 	}
  }

  public int declinePointer() {

	pointer--;

	if(pointer >= 0)
	  return pointer;
	else
	  return -1;
  }

  public boolean isPointerLegal(int p) {
    return (p>=0 && p<size());
  }

  public boolean isCurrentPointerLegal() {
    return (pointer>=0 && pointer<size());
  }

  public String[] getCurrentRow() {
		if(isPointerLegal(pointer))
			return (String[]) elementAt(pointer);
		else
			return null;
  }

  public String getField(int i) {
		if(isPointerLegal(pointer))
			return ((String[]) elementAt(pointer))[i];
		else
			return null;
  }


	public int getIndexOfField(Field f) {
		if(selectFields==null) {
		  throw new IllegalArgumentException("no field vector was provided to this result");
		}
		else
		  return selectFields.indexOf(f);
	}

	public void flip() {
		int vSize = this.size();

		if (vSize>1) {
			logCat.info("flipping "+vSize+" elements");
			for(int i=1; i<vSize; i++) {
				Object o = this.elementAt(i);
				this.remove(i);
				this.insertElementAt(o,0);
			}

		}

	}


	//------------------------------------------------------------
	// convenience method

	public static boolean isEmptyOrNull(Vector v) {
		return v==null || v.size()==0;
	}


  // used for providing TEI

  public Hashtable getCurrentRowAsHashtable() {

		if(selectFields==null) {
		  throw new IllegalArgumentException("no field vector was provided to this result");
		}

		Hashtable ht = new Hashtable();

		for(int i=0; i<selectFields.size(); i++) {
			Field f = (Field) selectFields.elementAt(i);
			ht.put(f.getName(), getCurrentRow()[i]);
		}

	  return ht;

	}

}