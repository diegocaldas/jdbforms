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

import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;

import java.io.Writer;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.BufferedWriter;

import org.apache.log4j.Category;



/**
 *  ReflectionUtil class
 *
 * @author  Luca Fossato
 * @created  20 novembre 2002
 */
public class ReflectionUtil
{
   /** log4j category */
   private static final Category logCat = Category.getInstance(ReflectionUtil.class);

   /**
    * Return the object having the input class name, instanced with the
    * constructor having the <code>constructorArgsTypes</code> arguments.
    *
    * @param className              the object class name
    * @param constructorArgsTypes   the object constructor arguments classes
    * @param constructorArgs        the object constructor arguments values
    * @return                       the instanced object
    * @exception Exception if any error occurs
    */
   public static Object newInstance(String className,
      Class[] constructorArgsTypes, Object[] constructorArgs)
      throws Exception
   {
      Class       myClass       = Class.forName(className);
      return newInstance(myClass, constructorArgsTypes, constructorArgs);
   }

	/**
	 * Return the object having the input class name, instanced with the
	 * constructor having the <code>constructorArgsTypes</code> arguments.
	 *
	 * @param className              the object class name
	 * @param constructorArgsTypes   the object constructor arguments classes
	 * @param constructorArgs        the object constructor arguments values
	 * @return                       the instanced object
	 * @exception Exception if any error occurs
	 */
	public static Object newInstance(Class clazz,
		Class[] constructorArgsTypes, Object[] constructorArgs)
		throws Exception
	{
		Constructor myConstructor = clazz.getConstructor(constructorArgsTypes);
		return myConstructor.newInstance(constructorArgs);
	}

   /**
    *  Invokes the underlying method represented by this Method object,
    *  on the specified object with the specified parameters.
    *  <br>
    *  Individual parameters are automatically unwrapped to match primitive
    *  formal parameters, and both primitive and reference parameters are subject
    *  to widening conversions as necessary.
    *  <br>
    *  The value returned by the underlying method is automatically wrapped
    *  in an object if it has a primitive type.
    *
    * @param className              the object class name
    * @param constructorArgsTypes   the arguments classes for the object method
    * @param constructorArgs        the arguments values for the object constructor
    * @return    If the method completes normally, the value it returns is returned
    *            to the caller of invoke; if the value has a primitive type,
    *            it is first appropriately wrapped in an object.
    *            If the underlying method return type is void, the invocation
    *            returns null.
    * @exception Exception if any error occurs
    */
   public static Object invoke(String className, String methodName,
      Class[] argsTypes, Object[] args) throws Exception
   {
      Class  c = Class.forName(className);
      Method m = c.getDeclaredMethod(methodName, argsTypes);
      Object i = c.newInstance();
      Object r = m.invoke(i, args);

      return r;
   }


   /**
    *  Get the String representation of the input object
    *
    * @param o  the object to introspect
    * @return   the String representation of the input object
    */
   public static String toString(Object o)
   {
      StringWriter   sw = new StringWriter();
      BufferedWriter bw = new BufferedWriter(new PrintWriter(sw));
      String         s  = null;

      reflectObject(o, bw);
      s = sw.getBuffer().toString();

      try
      {
         sw.close();
      }
      catch (Exception e)
      {
         logCat.error("::toString - cannot close the writer object", e);
      }

      return s;
   }


   /**
    *  Get the String representation of the class having
    *  the input full qualified name.
    *
    * @param c  the full qualified name of the class to introspect
    * @return   the String representation of the input object
    */
   public static String toString(String c)
   {
      StringWriter   sw = new StringWriter();
      BufferedWriter bw = new BufferedWriter(new PrintWriter(sw));
      String         s  = null;

      reflectClass(c, bw);
      s = sw.getBuffer().toString();

      try
      {
         sw.close();
      }
      catch (Exception e)
      {
         logCat.error("::toString - cannot close the writer object", e);
      }

      return s;
   }


   /**
    *  Reflect the input object state.
    *
    * @param  name Description of the Parameter
    * @param  w Description of the Parameter
    */
   public static void reflectObject(Object o, OutputStream os)
   {
      Writer w = new BufferedWriter(new OutputStreamWriter(os));
      reflectClass(o, true, w);
   }


   /**
    *  Reflect the input object state.
    *
    * @param  name Description of the Parameter
    * @param  w Description of the Parameter
    */
   public static void reflectObject(Object o, Writer w)
   {
      reflectClass(o, true, w);
   }


   /**
    *  Reflect the input class state.
    *
    * @param  name Description of the Parameter
    * @param  os Description of the Parameter
    */
   public static void reflectClass(String name, OutputStream os)
   {
      Writer w = new BufferedWriter(new OutputStreamWriter(os));
      reflectClass(name, w);
   }


   /**
    *  Reflect the input class state.
    *
    * @param  name Description of the Parameter
    * @param  w Description of the Parameter
    */
   public static void reflectClass(String name, Writer w)
   {
      Class c = null;

      try
      {
         c = Class.forName(name);
         reflectClass(c.newInstance(), false, w);
      }
      catch (Exception e)
      {
         logCat.error("Class " + name + " is not found.");

         return;
      }
   }


