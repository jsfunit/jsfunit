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

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This class tests invoking the test using the Cactus ServletTestRunner as
 * if it were a manual invocation from a browser.
 *
 * @author Stan Silvert
 */
public class ManualInvokeTest extends TestCase
{
   private String contextURL;
   
   public void setUp() throws IOException
   {
      String url = System.getProperty("cactus.contextURL");
      if (url != null)
      {
         this.contextURL = url;
         return;
      }
      
      this.contextURL = getContextURLFromCactusDotProperties();
   }
   
   private String getContextURLFromCactusDotProperties() throws IOException
   {
   
      InputStream in = null;
      try
      {
         in = ManualInvokeTest.class.getResourceAsStream("/cactus.properties");
         Properties props = new Properties();
         props.load(in);
         return props.getProperty("cactus.contextURL");
      }
      finally
      {
         try 
         {
            in.close();
         } 
         catch (Exception e)
         {
            // do nothing
         }
      }
   }
   
   public static Test suite()
   {
      return new TestSuite(ManualInvokeTest.class);
   }  

   /**
    * This test simulates invoking the JSFUnit tests manually through a browser.
    */
   public void testInvokeWithServletTestRunner() throws Exception
   {
      WebClient webClient = new WebClient();
      Page page = webClient.getPage(contextURL + "/ServletTestRunner?suite=org.jboss.jsfunit.jsfsession.hellojsf.HelloJSFIntegrationTest");
      String text = ((XmlPage)page).asXml();
      assertTrue(text.contains("errors=\"0\"") 
                 && text.contains("tests=\"5\"") 
                 && text.contains("failures=\"0\"") 
                 && text.contains("name=\"org.jboss.jsfunit.jsfsession.hellojsf.HelloJSFIntegrationTest\""));
   } 
   
   /**
    * This tests passing params from the redirector to the JSFUnit test.
    * 
    * @throws java.lang.Exception
    */
   public void testPassServletRedirectorParams() throws Exception
   {
      WebClient webClient = new WebClient();
      Page page = webClient.getPage(contextURL + "/ServletTestRunner?suite=org.jboss.jsfunit.jsfsession.hellojsf.PassServletRedirectParamsTest&foo=bar");
      String text = ((XmlPage)page).asXml();
      assertTrue(text.contains("errors=\"0\"") 
                 && text.contains("tests=\"1\"") 
                 && text.contains("failures=\"0\"") 
                 && text.contains("name=\"org.jboss.jsfunit.jsfsession.hellojsf.PassServletRedirectParamsTest\""));
   }
   
}
