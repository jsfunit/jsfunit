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
import java.util.Map;

import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 * <p>Stub implementation of <code>NavigationHandler</code>.</p>
 *
 * @author Apache Software Foundation
 */

public class NavigationHandlerStub extends NavigationHandler {


    // ------------------------------------------------------------ Constructors

    /**
     * <p>Construct a default instance.</p>
     */
    public NavigationHandlerStub() {
    }


    // ----------------------------------------------------- Stub Object Methods


    /**
     * <p>Add a outcome-viewId pair to the destinations map.</p>
     *
     * @param outcome Logical outcome string
     * @param viewId Destination view identifier
     */
    public void addDestination(String outcome, String viewId) {

        destinations.put(outcome, viewId);

    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>Set of destination view ids, keyed by logical outcome String
     * that will cause navigation to that view id.</p>
     */
    private Map destinations = new HashMap();


    // ----------------------------------------------- NavigationHandler Methods


    /**
     * <p>Process the specified navigation request.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param action Action method being executed
     * @param outcome Logical outcome from this action method
     */
    public void handleNavigation(FacesContext context,
                                 String action, String outcome) {

        // Navigate solely based on outcome, if we get a match
        String viewId = (String) destinations.get(outcome);
        if (viewId != null) {
            UIViewRoot view = getViewHandler(context).createView(context, viewId);
            context.setViewRoot(view);
        }

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the <code>ViewHandler</code> instance for this application.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     */
    private ViewHandler getViewHandler(FacesContext context) {

        return context.getApplication().getViewHandler();

    }


}
