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
package org.dbforms.util;
import java.io.InputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;

import org.apache.log4j.Category;



/**
 * A utility class to handle <code>multipart/form-data</code> requests,
 * the kind of requests that support file uploads.  This class emulates the
 * interface of <code>HttpServletRequest</code>, making it familiar to use.
 * It uses a "push" model where any incoming files are read and saved directly
 * to disk in the constructor. If you wish to have more flexibility, e.g.
 * write the files to a database, use the "pull" model
 * <code>MultipartParser</code> instead.
 * <p>
 * This class can receive arbitrarily large files (up to an artificial limit
 * you can set), and fairly efficiently too.
 * It cannot handle nested data (multipart content within multipart content)
 * or internationalized content (such as non Latin-1 filenames).
 * </p><p>
 * See the included <a href="upload.war">upload.war</a>
 * for an example of how to use this class.
 * <p>
 * The full file upload specification is contained in experimental RFC 1867,
 * available at <a href="http://www.ietf.org/rfc/rfc1867.txt">
 * http://www.ietf.org/rfc/rfc1867.txt</a>.
 * </p>
 * <p>
 * PLEASE NOTE: this class uses Multipart Support Classes by Jason Hunter.
 * Copyright (C) 1998 by Jason Hunter <jhunter@acm.org>.  All rights reserved.
 * Use of this class is limited.  Please see the LICENSE for more information.
 * Make sure that you project meets the requierements defined by Jason Hunter
 * respective O'reilly!</p>
 *
 * @see MultipartParser
 *
 * @author Jason Hunter
 * @author Geoff Soutter
 * @author Joe Peer - _changed_ it for use in DbForms (i apologize)
 */
public class MultipartRequest
{
   private static Category          logCat                = Category.getInstance(MultipartRequest.class
         .getName()); // logging category for this class

   //private File dir;
   private Hashtable       parameters = new Hashtable(); // name - Vector of values
   private Hashtable       files  = new Hashtable(); // name - UploadedFile
   private MultipartParser parser;

   /**
    * Constructs a new MultipartRequest to handle the specified request,
    * {saving any uploaded files to the given directory}, and limiting the
    * upload size to the specified length.  If the content is too large, an
    * IOException is thrown.  This constructor actually parses the
    * <tt>multipart/form-data</tt> and throws an IOException if there's any
    * problem reading or parsing the request.
    *
    * @param request the servlet request.
    * @param maxPostSize the maximum size of the POST content.
    * @exception IOException if the uploaded content is larger than
    * <tt>maxPostSize</tt> or there's a problem reading or parsing the request.
    */
   public MultipartRequest(HttpServletRequest request, int maxPostSize)
      throws IOException
   {
      // Sanity check values
      if (request == null)
      {
         throw new IllegalArgumentException("request cannot be null");
      }

      // Parse the incoming multipart, storing files in the dir provided,
      // and populate the meta objects which describe what we found
      logCat.info("maxpostsize=" + maxPostSize);
      parser = new MultipartParser(request, maxPostSize);

      Part part;

      while ((part = parser.readNextPart()) != null)
      {
         String name = part.getName();
         logCat.info("partname=" + name);

         if (part.isParam())
         {
            // It's a parameter part, add it to the vector of values
            ParamPart paramPart = (ParamPart) part;
            String    value = paramPart.getStringValue();

            // plain parameters get allways into parameters structure
            Vector existingValues = (Vector) parameters.get(name);

            if (existingValues == null)
            {
               existingValues = new Vector();
               parameters.put(name, existingValues);
            }

            existingValues.addElement(value);
         }
         else if (part.isFile())
         {
            // It's a file part
            FilePart filePart = (FilePart) part;
            String   fileName = filePart.getFileName();

            if (fileName != null)
            {
               // The part actually contained a file
               // #changes by joe peer:
               // we must delay storing the file-inputstream (into database, filesystem or whatever)
               // until we know exactly what should happen with it
               FileHolder fileHolder = new FileHolder(fileName,
                     filePart.getContentType(), filePart.getInputStream(),
                     true, maxPostSize);
               files.put(name, fileHolder);
               logCat.info("buffered and now added as " + name
                  + " the following fileHolder:" + fileHolder.getFileName());

               //      InputStream is = filePart.getInputStream();
               //      while(is.read() != -1) logCat.info("b");
               //      logCat.info("fin_");
               // #changes by joe peer:
               // if a file parameter is not null it gets into the parameters structures as well
               // this is important to provide a simple access to request (no distinction between files and plain params)
               Vector existingValues = (Vector) parameters.get(name);

               if (existingValues == null)
               {
                  existingValues = new Vector();
                  parameters.put(name, existingValues);
               }

               existingValues.addElement(fileName);
            }

            /* else {
            // The field did not contain a file
            files.put(name, new UploadedFile(null, null, null));
            }*/
            logCat.info("loop end");
         }
      }
   }

