/*
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

import java.util.*;
import org.dbforms.util.*;

import org.apache.log4j.Category;

/************************************************************************
 *
 * This class represents an error tag in dbforms-errors.xml
 *
 ************************************************************************/

public class Error {

	static Category logCat = Category.getInstance(Error.class.getName());
	// logging category for this class

	//------------------------ Properties ---------------------------------------------------------

	private String id; 				// the id of this error
	private String type; 			// the type of error
	private Hashtable messages; 	// the error messages, as provided in xml-error file Key=Locale value=Message


	public Error()
	{
		messages = new Hashtable();
	}



	//------------------------ property access methods --------------------------------------------

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setMessages(Hashtable msgs) {
		this.messages = msgs;
	}

	public Hashtable getMessages() {
		return messages;
	}
	
	public void setMessage(String language, String message) {
		this.messages.put(language,message);
	}

	public String getMessage(String language) {
		return (String) messages.get(language);
	}
	
	
	public void setType(String t) {
		this.type = t;
	}

	public String getType() {
		return type;
	}

	// --------------------- utility methods -------------------------------------------------------

	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("id=");
		buf.append(this.getId());
		buf.append(" type=");
		buf.append(this.getType());
		return buf.toString();
	}
	
	
		/**
	 * adds a Message-Object to this error
	 * and puts it into the datastructure for further references
	 * (this method gets called from DbFormsError)
	 */
	public void addMessage(Message message) {

		messages.put(message.getLanguage(), message.getMessage());
		logCat.info("Language " + message.getLanguage() + " Message " + message.getMessage());

	}
	
}