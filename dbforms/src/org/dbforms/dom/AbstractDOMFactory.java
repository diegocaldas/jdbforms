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

import org.dbforms.util.ReflectionUtil;
import org.dbforms.util.Util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.xpath.XPathEvaluator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.URL;
import java.net.URLConnection;



/**
 * abstract class to hide the implemtation details of the various dom
 * implementations.
 *
 * @author Henner Kollmann
 */
public abstract class AbstractDOMFactory {
   private static final ThreadLocal singlePerThread = new ThreadLocal();
   private static Log               logCat = LogFactory.getLog(AbstractDOMFactory.class
                                                               .getName());
   private static String            factoryClass = "org.dbforms.dom.DOMFactoryXALANImpl";

   /**
    * Creates a new DOMFactory object.
    */
   protected AbstractDOMFactory() {
   }

   /**
    * Get the thread singelton instance
    *
    * @return a DOMFactory instance per thread
    */
   public static AbstractDOMFactory instance() {
      AbstractDOMFactory fact = (AbstractDOMFactory) singlePerThread.get();

      if (fact == null) {
         try {
            fact = (AbstractDOMFactory) ReflectionUtil.newInstance(factoryClass);
         } catch (Exception e) {
            logCat.error("instance", e);
         }

         if (fact == null) {
            fact = new DOMFactoryXALANImpl();
         }

         singlePerThread.set(fact);
      }

      return fact;
   }


   /**
    * Creates a string representation of the given DOMDocument
    *
    * @param doc The document to transform
    *
    * @return string representation
    */
   public abstract String DOM2String(Document doc);


   /**
    * Creates an new DOMDocument from the given string
    *
    * @param data the string to parse
    *
    * @return The new DOMDocument
    */
   public abstract Document String2DOM(String data);


   /**
    * Creates a new empty DOMDocument
    *
    * @return An empty DOMDocument
    */
   public abstract Document newDOMDocument();


   /**
    * Reads a DOMDocument from given InputStream
    *
    * @param in The InputStream to read
    *
    * @return The new parsed DOMDocument
    */
   public abstract Document read(InputStream in);


   /**
    * Reads a DOMDocument from given url
    *
    * @param url the url to read from
    *
    * @return The new parsed DOMDocument
    */
   public Document read(String url) {
      Document doc = null;

      if (!Util.isNull(url)) {
         InputStream in   = null;
         String      path = null;
         URL         u    = null;

         try {
            u    = new URL(url);
            path = u.getPath();
         } catch (Exception e) {
            path = url;
         }

         if (u != null) {
            try {
               // Try to parse via URL connection
               URLConnection con = u.openConnection();
               con.connect();
               in = con.getInputStream();
            } catch (Exception e) {
               logCat.error("read", e);
            }
         }

         if (in == null) {
            try {
               in = new FileInputStream(path);
            } catch (Exception e) {
               logCat.error("read", e);
            }
         }

         if (in != null) {
            doc = read(in);

            try {
               in.close();
            } catch (Exception e) {
               logCat.error("read", e);
            }
         }
      }

      return doc;
   }


   /**
    * Writes a DOMElement into an OutputStream
    *
    * @param out OutputStream to write into
    * @param root root element to start writing
    */
   public abstract void write(OutputStream out,
                              Element      root) throws IOException;


   /**
    * Writes a DOMDocument into an OutputStream
    *
    * @param out OutputStream to write into
    * @param doc doc to write
    */
   public void write(OutputStream out,
                     Document     doc) throws IOException {
      write(out, doc.getDocumentElement());
   }


   /**
    * Writes an DOMDocument into a file
    *
    * @param url The url to write to
    * @param doc The document to write
    */
   public final void write(String   url,
                           Document doc) throws IOException {
      write(url, doc.getDocumentElement());
   }


   /**
    * Writes an DOMElement into a file
    *
    * @param url The url to write to
    * @param root root element to start writing
    */
   public final void write(String  url,
                           Element root) throws IOException {
      if (!Util.isNull(url) && (root != null)) {
         OutputStream out = null;

         try {
            URL           u   = new URL(url);
            URLConnection con = u.openConnection();
            con.connect();
            out = con.getOutputStream();
         } catch (Exception e) {
            logCat.error("write", e);
         }

         if (out == null) {
            try {
               out = new FileOutputStream(url);
            } catch (Exception e) {
               logCat.error("write", e);
            }
         }

         if (out != null) {
            write(out, root);
            out.close();
         } else {
            throw new IOException("no target found to wich we can write");
         }
      }
   }


   /**
    * returns an new created XPathEvaluator
    *
    * @return the new XPathEvaluator
    */
   public abstract XPathEvaluator newXPathEvaluator();


   /**
    * DOCUMENT ME!
    *
    * @param string
    */
   public static void setFactoryClass(String string) {
      factoryClass = string;
   }
}
