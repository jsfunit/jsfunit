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

package org.jboss.jsfunit.seam.registration;

import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.xml.sax.SAXException;

/**
 * Simple JSFUnit tests for the Seam Registration Example
 * 
 * @author Stan Silvert
 */
public class RegistrationTest extends ServletTestCase
{
   private JSFClientSession client;
   private JSFServerSession server;
   
   /**
    * Start a JSFUnit session by getting the /index.faces page.  Note that
    * because setUp() is called before each test, a new HttpSession will be
    * created each time a test is run.
    */
   public void setUp() throws IOException, SAXException
   {
      JSFSession jsfSession = new JSFSession("/register.seam");
      this.client = jsfSession.getJSFClientSession();
      this.server = jsfSession.getJSFServerSession();
   }
   
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      return new TestSuite( RegistrationTest.class );
   }
   
   /**
    */
   public void testGetCurrentViewId() throws IOException, SAXException
   {
      // Test navigation to initial viewID
      assertEquals("/register.xhtml", server.getCurrentViewID());
   }
   
   public void testValidation() throws IOException, SAXException
   {
      client.setValue("username", "a");
      client.setValue("realname", "a");
      client.setValue("password", "a");
      
      // inputs too short - validation error(s)
      assert(server.getFacesMessages().hasNext());
   }
   
   public void testGoToRegisteredPage() throws IOException, SAXException
   {
      client.setValue("username", "Mickey");
      client.setValue("realname", "Mickey Mouse");
      client.setValue("password", "cheesebread");
      
      client.click("submitbutton");
      
      assertEquals("/registered.xhtml", server.getCurrentViewID());
      assertTrue(client.getPageAsText().contains("Mickey"));
   }
}
