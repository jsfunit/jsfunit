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
 * The ClientFacade provides a simplified API that wraps HttpUnit.
 *
 * @author Stan Silvert
 */
public class ClientFacade
{
   private WebResponse webResponse;
   private String namingContainer = "";
   
   /**
    * Creates a new client interface for testing the JSF application.  The
    * NamingContainer will be set to the Id of the first form found on the 
    * returned page.
    *
    * This will also start a new HttpSession.
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
      setNamingContainer();
   }
   
   /**
    * Creates a new client interface for testing the JSF application.  
    * 
    * This will also start a brand new HttpSession.
    *
    * @param initialViewId The view Id used to start a client session with JSF.  Example: "/index.jsf"
    * @param namingContainer The NamingContainer that will be used when the page is returned.
    *
    * @throws MalformedURLException If the view Id cannot be used to create a URL for the JSF app
    * @throws IOException If there is an error calling the JSF app
    * @throws SAXException If the response from the JSF app cannot be parsed as HTML
    */
   public ClientFacade(String initialViewId, String namingContainer) throws MalformedURLException, IOException, SAXException
   {
      this(initialViewId);
      setNamingContainer(namingContainer);
   }
   
   /**
    * Get the HttpUnit WebResponse object from the last request.
    *
    * @return The HttpUnit WebResponse.
    */
   public WebResponse getWebResponse()
   {
      return this.webResponse;
   }
   
   /**
    * Get the NamingContainer that this ClientFacade is currently 
    * workfing on.
    *
    * @return The current NamingContainer.
    */
   public String getNamingContainer()
   {
      return this.namingContainer;
   }
   
   /**
    * Set the NamingContainer that subsequent API calls will work with.
    *
    * @param namingContainer The name of the NamingContainer.
    */
   public void setNamingContainer(String namingContainer)
   {
      if (namingContainer == null) throw new NullPointerException("namingContainer can not be null");
      
      if (namingContainer.equals("")) 
      {
         this.namingContainer = "";
         return;
      }
      
      this.namingContainer = namingContainer;
   }
   
   // Tries to set the NamingContainer based on the first form id
   private void setNamingContainer() throws SAXException
   {
      WebForm[] forms = this.webResponse.getForms();
      if (forms.length == 0) 
      {
         setNamingContainer("");
         return;
      }
      
      WebForm form = forms[0];
      String id = form.getID();
      
      if (id == null) 
      {
         setNamingContainer("");
         return;
      }
      
      setNamingContainer(id);
   }
   
   private String makeComponentPath(String componentId)
   {
      if (this.namingContainer.equals("")) return componentId;
      
      return this.namingContainer + NamingContainer.SEPARATOR_CHAR + componentId;
   }
   
   /**
    * Return the HttpUnit WebForm with the Id of the current NamingContainer.
    *
    * @return The current WebForm.
    *
    * @throws SAXException if the current response page can not be parsed
    */
   public WebForm getForm() throws SAXException
   {
      return getWebResponse().getFormWithID(this.namingContainer);
   }
   
   /**
    * Set a parameter value on the current NamingContainer (must be a form).
    *
    * @param paramName The name of the parameter.
    * @param value The value.
    *
    * @throws SAXException if the current response page can not be parsed
    */
   public void setParameter(String paramName, String value) throws SAXException
   {
      getForm().setParameter(makeComponentPath(paramName), value);
   }
   
   /**
    * Finds the first submit button on the NamingContainer (form) and
    * submits the form.  If found, this method will set the NamingContainer to the
    * id of the first form on the new page.
    *
    * @throws IOException if there is a problem submitting the form.
    * @throws SAXException if the response page can not be parsed
    */
   public void submit() throws SAXException, IOException
   {
      this.webResponse = getForm().submit();
      setNamingContainer();
   }
   
   /**
    * Finds the named submit button on the NamingContainer (form) and
    * submits the form.  If found, this method will set the NamingContainer to the
    * id of the first form on the new page.
    *
    * @param componentId The id of the submit button to be "pressed".
    *
    * @throws IOException if there is a problem submitting the form.
    * @throws SAXException if the response page can not be parsed
    */
   public void submit(String componentId) throws SAXException, IOException
   {
      SubmitButton button = getForm().getSubmitButtonWithID(makeComponentPath(componentId));
      this.webResponse = getForm().submit(button);
      setNamingContainer();
   }
}
