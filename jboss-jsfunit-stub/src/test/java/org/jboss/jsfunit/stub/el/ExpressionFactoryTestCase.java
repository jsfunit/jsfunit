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
import junit.framework.Test;
import junit.framework.TestSuite;
import org.jboss.jsfunit.stub.ApplicationStub12;
import org.jboss.jsfunit.stub.base.AbstractJsfTestCase;
import org.jboss.jsfunit.stub.el.ExpressionFactoryStub;

/**
 * <p>Test case for <code>ExpressionFactory.</p>
 */
public class ExpressionFactoryTestCase extends AbstractJsfTestCase {
    

    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public ExpressionFactoryTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    protected void setUp() throws Exception {

        super.setUp();
        factory = (ExpressionFactoryStub)
          ((ApplicationStub12) application).getExpressionFactory();

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(ExpressionFactoryTestCase.class));

    }


    // Tear down instance variables required by this test case.
    protected void tearDown() throws Exception {

        factory = null;
        super.tearDown();

    }


    // -------------------------------------------------------- Static Variables


    /**
     * <p>Input values to be converted, of all the interesting types.</p>
     */
    private static final Object[] INPUT_VALUES = {
        (String) null,
        "",
        "1234",
        Boolean.TRUE,
        Boolean.FALSE,
        new Byte((byte) 123),
        new Double(234),
        new Float(345),
        new Integer(456),
        new Long(567),
        new Short((short) 678),
    };


    /**
     * <p>Output values when converted to Boolean.</p>
     */
    private static final Boolean[] OUTPUT_BOOLEAN = {
        Boolean.FALSE,
        Boolean.FALSE,
        Boolean.FALSE,
        Boolean.TRUE,
        Boolean.FALSE,
        null,
        null,
        null,
        null,
        null,
        null
    };


    /**
     * <p>Output values when converted to Integer.</p>
     */
    private static final Integer[] OUTPUT_INTEGER = {
        new Integer(0),
        new Integer(0),
        new Integer(1234),
        new Integer(1),
        new Integer(0),
        new Integer(123),
        new Integer(234),
        new Integer(345),
        new Integer(456),
        new Integer(567),
        new Integer(678)
    };


    /**
     * <p>Output values when converted to String.</p>
     */
    private static final String[] OUTPUT_STRING = {
        "",
        "",
        "1234",
        "true",
        "false",
        "123",
        "234.0",
        "345.0",
        "456",
        "567",
        "678"
    };


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The expression factory to be tested.</p>
     */
    private ExpressionFactoryStub factory = null;


    // ------------------------------------------------- Individual Test Methods


    // Test coercion when expectedType is passed as Boolean.
    public void testCoerceToBoolean() {

        Object result = null;
        for (int i = 0; i < INPUT_VALUES.length; i++) {
            try {
                result = factory.coerceToType(INPUT_VALUES[i], Boolean.class);
                if ((INPUT_VALUES[i] == null)
                 || (INPUT_VALUES[i] instanceof String)
                 || (INPUT_VALUES[i] instanceof Boolean)) {
                    assertEquals("[" + i + "]", OUTPUT_BOOLEAN[i], result);
                } else {
                    fail("[" + i + "] should have thrown IllegalArgumentException");
                }
            } catch (IllegalArgumentException e) {
                if ((INPUT_VALUES[i] == null)
                 || (INPUT_VALUES[i] instanceof String)
                 || (INPUT_VALUES[i] instanceof Boolean)) {
                    fail("[" + i + "] threw IllegalArgumentException");
                } else {
                    ; // Expected result
                }
            }
        }

    }


    // Test coercion when expectedType is passed as null.  We should
    // get the original object back
    public void testCoerceToNull() {

        Object result = null;
        for (int i = 0; i < INPUT_VALUES.length; i++) {
            result = factory.coerceToType(INPUT_VALUES[i], null);
            if (INPUT_VALUES[i] == null) {
                assertNull(result);
            } else {
                assertTrue("[" + i + "]", result == INPUT_VALUES[i]);
            }
        }

    }


    // Test coercion when expectedType is Object.  We should
    // get the original object back.
    public void testCoerceToObject() {

        Object result = null;
        for (int i = 0; i < INPUT_VALUES.length; i++) {
            result = factory.coerceToType(INPUT_VALUES[i], Object.class);
            if (INPUT_VALUES[i] == null) {
                assertNull(result);
            } else {
                assertTrue("[" + i + "]", result == INPUT_VALUES[i]);
            }
        }

    }


    // Test coercion when expectedType is Integer
    public void testCoerceToInteger() {

        Object result = null;
        for (int i = 0; i < INPUT_VALUES.length; i++) {
            try {
                result = factory.coerceToType(INPUT_VALUES[i], Integer.class);
                if ((INPUT_VALUES[i] != null)
                 && (INPUT_VALUES[i] instanceof Boolean)) {
                    fail("[" + i + "] should have thrown IllegalArgumentException");
                } else {
                    assertEquals("[" + i + "]", OUTPUT_INTEGER[i], result);
                }
            } catch (IllegalArgumentException e) {
                if ((INPUT_VALUES[i] != null)
                 && (INPUT_VALUES[i] instanceof Boolean)) {
                    ; // Expected result
                } else {
                    fail("[" + i + "] should have thrown IllegalArgumentException");
                }
            }
        }

    }


    // Test coercion when expectedType is String.
    public void testCoerceToString() {

        Object result = null;
        for (int i = 0; i < INPUT_VALUES.length; i++) {
            result = factory.coerceToType(INPUT_VALUES[i], String.class);
            assertEquals("[" + i + "]", OUTPUT_STRING[i], result);
        }

    }


    // Test ValueExpression that wraps a literal String object and conversion to Integer
    public void testLiteralValueExpressionInteger() {

        ELContext context = facesContext.getELContext();

        ValueExpression expr = factory.createValueExpression("123", Integer.class);
        assertEquals(Integer.class, expr.getExpectedType());
        assertEquals(String.class, expr.getType(context));
        assertEquals(new Integer(123), expr.getValue(context));
        assertTrue(expr.isLiteralText());
        assertTrue(expr.isReadOnly(context));
        try {
            expr.setValue(context, "234");
            fail("Should have thrown PropertyNotWritableException");
        } catch (PropertyNotWritableException e) {
            ; // Expected result
        }

    }


    // Test ValueExpression that wraps a literal String object and no conversion
    public void testLiteralValueExpressionNone() {

        ELContext context = facesContext.getELContext();

        ValueExpression expr = factory.createValueExpression("abc", String.class);
        assertEquals(String.class, expr.getExpectedType());
        assertEquals(String.class, expr.getType(context));
        assertEquals("abc", expr.getValue(context));
        assertTrue(expr.isLiteralText());
        assertTrue(expr.isReadOnly(context));
        try {
            expr.setValue(context, "def");
            fail("Should have thrown PropertyNotWritableException");
        } catch (PropertyNotWritableException e) {
            ; // Expected result
        }

    }


    // Test ValueExpression that wraps a literal Integer object and conversion to String
    public void testLiteralValueExpressionString() {

        ELContext context = facesContext.getELContext();

        ValueExpression expr = factory.createValueExpression(new Integer(123), String.class);
        assertEquals(String.class, expr.getExpectedType());
        assertEquals(Integer.class, expr.getType(context));
        assertEquals("123", expr.getValue(context));
        assertTrue(expr.isLiteralText());
        assertTrue(expr.isReadOnly(context));
        try {
            expr.setValue(context, new Integer(234));
            fail("Should have thrown PropertyNotWritableException");
        } catch (PropertyNotWritableException e) {
            ; // Expected result
        }

    }


    // Test ValueExpression
    public void testValueExpressionString() {

        request.setAttribute("org.apache.shale.test", new Integer(123));
        ELContext context = facesContext.getELContext();

        ValueExpression expr = factory.createValueExpression(context, "#{requestScope['org.apache.shale.test']}", String.class);
        Object ref = expr.getValue(context);
        assertNotNull(ref);
        assertTrue(ref instanceof String);
        assertEquals("123", ref);
    }

    public void testPristine() {

        assertNotNull(factory);

    }



}
