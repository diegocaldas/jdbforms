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


import java.util.Locale;

/**
 * handling of internal messages
 *
 * @author Henner Kollmann
 */
public class MessageResourcesInternal
{
	/** DOCUMENT ME! */

    private static String RESOURCE =  "org.dbforms.resources.messages";

	protected static MessageResource msgRes = null;

	/*********************************************************************************************
	 *  Get the message from ResourceBundle.  If not present, return the defaultMsg at
	 *  the place of a null.  To avoid to doing this condition everywhere in the code ...
	 *
	 * @param  <code>String</code> : Message key to lookup.
	 * @param  <code>Locale</code> : Locale object to map message with good ResourceBundle.
	 * @param  <code>String</code> : String to return if the lookup message key is not found.
	 *
	* @return        <code>String</code> : Message resolve.
	 ********************************************************************************************/
	public static String getMessage(String msg, Locale locale, String defaultMsg)
	{
		String s = getMessage(msg, locale);
		if (Util.isNull(s)) 
			s = defaultMsg;
		return s;
	}


	/********************************************************************************************
	 *  Retrieve message from ResourceBundle.  If the ResourceBundle is not yet cached,
	 *  cache it and retreive message.
	 *
	 *         @param  <code>String</code> : Message key to lookup.
	 *         @param  <code>Locale</code> : Locale object to map message with good ResourceBundle.
	 *
	 *         @return        <code>String</code> : Message resolve, null if not found.
	 ********************************************************************************************/
	public static String getMessage(String msg, Locale loc)
	{
		init();
		return msgRes.getMessage(msg, loc);
	}


	/*********************************************************************************************
	 *  Retrieve message from ResourceBundle and replace parameter "{x}" with values in parms array.
	 *
	 *         @param  <code>String</code> : Message key to lookup.
	 *         @param  <code>Locale</code> : Locale object to map message with good ResourceBundle.
	 *         @param  <code>String[]</code> : Parameters to replace "{x}" in message .
	 *
	 *         @return        <code>String</code> : Message resolve with parameter replaced, null if message key not found.
	 ********************************************************************************************/
	public static String getMessage(String msg, Locale loc, String[] parms)
	{
		init();
		return msgRes.getMessage(msg, loc, parms);
	}
	
	private static void init()
	{
	   if (msgRes == null)
	      msgRes = new MessageResource(RESOURCE);
	}

}
