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

import org.dbforms.util.ParseUtil;
import org.dbforms.event.ReloadEvent;
import org.dbforms.event.WebEvent;
import org.apache.log4j.Category;
import javax.servlet.http.*;



/****
 *
 * <p>This tag renders a HTML TextArea - Element</p>
 *
 * this tag renders a dabase-datadriven textArea, which is an active element - the user
 * can change data
 *
 * @author Joachim Peer <j.peer@gmx.net>
 */
public class DbTextAreaTag extends DbBaseInputTag
{
   static Category logCat = Category.getInstance(DbTextAreaTag.class.getName());

   /** DOCUMENT ME! */
   protected String wrap;

   /** DOCUMENT ME! */
   protected String renderBody;

   /** DOCUMENT ME! */
   protected java.lang.String overrideValue;

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

      HttpServletRequest request = (HttpServletRequest) this.pageContext
         .getRequest();
      Vector             errors = (Vector) request.getAttribute("errors");
      WebEvent           we     = (WebEvent) request.getAttribute("webEvent");

      StringBuffer       tagBuf = new StringBuffer("<textarea name=\"");
      tagBuf.append(getFormFieldName());
      tagBuf.append("\" ");

      if (cols != null)
      {
         tagBuf.append(" cols=\"");
         tagBuf.append(cols);
         tagBuf.append("\"");
      }

      if (wrap != null)
      {
         tagBuf.append(" wrap=\"");
         tagBuf.append(wrap);
         tagBuf.append("\"");
      }

      if (rows != null)
      {
         tagBuf.append(" rows=\"");
         tagBuf.append(rows);
         tagBuf.append("\"");
      }

      if (accessKey != null)
      {
         tagBuf.append(" accesskey=\"");
         tagBuf.append(accessKey);
         tagBuf.append("\"");
      }

      if (tabIndex != null)
      {
         tagBuf.append(" tabindex=\"");
         tagBuf.append(tabIndex);
         tagBuf.append("\"");
      }

      tagBuf.append(prepareStyles());
      tagBuf.append(prepareEventHandlers());
      tagBuf.append(">");

      /* If the overrideValue attribute has been set, use its value instead of the one
      retrieved from the database.  This mechanism can be used to set an initial default
      value for a given field. */
      if (!"true".equals(renderBody))
      {
         if (this.getOverrideValue() != null)
         {
            //If the redisplayFieldsOnError attribute is set and we are in error mode, forget override!
            if (("true".equals(parentForm.getRedisplayFieldsOnError())
                     && (errors != null) && (errors.size() > 0))
                     || (we instanceof ReloadEvent))
            {
               tagBuf.append(getFormFieldValue());
            }
            else
            {
               tagBuf.append(this.getOverrideValue());
            }
         }
         else
         {
            if (we instanceof ReloadEvent)
            {
               String oldValue = ParseUtil.getParameter(request,
                     getFormFieldName());

               if (oldValue != null)
               {
                  tagBuf.append(oldValue);
               }
               else
               {
                  tagBuf.append(getFormFieldValue());
               }
            }
            else
            {
               tagBuf.append(getFormFieldValue());
            }
         }
      }

      try
      {
         pageContext.getOut().write(tagBuf.toString());
      }
      catch (java.io.IOException e)
      {
         throw new JspException("IO Error: " + e.getMessage());
      }

      if (!"true".equals(renderBody))
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
      try
      {
         if ("true".equals(renderBody) && (bodyContent != null))
         {
            bodyContent.writeOut(bodyContent.getEnclosingWriter());
            bodyContent.clearBody(); // workaround for duplicate rows in JRun 3.1
         }

         pageContext.getOut().write("</textArea>");

         // For generation Javascript Validation.  Need all original and modified fields name
         parentForm.addChildName(getFieldName(), getFormFieldName());
      }
      catch (java.io.IOException ioe)
      {
         throw new JspException("IO Error: " + ioe.getMessage());
      }

      return EVAL_PAGE;
   }


   /**
    * Insert the method's description here.
    * Creation date: (2001-06-27 17:44:16)
    * @return java.lang.String
    */
   public java.lang.String getOverrideValue()
   {
      return overrideValue;
   }


   /**
    * Insert the method's description here.
    * Creation date: (2001-06-27 17:44:16)
    * @param newOverrideValue java.lang.String
    */
   public void setOverrideValue(java.lang.String newOverrideValue)
   {
      overrideValue = newOverrideValue;
   }


   /**
    * Gets the renderBody
    * @return Returns a String
    */
   public String getRenderBody()
   {
      return renderBody;
   }


   /**
    * Sets the renderBody
    * @param renderBody The renderBody to set
    */
   public void setRenderBody(String renderBody)
   {
      this.renderBody = renderBody;
   }


   /**
    * DOCUMENT ME!
    *
    * @param wrap DOCUMENT ME!
    */
   public void setWrap(String wrap)
   {
      this.wrap = wrap;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getWrap()
   {
      return wrap;
   }
}