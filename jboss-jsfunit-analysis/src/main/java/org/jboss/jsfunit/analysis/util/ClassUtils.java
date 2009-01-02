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

package org.jboss.jsfunit.analysis.util;

import static junit.framework.Assert.fail;
/**
 * @author Dennis Byrne
 * @since 1.0
 */

public class ClassUtils {
	
	public boolean isAssignableFrom(Class[] constraints, Class clazz) {
		
		for(Class constraint : constraints) 
			if(constraint.isAssignableFrom(clazz)) 
				return true;
		
		return false;
	}
	
	public Class loadClass(String clazzName, String elementName) {

		if(clazzName == null)
			throw new RuntimeException("No class for element '" + elementName + "'");
		
		try {

			return getClass().getClassLoader().loadClass(clazzName.trim()); 

		} catch (ClassNotFoundException e) {

			try {

				return Thread.currentThread().getContextClassLoader().loadClass(clazzName.trim());

			}catch(ClassNotFoundException e2) {

				fail("Could not load class '" + clazzName + "' for element '" + elementName + "'");
				return null; // this line is unreachable but the compiler does not know this
			}
			
		}

	}
	
	public String getConstraintsList(Class[] constraints) {
		
		String msg = "";
		
		for(int c = 0; c < constraints.length ; c++) 
			msg += constraints[c].getName() + ( (c == constraints.length - 1 ? "" : " or ") );
		
		return msg;
	}
}