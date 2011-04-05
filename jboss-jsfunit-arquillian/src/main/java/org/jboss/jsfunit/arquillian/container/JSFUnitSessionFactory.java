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
package org.jboss.jsfunit.arquillian.container;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.servlet.http.HttpSession;

import org.jboss.jsfunit.api.BasicAuthentication;
import org.jboss.jsfunit.api.Browser;
import org.jboss.jsfunit.api.BrowserVersion;
import org.jboss.jsfunit.api.Cookies;
import org.jboss.jsfunit.api.FormAuthentication;
import org.jboss.jsfunit.api.InitialPage;
import org.jboss.jsfunit.api.InitialRequest;
import org.jboss.jsfunit.api.Proxy;
import org.jboss.jsfunit.framework.BasicAuthenticationStrategy;
import org.jboss.jsfunit.framework.FormAuthenticationStrategy;
import org.jboss.jsfunit.framework.InitialRequestStrategy;
import org.jboss.jsfunit.framework.WebClientSpec;
import org.jboss.jsfunit.framework.WebConversationFactory;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;

/**
 * JSFUnitSessionFactory
 *
 * @author Stan Silvert
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
public class JSFUnitSessionFactory
{
   private static final String JSFSESSION_FIELD_INJECTED = JSFUnitTestEnricher.class.getName() + ".JSFSESSION_FIELD_INJECTED";

   private static final String JSFSESSION_METHOD_INJECTED = JSFUnitTestEnricher.class.getName() + ".JSFSESSION_METHOD_INJECTED";

   //-------------------------------------------------------------------------------------||
   // Internal API -----------------------------------------------------------------------||
   //-------------------------------------------------------------------------------------||
   
   public static JSFClientSession getJSFClientSession(AnnotatedElement injectionPoint) throws IOException
   {
      return findJSFSession(injectionPoint).getJSFClientSession();
   }

   public static JSFServerSession getJSFServerSession(AnnotatedElement injectionPoint) throws IOException
   {
      return findJSFSession(injectionPoint).getJSFServerSession();
   }

   public static JSFSession findJSFSession(AnnotatedElement injectionPoint) throws IOException
   {
      JSFUnitSessionFactory factory = new JSFUnitSessionFactory();
      ElementType injectionType = factory.getElementType(injectionPoint);
      HttpSession httpSession = WebConversationFactory.getSessionFromThreadLocal();
      JSFSession jsfSession = null;
      if (injectionType == ElementType.FIELD)
      {
         jsfSession = (JSFSession) httpSession.getAttribute(JSFSESSION_FIELD_INJECTED);
      }
      if (injectionType == ElementType.METHOD)
      {
         jsfSession = (JSFSession) httpSession.getAttribute(JSFSESSION_METHOD_INJECTED);
      }

      if (jsfSession != null)
      {
         return jsfSession;
      }
      jsfSession = factory.createJSFSession(injectionPoint, injectionType, httpSession);
      return jsfSession;
   }

   //-------------------------------------------------------------------------------------||
   // Internal Helpers -------------------------------------------------------------------||
   //-------------------------------------------------------------------------------------||

   public JSFUnitSessionFactory()
   {
   }
   
   private JSFSession createJSFSession(AnnotatedElement injectionPoint, ElementType injectionType, HttpSession httpSession)
         throws IOException
   {
      JSFSession jsfSession = new JSFSession(createWebClientSpec(injectionPoint, injectionType));

      if (injectionType == ElementType.FIELD)
      {
         httpSession.setAttribute(JSFSESSION_FIELD_INJECTED, jsfSession);
      }
      if (injectionType == ElementType.METHOD)
      {
         httpSession.setAttribute(JSFSESSION_METHOD_INJECTED, jsfSession);
      }

      return jsfSession;
   }

   private WebClientSpec createWebClientSpec(AnnotatedElement injectionPoint, ElementType injectionType)
   {
      InitialPage initialPage = getAnnotation(InitialPage.class, injectionPoint, injectionType);
      if (initialPage == null)
      {
         throw new IllegalArgumentException(
               "@InitialPage required for injection.  Specify at class level for field injection or at method level for method injection.");
      }

      Browser browser = null;
      BrowserVersion browserAnno = getAnnotation(BrowserVersion.class, injectionPoint, injectionType);
      if (browserAnno == null)
      {
         browser = Browser.DEFAULT;
      }
      if (browserAnno != null)
      {
         browser = browserAnno.value();
      }

      String host = null;
      int port = 0;

      Proxy proxyAnno = getAnnotation(Proxy.class, injectionPoint, injectionType);
      if (proxyAnno != null)
      {
         host = proxyAnno.host();
         port = proxyAnno.port();
      }

      WebClientSpec wcSpec = new WebClientSpec(initialPage.value(), browser.getVersion(), host, port);

      BasicAuthentication basicAuthAnno = getAnnotation(BasicAuthentication.class, injectionPoint, injectionType);
      FormAuthentication formAuthAnno = getAnnotation(FormAuthentication.class, injectionPoint, injectionType);
      InitialRequest initReqAnno = getAnnotation(InitialRequest.class, injectionPoint, injectionType);
      
      validateOneNonNullInitialRequest(injectionType, basicAuthAnno, formAuthAnno, initReqAnno);

      if (basicAuthAnno != null)
      {
         String userName = basicAuthAnno.userName();
         String password = basicAuthAnno.password();
         wcSpec.setInitialRequestStrategy(new BasicAuthenticationStrategy(userName, password));
      }

      if (formAuthAnno != null)
      {
         String userName = formAuthAnno.userName();
         String password = formAuthAnno.password();
         String submitComponent = formAuthAnno.submitComponent();
         String userNameComponent = formAuthAnno.userNameComponent();
         String passwordComponent = formAuthAnno.passwordComponent();
         wcSpec.setInitialRequestStrategy(
               new FormAuthenticationStrategy(
                     userName, password, submitComponent, userNameComponent, passwordComponent));
      }

      if (initReqAnno != null)
      {
         Class<? extends InitialRequestStrategy> clazz = initReqAnno.value();
         InitialRequestStrategy initReq = newInstance(clazz);
         wcSpec.setInitialRequestStrategy(initReq);
      }

      setCookies(wcSpec, injectionPoint, injectionType);

      return wcSpec;
   }

   private void setCookies(WebClientSpec wcSpec, AnnotatedElement injectionPoint, ElementType injectionType)
   {
      Cookies cookiesAnno = getAnnotation(Cookies.class, injectionPoint, injectionType);
      if (cookiesAnno == null)
      {
         return;
      }

      String[] names = cookiesAnno.names();
      String[] values = cookiesAnno.values();
      if (names.length != values.length)
      {
         throw new IllegalArgumentException(
               "'names' and 'values' must have equal number of elements in @Cookies annotation.");
      }

      for (int i = 0; i < names.length; i++)
      {
         wcSpec.addCookie(names[i], values[i]);
      }
   }

   /**
   * Make sure that only one of the InitialRequest annotations was used.
   *
   * @throws IllegalArgumentException if more than one is null.
   */
   private void validateOneNonNullInitialRequest(ElementType injectionType, Annotation... initRequests)
   {
      boolean found = false;
      for (Annotation anno : initRequests)
      {
         if ((anno != null) && found)
         {
            if (injectionType == ElementType.METHOD)
            {
               throw new IllegalArgumentException(
                     "Only one of @BasicAuthentication, @FormAuthentication, or @InitialRequest is allowed per method.");
            }
            if (injectionType == ElementType.FIELD)
            {
               throw new IllegalArgumentException(
                     "Only one of @BasicAuthentication, @FormAuthentication, or @InitialRequest is allowed at the class level.");
            }
         }
         if (anno != null)
         {
            found = true;
         }
      }
   }

   private ElementType getElementType(AnnotatedElement injectionPoint)
   {
      if (injectionPoint instanceof Method)
      {
         return ElementType.METHOD;
      }
      if (injectionPoint instanceof Field)
      {
         return ElementType.FIELD;
      }

      // should not really be possible unless the JSFUnitResource changes 
      throw new IllegalArgumentException(
            "JSFUnit artifacts are only injectable at method parameters or field.  Found InjectionPoint Member "
                  + injectionPoint);
   }
   
   private <T extends Annotation> T getAnnotation(Class<T> annoClass, AnnotatedElement injectionPoint, ElementType injectionType)
   {
      if (injectionType == ElementType.METHOD)
      {
         return injectionPoint.getAnnotation(annoClass);
      }

      if (injectionType == ElementType.FIELD)
      {
         Class<?> clazz = ((Field)injectionPoint).getDeclaringClass();
         return clazz.getAnnotation(annoClass);
      }

      return null;
   }
   
   private static <T> T newInstance(Class<T> clazz)
   {
      try
      {
         return clazz.newInstance();
      }
      catch (InstantiationException e)
      {
         throw new IllegalArgumentException(e);
      }
      catch (IllegalAccessException e)
      {
         throw new IllegalArgumentException(e);
      }
   }
}
