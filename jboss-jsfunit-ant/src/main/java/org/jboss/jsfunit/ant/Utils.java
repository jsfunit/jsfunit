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
 */
package org.jboss.jsfunit.ant;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Utility Class to handle some file io operations that are extremely
 * useful but for some odd reason not implemeneted in the jdk itself.
 * 
 * @author Matt Wringe
 *
 */
public class Utils {

	/**
	 * Copy a file or directory from one location to another
	 * @param srcfile The file to copy
	 * @param destfile The destination for the copied file
	 * @throws Exception If an exception occured during the copy
	 */
	public static void copy (File srcfile, File destfile) throws Exception
	{
		if (srcfile.isDirectory())
		{
			destfile.mkdirs();
			String[] files = srcfile.list();
			for (int i=0; i<files.length; i++)
			{
				String newSrcFile = srcfile.getPath() + File.separator + files[i];
				String newDestFile = destfile.getPath() + File.separator + files[i];
				copy (new File (newSrcFile), new File (newDestFile));
			}
		}
		else if (srcfile.isFile())
		{
			FileChannel inputChannel = new FileInputStream(srcfile).getChannel();
			FileChannel outputChannel = new FileOutputStream(destfile).getChannel();
			inputChannel.transferTo(0, inputChannel.size(), outputChannel);
			inputChannel.close();
			outputChannel.close();

		}
		else
		{
			throw new Exception ("Invalid file : " + srcfile);
		}
	}


	/**
	 * Unzips a zipFile to the specified directory.
	 * @param zipFile The file to unzip
	 * @param destDirectory The directory to unzip the file
	 * @throws Exception If an error occurs during the unzipping process
	 */
	public static void unzip (ZipFile zipFile, File destDirectory) throws Exception
	{			 
		if (!destDirectory.exists())
		{
			destDirectory.mkdir();
		}
		else
		{
			if (!destDirectory.isDirectory())
			{
				throw new Exception ("The destDirectory file already exists and is not a directory");
			}
		}

		Enumeration enumer = zipFile.entries();
		while (enumer.hasMoreElements())
		{
			ZipEntry zipEntry = (ZipEntry)enumer.nextElement();
			if (zipEntry.isDirectory())
			{
				File dir = new File(destDirectory, zipEntry.toString());
				dir.mkdir();
			}
			else
			{
				File file = new File(destDirectory, zipEntry.toString());
				InputStream is = zipFile.getInputStream(zipEntry);
				OutputStream os = new FileOutputStream (file);
				int c;
				while ((c = is.read()) != -1)
				{
					os.write(c);
				}
				is.close();
				os.close();
			}
		}
	}

	/**
	 * Zips a directory
	 * @param srcDirectory the directory to zip
	 * @param destFile the newly created zip file
	 * @throws Exception
	 */
	public static void zip (File srcDirectory, File destFile) throws Exception
	{
		zip (srcDirectory, destFile, false);
	}

	/**
	 * Zips a directory. If the directory is an exploded archive, then don't add the 
	 * directory itself to the zipped file
	 * @param srcDirectory the directory to zip
	 * @param destFile the newly created zip file
	 * @param isArchive true if the srcDirectory is a exploded archive
	 * @throws Exception if an error occured during the zip
	 */
	public static void zip (File srcDirectory, File destFile, Boolean isArchive) throws Exception
	{
		if (destFile.exists())
		{
			throw new Exception ("The destFile [ + " + destFile + " already exists, cannot zip to an already existing file");
		}

		OutputStream os = new FileOutputStream(destFile);
		ZipOutputStream zos = new ZipOutputStream(os);

		try{
			if (isArchive)
			{
				archive ("", srcDirectory, zos);
			}
			else
			{
				zip ("", srcDirectory, zos);
			}
		} finally {
			zos.close();
			os.close();
		}
	}

	/**
	 * Zips a directory
	 * @param prefix used to specify the parent directories for the files
	 * @param srcDirectory the directory to be zipped
	 * @param zos the ZipOutputStream to do the zipping
	 * @throws IOException If an exception occurs during the zip process
	 */
	private static void zip (String prefix, File srcDirectory, ZipOutputStream zos) throws IOException
	{
		prefix += srcDirectory.getName() + "/";

		File[] children = srcDirectory.listFiles();
		for (int i=0; i< children.length; i++)
		{
			if (children[i].isDirectory()){
                            zip (prefix, children[i], zos);
			}
			else
			{
                            zipOneFile(prefix, children[i], zos);
			}
		}
	}

        /**
	 * Zips a single file.
	 * @param prefix used to specify the parent directory of the file
	 * @param archiveDirectory the directory to be zipped
	 * @param zos the ZipOutputStream used for the zip process
	 * @throws IOException If an exception occurs during the zip process
	 */
        private static void zipOneFile(String prefix, File file, ZipOutputStream zos) throws IOException
        {
            ZipEntry zipFileEntry = new ZipEntry(prefix + file.getName());
            InputStream in = new BufferedInputStream(new FileInputStream(file.getAbsolutePath()));
            int c;
            zos.putNextEntry(zipFileEntry);

            while ((c = in.read()) != -1)
            {
                    zos.write(c);
            }

            in.close();
            zos.flush();
        }
        
	/**
	 * Zips an exploded archive, this means that the directory itself is not added to the zip
	 * @param prefix used to specify the parent directories of the files
	 * @param archiveDirectory the directory to be zipped
	 * @param zos the ZipOutputStream used for the zip process
	 * @throws IOException If an exception occurs during the zip process
	 */
	private static void archive (String prefix, File archiveDirectory, ZipOutputStream zos) throws IOException
	{
		File[] children = archiveDirectory.listFiles();
		for (int i=0; i< children.length; i++)
		{
                    if (children[i].isFile()) 
                    {
                        zipOneFile("", children[i], zos);
                        continue;
                    }
                    
                    zip (prefix, children[i], zos);
		} 
	}

	/**
	 * Creates an exploded war from a war archive
	 * @param archiveFile The war File 
	 * @param destDirectory The directory to explode the war
	 * @throws Exception If an error occurs when exploding the war
	 */
	public static File explodeArchive (ZipFile archiveFile, File destDirectory) throws Exception
	{
		if (!destDirectory.exists())
		{
			destDirectory.mkdir();
		}
		else 
		{
			if (!destDirectory.isDirectory())
			{
				throw new Exception ("The destDirectory file already exists and is not a directory ");
			}
		}

		//we want to maintain the war archive name in an exploded war
		String archiveName = getArchiveName(archiveFile);
		File explodedArchive = new File(destDirectory + File.separator + archiveName);
		unzip (archiveFile, explodedArchive);
		return explodedArchive;
	}


	/**
	 * The getName() method for a ZipFile returns the full file path and not the 
	 * name of the actual file. This method is used to retrieve the file name
	 * without the file path.
	 * @param file The file to get the file name
	 */
	public static String getArchiveName (ZipFile file)
	{
		String filePath = file.getName();
		// TODO: should probably check that the last separator is not escaped    	 
		int lastSeparator = filePath.lastIndexOf(File.separator);
		String fileName = filePath.substring(lastSeparator);

		return fileName;
	}


	/**
	 * Imploded an already exploded archive
	 * @param explodedArchive the directory of the exploded archive
	 * @param destArchive the name of the new archive to create
	 * @throws Exception if an exception occurs during the archive process
	 */
	public static void archive(File explodedArchive, File destArchive) throws Exception {
		if (destArchive.exists())
		{
			throw new Exception ("The destArchive " + destArchive + "already exists");
		}
		zip (explodedArchive, destArchive, true);
	}

}
