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
import java.io.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import org.dbforms.*;
import org.dbforms.config.*;
import org.dbforms.util.*;
import org.CVS.*;
import org.apache.log4j.Category;



/****
 *
 * <p>the 3 examples below produce all the same result</p>
 *
 * <p><linkURL href="customer.jsp" table="customer" position="1:2:12-3:4:1992" /></p>
 *
 * <p><linkURL href="customer.jsp" table="customer" position="<%= currentKey %>" /></p>
 *
 * <p><linkURL href="customer.jsp" table="customer" />
 * <ul>  <position fieldName="id" value="103" /><br/>
 *   <position fieldName="cust_lang" value="2" /></ul>
 * </link>
 * </p>
 *
 * <p>result (off course without the line feeds)</p>
 *
 *
 * <pre>/servlet/control?
 * ac_goto_x=t&
 * data_ac_goto_x_fu=/customer.jsp&
 * data_ac_goto_x_destTable=17&
 * data_ac_goto_x_destPos=103~2</pre>
 *
 *
 * <p>Use it like this:</p>
 *
 *
 * <pre><a href="<linkURL href="customer.jsp" tableName="customer" position="103~2" />"> some text </a></pre>
 *
 * @author Joachim Peer <j.peer@gmx.net>
 */
public class DbLinkURLTag extends BodyTagSupport implements TryCatchFinally
{
   static Category       logCat     = Category.getInstance(DbLinkURLTag.class
         .getName()); // logging category for this class
   private DbFormsConfig config;
   private Table         table;
   private FieldValues   positionFv; // fields and their values, provided by embedded DbLinkPositionItem-Elements

   // -- properties
   private String    href;
   private String    tableName;
   private String    position;
   private DbFormTag parentForm;
   private String    keyToDestPos;
   private String    keyToKeyToDestPos;

   /**
    * used if parentTable is different to tableName:
    * field(s) in the main form that is/are linked to this form
    **/
   private String parentField;

   /**
    * used if parentTable is different to tableName:
    * field(s) in this forme that is/are linked to the parent form
    **/
   private String childField;

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getHref()
   {
      return href;
   }


