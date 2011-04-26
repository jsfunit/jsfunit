/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.jsfunit.jsfsession.hellojsf;

import java.io.IOException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.context.JSFUnitExternalContext;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;

/**
 * 
 * 
 * @author Stan Silvert
 */
public class JSFUnitExternalContextTest extends ServletTestCase
{
   private JSFClientSession client;
   private JSFServerSession server;
   
   /**
    * Start a JSFUnit session by getting the /index.faces page.
    */
   public void setUp() throws IOException
   {
      // Initial JSF request
      JSFSession jsfSession = new JSFSession("/index.faces");
      this.client = jsfSession.getJSFClientSession();
      this.server = jsfSession.getJSFServerSession();
   }
   
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      return new TestSuite( JSFUnitExternalContextTest.class );
   }
   
   /**
    * Test some methods in JSFUnitExternalContext.
    * See JSFUNIT-277
    */
   public void testInitialPage() throws IOException
   {
      FacesContext facesCtx = server.getFacesContext();
      JSFUnitExternalContext extCtx = (JSFUnitExternalContext)facesCtx.getExternalContext();
      
      // these methods will cause stack overflow without JSFUNIT-277
      extCtx.getRequestLocale();
      extCtx.getAuthType();
      extCtx.getRequestCharacterEncoding();
      extCtx.getRequestContentType();
      extCtx.getRequestContextPath();
   }
   
   
   
   
}
