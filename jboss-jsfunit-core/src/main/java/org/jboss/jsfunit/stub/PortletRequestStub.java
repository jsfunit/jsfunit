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

import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.WindowState;

/**
 * <p> Stub implementation of <code>PortletRequest</code>. </p>
 *
 * $Id: PortletRequestStub.java 516091 2007-03-08 16:25:17Z greddin $
 */

public class PortletRequestStub implements PortletRequest {

    // ------------------------------------------------------------ Constructors

    public PortletRequestStub() {

        super();

    }


    public PortletRequestStub(PortletSession session) {

        super();
        this.session = session;

    }


    // ----------------------------------------------------- Stub Object Methods

    /**
     * <p> Add a request parameter for this request. </p>
     *
     * @param name Parameter name
     * @param value Parameter value
     */
    public void addParameter(String name, String value) {

        String[] values = (String[]) parameters.get(name);
        if (values == null) {
            String[] results = new String[] { value };
            parameters.put(name, results);
            return;
        }
        String[] results = new String[values.length + 1];
        System.arraycopy(values, 0, results, 0, values.length);
        results[values.length] = value;
        parameters.put(name, results);

    }


    /**
     * <p> Set the <code>PortletSession</code> associated with this request.
     * </p>
     *
     * @param session The new session
     */
    public void setPortletSession(PortletSession session) {

        this.session = session;
    }


    /**
     * <p> Set the <code>Locale</code> associated with this request. </p>
     *
     * @param locale The new locale
     */
    public void setLocale(Locale locale) {

        this.locale = locale;

    }


    /**
     * <p> Set the <code>Principal</code> associated with this request. </p>
     *
     * @param principal The new Principal
     */
    public void setUserPrincipal(Principal principal) {

        this.principal = principal;

    }

    // ------------------------------------------------------ Instance Variables

    private Map attributes = new HashMap();
    private String contextPath = null;
    private Locale locale = null;
    private Map parameters = new HashMap();
    private Principal principal = null;
    private PortletSession session = null;


    // -------------------------------------------------- PortletRequest Methods


    /** {@inheritDoc} */
    public String getAuthType() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public String getContextPath() {

        return contextPath;

    }


    /** {@inheritDoc} */
    public Object getAttribute(String name) {

        return attributes.get(name);

    }


    /** {@inheritDoc} */
    public Enumeration getAttributeNames() {

        return new EnumerationStub(attributes.keySet().iterator());

    }


    /** {@inheritDoc} */
    public Locale getLocale() {

        return locale;
    }


    /** {@inheritDoc} */
    public Enumeration getLocales() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public String getParameter(String name) {

        String[] values = (String[]) parameters.get(name);
        if (values != null) {
            return values[0];
        } else {
            return null;
        }

    }


    /** {@inheritDoc} */
    public Map getParameterMap() {

        return parameters;

    }


    /** {@inheritDoc} */
    public Enumeration getParameterNames() {

        return new EnumerationStub(parameters.keySet().iterator());

    }


    /** {@inheritDoc} */
    public String[] getParameterValues(String name) {

        return (String[]) parameters.get(name);

    }


    /** {@inheritDoc} */
    public PortalContext getPortalContext() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public PortletMode getPortletMode() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public PortletSession getPortletSession() {

        return getPortletSession(true);

    }


    /** {@inheritDoc} */
    public PortletSession getPortletSession(boolean create) {

        if (create && (session == null)) {
            throw new UnsupportedOperationException();
        }
        return session;

    }


    /** {@inheritDoc} */
    public PortletPreferences getPreferences() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public Enumeration getProperties(String arg0) {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public String getProperty(String arg0) {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public Enumeration getPropertyNames() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public String getRemoteUser() {

        if (principal != null) {
            return principal.getName();
        } else {
            return null;
        }

    }


    /** {@inheritDoc} */
    public String getRequestedSessionId() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public String getResponseContentType() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public Enumeration getResponseContentTypes() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public String getScheme() {

        return ("http");

    }


    /** {@inheritDoc} */
    public String getServerName() {

        return ("localhost");

    }


    /** {@inheritDoc} */
    public int getServerPort() {

        return (8080);

    }


    /** {@inheritDoc} */
    public Principal getUserPrincipal() {

        return principal;

    }


    /** {@inheritDoc} */
    public WindowState getWindowState() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public boolean isPortletModeAllowed(PortletMode arg0) {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public boolean isRequestedSessionIdValid() {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public boolean isSecure() {

        return false;

    }


    /** {@inheritDoc} */
    public boolean isUserInRole(String arg0) {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public boolean isWindowStateAllowed(WindowState arg0) {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public void removeAttribute(String name) {

        if (attributes.containsKey(name)) {
            attributes.remove(name);
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
        attributes.put(name, value);

    }

}
