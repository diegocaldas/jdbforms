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

import org.dbforms.config.Field;

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
public class DbSearchCheckBoxTag extends DbSearchTag 
   implements 
      javax.servlet.jsp.tagext.TryCatchFinally {

   private String value = null;
   private String checked = "false";

   /**
    * @return
    */
   public String getValue() {
      return value;
   }


   /**
    * @param string
    */
   public void setValue(String string) {
      value = string;
   }

   /**
    * @return
    */
   public String getChecked() {
      return checked;
   }

   /**
    * @param string
    */
   public void setChecked(String string) {
      checked = string;
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
         Field field = getField();

         /*
                            <input type="hidden" name="searchalgo_0_1" value="weakEnd"/>
                            <input type="hidden" name="searchmode_0_1" value="AND"/>
                            <input type="input" name="search_0_1"/>
         */
         StringBuffer tagBuf = new StringBuffer();

         StringBuffer paramNameBuf = new StringBuffer();
         paramNameBuf.append(field.getSearchFieldName());

         tagBuf.append("<input type=\"input\" name=\"");
         tagBuf.append(paramNameBuf.toString());
         tagBuf.append("\" ");
         tagBuf.append("value=\"");
         tagBuf.append(getValue());
         if ("true".equalsIgnoreCase(getChecked())) {
            tagBuf.append(" checked=\"checked\" ");
         }

         tagBuf.append("\"");
         tagBuf.append(prepareStyles());
         tagBuf.append(prepareEventHandlers());
         tagBuf.append("/>\n");

 		 pageContext.getOut().write(renderPatternHtmlInputField());
         pageContext.getOut().write(RenderHiddenFields(field));
         pageContext.getOut().write(tagBuf.toString());
      } catch (java.io.IOException ioe) {
         throw new JspException("IO Error: " + ioe.getMessage());
      }

      return EVAL_PAGE;
   }

   public void doFinally() {
      checked = "false";
      value = null;
      super.doFinally();
   }

   /**
    * @see javax.servlet.jsp.tagext.TryCatchFinally#doCatch(java.lang.Throwable)
    */
   public void doCatch(Throwable t) throws Throwable {
      throw t;
   }

}
