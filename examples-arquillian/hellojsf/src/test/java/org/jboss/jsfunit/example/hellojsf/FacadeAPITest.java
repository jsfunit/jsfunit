/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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

import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlSelectManyListbox;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import junit.framework.Assert;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.jsfunit.cdi.Cookies;
import org.jboss.jsfunit.cdi.InitialPage;
import org.jboss.jsfunit.cdi.InitialRequest;
import org.jboss.jsfunit.cdi.Browser;
import org.jboss.jsfunit.cdi.BrowserVersion;
import org.jboss.jsfunit.cdi.Proxy;
import org.jboss.jsfunit.framework.Environment;
import org.jboss.jsfunit.jsfsession.ComponentIDNotFoundException;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.jboss.jsfunit.shrinkwrap.MavenArtifactResolver;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.exporter.ExplodedExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Version of the FacadeAPITest that uses Arquillian
 * 
 * @author Stan Silvert
 */
@RunWith(Arquillian.class)
@InitialPage("/index.faces")
public class FacadeAPITest
{

   private @Inject JSFClientSession client;
   private @Inject JSFServerSession server;

   @Deployment
   public static WebArchive createDeployment() {
      WebArchive war =
         ShrinkWrap.create(WebArchive.class, "test.war")
            .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"))
            .addPackage(Package.getPackage("org.jboss.jsfunit.example.hellojsf"))
   /*         .addClasses(org.slf4j.impl.Log4jLoggerAdapter.class,
                        org.slf4j.impl.Log4jLoggerFactory.class,
                        org.slf4j.impl.Log4jMDCAdapter.class,
                        org.slf4j.impl.StaticLoggerBinder.class,
                        org.slf4j.impl.StaticMarkerBinder.class,
                        org.slf4j.impl.StaticMDCBinder.class)  */

            .addResource(new File("src/main/webapp", "index.xhtml"))
            .addResource(new File("src/main/webapp", "finalgreeting.xhtml"))
            .addResource(new File("src/main/webapp", "secured-page.xhtml"))
            .addResource(new File("src/main/webapp", "NestedNamingContainers.xhtml"))
            .addResource(new File("src/main/webapp", "indexWithExtraComponents.xhtml"))
            .addResource(new File("src/main/webapp", "marathons.xhtml"))
            .addResource(new File("src/main/webapp", "marathons_datatable.xhtml"))
            .addWebResource(new File("src/main/webapp/WEB-INF/faces-config.xml"), "faces-config.xml")
            .addWebResource(new File("src/main/webapp/WEB-INF/local-module-faces-config.xml"), "local-module-faces-config.xml")
            .addWebResource(new File("src/main/webapp/WEB-INF/jboss-web.xml"), "jboss-web.xml")
            .addWebResource(new File("src/main/webapp/WEB-INF/classes/users.properties"), "classes/users.properties")
            .addWebResource(new File("src/main/webapp/WEB-INF/classes/roles.properties"), "classes/roles.properties")
            .addWebResource(EmptyAsset.INSTANCE, "beans.xml")
            .addManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));

      prepareForJetty(war);

      System.out.println(war.toString(true));
      // Uncomment to print the archive for debugging
      //  war.as(ExplodedExporter.class).exportExploded(new File("exploded"));
      //  System.out.println(war.toString(true));
      return war;
   }

   private static void prepareForJetty(WebArchive war)
   {  // property surefire sys prop setting
      if (System.getProperty("jetty-embedded") == null) return;

      System.out.println("*********************");
      System.out.println("Adding jetty stuff ");
      System.out.println("*********************");

      war.setWebXML(new File("src/main/jetty/web.xml"))

      /* Never quite got this working.
       * See http://community.jboss.org/wiki/HowdoIaddJARfilestothetestarchive
       * for MavenArtifactResolver */
       .addWebResource(new File("src/main/jetty/jetty-env.xml"), "jetty-env.xml")
       .addLibraries(MavenArtifactResolver.resolveQualifiedIds(
           "com.sun.faces:jsf-api:2.0.4-b03",
            "com.sun.faces:jsf-impl:2.0.4-b03",
            "org.jboss.weld.servlet:weld-servlet:1.1.0.Final",
            "org.glassfish.web:el-impl:2.2",
            "javax.annotation:jsr250-api:1.0",
            "javax.servlet:jstl:1.2")); //,
           // "org.eclipse.jetty:jetty-plus:7.0.2.v20100331")); //,
            //"org.eclipse.jetty:jetty-jndi:7.0.2.v20100331"));
   }

   @Test
   @InitialPage("/index.faces")
   @BrowserVersion(Browser.INTERNET_EXPLORER_6)
   @Proxy(host = "localhost", port = 8080)
   public void testCustomBrowserVersion(JSFSession jsfSession) throws IOException
   {
      Assert.assertEquals(com.gargoylesoftware.htmlunit.BrowserVersion.INTERNET_EXPLORER_6,
                          jsfSession.getWebClient().getBrowserVersion());
      Assert.assertEquals("localhost", jsfSession.getWebClient().getProxyConfig().getProxyHost());
      Assert.assertEquals(8080, jsfSession.getWebClient().getProxyConfig().getProxyPort());
   }

   /**
    */
   @Test
   @InitialPage("/index.faces")
   public void testGetCurrentViewId(JSFServerSession server) throws IOException
   {
      // Test navigation to initial viewID
      Assert.assertEquals("/index.xhtml", server.getCurrentViewID());
      Assert.assertEquals(server.getCurrentViewID(), server.getFacesContext().getViewRoot().getViewId());
   }

   @Test
   @InitialPage("/index.faces")
   @InitialRequest(SetSocketTimeoutRequestStrategy.class)
   public void testInitialRequestAnnotation(JSFSession jsfSession, JSFServerSession server)
   {
      // Test navigation to initial viewID
      Assert.assertEquals("/index.xhtml", server.getCurrentViewID());
      Assert.assertEquals(server.getCurrentViewID(), server.getFacesContext().getViewRoot().getViewId());

      // timeout set to 10001 in SetSocketTimeoutRequestStrategy
      Assert.assertEquals(10001, jsfSession.getWebClient().getTimeout());
   }

   @Test
   public void testSetParamAndSubmit() throws IOException
   {
      client.setValue("input_foo_text", "Stan");
      client.click("submit_button");

      UIComponent greeting = server.findComponent("greeting");
      Assert.assertTrue(greeting.isRendered());

      // test CDI bean
      Assert.assertTrue(client.getPageAsText().contains("Hello Stan"));
      Assert.assertEquals("Hello", server.getManagedBeanValue("#{mybean.hello}"));
   }

   @Test
   @InitialPage("/index.faces")
   @Cookies(names = {"cookie1", "cookie2"},
            values = {"value1", "value2"})
   public void testCustomCookies(JSFClientSession client, JSFServerSession server) throws IOException
   {
      Assert.assertEquals("value1", getCookieValue(server, "cookie1"));
      Assert.assertEquals("value2", getCookieValue(server, "cookie2"));

      // verify that cookies survive for the whole session
      client.click("submit_button");
      Assert.assertEquals("value1", getCookieValue(server, "cookie1"));
      Assert.assertEquals("value2", getCookieValue(server, "cookie2"));
   }

   private String getCookieValue(JSFServerSession server, String cookieName)
   {
      Object cookie = server.getFacesContext()
                            .getExternalContext()
                            .getRequestCookieMap()
                            .get(cookieName);
      if (cookie != null) return ((Cookie)cookie).getValue();
      return null;
   }

   @Test
   public void testSetCheckbox() throws IOException
   {
      client.setValue("input_foo_text", "Stan");
      client.click("funcheck"); // uncheck it
      client.click("submit_button");
      Assert.assertFalse((Boolean)server.getManagedBeanValue("#{checkbox.funCheck}"));

      client.click("funcheck"); // make it checked again
      client.click("submit_button");
      Assert.assertTrue((Boolean)server.getManagedBeanValue("#{checkbox.funCheck}"));
   }

   @Test
   public void testClickCommandLink() throws IOException
   {
      client.setValue("input_foo_text", "Stan");
      client.click("goodbye_button");
      client.click("go_back_link");

      // test that we are back on the first page
      Assert.assertEquals("/index.xhtml", server.getCurrentViewID());
   }

   @Test
   public void testCommandLinkWithoutViewChange() throws IOException
   {
      client.setValue("input_foo_text", "Stan");
      client.click("goodbye_button");
      client.click("stay_here_link");

      // test that we are still on the same page
      Assert.assertEquals("/finalgreeting.xhtml", server.getCurrentViewID());
   }

   @Test
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

      Assert.assertEquals("Stan", name);
   }

 /* Bug in Mojarra causes this to fail.
  * See http://java.net/jira/browse/JAVASERVERFACES-1527
  * SEVERE [javax.enterprise.resource.webcontainer.jsf.application] JSF1007: Duplicate component ID form1:marathonSelect found in view.
   @Test
   public void testCommandLinkWithParamFromLoopVariable() throws IOException
   {
      // test should not run for JSF 1.1 - it uses a loop variable from <c:forEach>
      if ((Environment.getJSFMajorVersion() == 1) &&
          (Environment.getJSFMinorVersion() < 2)) return;

      JSFSession jsfSession = new JSFSession("/marathons.faces");
      JSFClientSession client = jsfSession.getJSFClientSession();

      client.click("marathonSelect");
      Assert.assertTrue(client.getPageAsText().contains("Selected Marathon: BAA Boston Marathon"));

      client.click("marathonSelectj_id_3");
      Assert.assertTrue(client.getPageAsText().contains("Selected Marathon: Flora London Marathon"));

      client.click("marathonSelectj_id_5");
      Assert.assertTrue(client.getPageAsText().contains("Selected Marathon: Olympic Marathon"));
   } */

   @Test
   public void testCommandLinkWithParamFromDatatableVariable() throws IOException
   {

      JSFSession jsfSession = new JSFSession("/marathons_datatable.faces");
      JSFClientSession client = jsfSession.getJSFClientSession();

      client.click("0:marathonSelect");
      Assert.assertTrue(client.getPageAsText().contains("Selected Marathon: BAA Boston Marathon"));

      client.click("3:marathonSelect");
      Assert.assertTrue(client.getPageAsText().contains("Selected Marathon: Flora London Marathon"));

      client.click("5:marathonSelect");
      Assert.assertTrue(client.getPageAsText().contains("Selected Marathon: Olympic Marathon"));
   }

   @Test
   public void testInvalidateSession() throws IOException
   {

      JSFSession jsfSession = new JSFSession("/marathons_datatable.faces");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();

      client.click("0:marathonSelect");
      Assert.assertTrue(client.getPageAsText().contains("Selected Marathon: BAA Boston Marathon"));
      Assert.assertEquals("BAA Boston Marathon", server.getManagedBeanValue("#{marathons.selectedMarathon}"));

      client.click("invalidateSession");

      client.click("0:marathonSelect");
      Assert.assertTrue(client.getPageAsText().contains("Selected Marathon: BAA Boston Marathon"));
      Assert.assertEquals("BAA Boston Marathon", server.getManagedBeanValue("#{marathons.selectedMarathon}"));
   }

   @Test
   public void testServerSideComponentValue() throws IOException
   {
      testSetParamAndSubmit(); // put "Stan" into the input field

      // test the greeting component
      Assert.assertEquals("Hello Stan", server.getComponentValue("greeting"));
   }

   /**
    * This demonstrates how to test managed beans.
    */
   @Test
   public void testManagedBeanValue() throws IOException
   {
      testSetParamAndSubmit(); // put "Stan" into the input field

      Assert.assertEquals("Stan", server.getManagedBeanValue("#{foo.text}"));
   }

   @Test
   public void testFacesMessages() throws IOException
   {
      client.setValue("input_foo_text", "A"); // input too short - validation error
      client.click("submit_button");

      // Test that I was returned to the initial view because of input error
      Assert.assertEquals("/index.xhtml", server.getCurrentViewID());

      // Should be only one FacesMessge generated for the page.
      Iterator<FacesMessage> allMessages = server.getFacesMessages();
      allMessages.next();
      Assert.assertFalse(allMessages.hasNext());

      Iterator<FacesMessage> checkboxMessages = server.getFacesMessages("funcheck");
      Assert.assertFalse(checkboxMessages.hasNext());

      Iterator<FacesMessage> fooTextMessages = server.getFacesMessages("input_foo_text");
      FacesMessage message = fooTextMessages.next();
      Assert.assertTrue(message.getDetail().contains("input_foo_text"));
   }

   @Test
   @InitialPage("/indexWithExtraComponents.faces")
   public void testTextArea(JSFClientSession client, JSFServerSession server) throws IOException
   {
      client.setValue("input_foo_text", "Stan");
      Assert.assertEquals("Initial Value", server.getManagedBeanValue("#{foo2.text}"));
      client.setValue("MyTextArea", "New Value");
      client.click("submit_button");
      Assert.assertEquals("New Value", server.getManagedBeanValue("#{foo2.text}"));
   }

   @Test
   @InitialPage("/indexWithExtraComponents.faces")
   public void testSelectOneRadio(JSFClientSession client, JSFServerSession server) throws IOException
   {
      client.setValue("input_foo_text", "Stan");
      Assert.assertEquals("Blue", server.getManagedBeanValue("#{foo3.text}"));
      client.click("selectGreen");
      client.click("submit_button");
      Assert.assertEquals("Green", server.getManagedBeanValue("#{foo3.text}"));
   }

   @Test
   @InitialPage("/indexWithExtraComponents.faces")
   public void testSelectManyListbox(JSFClientSession client, JSFServerSession server) throws IOException
   {
      client.click("selectMonday");
      client.click("selectWednesday");
      client.click("selectFriday");
      client.click("submit_button");

      HtmlSelectManyListbox listBox = (HtmlSelectManyListbox)server.findComponent("Weekdays");
      Object[] selectedValues = listBox.getSelectedValues();
      Assert.assertEquals(3, selectedValues.length);
      List listOfValues = Arrays.asList(selectedValues);
      Assert.assertTrue(listOfValues.contains("Monday"));
      Assert.assertFalse(listOfValues.contains("Tuesday"));
      Assert.assertTrue(listOfValues.contains("Wednesday"));
      Assert.assertFalse(listOfValues.contains("Thursday"));
      Assert.assertTrue(listOfValues.contains("Friday"));
   }

   @Test
   @InitialPage("/indexWithExtraComponents.faces")
   public void testSelectManyListboxWithItemList(JSFClientSession client, JSFServerSession server) throws IOException
   {
      HtmlSelect select = (HtmlSelect)client.getElement("WeekdaysUsingItemList");
      select.getOptionByValue("Monday").setSelected(true);
      select.getOptionByValue("Tuesday").setSelected(false);
      select.getOptionByValue("Wednesday").setSelected(true);
      select.getOptionByValue("Thursday").setSelected(true);
      select.getOptionByValue("Friday").setSelected(false);
      client.click("submit_button");

      HtmlSelectManyListbox listBox = (HtmlSelectManyListbox)server.findComponent("WeekdaysUsingItemList");
      Object[] selectedValues = listBox.getSelectedValues();
      Assert.assertEquals(3, selectedValues.length);
      List listOfValues = Arrays.asList(selectedValues);
      Assert.assertTrue(listOfValues.contains("Monday"));
      Assert.assertFalse(listOfValues.contains("Tuesday"));
      Assert.assertTrue(listOfValues.contains("Wednesday"));
      Assert.assertTrue(listOfValues.contains("Thursday"));
      Assert.assertFalse(listOfValues.contains("Friday"));
   }

   @Test
   public void testNoCreationOfBeanDuringELExpressionReference() throws IOException
   {
      HttpSession session = (HttpSession)server.getFacesContext().getExternalContext().getSession(true);
      Assert.assertNull(session.getAttribute("unreferencedsessionbean"));

      MyBean bean = (MyBean)server.getManagedBeanValue("#{unreferencedsessionbean}");
      Assert.assertNull(bean);  //<--------- JSFUNIT-164

      bean = (MyBean)server.getManagedBeanValue("#{unreferencedrequestbean}");
      Assert.assertNull(bean);  //<--------- JSFUNIT-164

      bean = (MyBean)server.getManagedBeanValue("#{unreferencedapplicationbean}");
      Assert.assertNull(bean);  //<--------- JSFUNIT-164
   }

   @Test
   @InitialPage("/indexWithExtraComponents.faces")
   public void testReferencedBeans(JSFServerSession server, JSFClientSession client) throws IOException
   {
      String html = client.getPageAsText();
      Assert.assertTrue(html.contains("request bean scope string = request"));
      Assert.assertTrue(html.contains("session bean scope string = session"));
      Assert.assertTrue(html.contains("application bean scope string = application"));

      HttpSession session = (HttpSession)server.getFacesContext().getExternalContext().getSession(true);
      Assert.assertNotNull(session.getAttribute("referencedsessionbean"));

      MyBean bean = (MyBean)server.getManagedBeanValue("#{referencedsessionbean}");
      Assert.assertNotNull(bean);
      Assert.assertEquals(1, bean.myValue);

      bean = (MyBean)server.getManagedBeanValue("#{referencedrequestbean}");
      Assert.assertNotNull(bean);
      Assert.assertEquals(1, bean.myValue);

      bean = (MyBean)server.getManagedBeanValue("#{referencedapplicationbean}");
      Assert.assertNotNull(bean);
      Assert.assertEquals(1, bean.myValue);
   }

   @Test
   public void testClickThrowsComponentNotFound() throws IOException
   {
      try
      {
         client.click("thiselementisnotthere");
         Assert.fail("Expected ComponentIDNotFoundException");
      }
      catch (ComponentIDNotFoundException e)
      {
         // OK
      }
   }

   @Test
   public void testSetValueThrowsComponentNotFound() throws IOException
   {
      try
      {
         client.setValue("thiselementisnotthere", "bogusvalue");
         Assert.fail("Expected ComponentIDNotFoundException");
      }
      catch (ComponentIDNotFoundException e)
      {
         // OK
      }
   }

   @Test
   public void testTypeThrowsComponentNotFound() throws IOException
   {
      try
      {
         client.type("thiselementisnotthere", 'b');
         Assert.fail("Expected ComponentIDNotFoundException");
      }
      catch (ComponentIDNotFoundException e)
      {
         // OK
      }
   }
}
