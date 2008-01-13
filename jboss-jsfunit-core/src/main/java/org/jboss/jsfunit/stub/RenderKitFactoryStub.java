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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

/**
 * <p>Stub implementation of <code>RenderKitFactory</code>.</p>
 *
 * $Id$
 */

public class RenderKitFactoryStub extends RenderKitFactory {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Return a default instance.</p>
     */
    public RenderKitFactoryStub() {

        renderKits = new HashMap();

    }


    // ----------------------------------------------------- Stub Object Methods


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The set of render kits that have been registered here.</p>
     */
    private Map renderKits = new HashMap();


    // ------------------------------------------------ RenderKitFactory Methods


    /** {@inheritDoc} */
    public void addRenderKit(String renderKitId, RenderKit renderKit) {

        if ((renderKitId == null) || (renderKit == null)) {
            throw new NullPointerException();
        }
        if (renderKits.containsKey(renderKitId)) {
            throw new IllegalArgumentException(renderKitId);
        }
        renderKits.put(renderKitId, renderKit);

    }


    /** {@inheritDoc} */
    public RenderKit getRenderKit(FacesContext context, String renderKitId) {

        if (renderKitId == null) {
            throw new NullPointerException();
        }
        RenderKit renderKit = (RenderKit) renderKits.get(renderKitId);
        if (renderKit == null) {
            // Issue 38294 -- We removed the automatic creation of the
            // default renderkit in the constructor, allowing it to be
            // added by AbstractJsfTestCase in the usual case.  To preserve
            // backwards compatibility, however, create one on the fly
            // if the user asks for the default HTML renderkit and it has
            // not been manually added yet
            if (RenderKitFactory.HTML_BASIC_RENDER_KIT.equals(renderKitId)) {
                renderKit = new RenderKitStub();
                renderKits.put(RenderKitFactory.HTML_BASIC_RENDER_KIT,
                               renderKit);
                return renderKit;
            }
            throw new IllegalArgumentException(renderKitId);
        }
        return renderKit;

    }


    /** {@inheritDoc} */
    public Iterator getRenderKitIds() {

        return renderKits.keySet().iterator();

    }


}
