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
 * IOUtil class taken from jakarta-commons-sandbox project.
 *
 *
*/
package org.dbforms.util.external;


/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 *    "Apache Turbine" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    "Apache Turbine", nor may "Apache" appear in their name, without
 *    prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public final class IOUtil {
   private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

   /**
    * Private constructor to prevent instantiation.
    */
   private IOUtil() {
   }

   /**
    * Copy bytes from an <code>InputStream</code> to an
    * <code>OutputStream</code>, with buffering. This is equivalent to passing
    * a {@link java.io.BufferedInputStream} and {@link
    * java.io.BufferedOutputStream} to {@link #copy(InputStream,
    * OutputStream)}, and flushing the output stream afterwards. The streams
    * are not closed after the copy.
    *
    * @param input DOCUMENT ME!
    * @param output DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    *
    * @deprecated Buffering streams is actively harmful! See the class
    *             description as to why. Use {@link #copy(InputStream,
    *             OutputStream)} instead.
    */
   public static void bufferedCopy(final InputStream  input,
                                   final OutputStream output)
                            throws IOException {
      final BufferedInputStream  in  = new BufferedInputStream(input);
      final BufferedOutputStream out = new BufferedOutputStream(output);
      copy(in, out);
      out.flush();
   }


   /**
    * Compare the contents of two Streams to determine if they are equal or
    * not.
    *
    * @param input1 the first stream
    * @param input2 the second stream
    *
    * @return true if the content of the streams are equal or they both don't
    *         exist, false otherwise
    *
    * @throws IOException DOCUMENT ME!
    */
   public static boolean contentEquals(final InputStream input1,
                                       final InputStream input2)
                                throws IOException {
      final InputStream bufferedInput1 = new BufferedInputStream(input1);
      final InputStream bufferedInput2 = new BufferedInputStream(input2);

      int               ch = bufferedInput1.read();

      while (-1 != ch) {
         final int ch2 = bufferedInput2.read();

         if (ch != ch2) {
            return false;
         }

         ch = bufferedInput1.read();
      }

      final int ch2 = bufferedInput2.read();

      if (-1 != ch2) {
         return false;
      } else {
         return true;
      }
   }


   ///////////////////////////////////////////////////////////////
   // Core copy methods
   ///////////////////////////////////////////////////////////////

   /**
    * Copy bytes from an <code>InputStream</code> to an
    * <code>OutputStream</code>.
    *
    * @param input DOCUMENT ME!
    * @param output DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public static void copy(final InputStream  input,
                           final OutputStream output) throws IOException {
      copy(input, output, DEFAULT_BUFFER_SIZE);
   }


   /**
    * Copy bytes from an <code>InputStream</code> to an
    * <code>OutputStream</code>.
    *
    * @param input DOCUMENT ME!
    * @param output DOCUMENT ME!
    * @param bufferSize Size of internal buffer to use.
    *
    * @throws IOException DOCUMENT ME!
    */
   public static void copy(final InputStream  input,
                           final OutputStream output,
                           final int          bufferSize)
                    throws IOException {
      final byte[] buffer = new byte[bufferSize];
      int          n = 0;

      while (-1 != (n = input.read(buffer))) {
         output.write(buffer, 0, n);
      }
   }


   /**
    * Copy chars from a <code>Reader</code> to a <code>Writer</code>.
    *
    * @param input DOCUMENT ME!
    * @param output DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public static void copy(final Reader input,
                           final Writer output) throws IOException {
      copy(input, output, DEFAULT_BUFFER_SIZE);
   }


   /**
    * Copy chars from a <code>Reader</code> to a <code>Writer</code>.
    *
    * @param input DOCUMENT ME!
    * @param output DOCUMENT ME!
    * @param bufferSize Size of internal buffer to use.
    *
    * @throws IOException DOCUMENT ME!
    */
   public static void copy(final Reader input,
                           final Writer output,
                           final int    bufferSize) throws IOException {
      final char[] buffer = new char[bufferSize];
      int          n = 0;

      while (-1 != (n = input.read(buffer))) {
         output.write(buffer, 0, n);
      }
   }


   ///////////////////////////////////////////////////////////////
   // Derived copy methods
   // InputStream -> *
   ///////////////////////////////////////////////////////////////
   ///////////////////////////////////////////////////////////////
   // InputStream -> Writer

   /**
    * Copy and convert bytes from an <code>InputStream</code> to chars on a
    * <code>Writer</code>. The platform's default encoding is used for the
    * byte-to-char conversion.
    *
    * @param input DOCUMENT ME!
    * @param output DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public static void copy(final InputStream input,
                           final Writer      output) throws IOException {
      copy(input, output, DEFAULT_BUFFER_SIZE);
   }


   /**
    * Copy and convert bytes from an <code>InputStream</code> to chars on a
    * <code>Writer</code>. The platform's default encoding is used for the
    * byte-to-char conversion.
    *
    * @param input DOCUMENT ME!
    * @param output DOCUMENT ME!
    * @param bufferSize Size of internal buffer to use.
    *
    * @throws IOException DOCUMENT ME!
    */
   public static void copy(final InputStream input,
                           final Writer      output,
                           final int         bufferSize)
                    throws IOException {
      final InputStreamReader in = new InputStreamReader(input);
      copy(in, output, bufferSize);
   }


   /**
    * Copy and convert bytes from an <code>InputStream</code> to chars on a
    * <code>Writer</code>, using the specified encoding.
    *
    * @param input DOCUMENT ME!
    * @param output DOCUMENT ME!
    * @param encoding The name of a supported character encoding. See the <a
    *        href="http://www.iana.org/assignments/character-sets">IANA
    *        Charset Registry</a> for a list of valid encoding types.
    *
    * @throws IOException DOCUMENT ME!
    */
   public static void copy(final InputStream input,
                           final Writer      output,
                           final String      encoding)
                    throws IOException {
      final InputStreamReader in = new InputStreamReader(input, encoding);
      copy(in, output);
   }


   /**
    * Copy and convert bytes from an <code>InputStream</code> to chars on a
    * <code>Writer</code>, using the specified encoding.
    *
    * @param input DOCUMENT ME!
    * @param output DOCUMENT ME!
    * @param encoding The name of a supported character encoding. See the <a
    *        href="http://www.iana.org/assignments/character-sets">IANA
    *        Charset Registry</a> for a list of valid encoding types.
    * @param bufferSize Size of internal buffer to use.
    *
    * @throws IOException DOCUMENT ME!
    */
   public static void copy(final InputStream input,
                           final Writer      output,
                           final String      encoding,
                           final int         bufferSize)
                    throws IOException {
      final InputStreamReader in = new InputStreamReader(input, encoding);
      copy(in, output, bufferSize);
   }


   ///////////////////////////////////////////////////////////////
   // Derived copy methods
   // Reader -> *
   ///////////////////////////////////////////////////////////////
   ///////////////////////////////////////////////////////////////
   // Reader -> OutputStream

   /**
    * Serialize chars from a <code>Reader</code> to bytes on an
    * <code>OutputStream</code>, and flush the <code>OutputStream</code>.
    *
    * @param input DOCUMENT ME!
    * @param output DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public static void copy(final Reader       input,
                           final OutputStream output) throws IOException {
      copy(input, output, DEFAULT_BUFFER_SIZE);
   }


   /**
    * Serialize chars from a <code>Reader</code> to bytes on an
    * <code>OutputStream</code>, and flush the <code>OutputStream</code>.
    *
    * @param input DOCUMENT ME!
    * @param output DOCUMENT ME!
    * @param bufferSize Size of internal buffer to use.
    *
    * @throws IOException DOCUMENT ME!
    */
   public static void copy(final Reader       input,
                           final OutputStream output,
                           final int          bufferSize)
                    throws IOException {
      final OutputStreamWriter out = new OutputStreamWriter(output);
      copy(input, out, bufferSize);

      // NOTE: Unless anyone is planning on rewriting OutputStreamWriter, we have to flush
      // here.
      out.flush();
   }


   ///////////////////////////////////////////////////////////////
   // Derived copy methods
   // String -> *
   ///////////////////////////////////////////////////////////////
   ///////////////////////////////////////////////////////////////
   // String -> OutputStream

   /**
    * Serialize chars from a <code>String</code> to bytes on an
    * <code>OutputStream</code>, and flush the <code>OutputStream</code>.
    *
    * @param input DOCUMENT ME!
    * @param output DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public static void copy(final String       input,
                           final OutputStream output) throws IOException {
      copy(input, output, DEFAULT_BUFFER_SIZE);
   }


   /**
    * Serialize chars from a <code>String</code> to bytes on an
    * <code>OutputStream</code>, and flush the <code>OutputStream</code>.
    *
    * @param input DOCUMENT ME!
    * @param output DOCUMENT ME!
    * @param bufferSize Size of internal buffer to use.
    *
    * @throws IOException DOCUMENT ME!
    */
   public static void copy(final String       input,
                           final OutputStream output,
                           final int          bufferSize)
                    throws IOException {
      final StringReader       in  = new StringReader(input);
      final OutputStreamWriter out = new OutputStreamWriter(output);
      copy(in, out, bufferSize);

      // NOTE: Unless anyone is planning on rewriting OutputStreamWriter, we have to flush
      // here.
      out.flush();
   }


   ///////////////////////////////////////////////////////////////
   // String -> Writer

   /**
    * Copy chars from a <code>String</code> to a <code>Writer</code>.
    *
    * @param input DOCUMENT ME!
    * @param output DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public static void copy(final String input,
                           final Writer output) throws IOException {
      output.write(input);
   }


   ///////////////////////////////////////////////////////////////
   // Derived copy methods
   // byte[] -> *
   ///////////////////////////////////////////////////////////////
   ///////////////////////////////////////////////////////////////
   // byte[] -> Writer

   /**
    * Copy and convert bytes from a <code>byte[]</code> to chars on a
    * <code>Writer</code>. The platform's default encoding is used for the
    * byte-to-char conversion.
    *
    * @param input DOCUMENT ME!
    * @param output DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public static void copy(final byte[] input,
                           final Writer output) throws IOException {
      copy(input, output, DEFAULT_BUFFER_SIZE);
   }


   /**
    * Copy and convert bytes from a <code>byte[]</code> to chars on a
    * <code>Writer</code>. The platform's default encoding is used for the
    * byte-to-char conversion.
    *
    * @param input DOCUMENT ME!
    * @param output DOCUMENT ME!
    * @param bufferSize Size of internal buffer to use.
    *
    * @throws IOException DOCUMENT ME!
    */
   public static void copy(final byte[] input,
                           final Writer output,
                           final int    bufferSize) throws IOException {
      final ByteArrayInputStream in = new ByteArrayInputStream(input);
      copy(in, output, bufferSize);
   }


   /**
    * Copy and convert bytes from a <code>byte[]</code> to chars on a
    * <code>Writer</code>, using the specified encoding.
    *
    * @param input DOCUMENT ME!
    * @param output DOCUMENT ME!
    * @param encoding The name of a supported character encoding. See the <a
    *        href="http://www.iana.org/assignments/character-sets">IANA
    *        Charset Registry</a> for a list of valid encoding types.
    *
    * @throws IOException DOCUMENT ME!
    */
   public static void copy(final byte[] input,
                           final Writer output,
                           final String encoding) throws IOException {
      final ByteArrayInputStream in = new ByteArrayInputStream(input);
      copy(in, output, encoding);
   }


   /**
    * Copy and convert bytes from a <code>byte[]</code> to chars on a
    * <code>Writer</code>, using the specified encoding.
    *
    * @param input DOCUMENT ME!
    * @param output DOCUMENT ME!
    * @param encoding The name of a supported character encoding. See the <a
    *        href="http://www.iana.org/assignments/character-sets">IANA
    *        Charset Registry</a> for a list of valid encoding types.
    * @param bufferSize Size of internal buffer to use.
    *
    * @throws IOException DOCUMENT ME!
    */
   public static void copy(final byte[] input,
                           final Writer output,
                           final String encoding,
                           final int    bufferSize) throws IOException {
      final ByteArrayInputStream in = new ByteArrayInputStream(input);
      copy(in, output, encoding, bufferSize);
   }


   ///////////////////////////////////////////////////////////////
   // byte[] -> OutputStream

   /**
    * Copy bytes from a <code>byte[]</code> to an <code>OutputStream</code>.
    *
    * @param input DOCUMENT ME!
    * @param output DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public static void copy(final byte[]       input,
                           final OutputStream output) throws IOException {
      copy(input, output, DEFAULT_BUFFER_SIZE);
   }


   /**
    * Copy bytes from a <code>byte[]</code> to an <code>OutputStream</code>.
    *
    * @param input DOCUMENT ME!
    * @param output DOCUMENT ME!
    * @param bufferSize Size of internal buffer to use.
    *
    * @throws IOException DOCUMENT ME!
    */
   public static void copy(final byte[]       input,
                           final OutputStream output,
                           final int          bufferSize)
                    throws IOException {
      output.write(input);
   }


   /**
    * Unconditionally close an <code>Reader</code>. Equivalent to {@link
    * Reader#close()}, except any exceptions will be ignored.
    *
    * @param input A (possibly null) Reader
    */
   public static void shutdownReader(final Reader input) {
      if (input == null) {
         return;
      }

      try {
         input.close();
      } catch (final IOException ioe) {
         ;
      }
   }


   /**
    * Unconditionally close an <code>OutputStream</code>. Equivalent to {@link
    * OutputStream#close()}, except any exceptions will be ignored.
    *
    * @param output A (possibly null) OutputStream
    */
   public static void shutdownStream(final OutputStream output) {
      if (output == null) {
         return;
      }

      try {
         output.close();
      } catch (final IOException ioe) {
         ;
      }
   }


   /**
    * Unconditionally close an <code>InputStream</code>. Equivalent to {@link
    * InputStream#close()}, except any exceptions will be ignored.
    *
    * @param input A (possibly null) InputStream
    */
   public static void shutdownStream(final InputStream input) {
      if (input == null) {
         return;
      }

      try {
         input.close();
      } catch (final IOException ioe) {
         ;
      }
   }


   /**
    * Unconditionally close an <code>Writer</code>. Equivalent to {@link
    * Writer#close()}, except any exceptions will be ignored.
    *
    * @param output A (possibly null) Writer
    */
   public static void shutdownWriter(final Writer output) {
      if (output == null) {
         return;
      }

      try {
         output.close();
      } catch (final IOException ioe) {
         ;
      }
   }


   ///////////////////////////////////////////////////////////////
   // InputStream -> byte[]

   /**
    * Get the contents of an <code>InputStream</code> as a <code>byte[]</code>.
    *
    * @param input DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public static byte[] toByteArray(final InputStream input)
                             throws IOException {
      return toByteArray(input, DEFAULT_BUFFER_SIZE);
   }


   /**
    * Get the contents of an <code>InputStream</code> as a <code>byte[]</code>.
    *
    * @param input DOCUMENT ME!
    * @param bufferSize Size of internal buffer to use.
    *
    * @return DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public static byte[] toByteArray(final InputStream input,
                                    final int         bufferSize)
                             throws IOException {
      final ByteArrayOutputStream output = new ByteArrayOutputStream();
      copy(input, output, bufferSize);

      return output.toByteArray();
   }


   ///////////////////////////////////////////////////////////////
   // Reader -> byte[]

   /**
    * Get the contents of a <code>Reader</code> as a <code>byte[]</code>.
    *
    * @param input DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public static byte[] toByteArray(final Reader input)
                             throws IOException {
      return toByteArray(input, DEFAULT_BUFFER_SIZE);
   }


   /**
    * Get the contents of a <code>Reader</code> as a <code>byte[]</code>.
    *
    * @param input DOCUMENT ME!
    * @param bufferSize Size of internal buffer to use.
    *
    * @return DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public static byte[] toByteArray(final Reader input,
                                    final int    bufferSize)
                             throws IOException {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      copy(input, output, bufferSize);

      return output.toByteArray();
   }


   ///////////////////////////////////////////////////////////////
   // String -> byte[]

   /**
    * Get the contents of a <code>String</code> as a <code>byte[]</code>.
    *
    * @param input DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public static byte[] toByteArray(final String input)
                             throws IOException {
      return toByteArray(input, DEFAULT_BUFFER_SIZE);
   }


   /**
    * Get the contents of a <code>String</code> as a <code>byte[]</code>.
    *
    * @param input DOCUMENT ME!
    * @param bufferSize Size of internal buffer to use.
    *
    * @return DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public static byte[] toByteArray(final String input,
                                    final int    bufferSize)
                             throws IOException {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      copy(input, output, bufferSize);

      return output.toByteArray();
   }


   ///////////////////////////////////////////////////////////////
   // InputStream -> String

   /**
    * Get the contents of an <code>InputStream</code> as a String. The
    * platform's default encoding is used for the byte-to-char conversion.
    *
    * @param input DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public static String toString(final InputStream input)
                          throws IOException {
      return toString(input, DEFAULT_BUFFER_SIZE);
   }


   /**
    * Get the contents of an <code>InputStream</code> as a String. The
    * platform's default encoding is used for the byte-to-char conversion.
    *
    * @param input DOCUMENT ME!
    * @param bufferSize Size of internal buffer to use.
    *
    * @return DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public static String toString(final InputStream input,
                                 final int         bufferSize)
                          throws IOException {
      final StringWriter sw = new StringWriter();
      copy(input, sw, bufferSize);

      return sw.toString();
   }


   /**
    * Get the contents of an <code>InputStream</code> as a String.
    *
    * @param input DOCUMENT ME!
    * @param encoding The name of a supported character encoding. See the <a
    *        href="http://www.iana.org/assignments/character-sets">IANA
    *        Charset Registry</a> for a list of valid encoding types.
    *
    * @return DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public static String toString(final InputStream input,
                                 final String      encoding)
                          throws IOException {
      return toString(input, encoding, DEFAULT_BUFFER_SIZE);
   }


   /**
    * Get the contents of an <code>InputStream</code> as a String.
    *
    * @param input DOCUMENT ME!
    * @param encoding The name of a supported character encoding. See the <a
    *        href="http://www.iana.org/assignments/character-sets">IANA
    *        Charset Registry</a> for a list of valid encoding types.
    * @param bufferSize Size of internal buffer to use.
    *
    * @return DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public static String toString(final InputStream input,
                                 final String      encoding,
                                 final int         bufferSize)
                          throws IOException {
      final StringWriter sw = new StringWriter();
      copy(input, sw, encoding, bufferSize);

      return sw.toString();
   }


   ///////////////////////////////////////////////////////////////
   // Reader -> String

   /**
    * Get the contents of a <code>Reader</code> as a String.
    *
    * @param input DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public static String toString(final Reader input) throws IOException {
      return toString(input, DEFAULT_BUFFER_SIZE);
   }


   /**
    * Get the contents of a <code>Reader</code> as a String.
    *
    * @param input DOCUMENT ME!
    * @param bufferSize Size of internal buffer to use.
    *
    * @return DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public static String toString(final Reader input,
                                 final int    bufferSize)
                          throws IOException {
      final StringWriter sw = new StringWriter();
      copy(input, sw, bufferSize);

      return sw.toString();
   }


   ///////////////////////////////////////////////////////////////
   // byte[] -> String

   /**
    * Get the contents of a <code>byte[]</code> as a String. The platform's
    * default encoding is used for the byte-to-char conversion.
    *
    * @param input DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public static String toString(final byte[] input) throws IOException {
      return toString(input, DEFAULT_BUFFER_SIZE);
   }


   /**
    * Get the contents of a <code>byte[]</code> as a String. The platform's
    * default encoding is used for the byte-to-char conversion.
    *
    * @param input DOCUMENT ME!
    * @param bufferSize Size of internal buffer to use.
    *
    * @return DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public static String toString(final byte[] input,
                                 final int    bufferSize)
                          throws IOException {
      final StringWriter sw = new StringWriter();
      copy(input, sw, bufferSize);

      return sw.toString();
   }


   /**
    * Get the contents of a <code>byte[]</code> as a String.
    *
    * @param input DOCUMENT ME!
    * @param encoding The name of a supported character encoding. See the <a
    *        href="http://www.iana.org/assignments/character-sets">IANA
    *        Charset Registry</a> for a list of valid encoding types.
    *
    * @return DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public static String toString(final byte[] input,
                                 final String encoding)
                          throws IOException {
      return toString(input, encoding, DEFAULT_BUFFER_SIZE);
   }


   /**
    * Get the contents of a <code>byte[]</code> as a String.
    *
    * @param input DOCUMENT ME!
    * @param encoding The name of a supported character encoding. See the <a
    *        href="http://www.iana.org/assignments/character-sets">IANA
    *        Charset Registry</a> for a list of valid encoding types.
    * @param bufferSize Size of internal buffer to use.
    *
    * @return DOCUMENT ME!
    *
    * @throws IOException DOCUMENT ME!
    */
   public static String toString(final byte[] input,
                                 final String encoding,
                                 final int    bufferSize)
                          throws IOException {
      final StringWriter sw = new StringWriter();
      copy(input, sw, encoding, bufferSize);

      return sw.toString();
   }
}
