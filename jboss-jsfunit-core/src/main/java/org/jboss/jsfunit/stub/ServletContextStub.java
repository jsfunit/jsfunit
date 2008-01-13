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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletException;


/**
 * <p>Stub implementation of <code>ServletContext</code>.</p>
 *
 * <p><strong>WARNING</strong> - Before you can get meaningful results from
 * calls to the <code>getResource()</code>, <code>getResourceAsStream()</code>,
 * <code>getResourcePaths()</code>, or <code>getRealPath()</code> methods,
 * you must configure the <code>documentRoot</code> property, passing in a
 * <code>File</code> object pointing at a directory that simulates a
 * web application structure.</p>
 *
 * $Id$
 */

public class ServletContextStub implements ServletContext {



    // ----------------------------------------------------- Stub Object Methods


    /**
     * <p>Add a new listener instance that should be notified about
     * attribute changes.</p>
     *
     * @param listener Listener to be added
     */
    public void addAttributeListener(ServletContextAttributeListener listener) {
        attributeListeners.add(listener);
    }


    /**
     * <p>Add a context initialization parameter to the set of
     * parameters recognized by this instance.</p>
     *
     * @param name Parameter name
     * @param value Parameter value
     */
    public void addInitParameter(String name, String value) {
        parameters.put(name, value);
    }


    /**
     * <p>Add a new MIME type mapping to the set of mappings
     * recognized by this instance.</p>
     *
     * @param extension Extension to check for (without the period)
     * @param contentType Corresponding content type
     */
    public void addMimeType(String extension, String contentType) {
        mimeTypes.put(extension, contentType);
    }


    /**
     * <p>Set the document root for <code>getRealPath()</code>
     * resolution.  This parameter <strong>MUST</strong> represent
     * a directory.</p>
     *
     * @param documentRoot The new base directory
     */
    public void setDocumentRoot(File documentRoot) {
        this.documentRoot = documentRoot;
    }


    // ------------------------------------------------------ Instance Variables


    private List attributeListeners = new ArrayList();
    private Hashtable attributes = new Hashtable();
    private File documentRoot = null;
    private Hashtable mimeTypes = new Hashtable();
    private Hashtable parameters = new Hashtable();


    // -------------------------------------------------- ServletContext Methods


    /** {@inheritDoc} */
    public Object getAttribute(String name) {

        return attributes.get(name);

    }


    /** {@inheritDoc} */
    public Enumeration getAttributeNames() {

        return attributes.keys();

    }


    /** {@inheritDoc} */
    public ServletContext getContext(String uripath) {

        throw new UnsupportedOperationException();

    }

    /** {@inheritDoc} */
    public String getContextPath() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public String getInitParameter(String name) {

        return (String) parameters.get(name);

    }


    /** {@inheritDoc} */
    public Enumeration getInitParameterNames() {

        return parameters.keys();

    }


    /** {@inheritDoc} */
    public int getMajorVersion() {

        return 2;

    }


    /** {@inheritDoc} */
    public String getMimeType(String path) {

        int period = path.lastIndexOf('.');
        if (period < 0) {
            return null;
        }
        String extension = path.substring(period + 1);
        return (String) mimeTypes.get(extension);

    }


    /** {@inheritDoc} */
    public int getMinorVersion() {

        return 4;

    }


