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

import java.io.IOException;

/**
 * Implementation of a JspWriter which do not write anything
 */
import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspWriter;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class JspWriterBuffer extends JspWriter {
   private StringBuffer buf = new StringBuffer();

   /**
    * Creates a new JspWriterDummy object.
    *
    * @param bufferSize DOCUMENT ME!
    * @param autoFlush DOCUMENT ME!
    * @param response DOCUMENT ME!
    */
   protected JspWriterBuffer(int             bufferSize,
                             boolean         autoFlush,
                             ServletResponse response) {
      super(bufferSize, autoFlush);
   }

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public StringBuffer getBuffer() {
      return buf;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public int getRemaining() {
      return 0;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getResult() {
      return buf.toString();
   }


   /**
    * DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void clear() throws IOException {
      clearBuffer();
   }


   /**
    * DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void clearBuffer() throws IOException {
      buf = new StringBuffer();
   }


   /**
    * DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void close() throws IOException {
   }


   /**
    * DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void flush() throws IOException {
   }


   /**
    * DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void newLine() throws IOException {
   }


   /**
    * DOCUMENT ME!
    *
    * @param b DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void print(boolean b) throws IOException {
      buf.append(b);
   }


   /**
    * DOCUMENT ME!
    *
    * @param c DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void print(char c) throws IOException {
      buf.append(c);
   }


   /**
    * DOCUMENT ME!
    *
    * @param s DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void print(char[] s) throws IOException {
      buf.append(s);
   }


   /**
    * DOCUMENT ME!
    *
    * @param i DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void print(int i) throws IOException {
      buf.append(i);
   }


   /**
    * DOCUMENT ME!
    *
    * @param l DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void print(long l) throws IOException {
      buf.append(l);
   }


   /**
    * DOCUMENT ME!
    *
    * @param f DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void print(float f) throws IOException {
      buf.append(f);
   }


   /**
    * DOCUMENT ME!
    *
    * @param d DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void print(double d) throws IOException {
      buf.append(d);
   }


   /**
    * DOCUMENT ME!
    *
    * @param s DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void print(String s) throws IOException {
      buf.append(s);
   }


   /**
    * DOCUMENT ME!
    *
    * @param obj DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void print(Object obj) throws IOException {
      buf.append(obj);
   }


   /**
    * DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void println() throws IOException {
      buf.append('\n');
   }


   /**
    * DOCUMENT ME!
    *
    * @param x DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void println(boolean x) throws IOException {
      print(x);
      println();
   }


   /**
    * DOCUMENT ME!
    *
    * @param x DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void println(char x) throws IOException {
      print(x);
      println();
   }


   /**
    * DOCUMENT ME!
    *
    * @param x DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void println(int x) throws IOException {
      print(x);
      println();
   }


   /**
    * DOCUMENT ME!
    *
    * @param x DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void println(long x) throws IOException {
      print(x);
      println();
   }


   /**
    * DOCUMENT ME!
    *
    * @param x DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void println(float x) throws IOException {
      print(x);
      println();
   }


   /**
    * DOCUMENT ME!
    *
    * @param x DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void println(double x) throws IOException {
      print(x);
      println();
   }


   /**
    * DOCUMENT ME!
    *
    * @param x DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void println(char[] x) throws IOException {
      print(x);
      println();
   }


   /**
    * DOCUMENT ME!
    *
    * @param x DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void println(String x) throws IOException {
      print(x);
      println();
   }


   /**
    * DOCUMENT ME!
    *
    * @param x DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void println(Object x) throws IOException {
      print(x);
      println();
   }


   /**
    * DOCUMENT ME!
    *
    * @param c DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public void write(int c) throws IOException {
      buf.append(c);
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
   public void write(char[] cbuf,
                     int    off,
                     int    len) throws IOException {
      buf.append(cbuf, off, len);
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
   public void write(String str,
                     int    off,
                     int    len) throws IOException {
      write(str.toCharArray(), off, len);
   }
}
