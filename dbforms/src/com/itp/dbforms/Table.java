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

package com.itp.dbforms;

import java.util.*;
import java.sql.*;
import javax.servlet.http.HttpServletRequest;

import com.itp.dbforms.util.*;
import com.itp.dbforms.event.*;
import com.itp.dbforms.taglib.DbBaseHandlerTag; // for searchMode - constants #checke: how about a constants - class ?
import org.apache.log4j.Category;

/****
 * <p>
 * This class represents a table tag in dbforms-config.xml (dbforms config xml file)
 * </p>
 *
 * <p>
 * it also defines a lot of methods for preparing and actually performing
 * operations (queries) on the table
 * </p>
 *
 * @author Joe Peer <joepeer@excite.com>
 */

public class Table {

    static Category logCat = Category.getInstance(Table.class.getName()); // logging category for this class

	public static int GET_EQUAL = 0;
	public static int GET_EQUAL_OR_GREATER = 1;

	private int id;					// id of this table (generated by DbFormsConfig when parsing dbforms-config.xml)
  	private Vector fields;	// the Field-Objects this table constists of
  	private Vector key;			// subset of "fields", containting those keys which represent keys
  	private String name;		// the name of the Table
	private Hashtable fieldNameHash; // structure for quick acessing of fields "by name"
	private Vector diskblobs;				 // subset of "fields", containting those keys which represent DISKBLOBs (wondering about that term? -> see docu)

	// instance variables concerned with the ORDERING/SORTING characterstics of that table (ordering and sorting is used synonym here)
	private String orderBy;						 // the order-by clause, as specified in dbforms-config.xml (optional!)
	private FieldValue[] defaultOrder; // datastructure generated from "orderBy".
	private Vector defaultOrderFields; // contains Field-Objects which are referenced in the orderBy-string

	private GrantedPrivileges grantedPrivileges = null; // access control list for this object (if null, then its open to all users for all operations). defined in dbforms-config.xml

	private Vector interceptors; // application hookups

  public Table() {
		fields = new Vector();
		key = new Vector();
		fieldNameHash = new Hashtable();
		diskblobs = new Vector();
		interceptors = new Vector();
  }

	/**
	 * returns object containing info about rights mapped to user-roles. (context: this table object!)
   */
	public GrantedPrivileges getGrantedPrivileges() {
		return grantedPrivileges;
	}

  /**
   * checks if there exists a granted-privileges object and if so it queries if access/operation is possible
   */
  public boolean hasUserPrivileg(HttpServletRequest request, int privileg) {
		return (grantedPrivileges==null) ? true : grantedPrivileges.hasUserPrivileg(request, privileg);
	}



	/**
	 * set GrantedPrivileges, if defined in dbforms-config-xml
	 * (this method gets called from XML-digester)
   */
  public void setGrantedPrivileges(GrantedPrivileges grantedPrivileges) {
		this.grantedPrivileges = grantedPrivileges;
	}


	public void addInterceptor(Interceptor interceptor) {
		interceptors.addElement(interceptor);
	}

	public Vector getInterceptors() {
		return interceptors;
	}

	public boolean hasInterceptors() {
		return interceptors!=null && interceptors.size()>0;
	}

	/**
	 * adds a Field-Object to this table
	 * and puts it into othere datastructure for further references
	 * (this method gets called from DbFormsConfig)
	 */
  public void addField(Field field) {

		field.setId(fields.size());
		fields.addElement(field);
		if(field.isKey()) { // if the field is (part of) the key
			logCat.info("wow - field "+field.getName()+" is a key");
			key.addElement(field);
		} else logCat.info("field "+field.getName()+" is NO key");

		// for quicker lookup by name:
		fieldNameHash.put(field.getName(), field);

		// for quicker check for diskblobs
		if(field.getType() == FieldTypes.DISKBLOB) {
			diskblobs.addElement(field);
		}
  }

	/**
	 * returns the Field-Objet with specified id
	 *
	 * @param fieldId The id of the field to be returned
   */
	public Field getField(int fieldId) {
		return (Field) fields.elementAt(fieldId);
	}

	/**
	 * sets the ID of this table
	 * (this method gets called from DbFormsConfig)
   */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * returns ID of this table
	 */
	public int getId() {
		return id;
	}

