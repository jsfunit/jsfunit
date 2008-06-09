package org.jboss.jsfunit.analysis;

import junit.framework.TestCase;

public class ManagedBeanDuplicateKeys_JSFUNIT_34_TestCase extends TestCase {

	@Override
	protected void setUp() throws Exception {
		String property = TestUtils.getManagedProperty("setter", "value");
		String duplicate = TestUtils.getManagedProperty("setter", "value");
			
	}
	
	
}
