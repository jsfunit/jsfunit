package org.jboss.jsfunit.config;

import java.io.InputStream;

interface StreamProvider {

	public InputStream getInputStream(String path);
	
}
