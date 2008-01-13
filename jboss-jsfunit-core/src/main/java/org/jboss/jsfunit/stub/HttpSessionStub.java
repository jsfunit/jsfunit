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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionContext;

/**
 * <p>Stub implementation of <code>HttpSession</code>.</p>
 *
 * @author Apache Software Foundation
 */

public class HttpSessionStub implements HttpSession {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Configure a default instance.</p>
     */
    public HttpSessionStub() {

        super();

    }


    /**
     * <p>Configure a session instance associated with the specified
     * servlet context.</p>
     *
     * @param servletContext The associated servlet context
     */
    public HttpSessionStub(ServletContext servletContext) {

        super();
        setServletContext(servletContext);

    }


    // ----------------------------------------------------- Stub Object Methods


    /**
     * <p>Add a new listener instance that should be notified about
     * attribute changes.</p>
     *
     * @param listener The new listener to be added
     */
    public void addAttributeListener(HttpSessionAttributeListener listener) {
        attributeListeners.add(listener);
    }


    /**
     * <p>Set the ServletContext associated with this session.</p>
     *
     * @param servletContext The associated servlet context
     */
    public void setServletContext(ServletContext servletContext) {

        this.servletContext = servletContext;

    }


    // ------------------------------------------------------ Instance Variables


    private List attributeListeners = new ArrayList();
    private HashMap attributes = new HashMap();
    private String id = "123";
    private ServletContext servletContext = null;


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Set the session identifier of this session.</p>
     *
     * @param id The new session identifier
     */
    public void setId(String id) {
        this.id = id;
    }


    // ----------------------------------------------------- HttpSession Methods


    /** {@inheritDoc} */
    public Object getAttribute(String name) {

        return attributes.get(name);

    }


    /** {@inheritDoc} */
    public Enumeration getAttributeNames() {

        return new EnumerationStub(attributes.keySet().iterator());

    }


    /** {@inheritDoc} */
    public long getCreationTime() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public String getId() {

        return this.id;

    }


    /** {@inheritDoc} */
    public long getLastAccessedTime() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public int getMaxInactiveInterval() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public ServletContext getServletContext() {

        return this.servletContext;

    }


    /** {@inheritDoc} */
    public HttpSessionContext getSessionContext() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public Object getValue(String name) {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public String[] getValueNames() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public void invalidate() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public boolean isNew() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public void putValue(String name, Object value) {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public void removeAttribute(String name) {

        if (attributes.containsKey(name)) {
            Object value = attributes.remove(name);
            fireAttributeRemoved(name, value);
        }

    }


    /** {@inheritDoc} */
    public void removeValue(String name) {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public void setAttribute(String name, Object value) {

        if (name == null) {
            throw new IllegalArgumentException("Attribute name cannot be null");
        }
        if (value == null) {
            removeAttribute(name);
            return;
        }
        if (attributes.containsKey(name)) {
            Object oldValue = attributes.get(name);
            attributes.put(name, value);
            fireAttributeReplaced(name, oldValue);
        } else {
            attributes.put(name, value);
            fireAttributeAdded(name, value);
        }

    }


    /** {@inheritDoc} */
    public void setMaxInactiveInterval(int interval) {

        throw new UnsupportedOperationException();

    }


    // --------------------------------------------------------- Support Methods


    /**
     * <p>Fire an attribute added event to interested listeners.</p>
     *
     * @param key Attribute whose value was added
     * @param value The new value
     */
    private void fireAttributeAdded(String key, Object value) {
        if (attributeListeners.size() < 1) {
            return;
        }
        HttpSessionBindingEvent event =
                new HttpSessionBindingEvent(this, key, value);
        Iterator listeners = attributeListeners.iterator();
        while (listeners.hasNext()) {
            HttpSessionAttributeListener listener =
                    (HttpSessionAttributeListener) listeners.next();
            listener.attributeAdded(event);
        }
    }


    /**
     * <p>Fire an attribute removed event to interested listeners.</p>
     *
     * @param key Attribute whose value was removed
     * @param value The removed value
     */
    private void fireAttributeRemoved(String key, Object value) {
        if (attributeListeners.size() < 1) {
            return;
        }
        HttpSessionBindingEvent event =
                new HttpSessionBindingEvent(this, key, value);
        Iterator listeners = attributeListeners.iterator();
        while (listeners.hasNext()) {
            HttpSessionAttributeListener listener =
                    (HttpSessionAttributeListener) listeners.next();
            listener.attributeRemoved(event);
        }
    }


    /**
     * <p>Fire an attribute replaced event to interested listeners.</p>
     *
     * @param key Attribute whose value was replaced
     * @param value The original value
     */
    private void fireAttributeReplaced(String key, Object value) {
        if (attributeListeners.size() < 1) {
            return;
        }
        HttpSessionBindingEvent event =
                new HttpSessionBindingEvent(this, key, value);
        Iterator listeners = attributeListeners.iterator();
        while (listeners.hasNext()) {
            HttpSessionAttributeListener listener =
                    (HttpSessionAttributeListener) listeners.next();
            listener.attributeReplaced(event);
        }
    }


}
