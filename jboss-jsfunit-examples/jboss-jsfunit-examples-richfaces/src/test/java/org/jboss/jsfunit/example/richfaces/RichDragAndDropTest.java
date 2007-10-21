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
package org.jboss.jsfunit.example.richfaces;

import java.io.IOException;
import java.util.List;
import javax.xml.transform.TransformerException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.a4jsupport.RichFacesClient;
import org.jboss.jsfunit.facade.JSFClientSession;
import org.jboss.jsfunit.facade.JSFServerSession;
import org.xml.sax.SAXException;

/**
 * Peform JSFUnit tests on RichFaces demo application.
 *
 * @author Stan Silvert
 */
public class RichDragAndDropTest extends ServletTestCase
{
   public void testDragAndDrop() throws IOException, SAXException, TransformerException
   {
      JSFClientSession client = new JSFClientSession("/richfaces/dragSupport.jsf");
      RichFacesClient ajaxClient = new RichFacesClient(client);
      JSFServerSession server = new JSFServerSession(client);
      
      List phpList = (List)server.getManagedBeanValue("#{dndBean.containerPHP}");
      assertEquals(0, phpList.size()); // assert that list is empty
      
      // element 0 is Flexible Ajax - drag to php
      // added leading colon so JSFUnit doesn't also find 10:dragSource
      ajaxClient.dragAndDrop(":0:dragSource", "php");
      
      phpList = (List)server.getManagedBeanValue("#{dndBean.containerPHP}");
      assertEquals(1, phpList.size()); // list now has one element
      String frameworkName = (String)server.getManagedBeanValue("#{dndBean.containerPHP[0].name}");
      assertEquals("Flexible Ajax", frameworkName); // 1st element is "Flexible Ajax" ^^^^^
      
      ajaxClient.fireAjaxEvent("reset");
      phpList = (List)server.getManagedBeanValue("#{dndBean.containerPHP}");
      assertEquals(0, phpList.size()); // now it's gone again
   }
   
   public static Test suite()
   {
      return new TestSuite( RichDragAndDropTest.class );
   }
}
