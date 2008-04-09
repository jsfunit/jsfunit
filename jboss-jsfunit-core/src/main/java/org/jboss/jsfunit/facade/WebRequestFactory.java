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
import com.meterware.httpunit.WebResponse;
import org.xml.sax.SAXException;

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
   private JSFClientSession client;
   
   /**
    * Create a new WebRequestFactory.
    * 
    * 
    * @param client A JSFClientSession that requests will be based on.
    */
   public WebRequestFactory(JSFClientSession client)
   {
      this.client = client;
   }
   
   /**
    * Create a post request with the action URL.  The actionURL should
    * contain a leading '/'.  The PostMethodWebRequest returned will contain
    * the proper host and port for a post-back to the JSF servlet, but will
    * not contain any request parameters.
    *
    * @param actionURL The action URL to be appended to the protocol://host:port
    */
   public PostMethodWebRequest makePostRequest(String actionURL)
   {
      WebResponse latestResponse = client.getWebResponse();
      String protocol = latestResponse.getURL().getProtocol();
      String host = latestResponse.getURL().getHost();
      String port = Integer.toString(latestResponse.getURL().getPort());
      return new PostMethodWebRequest(protocol + "://" + host + ":" + port + actionURL);
   }
   
   private PostMethodWebRequest buildRequest(String actionURL, WebForm form)
   {
      PostMethodWebRequest req = makePostRequest(actionURL);
      // set params from the form
      String[] paramNames = form.getParameterNames();
      for (int i=0; i < paramNames.length; i++)
      {
         req.setParameter(paramNames[i], form.getParameterValues(paramNames[i]));
      }
      
      return req;
   }
   
   /**
    * Return a PostMethodWebRequest built from the component's parent form.  
    * The request will contain all the form's current param values and will
    * use the action URL from the form.
    *
    * With the returned PostMethodWebRequest, you can add your own params to
    * the form before submitting.
    *
    * @param componentID The ID for any JSF component that lives in the form.
    *                    It can also be the ID of the form itself.
    *
    * @return A PostMethodWebRequest based on the component's parent form.
    *
    * @throws SAXException if the current response page can not be parsed
    */
   public PostMethodWebRequest buildRequest(String componentID)
         throws SAXException
   {
      return buildRequest(this.client.getForm(componentID));
   }
   
   /**
    * Return a PostMethodWebRequest built from the component's parent form.  
    * The request will contain all the form's current param values and will
    * use the action URL specified.
    *
    * With the returned PostMethodWebRequest, you can add your own params to
    * the form before submitting.
    *
    * @param actionURL The alternate actionURL used to submit the form's data.
    * @param componentID The ID for any JSF component that lives in the form.
    *                    It can also be the ID of the form itself.
    *
    * @throws SAXException if the current response page can not be parsed
    */
   public PostMethodWebRequest buildRequest(String actionURL, String componentID)
         throws SAXException
   {
      WebForm form = this.client.getForm(componentID);
      return buildRequest(actionURL, form);
   }
   
   /**
    * Return a PostMethodWebRequest built from a WebForm object.  
    * The request will contain all the form's current param values and will
    * use the action URL from the form.
    *
    * With the returned PostMethodWebRequest, you can add your own params to
    * the form before submitting.
    *
    * @param form The WebForm to base the PostMethodWebRequest on.
    *
    * @return A PostMethodWebRequest based on the WebForm.
    *
    * @throws SAXException if the current response page can not be parsed
    */
   public PostMethodWebRequest buildRequest(WebForm form)
         throws SAXException
   {
      return buildRequest(form.getAction(), form);
   }
   
}