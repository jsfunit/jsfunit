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

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

public class AbstractFacesConfigTestCaseTestCase extends TestCase{
	
	private static final Set<String> STUBBED_RESOURCEPATH = new HashSet<String>() {{
		add("stubbed resource path");
	}};
	
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

		new AbstractFacesConfigTestCase(STUBBED_RESOURCEPATH, new StringStreamProvider(getXml(""))) {};
		
	}

	public void testMalFormed() {

		try {
			
			new AbstractFacesConfigTestCase(STUBBED_RESOURCEPATH, new StringStreamProvider(getXml("<"))) {};
			
			fail("malformed xml should fail");
			
		}catch(Exception e) { }
		
	}

	public void testFacesConfigHappyPath() {
		
		String body = "<validator>" 
					+ "<validator-id>javax.faces.DoubleRange</validator-id>"
					+ "<validator-class>" + AtomicIntegerValidator.class.getName() + "</validator-class>" 
					+ "</validator>";
		new AbstractFacesConfigTestCase(STUBBED_RESOURCEPATH, 
				new StringStreamProvider(getXml(body))) {};		
	}
	
	public void testFacesConfigElementsMissingInterface() {
		
		String body = "<application><action-listener>" + Pojo.class.getName() + "</action-listener></application>";
		
		try {
			
			new AbstractFacesConfigTestCase(STUBBED_RESOURCEPATH, 
					new StringStreamProvider(getXml(body))) {};
			
			fail("should have failed ");
			
		}catch(Exception e) { }
		
	}
	
	public void testFacesConfigElementsNonExistingClass() {
		
		String body = "<application><action-listener>com.nonexist.Foo</action-listener></application>";
		
		try {
			
			new AbstractFacesConfigTestCase(STUBBED_RESOURCEPATH, 
					new StringStreamProvider(getXml(body))) {};		
			
			fail("should have failed ");
			
		}catch(Exception e) { }
		
	}
	
	private String getXml(String body) {
		
		return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?><!DOCTYPE faces-config PUBLIC "
			+ "\"-//Sun Microsystems, Inc.//DTD JavaServer Faces Config 1.1//EN\""
			+ "\"http://java.sun.com/dtd/web-facesconfig_1_1.dtd\">"
			+ "<faces-config>"
			+ body
			+ "</faces-config>";
		
	}
}
