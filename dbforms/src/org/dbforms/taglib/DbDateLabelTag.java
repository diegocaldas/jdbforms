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
import javax.servlet.jsp.JspException;
import java.text.Format;
import org.apache.log4j.Category;
import org.dbforms.config.DbFormsConfigRegistry;
import org.dbforms.util.Util;



/****
 *
 * grunikiewicz.philip@hydro.qc.ca
 * 2001-05-14
 *
 * This class inherits from DbLabelTag.  It allows a developer to specify the displayed date format.
 *
 *
 */
public class DbDateLabelTag extends DbLabelTag
{
   static Category logCat = Category.getInstance(DbDateLabelTag.class.getName());

   /**
   grunikiewicz.philip@hydro.qc.ca
   2001-05-14
       
   If user has specified a date format - use it!
       
   */
   public int doEndTag() throws javax.servlet.jsp.JspException
   {
      try
      {
         Object currentValue = getFieldObject();

         Format format = getFormat();
         if (format == null)
         {
            try
            {
               format = DbFormsConfigRegistry.instance().lookup()
                                             .getDateFormatter();
            }
            catch (Exception e)
            {
               logCat.error(e);
            }
         }

         String fieldValue = Util.isNull(getNullFieldValue()) ? typicalDefaultValue(): getNullFieldValue();

         // Format date if the retrieved currentValue is not null
         if ((currentValue != null) && (format != null))
         {
            fieldValue = format.format(currentValue);
         }

         String s = prepareStyles();

         if (Util.isNull(s))
         {
            pageContext.getOut().write(fieldValue);
         }
         else
         {
            pageContext.getOut()
                       .write("<span " + s + "\">" + fieldValue + "</span>");
         }
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
}