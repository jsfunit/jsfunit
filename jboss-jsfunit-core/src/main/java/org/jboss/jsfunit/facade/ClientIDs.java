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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import org.jboss.jsfunit.framework.FacesContextBridge;

/**
 * This class gathers all the client IDs from the current component tree.  It
 * then allows finding a full client ID given a suffix of that ID.  This suffix
 * is usually just the component ID, but for specificity it can also include 
 * one or more of a component's naming containers such as in 
 * "mysubview:myform:mycomponentID".
 *
 * @author Stan Silvert
 */
public class ClientIDs
{
   private List<String> allClientIDs = new ArrayList<String>();
   
   /**
    * Create a new instance of ClientIDs.
    */
   public ClientIDs()
   {
      FacesContext facesContext = FacesContextBridge.getCurrentInstance();
      UIComponent component = facesContext.getViewRoot();
      addAllIDs(component, facesContext);
   }

   // recursively walk the component tree and add all the client IDs to the list
   private void addAllIDs(UIComponent component, FacesContext facesContext)
   {
      addClientID(component, facesContext);
      
      for (Iterator facetsAndChildren = component.getFacetsAndChildren(); facetsAndChildren.hasNext();)
      {
         addAllIDs((UIComponent)facetsAndChildren.next(), facesContext);
      }
   }
   
   private void addClientID(UIComponent component, FacesContext facesContext)
   {
      if (component instanceof UIViewRoot) return;
      
      String clientId = component.getClientId(facesContext);
      if (clientId == null) return;

      //System.out.println("adding clientID=" + clientId);
      allClientIDs.add(clientId);
   }
   
   /**
    * Given a client ID suffix, find the fully-qualified client ID.
    * 
    * @param suffix The client ID suffix.
    *
    * @return The fully-qualified client ID.
    *
    * @throws ComponentIDNotFoundException if no client ID matches the suffix
    * @throws DuplicateClientIDException if more than one client ID matches the suffix
    */
   public String find(String suffix)
   {
      if (suffix == null) throw new NullPointerException();
      
      List<String> matches = new ArrayList<String>();
      for (Iterator<String> ids = allClientIDs.iterator(); ids.hasNext();)
      {
         String id = ids.next();
         if (id.endsWith(suffix)) matches.add(id);
      }
      
      if (matches.size() == 1) return matches.get(0);
      
      if (matches.isEmpty()) throw new ComponentIDNotFoundException(suffix);
      
      throw new DuplicateClientIDException(suffix, matches);
   }
}
