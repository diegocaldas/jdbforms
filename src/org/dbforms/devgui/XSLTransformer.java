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

package org.dbforms.devgui;

import org.dbforms.util.Util;

import java.io.File;
import java.io.FileNotFoundException;

// Imported java classes
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Imported TraX classes
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;



/**
 * Simple sample code to show how to run the XSL processor from the API.
 */
public class XSLTransformer {
   /**
    * DOCUMENT ME!
    *
    * @param xmlFile DOCUMENT ME!
    * @param xslFile DOCUMENT ME!
    * @param destinationFile DOCUMENT ME!
    * @param useJsCalendar DOCUMENT ME!
    * @param xsltEncoding DOCUMENT ME!
    *
    * @throws TransformerException DOCUMENT ME!
    * @throws TransformerConfigurationException DOCUMENT ME!
    * @throws FileNotFoundException DOCUMENT ME!
    * @throws IOException DOCUMENT ME!
    */
   public static void transform(File    xmlFile,
                                File    xslFile,
                                File    destinationFile,
                                boolean useJsCalendar,
                                String  xsltEncoding)
                         throws TransformerException, 
                                TransformerConfigurationException, 
                                FileNotFoundException, IOException {
      // Use the static TransformerFactory.newInstance() method to instantiate 
      // a TransformerFactory. The javax.xml.transform.TransformerFactory 
      // system property setting determines the actual class to instantiate --
      // org.apache.xalan.transformer.TransformerImpl.
      TransformerFactory tFactory = TransformerFactory.newInstance();

      // Use the TransformerFactory to instantiate a Transformer that will work with  
      // the stylesheet you specify. This method call also processes the stylesheet
      // into a compiled Templates object.
      Transformer transformer = tFactory.newTransformer(new StreamSource(xslFile));

      // Setting the XSLT encoding configured in the XSL Transformation Panel
      if (!Util.isNull(xsltEncoding)) {
         transformer.setOutputProperty(OutputKeys.ENCODING, xsltEncoding);
      }

      // If user has checked checkbox to use JavaScript Calendar for editing of
      // date fields, we have to pass a corresponding parameter to transformer
      if (useJsCalendar) {
         transformer.setParameter("useCalendar", "true");
      }

      // Use the Transformer to apply the associated Templates object to an XML document
      // (foo.xml) and write the output to a file (foo.out).
      transformer.transform(new StreamSource(xmlFile),
                            new StreamResult(new FileOutputStream(destinationFile)));
   }
}
