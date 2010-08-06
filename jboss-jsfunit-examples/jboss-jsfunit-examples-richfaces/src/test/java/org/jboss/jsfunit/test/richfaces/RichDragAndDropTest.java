/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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

import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.jboss.jsfunit.richclient.RichFacesClient;

/**
 * Unit test hits RichFaces demo app.
 */
public class RichDragAndDropTest extends ServletTestCase
{   
   public RichDragAndDropTest( String testName )
   {
      super( testName );
   }
   
   public static Test suite()
   {
      return new TestSuite( RichDragAndDropTest.class );
   }
   
   public void testDragAndDrop() throws IOException
   {
      JSFSession jsfSession = JSFSessionFactory.makeSession("/richfaces/dragSupport.jsf");

      HtmlPage page = (HtmlPage)jsfSession.getJSFClientSession().getContentPage();
      
      HtmlTable table = (HtmlTable)page.getElementById("form:phptable");
      assertEquals(0, table.getRowCount());
      
      // element 0 is Flexible Ajax - drag to php
      HtmlElement dragElement = (HtmlElement)page.getElementById("form:src:0:dragItem");
      HtmlElement dropElement = (HtmlElement)page.getElementById("form:phppanel_body");
      
      page = (HtmlPage)dragElement.mouseDown();
      // need to trigger a mousemove outside of dragElement to initialize
      ((HtmlBody)page.getFirstByXPath("//body")).mouseMove();
      page = (HtmlPage)dropElement.mouseOver();
      page = (HtmlPage)dropElement.mouseMove();
      page = (HtmlPage)dropElement.mouseUp();
      
      table = (HtmlTable)page.getElementById("form:phptable");
      assertEquals(1, table.getRowCount()); 
   }

   public void testDandDUsingRichFacesClient() throws IOException
   {
      JSFSession jsfSession = JSFSessionFactory.makeSession("/richfaces/dragSupport.jsf");
      JSFClientSession client = jsfSession.getJSFClientSession();
      HtmlPage page = (HtmlPage)client.getContentPage();

      HtmlTable table = (HtmlTable)page.getElementById("form:phptable");
      assertEquals(0, table.getRowCount());


      RichFacesClient richClient = new RichFacesClient(client);
      richClient.dragAndDrop(":0:dragItem", "form:phppanel_body");

      table = (HtmlTable)client.getElement("phptable");
      assertEquals(1, table.getRowCount());
   }
   
}
