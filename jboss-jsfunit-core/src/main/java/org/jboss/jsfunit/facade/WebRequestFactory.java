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

package org.jboss.jsfunit.facade;

import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

/**
 * This class contains methods to create WebRequest objects that contain all
 * current parameters and values that exist in a form.  HttpUnit does not
 * allow you to add fields to a form.  So the way to add something is to 
 * create a WebRequest from here and then add params to the WebRequest.
 *
 * @author Stan Silvert
 */
public class WebRequestFactory
{
   private ClientFacade client;
   
   /**
    * Create a new WebRequestFactory.
    *
    * @param client A ClientFacade that requests will be based on.
    */
   public WebRequestFactory(ClientFacade client)
   {
      this.client = client;
   }
   
   private WebRequest makePostRequest(String actionURL)
   {
      WebResponse latestResponse = client.getWebResponse();
      String protocol = latestResponse.getURL().getProtocol();
      String host = latestResponse.getURL().getHost();
      String port = Integer.toString(latestResponse.getURL().getPort());
      return new PostMethodWebRequest(protocol + "://" + host + ":" + port + actionURL);
   }

   /**
    * Return a request based on the parameters of the WebForm.  The request 
    * will use the action URL from the form.
    *
    * @param form The WebForm that the reqeust will be built from.
    */
   public WebRequest makePostRequest(WebForm form)
   {
      return makePostRequest(form.getAction(), form);
   }
   
   /**
    * Return a request based on the parameters of the WebForm.  The request 
    * will use the specified action URL instead of the one from the WebForm.
    *
    * @param form The WebForm that the reqeust will be built from.
    */
   public WebRequest makePostRequest(String actionURL, WebForm form)
   {
      WebRequest req = makePostRequest(actionURL);
      // set params from the form
      String[] paramNames = form.getParameterNames();
      for (int i=0; i < paramNames.length; i++)
      {
         req.setParameter(paramNames[i], form.getParameterValues(paramNames[i]));
      }
      
      return req;
   }
   
}
