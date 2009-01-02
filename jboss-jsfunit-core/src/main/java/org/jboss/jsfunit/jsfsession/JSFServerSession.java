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

package org.jboss.jsfunit.jsfsession;

import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebResponse;
import java.util.Iterator;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import org.jboss.jsfunit.context.JSFUnitFacesContext;
import org.jboss.jsfunit.framework.FacesContextBridge;
import org.jboss.jsfunit.framework.RequestListener;

/**
 * The JSFServerSession provides a simplified API that wraps parts of the JSF API 
 * for things that you would commonly do in testing.
 * 
 * @author Stan Silvert
 * @since 1.0
 */
public class JSFServerSession implements RequestListener
{
   private ClientIDs clientIDs;
   private FacesContext currentFacesContext;
   
   /**
    * Create a new JSFServerSession.
    * 
    * @param client The JSFClientSession for the current web conversation.
    */
   JSFServerSession()
   {
      pageCreated();
   }
   
   public ClientIDs getClientIDs()
   {
      return this.clientIDs;
   }
   
   /**
    * Return the current view ID from the component tree.
    *
    * @return The current View ID.
    */
   public String getCurrentViewID()
   {
      return getFacesContext().getViewRoot().getViewId();
   }
   
   /**
    * Get the FacesContext object used in the last request.
    *
    * @return The FacesContext object used in the last request.
    */
   public FacesContext getFacesContext()
   {
      return FacesContextBridge.getCurrentInstance();
   }
   
   /**
    * Find a component in the JSF component tree.  
    *
    * @param componentID The JSF component ID or client ID suffix.
    *
    * @return The component.
    *
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the componentID suffix
    */
   public UIComponent findComponent(String componentID)
   {
      return clientIDs.findComponent(componentID);
   }
   
   /**
    * Find a component in the JSF component tree and return its value.
    * Note that the found component must implement ValueHolder.
    *
    * @param componentID The JSF component ID or client ID suffix.
    *
    * @return The value contained in the component.
    *
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the componentID suffix
    * @throws ClassCastException if the found component does not implement ValueHolder
    */
   public Object getComponentValue(String componentID)
   {
      UIComponent component = findComponent(componentID);
      if (!(component instanceof ValueHolder))
      {
         throw new ClassCastException(componentID + " must be an instance of ValueHolder.");
      }
      
      return ((ValueHolder)component).getValue();
   }
   
   /**
    * Evaluate an EL ValueExpression and return the value.
    *
    * @param elExpression The expression.
    *
    * @return The value.
    */
   public Object getManagedBeanValue(String elExpression)
   {
      FacesContext facesContext = getFacesContext();
      return facesContext.getApplication()
                         .createValueBinding(elExpression)
                         .getValue(facesContext);
   }
   
   /**
    * Return all the FacesMessages generated with the last JSF request.
    *
    * @param componentID The JSF component ID or client ID suffix.
    *
    * @return The FacesMessages
    */
   public Iterator<FacesMessage> getFacesMessages()
   {
      return getFacesContext().getMessages();
   }
   
   /**
    * Return all the FacesMessages generated for a component in the last 
    * JSF request.
    *
    * @param componentID The JSF component ID or client ID suffix.
    *
    * @return the FacesMessages.
    *
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the componentID suffix
    */
   public Iterator<FacesMessage> getFacesMessages(String componentID)
   {
      String clientID = this.clientIDs.findClientID(componentID);
      return getFacesContext().getMessages(clientID);
   }

   
   
   
   private void pageCreated()
   {
      // Note that the FacesContextBridge not only provides us with the FacesContext, 
      // it also associates the FacesContext with the JSFUnit thread so that 
      // FacesContext.getCurrentInstance() will work.
      JSFUnitFacesContext facesContext = (JSFUnitFacesContext)FacesContextBridge.getCurrentInstance();
      
      // if no FacesContext exists, we can't get the Client IDs
      if (facesContext == null) return;
      
      // Peformance optimization.  If the FacesContext instance didn't change,
      // there is no need to re-create the ClientIDs.
      if (this.currentFacesContext != facesContext)
      {
         this.clientIDs = new ClientIDs();  
         this.currentFacesContext = facesContext;
      }
   }

   //----------- Implementation of RequestListener
   public void beforeRequest(WebRequestSettings webRequestSettings)
   {
   }

   public void afterRequest(WebResponse webResponse)
   {
      pageCreated();
   }

   
}
