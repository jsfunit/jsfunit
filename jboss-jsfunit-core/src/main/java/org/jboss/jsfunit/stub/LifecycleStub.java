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

package org.jboss.jsfunit.stub;

import java.util.ArrayList;
import java.util.List;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;

/**
 * <p>Mock implementation of <code>Lifecycle</code>.</p>
 *
 * $Id$
 */

public class LifecycleStub extends Lifecycle {


    // ----------------------------------------------------- Mock Object Methods


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>List of event listeners for this instance.</p>
     */
    private List listeners = new ArrayList();


    // ------------------------------------------------------- Lifecycle Methods


    /** {@inheritDoc} */
    public void addPhaseListener(PhaseListener listener) {

        listeners.add(listener);

    }


    /** {@inheritDoc} */
    public void execute(FacesContext context) throws FacesException {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public PhaseListener[] getPhaseListeners() {

        return (PhaseListener[]) listeners.toArray(new PhaseListener[listeners.size()]);

    }


    /** {@inheritDoc} */
    public void removePhaseListener(PhaseListener listener) {

        listeners.remove(listener);

    }


    /** {@inheritDoc} */
    public void render(FacesContext context) throws FacesException {

        throw new UnsupportedOperationException();

    }


}