   /**
    * Returns the names of all the parameters as an Enumeration of
    * Strings.  It returns an empty Enumeration if there are no parameters.
    *
    * @return the names of all the parameters as an Enumeration of Strings.
    */
   public Enumeration getParameterNames()
   {
      return parameters.keys();
   }


   /**
    * Returns the names of all the uploaded files as an Enumeration of
    * Strings.  It returns an empty Enumeration if there are no uploaded
    * files.  Each file name is the name specified by the form, not by
    * the user.
    *
    * @return the names of all the uploaded files as an Enumeration of Strings.
    */
   public Enumeration getFileNames()
   {
      return files.keys();
   }


   /**
    * Returns the value of the named parameter as a String, or null if
    * the parameter was not sent or was sent without a value.  The value
    * is guaranteed to be in its normal, decoded form.  If the parameter
    * has multiple values, only the last one is returned (for backward
    * compatibility).  For parameters with multiple values, it's possible
    * the last "value" may be null.
    *
    * @param name the parameter name.
    * @return the parameter value.
    */
   public String getParameter(String name)
   {
      try
      {
         Vector values = (Vector) parameters.get(name);

         if ((values == null) || (values.size() == 0))
         {
            return null;
         }

         String value = (String) values.elementAt(values.size() - 1);

         return value;
      }
      catch (Exception e)
      {
         return null;
      }
   }


   /**
    * Returns the values of the named parameter as a String array, or null if
    * the parameter was not sent.  The array has one entry for each parameter
    * field sent.  If any field was sent without a value that entry is stored
    * in the array as a null.  The values are guaranteed to be in their
    * normal, decoded form.  A single value is returned as a one-element array.
    *
    * @param name the parameter name.
    * @return the parameter values.
    */
   public String[] getParameterValues(String name)
   {
      try
      {
         Vector values = (Vector) parameters.get(name);

         if ((values == null) || (values.size() == 0))
         {
            return null;
         }

         String[] valuesArray = new String[values.size()];
         values.copyInto(valuesArray);

         return valuesArray;
      }
      catch (Exception e)
      {
         return null;
      }
   }


   /**
    * Returns the filesystem name of the specified file, or null if the
    * file was not included in the upload.  A filesystem name is the name
    * specified by the user.  It is also the name under which the file is
    * actually saved.
    *
    * @param name the file name.
    * @return the filesystem name of the file.
    */
   public String getFilesystemName(String name)
   {
      try
      {
         //#changed by joe peer
         //UploadedFile file = (UploadedFile)files.get(name);
         FilePart filePart = (FilePart) files.get(name);

         return filePart.getFileName(); // may be null
      }
      catch (Exception e)
      {
         return null;
      }
   }


   /**
    * Returns the content type of the specified file (as supplied by the
    * client browser), or null if the file was not included in the upload.
    *
    * @param name the file name.
    * @return the content type of the file.
    */
   public String getContentType(String name)
   {
      try
      {
         //#changed by joe peer
         //UploadedFile file = (UploadedFile)files.get(name);
         FilePart filePart = (FilePart) files.get(name);

         return filePart.getContentType(); // may be null
      }
      catch (Exception e)
      {
         return null;
      }
   }


   /**
    * Returns a InputStream object for the specified file saved on the server's
    * filesystem, or null if the file was not included in the upload.
    *
    * @param name the file name.
    * @return a InputStream object for the named file.
    */
   public InputStream getFileInputStream(String name)
   {
      try
      {
         //#changed by joe peer
         //UploadedFile file = (UploadedFile)files.get(name);
         FilePart filePart = (FilePart) files.get(name);

         return filePart.getInputStream(); // may be null
      }
      catch (Exception e)
      {
         return null;
      }
   }


   /**
    * Returns a FilePart object for the specified file
    * this method was added by joe peer
    *
    * @param name the file name.
    * @return a FilePart object for the named file.
    */
   public FileHolder getFileHolder(String name)
   {
      try
      {
         return (FileHolder) files.get(name);
      }
      catch (Exception e)
      {
         return null;
      }
   }
}
