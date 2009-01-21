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
package org.jboss.jsfunit.test.richfaces;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HTMLParserListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;

/**
 * Peform JSFUnit tests on RichFaces demo application.
 *
 * @author Stan Silvert
 */
public class AjaxSupportTest extends ServletTestCase
{
   public static Test suite()
   {
      return new TestSuite( AjaxSupportTest.class );
   }

   public void testAjaxSupportWithOnkeyup() throws IOException
   {
      JSFSession jsfSession = JSFSessionFactory.makeSession("/richfaces/support.jsf");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();

      client.type("name", 'H');    
      client.type("name", 'e');
      client.type("name", 'l');
      client.type("name", 'l');
      client.type("name", 'o');
      client.type("name", ' ');
      client.type("name", 'W');
      client.type("name", 'o');
      client.type("name", 'r');
      client.type("name", 'l');
      client.type("name", 'd');
  
      Object userBeanValue = server.getManagedBeanValue("#{userBean.name}");
      assertEquals("Hello World", userBeanValue);
      String valueOfSpan = ((DomNode)client.getElement("outtext")).asText();
      assertEquals("Hello World", valueOfSpan);  
   }
   
   public void testAjaxSupportWithOnchange() throws IOException
   {
      JSFSession jsfSession = JSFSessionFactory.makeSession("/richfaces/support.jsf");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();

      client.setValue("name", "Hello World");
      Object userBeanValue = server.getManagedBeanValue("#{userBean.name}");
      assertEquals("Hello World", userBeanValue);
      
      String valueOfSpan = ((DomNode)client.getElement("outtext")).asText();
      assertEquals("Hello World", valueOfSpan); 
   } 
}
