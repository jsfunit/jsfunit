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

package org.jboss.jsfunit.jsfsession.hellojsf;

import java.io.IOException;
import javax.faces.component.UIComponent;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.framework.Environment;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.xml.sax.SAXException;

/**
 * This class does all of the tests in the FacadeAPITest, but it does it
 * against a form that uses a <f:subview> and prependId="false".  
 *
 * These tests will only run in a JSF 1.2 compatible environment.
 * 
 * @author Stan Silvert
 */
public class JSF1_2Test extends ServletTestCase
{
   private JSFClientSession client;
   private JSFServerSession server;
   
   /**
    * Start a JSFUnit session by getting the /index.jsf.1.2.faces page.  Note that
    * because setUp() is called before each test, a new HttpSession will be
    * created each time a test is run.
    */
   public void setUp() throws IOException, SAXException
   {
      JSFSession jsfSession = new JSFSession("/index.jsf.1.2.faces");
      this.client = jsfSession.getJSFClientSession();
      this.server = jsfSession.getJSFServerSession();
   }
   
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      if (!Environment.is12Compatible()) return new TestSuite();
      return new TestSuite( JSF1_2Test.class );
   }
   
   public void testSetParamAndSubmit() throws IOException, SAXException
   {
      client.setValue("input_foo_text", "Stan"); 
      client.click("submit_button");
      
      UIComponent greeting = server.findComponent("greeting");
      assertTrue(greeting.isRendered());
   }
   
   public void testSetCheckbox() throws IOException, SAXException
   {
      client.setValue("input_foo_text", "Stan");
      client.click("funcheck");
      client.click("submit_button");
      assertFalse((Boolean)server.getManagedBeanValue("#{checkbox.funCheck}"));
      
      client.click("funcheck");
      client.click("submit_button");
      assertTrue((Boolean)server.getManagedBeanValue("#{checkbox.funCheck}"));
   }
   
   public void testClickCommandLink() throws IOException, SAXException
   {
      System.out.println("*********** Dumpint ID's ****************");
      server.getClientIDs().dumpAllIDs();
      System.out.println("****************************************");
      client.setValue("input_foo_text", "Stan");
      client.click("goodbye_button");
      client.click("go_back_link");
      
      // test that we are back on the first page
      assertEquals("/index.jsp", server.getCurrentViewID());
   }
   
   public void testCommandLinkWithoutViewChange() throws IOException, SAXException
   {
      client.setValue("input_foo_text", "Stan");
      client.click("goodbye_button");
      client.click("stay_here_link");
      
      // test that we are still on the same page
      assertEquals("/finalgreeting.jsp", server.getCurrentViewID());
   }
   
   public void testServerSideComponentValue() throws IOException, SAXException
   {
      testSetParamAndSubmit(); // put "Stan" into the input field

      // test the greeting component
      assertEquals("Hello Stan", server.getComponentValue("greeting"));
   }
   
   public void testManagedBeanValue() throws IOException, SAXException
   {
      testSetParamAndSubmit(); // put "Stan" into the input field

      assertEquals("Stan", server.getManagedBeanValue("#{foo.text}"));
   }
   
   public void testClickALink() throws IOException, SAXException
   {
      client.click("SourceSimplifiedHelloJSFIntegrationTest");
      assertTrue(client.getPageAsText().contains("public class SimplifiedHelloJSFIntegrationTest"));
   }
      
}
