/*
 * JBoss, Home of Professional Open Source.
 * Copyright 20010, Red Hat Middleware LLC, and individual contributors
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

import java.io.File;
import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import junit.framework.Assert;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.jsfunit.cdi.Cookies;
import org.jboss.jsfunit.cdi.InitialPage;
import org.jboss.jsfunit.cdi.InitialRequest;
import org.jboss.jsfunit.cdi.Browser;
import org.jboss.jsfunit.cdi.BrowserVersion;
import org.jboss.jsfunit.cdi.Proxy;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
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
            .addPackage(Package.getPackage("org.jboss.jsfunit.cdi")) // not needed when Arquillian updated?
            .addResource(new File("src/main/webapp", "index.jsp"))
            .addResource(new File("src/main/webapp", "secured-page.jsp"))
            .addWebResource(new File("src/main/webapp/WEB-INF/faces-config.xml"), "faces-config.xml")
            .addWebResource(new File("src/main/webapp/WEB-INF/jboss-web.xml"), "jboss-web.xml")
            .addWebResource(new File("src/main/webapp/WEB-INF/classes/users.properties"), "classes/users.properties")
            .addWebResource(new File("src/main/webapp/WEB-INF/classes/roles.properties"), "classes/roles.properties")
            .addWebResource(EmptyAsset.INSTANCE, "beans.xml")
            .addManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));

      // Uncomment to print the archive for debugging
      // war.as(ExplodedExporter.class).exportExploded(new File("exploded"));
      // System.out.println(war.toString(true));

      return war;
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
   public void testGetCurrentViewId(JSFServerSession server)
   {
      // Test navigation to initial viewID
      Assert.assertEquals("/index.jsp", server.getCurrentViewID());
      Assert.assertEquals(server.getCurrentViewID(), server.getFacesContext().getViewRoot().getViewId());
   }

   @Test
   @InitialPage("/index.faces")
   @InitialRequest(SetSocketTimeoutRequestStrategy.class)
   public void testInitialRequestAnnotation(JSFSession jsfSession, JSFServerSession server)
   {
      // Test navigation to initial viewID
      Assert.assertEquals("/index.jsp", server.getCurrentViewID());
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

}
