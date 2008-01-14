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

import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.jsfunit.stub.base.AbstractJsfTestCase;

/**
 * <p>Simple unit tests for Stub Objects that have behavior.</p>
 */

public class StubsTestCase extends AbstractJsfTestCase {


    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public StubsTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    protected void setUp() throws Exception {

        super.setUp();

        // Set up Servlet API Objects
        servletContext.addInitParameter("appParamName", "appParamValue");
        servletContext.setAttribute("appScopeName", "appScopeValue");
        servletContext.setAttribute("sameKey", "sameKeyAppValue");
        session.setAttribute("sesScopeName", "sesScopeValue");
        session.setAttribute("sameKey", "sameKeySesValue");
        request.setAttribute("reqScopeName", "reqScopeValue");
        request.setAttribute("sameKey", "sameKeyReqValue");
        request.setAttribute("test", new TestMockBean());

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(StubsTestCase.class));

    }


    // Tear down instance variables required by this test case.
    protected void tearDown() throws Exception {


        super.tearDown();

    }


    // ------------------------------------------------------ Instance Variables


    // ------------------------------------------------- Individual Test Methods


    public void testMethodBindingGetTypePositive() throws Exception {

        Class argsString[] = new Class[] { String.class };
        Class argsNone[] = new Class[0];

        checkMethodBindingGetType("test.getCommand", argsNone, String.class);
        checkMethodBindingGetType("test.setCommand", argsString,  null);
        checkMethodBindingGetType("test.getInput", argsNone, String.class);
        checkMethodBindingGetType("test.setInput", argsString, null);
        checkMethodBindingGetType("test.getOutput", argsNone, String.class);
        checkMethodBindingGetType("test.setOutput", argsString, null);
        checkMethodBindingGetType("test.combine", argsNone, String.class);

    }


    public void testMethodBindingInvokePositive() throws Exception {

        TestMockBean bean = (TestMockBean) request.getAttribute("test");
        MethodBinding mb = null;
        Class argsString[] = new Class[] { String.class };
        Class argsNone[] = new Class[0];
        assertEquals("::", bean.combine());

        mb = application.createMethodBinding("test.setCommand", argsString);
        mb.invoke(facesContext, new String[] { "command" });
        assertEquals("command", bean.getCommand());
        mb = application.createMethodBinding("test.setInput", argsString);
        mb.invoke(facesContext, new String[] { "input" });
        assertEquals("input", bean.getInput());
        mb = application.createMethodBinding("test.setOutput", argsString);
        mb.invoke(facesContext, new String[] { "output" });
        assertEquals("output", bean.getOutput());
        mb = application.createMethodBinding("test.combine", null);
        assertEquals("command:input:output", bean.combine());
        assertEquals("command:input:output", mb.invoke(facesContext, null));

    }


    // Positive tests for ValueBinding.getValue()
    public void testValueBindingGetValuePositive() throws Exception {

        // Implicit search
        checkValueBindingGetValue("appScopeName", "appScopeValue");
        checkValueBindingGetValue("sesScopeName", "sesScopeValue");
        checkValueBindingGetValue("reqScopeName", "reqScopeValue");
        checkValueBindingGetValue("sameKey", "sameKeyReqValue"); // Req scope

        // Explicit scope search
        checkValueBindingGetValue("applicationScope.appScopeName",
                                  "appScopeValue");
        checkValueBindingGetValue("applicationScope.sameKey",
                                  "sameKeyAppValue");
        checkValueBindingGetValue("sessionScope.sesScopeName",
                                  "sesScopeValue");
        checkValueBindingGetValue("sessionScope.sameKey",
                                  "sameKeySesValue");
        checkValueBindingGetValue("requestScope.reqScopeName",
                                  "reqScopeValue");
        checkValueBindingGetValue("requestScope.sameKey",
                                  "sameKeyReqValue");

    }


    // Positive tests for ValueBinding.putValue()
    public void testValueBindingPutValuePositive() throws Exception {

        ValueBinding vb = null;

        // New top-level variable
        assertNull(request.getAttribute("newSimpleName"));
        assertNull(session.getAttribute("newSimpleName"));
        assertNull(servletContext.getAttribute("newSimpleName"));
        vb = application.createValueBinding("newSimpleName");
        vb.setValue(facesContext, "newSimpleValue");
        assertEquals("newSimpleValue", request.getAttribute("newSimpleName"));
        assertNull(session.getAttribute("newSimpleName"));
        assertNull(servletContext.getAttribute("newSimpleName"));

        // New request-scope variable
        assertNull(request.getAttribute("newReqName"));
        assertNull(session.getAttribute("newReqName"));
        assertNull(servletContext.getAttribute("newReqName"));
        vb = application.createValueBinding("requestScope.newReqName");
        vb.setValue(facesContext, "newReqValue");
        assertEquals("newReqValue", request.getAttribute("newReqName"));
        assertNull(session.getAttribute("newReqName"));
        assertNull(servletContext.getAttribute("newReqName"));

        // New session-scope variable
        assertNull(request.getAttribute("newSesName"));
        assertNull(session.getAttribute("newSesName"));
        assertNull(servletContext.getAttribute("newSesName"));
        vb = application.createValueBinding("sessionScope.newSesName");
        vb.setValue(facesContext, "newSesValue");
        assertNull(request.getAttribute("newSesName"));
        assertEquals("newSesValue", session.getAttribute("newSesName"));
        assertNull(servletContext.getAttribute("newSesName"));

        // New application-scope variable
        assertNull(request.getAttribute("newAppName"));
        assertNull(session.getAttribute("newAppName"));
        assertNull(servletContext.getAttribute("newAppName"));
        vb = application.createValueBinding("applicationScope.newAppName");
        vb.setValue(facesContext, "newAppValue");
        assertNull(request.getAttribute("newAppName"));
        assertNull(session.getAttribute("newAppName"));
        assertEquals("newAppValue", servletContext.getAttribute("newAppName"));

        // Old top-level variable (just created)
        assertEquals("newSimpleValue", request.getAttribute("newSimpleName"));
        assertNull(session.getAttribute("newSimpleName"));
        assertNull(servletContext.getAttribute("newSimpleName"));
        vb = application.createValueBinding("newSimpleName");
        vb.setValue(facesContext, "newerSimpleValue");
        assertEquals("newerSimpleValue", request.getAttribute("newSimpleName"));
        assertNull(session.getAttribute("newSimpleName"));
        assertNull(servletContext.getAttribute("newSimpleName"));

        // Old hierarchically found variable
        assertEquals("sameKeyAppValue", servletContext.getAttribute("sameKey"));
        assertEquals("sameKeySesValue", session.getAttribute("sameKey"));
        assertEquals("sameKeyReqValue", request.getAttribute("sameKey"));
        vb = application.createValueBinding("sameKey");
        vb.setValue(facesContext, "sameKeyNewValue");
        assertEquals("sameKeyAppValue", servletContext.getAttribute("sameKey"));
        assertEquals("sameKeySesValue", session.getAttribute("sameKey"));
        assertEquals("sameKeyNewValue", request.getAttribute("sameKey"));


    }


    // --------------------------------------------------------- Private Methods


    private void checkMethodBindingGetType(String ref, Class params[],
                                           Class expected) throws Exception {

        MethodBinding mb = application.createMethodBinding(ref, params);
        assertNotNull("MethodBinding[" + ref + "] exists", mb);
        assertEquals("MethodBinding[" + ref + "] type",
                     expected,
                     mb.getType(facesContext));

    }


    private void checkValueBindingGetValue(String ref, Object expected) {

        ValueBinding vb = application.createValueBinding(ref);
        assertNotNull("ValueBinding[" + ref + "] exists", vb);
        assertEquals("ValueBinding[" + ref + "] value",
                     expected,
                     vb.getValue(facesContext));

    }


}
