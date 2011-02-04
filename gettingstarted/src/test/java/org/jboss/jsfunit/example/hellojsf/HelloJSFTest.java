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

import java.io.File;
import java.io.IOException;
import javax.faces.component.UIComponent;
import junit.framework.Assert;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.jsfunit.cdi.InitialPage;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.shrinkwrap.MavenArtifactResolver;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Version of the HelloJSFTest that uses Arquillian
 * 
 * @author Stan Silvert
 */
@RunWith(Arquillian.class)
public class HelloJSFTest
{
   // property surefire sys prop setting
   public static final boolean IS_JETTY = (System.getProperty("jetty-embedded") != null);

   @Deployment
   public static WebArchive createDeployment() {
      WebArchive war =
         ShrinkWrap.create(WebArchive.class, "test.war")
            .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"))
            .addPackage(Package.getPackage("org.jboss.jsfunit.example.hellojsf")) // my test package
            .addResource(new File("src/main/webapp", "index.xhtml"))
            .addWebResource(EmptyAsset.INSTANCE, "beans.xml")
            .addWebResource(new File("src/main/webapp/WEB-INF/faces-config.xml"), "faces-config.xml")
            .addManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));

      prepareForJetty(war);

//      System.out.println(war.toString(true)); // for debugging
      return war;
   }

   private static void prepareForJetty(WebArchive war)
   {  
      if (!IS_JETTY) return;
      
      war.setWebXML(new File("src/main/jetty/web.xml")) // jetty web.xml needs extra declarations
         .addWebResource(new File("src/main/jetty/jetty-env.xml"), "jetty-env.xml")
         .addLibraries(MavenArtifactResolver.resolveQualifiedIds(
            "com.sun.faces:jsf-api:2.0.4-b03",
            "com.sun.faces:jsf-impl:2.0.4-b03",                    
            "org.glassfish.web:el-impl:2.2",
            "javax.annotation:jsr250-api:1.0",
            "javax.servlet:jstl:1.2")); 
   }

   @Test
   @InitialPage("/index.faces")
   public void testInitialPage(JSFServerSession server, JSFClientSession client) throws IOException
   {
      // Test navigation to initial viewID
       Assert.assertEquals("/index.xhtml", server.getCurrentViewID());

       // Set the param and submit
       client.setValue("name", "Stan");
       client.click("submit_button");

       // Assert that the greeting component is in the component tree and rendered
       UIComponent greeting = server.findComponent("greeting");
       Assert.assertTrue(greeting.isRendered());

       // Test a managed bean using EL. We cheat and use the request object.
       Assert.assertEquals("Stan", server.getManagedBeanValue("#{request.getParameter('form1:name')}"));
   }

   
}
