/**
 * 
 */
package org.jboss.jsfunit.analysis.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author ajesse
 *
 */
public class FileUtilsTest extends TestCase
{

   File tempFolder = null;

   List<File> tempFiles = null;

   String[] testFiles0 = null;

   String[] testFiles1 = null;

   String[] testFiles2 = null;

   String[] testFiles3 = null;

   String[] testFiles4 = null;

   String[] testFolderName = null;

   File[] testFolder = null;

   static final List<String> EXTENSIONS_XHML_INC_JSPX = new ArrayList<String>();

   static final List<String> EXTENSIONS_JSPX = new ArrayList<String>();

   static final List<String> EXTENSIONS_XHTML = new ArrayList<String>();

   {
      EXTENSIONS_XHML_INC_JSPX.add("xhtml");
      EXTENSIONS_XHML_INC_JSPX.add("jspx");
      EXTENSIONS_XHML_INC_JSPX.add("inc");
      EXTENSIONS_JSPX.add("jspx");
      EXTENSIONS_XHTML.add("xhtml");
   }

   public class BadFile extends File
   {
      /**
       * 
       */
      private static final long serialVersionUID = 1L;

      /**
       * 
       * @param pathname
       */
      public BadFile(String pathname)
      {
         super(pathname);
      }

      /**
       * 
       */
      @Override
      public String getCanonicalPath() throws IOException
      {
         throw new IOException("bad_one");
      }

   }

   /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception
   {
      super.setUp();
      if (tempFolder == null)
      {
         String tmpDir = System.getProperty("java.io.tmpdir");
         tempFolder = new File(tmpDir);
      }
      if (tempFiles == null)
      {
         tempFiles = new ArrayList<File>();
         testFolder = new File[3];
         testFolderName = new String[3];
         testFolder[0] = new File(tempFolder, "FileUtilsTest");
         testFolder[0].mkdir();
         testFolder[1] = new File(testFolder[0], "SubFolder1");
         testFolder[1].mkdir();
         testFolder[2] = new File(testFolder[1], "SubFolder2");
         testFolder[2].mkdir();
         testFolder[0].deleteOnExit();
         testFolder[1].deleteOnExit();
         testFolder[2].deleteOnExit();
         tempFiles.add(testFolder[0]);
         tempFiles.add(testFolder[1]);
         tempFiles.add(testFolder[2]);
         testFolderName[0] = testFolder[0].getPath();
         testFolderName[1] = testFolder[1].getPath();
         testFolderName[2] = testFolder[2].getPath();
         testFiles0 = new String[5];
         for (int i = 0; i < 5; i++)
         {
            File tempFile = new File(testFolder[0], "tempFile0_" + i + ".tmp");
            tempFile.deleteOnExit();
            tempFile.createNewFile();
            tempFiles.add(tempFile);
            testFiles0[i] = tempFile.getPath();
         }
         testFiles1 = new String[3];
         for (int i = 0; i < 3; i++)
         {
            File tempFile = new File(testFolder[0], "tempFile1_" + i + ".xhtml");
            tempFile.deleteOnExit();
            tempFile.createNewFile();
            tempFiles.add(tempFile);
            testFiles1[i] = tempFile.getPath();
         }
         testFiles2 = new String[3];
         for (int i = 0; i < 3; i++)
         {
            File tempFile = new File(testFolder[0], "tempFile2_" + i + ".inc");
            tempFile.deleteOnExit();
            tempFile.createNewFile();
            tempFiles.add(tempFile);
            testFiles2[i] = tempFile.getPath();
         }
         testFiles3 = new String[10];
         for (int i = 0; i < 10; i++)
         {
            File tempFile = new File(testFolder[1], "tempFile3_" + i + ".xhtml");
            tempFile.deleteOnExit();
            tempFile.createNewFile();
            tempFiles.add(tempFile);
            testFiles3[i] = tempFile.getPath();
         }
         testFiles4 = new String[3];
         for (int i = 0; i < 3; i++)
         {
            File tempFile = new File(testFolder[2], "tempFile4_" + i + ".xhtml");
            tempFile.deleteOnExit();
            tempFile.createNewFile();
            tempFiles.add(tempFile);
            testFiles4[i] = tempFile.getPath();
         }
      }
   }

