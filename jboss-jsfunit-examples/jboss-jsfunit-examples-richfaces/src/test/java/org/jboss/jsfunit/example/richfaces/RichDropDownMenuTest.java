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
import org.xml.sax.SAXException;

/**
 * Peform JSFUnit tests on RichFaces demo application.
 *
 * @author Stan Silvert
 */
public class RichDropDownMenuTest extends ServletTestCase
{
   public void testDropDownMenu() throws IOException, SAXException
   {
      JSFClientSession client = new JSFClientSession("/richfaces/dropDownMenu.jsf");
      RichFacesClient ajaxClient = new RichFacesClient(client);
      JSFServerSession server = new JSFServerSession(client);
      
      ajaxClient.ajaxSubmit("New");
      String selection = (String)server.getManagedBeanValue("#{ddmenu.current}");
      assertEquals(selection, "New");
      
      ajaxClient.ajaxSubmit("Open");
      selection = (String)server.getManagedBeanValue("#{ddmenu.current}");
      assertEquals(selection, "Open");
      
      ajaxClient.ajaxSubmit("TextFile");
      selection = (String)server.getManagedBeanValue("#{ddmenu.current}");
      assertEquals(selection, "Save as Text File");
      
      ajaxClient.ajaxSubmit("Close");
      selection = (String)server.getManagedBeanValue("#{ddmenu.current}");
      assertEquals(selection, "Close");
      
      ajaxClient.ajaxSubmit("Exit");
      selection = (String)server.getManagedBeanValue("#{ddmenu.current}");
      assertEquals(selection, "Exit");
   }
   
   public void testClickHomePageLink() throws IOException, SAXException
   {
      JSFClientSession client = new JSFClientSession("/richfaces/dropDownMenu.jsf");
      client.clickLink("jsfunitHomePageLink");
      String newUrl = client.getWebResponse().getURL().toString();
      assertEquals("http://www.jboss.org/jsfunit/", newUrl);
   }
   
   public static Test suite()
   {
      return new TestSuite( RichDropDownMenuTest.class );
   }
}
