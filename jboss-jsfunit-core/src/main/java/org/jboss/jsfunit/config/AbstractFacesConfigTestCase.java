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
 * 
 */

package org.jboss.jsfunit.config;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Dennis Byrne
 */

public abstract class AbstractFacesConfigTestCase extends TestCase {

	protected final Map<String, Document> documentsByPath = new HashMap<String, Document>();
	
	private StreamProvider streamProvider;
	
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
	
	public AbstractFacesConfigTestCase(Set<String> facesConfigPaths) {
		this(facesConfigPaths, new ResourceUtils());
	}
	
	AbstractFacesConfigTestCase(Set<String> facesConfigPaths, StreamProvider streamProvider) {
		
		if(streamProvider == null)
			throw new IllegalArgumentException("stream provider is null");
		
		if(facesConfigPaths == null || facesConfigPaths.isEmpty())
			throw new IllegalArgumentException("facesConfigPaths is null or empty");
		
		this.streamProvider = streamProvider;
		parseResources(facesConfigPaths);
		
	}

	private void parseResources(Set<String> facesConfigPaths) {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringComments(true);
		factory.setIgnoringElementContentWhitespace(true);
		
		DocumentBuilder builder = null;
		
		try {
			 builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("Could not create a " + DocumentBuilder.class.getName());
		}
		
		for(String facesConfigPath : facesConfigPaths){
			String xml = getXml(facesConfigPath);
			Document document = null;
			try {
				document = builder.parse( new ByteArrayInputStream(xml.getBytes()));
			} catch (Exception e) {
				throw new RuntimeException("Could not parse document '" + facesConfigPath + "'\n" + xml, e);
			}
			documentsByPath.put(facesConfigPath, document);
		}
	}
	
	private String getXml(String resourcePath) {
		InputStream stream = streamProvider.getInputStream(resourcePath);
		
		if(stream == null)
			throw new RuntimeException("Could not locate faces config file '" + resourcePath + "'" );
		
		String xml = new ResourceUtils().getAsString(stream, resourcePath);
		
		// TODO find a better way to prevent DOM from going to the Internet
		int indexOf = xml.indexOf("<faces-config");
		if(indexOf > 0)
			xml = xml.substring(indexOf, xml.length());
		return xml;
	}

	public void testClassDefinitions() {
		
		Iterator<String> facesConfigPaths = documentsByPath.keySet().iterator();
		
		for( ; facesConfigPaths.hasNext() ; ) {
			String facesConfigPath = facesConfigPaths.next();
			classDefinitions(documentsByPath.get(facesConfigPath), facesConfigPath);
		}
	}
	
	private void classDefinitions(Node node, String faceConfigPath) {
		
		String nodeName = node.getNodeName();
		
		if(CLASS_CONSTRAINTS.keySet().contains(nodeName)) {
			
			Class clazz = new ClassUtils().loadClass(node.getNodeValue(), nodeName);
			Class[] constraints = CLASS_CONSTRAINTS.get(nodeName);
			
			if( constraints.length > 0 && ! isAssignableFrom(constraints, clazz) )
				throw new RuntimeException(faceConfigPath + ": " + clazz.getName() + ", in element " + nodeName 
						+ " should be a " + getConstraintsList(constraints));
		}else {
		
			NodeList children = node.getChildNodes();
			for(int i = 0; i < children.getLength(); i++)
				classDefinitions(children.item(i), faceConfigPath);
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
		
		for(int c = 0; c < constraints.length ; c++) 
			msg += constraints[c].getName() + ( (c == constraints.length - 1 ? "" : " or ") );
		
		return msg;
	}
	
	public void testManagedBeans() {

		Iterator<String> facesConfigPaths = documentsByPath.keySet().iterator();
		Map<String, String> managedBeanNames = new HashMap<String, String>();
		
		for( ; facesConfigPaths.hasNext() ; ) {
			String facesConfigPath = facesConfigPaths.next();
			managedBeans(documentsByPath.get(facesConfigPath), facesConfigPath, managedBeanNames);
		}
		
	}
	
	private void managedBeans(Node node, String facesConfigPath, final Map<String, String> managedBeanNames) {
		
		String nodeName = node.getNodeName();
		NodeList children = node.getChildNodes();
		
		if("managed-bean".equals(nodeName)) { 
			doManagedBean(node, facesConfigPath, children, managedBeanNames);
		}else { 
			for(int i = 0; i < children.getLength(); i++)
				managedBeans(children.item(i), facesConfigPath, managedBeanNames);
		}
	}

	private void doManagedBean(Node parent, String facesConfigPath, 
			NodeList children, final Map<String, String> managedBeanNames) {
		
		String name = null;
		String clazz = null;
		String scope = null;
		List<String> propertyNames = new LinkedList<String>();
		
		for(int i = 0 ; i < children.getLength(); i++) {
			Node child = children.item(i);
			String nodeName = child.getNodeName();
			String text = child.getTextContent();
			if("managed-bean-name".equals(nodeName)) 
				name = text;
			else if("managed-bean-scope".equals(nodeName)) 
				scope = text;
			else if("managed-bean-class".equals(nodeName)) 
				clazz = text;
			else if("managed-property".equals(nodeName))
				doManagedBeanProperty(child, propertyNames, name, facesConfigPath);
		}
		
		if(scope == null || "".equals(scope))
			throw new RuntimeException("could not determine scope of " + parent.getNodeName() + " '" 
					+ name + "' in " + facesConfigPath);
		if(clazz == null || "".equals(clazz))
			throw new RuntimeException("could not determine class of " + parent.getNodeName() + " '" 
					+ name + "' in " + facesConfigPath);
		if(name == null || "".equals(name))
			throw new RuntimeException("could not determine name of " + parent.getNodeName() + 
					" in " + facesConfigPath);
		
		if(managedBeanNames.keySet().contains(name))
			throw new RuntimeException("managed bean '" + name + "' in '" + facesConfigPath 
					+ "' is duplicated. Look for a managed bean w/ the same name in '" + managedBeanNames.get(name) + "'");
		
		managedBeanNames.put(name, facesConfigPath);
		
		List<String> scopes = new ArrayList<String>(){{
			add("none");
			add("request");
			add("session");
			add("application");
			}};
		
		if ( ! scopes.contains(scope) )
			throw new RuntimeException("managed bean '" + name + "' in " 
					+ facesConfigPath + " has an invalid scope '" + scope + "'");
		
		if(( "session".equals(scope) || "application".equals(scope) ) ) {
			Class managedBeanClass = new ClassUtils().loadClass(clazz, "managed-bean-class"); 
			if( ! Serializable.class.isAssignableFrom(managedBeanClass))
				throw new RuntimeException("Managed bean '" + parent.getNodeValue() + "' is in " 
						+ scope + " scope, so it needs to implement " + Serializable.class);
		}
	}

	private void doManagedBeanProperty(Node parent, final List<String> propertyNames, 
			String managedBeanName, String facesConfig) {

		NodeList children = parent.getChildNodes();
		
		for(int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if("property-name".equals(child.getNodeName())) {
				String name = child.getTextContent();
				if(propertyNames.contains(name))
					throw new RuntimeException("managed bean '" + managedBeanName 
							+ "' in " + facesConfig + " has a duplicate property named " + name);
				propertyNames.add(name);
			}
		}
	}
	
}