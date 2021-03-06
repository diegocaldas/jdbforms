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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.config.FieldTypes;

import org.dbforms.dom.DOMFactory;

import org.dbforms.util.TimeUtil;
import org.dbforms.util.Util;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.xpath.XPathEvaluator;
import org.w3c.dom.xpath.XPathNSResolver;
import org.w3c.dom.xpath.XPathResult;



/**
 * Delegates the whole xpath stuff to this class. Holds the result of an xpath
 * query. Do the mapping between java objects and fields.
 *
 * @author hkk
 */
public class XMLDataResult {
   private static Log      logCat    = LogFactory.getLog(XMLDataResult.class);
   private Element         root;
   private XPathEvaluator  evaluator;
   private XPathNSResolver resolver;
   private XPathResult     data;
   private boolean         changed = false;

   /**
    * Creates a new XMLDataResult object.
    *
    * @param root xml dom object
    * @param qry xpath string to query
    */
   public XMLDataResult(Element root,
                        String  qry) {
      this.root      = root;
      this.evaluator = DOMFactory.instance()
                                 .newXPathEvaluator();
      resolver = evaluator.createNSResolver(root);

      // Evaluate the xpath expression.
      data = (XPathResult) evaluator.evaluate(qry, this.root, resolver,
                                              XPathResult.ORDERED_NODE_SNAPSHOT_TYPE,
                                              null);
   }

   /**
    * sets the field value of a special node as string. node is decribed by an
    * xpath string
    *
    * @param i node of result to return
    * @param expression xpath string which discribes the field to return
    * @param objectType field type to return
    * @param value value to set
    */
   public void setItemValue(int    i,
                            String expression,
                            int    objectType,
                            Object value) {
   }


   /**
    * returns the field value of a special node as Object. Node is decribed by
    * an xpath string
    *
    * @param i node of result to return
    * @param expression xpath string which discribes the field to return
    * @param objectType field type to return
    *
    * @return value as Object of selected type
    */
   public Object getItemValue(int    i,
                              String expression,
                              int    objectType) {
      Object result = null;

      try {
         Node n = item(i);

         if (n != null) {
            XPathResult pdata = (XPathResult) evaluator.evaluate(expression, n,
                                                                resolver,
                                                                XPathResult.FIRST_ORDERED_NODE_TYPE,
                                                                null);

            if (pdata != null) {
               n = pdata.getSingleNodeValue();

               if (n != null) {
                  switch (objectType) {
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
                     case FieldTypes.TIME:
                        result = TimeUtil.parseISO8601Date(toString(n));

                        break;

                     default:
                        result = toString(n);

                        break;
                  }
               }
            }
         }
      } catch (Exception e) {
         logCat.error("getItemValue", e);
      }

      return result;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public Element getRoot() {
      return root;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public boolean hasChanged() {
      return changed;
   }


   /**
    * returns the result at index as dom node
    *
    * @param index node of result to return
    *
    * @return the node at index
    */
   public Node item(int index) {
      Node res = null;

      if (index < data.getSnapshotLength()) {
         res = data.snapshotItem(index);
      }

      return res;
   }


   /**
    * size of resultset
    *
    * @return size of result set
    */
   public int size() {
      int res = data.getSnapshotLength();

      return res;
   }


   private String toString(Node element) {
      String result = null;

      if (element != null) {
         if (element.getNodeType() == Node.TEXT_NODE) {
            result = element.getNodeValue();
         } else {
            for (Node tx = element.getFirstChild(); tx != null;
                       tx = tx.getNextSibling()) {
               result = toString(tx);

               if (!Util.isNull(result)) {
                  break;
               }
            }
         }
      }

      return result;
   }
}