   /* (non-Javadoc)
    * @see junit.framework.TestCase#tearDown()
    */
   protected void tearDown() throws Exception
   {
      super.tearDown();
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.FileUtils#findFilesRecursive(java.lang.String, java.util.List)}.
    */
   public void testFindFilesRecursiveString()
   {
      try
      {
         FileUtils.findFilesRecursive((File) null, null);
         fail("should have failed");
      }
      catch (IllegalArgumentException iae)
      {
         assertEquals("Folder must not be null", iae.getMessage());
      }
      List<String> foundFiles = FileUtils.findFilesRecursive(testFolder[0].getAbsolutePath(), null);
      assertEquals(26, foundFiles.size());
      foundFiles = FileUtils.findFilesRecursive(testFolder[0].getAbsolutePath(), EXTENSIONS_XHML_INC_JSPX);
      assertEquals(19, foundFiles.size());
      foundFiles = FileUtils.findFilesRecursive(testFolder[0].getAbsolutePath(), EXTENSIONS_JSPX);
      assertEquals(0, foundFiles.size());
      foundFiles = FileUtils.findFilesRecursive(testFolder[0].getAbsolutePath(), EXTENSIONS_XHTML);
      assertEquals(16, foundFiles.size());
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.FileUtils#findFilesRecursive(java.io.File, java.util.List)}.
    */
   public void testFindFilesRecursiveFile()
   {
      try
      {
         FileUtils.findFilesRecursive((File) null, null);
         fail("should have failed");
      }
      catch (IllegalArgumentException iae)
      {
         assertEquals("Folder must not be null", iae.getMessage());
      }
      List<String> foundFiles = FileUtils.findFilesRecursive(testFolder[0], null);
      assertEquals(26, foundFiles.size());
      foundFiles = FileUtils.findFilesRecursive(testFolder[0], EXTENSIONS_XHML_INC_JSPX);
      assertEquals(19, foundFiles.size());
      foundFiles = FileUtils.findFilesRecursive(testFolder[0], EXTENSIONS_JSPX);
      assertEquals(0, foundFiles.size());
      foundFiles = FileUtils.findFilesRecursive(testFolder[0], EXTENSIONS_XHTML);
      assertEquals(16, foundFiles.size());
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.FileUtils#findFiles(java.lang.String, java.util.List)}.
    */
   public void testFindFilesString()
   {
      try
      {
         FileUtils.findFiles((File) null, null);
         fail("should have failed");
      }
      catch (IllegalArgumentException iae)
      {
         assertEquals("Folder must not be null", iae.getMessage());
      }
      List<String> foundFiles = FileUtils.findFiles(testFolder[0].getAbsolutePath(), null);
      assertEquals(12, foundFiles.size());
      foundFiles = FileUtils.findFiles(testFolder[0].getAbsolutePath(), EXTENSIONS_XHML_INC_JSPX);
      assertEquals(6, foundFiles.size());
      foundFiles = FileUtils.findFiles(testFolder[0].getAbsolutePath(), EXTENSIONS_JSPX);
      assertEquals(0, foundFiles.size());
      foundFiles = FileUtils.findFiles(testFolder[0].getAbsolutePath(), EXTENSIONS_XHTML);
      assertEquals(3, foundFiles.size());
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.FileUtils#findFiles(java.io.File, java.util.List)}.
    */
   public void testFindFilesFile()
   {
      try
      {
         FileUtils.findFiles((File) null, null);
         fail("should have failed");
      }
      catch (IllegalArgumentException iae)
      {
         assertEquals("Folder must not be null", iae.getMessage());
      }
      List<String> foundFiles = FileUtils.findFiles(testFolder[0], null);
      assertEquals(12, foundFiles.size());
      foundFiles = FileUtils.findFiles(testFolder[0], EXTENSIONS_XHML_INC_JSPX);
      assertEquals(6, foundFiles.size());
      foundFiles = FileUtils.findFiles(testFolder[0], EXTENSIONS_JSPX);
      assertEquals(0, foundFiles.size());
      foundFiles = FileUtils.findFiles(testFolder[0], EXTENSIONS_XHTML);
      assertEquals(3, foundFiles.size());

      for (Iterator<String> testFiles = foundFiles.iterator(); testFiles.hasNext();)
      {
         String testFileName = testFiles.next();
         File testFile = new File(testFileName);
         assertTrue(testFile.canRead());
      }

      foundFiles = FileUtils.findFiles(new BadFile(testFolder[0].getAbsolutePath()), null);
      assertEquals(12, foundFiles.size());
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.FileUtils#findFiles(java.io.File, java.util.List)}.
    */
   public void testFindFilesFileAndAccess()
   {
      List<String> foundFiles = FileUtils.findFiles(testFolder[0], EXTENSIONS_XHTML);
      for (Iterator<String> testFiles = foundFiles.iterator(); testFiles.hasNext();)
      {
         String testFileName = testFiles.next();
         File testFile = new File(testFileName);
         assertTrue(testFile.canRead());
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.FileUtils#extractExtension(java.lang.String)}.
    */
   public void testExtractExtension()
   {
      assertEquals("", FileUtils.extractExtension("testFile"));
      assertEquals("txt", FileUtils.extractExtension("testFile.txt"));
      assertEquals("htaccess", FileUtils.extractExtension(".htaccess"));
      assertEquals("", FileUtils.extractExtension(null));
      assertEquals("txt", FileUtils.extractExtension("testFile.doc.txt"));
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.FileUtils.FolderFilter#accept(File)}.
    */
   public void testFolderFilterAccept()
   {
      FileUtils.FolderFilter folderFilter = new FileUtils.FolderFilter();
      assertTrue(folderFilter.accept(tempFolder));
      assertFalse(folderFilter.accept(null));
      File tempFile = new File(tempFolder, "testFolderFilterAccept_1.tmp");
      tempFile.deleteOnExit();
      assertFalse(folderFilter.accept(tempFile));
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.FileUtils.FileExtensionFilter#accept(File, String)}.
    */
   public void testFileExtensionFilterAccept()
   {
      FileUtils.FileExtensionFilter filterNullExt = new FileUtils.FileExtensionFilter(null);
      assertTrue(filterNullExt.accept(tempFolder, "test1.tmp"));
      FileUtils.FileExtensionFilter filterNoExt = new FileUtils.FileExtensionFilter();
      assertTrue(filterNoExt.accept(tempFolder, "test1.tmp"));
      FileUtils.FileExtensionFilter filterEmptyExt = new FileUtils.FileExtensionFilter(new ArrayList<String>());
      assertTrue(filterEmptyExt.accept(tempFolder, "test1.tmp"));
      List<String> extensions = new ArrayList<String>();
      extensions.add("tmp");
      extensions.add("xls");
      FileUtils.FileExtensionFilter filterExt = new FileUtils.FileExtensionFilter(extensions);
      assertTrue(filterExt.accept(tempFolder, "test1.tmp"));
      assertFalse(filterExt.accept(tempFolder, "test1.pdf"));
      FileUtils.FileExtensionFilter filterLateExt = new FileUtils.FileExtensionFilter();
      assertTrue(filterLateExt.accept(tempFolder, "test1.tmp"));
      assertTrue(filterLateExt.accept(tempFolder, "test1.pdf"));
      filterLateExt.setAllowedExtensions(extensions);
      assertTrue(filterLateExt.accept(tempFolder, "test1.tmp"));
      assertFalse(filterLateExt.accept(tempFolder, "test1.pdf"));
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.FileUtils#getAllowedExtensions()}.
    */
   public void testFileExtensionFilterGetAllowedExtensions()
   {
      FileUtils.FileExtensionFilter filter = new FileUtils.FileExtensionFilter();
      assertNull(filter.getAllowedExtensions());
      filter.setAllowedExtensions(EXTENSIONS_JSPX);
      assertNotNull(filter.getAllowedExtensions());
      assertEquals(EXTENSIONS_JSPX, filter.getAllowedExtensions());
   }
}
