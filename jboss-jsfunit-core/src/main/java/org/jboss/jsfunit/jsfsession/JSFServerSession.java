/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
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

import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebResponse;
import java.util.Iterator;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.jboss.jsfunit.context.JSFUnitFacesContext;
import org.jboss.jsfunit.context.NoNewEntryMap;
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
   
   /**
    * Get the immutable ClientIDs object.  This is typically used only by
    * JSFUnit.
    * 
    * @return The ClientIDs object
    * @see org.jboss.jsfunit.jsfsession.ClientIDs
    */
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
      return clientIDs.getComponentValue(componentID);
   }
   
   /**
    * Evaluate an EL ValueExpression and return the value.
    *
    * @param elExpression The expression.
    *
    * @return The value. Return <code>null</code> if the managed bean is
    *         request, session, or application scope and does not yet exist.
    */
   public Object getManagedBeanValue(String elExpression)
   {
      FacesContext facesContext = getFacesContext();
      
      try
      {
         setELRunning(true, facesContext.getExternalContext());
         return facesContext.getApplication()
                            .createValueBinding(elExpression)
                            .getValue(facesContext);
      }
      catch (RuntimeException re)
      {  // JSFUNIT-164
         Throwable chain = re;
         do
         {
            if (chain instanceof NoNewEntryMap.NewEntryNotAllowedException)
            {
               return null;
            }
            chain = chain.getCause();
         }
         while (chain != null);
         
         throw re;
      }
      finally
      {
         setELRunning(false, facesContext.getExternalContext());
      }
   }
   
   // This tells the Map to throw an exception if an EL expression tries to
   // create a new managed bean in request, session, or applicaiton scope.
   // In a JSF request, we want the bean to be created by EL.  But in a test we 
   // want to know that the previous request DID NOT create it.
   private void setELRunning(boolean isELRunning, ExternalContext extContext)
   {
      NoNewEntryMap requestMap = (NoNewEntryMap)extContext.getRequestMap();
      requestMap.setELRunning(isELRunning);
      
      NoNewEntryMap sessionMap = (NoNewEntryMap)extContext.getSessionMap();
      sessionMap.setELRunning(isELRunning);
      
      NoNewEntryMap applicationMap = (NoNewEntryMap)extContext.getApplicationMap();
      applicationMap.setELRunning(isELRunning);
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
      
      // if no FacesContext exists, we can're get the Client IDs
      if (facesContext == null) return;
      
      // Peformance optimization.  If the FacesContext instance didn're change,
      // there is no need to re-create the ClientIDs.
      if (this.currentFacesContext != facesContext)
      {
         this.clientIDs = new ClientIDs();  
         this.currentFacesContext = facesContext;
      }
   }

   //----------- Implementation of RequestListener
   public void beforeRequest(WebRequest webRequest)
   {
   }

   public void afterRequest(WebResponse webResponse)
   {
      pageCreated();
   }
   
}
