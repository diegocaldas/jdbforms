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
package org.dbforms.xmldb;
import java.io.*;
import java.util.*;



/**
 * DOCUMENT ME!
 *
 * @version $Revision$
 * @author $author$
 */
public class FileSplitter
{
   private String sourceFile;
   private String destDir;
   File           fSource;
   File           fDestDir;

   /**
    * Creates a new FileSplitter object.
    *
    * @param s DOCUMENT ME!
    * @param d DOCUMENT ME!
    */
   public FileSplitter(File s, File d)
   {
      this.fSource     = s;
      this.fDestDir    = d;

      testFiles();
   }


   /**
    * Creates a new FileSplitter object.
    *
    * @param sourceFile DOCUMENT ME!
    * @param destDir DOCUMENT ME!
    */
   public FileSplitter(String sourceFile, String destDir)
   {
      this.sourceFile    = sourceFile;
      this.destDir       = destDir;

      this.fSource     = new File(sourceFile);
      this.fDestDir    = new File(destDir);

      testFiles();
   }

   private void testFiles()
   {
      // preparing INPUT
      if (!fSource.exists())
      {
         System.out.println("ERROR: source file not found");
         System.exit(1);
      }

      if (!fSource.canRead())
      {
         System.out.println("ERROR: source file not readable for this process");
         System.exit(1);
      }

      // preparing OUTPUT
      if (!fDestDir.exists())
      {
         System.out.println("ERROR: destination directory not found");
         System.exit(1);
      }

      if (!fDestDir.isDirectory())
      {
         System.out.println("ERROR: destination is not a directory");
         System.exit(1);
      }

      if (!fDestDir.canWrite())
      {
         System.out.println(
            "ERROR: destination directory not writable for this process");
         System.exit(1);
      }
   }


   /**
    * DOCUMENT ME!
    */
   public void splitFile()
   {
      try
      {
         BufferedReader in       = new BufferedReader(new FileReader(fSource));
         BufferedWriter out      = null;
         String         line     = null;
         String         fileName = null;

         while ((line = in.readLine()) != null)
         {
            if (line.startsWith("//--file"))
            {
               int preBegin = line.indexOf('\"');
               int postEnd = line.indexOf('\"', preBegin + 1);

               fileName = line.substring(preBegin + 1, postEnd);

               System.out.println("current file:" + fileName);

               if (out != null)
               {
                  out.close();
               }

               out = new BufferedWriter(new FileWriter(
                        new File(fDestDir, fileName)));
            }
            else
            {
               if (out != null)
               {
                  out.write(line);
                  out.newLine();
               }
            }
         }

         if (out != null)
         {
            out.close();
         }
      }
      catch (IOException ioe)
      {
         System.out.println("ERROR: " + ioe.toString());
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param args DOCUMENT ME!
    */
   public static void main(String[] args)
   {
      if (args.length != 2)
      {
         System.out.println(
            "usage: java FileSplitter sourceFile destinationDirectory\n\nexample:\njava FileSplitter source.jsp c:\\tomcat\\webapps\\your_app\\");
         System.exit(1);
      }

      new FileSplitter(args[0], args[1]).splitFile();
   }
}
