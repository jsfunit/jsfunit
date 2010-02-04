/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.jsfunit.test.richfaces;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import java.io.IOException;
import javax.faces.context.ExternalContext;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.jboss.jsfunit.richclient.RichFacesClient;


/**
 * Test methods for manipulating RichTree
 *
 * @author Stan Silvert
 */
public class RichTreeTest extends ServletTestCase
{
    private JSFSession jsfSession;
    private JSFClientSession client;
    private RichFacesClient richClient;
    private JSFServerSession server;

    public static Test suite()
    {
        return new TestSuite(RichTreeTest.class);
    }

    @Override
    public void setUp() throws IOException
    {
        this.jsfSession = JSFSessionFactory.makeSession("/richfaces/tree.jsf");
        this.client = jsfSession.getJSFClientSession();
        this.richClient = new RichFacesClient(client);
        this.server = jsfSession.getJSFServerSession();
    }

    public void testRichTreeAjaxSwitchType() throws IOException
    {
        testRichTree("ajax");
    }
    
    public void testRichTreeClientSwitchType() throws IOException
    {
        testRichTree("client");
    }

    public void testRichTreeServerSwitchType() throws IOException
    {
        testRichTree("server");
    }

    private void testRichTree(String switchType) throws IOException
    {
        String treeId = switchType + "Tree";
        String artistNode = switchType + "TreeArtistNode";
        String albumNode = switchType + "TreeAlbumNode";
        String songNode = switchType + "TreeSongNode";
        String nodeValueParamName = switchType + "TreeNodeValue";

        assertFalse(richClient.isTreeHandleExpanded(treeId, artistNode, "Baccara"));
        HtmlElement handle = richClient.getTreeHandle(treeId, artistNode, "Baccara");
        handle.click();
        assertTrue(richClient.isTreeHandleExpanded(treeId, artistNode, "Baccara"));

        HtmlElement element = richClient.getTreeNodeByText(treeId, "albumLink", "Grand Collection");
        element.click();
        assertEquals("Grand Collection", getRequestParam(nodeValueParamName));

        handle = richClient.getTreeHandle(treeId, albumNode, "Grand Collection");
        handle.click(); // open handle
        assertTrue(richClient.isTreeHandleExpanded(treeId, albumNode, "Grand Collection"));
        assertFalse(richClient.isTreeHandleExpanded(treeId, albumNode, "The Road To Hell"));

        element = richClient.getTreeNodeByText(treeId, "songLink", "Borriquito");
        element.click();
        assertEquals("Borriquito", getRequestParam(nodeValueParamName));

        // close Grand Collection handle - need to get it again
        handle = richClient.getTreeHandle(treeId, albumNode, "Grand Collection");
        handle.click();
        assertFalse(richClient.isTreeHandleExpanded(treeId, albumNode, "Grand Collection"));

        element = richClient.getTreeNodeByText(treeId, "artistLink", "Chris Rea");
        element.click();
        assertEquals("Chris Rea", getRequestParam(nodeValueParamName));

        assertFalse(richClient.isTreeHandleExpanded(treeId, artistNode, "Chris Rea"));
        handle = richClient.getTreeHandle(treeId, artistNode, "Chris Rea");
        handle.click();
        assertTrue(richClient.isTreeHandleExpanded(treeId, artistNode, "Chris Rea"));
        assertFalse(richClient.isTreeHandleExpanded(treeId, albumNode, "The Road To Hell"));

        // JSFUNIT-238 if you remove the following three lines then the next assert will fail
        element = richClient.getTreeNodeByText(treeId, "albumLink", "The Road To Hell");
        element.click();
        assertEquals("The Road To Hell", getRequestParam(nodeValueParamName));

        handle = richClient.getTreeHandle(treeId, albumNode, "The Road To Hell");
        handle.click(); // open handle

        handle = richClient.getTreeHandle(treeId, albumNode, "The Road To Hell");
        assertTrue(richClient.isTreeHandleExpanded(treeId, albumNode, "The Road To Hell"));
        assertFalse(richClient.isTreeHandleExpanded(treeId, albumNode, "Grand Collection"));

        element = richClient.getTreeNodeByText(treeId, "songLink", "Texas");
        element.click();
        assertEquals("Texas", getRequestParam(nodeValueParamName));

        // close The Road to Hell handle - need to get it again
        handle = richClient.getTreeHandle(treeId, albumNode, "The Road To Hell");
        handle.click();
        assertFalse(richClient.isTreeHandleExpanded(treeId, albumNode, "The Road To Hell"));
    }

    private String getRequestParam(String paramName)
    {
        ExternalContext extCtx = server.getFacesContext().getExternalContext();
        return (String)extCtx.getRequestParameterMap().get(paramName);
    }

}
