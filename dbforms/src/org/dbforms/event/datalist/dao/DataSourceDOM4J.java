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

import java.net.URI;
import java.util.List;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.xpath.XPathEvaluator;
import org.w3c.dom.xpath.XPathExpression;
import org.w3c.dom.xpath.XPathNSResolver;
import org.w3c.dom.xpath.XPathException;
import org.w3c.dom.xpath.XPathResult;


import org.dom4j.dom.DOMDocumentFactory;
import org.dom4j.io.SAXReader;


import org.dbforms.config.Table;



/**
 * Special implementation of DataSource. This class deals with xml data
 * 
 * @author hkk
 */
public class DataSourceDOM4J extends DataSourceXMLAbstract
{
   private class XPathImpl implements XPathResult
   {
      private static final int LIST_TYPE = -42;
      private List list = null;
		private String string = null;
		private Number number = null;
		private short resultType = ANY_TYPE;
		
		public XPathImpl(List list)
		{
		   this.list = list;
		   resultType = LIST_TYPE;
		}

		public XPathImpl(String string)
		{
			this.string = string;
			resultType = STRING_TYPE;
		}

		public XPathImpl(Number number)
		{
			this.number = number;
			resultType = NUMBER_TYPE;
		}
		
		public short getResultType()
		{
		   return resultType;                  
		}
		
		public double getNumberValue()
										 throws XPathException
        {  										 
           if (getResultType() != NUMBER_TYPE) 
		      throw new XPathException(XPathException.TYPE_ERR, "wrong type");  
           return number.doubleValue();
        }

		public String getStringValue()
										 throws XPathException
        {										 
		   if (getResultType() != STRING_TYPE) 
		      throw new XPathException(XPathException.TYPE_ERR, "wrong type");  
           return string;
        }

      public Node getSingleNodeValue()
                               throws XPathException
        {                               
           if (getResultType() != LIST_TYPE) 
              throw new XPathException(XPathException.TYPE_ERR, "wrong type");  
             return (Node) list.get(0);
        }

		public boolean getBooleanValue()
										 throws XPathException
        {										 
			throw new XPathException(XPathException.TYPE_ERR, "unsupported type");  
        }
										 

		public boolean getInvalidIteratorState()
		{
		   return false;
		}

		public int getSnapshotLength()
										 throws XPathException
        {										 
 		   if (getResultType() != LIST_TYPE) 
				throw new XPathException(XPathException.TYPE_ERR, "wrong type");  
           return list.size();
        }

		public Node iterateNext()
										throws XPathException, DOMException
        {										
		   throw new XPathException(DOMException.NOT_SUPPORTED_ERR, "unsupported");  
        }

		public Node snapshotItem(int index)
										 throws XPathException
        {										 
			if (getResultType() != LIST_TYPE) 
				throw new XPathException(XPathException.TYPE_ERR, "wrong type");  
			return (Node)list.get(index);
        }

   }

   private class EvaluatorImpl implements XPathEvaluator 
   {
		public XPathExpression createExpression(String expression, 
												XPathNSResolver resolver)
												throws XPathException, DOMException
        {															 
		   throw new XPathException(DOMException.NOT_SUPPORTED_ERR, "unsupported");  
        }

		public XPathNSResolver createNSResolver(Node nodeResolver)
		{
		   return null;
		}

		public Object evaluate(String expression, 
							          Node contextNode, 
									  XPathNSResolver resolver, 
									  short type, 
									  Object result)
									  throws XPathException, DOMException
        {									  
		   if (!(contextNode instanceof org.dom4j.Node))
		      throw new DOMException(DOMException.WRONG_DOCUMENT_ERR, "no DOM4J document"); 
		   switch (type) {
		      case XPathResult.ORDERED_NODE_SNAPSHOT_TYPE:
            case XPathResult.FIRST_ORDERED_NODE_TYPE:
 				      return new XPathImpl(((org.dom4j.Node) contextNode).selectNodes(expression));
              case XPathResult.STRING_TYPE:
                  return new XPathImpl(((org.dom4j.Node) contextNode).valueOf(expression));
              case XPathResult.NUMBER_TYPE:
			         return new XPathImpl(((org.dom4j.Node) contextNode).numberValueOf(expression));
              default:
                 throw new XPathException(XPathException.TYPE_ERR, "not supported type");  
		   }
        }
   }
   
   /**
    * Contructor
    * 
    * @param table to set
    */
   public DataSourceDOM4J(Table table)
   {
      super(table);
   }

	/**
	 * creates a new XPathEvaluator to use inside the XMLDataResult
	 *  
	 * @return a new DOM XPathEvaluator
	 */
	protected XPathEvaluator getXPathEvaluator() 
	{
	   return new EvaluatorImpl();
	}

   /**
    * gets the input document from the remote system. 
    * 
    * @param uri the uri to query
    * 
    * @return NODE the result
    * 
    * @throws Exception Exception during processing IO
    */
   protected Document read(URI uri) throws Exception
   {
	  SAXReader reader = new SAXReader(DOMDocumentFactory.getInstance());
	  Document doc     = (Document) reader.read(uri.getPath());
      return doc;
   }

}