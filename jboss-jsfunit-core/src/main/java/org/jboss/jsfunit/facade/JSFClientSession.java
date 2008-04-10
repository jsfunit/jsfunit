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
import java.util.Iterator;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlOutputLink;
import javax.xml.transform.TransformerException;
import org.jboss.jsfunit.framework.WebConversationFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * The JSFClientSession provides a simplified API that wraps HttpUnit.  With a
 * single JSFClientSession object, you can simulate an entire user session as you
 * set parameters and submit data using submit() and clickCommandLink() methods.
 * 
 * 
 * @author Stan Silvert
 */
public class JSFClientSession
{
   private WebConversation webConversation;
   private WebResponse webResponse;
   private ClientIDs clientIDs;
   private Document updatedDOM;
   private WebRequestFactory requestFactory;
   
   /**
    * Creates a new client interface for testing the JSF application.   
    * This will also clear the HttpSession.
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
   public JSFClientSession(String initialPage) throws MalformedURLException, IOException, SAXException
   {
      this.webConversation = WebConversationFactory.makeWebConversation();
      doInitialRequest(initialPage);
   }
   
   /**
    * Creates a new client interface for testing a JSF application using a
    * customized WebConversation.  To use this constructor, first get a
    * WebConversation from org.jboss.jsfunit.framework.WebConversationFactory.
    * <br/><br/>
    * Example:<br/>
    * <code>
    * WebConversation webConv = WebConversationFactory.makeWebConversation();<br/>
    * webConv.setAuthorization("myuser", "mypassword");<br/>
    * webConv.setHeaderField("Accept-Language", "es-mx,es");<br/> 
    * JSFClientSession client = new JSFClientSession(webConv, "/index.jsf");<br/>
    * </code>
    * <br/>
    * Note that the initialPage param should be something that maps into the FacesServlet.
    * In the case where the FacesServlet is extension mapped in web.xml, this param will be something
    * like "/index.jsf" or "/index.faces".  If the FacesServlet is path-mapped then the
    * initialPage param will be something like "/faces/index.jsp".
    * 
    * 
    * @param webConversation A WebConversation object with "custom" attributes.
    * @param initialPage The page used to start a client session with JSF.  Example: "/index.jsf"
    * @throws IllegalArgumentException if the WebConversation did not come from the
    *                                  WebConversationFactory.
    * @throws MalformedURLException If the initialPage cannot be used to create a URL for the JSF app
    * @throws IOException If there is an error calling the JSF app
    * @throws SAXException If the response from the JSF app cannot be parsed as HTML
    */
   public JSFClientSession(WebConversation webConversation, String initialPage) 
        throws MalformedURLException, IOException, SAXException
   {
      if (!WebConversationFactory.isJSFUnitWebConversation(webConversation))
      {
          throw new IllegalArgumentException("WebConversation was not created with WebConversationFactory.");
      }
      
      this.webConversation = webConversation;
      doInitialRequest(initialPage);
   }
   
   // common code for constructors
   private void doInitialRequest(String initialPage)
           throws MalformedURLException, IOException, SAXException
   {
      WebRequest req = new GetMethodWebRequest(WebConversationFactory.getWARURL() + initialPage);
      doWebRequest(req);
      this.requestFactory = new WebRequestFactory(this);
   }
   
   /**
    * Get the ClientIDs helper class which provides useful information and
    * lookups for the components in the server-side component tree.
    */
   public ClientIDs getClientIDs()
   {
      return this.clientIDs;
   }
   
   /**
    * Package-private method to get the WebForm that contains the given 
    * component.
    *
    * @param componentID The ID of the component contained by the form, or the
    *                    ID of the form itself.
    *
    * @return The WebForm.
    *
    * @throws SAXException if the current response page can not be parsed
    * @throws ComponentIDNotFoundException if the component can not be found
    * @throws DuplicateClientIDException if more than one client ID matches the 
    *                                    componentID suffix
    * @throws FormNotFoundException if no form parameter can be found matching 
    *                               the componentID
    */
   public WebForm getForm(String componentID) throws SAXException
   {
      String clientID = this.clientIDs.findClientID(componentID);
      WebForm[] forms = getWebResponse().getForms();
      if (forms.length == 0) throw new FormNotFoundException(componentID);
      
      for (int i=0; i < forms.length; i++)
      {
         if (this.clientIDs.isAncestor(clientID, forms[i].getID()))
         {
            return forms[i];
         }
      } 
      
      throw new FormNotFoundException(componentID);
   } 
   
