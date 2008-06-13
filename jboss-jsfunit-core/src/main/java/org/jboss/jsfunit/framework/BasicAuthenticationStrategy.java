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

package org.jboss.jsfunit.framework;

import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.Page;
import java.io.IOException;

/**
 * Performs JEE container-managed BASIC authentication using 
 * only a user name and password.
 *
 * @author Stan Silvert
 */
public class BasicAuthenticationStrategy extends SimpleInitialRequestStrategy
{
   private DefaultCredentialsProvider credProvider;
   
   /**
    * Create a new BasicAuthenticationStrategy.
    *
    * @param userName The user name to send.
    * @param password The password to send.
    */
   public BasicAuthenticationStrategy(String userName, String password)
   {
      this.credProvider = new DefaultCredentialsProvider();
      this.credProvider.addCredentials(userName, password);
   }
   
   /**
    * Perform the initial request and provide Basic authentication 
    * credentials when challenged.
    *
    * @param wcSpec The WebClient specification.
    *
    * @return The requested page if authentication passed.  Otherwise, return
    *         the error page from the server (usually a 401 error).
    */
   public Page doInitialRequest(WebClientSpec wcSpec) throws IOException
   {
      wcSpec.getWebClient().setCredentialsProvider(this.credProvider);
      return super.doInitialRequest(wcSpec);
   }
   
}
