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
   
   public void beforePhase(PhaseEvent event)
   {
      SpyManager spyManager = SpyManager.getInstance();
      
      if (event.getPhaseId() == PhaseId.RESTORE_VIEW) 
      {
         spyManager.newRequest(event.getFacesContext());
      }
      
      spyManager.takeSnapshotBefore(event);
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
