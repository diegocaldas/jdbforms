/*
 * Created on 03.08.2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.dbforms.util;

import junit.framework.TestCase;
import java.util.Locale;

/**
 * @author hkk
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TestMessageResourcesInternal extends TestCase
{

	public void testMessageResourceInternal() throws Exception
	{
		MessageResourcesInternal.setSubClass("org.dbforms.resources.messages");
		assertTrue(MessageResourcesInternal.getMessage("dbforms.testmessage", Locale.ENGLISH).equals("this is a test"));
		assertTrue(MessageResourcesInternal.getMessage("dbforms.testmessage", Locale.GERMAN).equals("dies ist ein test"));
	}

}
