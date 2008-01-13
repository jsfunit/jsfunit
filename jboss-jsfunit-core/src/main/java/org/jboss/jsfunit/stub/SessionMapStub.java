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

import javax.servlet.http.HttpSession;


/**
 * <p>Stub impementation of <code>Map</code> for the session scope
 * attributes managed by {@link ExternalContextStub}.</p>
 *
 * $Id$
 */

class SessionMapStub implements Map {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct an instance exposing the attributes of the specified
     * session.</p>
     *
     * @param session Session whose attributes are to be exposed
     *  as a Map
     */
    public SessionMapStub(HttpSession session) {

        this.session = session;

    }


    // ----------------------------------------------------- Stub Object Methods


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The session whose attributes we are exposing as a Map.</p>
     */
    private HttpSession session = null;


    // ------------------------------------------------------------- Map Methods


    /** {@inheritDoc} */
    public void clear() {

        Iterator keys = keySet().iterator();
        while (keys.hasNext()) {
            session.removeAttribute((String) keys.next());
        }

    }


    /** {@inheritDoc} */
    public boolean containsKey(Object key) {

        return session.getAttribute(key(key)) != null;

    }


    /** {@inheritDoc} */
    public boolean containsValue(Object value) {

        if (value == null) {
            return false;
        }
        Enumeration keys = session.getAttributeNames();
        while (keys.hasMoreElements()) {
            Object next = session.getAttribute((String) keys.nextElement());
            if (next == value) {
                return true;
            }
        }
        return false;

    }


    /** {@inheritDoc} */
    public Set entrySet() {

        Set set = new HashSet();
        Enumeration keys = session.getAttributeNames();
        while (keys.hasMoreElements()) {
            set.add(session.getAttribute((String) keys.nextElement()));
        }
        return set;

    }


    /** {@inheritDoc} */
    public boolean equals(Object o) {

        return session.equals(o);

    }


    /** {@inheritDoc} */
    public Object get(Object key) {

        return session.getAttribute(key(key));

    }


    /** {@inheritDoc} */
    public int hashCode() {

        return session.hashCode();

    }


    /** {@inheritDoc} */
    public boolean isEmpty() {

        return size() < 1;

    }


    /** {@inheritDoc} */
    public Set keySet() {

        Set set = new HashSet();
        Enumeration keys = session.getAttributeNames();
        while (keys.hasMoreElements()) {
            set.add(keys.nextElement());
        }
        return set;

    }


    /** {@inheritDoc} */
    public Object put(Object key, Object value) {

        if (value == null) {
            return remove(key);
        }
        String skey = key(key);
        Object previous = session.getAttribute(skey);
        session.setAttribute(skey, value);
        return previous;

    }


    /** {@inheritDoc} */
    public void putAll(Map map) {

        Iterator keys = map.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            session.setAttribute(key, map.get(key));
        }

    }


    /** {@inheritDoc} */
    public Object remove(Object key) {

        String skey = key(key);
        Object previous = session.getAttribute(skey);
        session.removeAttribute(skey);
        return previous;

    }


    /** {@inheritDoc} */
    public int size() {

        int n = 0;
        Enumeration keys = session.getAttributeNames();
        while (keys.hasMoreElements()) {
            keys.nextElement();
            n++;
        }
        return n;

    }


    /** {@inheritDoc} */
    public Collection values() {

        List list = new ArrayList();
        Enumeration keys = session.getAttributeNames();
        while (keys.hasMoreElements()) {
            list.add(session.getAttribute((String) keys.nextElement()));
        }
        return (list);

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the specified key, after converting it to a String.</p>
     *
     * @param key Key to be converted
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
