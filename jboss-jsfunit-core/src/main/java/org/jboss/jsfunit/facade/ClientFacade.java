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
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import java.io.IOException;
import java.net.MalformedURLException;
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
   private ClientIDs clientIDs;
   
   /**
    * Creates a new client interface for testing the JSF application.   
    * This will also start a new HttpSession.
    * 
    * Note that the initialPage param should be something that maps into the FacesServlet.
    * In the case where the FacesServlet is extension mapped in web.xml, this param will be something
    * like "/index.jsf" or "/index.faces".  If the FacesServlet is path-mapped then the
    * initialPage param will be something like "/faces/index.jsp".
    * 
    * @param initialPage The page used to start a client session with JSF.  Example: "/index.jsf"
    *
    * @throws MalformedURLException If the initialPage cannot be used to create a URL for the JSF app
    * @throws IOException If there is an error calling the JSF app
    * @throws SAXException If the response from the JSF app cannot be parsed as HTML
    */
   public ClientFacade(String initialPage) throws MalformedURLException, IOException, SAXException
   {
      WebConversation webConversation = WebConversationFactory.makeWebConversation();
      WebRequest req = new GetMethodWebRequest(WebConversationFactory.getWARURL() + initialPage);
      this.webResponse = webConversation.getResponse(req);
      this.clientIDs = new ClientIDs();
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
    * Return the HttpUnit WebForm with the Id of the current NamingContainer.
    *
    * @param componentID The id of the <h:form> component.
    *
    * @return The current WebForm.
    *
    * @throws SAXException if the current response page can not be parsed
    * @throws ComponentIDNotFoundException if the form can not be found on the page
    * @throws DuplicateClientIDException if more than one client ID matches the componentID suffix
    */
   public WebForm getForm(String componentID) throws SAXException
   {
      String clientID = this.clientIDs.find(componentID);
      WebForm[] forms = getWebResponse().getForms();
      if (forms.length == 0) throw new ComponentIDNotFoundException(componentID);
      
      for (int i=0; i < forms.length; i++)
      {
         if (forms[i].hasParameterNamed(clientID)) return forms[i];
      }
      
      throw new ComponentIDNotFoundException(componentID);
   }
   
   /**
    * Set a parameter value on the current NamingContainer (must be a form).
    *
    * @param componentID The JSF component ID or a suffix of the client ID.
    * @param value The value to set before the form is submitted.
    *
    * @throws SAXException if the current response page can not be parsed
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the componentID suffix
    */
   public void setParameter(String componentID, String value) throws SAXException
   {
      String clientID = this.clientIDs.find(componentID);
      getForm(clientID).setParameter(clientID, value);
   }
   
   /**
    * Finds the first submit button on the form and submits the form.  
    *
    * @throws IllegalStateException if page does not contain a single form.
    * @throws IOException if there is a problem submitting the form.
    * @throws SAXException if the response page can not be parsed
    */
   public void submit() throws SAXException, IOException
   {
      WebForm[] forms = getWebResponse().getForms();
      if (forms.length != 1) throw new IllegalStateException("For this method, page must contain only one form.  Use another version of the submit() method.");
      
      this.webResponse = forms[0].submit();
      this.clientIDs = new ClientIDs();
   }
   
   /**
    * Finds the named submit button on a form and submits the form.  
    *
    * @param componentID The JSF component id (or a suffix of the client ID) of the submit button to be "pressed".
    *
    * @throws IOException if there is a problem submitting the form.
    * @throws SAXException if the response page can not be parsed
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the componentID suffix
    */
   public void submit(String componentID) throws SAXException, IOException
   {
      String clientID = this.clientIDs.find(componentID);
      WebForm form = getForm(clientID);
      SubmitButton button = form.getSubmitButtonWithID(clientID);
      this.webResponse = form.submit(button);
      this.clientIDs = new ClientIDs();
   }
   
   /**
    * Finds the named link and clicks it.
    *
    * @param componentID The JSF component id (or a suffix of the client ID) of the link to be "clicked".
    *
    * @throws IOException if there is a problem clicking the link.
    * @throws SAXException if the response page can not be parsed.
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the componentID suffix
    */
   public void click(String componentID) throws SAXException, IOException
   {
      String clientID = this.clientIDs.find(componentID);
      WebLink link = this.webResponse.getLinkWithID(clientID);
      if (link == null) throw new ComponentIDNotFoundException(componentID);
      this.webResponse = link.click();
      this.clientIDs = new ClientIDs();
   }
}
