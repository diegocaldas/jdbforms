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
import javax.servlet.jsp.*;

import org.dbforms.config.*;

/**
 *
 * to be embedded inside a linkURL-element, as shown in example below:
 *
 * <linkURL href="customer.jsp" table="customer" />
 *   <position fieldName="id" value="103" />
 *   <position fieldName="cust_lang" value="2" />
 * </link>
 *
 */
public class DbLinkPositionItemTag extends DbBaseHandlerTag
      implements javax.servlet.jsp.tagext.TryCatchFinally
{
   
   String value;
   /**
    * Set up the tag with parent tag's table and link to the field.  Then add the data to the
    * enclosing linkURL tag.
    *
    * @return DOCUMENT ME!
    *
    * @throws JspException  thrown when error occurs in processing the body of
    *                       this method
    */
   public int doStartTag() throws JspException
   {
      DbLinkURLTag parentTag = (DbLinkURLTag) this.getParent();

      if (parentTag != null)
      {
         Table table = parentTag.getTable();
         Field field = table.getFieldByName(getName());
         parentTag.addPositionPart(field, getValue());
      }
      else
      {
         throw new JspException(
            "DbLinkPositionItem-element must be placed inside a DbLinkURL-element!");
      }

      return EVAL_BODY_INCLUDE;
   }
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

	public void doFinally()
	{
		value = null;
		super.doFinally();
	}

   /**
    * @see javax.servlet.jsp.tagext.TryCatchFinally#doCatch(java.lang.Throwable)
    */
   public void doCatch(Throwable t) throws Throwable
   {
      throw t;
   }
}
