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

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HTMLParserListener;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.framework.JSFUnitWebConnection;
import org.jboss.jsfunit.framework.WebClientSpec;
import org.jboss.jsfunit.jsfsession.HtmlUnitSnooper;
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

   /* Test using only HtmlUnit.  This tries to mimic exactly what JSFUnit does
    * under the covers.
    */
   /* public void testWithoutJSFUnit() throws Exception
   {
      WebClient webClient = new WebClient(BrowserVersion.getDefault(), "localhost", 8008);
      
      // Change this to hit the live demo instead of local instance
      //HtmlPage page = (HtmlPage)webClient.getPage("http://livedemo.exadel.com/richfaces-demo/richfaces/support.jsf");
      HtmlPage page = (HtmlPage)webClient.getPage("http://localhost:8080/jboss-jsfunit-examples-richfaces/richfaces/support.jsf");
      
      JSFUnitWebConnection jsfUnitWebConn = new JSFUnitWebConnection(webClient.getWebConnection());
      jsfUnitWebConn.addListener(new HtmlUnitSnooper());
      webClient.setWebConnection(jsfUnitWebConn);
      webClient.setAjaxController(new NicelyResynchronizingAjaxController());
  
      HtmlInput field = (HtmlInput)page.getElementById("myform:myinput");
      page = (HtmlPage)field.type('H');
      page = (HtmlPage)field.type('e');
      page = (HtmlPage)field.type('l');
      page = (HtmlPage)field.type('l');
      page = (HtmlPage)field.type('o');
      page = (HtmlPage)field.type(' ');
      page = (HtmlPage)field.type('W');
      page = (HtmlPage)field.type('o');
      page = (HtmlPage)field.type('r');
      page = (HtmlPage)field.type('l');
      page = (HtmlPage)field.type('d');
      
      HtmlSpan span = (HtmlSpan)page.getElementById("myform:outtext");
      System.out.println("***************");
      System.out.println("field=" + field.asXml());
      System.out.println("cookies=" + webClient.getCookieManager().getCookies());
      System.out.println("asText=" + span.asText());
      System.out.println("asXml=" + span.asXml());
      System.out.println("***************");
      assertTrue(span.asXml().contains("Hello World"));
   } */ 
   
   public void testAjaxSupportWithOnkeyup() throws IOException
   {
      JSFSession jsfSession = JSFSessionFactory.makeSession("/richfaces/support.jsf");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      client.type("myinput", 'H');    
      client.type("myinput", 'e');
      client.type("myinput", 'l');
      client.type("myinput", 'l');
      client.type("myinput", 'o');
      client.type("myinput", ' ');
      client.type("myinput", 'W');
      client.type("myinput", 'o');
      client.type("myinput", 'r');
      client.type("myinput", 'l');
      client.type("myinput", 'd');
  
      Object userBeanValue = server.getManagedBeanValue("#{userBean.name}");
      assertEquals("Hello World", userBeanValue);
      String valueOfSpan = ((DomNode)client.getElement("outtext")).asText();
      String asText = valueOfSpan;
      String asXml = ((DomNode)client.getElement("outtext")).asXml();
      System.out.println("***************");
      System.out.println("valueOfSpan");
      System.out.println("asText=" + asText);
      System.out.println("asXml=" + asXml);
      System.out.println("***************");
      assertEquals("Hello World", valueOfSpan);  
      
   } 
   
   /*
   public void testAjaxSupportWithOnchange() throws IOException
   {
      JSFSession jsfSession = JSFSessionFactory.makeSession("/richfaces/support.jsf");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();

      client.setValue("myinput", "Hello World");
      Object userBeanValue = server.getManagedBeanValue("#{userBean.name}");
      assertEquals("Hello World", userBeanValue);
      
      String valueOfSpan = ((DomNode)client.getElement("outtext")).asText();
      assertEquals("Hello World", valueOfSpan); 
   } */
}
