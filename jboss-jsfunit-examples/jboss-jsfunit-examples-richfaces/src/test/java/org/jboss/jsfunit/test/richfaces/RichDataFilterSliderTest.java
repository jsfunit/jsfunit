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

import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.jboss.jsfunit.richclient.RichFacesClient;
import org.w3c.dom.Element;

/**
 * Peform JSFUnit tests on RichFaces demo application.
 *
 * @author Stan Silvert
 */
public class RichDataFilterSliderTest extends ServletTestCase
{
   public void testDataFilterSlider() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/richfaces/dataFilterSlider.jsf");
      JSFClientSession client = jsfSession.getJSFClientSession();
      RichFacesClient richClient = new RichFacesClient(client);
      
      richClient.setDataFilterSlider("slider_1", "60000");  

      // The data table is built with random data, so there's nothing to 
      // reliably assert about it except the make of the car.
      Element element = client.getElement("form1:carList:0:make");
      assertEquals("Ford", element.getTextContent());
      
      // Click the link to change make to Chevy.  I happen to know it is the
      // first in the list.
      client.click("form1:carIndex:0:switchMake");
      element = client.getElement("form1:carList:0:make");
      assertEquals("Chevrolet", element.getTextContent());
   }
   
   public static Test suite()
   {
      return new TestSuite( RichDataFilterSliderTest.class );
   }
}
