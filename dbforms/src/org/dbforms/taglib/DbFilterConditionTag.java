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
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;
import org.apache.log4j.Category;
import org.dbforms.config.FieldValue;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.Util;



/**
 * Holds an sql condition that has to be nested inside a DbFilterTag.
 * A condition is specified as sql code in the body of the tag.
 * The character ? is a placeholder for user's input substitution.
 * Every char ? found in sql code is replaced with value evalutated from
 * corresponding filterValue tag nested. So there must be as ? as filterValue tags.
 * 
 * @author Sergio Moretti <s.moretti@nsi-mail.it>
 * 
 * @version $Revision$
 */
public class DbFilterConditionTag extends BodyTagSupport
   implements TryCatchFinally
{
   /**
    * tag's state holder.
    * Used a separate class to hold tag's state to workaround to Tag pooling, in which
    * an tag object is reused, but we have the need to store informations about all 
    * child tags in the parent, so we store the state, and apply it to a dummy tag when needed. 
    * 
    * @author Sergio Moretti
    */
   protected class State
   {
      /**
       * identifier of condition
       */
      protected int conditionId = -1;

      /**
       * raw filter condition
       */
      protected String filterCondition = null;

      /**
       * condition's label, appear as an option in html select element  
       */
      protected String label = null;

      /**
       * list of value object's state contained in this condition
       */
      protected ArrayList values = null;
   }

   /** DOCUMENT ME! */
   protected static Category logCat = Category.getInstance(
                                               DbFilterConditionTag.class.getName());

   /**
    * generate condition from request. Called from nested DbFilterTag object
    * 
    * @param request
    * @param tableId
    * @param conditionId
    * @return string containing sql condition code
    */
   protected static String getSqlFilter(HttpServletRequest request, int tableId, 
                                        int conditionId)
   {
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
    * @return string containing sql condition code
    */
   protected static FieldValue[] getSqlFilterParams(HttpServletRequest request, 
                                                    int tableId, 
                                                    int conditionId)
   {
      return DbFilterValueTag.readValuesFromRequest(request, tableId, 
                                                    conditionId);
   }


   /**
    * generate condition from request. Called from nested DbFilterTag object
    * 
    * @param request
    * @param tableId
    * @param conditionId
    * @return string containing sql condition code
    * @todo search for \? to let use the ? char in condition
    */
   protected static String generateFilterCondition(HttpServletRequest request, 
                                                   int tableId, int conditionId)
   {
      // read raw condition from request
      String filterCondition = ParseUtil.getParameter(request, 
                                                      DbFilterTag.getFilterName(
                                                               tableId)
                                                      + DbFilterTag.FLT_COND
                                                      + conditionId);

      if (Util.isNull(filterCondition))
      {
         return null;
      }

      // build up the list of the values of the nested filterValue's parameters 
      FieldValue[] values = DbFilterValueTag.readValuesFromRequest(request, 
                                                                   tableId, 
                                                                   conditionId);

      logCat.debug("init parse filterCondition : " + filterCondition
                   + ", values : " + FieldValue.toString(values));

      /**
       * substitute ? with corresponding value in list 
       */
      int          p1  = 0;
      int          p2  = filterCondition.indexOf('?', p1);
      StringBuffer buf = new StringBuffer();
      int          cnt = 0;

      while (p2 > -1)
      {
         // add the string before the next ?
         buf.append(filterCondition.substring(p1, p2));

         // if values are exausted, then abort
         if (cnt >= values.length)
         {
            logCat.error("reference to a missing filterValue in "
                         + filterCondition);

            return null;
         }

         // retrieve value
         String value = (String) values[cnt].getFieldValue();

         if (value == null)
         {
            value = "";
         }


         // add value to string gbuffer
         buf.append(value);


         // restart search from next char after ? 
         p1 = p2 + 1;
         p2 = filterCondition.indexOf('?', p1);
         cnt++;
      }


      // add remaining part of string
      buf.append(filterCondition.substring(p1));
      filterCondition = buf.toString();
      logCat.debug("end parse filterCondition : " + filterCondition);

      return filterCondition;
   }


   /**
    * condition prefix for request parameter
    * 
    * @param tableId
    * @param conditionId
    * @return
    */
   protected static String getConditionName(int tableId, int conditionId)
   {
      return DbFilterTag.getFilterName(tableId) + DbFilterTag.FLT_COND
             + conditionId;
   }

   /**
    * object containing tag's state
    */
   private State state;

   /**
    * 
    */
   public DbFilterConditionTag()
   {
      super();
      state = new State();
   }

   /**
    * add a value to the value's list. called from nested DbFilterValueTag objs.
    * 
    * @param value
    * @return index of the newly added object
    */
   protected int addValue(DbFilterValueTag value)
   {
      state.values.add(value.getState());

      return state.values.size() - 1;
   }


   /**
    * read filterCondition from body
    * 
    * @see javax.servlet.jsp.tagext.IterationTag#doAfterBody()
    */
   public int doAfterBody() throws JspException
   {
      if (this.bodyContent != null)
      {
         state.filterCondition = bodyContent.getString().trim();
      }

      return SKIP_BODY;
   }


   /**
    * initialize environment and process body only if this condition is the currently selected.
    * 
    * @see javax.servlet.jsp.tagext.Tag#doStartTag()
    */
   public int doStartTag() throws JspException
   {
      init();

      String sel = ParseUtil.getParameter(
                            (HttpServletRequest) pageContext.getRequest(), 
                            ((DbFilterTag) getParent()).getFilterName()
                            + DbFilterTag.FLT_SEL);

      // process body only if this condition is active
      if (sel != null)
      {
         int selId = Integer.parseInt(sel);

         if (selId == state.conditionId)
         {
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
   public boolean equals(Object obj)
   {
      return (obj instanceof DbFilterConditionTag)
             && (state.conditionId == ((DbFilterConditionTag) obj).state.conditionId);
   }


   /**
    * condition prefix for request parameters
    * 
    * @return
    */
   protected String getConditionName()
   {
      return getConditionName(((DbFilterTag) getParent()).getTableId(), 
                              state.conditionId);
   }


   /**
    * @return
    */
   public String getLabel()
   {
      return state.label;
   }


   /**
    * @return
    */
   protected State getState()
   {
      return state;
   }


   /**
    * initialize class's attributes
    */
   private void init()
   {
      // state object is createad in constructor (for the first use) and in doEndTag next times
      state.values = new ArrayList();


      //state.parentFilter = (DbFilterTag) getParent();
      state.conditionId     = ((DbFilterTag) getParent()).addCondition(this);
      state.filterCondition = null;

      if (state.label == null)
      {
         state.label = Integer.toString(state.conditionId);
      }
   }


   /**
    * render output, called from parent DbFilterCondition obj.
    * 
    * @return string containing html code for this obj
    */
   protected StringBuffer render() throws JspException
   {
      StringBuffer buf = new StringBuffer();
      buf.append("<input type=\"hidden\" name=\""
                 + ((DbFilterTag) getParent()).getFilterName()
                 + DbFilterTag.FLT_COND + state.conditionId + "\" value=\""
                 + state.filterCondition + "\" />\n");

      DbFilterValueTag value = new DbFilterValueTag();

      for (Iterator i = state.values.iterator(); i.hasNext();)
      {
         value.setState(this.pageContext, this, 
                        (DbFilterValueTag.State) i.next());
         buf.append(value.render());
      }

      return buf;
   }


   /**
    * @param string
    */
   public void setLabel(String string)
   {
      state.label = string;
   }


   /**
    * @param state
    */
   protected void setState(PageContext pg, DbFilterTag parent, State state)
   {
      setParent(parent);
      setPageContext(pg);
      this.state = state;
   }


   /**
    * @see javax.servlet.jsp.tagext.TryCatchFinally#doCatch(java.lang.Throwable)
    */
   public void doCatch(Throwable t) throws Throwable
   {
      logCat.error("doCatch called - " + t.toString());
      throw t;
   }


   /**
    * reset tag state
    * 
    * @see javax.servlet.jsp.tagext.TryCatchFinally#doFinally()
    */
   public void doFinally()
   {
      //logCat.debug("doFinally called");
      state = new State();
   }
}