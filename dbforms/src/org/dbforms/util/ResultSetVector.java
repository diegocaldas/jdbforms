/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * DbForms - a Rapid Application Development Framework
 * Copyright (C) 2001 Joachim Peer <j.peer@gmx.net> et al.
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

import java.util.*;
import java.sql.*;
import org.dbforms.Field;
import org.apache.log4j.Category;

/****
 *
 * <p>In version 0.5, this class held the actual data of a ResultSet (SELECT from a table).
 * The main weakness of this class was that it used too much memory and processor time:
 * <ul>
 * <li>1. every piece of data gots stored in an array (having a table with a million datasets
 * will mean running into trouble because all the memory gets allocated at one time.)
 * <li>2. every piece of data gots converted into a String and trim()ed
 * </ul>
 * </p>
 * <p>
 * since version 0.7 DbForms queries only those record from the database the user really
 * wants to see. this way you can query from a table with millions of records and you will
 * still have no memory problems, [exception: you choose count="*" in DbForms-tag :=) -> see
 * org.dbforms.taglib.DbFormTag]
 * </p>
 *
 * @author Joe Peer <j.peer@gmx.net>
 */

public class ResultSetVector extends Vector {

  static Category logCat = Category.getInstance(ResultSetVector.class.getName()); // logging category for this class

  private int pointer=0;
  private Vector selectFields;
  private Hashtable selectFieldsHashtable;

  Vector objectVector;

  public ResultSetVector() {
    super();
    objectVector = new Vector();
  }

  public ResultSetVector(ResultSet rs) throws java.sql.SQLException {
    this();

    ResultSetMetaData rsmd = rs.getMetaData();
    int columns = rsmd.getColumnCount();

    while(rs.next()) {

    	Object[] objectRow = new Object[columns];
    	String[] stringRow = new String[columns];

    	for(int i=0; i<columns; i++) {
		  Object tmpObj = rs.getObject(i+1);

		  logCat.debug("col="+(i+1)+", tmpObj="+tmpObj);

    	  if(tmpObj!=null) {
			 objectRow[i] = tmpObj;
			 stringRow[i] = tmpObj.toString();
    	  } else {
			 objectRow[i] = null; // #checkme: really necessary?
			 stringRow[i] = "";
		 }
    	}

    	this.addElement(stringRow);
    	objectVector.addElement(objectRow);
    }

  }


  public ResultSetVector(Vector selectFields, ResultSet rs) throws java.sql.SQLException {
    this(rs);
    this.selectFields = selectFields;
    this.setupSelectFieldsHashtable();
  }

  private void setupSelectFieldsHashtable() {
	if(selectFields==null) {
		logCat.warn("selectField is null");
		return;
	}

	selectFieldsHashtable = new Hashtable();

	for(int i=0; i<selectFields.size(); i++) {
		Field f = (Field) selectFields.elementAt(i);
		selectFieldsHashtable.put(f.getName(), f);
	}
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
		if(isPointerLegal(pointer)) {
			return (String[]) elementAt(pointer);
		}
		else
			return null;
  }

  public Object[] getCurrentRowAsObjects() {
		if(isPointerLegal(pointer)) {
			return (Object[]) objectVector.elementAt(pointer);
		}
		else
			return null;
  }

  public String getField(int i) {
		if(isPointerLegal(pointer))
			return ((String[]) elementAt(pointer))[i];
		else
			return null;
  }


  public String getField(String fieldName) {
		if(isPointerLegal(pointer)) {
			Field f = (Field) selectFieldsHashtable.get(fieldName);
			int fieldIndex = selectFields.indexOf(f);
			return ((String[]) elementAt(pointer))[fieldIndex];
		} else
			return null;
  }

  public Object getFieldAsObject(int i) {
		if(isPointerLegal(pointer))
			return ((Object[]) elementAt(pointer))[i];
		else
			return null;
  }


  public Object getFieldAsObject(String fieldName) {
		if(isPointerLegal(pointer)) {
			Field f = (Field) selectFieldsHashtable.get(fieldName);
			int fieldIndex = selectFields.indexOf(f);
			return ((Object[]) objectVector.elementAt(pointer))[fieldIndex];
		} else
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
			logCat.info("flipping "+vSize+" elements!");
			for(int i=1; i<vSize; i++) {
				Object o = this.elementAt(i);

				//logCat.debug("o="+o);

				this.remove(i);
				this.insertElementAt(o,0);

				// we must flip the duplicate vector, too
				o=objectVector.elementAt(i);
				objectVector.remove(i);
				objectVector.insertElementAt(o,0);
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