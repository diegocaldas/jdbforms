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

package org.dbforms.taglib;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.JspTestCase;
import org.apache.cactus.WebResponse;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.DbFormsConfigRegistry;
import org.dbforms.servlets.ConfigServlet;
import org.dbforms.config.Table;
import org.dbforms.util.AssertUtils;




/**
 * Tests of the <code>DbFormTag</code> class.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 *
 */
public class TestDbFormTag extends JspTestCase
{
    private DbFormTag tag;
    private BodyContent tagContent;
    private Table tblAuthor = null;

    /**
     * Defines the testcase name for JUnit.
     *
     * @param theName the testcase's name.
     */
    public TestDbFormTag(String theName)
    {
        super(theName);
    }

    /**
     * Start the tests.
     *
     * @param theArgs the arguments. Not used
     */
    public static void main(String[] theArgs)
    {
        junit.swingui.TestRunner.main(new String[]
        {
            TestDbFormTag.class.getName()
        });
    }


    /**
     * @return a test suite (<code>TestSuite</code>) that includes all methods
     *         starting with "test"
     */
    public static Test suite()
    {
        // All methods starting with "test" will be executed in the test suite.
        return new TestSuite(TestDbFormTag.class);
    }


    /**
     * In addition to creating the tag instance and adding the pageContext to
     * it, this method creates a BodyContent object and passes it to the tag.
     */
    public void setUp() throws Exception
    {
        DbFormsConfigRegistry.instance().register(null);
        tblAuthor = null;
        config.setInitParameter("dbformsConfig", "/WEB-INF/dbforms-config.xml");
        config.setInitParameter("log4j.configuration", "/WEB-INF/log4j.properties");

        ConfigServlet configServlet = new ConfigServlet();
        configServlet.init(config);

        DbFormsConfig dbFormsConfig = (DbFormsConfig) configServlet.getServletContext().getAttribute(DbFormsConfig.CONFIG);

        this.tag = new DbFormTag();
        this.tag.setPageContext(this.pageContext);

        //create the BodyContent object and call the setter on the tag instance
        //this.tagContent = this.pageContext.pushBody();
        //this.tag.setBodyContent(this.tagContent);
    }


    //-------------------------------------------------------------------------

    /**
     * Sets the replacement target and replacement String on the tag, then calls
     * doAfterBody(). Most of the assertion work is done in endReplacement().
     */
    public void testMaxRows() throws Exception
    {
        this.tag.setMaxRows("1");
        assertEquals("1", this.tag.getMaxRows());
        assertTrue("Should be 1", this.tag.getCount() == 1);

        this.tag.setMaxRows("0");
        assertEquals("0", this.tag.getMaxRows());
        assertTrue("should be 0", this.tag.getCount() == 0);
    }


    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void testMaxRows2() throws Exception
    {
        this.tag.setMaxRows("*");
        assertEquals("*", this.tag.getMaxRows());
        assertTrue("should be 0", this.tag.getCount() == 0);
    }


    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void testSetUpTagNoTable() throws Exception
    {
        // mimic
        //<db:dbform redisplayFieldsOnError="true" autoUpdate="true" followUp="/AUTHOR_poweruser_list.jsp" maxRows="*">
        this.tag.setMaxRows("*");

        this.tag.setRedisplayFieldsOnError("true");
        this.tag.setAutoUpdate("true");

        int result = this.tag.doStartTag();
        assertEquals(BodyTag.EVAL_BODY_BUFFERED, result);
    }


    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void testSetUpTagForAuthor() throws Exception
    {
        // mimic
        //<db:dbform redisplayFieldsOnError="true" autoUpdate="true" followUp="/AUTHOR_poweruser_list.jsp" maxRows="*" tableName="AUTHOR">
        this.tag.setMaxRows("*");
        this.tag.setTableName("AUTHOR");
        tblAuthor = tag.getTable();
        assertTrue("Make sure we get table name back", tag.getTableName().equals("AUTHOR"));
        assertTrue("Make sure we get table with right name back", tag.getTable().getName().equals("AUTHOR"));

        this.tag.setRedisplayFieldsOnError("true");
        this.tag.setAutoUpdate("true");

        int result = this.tag.doStartTag();
        assertEquals(BodyTag.EVAL_BODY_BUFFERED, result);
    }


    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void testSetUpTagForBook() throws Exception
    {
        // mimic
        //<db:dbform redisplayFieldsOnError="true" autoUpdate="true" followUp="/BOOK_poweruser_list.jsp" maxRows="*" tableName="BOOK">
        this.tag.setMaxRows("*");
        this.tag.setTableName("BOOK");
        tblAuthor = tag.getTable();
        assertTrue("Make sure we get table name back", tag.getTableName().equals("BOOK"));
        assertTrue("Make sure we get table with right name back", tag.getTable().getName().equals("BOOK"));

        this.tag.setRedisplayFieldsOnError("true");
        this.tag.setAutoUpdate("true");

        int result = this.tag.doStartTag();
        assertEquals(BodyTag.EVAL_BODY_BUFFERED, result);
    }


    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void testSetUpTagForAuthorWithEverything() throws Exception
    {
        // mimic
        //<db:dbform redisplayFieldsOnError="true" autoUpdate="true" followUp="/AUTHOR_poweruser_list.jsp" maxRows="*" tableName="AUTHOR">
        this.tag.setMaxRows("*");
        this.tag.setTableName("AUTHOR");
        this.tag.setRedisplayFieldsOnError("true");
        this.tag.setAutoUpdate("true");
        this.tag.setTarget("_TOP");
        this.tag.setFollowUp("/AUTHOR_poweruser_list.jsp");
        this.tag.setOrderBy("NAME");
        this.tag.setReadOnly("false");

        int result = this.tag.doStartTag();
        assertEquals(BodyTag.EVAL_BODY_BUFFERED, result);
    }


    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void testEndTagForAuthor() throws Exception
    {
        // mimic
        //<db:dbform redisplayFieldsOnError="true" autoUpdate="true" followUp="/AUTHOR_poweruser_list.jsp" maxRows="*" tableName="AUTHOR">
        this.tag.setMaxRows("*");
        this.tag.setTableName("AUTHOR");
        tblAuthor = tag.getTable();
        assertTrue("Make sure we get table name back", tag.getTableName().equals("AUTHOR"));
        assertTrue("Make sure we get table with right name back", tag.getTable().getName().equals("AUTHOR"));

        this.tag.setRedisplayFieldsOnError("true");
        this.tag.setAutoUpdate("true");

        int result = this.tag.doEndTag();
        assertEquals(BodyTag.EVAL_PAGE, result);
    }


