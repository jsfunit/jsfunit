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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.MethodBinding;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.render.RenderKitFactory;
import javax.faces.validator.Validator;

/**
 * <p>Stub implementation of <code>Application</code>.</p>
 *
 * $Id$
 */

public class ApplicationStub extends Application {


    // ------------------------------------------------------------ Constructors

    /**
     * <p>Construct a default instance.</p>
     */
    public ApplicationStub() {

        setActionListener(new ActionListenerStub());
        components = new HashMap();
        converters = new HashMap();
        converters1 = new HashMap();
        setDefaultLocale(Locale.getDefault());
        setDefaultRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
        setNavigationHandler(new NavigationHandlerStub());
        setPropertyResolver(new PropertyResolverStub());
        setStateManager(new StateManagerStub());
        setSupportedLocales(new ArrayList());
        validators = new HashMap();
        setVariableResolver(new VariableResolverStub());
        setViewHandler(new ViewHandlerStub());

        // Register the standard by-id converters
        addConverter("javax.faces.BigDecimal", "javax.faces.convert.BigDecimalConverter");
        addConverter("javax.faces.BigInteger", "javax.faces.convert.BigIntegerConverter");
        addConverter("javax.faces.Boolean",    "javax.faces.convert.BooleanConverter");
        addConverter("javax.faces.Byte",       "javax.faces.convert.ByteConverter");
        addConverter("javax.faces.Character",  "javax.faces.convert.CharacterConverter");
        addConverter("javax.faces.DateTime",   "javax.faces.convert.DateTimeConverter");
        addConverter("javax.faces.Double",     "javax.faces.convert.DoubleConverter");
        addConverter("javax.faces.Float",      "javax.faces.convert.FloatConverter");
        addConverter("javax.faces.Integer",    "javax.faces.Convert.IntegerConverter");
        addConverter("javax.faces.Long",       "javax.faces.convert.LongConverter");
        addConverter("javax.faces.Number",     "javax.faces.convert.NumberConverter");
        addConverter("javax.faces.Short",      "javax.faces.convert.ShortConverter");

        // Register the standard by-type converters
        addConverter(Boolean.class,            "javax.faces.convert.BooleanConverter");
        addConverter(Boolean.TYPE,             "javax.faces.convert.BooleanConverter");
        addConverter(Byte.class,               "javax.faces.convert.ByteConverter");
        addConverter(Byte.TYPE,                "javax.faces.convert.ByteConverter");
        addConverter(Character.class,          "javax.faces.convert.CharacterConverter");
        addConverter(Character.TYPE,           "javax.faces.convert.CharacterConverter");
        addConverter(Double.class,             "javax.faces.convert.DoubleConverter");
        addConverter(Double.TYPE,              "javax.faces.convert.DoubleConverter");
        addConverter(Float.class,              "javax.faces.convert.FloatConverter");
        addConverter(Float.TYPE,               "javax.faces.convert.FloatConverter");
        addConverter(Integer.class,            "javax.faces.convert.IntegerConverter");
        addConverter(Integer.TYPE,             "javax.faces.convert.IntegerConverter");
        addConverter(Long.class,               "javax.faces.convert.LongConverter");
        addConverter(Long.TYPE,                "javax.faces.convert.LongConverter");
        addConverter(Short.class,              "javax.faces.convert.ShortConverter");
        addConverter(Short.TYPE,               "javax.faces.convert.ShortConverter");

    }


    // ----------------------------------------------------- Stub Object Methods


    // ------------------------------------------------------ Instance Variables


    private ActionListener actionListener = null;
    private Map components = null;
    private Map converters = null; // By id
    private Map converters1 = null; // By type
    private Locale defaultLocale = null;
    private String defaultRenderKitId = null;
    private String messageBundle = null;
    private NavigationHandler navigationHandler = null;
    private PropertyResolver propertyResolver = null;
    private StateManager stateManager = null;
    private Collection supportedLocales = null;
    private Map validators = null;
    private VariableResolver variableResolver = null;
    private ViewHandler viewHandler = null;


    // ----------------------------------------------------- Application Methods


    /** {@inheritDoc} */
    public ActionListener getActionListener() {

        return this.actionListener;

    }


