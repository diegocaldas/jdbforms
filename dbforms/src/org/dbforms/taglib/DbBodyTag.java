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

import org.dbforms.config.ResultSetVector;

import org.dbforms.util.Util;

import java.io.UnsupportedEncodingException;

import java.util.Map;

import javax.servlet.jsp.JspException;



/**
 * <p>
 * this tag renders a Body-tag. it is supposed to be nested within a DbFormTag.
 * because this tag is nested within a DbFormTag it is invoked every time the
 * parent dbFormTag gets evaluated, AND it gets rendered in every
 * evalation-loop (if there exists data to be rendered)
 * </p>
 *
 * @author Joachim Peer
 */
public class DbBodyTag extends DbBaseHandlerTag
   implements javax.servlet.jsp.tagext.TryCatchFinally {
   private String allowNew = "true";

   /**
    * DOCUMENT ME!
    *
    * @param allowNew DOCUMENT ME!
    */
   public void setAllowNew(String allowNew) {
      this.allowNew = allowNew;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getAllowNew() {
      return allowNew;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws JspException DOCUMENT ME!
    */
   public int doAfterBody() throws JspException {
      DbFormTag myParent = getParentForm(); 

      try {
         // each rendering loop represents one row of data.
         // for every row we need to print some data needed by the controller servlet in order to
         // correctly dispatching and eventually modifying our data.
         //
         // now the key of the current dataset is printed out (always)
         // this key will be used by actions such as delete or update.
         if (myParent.getTable() != null) {
            String curKeyString = myParent.getTable()
                                          .getKeyPositionString(myParent
                                                                .getResultSetVector());

            if (!Util.isNull(curKeyString)) {
               curKeyString = Util.encode(curKeyString,
                                          pageContext.getRequest().getCharacterEncoding());
               myParent.appendToChildElementOutput("<input type=\"hidden\" name=\"k_"
                                                   + myParent.getTable().getId()
                                                   + "_"
                                                   + myParent.getPositionPath()
                                                   + "\" value=\""
                                                   + curKeyString + "\"/>");
            }
         }
      } catch (UnsupportedEncodingException uee) {
         throw new JspException(uee.toString());
      }

      return SKIP_BODY;
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
    * @throws JspException DOCUMENT ME!
    */
   public int doEndTag() throws JspException {
      try {
         if (bodyContent != null) {
            bodyContent.writeOut(bodyContent.getEnclosingWriter());
            bodyContent.clearBody();

            // workaround for duplicate rows in JRun 3.1
         }
      } catch (java.io.IOException e) {
         throw new JspException("IO Error: " + e.getMessage());
      }

      return EVAL_PAGE;
   }


   // by default this is "true" - if so, the body is rendered at least 1 time, even if there are no data rows in the table. this enables the user to insert a new data row. - to disable this feature, allowNew has to be set to "false"

   /**
    * DOCUMENT ME!
    */
   public void doFinally() {
      allowNew = "true";
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public int doStartTag() throws javax.servlet.jsp.JspException {
      DbFormTag myParent = getParentForm();
      ResultSetVector rsv = myParent.getResultSetVector();
      // the body may be rendered under the following circumstances:
      // - resultSetVector > 0 && not footer reached => render a row
      // - resultSetVector == 0 && allowNew==true    => insert a new row
      if (
      		(!ResultSetVector.isNull(rsv) && myParent.isFooterReached())  
      		||
			(ResultSetVector.isNull(rsv) && Util.getFalse(allowNew))
          ) {
            return SKIP_BODY;
      }

      myParent.updatePositionPath();
      // to enable access from jsp we provide a variable
      // #fixme: we need a more convenient data access structure
      //
      // #fixme: this is a CRAZY WEIRED ODD SQUARE WORKAROUND
      //

      if (!ResultSetVector.isNull(rsv)) {
         Map dbforms = (Map) pageContext.getAttribute("dbforms");

         if (dbforms != null) {
            DbFormContext dbContext = (DbFormContext) dbforms.get(myParent
                                                                  .getName());
            if (dbContext != null) {
               dbContext.setCurrentRow(rsv.getCurrentRowAsMap());
               try {
                  dbContext.setPosition(Util.encode(myParent.getTable().getPositionString(rsv),
                                                    pageContext.getRequest().getCharacterEncoding()));
               } catch (Exception e) {
                  throw new JspException(e.getMessage());
               }
            }
         }

         if (!rsv.isLast()) {
            rsv.moveNext(); // teleport us to future...

            // This must be done because currentRow_xxx is reread from 
            // the pagecontext after(!) the body of the tag. This means 
            // that the current body contains the currentRow of rsv(i - 1)
            // # jp 27-06-2001: replacing "." by "_", so that SCHEMATA can be used
            pageContext.setAttribute("currentRow_"
                                     + myParent.getTableName().replace('.', '_'),
                                     rsv.getCurrentRowAsMap());

            try {
               pageContext.setAttribute("position_"
                                        + myParent.getTableName().replace('.',
                                                                          '_'),
                                        Util.encode(myParent.getTable().getPositionString(rsv),
                                                    pageContext.getRequest().getCharacterEncoding()));
            } catch (Exception e) {
               throw new JspException(e.getMessage());
            }

            rsv.movePrevious(); // ...and back to present ;=)
         }
      }

      return EVAL_BODY_BUFFERED;
   }
}
