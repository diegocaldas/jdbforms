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

import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.FieldValue;
import org.dbforms.config.FieldValues;
import org.dbforms.config.Table;
import org.dbforms.config.ValidationException;

import org.dbforms.util.FileHolder;

import java.sql.Connection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;



/**
 * This Interceptor can be used to automatically store the filenames, content
 * types and file sizes of BLOBS in some table column specified, so that it is
 * not lost.  Currently, the interceptor must be configured manually in
 * dbforms-config.xml inside the adequate table definition (see user guide)
 * The interceptor MUST be initialized with the following parameters:
 *
 * <ul>
 * <li>
 * <b>blob-column </b>- the name of the BLOB field
 * </li>
 * <li>
 * <b>name-column </b>- a character field to store the FILENAME of the uploaded
 * file
 * </li>
 * </ul>
 *
 * Optionaly may be specified:
 *
 * <ul>
 * <li>
 * <b>mime-column </b>- a character field to store the content typpe of the
 * uploaded file
 * </li>
 * <li>
 * <b>size-column </b>- an integer field to store the file size of the uploaded
 * file
 * </li>
 * </ul>
 *
 * if the table contains multiple BLOBs, then a unique integer n has to be
 * appended to blob-column and blob-name to associate the correct pairs of
 * BLOB, NAME, MIME and SIZE fields.  Usage Examples:
 *
 * <p>
 * if one BLOB:
 * </p>
 *
 * <p>
 * &lt;interceptor className="org.dbforms.event.BlobInterceptor"&gt; <br>
 * &nbsp;&nbsp;&lt;param name="blob-column" value="file"/&gt; <br>
 * &nbsp;&nbsp;&lt;param name="name-column" value="filename"/&gt; <br>
 * &nbsp;&nbsp;&lt;param name="mime-column" value="mime_type"/&gt; <br>
 * &nbsp;&nbsp;&lt;param name="size-column" value="file_size"/&gt; <br>
 * &lt;/interceptor&gt; <br>
 * </p>
 *
 * <p>
 * if mulitple BLOBs:
 * </p>
 *
 * <p>
 * &lt;interceptor className="org.dbforms.event.BlobInterceptor"&gt; <br>
 * &nbsp;&nbsp;&lt;param name="blob-column1" value="file"/&gt; <br>
 * &nbsp;&nbsp;&lt;param name="name-column1" value="filename"/&gt; <br>
 * &nbsp;&nbsp;&lt;param name="mime-column1" value="mime_type"/&gt; <br>
 * &nbsp;&nbsp;&lt;param name="size-column1" value="file_size"/&gt; <br>
 * &nbsp;&nbsp;&lt;param name="blob-column2" value="otherfile"/&gt; <br>
 * &nbsp;&nbsp;&lt;param name="name-column2" value="otherfilename"/&gt; <br>
 * &nbsp;&nbsp;&lt;param name="mime-column2" value="other_mime_type"/&gt; <br>
 * &nbsp;&nbsp;&lt;param name="size-column2" value="other_file_size"/&gt; <br>
 * &nbsp;&nbsp;&lt;param name="blob-column3" value="foofile"/&gt; <br>
 * &nbsp;&nbsp;&lt;param name="name-column3" value="foofilename"/&gt; <br>
 * &nbsp;&nbsp;&lt;param name="mime-column3" value="foo_mime_type"/&gt; <br>
 * &nbsp;&nbsp;&lt;param name="size-column3" value="foo_file_size"/&gt; <br>
 * &lt;/interceptor&gt; <br>
 * </p>
 *
 * @author Joe Peer
 */
public class BlobInterceptor extends DbEventInterceptorSupport {
   private static final int BLOB_COL      = 0;
   private static final int NAME_COL      = 1;
   private static final int MIME_COL      = 2;
   private static final int SIZE_COL      = 3;
   private HashMap          blobFieldData;

