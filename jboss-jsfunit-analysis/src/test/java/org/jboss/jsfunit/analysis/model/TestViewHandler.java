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
import java.util.Locale;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 * A TestViewHandler.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
public class TestViewHandler extends ViewHandler
{
   @Override
   public Locale calculateLocale(FacesContext arg0)
   {
      return null;
   }

   @Override
   public String calculateRenderKitId(FacesContext arg0)
   {
      return null;
   }

   @Override
   public UIViewRoot createView(FacesContext arg0, String arg1)
   {
      return null;
   }

   @Override
   public String getActionURL(FacesContext arg0, String arg1)
   {
      return null;
   }

   @Override
   public String getResourceURL(FacesContext arg0, String arg1)
   {
      return null;
   }

   @Override
   public void renderView(FacesContext arg0, UIViewRoot arg1) throws IOException, FacesException
   {
   }

   @Override
   public UIViewRoot restoreView(FacesContext arg0, String arg1)
   {
      return null;
   }

   @Override
   public void writeState(FacesContext arg0) throws IOException
   {
   }
}
