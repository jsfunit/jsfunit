package org.jboss.jsfunit.analysis;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

public class ManagedBeanDuplicateKeys_JSFUNIT_34_TestCase extends TestCase {

	public void testDuplicateKeys() throws Exception {
		String property = "<managed-property>" +
		  " <property-name>map</property-name>" +
		  " <map-entries>" +
		  "  <map-entry>" +
		  "   	<key>duplicate</key>" +
		  "   	<value>3</value>" +
		  "  </map-entry>" +
	      "  <map-entry>" +
	      "  	<key>duplicate</key>" +
	      "  	<value>4</value>" +
	      "  </map-entry>" +
	      " </map-entries>" +
	      "</managed-property>";

		String managedBean = TestUtils.getManagedBean("bean", ManagedBeanWithMap.class, "none", property);
		String facesConfig = TestUtils.getFacesConfig(managedBean);
		StreamProvider streamProvider = new StringStreamProvider(facesConfig);
		
		try {
			
			new AbstractFacesConfigTestCase(TestUtils.STUBBED_RESOURCEPATH, streamProvider) {}.testManagedBeans();
			
			throw new RuntimeException("should have failed");
			
		}catch(AssertionFailedError e) { 
			
			String msg = "Managed Bean 'bean' has a managed Map property w/ a duplicate key 'duplicate'";
			assertEquals(msg, e.getMessage());
			
		}
		
	}
	
}