   /**
    * The method submits the WebRequest to the server using the WebConversation
    * of this JSFClientSession instance.  
    * 
    * At the end of this method, a new view from the server will be loaded so 
    * that you can continue to use this JSFClientSession instance to make further 
    * requests.
    * 
    * 
    * @param request The WebRequest
    * @throws IOException If there is an error calling the JSF app
    * @throws SAXException If the response from the JSF app cannot be parsed as 
    *                      HTML
    */
   public void doWebRequest(WebRequest request) throws SAXException, IOException
   {
      this.webResponse = this.webConversation.getResponse(request);
      updateInternalState();
      if (FaceletsErrorPageException.isFaceletsErrorPage(this))
      {
         throw new FaceletsErrorPageException(this);
      }
   }
    
   // update clientIDs and updatedDOM
   private void updateInternalState() throws SAXException
   {
      this.clientIDs = new ClientIDs();
      
      try 
      {
         this.updatedDOM = DOMUtil.convertToDomLevel2(this.webResponse.getDOM());
      }
      catch (TransformerException e)
      {
         throw new RuntimeException(e);
      }
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
    * Get the current state of the DOM including any changes made through
    * the setParameter() method.  WebResponse.getDOM() will only include the
    * state of the DOM after the last request was completed.
    */
   public Document getUpdatedDOM()
   {
      return this.updatedDOM;
   }
   
   /**
    * Get the HttpUnit WebConversation used by this instance.
    *
    * @return The WebConversation
    */
   public WebConversation getWebConversation()
   { 
      return this.webConversation;
   }
   
   private void setValueOnDOM(String clientID, String value) throws SAXException
   {
      Element element = DOMUtil.findElementWithID(clientID, this.updatedDOM);
      element.setAttribute("value", value);
   }
   
   /**
    * Set a parameter value on a form.
    *
    * @param componentID The JSF component ID or a suffix of the client ID.
    * @param value The value to set before the form is submitted.
    *
    * @throws SAXException if the current response page can not be parsed
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the 
    *                                    componentID suffix
    */
   public void setParameter(String componentID, String value) throws SAXException
   {
      String clientID = this.clientIDs.findClientID(componentID);
      getForm(clientID).setParameter(clientID, value);
      setValueOnDOM(clientID, value);
   }
   
   /**
    * Sets a parameter on a rendered input component.  This method is used for
    * setting values on custom components that render extra HTML input tags.
    *
    * @param clientID The fully-qualified JSF client ID of the component.
    * @param inputID The value of the id attribute of the extra input tag.
    * @param inputName The value of the name attribute of the extra input tag.
    * @param value The value to set before the form is submitted.
    *
    * @throws SAXException if the current response page can not be parsed
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the 
    *                                    componentID suffix
    */
   
   public void setParameter(String clientID, String inputID, String inputName, String value)
         throws SAXException
   {
      WebForm form = getForm(clientID);
      form.setParameter(inputName, value);
      setValueOnDOM(inputID, value);
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
    * @throws DuplicateClientIDException if more than one client ID matches the 
    *                                    componentID suffix
    * @throws IllegalArgumentException if the componentID does not resolve to a 
    *                                  checkbox control
    */
   public void setCheckbox(String componentID, boolean state) throws SAXException
   {
      String clientID = this.clientIDs.findClientID(componentID);
      getForm(clientID).setCheckbox(clientID, state);
      updateCheckboxInDOM(clientID, state);
   }
   
   private void updateCheckboxInDOM(String clientID, boolean state)
   {
      Element element = DOMUtil.findElementWithID(clientID, this.updatedDOM);
      Attr attr = element.getAttributeNode("checked");
      
      if ((attr == null) && !state) return;
      
      if ((attr != null) && !state) 
      {
         element.removeAttributeNode(attr);
         return;
      }
      
      if ((attr != null) && state) 
      {
         attr.setValue("checked");
         return;
      }
      
      if ((attr == null) && state)
      {
         Attr newAttr = this.updatedDOM.createAttribute("checked");
         newAttr.setValue("checked");
         element.setAttributeNode(newAttr);
      }
   }
   
   /**
    * Finds the lone form on the page and submits the form.
    *
    * At the end of this method call, the new view will be loaded so you can
    * perform tests on the next page.
    *
    * @throws IllegalStateException if page does not contain exactly one form.
    * @throws IOException if there is a problem submitting the form.
    * @throws SAXException if the response page can not be parsed
    */
   public void submit() throws SAXException, IOException
   {
      WebForm[] forms = getWebResponse().getForms();
      if (forms.length != 1) 
         throw new IllegalStateException("For this method, page must contain" +
                                         " only one form.  Use another " +
                                         "version of the submit() method.");
      doWebRequest(forms[0].getRequest());
   }
   
   /**
    * Finds the named submit button on a form and submits the form.  
    *
    * At the end of this method call, the new view will be loaded so you can
    * perform tests on the next page.
    *
    * @param componentID The JSF component id (or a suffix of the client ID) of 
    *                    the submit button to be "pressed".
    *
    * @throws IOException if there is a problem submitting the form.
    * @throws SAXException if the response page can not be parsed
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the 
    *                                    componentID suffix
    * @throws ComponentTypeException if the componentID does reference an
    *                                HtmlCommandButton JSF component.
    */
   public void submit(String componentID) throws SAXException, IOException
   {
      ComponentTypeException.check("submit(componentID)", componentID, HtmlCommandButton.class, clientIDs);
      String clientID = this.clientIDs.findClientID(componentID);
      WebForm form = getForm(clientID);
      SubmitButton button = form.getSubmitButtonWithID(clientID);
      doWebRequest(form.getRequest(button));
   }
   
   /**
    * Submits a form without pressing a button.  This is useful for testing
    * forms that are submitted via javascript.
    *
    * At the end of this method call, the new view will be loaded so you can
    * perform tests on the next page.
    *
    * @param componentID The JSF component id (or a suffix of the client ID) of 
    *                    a component on the form to be submitted.  This can also
    *                    be the ID of the form itself.
    *
    * @throws IOException if there is a problem submitting the form.
    * @throws SAXException if the response page can not be parsed
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the 
    *                                    componentID suffix
    */
   public void submitNoButton(String componentID) 
         throws SAXException, IOException
   {
      String clientID = this.clientIDs.findClientID(componentID);
      WebForm form = getForm(clientID);
      doWebRequest(form.getRequest());
   }
   
   /**
    * Finds the named link and clicks it.  This method is used to click static
    * links such as those produced by h:outputLink.  If you need to submit
    * a form using an h:commandLink, use clickCommandLink() instead.
    * 
    * At the end of this method call, a new page will be loaded.  So you can
    * use this JSFClientSession instance to do tests on the page.  However, static 
    * links typically do not call into the JSF servlet.  Therefore, you have
    * exited the realm of JSF.  In that case you will probably need a new 
    * JSFClientSession instance to do more JSF testing.
    * 
    * 
    * @param componentID The JSF component id (or a suffix of the client ID) of 
    *                    the link to be "clicked".
    * @throws IOException if there is a problem clicking the link.
    * @throws SAXException if the response page can not be parsed.
    * @throws ComponentIDNotFoundException if the component can not be found
    * @throws DuplicateClientIDException if more than one client ID matches the 
    *                                    componentID suffix
    * @throws ComponentTypeException if the componentID does reference an
    *                                HtmlOutputLink JSF component.
    */
   public void clickLink(String componentID) throws SAXException, IOException
   {
      ComponentTypeException.check("clickLink()", componentID, HtmlOutputLink.class, clientIDs);
      String clientID = this.clientIDs.findClientID(componentID);
      WebLink link = this.webResponse.getLinkWithID(clientID);
      if (link == null) throw new ComponentIDNotFoundException(componentID);
      this.webResponse = link.click();
      this.clientIDs = new ClientIDs();
   }
   
   /**
    * Finds the named command link and uses the link to submit its form.
    *
    * At the end of this method call, the new view will be loaded so you can
    * perform tests on the next page.
    *
    * @param componentID The JSF component id (or a suffix of the client ID) of 
    *                    the link to be "clicked".
    *
    * @throws IOException if there is a problem clicking the link.
    * @throws SAXException if the response page can not be parsed.
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the 
    *                                    componentID suffix
    * @throws ComponentTypeException if the componentID does reference an
    *                                HtmlCommandLink JSF component.
    */
   public void clickCommandLink(String componentID) 
         throws SAXException, IOException
   {
      ComponentTypeException.check("clickCommandLink()", componentID, HtmlCommandLink.class, clientIDs);
      WebRequest req = this.requestFactory.buildRequest(componentID);
      setCmdLinkParams(req, componentID);
      doWebRequest(req);
   }
   
   // sets only for MyFaces 1.1.4
   private void setFParams(WebRequest req, String componentID) 
         throws SAXException, IOException
   {
      JSFServerSession server = new JSFServerSession(this);
      UIComponent cmdLink = server.findComponent(componentID);
      if (cmdLink.getChildCount() == 0) return;
      
      for (Iterator i = cmdLink.getChildren().iterator(); i.hasNext();)
      {
         UIComponent child = (UIComponent)i.next();
         if (!child.isRendered()) continue;
         
         if (child instanceof UIParameter)
         {
            UIParameter uiParam = (UIParameter)child;
            req.setParameter(uiParam.getName(), (String)uiParam.getValue());
         }
      }
   }
   
   private void setCmdLinkParams(WebRequest req, String componentID) 
         throws SAXException, IOException
   {
      String clientID = this.clientIDs.findClientID(componentID);
      WebForm form = getForm(componentID);
      
      String myFaces115CmdLink = form.getID() + ":" + "_idcl";
      if (form.hasParameterNamed(myFaces115CmdLink))
      {
         req.setParameter(myFaces115CmdLink, clientID);
         setMyFacesCmdLinkParams(req, clientID);
         return;
      }
      
      String myFaces114CmdLink = form.getID() + ":" + "_link_hidden_";
      if (form.hasParameterNamed(myFaces114CmdLink))
      {
         req.setParameter(myFaces114CmdLink, clientID);
         setFParams(req, componentID);
         return;
      }
      
      setMojarraCmdLinkParams(req, clientID);
   }
   
   // MyFaces 1.1.5 only
   private void setMyFacesCmdLinkParams(WebRequest req, String clientID)
         throws SAXException, IOException
   {
      String script = this.webResponse.getElementWithID(clientID).getAttribute("onclick");
      if ( (!script.contains(",[['")) && (!script.contains("']]);")) ) return;
      script = script.substring(script.indexOf(",[[") + 2);
      script = script.substring(0, script.indexOf("']]);") + 2);
      
      String[] requestParams = script.split("'");
      for (int i=1; i < requestParams.length; i += 4)
      {
         String paramName = requestParams[i];
         String paramValue = requestParams[i + 2];
         req.setParameter(paramName, paramValue);
      }
   }
   
   // for Mojarra (JSF RI 1.2)
   private void setMojarraCmdLinkParams(WebRequest req, String clientID)
         throws SAXException, IOException
   {
      String script = this.webResponse.getElementWithID(clientID).getAttribute("onclick");
      script = script.substring(script.indexOf("{jsfcljs("));
      String[] jsfcljsParams = script.split(",'");
      String paramString = jsfcljsParams[1];
      
      // remove trailing single-quote (most cases)
      if (paramString.endsWith("'")) 
      {
         paramString = paramString.substring(0, paramString.length() - 1); 
      }
      else // last param empty so it looked like 'foo,bar,foo,bar,',''
      {
         paramString += ", "; // add a space to make next split work
      }  
      
      String[] requestParams = paramString.split(",");
      for (int i=0; i < requestParams.length; i += 2)
      {
         String paramName = requestParams[i];
         String paramValue = requestParams[i + 1];
         req.setParameter(paramName, paramValue);
      }
   }
}
