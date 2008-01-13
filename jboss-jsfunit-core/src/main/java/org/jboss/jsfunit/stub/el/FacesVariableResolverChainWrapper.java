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

import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.PropertyNotFoundException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.VariableResolver;

/**
 * <p><code>ELResolver</code> implementation that wraps the legacy (JSF 1.1)
 * <code>VariableResolver</code> chain.  See the JSF 1.2 Specification, section
 * 5.6.1.5, for requirements implemented by this class.</p>
 *
 * @since 1.0.4
 * @author Apache Software Foundation
 */
public class FacesVariableResolverChainWrapper extends AbstractELResolver {
    

    /**
     * <p>Return the most general type this resolver accepts for the
     * <code>property</code> argument.</p>
     */
    public Class getCommonPropertyType(ELContext context, Object base) {

        if (base != null) {
            return null;
        } else {
            return String.class;
        }

    }


    /**
     * <p>Return an <code>Iterator</code> over the attributes that this
     * resolver knows how to deal with.</p>
     *
     * @param context <code>ELContext</code> for evaluating this value
     * @param base Base object against which this evaluation occurs
     */
    public Iterator getFeatureDescriptors(ELContext context, Object base) {

        return null;

    }



    /**
     * <p>Return the Java type of the specified property.</p>
     *
     * @param context <code>ELContext</code> for evaluating this value
     * @param base Base object against which this evaluation occurs
     *  (must be null because we are evaluating a top level variable)
     * @param property Property name to be accessed
     */
    public Class getType(ELContext context, Object base, Object property) {

        if ((base == null) && (property == null)) {
            throw new PropertyNotFoundException("No property specified");
        }
        return null;

    }


    /**
     * <p>Evaluate with the legacy variable resolver chain and return
     * the value.</p>
     *
     * @param context <code>ELContext</code> for evaluating this value
     * @param base Base object against which this evaluation occurs
     *  (must be null because we are evaluating a top level variable)
     * @param property Property name to be accessed
     */
    public Object getValue(ELContext context, Object base, Object property) {

        if (base != null) {
            return null;
        }
        if (property == null) {
            throw new PropertyNotFoundException("No property specified");
        }

        FacesContext fcontext = (FacesContext) context.getContext(FacesContext.class);
        ExternalContext econtext = fcontext.getExternalContext();
        String name = property.toString();

        context.setPropertyResolved(true);
        ELContext elContext = fcontext.getELContext();
        VariableResolver vr = fcontext.getApplication().getVariableResolver();
        try {
            return vr.resolveVariable(fcontext, name);
        } catch (Exception e) {
            context.setPropertyResolved(false);
            throw new ELException(e);
        }

    }


    /**
     * <p>Return <code>true</code> if the specified property is read only.</p>
     *
     * @param context <code>ELContext</code> for evaluating this value
     * @param base Base object against which this evaluation occurs
     *  (must be null because we are evaluating a top level variable)
     * @param property Property name to be accessed
     */
    public boolean isReadOnly(ELContext context, Object base, Object property) {

        if ((base == null) && (property == null)) {
            throw new PropertyNotFoundException("No property specified");
        }
        return false;

    }



    /**
     * <p>Set the value of a scoped object for the specified name.</p>
     *
     * @param context <code>ELContext</code> for evaluating this value
     * @param base Base object against which this evaluation occurs
     *  (must be null because we are evaluating a top level variable)
     * @param property Property name to be accessed
     * @param value New value to be set
     */
    public void setValue(ELContext context, Object base, Object property, Object value) {

        if ((base == null) && (property == null)) {
            throw new PropertyNotFoundException("No property specified");
        }

    }


}
