/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.jsfunit.analysis.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

import junit.framework.TestCase;

/**
 * A ResourceUtilsTest.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ResourceUtilsTest extends TestCase
{
   private final static String RESOURCE_NAME = "org/jboss/jsfunit/analysis/util/testdata/resourceutils.txt";
   private final static int RESOURCE_SIZE = 33;
   private final static String RESOURCE_CONTENT = "This is test data for unit tests.";

   protected void setUp() throws Exception
   {
      super.setUp();
   }

   protected void tearDown() throws Exception
   {
      super.tearDown();
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.ResourceUtils#getClassPathResourcesAsStreams(java.lang.String)}.
    */
   public void testGetClassPathResourcesAsStreams()
   {
      List <InputStream> resources = ResourceUtils.getClassPathResourcesAsStreams(RESOURCE_NAME);
      assertEquals(1, resources.size());
      InputStream resource = resources.get(0);
      try
      {
         assertEquals(RESOURCE_SIZE,resource.available());
      }
      catch (IOException e)
      {
         fail(e.getMessage());
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.ResourceUtils#getAsString(java.io.InputStream, java.lang.String)}.
    */
   public void testGetAsString()
   {
      String content = new ResourceUtils().getAsString(ResourceUtils.getClassPathResourcesAsStreams(RESOURCE_NAME).get(0), RESOURCE_NAME);
      assertEquals(RESOURCE_CONTENT,content);
   }

}
