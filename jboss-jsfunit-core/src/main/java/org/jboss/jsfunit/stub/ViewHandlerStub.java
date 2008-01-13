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

import java.util.Locale;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKitFactory;

/**
 * <p>Mock implementation of <code>ViewHandler</code>.</p>
 *
 * $Id$
 */

public class ViewHandlerStub extends ViewHandler {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a default instance.</p>
     */
    public ViewHandlerStub() {
    }


    // ----------------------------------------------------- Mock Object Methods


    // ------------------------------------------------------ Instance Variables


    // ----------------------------------------------------- ViewHandler Methods


    /** {@inheritDoc} */
    public Locale calculateLocale(FacesContext context) {

        Locale locale = context.getApplication().getDefaultLocale();
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return locale;

    }


    /** {@inheritDoc} */
    public String calculateRenderKitId(FacesContext context) {

        String renderKitId = context.getApplication().getDefaultRenderKitId();
        if (renderKitId == null) {
            renderKitId = RenderKitFactory.HTML_BASIC_RENDER_KIT;
        }
        return renderKitId;

    }


    /** {@inheritDoc} */
    public UIViewRoot createView(FacesContext context, String viewId) {

        // Save locale and renderKitId from previous view (if any), per spec
        Locale locale = null;
        String renderKitId = null;
        if (context.getViewRoot() != null) {
            locale = context.getViewRoot().getLocale();
            renderKitId = context.getViewRoot().getRenderKitId();
        }

        // Configure a new UIViewRoot instance
        UIViewRoot view = new UIViewRoot();
        view.setViewId(viewId);
        if (locale != null) {
            view.setLocale(locale);
        } else {
            view.setLocale
              (context.getApplication().getViewHandler().calculateLocale(context));
        }
        if (renderKitId != null) {
            view.setRenderKitId(renderKitId);
        } else {
            view.setRenderKitId
              (context.getApplication().getViewHandler().calculateRenderKitId(context));
        }

        // Return the configured instance
        return view;

    }


    /** {@inheritDoc} */
    public String getActionURL(FacesContext context, String viewId) {

        return FacesContext.getCurrentInstance().getExternalContext().
                getRequestContextPath() + viewId;

    }


    /** {@inheritDoc} */
    public String getResourceURL(FacesContext context, String path) {

        return FacesContext.getCurrentInstance().getExternalContext().
                getRequestContextPath() + path;

    }


    /** {@inheritDoc} */
    public void renderView(FacesContext context, UIViewRoot view) {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public UIViewRoot restoreView(FacesContext context, String viewId) {

        throw new UnsupportedOperationException();

    }


    /** {@inheritDoc} */
    public void writeState(FacesContext context) {

    }


}
