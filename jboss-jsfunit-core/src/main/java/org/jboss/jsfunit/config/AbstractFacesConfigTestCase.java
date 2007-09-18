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

import org.xml.sax.HandlerBase;

public abstract class AbstractFacesConfigTestCase extends TestCase {

	protected Set<String> facesConfigPaths = new HashSet<String>();
	
	private final Map<String, Class[]> CLASS_CONSTRAINTS = new HashMap<String, Class[]>(){{
		
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
	
	private final Map<String, List<String>> VALUE_CONSTRAINTS = new HashMap<String, List<String>>(){{
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
		
		if(facesConfigPaths == null)
			throw new IllegalArgumentException("facesConfigPaths is null");
		
		if(facesConfigPaths.isEmpty())
			throw new IllegalArgumentException("facesConfigPaths is empty");
		
		parseResources(facesConfigPaths, streamProvider);

		this.facesConfigPaths = facesConfigPaths;
		
	}

	private void parseResources(Set<String> facesConfigPaths, StreamProvider streamProvider) {
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false); // TODO set to true
		factory.setNamespaceAware(false); // TODO set to true
		
		for(String resourcePath : facesConfigPaths) 
			parseResource(streamProvider, factory, resourcePath);
		
	}

	private void parseResource(StreamProvider streamProvider, SAXParserFactory factory, String resourcePath) {

		String xml = getXml(streamProvider, resourcePath);
		
		FacesConfigHandler handler = handle(factory, xml);
		
		for(String elementName : handler.getClasses().keySet()) {
			
			List<String> classNames = handler.getClasses().get(elementName);
			
			for(String className : classNames) {
				 
				Class clazz = new ClassUtils().loadClass(className, elementName);
				Class[] constraints = CLASS_CONSTRAINTS.get(elementName);
				
				if( ! isAssignableFrom(constraints, clazz) )
					throw new RuntimeException(clazz.getName() + ", in element " + elementName 
							+ " should be a " + getConstraintsList(constraints));
			}
		}
	}
	
	private String getConstraintsList(Class[] constraints) {
		
		String msg = "";
		
		for(int c = 0; c < constraints.length ; c++) {
			String append = c == constraints.length - 1 ? "" : " or ";
			msg += constraints[c].getName() + append;
		}
		
		return msg;
	}
	
	private boolean isAssignableFrom(Class[] constraints, Class clazz) {
		
		for(Class constraint : constraints) 
			if(constraint.isAssignableFrom(clazz)) 
				return true;
		
		return false;
	}
	
	private FacesConfigHandler handle(SAXParserFactory factory, String xml) {
		FacesConfigHandler handler = new FacesConfigHandler(CLASS_CONSTRAINTS.keySet(), VALUE_CONSTRAINTS.keySet());
		
		try {
			factory.newSAXParser().parse(new ByteArrayInputStream(xml.getBytes()), handler);
		} catch (Exception e) {
			throw new RuntimeException("Could not parse XML:" + xml);
		}
		return handler;
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

	public void testConfiguration() {
		
		// move constructor code to here
		
	}
}
