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

package org.jboss.jsfunit.stub.base;

import java.util.Iterator;

/**
 * <p>Abstract base class for testing <code>ViewController</code>
 * implementations.</p>
 *
 * <p><strong>WARNING</strong> - If you choose to subclass this class, be sure
 * your <code>setUp()</code> and <code>tearDown()</code> methods call
 * <code>super.setUp()</code> and <code>super.tearDown()</code> respectively,
 * and that you implement your own <code>suite()</code> method that exposes
 * the test methods for your test case.</p>
 */
public abstract class AbstractViewControllerTestCase extends AbstractJsfTestCase {

    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new instance of this test case.</p>
     *
     * @param name Test case name
     */
    public AbstractViewControllerTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // ------------------------------------------------------ Instance Variables


    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Test that the specified number of messages have been queued on the
     * <code>FacesContext</code> instance, without regard to matching a
     * particular client identifier.</p>
     *
     * @param expected The expected number of messages
     */
    protected void checkMessageCount(int expected) {

        int actual = 0;
        Iterator messages = facesContext.getMessages();
        while (messages.hasNext()) {
            messages.next();
            actual++;
        }
        assertEquals("Complete message count", expected, actual);

    }


    /**
     * <p>Test that the specified number of messages have been queued on the
     * <code>FacesContext</code> instance, for the specified client id.</p>
     *
     * @param clientId Client identifier of the component for which to
     *  count queued messages
     * @param expected The expected number of messages
     */
    protected void checkMessageCount(String clientId, int expected) {

        int actual = 0;
        Iterator messages = facesContext.getMessages(clientId);
        while (messages.hasNext()) {
            messages.next();
            actual++;
        }
        assertEquals("Complete message count", expected, actual);

    }


}
