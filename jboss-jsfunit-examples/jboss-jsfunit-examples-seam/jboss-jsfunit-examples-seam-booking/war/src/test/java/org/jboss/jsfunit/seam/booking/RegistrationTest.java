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

package org.jboss.jsfunit.seam.booking;

import java.io.IOException;
import javax.faces.application.FacesMessage;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;

/**
 * 
 * 
 * @author Stan Silvert
 */
public class RegistrationTest extends ServletTestCase
{
   
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      return new TestSuite( RegistrationTest.class );
   }
   
   /**
    */
   public void testGoToRegisterPage() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/home.seam");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      client.click("register");
      assertEquals("/register.xhtml", server.getCurrentViewID());
   }
   
   public void testRegisterNewUser() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/home.seam");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      client.click("register");
      String username = new Long(System.currentTimeMillis()).toString();
      client.setValue("username", username); // unique name
      client.setValue(":name", "Stan Silvert");
      client.setValue("password", "foobar");
      client.setValue("verify", "foobar");
      client.click("register");
      
      assertEquals("/home.xhtml", server.getCurrentViewID());
   }
   
   public void testLogin() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/home.seam");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      RegisterBot.registerUser("LoginTestUser", "password");
      RegisterBot.login(client, "LoginTestUser", "password");
      FacesMessage message = (FacesMessage)server.getFacesMessages().next();
      assertTrue(message.getDetail().contains("Welcome, LoginTestUser"));
      assertEquals("/main.xhtml", server.getCurrentViewID());
   }
   
   public void testRegisterAndLogin() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/home.seam");
      JSFServerSession server = jsfSession.getJSFServerSession();
      FacesMessage message = (FacesMessage)server.getFacesMessages().next();
      assertTrue(message.getDetail().contains("Welcome, RegstrLoginUser"));
      assertEquals("/main.xhtml", server.getCurrentViewID());
   }
   
}
