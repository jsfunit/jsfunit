package org.jboss.jsfunit.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class ResourceUtils implements StreamProvider{

	public InputStream getInputStream(String resourceName) {

		InputStream stream = getClass().getClassLoader().getResourceAsStream(resourceName);

		if (stream == null)
			stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);

		return stream;
	}
	
	public String getAsString(InputStream stream, String resourceName) {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		StringBuffer buffer = new StringBuffer();
		String temp = null;
		
		try {
			
			while ((temp = reader.readLine()) != null)
				buffer.append(temp);
			
		} catch (IOException e) {
			throw new RuntimeException("Could not read file " + resourceName, e);
		}
		
		return buffer.toString();
	}
	
}
