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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.config.Field;
import org.dbforms.config.FieldValue;
import org.dbforms.config.FieldValues;
import org.dbforms.config.Table;

import org.dbforms.util.Util;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;



/**
 * <p>
 * the 3 examples below produce all the same result
 * </p>
 *
 * <p>
 * &lt;linkURL href="customer.jsp" table="customer" position="1:2:12-3:4:1992"
 * /&gt;
 * </p>
 *
 * <p>
 * &lt;linkURL href="customer.jsp" table="customer" position="&lt;%= currentKey
 * %&gt;" /&gt;
 * </p>
 *
 * <p>
 * &lt;linkURL href="customer.jsp" table="customer" /&gt; &lt;position
 * fieldName="id" value="103" /&gt; &lt;position fieldName="cust_lang"
 * value="2" /&gt; &lt;/linkURL&gt;
 * </p>
 *
 * <p>
 * result (off course without the line feeds)
 * </p>
 * <pre>/servlet/control?
 * ac_goto_x=t&
 * data_ac_goto_x_fu=/customer.jsp&
 * data_ac_goto_x_destTable=17&
 * data_ac_goto_x_destPos=103~2</pre>
 *
 * <p>
 * Use it like this:
 * </p>
 * <pre><a href="&lt;linkURL href="customer.jsp" tableName="customer" position="103~2" /&gt;"> some text </a></pre>
 *
 * @author Joachim Peer
 */