	/**
	 * sets the name of the table
	 * (this method gets called from XML-digester)
	 */
  public void setName(String name) {
    this.name = name;
  }

	/**
	 * returns name of the table
   */
  public String getName() {
		return name;
	}

	/**
	 * returns the vector of fields this table constists of
	 */
  public Vector getFields() {
		return fields;
	}

	/**
	 * returns the field-objects as specified by name (or null if no field with
	 * the specified name exists in this table)
	 * @param name The name of the field
	 */
	public Field getFieldByName(String name) {
		return (Field) fieldNameHash.get(name);
	}

	/**
	 * returns the key of this table (consisting of Field-Objects representing key-fields)
	 */
  public Vector getKey() {
		return key;
	}

	/**
	 * determinates if this table contains a diskblob field.
	 * (this method is used by DeleteEvent which needs to delete files referenced
	 * by a diskblob field)
	 */
	public boolean containsDiskblob() {
		return diskblobs.size() > 0;
	}

	/**
	 * returns a Vector of Field-Objects representing fields of type "DISKBLOB"
	 */
	public Vector getDiskblobs() {
		return diskblobs;
	}

	/**
	 * this method generated a datastructure holding sorting information from "orderBy".clause in XML-config
	 */
	public void initDefaultOrder() {

		if(orderBy==null) { // if developer specified no orderBy in XML, then we set the KEYs as DEFAULT ORDER
			initDefaultOrderFromKeys();
		  return;
		}

		// build the datastructure, containing Fields, and infos about sort
		defaultOrder = this.createOrderFieldValues(orderBy, null);

		// building a list of the fields contained in the defaultOrder structure
		defaultOrderFields = new Vector();
		for(int i=0; i<defaultOrder.length; i++)
			defaultOrderFields.addElement(defaultOrder[i].getField());

		logCat.info("Table.initDefaultOrder done.");
	}

	/**
   * this method generated a datastructure holding sorting information from "orderBy"
   * only the keys are used as order criteria. by default all ascending
   * (check SQL spec + docu)
   */
	public void initDefaultOrderFromKeys() {

		defaultOrder = new FieldValue[key.size()];
		defaultOrderFields = new Vector();

		for(int i=0; i<this.key.size(); i++) {
			Field keyField = (Field) key.elementAt(i);
			defaultOrder[i] = new FieldValue();
			defaultOrder[i].setField(keyField);
			defaultOrderFields.addElement(keyField);
		}

		logCat.info("Table.initDefaultOrderfromKey done.");
	}

	/**
	 * sets a default-orderBy clause from xml config
	 * (this method gets called from XML-digester)
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

  /**
   * return default-orderBy clause from xml config or null if not specified
   */
	public String getOrderBy() {
		return orderBy;
	}

	/**
   * return the datastructure containing info about the default sorting behavior of this table
   */
	public FieldValue[] getDefaultOrder() {
		return defaultOrder;
	}

  /**
   * return a list of the fields contained in the defaultOrder structure
   */
	public Vector getDefaultOrderFields() {
		return defaultOrderFields;
	}


