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

package org.jboss.jsfunit.a4jsupport;

import com.meterware.httpunit.PostMethodWebRequest;
import java.io.IOException;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.ajax4jsf.renderkit.AjaxContainerRenderer;
import org.ajax4jsf.renderkit.AjaxRendererUtils;
import org.jboss.jsfunit.facade.JSFClientSession;
import org.xml.sax.SAXException;

/**
 * When Ajax4jsf was merged into RichFaces, some classes were renamed and
 * placed in different packages.  Therefore, you need to use the RichFacesClient
 * instead of Ajax4jsfClient if you use RichFaces 3.1 or later.
 *
 * @author ssilvert
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
   Map buildEventOptions(FacesContext ctx, UIComponent uiComp)
   {
      return AjaxRendererUtils.buildEventOptions(ctx, uiComp);
   }
   
   /**
    * The AjaxRendererUtils and AjaxContainerRenderer were placed in a different 
    * package for RichFaces 3.1 so we facotr them out here to allow the 
    * RichFacesClient to override.
    */
   @Override
   void setA4JParam(PostMethodWebRequest req, FacesContext ctx, UIComponent uiComp)
   {
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
   public void setSliderValue(String componentID, String value) throws SAXException
   {
      String renderedInputID = client.getClientIDs().findClientID(componentID);
      renderedInputID += "slider_val";
      client.setParameter(componentID, renderedInputID, value);
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
   
   private void clickDataTableScroller(String componentID, String value)
         throws SAXException, IOException
   {
      AjaxEvent event = new AjaxEvent(componentID);
      String clientID = client.getClientIDs().findClientID(componentID);
      event.setExtraRequestParam(clientID, value);
      fireAjaxEvent(event);
   }
}
