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
import org.dbforms.util.Util;
import org.apache.log4j.Category;
import org.dbforms.event.eventtype.EventType;

import org.dbforms.util.MessageResources;
import javax.servlet.http.HttpServletRequest;

/****
 *
 * <p>This tag renders a Delete Button</p>
 *
 *
 *
 * @author Joachim Peer <j.peer@gmx.net>
 */
public class DbDeleteButtonTag extends DbBaseButtonTag
{
   static Category    logCat         = Category.getInstance(DbDeleteButtonTag.class
         .getName()); // logging category for this class
   private static int uniqueID;
   private String     confirmMessage = null;

   static
   {
      uniqueID = 1;
   }

   private String associatedRadio;

   /**
    * DOCUMENT ME!
    *
    * @param associatedRadio DOCUMENT ME!
    */
   public void setAssociatedRadio(String associatedRadio)
   {
      this.associatedRadio = associatedRadio;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getAssociatedRadio()
   {
      return associatedRadio;
   }


   /**
    * DOCUMENT ME!
    *
    * @param confirmMessage DOCUMENT ME!
    */
   public void setConfirmMessage(String confirmMessage)
   {
      this.confirmMessage = confirmMessage;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getConfirmMessage()
   {
      return confirmMessage;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws javax.servlet.jsp.JspException DOCUMENT ME!
    * @throws JspException DOCUMENT ME!
    */
   public int doStartTag() throws javax.servlet.jsp.JspException
   {
		super.doStartTag();
		
      DbDeleteButtonTag.uniqueID++; // make sure that we don't mix up buttons


      if (getConfirmMessage() != null)
      {
         String onclick = (getOnClick() != null) ? getOnClick() : "";

         if (onclick.lastIndexOf(";") != (onclick.length() - 1))
         {
            onclick += ";"; // be sure javascript end with ";"
         }

         String message = getConfirmMessage();

         if (getParentForm().getCaptionResource().equals("true"))
         {
            try
            {
               message = MessageResources.getMessage((HttpServletRequest)pageContext.getRequest(), getConfirmMessage());
            }
            catch (Exception e)
            {
               logCat.debug("confirm(" + caption + ") Exception : "
                  + e.getMessage());
            }
         }

         setOnClick(onclick + "return confirm('" + message + "');");
      }

      if (getParentForm().getFooterReached()
               && Util.isNull(getParentForm().getResultSetVector()))
      {
         // 20030521 HKK: Bug fixing, thanks to Michael Slack! 
         return SKIP_BODY;
      }

      try
      {
         // first, determinate the name of the button tag
         StringBuffer tagNameBuf = new StringBuffer("ac_delete");

         if (associatedRadio != null)
         {
            tagNameBuf.append("ar");
         }

         tagNameBuf.append("_");
         tagNameBuf.append(table.getId());

         if (associatedRadio == null)
         {
            tagNameBuf.append("_");
            tagNameBuf.append(getParentForm().getPositionPath());
         }

         // PG - Render the name unique
         tagNameBuf.append("_");
         tagNameBuf.append(uniqueID);

         String tagName = tagNameBuf.toString();

         // then render it and its associtated data-tags
         StringBuffer tagBuf = new StringBuffer();

         if (associatedRadio != null)
         {
            tagBuf.append(getDataTag(tagName, "arname", associatedRadio));
         }

         if (followUp != null)
         {
            tagBuf.append(getDataTag(tagName, "fu", followUp));
         }

         if (followUpOnError != null)
         {
            tagBuf.append(getDataTag(tagName, "fue", followUpOnError));
         }

         tagBuf.append(getButtonBegin());
         tagBuf.append(" name=\"");
         tagBuf.append(tagName);
			tagBuf.append(getButtonEnd());

         pageContext.getOut().write(tagBuf.toString());
      }
      catch (java.io.IOException ioe)
      {
         throw new JspException("IO Error: " + ioe.getMessage());
      }

      if (choosenFlavor == FLAVOR_MODERN)
      {
         return EVAL_BODY_BUFFERED;
      }
      else
      {
         return SKIP_BODY;
      }
   }


}
