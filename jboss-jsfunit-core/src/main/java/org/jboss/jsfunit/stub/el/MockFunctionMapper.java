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

package org.jboss.jsfunit.stub.el;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.el.FunctionMapper;

/**
 * <p>Mock implementation of <code>FunctionMapper</code>.</p>
 *
 * @since 1.0.4
 */

public class MockFunctionMapper extends FunctionMapper {
    

    // ------------------------------------------------------------ Constructors


    /** Creates a new instance of MockFunctionMapper */
    public MockFunctionMapper() {
    }
    

    // ------------------------------------------------------ Instance Variables


    /**
     * <p>Map of <code>Method</code> descriptors for static methods, keyed by
     * a string composed of the prefix (or "" if none), a ":", and the local name.</p>
     */
    private Map functions = new HashMap();


    // ----------------------------------------------------- Mock Object Methods


    /**
     * <p>Store a mapping of the specified prefix and localName to the
     * specified method, which must be static.</p>
     */
    public void mapFunction(String prefix, String localName, Method method) {

        functions.put(prefix + ":" + localName, method);

    }


    // -------------------------------------------------- FunctionMapper Methods


    /** {@inheritDoc} */
    public Method resolveFunction(String prefix, String localName) {

        return (Method) functions.get(prefix + ":" + localName);

    }


}
