/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.jsfunit.arquillian.client;

import org.jboss.arquillian.container.test.spi.TestDeployment;
import org.jboss.arquillian.container.test.spi.client.deployment.ProtocolArchiveProcessor;
import org.jboss.jsfunit.arquillian.container.JSFUnitCleanupTestTreadFilter;
import org.jboss.jsfunit.framework.JSFUnitFilter;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.spec.servlet.web.WebAppDescriptor;

/**
 * Extension that will add JSFUnit required filters for Servlet 2.5 based applications.
 * 
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
public class JSFUnitProtocolArchiveProcessor implements ProtocolArchiveProcessor {
    private static final ArchivePath WEB_XML = ArchivePaths.create("WEB-INF/web.xml");
    private static final Double WEB_XML_VERSION = 2.5;

    /**
     * If this is a Servlet 2.5 Archive, append the JSFUnit filter to the web.xml.
     */
    @Override
    public void process(TestDeployment testDeployment, Archive<?> protocolArchive) {
        if (WebArchive.class.isInstance(protocolArchive)) {
            WebArchive webArchive = WebArchive.class.cast(protocolArchive);

            if (webArchive.contains(WEB_XML)) {
                WebAppDescriptor descriptor = loadDescriptor(webArchive.get(WEB_XML));
                if (shouldAddJSFUnitFilters(descriptor)) {
                    addJSFUnitFilters(descriptor);
                    webArchive.delete(WEB_XML);
                    webArchive.add(new StringAsset(descriptor.exportAsString()), WEB_XML);
                }
            }
        }
    }

    private boolean shouldAddJSFUnitFilters(WebAppDescriptor descriptor) {
        try {
            Double definedVersion = Double.parseDouble(descriptor.getVersion());
            return definedVersion <= WEB_XML_VERSION;
        } catch (NumberFormatException e) {
            throw new RuntimeException(
                    "Could not parse the web.xml version number to determine if it is less or equal to 2.5, "
                            + "this so we can merge in the JSFUnit support", e);
        }
    }

    private WebAppDescriptor loadDescriptor(Node node) {
        return Descriptors.importAs(WebAppDescriptor.class).from(node.getAsset().openStream());
    }

    private WebAppDescriptor addJSFUnitFilters(WebAppDescriptor descriptor) {
        return descriptor.filter(JSFUnitCleanupTestTreadFilter.class, "/ArquillianServletRunner").filter(JSFUnitFilter.class,
                "/ArquillianServletRunner");
    }
}
