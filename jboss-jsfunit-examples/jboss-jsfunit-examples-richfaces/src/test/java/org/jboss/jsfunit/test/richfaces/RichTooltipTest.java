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

import javax.faces.component.UIComponent;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.cactus.ServletTestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.jboss.jsfunit.richclient.RichFacesClient;
import org.richfaces.component.html.HtmlToolTip;
import org.richfaces.demo.tooltip.ToolTipData;
import org.w3c.dom.Element;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;

public class RichTooltipTest extends ServletTestCase {
	// -- Logger
	protected static Log log = LogFactory.getLog(RichTooltipTest.class);

	public static Test suite()
	{
		return new TestSuite( RichTooltipTest.class );
	}

	private static final String _DEFAULT_CLIENTSIDE_TOOLTIP = "tooltip1";	
	private static final String _FOLLOWMOUSE_TOOLTIP = "tooltip2";	
	private static final String _SERVERREDNER_TOOLTIP = "form1:tooltip3";	
	private static final String _MOUSECLICK_TOOLTIP = "form2:tooltip4";	
	
	private static final String _DEFAULT_CLIENTSIDE_PANEL = "sample1";
	private static final String _FOLLOWMOUSE_PANEL = "sample2";	
	private static final String _SERVERREDNER_PANEL = "sample3";
	private static final String _MOUSECLICK_PANEL = "sample4";
	
	// JSFUnit support objects
	private JSFSession jsfSession;
	private JSFClientSession client;
	private RichFacesClient ajaxClient;
	private JSFServerSession server;

	public void setUp() throws IOException
	{
		this.jsfSession = new JSFSession("/richfaces/toolTip.jsf");
		this.client = jsfSession.getJSFClientSession();
		this.ajaxClient = new RichFacesClient(client);
		this.server = jsfSession.getJSFServerSession();
	}
        
        public void tearDown() throws Exception
        {
           this.jsfSession = null;
           this.client = null;
           this.ajaxClient = null;
           this.server = null;
        }

	public void testTooltip_DefaultClientSide()
	{
		// Make sure we can lookup server component by name
		HtmlToolTip tt = (HtmlToolTip)server.findComponent(_DEFAULT_CLIENTSIDE_TOOLTIP);
		assertNotNull("Unable to find server-side HtmlToolTip component",tt);
		
		// 1) Find client-side tooltip span
		HtmlSpan tooltip1 = (HtmlSpan)((HtmlPage)client.getContentPage()).getElementById(_DEFAULT_CLIENTSIDE_TOOLTIP);	// Required because name collision
		assertNotNull("Unable to find client-side span named \""+_DEFAULT_CLIENTSIDE_TOOLTIP+"\"",tooltip1);				
		// 2) Find Richpanel div to perform mouse-over (parent element)
		HtmlDivision parentDiv = (HtmlDivision)client.getElement(_DEFAULT_CLIENTSIDE_PANEL);
		assertNotNull("Unable to find \"sample1\" control",parentDiv);
		// 3) Find Richpanel internal div (also needed)
		HtmlDivision parentSubDiv = (HtmlDivision)client.getElement(_DEFAULT_CLIENTSIDE_PANEL+"_body");
		assertNotNull("Unable to find \"sample1_body\" control",parentSubDiv);
		// 4) Make sure tooltip is currently hidden (style contains 'display: none')
		String hiddenStyle = tooltip1.getStyleAttribute();
		assertTrue("Tooltip not hidden as expected: ["+hiddenStyle+"]",
				hiddenStyle.contains("display:none") || hiddenStyle.contains("display: none"));
		// 5) Move mouse over div to make tooltip display
		// 	Yes, all three of these are required.
		parentDiv.mouseOver();
		parentDiv.mouseMove();
		parentSubDiv.mouseOver();

		// 6) Make sure tooltip is currently visible (style does not contain 'display: none')
		String visibleStyle = tooltip1.getStyleAttribute();
		assertFalse("Tooltip not visible as expected: ["+visibleStyle+"]",
				visibleStyle.contains("display:none") || visibleStyle.contains("display: none"));		
	}
	
