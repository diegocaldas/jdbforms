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
package org.dbforms.event.datalist.dao;

import org.w3c.dom.Node;
import org.w3c.dom.xpath.XPathResult;
import org.w3c.dom.xpath.XPathEvaluator;
import org.w3c.dom.xpath.XPathNSResolver;
import org.apache.xpath.domapi.XPathEvaluatorImpl;
import org.dbforms.util.FieldTypes;
import org.dbforms.util.TimeUtil;



/**
 * Delegates the whole xpath stuff to this class.
 * Holds the result of an xpath query.
 * Do the mapping between java objects and fields.
 *
 *  @author hkk
 *
 */
public class XMLDataResult
{
   private XPathResult     data;
   private XPathEvaluator  evaluator;
   private XPathNSResolver resolver;

   /**
    * Creates a new XMLDataResult object.
    *
    * @param doc xml dom object
    * @param qry xpath string to query
    */
   public XMLDataResult(Node root, String qry)
   {
      // Create an XPath evaluator and pass in the document.
      evaluator    = new XPathEvaluatorImpl();
      resolver     = evaluator.createNSResolver(root);

      // Evaluate the xpath expression. 
      // Is eventally done twice here, but we need the result as an XPathResult!
      data = (XPathResult) evaluator.evaluate(qry, root, resolver,
            XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
   }

   /**
    * returns the result at index as dom node
    *
    * @param index node of result to return
    *
    * @return the node at index
    */
   public Node item(int index)
   {
      return data.snapshotItem(index);
   }


   /**
    * returns the field value of a special node as string. Value is decribed by an
    * xpath string
    *
    * @param index      node of result to return
    * @param expression xpath string which discribes the field to return
    *
    * @return value as string
    */
   public String itemValue(int i, String expression)
   {
      return (String) itemValue(i, expression, FieldTypes.CHAR);
   }


   /**
    * returns the field value of a special node as Object. Value is decribed by an
    * xpath string
    *
    * @param index      node of result to return
    * @param expression xpath string which discribes the field to return
     * @param objectType field type to return
    *
    * @return value as Object of selected type
    */
   public Object itemValue(int i, String expression, int objectType)
   {
      short  type   = XPathResult.STRING_TYPE;
      Object result = null;

      switch (objectType)
      {
         case FieldTypes.CHAR:
         case FieldTypes.DATE:
         case FieldTypes.TIMESTAMP:
            type = XPathResult.STRING_TYPE;

            break;

         case FieldTypes.FLOAT:
         case FieldTypes.NUMERIC:
         case FieldTypes.INTEGER:
            type = XPathResult.NUMBER_TYPE;

            break;
      }

      XPathResult data = (XPathResult) evaluator.evaluate(expression, item(i),
            resolver, type, null);

      if (data != null)
      {
         switch (objectType)
         {
            case FieldTypes.CHAR:
               result = data.getStringValue();

               break;

            case FieldTypes.FLOAT:
            case FieldTypes.NUMERIC:
               result = new Double(data.getNumberValue());

               break;

            case FieldTypes.INTEGER:

               Double d = new Double(data.getNumberValue());
               result = new Integer(d.intValue());

               break;

            case FieldTypes.DATE:
            case FieldTypes.TIMESTAMP:
               result = TimeUtil.parseISO8601Date(data.getStringValue());

               break;

            default:
               result = data.getStringValue();

               break;
         }
      }

      return result;
   }


   /**
    * size of resultset
    *
    * @return size of result set
    */
   public int size()
   {
      return data.getSnapshotLength();
   }
}
