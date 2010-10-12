/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.jsfunit.example.ajax4jsf;

import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;

/**
 * Peform JSFUnit tests on three of the Ajax4jsf demo applications.
 *
 * @author Stan Silvert
 */
public class A4JSupportTest extends ServletTestCase
{
   
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      return new TestSuite( A4JSupportTest.class );
   }

   /**
    * Test simple a4j:support.
    */
   public void testA4JSupport() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/a4jsupport.jsf");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();

      client.setValue("val1", "2009");
      assertEquals("2009", server.getManagedBeanValue("#{bean.val1}"));

      client.setValue("val2", "2010");
      assertEquals("2010", server.getManagedBeanValue("#{bean.val2}"));
   }
   
}
