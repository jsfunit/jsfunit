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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletContext;
import javax.portlet.PortletSession;

/**
 * <p> Mock implementation of <code>PortletSession</code>. </p>
 * 
 * $Id: PortletSessionStub.java 516091 2007-03-08 16:25:17Z greddin $
 */
public class PortletSessionStub implements PortletSession {

    // ------------------------------------------------------------ Constructors

    /**
     * <p> Configure a default instance. </p>
     */
    public PortletSessionStub() {

        super();

    }


    /**
     * <p> Configure a session instance associated with the specified servlet
     * context. </p>
     *
     * @param servletContext The associated servlet context
     */
    public PortletSessionStub(PortletContext portletContext) {

        super();
        this.portletContext = portletContext;

    }


    // ----------------------------------------------------- Mock Object Methods

    /**
     * <p> Set the <code>PortletContext</code> associated with this session.
     * </p>
     *
     * @param servletContext The associated servlet context
     */
    public void setPortletContext(PortletContext portletContext) {

        this.portletContext = portletContext;

    }

    // ------------------------------------------------------ Instance Variables

    private Map portletAttributes = new HashMap();
    private Map applicationAttributes = new HashMap();
    private String id = "123";
    private PortletContext portletContext = null;


    // ---------------------------------------------------------- Public Methods

    /**
     * <p> Set the session identifier of this session. </p>
     *
     * @param id The new session identifier
     */
    public void setId(String id) {

        this.id = id;

    }


    // -------------------------------------------------- PortletSession Methods


    /** {@inheritDoc} */
    public Object getAttribute(String name) {

        return getAttribute(name, PORTLET_SCOPE);

    }


    /** {@inheritDoc} */
    public Object getAttribute(String name, int scope) {

        if (scope == PORTLET_SCOPE) {
            return portletAttributes.get(name);
        } else if (scope == APPLICATION_SCOPE) {
            return applicationAttributes.get(name);
        }

        throw new IllegalArgumentException("Scope constant " + scope
                + " not recognized");

    }


    /** {@inheritDoc} */
    public Enumeration getAttributeNames() {

        return getAttributeNames(PORTLET_SCOPE);

    }


    /** {@inheritDoc} */
    public Enumeration getAttributeNames(int scope) {

        if (scope == PORTLET_SCOPE) {
            return new EnumerationStub(portletAttributes.keySet().iterator());
        } else if (scope == APPLICATION_SCOPE) {
            return new EnumerationStub(applicationAttributes.keySet()
                    .iterator());
        }

        throw new IllegalArgumentException("Scope constant " + scope
                + " not recognized");

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
    public PortletContext getPortletContext() {

        return portletContext;
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
    public void removeAttribute(String name) {

        removeAttribute(name, PORTLET_SCOPE);

    }


    /** {@inheritDoc} */
    public void removeAttribute(String name, int scope) {

        Map attributes;
        if (scope == PORTLET_SCOPE) {
            attributes = portletAttributes;
        } else if (scope == APPLICATION_SCOPE) {
            attributes = applicationAttributes;
        } else {
            throw new IllegalArgumentException("Scope constant " + scope
                    + " not recognized");
        }
        if (attributes.containsKey(name)) {
            attributes.remove(name);
        }

    }


    /** {@inheritDoc} */
    public void setAttribute(String name, Object value) {

        setAttribute(name, value, PORTLET_SCOPE);

    }


    /** {@inheritDoc} */
    public void setAttribute(String name, Object value, int scope) {

        if (name == null) {
            throw new IllegalArgumentException("Attribute name cannot be null");
        }
        if (value == null) {
            removeAttribute(name, scope);
            return;
        }

        Map attributes;
        if (scope == PORTLET_SCOPE) {
            attributes = portletAttributes;
        } else if (scope == APPLICATION_SCOPE) {
            attributes = applicationAttributes;
        } else {
            throw new IllegalArgumentException("Scope constant " + scope
                    + " not recognized");
        }
        attributes.put(name, value);

    }


    /** {@inheritDoc} */
    public void setMaxInactiveInterval(int arg0) {

        throw new UnsupportedOperationException();

    }

}
