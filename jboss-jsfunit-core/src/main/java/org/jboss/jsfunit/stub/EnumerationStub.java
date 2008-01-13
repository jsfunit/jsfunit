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
import java.util.Iterator;

/**
 * <p>Mock implementation of an <code>Enumeration</code> wrapper around
 * an <code>Iterator</code>.</p>
 *
 * $Id$
 */

class EnumerationStub implements Enumeration {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a wrapper instance.</p>
     *
     * @param iterator The <code>Iterator</code> to be wrapped
     */
    public EnumerationStub(Iterator iterator) {

        this.iterator = iterator;

    }


    // ----------------------------------------------------- Mock Object Methods


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The <code>Iterator</code> we are wrapping.</p>
     */
    private Iterator iterator;


    // ----------------------------------------------------- Enumeration Methods


    /** {@inheritDoc} */
    public boolean hasMoreElements() {

        return iterator.hasNext();

    }


    /** {@inheritDoc} */
    public Object nextElement() {

        return iterator.next();

    }


}
