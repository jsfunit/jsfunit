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

package org.jboss.jsfunit.richfaces;

import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebForm;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.xml.transform.TransformerException;
import org.ajax4jsf.renderkit.AjaxContainerRenderer;
import org.ajax4jsf.renderkit.AjaxRendererUtils;
import org.jboss.jsfunit.facade.ClientIDs;
import org.jboss.jsfunit.facade.DOMUtil;
import org.jboss.jsfunit.facade.JSFClientSession;
import org.jboss.jsfunit.facade.WebRequestFactory;
import org.richfaces.component.UITab;
import org.richfaces.component.UITabPanel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * When Ajax4jsf was merged into RichFaces, some classes were renamed and
 * placed in different packages.  Therefore, you need to use the RichFacesClient
 * instead of Ajax4jsfClient if you use RichFaces 3.1 or later.
 *
 * @author Stan Silvert
 */
public class RichFacesClient extends Ajax4jsfClient
{

   public RichFacesClient(JSFClientSession client)
   {
      super(client);
   }
   
   /**
    * The AjaxRendererUtils was placed in a different package for RichFaces 3.1
    * so we facotr it out here to allow the RichFacesClient to override.
    */
   @Override
   Map buildEventOptions(UIComponent uiComp)
   {
      FacesContext ctx = FacesContext.getCurrentInstance();
      return AjaxRendererUtils.buildEventOptions(ctx, uiComp);
   }
   
   /**
    * The AjaxRendererUtils and AjaxContainerRenderer were placed in a different 
    * package for RichFaces 3.1 so we facotr them out here to allow the 
    * RichFacesClient to override.
    */
   @Override
   void setA4JParam(PostMethodWebRequest req, UIComponent uiComp)
   {
      FacesContext ctx = FacesContext.getCurrentInstance();
      UIComponent container = (UIComponent)AjaxRendererUtils.findAjaxContainer(ctx, uiComp);
      req.setParameter(AjaxContainerRenderer.AJAX_PARAMETER_NAME, container.getClientId(ctx));
   }
   
   /**
    * Set a parameter value on a DataFilterSlider.
    *
    * @param componentID The JSF component ID or a suffix of the client ID.
    * @param value The value to set before the form is submitted.
    *
    * @throws SAXException if the current response page can not be parsed
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the 
    *                                    componentID suffix
    */
   public void setDataFilterSlider(String componentID, String value) throws SAXException
   {
      setSuffixxedValue(componentID, value, "slider_val");
   }
   
   /**
    * Set the value of a InputNumberSlider.
    *
    * @param componentID The JSF component ID or a suffix of the client ID.
    * @param value The value to set before the form is submitted.
    *
    * @throws SAXException if the current response page can not be parsed
    * @throws IOException if an internal refresh is needed and there is an 
    *                     error sending a request to the server.
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the 
    *                                    componentID suffix
    */
   public void setInputNumberSlider(String componentID, String value) 
         throws SAXException, IOException
   {
      String clientID = client.getClientIDs().findClientID(componentID);
      Document doc = client.getUpdatedDOM();
      Element input = DOMUtil.findElementWithID(clientID + "Input", doc);
      if (input.getAttribute("type").equals("hidden"))
      {
         input.setAttribute("type", "text");
         refreshPageFromDOM();
      }
      
      client.setParameter(clientID, clientID + "Input", clientID, value);
   }
   
