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

package org.dbforms.conprovider;

import java.io.PrintWriter;
import org.apache.log4j.Category;
import org.apache.log4j.Priority;



/**
 * Creates a PrintWriter that logs all the print/println information
 * to the category used in the constructorù
 *
 * @author JD Evora
 */
public class Log4jPrintWriter extends PrintWriter
{
    Priority level;
    Category cat;
    StringBuffer text = new StringBuffer("");


    /**
     * Creates a new Log4jPrintWriter object.
     *
     * @param cat DOCUMENT ME!
     * @param level DOCUMENT ME!
     */
    public Log4jPrintWriter(org.apache.log4j.Category cat, org.apache.log4j.Priority level)
    {
        super(System.err); // PrintWriter doesn't have default constructor.
        this.level = level;
        this.cat = cat;
    }


    /**
     *  overrides all the print and println methods
     *  for 'print' it to the constructor's Category
     */
    public void close()
    {
        flush();
    }


    /**
     * DOCUMENT ME!
     */
    public void flush()
    {
        if (!text.toString().equals(""))
        {
            cat.log(level, text.toString());
            text.setLength(0);
        }
    }


    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    public void print(boolean b)
    {
        text.append(b);
    }


    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void print(char c)
    {
        text.append(c);
    }


    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    public void print(char[] s)
    {
        text.append(s);
    }


    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public void print(double d)
    {
        text.append(d);
    }


    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     */
    public void print(float f)
    {
        text.append(f);
    }


    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     */
    public void print(int i)
    {
        text.append(i);
    }


    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    public void print(long l)
    {
        text.append(l);
    }


    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     */
    public void print(Object obj)
    {
        text.append(obj);
    }


    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    public void print(String s)
    {
        text.append(s);
    }


    /**
     * DOCUMENT ME!
     */
    public void println()
    {
        if (!text.toString().equals(""))
        {
            cat.log(level, text.toString());
            text.setLength(0);
        }
    }


    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     */
    public void println(boolean x)
    {
        text.append(x);
        cat.log(level, text.toString());
        text.setLength(0);
    }


    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     */
    public void println(char x)
    {
        text.append(x);
        cat.log(level, text.toString());
        text.setLength(0);
    }


    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     */
    public void println(char[] x)
    {
        text.append(x);
        cat.log(level, text.toString());
        text.setLength(0);
    }


    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     */
    public void println(double x)
    {
        text.append(x);
        cat.log(level, text.toString());
        text.setLength(0);
    }


    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     */
    public void println(float x)
    {
        text.append(x);
        cat.log(level, text.toString());
        text.setLength(0);
    }


    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     */
    public void println(int x)
    {
        text.append(x);
        cat.log(level, text.toString());
        text.setLength(0);
    }


    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     */
    public void println(long x)
    {
        text.append(x);
        cat.log(level, text.toString());
        text.setLength(0);
    }


    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     */
    public void println(Object x)
    {
        text.append(x);
        cat.log(level, text.toString());
        text.setLength(0);
    }


    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     */
    public void println(String x)
    {
        text.append(x);
        cat.log(level, text.toString());
        text.setLength(0);
    }
}
