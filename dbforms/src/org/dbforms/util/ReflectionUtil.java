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

import java.lang.reflect.*;



/**
 *  ReflectionUtil class
 *
 * @author  Luca Fossato
 * @created  20 novembre 2002
 */
public class ReflectionUtil
{
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
    public static Object newInstance(String   className,
                                     Class[]  constructorArgsTypes,
                                     Object[] constructorArgs)
        throws Exception
    {
        Class myClass = Class.forName(className);
        Constructor myConstructor = myClass.getConstructor(constructorArgsTypes);

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
    public static Object invoke(String   className,
                                String   methodName,
                                Class[]  argsTypes,
                                Object[] args)
        throws Exception
    {
        Class c  = Class.forName(className);
        Method m = c.getDeclaredMethod(methodName, argsTypes);
        Object i = c.newInstance();
        Object r = m.invoke(i, args);

        return r;
    }
}
