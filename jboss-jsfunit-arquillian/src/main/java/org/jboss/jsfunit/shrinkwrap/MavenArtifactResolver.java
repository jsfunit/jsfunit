/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package org.jboss.jsfunit.shrinkwrap;

import java.io.File;

/**
 * Code from http://community.jboss.org/wiki/HowdoIaddJARfilestothetestarchive
 *
 * @author Dan Allen
 * @author Stan Silvert <ssilvert@redhat.com> (C) <2011> <Red Hat, Inc.>
 */
public class MavenArtifactResolver
{
   private static final String LOCAL_MAVEN_REPO =
         System.getProperty("maven.repo.local") != null ?
               System.getProperty("maven.repo.local") :
               (System.getProperty("user.home") + File.separatorChar +
               ".m2" + File.separatorChar + "repository");

   public static File resolve(final String groupId, final String artifactId,
      final String version)
   {
      return resolve(groupId, artifactId, version, null);
   }

   public static File resolve(final String groupId, final String artifactId,
      final String version, final String classifier)
   {
      return new File(LOCAL_MAVEN_REPO + File.separatorChar +
            groupId.replace(".", File.separator) + File.separatorChar +
            artifactId + File.separatorChar +
            version + File.separatorChar +
            artifactId + "-" + version +
            (classifier != null ? ("-" + classifier) : "") + ".jar");
   }

   public static File resolveQualifiedId(final String qualifiedArtifactId)
   {
      String[] segments = qualifiedArtifactId.split(":");
      if (segments.length == 3)
      {
         return resolve(segments[0], segments[1], segments[2]);
      }
      else if (segments.length == 4)
      {
         return resolve(segments[0], segments[1], segments[2], segments[3]);
      }
      throw new IllegalArgumentException("Invalid qualified artifactId syntax: " +
         qualifiedArtifactId);
   }

   public static File[] resolveQualifiedIds(final String... qualifiedArtifactIds)
   {
      int n = qualifiedArtifactIds.length;
      File[] artifacts = new File[n];
      for (int i = 0; i < n; i++)
      {
         artifacts[i] = resolveQualifiedId(qualifiedArtifactIds[i]);
      }

      return artifacts;
   }
}