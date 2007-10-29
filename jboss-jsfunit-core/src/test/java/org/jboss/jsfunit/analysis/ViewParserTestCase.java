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

import static org.jboss.jsfunit.analysis.util.ParserUtils.getDocument;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

/**
 * @author Dennis Byrne
 */

public class ViewParserTestCase extends TestCase {

	private ViewParser viewParser;
	private static final String EL = "#{bean.foo}";
	
	@Override
	protected void setUp() throws Exception {
		
		viewParser = new ViewParser();
		
	}

	public void testAction() throws Exception{
		
		viewParser.parse(getDocument("<root><actionListener action='" + EL + "'/></root>"), "foo");
		Map<String, List<String>> actions = viewParser.getActions();
		assertEquals(1, actions.size());
		assertEquals(EL, actions.get("foo").get(0));
		
	}
	
	public void testActionListenerTag() throws Exception{
		
		viewParser.parse(getDocument("<root><actionListener binding='" + EL + "'/></root>"), "foo");
		Map<String, List<String>> actionListeners = viewParser.getActionListeners();
		assertEquals(1, actionListeners.size());
		assertEquals(EL, actionListeners.get("foo").get(0));
		
	}
	
	public void testActionListenerAttribute() throws Exception{
		
		viewParser.parse(getDocument("<root actionListener='" + EL + "' />"), "foo");
		Map<String, List<String>> actionListeners = viewParser.getActionListeners();
		assertEquals(1, actionListeners.size());
		assertEquals(EL, actionListeners.get("foo").get(0));
		
	}
	
	public void testTwoDocuments() throws Exception{
		
		viewParser.parse(getDocument("<root actionListener='" + EL + "' />"), "foo");
		viewParser.parse(getDocument("<root actionListener='" + EL + "' />"), "foo2");
		Map<String, List<String>> actionListeners = viewParser.getActionListeners();
		assertEquals(2, actionListeners.size());
		
	}
	
	public void testNothing() throws Exception {
		
		viewParser.parse(getDocument("<root a='foo'/>"), "foo");
		assertEquals(0, viewParser.getActionListeners().size()); 
		assertEquals(0, viewParser.getActions().size());
		
	}

}