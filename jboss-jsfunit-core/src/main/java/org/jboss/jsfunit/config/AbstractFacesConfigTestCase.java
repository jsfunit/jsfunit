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

package org.jboss.jsfunit.config;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.ApplicationFactory;
import javax.faces.application.NavigationHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContextFactory;
import javax.faces.convert.Converter;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.faces.validator.Validator;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.TestCase;

public abstract class AbstractFacesConfigTestCase extends TestCase {

	protected Set<String> facesConfigPaths = new HashSet<String>();
	
	private final static Map<String, Class[]> CLASS_CONSTRAINTS = new HashMap<String, Class[]>(){{
		
		put("action-listener", new Class[]{ActionListener.class});
		put("navigation-handler", new Class[]{NavigationHandler.class});
		put("variable-resolver", new Class[]{VariableResolver.class});
		put("property-resolver", new Class[]{PropertyResolver.class});
		put("view-handler", new Class[]{ViewHandler.class});
		put("state-manager", new Class[] {StateManager.class});
		
		put("faces-context-factory", new Class[]{FacesContextFactory.class});
		put("application-factory", new Class[]{ApplicationFactory.class});
		put("lifecycle-factory", new Class[]{LifecycleFactory.class});
		put("render-kit-factory", new Class[]{RenderKitFactory.class});
		
		put("component-class", new Class[]{UIComponent.class});
		put("converter-class", new Class[]{Converter.class});
		put("validator-class", new Class[]{Validator.class});

		put("managed-bean-class", new Class[]{});
		put("key-class", new Class[]{});
		put("value-class", new Class[]{});
		
		put("render-kit-class", new Class[]{RenderKit.class});
		put("renderer-class", new Class[]{Renderer.class});
		
		put("phase-listener", new Class[]{PhaseListener.class});
		put("converter-for-class", new Class[]{});
		
	}};
	
	private final static Map<String, List<String>> VALUE_CONSTRAINTS = new HashMap<String, List<String>>(){{
		put("managed-bean-scope", 
				new ArrayList<String>(){{
					add("none");
					add("request");
					add("session");
					add("application");
					}});
	}};
	
	private Map<String, List<String>> classNamesByElement ;
	
	public AbstractFacesConfigTestCase(Set<String> facesConfigPaths) {
		this(facesConfigPaths, new ResourceUtils());
	}
	
	AbstractFacesConfigTestCase(Set<String> facesConfigPaths, StreamProvider streamProvider) {
		
		if(streamProvider == null)
			throw new IllegalArgumentException("stream provider is null");
		
		if(facesConfigPaths == null || facesConfigPaths.isEmpty())
			throw new IllegalArgumentException("facesConfigPaths is null or empty");
		
		parseResources(facesConfigPaths, streamProvider);

		this.facesConfigPaths = facesConfigPaths;
		
	}

	private void parseResources(Set<String> facesConfigPaths, StreamProvider streamProvider) {
		
		// create this once, as it holds state across > 1 conf source
		FacesConfigHandler handler = new FacesConfigHandler(CLASS_CONSTRAINTS.keySet(), VALUE_CONSTRAINTS.keySet());
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false); // TODO set to true
		factory.setNamespaceAware(false); // TODO set to true
		
		for(String resourcePath : facesConfigPaths) {
			String xml = getXml(streamProvider, resourcePath);
			try {
				factory.newSAXParser().parse(new ByteArrayInputStream(xml.getBytes()), handler);
			} catch (Exception e) {
				throw new RuntimeException("Could not parse XML:" + xml);
			}
		}
		
		classNamesByElement = handler.getClassNamesByElement();
	}

	private String getXml(StreamProvider streamProvider, String resourcePath) {
		InputStream stream = streamProvider.getInputStream(resourcePath);
		
		if(stream == null)
			throw new RuntimeException("Could not locate faces config file '" + resourcePath + "'" );
		
		String xml = new ResourceUtils().getAsString(stream, resourcePath);
		
		// TODO find a better way to prevent SAX from going to the Internet
		int indexOf = xml.indexOf("<faces-config");
		if(indexOf > 0)
			xml = xml.substring(indexOf, xml.length());
		return xml;
	}

	public void testClassDefinitions() {
		
		for(String elementName : classNamesByElement.keySet()) {
			
			List<String> classNames = classNamesByElement.get(elementName);
			
			for(String className : classNames) {
				
				Class clazz = new ClassUtils().loadClass(className, elementName);
				Class[] constraints = CLASS_CONSTRAINTS.get(elementName);
				
				if( constraints.length > 0 && ! isAssignableFrom(constraints, clazz) )
					throw new RuntimeException(clazz.getName() + ", in element " + elementName 
							+ " should be a " + getConstraintsList(constraints));
			}
		}
		
	}
	
	private boolean isAssignableFrom(Class[] constraints, Class clazz) {
		
		for(Class constraint : constraints) 
			if(constraint.isAssignableFrom(clazz)) 
				return true;
		
		return false;
	}
	
	private String getConstraintsList(Class[] constraints) {
		
		String msg = "";
		
		for(int c = 0; c < constraints.length ; c++) {
			String append = c == constraints.length - 1 ? "" : " or ";
			msg += constraints[c].getName() + append;
		}
		
		return msg;
	}
}
