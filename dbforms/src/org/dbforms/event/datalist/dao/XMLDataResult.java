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

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.xpath.XPathResult;
import org.w3c.dom.xpath.XPathEvaluator;
import org.w3c.dom.xpath.XPathNSResolver;

import org.dbforms.config.FieldTypes;

import org.dbforms.util.TimeUtil;
import org.dbforms.util.Util;



/**
 * Delegates the whole xpath stuff to this class. Holds the result of an xpath
 * query. Do the mapping between java objects and fields.
 * 
 * @author hkk
 */
public class XMLDataResult
{
   private XPathResult     data;
   private XPathEvaluator  evaluator;
   private XPathNSResolver resolver;
   private boolean changed = false;
   private Document doc;
   /**
    * Creates a new XMLDataResult object.
    * 
    * @param root xml dom object
    * @param qry xpath string to query
    */
   public XMLDataResult(XPathEvaluator evaluator, Document root, String qry)
   {
      this.doc = root;
      this.evaluator = evaluator;
      resolver  = evaluator.createNSResolver(root);
      // Evaluate the xpath expression. 
      data = (XPathResult) evaluator.evaluate(qry, doc, resolver, 
                                              XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, 
                                              null);
   }

   public boolean hasChanged() {
      return changed;
   }
   
   public Document getDocument() {
      return doc;
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
    * sets the field value of a special node as string. node is decribed by
    * an xpath string
    * 
    * @param i    node of result to return
    * @param      expression xpath string which discribes the field to return
    * @param      objectType field type to return
    * @value       the value to set
    */
   public void setItemValue(int i, String expression, int objectType, Object value)
   {

   }

   
   private String toString(Node element) {
      String result = null;
      if (element != null) {
         if (element.getNodeType() == Node.TEXT_NODE) { 
            result = element.getNodeValue();
         } else {
            for (Node tx = element.getFirstChild(); tx != null; tx = tx.getNextSibling()) {
               result = toString(tx);
               if (!Util.isNull(result))
                  break;
            }
         }
      }
      return result;
   }

   /**
    * returns the field value of a special node as Object. Node is decribed by
    * an xpath string
    * 
    * @param i      node of result to return
    * @param expression xpath string which discribes the field to return
    * @param objectType field type to return
    * 
    * @return value as Object of selected type
    */
   public Object getItemValue(int i, String expression, int objectType)
   {
      Object result = null;
      XPathResult data = (XPathResult) evaluator.evaluate(expression, item(i), 
                                                           resolver, XPathResult.FIRST_ORDERED_NODE_TYPE, null);

    
      if (data != null)
      {
         Node n = data.getSingleNodeValue();
         if (n != null) 
         {
            switch (objectType)
            {
               case FieldTypes.CHAR:
                  result = toString(n);
                  break;
   
               case FieldTypes.FLOAT:
               case FieldTypes.NUMERIC:
                  result = new Double(toString(n));
   
                  break;
   
               case FieldTypes.INTEGER:
                  result = new Integer(toString(n));
   
                  break;
   
               case FieldTypes.DATE:
               case FieldTypes.TIMESTAMP:
                  result = TimeUtil.parseISO8601Date(toString(n));
   
                  break;
   
               default:
                  result = toString(n);
   
                  break;
            }
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