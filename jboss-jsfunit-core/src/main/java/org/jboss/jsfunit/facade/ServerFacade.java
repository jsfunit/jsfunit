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

package org.jboss.jsfunit.facade;

import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import org.jboss.jsfunit.framework.FacesContextBridge;

/**
 * The ServerFacade provides a simplified API that wraps parts of the JSF API 
 * for things that you would commonly do in testing.
 *
 * @author Stan Silvert
 */
public class ServerFacade
{
   /**
    * Create a new ServerFacade.
    */
   public ServerFacade()
   {
   }
   
   /**
    * Return the current view Id from the component tree.
    *
    * @return The current View Id.
    */
   public String getCurrentViewId()
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
    * Find a component in the JSF component tree.  This will start searching
    * for the component inside the current NamingContainer.
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
      return getFacesContext().getViewRoot().findComponent(new ClientIDs().find(componentID));
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
}
