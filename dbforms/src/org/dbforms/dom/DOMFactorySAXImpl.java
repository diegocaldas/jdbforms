/*
 * Created on 12.10.2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.dbforms.dom;

import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;

import org.w3c.dom.Document;

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
 * @author hkk
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DOMFactorySAXImpl extends DOMFactory {
   private Category logCat = Category.getInstance(this.getClass().getName());

   private DOMWriter writer = createDOMWriter();
   private DocumentBuilder builder = createDOMBuilder();

   public XPathEvaluator newXPathEvaluator() {
      return new XPathEvaluatorImpl();
   }

   public Document newDOMDocument() {
      return builder.newDocument();
   }

   public String DOM2String(Document doc) {
      return writer.writeToString(doc);
   }

   public Document String2DOM(String data) {
      Document doc = null;
      if (!Util.isNull(data)) {
         // String parsen
         try {
            // String parsen
            InputSource in = new InputSource(new StringReader(data));
            doc = builder.parse(in);
         } catch (Exception e) {
            logCat.error(e);
         }
      }
      return doc;
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

   private DOMWriter createDOMWriter() {
      DOMWriter res = null;
      try {
         res = new org.apache.xml.serialize.DOMWriterImpl();
         res.setEncoding("ISO-8859-1");
         res.setNewLine("CR-LF");
      } catch (Exception e) {
         logCat.error(e);
      }
      return res;
   }

   public Document read(String url) {
      Document doc = null;
      if (!Util.isNull(url)) {
         InputStream in = null;
         try {
            // Try to parse via URL connection
            URL u = new URL(url);
            URLConnection con = u.openConnection();
            con.connect();
            in = con.getInputStream();
         } catch (Exception e) {
            logCat.error(e);
         }
         if (in == null) {
            try {
               in = new FileInputStream(url);
            } catch (Exception e) {
               logCat.error(e);
            }
         }
         if (in != null) {
            try {
               InputSource src = new InputSource(in);
               doc = builder.parse(src);
               in.close();
            } catch (Exception e) {
               logCat.error(e);
            }
         }
      }
      return doc;
   }

   public void write(OutputStream out, Document doc) {
      writer.writeNode(out, doc);
   }

}