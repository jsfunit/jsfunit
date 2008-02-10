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

import java.io.CharArrayWriter;
import java.io.PrintWriter;


/**
 * <p>Stub implementation of <code>PrintWriter</code>.</p>
 *
 * $Id: PrintWriterStub.java 464373 2006-10-16 04:21:54Z rahul $
 * @author Apache Software Foundation
 */

public class PrintWriterStub extends PrintWriter {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Return a default instance.</p>
     *
     * @param writer Temporary buffer storage for us to use
     */
    public PrintWriterStub(CharArrayWriter writer) {
        super(writer);
        this.caw = writer;
    }


    // ----------------------------------------------------- Stub Object Methods


    /**
     * <p>Return the content that has been written to this writer.</p>
     */
    public char[] content() {
        return caw.toCharArray();
    }


    /**
     * <p>Reset this output stream so that it appears no content has been
     * written.</p>
     */
    public void reset() {
        caw.reset();
    }


    /**
     * <p>Return the number of characters that have been written to this writer.</p>
     */
    public int size() {
        return caw.size();
    }



    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The writer we will use for buffering.</p>
     */
    private CharArrayWriter caw = null;


    // ----------------------------------------------------- PrintWriter Methods


}
