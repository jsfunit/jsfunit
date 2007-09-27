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

package org.jboss.jsfunit.analysis;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.jboss.jsfunit.analysis.AbstractFacesConfigTestCase;
import org.jboss.jsfunit.analysis.StreamProvider;
import org.jboss.jsfunit.analysis.model.AtomicIntegerValidator;
import org.jboss.jsfunit.analysis.model.Pojo;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/**
 * @author Dennis Byrne
 */

public class ClassDefinitionsTestCase extends TestCase{
	
	private static final String CORRECT = "<validator>" 
		+ "<validator-id>javax.faces.DoubleRange</validator-id>"
		+ "<validator-class>" + AtomicIntegerValidator.class.getName() + "</validator-class>" 
		+ "</validator>";
	
	private static final String NON_EXISTING_ACTION_LISTENER 
		= "<application><action-listener>com.nonexist.Foo</action-listener></application>";
	
	public void testEmptyFacesConfig() {

		try {

			new AbstractFacesConfigTestCase(null){ };

			fail("should fail if created w/ null");

		}catch(Exception t) { }

		try {
			
			new AbstractFacesConfigTestCase(new HashSet<String>()) {};
			
			fail("should fail w/ empty paths");
			
		}catch(Exception t) { }
		
	}

	public void testNonExistingResource() {
		
		try {
			
			new AbstractFacesConfigTestCase(new HashSet<String>() {{
				add("non-exist");
			}}) {};
			
			fail("file does not exists");
			
		}catch(Exception t) {}
		
	}
	
	public void testEmptyFacesConfiguration() {

		new AbstractFacesConfigTestCase(TestUtils.STUBBED_RESOURCEPATH, new StringStreamProvider(TestUtils.getFacesConfig(""))) {};
		
	}

	public void testMalFormed() {

		try {
			
			new AbstractFacesConfigTestCase(TestUtils.STUBBED_RESOURCEPATH, new StringStreamProvider(TestUtils.getFacesConfig("<"))) {};
			
			fail("malformed xml should fail");
			
		}catch(Exception e) { }
		
	}

	public void testFacesConfigHappyPath() {
		StreamProvider streamProvider = new StringStreamProvider(TestUtils.getFacesConfig(CORRECT));
		new AbstractFacesConfigTestCase(TestUtils.STUBBED_RESOURCEPATH, streamProvider) {}.testClassDefinitions();		
	}
	
	public void testFacesConfigElementsMissingInterface() {
		
		String body = "<application><action-listener>" + Pojo.class.getName() + "</action-listener></application>";
		
		try {
			
			new AbstractFacesConfigTestCase(TestUtils.STUBBED_RESOURCEPATH, 
					new StringStreamProvider(TestUtils.getFacesConfig(body))) {}.testClassDefinitions();
			
			throw new RuntimeException("should have failed ");
			
		}catch(AssertionFailedError e) { }
		
	}
	
	public void testFacesConfigElementsNonExistingClass() {
		
		try {
			
			new AbstractFacesConfigTestCase(TestUtils.STUBBED_RESOURCEPATH, 
					new StringStreamProvider(TestUtils.getFacesConfig(NON_EXISTING_ACTION_LISTENER))) {}.testClassDefinitions();
			
			throw new RuntimeException("should have failed ");
			
		}catch(AssertionFailedError e) { }
		
	}
	
	public void testCanHandleMoreThanOneConfigFile() {
		
		Set<String> paths = new HashSet<String>() {{
			add("stubbed resource path");
			add("second stubbed resource path");
		}};
		
		StreamProvider streamProvider = new StreamProvider() {
			
			private InputStream stream;
			
			public InputStream getInputStream(String path) {
				
				stream = stream == null ? new ByteArrayInputStream(TestUtils.getFacesConfig(CORRECT).getBytes())
						: new ByteArrayInputStream(TestUtils.getFacesConfig(NON_EXISTING_ACTION_LISTENER).getBytes());
				
				return stream;
			}
			
		};
		
		try {
			
			new AbstractFacesConfigTestCase(paths, streamProvider) {}.testClassDefinitions();
			
			throw new RuntimeException("should have failed");
			
		}catch(AssertionFailedError e) { }
		
	}
	
}
