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
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.jboss.jsfunit.richclient.RichFacesClient;

/**
 * Peform JSFUnit tests on RichFaces demo application.
 *
 * @author Stan Silvert
 */
public class RichScrollableDataTableTest extends ServletTestCase
{
   private JSFSession jsfSession;
   private JSFClientSession client;
   private RichFacesClient ajaxClient;
   private JSFServerSession server;
           
   public void setUp() throws IOException
   {
     this.jsfSession = JSFSessionFactory.makeSession("/richfaces/scrollableDataTable.jsf");
     this.client = jsfSession.getJSFClientSession();
     this.ajaxClient = new RichFacesClient(this.client);
     this.server = jsfSession.getJSFServerSession();
   }
   
   public void tearDown() throws Exception
   {
      this.jsfSession = null;
      this.client = null;
      this.ajaxClient = null;
      this.server = null;
   }
   
   public void testScrollableDataTable() throws IOException
   {
      HtmlElement stock = (HtmlElement)client.getElement("carList:c_0_5");
      stock.click();
      
      client.click("setSelection");
      
      List selectedCars = (List)server.getManagedBeanValue("#{dataTableScrollerBean.selectedCars}");
      assertEquals(1, selectedCars.size());
       
      assertTrue(true);
   }
   
   public static Test suite()
   {
      return new TestSuite( RichScrollableDataTableTest.class );
   }
   
}
