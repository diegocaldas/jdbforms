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
import org.apache.log4j.Category;
import org.dbforms.validation.ValidatorConstants;



/****
 *
 * <p>this tag renders an "Insert"-button.
 *
 * #fixme - define abstract base class [should be fixed in release 0.6]
 *
 * @author Joachim Peer <j.peer@gmx.net>
 */
public class DbInsertButtonTag extends DbBaseButtonTag
      implements javax.servlet.jsp.tagext.TryCatchFinally
{
   private static Category logCat = Category.getInstance(DbInsertButtonTag.class
         .getName());


   private String showAlways = "false";
	
	public void doFinally()
	{
		showAlways = "false";
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
	 * returns the JavaScript validation flags.
	 * Will be put into the onClick event of the main form
	 * Must be overloaded by update and delete button
	 *
	 * @return the java script validation vars.
	 */
	protected String JsValidation()
	{
		return  (ValidatorConstants.JS_CANCEL_VALIDATION	
		       + "=true;" 
		       + ValidatorConstants.JS_UPDATE_VALIDATION_MODE
			   + "=false;");
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
		
      logCat.info("pos DbInsertButtonTag 1");

      if (!"true".equalsIgnoreCase(showAlways)
               && !(getParentForm().getFooterReached()
               && ResultSetVector.isNull(getParentForm().getResultSetVector())))
      {
         // 20030521 HKK: Bug fixing, thanks to Michael Slack! 
         return SKIP_BODY;
      }

      /*
                      if (!parentForm.getFooterReached())
                              return SKIP_BODY; //  contrary to dbUpdate and dbDelete buttons!
      */
      logCat.info("pos DbInsertButtonTag 2");

      try
      {
         logCat.info("pos DbInsertButtonTag 3");

         StringBuffer tagBuf     = new StringBuffer();
         StringBuffer tagNameBuf = new StringBuffer();

         tagNameBuf.append("ac_insert_");
         tagNameBuf.append(getTable().getId());
         tagNameBuf.append("_");
         tagNameBuf.append(getParentForm().getPositionPathCore());

         // PG - Render the name unique
         tagNameBuf.append("_");
         tagNameBuf.append(Integer.toString(getUniqueID()));

         String tagName = tagNameBuf.toString();

         if (getFollowUp() != null)
         {
            tagBuf.append(getDataTag(tagName, "fu", getFollowUp()));
         }

         if (getFollowUpOnError() != null)
         {
            tagBuf.append(getDataTag(tagName, "fue", getFollowUpOnError()));
         }

         //tagBuf.append( getDataTag(tagName, "id", Integer.toString(parentForm.getFrozenCumulatedCount())) );
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

      if (getChoosenFlavor() == FLAVOR_MODERN)
      {
         return EVAL_BODY_BUFFERED;
      }
      else
      {
         return SKIP_BODY;
      }
   }


   /**
    * Returns the showAlways.
    * @return String
    */
   public String getShowAlways()
   {
      return showAlways;
   }


   /**
    * Sets the showAlways.
    * @param showAlways The showAlways to set
    */
   public void setShowAlways(String showAlways)
   {
      this.showAlways = showAlways;
   }
}
