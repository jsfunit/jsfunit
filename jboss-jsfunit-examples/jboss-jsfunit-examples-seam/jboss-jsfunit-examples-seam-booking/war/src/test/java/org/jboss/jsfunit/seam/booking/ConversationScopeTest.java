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
import javax.faces.application.FacesMessage;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.xml.sax.SAXException;

/**
 *
 * @author Stan Silvert
 */
public class ConversationScopeTest extends ServletTestCase
{
   
   public void testGetHotelBooking() throws IOException, SAXException
   {
      JSFSession jsfSession = RegisterBot.registerAndLogin("ConvScopeUser", "password");
      JSFServerSession server = jsfSession.getJSFServerSession();
      JSFClientSession client = jsfSession.getJSFClientSession();
      
      client.setValue("searchString", "Hilton");
      client.click("findHotels");
      assertTrue(client.getPageAsText().contains("Hilton"));
      client.click(":0:viewHotel");
      assertEquals("/hotel.xhtml", server.getCurrentViewID());
      assertNotNull(server.getManagedBeanValue("#{hotel}"));
   }

   public void testTemporaryConversation() throws IOException, SAXException
   {
      JSFSession jsfSession = new JSFSession("/home.seam");
      JSFServerSession server = jsfSession.getJSFServerSession();
      JSFClientSession client = jsfSession.getJSFClientSession();
      
      client.click("register");
      client.setValue("username", "ssilvert");
      client.setValue(":name", "Stan Silvert");
      client.setValue("password", "foobar");
      client.setValue("verify", "barfoo");
      client.click("register");
      
      FacesMessage message = (FacesMessage)server.getFacesMessages().next();
      assertEquals("Re-enter your password", message.getDetail());
   }
}