  /**
   *	Prepares the Querystring for the select statement
   *	if the statement is for a sub-form (=> doConstrainedSelect),
   *	we set some place holders for correct mapping
   *
   *	@param fieldsToSelect - vector of fields to be selected
   *	@param childFieldValues - horziontal selection: filetering rows according to the value definitions in childFieldValues. If childFieldValues is null, no horizontal selection is made.
   */
  private String getSelectQuery(Vector fieldsToSelect, FieldValue[] fvEqual, FieldValue[] fvOrder, int compareMode) {
	  StringBuffer buf = new StringBuffer();
	  buf.append("SELECT ");

	  int fieldsToSelectSize = fieldsToSelect.size(); // #checkme: do i need this when using Hotspot ?

		// we scroll through vector directly (no enumeration!) to maintain correct order of elements
	  for(int i=0; i<fieldsToSelectSize; i++) {
			Field f = (Field) fieldsToSelect.elementAt(i);
	 	  buf.append( f.getName() );
	    if(i<fieldsToSelect.size()-1) buf.append(", ");
	  }
	  buf.append(" FROM ");
		buf.append(this.name);

		boolean firstTermExists = false;

		if(fvEqual != null && fvEqual.length > 0) {

			buf.append(" WHERE ( ");

			// check if the fieldvalues contain _search_ information
			if(fvEqual[0].getSearchMode()==DbBaseHandlerTag.SEARCHMODE_NONE) {
				buf.append(FieldValue.getWhereClause(fvEqual));
			} else {
				buf.append(FieldValue.getWhereEqualsSearchClause(fvEqual));
			}

			buf.append(" ) ");

			firstTermExists = true;
		}

		if(fvOrder != null && fvOrder.length > 0) {

			if(compareMode!=FieldValue.COMPARE_NONE) {

				buf.append( firstTermExists ? " AND ( " : " WHERE ( ");
				buf.append(FieldValue.getWhereAfterClause(fvOrder, compareMode));
				buf.append(" ) ");

			}

			buf.append(" ORDER BY ");
			for(int i=0; i<fvOrder.length; i++) {
				buf.append(fvOrder[i].getOrderClause());
				if(i<fvOrder.length - 1) buf.append(",");
			}
		}



		logCat.info("doSelect:"+buf.toString());
		return buf.toString();
  }

  /**
   *	Prepares SELECT-Statement
   *	if the statement is for a sub-form (=> doConstrainedSelect),
   *	we set some values for correct mapping
   *
   *	!!@param fieldsToSelect - vector of fields to be selected
   *	1!@param childFieldValues - horziontal selection: filetering rows according to the value definitions in childFieldValues. If childFieldValues is null, no horizontal selection is made.
   *  @param connection - the active db connection to use
   */
  public ResultSet getDoSelectResultSet(Vector fieldsToSelect, FieldValue[] fvEqual, FieldValue[] fvOrder, int compareMode, int maxRows, Connection con)
  throws SQLException {

		PreparedStatement ps = con.prepareStatement(getSelectQuery(fieldsToSelect, fvEqual, fvOrder, compareMode));
		ps.setMaxRows(maxRows); // important when quering huge tables

		int curCol = 1;

		if(fvEqual != null && fvEqual.length > 0) {
			curCol = FieldValue.populateWhereEqualsClause(fvEqual, ps, curCol);
		}

		if(compareMode!=FieldValue.COMPARE_NONE && fvOrder != null && fvOrder.length > 0) {
			FieldValue.populateWhereAfterClause(fvOrder, ps, curCol);
		}

		return ps.executeQuery();
  }


	/**
 	 *	perform select query
   *
   *	@param fieldsToSelect - vector of fields to be selected
   *	@param childFieldValues - horziontal selection: filetering rows according to the value definitions in childFieldValues. If childFieldValues is null, no horizontal selection is made.
   *  @param maxRows - how many rows should be stored in the resultSet (zero means unlimited)
   *  @param conditionAttribute - this attribute gets applied only if childFieldValues not null; "0" means normal (query rows with EQUAL fields), "1" means quering rows with EQUAL-OR-GREATER logic
   *  @param connection - the active db connection to use
   */
	public ResultSetVector doConstrainedSelect(Vector fieldsToSelect, FieldValue[] fvEqual, FieldValue[] vfOrder, int compareMode, int maxRows, Connection con)
	throws SQLException {
		ResultSetVector result = new ResultSetVector(fieldsToSelect, getDoSelectResultSet(fieldsToSelect, fvEqual, vfOrder, compareMode, maxRows, con) );
		logCat.info("rsv size="+result.size());
		return result;
	}


  private String createToken(Field field, String fieldValue) {

	  StringBuffer buf = new StringBuffer();
	  buf.append(field.getId());
	  buf.append(":");
	  buf.append(fieldValue.length());
	  buf.append(":");
	  buf.append(fieldValue);
	  return buf.toString();

  }


