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
 * 
 */
package org.jboss.jsfunit.analysis.model;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.render.Renderer;

/**
 * A TestComponent.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
@SuppressWarnings("deprecation")
public class TestComponent extends UIComponent
{

   @Override
   protected void addFacesListener(FacesListener arg0)
   {
   }

   @Override
   public void broadcast(FacesEvent arg0) throws AbortProcessingException
   {
   }

   @Override
   public void decode(FacesContext arg0)
   {
   }

   @Override
   public void encodeBegin(FacesContext arg0) throws IOException
   {
   }

   @Override
   public void encodeChildren(FacesContext arg0) throws IOException
   {
   }

   @Override
   public void encodeEnd(FacesContext arg0) throws IOException
   {
   }

   @Override
   public UIComponent findComponent(String arg0)
   {
      return null;
   }

   @Override
   public Map<String, Object> getAttributes()
   {
      return null;
   }

   @Override
   public int getChildCount()
   {
      return 0;
   }

   @Override
   public List<UIComponent> getChildren()
   {
      return null;
   }

   @Override
   public String getClientId(FacesContext arg0)
   {
      return null;
   }

   @Override
   protected FacesContext getFacesContext()
   {
      return null;
   }

   @Override
   protected FacesListener[] getFacesListeners(Class arg0)
   {
      return null;
   }

   @Override
   public UIComponent getFacet(String arg0)
   {
      return null;
   }

   @Override
   public Map<String, UIComponent> getFacets()
   {
      return null;
   }

   @Override
   public Iterator<UIComponent> getFacetsAndChildren()
   {
      return null;
   }

   @Override
   public String getFamily()
   {
      return null;
   }

   @Override
   public String getId()
   {
      return null;
   }

   @Override
   public UIComponent getParent()
   {
      return null;
   }

   @Override
   protected Renderer getRenderer(FacesContext arg0)
   {
      return null;
   }

   @Override
   public String getRendererType()
   {
      return null;
   }

   @Override
   public boolean getRendersChildren()
   {
      return false;
   }

   @Override
   public ValueBinding getValueBinding(String arg0)
   {
      return null;
   }

   @Override
   public boolean isRendered()
   {
      return false;
   }

   @Override
   public void processDecodes(FacesContext arg0)
   {
   }

   @Override
   public void processRestoreState(FacesContext arg0, Object arg1)
   {
   }

   @Override
   public Object processSaveState(FacesContext arg0)
   {
      return null;
   }

   @Override
   public void processUpdates(FacesContext arg0)
   {
   }

   @Override
   public void processValidators(FacesContext arg0)
   {
   }

   @Override
   public void queueEvent(FacesEvent arg0)
   {
   }

   @Override
   protected void removeFacesListener(FacesListener arg0)
   {
   }

   @Override
   public void setId(String arg0)
   {
   }

   @Override
   public void setParent(UIComponent arg0)
   {
   }

   @Override
   public void setRendered(boolean arg0)
   {
   }

   @Override
   public void setRendererType(String arg0)
   {
   }

   @Override
   public void setValueBinding(String arg0, ValueBinding arg1)
   {
   }

   public boolean isTransient()
   {
      return false;
   }

   public void restoreState(FacesContext arg0, Object arg1)
   {
   }

   public Object saveState(FacesContext arg0)
   {
      return null;
   }

   public void setTransient(boolean arg0)
   {
   }

}
