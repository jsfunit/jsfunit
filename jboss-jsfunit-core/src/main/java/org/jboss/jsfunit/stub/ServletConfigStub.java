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
import java.util.Hashtable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * <p>Stub implementation of <code>ServletConfig</code>.</p>
 *
 * @author Apache Software Foundation
 */

public class ServletConfigStub implements ServletConfig {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a default instance.</p>
     */
    public ServletConfigStub() {
    }


    /**
     * <p>Construct an instance associated with the specified
     * servlet context.</p>
     *
     * @param context The associated ServletContext
     */
    public ServletConfigStub(ServletContext context) {
        setServletContext(context);
    }


    // ----------------------------------------------------- Stub Object Methods


    /**
     * <p>Add a servlet initialization parameter.</p>
     *
     * @param name Parameter name
     * @param value Parameter value
     */
    public void addInitParameter(String name, String value) {

        parameters.put(name, value);

    }


    /**
     * <p>Set the servlet context for this application.</p>
     *
     * @param context The new servlet context
     */
    public void setServletContext(ServletContext context) {

        this.context = context;

    }


    // ------------------------------------------------------ Instance Variables


    private ServletContext context;
    private Hashtable parameters = new Hashtable();


    // --------------------------------------------------- ServletConfig Methods


    /** {@inheritDoc} */
    public String getInitParameter(String name) {

        return (String) parameters.get(name);

    }


    /** {@inheritDoc} */
    public Enumeration getInitParameterNames() {

        return parameters.keys();

    }


    /** {@inheritDoc} */
    public ServletContext getServletContext() {

        return this.context;

    }


    /** {@inheritDoc} */
    public String getServletName() {

        return "ServletStub";

    }


}