    /** {@inheritDoc} */
    public RequestDispatcher getNamedDispatcher(String name) {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public String getRealPath(String path) {

        if (documentRoot != null) {
            if (!path.startsWith("/")) {
                throw new IllegalArgumentException("The specified path ('"
                        + path + "') does not start with a '/' character");
            }
            File resolved = new File(documentRoot, path.substring(1));
            try {
                return resolved.getCanonicalPath();
            } catch (IOException e) {
                return resolved.getAbsolutePath();
            }
        } else {
            return null;
        }

    }


    /** {@inheritDoc} */
    public RequestDispatcher getRequestDispatcher(String path) {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public URL getResource(String path) throws MalformedURLException {

        if (documentRoot != null) {
            if (!path.startsWith("/")) {
                throw new MalformedURLException("The specified path ('"
                        + path + "') does not start with a '/' character");
            }
            File resolved = new File(documentRoot, path.substring(1));
            if (resolved.exists()) {
                return resolved.toURL();
            } else {
                return null;
            }
        } else {
            return null;
        }

    }


    /** {@inheritDoc} */
    public InputStream getResourceAsStream(String path) {

        try {
            URL url = getResource(path);
            if (url != null) {
                return url.openStream();
            }
        } catch (Exception e) {
            ;
        }
        return null;

    }


    /** {@inheritDoc} */
    public Set getResourcePaths(String path) {

        if (documentRoot == null) {
            return null;
        }

        // Enforce the leading slash restriction
        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("The specified path ('"
                    + path + "') does not start with a '/' character");
        }

        // Locate the File node for this path's directory (if it exists)
        File node = new File(documentRoot, path.substring(1));
        if (!node.exists()) {
            return null;
        }
        if (!node.isDirectory()) {
            return null;
        }

        // Construct a Set containing the paths to the contents of this directory
        Set set = new HashSet();
        String[] files = node.list();
        if (files == null) {
            return null;
        }
        for (int i = 0; i < files.length; i++) {
            String subfile = path + files[i];
            File subnode = new File(node, files[i]);
            if (subnode.isDirectory()) {
                subfile += "/";
            }
            set.add(subfile);
        }

        // Return the completed set
        return set;

    }


    /** {@inheritDoc} */
    public Servlet getServlet(String name) throws ServletException {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public String getServletContextName() {

        return "ServletContextStub";

    }


    /** {@inheritDoc} */
    public String getServerInfo() {

        return "ServletContextStub";

    }


    /** {@inheritDoc} */
    public Enumeration getServlets() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public Enumeration getServletNames() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public void log(String message) {

        System.out.println(message);

    }


    /** {@inheritDoc} */
    public void log(Exception exception, String message) {

        System.out.println(message);
        exception.printStackTrace();

    }


    /** {@inheritDoc} */
    public void log(String message, Throwable exception) {

        System.out.println(message);
        exception.printStackTrace();

    }


    /** {@inheritDoc} */
    public void removeAttribute(String name) {

        if (attributes.containsKey(name)) {
            Object value = attributes.remove(name);
            fireAttributeRemoved(name, value);
        }

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


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Fire an attribute added event to interested listeners.</p>
     *
     * @param key Attribute whose value has been added
     * @param value The new value
     */
    private void fireAttributeAdded(String key, Object value) {
        if (attributeListeners.size() < 1) {
            return;
        }
        ServletContextAttributeEvent event =
                new ServletContextAttributeEvent(this, key, value);
        Iterator listeners = attributeListeners.iterator();
        while (listeners.hasNext()) {
            ServletContextAttributeListener listener =
                    (ServletContextAttributeListener) listeners.next();
            listener.attributeAdded(event);
        }
    }


    /**
     * <p>Fire an attribute removed event to interested listeners.</p>
     *
     * @param key Attribute whose value has been removed
     * @param value The value that was removed
     */
    private void fireAttributeRemoved(String key, Object value) {
        if (attributeListeners.size() < 1) {
            return;
        }
        ServletContextAttributeEvent event =
                new ServletContextAttributeEvent(this, key, value);
        Iterator listeners = attributeListeners.iterator();
        while (listeners.hasNext()) {
            ServletContextAttributeListener listener =
                    (ServletContextAttributeListener) listeners.next();
            listener.attributeRemoved(event);
        }
    }


    /**
     * <p>Fire an attribute replaced event to interested listeners.</p>
     *
     * @param key Attribute whose value has been replaced
     * @param value The original value
     */
    private void fireAttributeReplaced(String key, Object value) {
        if (attributeListeners.size() < 1) {
            return;
        }
        ServletContextAttributeEvent event =
                new ServletContextAttributeEvent(this, key, value);
        Iterator listeners = attributeListeners.iterator();
        while (listeners.hasNext()) {
            ServletContextAttributeListener listener =
                    (ServletContextAttributeListener) listeners.next();
            listener.attributeReplaced(event);
        }
    }


}
