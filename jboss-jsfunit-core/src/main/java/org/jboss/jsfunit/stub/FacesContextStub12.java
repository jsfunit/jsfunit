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

import javax.el.ELContext;
import javax.el.ELContextEvent;
import javax.el.ELContextListener;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;

import org.jboss.jsfunit.stub.el.ELContextStub;

/**
 * <p>Stub implementation of <code>FacesContext</code> that includes the semantics
 * added by JavaServer Faces 1.2.</p>
 *
 * $Id: FacesContextStub12.java 464373 2006-10-16 04:21:54Z rahul $
 * @author Apache Software Foundation
 * @since 1.0.4
 */

public class FacesContextStub12 extends FacesContextStub {


    // ------------------------------------------------------------ Constructors


    public FacesContextStub12() {
        super();
        setCurrentInstance(this);
    }


    public FacesContextStub12(ExternalContext externalContext) {
        super(externalContext);
    }


    public FacesContextStub12(ExternalContext externalContext, Lifecycle lifecycle) {
        super(externalContext, lifecycle);
    }


    // ----------------------------------------------------- Stub Object Methods


    /**
     * <p>Set the <code>ELContext</code> instance for this instance.</p>
     *
     * @param elContext The new ELContext
     */
    public void setELContext(ELContext elContext) {

        this.elContext = elContext;

    }


    // ------------------------------------------------------ Instance Variables


    private ELContext elContext = null;


    // ---------------------------------------------------- FacesContext Methods


    /** {@inheritDoc} */
    public ELContext getELContext() {

        if (this.elContext == null) {

            // Initialize a new ELContext
            this.elContext = new ELContextStub();
            this.elContext.putContext(FacesContext.class, this);

            // Notify interested listeners that this ELContext was created
            ELContextListener[] listeners = getApplication().getELContextListeners();
            if ((listeners != null) && (listeners.length > 0)) {
                ELContextEvent event = new ELContextEvent(this.elContext);
                for (int i = 0; i < listeners.length; i++) {
                    listeners[i].contextCreated(event);
                }
            }

        }
        return this.elContext;

    }


    /** {@inheritDoc} */
    public void release() {
        super.release();
        this.elContext = null;
    }


}
