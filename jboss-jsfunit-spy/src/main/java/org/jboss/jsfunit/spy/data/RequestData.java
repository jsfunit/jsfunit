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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.servlet.http.HttpServletRequest;
import org.jboss.jsfunit.spy.context.SpyHttpServletRequest;

/**
 *
 * @author Stan Silvert
 * @since 1.1
 */
public class RequestData 
{
   private HttpServletRequest request;
   
   private List<Snapshot> snapshots = new ArrayList<Snapshot>();
   
   RequestData(FacesContext facesContext)
   {
      ExternalContext extCtx = facesContext.getExternalContext();
      this.request = new SpyHttpServletRequest((HttpServletRequest)extCtx.getRequest());
   }
   
   void takeSnapshotBefore(PhaseEvent event)
   {
      this.snapshots.add(new Snapshot(BeforeOrAfter.BEFORE, event));
   }
   
   void takeSnapshotAfter(PhaseEvent event)
   {
      this.snapshots.add(new Snapshot(BeforeOrAfter.AFTER, event));
   }
   
   public HttpServletRequest getHttpRequest()
   {
      return this.request;
   }
   
   public List<Snapshot> getSnapshots()
   {
      return Collections.unmodifiableList(this.snapshots);
   }
   
}
