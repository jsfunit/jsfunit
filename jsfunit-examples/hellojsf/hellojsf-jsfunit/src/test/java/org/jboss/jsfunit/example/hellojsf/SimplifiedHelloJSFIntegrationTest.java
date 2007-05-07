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

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.SubmitButton;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.facade.ClientFacade;
import org.jboss.jsfunit.facade.ServerFacade;
import org.jboss.jsfunit.framework.FacesContextBridge;
import org.jboss.jsfunit.framework.WebConversationFactory;
import org.xml.sax.SAXException;

/**
 * This is a simplified version of the HelloJSFIntegrationTest.  Instead of using
 * HttpUnit and JSF API's, it uses JSFUnit's ClientFacade and ServerFacade classes
 * to do most of the work.
 *
 * JSFUnit is designed to allow complete integration testing and debugging of
 * JSF applications at the JSF level.  In short, it gives you
 * access to the state of the FacesContext and managed beans after every
 * request.  With the FacesContext in hand, you are able to do integration testing
 * of JSF applications at the proper level of abstraction.
 *
 * The typical usage pattern of JSFUnit is to submit a request with httpunit and then
 * examine both the raw HTML output (httpunit tests) and JSF internals (JSFUnit tests).
 * The httpunit-style tests are not shown here.
 *
 * The JSFUnit tests below demonstrate testing:
 * - Navigation:      "Did this input take me to the correct view?"
 * - View Components: "Does the JSF component tree contain the correct components?"
 *                    "Do these components have the expected state?"
 * - Managed Beans:   "What is the state of my managed beans?"  This can include managed beans in
 *                    Seam-defined scopes such as conversation scope.  The tests below only
 *                    demonstrate request scope, but you can find anything reachable with the EL.
 * - Validation:      "Does invalid input generate the proper FacesMessage and error to the user."
 *
 * Tests not shown below:
 * - EL Expressions:     You use the Expression Language (EL) to examine the state of your
 *                       managed beans.  JSFUnit allows you to invoke any EL expression.
 *                       So, it is also possible to use the EL to invoke actions
 *                       from JSFUnit without submitting a request.  This is not deomonstrated
 *                       below, but it is relatively easy to do.
 * - Application Config: Because you have access to FacesContext, you can also call
 *                       FacesContext.getApplication().  From there you can test to make sure
 *                       your application configuration is correct.  This would include tests
 *                       for proper installation of converters, validators, component types,
 *                       locales, and resource bundles.
 *
 * This class tests the HelloJSF application.  This is a simple Hello World
 * application written in JSF with a single managed bean bound to the name "foo" in request scope.
 *
 * @author Stan Silvert
 */
public class SimplifiedHelloJSFIntegrationTest extends ServletTestCase
{
   private ClientFacade client;
   
   /**
    * Start a JSFUnit session by getting the /index.faces page.
    */
   public void setUp() throws IOException, SAXException
   {
      // Initial JSF request
      this.client = new ClientFacade("/index.faces");
   }
   
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      return new TestSuite( SimplifiedHelloJSFIntegrationTest.class );
   }
   
   /**
    * The initial page was called up in the setUp() method.  This shows
    * some simple JSFUnit tests you can do on that page.
    */
   public void testInitialPage() throws IOException, SAXException
   {
      ServerFacade server = new ServerFacade("form1");

      // Test navigation to initial viewID
      assertEquals("/index.jsp", server.getCurrentViewId());

      // Assert that the prompt component is in the component tree and rendered
      UIComponent prompt = server.findComponent("prompt");
      assertTrue(prompt.isRendered());

      // Assert that the greeting component is in the component tree but not rendered
      UIComponent greeting = server.findComponent("greeting");
      assertFalse(greeting.isRendered());
   }
   
   
   public void testInputValidation() throws IOException, SAXException
   {
      client.setParameter("input_foo_text", "A"); // input too short - validation error
      client.submit("submit_button");

      ServerFacade server = new ServerFacade();
      
      // Test that I was returned to the initial view because of input error
      assertEquals("/index.jsp", server.getCurrentViewId());
      
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
      client.setParameter("input_foo_text", "Stan");
      client.submit("submit_button");

      ServerFacade server = new ServerFacade("form1");
      
      // test the greeting component
      UIComponent greeting = server.findComponent("greeting");
      assertTrue(greeting.isRendered());
      assertEquals("Hello Stan", server.getComponentValue("greeting"));

      // Test the backing bean.  Since I am in-container, I can test any
      // managed bean in any scope - even Seam scopes such as Conversation.
      assertEquals("Stan", server.getManagedBeanValue("#{foo.text}"));
   }
   
   public void testGoodbyeButton() throws IOException, SAXException
   {
      testValidInput(); // put "Stan" into the input field
      client.submit("goodbye_button");

      // I'll be looking at a component outside the form. 
      // So set the NamingContainer to root ("")
      ServerFacade server = new ServerFacade("");
      
      // Test navigation to a new view
      assertEquals("/finalgreeting.jsp", server.getCurrentViewId());

      // Test the greeting
      assertEquals("Bye Stan. I enjoyed our chat.", server.getComponentValue("finalgreeting"));
   }
   
}
