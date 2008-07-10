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
import java.util.Iterator;
import javax.faces.application.FacesMessage;
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
public class AjaxRegionValidationErrorTest extends ServletTestCase
{
   public static Test suite()
   {
      return new TestSuite( AjaxRegionValidationErrorTest.class );
   }

   public void testNoAjaxRegion() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/richfaces/region.jsf");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      client.type("form1:inname", 'f');     
      
      Object userBeanValue = server.getManagedBeanValue("#{userBean.name}");
      assertTrue((userBeanValue == null) || (userBeanValue.equals("")));
      FacesMessage message = (FacesMessage)server.getFacesContext().getMessages().next();
      assertTrue(message.getDetail().contains("Value is required"));
   }
   
   public void testWithAjaxRegion() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/richfaces/region.jsf");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      client.type("form2:inname", 'f');     
      
      Object userBeanValue = server.getManagedBeanValue("#{userBean.name}");
      assertEquals("f", userBeanValue);
      Iterator messages = server.getFacesContext().getMessages();
      assertFalse(messages.hasNext());
   }
}