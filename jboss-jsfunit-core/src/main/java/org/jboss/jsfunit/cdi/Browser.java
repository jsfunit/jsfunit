/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.jsfunit.cdi;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * An enum wrapper for HtmlUnit's BrowserVersion.
 *
 * @author ssilvert
 */
public enum Browser {

   DEFAULT(BrowserVersion.getDefault()),
   FIREFOX_3(BrowserVersion.FIREFOX_3),
   FIREFOX_3_6(BrowserVersion.FIREFOX_3_6),
   INTERNET_EXPLORER_6(BrowserVersion.INTERNET_EXPLORER_6),
   INTERNET_EXPLORER_7(BrowserVersion.INTERNET_EXPLORER_7),
   INTERNET_EXPLORER_8(BrowserVersion.INTERNET_EXPLORER_8);

   private BrowserVersion browserVersion;

   private Browser(BrowserVersion browserVersion)
   {
      this.browserVersion = browserVersion;
   }

   public BrowserVersion getVersion()
   {
      return this.browserVersion;
   }
}
