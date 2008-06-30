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
import com.gargoylesoftware.htmlunit.WebWindowEvent;
import com.gargoylesoftware.htmlunit.WebWindowListener;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlIsIndex;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.html.HtmlSelectOneRadio;
import javax.faces.context.FacesContext;
import org.w3c.dom.Element;

/**
 *
 * @author Stan Silvert
 */
public class JSFClientSession implements WebWindowListener
{
   
   private JSFServerSession jsfServerSession;
   private Page contentPage;
   
   JSFClientSession(JSFServerSession jsfServerSession, Page initialPage)
   {
      this.jsfServerSession = jsfServerSession;
      this.contentPage = initialPage;
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
    * Get a DOM Element on the current page that has the given JSF componentID.
    *
    * @param componentID The JSF component id (or a suffix of the client ID)
    *
    * @return The Element, or <code>null</code> if not found.
    *
    * @throws DuplicateClientIDException if more than one client ID matches the suffix
    * @throws ClassCastException if the current page is not an HtmlPage. 
    */
   public Element getElement(String componentID)
   {
      DomNode domPage = (DomNode)this.contentPage;
      String xpathQuery = buildXPathQuery(componentID);
      List elements = domPage.getByXPath(xpathQuery);
      if (elements.size() == 0) return null;
      if (elements.size() == 1) return (Element)elements.get(0);
      throw new DuplicateClientIDException(elements, componentID);
   }
   
   private String buildXPathQuery(String componentID)
   {
      return "//*[" + endsWith("ID", componentID) + " or " + endsWith("id", componentID) + "]";
   }

   // XPath 1.0 doesn't have the ends-with function, so I have to make it myself
   private String endsWith(String attribute, String string)
   {
      return "('" + string + "' = substring(@" + attribute + ",string-length(@" + attribute + ") - string-length('" + string + "') + 1))";
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
    */
   public void setValue(String componentID, String value)
   {
      Element input = getElement(componentID);
      
      if (input instanceof HtmlInput) 
      {
         ((HtmlInput)input).setValueAttribute(value);
         return;
      }
      
      if (input instanceof HtmlTextArea)
      {
         ((HtmlTextArea)input).setText(value);
         return;
      }
      
      if (input instanceof HtmlIsIndex)
      {
         ((HtmlIsIndex)input).setValue(value);
         return;
      }
      
      throw new IllegalArgumentException("This method can not be used on components of type " + input.getClass().getName());
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
    */
   public void type(String componentID, char c) throws IOException
   {
      HtmlElement element = (HtmlElement)getElement(componentID);
      element.type(c);
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
    */
   public void click(String componentID) throws IOException
   {
      Element element = getElement(componentID);
      
      if ((element == null) && (parentIsHtmlSelect(componentID)))
      {
         clickSelect(componentID);
         return;
      }
      
      if ((element == null) && (parentIsSelectOneRadio(componentID)))
      {
         clickRadio(componentID);
         return;
      }
      
      if (element instanceof ClickableElement)
      {
         ((ClickableElement)element).click();
         return;
      }
      
      throw new IllegalArgumentException("This method can not be used on components of type " + element.getClass().getName());
   }
   
   // return true if parent JSF component is HtmlSelectOneRadio
   private boolean parentIsSelectOneRadio(String componentID)
   {
      String parentClientID = parentElementClientID(componentID);
      UIComponent parentComponent = jsfServerSession.findComponent(parentClientID);
      return (parentComponent instanceof HtmlSelectOneRadio);
   }
   
   // return true if parent html component is an HtmlSelect
   private boolean parentIsHtmlSelect(String componentID)
   {
      Element parentElement = getElement(parentElementClientID(componentID));
      return (parentElement instanceof HtmlSelect);
   }
   
   private String parentElementClientID(String componentID)
   {
      FacesContext facesContext = jsfServerSession.getFacesContext();
      UIComponent component = jsfServerSession.findComponent(componentID);
      return component.getParent().getClientId(facesContext);
   }
   
   private void clickRadio(String componentID) throws IOException
   {
      String itemValue = getSelectItemValue(componentID);
      String parentID = parentElementClientID(componentID);
      HtmlRadioButtonInput radioInput = findRadioInput(parentID, itemValue);
      radioInput.click();
   }
   
   private String getSelectItemValue(String componentID)
   {
      UIComponent uiComponent = jsfServerSession.findComponent(componentID);
      if (!(uiComponent instanceof UISelectItem)) 
      {
         throw new IllegalArgumentException(componentID + " is not a UISelectItem.");
      }
      
      return ((UISelectItem)uiComponent).getItemValue().toString();
   }
   
   private void clickSelect(String componentID) throws IOException
   {
      String parentID = parentElementClientID(componentID);
      
      Element element = getElement(parentID);
      HtmlSelect htmlSelect = (HtmlSelect)element;
      
      String optionValue = getSelectItemValue(componentID);
      
      HtmlOption htmlOption = htmlSelect.getOptionByValue(optionValue);
      htmlOption.click();
   }
   
   private HtmlRadioButtonInput findRadioInput(String componentID, String optionToSelect)
   {
      String clientID = jsfServerSession.getClientIDs().findClientID(componentID);
      HtmlPage htmlPage = (HtmlPage)this.contentPage;
      List<HtmlElement> elements = htmlPage.getHtmlElementsByName(clientID);
      for (Iterator<HtmlElement> i = elements.iterator(); i.hasNext();)
      {
         HtmlElement htmlElement = i.next();
         if ((htmlElement instanceof HtmlRadioButtonInput) && 
             (htmlElement.getAttribute("value").equals(optionToSelect)))
         {
            return (HtmlRadioButtonInput)htmlElement;
         }
      }
      
      return null;
   }
   
   // ------ Implementation of WebWindowListener
   public void webWindowOpened(WebWindowEvent webWindowEvent)
   {
   }

   public void webWindowContentChanged(WebWindowEvent webWindowEvent)
   {
      Page page = webWindowEvent.getNewPage();
      if(!(page instanceof HtmlPage)) 
      {
         this.contentPage = page;
         return;
      }
      
      HtmlPage ht = (HtmlPage)page;
      if( ht.getDocumentElement().hasChildNodes() )
      {
         this.contentPage = page;
      }
   }

   public void webWindowClosed(WebWindowEvent webWindowEvent)
   {
   }
}
