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
import java.net.URLConnection;
import java.io.InputStream;

import org.apache.log4j.Category;

//	Imported JAVA API for XML Parsing 1.0 classes
import org.apache.xpath.domapi.XPathEvaluatorImpl;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

//	Imported dom classes
import org.w3c.dom.Document;
import org.w3c.dom.xpath.XPathEvaluator;

import org.dbforms.config.Table;



/**
 * Special implementation of DataSource. This class deals with xml data
 * 
 * @author hkk
 */
public class DataSourceSAX extends DataSourceXMLAbstract
{

   private static DocumentBuilderFactory dfactory         = null;
   private static DocumentBuilder        builder          = null;
   private static XPathEvaluatorImpl     evaluator        = null;  
   private Category                      logCat           = 
            Category.getInstance(this.getClass().getName());

   /**
    * Contructor
    * 
    * @param table to set
    */
   public DataSourceSAX(Table table)
   {
      super(table);
      if (dfactory == null)
      {
         try
         {
            dfactory = DocumentBuilderFactory.newInstance();
            dfactory.setValidating(false);
            dfactory.setNamespaceAware(false);
            builder = dfactory.newDocumentBuilder();
			   evaluator = new XPathEvaluatorImpl();
         }
         catch (Exception e)
         {
            logCat.error(e);
         }
      }
   }

	/**
	 * creates a new XPathEvaluator to use inside the XMLDataResult
	 *  
	 * @return a new DOM XPathEvaluator
	 */
	protected XPathEvaluator getXPathEvaluator() 
	{
	   return evaluator;
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
      URLConnection con = uri.toURL().openConnection();
      con.connect();
      InputStream in  = con.getInputStream();
      InputSource src = new InputSource(in);
      Document    doc = builder.parse(src);
      return doc;
   }

}