   /**
    * Set the value of an InputNumberSpinner.
    *
    * @param componentID The JSF component ID or a suffix of the client ID.
    * @param value The value to set before the form is submitted.
    *
    * @throws SAXException if the current response page can not be parsed
    * @throws IOException if an internal refresh is needed and there is an 
    *                     error sending a request to the server.
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the 
    *                                    componentID suffix
    */
   public void setInputNumberSpinner(String componentID, String value)
         throws SAXException, IOException
   {
      String clientID = client.getClientIDs().findClientID(componentID);
      Element docElement = client.getUpdatedDOM().getDocumentElement();
      Element input = DOMUtil.findElementWithAttribValue("name", clientID, docElement);
      input.setAttribute("value", value);
      refreshPageFromDOM();
   }
   
   
   /**
    * Set a parameter value on a RichCalendar component.
    *
    * @param componentID The JSF component ID or a suffix of the client ID.
    * @param value The value to set before the form is submitted.
    *
    * @throws SAXException if the current response page can not be parsed
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the 
    *                                    componentID suffix
    */
   public void setCalendarValue(String componentID, String value)
         throws SAXException, IOException
   {
      String clientID = client.getClientIDs().findClientID(componentID);
      clientID += "InputDate";
      Document doc = client.getUpdatedDOM();
      Element input = DOMUtil.findElementWithID(clientID, doc);
      if (input.getAttribute("readonly").equals("true")) 
      {
         input.removeAttribute("readonly");
         refreshPageFromDOM();
      }
      
      setSuffixxedValue(componentID, value, "InputDate");
   }
   
   private void refreshPageFromDOM() throws SAXException, IOException
   {
      Document doc = client.getUpdatedDOM();
      try 
      {
         JSFAJAX.addResponseStringToSession(DOMUtil.docToHTMLString(doc));
         client.doWebRequest(JSFAJAX.createJSFUnitFilterRequest(getContentType()));
      } 
      catch (TransformerException e) 
      {
         throw new RuntimeException(e);
      }
      catch (MalformedURLException e)
      {
         throw new RuntimeException(e);
      }
   }
   
   private void setSuffixxedValue(String componentID, String value, String suffix)
         throws SAXException
   {
      String clientID = client.getClientIDs().findClientID(componentID);
      String renderedInputID = clientID + suffix;
      client.setParameter(clientID, renderedInputID, renderedInputID, value);
   }
   
   /**
    * Click a value on a DataTableScroller.
    *
    * @param componentID The JSF component ID or a suffix of the client ID.
    * @param value The number to click on the scroller.
    *
    * @throws SAXException if the current response page can not be parsed
    * @throws IOException if there is a problem submitting the form
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the 
    *                                    componentID suffix
    */
   public void clickDataTableScroller(String componentID, int value) 
         throws SAXException, IOException
   {
      clickDataTableScroller(componentID, Integer.toString(value));
   }
   
   /**
    * Click an arrow (control) on a DataTableScroller.
    *
    * @param componentID The JSF component ID or a suffix of the client ID.
    * @param value The control to click on the scroller.
    *
    * @throws SAXException if the current response page can not be parsed
    * @throws IOException if there is a problem submitting the form
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the 
    *                                    componentID suffix
    */
   public void clickDataTableScroller(String componentID, ScrollerControl control)
         throws SAXException, IOException
   {
      clickDataTableScroller(componentID, control.toString());
   }
   
   private void clickDataTableScroller(String componentID, String control)
         throws SAXException, IOException
   {
      String clientID = client.getClientIDs().findClientID(componentID);
      Map<String, String> params = new HashMap<String, String>(1);
      params.put(clientID, control);
      ajaxSubmit(clientID, params);
   }
   
   /**
    * Click a rich:panelMenuItem inside a rich:panelMenu.
    *
    * @param componentID The JSF component ID or a suffix of the client ID of
    *                    the rich:panelMenuItem to be clicked.
    *
    * @throws SAXException if the current response page can not be parsed
    * @throws IOException if there is a problem submitting the form
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the 
    *                                    componentID suffix
    */
   public void clickPanelMenuItem(String componentID)
         throws SAXException, IOException
   {
      String clientID = client.getClientIDs().findClientID(componentID);
      
      String selectedItem = clientID;
      if (selectedItem.contains(":"))
      {
         selectedItem = clientID.substring(clientID.lastIndexOf(':') + 1, clientID.length());
      }
      
      Map<String, String> params = new HashMap<String, String>(2);
      String formID = this.client.getForm(componentID).getID();
      params.put("panelMenuActionform:" + selectedItem, formID + ":" + selectedItem);
      params.put(formID + ":ajaxPanelMenuselectedItemName", selectedItem);
      ajaxSubmit(clientID, params);
   }
   
