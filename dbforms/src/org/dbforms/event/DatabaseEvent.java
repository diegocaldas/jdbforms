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

import java.sql.SQLException;
import java.sql.Connection;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.Field;
import org.dbforms.util.FieldValue;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.Constants;
import org.dbforms.util.FieldValues;
import org.dbforms.util.FieldTypes;
import org.dbforms.util.Util;


/**
 *  Abstract base class for all web-events related to database operations
 *  like inserts, updates, deletes.
 *
 * @author Joe Peer <j.peer@gmx.net>
 */
public abstract class DatabaseEvent extends WebEvent
{
   /** DOCUMENT ME! */
   protected String keyId;

   /**
    * Creates a new DatabaseEvent object.
    *
    * @param tableId DOCUMENT ME!
    * @param keyId DOCUMENT ME!
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    */
   public DatabaseEvent(int tableId, String keyId, HttpServletRequest request,
      DbFormsConfig config)
   {
      super(tableId, request, config);
      this.keyId = keyId;
   }

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
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
    */
   public Hashtable getAssociativeFieldValues(FieldValues scalarFieldValues)
   {
      Hashtable   result  = new Hashtable();
      Enumeration scalars = scalarFieldValues.keys();

      while (scalars.hasMoreElements())
      {
         String fieldName = (String) scalars.nextElement();
         result.put(fieldName, scalarFieldValues.get(fieldName).getFieldValue());
      }

      return result;
   }


   /**
    *  DO the validation of <FORM> with Commons-Validator.
    *
    * @param  formValidatorName The form name to retreive in validation.xml
    * @param  request The servlet request we are processing
    * @param  e the web event
    * @exception  MultipleValidationException The Vector of errors throwed with this exception
    */
   public void doValidation(String formValidatorName, ServletContext context,
      HttpServletRequest request) throws MultipleValidationException
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @param insertMode DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   protected FieldValues getFieldValues(boolean insertMode)
   {
      FieldValues result    = new FieldValues();
      String      paramStub = "f_" + tableId + "_"
         + (insertMode ? Constants.INSERTPREFIX : "") + keyId + "_";
      Vector      params    = ParseUtil.getParametersStartingWith(request,
            paramStub);
      Enumeration enum      = params.elements();

      while (enum.hasMoreElements())
      {
         String param = (String) enum.nextElement();
         String value = ParseUtil.getParameter(request, param);
         logCat.info("::getFieldValues - param=" + param + " value=" + value);

         int        iiFieldId = Integer.parseInt(param.substring(
                  paramStub.length()));
         Field      f  = table.getField(iiFieldId);
         FieldValue fv = new FieldValue(f, value);

         if ((f.getType() == FieldTypes.BLOB)
                  || (f.getType() == FieldTypes.DISKBLOB))
         {
            // in case of a BLOB or DISKBLOB save get the FileHolder for later use
            fv.setFileHolder(ParseUtil.getFileHolder(request,
                  "f_" + tableId + (insertMode ? Constants.INSERTPREFIX : "")
                  + keyId + "_" + iiFieldId));
         }

         result.put(f.getName(), fv);
      }

      return result;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   protected String getKeyValues()
   {
      return Util.decode(ParseUtil.getParameter(request, "k_" + tableId + "_" + keyId));
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