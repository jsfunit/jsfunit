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
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import junit.framework.TestCase;

public class FacesConfigHandlerTestCase extends TestCase {
	
	private final Map<String, Class[]> EMPTY_CLASSES = new HashMap<String, Class[]>();
	private final Map<String, String[]> EMPTY_VALUES = new HashMap<String, String[]>();
	private final String CLASS_NAME = "com.ebay.Foo";
	
	public void testClasses() throws Exception{
		
		Map<String, Class[]> classes = new HashMap<String, Class[]>(){{
			put("foo", new Class[] {});
		}};
		
        FacesConfigHandler handler = new FacesConfigHandler(classes.keySet(), EMPTY_VALUES.keySet());
        InputStream is = new ByteArrayInputStream(("<root><foo>" + CLASS_NAME + "</foo><bar></bar></root>").getBytes());
        getParser().parse(is, handler);
        
        assertEquals(1, handler.getClassNamesByElement().keySet().size());
        assertEquals(CLASS_NAME, handler.getClassNamesByElement().get("foo").get(0));
        assertEquals(0, handler.getValuesByElement().keySet().size());
	}

	public void testValues() throws Exception{
		
		Map<String, String[]> values = new HashMap<String, String[]>(){{
			put("bar", new String[] {});
		}};	
		FacesConfigHandler handler = new FacesConfigHandler(EMPTY_CLASSES.keySet(), values.keySet());
		InputStream is = new ByteArrayInputStream(("<root><foo></foo><bar>" + CLASS_NAME + "</bar></root>").getBytes());
		getParser().parse(is, handler);

        assertEquals(0, handler.getClassNamesByElement().keySet().size());
        assertEquals(1, handler.getValuesByElement().keySet().size());
        assertEquals(CLASS_NAME, handler.getValuesByElement().get("bar").get(0));
	}
	
	public void testClassesAndValues() throws Exception{

		Map<String, Class[]> classes = new HashMap<String, Class[]>(){{
			put("foo", new Class[] {});
		}};
		Map<String, String[]> values = new HashMap<String, String[]>(){{
			put("bar", new String[] {});
		}};			
		FacesConfigHandler handler = new FacesConfigHandler(classes.keySet(), values.keySet());
		InputStream is = new ByteArrayInputStream(("<root><foo>" + CLASS_NAME + "</foo><bar>" + CLASS_NAME + "</bar></root>").getBytes());
		getParser().parse(is, handler);		

        assertEquals(1, handler.getClassNamesByElement().keySet().size());
        assertEquals(CLASS_NAME, handler.getClassNamesByElement().get("foo").get(0));
        assertEquals(1, handler.getValuesByElement().keySet().size());
        assertEquals(CLASS_NAME, handler.getValuesByElement().get("bar").get(0));
	}
	
	public void testNeither() throws Exception{
		
		FacesConfigHandler handler = new FacesConfigHandler(EMPTY_CLASSES.keySet(), EMPTY_VALUES.keySet());
		InputStream is = new ByteArrayInputStream(("<root><foo>" + CLASS_NAME + "</foo><bar>" + CLASS_NAME + "</bar></root>").getBytes());
		getParser().parse(is, handler);
		
		assertEquals(0, handler.getClassNamesByElement().keySet().size());
		assertEquals(0, handler.getValuesByElement().keySet().size());
	}
	
	private SAXParser getParser() throws ParserConfigurationException, SAXException {
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(false);
		return factory.newSAXParser();
		
	}
	
	public void testMultipleResources() throws Exception{
		
		Map<String, Class[]> classes = new HashMap<String, Class[]>(){{
			put("foo", new Class[] {});
		}};
		String xml = "<root><foo>" + CLASS_NAME + "</foo><bar></bar></root>";
		
        FacesConfigHandler handler = new FacesConfigHandler(classes.keySet(), EMPTY_VALUES.keySet());
        getParser().parse(new ByteArrayInputStream(xml.getBytes()), handler);
        getParser().parse(new ByteArrayInputStream(xml.getBytes()), handler);
        
        assertEquals(1, handler.getClassNamesByElement().keySet().size());
        assertEquals(CLASS_NAME, handler.getClassNamesByElement().get("foo").get(0));
        assertEquals(CLASS_NAME, handler.getClassNamesByElement().get("foo").get(1));
        assertEquals(0, handler.getValuesByElement().keySet().size());
		
	}

}