    /**
     * Verifies that the target text is in the tag's
     * body.
     */
    public void endSetUpTagNoTable(WebResponse theResponse)
    {
        String content = theResponse.getText();
        AssertUtils.assertContains("<form name=\"dbform\"",content);
        AssertUtils.assertContains("method=\"post\"",content);

    }


    /**
     * DOCUMENT ME!
     *
     * @param theResponse DOCUMENT ME!
     */
    public void endSetUpTagForAuthor(WebResponse theResponse)
    {
        /* expected content:
        <form name="dbform" action="/test/servlet/control;jsessionid=9056E6E9E4F59E13EF9DE8E371E2D5A3" method="post">
        <input type="hidden" name="invtable" value="0"><input type="hidden" name="autoupdate_0" value="true">
        <input type="hidden" name="fu_0" value="null"><input type="hidden" name="source" value="/test">
        <input type="hidden" name="customEvent">
        */
        String content = theResponse.getText();

        AssertUtils.assertContains("<form name=\"dbform\"",content);
        AssertUtils.assertContains("method=\"post\"",content);
        AssertUtils.assertContains("<input type=\"hidden\" name=\"invtable\" value=\"0\">",content);
        AssertUtils.assertContains("<input type=\"hidden\" name=\"autoupdate_0\" value=\"true\">",content);
        AssertUtils.assertContains("<input type=\"hidden\" name=\"fu_0\" value=\"null\">",content);
    }


    /**
     * DOCUMENT ME!
     *
     * @param theResponse DOCUMENT ME!
     */
    public void endSetUpTagForBook(WebResponse theResponse)
    {
        /* expected content:
        <form name="dbform" action="/test/servlet/control;jsessionid=9056E6E9E4F59E13EF9DE8E371E2D5A3" method="post">
        <input type="hidden" name="invtable" value="1"><input type="hidden" name="autoupdate_1" value="true">
        <input type="hidden" name="fu_1" value="null"><input type="hidden" name="source" value="/test">
        <input type="hidden" name="customEvent">
        */
        String content = theResponse.getText();
        AssertUtils.assertContains("<form name=\"dbform\"",content);
        AssertUtils.assertContains("method=\"post\"",content);
        AssertUtils.assertContains("<input type=\"hidden\" name=\"invtable\" value=\"1\">",content);
        AssertUtils.assertContains("<input type=\"hidden\" name=\"autoupdate_1\" value=\"true\">",content);
        AssertUtils.assertContains("<input type=\"hidden\" name=\"fu_1\" value=\"null\">",content);

    }


    /**
     * DOCUMENT ME!
     *
     * @param theResponse DOCUMENT ME!
     */
    public void endSetUpTagForAuthorWithEverything(WebResponse theResponse)
    {
        String content = theResponse.getText();

        AssertUtils.assertContains("name=\"dbform\"",content);
//        AssertUtils.assertContains("action=\"test/servlet/control",content);
        AssertUtils.assertContains("target=\"_TOP\"",content);
        AssertUtils.assertContains("method=\"post\"",content);
        AssertUtils.assertContains("type=\"hidden\"",content);
        AssertUtils.assertContains("name=\"invtable\"",content);
        AssertUtils.assertContains("value=\"0\"",content);
        AssertUtils.assertContains("name=\"autoupdate_0\" value=\"true\"",content);
        AssertUtils.assertContains("<input type=\"hidden\" name=\"fu_0\" value=\"/AUTHOR_poweruser_list.jsp\">",content);
        AssertUtils.assertContains("<input type=\"hidden\" name=\"customEvent\">",content);

    }


    /**
     * Verifies that the target text is in the tag's
     * body.
     */
    public void endEndTagForAuthor(WebResponse theResponse)
    {
        String content = theResponse.getText();
        String expectedContentA = "</form>";

        assertTrue("Make sure content matches expected content:" + expectedContentA, content.indexOf(expectedContentA) >= 0);
    }


    /**
     * DOCUMENT ME!
     */
    public void tearDown()
    {
        //necessary for tag to output anything on most servlet engines.
//        this.pageContext.popBody();
    }


}