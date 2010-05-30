/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.jsfunit.analysis.util;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;

import junit.framework.TestCase;

import org.jboss.jsfunit.analysis.StreamProvider;
import org.jboss.jsfunit.analysis.StringStreamProvider;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

/**
 * A ParserUtilsTest.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ParserUtilsTest extends TestCase
{
   class BadStringStreamProvider extends StringStreamProvider 
   {
      private String path;
      public BadStringStreamProvider(String xml, String path) 
      {
         super(xml);
         this.path = path;
      }
      public InputStream getInputStream(String path) {
         if (path != null && path.equals(this.path))
         {
           return super.getInputStream(path);
         }
         else
         {
            return null;
         }
     }

   }

   protected void setUp() throws Exception
   {
      super.setUp();
   }

   protected void tearDown() throws Exception
   {
      super.tearDown();
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.ParserUtils#getDocumentBuilder()}.
    */
   public void testGetDocumentBuilder()
   {
      Object tempObj = ParserUtils.getDocumentBuilder();
      assertNotNull(tempObj);
      assertTrue(DocumentBuilder.class.isAssignableFrom(tempObj.getClass()));
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.ParserUtils#getXml(java.lang.String, org.jboss.jsfunit.analysis.StreamProvider)}.
    */
   public void testGetXmlFileFound()
   {
      assertNotNull(ParserUtils.getXml("testPath", new BadStringStreamProvider("test", "testPath")));
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.ParserUtils#getXml(java.lang.String, org.jboss.jsfunit.analysis.StreamProvider)}.
    */
   public void testGetXmlFileNotFound()
   {
      try
      {
         ParserUtils.getXml("badPath", new BadStringStreamProvider("test", "testPath"));
         fail("RuntimeException expected");
      } catch (RuntimeException re) {
         assertEquals("Could not locate file 'badPath'", re.getMessage());
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.ParserUtils#getDocument(java.lang.String)}.
    */
   public void testGetDocumentNullXml()
   {
      try
      {
         Document testDoc = ParserUtils.getDocument(null);
         assertNotNull(testDoc);
      } 
      catch (IllegalArgumentException iae)
      {
         assertEquals("input must not be null", iae.getMessage());
      }
      catch (Throwable t) 
      {
         fail("wrong exception " + t.getClass().getName());
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.ParserUtils#getDocument(java.lang.String)}.
    */
   public void testGetDocumentBadXml()
   {
      try
      {
         Document testDoc = ParserUtils.getDocument("bad xml");
         assertNotNull(testDoc);
      } 
      catch (SAXParseException spe)
      {
         assertEquals("Content is not allowed in prolog.", spe.getMessage());
      }
      catch (Throwable t) 
      {
         fail("wrong exception " + t.getClass().getName());
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.ParserUtils#getDocument(java.lang.String)}.
    */
   public void testGetDocument()
   {
      try
      {
         Document testDoc = ParserUtils.getDocument("<root></root>");
         assertNotNull(testDoc);
      } 
      catch (Throwable t) 
      {
         fail("should be ok...");
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.ParserUtils#query(org.w3c.dom.Node, java.lang.String, java.lang.String)}.
    */
   public void testQueryNull()
   {
      try
      {
         ParserUtils.query(null, null, null);
      }
      catch (NullPointerException npe)
      {
         assertEquals("document was null null", npe.getMessage());
      }
      try
      {
         Document testDoc = ParserUtils.getDocument("<?xml version=\"1.0\"?><a id=\"test\" />");
         ParserUtils.query(testDoc, null, "test");
      } 
      catch (Throwable t) 
      {
         assertEquals("Could not run XPath query 'null' on document test", t.getMessage());
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.ParserUtils#query(org.w3c.dom.Node, java.lang.String, java.lang.String)}.
    */
   public void testQuery()
   {
      try
      {
         Document testDoc = ParserUtils.getDocument("<?xml version=\"1.0\"?><root><a id=\"test\"></a></root>");
         NodeList results = ParserUtils.query(testDoc, "//a", "test");
         assertNotNull(results);
         assertEquals(1,results.getLength());
      } 
      catch (Throwable t) 
      {
         t.printStackTrace();
         fail("supposed to work");
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.ParserUtils#querySingle(org.w3c.dom.Node, java.lang.String, java.lang.String)}.
    */
   public void testQuerySingle()
   {
      try
      {
         Document testDoc = ParserUtils.getDocument("<?xml version=\"1.0\"?><root><a id=\"test\">content</a></root>");
         String result = ParserUtils.querySingle(testDoc, "//a", "test");
         assertNotNull(result);
         assertEquals("content",result);
      } 
      catch (Throwable t) 
      {
         t.printStackTrace();
         fail("supposed to work");
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.ParserUtils#querySingle(org.w3c.dom.Node, java.lang.String, java.lang.String)}.
    */
   public void testQuerySingleNotFound()
   {
      try
      {
         Document testDoc = ParserUtils.getDocument("<?xml version=\"1.0\"?><root><b id=\"test\"></b></root>");
         String result = ParserUtils.querySingle(testDoc, "//a", "test");
         assertNull(result);
      } 
      catch (Throwable t) 
      {
         t.printStackTrace();
         fail("supposed to work");
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.ParserUtils#querySingle(org.w3c.dom.Node, java.lang.String, java.lang.String)}.
    */
   public void testQuerySingleDuplicate()
   {
      try
      {
         Document testDoc = ParserUtils.getDocument("<?xml version=\"1.0\"?><root><a id=\"test\"></a><a id=\"test2\"></a></root>");
         ParserUtils.querySingle(testDoc, "//a", "test");
         fail("expected to crash");
      } 
      catch (Throwable t) 
      {
         assertEquals("query //a returned 2 results. Should have been one.", t.getMessage());
         assertTrue(t instanceof RuntimeException);
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.ParserUtils#getDomDocument(java.lang.String, org.jboss.jsfunit.analysis.StreamProvider)}.
    */
   public void testGetDomDocumentStringStreamProvider()
   {
      StreamProvider streamProvider = new StringStreamProvider("<?xml version=\"1.0\"?><root><a id=\"test\"></a><a id=\"test2\"></a></root>");
      Document result = ParserUtils.getDomDocument("test",streamProvider);
      assertNotNull(result);
      assertTrue(Document.class.isAssignableFrom(result.getClass()));
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.ParserUtils#getDomDocument(java.io.InputStream, java.lang.String)}.
    */
   public void testGetDomDocumentInputStreamStringNull()
   {
      try 
      {
         ParserUtils.getDomDocument((InputStream)null, "test");
      }
      catch (Throwable t)
      {
         assertEquals("Could not parse document 'test'", t.getMessage());
         assertTrue(t instanceof RuntimeException);
      }
   }

}
