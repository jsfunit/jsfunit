package org.jboss.jsfunit.config;

class ClassUtils {

	public Class loadClass(String clazzName, String elementName) {

		try {

			return getClass().getClassLoader().loadClass(clazzName.trim()); 

		} catch (ClassNotFoundException e) {

			try {

				return Thread.currentThread().getContextClassLoader().loadClass(clazzName.trim());

			}catch(ClassNotFoundException e2) {

				throw new RuntimeException("could not load class " + clazzName + " for element " + elementName);

			}
			
		}

	}
}