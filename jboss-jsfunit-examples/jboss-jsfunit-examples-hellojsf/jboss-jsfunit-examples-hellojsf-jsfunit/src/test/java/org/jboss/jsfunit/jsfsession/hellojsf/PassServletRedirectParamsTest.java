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

package org.jboss.jsfunit.jsfsession.hellojsf;

import java.util.Map;
import junit.framework.TestCase;
import org.jboss.jsfunit.framework.WebClientSpec;
import org.jboss.jsfunit.jsfsession.JSFSession;

/**
 * This tests to see if the params from the Redirector can be passed into
 * JSFUnit tests.  This test should be excluded in the pom as it is only
 * called manually from the ManualInvokeTest
 *
 * @author Stan Silvert
 */
public class PassServletRedirectParamsTest extends TestCase
{
   public void testReceiveRedirectorParams() throws Exception
   {
      Map redirectorParams = WebClientSpec.getRedirectorRequestParams();
      assertNotNull(redirectorParams);
      String paramValue = ((String[])redirectorParams.get("foo"))[0];
      assertEquals("bar", paramValue);
      
      JSFSession jsfSession = new JSFSession("/index.faces");
      redirectorParams = jsfSession.getRedirectorRequestParams();
      assertNotNull(redirectorParams);
      paramValue = ((String[])redirectorParams.get("foo"))[0];
      assertEquals("bar", paramValue);
   }
}
