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

package org.jboss.jsfunit.test.richfaces;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import java.io.IOException;
import org.jboss.jsfunit.framework.WebClientSpec;
import org.jboss.jsfunit.jsfsession.JSFSession;

/**
 * Creates JSFSession for all RichFaces tests.
 *
 * @author Stan Silvert
 * @since 1.0
 */
public class JSFSessionFactory 
{
   private static SilentCssErrorHandler silentHandler = new SilentCssErrorHandler();

   // don't allow instance
   private JSFSessionFactory() {}
   
   public static JSFSession makeSession(String initialPage) throws IOException
   {
      return makeSession(initialPage, BrowserVersion.getDefault());
   }
   
   public static JSFSession makeSession(String initialPage, BrowserVersion browserVersion) throws IOException
   {
      return makeSession(new WebClientSpec(initialPage, browserVersion));
   }
   
   public static JSFSession makeSession(WebClientSpec wcSpec) throws IOException
   {
      wcSpec.getWebClient().setCssErrorHandler(silentHandler);
      return new JSFSession(wcSpec);
   }

}
