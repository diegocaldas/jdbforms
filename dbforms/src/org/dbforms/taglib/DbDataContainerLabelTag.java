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

import org.dbforms.config.ResultSetVector;

import org.dbforms.util.KeyValuePair;
import org.dbforms.util.Util;

import java.util.List;

import javax.servlet.jsp.JspException;



/**
 * this tag renders a dabase-datadriven LABEL, which is a passive element (it
 * can't be changed by the user) - it is predestinated for use with read-only
 * data (i.e. primary keys you don't want the user to change, etc) so far it
 * is equivalent to DbLabelTag. But this tag may have a body containing any
 * kind of EmbeddedData - tag. i put this feature into a seperate class for
 * performance reasons (we do not want the overhead of pushing and poping the
 * jsp writer to and off the stack
 *
 * @author Joachim Peer
 */
public class DbDataContainerLabelTag extends DbBaseHandlerTag
   implements DataContainer, javax.servlet.jsp.tagext.TryCatchFinally {
   // logging category for this class
   private List   embeddedData = null;
   private Log    logCat = LogFactory.getLog(this.getClass().getName());
   private String strict = "false";

   /**
    * This method is a "hookup" for EmbeddedData - Tags which can assign the
    * lines of data they loaded (by querying a database, or by rendering
    * data-subelements, etc. etc.) and make the data available to this tag.
    * [this method is defined in Interface DataContainer]
    *
    * @param embeddedData DOCUMENT ME!
    */
   public void setEmbeddedData(List embeddedData) {
      this.embeddedData = embeddedData;
   }


   /**
    * DOCUMENT ME!
    *
    * @param string
    */
   public void setStrict(String string) {
      strict = string;
   }


   /**
    * DOCUMENT ME!
    *
    * @return
    */
   public String getStrict() {
      return strict;
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
    * @throws javax.servlet.jsp.JspException DOCUMENT ME!
    * @throws JspException DOCUMENT ME!
    */
   public int doEndTag() throws javax.servlet.jsp.JspException {
      try {
         String fieldValue = "";

         if (!Util.getTrue(strict)) {
            fieldValue = this.getFormattedFieldValue();
         }

         String compareValue = this.getFieldValue();

         // "fieldValue" is the variable actually printed out
         if (!ResultSetVector.isNull(getParentForm().getResultSetVector())) {
            if (embeddedData != null) { //  embedded data is nested in this tag

               int    embeddedDataSize  = embeddedData.size();
               int    i                 = 0;
               String embeddedDataValue = null;

               while (i < embeddedDataSize) {
                  KeyValuePair aKeyValuePair = (KeyValuePair) embeddedData.get(i);

                  if (aKeyValuePair.getKey()
                                         .equals(compareValue)) {
                     embeddedDataValue = aKeyValuePair.getValue();

                     break;
                  }

                  i++;
               }

               if (embeddedDataValue != null) {
                  fieldValue = embeddedDataValue;

                  // we'll print out embedded value associated with the current value
               }
            }
         }

         // PG, 2001-12-14
         // If maxlength was input, trim display
         String size = null;

         if (((size = this.getMaxlength()) != null)
                   && (size.trim()
                                 .length() > 0)) {
            //convert to int
            int count = Integer.parseInt(size);

            // Trim and add trim indicator (...)
            if (count < fieldValue.length()) {
               fieldValue = fieldValue.substring(0, count);
               fieldValue += "...";
            }
         }

         // SM 2003-08-05
         // if styleClass is present, render a SPAN with text included
         String s = prepareStyles();

         if (Util.isNull(s)) {
            pageContext.getOut()
                       .write(fieldValue);
         } else {
            pageContext.getOut()
                       .write("<span " + s + "\">" + fieldValue + "</span>");
         }
      } catch (java.io.IOException ioe) {
         logCat.error(ioe);
         throw new JspException("IO Error: " + ioe.getMessage());
      } catch (Exception e) {
         logCat.error(e);
         throw new JspException("Error: " + e.getMessage());
      }

      return EVAL_PAGE;
   }


   /**
    * DOCUMENT ME!
    */
   public void doFinally() {
      embeddedData = null;
      super.doFinally();
   }
}
