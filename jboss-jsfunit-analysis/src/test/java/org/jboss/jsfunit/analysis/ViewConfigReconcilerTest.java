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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.jboss.jsfunit.analysis.model.ManagedBean;
import org.jboss.jsfunit.analysis.util.ParserUtils;
import org.w3c.dom.Document;

/**
 * @author Dennis Byrne
 */

public class ViewConfigReconcilerTest extends TestCase {

	private Map<String, Document> configByPath;
	private Map<String, List<String>> goodActionListeners;
	private Map<String, List<String>> goodActions;
	
	protected void setUp() throws Exception {
		
		String manageBean = TestUtils.getManagedBean("bean", ManagedBean.class, "none");
		String facesConfig = TestUtils.getFacesConfig(manageBean);
		final Document facesConfigDocument = ParserUtils.getDocument(facesConfig);
		configByPath = new HashMap<String, Document>(){{
			put("path2config", facesConfigDocument);
		}};
		
		goodActionListeners = new HashMap<String, List<String>>(){{
			put("path2view", new ArrayList<String>() {{
				add("#{bean.beanActionListener}");
			}});
		}};
		
		goodActions = new HashMap<String, List<String>>(){{
			put("path2view", new ArrayList<String>() {{
				add("#{bean.beanAction}");
			}});
		}};
	}

	public void testNoProblem() throws Exception{
		
		new ViewConfigReconciler(goodActionListeners, goodActions, configByPath).reconcileActions();
		new ViewConfigReconciler(goodActionListeners, goodActions, configByPath).reconcileActionListeners();
		
		new ViewConfigReconciler(goodActionListeners, new HashMap<String, List<String>>(), configByPath).reconcileActions();
		new ViewConfigReconciler(goodActionListeners, new HashMap<String, List<String>>(), configByPath).reconcileActionListeners();
		
		new ViewConfigReconciler(new HashMap<String, List<String>>(), goodActions, configByPath).reconcileActions();
		new ViewConfigReconciler(new HashMap<String, List<String>>(), goodActions, configByPath).reconcileActionListeners();
	}
	
	public void testMissingBeanAction() {
		
		Map<String, List<String>> actions = new HashMap<String, List<String>>(){{
			put("path2view", new ArrayList<String>() {{
				add("#{badbean.action}");
			}});
		}};
		
		try {
			
			new ViewConfigReconciler(goodActionListeners, actions, configByPath).reconcileActions();
			
			throw new RuntimeException();
			
		}catch(AssertionFailedError e) { }
		
	}
	
	public void testMissingAction() {

		Map<String, List<String>> actions = new HashMap<String, List<String>>(){{
			put("path2view", new ArrayList<String>() {{
				add("#{bean.badaction}");
			}});
		}};
		
		try {
			
			new ViewConfigReconciler(goodActionListeners, actions, configByPath).reconcileActions();
			
			throw new RuntimeException();
			
		}catch(AssertionFailedError e) { }		
		
	}
	
	public void testMissingBeanActionListener() {
		
		Map<String, List<String>> actionListeners = new HashMap<String, List<String>>(){{
			put("path2view", new ArrayList<String>() {{
				add("#{badbean.actionListener}");
			}});
		}};
		
		try {
			
			new ViewConfigReconciler(actionListeners, goodActions, configByPath).reconcileActionListeners();
			
			throw new RuntimeException();
			
		}catch(AssertionFailedError e) { }
		
	}
	
	public void testMissingActionListener() {
		
		Map<String, List<String>> actionListeners = new HashMap<String, List<String>>(){{
			put("path2view", new ArrayList<String>() {{
				add("#{bean.badactionListener}");
			}});
		}};
		
		try {
			
			new ViewConfigReconciler(actionListeners, goodActions, configByPath).reconcileActionListeners();
			
			throw new RuntimeException();
			
		}catch(AssertionFailedError e) { }
		
	}
}
