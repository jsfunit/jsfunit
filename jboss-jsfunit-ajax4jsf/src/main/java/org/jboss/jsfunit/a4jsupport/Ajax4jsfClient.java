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
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.ajax4jsf.framework.renderer.AjaxContainerRenderer;
import org.ajax4jsf.framework.renderer.AjaxRendererUtils;
import org.jboss.jsfunit.facade.JSFClientSession;
import org.jboss.jsfunit.facade.JSFServerSession;
import org.jboss.jsfunit.facade.WebRequestFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * This class creates Ajax4jsf HTTP requests for Ajax4jsf components.  It 
 * should be used for Ajax4jsf 1.1.
 *
 * @author Stan Silvert
 */
public class Ajax4jsfClient
{
   private JSFClientSession client;
   private WebRequestFactory requestFactory;
   
   private String ajaxResponse;
   
   public Ajax4jsfClient(JSFClientSession client)
   {
      if (client == null) throw new NullPointerException("client can not be null");
      this.client = client;
      this.requestFactory = new WebRequestFactory(client);
   }
   
   private String makeClientID(String[] terms, int treeDepth)
   {
      StringBuilder builder = new StringBuilder(terms[0]);
      for (int i=1; i < treeDepth; i++)
      {
         builder.append(NamingContainer.SEPARATOR_CHAR);
         builder.append(terms[i]);
      }
      
      return builder.toString();
   }
   
   /**
    * The A4J API calls used in this class are meant to be used during rendering.  At that time,
    * UIData components in the tree have their row indecies set for the child component
    * being processed.  To recreate that state, we go up the tree and find all the UIData components that are
    * ancestors of the component in question.  Then we save the old row index and set the UIData
    * component to the row for which our component belongs.
    *
    * @return A map of UIData components for which the row index has been changed, along with the
    *         original row index.
    */
   private Map<UIData, Integer> setRowIndicies(JSFServerSession server, String componentID) throws SAXException, IOException
   {
      String clientID = server.findClientID(componentID);
      
      Map<UIData,Integer> rowIndices = new HashMap<UIData,Integer>();
      String[] terms = clientID.split(Character.toString(NamingContainer.SEPARATOR_CHAR));
      for (int i=0; i < terms.length; i++)
      {
         try
         {
            int rowIndex = Integer.parseInt(terms[i]);
            UIComponent parent = server.findComponent(makeClientID(terms, i));
            if (parent instanceof UIData)
            {
               UIData uiData = (UIData)parent;
               rowIndices.put(uiData, new Integer(uiData.getRowIndex()));
               uiData.setRowIndex(rowIndex);
            }
         } 
         catch (NumberFormatException e)
         {
            // OK. Parent can't be a UIData
         }
      }
      
      return rowIndices;
   }
      
   /**
    * Restore all the UIData row indices to their original values.  Probably,
    * this will always be -1, but just in case...
    */   
   private void restoreRowIndices(Map<UIData, Integer> rowIndices)
   {
      for (Iterator<UIData> i = rowIndices.keySet().iterator(); i.hasNext();)
      {
         UIData uiData = i.next();
         uiData.setRowIndex(rowIndices.get(uiData).intValue());
      }
   } 

   /**
    * This method returns the last Ajax response.
    */
   public String getAjaxResponse()
   {
      return this.ajaxResponse;
   }
   
   /**
    * Fire the AJAX event associated with this component.  Most of the time,
    * you can call this simple version of the method.  See the AjaxEvent class
    * for other event options.
    *
    * @param componentID The JSF component ID or a suffix of the client ID.
    *
    * @throws IOException if there is a problem sending the AJAX request.
    * @throws SAXException if the response page can not be parsed
    */
   public void fireAjaxEvent(String componentID) 
         throws SAXException, IOException
   {
      fireAjaxEvent(new AjaxEvent(componentID));
   }
   
   /**
    * Fire the AJAX event associated with this component.  
    *
    * @param event The event definition.
    *
    * @throws IOException if there is a problem sending the AJAX request.
    * @throws SAXException if the response page can not be parsed
    */
   public void fireAjaxEvent(AjaxEvent event) 
         throws SAXException, IOException
   {
      JSFServerSession server = new JSFServerSession(client);
      String componentID = event.getComponentID();
      Map<UIData, Integer> indiciesToRestore = setRowIndicies(server, componentID);
      
      UIComponent uiComp = server.findComponent(componentID);
      FacesContext ctx = server.getFacesContext();
      Map options = buildEventOptions(ctx, uiComp);
      PostMethodWebRequest req = requestFactory.buildRequest((String)options.get("actionUrl"),
                                                             componentID);

      // Tell A4J that it's an AJAX request
      setA4JParam(req, ctx, uiComp);
      
      addExtraA4JParams(req, options);

      restoreRowIndices(indiciesToRestore);
      
      addExtraUserParams(req, event);
      
      Document oldDoc = client.getUpdatedDOM();
      client.doWebRequest(req);
      WebResponse newResponse = client.getWebResponse();
      
      this.ajaxResponse = client.getWebResponse().getText();

      try {
         WebRequest refreshRequest = JSFAJAX.processResponse(oldDoc, newResponse, options);
         if (refreshRequest != null) client.doWebRequest(refreshRequest);
      } catch (ParserConfigurationException e) {
         throw new RuntimeException(e);
      } catch (TransformerException e) {
         throw new RuntimeException(e);
      }
   }
   
   private void addExtraA4JParams(PostMethodWebRequest req, Map options)
   {
      Map params = (Map)options.get("parameters");
      for (Iterator i = params.keySet().iterator(); i.hasNext();)
      {
         String param = (String)i.next();
         req.setParameter(param, (String)params.get(param));
      }
   }
   
   private void addExtraUserParams(PostMethodWebRequest req, AjaxEvent event)
   {
      Map<String, String> params = event.getExtraRequestParams();
      for (Iterator<String> i = params.keySet().iterator(); i.hasNext();)
      {
         String name = i.next();
         String value = params.get(name);
         req.setParameter(name, value);
      }
   }
   
   /**
    * The AjaxRendererUtils was placed in a different package for RichFaces 3.1
    * so we facotr it out here to allow the RichFacesClient to override.
    */
   Map buildEventOptions(FacesContext ctx, UIComponent uiComp)
   {
      return AjaxRendererUtils.buildEventOptions(ctx, uiComp);
   }
   
   /**
    * The AjaxRendererUtils and AjaxContainerRenderer were placed in a different 
    * package for RichFaces 3.1 so we facotr them out here to allow the 
    * RichFacesClient to override.
    */
   void setA4JParam(PostMethodWebRequest req, FacesContext ctx, UIComponent uiComp)
   {
      UIComponent container = (UIComponent)AjaxRendererUtils.findAjaxContainer(ctx, uiComp);
      req.setParameter(AjaxContainerRenderer.AJAX_PARAMETER_NAME, container.getClientId(ctx));
   }

}
