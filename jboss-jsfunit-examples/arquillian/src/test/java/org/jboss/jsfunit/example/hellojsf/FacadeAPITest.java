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
import junit.framework.Assert;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.jsfunit.cdi.InitialPage;
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
            .addWebResource(new File("src/main/webapp/WEB-INF/faces-config.xml"), "faces-config.xml")
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
   public void testGetCurrentViewId(JSFServerSession server) throws IOException
   {
      // Test navigation to initial viewID
      Assert.assertEquals("/index.jsp", server.getCurrentViewID());
      Assert.assertEquals(server.getCurrentViewID(), server.getFacesContext().getViewRoot().getViewId());
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

}
