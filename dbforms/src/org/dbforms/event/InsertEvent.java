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

import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.http.*;

import org.apache.log4j.Category;
import com.oreilly.servlet.multipart.*;

import org.dbforms.*;
import org.dbforms.util.*;




/****
 *  This event prepares and performs a SQL-Insert operation.
 *
 * @author Joe Peer <j.peer@gmx.net>
 */
public class InsertEvent extends DatabaseEvent
{
    /** logging category for this class */
    static Category logCat = Category.getInstance(InsertEvent.class.getName());

    private Table table;
    private String idStr;


    /**
     *  Insert actionbutton-strings is as follows: ac_insert_12_root_3
     *  which is equivalent to:
     *
     *       ac_insert  : insert action event
     *       12         : table id
     *       root       : key
     *       3          : button count used to identify individual insert buttons
     */
    public InsertEvent(String str, HttpServletRequest request, DbFormsConfig config)
    {
        this.request = request;
        this.config  = config;

        this.tableId = ParseUtil.getEmbeddedStringAsInteger(str, 2, '_');
        this.table   = config.getTable(tableId);
        this.idStr   = ParseUtil.getEmbeddedString(str, 3, '_');

        logCat.info("parsing insertevent");
        logCat.info("tableName=" + table.getName());
        logCat.info("tableId=" + tableId);
        logCat.info("idStr=" + idStr);                   // ie. "root", "1@root"
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
    public Hashtable getFieldValues()
    {
        Hashtable result = new Hashtable();

        String paramStub = "f_" + tableId + "_ins" + idStr + "_";
        Vector params    = ParseUtil.getParametersStartingWith(request, paramStub);
        Enumeration enum = params.elements();

        while (enum.hasMoreElements())
        {
            String param = (String) enum.nextElement();
            String value = ParseUtil.getParameter(request, param);

            logCat.info("::getFieldValues - param=" + param + " value=" + value);

            Integer iiFieldId = new Integer(param.substring(paramStub.length()));
            result.put(iiFieldId, value);
        }

        return result;
    }


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
    public Hashtable getAssociativeFieldValues(Hashtable scalarFieldValues)
    {
        Hashtable   result  = new Hashtable();
        Enumeration scalars = scalarFieldValues.keys();

        while (scalars.hasMoreElements())
        {
            Integer fieldIndex = (Integer) scalars.nextElement();
            String  fieldName  = table.getField(fieldIndex.intValue()).getName();
            result.put(fieldName, scalarFieldValues.get(fieldIndex));
        }

        return result;
    }


    /**
     *  Process this event.
     *
     * @param con the jdbc connection object
     * @throws SQLException if any data access error occurs
     * @throws MultipleValidationException if any validation error occurs
     */
    public void processEvent(Connection con) throws SQLException, MultipleValidationException
    {
        // Applying given security contraints (as defined in dbforms-config xml file)
        // part 1: check if requested privilge is granted for role
        if (!hasUserPrivileg(GrantedPrivileges.PRIVILEG_INSERT))
        {
            throw new SQLException("Sorry, adding data to table " + table.getName() + " is not granted for this session.");
        }

        Hashtable fieldValues = getFieldValues();

        if (fieldValues.size() == 0)
        {
            throw new SQLException("no parameters");
        }

        // part 2: check if there are interceptors to be processed (as definied by
        // "interceptor" element embedded in table element in dbforms-config xml file)
        if (table.hasInterceptors())
        {
            try
            {
                Hashtable associativeArray = getAssociativeFieldValues(fieldValues);

                // process the interceptors associated to this table
                table.processInterceptors(DbEventInterceptor.PRE_INSERT, request, associativeArray, config, con);

                // synchronize data which may be changed by interceptor:
                table.synchronizeData(fieldValues, associativeArray);
            }

            // better to log exceptions generated by method errors (fossato, 2002.12.04);
            catch (SQLException sqle)
            {
                // PG = 2001-12-04
                // No need to add extra comments, just re-throw exception
                SqlUtil.logSqlException(sqle);
                throw sqle;
            }
            catch (MultipleValidationException mve)
            {
                // PG, 2001-12-14
                // Support for multiple error messages in one interceptor
                logCat.error("::processEvent - MultipleValidationException", mve);
                throw mve;
            }
        }

        // End of interceptor processing
        if (!checkSufficentValues(fieldValues))
        {
            throw new SQLException("unsufficent parameters");
        }

        // 20021031-HKK: table.getInsertStatement()
        PreparedStatement ps = con.prepareStatement(table.getInsertStatement(fieldValues));

        // now we provide the values;
        // every key is the parameter name from of the form page;
        Enumeration enum = fieldValues.keys();
        int col = 1;

        while (enum.hasMoreElements())
        {
            Integer iiFieldId = (Integer) enum.nextElement();
            Field curField    = (Field) table.getFields().elementAt(iiFieldId.intValue());

            if (curField != null)
            {
                int fieldType = curField.getType();
                Object value = null;

                if (fieldType == FieldTypes.BLOB)
                {
                    // in case of a BLOB we supply the FileHolder object to SqlUtils for further operations
                    value = ParseUtil.getFileHolder(request, "f_" + tableId + "_ins" + idStr + "_" + iiFieldId);
                }
                else if (fieldType == FieldTypes.DISKBLOB)
                {
                    // check if we need to store it encoded or not
                    if ("true".equals(curField.getEncoding()))
                    {
                        FileHolder fileHolder = ParseUtil.getFileHolder(request, "f_" + tableId + "_ins" + idStr + "_" + iiFieldId);

                        // encode fileName
                        String fileName = fileHolder.getFileName();
                        int dotIndex = fileName.lastIndexOf('.');
                        String suffix = (dotIndex != -1) ? fileName.substring(dotIndex) : "";
                        fileHolder.setFileName(UniqueIDGenerator.getUniqueID() + suffix);

                        // a diskblob gets stored to db as an ordinary string (it's only the reference!)
                        value = fileHolder.getFileName();
                    }
                    else
                    {
                        // a diskblob gets stored to db as an ordinary string	 (it's only the reference!)
                        value = fieldValues.get(iiFieldId);
                    }
                }
                else
                {
                    // in case of simple db types we just supply a string representing the value of the fields
                    value = fieldValues.get(iiFieldId);
                }

                logCat.info("PRE_INSERT: field=" + curField.getName() + " col=" + col + " value=" + value);
                SqlUtil.fillPreparedStatement(ps, col, value, fieldType);
                col++;
            }
        }

        // execute the query & throws an exception if something goes wrong
        ps.executeUpdate();
        ps.close(); // #JP Jun 27, 2001

        enum = fieldValues.keys();

        while (enum.hasMoreElements())
        {
            Integer iiFieldId = (Integer) enum.nextElement();

            Field curField = (Field) table.getFields().elementAt(iiFieldId.intValue());

            if (curField != null)
            {
                int fieldType = curField.getType();
                String directory = curField.getDirectory();

                if (fieldType == FieldTypes.DISKBLOB)
                {
                    // check if directory-attribute was provided
                    if (directory == null)
                    {
                        throw new IllegalArgumentException("directory-attribute needed for fields of type DISKBLOB");
                    }

                    // instanciate file object for that dir
                    File dir = new File(directory);

                    // Check saveDirectory is truly a directory
                    if (!dir.isDirectory())
                    {
                        throw new IllegalArgumentException("Not a directory: " + directory);
                    }

                    // Check saveDirectory is writable
                    if (!dir.canWrite())
                    {
                        throw new IllegalArgumentException("Not writable: " + directory);
                    }

                    // dir is ok so lets store the filepart
                    FileHolder fileHolder = ParseUtil.getFileHolder(request, "f_" + tableId + "_ins" + idStr + "_" + iiFieldId);

                    if (fileHolder != null)
                    {
                        try
                        {
                            fileHolder.writeBufferToFile(dir);

                            //filePart.getInputStream().close();
                            logCat.info("fin + closedy");
                        }
                        catch (IOException ioe)
                        {
                            //#checkme: this would be a good place for rollback in database!!
                            throw new SQLException("could not store file '" + fileHolder.getFileName() + "' to dir '" + directory + "'");
                        }
                    }
                    else
                    {
                        logCat.info("uh! empty fileHolder");
                    }
                }
            }
        }

        //Patch insert nav by Stefano Borghi
        //Show the last record inserted
        String firstPosition = null;
        Vector key = table.getKey();
        FieldValue[] fvEqual = new FieldValue[key.size()];

        for (int i = 0; i < key.size(); i++)
        {
            Field field = (Field) key.elementAt(i);
            String value = (String) fieldValues.get(new Integer(field.getId()));
            FieldValue keyFieldValue = new FieldValue(field, value, false);
            fvEqual[i] = keyFieldValue;
        }

        ResultSetVector resultSetVector = table.doConstrainedSelect(table.getFields(), fvEqual, null, Table.GET_EQUAL, 1, con);

        if (resultSetVector != null)
        {
            resultSetVector.setPointer(0);
            firstPosition = table.getPositionString(resultSetVector);
        }

        request.setAttribute("firstpos_" + tableId, firstPosition);

        //end patch
        // finally, we process interceptor again (post-insert)
        if (table.hasInterceptors())
        {
            try
            {
                // process the interceptors associated to this table
                table.processInterceptors(DbEventInterceptor.POST_INSERT, request, null, config, con);
            }
            catch (SQLException sqle)
            {
                // PG = 2001-12-04
                // No need to add extra comments, just re-throw exceptions as SqlExceptions
                throw new SQLException(sqle.getMessage());
            }
        }
    }




    /**
     *  Check if the input hash table has got sufficent parameters.
     *
     * @param fieldValues the hash map containing the names and values taken from
     *                    the request object
     * @return true  if the hash table has got sufficent parameters,
     *         false otherwise
     * @throws SQLException  if any data access error occurs
     */
    private boolean checkSufficentValues(Hashtable fieldValues) throws SQLException
    {
        Vector fields = table.getFields();

        for (int i = 0; i < fields.size(); i++)
        {
            Field field = (Field) fields.elementAt(i);

            // if a field is a key and if it is NOT automatically generated,
            // then it should be provided by the user
            if (!field.getIsAutoInc() && field.isKey())
            {
                if (fieldValues.get(new Integer(i)) == null)
                {
                    throw new SQLException("Field " + field.getName() + " is missing");
                }
            }

            // in opposite, if a field is automatically generated by the RDBMS, we need to
            else if (field.getIsAutoInc())
            {
                if (fieldValues.get(new Integer(i)) != null)
                {
                    throw new SQLException("Field " + field.getName() + " should be calculated by RDBMS, remove it from the form");
                }
            }
             // in future we could do some other checks like NOT-NULL conditions,etc.
        }

        return true;
    }
}
