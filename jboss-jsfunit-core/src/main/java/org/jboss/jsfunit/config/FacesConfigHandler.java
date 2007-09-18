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
	private Map<String, List<String>> classNames = new HashMap<String, List<String>>();
	private Map<String, List<String>> valueNames = new HashMap<String, List<String>>();
	private boolean clazz;
	private boolean value;
	private StringBuffer buffer;

	public FacesConfigHandler(Set<String> classElementNames, Set<String> valueElementNames) {
		this.classElementNames = classElementNames;
		this.valueElementNames = valueElementNames;
	}
	
	public Map<String, List<String>> getClasses() {
		return classNames;
	}

	public Map<String, List<String>> getValues() {
		return valueNames;
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
			List<String> classNames = this.classNames.get(qName);
			if( classNames == null )
				classNames = new LinkedList<String>();
			classNames.add(buffer.toString());
			this.classNames.put(qName, classNames);
		}else if(value) {
			List<String> valueNames = this.valueNames.get(qName);
			if(valueNames == null)
				valueNames = new LinkedList<String>();
			valueNames.add(buffer.toString());
			this.valueNames.put(qName, valueNames);
		}
		
		clazz = value = false;
	}

}