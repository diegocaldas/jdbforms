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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.xpath.domapi.XPathEvaluatorImpl;

import org.dbforms.util.Util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.xpath.XPathEvaluator;

import org.xml.sax.InputSource;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;



/**
 * special implementation of the DOMFactory for xerces/xalan implementation,
 * This is the default class used by DOMFactory!
 *
 * @author Henner Kollmann
 */
public class DOMFactoryXALANImpl extends DOMFactory {
   private DocumentBuilder builder     = createDOMBuilder();
   private Log             logCat      = LogFactory.getLog(this.getClass().getName());
   private Transformer     transformer = createDOMWriter();

   /**
    * Creates a string representation of the given DOMDocument
    *
    * @param doc The document to transform
    *
    * @return string representation
    */
   public String DOM2String(Document doc) {
      StringWriter writer = new StringWriter();

      try {
         StreamResult result = new StreamResult(writer);
         DOMSource    source = new DOMSource(doc);
         transformer.transform(source, result);
      } catch (Exception e) {
         logCat.error("write", e);
      }

      String s = writer.toString();

      return s;
   }


   /**
    * Creates an new DOMDocument from the given string
    *
    * @param data the string to parse
    *
    * @return The new DOMDocument
    */
   public Document String2DOM(String data) {
      Document doc = null;

      if (!Util.isNull(data)) {
         // String parsen
         try {
            // String parsen
            InputSource in = new InputSource(new StringReader(data));
            doc = builder.parse(in);
         } catch (Exception e) {
            logCat.error(e.getMessage() + "\n" + data);
         }
      }

      return doc;
   }


   /**
    * Creates a new empty DOMDocument
    *
    * @return An empty DOMDocument
    */
   public Document newDOMDocument() {
      return builder.newDocument();
   }


   /**
    * returns an new created XPathEvaluator
    *
    * @return the new XPathEvaluator
    */
   public XPathEvaluator newXPathEvaluator() {
      return new XPathEvaluatorImpl();
   }


   /**
    * Reads a DOMDocument from given url
    *
    * @param in the url to read from
    *
    * @return The new parsed DOMDocument
    */
   public Document read(InputStream in) {
      Document doc = null;

      try {
         InputSource src = new InputSource(in);
         doc = builder.parse(src);
      } catch (Exception e) {
         logCat.error(e);
      }

      return doc;
   }


   /**
    * Writes a DOMDocument into an OutputStream
    *
    * @param out OutputStream to write into
    * @param root The Ddcument to write
    */
   public void write(OutputStream out,
                     Element      root) {
      try {
         StreamResult result = new StreamResult(out);
         DOMSource    source = new DOMSource(root);
         transformer.transform(source, result);
      } catch (Exception e) {
         logCat.error("write", e);
      }
   }


   private DocumentBuilder createDOMBuilder() {
      DocumentBuilder builder = null;

      try {
         // Init DOM
         DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
         dfactory.setValidating(false);
         dfactory.setNamespaceAware(false);
         builder = dfactory.newDocumentBuilder();
      } catch (Exception e) {
         logCat.error(e);
      }

      return builder;
   }


   private Transformer createDOMWriter() {
      Transformer transformer = null;

      try {
         TransformerFactory transFactory = TransformerFactory.newInstance();
         transformer = transFactory.newTransformer();
      } catch (Exception e) {
         logCat.error("createDOMWriter", e);
      }

      return transformer;
   }
}
