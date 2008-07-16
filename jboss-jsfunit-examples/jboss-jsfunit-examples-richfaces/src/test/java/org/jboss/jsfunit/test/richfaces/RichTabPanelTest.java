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

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.jsfsession.ClientIDs;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.jboss.jsfunit.richfaces.RichFacesClient;
import org.richfaces.component.UITabPanel;

/**
 * Peform JSFUnit tests on RichFaces demo application.
 *
 * @author Stan Silvert
 */
public class RichTabPanelTest extends ServletTestCase
{
   private JSFClientSession client;
   private RichFacesClient ajaxClient;
   private JSFServerSession server;
           
   public void setUp() throws IOException
   {
     JSFSession jsfSession = new JSFSession("/richfaces/tabPanel.jsf");
     this.client = jsfSession.getJSFClientSession();
     this.ajaxClient = new RichFacesClient(this.client);
     this.server = jsfSession.getJSFServerSession();
   }
   
   public void testDefaultTabPanel() throws IOException
   {
  /*    UITabPanel panel = (UITabPanel)server.findComponent("defaultTabPanel");
      String selectedTab = (String)panel.getSelectedTab();
      assertEquals("defaultFirst", selectedTab);
      
      ajaxClient.clickTab("defaultSecond");
      
      panel = (UITabPanel)server.findComponent("defaultTabPanel");
      selectedTab = (String)panel.getSelectedTab();
      assertEquals("defaultSecond", selectedTab); */
   }
   
   public void testAjaxTabPanel() throws IOException
   {/*
      UITabPanel panel = (UITabPanel)server.findComponent("ajaxTabPanel");
      String selectedTab = (String)panel.getSelectedTab();
      assertEquals("ajaxFirst", selectedTab);
ClientIDs idsBefore = server.getClientIDs();      
      ajaxClient.clickTab("ajaxThird");
ClientIDs idsAfter = server.getClientIDs();
assertTrue(idsBefore != idsAfter);
      panel = (UITabPanel)server.findComponent("ajaxTabPanel");
      selectedTab = (String)panel.getSelectedTab();
      assertEquals("ajaxThird", selectedTab); */
   } 
   
   public void testDisabledTabPanel() throws IOException
   {
      // if the tab is disabled, this is a no-op      
      ajaxClient.clickTab("ajaxSecond");
      
      UITabPanel panel = (UITabPanel)server.findComponent("ajaxTabPanel");
      String selectedTab = (String)panel.getSelectedTab();
      assertEquals("ajaxFirst", selectedTab);
   }
   
   public void testClientTabPanel() throws IOException
   {
      HtmlElement clientSecond = (HtmlElement)client.getElement("clientSecond");
      assertTrue(clientSecond.getTextContent().contains("Here is tab #2")); // hidden, but already there
      
      HtmlElement clientThird = (HtmlElement)client.getElement("clientThird");
      assertTrue(clientThird.getTextContent().contains("Here is tab #3")); // hidden, but already there

      // If tab is in client mode, this is a no-op. No server request triggered.
      ajaxClient.clickTab("clientSecond");
   }
   
   public static Test suite()
   {
      return new TestSuite( RichTabPanelTest.class );
   }
}
