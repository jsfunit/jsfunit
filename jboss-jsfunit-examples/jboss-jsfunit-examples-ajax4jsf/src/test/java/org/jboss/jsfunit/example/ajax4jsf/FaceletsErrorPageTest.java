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

package org.jboss.jsfunit.example.ajax4jsf;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebResponse;
import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.framework.JSFUnitWebConnection;
import org.jboss.jsfunit.framework.RequestListener;
import org.jboss.jsfunit.framework.WebClientSpec;
import org.jboss.jsfunit.framework.FaceletsErrorPageException;
import org.jboss.jsfunit.jsfsession.JSFSession;

/**
 * If in facelets development mode, facelets will return an HTML error page
 * instead of throwing an error.
 *
 * @author Stan Silvert
 */
public class FaceletsErrorPageTest extends ServletTestCase implements RequestListener
{
   
   private WebResponse latestResponse;

   @Override
   protected void tearDown() throws Exception {
      this.latestResponse = null;
      super.tearDown();
   }
   
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      return new TestSuite( FaceletsErrorPageTest.class );
   }

   /**
    * If in dev mode, facelets brings up an error page if JSF throws an error.
    */
   public void testErrorPageDetection() throws IOException
   {
      try 
      {
         JSFSession jsfSession = new JSFSession("/badIndex.jsf");
         fail("Expeted FaceletsErrorPageException");
      } 
      catch (FaceletsErrorPageException e)
      {
         // OK
      }
      
   } 
   
   public void testIsNotFaceletsErrorPage() throws IOException
   {
      WebClientSpec wcSpec = new WebClientSpec("/index.jsf");
      WebClient webClient = wcSpec.getWebClient();
      JSFUnitWebConnection webConnection = (JSFUnitWebConnection)webClient.getWebConnection();
      webConnection.addListener(this);
      new JSFSession(wcSpec);

      assertNotNull(this.latestResponse);
      assertFalse(FaceletsErrorPageException.isFaceletsErrorPage(this.latestResponse));
   }
   
   public void testIsFaceletsErrorPage() throws IOException
   {
      WebClientSpec wcSpec = new WebClientSpec("/badIndex.jsf");
      WebClient webClient = wcSpec.getWebClient();
      JSFUnitWebConnection webConnection = (JSFUnitWebConnection)webClient.getWebConnection();
      webConnection.addListener(this);
      
      try
      {
         new JSFSession(wcSpec);
      } 
      catch (FaceletsErrorPageException e)
      {
         // ignore
      }

      assertNotNull(this.latestResponse);
      assertTrue(FaceletsErrorPageException.isFaceletsErrorPage(this.latestResponse));
   }

   // ----------- Implement RequestListener -------------------------
   public void afterRequest(WebResponse webResponse) {
      this.latestResponse = webResponse;
   }

   public void beforeRequest(WebRequestSettings webRequestSettings) {
   }
}
