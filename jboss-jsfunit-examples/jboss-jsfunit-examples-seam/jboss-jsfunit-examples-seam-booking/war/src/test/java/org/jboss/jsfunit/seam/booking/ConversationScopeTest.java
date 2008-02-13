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
import java.util.Iterator;
import javax.faces.application.FacesMessage;
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
   
   public void testGetHotelBooking() throws IOException, SAXException
   {
      SeamClient client = RegisterBot.registerAndLogin("ConvScopeUser", "password");
      JSFServerSession server = new JSFServerSession(client);
      RichFacesClient richClient = new RichFacesClient(client);
      
      client.setParameter("searchString", "Hilton");
      richClient.ajaxSubmit("findHotels");
      assertTrue(richClient.getAjaxResponse().contains("Hilton"));
      client.clickSLink(":0:viewHotel");
      assertEquals("/hotel.xhtml", server.getCurrentViewID());
      assertNotNull(server.getManagedBeanValue("#{hotel}"));
   }

   public void testTemporaryConversation() throws IOException, SAXException
   {
      SeamClient client = new SeamClient("/home.seam");
      JSFServerSession server = new JSFServerSession(client);
      
      client.clickSLink("register");
      client.setParameter("username", "ssilvert");
      client.setParameter(":name", "Stan Silvert");
      client.setParameter("password", "foobar");
      client.setParameter("verify", "barfoo");
      client.submit("register");
      
      FacesMessage message = (FacesMessage)server.getFacesMessages().next();
      assertEquals("Re-enter your password", message.getDetail());
   }
}