  /**
   * builds a "position- string" representing the values of the current row in the given
   * ResultSetVector..
   *
   * not all field-values get explicitl listed in this string. only fields important
   * for navigation and sorting are listed.
   *
   * position strings are used as request parameters allowing the framework to keep track
   * of the position the user comes from or goes to.
   *
   * look into com.itp.tablib.DbFormTag for better understanding
   *
   * changed 0-04-2001 by joe
   * #note: enhanced algorithm since version 0.9!
   */
  public String getPositionString(ResultSetVector rsv) {
		StringBuffer buf = new StringBuffer();

		if(ResultSetVector.isEmptyOrNull(rsv)) return null;

		String[] currentRow = rsv.getCurrentRow();
		if(currentRow==null) return null;;

		int cnt =0 ;
		for(int i=0; i<fields.size(); i++) {
			Field f = (Field) fields.elementAt(i);

			if(f.isKey() || f.isFieldSortable()) {

				//bugfix 30-04-2001:
				//
				//we want the "-" sign only if a token was already written out
				//
				if(cnt>0) buf.append("-"); // control byte
				buf.append( createToken(f, currentRow[f.getId()]) );
				cnt++;

			}

		}

		return buf.toString();
	}






	/**
	 * does basically the same as getPositionString but only for key-fields
	 * #checkme: could be merged with getPositionString
	 * #fixme: replace seperator-based tokenization by better algoithm!
	 */
	public String getKeyPositionString(ResultSetVector rsv) {
		StringBuffer buf = new StringBuffer();

		if(ResultSetVector.isEmptyOrNull(rsv)) return null;

		String[] currentRow = rsv.getCurrentRow();
		if(currentRow==null) return null;;

		for(int i=0; i<key.size(); i++) {
			Field f = (Field) key.elementAt(i);
			/*
			int keyIndex = (f).getId();
			buf.append(currentRow[keyIndex]);*/

			if(i>0) buf.append("-"); // control byte
			buf.append( createToken(f, currentRow[f.getId()]) );


		}

		return buf.toString();
	}


	public String getKeyPositionString(Hashtable fvHT) {

		StringBuffer buf = new StringBuffer();

		for(int i=0; i<key.size(); i++) {
			Field f = (Field) key.elementAt(i);
			String value = (String) fvHT.get(f);

			if(value == null)
			  throw new IllegalArgumentException("wrong fields provided");

			if(i>0) buf.append("-"); // control byte
			buf.append( createToken(f,value) );


		}

		return buf.toString();
	}


	/**
	used for instance by goto with prefix
	*/

	public String getPositionStringFromFieldAndValueHt(Hashtable ht) {
		StringBuffer buf = new StringBuffer();

		int cnt = 0;
		Enumeration enum = ht.keys();
		while(enum.hasMoreElements()) {
			String fieldName = (String) enum.nextElement();

			Field aField = getFieldByName(fieldName);

			if(aField != null) {
				String fieldValue = (String) ht.get(fieldName);

				if(cnt>0) buf.append('-'); // control byte
				buf.append(createToken(aField,fieldValue));
				cnt++;


			} else
			  logCat.error("provided goto field " + fieldName + " not found!");
		}

		return buf.toString();
	}

/*
  public FieldValue[]  getFieldValuesFromPosition(String position) {
	 Hashtable ht = getFieldValuesFromPosition(position);

	 Enumeration enum = ht.elements();
	 for()  {

     }

  }
*/


  /**
   * this method parses a position string and build a data structure
   * representing the values of the fields decoded from the position.
   *
   * #fixme: replace seperator-based tokenization by better algoithm!
   */
  public Hashtable getFieldValuesFromPositionAsHt(String position) {

	if(position==null) return null;
	position = position.trim();

    Hashtable result = new Hashtable();

	int startIndex = 0;
	boolean endOfString = false;

	// looping through the string
	while(!endOfString) {

		int firstColon = position.indexOf(':', startIndex);
		int secondColon = position.indexOf(':', firstColon+1);

		String fieldIdStr = position.substring(startIndex, firstColon);
		int fieldId = Integer.parseInt(fieldIdStr);

		String valueLengthStr = position.substring(firstColon+1, secondColon);
		int valueLength = Integer.parseInt(valueLengthStr);

		int controlIndex = secondColon+1+valueLength;

    	String valueStr = position.substring(secondColon+1, controlIndex);

    	FieldValue fv = new FieldValue();
    	fv.setField( getField(fieldId) );
    	fv.setFieldValue(valueStr);

		result.put(new Integer(fieldId), fv);

		if(controlIndex == position.length()) {
		  endOfString = true;
		} else if(controlIndex > position.length()) {
		  logCat.warn("Controlbyte wrong but continuing execution");
  		  endOfString = true;
		} else {
		  char controlByte = position.charAt(controlIndex);
		  if(controlByte != '-') {
			  logCat.error("Controlbyte wrong, abandon execution");
			  throw new IllegalArgumentException();
		  }
		  startIndex = controlIndex + 1;

		  if(position.length() == startIndex) endOfString = true;


		}

	}

	return result;
  }



