/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 * 
 */
package org.jboss.jsfunit.analysis.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for file handling.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $$Revision: $$
 */
public class FileUtils
{
   /** 
    * <p>A FilenameFilter implementation that accepts all files that have an
    * extension from a list passed into this filter.
    * If no extension list is set, or the list is empty, then all files will be accepted.</p>  
    */
   public static class FileExtensionFilter implements FilenameFilter
   {
      private List<String> allowedExtensions;

      public FileExtensionFilter()
      {
         this.allowedExtensions = null;
      }

      public FileExtensionFilter(List<String> allowedExtensions)
      {
         this.allowedExtensions = allowedExtensions;
      }

      public List<String> getAllowedExtensions()
      {
         return this.allowedExtensions;
      };

      public void setAllowedExtensions(List<String> allowedExtensions)
      {
         this.allowedExtensions = allowedExtensions;
      };

      public boolean accept(File dir, String name)
      {
         if (allowedExtensions == null || allowedExtensions.isEmpty())
         {
            return true;
         }
         else
         {
            String extension = FileUtils.extractExtension(name);
            return allowedExtensions.contains(extension);
         }
      };
   }

   /**
    * A FileFilter that accepts all folders.
    */
   public static class FolderFilter implements FileFilter
   {
      public boolean accept(File inFile)
      {
         if (inFile != null)
         {
            return inFile.isDirectory();
         }
         return false;
      }
   }

   /**
    * Find all files in the passed in directory and all sub-directories.
    * 
    * @param rootPath
    *            path of the root-directory
    * @param allowedExtensions
    *            list off allowed file extensions
    * @return a list of file-paths
    */
   public static List<String> findFilesRecursive(String rootPath, List<String> allowedExtensions)
   {
      File rootFolder = new File(rootPath);
      return findFilesRecursive(rootFolder, allowedExtensions);
   }

   /**
    * Find all files in the passed in directory and all sub-directories.
    * 
    * @param rootFolder
    *            path of the root-directory
    * @param allowedExtensions
    *            list off allowed file extensions
    * @return a list of file-paths
    */
   public static List<String> findFilesRecursive(File rootFolder, List<String> allowedExtensions)
   {
      List<String> foundFiles = FileUtils.findFiles(rootFolder, allowedExtensions);

      File[] subFolders = rootFolder.listFiles(new FolderFilter());
      for (int i = 0; i < subFolders.length; i++)
      {
         foundFiles.addAll(findFilesRecursive(subFolders[i], allowedExtensions));
      }

      return foundFiles;
   }

   /**
    * Find all files in the passed in directory.
    * 
    * @param folderPath
    *            path of the root-directory
    * @param allowedExtensions
    *            list off allowed file extensions
    * @return a list of file-paths
    */
   public static List<String> findFiles(String folderPath, List<String> allowedExtensions)
   {
      File folder = new File(folderPath);
      return FileUtils.findFiles(folder, allowedExtensions);
   }

   /**
    * Find all files in the passed in directory.
    * 
    * @param folder
    *            File object of a folder
    * @param allowedExtensions
    *            list off allowed file extensions
    * @return a list of file-paths
    */
   public static List<String> findFiles(File folder, List<String> allowedExtensions)
   {
      if (folder == null)
      {
         throw new IllegalArgumentException("Folder must not be null");
      }
      String folderPath;
      try
      {
         folderPath = folder.getCanonicalPath() + File.separator;
      }
      catch (IOException e)
      {
         folderPath = "---exception: " + e.getLocalizedMessage() + File.separator;
      }
      List<String> foundFiles = new ArrayList<String>();
      FilenameFilter filter = new FileUtils.FileExtensionFilter(allowedExtensions);
      String[] files = folder.list(filter);
      for (int i = 0; i < files.length; i++)
      {
         foundFiles.add(folderPath + files[i]);
      }
      return foundFiles;
   }

   /**
    * Extract the file-extension
    * @param fileName the file name
    * @return empty string (if no extension) or the files extension
    */
   public static String extractExtension(String fileName)
   {
      String extension = null;
      if (fileName == null)
      {
         extension = "";
      }
      else
      {
         int place = fileName.lastIndexOf('.');
         if (place >= 0)
         {
            extension = fileName.substring(place + 1);
         }
         else
         {
            extension = "";
         }
      }
      return extension;
   }
}
