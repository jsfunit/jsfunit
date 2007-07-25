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

import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.parsing.HTMLParserFactory;
import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.a4jsupport.Ajax4jsfClient;
import org.jboss.jsfunit.facade.ClientFacade;
import org.jboss.jsfunit.facade.ServerFacade;
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

   public void testEcho() throws IOException, SAXException
   {
      ClientFacade client = new ClientFacade("/pages/echo.jsf");
      Ajax4jsfClient ajaxClient = new Ajax4jsfClient(client);
      ServerFacade server = new ServerFacade(client);
      
      // Note: input_text is sesison scoped
      client.setParameter("input_text", "foo");
      ajaxClient.fireAjaxEvent("a4jsupport");
      assertEquals("foo", server.getManagedBeanValue("#{textbean.text}"));
      
      // simulate hitting the "b" key on the field
      client.setParameter("input_text", "foob");
      ajaxClient.fireAjaxEvent("a4jsupport");
      assertEquals("foob", server.getManagedBeanValue("#{textbean.text}"));
      
      // Note that for request scoped params, Ajax4jsf does not update the
      // backing bean or even the component view.
      client.setParameter("input_text_request_scope", "foo");
      ajaxClient.fireAjaxEvent("a4jsupport_request_scope");
      assertEquals("", server.getComponentValue("input_text_request_scope"));
      assertEquals("", server.getManagedBeanValue("#{request_scope_textbean.text}"));
      
      // If you do a regular submit instead of an ajax request, request scoped data
      // is applied on the server side. Also, because the data for input_text_request_scope
      // was not applied to the server side view previously, I have to set the parameter again.
      client.setParameter("input_text_request_scope", "foo");
      client.submit();
      assertEquals("foo", server.getComponentValue("input_text_request_scope"));
      assertEquals("foo", server.getManagedBeanValue("#{request_scope_textbean.text}"));
   }
   
   public void testSelectionList() throws IOException, SAXException
   {
      ClientFacade client = new ClientFacade("/list.jsf");
      Ajax4jsfClient ajaxClient = new Ajax4jsfClient(client);
      ServerFacade server = new ServerFacade(client);
      
      client.setParameter("list", "Lott, Charlie");
      ajaxClient.fireAjaxEvent("a4jsupport");
      assertEquals("Mr.", server.getManagedBeanValue("#{userList.currentUser.prefix}"));
      assertEquals("Charlie", server.getManagedBeanValue("#{userList.currentUser.firstName}"));
      assertEquals("Lott", server.getManagedBeanValue("#{userList.currentUser.lastName}"));
      assertEquals("8888 Spartan Rd. Washington D.C., VA 70938-3445", server.getManagedBeanValue("#{userList.currentUser.address}"));
      assertEquals("Talk Radio Host", server.getManagedBeanValue("#{userList.currentUser.jobTitle}"));
      
      client.setParameter("list", "Story, Leslie");
      ajaxClient.fireAjaxEvent("a4jsupport");
      assertEquals("Mrs.", server.getManagedBeanValue("#{userList.currentUser.prefix}"));
      assertEquals("Leslie", server.getManagedBeanValue("#{userList.currentUser.firstName}"));
      assertEquals("Story", server.getManagedBeanValue("#{userList.currentUser.lastName}"));
      assertEquals("834 Thomas Road Atlanta, GA 72890-3423", server.getManagedBeanValue("#{userList.currentUser.address}"));
      assertEquals("Ajax Evangelist", server.getManagedBeanValue("#{userList.currentUser.jobTitle}"));
   }
   
   public void testRepeatRerender() throws IOException, SAXException
   {
      ClientFacade client = new ClientFacade("/pages/a4j-repeat-rerender.jsf");
      Ajax4jsfClient ajaxClient = new Ajax4jsfClient(client);
      ServerFacade server = new ServerFacade(client);
      
      ajaxClient.fireAjaxEvent("0:command_link_up");
      
      assertEquals(1, server.getManagedBeanValue("#{bean.requestCounter}"));
      assertEquals(1, server.getManagedBeanValue("#{bean.collection[0]}"));

      ajaxClient.fireAjaxEvent("2:command_link_up");
      assertEquals(2, server.getManagedBeanValue("#{bean.requestCounter}"));
      assertEquals(1, server.getManagedBeanValue("#{bean.collection[2]}"));
      
      ajaxClient.fireAjaxEvent("1:command_link_down");
      assertEquals(3, server.getManagedBeanValue("#{bean.requestCounter}"));
      assertEquals(-1, server.getManagedBeanValue("#{bean.collection[1]}"));
   }
   
}
