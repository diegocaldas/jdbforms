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

package org.dbforms.taglib;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.util.Util;

import javax.servlet.jsp.tagext.*;

import java.io.Serializable;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class DbFormTagTEI extends TagExtraInfo implements Serializable {
   // logging category for this class
   static Log logCat = LogFactory.getLog(DbFormTagTEI.class.getName());

   /**
    * DOCUMENT ME!
    *
    * @param data DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public VariableInfo[] getVariableInfo(TagData data) {
      StringBuffer[] varNames = {
                                   new StringBuffer("currentRow"),
                                   new StringBuffer("position"),
                                   new StringBuffer("searchFieldNames"),
                                   new StringBuffer("searchFieldModeNames"),
                                   new StringBuffer("searchFieldAlgorithmNames"),
                                   new StringBuffer("rsv")
                                };

      // convention for DbFroms TEI-provided variables: varName "_" tableName
      // this is necessary to prevent JSP compiler errors if forms are nested
      String table = null;

      try {
         table = data.getAttributeString("tableName");
      } catch (Exception e) {
         table = null;
      }

      String parentField = null;

      try {
         parentField = data.getAttributeString("parentField");
      } catch (Exception e) {
         parentField = null;
      }

      String childField = null;

      try {
         childField = data.getAttributeString("childField");
      } catch (Exception e) {
         childField = null;
      }

      if (table != null) {
         for (int i = 0; i < varNames.length; i++) {
            varNames[i].append("_");
            varNames[i].append(table.replace('.', '_')); // # jp 27-06-2001
         }
      }

      logCat.info("*** TEI CLASS IN ACTION ***");
      logCat.info("table=" + table);
      logCat.info("varNames[0]=" + varNames[0].toString());

      int count = 0;

      if (!Util.isNull(table)) {
         count = count + 6;
      }

      if (Util.isNull(parentField) && Util.isNull(childField)) {
         count = count + 1;
      }

      VariableInfo[] info = null;

      if (count > 0) {
         info = new VariableInfo[count];

         int i = 0;

         if (!Util.isNull(table)) {
            info[i++] = new VariableInfo(varNames[0].toString(),
                                         "java.util.Map", true,
                                         VariableInfo.NESTED);
            info[i++] = new VariableInfo(varNames[1].toString(),
                                         "java.lang.String", true,
                                         VariableInfo.NESTED);
            info[i++] = new VariableInfo(varNames[2].toString(),
                                         "java.util.Map", true,
                                         VariableInfo.NESTED);
            info[i++] = new VariableInfo(varNames[3].toString(),
                                         "java.util.Map", true,
                                         VariableInfo.NESTED);
            info[i++] = new VariableInfo(varNames[4].toString(),
                                         "java.util.Map", true,
                                         VariableInfo.NESTED);
            info[i++] = new VariableInfo(varNames[5].toString(),
                                         "org.dbforms.config.ResultSetVector",
                                         true, VariableInfo.NESTED);
         }

         if (Util.isNull(parentField) && Util.isNull(childField)) {
            info[i++] = new VariableInfo("dbforms", "java.util.Map", true,
                                         VariableInfo.NESTED);
         }
      }

      return info;
   }
}
