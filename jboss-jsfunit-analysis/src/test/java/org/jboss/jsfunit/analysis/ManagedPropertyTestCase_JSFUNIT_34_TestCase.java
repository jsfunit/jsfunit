package org.jboss.jsfunit.analysis;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.w3c.dom.Node;

/**
 * A TestCase related to Jira-issue JSFUNIT_26.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
public class ManagedPropertyTestCase_JSFUNIT_34_TestCase extends TestCase
{

   public void testDuplicateKeys() throws Exception
   {
      String property = "<managed-property>" + " <property-name>map</property-name>" + " <map-entries>"
            + "  <map-entry>" + "   	<key>duplicate</key>" + "   	<value>3</value>" + "  </map-entry>"
            + "  <map-entry>" + "  	<key>duplicate</key>" + "  	<value>4</value>" + "  </map-entry>"
            + " </map-entries>" + "</managed-property>";
      Node managedPropertyNode = createManagedPropertyNode(property, "map");

      ManagedPropertyTestCase testCase = new ManagedPropertyTestCase("ManagedPropertyTestCase_JSFUNIT_34_TestCase",
            (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0], "bean", "bean", "map", managedPropertyNode);
      try
      {
         testCase.testMapDuplicateKeys();
         throw new RuntimeException("should have failed");
      }
      catch (AssertionFailedError e)
      {
         String msg = "Managed Bean 'bean' has a managed Map property with a duplicate key 'duplicate'";
         assertEquals(msg, e.getMessage());
      }
   }

   public void testNonDuplicateKeys() throws Exception
   {
      String property = "<managed-property>" + " <property-name>map</property-name>" + " <map-entries>"
            + "  <map-entry>" + "     <key>duplicate</key>" + "     <value>3</value>" + "  </map-entry>"
            + "  <map-entry>" + "     <key>NotADuplicate</key>" + "     <value>4</value>" + "  </map-entry>"
            + " </map-entries>" + "</managed-property>";

      Node managedPropertyNode = createManagedPropertyNode(property, "map");

      ManagedPropertyTestCase testCase = new ManagedPropertyTestCase("ManagedPropertyTestCase_JSFUNIT_34_TestCase",
            (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0], "bean", "bean", "map", managedPropertyNode);
      try
      {
         testCase.testMapDuplicateKeys();

      }
      catch (AssertionFailedError e)
      {
         throw new RuntimeException("should not have failed");
      }
   }

   private Node createManagedPropertyNode(String property, String propertyName)
   {
      String managedBean = TestUtils.getManagedBean("bean", ManagedBeanWithMap.class, "none", property);
      String facesConfig = TestUtils.getFacesConfig(managedBean);
      return TestUtils.createManagedPropertyNode(facesConfig, propertyName);
   }
}
