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

import java.io.ByteArrayOutputStream;
import javax.servlet.ServletOutputStream;

/**
 * <p>Mock implementation of <code>ServletOutputStream</code>.</p>
 *
 * $Id: ServletOutputStreamStub.java 464373 2006-10-16 04:21:54Z rahul $
 */

public class ServletOutputStreamStub extends ServletOutputStream {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Return a default instance.</p>
     *
     * @param stream The stream we will use to buffer output
     */
    public ServletOutputStreamStub(ByteArrayOutputStream stream) {
        this.baos = stream;
    }


    // ----------------------------------------------------- Mock Object Methods


    /**
     * <p>Return the content that has been written to this output stream.</p>
     */
    public byte[] content() {
        return baos.toByteArray();
    }


    /**
     * <p>Reset this output stream so that it appears no content has been
     * written.</p>
     */
    public void reset() {
        baos.reset();
    }


    /**
     * <p>Return the number of bytes that have been written to this output stream.</p>
     */
    public int size() {
        return baos.size();
    }



    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The internal buffer we use to capture output.</p>
     */
    private ByteArrayOutputStream baos = null;


    // --------------------------------------------- ServletOutputStream Methods


    /**
     * <p>Write the specified content to our internal cache.</p>
     *
     * @param content Content to be written
     */
    public void write(int content) {
        baos.write(content);
    }


}
