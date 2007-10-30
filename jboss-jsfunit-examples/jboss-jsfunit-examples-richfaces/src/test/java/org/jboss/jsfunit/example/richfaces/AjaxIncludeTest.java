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
import javax.xml.parsers.ParserConfigurationException;
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
public class AjaxIncludeTest extends ServletTestCase
{
   public void testWizard() throws IOException, SAXException, ParserConfigurationException, TransformerException
   {
      JSFClientSession client = new JSFClientSession("/richfaces/include.jsf");
      RichFacesClient ajaxClient = new RichFacesClient(client);
      JSFServerSession server = new JSFServerSession(client);
      
      client.setParameter("fn", "Stan");
      client.setParameter("ln", "Silvert");
      client.setParameter("comp", "JBoss");
      ajaxClient.ajaxSubmit("wizardNext");
      
      client.setParameter("notes", "Here is my note");
      ajaxClient.ajaxSubmit("wizardNext");
      assertEquals("Here is my note", server.getManagedBeanValue("#{profile.notes}"));
      
      String page = client.getWebResponse().getText();
      assertTrue(page.contains("Stan"));
      assertTrue(page.contains("Silvert"));
      assertTrue(page.contains("JBoss"));
      assertTrue(page.contains("Here is my note"));
      
      ajaxClient.ajaxSubmit("wizardPrevious"); // back to Notes input page
      ajaxClient.ajaxSubmit("wizardPrevious"); // back to first input page
      assertTrue(page.contains("value=\"Stan\""));
      assertEquals("Stan", server.getManagedBeanValue("#{profile.firstName}"));
   }
   
   public static Test suite()
   {
      return new TestSuite( AjaxIncludeTest.class );
   }
}
