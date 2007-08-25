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

package org.jboss.jsfunit.example.hellojsf;

import com.meterware.httpunit.WebConversation;
import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.facade.ClientFacade;
import org.jboss.jsfunit.facade.ServerFacade;
import org.jboss.jsfunit.framework.WebConversationFactory;
import org.xml.sax.SAXException;

/**
 * This class tests a ClientFacade that uses a custom WebConversation.
 *
 * @author Stan Silvert
 */
public class CustomWebConversationTest extends ServletTestCase
{
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      return new TestSuite( CustomWebConversationTest.class );
   }
   
   public void testCustomizedWebConversation() throws IOException, SAXException
   {
      WebConversation webConv = WebConversationFactory.makeWebConversation();
      webConv.setHeaderField("mycoolheader", "mycoolvalue");
      ClientFacade client = new ClientFacade(webConv, "/index.faces");
      ServerFacade server = new ServerFacade(client);
      Object headerValue = server.getFacesContext()
                                 .getExternalContext()
                                 .getRequestHeaderValuesMap()
                                 .get("mycoolheader");
      assertEquals("mycoolvalue", ((String[])headerValue)[0]);
   }
   
   public void testNullWebConversation() throws IOException, SAXException
   {
      try
      {
         ClientFacade client = new ClientFacade(null, "/index.faces");
         fail("Expected IllegalArgumentException");
      }
      catch (IllegalArgumentException e)
      {
         // OK
      }
   }
   
   // WebConversation must come from the WebConversationFactory
   public void testInvalidWebConversation() throws IOException, SAXException
   {
      try
      {
         WebConversation webConv = new WebConversation();
         ClientFacade client = new ClientFacade(webConv, "/index.faces");
         fail("Expected IllegalArgumentException");
      }
      catch (IllegalArgumentException e)
      {
         // OK
      }
   }
   
   // getWebConversation breaks encapsulation.  Is that bad for this kind
   // of Facade?
   public void testGetWebConversation() throws IOException, SAXException
   {
      WebConversation webConv = WebConversationFactory.makeWebConversation();
      
      ClientFacade client = new ClientFacade(webConv, "/index.faces");
      
      WebConversation webConvFromClient = client.getWebConversation();
      assertEquals(webConv, webConvFromClient);
      
      webConvFromClient.setHeaderField("mycoolheader", "mycoolvalue");
      client.submit("submit_button");
      
      ServerFacade server = new ServerFacade(client);
      Object headerValue = server.getFacesContext()
                                 .getExternalContext()
                                 .getRequestHeaderValuesMap()
                                 .get("mycoolheader");
      assertEquals("mycoolvalue", ((String[])headerValue)[0]);
   }
   
}
