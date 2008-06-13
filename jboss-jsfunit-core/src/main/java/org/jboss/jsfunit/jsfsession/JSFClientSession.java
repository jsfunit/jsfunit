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

package org.jboss.jsfunit.jsfsession;

import com.gargoylesoftware.htmlunit.JavaScriptPage;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.ThreadManager;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import java.io.IOException;
import org.w3c.dom.Element;

/**
 *
 * @author Stan Silvert
 */
public class JSFClientSession implements PageCreationListener
{
   
   private JSFServerSession jsfServerSession;
   private Page contentPage;
   private long javascriptTimeout = 10000;
   
   JSFClientSession(JSFServerSession jsfServerSession, Page initialPage)
   {
      this.jsfServerSession = jsfServerSession;
      pageCreated(initialPage); 
   }
   
   /**
    * Get the javascript timeout.  This determines how long the JSFClientSession
    * will wait for javascript to complete after an AJAX call.
    *
    * @return The javascript timeout in milliseconds.
    */
   public long getJavascriptTimeout()
   {
      return this.javascriptTimeout;
   }
   
   /**
    * Set the javascript timeout.  This determines how long the JSFClientSession
    * will wait for javascript to complete after an AJAX call.
    *
    * @param javascriptTimeout The timeout in milliseconds.
    */
   public void setJavascriptTimeout(long javascriptTimeout)
   {
      this.javascriptTimeout = javascriptTimeout;
   }
   
   /**
    * Get the latest content page returned from the server.  This page may
    * have been changed by javascript or direct manipulation of the DOM.
    *
    * @return The Page.
    */
   public Page getContentPage()
   {
      return this.contentPage;
   }
   
   /**
    * Get the content page as a text String.
    *
    * @return the text
    */
   public String getPageAsText()
   {
      if (contentPage instanceof HtmlPage) return ((HtmlPage)contentPage).asXml();
      if (contentPage instanceof TextPage) return ((TextPage)contentPage).getContent();
      if (contentPage instanceof XmlPage) return ((XmlPage)contentPage).asXml();
      if (contentPage instanceof JavaScriptPage) return ((JavaScriptPage)contentPage).getContent();
      
      throw new IllegalStateException("This page can not be converted to text.  Page type is " + contentPage.getClass().getName());
   }
   
   /**
    * Set the value attribute of a JSF component.
    * 
    * @param componentID The JSF component id (or a suffix of the client ID) of 
    *                    a component rendered as an HtmlInput component. 
    *
    * @throws ComponentIDNotFoundException if no client ID matches the suffix
    * @throws DuplicateClientIDException if more than one client ID matches the suffix
    * @throws ClassCastException if the current page is not an HtmlPage or the 
    *                            specified component is not an HtmlInput.
    * @throws JavascriptTimeoutException if this action triggered javascript that did not complete
    */
   public void setValue(String componentID, String value)
   {
      HtmlInput input = (HtmlInput)getElement(componentID);
      input.setValueAttribute(value);
      waitForJavascript();
   }
   
   /**
    * Simulates typing a character while this JSF component has focus.
    *
    * @param componentID The JSF component id (or a suffix of the client ID) of 
    *                    a component rendered as an HtmlElement. 
    *
    * @throws ComponentIDNotFoundException if no client ID matches the suffix
    * @throws DuplicateClientIDException if more than one client ID matches the suffix
    * @throws ClassCastException if the current page is not an HtmlPage or the 
    *                            specified component is not an HtmlElement.
    * @throws JavascriptTimeoutException if this action triggered javascript that did not complete
    */
   public void type(String componentID, char c) throws IOException
   {
      HtmlElement element = (HtmlElement)getElement(componentID);
      element.type(c);
      waitForJavascript();
   }
   
   /**
    * Click a JSF component.
    * 
    * @param componentID The JSF component id (or a suffix of the client ID) to be clicked.
    *
    * @throws ComponentIDNotFoundException if no client ID matches the suffix
    * @throws DuplicateClientIDException if more than one client ID matches the suffix
    * @throws ClassCastException if the current page is not an HtmlPage or the 
    *                            specified component is not a ClickableElement.
    * @throws JavascriptTimeoutException if this action triggered javascript that did not complete
    */
   public void click(String componentID) throws IOException
   {
      ClickableElement element = (ClickableElement)getElement(componentID);
      element.click();
      waitForJavascript();
   }
   
   /**
    * Set the "checked" attribute for a JSF checkbox component.
    * 
    * @param componentID The JSF component id (or a suffix of the client ID) to be clicked.
    *
    * @throws ComponentIDNotFoundException if no client ID matches the suffix
    * @throws DuplicateClientIDException if more than one client ID matches the suffix
    * @throws ClassCastException if the current page is not an HtmlPage or the 
    *                            specified component is not an HtmlCheckBoxInput.
    * @throws JavascriptTimeoutException if this action triggered javascript that did not complete
    */
   public void setChecked(String componentID, boolean isChecked) throws IOException
   {
      HtmlCheckBoxInput element = (HtmlCheckBoxInput)getElement(componentID);
      element.setChecked(isChecked);
      waitForJavascript();
   }
   
   public void waitForJavascript()
   {
      ThreadManager threadManager = this.contentPage.getEnclosingWindow().getThreadManager();
      if (!threadManager.joinAll(this.javascriptTimeout))
      {
         throw new JavascriptTimeoutException(threadManager.toString());
      }
   }
   
   /**
    * Get a DOM Element on the current page that has the given JSF componentID.
    *
    * @param componentID The JSF component id (or a suffix of the client ID)
    *
    * @throws ComponentIDNotFoundException if no client ID matches the suffix
    * @throws DuplicateClientIDException if more than one client ID matches the suffix
    * @throws ClassCastException if the current page is not an HtmlPage. 
    * @throws JavascriptTimeoutException if this action triggered javascript that did not complete
    */
   public Element getElement(String componentID)
   {
      String clientID = jsfServerSession.getClientIDs().findClientID(componentID);
      HtmlPage htmlPage = (HtmlPage)this.contentPage;
      return htmlPage.getElementById(clientID);
   }
   
   // ------ Implementation of PageCreationListener
   public void pageCreated(Page page)
   {
      this.contentPage = page;
   }
}
