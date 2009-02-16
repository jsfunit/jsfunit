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

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.util.List;

/**
 * Performs FORM authentication for JEE container-managed security.  Note that
 * the form-login-page and the form-error-page specified in web.xml don't have
 * to be JSF pages.<br/><br/>
 *
 * This class needs to know the user name and password to log in.  It also
 * needs to know the name of a submit button or other component that can
 * submit the form containing the login credentials.  Because this might not
 * be a JSF page, this class finds the "submit" component using the name 
 * attribute.  The default value is "Submit".
 *
 * @author Stan Silvert
 * @since 1.0
 */
public class FormAuthenticationStrategy extends SimpleInitialRequestStrategy
{

   private String userName;
   private String password;
   
   private String userNameComponent = "j_username";
   private String passwordComponent = "j_password";
   private String submitComponent = "Submit";
   
   /**
    * Create a new FormAuthenticationStrategy.  User name and password fields
    * default to standard JEE/Servlet naming standard.
    *
    * @param userName The user name.
    * @param password The password.
    * @param submitComponent The value of the name attribute for the submit component.
    */
   public FormAuthenticationStrategy(String userName, String password, String submitComponent)
   {
      this.userName = userName;
      this.password = password;
      this.submitComponent = submitComponent;
   }
   
   /**
    * Create a new FormAuthenticationStrategy.  Typically, this constructor is subclassed.
    *
    * @param userName The user name.
    * @param password The password.
    * @param submitComponent The value of the name attribute for the submit component.
    * @param userNameComponent The value of the name attribute for the user name input.
    * @param passwordComponent The value of the name attribute for the password input.
    */
   public FormAuthenticationStrategy(String userName, String password, 
                                     String submitComponent, String userNameComponent, String passwordComponent)
   {
      this(userName, password, submitComponent);
      this.userNameComponent = userNameComponent;
      this.passwordComponent = passwordComponent;
   }
   
   /**
    * Perform the initial request and provide FORM authentication 
    * credentials when challenged.
    *
    * @param wcSpec The WebClient specification.
    *
    * @return The requested page if authentication passed.  Otherwise, return
    *         the error page specified in web.xml.
    */
   public Page doInitialRequest(WebClientSpec wcSpec) throws IOException
   {
      HtmlPage page = (HtmlPage)super.doInitialRequest(wcSpec);
      setValue(page, this.userNameComponent, this.userName);
      setValue(page, this.passwordComponent, this.password);
      return clickSubmitComponent(page);
   }
   
   protected Page clickSubmitComponent(HtmlPage page) throws IOException
   {
      HtmlElement htmlElement = getElement(page, this.submitComponent);
      if (!(htmlElement instanceof ClickableElement))
      {
         throw new IllegalStateException("Component with name=" + this.submitComponent + " is not a ClickableElement.");
      }
      
      ClickableElement clickable = (ClickableElement)htmlElement;
      return clickable.click();
   }
   
   protected void setValue(HtmlPage page, String elementName, String value)
   {
      HtmlElement element = getElement(page, elementName);
      if (!(element instanceof HtmlInput))
      {
         throw new IllegalStateException("Component with name=" + elementName + " is not an HtmlInput element.");
      }
      HtmlInput inputElement = (HtmlInput)element;
      inputElement.setValueAttribute(value);
   }
   
   protected HtmlElement getElement(HtmlPage page, String elementName)
   {
      List<HtmlElement> elements = page.getHtmlElementsByName(elementName);
      if (elements.size() == 0) 
      {
         throw new IllegalStateException("Component with name=" + elementName + " was not found on the login page.");
      } 
      if (elements.size() > 1) 
      {
         throw new IllegalStateException("More than one component with name=" + elementName + " was found on the login page.");
      }
      
      return elements.get(0);
   }
   
}
