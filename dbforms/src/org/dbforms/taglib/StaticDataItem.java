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

import org.dbforms.util.KeyValuePair;
import org.dbforms.util.MessageResources;
import org.dbforms.util.Util;

import javax.servlet.jsp.JspException;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class StaticDataItem extends DbBaseHandlerTag
   implements javax.servlet.jsp.tagext.TryCatchFinally {
   private static Log logCat = LogFactory.getLog(StaticDataItem.class.getName()); // logging category for this class
   private String     key;
   private String     value;

   /**
    * DOCUMENT ME!
    *
    * @param key DOCUMENT ME!
    */
   public void setKey(String key) {
      this.key = key;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getKey() {
      return key;
   }


   /**
    * DOCUMENT ME!
    *
    * @param string
    */
   public void setValue(String string) {
      value = string;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getValue() {
      String message = null;

      if ((value != null)
                && (getParent() instanceof StaticData
                && getParent()
                            .getParent() instanceof DbBaseHandlerTag
                && getParentForm()
                            .hasCaptionResourceSet())) {
         try {
            message = MessageResources.getMessage(value, getLocale());

            if (!Util.isNull(message)) {
               value = message;
            }
         } catch (Exception e) {
            logCat.debug("getValue(" + value + ") Exception : "
                         + e.getMessage());
         }
      }

      return value;
   }


   /**
    * @see javax.servlet.jsp.tagext.TryCatchFinally#doCatch(java.lang.Throwable)
    */
   public void doCatch(Throwable t) throws Throwable {
      throw t;
   }


   /**
    * DOCUMENT ME!
    */
   public void doFinally() {
      key   = null;
      value = null;
      super.doFinally();
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws JspException DOCUMENT ME!
    */
   public int doStartTag() throws JspException {
      if ((getParent() != null)
                && getParent() instanceof StaticDataAddInterface) {
         ((StaticDataAddInterface) getParent()).addElement(new KeyValuePair(key,
                                                                            getValue()));
      } else {
         throw new JspException("StaticDataItem element must be placed inside a AddStaticData element!");
      }

      return EVAL_BODY_INCLUDE;
   }
}
