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

package org.dbforms.interfaces;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.xpath.XPathEvaluator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * abstract class to hide the implemtation details of the various dom
 * implementations.
 * 
 * @author Henner Kollmann
 */
public interface IDOMFactory {

	/**
	 * Creates a string representation of the given DOMDocument
	 * 
	 * @param doc
	 *            The document to transform
	 * 
	 * @return string representation
	 */
	public String DOM2String(Document doc);

	/**
	 * Creates an new DOMDocument from the given string
	 * 
	 * @param data
	 *            the string to parse
	 * 
	 * @return The new DOMDocument
	 */
	public Document String2DOM(String data);

	/**
	 * Creates a new empty DOMDocument
	 * 
	 * @return An empty DOMDocument
	 */
	public Document newDOMDocument();

	/**
	 * Reads a DOMDocument from given InputStream
	 * 
	 * @param in
	 *            The InputStream to read
	 * 
	 * @return The new parsed DOMDocument
	 */
	public Document read(InputStream in);

	/**
	 * Reads a DOMDocument from given url
	 * 
	 * @param url
	 *            the url to read from
	 * 
	 * @return The new parsed DOMDocument
	 */
	public Document read(String url);

	/**
	 * Writes a DOMElement into an OutputStream
	 * 
	 * @param out
	 *            OutputStream to write into
	 * @param root
	 *            root element to start writing
	 */
	public void write(OutputStream out, Element root) throws IOException;

	/**
	 * Writes a DOMDocument into an OutputStream
	 * 
	 * @param out
	 *            OutputStream to write into
	 * @param doc
	 *            doc to write
	 */
	public void write(OutputStream out, Document doc) throws IOException;

	/**
	 * Writes an DOMDocument into a file
	 * 
	 * @param url
	 *            The url to write to
	 * @param doc
	 *            The document to write
	 */
	public void write(String url, Document doc) throws IOException;

	/**
	 * Writes an DOMElement into a file
	 * 
	 * @param url
	 *            The url to write to
	 * @param root
	 *            root element to start writing
	 */
	public void write(String url, Element root) throws IOException;

	/**
	 * returns an new created XPathEvaluator
	 * 
	 * @return the new XPathEvaluator
	 */
	public XPathEvaluator newXPathEvaluator();

}
