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

import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.VariableResolver;

/**
 * <p>Stub implementation of <code>VariableResolver</code>.</p>
 *
 * <p>This implementation recognizes the standard scope names
 * <code>applicationScope</code>, <code>facesContext</code>,
 * <code>RequestScope</code>, and
 * <code>sessionScope</code>, plus it knows how to search in ascending
 * scopes for non-reserved names.</p>
 *
 * @author Apache Software Foundation
 */

public class VariableResolverStub extends VariableResolver {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a default instance.</p>
     */
    public VariableResolverStub() {
    }


    // ----------------------------------------------------- Stub Object Methods


    // ------------------------------------------------------ Instance Variables


    // ------------------------------------------------ VariableResolver Methods


    /** {@inheritDoc} */
    public Object resolveVariable(FacesContext context, String name) {

        if ((context == null) || (name == null)) {
            throw new NullPointerException();
        }

        // Check for magic names
        if ("application".equals(name)) {
            return external().getContext();
        } else if ("applicationScope".equals(name)) {
            return external().getApplicationMap();
        } else if ("cookie".equals(name)) {
            return external().getRequestCookieMap();
        } else if ("facesContext".equals(name)) {
            return FacesContext.getCurrentInstance();
        } else if ("header".equals(name)) {
            return external().getRequestHeaderMap();
        } else if ("headerValues".equals(name)) {
            return external().getRequestHeaderValuesMap();
        } else if ("param".equals(name)) {
            return external().getRequestParameterMap();
        } else if ("paramValues".equals(name)) {
            return external().getRequestParameterValuesMap();
        } else if ("request".equals(name)) {
            return external().getRequest();
        } else if ("requestScope".equals(name)) {
            return external().getRequestMap();
        } else if ("response".equals(name)) {
            return external().getResponse();
        } else if ("session".equals(name)) {
            return external().getSession(true);
        } else if ("sessionScope".equals(name)) {
            return external().getSessionMap();
        } else if ("view".equals(name)) {
            return FacesContext.getCurrentInstance().getViewRoot();
        }

        // Search ascending scopes for non-magic names
        Map map = null;
        map = external().getRequestMap();
        if (map.containsKey(name)) {
            return map.get(name);
        }
        map = external().getSessionMap();
        if ((map != null) && (map.containsKey(name))) {
            return map.get(name);
        }
        map = external().getApplicationMap();
        if (map.containsKey(name)) {
            return map.get(name);
        }

        // No such variable can be found
        return null;

    }



    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the <code>ExternalContext</code> for this request.</p>
     */
    private ExternalContext external() {

        return FacesContext.getCurrentInstance().getExternalContext();

    }


}
