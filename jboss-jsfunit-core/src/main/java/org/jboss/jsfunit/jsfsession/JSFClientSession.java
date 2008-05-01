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
import com.gargoylesoftware.htmlunit.html.ClickableElement;
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
   private Page currentPage;
   
   JSFClientSession(JSFServerSession jsfServerSession)
   {
      this.jsfServerSession = jsfServerSession;
   }
   
   public Page getCurrentPage()
   {
      return this.currentPage;
   }
   
   public String getPageText()
   {
      if (currentPage instanceof HtmlPage) return ((HtmlPage)currentPage).asXml();
      if (currentPage instanceof TextPage) return ((TextPage)currentPage).getContent();
      if (currentPage instanceof XmlPage) return ((XmlPage)currentPage).asXml();
      if (currentPage instanceof JavaScriptPage) return ((JavaScriptPage)currentPage).getContent();
      
      throw new IllegalStateException("This page can not be converted to text.  Page type is " + currentPage.getClass().getName());
   }
   
   /**
    * Set the value attribute of a JSF component.
    * 
    * @param componentID The JSF component id (or a suffix of the client ID) of 
    *                    a component on the form to be submitted.  This can also
    *                    be the ID of the form itself.
    *
    * @throws ComponentIDNotFoundException if no client ID matches the suffix
    * @throws DuplicateClientIDException if more than one client ID matches the suffix
    * @throws ClassCastException if the current page is not an HtmlPage
    */
   public void setValue(String componentID, String value)
   {
      String clientID = jsfServerSession.getClientIDs().findClientID(componentID);
      HtmlPage htmlPage = (HtmlPage)this.currentPage;
      Element element = htmlPage.getElementById(clientID);
      element.setAttribute("value", value);
   }
   
   public void click(String componentID) throws IOException
   {
      String clientID = jsfServerSession.getClientIDs().findClientID(componentID);
      HtmlPage htmlPage = (HtmlPage)this.currentPage;
      ClickableElement element = (ClickableElement)htmlPage.getElementById(clientID);
      element.click();
   }
   
   // ------ Implementation of PageCreationListener
   public void pageCreated(Page page)
   {
      this.currentPage = page;
   }
}
