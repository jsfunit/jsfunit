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
package org.jboss.jsfunit.cdi;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.jsfunit.arquillian.container.JSFUnitSessionFactory;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;

/**
 * A CDI enabled producer for creating {@link JSFSession}, {@link JSFClientSession} and {@link JSFServerSession}. 
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
// TODO: The CDI API can be removed when ARQ-405 is fixed. We don't really need to support both CDI and Arq
public class JSFUnitCDIProducer
{
   //-------------------------------------------------------------------------------------||
   // CDI API ----------------------------------------------------------------------------||
   //-------------------------------------------------------------------------------------||
   
   @Produces
   static JSFClientSession getJSFClientSession(InjectionPoint injectionPoint) throws IOException
   {
      return JSFUnitSessionFactory.getJSFClientSession(convertInjectionPoint(injectionPoint));
   }

   @Produces
   static JSFServerSession getJSFServerSession(InjectionPoint injectionPoint) throws IOException
   {
      return JSFUnitSessionFactory.getJSFServerSession(convertInjectionPoint(injectionPoint));
   }

   @Produces
   static JSFSession findJSFSession(InjectionPoint injectionPoint) throws IOException
   {
      return JSFUnitSessionFactory.findJSFSession(convertInjectionPoint(injectionPoint));
   }

   private static AnnotatedElement convertInjectionPoint(InjectionPoint injectionPoint)
   {
      if(injectionPoint.getMember() instanceof AnnotatedElement)
      {
         return (AnnotatedElement)injectionPoint.getMember();
      }
      throw new RuntimeException("Illegal injection point: " + injectionPoint.getMember());
   }
}
