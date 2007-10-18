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
package org.jboss.jsfunit.framework;

import java.util.Iterator;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * For now, this just contains a couple of simple utility methods to dump
 * the client IDs in the component tree.
 *
 * @author ssilvert
 */
public class DebugUtil
{
   
   /** Don't allow a new instance of DebugUtil */
   private DebugUtil()
   {
   }
   
   /**
    * Dump the component tree to standard out starting with the view root.
    */
   public static void dumpComponentTree(FacesContext facesContext)
   {
      dumpComponentTree(facesContext.getViewRoot(), facesContext);
   }
   
   /**
    * Dump the component tree to standard out starting with the given component.
    */
   public static void dumpComponentTree(UIComponent component, FacesContext facesContext)
   {
      if (component == null) return;

      System.out.println(component.getClientId(facesContext));
      
      for (Iterator facetsAndChildren = component.getFacetsAndChildren(); facetsAndChildren.hasNext();)
      {
         UIComponent facetOrChild = (UIComponent)facetsAndChildren.next();
         dumpComponentTree(facetOrChild, facesContext);
      }
   }
   
}