  /**
  in version 0.9 this method moved from FieldValue.fillWithValues to Table.fillWithValues

   */
  public void fillWithValues(FieldValue[] orderConstraint, String aPosition) {

		Hashtable ht = getFieldValuesFromPositionAsHt(aPosition);

		logCat.info("*** parsing through: "+aPosition);

		// then we copy some of those values into the orderConstraint
		for(int i=0; i<orderConstraint.length; i++) {
			logCat.info("***"+orderConstraint[i].getField().getId()+"***");
			//orderConstraint[i].setFieldValue( values[orderConstraint[i].getField().getId()] );

			Integer aFieldId = new Integer(orderConstraint[i].getField().getId());
			FieldValue aFieldValue = (FieldValue) ht.get(aFieldId);
			if(aFieldValue != null)
				orderConstraint[i].setFieldValue( aFieldValue.getFieldValue() );
			else
				logCat.warn("position entry has null value:"+orderConstraint[i].getField().getName());

		}

	}



	// ----------------- some convenience methods ---------------------------------------------


	/**
	 * generates a part of the SQL where clause needed to select a distinguished row form the table
	 * this is done by querying for KEY VALUES !
	 */
	public String getWhereClauseForPS() {
			StringBuffer buf = new StringBuffer();
			int cnt = this.getKey().size();

			for(int i=0; i<cnt; i++) {
				Field keyField = (Field) this.getKey().elementAt(i); // get the name of the encoded key field
				buf.append(keyField.getName());
				buf.append(" = ?");
				if(i<cnt-1) buf.append(" AND ");
			}

			return buf.toString();
	}

	/**
	 * POPULATES a part of the SQL where clause needed to select a distinguished row form the table
	 * using values endcoded in a string
	 * #fixme: replace seperator-based tokenization by better algoithm!
   */
	public void populateWhereClauseForPS(String keyValuesStr, PreparedStatement ps, int startColumn)
	throws SQLException {
				int col = startColumn;
				// then we list the values of the key-fields, so that the WHERE clause matches the right dataset
				Hashtable keyValuesHt = getFieldValuesFromPositionAsHt(keyValuesStr);

				int keyLength = this.getKey().size();
				for(int i=0; i<keyLength; i++) {
					Field curField = (Field) this.getKey().elementAt(i);

					FieldValue aFieldValue = (FieldValue) keyValuesHt.get( new Integer(curField.getId()) );
					String valueStr = aFieldValue.getFieldValue();

					SqlUtil.fillPreparedStatement(ps, col, valueStr, curField.getType());
					col ++;
				}
 }

  /**
   * generates part of a field list for a  SQL SELECT clause selecting the DISKBLOB
   * fields from a table
   * (used by DeleteEvent to maintain data consistence)
   */
  public String getDisblobSelectClause() {
			StringBuffer buf = new StringBuffer();
			int cnt =  diskblobs.size();

			for(int i=0; i<cnt; i++) {
				Field diskblobField = (Field) diskblobs.elementAt(i); // get the name of the encoded key field
				buf.append(diskblobField.getName());

				if(i<cnt-1) buf.append(", ");
			}

			return buf.toString();
	}



