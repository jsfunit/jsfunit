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
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Stub impementation of <code>Map</code> for the request scope
 * attributes managed by {@link ExternalContextStub}.</p>
 *
 * $Id$
 */

class RequestMapStub implements Map {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new instance, exposing the attributes of the
     * specified request as a map.</p>
     *
     * @param request The HttpServletRequest to wrap
     */
    public RequestMapStub(HttpServletRequest request) {

        this.request = request;

    }


    // ----------------------------------------------------- Stub Object Methods


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The HttpServletRequest whose attributes we are exposing
     * as a Map.</p>
     */
    private HttpServletRequest request = null;


    // ------------------------------------------------------------- Map Methods


    /** {@inheritDoc} */
    public void clear() {

        Iterator keys = keySet().iterator();
        while (keys.hasNext()) {
            request.removeAttribute((String) keys.next());
        }

    }


    /** {@inheritDoc} */
    public boolean containsKey(Object key) {

        return request.getAttribute(key(key)) != null;

    }


    /** {@inheritDoc} */
    public boolean containsValue(Object value) {

        if (value == null) {
            return false;
        }
        Enumeration keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            Object next = request.getAttribute((String) keys.nextElement());
            if (next == value) {
                return true;
            }
        }
        return false;

    }


    /** {@inheritDoc} */
    public Set entrySet() {

        Set set = new HashSet();
        Enumeration keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            set.add(request.getAttribute((String) keys.nextElement()));
        }
        return set;

    }


    /** {@inheritDoc} */
    public boolean equals(Object o) {

        return request.equals(o);

    }


    /** {@inheritDoc} */
    public Object get(Object key) {

        return request.getAttribute(key(key));

    }


    /** {@inheritDoc} */
    public int hashCode() {

        return request.hashCode();

    }


    /** {@inheritDoc} */
    public boolean isEmpty() {

        return size() < 1;

    }


    /** {@inheritDoc} */
    public Set keySet() {

        Set set = new HashSet();
        Enumeration keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            set.add(keys.nextElement());
        }
        return set;

    }


    /** {@inheritDoc} */
    public Object put(Object key, Object value) {

        if (value == null) {
            return (remove(key));
        }
        String skey = key(key);
        Object previous = request.getAttribute(skey);
        request.setAttribute(skey, value);
        return previous;

    }


    /** {@inheritDoc} */
    public void putAll(Map map) {

        Iterator keys = map.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            request.setAttribute(key, map.get(key));
        }

    }


    /** {@inheritDoc} */
    public Object remove(Object key) {

        String skey = key(key);
        Object previous = request.getAttribute(skey);
        request.removeAttribute(skey);
        return previous;

    }


    /** {@inheritDoc} */
    public int size() {

        int n = 0;
        Enumeration keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            keys.nextElement();
            n++;
        }
        return n;

    }


    /** {@inheritDoc} */
    public Collection values() {

        List list = new ArrayList();
        Enumeration keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            list.add(request.getAttribute((String) keys.nextElement()));
        }
        return list;

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the specified key converted to a string.</p>
     *
     * @param key The key to convert
     */
    private String key(Object key) {

        if (key == null) {
            throw new IllegalArgumentException();
        } else if (key instanceof String) {
            return ((String) key);
        } else {
            return (key.toString());
        }

    }


}
