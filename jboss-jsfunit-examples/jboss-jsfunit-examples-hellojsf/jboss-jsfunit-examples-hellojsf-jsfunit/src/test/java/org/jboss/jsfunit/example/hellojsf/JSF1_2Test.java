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

import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import java.io.IOException;
import javax.faces.component.UIComponent;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.facade.JSFClientSession;
import org.jboss.jsfunit.facade.ComponentIDNotFoundException;
import org.jboss.jsfunit.facade.FormNotFoundException;
import org.jboss.jsfunit.facade.JSFServerSession;
import org.jboss.jsfunit.framework.Environment;
import org.xml.sax.SAXException;

/**
 * This class does all of the tests in the FacadeAPITest, but it does it
 * against a form that uses a <f:subview> and prependId="false".  Also, we
 * use this class to test the Environment class.
 *
 * These tests will only run in a JSF 1.2 environment.
 * 
 * @author Stan Silvert
 */
public class JSF1_2Test extends ServletTestCase
{
   private JSFClientSession client;
   
   /**
    * Start a JSFUnit session by getting the /index.jsf.1.2.faces page.  Note that
    * because setUp() is called before each test, a new HttpSession will be
    * created each time a test is run.
    */
   public void setUp() throws IOException, SAXException
   {
      this.client = new JSFClientSession("/index.jsf.1.2.faces");
   }
   
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      if (Environment.getJSFMinorVersion() != 2) return new TestSuite();
      return new TestSuite( JSF1_2Test.class );
   }
   
   public void testJSFMajorVersion() 
   {
      assertEquals(1, Environment.getJSFMajorVersion());
   }
   
   public void testJSFMinorVersion()
   {
      String jsfMinorProp = System.getProperty("jsfunit.jsfMinorVersion", "2");
      int minorVersion = Integer.parseInt(jsfMinorProp);
      assertEquals(minorVersion, Environment.getJSFMinorVersion());
   }
   
   public void testSetParamAndSubmit() throws IOException, SAXException
   {
      client.setParameter("input_foo_text", "Stan"); 
      client.submit("submit_button");
      
      JSFServerSession server = new JSFServerSession(client);
      UIComponent greeting = server.findComponent("greeting");
      assertTrue(greeting.isRendered());
   }
   
   public void testSubmitNoButton() throws IOException, SAXException
   {
      JSFClientSession client = new JSFClientSession("/indexNoButtons.faces");
      JSFServerSession server = new JSFServerSession(client);
      UIComponent greeting = server.findComponent("greeting");
      assertFalse(greeting.isRendered());
      
      client.setParameter("input_foo_text", "Stan"); 
      client.submitNoButton("form1");
      
      greeting = server.findComponent("greeting");
      assertTrue(greeting.isRendered());
   }
   
   public void testSetCheckbox() throws IOException, SAXException
   {
      JSFServerSession server = new JSFServerSession(client);
      client.setCheckbox("funcheck", false);
      client.submit("submit_button");
      assertFalse((Boolean)server.getManagedBeanValue("#{checkbox.funCheck}"));
      
      client.setCheckbox("funcheck", true);
      client.submit("submit_button");
      assertTrue((Boolean)server.getManagedBeanValue("#{checkbox.funCheck}"));
   }
   
   public void testNoArgSubmit() throws IOException, SAXException
   {
      client.submit("goodbye_button");  // go to finalgreeting page
      client.submit(); // only one submit button on finalgreeting page
      JSFServerSession server = new JSFServerSession(client);
      
      // test that we are back on the first page
      assertEquals("/index.jsp", server.getCurrentViewID());  
   }
   
   public void testClickCommandLink() throws IOException, SAXException
   {
      client.submit("goodbye_button");
      client.clickCommandLink("go_back_link");
      JSFServerSession server = new JSFServerSession(client);
      
      // test that we are back on the first page
      assertEquals("/index.jsp", server.getCurrentViewID());
   }
   
   public void testCommandLinkWithoutViewChange() throws IOException, SAXException
   {
      client.submit("goodbye_button");
      client.clickCommandLink("stay_here_link");
      JSFServerSession server = new JSFServerSession(client);
      
      // test that we are still on the same page
      assertEquals("/finalgreeting.jsp", server.getCurrentViewID());
   }
   
   public void testServerSideComponentValue() throws IOException, SAXException
   {
      testSetParamAndSubmit(); // put "Stan" into the input field

      // test the greeting component
      JSFServerSession server = new JSFServerSession(client);
      assertEquals("Hello Stan", server.getComponentValue("greeting"));
   }
   
   public void testManagedBeanValue() throws IOException, SAXException
   {
      testSetParamAndSubmit(); // put "Stan" into the input field

      JSFServerSession server = new JSFServerSession(client);
      assertEquals("Stan", server.getManagedBeanValue("#{foo.text}"));
   }
   
   public void testClickALink() throws IOException, SAXException
   {
      client.clickLink("SourceSimplifiedHelloJSFIntegrationTest");
      WebResponse response = client.getWebResponse();
      assertTrue(response.getText().contains("public class SimplifiedHelloJSFIntegrationTest"));
   }
      
}
