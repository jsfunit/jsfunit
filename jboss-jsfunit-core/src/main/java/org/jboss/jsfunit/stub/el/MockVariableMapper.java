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

import java.util.HashMap;
import java.util.Map;
import javax.el.ValueExpression;
import javax.el.VariableMapper;

/**
 * <p>Mock implementation of <code>VariableMapper</code>.</p>
 *
 * @since 1.0.4
 */

public class MockVariableMapper extends VariableMapper {
    

    // ------------------------------------------------------------ Constructors


    /** Creates a new instance of MockVariableMapper */
    public MockVariableMapper() {
    }
    

    // ------------------------------------------------------ Instance Variables


    /**
     * <p>Map of <code>ValueExpression</code>s, keyed by variable name.</p>
     */
    private Map expressions = new HashMap();


    // ----------------------------------------------------- Mock Object Methods


    // -------------------------------------------------- FunctionMapper Methods


    /** {@inheritDoc} */
    public ValueExpression resolveVariable(String variable) {

        return (ValueExpression) expressions.get(variable);

    }


    /** {@inheritDoc} */
    public ValueExpression setVariable(String variable, ValueExpression expression) {

        ValueExpression original = (ValueExpression) expressions.get(variable);
        if (expression == null) {
            expressions.remove(variable);
        } else {
            expressions.put(variable, expression);
        }
        return original;

    }


}
