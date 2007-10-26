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

import static junit.framework.Assert.fail;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * @author Dennis Byrne
 */

public class ViewConfigReconciler {

	private Map<String, List<String>> actionListeners ;
	private Map<String, List<String>> actions ;
	private Map<String, Document> configByPath;
	
	ViewConfigReconciler(Map<String, List<String>> actionListeners, 
						 Map<String, List<String>> actions,
						 Map<String, Document> configByPath){
		this.actionListeners = actionListeners;
		this.actions = actions;
		this.configByPath = configByPath;
	}
	
	void reconcile() {
		
		reconcileEL((Iterator<String>) actions.keySet().iterator(), actions);
		reconcileEL((Iterator<String>) actionListeners.keySet().iterator(), actionListeners);
		
	}

	private void reconcileEL(Iterator<String> paths, Map<String, List<String>> elByPath) {
		
		for( ; paths.hasNext() ; ) {
			String path = paths.next();
			reconcileEl(path, elByPath.get(path));
		}
		
	}

	private void reconcileEl(String path, List<String> els) {
		for( String el : els ) {
			if(isEl(el)) {
				String unwrapped = el.substring(2, el.length() - 1);
				String beanName = unwrapped.substring(0, unwrapped.indexOf('.'));
				Iterator<String> configPaths = configByPath.keySet().iterator();
				for( ; configPaths.hasNext() ; )
					verifyBeanExists(path, beanName, configByPath.get(configPaths.next()), el);
			}
		}
	}

	private void verifyBeanExists(String path, String bean, Document document, String el) {

		if(document == null)
			throw new NullPointerException("document was null");
		
		NodeList list = document.getElementsByTagName("managed-bean-name");
		
		boolean found = false;
		for(int c = 0; c < list.getLength(); c++) {
			if( bean.equals(list.item(c).getTextContent())) {
				found = true; break;
			}
		}
		
		if(! found)
			fail(path + " has an EL expression '" + el + "' which references a "
					+ " managed bean '" + bean + "', but no managed bean by this name can be found.");
	}
	
	private boolean isEl(String questionable) {
		return (questionable != null 
				&& questionable.length() > 3 
				&& questionable.charAt(0) == '#'
				&& questionable.charAt(1) == '{'
				&& questionable.charAt(questionable.length() - 1) == '}');
	}
}
