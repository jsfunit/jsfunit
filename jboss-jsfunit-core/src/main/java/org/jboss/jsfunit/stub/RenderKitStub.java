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

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.render.ResponseStateManager;

/**
 * <p>Stub implementation of <code>RenderKit</code>.</p>
 *
 * $Id$
 */

public class RenderKitStub extends RenderKit {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Return a default instance.</p>
     */
    public RenderKitStub() {
    }


    // ----------------------------------------------------- Stub Object Methods


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The set of renderers registered here.</p>
     */
    private Map renderers = new HashMap();


    // ------------------------------------------------------- RenderKit Methods


    /** {@inheritDoc} */
    public void addRenderer(String family, String rendererType,
                            Renderer renderer) {

        if ((family == null) || (rendererType == null) || (renderer == null)) {
            throw new NullPointerException();
        }
        renderers.put(family + "|" + rendererType, renderer);

    }


    /** {@inheritDoc} */
    public Renderer getRenderer(String family, String rendererType) {

        if ((family == null) || (rendererType == null)) {
            throw new NullPointerException();
        }
        return (Renderer) renderers.get(family + "|" + rendererType);

    }


    /** {@inheritDoc} */
    public ResponseWriter createResponseWriter(Writer writer,
                                               String contentTypeList,
                                               String characterEncoding) {

       return new ResponseWriterStub(writer, contentTypeList, characterEncoding);

    }


    /** {@inheritDoc} */
    public ResponseStream createResponseStream(OutputStream out) {

        final OutputStream stream = out;
        return new ResponseStream() {

            public void close() throws IOException {
                stream.close();
            }

            public void flush() throws IOException {
                stream.flush();
            }

            public void write(byte[] b) throws IOException {
                stream.write(b);
            }

            public void write(byte[] b, int off, int len) throws IOException {
                stream.write(b, off, len);
            }

            public void write(int b) throws IOException {
                stream.write(b);
            }

        };


    }


    /** {@inheritDoc} */
    public ResponseStateManager getResponseStateManager() {

        throw new UnsupportedOperationException();

    }


}
