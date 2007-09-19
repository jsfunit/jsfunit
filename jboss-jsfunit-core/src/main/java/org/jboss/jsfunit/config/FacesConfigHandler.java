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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class FacesConfigHandler extends DefaultHandler {
	
	private Set<String> classElementNames;
	private Set<String> valueElementNames;
	
	private boolean clazz;
	private boolean value;
	private StringBuffer buffer;

	private Map<String, List<String>> classNamesByElement = new HashMap<String, List<String>>();
	private Map<String, List<String>> valuesByElement = new HashMap<String, List<String>>();

	public FacesConfigHandler(Set<String> classElementNames, Set<String> valueElementNames) {
		this.classElementNames = classElementNames;
		this.valueElementNames = valueElementNames;
	}
	
	public Map<String, List<String>> getClassNamesByElement() {
		return classNamesByElement;
	}

	public Map<String, List<String>> getValuesByElement() {
		return valuesByElement;
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (clazz || value)
			buffer.append(new String(ch, start, length).trim());
	}

	public void startElement(String ns, String local, String qName,
			Attributes atts) throws SAXException {

		if ( (clazz = classElementNames.contains(qName)) | (value = valueElementNames.contains(qName)) )
			buffer = new StringBuffer();
	}

	public void endElement(String ns, String local, String qName)
			throws SAXException {

		if(clazz && value)
			throw new IllegalStateException("cannot be looking for a element class and an element value");
		
		if (clazz) {
			List<String> classNames = this.classNamesByElement.get(qName);
			if( classNames == null )
				classNames = new LinkedList<String>();
			classNames.add(buffer.toString());
			this.classNamesByElement.put(qName, classNames);
		}else if(value) {
			List<String> valueNames = this.valuesByElement.get(qName);
			if(valueNames == null)
				valueNames = new LinkedList<String>();
			valueNames.add(buffer.toString());
			this.valuesByElement.put(qName, valueNames);
		}
		
		clazz = value = false;
	}

}