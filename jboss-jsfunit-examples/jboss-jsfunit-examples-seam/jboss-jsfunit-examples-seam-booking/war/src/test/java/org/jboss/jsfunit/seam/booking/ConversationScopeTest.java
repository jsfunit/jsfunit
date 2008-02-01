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

package org.jboss.jsfunit.seam.booking;

import java.io.IOException;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.facade.JSFClientSession;
import org.jboss.jsfunit.facade.JSFServerSession;
import org.jboss.jsfunit.richfaces.RichFacesClient;
import org.jboss.jsfunit.seam.SeamClient;
import org.xml.sax.SAXException;

/**
 *
 * @author Stan Silvert
 */
public class ConversationScopeTest extends ServletTestCase
{
   
   private JSFClientSession client;
   private JSFServerSession server;
   private SeamClient seamClient;
   private RichFacesClient richClient;
   
   /**
    * Start a JSFUnit session by getting the /index.faces page.  Note that
    * because setUp() is called before each test, a new HttpSession will be
    * created each time a test is run.
    */
   public void setUp() throws IOException, SAXException
   {
      this.client = RegisterBot.registerAndLogin("ConvScopeUser", "password");
      this.server = new JSFServerSession(this.client);
      this.seamClient = new SeamClient(this.client);
      this.richClient = new RichFacesClient(this.client);
   }
   
   public void testGetHotelBooking() throws IOException, SAXException
   {
      client.setParameter("searchString", "Hilton");
      richClient.ajaxSubmit("findHotels");
      assertTrue(richClient.getAjaxResponse().contains("Hilton"));
      seamClient.clickSLink(":0:viewHotel");
      assertEquals("/hotel.xhtml", server.getCurrentViewID());
      assertNotNull(server.getManagedBeanValue("#{hotel}"));
      
   }
   
}
