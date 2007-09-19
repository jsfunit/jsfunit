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

import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.facade.JSFClientSession;
import org.jboss.jsfunit.facade.ComponentIDNotFoundException;
import org.jboss.jsfunit.facade.DuplicateClientIDException;
import org.jboss.jsfunit.facade.JSFServerSession;
import org.xml.sax.SAXException;

/**
 * Test the facade API on a page that contains nested subviews/NamingContainers.
 *
 * @author Stan Silvert
 */
public class NestedNamingContainersTest extends ServletTestCase
{
   private JSFClientSession client;
   
   /**
    * Start a JSFUnit session by getting the /index.faces page.
    */
   public void setUp() throws IOException, SAXException
   {
      // Initial JSF request
      this.client = new JSFClientSession("/NestedNamingContainers.faces");
   }
   
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      return new TestSuite( NestedNamingContainersTest.class );
   }
   
   // These first four tests are identical to the ones in 
   // SimplifiedHelloJSFIntegrationTest except that they operate 
   // on NestedNamingContainers.jsp which contains a form with 
   // with subviews.  It also contains two forms.  So you have
   // to specify which form you are operating on.
   
   /**
    * The initial page was called up in the setUp() method.  This shows
    * some simple JSFUnit tests you can do on that page.
    */
   public void testInitialPage() throws IOException, SAXException
   {
      JSFServerSession server = new JSFServerSession(client);

      // Test navigation to initial viewID
      assertEquals("/NestedNamingContainers.jsp", server.getCurrentViewId());

      // Assert that the prompt component is in the component tree and rendered
      UIComponent prompt = server.findComponent("form1:prompt");
      assertTrue(prompt.isRendered());

      // Assert that the greeting component is in the component tree but not rendered
      UIComponent greeting = server.findComponent("form1:greeting");
      assertFalse(greeting.isRendered());
   }
   
   
   public void testInputValidation() throws IOException, SAXException
   {
      client.setParameter("form1:input_foo_text", "A"); // input too short - validation error
      client.submit("form1:submit_button");

      JSFServerSession server = new JSFServerSession(client);
      
      // Test that I was returned to the initial view because of input error
      assertEquals("/NestedNamingContainers.jsp", server.getCurrentViewId());
      
      // Should be only one FacesMessge generated for this test.
      // It is for the component input_foo_text.
      FacesMessage message = (FacesMessage)server.getFacesContext().getMessages().next();
      assertTrue(message.getDetail().contains("input_foo_text"));
   }
   
   /**
    * This demonstrates how to test managed beans.
    */
   public void testValidInput() throws IOException, SAXException
   {
      client.setParameter("form1:input_foo_text", "Stan");
      client.submit("form1:submit_button");

      JSFServerSession server = new JSFServerSession(client);
      
      // test the greeting component
      UIComponent greeting = server.findComponent("form1:greeting");
      assertTrue(greeting.isRendered());
      assertEquals("Hello Stan", server.getComponentValue("form1:greeting"));

      // Test the backing bean.  Since I am in-container, I can test any
      // managed bean in any scope - even Seam scopes such as Conversation.
      assertEquals("Stan", server.getManagedBeanValue("#{foo.text}"));
   }
   
   public void testGoodbyeButton() throws IOException, SAXException
   {
      testValidInput(); // put "Stan" into the input field
      client.submit("form1:goodbye_button");

      JSFServerSession server = new JSFServerSession(client);
      
      // Test navigation to a new view
      assertEquals("/finalgreeting.jsp", server.getCurrentViewId());

      // Test the greeting
      assertEquals("Bye Stan. I enjoyed our chat.", server.getComponentValue("finalgreeting"));
   }
   
   public void testDualFormAmbiguity() throws IOException, SAXException
   {
      JSFServerSession server = new JSFServerSession(client);
      
      try
      {
         server.findComponent("greeting");
         fail("Expected DuplicateClientIDException");
      }
      catch (DuplicateClientIDException e)
      {
         // ok
      }
   }
   
   public void testComponentIDNotFound() throws IOException, SAXException
   {
      try
      {
         client.submit("bogus_submit_button");
         fail("Expected ComponentIDNotFoundException");
      }
      catch (ComponentIDNotFoundException e)
      {
         // ok
      }
   }
}
