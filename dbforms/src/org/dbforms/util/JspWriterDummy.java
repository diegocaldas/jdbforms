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

public class JspWriterDummy extends JspWriter {

	protected JspWriterDummy(
			int bufferSize, 
			boolean autoFlush, 
			ServletResponse response) {
		super(bufferSize, autoFlush);
	}
	
	
	public void write(int c) throws IOException {
	}
	
	public void write(char cbuf[], int off, int len) throws IOException {
	}
	
	public void write(
			String str,
			int off,
			int len) throws IOException {
	}

	public void clear() throws IOException {
	}

	public void clearBuffer() throws IOException {
	}

	public void close() throws IOException {
	}
	
	public void flush() throws IOException {
	}
	
	public int getRemaining() {
		return 0; 
	}

	public void newLine() throws IOException {
	}

	public void print(boolean b) throws IOException {
	}

	public void print(char c) throws IOException {
	}

    public void print(char s[]) throws IOException {
	}

	public void print(int i) throws IOException {
	}
	
	public void print(long l) throws IOException {
	}

	public void print(float f) throws IOException {
	}
	
	public void print(double d) throws IOException {
	}

	public void print(String s) throws IOException {
	}

	public void print(Object obj) throws IOException {
	}
	
	public void println() throws IOException {
	}
	
	public void println(boolean x) throws IOException {
	}
	
	public void println(char x) throws IOException {
	}
	
	public void println(int x) throws IOException {
	}
	
	public void println(long x) throws IOException {
	}
	
	public void println(float x) throws IOException {
	}
	
	public void println(double x) throws IOException {
	}
	
	public void println(char x[]) throws IOException {
	}
	
	public void println(String x) throws IOException {
	}
	
	public void println(Object x) throws IOException {
	}

	
}
