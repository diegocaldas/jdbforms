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
import javax.servlet.http.*;
import javax.servlet.jsp.*;


/**
 * #fixme docu to come
 *
 * @author  Joe Peer
 * @created  06 August 2002
 */
public class DbBlobURLTag extends DbBaseHandlerTag
{

   // --------------------------------------------------------- Public Methods
   // DbForms specific

   /**
    *  Description of the Method
    *
    * @return  Description of the Return Value
    * @exception  javax.servlet.jsp.JspException Description of the Exception
    */
   public int doEndTag() throws javax.servlet.jsp.JspException
   {
      try
      {
         StringBuffer tagBuf = new StringBuffer(((HttpServletRequest) pageContext
               .getRequest()).getContextPath());

         tagBuf.append("/servlet/file?tf=").append(getTableFieldCode())
               .append("&keyval=").append(getKeyVal());

         // append the defaultValue parameter;
         if (getDefaultValue() != null)
         {
            tagBuf.append("&defaultValue=" + getDefaultValue());

            //logCat.info("::doEndTag - defaultValue set to [" + defaultValue + "]");
         }

         pageContext.getOut().write(tagBuf.toString());
      }
      catch (java.io.IOException ioe)
      {
         throw new JspException("IO Error: " + ioe.getMessage());
      }

      return EVAL_PAGE;
   }


   // ------------------------------------------------------ Protected Methods
   // DbForms specific

   /**
    *  Generates the decoded name.
    *
    * @return  The tableFieldCode value
    */
   private String getTableFieldCode()
   {
      StringBuffer buf = new StringBuffer();

      buf.append(getParentForm().getTable().getId());
      buf.append("_");
      buf.append(getField().getId());

      return buf.toString();
   }


   /**
    *  Gets the keyVal attribute of the DbBlobURLTag object
    *
    * @return  The keyVal value
    */
   protected String getKeyVal()
   {
      return getParentForm().getTable().getKeyPositionString(getParentForm().getResultSetVector());
   }

}
