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

import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.framework.FormAuthenticationStrategy;
import org.jboss.jsfunit.framework.WebClientSpec;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;

/**
 * Test the FormAuthenticationStrategy.
 * 
 * @author Stan Silvert
 */
public class FormAuthenticationTest extends ServletTestCase
{
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      return new TestSuite( FormAuthenticationTest.class );
   }
   
   public void testFormAuth() throws IOException
   {
      WebClientSpec wcSpec = new WebClientSpec("/jsf/form-secured-page.jsp");
      FormAuthenticationStrategy formStrategy = new FormAuthenticationStrategy("user", "password");
      formStrategy.setSubmitComponent("login_button");
      wcSpec.setInitialRequestStrategy(formStrategy);
      
      JSFSession jsfSession = new JSFSession(wcSpec);
      JSFServerSession server = jsfSession.getJSFServerSession();
      assertEquals("/form-secured-page.jsp", server.getCurrentViewID());
      JSFClientSession client = jsfSession.getJSFClientSession();
      assertTrue(client.getPageAsText().contains("Welcome to the Form Secured Application Page"));
   }
   
   public void testInvalidLogin() throws IOException
   {
      WebClientSpec wcSpec = new WebClientSpec("/jsf/form-secured-page.jsp");
      FormAuthenticationStrategy formStrategy = new FormAuthenticationStrategy("invaliduser", "invalidpassword");
      formStrategy.setSubmitComponent("login_button");
      wcSpec.setInitialRequestStrategy(formStrategy);
      
      JSFSession jsfSession = new JSFSession(wcSpec);
      JSFClientSession client = jsfSession.getJSFClientSession();
      assertTrue(client.getPageAsText().contains("Error logging in"));
   }
   
}
