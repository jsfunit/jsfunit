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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;

/**
 * <p>Stub implementation of <code>PropertyResolver</code>.</p>
 *
 * $Id$
 */

public class PropertyResolverStub extends PropertyResolver {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a default instance.</p>
     */
    public PropertyResolverStub() {
    }


    // ----------------------------------------------------- Stub Object Methods


    // ------------------------------------------------------ Instance Variables


    // ------------------------------------------------ PropertyResolver Methods


    /** {@inheritDoc} */
    public Object getValue(Object base, Object property)
        throws EvaluationException, PropertyNotFoundException {

        if (base == null) {
            throw new NullPointerException();
        }
        if (base instanceof Map) {
            return ((Map) base).get(property);
        }
        String name = property.toString();
        PropertyDescriptor descriptor = descriptor(base.getClass(), name);
        try {
            return descriptor.getReadMethod().invoke(base, (Object[]) null);
        } catch (IllegalAccessException e) {
            throw new EvaluationException(e);
        } catch (InvocationTargetException e) {
            throw new EvaluationException(e.getTargetException());
        }

    }


    /** {@inheritDoc} */
    public Object getValue(Object base, int index)
        throws PropertyNotFoundException {

        return getValue(base, "" + index);

    }


    /** {@inheritDoc} */
    public void setValue(Object base, Object property, Object value)
        throws PropertyNotFoundException {

        if (base == null) {
            throw new NullPointerException();
        }
        if (base instanceof Map) {
            ((Map) base).put(property, value);
            return;
        }
        String name = property.toString();
        PropertyDescriptor descriptor = descriptor(base.getClass(), name);
        try {
            descriptor.getWriteMethod().invoke(base, new Object[] { value });
        } catch (IllegalAccessException e) {
            throw new EvaluationException(e);
        } catch (InvocationTargetException e) {
            throw new EvaluationException(e.getTargetException());
        }

    }


    /** {@inheritDoc} */
    public void setValue(Object base, int index, Object value)
        throws PropertyNotFoundException {

        setValue(base, "" + index, value);

    }


    /** {@inheritDoc} */
    public boolean isReadOnly(Object base, Object property)
        throws PropertyNotFoundException {

        if (base == null) {
            throw new NullPointerException();
        }
        if (base instanceof Map) {
            return false; // We have no way to know anything more specific
        }
        String name = property.toString();
        PropertyDescriptor descriptor = descriptor(base.getClass(), name);
        return (descriptor.getWriteMethod() == null);

    }


    /** {@inheritDoc} */
    public boolean isReadOnly(Object base, int index)
        throws PropertyNotFoundException {

        return isReadOnly(base, "" + index);

    }


    /** {@inheritDoc} */
    public Class getType(Object base, Object property)
        throws PropertyNotFoundException {

        if (base == null) {
            throw new NullPointerException();
        }
        if (base instanceof Map) {
            Object value = ((Map) base).get(property);
            if (value != null) {
                return value.getClass();
            } else {
                return Object.class;
            }
        }
        String name = property.toString();
        PropertyDescriptor descriptor = descriptor(base.getClass(), name);
        return descriptor.getPropertyType();

    }


    /** {@inheritDoc} */
    public Class getType(Object base, int index)
        throws PropertyNotFoundException {

        return getType(base, "" + index);

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the <code>PropertyDescriptor</code> for the specified
     * property of the specified class.</p>
     *
     * @param clazz Class from which to extract a property descriptor
     * @param name Name of the desired property
     *
     * @exception EvaluationException if we cannot access the introspecition
     *  information for this class
     * @exception PropertyNotFoundException if the specified property does
     *  not exist on the specified class
     */
    private PropertyDescriptor descriptor(Class clazz, String name) {

        System.err.println("descriptor(class=" + clazz.getName() + ", name=" + name);
        BeanInfo info = null;
        try {
            info = Introspector.getBeanInfo(clazz);
            System.err.println("  Found BeanInfo " + info);
        } catch (IntrospectionException e) {
            throw new EvaluationException(e);
        }
        PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
        for (int i = 0; i < descriptors.length; i++) {
            if (name.equals(descriptors[i].getName())) {
                System.err.print("  Found PropertyDescriptor " + descriptors[i]);
                return descriptors[i];
            }
        }
        System.err.print("  No property descriptor for property " + name);
        throw new PropertyNotFoundException(name);

    }


}