	public void testTooltip_FollowMouse() throws InterruptedException
	{
		// Make sure we can lookup server component by name
		HtmlToolTip tt = (HtmlToolTip)server.findComponent(_FOLLOWMOUSE_TOOLTIP);
		assertNotNull("Unable to find server-side HtmlToolTip component",tt);
		
		// 1) Find client-side tooltip span
		HtmlSpan tooltip1 = (HtmlSpan)((HtmlPage)client.getContentPage()).getElementById(_FOLLOWMOUSE_TOOLTIP);	// Required because name collision
		assertNotNull("Unable to find client-side span named \""+_FOLLOWMOUSE_TOOLTIP+"\"",tooltip1);				
		// 2) Find Richpanel div to perform mouse-over (parent element)
		HtmlDivision parentDiv = (HtmlDivision)client.getElement(_FOLLOWMOUSE_PANEL);
		assertNotNull("Unable to find \"sample1\" control",parentDiv);
		// 3) Find Richpanel internal div (also needed)
		HtmlDivision parentSubDiv = (HtmlDivision)client.getElement(_FOLLOWMOUSE_PANEL+"_body");
		assertNotNull("Unable to find \"sample1_body\" control",parentSubDiv);
		// 4) Make sure tooltip is currently hidden (style contains 'display: none')
		String hiddenStyle = tooltip1.getStyleAttribute();
		assertTrue("Tooltip not hidden as expected: ["+hiddenStyle+"]",
				hiddenStyle.contains("display:none") || hiddenStyle.contains("display: none"));
		// 5) Move mouse over div to make tooltip display
		// 	Yes, all three of these are required.
		parentDiv.mouseOver();
		parentDiv.mouseMove();
		parentSubDiv.mouseOver();

		// 0.5 delay on tooltip - so wait a sec
		Thread.sleep(1000);
		
		// 6) Make sure tooltip is currently visible (style does not contain 'display: none')
		String visibleStyle = tooltip1.getStyleAttribute();
		assertFalse("Tooltip not visible as expected: ["+visibleStyle+"]",
				visibleStyle.contains("display:none") || visibleStyle.contains("display: none"));		
	}
	
	public void testTooltip_ServerRender() throws InterruptedException
	{
		// Make sure we can lookup server component by name
		HtmlToolTip tt = (HtmlToolTip)server.findComponent(_SERVERREDNER_TOOLTIP);
		assertNotNull("Unable to find server-side HtmlToolTip component",tt);

		// 1) Server-side rendered tooltip seems to be a child div 
		HtmlDivision tooltip1 = (HtmlDivision)((HtmlPage)client.getContentPage()).getElementById(_SERVERREDNER_TOOLTIP);
		assertNotNull("Unable to find client-side div named \""+_SERVERREDNER_TOOLTIP+"\"",tooltip1);

		// 2) Find Richpanel div to perform mouse-over (parent element)
		HtmlDivision parentDiv = (HtmlDivision)client.getElement(_SERVERREDNER_PANEL);
		assertNotNull("Unable to find \"sample1\" control",parentDiv);
		// 3) Find Richpanel internal div (also needed)
		HtmlDivision parentSubDiv = (HtmlDivision)client.getElement(_SERVERREDNER_PANEL+"_body");
		assertNotNull("Unable to find \"sample1_body\" control",parentSubDiv);
		// 4) Make sure tooltip is currently hidden (style contains 'display: none')
		String hiddenStyle = tooltip1.getStyleAttribute();
		assertTrue("Tooltip not hidden as expected: ["+hiddenStyle+"]",
				hiddenStyle.contains("display:none") || hiddenStyle.contains("display: none"));
		// 5) Lookup backing bean and current tooltip counter
		ToolTipData toolTipData = (ToolTipData)server.getManagedBeanValue("#{toolTipData}");
		assertNotNull("Couldn't find ToolTipData managed bean",toolTipData);
		int startVal = toolTipData.getTooltipCounterWithoutMod();
		//6) Move mouse over div to make tooltip display
		// 	Yes, all three of these are required.
		parentDiv.mouseOver();
		parentDiv.mouseMove();
		parentSubDiv.mouseOver();

		// 0.5 delay on tooltip - so wait a sec
		Thread.sleep(1000);

		// 7) Make sure tooltip is currently visible (style does not contain 'display: none')
		String visibleStyle = tooltip1.getStyleAttribute();
		assertFalse("Tooltip not visible as expected: ["+visibleStyle+"]",
				visibleStyle.contains("display:none") || visibleStyle.contains("display: none"));				
		// 8) Our tooltip counter should have increased by 1
		toolTipData = (ToolTipData)server.getManagedBeanValue("#{toolTipData}");
		int endVal = toolTipData.getTooltipCounterWithoutMod();
		assertEquals("toolTipCounter did not increment as expected",startVal+1,endVal);
	}
	
