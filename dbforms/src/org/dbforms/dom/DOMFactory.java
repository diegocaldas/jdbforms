/*
 * Created on 12.10.2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.dbforms.dom;


import java.net.URL;
import java.net.URLConnection;

import java.io.FileOutputStream;
import java.io.OutputStream;
import org.w3c.dom.Document;
import org.w3c.dom.xpath.XPathEvaluator;

import org.apache.log4j.Category;

import org.dbforms.util.Util;

/**
 * @author hkk
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class DOMFactory {
   private final static ThreadLocal singlePerThread = new ThreadLocal();

   private Category logCat = Category.getInstance(this.getClass().getName());

   public static DOMFactory instance() {
      DOMFactory fact = (DOMFactory) singlePerThread.get();
      if (fact == null) {
         fact = new DOMFactorySAXImpl();
         singlePerThread.set(fact);
      }
      return fact;
   }

   protected DOMFactory() {}

   public abstract Document newDOMDocument();
   public abstract String DOM2String(Document doc);
   public abstract Document String2DOM(String data);
   public abstract Document read(String url);
   public abstract void write(OutputStream out, Document doc);

   public abstract XPathEvaluator newXPathEvaluator();

   
   public final void write(String url, Document doc) {
      if (!Util.isNull(url) && (doc != null)) {
         OutputStream out = null;
         try {
            URL u = new URL(url);
            URLConnection con = u.openConnection();
            con.connect();
            out = con.getOutputStream();
         } catch (Exception e) {
            logCat.error(e);
         }
         if (out == null) {
            try {
               out = new FileOutputStream(url);
            } catch (Exception e) {
               logCat.error(e);
            }
         }
         if (out != null) {
            try {
               write(out, doc);
               out.close();
            } catch (Exception e) {
               logCat.error(e);
            }
         }
      }
   }


}
