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

package org.jboss.jsfunit.example.hellojsf;

import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.facade.ComponentTypeException;
import org.jboss.jsfunit.facade.JSFClientSession;
import org.xml.sax.SAXException;

/**
 * This class tests for exceptions that should be thrown due to improper
 * usage of the API.
 * 
 * @author Stan Silvert
 */
public class FacadeAPIUsageTest extends ServletTestCase
{
   private JSFClientSession client;
   
   /**
    * Start a JSFUnit session by getting the /index.faces page.  Note that
    * because setUp() is called before each test, a new HttpSession will be
    * created each time a test is run.
    */
   public void setUp() throws IOException, SAXException
   {
      this.client = new JSFClientSession("/index.faces");
   }
   
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      return new TestSuite( FacadeAPIUsageTest.class );
   }
   
   public void testBadURLInCtor() throws IOException, SAXException
   {
      // doesn't run on JSF 1.2_09 - turn off this test for JBoss
      // this test will be removed after Beta 3 anyway
      if (System.getProperty("jboss.server.home.dir") != null) return;
      
      try
      {
         this.client = new JSFClientSession("/index.jsp");
         fail("Expected IllegalArgumentException");
      }
      catch (IllegalArgumentException e)
      {
         assertTrue(e.getMessage().contains("FacesServlet"));
      }
      
      try
      {
         this.client = new JSFClientSession("/foo.bar");
         fail("Expected IllegalArgumentException");
      }
      catch (IllegalArgumentException e)
      {
         assertTrue(e.getMessage().contains("FacesServlet"));
      }
   }
   
   // bad clickCommandLink call
   public void testClickCommandLinkUsage() throws IOException, SAXException
   {
      try
      {
         client.clickCommandLink("submit_button");
         fail("Expected ComponentTypeException");
      }
      catch (ComponentTypeException e)
      {
         assertTrue(e.getMessage().contains("clickCommandLink("));
      }
   }
   
   // bad clickLink call
   public void testClickLinkUsage() throws IOException, SAXException
   {
      try
      {
         client.clickLink("submit_button");
         fail("Expected ComponentTypeException");
      }
      catch (ComponentTypeException e)
      {
         assertTrue(e.getMessage().contains("clickLink("));
      }
   }
   
   // bad submit(clientID) call
   public void testSubmit() throws IOException, SAXException
   {
      try
      {
         client.submit("prompt");
         fail("Expected ComponentTypeException");
      }
      catch (ComponentTypeException e)
      {
         assertTrue(e.getMessage().contains("submit("));
      }
   }
   
   
}