   /**
    * Drag a component with rich:dragSupport to a component with rich:dropSupport.
    *
    * @param dragComponentID The JSF component ID or a suffix of the client ID
    *                        for the rich:dragSupport component.
    * @param dropTargetComponentID The JSF component ID or a suffix of the client ID
    *                              for the target rich:dropSupport component.
    *
    * @throws SAXException if the current response page can not be parsed
    * @throws IOException if there is a problem submitting the form
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the 
    *                                    componentID suffix
    */
   public void dragAndDrop(String dragComponentID, String dropTargetComponentID)
         throws SAXException, IOException
   {
      String dragClientID = client.getClientIDs().findClientID(dragComponentID);
      String dropClientID = client.getClientIDs().findClientID(dropTargetComponentID);
      Map<String, String> params = new HashMap<String, String>(3);
      params.put("dragSourceId", dragClientID);
      params.put("dropTargetId", dropClientID);
      params.put(dragClientID, dragClientID);
      ajaxSubmit(dropClientID, params);
   }
   
   /**
    * Click a tab on a TabPanel.  Note that this method is a no-op if the tab
    * is in a disabled state or if the TabPanel is in client mode.  In client
    * mode, the tab click does not generate an ajax submit but only changes 
    * the visible tab already present on the client.  JSFUnit does not 
    * currently deal with these kinds of "client only" operations.
    *
    * @param tabPanelComponentID The JSF component ID or a suffix of the client ID
    *                            for the rich:tabPanel component.
    * @param tabPanelComponentID The JSF component ID or a suffix of the client ID
    *                            for the rich:tab component.
    *
    * @throws SAXException if the current response page can not be parsed
    * @throws IOException if there is a problem submitting the form
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the 
    *                                    componentID suffix
    */
   public void clickTab(String tabPanelComponentID, String tabComponentID)
         throws SAXException, IOException
   {
      UITab tab = (UITab)client.getClientIDs().findComponent(tabComponentID);
      if (tab.isDisabled()) return;
      
      UITabPanel panel = (UITabPanel)client.getClientIDs().findComponent(tabPanelComponentID);
      String switchType = panel.getSwitchType();
      if (switchType.equals("server")) clickServerTab(tabPanelComponentID, tabComponentID);
      if (switchType.equals("ajax")) clickAjaxTab(tabPanelComponentID, tabComponentID);
   }
   
   private void clickAjaxTab(String tabPanelComponentID, String tabComponentID)
         throws SAXException, IOException
   {
      ClientIDs clientIDs = client.getClientIDs();
      Map<UIData, Integer> indiciesToRestore = setRowIndicies(tabComponentID);
      
      UIComponent uiComp = clientIDs.findComponent(tabComponentID);
      Map options = buildEventOptions(uiComp);
      
      WebForm form = findFormForTabPanel(tabPanelComponentID);
      PostMethodWebRequest req = requestFactory.buildRequest(form);

      setA4JParam(req, uiComp);
      addExtraA4JParams(req, options);
      restoreRowIndices(indiciesToRestore);
      doAjaxRequest(req, options);
   }
   
   private void clickServerTab(String tabPanelComponentID, String tabComponentID)
         throws SAXException, IOException
   {
      String panelClientID = client.getClientIDs().findClientID(tabPanelComponentID);
      String tabClientID = client.getClientIDs().findClientID(tabComponentID);
      WebForm form = findFormForTabPanel(tabPanelComponentID);
      String formID = form.getID();
      WebRequestFactory reqFactory = new WebRequestFactory(client);
      PostMethodWebRequest postRequest = reqFactory.buildRequest(form);
      postRequest.setParameter(formID + ":_idcl", tabClientID);
      postRequest.setParameter(tabClientID + "_server_submit", 
                               tabClientID + "_server_submit");
      client.doWebRequest(postRequest);
   }
   
   private WebForm findFormForTabPanel(String tabPanelComponentID) throws SAXException
   {
      String tabPanelClientID = client.getClientIDs().findClientID(tabPanelComponentID);
      WebForm form = client.getWebResponse().getFormWithID(tabPanelClientID + ":_form");
      if (form != null) return form;
      
      return client.getForm(tabPanelClientID);
   }
   
}
