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

package org.jboss.jsfunit.spy.ui;

import java.util.List;
import javax.faces.event.PhaseId;
import org.jboss.jsfunit.spy.data.BeforeOrAfter;
import org.jboss.jsfunit.spy.data.Scope;
import org.jboss.jsfunit.spy.data.Snapshot;

/**
 *
 * @author Stan Silvert
 * @since 1.1
 */
public class PhaseValues {

   private String attributeName;
   
   private String[][] values = new String[PhaseId.VALUES.size()][BeforeOrAfter.values().length];
   
   PhaseValues(Scope scope, String attributeName, List<Snapshot> snapshots)
   {
      this.attributeName = attributeName;
      
      for (Snapshot snapshot : snapshots)
      {
         String value = snapshot.getScopeMap(scope).get(attributeName);
     /*    System.out.println("***********************");
         System.out.println("scope=" + scope);
         System.out.println("PhaseId=" + snapshot.getPhaseId());
         System.out.println("BeforeOrAfter=" + snapshot.getBeforeOrAfter());
         System.out.println("value=" + value);
         System.out.println("***********************"); */
         values[snapshot.getPhaseId().getOrdinal()][snapshot.getBeforeOrAfter().ordinal()] = value;
      }
   }
   
   public String getAttributeName()
   {
      return attributeName;
   }
   
   public String getApplyRequestValuesAfter()
   {
      return values[PhaseId.APPLY_REQUEST_VALUES.getOrdinal()][BeforeOrAfter.AFTER.ordinal()];
   }

   public String getApplyRequestValuesBefore()
   {
      return values[PhaseId.APPLY_REQUEST_VALUES.getOrdinal()][BeforeOrAfter.BEFORE.ordinal()];
   }

   public String getInvokeApplicationAfter()
   {
      return values[PhaseId.INVOKE_APPLICATION.getOrdinal()][BeforeOrAfter.AFTER.ordinal()];
   }

   public String getInvokeApplicationBefore()
   {
      return values[PhaseId.INVOKE_APPLICATION.getOrdinal()][BeforeOrAfter.BEFORE.ordinal()];
   }

   public String getProcessValidationsAfter()
   {
      return values[PhaseId.PROCESS_VALIDATIONS.getOrdinal()][BeforeOrAfter.AFTER.ordinal()];
   }

   public String getProcessValidationsBefore()
   {
      return values[PhaseId.PROCESS_VALIDATIONS.getOrdinal()][BeforeOrAfter.BEFORE.ordinal()];
   }

   public String getRenderResponseAfter()
   {
      return values[PhaseId.RENDER_RESPONSE.getOrdinal()][BeforeOrAfter.AFTER.ordinal()];
   }

   public String getRenderResponseBefore()
   {
      return values[PhaseId.RENDER_RESPONSE.getOrdinal()][BeforeOrAfter.BEFORE.ordinal()];
   }

   public String getRestoreViewAfter()
   {
      return values[PhaseId.RESTORE_VIEW.getOrdinal()][BeforeOrAfter.AFTER.ordinal()];
   }

   public String getRestoreViewBefore()
   {
      return values[PhaseId.RESTORE_VIEW.getOrdinal()][BeforeOrAfter.BEFORE.ordinal()];
   }

   public String getUpdateModelValuesAfter()
   {
      return values[PhaseId.UPDATE_MODEL_VALUES.getOrdinal()][BeforeOrAfter.AFTER.ordinal()];
   }

   public String getUpdateModelValuesBefore()
   {
      return values[PhaseId.UPDATE_MODEL_VALUES.getOrdinal()][BeforeOrAfter.BEFORE.ordinal()];
   }
    
}