	private Vector createOrderFVFromAttribute(String order) {
		Vector result = new Vector();

		StringTokenizer st = new StringTokenizer(order, ",");
		while(st.hasMoreTokens()) {

			String token = st.nextToken();
			logCat.info("token="+token);

			FieldValue fv = new FieldValue();
			boolean sortDirection = Field.ORDER_ASCENDING; // we propose the default

			int index = token.indexOf("ASC");
			if(index == -1) {
				index = token.indexOf("DESC"); // if not default...
				if(index!=-1)
				  sortDirection = Field.ORDER_DESCENDING; // ... we set desc.
			}

			String fieldName;
			if(index == -1)  {
				fieldName = token.trim();
			} else {
				fieldName =  token.substring(0, index).trim();
			}

			fv.setField(this.getFieldByName(fieldName));
			fv.setSortDirection(sortDirection);

			logCat.info("Field '"+fieldName+"' is ordered in mode:"+sortDirection);
			result.addElement(fv);
		}

		return result;
	}


	private Vector createOrderFVFromRequest(HttpServletRequest request, String paramStub, Vector sortFields) {
		Vector result = new Vector();
		int fieldIndex = paramStub.length()+1; // "sort_1" -> fieldindex= 8 (length of paramStub "order_1" is 7)

		for(int i=0; i<sortFields.size(); i++) {

			String dataParam = (String) sortFields.elementAt(i);
			int fieldId = Integer.parseInt(dataParam.substring(fieldIndex));
			String sortState = ParseUtil.getParameter(request, dataParam);

			logCat.info("### dataparam="+dataParam);
			logCat.info("### fieldId="+fieldId);
			logCat.info("### sortState="+sortState);

			if(sortState.equalsIgnoreCase("asc") || sortState.equalsIgnoreCase("desc")) {
				boolean sortDirection = sortState.equalsIgnoreCase("asc") ? Field.ORDER_ASCENDING : Field.ORDER_DESCENDING;
				FieldValue fv = new FieldValue();
				fv.setField(this.getField(fieldId));
				fv.setSortDirection(sortDirection);
				result.addElement(fv);
			}
		}

		return result;
	}

	/**
	@param order - a String from JSP/XML. provided by the user in SQL-Style:

	Column ["ASC" | "DESC"] {"," Column ["ASC" | "DESC"] }*
	(if neither ASC nor DESC follow "Col", then ASC is choosen as default)


	this method assures, that ALL KEY FIELDs are part of the order criteria,
	in any case (independly from the order-Str). if necessary it appends them.
	WHY: to ensure correct scrollig (not getting STUCK if the search criteria
  are not "sharp" enough)
  #fixme - better explaination

	#fixme - determinate illegal input and throw IllegalArgumentException
	*/

	public FieldValue[] createOrderFieldValues(String order, HttpServletRequest request) {
		Vector result = null;

		if(request != null) {
			String paramStub = "sort_"+this.getId();
			Vector sortFields = ParseUtil.getParametersStartingWith(request, paramStub);

			if(sortFields.size() > 0)
				result = createOrderFVFromRequest(request, paramStub, sortFields);
			else
			  result = new Vector();

			logCat.info("result="+result.size());

		} else if(order!=null) {
			result = createOrderFVFromAttribute(order);

			logCat.debug("@@@ 1");
			for(int i=0; i<result.size(); i++) {
				FieldValue fieldVal = (FieldValue) result.elementAt(i);
				logCat.debug("fieldValue "+fieldVal.toString());
			}
		}

    // scroll through keys and append to order criteria, if not already included
		for(int i=0; i<this.key.size(); i++) {
			Field keyField = (Field) key.elementAt(i);

			boolean found = false; int j=0;
			while(!found && j<result.size()) {
				FieldValue fv = (FieldValue) result.elementAt(j);
				if(fv.getField() == keyField) found = true;
				j++;
			}

			if(!found) addFieldValue(result, keyField);
		}

		FieldValue[] resultArray = new FieldValue[result.size()];
		result.copyInto(resultArray);

			logCat.debug("@@@ 2");
			for(int i=0; i<resultArray.length; i++) {
				logCat.debug("fieldValue "+resultArray[i].toString());
			}

    return resultArray;
	}


  //------------------------------ utility / helper methods ---------------------------------

	/**
	 * for logging / debugging purposes only
	 */
  public String traverse() {
    StringBuffer buf = new StringBuffer();
    buf.append("\nname=");
    buf.append(name);
		buf.append(" ");

    for(int i=0; i<fields.size(); i++) {
      Field f = (Field) fields.elementAt(i);
      buf.append("\nfield: ");
      buf.append(f.toString());
		}
		return buf.toString();
  }


