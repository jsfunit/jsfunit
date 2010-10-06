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

import com.gargoylesoftware.htmlunit.BrowserVersion;
import java.io.File;
import java.io.IOException;
import javax.faces.component.UIComponent;
import junit.framework.Assert;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.spi.event.suite.Before;
import org.jboss.jsfunit.framework.WebClientSpec;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.jboss.shrinkwrap.api.ShrinkWrap;
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
public class FacadeAPITest
{

   private JSFClientSession client;
   private JSFServerSession server;

   @Deployment
   public static WebArchive createDeployment() {
      WebArchive war =
         ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(
                  CheckboxBean.class,
                  Marathon.class,
                  Marathons.class)
            .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"))
            .addResource(new File("src/main/webapp", "index.jsp"))
            .addWebResource(new File("src/main/webapp/WEB-INF/faces-config.xml"), "faces-config.xml");

      // Uncomment to print the archive for debugging
      // war.as(ExplodedExporter.class).exportExploded(new File("exploded"));
      // System.out.println(war.toString(true));

      return war;
   }

   /**
    * Start a JSFUnit session by getting the /index.faces page.  Note that
    * because setUp() is called before each test, a new HttpSession will be
    * created each time a test is run.
    */
 /*  @Before <-- this isn't working
   public void setUp() throws IOException
   {
      // Initial JSF request
      JSFSession jsfSession = new JSFSession("/index.faces");
      this.client = jsfSession.getJSFClientSession();
      this.server = jsfSession.getJSFServerSession();
   } */

   @Test
   public void testCustomBrowserVersion() throws IOException
   {
      WebClientSpec wcSpec = new WebClientSpec("/index.faces", BrowserVersion.INTERNET_EXPLORER_7);
      JSFSession jsfSession = new JSFSession(wcSpec);
      Assert.assertEquals(BrowserVersion.INTERNET_EXPLORER_7, jsfSession.getWebClient().getBrowserVersion());
   }

   /**
    */
   @Test
   public void testGetCurrentViewId() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/index.faces");
      JSFServerSession server = jsfSession.getJSFServerSession();

      // Test navigation to initial viewID
      Assert.assertEquals("/index.jsp", server.getCurrentViewID());
      Assert.assertEquals(server.getCurrentViewID(), server.getFacesContext().getViewRoot().getViewId());
   }

   @Test
   public void testSetParamAndSubmit() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/index.faces");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();

      client.setValue("input_foo_text", "Stan");
      client.click("submit_button");

      UIComponent greeting = server.findComponent("greeting");
      Assert.assertTrue(greeting.isRendered());
   }

}
