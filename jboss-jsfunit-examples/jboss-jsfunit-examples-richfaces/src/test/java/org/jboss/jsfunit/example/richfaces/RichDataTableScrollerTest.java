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
import javax.xml.transform.TransformerException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.a4jsupport.RichFacesClient;
import org.jboss.jsfunit.a4jsupport.ScrollerControl;
import org.jboss.jsfunit.facade.DOMUtil;
import org.jboss.jsfunit.facade.JSFClientSession;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Peform JSFUnit tests on RichFaces demo application.
 *
 * @author Stan Silvert
 */
public class RichDataTableScrollerTest extends ServletTestCase
{
   public void testDataTableScrollerClickNumber() 
         throws IOException, SAXException, TransformerException
   {
      JSFClientSession client = new JSFClientSession("/richfaces/dataTableScroller.jsf");
      RichFacesClient ajaxClient = new RichFacesClient(client);
      
      ajaxClient.clickDataTableScroller("myScroller", 1);
      Document doc = DOMUtil.convertToDomLevel2(client.getUpdatedDOM());
      Element element = DOMUtil.findElementWithID("form1:carList:0:make", doc);
      assertEquals("Chevrolet", element.getTextContent());
      
      ajaxClient.clickDataTableScroller("myScroller", 12);
      doc = DOMUtil.convertToDomLevel2(client.getUpdatedDOM());
      element = DOMUtil.findElementWithID("form1:carList:119:make", doc);
      assertEquals("Infiniti", element.getTextContent()); 
   }
   
   public void testDataTableScrollerClickArrows() 
         throws IOException, SAXException, TransformerException
   {
      JSFClientSession client = new JSFClientSession("/richfaces/dataTableScroller.jsf");
      RichFacesClient ajaxClient = new RichFacesClient(client);
      
      ajaxClient.clickDataTableScroller("myScroller", ScrollerControl.first);
      Document doc = DOMUtil.convertToDomLevel2(client.getUpdatedDOM());
      Element element = DOMUtil.findElementWithID("form1:carList:0:make", doc);
      assertEquals("Chevrolet", element.getTextContent());
      
      ajaxClient.clickDataTableScroller("myScroller", ScrollerControl.last);
      doc = DOMUtil.convertToDomLevel2(client.getUpdatedDOM());
      element = DOMUtil.findElementWithID("form1:carList:119:make", doc);
      assertEquals("Infiniti", element.getTextContent());
      
      // just for fun, click the others
      ajaxClient.clickDataTableScroller("myScroller", ScrollerControl.fastrewind);
      ajaxClient.clickDataTableScroller("myScroller", ScrollerControl.fastforward);
      ajaxClient.clickDataTableScroller("myScroller", ScrollerControl.previous);
      ajaxClient.clickDataTableScroller("myScroller", ScrollerControl.next);
   }
   
   public static Test suite()
   {
      return new TestSuite( RichDataTableScrollerTest.class );
   }
}