  // helper method used locally in this class
	private void addFieldValue(Vector stub, Field f)  {
		FieldValue fv = new FieldValue();
		fv.setField(f);
		stub.addElement(fv);
	}


	/**
	 transfer values from asscociative (name-orientated, user friendly) hashtable [param 'assocFv']
	 into hashtable used interally by DbForms

	 used during parameter-passing for interceptors
	*/
	public void synchronizeData(Hashtable fv, Hashtable assocFv) {
/*

this (old) piece of code copies only fields which are in both hashtables

		Enumeration enum = fv.keys();
		while(enum.hasMoreElements()) {
			Integer ii = (Integer) enum.nextElement();
			Field f = this.getField(ii.intValue());
			String newValue = (String) assocFv.get(f.getName());

			if(newValue!=null)
				fv.put(ii, newValue);
		}


*/

// this (new) piece of code copies all fields which are in assoc.-hashtable

		Enumeration enum = assocFv.keys();
		while(enum.hasMoreElements()) {
			String aFieldName = (String) enum.nextElement();
			Field f = this.getFieldByName(aFieldName);

			if(f!=null) {

				Integer ii = new Integer( f.getId() );
				String newValue = (String) assocFv.get( aFieldName );

				if(newValue!=null)
				  fv.put(ii, newValue);
			}

		}


	}


	public void processInterceptors(int action, HttpServletRequest request, Hashtable associativeArray, DbFormsConfig config, Connection con)
	throws SQLException {

		try {

			int interceptorsCnt = interceptors.size();
			for(int i=0; i<interceptorsCnt; i++) {
				Interceptor interceptor = (Interceptor) interceptors.elementAt(i);
				Class interceptorClass = Class.forName(interceptor.getClassName());
				DbEventInterceptor dbi = (DbEventInterceptor) interceptorClass.newInstance();

				if(action == DbEventInterceptor.PRE_INSERT) {

					if(dbi.preInsert(request, associativeArray, config, con)==DbEventInterceptor.DENY_OPERATION)
						throw new SQLException("Sorry, adding data to table "+this.getName()+" was not granted this time. Your request violated a condition.");

				} else if(action == DbEventInterceptor.POST_INSERT) {

					dbi.postInsert(request,  config, con);

				} else if(action == DbEventInterceptor.PRE_UPDATE) {

					if(dbi.preUpdate(request, associativeArray, config, con)==DbEventInterceptor.DENY_OPERATION)
						throw new SQLException("Sorry, updating data in table "+this.getName()+" was not granted this time. Your request violated a condition.");

				} else if(action == DbEventInterceptor.POST_UPDATE) {

					dbi.postUpdate(request,  config, con);

				} else if(action == DbEventInterceptor.PRE_DELETE) {

					if(dbi.preDelete(request, associativeArray, config, con)==DbEventInterceptor.DENY_OPERATION)
						throw new SQLException("Sorry, deleting data from table "+this.getName()+" was not granted this time. Your request violated a condition.");

				} else if(action == DbEventInterceptor.POST_DELETE) {

					dbi.postDelete(request, config, con);

				} else if(action == DbEventInterceptor.PRE_SELECT) {

					if(dbi.preSelect(request, config, con)==DbEventInterceptor.DENY_OPERATION)
						throw new SQLException("Sorry, selecting data from table "+this.getName()+" was not granted this time. Your request violated a condition.");

				} else if(action == DbEventInterceptor.POST_SELECT) {

					dbi.postSelect(request,  config, con);

				}
			}

		} catch(ClassNotFoundException cnfe) {
			throw new SQLException("Exception in interceptor: " + cnfe.getMessage());
		} catch(InstantiationException ie) {
			throw new SQLException("Exception in interceptor: " +ie.getMessage());
		} catch(IllegalAccessException iae) {
			throw new SQLException("Exception in interceptor: " + iae.getMessage());
		} catch(SQLException sqle) {
			throw new SQLException("Exception in interceptor: " + sqle.getMessage());
		} catch(ValidationException ve) {
			throw new SQLException("Input validation Exception in interceptor: " + ve.getMessage());
		}

	}

}