    /** {@inheritDoc} */
    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }


    /** {@inheritDoc} */
    public Locale getDefaultLocale() {

        return this.defaultLocale;

    }

    /** {@inheritDoc} */
    public void setDefaultLocale(Locale defaultLocale) {

        this.defaultLocale = defaultLocale;

    }

    /** {@inheritDoc} */
    public String getDefaultRenderKitId() {

        return this.defaultRenderKitId;

    }

    /** {@inheritDoc} */
    public void setDefaultRenderKitId(String defaultRenderKitId) {

        this.defaultRenderKitId = defaultRenderKitId;

    }


    /** {@inheritDoc} */
    public String getMessageBundle() {

        return this.messageBundle;

    }


    /** {@inheritDoc} */
    public void setMessageBundle(String messageBundle) {

        this.messageBundle = messageBundle;

    }


    /** {@inheritDoc} */
    public NavigationHandler getNavigationHandler() {

        return this.navigationHandler;

    }


    /** {@inheritDoc} */
    public void setNavigationHandler(NavigationHandler navigationHandler) {

        this.navigationHandler = navigationHandler;

    }


    /** {@inheritDoc} */
    public PropertyResolver getPropertyResolver() {

        return this.propertyResolver;

    }


    /** {@inheritDoc} */
    public void setPropertyResolver(PropertyResolver propertyResolver) {

        this.propertyResolver = propertyResolver;

    }


    /** {@inheritDoc} */
    public StateManager getStateManager() {

        return this.stateManager;

    }


    /** {@inheritDoc} */
    public void setStateManager(StateManager stateManager) {

        this.stateManager = stateManager;

    }


    /** {@inheritDoc} */
    public Iterator getSupportedLocales() {

        return this.supportedLocales.iterator();

    }


    /** {@inheritDoc} */
    public void setSupportedLocales(Collection supportedLocales) {

        this.supportedLocales = supportedLocales;

    }


    /** {@inheritDoc} */
    public VariableResolver getVariableResolver() {

        return this.variableResolver;
    }


    /** {@inheritDoc} */
    public void setVariableResolver(VariableResolver variableResolver) {

        this.variableResolver = variableResolver;

    }


    /** {@inheritDoc} */
    public ViewHandler getViewHandler() {

        return this.viewHandler;

    }


    /** {@inheritDoc} */
    public void setViewHandler(ViewHandler viewHandler) {

        this.viewHandler = viewHandler;

    }


    /** {@inheritDoc} */
    public void addComponent(String componentType, String componentClass) {

        components.put(componentType, componentClass);

    }


    /** {@inheritDoc} */
    public UIComponent createComponent(String componentType) {

        if (componentType == null) {
            throw new NullPointerException("Requested component type is null");
        }
        String componentClass = (String) components.get(componentType);
        if (componentClass == null) {
            throw new FacesException("No component class registered for component type '"
                    + componentType + "'");
        }
        try {
            Class clazz = Class.forName(componentClass);
            return ((UIComponent) clazz.newInstance());
        } catch (Exception e) {
            throw new FacesException(e);
        }

    }


    /** {@inheritDoc} */
    public UIComponent createComponent(ValueBinding componentBinding,
                                       FacesContext context,
                                       String componentType)
        throws FacesException {

        UIComponent component = null;
        try {
            component = (UIComponent) componentBinding.getValue(context);
            if (component == null) {
                component = createComponent(componentType);
                componentBinding.setValue(context, component);
            }

        } catch (Exception e) {
            throw new FacesException(e);
        }
        return component;
    }


    /** {@inheritDoc} */
    public Iterator getComponentTypes() {

        return (components.keySet().iterator());

    }


    /** {@inheritDoc} */
    public void addConverter(String converterId, String converterClass) {

        converters.put(converterId, converterClass);

    }


    /** {@inheritDoc} */
    public void addConverter(Class targetClass, String converterClass) {

        converters1.put(targetClass, converterClass);

    }


    /** {@inheritDoc} */
    public Converter createConverter(String converterId) {

        String converterClass = (String) converters.get(converterId);
        if (converterClass == null) {
            return null;
        }
        try {
            Class clazz = Class.forName(converterClass);
            return ((Converter) clazz.newInstance());
        } catch (Exception e) {
            throw new FacesException(e);
        }

    }


    /** {@inheritDoc} */
    public Converter createConverter(Class targetClass) {

        String converterClass = (String) converters1.get(targetClass);
        if (converterClass == null) {
            return null;
        }
        try {
            Class clazz = Class.forName(converterClass);
            return ((Converter) clazz.newInstance());
        } catch (Exception e) {
            throw new FacesException(e);
        }

    }


    /** {@inheritDoc} */
    public Iterator getConverterIds() {

        return (converters.keySet().iterator());

    }


    /** {@inheritDoc} */
    public Iterator getConverterTypes() {

        return (converters1.keySet().iterator());

    }


    /** {@inheritDoc} */
    public MethodBinding createMethodBinding(String ref, Class[] params) {

        if (ref == null) {
            throw new NullPointerException();
        } else {
            return (new MethodBindingStub(this, ref, params));
        }

    }


    /** {@inheritDoc} */
    public ValueBinding createValueBinding(String ref) {

        if (ref == null) {
            throw new NullPointerException();
        } else {
            return (new ValueBindingStub(this, ref));
        }

    }


    /** {@inheritDoc} */
    public void addValidator(String validatorId, String validatorClass) {

        validators.put(validatorId, validatorClass);

    }


    /** {@inheritDoc} */
    public Validator createValidator(String validatorId) {

        String validatorClass = (String) validators.get(validatorId);
        try {
            Class clazz = Class.forName(validatorClass);
            return ((Validator) clazz.newInstance());
        } catch (Exception e) {
            throw new FacesException(e);
        }

    }


    /** {@inheritDoc} */
    public Iterator getValidatorIds() {
        return (validators.keySet().iterator());
    }


}