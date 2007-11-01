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

import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.richfaces.Ajax4jsfClient;
import org.jboss.jsfunit.facade.JSFClientSession;
import org.jboss.jsfunit.facade.JSFServerSession;
import org.xml.sax.SAXException;

/**
 * Peform JSFUnit tests on three of the Ajax4jsf demo applications.
 *
 * @author Stan Silvert
 */
public class A4JTest extends ServletTestCase
{
   
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      return new TestSuite( A4JTest.class );
   }

   /**
    * Test the Echo demo page.
    */
   public void testEcho() throws IOException, SAXException
   {
      JSFClientSession client = new JSFClientSession("/pages/echo.jsf");
      Ajax4jsfClient ajaxClient = new Ajax4jsfClient(client);
      JSFServerSession server = new JSFServerSession(client);
      
      // Note: input_text is session scoped
      client.setParameter("input_text", "foo");
      ajaxClient.ajaxSubmit("a4jsupport");
      assertEquals("foo", server.getManagedBeanValue("#{textbean.text}"));
      
      // simulate hitting the "b" key on the field
      client.setParameter("input_text", "foob");
      ajaxClient.ajaxSubmit("a4jsupport");
      assertEquals("foob", server.getManagedBeanValue("#{textbean.text}"));
      
      client.setParameter("input_text_request_scope", "foo");
      ajaxClient.ajaxSubmit("a4jsupport_request_scope");
      assertEquals("foo", server.getComponentValue("input_text_request_scope"));
      assertEquals("foo", 
                  server.getManagedBeanValue("#{request_scope_textbean.text}"));
      
      // If you do a regular submit instead of an ajax request, request scoped 
      // data is applied on the server side. Also, because the data for 
      // input_text_request_scope was not applied to the server side view 
      // previously, I have to set the parameter again.
      client.setParameter("input_text_request_scope", "foo");
      client.submit();
      assertEquals("foo", 
                   server.getComponentValue("input_text_request_scope"));
      assertEquals("foo", 
                  server.getManagedBeanValue("#{request_scope_textbean.text}"));
   }
   
   /**
    * Test the Selection List demo
    */
   public void testSelectionList() throws IOException, SAXException
   {
      JSFClientSession client = new JSFClientSession("/list.jsf");
      Ajax4jsfClient ajaxClient = new Ajax4jsfClient(client);
      JSFServerSession server = new JSFServerSession(client);
      
      client.setParameter("list", "Lott, Charlie");
      ajaxClient.ajaxSubmit("a4jsupport");
      assertEquals("Mr.", 
               server.getManagedBeanValue("#{userList.currentUser.prefix}"));
      assertEquals("Charlie", 
               server.getManagedBeanValue("#{userList.currentUser.firstName}"));
      assertEquals("Lott", 
               server.getManagedBeanValue("#{userList.currentUser.lastName}"));
      assertEquals("8888 Spartan Rd. Washington D.C., VA 70938-3445", 
               server.getManagedBeanValue("#{userList.currentUser.address}"));
      assertEquals("Talk Radio Host", 
               server.getManagedBeanValue("#{userList.currentUser.jobTitle}"));
      
      client.setParameter("list", "Story, Leslie");
      ajaxClient.ajaxSubmit("a4jsupport");
      assertEquals("Mrs.", 
               server.getManagedBeanValue("#{userList.currentUser.prefix}"));
      assertEquals("Leslie", 
               server.getManagedBeanValue("#{userList.currentUser.firstName}"));
      assertEquals("Story", 
               server.getManagedBeanValue("#{userList.currentUser.lastName}"));
      assertEquals("834 Thomas Road Atlanta, GA 72890-3423", 
               server.getManagedBeanValue("#{userList.currentUser.address}"));
      assertEquals("Ajax Evangelist", 
               server.getManagedBeanValue("#{userList.currentUser.jobTitle}"));
   }
   
   /**
    * Test the Repeat Rerender demo.
    *
    * The interesting thing about this one from a JSFUnit perspective is 
    * the references to indexed components.  Any time you refer to a component
    * that is inside a JSF DataModel, you use the JSF-generated index
    * added to the ID of the component.  Do a "view source" on the demo 
    * page to see how the full ID is generated.  
    *
    * The JSFUnit API will always find the component based on the suffix you 
    * pass in.  In this case, there are many components that end with
    * "command_link_up".  But there is only one that ends with 
    * "0:command_link_up" or "8:command_link_up".
    */
   public void testRepeatRerender() throws IOException, SAXException
   {
      JSFClientSession client = new JSFClientSession("/pages/a4j-repeat-rerender.jsf");
      Ajax4jsfClient ajaxClient = new Ajax4jsfClient(client);
      JSFServerSession server = new JSFServerSession(client);
      
      ajaxClient.ajaxSubmit("0:command_link_up");
      assertEquals(1, server.getManagedBeanValue("#{bean.requestCounter}"));
      assertEquals(1, server.getManagedBeanValue("#{bean.collection[0]}"));
      
      ajaxClient.ajaxSubmit("1:command_link_down");
      ajaxClient.ajaxSubmit("1:command_link_down"); // do this one twice
      assertEquals(3, server.getManagedBeanValue("#{bean.requestCounter}"));
      assertEquals(-2, server.getManagedBeanValue("#{bean.collection[1]}"));
      
      ajaxClient.ajaxSubmit("2:command_link_up");
      assertEquals(4, server.getManagedBeanValue("#{bean.requestCounter}"));
      assertEquals(1, server.getManagedBeanValue("#{bean.collection[2]}"));
      
      ajaxClient.ajaxSubmit("8:command_link_up");
      assertEquals(5, server.getManagedBeanValue("#{bean.requestCounter}"));
      assertEquals(1, server.getManagedBeanValue("#{bean.collection[8]}"));
   }
   
}
