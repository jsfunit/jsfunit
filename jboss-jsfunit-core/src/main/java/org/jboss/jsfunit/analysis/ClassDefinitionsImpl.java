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

package org.jboss.jsfunit.analysis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

import org.jboss.jsfunit.analysis.util.ClassUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Dennis Byrne
 */

class ClassDefinitionsImpl {

	private final Map<String, Document> documentsByPath;
	
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
	
	public ClassDefinitionsImpl(Map<String, Document> documentsByPath) {
		this.documentsByPath = documentsByPath;
	}
	
	public void test() {
		
		Iterator<String> facesConfigPaths = documentsByPath.keySet().iterator();
		
		for( ; facesConfigPaths.hasNext() ; ) {
			String facesConfigPath = facesConfigPaths.next();
			classDefinitions(documentsByPath.get(facesConfigPath), facesConfigPath);
		}
		
	}
	
	private void classDefinitions(Node node, String faceConfigPath) {
		
		String nodeName = node.getNodeName();
		
		if(CLASS_CONSTRAINTS.keySet().contains(nodeName)) {
			
			Class clazz = new ClassUtils().loadClass(node.getTextContent(), nodeName);
			Class[] constraints = CLASS_CONSTRAINTS.get(nodeName);
			
			if( constraints.length > 0 && ! new ClassUtils().isAssignableFrom(constraints, clazz) )
				throw new RuntimeException(faceConfigPath + ": " + clazz.getName() + ", in element " + nodeName 
						+ " should be a " + new ClassUtils().getConstraintsList(constraints));
		}else {
		
			NodeList children = node.getChildNodes();
			for(int i = 0; i < children.getLength(); i++)
				classDefinitions(children.item(i), faceConfigPath);
		}
		
	}
	
}
