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

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlSelectManyListbox;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.example.hellojsf.MyBean;
import org.jboss.jsfunit.framework.Environment;
import org.jboss.jsfunit.framework.WebClientSpec;
import org.jboss.jsfunit.jsfsession.ComponentIDNotFoundException;
import org.jboss.jsfunit.jsfsession.DuplicateClientIDException;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;

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
   private JSFServerSession server;
   
   /**
    * Start a JSFUnit session by getting the /index.faces page.  Note that
    * because setUp() is called before each test, a new HttpSession will be
    * created each time a test is run.
    */
   public void setUp() throws IOException
   {
      // Initial JSF request
      JSFSession jsfSession = new JSFSession("/index.faces");
      this.client = jsfSession.getJSFClientSession();
      this.server = jsfSession.getJSFServerSession();
   }
   
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      return new TestSuite( FacadeAPITest.class );
   }
   
   public void testCustomBrowserVersion() throws IOException
   {
      WebClientSpec wcSpec = new WebClientSpec("/index.faces", BrowserVersion.INTERNET_EXPLORER_7_0);
      JSFSession jsfSession = new JSFSession(wcSpec);
      assertEquals(BrowserVersion.INTERNET_EXPLORER_7_0, jsfSession.getWebClient().getBrowserVersion());
   }
   
   /**
    */
   public void testGetCurrentViewId() throws IOException
   {
      // Test navigation to initial viewID
      assertEquals("/index.jsp", server.getCurrentViewID());
      assertEquals(server.getCurrentViewID(), server.getFacesContext().getViewRoot().getViewId());
   }
   
   public void testSetParamAndSubmit() throws IOException
   {
      client.setValue("input_foo_text", "Stan"); 
      client.click("submit_button");
      
      UIComponent greeting = server.findComponent("greeting");
      assertTrue(greeting.isRendered());
   }
   
   public void testSetCheckbox() throws IOException
   {
      client.click("funcheck"); // uncheck it
      client.click("submit_button");
      assertFalse((Boolean)server.getManagedBeanValue("#{checkbox.funCheck}"));
      
      client.click("funcheck"); // make it checked again
      client.click("submit_button");
      assertTrue((Boolean)server.getManagedBeanValue("#{checkbox.funCheck}"));
   }
   
   public void testClickCommandLink() throws IOException
   {
      client.click("goodbye_button");
      client.click("go_back_link");
      
      // test that we are back on the first page
      assertEquals("/index.jsp", server.getCurrentViewID());
   }
   
   public void testCommandLinkWithoutViewChange() throws IOException
   {
      client.click("goodbye_button");
      client.click("stay_here_link");
      
      // test that we are still on the same page
      assertEquals("/finalgreeting.jsp", server.getCurrentViewID());
   }
   
   public void testCommandLinkWithFParam() throws IOException
   {
      client.setValue("input_foo_text", "Stan");
      client.click("goodbye_button");
      client.click("stay_here_link");
      
      // link includes <f:param id="name" name="name" value="#{foo.text}"/>
      String name = (String)FacesContext.getCurrentInstance()
                                        .getExternalContext()
                                        .getRequestParameterMap()
                                        .get("name");
      
      assertEquals("Stan", name);
   }

   public void testCommandLinkWithParamFromLoopVariable() throws IOException
   {
      // test should not run for JSF 1.1 - it uses a loop variable from <c:forEach>
      if ((Environment.getJSFMajorVersion() == 1) &&
          (Environment.getJSFMinorVersion() < 2)) return;
      
      JSFSession jsfSession = new JSFSession("/marathons.faces");
      JSFClientSession client = jsfSession.getJSFClientSession();
      
      client.click("marathonSelect");
      assertTrue(client.getPageAsText().contains("Selected Marathon: BAA Boston Marathon"));
      
      client.click("marathonSelectj_id_3");
      assertTrue(client.getPageAsText().contains("Selected Marathon: Flora London Marathon"));
      
      client.click("marathonSelectj_id_5");
      assertTrue(client.getPageAsText().contains("Selected Marathon: Olympic Marathon"));
   }
   
   public void testCommandLinkWithParamFromDatatableVariable() throws IOException
   {
      
      JSFSession jsfSession = new JSFSession("/marathons_datatable.faces");
      JSFClientSession client = jsfSession.getJSFClientSession();
      
      client.click("0:marathonSelect");
      assertTrue(client.getPageAsText().contains("Selected Marathon: BAA Boston Marathon"));
      
      client.click("3:marathonSelect");
      assertTrue(client.getPageAsText().contains("Selected Marathon: Flora London Marathon"));
      
      client.click("5:marathonSelect");
      assertTrue(client.getPageAsText().contains("Selected Marathon: Olympic Marathon"));
   }
   
   public void testInvalidateSession() throws IOException
   {
      
      JSFSession jsfSession = new JSFSession("/marathons_datatable.faces");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      client.click("0:marathonSelect");
      assertTrue(client.getPageAsText().contains("Selected Marathon: BAA Boston Marathon"));
      assertEquals("BAA Boston Marathon", server.getManagedBeanValue("#{marathons.selectedMarathon}"));
      
      client.click("invalidateSession");
      
      client.click("0:marathonSelect");
      assertTrue(client.getPageAsText().contains("Selected Marathon: BAA Boston Marathon"));
      assertEquals("BAA Boston Marathon", server.getManagedBeanValue("#{marathons.selectedMarathon}"));
   }
   
   public void testServerSideComponentValue() throws IOException
   {
      testSetParamAndSubmit(); // put "Stan" into the input field

      // test the greeting component
      assertEquals("Hello Stan", server.getComponentValue("greeting"));
   }
   
   /**
    * This demonstrates how to test managed beans.
    */
   public void testManagedBeanValue() throws IOException
   {
      testSetParamAndSubmit(); // put "Stan" into the input field

      assertEquals("Stan", server.getManagedBeanValue("#{foo.text}"));
   }
   
   public void testClickALink() throws IOException
   {
      client.click("SourceSimplifiedHelloJSFIntegrationTest");
      assertTrue(client.getPageAsText().contains("public class SimplifiedHelloJSFIntegrationTest"));
   }
   
   public void testFacesMessages() throws IOException
   {
      client.setValue("input_foo_text", "A"); // input too short - validation error
      client.click("submit_button");

      // Test that I was returned to the initial view because of input error
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
   
   public void testTextArea() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/indexWithExtraComponents.faces");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      assertEquals("Initial Value", server.getManagedBeanValue("#{foo2.text}"));
      client.setValue("MyTextArea", "New Value");
      client.click("submit_button");
      assertEquals("New Value", server.getManagedBeanValue("#{foo2.text}"));
   }
   
   public void testSelectOneRadio() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/indexWithExtraComponents.faces");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      assertEquals("Blue", server.getManagedBeanValue("#{foo3.text}"));
      client.click("selectGreen");
      client.click("submit_button");
      assertEquals("Green", server.getManagedBeanValue("#{foo3.text}"));
   }
   
   public void testSelectManyListbox() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/indexWithExtraComponents.faces");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      client.click("selectMonday");
      client.click("selectWednesday");
      client.click("selectFriday");
      client.click("submit_button");
      
      HtmlSelectManyListbox listBox = (HtmlSelectManyListbox)server.findComponent("Weekdays");
      Object[] selectedValues = listBox.getSelectedValues();
      assertEquals(3, selectedValues.length);
      List listOfValues = Arrays.asList(selectedValues);
      assertTrue(listOfValues.contains("Monday"));
      assertFalse(listOfValues.contains("Tuesday"));
      assertTrue(listOfValues.contains("Wednesday"));
      assertFalse(listOfValues.contains("Thursday"));
      assertTrue(listOfValues.contains("Friday"));
   }
   
   public void testSelectManyListboxWithItemList() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/indexWithExtraComponents.faces");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
    
      HtmlSelect select = (HtmlSelect)client.getElement("WeekdaysUsingItemList");
      select.getOptionByValue("Monday").setSelected(true);
      select.getOptionByValue("Tuesday").setSelected(false);
      select.getOptionByValue("Wednesday").setSelected(true);
      select.getOptionByValue("Thursday").setSelected(true);
      select.getOptionByValue("Friday").setSelected(false);
      client.click("submit_button");
      
      HtmlSelectManyListbox listBox = (HtmlSelectManyListbox)server.findComponent("WeekdaysUsingItemList");
      Object[] selectedValues = listBox.getSelectedValues();
      assertEquals(3, selectedValues.length);
      List listOfValues = Arrays.asList(selectedValues);
      assertTrue(listOfValues.contains("Monday"));
      assertFalse(listOfValues.contains("Tuesday"));
      assertTrue(listOfValues.contains("Wednesday"));
      assertTrue(listOfValues.contains("Thursday"));
      assertFalse(listOfValues.contains("Friday"));
   }
   
   public void testGetElementThrowsDuplicateIDException() throws IOException
   {
      try
      {
         client.getElement("Test");
         fail("Expected DuplicateClientIDException");
      } 
      catch (DuplicateClientIDException e)
      {
         // OK
      }
   }
   
   public void testNoCreationOfBeanDuringELExpressionReference() throws IOException
   {
      HttpSession session = (HttpSession)server.getFacesContext().getExternalContext().getSession(true);
      assertNull(session.getAttribute("unreferencedsessionbean"));
      
      MyBean bean = (MyBean)server.getManagedBeanValue("#{unreferencedsessionbean}");
      assertNull(bean);  //<--------- JSFUNIT-164
      
      bean = (MyBean)server.getManagedBeanValue("#{unreferencedrequestbean}");
      assertNull(bean);  //<--------- JSFUNIT-164
      
      bean = (MyBean)server.getManagedBeanValue("#{unreferencedapplicationbean}");
      assertNull(bean);  //<--------- JSFUNIT-164
   }
   
   public void testReferencedBeans() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/indexWithExtraComponents.faces");
      JSFServerSession server = jsfSession.getJSFServerSession();
      JSFClientSession client = jsfSession.getJSFClientSession();
      
      String html = client.getPageAsText();
      assertTrue(html.contains("request bean scope string = request"));
      assertTrue(html.contains("session bean scope string = session"));
      assertTrue(html.contains("application bean scope string = application"));
      
      HttpSession session = (HttpSession)server.getFacesContext().getExternalContext().getSession(true);
      assertNotNull(session.getAttribute("referencedsessionbean"));
      
      MyBean bean = (MyBean)server.getManagedBeanValue("#{referencedsessionbean}");
      assertNotNull(bean);
      assertEquals(1, bean.myValue);
      
      bean = (MyBean)server.getManagedBeanValue("#{referencedrequestbean}");
      assertNotNull(bean);
      assertEquals(1, bean.myValue);
      
      bean = (MyBean)server.getManagedBeanValue("#{referencedapplicationbean}");
      assertNotNull(bean);
      assertEquals(1, bean.myValue);
   }
   
   public void testClickThrowsComponentNotFound() throws IOException
   {
      try
      {
         client.click("thiselementisnotthere");
         fail("Expected ComponentIDNotFoundException");
      }
      catch (ComponentIDNotFoundException e)
      {
         // OK
      }
   }
   
   public void testSetValueThrowsComponentNotFound() throws IOException
   {
      try
      {
         client.setValue("thiselementisnotthere", "bogusvalue");
         fail("Expected ComponentIDNotFoundException");
      }
      catch (ComponentIDNotFoundException e)
      {
         // OK
      }
   }
   
   public void testTypeThrowsComponentNotFound() throws IOException
   {
      try
      {
         client.type("thiselementisnotthere", 'b');
         fail("Expected ComponentIDNotFoundException");
      }
      catch (ComponentIDNotFoundException e)
      {
         // OK
      }
   }
   
}
