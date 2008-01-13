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

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * <p>Mock implementation of <code>Servlet</code>.</p>
 *
 * $Id$
 */

public class ServletStub implements Servlet {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a default Servlet instance.</p>
     */
    public ServletStub() {
    }


    /**
     * <p>Create a new Servlet with the specified ServletConfig.</p>
     *
     * @param config The new ServletConfig instance
     */
    public ServletStub(ServletConfig config) throws ServletException {
        init(config);
    }


    // ----------------------------------------------------- Mock Object Methods


    /**
     * <p>Set the <code>ServletConfig</code> instance for this servlet.</p>
     *
     * @param config The new ServletConfig instance
     */
    public void setServletConfig(ServletConfig config) {

        this.config = config;

    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The <code>ServletConfig</code> instance for this servlet.</p>
     */
    private ServletConfig config;


    // --------------------------------------------------------- Servlet Methods


    /** {@inheritDoc} */
    public void destroy() {
    }


    /** {@inheritDoc} */
    public ServletConfig getServletConfig() {

        return this.config;

    }


    /** {@inheritDoc} */
    public String getServletInfo() {

        return "ServletStub";

    }


    /** {@inheritDoc} */
    public void init(ServletConfig config) throws ServletException {

        this.config = config;

    }


    /** {@inheritDoc} */
    public void service(ServletRequest request, ServletResponse response)
      throws IOException, ServletException {

        // Do nothing by default

    }


}
