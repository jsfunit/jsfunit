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

package org.jboss.jsfunit.seam.booking;

import java.io.IOException;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;

/**
 * Test a Seam bean that has the @Startup annotation and session scope
 *
 * @author Stan Silvert
 */
public class StartupSessionBeanTest extends ServletTestCase
{
   
   public void testStartupSessionBean() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/home.seam");
      JSFServerSession server = jsfSession.getJSFServerSession();
      assertNotNull(server.getManagedBeanValue("#{sessionScope.StartupSessionBean}"));
   }
   
}