   /**
    * DOCUMENT ME!
    *
    * @param href DOCUMENT ME!
    */
   public void setHref(String href)
   {
      this.href = href;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getTableName()
   {
      return tableName;
   }


   /**
    * DOCUMENT ME!
    *
    * @param tableName DOCUMENT ME!
    */
   public void setTableName(String tableName)
   {
      this.tableName = tableName;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getPosition()
   {
      return position;
   }


   /**
    * DOCUMENT ME!
    *
    * @param position DOCUMENT ME!
    */
   public void setPosition(String position)
   {
      this.position = position;
   }


   /**
   to be called by DbLinkPositonItems
   */
   public void addPositionPart(Field field, String value)
   {
      if (positionFv == null)
      {
         positionFv = new FieldValues();
      }

      // 2003-03-29 HKK: Change from Hashtable to FieldValueTable
      FieldValue fv = new FieldValue(field, value);
      positionFv.put(field.getName(), fv);
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public Table getTable()
   {
      return table;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws JspException  thrown when error occurs in processing the body of
    *                       this method

    * @throws IllegalArgumentException thrown when some parameters are missing.
    */
   public int doStartTag() throws javax.servlet.jsp.JspException
   {
      // determinate table
      if (this.tableName != null)
      {
         this.table = config.getTableByName(tableName);
      }
      else if (this.parentForm != null)
      { // we must try if we get info from parentForm
         this.table = parentForm.getTable();
      }
      else
      {
         throw new IllegalArgumentException(
            "no table specified. either you define expliclty the attribute \"tableName\" or you put this tag inside a db:form!");
      }

      if (position == null) // if position was not set explicitly,
      {
         return EVAL_BODY_BUFFERED; // we have to evaluate body and hopefully find DbLinkPositionItems there
      }
      else
      {
         return SKIP_BODY; // if position was provided we don't need to look into body
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws JspException  thrown when error occurs in processing the body of
    *                       this method
    */
   public int doBodyEndTag() throws javax.servlet.jsp.JspException
   {
      return SKIP_BODY;
   }


   private String getDataTag(String primaryTagName, String dataKey,
      String dataValue)
   {
      String s = "";

      if (!Util.isNull(dataValue))
      {
         StringBuffer tagBuf = new StringBuffer();
         tagBuf.append(primaryTagName);
         tagBuf.append("_");
         tagBuf.append(dataKey);
         tagBuf.append("=");
         tagBuf.append(dataValue);
         tagBuf.append("&");
         s = tagBuf.toString();
      }

      return s;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws JspException  thrown when error occurs in processing the body of
    *                       this method
    */
   public int doEndTag() throws javax.servlet.jsp.JspException
   {
      try
      {
         // determinate position inside table (key)
         if (this.position == null)
         { // not explic. def. by attribute

            if (positionFv != null)
            { // but (maybe) defined by sub-elements (DbLinkPositionItem)
               position = table.getKeyPositionString(positionFv);
            }
         }

         // build tag
         StringBuffer       tagBuf      = new StringBuffer(200);
         HttpServletRequest request     = (HttpServletRequest) pageContext
            .getRequest();
         String             contextPath = request.getContextPath();
         tagBuf.append(contextPath);

         // 2002-01-17 Fix contributed by Dirk Kraemer and Bertram Gong//
         if (!contextPath.endsWith("/"))
         {
            tagBuf.append("/");
         }

         tagBuf.append("servlet/control?");

         String tagName = "ac_goto";
         tagBuf.append(getDataTag(tagName, "x", "t"));

         tagName = "data" + tagName + "_x";
         tagBuf.append(getDataTag(tagName, "fu", href));

         // table is required. we force to define a valid table.
         // because we do not want the developer to use this tag instead of
         // normal <a href="">-tags to arbitrary (static) ressources, as this would slow down the application.
         tagBuf.append(getDataTag(tagName, "destTable", table.getName()));

         // position within table is not required.
         // if no position was provided/determinated, dbForm will navigate to the first row
         // 2002-11-20 HKK: Fixed encoding bug!
         tagBuf.append(getDataTag(tagName, "destPos", Util.encode(position)));

         // 2002-11-21 HKK: Allow same keys as in dbgotobutton
         tagBuf.append(getDataTag(tagName, "keyToDestPos",
               Util.encode(keyToDestPos)));
         tagBuf.append(getDataTag(tagName, "keyToKeyDestPos",
               Util.encode(keyToKeyToDestPos)));

         // 2002-11-21 HKK: New: send parent table name as parameter if it is different to table
         if (table != parentForm.getTable())
         {
            tagBuf.append(getDataTag(tagName, "srcTable",
                  parentForm.getTable().getName()));
            tagBuf.append(getDataTag(tagName, "childField",
                  Util.encode(childField)));
            tagBuf.append(getDataTag(tagName, "parentField",
                  Util.encode(parentField)));
         }

         HttpServletResponse response = (HttpServletResponse) pageContext
            .getResponse();
         String              s = tagBuf.toString();
         s    = s.substring(0, s.length() - 1);
         s    = response.encodeUrl(s);
         pageContext.getOut().write(s);
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


   /**
    * DOCUMENT ME!
    *
    * @param pageContext DOCUMENT ME!
    */
   public void setPageContext(final javax.servlet.jsp.PageContext pageContext)
   {
      super.setPageContext(pageContext);
      this.config = (DbFormsConfig) pageContext.getServletContext()
                                               .getAttribute(DbFormsConfig.CONFIG);
   }


   /**
    * DOCUMENT ME!
    *
    * @param parent DOCUMENT ME!
    */
   public void setParent(final javax.servlet.jsp.tagext.Tag parent)
   {
      super.setParent(parent);
      this.parentForm = (DbFormTag) findAncestorWithClass(this, DbFormTag.class); // may be null!
   }


   /**
    * Returns the keyToDestPos.
    * @return String
    */
   public String getKeyToDestPos()
   {
      return keyToDestPos;
   }


   /**
    * Returns the keyToKeyToDestPos.
    * @return String
    */
   public String getKeyToKeyToDestPos()
   {
      return keyToKeyToDestPos;
   }


   /**
    * Sets the keyToDestPos.
    * @param keyToDestPos The keyToDestPos to set
    */
   public void setKeyToDestPos(String keyToDestPos)
   {
      this.keyToDestPos = keyToDestPos;
   }


   /**
    * Sets the keyToKeyToDestPos.
    * @param keyToKeyToDestPos The keyToKeyToDestPos to set
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
   public String getDestPos()
   {
      return position;
   }


   /**
    * DOCUMENT ME!
    *
    * @param position DOCUMENT ME!
    */
   public void setDestPos(String position)
   {
      this.position = position;
   }


   /**
    * Returns the childField.
    * @return String
    */
   public String getChildField()
   {
      return childField;
   }


   /**
    * Returns the parentField.
    * @return String
    */
   public String getParentField()
   {
      return parentField;
   }


   /**
    * Sets the childField.
    * @param childField The childField to set
    */
   public void setChildField(String childField)
   {
      this.childField = childField;
   }


   /**
    * Sets the parentField.
    * @param parentField The parentField to set
    */
   public void setParentField(String parentField)
   {
      this.parentField = parentField;
   }


   /**
    * DOCUMENT ME!
    */
   public void doFinally()
   {
      logCat.info("doFinally called");
      position = null;

      if (positionFv != null)
      {
         positionFv.clear();
      }

      positionFv = null;
   }


   /**
    * DOCUMENT ME!
    *
    * @param  t DOCUMENT ME!
    * @throws  Throwable DOCUMENT ME!
    */
   public void doCatch(Throwable t) throws Throwable
   {
      logCat.info("doCatch called - " + t.toString());
      throw t;
   }
}
