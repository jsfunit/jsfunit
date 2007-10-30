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
public class AjaxKeepaliveTest extends ServletTestCase
{ 
   public void testWithoutKeepalive() throws IOException, SAXException
   {
      JSFClientSession client = new JSFClientSession("/richfaces/keepAlive.jsf");
      RichFacesClient ajaxClient = new RichFacesClient(client);
      JSFServerSession server = new JSFServerSession(client);

      client.setParameter("form1:firstAddend", "2");
      ajaxClient.ajaxSubmit("form1:firstAddendAjax");
      assertEquals(2, server.getManagedBeanValue("#{rsBean.addent1}"));
      
      client.setParameter("form1:secondAddend", "3");
      ajaxClient.ajaxSubmit("form1:secondAddendAjax");
      assertEquals(3, server.getManagedBeanValue("#{rsBean.addent2}"));
      
      ajaxClient.ajaxSubmit("form1:btn");
      assertNotNull(server.getManagedBeanValue("#{rsBean}"));
      assertNull(server.getManagedBeanValue("#{rsBean.sum}"));
   } 
   
   public void testWithKeepalive() throws IOException, SAXException
   {
      JSFClientSession client = new JSFClientSession("/richfaces/keepAlive.jsf");
      RichFacesClient ajaxClient = new RichFacesClient(client);
      JSFServerSession server = new JSFServerSession(client);

      client.setParameter("form2:firstAddend", "2");
      ajaxClient.ajaxSubmit("form2:firstAddendAjax");
      assertEquals(2, server.getManagedBeanValue("#{rsBean2.addent1}"));
      
      client.setParameter("form2:secondAddend", "3");
      ajaxClient.ajaxSubmit("form2:secondAddendAjax");
      assertEquals(3, server.getManagedBeanValue("#{rsBean2.addent2}"));
      
      ajaxClient.ajaxSubmit("form2:btn2");
      assertNotNull(server.getManagedBeanValue("#{rsBean2}"));
      assertEquals(5, server.getManagedBeanValue("#{rsBean2.sum}"));
   }
   
   public static Test suite()
   {
      return new TestSuite( AjaxKeepaliveTest.class );
   }
}
