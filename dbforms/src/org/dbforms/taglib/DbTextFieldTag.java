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

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.dbforms.util.ParseUtil;
import org.dbforms.event.ReloadEvent;
import org.dbforms.event.WebEvent;
import org.apache.log4j.Category;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;



/****
 *
 * <p>This tag renders a HTML TextArea - Element</p>
 *
 * this tag renders a dabase-datadriven textArea, which is an active element - the user
 * can change data
 *
 * @author Joachim Peer
 */
public class DbTextFieldTag extends DbBaseInputTag
{

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws javax.servlet.jsp.JspException DOCUMENT ME!
    */
   public int doStartTag() throws javax.servlet.jsp.JspException
   {
      super.doStartTag();

      return SKIP_BODY;
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

      try
      {
         /* Does the developer require the field to be hidden, displayed or displayed as password? */
         String value = null;

         if ("true".equals(this.getHidden()))
         {
            value = "<input type=\"hidden\" name=\"";
         }
         else if ("true".equals(this.getPassword()))
         {
            value = "<input type=\"password\" name=\"";
         }
         else
         {
            value = "<input type=\"text\" name=\"";
         }

         StringBuffer tagBuf = new StringBuffer(value);
         tagBuf.append(getFormFieldName());
         tagBuf.append("\" value=\"");
			tagBuf.append(getFormFieldValue());
         tagBuf.append("\" ");

         if (getAccessKey() != null)
         {
            tagBuf.append(" accesskey=\"");
            tagBuf.append(getAccessKey());
            tagBuf.append("\"");
         }

         if (maxlength != null)
         {
            tagBuf.append(" maxlength=\"");
            tagBuf.append(maxlength);
            tagBuf.append("\"");
         }

         if (cols != null)
         {
            tagBuf.append(" size=\"");
            tagBuf.append(cols);
            tagBuf.append("\"");
         }

         if (getTabIndex() != null)
         {
            tagBuf.append(" tabindex=\"");
            tagBuf.append(getTabIndex());
            tagBuf.append("\"");
         }

         tagBuf.append(prepareStyles());
         tagBuf.append(prepareEventHandlers());
         tagBuf.append("/>");

         pageContext.getOut().write(tagBuf.toString());
         
			// Writes out the old field value
			writeOutOldValue();

         // For generation Javascript Validation.  Need all original and modified fields name
			getParentForm().addChildName(getFieldName(), getFormFieldName());
      }
      catch (java.io.IOException ioe)
      {
         throw new JspException("IO Error: " + ioe.getMessage());
      }

      return EVAL_PAGE;
   }


	private java.lang.String password      = "false";

   /**
    *  Determines if the text field should be a password text field (display '****')
    */
   public void setPassword(String pwd)
   {
      this.password = pwd;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getPassword()
   {
      return this.password;
   }
}
