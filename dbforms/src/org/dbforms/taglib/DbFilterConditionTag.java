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

import org.dbforms.config.FieldValue;

import org.dbforms.util.ParseUtil;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TryCatchFinally;



/**
 * Holds an sql condition that has to be nested inside a DbFilterTag. A
 * condition is specified as sql code in the body of the tag. The character ?
 * is a placeholder for user's input substitution. Every char ? found in sql
 * code is replaced with value evalutated from corresponding filterValue tag
 * nested. So there must be as ? as filterValue tags.
 *
 * @author Sergio Moretti
 * @version $Revision$
 */
public class DbFilterConditionTag extends AbstractScriptHandlerTag
   implements TryCatchFinally {

   /** object containing tag's state */
   private transient State state;

   /**
                                                                                        *
                                                                                        */
   public DbFilterConditionTag() {
      super();
      state = new State();
   }

   /**
    * DOCUMENT ME!
    *
    * @param string
    */
   public void setLabel(String string) {
      state.label = string;
   }


   /**
    * DOCUMENT ME!
    *
    * @return
    */
   public String getLabel() {
      return state.label;
   }


   /**
    * read filterCondition from body
    *
    * @see javax.servlet.jsp.tagext.IterationTag#doAfterBody()
    */
   public int doAfterBody() throws JspException {
      if (this.bodyContent != null) {
         state.filterCondition = bodyContent.getString()
                                            .trim();
      }

      return SKIP_BODY;
   }


   /**
    * reset tag state
    *
    * @see javax.servlet.jsp.tagext.TryCatchFinally#doFinally()
    */
   public void doFinally() {
      state = new State();
   }


   /**
    * initialize environment and process body only if this condition is the
    * currently selected.
    *
    * @see javax.servlet.jsp.tagext.Tag#doStartTag()
    */
   public int doStartTag() throws JspException {
      init();

      String sel = ParseUtil.getParameter((HttpServletRequest) pageContext
                                          .getRequest(),
                                          ((DbFilterTag) getParent())
                                          .getFilterName()
                                          + DbFilterTag.FLT_SEL);

      // process body only if this condition is active
      if (sel != null) {
         int selId = Integer.parseInt(sel);

         if (selId == state.conditionId) {
            return EVAL_BODY_BUFFERED;
         }
      }

      return SKIP_BODY;
   }


   /**
    * comparison using conditionId field
    *
    * @see java.lang.Object#equals(java.lang.Object)
    */
   public boolean equals(Object obj) {
      return (obj instanceof DbFilterConditionTag)
             && (state.conditionId == ((DbFilterConditionTag) obj).state.conditionId);
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public int hashCode() {
      return state.conditionId;
   }


   /**
    * condition prefix for request parameter
    *
    * @param tableId
    * @param conditionId
    *
    * @return
    */
   protected static String getConditionName(int tableId,
                                            int conditionId) {
      return DbFilterTag.getFilterName(tableId) + DbFilterTag.FLT_COND
             + conditionId;
   }


   /**
    * generate condition from request. Called from nested DbFilterTag object
    *
    * @param request
    * @param tableId
    * @param conditionId
    *
    * @return string containing sql condition code
    */
   protected static String getSqlFilter(HttpServletRequest request,
                                        int                tableId,
                                        int                conditionId) {
      return ParseUtil.getParameter(request,
                                    DbFilterTag.getFilterName(tableId)
                                    + DbFilterTag.FLT_COND + conditionId);
   }


   /**
    * generate condition from request. Called from nested DbFilterTag object
    *
    * @param request
    * @param tableId
    * @param conditionId
    *
    * @return string containing sql condition code
    */
   protected static FieldValue[] getSqlFilterParams(HttpServletRequest request,
                                                    int                tableId,
                                                    int                conditionId) {
      return DbFilterValueTag.readValuesFromRequest(request, tableId,
                                                    conditionId);
   }



   /**
    * condition prefix for request parameters
    *
    * @return
    */
   protected String getConditionName() {
      return getConditionName(((DbFilterTag) getParent()).getTableId(),
                              state.conditionId);
   }


   /**
    * DOCUMENT ME!
    *
    * @param pg DOCUMENT ME!
    * @param parent DOCUMENT ME!
    * @param state
    */
   protected void setState(PageContext pg,
                           DbFilterTag parent,
                           State       state) {
      setParent(parent);
      setPageContext(pg);
      this.state = state;
   }


   /**
    * DOCUMENT ME!
    *
    * @return
    */
   protected State getState() {
      return state;
   }


   /**
    * add a value to the value's list. called from nested DbFilterValueTag
    * objs.
    *
    * @param value
    *
    * @return index of the newly added object
    */
   protected int addValue(DbFilterValueTag value) {
      state.values.add(value.getState());

      return state.values.size() - 1;
   }


   /**
    * render output, called from parent DbFilterCondition obj.
    *
    * @return string containing html code for this obj
    */
   protected StringBuffer render() throws JspException {
      StringBuffer buf = new StringBuffer();
      buf.append("<input type=\"hidden\" name=\""
                 + ((DbFilterTag) getParent()).getFilterName()
                 + DbFilterTag.FLT_COND + state.conditionId + "\" value=\""
                 + state.filterCondition + "\" />\n");

      DbFilterValueTag value = new DbFilterValueTag();

      for (Iterator i = state.values.iterator(); i.hasNext();) {
         value.setState(this.pageContext, this,
                        (DbFilterValueTag.State) i.next());
         buf.append(value.render());
      }

      return buf;
   }


   /**
    * initialize class's attributes
    */
   private void init() {
      // state object is createad in constructor (for the first use) and in doEndTag next times
      state.values = new ArrayList();

      //state.parentFilter = (DbFilterTag) getParent();
      state.conditionId     = ((DbFilterTag) getParent()).addCondition(this);
      state.filterCondition = null;

      if (state.label == null) {
         state.label = Integer.toString(state.conditionId);
      }
   }

   /**
    * tag's state holder. Used a separate class to hold tag's state to
    * workaround to Tag pooling, in which an tag object is reused, but we have
    * the need to store informations about all  child tags in the parent, so
    * we store the state, and apply it to a dummy tag when needed.
    *
    * @author Sergio Moretti
    */
   protected static class State {
      /** list of value object's state contained in this condition */
      protected ArrayList values = null;

      /** raw filter condition */
      protected String filterCondition = null;

      /** condition's label, appear as an option in html select element */
      protected String label = null;

      /** identifier of condition */
      protected int conditionId = -1;
   }
}
