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

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.xpath.XPathEvaluator;
import org.apache.log4j.Category;
import org.dbforms.util.Util;



/**
 * abstract class to hide the implemtation details of the various dom
 * implementations.
 * 
 * @author Henner Kollmann
 */
public abstract class DOMFactory
{
   private final static ThreadLocal singlePerThread = new ThreadLocal();
   private Category                 logCat = Category.getInstance(
                                                      this.getClass().getName());

   /**
    * Get the thread singelton instance
    *
    * @return a DOMFactory instance per thread
    */ 
   public static DOMFactory instance()
   {
      DOMFactory fact = (DOMFactory) singlePerThread.get();

      if (fact == null)
      {
         fact = new DOMFactorySAXImpl();
         singlePerThread.set(fact);
      }

      return fact;
   }

   /**
    * Creates a new DOMFactory object.
    */
   protected DOMFactory()
   {
   }

   /**
    * Creates a new empty DOMDocument
    *
    * @return An empty DOMDocument
    */
   public abstract Document newDOMDocument();


	/**
	 * Creates an new DOMDocument from the given string
	 *
	 * @param data the string to parse
	 *
	 * @return The new DOMDocument
	 */
	public abstract Document String2DOM(String data);

   /**
    * Creates a string representation of the given DOMDocument 
    *
    * @param doc The document to transform
    *
    * @return string representation
    */
   public abstract String DOM2String(Document doc);

   /**
    * Reads a DOMDocument from given url
    *
    * @param url the url to read from
    *
    * @return The new parsed DOMDocument 
    */
   public abstract Document read(String url);


   /**
    * Writes a DOMDocument into an OutputStream
    *
    * @param out OutputStream to write into
    * @param doc The Ddcument to write
    */
   public abstract void write(OutputStream out, Document doc) throws IOException;


	/**
	 * Writes an DOMDocument into a file
	 *
	 * @param url The url to write to
	 * @param doc The document to write
	 */
	public final void write(String url, Document doc) throws IOException
	{
		if (!Util.isNull(url) && (doc != null))
		{
			OutputStream out = null;

			try
			{
				URL           u   = new URL(url);
				URLConnection con = u.openConnection();
				con.connect();
				out = con.getOutputStream();
			}
			catch (Exception e)
			{
				logCat.error(e);
			}

			if (out == null)
			{
				try
				{
					out = new FileOutputStream(url);
				}
				catch (Exception e)
				{
					logCat.error(e);
				}
			}

			if (out != null)
			{
				write(out, doc);
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


}
 