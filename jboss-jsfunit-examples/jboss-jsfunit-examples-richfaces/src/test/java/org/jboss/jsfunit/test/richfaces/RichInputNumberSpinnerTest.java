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
import org.jboss.jsfunit.richclient.RichFacesClient;

/**
 * Peform JSFUnit tests on RichFaces demo application.
 *
 * @author Stan Silvert
 */
public class RichInputNumberSpinnerTest extends ServletTestCase
{
   public void testNumberSpinners() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/richfaces/inputNumberSpinner.jsf");
      JSFServerSession server = jsfSession.getJSFServerSession();
      JSFClientSession client = jsfSession.getJSFClientSession();
      RichFacesClient ajaxClient = new RichFacesClient(client);
      
      ajaxClient.setInputNumberSpinner("spinner1", "44");
      ajaxClient.clickInputNumberSpinnerUp("spinner1");
      client.click("submit");
      
      String value = (String)server.getComponentValue("spinner1");
      assertEquals("45", value);
      
      ajaxClient.clickInputNumberSpinnerDown("spinner2");
      client.click("submit");
      
      value = (String)server.getComponentValue("spinner2");
      assertEquals("40", value);
   }
   
   public static Test suite()
   {
      return new TestSuite( RichInputNumberSpinnerTest.class );
   }
}
