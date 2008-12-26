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
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.jboss.seam.example.booking.Hotel;
import org.jboss.seam.example.booking.HotelBooking;

/**
 * Tests JSFUnit interaction with Seam conversation scope.
 *
 * @author Stan Silvert
 */
public class HtmlUnitConversationScopeTest extends ServletTestCase
{
   
   public void testGetHotelBooking() throws IOException
   {
      //org.jboss.seam.security.Identity.setSecurityEnabled(false);

      registerUser("ConvScopeUser", "password");
      
      JSFSession jsfSession = new JSFSession("/home.seam");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      // log in
      client.setValue("username", "ConvScopeUser");
      client.setValue("password", "password");
      client.click("login:login");
      
      // type in Hilton      
      client.type("searchString", 'H');
      client.type("searchString", 'i');
      client.type("searchString", 'l');
      client.type("searchString", 't');
      client.type("searchString", 'o');
      client.type("searchString", 'n');
      
      assertTrue(client.getPageAsText().contains("Hilton Tel Aviv"));
      
      client.click(":0:viewHotel");
      assertEquals("/hotel.xhtml", server.getCurrentViewID());
      
      client.click("hotel:bookHotel");
      
      Hotel hotel = (Hotel)server.getManagedBeanValue("#{seamconversation.hotel}");
      assertNotNull(hotel);
      assertEquals("Hilton Diagonal Mar", hotel.getName());
      
      HotelBooking booking = (HotelBooking)server.getManagedBeanValue("#{seamconversation.hotelBooking}");
      assertNotNull(booking);
      String selectedHotelName = booking.getSelectedHotelName();
      assertEquals("Hilton Diagonal Mar", selectedHotelName);
   } 
   
   // Make sure that conversation scope cache gets cleared when a new JSFSession
   // is created.
   public void testConversationScopeLeak() throws IOException
   {
      testGetHotelBooking();
      
      JSFSession jsfSession = new JSFSession("/home.seam");
      JSFServerSession server = jsfSession.getJSFServerSession();
      assertNull(server.getManagedBeanValue("#{seamconversation.hotel}"));
   }
   
   /**
    * Register a new user for the booking demo.  If the username already exists
    * then this method returns sucessfully.
    *
    * @throws IllegalArgumentException if the username/password is invalid.
    */
   public static void registerUser(String username, String password) throws IOException
   {
      //org.jboss.seam.security.Identity.setSecurityEnabled(false);

      JSFSession jsfSession = new JSFSession("/home.seam");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      client.click("register");
      client.setValue("username", username);
      client.setValue(":name", username);
      client.setValue("password", password);
      client.setValue("verify", password);
      client.click("register");
      
      Iterator facesMessages = server.getFacesMessages();
      if (facesMessages.hasNext()) 
      {
         FacesMessage message = (FacesMessage)facesMessages.next();
         String detail = message.getDetail();
         if (detail.toLowerCase().contains("already exists")) return;  // OK
         if (detail.toLowerCase().contains("successfully registered")) return; // OK
         throw new IllegalArgumentException(detail);
      }
   }
   
   public void testTemporaryConversation() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/home.seam");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      client.click("register");
      client.setValue("username", "ssilvert");
      client.setValue(":name", "Stan Silvert");
      client.setValue("password", "foobar");
      client.setValue("verify", "barfoo"); // password doesn't match
      client.click("register");
      
      FacesMessage message = (FacesMessage)server.getFacesMessages().next();
      assertEquals("Re-enter your password", message.getDetail());
   }
   
   // JSFUNIT-172
   public void testIdentityExistsAfterInitialRequest() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/home.seam");
      JSFServerSession server = jsfSession.getJSFServerSession();
      assertNotNull(server.getManagedBeanValue("#{identity}"));
   }
}
