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
import javax.servlet.jsp.tagext.*;
import org.apache.log4j.Category;



/**
 * DOCUMENT ME!
 *
 * @version $Revision$
 * @author $author$
 */
public class DbFormTagTEI extends TagExtraInfo
{
    static Category logCat = Category.getInstance(DbFormTagTEI.class.getName()); // logging category for this class

    /**
     * DOCUMENT ME!
     *
     * @param data DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public VariableInfo[] getVariableInfo(TagData data)
    {
        StringBuffer[] varNames = 
        {
            new StringBuffer("currentRow"), new StringBuffer("position"), 
            new StringBuffer("searchFieldNames"), 
            new StringBuffer("searchFieldModeNames"), 
            new StringBuffer("searchFieldAlgorithmNames"), 
            new StringBuffer("rsv")
        };

        // convention for DbFroms TEI-provided variables: varName "_" tableName
        // this is necessary to prevent JSP compiler errors if forms are nested
        String table = data.getAttributeString("tableName");

        if (table != null)
        {
            for (int i = 0; i < varNames.length; i++)
            {
                varNames[i].append("_");
                varNames[i].append(table.replace('.', '_')); // # jp 27-06-2001
            }
        }

        logCat.info("*** TEI CLASS IN ACTION ***");
        logCat.info("table=" + table);
        logCat.info("varNames[0]=" + varNames[0].toString());

        VariableInfo[] info = 
        {

            new VariableInfo(varNames[0].toString(), "java.util.Hashtable", true, VariableInfo.NESTED), 

            new VariableInfo(varNames[1].toString(), "java.lang.String", true, VariableInfo.NESTED), 

            new VariableInfo(varNames[2].toString(), "java.util.Hashtable", true, VariableInfo.NESTED), 

            new VariableInfo(varNames[3].toString(), "java.util.Hashtable", true, VariableInfo.NESTED), 

            new VariableInfo(varNames[4].toString(), "java.util.Hashtable", true, VariableInfo.NESTED), 

            new VariableInfo(varNames[5].toString(), "org.dbforms.util.ResultSetVector", true, VariableInfo.NESTED)
        };

        return info;
    }
}