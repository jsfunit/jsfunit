package org.jboss.jsfunit.analysis.el;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestSuite;

import org.apache.commons.digester.Digester;
import org.apache.commons.io.filefilter.IOFileFilter;

/**
 * 
 * @author Jason
 * @since 1.0
 */
public class ELTestSuiteBuilder
{
	public static TestSuite getTestSuite(final File baseDir,
										final IOFileFilter fileFilter,
										final Collection<SkipExpressionSpec> toSkip,
										final Map<String, Class<?>> beanMap)
	{
		return getTestSuite(ELIterFactory.getIterable(baseDir, fileFilter, toSkip), beanMap);
	}

	public static TestSuite getTestSuite(final File baseDir,
										final IOFileFilter fileFilter,
										final IOFileFilter dirFilter,
										final Collection<SkipExpressionSpec> toSkip,
										final Map<String, Class<?>> beanMap)
	{
		return getTestSuite(ELIterFactory.getIterable(baseDir, fileFilter, dirFilter, toSkip), beanMap);
	}

	public static TestSuite getTestSuite(final Iterable<ELBundle> elIter, final Map<String, Class<?>> beanMap)
	{
		final TestSuite out = new TestSuite();
		for(final ELBundle bundle: elIter) {
			out.addTest(new ELTestCase(bundle, beanMap));
		}
		return out;
	}

	//TODO: these should probably live somewhere else
	public static Map<String, Class<?>> getBeanMap(final List<File> beanConfigs)
	{
		final Map<String, Class<?>> out = new HashMap<String, Class<?>>();
		for(final File config: beanConfigs) {
			out.putAll(getBeanMap(config));
		}
		return out;
	}

	public static Map<String, Class<?>> getBeanMap(final File config)
	{
		final Map<String, String> tmp = new HashMap<String, String>();
		final Digester configdigester = new Digester();
		configdigester.push(tmp);

		final String base = "faces-config/managed-bean";
		configdigester.addCallMethod(base, "put", 2);
		configdigester.addCallParam(base + "/managed-bean-name", 0);
		configdigester.addCallParam(base + "/managed-bean-class", 1);

		try {
			configdigester.parse(config.toURL());
		}
		catch(final Exception e) {
			throw new RuntimeException(e);
		}

		final Map<String, Class<?>> out = new HashMap<String, Class<?>>();
		for(final Map.Entry<String, String> e: tmp.entrySet()) {
			final Class<?> clazz;
			try {
				 clazz = Class.forName(e.getValue());
			}
			catch(final ClassNotFoundException cnfe) {
				//TODO: log or warn somehow?
				continue;
			}

			out.put(e.getKey(), clazz);
		}
		return out;
	}
}
