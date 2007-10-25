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
public class RichDataFilterSliderTest extends ServletTestCase
{
   public void testDataFilterSlider() throws IOException, SAXException, TransformerException
   {
      JSFClientSession client = new JSFClientSession("/richfaces/dataFilterSlider.jsf");
      RichFacesClient ajaxClient = new RichFacesClient(client);
      
      ajaxClient.setDataFilterSlider("slider_1", "60000");  
      ajaxClient.fireAjaxEvent("form1");
      // The data table is built with random data, so there's nothing to 
      // reliably assert about it except the make of the car.
      
      Document doc = DOMUtil.convertToDomLevel2(client.getUpdatedDOM());
      Element element = DOMUtil.findElementWithID("form1:carList:0:make", doc);
      assertEquals("Ford", element.getTextContent());
      
      // Click the link to change make to Chevy.  I happen to know it is the
      // first in the list.
      ajaxClient.fireAjaxEvent("form1:carIndex:0:switchMake");
      doc = DOMUtil.convertToDomLevel2(client.getUpdatedDOM());
      element = DOMUtil.findElementWithID("form1:carList:0:make", doc);
      assertEquals("Chevrolet", element.getTextContent());
   }
   
   public static Test suite()
   {
      return new TestSuite( RichDataFilterSliderTest.class );
   }
}
