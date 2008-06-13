/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.framework.BasicAuthenticationStrategy;
import org.jboss.jsfunit.framework.WebClientSpec;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;

/**
 * Test the BasicAuthenticationStrategy.
 * 
 * @author Stan Silvert
 */
public class BasicAuthenticationTest extends ServletTestCase
{
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      return new TestSuite( BasicAuthenticationTest.class );
   }
   
   public void testBasicAuth() throws IOException
   {
      WebClientSpec wcSpec = new WebClientSpec("/secured-page.faces");
      wcSpec.setInitialRequestStrategy(new BasicAuthenticationStrategy("admin", "password"));
      
      JSFSession jsfSession = new JSFSession(wcSpec);
      JSFServerSession server = jsfSession.getJSFServerSession();
      assertEquals("/secured-page.jsp", server.getCurrentViewID());
      JSFClientSession client = jsfSession.getJSFClientSession();
      assertTrue(client.getPageAsText().contains("Welcome to the Basic Secured Application Page"));
   }
   
   public void testInvalidLogin() throws IOException
   {
      WebClientSpec wcSpec = new WebClientSpec("/secured-page.faces");
      wcSpec.getWebClient().setPrintContentOnFailingStatusCode(false);
      wcSpec.setInitialRequestStrategy(new BasicAuthenticationStrategy("invaliduser", "invalidpassword"));
      
      try
      {
         new JSFSession(wcSpec);
         fail();
      }
      catch (FailingHttpStatusCodeException e)
      {
         // Should get 401 Unauthorized
         assertEquals(401, e.getStatusCode());
      }
      
   }
   
}