	public void testTooltip_MouseClick() throws IOException, InterruptedException
	{
		// Make sure we can lookup server component by name
		HtmlToolTip tt = (HtmlToolTip)server.findComponent(_MOUSECLICK_TOOLTIP);
		assertNotNull("Unable to find server-side HtmlToolTip component",tt);

		// 1) Server-side rendered tooltip seems to be a child div 
		HtmlDivision tooltip1 = (HtmlDivision)((HtmlPage)client.getContentPage()).getElementById(_MOUSECLICK_TOOLTIP);
		assertNotNull("Unable to find client-side div named \""+_MOUSECLICK_TOOLTIP+"\"",tooltip1);
		// 2) Find Richpanel div to perform mouse-over (parent element)
		HtmlDivision parentDiv = (HtmlDivision)client.getElement(_MOUSECLICK_PANEL);
		assertNotNull("Unable to find \"sample1\" control",parentDiv);
		// 3) Find Richpanel internal div (also needed)
		HtmlDivision parentSubDiv = (HtmlDivision)client.getElement(_MOUSECLICK_PANEL+"_body");
		assertNotNull("Unable to find \"sample1_body\" control",parentSubDiv);
		// 4) Make sure tooltip is currently hidden (style contains 'display: none')
		String hiddenStyle = tooltip1.getStyleAttribute();
		assertTrue("Tooltip not hidden as expected: ["+hiddenStyle+"]",
				hiddenStyle.contains("display:none") || hiddenStyle.contains("display: none"));
		// 5) Lookup backing bean and current tooltip counter
		ToolTipData toolTipData = (ToolTipData)server.getManagedBeanValue("#{toolTipData}");
		assertNotNull("Couldn't find ToolTipData managed bean",toolTipData);
		int startVal = toolTipData.getTooltipCounterWithoutMod();
		// 6) Move mouse over div to make tooltip display
		// 	Yes, all three of these are required.
		parentDiv.mouseOver();
		parentDiv.mouseMove();
		parentSubDiv.mouseOver();
		parentSubDiv.click();		

		// 0.5 delay on tooltip - so wait a sec
		Thread.sleep(1000);

		// 7) Make sure tooltip is currently visible (style does not contain 'display: none')
		String visibleStyle = tooltip1.getStyleAttribute();
		assertFalse("Tooltip not visible as expected: ["+visibleStyle+"]",
				visibleStyle.contains("display:none") || visibleStyle.contains("display: none"));				
		// 8) Our tooltip counter should have increased by 1
		toolTipData = (ToolTipData)server.getManagedBeanValue("#{toolTipData}");
		int endVal = toolTipData.getTooltipCounterWithoutMod();
		assertEquals("toolTipCounter did not increment as expected",startVal+1,endVal);
	}
}
