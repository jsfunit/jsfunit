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
package org.jboss.jsfunit.test.richfaces;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
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
public class AjaxRegionSelfRenderTest extends ServletTestCase
{
   public static Test suite()
   {
      return new TestSuite( AjaxRegionSelfRenderTest.class );
   }

   public void testNotUsingOutputTextTag() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/richfaces/region.jsf");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      client.type("form3:name", 'f');
      client.type("form3:name", 'o');
      client.type("form3:name", 'o');
      client.type("form3:name", 'b');
      client.type("form3:name", 'a');
      client.type("form3:name", 'r');

      Object userBeanValue = server.getManagedBeanValue("#{userBean.name}");
      assertTrue(userBeanValue.equals("foobar"));
      String text = "This text will disappear during the partial update";
      HtmlElement span = (HtmlElement)client.getElement("out3");
      assertFalse(span.getTextContent().contains(text));
   }
   
   public void testUsingOutputTextTag() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/richfaces/region.jsf");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      client.type("form4:name", 'f');
      client.type("form4:name", 'o');
      client.type("form4:name", 'o');
      client.type("form4:name", 'b');
      client.type("form4:name", 'a');
      client.type("form4:name", 'r');
 
      Object userBeanValue = server.getManagedBeanValue("#{userBean.name}");
      assertTrue(userBeanValue.equals("foobar"));
      String text = "not disappear because it is printed with h:outputText";
      HtmlElement span = (HtmlElement)client.getElement("out4");
      assertTrue(span.getTextContent().contains(text));
   }
}