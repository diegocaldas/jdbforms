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
package org.dbforms.event;


import java.sql.Connection;
import java.sql.SQLException;

import java.util.Iterator;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.dbforms.config.MultipleValidationException;
import org.dbforms.config.Constants;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.Field;
import org.dbforms.config.FieldTypes;
import org.dbforms.config.FieldValue;
import org.dbforms.config.FieldValues;

import org.dbforms.util.ParseUtil;
import org.dbforms.util.Util;


/**
 *  Abstract base class for all web-events related to database operations
 *  like inserts, updates, deletes.
 *
 * @author Joe Peer <j.peer@gmx.net>
 */
public abstract class DatabaseEvent extends WebEvent
{
  /** key identifier */
  protected String keyId;


  /**
   * Creates a new DatabaseEvent object.
   *
   * @param tableId the table id
   * @param keyId   the key id
   * @param request the request object
   * @param config  the configuration object
   */
  public DatabaseEvent(int                tableId,
                       String             keyId,
                       HttpServletRequest request,
                       DbFormsConfig      config)
  {
    super(tableId, request, config);
    this.keyId = keyId;
  }


  /**
   * Get the keyId parameter value
   *
   * @return keyId parameter value
   */
  public String getKeyId()
  {
    return keyId;
  }


  /**
  *  Get the hash table containing the form field names and values taken
  *  from the request object.
  *  <br>
  *  Example of a request parameter:<br>
  *  <code>
  *    name  = f_0_insroot_6
  *    value = foo-bar
  *  </code>
  *
  * @return the hash map containing the names and values taken from
  *         the request object
  */
  public abstract FieldValues getFieldValues();


  /**
   *  Get the hash table containing the field names and values
   *  to insert into the specified database table.
   *  <br>
   *  This method is used in ConditionChecker only
   *  (see: <code>Controller.doValidation()</code> )
   *  <br>
   *  Example of a hash table entry:<br>
   *  <code>
   *    key:   LAST_NAME
   *    value: foo-bar
   *  </code>
   *
   * @param scalarFieldValues the hash map containing the names and values
   *                          taken from the request object
   *                          (see: <code>getFieldValues()</code>
   *
   * @return hash table containing the field names and values
   *            to insert into the specified database table.
   */
  public Hashtable getAssociativeFieldValues(FieldValues scalarFieldValues)
  {
    Hashtable result = new Hashtable();
    Iterator scalars = scalarFieldValues.keys();

    while (scalars.hasNext())
    {
      String fieldName = (String) scalars.next();
      result.put(fieldName, scalarFieldValues.get(fieldName).getFieldValue());
    }

    return result;
  }


  /**
   *  DO the validation of <FORM> with Commons-Validator.
   *
   * @param  formValidatorName The form name to retreive in validation.xml
   * @param  context The servlet context we are processing
   * @param  request The servlet request we are processing
   *
   * @exception  MultipleValidationException The Vector of errors throwed with this exception
   */
  public void doValidation(String             formValidatorName,
                           ServletContext     context,
                           HttpServletRequest request)
                    throws MultipleValidationException
  {
  }


  /**
   * Get the FieldValues object representing the collection 
   * of FieldValue objects builded from the request parameters
   *
   * @param insertMode true DOCUMENT ME!
   *
   * @return the FieldValues object representing the collection 
   *         of FieldValue objects builded from the request parameters
   */
  protected FieldValues getFieldValues(boolean insertMode)
  {
    FieldValues result = new FieldValues();
    String paramStub = ("f_" 
                        + tableId 
                        + "_" 
                        + (insertMode ? Constants.INSERTPREFIX : "") 
                        + keyId
                        + "_");
    Vector params = ParseUtil.getParametersStartingWith(request, paramStub);
    boolean doIt  = insertMode || "delete".equals(getType());
    
    // First check if update is necessary
    if (!doIt)
    { 
      Iterator enum = params.iterator();
      
      while (enum.hasNext())
      {
        String param = (String) enum.next();
        
        // value of the named parameter;
        String value = ParseUtil.getParameter(request, param);
        
        // old value of the named parameter;
        String oldValue = ParseUtil.getParameter(request, "o" + param);
        
        // if they are not equals, set the update flag for this field
        // and exit from the loop;
        doIt = !value.equals(oldValue);
        
        if (doIt)
        {
          break;
        }
      }
    }

    //  if update is necessary then do update for all data columns
    if (doIt)
    {
      Iterator enum = params.iterator();
      while (enum.hasNext())
      {
        String param = (String) enum.next();
        String value = ParseUtil.getParameter(request, param);
        String oldValue = ParseUtil.getParameter(request, "o" + param);

        //logCat.debug("::getFieldValues - param:" + param + " value:" + value);

        int iiFieldId = Integer.parseInt(param.substring(paramStub.length()));
        Field f = table.getField(iiFieldId);
        FieldValue fv = new FieldValue(f, value);

        fv.setOldValue(oldValue);

        if ((f.getType() == FieldTypes.BLOB) || (f.getType() == FieldTypes.DISKBLOB))
        {
          // in case of a BLOB or DISKBLOB save get the FileHolder for later use
          fv.setFileHolder(ParseUtil.getFileHolder(request,
                                                   "f_" + tableId
																	+ "_"
                                                   + (insertMode ? Constants.INSERTPREFIX : "")  
                                                   + keyId + "_" + iiFieldId));
        }

        result.put(fv);
      }
    }

    return result;
  }


  /**
   * Return the key values string from the request object 
   *
   * @return the key values string from the request object 
   */
  protected String getKeyValues()
  {
    String key = Util.decode(ParseUtil.getParameter(request, "k_" + tableId + "_" + keyId));
    logCat.info("::getKeyValues - key: " + key);
    return key;
  }


  /**
   *  Process this event.
   *
   * @param con the jdbc connection object
   * @throws SQLException if any data access error occurs
   * @throws MultipleValidationException if any validation error occurs
   */
  public abstract void processEvent(Connection con)
                             throws SQLException, MultipleValidationException;
}
