/*
 * Created on 03.08.2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.dbforms.util;

import java.util.Locale;

/**
 * @author hkk
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TestMessageResourcesInternal extends AbstractTestCase
{

	public void testMessageResourceInternal() throws Exception
	{
      MessageResources.setSubClass("resources");
      String s = MessageResourcesInternal.getMessage("dbforms.testmessage", Locale.ENGLISH);
		assertTrue(s.equals("this is a test"));
		s = MessageResourcesInternal.getMessage("dbforms.testmessage", Locale.GERMAN);
		assertTrue(s.equals("dies ist ein test"));
	}

}
