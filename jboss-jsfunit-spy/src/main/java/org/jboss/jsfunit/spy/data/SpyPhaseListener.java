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

package org.jboss.jsfunit.spy.data;

import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 *
 * @author Stan Silvert
 * @since 1.1
 */
public class SpyPhaseListener implements PhaseListener
{
   
   /**
    * Try to initialize SpyManager from the constructor of SpyPhaseListener,
    * just in case the SpyManagerContextListener didn't run.  This happnes
    * with Seam?
    */
   public SpyPhaseListener()
   {
      ExternalContext extCtx = FacesContext.getCurrentInstance().getExternalContext();
      Map appMap = extCtx.getApplicationMap();
      if (!appMap.containsKey(SpyManager.EL_KEY))
      {
         SpyManager spyManager = new SpyManager();
         appMap.put(SpyManager.EL_KEY, spyManager);
      }
   }
   
   public void beforePhase(PhaseEvent event)
   {

      if (handleSessionExpired(event)) return;
      
      SpyManager.getInstance().takeSnapshotBefore(event);
   }
   
   // Handle session expired for the jsf spy UI
   // Return true if session expired and we are requesting the spy UI
   private boolean handleSessionExpired(PhaseEvent event)
   {
      if (event.getPhaseId() != PhaseId.RESTORE_VIEW) return false;
      
      FacesContext facesContext = event.getFacesContext();
      ExternalContext extCtx = facesContext.getExternalContext();
      
      String pathInfo = extCtx.getRequestPathInfo();
      if (pathInfo == null) return false;
      if (!pathInfo.startsWith("/jsfunit-spy-ui")) return false;
      
      if (extCtx.getSession(false) != null) return false;
      
      facesContext.getApplication()
                  .getNavigationHandler()
                  .handleNavigation(facesContext, "", "spysessionexpired");
      
      return true;
   }
   
   public void afterPhase(PhaseEvent event)
   {
      SpyManager spyManager = SpyManager.getInstance();
      spyManager.takeSnapshotAfter(event);
   }

   public PhaseId getPhaseId()
   {
      return PhaseId.ANY_PHASE;
   }

}
