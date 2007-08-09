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
import com.meterware.httpunit.WebForm;
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
import org.ajax4jsf.framework.renderer.AjaxContainerRenderer;
import org.ajax4jsf.framework.renderer.AjaxRendererUtils;
import org.jboss.jsfunit.facade.ClientFacade;
import org.jboss.jsfunit.facade.ServerFacade;
import org.jboss.jsfunit.facade.WebRequestFactory;
import org.xml.sax.SAXException;

/**
 * This class creates Ajax4jsf HTTP requests for Ajax4jsf components.
 *
 * @author Stan Silvert
 */
public class Ajax4jsfClient
{
   private ClientFacade client;
   private WebRequestFactory requestFactory;
   
   public Ajax4jsfClient(ClientFacade client)
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
   private Map<UIData, Integer> setRowIndicies(ServerFacade server, String componentID) throws SAXException, IOException
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
    * Fire the AJAX event associated with this component.
    *
    * @param componentID The JSF component ID or a suffix of the client ID.
    *
    * @throws IOException if there is a problem sending the AJAX request.
    * @throws SAXException if the response page can not be parsed
    */
   public void fireAjaxEvent(String componentID) throws SAXException, IOException
   {
      ServerFacade server = new ServerFacade(client);
      Map<UIData, Integer> indiciesToRestore = setRowIndicies(server, componentID);
      
      UIComponent uiComp = server.findComponent(componentID);
      Map options = AjaxRendererUtils.buildEventOptions(server.getFacesContext(), uiComp);
      
      WebRequest req = requestFactory.makePostRequest((String)options.get("actionUrl"),
                                                      client.getForm(componentID));

      // Tell A4J that it's an AJAX request
      FacesContext ctx = server.getFacesContext();
      UIComponent container = (UIComponent)AjaxRendererUtils.findAjaxContainer(ctx, uiComp);
      req.setParameter(AjaxContainerRenderer.AJAX_PARAMETER_NAME, container.getClientId(ctx));
      
      // Add the extra A4J params
      Map params = (Map)options.get("parameters");
      for (Iterator i = params.keySet().iterator(); i.hasNext();)
      {
         String param = (String)i.next();
         req.setParameter(param, (String)params.get(param));
      }

      restoreRowIndices(indiciesToRestore);
      client.ajaxRequest(req);
   }
   
}
