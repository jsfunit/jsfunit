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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.faces.application.Application;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;

/**
 * <p>Stub implementation of <code>MethodBinding</code>.</p>
 *
 * <p>This implementation is subject to the following restrictions:</p>
 * <ul>
 * <li>The portion of the method reference expression before the final
 *     "." must conform to the limitations of {@link ValueBindingStub}.</li>
 * <li>The name of the method to be executed cannot be delimited by "[]".</li>
 * </ul>
 */

public class MethodBindingStub extends MethodBinding implements StateHolder {


    // ------------------------------------------------------------ Constructors

    /**
     * <p>Construct a default instance.</p>
     */
    public MethodBindingStub() {
    }


    /**
     * <p>Construct a configured instance.</p>
     *
     * @param application Application instance for this application
     * @param ref Method binding expression to be parsed
     * @param args Signature of this method
     */
    public MethodBindingStub(Application application, String ref,
                             Class[] args) {

        this.application = application;
        this.args = args;
        if (ref.startsWith("#{") && ref.endsWith("}")) {
            ref = ref.substring(2, ref.length() - 1);
        }
        this.ref = ref;
        int period = ref.lastIndexOf(".");
        if (period < 0) {
            throw new ReferenceSyntaxException(ref);
        }
        vb = application.createValueBinding(ref.substring(0, period));
        name = ref.substring(period + 1);
        if (name.length() < 1) {
            throw new ReferenceSyntaxException(ref);
        }

    }


    // ------------------------------------------------------ Instance Variables


    private Application application;
    private Class args[];
    private String name;
    private String ref;
    private ValueBinding vb;


    // --------------------------------------------------- MethodBinding Methods


    /** {@inheritDoc} */
    public Object invoke(FacesContext context, Object[] params)
        throws EvaluationException, MethodNotFoundException {

        if (context == null) {
            throw new NullPointerException();
        }
        Object base = vb.getValue(context);
        if (base == null) {
            throw new EvaluationException("Cannot find object via expression \""
                                          + vb.getExpressionString() + "\"");
        }
        Method method = method(base);
        try {
            return (method.invoke(base, params));
        } catch (IllegalAccessException e) {
            throw new EvaluationException(e);
        } catch (InvocationTargetException e) {
            throw new EvaluationException(e.getTargetException());
        }

    }


    /** {@inheritDoc} */
    public Class getType(FacesContext context) {

        Object base = vb.getValue(context);
        Method method = method(base);
        Class returnType = method.getReturnType();
        if ("void".equals(returnType.getName())) {
            return (null);
        } else {
            return (returnType);
        }

    }

    /** {@inheritDoc} */
    public String getExpressionString() {
        return "#{" + ref + "}";
    }

    // ----------------------------------------------------- StateHolder Methods


    /** {@inheritDoc} */
    public Object saveState(FacesContext context) {
        Object values[] = new Object[4];
        values[0] = name;
        values[1] = ref;
        values[2] = UIComponentBase.saveAttachedState(context, vb);
        values[3] = args;
        return (values);
    }


    /** {@inheritDoc} */
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        name = (String) values[0];
        ref = (String) values[1];
        vb = (ValueBinding) UIComponentBase.restoreAttachedState(context, 
                                                                 values[2]);
        args = (Class []) values[3];
    }


    /**
     * <p>Flag indicating this is a transient instance.</p>
     */
    private boolean transientFlag = false;


    /** {@inheritDoc} */
    public boolean isTransient() {
        return (this.transientFlag);
    }


    /** {@inheritDoc} */
    public void setTransient(boolean transientFlag) {
        this.transientFlag = transientFlag;
    }

    /** {@inheritDoc} */
    public int hashCode() {
        if (ref == null) {
            return 0;
        } else {
            return ref.hashCode();
        }
    }

    /** {@inheritDoc} */
    public boolean equals(Object otherObj) {
        MethodBindingStub other = null;

        if (!(otherObj instanceof MethodBindingStub)) {
            return false;
        }
        other = (MethodBindingStub) otherObj;
        // test object reference equality
        if (this.ref != other.ref) {
            // test object equality
            if (null != this.ref && null != other.ref) {
                if (!this.ref.equals(other.ref)) {
                    return false;
                }
            }
            return false;
        }
        // no need to test name, since it flows from ref.
        // test our args array
        if (this.args != other.args) {
            if (this.args.length != other.args.length) {
                return false;
            }
            for (int i = 0, len = this.args.length; i < len; i++) {
                if (this.args[i] != other.args[i]) {
                    if (!this.ref.equals(other.ref)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the <code>Method</code> to be called.</p>
     *
     * @param base Base object from which to extract the method reference
     */
    Method method(Object base) {

        Class clazz = base.getClass();
        try {
            return (clazz.getMethod(name, args));
        } catch (NoSuchMethodException e) {
            throw new MethodNotFoundException(ref + ": " + e.getMessage());
        }

    }



}
