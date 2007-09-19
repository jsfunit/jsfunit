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

package org.jboss.jsfunit.facade;

import com.meterware.httpunit.WebForm;
import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.xml.sax.SAXException;

/**
 * This class tests the package-private methods of the JSFClientSession.
 * 
 * 
 * @author Stan Silvert
 */
public class JSFClientSessionPackagePrivateTest extends ServletTestCase
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
      return new TestSuite( JSFClientSessionPackagePrivateTest.class );
   }
   
   public void testGetClientIDs() throws IOException, SAXException
   {
      ClientIDs clientIDs = client.getClientIDs();
      assertEquals("form1:input_foo_text", 
                   clientIDs.findClientID("input_foo_text"));
   }
   
   public void testGetForm() throws IOException, SAXException
   {
      // test passing a form param
      WebForm form = client.getForm("input_foo_text");
      assertEquals("form1", form.getID());
      
      // test passing a component in the form that is not a param
      form = client.getForm("prompt");
      assertEquals("form1", form.getID());
      
      // test passing the form ID itself
      form = client.getForm("form1");
      assertEquals("form1", form.getID());
      
      // test ComponentIDNotFoundException
      try
      {
         form = client.getForm("bogus");
         fail("Expected ComponentIDNotFoundException");
      }
      catch (ComponentIDNotFoundException e)
      {
         // OK
      }
      
      // test FormNotFoundException
      // "title" is on the page (a valid component) but it's not inside the form
      try
      {
         form = client.getForm("title");
         fail("Expected FormNotFoundException");
      }
      catch (FormNotFoundException e)
      {
         // OK
      }
   }
}
