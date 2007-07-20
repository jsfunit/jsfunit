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
import org.jboss.jsfunit.a4jsupport.Ajax4jsfClient;
import org.jboss.jsfunit.facade.ClientFacade;
import org.jboss.jsfunit.facade.ServerFacade;
import org.xml.sax.SAXException;

/**
 *
 * @author Stan Silvert
 */
public class A4JTest extends ServletTestCase
{
   private ClientFacade client;
   
   /**
    * 
    */
   public void setUp() throws IOException, SAXException
   {
      //this.client = new ClientFacade("/pages/echo.jsf");
      this.client = new ClientFacade("/pages/a4j-repeat-rerender.jsf");
   }
   
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      return new TestSuite( A4JTest.class );
   }

   public void testUpClick() throws IOException, SAXException
   {
      Ajax4jsfClient ajaxClient = new Ajax4jsfClient(client);
      ServerFacade server = new ServerFacade(client);
      
      ajaxClient.fireAjaxEvent("0:command_link_up");
      
      assertEquals(1, server.getManagedBeanValue("#{bean.requestCounter}"));
      assertEquals(1, server.getManagedBeanValue("#{bean.collection[0]}"));

      ajaxClient.fireAjaxEvent("2:command_link_up");
      assertEquals(2, server.getManagedBeanValue("#{bean.requestCounter}"));
      //assertEquals(1, server.getManagedBeanValue("#{bean.collection[2]}"));
      
      ajaxClient.fireAjaxEvent("1:command_link_down");
      assertEquals(3, server.getManagedBeanValue("#{bean.requestCounter}"));
      //assertEquals(-1, server.getManagedBeanValue("#{bean.collection[1]}"));
   }
   
}
