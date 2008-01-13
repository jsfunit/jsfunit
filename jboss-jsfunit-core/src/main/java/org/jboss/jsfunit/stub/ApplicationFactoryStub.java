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

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;

/**
 * <p>Stub implementation of <code>ApplicationFactory</code>.</p>
 *
 * $Id$
 */

public class ApplicationFactoryStub extends ApplicationFactory {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a default instance.</p>
     */
    public ApplicationFactoryStub() {

    }


    // ----------------------------------------------------- Stub Object Methods


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The <code>Application</code> instance to be returned by
     * this factory.</p>
     */
    private Application application = null;


    // --------------------------------------------- AppolicationFactory Methods


    /** {@inheritDoc} */
    public Application getApplication() {

        if (this.application == null) {
            Class clazz = null;
            try {
                clazz = this.getClass().getClassLoader().loadClass
                  ("org.jboss.jsfunit.stub.ApplicationStub12");
                this.application = (ApplicationStub) clazz.newInstance();
            } catch (NoClassDefFoundError e) {
                clazz = null; // We are not running in a JSF 1.2 environment
            } catch (ClassNotFoundException e) {
                clazz = null; // Same as above
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new FacesException(e);
            }
            if (clazz == null) {
                try {
                    clazz = this.getClass().getClassLoader().loadClass
                      ("org.jboss.jsfunit.stub.ApplicationStub");
                    this.application = (ApplicationStub) clazz.newInstance();
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new FacesException(e);
                }
            }
        }
        return this.application;

    }


    /** {@inheritDoc} */
    public void setApplication(Application application) {

        this.application = application;

    }


}
