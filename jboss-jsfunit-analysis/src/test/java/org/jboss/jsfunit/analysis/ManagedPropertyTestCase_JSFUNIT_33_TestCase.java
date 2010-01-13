package org.jboss.jsfunit.analysis;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.jboss.jsfunit.analysis.model.ManagedBean;
import org.w3c.dom.Node;

/**
 * A TestCase related to Jira-issue JSFUNIT_26.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
public class ManagedPropertyTestCase_JSFUNIT_33_TestCase extends TestCase
{
   public void testExistingProperty()
   {
      String property = Utilities.getManagedProperty("existing", "value");
      Node managedPropertyNode = createManagedPropertyNode(property, "existing");

      ManagedPropertyTestCase testCase = new ManagedPropertyTestCase("ManagedPropertyTestCase_JSFUNIT_34_TestCase",
            (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "bean", ManagedBean.class.getName(), "existing",
            managedPropertyNode);
      testCase.testPropertyAccessors();
   }

   public void testExistingBooleanProperty()
   {
      String property = Utilities.getManagedProperty("existingBoolean", "value");
      Node managedPropertyNode = createManagedPropertyNode(property, "existingBoolean");

      ManagedPropertyTestCase testCase = new ManagedPropertyTestCase("ManagedPropertyTestCase_JSFUNIT_34_TestCase",
            (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "bean", ManagedBean.class.getName(),
            "existingBoolean", managedPropertyNode);
      testCase.testPropertyAccessors();
   }

   public void testMissingProperty()
   {

      String property = Utilities.getManagedProperty("notThere", "value");
      Node managedPropertyNode = createManagedPropertyNode(property, "notThere");

      ManagedPropertyTestCase testCase = new ManagedPropertyTestCase("ManagedPropertyTestCase_JSFUNIT_34_TestCase",
            (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "bean", ManagedBean.class.getName(), "notThere",
            managedPropertyNode);
      try
      {
         testCase.testPropertyAccessors();
         throw new RuntimeException("should have failed");

      }
      catch (AssertionFailedError e)
      {
         String msg = "The managed bean 'bean' has a managed property called 'notThere', but org.jboss.jsfunit.analysis.model.ManagedBean has no method setNotThere(?)";
         assertEquals(msg, e.getMessage());
      }

   }

   public void testMissingPropertySetter()
   {

      String property = Utilities.getManagedProperty("noSetter", "value");
      Node managedPropertyNode = createManagedPropertyNode(property, "noSetter");

      ManagedPropertyTestCase testCase = new ManagedPropertyTestCase("ManagedPropertyTestCase_JSFUNIT_34_TestCase",
            (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "bean", ManagedBean.class.getName(), "noSetter",
            managedPropertyNode);
      try
      {
         testCase.testPropertyAccessors();
         throw new RuntimeException("should have failed");

      }
      catch (AssertionFailedError e)
      {
         String msg = "The managed bean 'bean' has a managed property called 'noSetter', but org.jboss.jsfunit.analysis.model.ManagedBean has no method setNoSetter(?)";
         assertEquals(msg, e.getMessage());
      }

   }

   public void testMissingPropertyGetter()
   {

      String property = Utilities.getManagedProperty("noGetter", "value");
      Node managedPropertyNode = createManagedPropertyNode(property, "noGetter");

      ManagedPropertyTestCase testCase = new ManagedPropertyTestCase("ManagedPropertyTestCase_JSFUNIT_34_TestCase",
            (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "bean", ManagedBean.class.getName(), "noGetter",
            managedPropertyNode);
      try
      {
         testCase.testPropertyAccessors();
         throw new RuntimeException("should have failed");

      }
      catch (AssertionFailedError e)
      {
         String msg = "The managed bean 'bean' has a managed property called 'noGetter', but org.jboss.jsfunit.analysis.model.ManagedBean has neither a method getNoGetter() nor a method isNoGetter()";
         assertEquals(msg, e.getMessage());
      }

   }

   private Node createManagedPropertyNode(String property, String propertyName)
   {
      String managedBean = Utilities.getManagedBean("bean", ManagedBean.class, "none", property);
      String facesConfig = Utilities.getFacesConfig(managedBean);
      return Utilities.createManagedPropertyNode(facesConfig, propertyName);
   }
}
