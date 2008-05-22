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

import com.gargoylesoftware.htmlunit.WebClient;
import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.framework.WebConversationFactory;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;

/**
 * This class tests a JSFClientSession that uses a custom WebConversation.
 * 
 * 
 * @author Stan Silvert
 */
public class CustomWebClientTest extends ServletTestCase
{
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      return new TestSuite( CustomWebClientTest.class );
   }
   
   public void testCustomizedWebConversation() throws IOException
   {
      WebClient webClient = WebConversationFactory.makeWebClient();
      webClient.addRequestHeader("mycoolheader", "mycoolvalue");
      JSFSession jsfSession = new JSFSession(webClient, "/index.faces");
      JSFServerSession server = jsfSession.getJSFServerSession();
      Object headerValue = server.getFacesContext()
                                 .getExternalContext()
                                 .getRequestHeaderValuesMap()
                                 .get("mycoolheader");
      assertEquals("mycoolvalue", ((String[])headerValue)[0]);
   }
   
   /*
   These two tests can't be fixed until HtmlUnit fixes 
    https://sourceforge.net/tracker/?func=detail&atid=448266&aid=1965338&group_id=47038
    
   public void testNullWebConversation() throws IOException
   {
      try
      {
         JSFSession client = new JSFSession(null, "/index.faces");
         fail("Expected IllegalArgumentException");
      }
      catch (IllegalArgumentException e)
      {
         // OK
      }
   }
   
   // WebConversation must come from the WebConversationFactory
   public void testInvalidWebConversation() throws IOException
   {
      try
      {
         WebConversation webConv = new WebConversation();
         JSFClientSession client = new JSFClientSession(webConv, "/index.faces");
         fail("Expected IllegalArgumentException");
      }
      catch (IllegalArgumentException e)
      {
         // OK
      }
   }
   */
   
   // getWebConversation breaks encapsulation.  Is that bad for this kind
   // of Facade?
   public void testGetWebClient() throws IOException
   {
      WebClient webClient = WebConversationFactory.makeWebClient();
      
      JSFSession jsfSession = new JSFSession(webClient, "/index.faces");
      JSFClientSession client = jsfSession.getJSFClientSession();
      
      WebClient webClientFromJSFSession = jsfSession.getWebClient();
      assertEquals(webClient, webClientFromJSFSession);
      
      webClientFromJSFSession.addRequestHeader("mycoolheader", "mycoolvalue");
      client.click("submit_button");
      
      JSFServerSession server = jsfSession.getJSFServerSession();
      Object headerValue = server.getFacesContext()
                                 .getExternalContext()
                                 .getRequestHeaderValuesMap()
                                 .get("mycoolheader");
      assertEquals("mycoolvalue", ((String[])headerValue)[0]);
   }
   
}
