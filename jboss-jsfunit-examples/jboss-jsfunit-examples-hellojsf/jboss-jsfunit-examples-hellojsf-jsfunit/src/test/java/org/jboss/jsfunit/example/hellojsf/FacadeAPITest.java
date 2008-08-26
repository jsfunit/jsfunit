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
import java.util.Iterator;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.facade.JSFClientSession;
import org.jboss.jsfunit.facade.JSFServerSession;
import org.jboss.jsfunit.framework.Environment;
import org.xml.sax.SAXException;

/**
 * This class tests all of the API's in the JSFClientSession and JSFServerSession.
 * 
 * 
 * 
 * @author Stan Silvert
 */
public class FacadeAPITest extends ServletTestCase
{
   private JSFClientSession client;
   
   /**
    * Start a JSFUnit session by getting the /index.faces page.  Note that
    * because setUp() is called before each test, a new HttpSession will be
    * created each time a test is run.
    */
   public void setUp() throws IOException, SAXException
   {
      this.client = new JSFClientSession("/index.faces");
   }
   
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      return new TestSuite( FacadeAPITest.class );
   }
   
   /**
    */
   public void testGetCurrentViewId() throws IOException, SAXException
   {
      JSFServerSession server = new JSFServerSession(client);
      
      // Test navigation to initial viewID
      assertEquals("/index.jsp", server.getCurrentViewID());
      assertEquals(server.getCurrentViewID(), server.getFacesContext().getViewRoot().getViewId());
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
   
   /**
    * Tests JSFClientSession.submit().  This can only be called if there is
    * only one submit button on the form.
    */
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
      // doesn't run on JSF 1.2_09 - turn off this test for JBoss
      // this test will be removed after Beta 3 anyway
      if (System.getProperty("jboss.server.home.dir") != null) return;
      
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
   
   public void testCommandLinkWithFParam() throws IOException, SAXException
   {
      // doesn't run on JSF 1.2_09 - turn off this test for JBoss
      // this test will be removed after Beta 3 anyway
      if (System.getProperty("jboss.server.home.dir") != null) return;
      
      client.setParameter("input_foo_text", "Stan");
      client.submit("goodbye_button");
      client.clickCommandLink("stay_here_link");
      
      // link includes <f:param id="name" name="name" value="#{foo.text}"/>
      String name = (String)FacesContext.getCurrentInstance()
                                        .getExternalContext()
                                        .getRequestParameterMap()
                                        .get("name");
      
      assertEquals("Stan", name);
   }

   public void testCommandLinkWithParamFromLoopVariable() throws IOException, SAXException
   {
      // doesn't run on JSF 1.2_09 - turn off this test for JBoss
      // this test will be removed after Beta 3 anyway
      if (System.getProperty("jboss.server.home.dir") != null) return;
      
      // test should not run for JSF 1.1 - it uses a loop variable from <c:forEach>
      if ((Environment.getJSFMajorVersion() == 1) &&
          (Environment.getJSFMinorVersion() < 2)) return;
      
      JSFClientSession client = new JSFClientSession("/marathons.faces");
      
      client.clickCommandLink("marathonSelect");
      assertTrue(client.getWebResponse().getText().contains("Selected Marathon: BAA Boston Marathon"));
      
      client.clickCommandLink("marathonSelectj_id_3");
      assertTrue(client.getWebResponse().getText().contains("Selected Marathon: Flora London Marathon"));
      
      client.clickCommandLink("marathonSelectj_id_5");
      assertTrue(client.getWebResponse().getText().contains("Selected Marathon: Olympic Marathon"));
   }
   
   // componentID must be a command link
   private boolean isMyFaces114(JSFClientSession client, String componentID) throws IOException, SAXException
   {
      WebForm form = client.getForm(componentID);
      
      String myFaces115CmdLink = form.getID() + ":" + "_idcl";
      if (form.hasParameterNamed(myFaces115CmdLink)) return false;
      
      String myFaces114CmdLink = form.getID() + ":" + "_link_hidden_";
      return form.hasParameterNamed(myFaces114CmdLink);
   }
   
   public void testCommandLinkWithParamFromDatatableVariable() throws IOException, SAXException
   {
      // doesn't run on JSF 1.2_09 - turn off this test for JBoss
      // this test will be removed after Beta 3 anyway
      if (System.getProperty("jboss.server.home.dir") != null) return;
      
      JSFClientSession client = new JSFClientSession("/marathons_datatable.faces");
      
      if (isMyFaces114(client, "0:marathonSelect")) return; // don't run this test under MyFaces 1.1.4
      
      client.clickCommandLink("0:marathonSelect");
      assertTrue(client.getWebResponse().getText().contains("Selected Marathon: BAA Boston Marathon"));
      
      client.clickCommandLink("3:marathonSelect");
      assertTrue(client.getWebResponse().getText().contains("Selected Marathon: Flora London Marathon"));
      
      client.clickCommandLink("5:marathonSelect");
      assertTrue(client.getWebResponse().getText().contains("Selected Marathon: Olympic Marathon"));
   }
   
   public void testServerSideComponentValue() throws IOException, SAXException
   {
      testSetParamAndSubmit(); // put "Stan" into the input field

      // test the greeting component
      JSFServerSession server = new JSFServerSession(client);
      assertEquals("Hello Stan", server.getComponentValue("greeting"));
   }
   
   /**
    * This demonstrates how to test managed beans.
    */
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
   
   public void testFacesMessages() throws IOException, SAXException
   {
      client.setParameter("input_foo_text", "A"); // input too short - validation error
      client.submit("submit_button");

      // Test that I was returned to the initial view because of input error
      JSFServerSession server = new JSFServerSession(client);
      assertEquals("/index.jsp", server.getCurrentViewID());
      
      // Should be only one FacesMessge generated for the page.
      Iterator<FacesMessage> allMessages = server.getFacesMessages();
      allMessages.next();
      assertFalse(allMessages.hasNext());
      
      Iterator<FacesMessage> checkboxMessages = server.getFacesMessages("funcheck");
      assertFalse(checkboxMessages.hasNext());
      
      Iterator<FacesMessage> fooTextMessages = server.getFacesMessages("input_foo_text");
      FacesMessage message = fooTextMessages.next();
      assertTrue(message.getDetail().contains("input_foo_text"));
   }
   
}
