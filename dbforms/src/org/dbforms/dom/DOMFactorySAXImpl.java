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

package org.dbforms.dom;
import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;
import org.w3c.dom.ls.DOMWriter;
import org.w3c.dom.xpath.XPathEvaluator;
import org.apache.xpath.domapi.XPathEvaluatorImpl;
import org.apache.log4j.Category;
import org.dbforms.util.Util;



/**
 * special implementation of the DOMFactory for xerces/xalan implementation,
 * This is the default class used by DOMFactory!
 * 
 * @author Henner Kollmann
 */
public class DOMFactorySAXImpl extends DOMFactory
{
   private Category        logCat = Category.getInstance(
                                             this.getClass().getName());
   private DOMWriter       writer  = createDOMWriter();
   private DocumentBuilder builder = createDOMBuilder();

   /**
    * returns an new created XPathEvaluator
    * 
    * @return the new XPathEvaluator
    */
   public XPathEvaluator newXPathEvaluator()
   {
      return new XPathEvaluatorImpl();
   }


   /**
    * Creates a new empty DOMDocument
    * 
    * @return An empty DOMDocument
    */
   public Document newDOMDocument()
   {
      return builder.newDocument();
   }


   /**
    * Creates a string representation of the given DOMDocument
    * 
    * @param doc The document to transform
    * 
    * @return string representation
    */
   public String DOM2String(Document doc)
   {
      return writer.writeToString(doc);
   }


   /**
    * Creates an new DOMDocument from the given string
    * 
    * @param data the string to parse
    * 
    * @return The new DOMDocument
    */
   public Document String2DOM(String data)
   {
      Document doc = null;

      if (!Util.isNull(data))
      {
         // String parsen
         try
         {
            // String parsen
            InputSource in = new InputSource(new StringReader(data));
            doc = builder.parse(in);
         }
         catch (Exception e)
         {
            logCat.error(e.getMessage() + "\n" + data);
         }
      }

      return doc;
   }


   private DocumentBuilder createDOMBuilder()
   {
      DocumentBuilder builder = null;

      try
      {
         // Init DOM
         DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
         dfactory.setValidating(false);
         dfactory.setNamespaceAware(false);
         builder = dfactory.newDocumentBuilder();
      }
      catch (Exception e)
      {
         logCat.error(e);
      }

      return builder;
   }


   /**
    * Reads a DOMDocument from given url
    * 
    * @param url the url to read from
    * 
    * @return The new parsed DOMDocument
    */
   public Document read(String url)
   {
      Document doc = null;

      if (!Util.isNull(url))
      {
         InputStream in = null;

         try
         {
            // Try to parse via URL connection
            URL           u   = new URL(url);
            URLConnection con = u.openConnection();
            con.connect();
            in = con.getInputStream();
         }
         catch (Exception e)
         {
            logCat.error(e);
         }

         if (in == null)
         {
            try
            {
               in = new FileInputStream(url);
            }
            catch (Exception e)
            {
               logCat.error(e);
            }
         }

         if (in != null)
         {
            try
            {
               InputSource src = new InputSource(in);
               doc = builder.parse(src);
               in.close();
            }
            catch (Exception e)
            {
               logCat.error(e);
            }
         }
      }

      return doc;
   }


   /**
    * Writes a DOMDocument into an OutputStream
    * 
    * @param out OutputStream to write into
    * @param doc The Ddcument to write
    */
   public void write(OutputStream out, Element root)
   {
      writer.writeNode(out, root);
   }


   private DOMWriter createDOMWriter()
   {
      DOMWriter res = null;

      try
      {
         res = new org.apache.xml.serialize.DOMWriterImpl();
         res.setEncoding("ISO-8859-1");
         res.setNewLine("CR-LF");
      }
      catch (Exception e)
      {
         logCat.error(e);
      }

      return res;
   }
}