   /**
    * takes params, sorts/groups and stores for later use
    *
    * @param params DOCUMENT ME!
    */
   public void setParams(Map params) {
      super.setParams(params);
      blobFieldData = new HashMap();

      for (Iterator iter = params.entrySet()
                                 .iterator(); iter.hasNext();) {
         Map.Entry me    = (Map.Entry) iter.next();
         String    key   = (String) me.getKey();
         String    value = (String) me.getValue();

         if (key.startsWith("blob-column")) {
            Integer  ii = getSuffixAsInteger(key, "blob-column");
            String[] s = (String[]) blobFieldData.get(ii);

            if (s == null) {
               s = new String[4];
               blobFieldData.put(ii, s);
            }

            s[BLOB_COL] = value;
         } else if (key.startsWith("name-column")) {
            Integer  ii = getSuffixAsInteger(key, "name-column");
            String[] s = (String[]) blobFieldData.get(ii);

            if (s == null) {
               s = new String[4];
               blobFieldData.put(ii, s);
            }

            s[NAME_COL] = value;
         } else if (key.startsWith("mime-column")) {
            Integer  ii = getSuffixAsInteger(key, "mime-column");
            String[] s = (String[]) blobFieldData.get(ii);

            if (s == null) {
               s = new String[4];
               blobFieldData.put(ii, s);
            }

            s[MIME_COL] = value;
         } else if (key.startsWith("size-column")) {
            Integer  ii = getSuffixAsInteger(key, "size-column");
            String[] s = (String[]) blobFieldData.get(ii);

            if (s == null) {
               s = new String[4];
               blobFieldData.put(ii, s);
            }

            s[SIZE_COL] = value;
         }
      }
   }


   /**
    * stores filenames, content types and file sizes of all blobs in the
    * NAME_COLUMNs, MIME_COLUMNs and SIZE_COLUMNs specified
    *
    * @param request DOCUMENT ME!
    * @param table DOCUMENT ME!
    * @param fieldValues DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public int preInsert(HttpServletRequest request,
                        Table              table,
                        FieldValues        fieldValues,
                        DbFormsConfig      config,
                        Connection         con) throws ValidationException {
      assignBlobData(table, fieldValues);

      return GRANT_OPERATION;
   }


   /**
    * stores filenames, content types and file sizes of all blobs in the
    * NAME_COLUMNs, MIME_COLUMNs and SIZE_COLUMNs specified
    *
    * @param request DOCUMENT ME!
    * @param table DOCUMENT ME!
    * @param fieldValues DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public int preUpdate(HttpServletRequest request,
                        Table              table,
                        FieldValues        fieldValues,
                        DbFormsConfig      config,
                        Connection         con) throws ValidationException {
      assignBlobData(table, fieldValues);

      return GRANT_OPERATION;
   }


   /**
    * utility function
    *
    * @param value DOCUMENT ME!
    * @param stub DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   private Integer getSuffixAsInteger(String value,
                                      String stub) {
      String suffix = value.substring(stub.length());

      if (suffix.length() == 0) {
         return new Integer(Integer.MIN_VALUE);
      } else {
         return new Integer(suffix);
      }
   }


   /**
    * goes through params and makes sure that the fileholder's file name info
    * associated to the blob field BLOB_COLUMN is stored in the NAME_COLUMN
    * field. The same is with MIME_COLUMN and SIZE_COLUMN fields.
    *
    * @param table DOCUMENT ME!
    * @param fieldValues DOCUMENT ME!
    */
   private void assignBlobData(Table       table,
                               FieldValues fieldValues) {
      for (Iterator iter = blobFieldData.values()
                                        .iterator(); iter.hasNext();) {
         String[]   s  = (String[]) iter.next();
         FieldValue fv = fieldValues.get(s[BLOB_COL]);

         if (fv != null) {
            Object o = fv.getFieldValueAsObject();

            if ((o != null) && o instanceof FileHolder) {
               String fileName    = ((FileHolder) o).getFileName();
               String contentType = ((FileHolder) o).getContentType();
               int    fileLength  = ((FileHolder) o).getFileLength();
               setValue(table, fieldValues, s[NAME_COL], fileName);

               if (s[MIME_COL] != null) {
                  setValue(table, fieldValues, s[MIME_COL], contentType);
               }

               if (s[SIZE_COL] != null) {
                  setValue(table, fieldValues, s[SIZE_COL],
                           String.valueOf(fileLength));
               }
            }
         }
      }
   }
}