public class DbLinkURLTag extends DbBaseHandlerTag
   implements javax.servlet.jsp.tagext.TryCatchFinally {
   private static Log            logCat = LogFactory.getLog(DbLinkURLTag.class
                                                            .getName()); // logging category for this class
   private transient FieldValues positionFv; // fields and their values, provided by embedded DbLinkPositionItem-Elements

   /**
    * used if parentTable is different to tableName: field(s) in this forme
    * that is/are linked to the parent form
    */
   private String childField;

   // -- properties
   private String href;
   private String keyToDestPos;
   private String keyToKeyToDestPos;

   /**
    * used if parentTable is different to tableName: field(s) in the main form
    * that is/are linked to this form
    */
   private String parentField;
   private String position;
   private String singleRow = "false";
   private String tableName;

   /**
    * Sets the childField.
    *
    * @param childField The childField to set
    */
   public void setChildField(String childField) {
      this.childField = childField;
   }


   /**
    * Returns the childField.
    *
    * @return String
    */
   public String getChildField() {
      return childField;
   }


   /**
    * DOCUMENT ME!
    *
    * @param position DOCUMENT ME!
    */
   public void setDestPos(String position) {
      this.position = position;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getDestPos() {
      return position;
   }


   /**
    * DOCUMENT ME!
    *
    * @param href DOCUMENT ME!
    */
   public void setHref(String href) {
      this.href = href;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getHref() {
      return href;
   }


   /**
    * Sets the keyToDestPos.
    *
    * @param keyToDestPos The keyToDestPos to set
    */
   public void setKeyToDestPos(String keyToDestPos) {
      this.keyToDestPos = keyToDestPos;
   }


   /**
    * Returns the keyToDestPos.
    *
    * @return String
    */
   public String getKeyToDestPos() {
      return keyToDestPos;
   }


   /**
    * Sets the keyToKeyToDestPos.
    *
    * @param keyToKeyToDestPos The keyToKeyToDestPos to set
    */
   public void setKeyToKeyToDestPos(String keyToKeyToDestPos) {
      this.keyToKeyToDestPos = keyToKeyToDestPos;
   }


   /**
    * Returns the keyToKeyToDestPos.
    *
    * @return String
    */
   public String getKeyToKeyToDestPos() {
      return keyToKeyToDestPos;
   }


   /**
    * Sets the parentField.
    *
    * @param parentField The parentField to set
    */
   public void setParentField(String parentField) {
      this.parentField = parentField;
   }


   /**
    * Returns the parentField.
    *
    * @return String
    */
   public String getParentField() {
      return parentField;
   }


   /**
    * DOCUMENT ME!
    *
    * @param position DOCUMENT ME!
    */
   public void setPosition(String position) {
      this.position = position;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getPosition() {
      return position;
   }


   /**
    * DOCUMENT ME!
    *
    * @param string
    */
   public void setSingleRow(String string) {
      singleRow = string;
   }


   /**
    * DOCUMENT ME!
    *
    * @return the attribute
    */
   public String getSingleRow() {
      return singleRow;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public Table getTable() {
      if (!Util.isNull(tableName)) {
         return getConfig()
                   .getTableByName(tableName);
      } else if (getParentForm() != null) { // we must try if we get info from parentForm

         return getParentForm()
                   .getTable();
      } else {
         throw new IllegalArgumentException("no table specified. either you define expliclty the attribute \"tableName\" or you put this tag inside a db:form!");
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param tableName DOCUMENT ME!
    */
   public void setTableName(String tableName) {
      this.tableName = tableName;
   }


   /**
    * to be called by DbLinkPositonItems
    *
    * @param field DOCUMENT ME!
    * @param value DOCUMENT ME!
    */
   public void addPositionPart(Field  field,
                               String value) {
      if (positionFv == null) {
         positionFv = new FieldValues();
      }

      // 2003-03-29 HKK: Change from Hashtable to FieldValueTable
      FieldValue fv = new FieldValue(field, value);
      positionFv.put(fv);
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws JspException thrown when error occurs in processing the body of
    *         this method
    */
   public int doBodyEndTag() throws javax.servlet.jsp.JspException {
      return SKIP_BODY;
   }


   /**
    * @see javax.servlet.jsp.tagext.TryCatchFinally#doCatch(java.lang.Throwable)
    */
   public void doCatch(Throwable t) throws Throwable {
      throw t;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws JspException thrown when error occurs in processing the body of
    *         this method
    */
   public int doEndTag() throws javax.servlet.jsp.JspException {
      try {
         HttpServletResponse response = (HttpServletResponse) pageContext
                                        .getResponse();

         String              s = makeUrl();
         s = response.encodeURL(s);
         pageContext.getOut()
                    .write(s);
      } catch (java.io.IOException ioe) {
         throw new JspException("IO Error: " + ioe.getMessage());
      } catch (Exception e) {
         throw new JspException("Error: " + e.getMessage());
      }

      return EVAL_PAGE;
   }


   /**
    * DOCUMENT ME!
    */
   public void doFinally() {
      logCat.info("doFinally called");
      position = null;

      if (positionFv != null) {
         positionFv.clear();
      }

      positionFv        = null;
      href              = null;
      tableName         = null;
      position          = null;
      keyToDestPos      = null;
      keyToKeyToDestPos = null;
      singleRow         = "false";

      super.doFinally();
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws JspException thrown when error occurs in processing the body of
    *         this method
    * @throws IllegalArgumentException thrown when some parameters are missing.
    */
   public int doStartTag() throws javax.servlet.jsp.JspException {
      if (Util.isNull(getPosition())) { // if position was not set explicitly,

         return EVAL_BODY_BUFFERED; // we have to evaluate body and hopefully find DbLinkPositionItems there
      } else {
         return SKIP_BODY; // if position was provided we don't need to look into body
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws UnsupportedEncodingException DOCUMENT ME!
    */
   protected String makeUrl() throws UnsupportedEncodingException {
      // determinate position inside table (key)
      if (this.position == null) { // not explic. def. by attribute

         if (positionFv != null) { // but (maybe) defined by sub-elements (DbLinkPositionItem)
            position = getTable()
                          .getKeyPositionString(positionFv);
         }
      }

      // build tag
      StringBuffer       tagBuf  = new StringBuffer(200);
      HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
      String             contextPath = request.getContextPath();
      tagBuf.append(contextPath);

      // 2002-01-17 Fix contributed by Dirk Kraemer and Bertram Gong//
      if (!contextPath.endsWith("/")) {
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
      tagBuf.append(getDataTag(tagName, "destTable", getTable().getName()));

      // position within table is not required.
      // if no position was provided/determinated, dbForm will navigate to the first row
      // 2002-11-20 HKK: Fixed encoding bug!
      tagBuf.append(getDataTag(tagName, "destPos",
                               Util.encode(position,
                                           pageContext.getRequest().getCharacterEncoding())));

      // 2002-11-21 HKK: Allow same keys as in dbgotobutton
      tagBuf.append(getDataTag(tagName, "keyToDestPos",
                               Util.encode(keyToDestPos,
                                           pageContext.getRequest().getCharacterEncoding())));
      tagBuf.append(getDataTag(tagName, "keyToKeyDestPos",
                               Util.encode(keyToKeyToDestPos,
                                           pageContext.getRequest().getCharacterEncoding())));

      // 2002-11-21 HKK: New: send parent table name as parameter if it is different to table
      if (getTable() != getParentForm()
                                 .getTable()) {
         tagBuf.append(getDataTag(tagName, "srcTable",
                                  getParentForm().getTable().getName()));
         tagBuf.append(getDataTag(tagName, "childField",
                                  Util.encode(childField,
                                              pageContext.getRequest().getCharacterEncoding())));
         tagBuf.append(getDataTag(tagName, "parentField",
                                  Util.encode(parentField,
                                              pageContext.getRequest().getCharacterEncoding())));
      }

      tagBuf.append(getDataTag(tagName, "singleRow", getSingleRow()));

      String s = tagBuf.toString();
      s = s.substring(0, s.length() - 1);

      return s;
   }


   private String getDataTag(String primaryTagName,
                             String dataKey,
                             String dataValue) {
      String s = "";

      if (!Util.isNull(dataValue)) {
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
}
