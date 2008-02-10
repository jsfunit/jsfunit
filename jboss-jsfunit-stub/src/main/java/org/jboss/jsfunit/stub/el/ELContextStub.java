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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;
import javax.faces.context.FacesContext;

/**
 * <p>Stub implementation of <code>ELContext</code>.</p>
 *
 * @since 1.0.4
 * @author Apache Software Foundation
 */

public class ELContextStub extends ELContext {
    

    // ------------------------------------------------------------ Constructors


    /** Creates a new instance of ELContextStub */
    public ELContextStub() {
    }
    

    // ------------------------------------------------------ Instance Variables


    private Map contexts = new HashMap();
    private FunctionMapper functionMapper = new FunctionMapperStub();
    private Locale locale = Locale.getDefault();
    private boolean propertyResolved;
    private VariableMapper variableMapper = new VariableMapperStub();


    // ----------------------------------------------------- Stub Object Methods



    // ------------------------------------------------------- ELContext Methods


    /** {@inheritDoc} */
    public Object getContext(Class key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return contexts.get(key);
    }


    /** {@inheritDoc} */
    public ELResolver getELResolver() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().getELResolver();
    }


    /** {@inheritDoc} */
    public FunctionMapper getFunctionMapper() {
        return this.functionMapper;
    }


    /** {@inheritDoc} */
    public Locale getLocale() {
        return this.locale;
    }


    /** {@inheritDoc} */
    public boolean isPropertyResolved() {
        return this.propertyResolved;
    }


    /** {@inheritDoc} */
    public void putContext(Class key, Object value) {
        if ((key == null) || (value == null)) {
            throw new NullPointerException();
        }
        contexts.put(key, value);
    }


    /** {@inheritDoc} */
    public void setPropertyResolved(boolean propertyResolved) {
        this.propertyResolved = propertyResolved;
    }


    /** {@inheritDoc} */
    public VariableMapper getVariableMapper() {
        return this.variableMapper;
    }


    /** {@inheritDoc} */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }


}