   /**
    *   PRIVATE methods here
    */
   /**
    *  Reflect the input class state.
    *
    * @param  name Description of the Parameter
    * @param  w Description of the Parameter
    */
   private static void reflectClass(Object o, boolean dumpValues, Writer w)
   {
      PrintWriter pw = new PrintWriter(w);
      Class       c = o.getClass();

      // Print Declaration
      pw.println(Modifier.toString(c.getModifiers()) + " " + c.getName());

      // Print Superclass
      if (c.getSuperclass() != null)
      {
         pw.print("  extends " + c.getSuperclass().getName());
      }

      // Print interfaces
      Class[] interfaces = c.getInterfaces();

      for (int i = 0; i < interfaces.length; i++)
      {
         if (i == 0)
         {
            pw.print(" implements ");
         }
         else
         {
            pw.print(", ");
         }

         pw.print(interfaces[i].getName());
      }

      pw.println("\n{");

      try
      {
         listClassVariables(pw, o, dumpValues);
      }
      catch (Exception e)
      {
         logCat.error("::reflectClass - cannot list the class variables", e);
      }

      listClassConstructors(pw, c);
      listClassMethods(pw, c);

      pw.println("\n}");
      pw.flush();
   }


   /**
    *  Gets the typeName attribute of the input class
    *
    * @param  c Description of the Parameter
    * @return  The typeName value
    */
   private static String getTypeName(Class c)
   {
      if (c.isArray())
      {
         try
         {
            Class cl         = c;
            int   dimensions = 0;

            while (cl.isArray())
            {
               dimensions++;
               cl = cl.getComponentType();
            }

            StringBuffer sb = new StringBuffer();

            sb.append(cl.getName());

            for (int i = 0; i < dimensions; i++)
               sb.append("[]");

            return sb.toString();
         }
         catch (Throwable e)
         {
            logCat.error("::getTypeName - cannot get the class type", e);
         }
      }

      return c.getName();
   }


   /**
    *  Get the class constructors.
    *
    * @param  pw Description of the Parameter
    * @param  c Description of the Parameter
    */
   private static void listClassConstructors(PrintWriter pw, Class c)
   {
      String        name         = c.getName();
      Constructor[] constructors = c.getDeclaredConstructors();

      for (int i = 0; i < constructors.length; i++)
      {
         if (i == 0)
         {
            pw.println("  // Constructors");
         }

         pw.print("  " + Modifier.toString(constructors[i].getModifiers())
            + " " + constructors[i].getName() + "(");

         listParameters(pw, constructors[i].getParameterTypes());
         pw.println(");");
      }

      if (constructors.length > 0)
      {
         pw.println();
      }
   }


   /**
    *  Get the class methods
    *
    * @param  pw Description of the Parameter
    * @param  c Description of the Parameter
    */
   private static void listClassMethods(PrintWriter pw, Class c)
   {
      String   name    = c.getName();
      Method[] methods = c.getDeclaredMethods();

      for (int i = 0; i < methods.length; i++)
      {
         if (i == 0)
         {
            pw.println("  // Methods");
         }

         pw.print("  " + Modifier.toString(methods[i].getModifiers()) + " "
            + getTypeName(methods[i].getReturnType()) + " "
            + methods[i].getName() + "(");

         listParameters(pw, methods[i].getParameterTypes());
         pw.println(");");
      }
   }


   /**
    *  Get the class variables
    *
    * @param  pw         Description of the Parameter
    * @param  o          Description of the Parameter
    * @param  dumpValues Description of the Parameter
    */
   private static void listClassVariables(PrintWriter pw, Object o,
      boolean dumpValues) throws Exception
   {
      Class   c      = o.getClass();
      String  name   = c.getName();
      Field[] fields = c.getDeclaredFields();

      for (int i = 0; i < fields.length; i++)
      {
         if (i == 0)
         {
            pw.println("  // Variables");
         }

         // Only take those that belong to this class
         pw.print("  " + Modifier.toString(fields[i].getModifiers()) + " "
            + getTypeName(fields[i].getType()) + " " + fields[i].getName()); //+ ";");

         // try to get the field value;
         if ((o != null) && dumpValues)
         {
            Field  f      = fields[i];
            String fValue = null;

            if (f.isAccessible())
            {
               fValue = f.get(o).toString();
            }
            else
            {
               try
               {
                  f.setAccessible(true);
                  fValue = f.get(o).toString();
                  f.setAccessible(false);
               }
               catch (Exception e)
               {
                  fValue = "NOT ACCESSIBLE";
               }
            }

            pw.print(" = [ " + fValue + " ]");
         }

         pw.println(";");
      }

      if (fields.length > 0)
      {
         pw.println();
      }
   }


   /**
    *  Get the type of the class parameters
    *
    * @param  pw Description of the Parameter
    * @param  parameters Description of the Parameter
    */
   private static void listParameters(PrintWriter pw, Class[] parameters)
   {
      for (int j = 0; j < parameters.length; j++)
      {
         pw.print(getTypeName(parameters[j]));

         if (j < (parameters.length - 1))
         {
            pw.print(", ");
         }
      }
   }
}
