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
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

import org.dbforms.util.*;
import org.dbforms.config.*;
import org.apache.log4j.Category;



/**
 * <p>renders a input field for searching with special default search modes.</p>
 * <p>example:</p>
        &lt;input type="hidden" name="searchalgo_0_1" value="weakEnd"/&gt;<br/>
        &lt;input type="hidden" name="searchmode_0_1" value="AND"/&gt;<br/>
        &lt;input type="input" name="search_0_1"/&gt;<br/>
 *
 *  searchalgo and searchmode are set by parameter.
 *
 * @author Henner Kollmann  (Henner.Kollmann@gmx.de)
 */
public class DbSearchTag extends TagSupportWithScriptHandler
{
   static Category     logCat       = Category.getInstance(DbSortTag.class
         .getName()); // logging category for this class

   /** DOCUMENT ME! */
   protected DbFormTag parentForm;
   private String      fieldName;
   private String      searchAlgo   = "sharp";
   private String      searchMode   = "and";
   private String      defaultValue = null;
   private String      id           = null;

   /**
    * DOCUMENT ME!
    *
    * @param fieldName DOCUMENT ME!
    */
   public void setFieldName(String fieldName)
   {
      this.fieldName = fieldName;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getFieldName()
   {
      return fieldName;
   }


   /**
    * DOCUMENT ME!
    *
    * @param searchAlgo DOCUMENT ME!
    */
   public void setSearchAlgo(String searchAlgo)
   {
      this.searchAlgo = searchAlgo;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getSearchAlgo()
   {
      return searchAlgo;
   }


   /**
    * DOCUMENT ME!
    *
    * @param searchMode DOCUMENT ME!
    */
   public void setSearchMode(String searchMode)
   {
      this.searchMode = searchMode;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getSearchMode()
   {
      return searchMode;
   }


   /**
    * DOCUMENT ME!
    *
    * @param value DOCUMENT ME!
    */
   public void setDefault(String value)
   {
      this.defaultValue = value;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getDefault()
   {
      return defaultValue;
   }


   /**
    * DOCUMENT ME!
    *
    * @param tableId DOCUMENT ME!
    * @param fieldId DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   protected String RenderHiddenFields(int tableId, int fieldId)
   {
      StringBuffer tagBuf        = new StringBuffer();
      StringBuffer paramNameBufA = new StringBuffer();
      paramNameBufA.append("searchalgo_");
      paramNameBufA.append(tableId);
      paramNameBufA.append("_");
      paramNameBufA.append(fieldId);
      tagBuf.append("<input type=\"hidden\" name=\"");
      tagBuf.append(paramNameBufA.toString());
      tagBuf.append("\" value=\"");
      tagBuf.append(getSearchAlgo());
      tagBuf.append("\"/>\n");

      StringBuffer paramNameBufB = new StringBuffer();
      paramNameBufB.append("searchmode_");
      paramNameBufB.append(tableId);
      paramNameBufB.append("_");
      paramNameBufB.append(fieldId);
      tagBuf.append("<input type=\"hidden\" name=\"");
      tagBuf.append(paramNameBufB.toString());
      tagBuf.append("\" value=\"");
      tagBuf.append(getSearchMode());
      tagBuf.append("\"/>\n");

      return tagBuf.toString();
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
         int   tableId = parentForm.getTable().getId();
         Field field   = parentForm.getTable().getFieldByName(fieldName);
         int   fieldId = field.getId();

         /*
                            <input type="hidden" name="searchalgo_0_1" value="weakEnd"/>
                            <input type="hidden" name="searchmode_0_1" value="AND"/>
                            <input type="input" name="search_0_1"/>
         */
         HttpServletRequest request = (HttpServletRequest) pageContext
            .getRequest();
         StringBuffer       tagBuf = new StringBuffer();

         StringBuffer       paramNameBuf = new StringBuffer();
         paramNameBuf.append("search_");
         paramNameBuf.append(tableId);
         paramNameBuf.append("_");
         paramNameBuf.append(fieldId);

         String oldValue = ParseUtil.getParameter(request,
               paramNameBuf.toString());
         tagBuf.append("<input type=\"input\" name=\"");
         tagBuf.append(paramNameBuf.toString());
         tagBuf.append("\" ");

         if (!Util.isNull(getId()))
         {
            tagBuf.append("id=\"");
            tagBuf.append(getId());
            tagBuf.append("\" ");
         }

         tagBuf.append("value=\"");

         if (oldValue != null)
         {
            tagBuf.append(oldValue);
         }
         else if (defaultValue != null)
         {
            tagBuf.append(defaultValue);
         }

         tagBuf.append("\"");
         tagBuf.append(prepareStyles());
         tagBuf.append(prepareEventHandlers());
         tagBuf.append("/>\n");

         pageContext.getOut().write(RenderHiddenFields(tableId, fieldId));
         pageContext.getOut().write(tagBuf.toString());
      }
      catch (java.io.IOException ioe)
      {
         throw new JspException("IO Error: " + ioe.getMessage());
      }

      return EVAL_PAGE;
   }


   /**
    * DOCUMENT ME!
    *
    * @param parent DOCUMENT ME!
    */
   public void setParent(final javax.servlet.jsp.tagext.Tag parent)
   {
      super.setParent(parent);
      this.parentForm = (DbFormTag) findAncestorWithClass(this, DbFormTag.class);
   }


   /**
    * Returns the id.
    * @return String
    */
   public String getId()
   {
      return id;
   }


   /**
    * Sets the id.
    * @param id The id to set
    */
   public void setId(String id)
   {
      this.id = id;
   }
}