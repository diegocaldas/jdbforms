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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.jsp.JspException;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.Field;
import org.dbforms.config.Table;

import org.dbforms.util.FieldValue;
import org.dbforms.util.FieldValues;
import org.dbforms.util.Util;

import org.dbforms.validation.ValidatorConstants;

import org.apache.log4j.Category;



/****
 *
 * <p>this tag renders a Goto-button.
 *
 * @author Joachim Peer 
 */
public class DbGotoButtonTag extends DbBaseButtonTag
{
   static Category    logCat            = Category.getInstance(DbGotoButtonTag.class
         .getName()); // logging category for this class
   private String     destination;
   private String     destTable;
   private String     destPos;
   private String     keyToDestPos;
   private String     keyToKeyToDestPos;
	private String     singleRow = "false";

   private static int uniqueID = 1;


   /**
    * DOCUMENT ME!
    *
    * @param destination DOCUMENT ME!
    */
   public void setDestination(String destination)
   {
      this.destination = destination;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getDestination()
   {
      return destination;
   }


   /**
    * DOCUMENT ME!
    *
    * @param destTable DOCUMENT ME!
    */
   public void setDestTable(String destTable)
   {
      this.destTable = destTable;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getDestTable()
   {
      return destTable;
   }


   /**
    * DOCUMENT ME!
    *
    * @param destPos DOCUMENT ME!
    */
   public void setDestPos(String destPos)
   {
      this.destPos = destPos;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getDestPos()
   {
      return destPos;
   }


   /**
    * DOCUMENT ME!
    *
    * @param keyToDestPos DOCUMENT ME!
    */
   public void setKeyToDestPos(String keyToDestPos)
   {
      this.keyToDestPos = keyToDestPos;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getKeyToDestPos()
   {
      return keyToDestPos;
   }


   /**
    * DOCUMENT ME!
    *
    * @param keyToKeyToDestPos DOCUMENT ME!
    */
   public void setKeyToKeyToDestPos(String keyToKeyToDestPos)
   {
      this.keyToKeyToDestPos = keyToKeyToDestPos;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getKeyToKeyToDestPos()
   {
      return keyToKeyToDestPos;
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
      DbGotoButtonTag.uniqueID++; // make sure that we don't mix up buttons

      if ((parentForm.getFormValidatorName() != null)
               && (parentForm.getFormValidatorName().length() > 0)
               && parentForm.getJavascriptValidation().equals("true"))
      {
         String onclick = (getOnClick() != null) ? getOnClick() : "";

         if (onclick.lastIndexOf(";") != (onclick.length() - 1))
         {
            onclick += ";"; // be sure javascript end with ";"
         }

         setOnClick(onclick + ValidatorConstants.JS_CANCEL_VALIDATION
            + "=false;");
      }

      try
      {
         String       tagName = "ac_goto_" + uniqueID;

         StringBuffer tagBuf = new StringBuffer();

         // mask destination as "fu" (FollowUp), so that we can use standard-event dispatching facilities
         // from Controller and dont have to invent something new!
         // #checkme: should we rename destination to followUp ?
         if (destination != null)
         {
            tagBuf.append(getDataTag(tagName, "fu", destination));
         }

         if (destTable != null)
         {
            tagBuf.append(getDataTag(tagName, "destTable", destTable));
         }

         if (destPos != null)
         {
            tagBuf.append(getDataTag(tagName, "destPos", destPos));
         }

         if (keyToDestPos != null)
         {
            tagBuf.append(getDataTag(tagName, "keyToDestPos", keyToDestPos));
         }

         if (keyToKeyToDestPos != null)
         {
            tagBuf.append(getDataTag(tagName, "keyToKeyToDestPos",
                  keyToKeyToDestPos));
         }

			tagBuf.append(getDataTag(tagName, "singleRow", getSingleRow()));


         tagBuf.append(getButtonBegin());
         tagBuf.append(" name=\"");
         tagBuf.append(tagName);
         tagBuf.append("\">");

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


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws javax.servlet.jsp.JspException DOCUMENT ME!
    * @throws JspException DOCUMENT ME!
    */
   public int doEndTag() throws javax.servlet.jsp.JspException
   {
      if (choosenFlavor == FLAVOR_MODERN)
      {
         if (parentForm.getFooterReached()
                  && Util.isNull(parentForm.getResultSetVector()))
         {
            return EVAL_PAGE;
         }

         try
         {
            if (bodyContent != null)
            {
               bodyContent.writeOut(bodyContent.getEnclosingWriter());
            }

            pageContext.getOut().write("</button>");
         }
         catch (java.io.IOException ioe)
         {
            throw new JspException("IO Error: " + ioe.getMessage());
         }
      }

      return EVAL_PAGE;
   }

	/**
	 * @return the attribute
	 */
	public String getSingleRow()
	{
		return singleRow;
	}

	/**
	 * @param string
	 */
	public void setSingleRow(String string)
	{
		singleRow = string;
	}

}
