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
import java.util.Iterator;
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
	
	private final static Map<String, List<String>> VALUE_CONSTRAINTS = new HashMap<String, List<String>>(){{
		put("managed-bean-scope", 
				new ArrayList<String>(){{
					add("none");
					add("request");
					add("session");
					add("application");
					}});
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
			throw new RuntimeException("Could not create " + DocumentBuilder.class.getName());
		}
		
		for(String facesConfigPath : facesConfigPaths){
			String xml = getXml(facesConfigPath);
			Document document = null;
			try {
				document = builder.parse( new ByteArrayInputStream(xml.getBytes()));
			} catch (Exception e) {
				throw new RuntimeException("Could not parse document " + facesConfigPath, e);
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
			testClassDefinitions(documentsByPath.get(facesConfigPath), facesConfigPath);
		}
	}
	
	private void testClassDefinitions(Node node, String faceConfigPath) {
		
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
				testClassDefinitions(children.item(i), faceConfigPath);
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
}