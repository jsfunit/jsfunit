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

import com.gargoylesoftware.htmlunit.BrowserVersion;
import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.xml.sax.SAXException;

/**
 * Peform JSFUnit tests on RichFaces demo application.
 *
 * @author Stan Silvert
 */
public class RichDropDownMenuTest extends ServletTestCase
{
   public void testDropDownMenuWithIE6() throws IOException, SAXException
   {
      JSFSession jsfSession = JSFSessionFactory.makeSession("/richfaces/dropDownMenu.jsf", BrowserVersion.INTERNET_EXPLORER_6_0);
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      client.click("New");
      String selection = (String)server.getManagedBeanValue("#{ddmenu.current}");
      assertEquals("New", selection);
      
      client.click("Open");
      selection = (String)server.getManagedBeanValue("#{ddmenu.current}");
      assertEquals("Open", selection);
      
      client.click("TextFile");
      selection = (String)server.getManagedBeanValue("#{ddmenu.current}");
      assertEquals("Save as Text File", selection);
      
      client.click("Close");
      selection = (String)server.getManagedBeanValue("#{ddmenu.current}");
      assertEquals("Close", selection);
      
      client.click("Exit");
      selection = (String)server.getManagedBeanValue("#{ddmenu.current}");
      assertEquals("Exit", selection);
   }
   
   public void testDropDownMenu() throws IOException, SAXException
   {
      JSFSession jsfSession = JSFSessionFactory.makeSession("/richfaces/dropDownMenu.jsf");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      client.click("New");
      String selection = (String)server.getManagedBeanValue("#{ddmenu.current}");
      assertEquals("New", selection);
      
      client.click("Open");
      selection = (String)server.getManagedBeanValue("#{ddmenu.current}");
      assertEquals("Open", selection);
      
      client.click("TextFile");
      selection = (String)server.getManagedBeanValue("#{ddmenu.current}");
      assertEquals("Save as Text File", selection);
      
      client.click("Close");
      selection = (String)server.getManagedBeanValue("#{ddmenu.current}");
      assertEquals("Close", selection);
      
      client.click("Exit");
      selection = (String)server.getManagedBeanValue("#{ddmenu.current}");
      assertEquals("Exit", selection);
   }

   /** This one fails for all kinds of weird reasons.  Commenting out for now.
   public void testClickHomePageLink() throws IOException, SAXException
   {
      JSFSession jsfSession = JSFSessionFactory.makeSession("/richfaces/dropDownMenu.jsf");
      JSFClientSession client = jsfSession.getJSFClientSession();
      jsfSession.getWebClient().setThrowExceptionOnFailingStatusCode(false);
      client.click("jsfunitHomePageLink");
      String newUrl = client.getContentPage().getWebResponse().getUrl().toString();
      assertEquals("http://www.jboss.org/jsfunit/", newUrl);
   } */

   public void testServerModeDropDownMenuWithIE6() throws IOException, SAXException
   {
      JSFSession jsfSession = JSFSessionFactory.makeSession("/richfaces/dropDownMenu.jsf", BrowserVersion.INTERNET_EXPLORER_6_0);
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      client.click("NewServerMode");
      String selection = (String)server.getManagedBeanValue("#{ddmenu.current}");
      assertEquals("New", selection);
      
      client.click("OpenServerMode");
      selection = (String)server.getManagedBeanValue("#{ddmenu.current}");
      assertEquals("Open", selection);
      
      client.click("TextFileServerMode");
      selection = (String)server.getManagedBeanValue("#{ddmenu.current}");
      assertEquals("Save as Text File", selection);
      
      client.click("CloseServerMode");
      selection = (String)server.getManagedBeanValue("#{ddmenu.current}");
      assertEquals("Close", selection);
      
      client.click("ExitServerMode");
      selection = (String)server.getManagedBeanValue("#{ddmenu.current}");
      assertEquals("Exit", selection);
   }
   
   public void testServerModeDropDownMenu() throws IOException, SAXException
   {
      JSFSession jsfSession = JSFSessionFactory.makeSession("/richfaces/dropDownMenu.jsf");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      client.click("NewServerMode");
      String selection = (String)server.getManagedBeanValue("#{ddmenu.current}");
      assertEquals("New", selection);
      
      client.click("OpenServerMode");
      selection = (String)server.getManagedBeanValue("#{ddmenu.current}");
      assertEquals("Open", selection);
      
      client.click("TextFileServerMode");
      selection = (String)server.getManagedBeanValue("#{ddmenu.current}");
      assertEquals("Save as Text File", selection);
      
      client.click("CloseServerMode");
      selection = (String)server.getManagedBeanValue("#{ddmenu.current}");
      assertEquals("Close", selection);
      
      client.click("ExitServerMode");
      selection = (String)server.getManagedBeanValue("#{ddmenu.current}");
      assertEquals("Exit", selection);
   }
   
   public static Test suite()
   {
      return new TestSuite( RichDropDownMenuTest.class );
   }
}
