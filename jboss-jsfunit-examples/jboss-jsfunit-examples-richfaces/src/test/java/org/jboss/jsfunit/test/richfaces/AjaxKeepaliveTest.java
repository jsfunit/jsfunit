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
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;

/**
 * Peform JSFUnit tests on RichFaces demo application.
 *
 * @author Stan Silvert
 */
public class AjaxKeepaliveTest extends ServletTestCase
{ 
   public void testWithoutKeepalive() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/richfaces/keepAlive.jsf");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();

      client.type("form1:firstAddend", '2');
      assertEquals(2, server.getManagedBeanValue("#{rsBean.addent1}"));
      
      client.type("form1:secondAddend", '3');
      assertEquals(3, server.getManagedBeanValue("#{rsBean.addent2}"));
      
      client.click("form1:btn");
      assertNotNull(server.getManagedBeanValue("#{rsBean}"));
      assertNull(server.getManagedBeanValue("#{rsBean.sum}"));
   } 
   
   public void testWithKeepalive() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/richfaces/keepAlive.jsf");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();

      client.type("form2:firstAddend", '2');
      assertEquals(2, server.getManagedBeanValue("#{rsBean2.addent1}"));
      
      client.type("form2:secondAddend", '3');
      assertEquals(3, server.getManagedBeanValue("#{rsBean2.addent2}"));
      
      client.click("form2:btn2");
      assertNotNull(server.getManagedBeanValue("#{rsBean2}"));
      assertEquals(5, server.getManagedBeanValue("#{rsBean2.sum}"));
   }
   
   public static Test suite()
   {
      return new TestSuite( AjaxKeepaliveTest.class );
   }
}
