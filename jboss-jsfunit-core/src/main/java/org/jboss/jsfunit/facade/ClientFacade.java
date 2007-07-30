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
   private WebConversation webConversation;
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
      this.webConversation = WebConversationFactory.makeWebConversation();
      WebRequest req = new GetMethodWebRequest(WebConversationFactory.getWARURL() + initialPage);
      this.webResponse = webConversation.getResponse(req);
      this.clientIDs = new ClientIDs();
   }
   
   /**
    * Protected method used by ServerFacade
    */
   protected ClientIDs getClientIDs()
   {
      return this.clientIDs;
   }
   
   /**
    * Get the HttpUnit WebResponse object from the latest request.
    *
    * @return The HttpUnit WebResponse.
    */
   public WebResponse getWebResponse()
   {
      return this.webResponse;
   }
   
   /**
    * Return the HttpUnit WebForm that contains the given component.
    *
    * @param componentID The id of the component contained by the form.
    *
    * @return The WebForm.
    *
    * @throws SAXException if the current response page can not be parsed
    * @throws ComponentIDNotFoundException if the component can not be found
    * @throws DuplicateClientIDException if more than one client ID matches the componentID suffix
    * @throws FormNotFoundException if no form parameter can be found matching the componentID
    */
   public WebForm getForm(String componentID) throws SAXException
   {
      String clientID = this.clientIDs.findClientID(componentID);
      WebForm[] forms = getWebResponse().getForms();
      if (forms.length == 0) throw new FormNotFoundException(componentID);
      
      for (int i=0; i < forms.length; i++)
      {
         if (clientID.startsWith(forms[i].getID())) return forms[i];
      }
      
      throw new FormNotFoundException(componentID);
   } 
   
   /**
    * Set a parameter value on a form.
    *
    * @param componentID The JSF component ID or a suffix of the client ID.
    * @param value The value to set before the form is submitted.
    *
    * @throws SAXException if the current response page can not be parsed
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the componentID suffix
    */
   public void setParameter(String componentID, String... value) throws SAXException
   {
      String clientID = this.clientIDs.findClientID(componentID);
      getForm(clientID).setParameter(clientID, value);
   }
   
   /**
    * Set a checkbox value on a form.  This method is needed because
    * setParameter can not "uncheck" a checkbox.
    *
    * @param componentID The JSF component ID or a suffix of the client ID.
    * @param value The value to set before the form is submitted.
    *
    * @throws SAXException if the current response page can not be parsed
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the componentID suffix
    * @throws IllegalArgumentException if the componentID does not resolve to a checkbox control
    */
   public void setCheckbox(String componentID, boolean state) throws SAXException
   {
      String clientID = this.clientIDs.findClientID(componentID);
      getForm(clientID).setCheckbox(clientID, state);
   }
   
   /**
    * Finds the lone form on the page and submits the form.
    *
    * @throws IllegalStateException if page does not contain exactly one form.
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
      String clientID = this.clientIDs.findClientID(componentID);
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
   public void clickLink(String componentID) throws SAXException, IOException
   {
      String clientID = this.clientIDs.findClientID(componentID);
      WebLink link = this.webResponse.getLinkWithID(clientID);
      if (link == null) throw new ComponentIDNotFoundException(componentID);
      this.webResponse = link.click();
      this.clientIDs = new ClientIDs();
   }
   
   /**
    * Sends WebRequest to the server and then does a "refresh".  
    * 
    * Each AJAX-enabled component library has its own "protocol" for sending AJAX requests to the 
    * server via javascript.  Because JSFUnit/HttpUnit can't reliably handle javascript, each 
    * AJAX-enabled JSF component library must prepare the WebRequest and submit it through this method.
    *
    * When an AJAX request is sent through this method, a second "refresh" request is submitted to the
    * server.  This allows the client to sync up with the component tree on the server side.  Therefore,
    * AJAX changes that are "client side only" will not be preserved.
    *
    * @param request A request prepared using the AJAX JSF component library's required params.
    *
    * @throws IOException if there is a problem submitting the request.
    * @throws SAXException if the response page can not be parsed.
    */
   public void ajaxRequest(WebRequest request) throws SAXException, IOException
   {
      this.webResponse = this.webConversation.getResponse(request);
      String url = this.webResponse.getURL().toString();
      this.webResponse = this.webConversation.getResponse(new GetMethodWebRequest(url));
      this.clientIDs = new ClientIDs();
   }
}
