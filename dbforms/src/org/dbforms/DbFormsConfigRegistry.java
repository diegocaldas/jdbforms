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

package org.dbforms;

import java.util.HashMap;



/**
 *  Registry for DbFormsConfig classes
 *
 * @author  Luca Fossato
 * @created  2 Dicember 2002
 */
public class DbFormsConfigRegistry
{
    /** default config name */
    private static final String DEFAULT_NAME = "default";

    /** map of DbFormsConfig objects */
    private static HashMap map = new HashMap();

    /** unique instance for this class */
    private static DbFormsConfigRegistry instance = null;


    /**
     *  Protected constructor
     */
    protected DbFormsConfigRegistry()
    {
    }


    /**
     *  Get the instance of DatabaseEventFactory class.
     *
     * @return  the instance of DatabaseEventFactory class
     */
    public static synchronized DbFormsConfigRegistry instance()
    {
        if (instance == null)
        {
            instance = new DbFormsConfigRegistry();
        }

        return instance;
    }


    /**
     *  Register the input DbFormsConfig object into the registry
     *  as the default config object.
     *
     * @param config the DbFormsConfig object
     */
    public void register(DbFormsConfig config)
    {
        register(DEFAULT_NAME, config);
    }


    /**
     *  Register a DbFormsConfig object into the registry
     *
     * @param name   the DbFormsConfig name used as the registry key
     * @param config the DbFormsConfig object
     */
    public void register(String name, DbFormsConfig config)
    {
        map.put(name, config);
    }


    /**
     *  Look up the default DbFormsConfig object stored into the registry.
     *
     * @exception Exception if the lookup operation fails
     */
    public DbFormsConfig lookup() throws Exception
    {
        return lookup(DEFAULT_NAME);
    }


    /**
     *  Look up a DbFormsConfig object stored into the registry.
     *
     * @param name the DbFormsConfig name previously used to store the
     *             config object into the registry.
     * @exception Exception if the lookup operation fails
     */
    public DbFormsConfig lookup(String name) throws Exception
    {
        DbFormsConfig config = null;

        if (map.containsKey(name))
        {
            config = (DbFormsConfig) map.get(name);
        }
        else
        {
            throw new Exception("cannot lookup a config object with the name [" + name + "]");
        }

        return config;
    }
}
