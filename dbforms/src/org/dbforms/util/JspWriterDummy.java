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
/*
 * $Header$
 * $Revision$
 * $Date$
 *
 */
package org.dbforms.util;

/**
 * Implementation of a JspWriter which do not write anything
 *
 */
import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspWriter;
import java.io.PrintWriter;
import java.io.IOException;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class JspWriterDummy extends JspWriter
{
   /**
    * Creates a new JspWriterDummy object.
    *
    * @param bufferSize DOCUMENT ME!
    * @param autoFlush DOCUMENT ME!
    * @param response DOCUMENT ME!
    */
   protected JspWriterDummy(int bufferSize, boolean autoFlush,
      ServletResponse response)
   {
      super(bufferSize, autoFlush);
   }

   /**
    * DOCUMENT ME!
    *
    * @param c DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void write(int c) throws IOException
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @param cbuf DOCUMENT ME!
    * @param off DOCUMENT ME!
    * @param len DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void write(char[] cbuf, int off, int len) throws IOException
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @param str DOCUMENT ME!
    * @param off DOCUMENT ME!
    * @param len DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void write(String str, int off, int len) throws IOException
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void clear() throws IOException
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void clearBuffer() throws IOException
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void close() throws IOException
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void flush() throws IOException
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public int getRemaining()
   {
      return 0;
   }


   /**
    * DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void newLine() throws IOException
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @param b DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void print(boolean b) throws IOException
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @param c DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void print(char c) throws IOException
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @param s DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void print(char[] s) throws IOException
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @param i DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void print(int i) throws IOException
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @param l DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void print(long l) throws IOException
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @param f DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void print(float f) throws IOException
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @param d DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void print(double d) throws IOException
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @param s DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void print(String s) throws IOException
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @param obj DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void print(Object obj) throws IOException
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void println() throws IOException
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @param x DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void println(boolean x) throws IOException
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @param x DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void println(char x) throws IOException
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @param x DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void println(int x) throws IOException
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @param x DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void println(long x) throws IOException
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @param x DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void println(float x) throws IOException
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @param x DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void println(double x) throws IOException
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @param x DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void println(char[] x) throws IOException
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @param x DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void println(String x) throws IOException
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @param x DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void println(Object x) throws IOException
   {
   }
}
