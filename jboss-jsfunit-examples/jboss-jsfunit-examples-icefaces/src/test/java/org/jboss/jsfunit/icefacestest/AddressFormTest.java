/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.jsfunit.icefacestest;

import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;

/**
 * Peform JSFUnit tests on the ICEfaces AddressForm demo
 *
 * @author Stan Silvert
 */
public class AddressFormTest extends ServletTestCase
{
   
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      return new TestSuite( AddressFormTest.class  );
   }

   /**
    * Basic client side test for Address Form
    */
   public void testAddressForm() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/address.iface");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      assertNotNull(server.getFacesContext());
      
      HtmlPage page = (HtmlPage)client.getContentPage();
      ClickableElement mrTitle = (ClickableElement)page.getFirstByXPath(".//option[@value='Mr.']");
      mrTitle.click();
      
      client.setValue("zip", "42303");
      //client.setValue("city", "Owensboro");
      //client.setValue("state", "KY");
      client.setValue("firstName", "Stan");
      client.setValue("lastName", "Silvert");
      client.click("Submit");
      
      //System.out.println(client.getPageAsText());
      assertTrue(client.getPageAsText().contains("AddressForm Results")); 
   }
   
}
