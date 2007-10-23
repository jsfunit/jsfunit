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

import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;

/**
 * @author Dennis Byrne
 */

public class ViewConfigReconciler {

	private Map<String, List<String>> actionListeners ;
	private Map<String, List<String>> actions ;
	protected Map<String, Document> configByPath;
	
	ViewConfigReconciler(Map<String, List<String>> actionListeners, 
						 Map<String, List<String>> actions,
						 Map<String, Document> configByPath){
		this.actionListeners = actionListeners;
		this.actions = actions;
		this.configByPath = configByPath;
	}
	
	void reconcile() {
		
		throw new UnsupportedOperationException("in progress");
		
	}
	
}
