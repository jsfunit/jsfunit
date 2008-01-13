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

import java.lang.reflect.Constructor;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Stub implementation of <code>FacesContextFactory</code>.</p>
 *
 * $Id$
 */

public class FacesContextFactoryStub extends FacesContextFactory {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Look up the constructor we will use for creating <code>FacesContextStub</code>
     * instances.</p>
     */
    public FacesContextFactoryStub() {

        Class clazz = null;

        // Try to load the 1.2 version of our stub FacesContext class
        try {
            clazz = this.getClass().getClassLoader().loadClass("org.jboss.jsfunit.stub.FacesContextStub12");
            constructor = clazz.getConstructor(facesContextSignature);
            jsf12 = true;
        } catch (NoClassDefFoundError e) {
            // We are not running on JSF 1.2, so go to our fallback
            clazz = null;
            constructor = null;
        } catch (ClassNotFoundException e) {
            // Same as above
            clazz = null;
            constructor = null;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new FacesException(e);
        }

        // Fall back to the 1.1 version if we could not load the 1.2 version
        try {
            if (clazz == null) {
                clazz = this.getClass().getClassLoader().loadClass("org.jboss.jsfunit.stub.FacesContextStub");
                constructor = clazz.getConstructor(facesContextSignature);
                jsf12 = false;
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new FacesException(e);
        }

    }


    // ----------------------------------------------------- Stub Object Methods


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The constructor for creating a <code>FacesContext</code> instance,
     * taking an <code>ExternalContext</code> and <code>Lifecycle</code>.</p>
     */
    private Constructor constructor = null;


    /**
     * <p>The parameter signature of the ExternalContext constructor we wish to call.</p>
     */
    private static Class[] externalContextSignature = new Class[] {
        ServletContext.class, HttpServletRequest.class, HttpServletResponse.class
    };


    /**
     * <p>The parameter signature of the FacesContext constructor we wish to call.</p>
     */
    private static Class[] facesContextSignature = new Class[] {
        ExternalContext.class, Lifecycle.class
    };


    /**
     * <p>Flag indicating that we are running in a JSF 1.2 environment.</p>
     */
    private boolean jsf12 = false;


    // --------------------------------------------- FacesContextFactory Methods


    /** {@inheritDoc} */
    public FacesContext getFacesContext(Object context, Object request,
                                        Object response,
                                        Lifecycle lifecycle) throws FacesException {

        // Select the appropriate ExternalContextStub implementation class
        Class clazz = ExternalContextStub.class;
        if (jsf12) {
            try {
                clazz = this.getClass().getClassLoader().loadClass
                  ("org.jboss.jsfunit.stub.ExternalContextStub12");
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }

        // Select the constructor we wish to call
        Constructor mecConstructor = null;
        try {
            mecConstructor = clazz.getConstructor(externalContextSignature);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new FacesException(e);
        }

        // Construct an appropriate ExternalContextStub instance
        ExternalContextStub externalContext = null;
        try {
            externalContext = (ExternalContextStub) mecConstructor.newInstance
              (new Object[] { context, request, response });
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new FacesException(e);
        }

        // Construct an appropriate FacesContextStub instance and return it
        try {
            return (FacesContextStub)
              constructor.newInstance(new Object[] { externalContext, lifecycle });
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new FacesException(e);
        }

    }


}
