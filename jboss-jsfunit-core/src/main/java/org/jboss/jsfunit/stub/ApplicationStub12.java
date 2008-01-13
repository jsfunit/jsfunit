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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.el.ArrayELResolver;
import javax.el.BeanELResolver;
import javax.el.CompositeELResolver;
import javax.el.ELContextListener;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.ListELResolver;
import javax.el.MapELResolver;
import javax.el.ResourceBundleELResolver;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.jboss.jsfunit.stub.el.FacesImplicitObjectELResolver;
import org.jboss.jsfunit.stub.el.FacesPropertyResolverChainWrapper;
import org.jboss.jsfunit.stub.el.FacesResourceBundleELResolver;
import org.jboss.jsfunit.stub.el.FacesScopedAttributeELResolver;
import org.jboss.jsfunit.stub.el.FacesVariableResolverChainWrapper;
import org.jboss.jsfunit.stub.el.MockExpressionFactory;

/**
 * <p>Mock implementation of <code>ExternalContext</code> that includes the semantics
 * added by JavaServer Faces 1.2.</p>
 *
 * $Id: ApplicationStub12.java 503872 2007-02-05 21:10:06Z rahul $
 *
 * @since 1.0.4
 */
public class ApplicationStub12 extends ApplicationStub {
    

    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a default instance.</p>
     */
    public ApplicationStub12() {

        super();

        // Configure our expression factory and EL resolvers
        expressionFactory = new MockExpressionFactory();

    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>A list of resource bundles configured for this application.</p>
     */
    private Map bundles = new HashMap();


    /**
     * <p>The set of configured ELContextListener instances.</p>
     */
    private List elContextListeners = new ArrayList();


    /**
     * <p>Expression factory for this instance.</p>
     */
    private ExpressionFactory expressionFactory = null;


    /**
     * <p>The configured composite resolver to be returned by <code>getELResolver()</code>.
     * This value is lazily instantiated.</p>
     */
    private ELResolver resolver = null;


    /**
     * <p>The set of ELResolver instances configured on this instance.</p>
     */
    private List resolvers = new ArrayList();


    // ----------------------------------------------------- Mock Object Methods


    /**
     * <p>Add the specified resource bundle to those associated with
     * this application.</p>
     *
     * @param name Name under which to add this resource bundle
     * @param bundle ResourceBundle to add
     */
    public void addResourceBundle(String name, ResourceBundle bundle) {
        bundles.put(name, bundle);
    }


    /**
     * <p>Return a <code>Map</code> of the resource bundles configured
     * for this application, keyed by name.</p>
     */
    public Map getResourceBundles() {
        return bundles;
    }


    // ----------------------------------------------------- Application Methods


    /** {@inheritDoc} */
    public void addELContextListener(ELContextListener listener) {

        elContextListeners.add(listener);

    }


    /** {@inheritDoc} */
    public void addELResolver(ELResolver resolver) {

        // Simulate the restriction that you cannot add resolvers after
        // the first request has been processed.
        if (this.resolver != null) {
            throw new IllegalStateException("Cannot add resolvers now");
        }

        resolvers.add(resolver);

    }


    /** {@inheritDoc} */
    public UIComponent createComponent(ValueExpression expression,
                                       FacesContext context,
                                       String componentType) {

        UIComponent component = null;
        try {
            component = (UIComponent) expression.getValue(context.getELContext());
            if (component == null) {
                component = createComponent(componentType);
                expression.setValue(context.getELContext(), component);
            }

        } catch (Exception e) {
            throw new FacesException(e);
        }
        return component;

    }


    /** {@inheritDoc} */
    public Object evaluateExpressionGet(FacesContext context,
                                        String expression,
                                        Class expectedType) throws ELException {

        ValueExpression ve = getExpressionFactory().createValueExpression
          (context.getELContext(), expression, expectedType);
        return ve.getValue(context.getELContext());

    }


    /** {@inheritDoc} */
    public ELContextListener[] getELContextListeners() {

        return (ELContextListener[])
          elContextListeners.toArray(new ELContextListener[elContextListeners.size()]);

    }


    /** {@inheritDoc} */
    public ELResolver getELResolver() {

        if (resolver == null) {

            // Configure a default ELResolver per Section 5.6.2 of JSF 1.2
            CompositeELResolver composite = new CompositeELResolver();

            composite.add(new FacesImplicitObjectELResolver());

            CompositeELResolver nested = new CompositeELResolver();
            // FIXME - nested.add() "ELResolvers from application configuration resources"
            nested.add(new FacesVariableResolverChainWrapper());
            nested.add(new FacesPropertyResolverChainWrapper());
            Iterator items = resolvers.iterator();
            while (items.hasNext()) {
                nested.add((ELResolver) items.next());
            }
            composite.add(nested);

            // composite.add(new faces.ManagedBeanELResolver()); // FIXME
            composite.add(new ResourceBundleELResolver());
            composite.add(new FacesResourceBundleELResolver());
            composite.add(new MapELResolver());
            composite.add(new ListELResolver());
            composite.add(new ArrayELResolver());
            composite.add(new BeanELResolver());
            composite.add(new FacesScopedAttributeELResolver());

            // Make the resolver we have configured the application wide one
            resolver = composite;

        }
        return resolver;

    }


    /** {@inheritDoc} */
    public ExpressionFactory getExpressionFactory() {

        return this.expressionFactory;

    }


    /** {@inheritDoc} */
    public ResourceBundle getResourceBundle(FacesContext context, String name) {

        if ((context == null) || (name == null)) {
            throw new NullPointerException();
        }
        Locale locale = null;
        UIViewRoot viewRoot = context.getViewRoot();
        if (viewRoot != null) {
            locale = viewRoot.getLocale();
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return ResourceBundle.getBundle(name, locale);

    }


    /** {@inheritDoc} */
    public void removeELContextListener(ELContextListener listener) {

        elContextListeners.remove(listener);

    }


}
