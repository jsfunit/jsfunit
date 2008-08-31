/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.cactus.ServletTestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.jboss.jsfunit.richclient.RichFacesClient;
import org.richfaces.component.html.HtmlTree;
import org.richfaces.demo.tree.Album;
import org.richfaces.demo.tree.Artist;
import org.richfaces.demo.tree.Song;
import org.richfaces.model.TreeNode;
import org.w3c.dom.Element;

import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlImage;

public class RichTreeTest extends ServletTestCase {
	// -- Logger
	//protected static Log log = LogFactory.getLog(RichTreeTest.class);

	public static Test suite()
	{
		return new TestSuite( RichTreeTest.class );
	}

	private JSFSession jsfSession;
	private JSFClientSession client;
	private RichFacesClient richClient;
	private JSFServerSession server;

	public void setUp() throws IOException
	{
		this.jsfSession = new JSFSession("/richfaces/tree.jsf");
		this.client = jsfSession.getJSFClientSession();
		this.richClient = new RichFacesClient(client);
		this.server = jsfSession.getJSFServerSession();
	}
	
	private static final String _AJAX_TREE_NAME = "ajaxTree";
	private static final String _SERVER_TREE_NAME = "serverTree";
		
	@SuppressWarnings("unchecked")
	private String getNodeTemplateId( String root, TreeNode node ) {
		String nodeKey = "";
		if( node instanceof Artist ) { nodeKey = "Artist"; }
		if( node instanceof Album ) { nodeKey = "Album"; }
		if( node instanceof Song ) { nodeKey = "Song"; }
		return root + "Tree" + nodeKey + "Node";
	}
	
	@SuppressWarnings("unchecked")
	private void checkTreeNode( String type, String key, TreeNode node ) throws IOException, InterruptedException {
		final String nodeID = ":"+key+"::"+getNodeTemplateId(type,node);
		//log.info(type+" : "+nodeID);
				
		// Check to see if this is a leaf
		Element e = client.getElement(nodeID+":handle:img");
		if( e == null ) {
			// Make sure we are currently closed
			HtmlImage collapsed = (HtmlImage)client.getElement(nodeID+":handle:img:collapsed");
			assertNotNull("Can't find node 'collapsed' handle ["+nodeID+":handle:img:collapsed"+"]",collapsed);
			HtmlImage expanded = (HtmlImage)client.getElement(nodeID+":handle:img:expanded");
			assertNotNull("Can't find node 'expanded' handle ["+nodeID+":handle:img:expanded"+"]",expanded);
			assertFalse("",collapsed.getStyleAttribute().contains("none"));
			assertTrue("",expanded.getStyleAttribute().contains("none"));
			
			// Click on the current node
			richClient.clickTreeNodeHandle( key, getNodeTemplateId(type,node) );
			
			// Make sure we are currently open
			collapsed = (HtmlImage)client.getElement(nodeID+":handle:img:collapsed");
			assertNotNull("Can't find node 'collapsed' handle ["+nodeID+":handle:img:collapsed"+"]",collapsed);
			expanded = (HtmlImage)client.getElement(nodeID+":handle:img:expanded");
			assertNotNull("Can't find node 'expanded' handle ["+nodeID+":handle:img:expanded"+"]",expanded);
			assertFalse("",expanded.getStyleAttribute().contains("none"));
			assertTrue("",collapsed.getStyleAttribute().contains("none"));
						
			// Check the children for their nodes
			Iterator i = node.getChildren();
			while( i.hasNext() ) {
				Entry entry = (Entry) i.next();
				TreeNode child = (TreeNode)entry.getValue();
				checkTreeNode( type, (String)entry.getKey(), child );
			}
			
			//Click on the current node to close
			richClient.clickTreeNodeHandle( key, getNodeTemplateId(type, node) );

			// Make sure we are currently closed
			collapsed = (HtmlImage)client.getElement(nodeID+":handle:img:collapsed");
			assertNotNull("Can't find node 'collapsed' handle ["+nodeID+":handle:img:collapsed"+"]",collapsed);
			expanded = (HtmlImage)client.getElement(nodeID+":handle:img:expanded");
			assertNotNull("Can't find node 'expanded' handle ["+nodeID+":handle:img:expanded"+"]",expanded);
			assertFalse("",collapsed.getStyleAttribute().contains("none"));
			assertTrue("",expanded.getStyleAttribute().contains("none"));
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void testRichTree_AjaxSwitchType() throws IOException, InterruptedException
	{
		HtmlTree ajaxTree = (HtmlTree)server.findComponent(_AJAX_TREE_NAME);
		assertNotNull("Unable to find ajax tree component ["+_AJAX_TREE_NAME+"]",ajaxTree);

		HtmlDivision treeElement = (HtmlDivision)client.getElement(_AJAX_TREE_NAME);
		assertNotNull("Unable to find tree root DIV",treeElement);
		
		TreeNode data = (TreeNode)ajaxTree.getValue();
		assertNotNull("Unable to query for tree data",data);

		Iterator i = data.getChildren();
		while( i.hasNext() ) {
			Entry entry = (Entry) i.next();
			TreeNode child = (TreeNode)entry.getValue();
			checkTreeNode( "ajax", (String)entry.getKey(), child );			
		}
		
		treeElement = (HtmlDivision)client.getElement(_AJAX_TREE_NAME);
		assertNotNull("Unable to find tree root DIV",treeElement);
		//log.info(treeElement.asXml());
	}
	
	@SuppressWarnings("unchecked")
	public void testRichTree_ServerSwitchType() throws IOException, InterruptedException
	{
		HtmlTree serverTree = (HtmlTree)server.findComponent(_SERVER_TREE_NAME);
		assertNotNull("Unable to find ajax tree component ["+_SERVER_TREE_NAME+"]",serverTree);

		HtmlDivision treeElement = (HtmlDivision)client.getElement(_SERVER_TREE_NAME);
		assertNotNull("Unable to find tree root DIV",treeElement);
		
		TreeNode data = (TreeNode)serverTree.getValue();
		assertNotNull("Unable to query for tree data",data);
		
		Iterator i = data.getChildren();
		while( i.hasNext() ) {
			Entry entry = (Entry) i.next();
			TreeNode child = (TreeNode)entry.getValue();
			checkTreeNode( "server", (String)entry.getKey(), child );			
		}
		
		treeElement = (HtmlDivision)client.getElement(_SERVER_TREE_NAME);
		assertNotNull("Unable to find tree root DIV",treeElement);
		//log.info(treeElement.asXml());
	}
}
