/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * DbForms - a Rapid Application Development Framework
 * Copyright (C) 2001 Joachim Peer <j.peer@gmx.net> et al.
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.dbforms.devgui;

import java.io.*;
import org.xml.sax.SAXException;
import org.apache.xalan.xslt.XSLTProcessorFactory;
import org.apache.xalan.xslt.XSLTInputSource;
import org.apache.xalan.xslt.XSLTResultTarget;
import org.apache.xalan.xslt.XSLTProcessor;

/**
 * Simple sample code to show how to run the XSL processor
 * from the API.
 */
public class XSLTransformer
{


    public static void transform(File xmlFile, File xslFile, File destinationFile)
    throws java.io.IOException,
           java.net.MalformedURLException,
           org.xml.sax.SAXException
	{

    	// Have the XSLTProcessorFactory obtain a interface to a
    	// new XSLTProcessor object.
    	XSLTProcessor processor = XSLTProcessorFactory.getProcessor();

    	// Have the XSLTProcessor processor object transform "foo.xml" to
    	// System.out, using the XSLT instructions found in "foo.xsl".
	    processor.process(new XSLTInputSource(new FileInputStream(xmlFile)),
    	                  new XSLTInputSource(new FileInputStream(xslFile)),
	                      new XSLTResultTarget(new FileOutputStream(destinationFile)));
	}
}
