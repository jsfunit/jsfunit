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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import javax.el.ELContext;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;
import javax.faces.context.FacesContext;

import org.jboss.jsfunit.stub.ApplicationStub12;

/**
 * <p><code>ELResolver</code> implementation that accesses resource bundles
 * in the current application.  See the JSF 1.2 Specification, section
 * 5.6.1.3, for requirements implemented by this class.</p>
 *
 * @since 1.0.4
 */
public class FacesResourceBundleELResolver extends AbstractELResolver {
    

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

        if (base != null) {
            return null;
        }

        // Create the variables we will need
        List descriptors = new ArrayList();
        FacesContext fcontext = (FacesContext) context.getContext(FacesContext.class);
        ApplicationStub12 application =
                (ApplicationStub12) fcontext.getApplication();
        String key = null;
        Object value = null;

        // Create a feature descriptor for each configured resource bundle
        Iterator entries = application.getResourceBundles().entrySet().iterator();
        while (entries.hasNext()) {
            Entry entry = (Entry) entries.next();
            key = (String) entry.getKey();
            value = entry.getValue();
            descriptors.add(descriptor(key, key, "Resource Bundle " + key,
                                       false, false, true,
                                       ResourceBundle.class, true));
        }

        // Return the accumulated descriptors
        return descriptors.iterator();

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

        if (base != null) {
            return null;
        }
        if (property == null) {
            throw new PropertyNotFoundException("No property specified");
        }
        FacesContext fcontext = (FacesContext) context.getContext(FacesContext.class);
        ResourceBundle bundle =
          fcontext.getApplication().getResourceBundle(fcontext, property.toString());
        if (bundle != null) {
            context.setPropertyResolved(true);
            return ResourceBundle.class;
        }
        return null;

    }


    /**
     * <p>Return a resource bundle for the specified name (if any);
     * otherwise, return <code>null</code>.</p>
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
        ResourceBundle bundle =
          fcontext.getApplication().getResourceBundle(fcontext, property.toString());
        if (bundle != null) {
            context.setPropertyResolved(true);
            return bundle;
        }
        return null;

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

        if (base != null) {
            return false;
        }
        if (property == null) {
            throw new PropertyNotFoundException("No property specified");
        }
        FacesContext fcontext = (FacesContext) context.getContext(FacesContext.class);
        ResourceBundle bundle =
          fcontext.getApplication().getResourceBundle(fcontext, property.toString());
        if (bundle != null) {
            context.setPropertyResolved(true);
            return true;
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

        if (base != null) {
            return;
        }
        if (property == null) {
            throw new PropertyNotFoundException("No property specified");
        }

        FacesContext fcontext = (FacesContext) context.getContext(FacesContext.class);
        ResourceBundle bundle =
          fcontext.getApplication().getResourceBundle(fcontext, property.toString());
        if (bundle != null) {
            context.setPropertyResolved(true);
            throw new PropertyNotWritableException(property.toString());
        }

    }


}
