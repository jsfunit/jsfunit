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

import java.util.List;
import javax.faces.event.PhaseId;

/**
 *
 * @author Stan Silvert
 * @since 1.1
 */
public class PhaseData {

   public enum Status {COMPLETED, SKIPPED, ABORTED};
   
   private PhaseId phaseId;
   private Status status = Status.SKIPPED;
   private long elapsedTime = 0L;
   
   PhaseData(PhaseId phaseId, List<Snapshot> snapshots)
   {
      this.phaseId = phaseId;
      
      Snapshot before = findSnapshot(BeforeOrAfter.BEFORE, phaseId, snapshots);
      Snapshot after = findSnapshot(BeforeOrAfter.AFTER, phaseId, snapshots);
      
      if (before == null) return; // skipped
      
      if (after == null) // aborted
      {
         this.status = Status.ABORTED;
         return;
      }
      
      this.status = Status.COMPLETED;
      this.elapsedTime = after.getTimestamp() - before.getTimestamp();
   }
   
   private Snapshot findSnapshot(BeforeOrAfter beforeOrAfter, PhaseId phaseId, List<Snapshot> snapshots)
   {
      for (Snapshot snapshot : snapshots)
      {
         if ( (snapshot.getBeforeOrAfter() == beforeOrAfter) && (snapshot.getPhaseId() == phaseId) )
         {
            return snapshot;
         }
      }
      
      return null;
   }

   public long getElapsedTime()
   {
      return elapsedTime;
   }

   public PhaseId getPhaseId()
   {
      return this.phaseId;
   }

   public Status getStatus()
   {
      return status;
   }

}
