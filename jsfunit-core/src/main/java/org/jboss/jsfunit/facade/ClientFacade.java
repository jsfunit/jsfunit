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

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.SubmitButton;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.faces.component.NamingContainer;
import org.jboss.jsfunit.framework.WebConversationFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author Stan Silvert
 */
public class ClientFacade
{
   private WebResponse webResponse;
   private String namingContainer = "";
   
   /**
    * Creates a new client interface for testing the JSF application.
    *
    * @param initialViewId The view Id used to start a client session with JSF.  Example: "/index.jsf"
    *
    * @throws MalformedURLException If the view Id cannot be used to create a URL for the JSF app
    * @throws IOException If there is an error calling the JSF app
    * @throws SAXException If the response from the JSF app cannot be parsed as HTML
    */
   public ClientFacade(String initialViewId) throws MalformedURLException, IOException, SAXException
   {
      WebConversation webConversation = WebConversationFactory.makeWebConversation();
      WebRequest req = new GetMethodWebRequest(WebConversationFactory.getWARURL() + initialViewId);
      this.webResponse = webConversation.getResponse(req);
   }
   
   public WebResponse getWebResponse()
   {
      return this.webResponse;
   }
   
   public void setNamingContainer(String namingContainer)
   {
      if (namingContainer.equals("")) 
      {
         this.namingContainer = "";
         return;
      }
      
      this.namingContainer = namingContainer + NamingContainer.SEPARATOR_CHAR;
   }
   
   public WebForm getForm() throws SAXException
   {
      return getWebResponse().getFormWithID(this.namingContainer);
   }
   
   public void setParameter(String paramName, String value) throws SAXException
   {
      getForm().setParameter(this.namingContainer + paramName, value);
   }
   
   /**
    * Finds the first submit button on the NamingContainer (form) and
    * submits the form.
    */
   public void submit() throws SAXException, IOException
   {
      getForm().submit();
   }
   
   public void submit(String componentId) throws SAXException, IOException
   {
      SubmitButton button = getForm().getSubmitButtonWithID(this.namingContainer + componentId);
      getForm().submit(button);
   }
}
