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

import org.dbforms.util.*;

import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.jsp.*;



/**
 * <p>
 * This tag renders a javascript array with Embeded data.  Only Value is
 * generated.
 * </p>
 *
 * @author Eric Beaumier
 */
public class JavascriptArrayTag extends TagSupportWithScriptHandler
   implements DataContainer, javax.servlet.jsp.tagext.TryCatchFinally {
   private static Log logCat = LogFactory.getLog(JavascriptArrayTag.class
                                                 .getName()); // logging category for this class
   private List       embeddedData = null;
   private String     name         = null;

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
    * @return DOCUMENT ME!
    */
   public Escaper getEscaper() {
      return getConfig()
                .getEscaper();
   }


   /**
    * DOCUMENT ME!
    *
    * @param name DOCUMENT ME!
    */
   public void setName(String name) {
      this.name = name;
   }


   /**
    * DOCUMENT ME!
    *
    * @param t DOCUMENT ME!
    *
    * @throws Throwable DOCUMENT ME!
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
      StringBuffer tagBuf = new StringBuffer();

      if (embeddedData == null) { // no embedded data is nested in this tag
         logCat.warn("No EmbeddedData provide for javascriptArray TagLib "
                     + name);

         return EVAL_PAGE;
      } else {
         tagBuf.append("\n<script language=\"javascript\">\n");
         tagBuf.append("   var " + name + " = new Array();\n");

         int embeddedDataSize = embeddedData.size();

         for (int i = 0; i < embeddedDataSize; i++) {
            KeyValuePair aKeyValuePair = (KeyValuePair) embeddedData.get(i);
            String       aKey = aKeyValuePair.getKey();
            tagBuf.append("   ")
                  .append(name)
                  .append("[")
                  .append(i)
                  .append("] = new Array('")
                  .append(aKey)
                  .append("'");

            String          aValue = aKeyValuePair.getValue();

            StringTokenizer st = new StringTokenizer(aValue, ",");

            while (st.hasMoreTokens())
               tagBuf.append(",'")
                     .append(st.nextToken())
                     .append("'");

            tagBuf.append(");\n");
         }

         tagBuf.append("</script>\n");
      }

      try {
         pageContext.getOut()
                    .write(tagBuf.toString());
      } catch (java.io.IOException ioe) {
         throw new JspException("IO Error: " + ioe.getMessage());
      }

      return EVAL_PAGE;
   }


   /**
    * DOCUMENT ME!
    */
   public void doFinally() {
      name         = null;
      embeddedData = null;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws javax.servlet.jsp.JspException DOCUMENT ME!
    */
   public int doStartTag() throws javax.servlet.jsp.JspException {
      return EVAL_BODY_BUFFERED;
   }
}
