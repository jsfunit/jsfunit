package org.jboss.jsfunit.analysis.util;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.jboss.jsfunit.analysis.util");
		//$JUnit-BEGIN$
		suite.addTestSuite(FileUtilsTest.class);
        suite.addTestSuite(ResourceUtilsTest.class);
        suite.addTestSuite(ConfigUtilsTest.class);
        suite.addTestSuite(ParserUtilsTest.class);
		//$JUnit-END$
		return suite;
	}

}
