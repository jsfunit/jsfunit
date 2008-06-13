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
import com.gargoylesoftware.htmlunit.WebClient;
import java.io.IOException;
import javax.servlet.http.Cookie;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.framework.WebClientSpec;
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
      WebClientSpec wcSpec = new WebClientSpec("/index.faces");
      WebClient webClient = wcSpec.getWebClient();
      assertNotNull(webClient);
      assertEquals(BrowserVersion.FIREFOX_2, webClient.getBrowserVersion());
      
      webClient.addRequestHeader("mycoolheader", "mycoolvalue");
      JSFSession jsfSession = new JSFSession(wcSpec);
      JSFServerSession server = jsfSession.getJSFServerSession();
      Object headerValue = server.getFacesContext()
                                 .getExternalContext()
                                 .getRequestHeaderValuesMap()
                                 .get("mycoolheader");
      assertEquals("mycoolvalue", ((String[])headerValue)[0]);
   }
   
   public void testGetWebClient() throws IOException
   {
      WebClientSpec wcSpec = new WebClientSpec("/index.faces");
      assertEquals("/index.faces", wcSpec.getInitialPage());
      
      JSFSession jsfSession = new JSFSession(wcSpec);
      JSFClientSession client = jsfSession.getJSFClientSession();
      
      WebClient webClientFromJSFSession = jsfSession.getWebClient();
      assertEquals(wcSpec.getWebClient(), webClientFromJSFSession);
      
      webClientFromJSFSession.addRequestHeader("mycoolheader", "mycoolvalue");
      client.click("submit_button");
      
      JSFServerSession server = jsfSession.getJSFServerSession();
      Object headerValue = server.getFacesContext()
                                 .getExternalContext()
                                 .getRequestHeaderValuesMap()
                                 .get("mycoolheader");
      assertEquals("mycoolvalue", ((String[])headerValue)[0]);
   }

   public void testGetProxyHostAndPort()
   {
      WebClientSpec wcSpec = new WebClientSpec("/index.faces", BrowserVersion.FIREFOX_2, "myhost", 333);
      assertEquals("myhost", wcSpec.getProxyHost());
      assertEquals(333, wcSpec.getProxyPort());
   }
   
   public void testCustomCookies() throws IOException
   {
      WebClientSpec wcSpec = new WebClientSpec("/index.faces");
      wcSpec.addCookie("mycookie", "mycookievalue");
      
      JSFSession jsfSession = new JSFSession(wcSpec);

      assertEquals("mycookievalue", getCookieValue(jsfSession, "mycookie"));
      
      wcSpec.removeCookie("mycookie");
      wcSpec.addCookie("secondcookie", "anothervalue");
      
      JSFClientSession client = jsfSession.getJSFClientSession();
      client.click("submit_button");
      assertNull(getCookieValue(jsfSession, "mycookie"));
      assertEquals("anothervalue", getCookieValue(jsfSession, "secondcookie"));
   }
   
   private String getCookieValue(JSFSession jsfSession, String cookieName)
   {
      JSFServerSession server = jsfSession.getJSFServerSession();
      Object cookie = server.getFacesContext()
                            .getExternalContext()
                            .getRequestCookieMap()
                            .get(cookieName);
      if (cookie != null) return ((Cookie)cookie).getValue();
      return null;
   }
}
