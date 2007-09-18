package org.jboss.jsfunit.config;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

class StringStreamProvider implements StreamProvider {

	private String xml;
	
	public StringStreamProvider(String xml) {
		this.xml = xml;
	}
	
	public InputStream getInputStream(String path) {
		return new ByteArrayInputStream(xml.getBytes());
	}

}
