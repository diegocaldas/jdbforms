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

import org.dbforms.config.ResultSetVector;
import org.dbforms.event.eventtype.EventType;


/****
 *
 * <p>this tag renders an "previous"-button.
 *
 *
 * @author Joachim Peer <j.peer@gmx.net>
 */
public class DbNavPrevButtonTag extends DbBaseButtonTag
      implements javax.servlet.jsp.tagext.TryCatchFinally
{
   private String  stepWidth;

	public void doFinally()
	{
		stepWidth = null;
		super.doFinally();
	}

   /**
    * @see javax.servlet.jsp.tagext.TryCatchFinally#doCatch(java.lang.Throwable)
    */
   public void doCatch(Throwable t) throws Throwable
   {
      throw t;
   }


   /**
    * DOCUMENT ME!
    *
    * @param stepWidth DOCUMENT ME!
    */
   public void setStepWidth(String stepWidth)
   {
      this.stepWidth = stepWidth;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getStepWidth()
   {
      return stepWidth;
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
		
      if (getParentForm().getFooterReached()
               && ResultSetVector.isNull(getParentForm().getResultSetVector()))
      {
         // 20030521 HKK: Bug fixing, thanks to Michael Slack! 
         return SKIP_BODY;
      }

      try
      {
         StringBuffer tagBuf  = new StringBuffer();
         String       tagName = EventType.EVENT_NAVIGATION_TRANSFER_PREV + table.getId() + "_" + Integer.toString(getUniqueID());

         if (stepWidth != null)
         {
            tagBuf.append(getDataTag(tagName, "sw", stepWidth));
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
			if (getParentForm().getResultSetVector().isFirstPage()) 
				tagBuf.append(" disabled=\"true\"");
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
