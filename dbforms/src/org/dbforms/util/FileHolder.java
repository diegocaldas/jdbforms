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
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.File;



/**
 * A <code>FileHolder</code> holds data saved from a
 * com.oreilly.servlet.multipart.FilePart either as byteArray [now] or as
 * reference to temp-file[future versions]
 * 
 * @author Joe Peer
 */
public class FileHolder
{
   private boolean         toMemory;

   // "file system" name of the file
   private String fileName;

   // content type of the file
   private String contentType;

   // buffer to hold data till its use (used if memory buffering choosen)
   private byte[] memoryBuffer;

   // stores the length of the file
   private int fileLength;

   /**
    * Constructor - takes descriptive data (fileName, contentType) -
    * inputstream, coming from servletinputstream - we must read it out _now_
    * - toMemory: true->write it to memory (implemented), false->write it to
    * tempfile (not implemented yet)
    * 
    * @param fileName DOCUMENT ME!
    * @param contentType DOCUMENT ME!
    * @param is DOCUMENT ME!
    * @param toMemory DOCUMENT ME!
    * @param maxSize DOCUMENT ME!
    * 
    * @throws IOException DOCUMENT ME!
    * @throws IllegalArgumentException DOCUMENT ME!
    */
   public FileHolder(String fileName, String contentType, InputStream is, 
                     boolean toMemory, int maxSize)
              throws IOException, IllegalArgumentException
   {
      this.toMemory    = toMemory;
      this.fileName    = fileName;
      this.contentType = contentType;

      if (toMemory)
      {
         bufferInMemory(maxSize, is);
      }
      else
      {
         throw new IllegalArgumentException(
                  "tmpFile-feature not implemented yet");
      }
   }

   /**
    * Returns the name that the file was stored with on the remote system, or
    * <code>null</code> if the user didn't enter a file to be uploaded. Note:
    * this is not the same as the name of the form parameter used to transmit
    * the file; that is available from the <code>getName</code> method.
    * 
    * @return name of file uploaded or <code>null</code>.
    * 
    * @see Part#getName()
    */
   public String getFileName()
   {
      return fileName;
   }


   /**
    * Set the name of the file.
    * 
    * @param fileName DOCUMENT ME!
    */
   public void setFileName(String fileName)
   {
      this.fileName = fileName;
   }


   /**
    * Returns the content type of the file data contained within.
    * 
    * @return content type of the file data.
    */
   public String getContentType()
   {
      return contentType;
   }


   /**
    * <p>
    * Returns the length of the file representated by this FileHolder
    * </p>
    * 
    * <p>
    * fileLength gets determinated either during "bufferInMemory" or
    * "bufferInFile" (not impl. yet)
    * </p>
    * 
    * @return content type of the file data.
    */
   public int getFileLength()
   {
      return fileLength;
   }


   private void bufferInMemory(int maxSize, InputStream partInput)
                        throws IOException
   {
      byte[] tmpMemoryBuffer = new byte[maxSize]; // this could lead to memory problems. if it does used filebuffering instead
      fileLength   = partInput.read(tmpMemoryBuffer);
      memoryBuffer = new byte[fileLength];
      System.arraycopy(tmpMemoryBuffer, 0, memoryBuffer, 0, fileLength);
      tmpMemoryBuffer = null;
   }


   /**
    * DOCUMENT ME!
    * 
    * @return DOCUMENT ME!
    * 
    */
   public InputStream getInputStreamFromBuffer()
   {
      if (toMemory)
      {
         return new ByteArrayInputStream(memoryBuffer);
      }
      else
      {
         throw new IllegalArgumentException(
                  "tmpFile-feature not implemented yet");
      }
   }


   /**
    * DOCUMENT ME!
    * 
    * @return DOCUMENT ME!
    */
   public byte[] getMemoryBuffer()
   {
      return memoryBuffer;
   }


   /**
    * Writes out the the file in memory to disk.
    * 
    * @param fileOrDirectory The file, or directory to write the file to.  If
    *        it is a directory, you must provide a file already.
    * 
    * @throws IOException Thrown if there are rpoblems writing the file.
    */
   public void writeBufferToFile(File fileOrDirectory)
                          throws IOException
   {
      if (!toMemory)
      {
         throw new IllegalArgumentException(
                  "tmpFile-feature not implemented yet");
      }

      OutputStream fileOut = null;

      try
      {
         // Only do something if this part contains a file
         if (fileName != null)
         {
            // Check if user supplied directory
            File file;

            if (fileOrDirectory.isDirectory())
            {
               // Write it to that dir the user supplied,
               // with the filename it arrived with
               file = new File(fileOrDirectory, fileName);
            }
            else
            {
               // Write it to the file the user supplied,
               // ignoring the filename it arrived with
               file = fileOrDirectory;
            }

            fileOut = new BufferedOutputStream(new FileOutputStream(file));
            fileOut.write(memoryBuffer, 0, memoryBuffer.length);
         }
      }
      finally
      {
         if (fileOut != null)
         {
            fileOut.close();
         }
      }
   }
}