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
import java.util.*;
import java.sql.*;
import java.io.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import org.dbforms.config.*;
import org.dbforms.util.*;
import org.apache.log4j.Category;



/****
 *
 * grunikiewicz.philip@hydro.qc.ca
 * 2001-05-14
 *
 * This class inherits from DbLabelTag.  It allows a developer to specify the displayed date format.
 * By default, the following format is used:
 *
 *        new SimpleDateFormat("yyyy-MM-dd")
 *
 */
public class DbDateLabelTag extends DbLabelTag
{
   static Category logCat = Category.getInstance(DbDateLabelTag.class.getName());

   // this property is moved into the supercalss (DbLabelTag)  
   // please remove the commented attribute below after the release of
   // the future 1.1.3 version !
   //
   // fossato <fossato@pow2.com> [2002.11.09]
   //protected java.text.Format format = DbFormsConfig.getDateFormatter();

   /**
   grunikiewicz.philip@hydro.qc.ca
   2001-05-14

   If user has specified a date format - use it!

   */
   public int doEndTag() throws javax.servlet.jsp.JspException
   {
      try
      {
         Object fieldValue = NO_DATA;

         if (!Util.isNull(parentForm.getResultSetVector()))
         {
            Object[] currentRow = parentForm.getResultSetVector()
                                            .getCurrentRowAsObjects();

            // fetch database row as java objects
            Object currentValue = currentRow[field.getId()];

            // Format date if the retrieved currentValue is not null
            if (currentValue != null)
            {
               // if the format object is not set, retrieve the default 
               // date formatter from the prefs; <fossato@pow2.com> [2002.11.09]
               if (format == null)
               {
                  try {
							format = DbFormsConfigRegistry.instance().lookup().getDateFormatter();
                  } catch (Exception e) {
                  }
                  
               }

               // anyway, a format object must exist !
               fieldValue = (format != null) ? format.format(currentValue)
                                             : currentValue;
            }
            else
            {
               fieldValue = ""; // null == empty string
            }
         }

         pageContext.getOut().write(fieldValue.toString());
      }
      catch (java.io.IOException ioe)
      {
         throw new JspException("IO Error: " + ioe.getMessage());
      }
      catch (Exception e)
      {
         throw new JspException("Error: " + e.getMessage());
      }

      return EVAL_PAGE;
   }

   // Note on format accessors: they are moved into DbLabelTag;
   // please remove the commented code below after the release of
   // the future 1.1.3 version !
   //
   // fossato <fossato@pow2.com> [2002.11.09]

   /**
    * Insert the method's description here.
    * Creation date: (2001-05-14 15:28:11)
    * @return java.text.Format
    */

   //public java.text.Format getFormat() {
   //	return format;
   //}

   /**
    * Insert the method's description here.
    * Creation date: (2001-05-14 15:28:11)
    * @param newFormat java.text.Format
    */

   //public void setFormat(java.text.Format newFormat) {
   //	format = newFormat;
   //}
}
