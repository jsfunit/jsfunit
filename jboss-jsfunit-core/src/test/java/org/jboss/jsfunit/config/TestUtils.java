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

public class TestUtils {

	public static final Set<String> STUBBED_RESOURCEPATH = new HashSet<String>() {{
		add("stubbed resource path");
	}};
	
	private TestUtils() {}
	
	public static final String getFacesConfig(String body) {
		
		return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?><!DOCTYPE faces-config PUBLIC "
			+ "\"-//Sun Microsystems, Inc.//DTD JavaServer Faces Config 1.1//EN\""
			+ "\"http://java.sun.com/dtd/web-facesconfig_1_1.dtd\">"
			+ "<faces-config>"
			+ body
			+ "</faces-config>";
	}
	
}
