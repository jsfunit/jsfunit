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

package org.jboss.jsfunit.stub.el;

import java.beans.FeatureDescriptor;
import javax.el.ELResolver;

/**
 * <p>Convenience base class for EL resolvers.</p>
 */
abstract class AbstractELResolver extends ELResolver {
    


    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Create and return a <code>FeatureDescriptor</code> configured with
     * the specified arguments.</p>
     *
     * @param name Feature name
     * @param displayName Display name
     * @param description Short description
     * @param expert Flag indicating this feature is for experts
     * @param hidden Flag indicating this feature should be hidden
     * @param preferred Flag indicating this feature is the preferred one
     *  among features of the same type
     * @param type Runtime type of this feature
     * @param designTime Flag indicating feature is resolvable at design time
     */
    protected FeatureDescriptor descriptor(String name, String displayName,
      String description, boolean expert, boolean hidden, boolean preferred,
      Object type, boolean designTime) {

      FeatureDescriptor descriptor = new FeatureDescriptor();

      descriptor.setName(name);
      descriptor.setDisplayName(displayName);
      descriptor.setShortDescription(description);
      descriptor.setExpert(expert);
      descriptor.setHidden(hidden);
      descriptor.setPreferred(preferred);
      descriptor.setValue(ELResolver.TYPE, type);
      if (designTime) {
          descriptor.setValue(ELResolver.RESOLVABLE_AT_DESIGN_TIME, Boolean.TRUE);
      } else {
          descriptor.setValue(ELResolver.RESOLVABLE_AT_DESIGN_TIME, Boolean.FALSE);
      }

      return descriptor;

    }


}
