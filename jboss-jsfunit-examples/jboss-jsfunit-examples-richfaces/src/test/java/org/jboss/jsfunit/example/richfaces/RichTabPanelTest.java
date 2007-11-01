/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.jsfunit.example.richfaces;

import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.richfaces.RichFacesClient;
import org.jboss.jsfunit.facade.JSFClientSession;
import org.jboss.jsfunit.facade.JSFServerSession;
import org.richfaces.component.UITabPanel;
import org.xml.sax.SAXException;

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
           
   public void setUp() throws IOException, SAXException
   {
     this.client = new JSFClientSession("/richfaces/tabPanel.jsf");
     this.ajaxClient = new RichFacesClient(client);
     this.server = new JSFServerSession(client);
   }
   
   public void testDefaultTabPanel() throws IOException, SAXException
   {
      UITabPanel panel = (UITabPanel)server.findComponent("defaultTabPanel");
      String selectedTab = (String)panel.getSelectedTab();
      assertEquals("defaultFirst", selectedTab);
      
      ajaxClient.clickTab("defaultTabPanel", "defaultSecond");
      
      panel = (UITabPanel)server.findComponent("defaultTabPanel");
      selectedTab = (String)panel.getSelectedTab();
      assertEquals("defaultSecond", selectedTab);
   }
   
   public void testAjaxTabPanel() throws IOException, SAXException
   {
      UITabPanel panel = (UITabPanel)server.findComponent("ajaxTabPanel");
      String selectedTab = (String)panel.getSelectedTab();
      assertEquals("ajaxFirst", selectedTab);
      
      ajaxClient.clickTab("ajaxTabPanel", "ajaxThird");
      
      panel = (UITabPanel)server.findComponent("ajaxTabPanel");
      selectedTab = (String)panel.getSelectedTab();
      assertEquals("ajaxThird", selectedTab);
   }
   
   public void testDisabledTabPanel() throws IOException, SAXException
   {
      // if the tab is disabled, this is a no-op      
      ajaxClient.clickTab("ajaxTabPanel", "ajaxSecond");
      
      UITabPanel panel = (UITabPanel)server.findComponent("ajaxTabPanel");
      String selectedTab = (String)panel.getSelectedTab();
      assertEquals("ajaxFirst", selectedTab);
   }
   
   public void testClientTabPanel() throws IOException, SAXException
   {
      String page = client.getWebResponse().getText();
      assertTrue(page.contains("Here is tab #2")); // hidden, but already there
      assertTrue(page.contains("Here is tab #3")); // hidden, but already there

      // If tab is in client mode, this is a no-op. No server request triggered.
      ajaxClient.clickTab("clientTabPanel", "clientSecond");
   }
   
   public static Test suite()
   {
      return new TestSuite( RichTabPanelTest.class );
   }
}
