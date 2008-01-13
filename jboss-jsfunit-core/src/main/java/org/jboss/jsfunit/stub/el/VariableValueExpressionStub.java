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

import javax.el.ELContext;
import javax.el.PropertyNotWritableException;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

/**
 * <p>Stub implementation of <code>ValueExpression</code> that wraps a variable.</p>
 */
public class VariableValueExpressionStub extends ValueExpression {
    

    // ------------------------------------------------------------ Constructors


    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 4475919948345298291L;


    /**
     * <p>Construct a new expression for the specified instance.</p>
     *
     * @param instance Variable instance to be wrapped
     * @param expectedType Expected type of the result
     */
    public VariableValueExpressionStub(Object instance, Class expectedType) {

        if (instance == null) {
            throw new NullPointerException("Instance cannot be null");
        }
        this.instance = instance;
        this.expectedType = expectedType;

    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The expected result type for <code>getValue()</code> calls.</p>
     */
    private Class expectedType = null;


    /**
     * <p>The variable instance being wrapped by this expression.</p>
     */
    private Object instance = null;


    // ------------------------------------------------------ Expression Methods


    /**
     * <p>Return <code>true</code> if this expression is equal to the
     * specified expression.</p>
     *
     * @param obj Object to be compared
     */
    public boolean equals(Object obj) {

        if ((obj != null) & (obj instanceof ValueExpression)) {
            return instance.toString().equals(((ValueExpression) obj).getExpressionString());
        } else {
            return false;
        }

    }


    /**
     * <p>Return the original String used to create this expression,
     * unmodified.</p>
     */
    public String getExpressionString() {

        return this.instance.toString();

    }


    /**
     * <p>Return the hash code for this expression.</p>
     */
    public int hashCode() {

        return this.instance.toString().hashCode();

    }


    /**
     * <p>Return <code>true</code> if the expression string for this expression
     * contains only literal text.</p>
     */
    public boolean isLiteralText() {

        return true;

    }


    // ------------------------------------------------- ValueExpression Methods


    /**
     * <p>Return the type that the result of this expression will
     * be coerced to.</p>
     */
    public Class getExpectedType() {

        return this.expectedType;

    }


    /**
     * <p>Evaluate this expression relative to the specified context,
     * and return the most general type that is acceptable for the
     * value passed in a <code>setValue()</code> call.</p>
     *
     * @param context ELContext for this evaluation
     */
    public Class getType(ELContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        return this.instance.getClass();

    }


    /**
     * <p>Evaluate this expression relative to the specified context,
     * and return the result.</p>
     *
     * @param context ELContext for this evaluation
     */
    public Object getValue(ELContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        FacesContext fcontext = (FacesContext) context.getContext(FacesContext.class);
        return fcontext.getApplication().getExpressionFactory().coerceToType(instance, expectedType);

    }


    /**
     * <p>Evaluate this expression relative to the specified context,
     * and return <code>true</code> if a call to <code>setValue()</code>
     * will always fail.</p>
     *
     * @param context ELContext for this evaluation
     */
    public boolean isReadOnly(ELContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        return true;

    }



    /**
     * <p>Evaluate this expression relative to the specified context,
     * and set the result to the specified value.</p>
     *
     * @param context ELContext for this evaluation
     * @param value Value to which the result should be set
     */
    public void setValue(ELContext context, Object value) {

        if (context == null) {
            throw new NullPointerException();
        }

        throw new PropertyNotWritableException();

    }


}
