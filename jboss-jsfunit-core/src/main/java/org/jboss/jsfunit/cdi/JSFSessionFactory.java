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

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.servlet.http.HttpSession;
import org.jboss.jsfunit.framework.BasicAuthenticationStrategy;
import org.jboss.jsfunit.framework.Environment;
import org.jboss.jsfunit.framework.FormAuthenticationStrategy;
import org.jboss.jsfunit.framework.InitialRequestStrategy;
import org.jboss.jsfunit.framework.WebConversationFactory;
import org.jboss.jsfunit.framework.WebClientSpec;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;

/**
 * This class is called by CDI to create the JSFSession, JSFClientSession, and
 * JSFServerSession.  JSFUnit annotations are used to describe how the JSFSession
 * will be created.
 *
 * It will store a copy of JSFSession in the HttpSession for the active test.
 *
 * @author Stan Silvert
 */
public class JSFSessionFactory {
   public static final String JSFSESSION_FIELD_INJECTED = JSFSessionFactory.class.getName() + ".JSFSESSION_FIELD_INJECTED";
   public static final String JSFSESSION_METHOD_INJECTED = JSFSessionFactory.class.getName() + ".JSFSESSION_METHOD_INJECTED";

   public JSFSessionFactory()
   {
   }

   private Annotation getAnnotation(Class annoClass,
                                    InjectionPoint injectionPoint,
                                    ElementType injectionType)
   {
      if (injectionType == ElementType.METHOD )
      {
         Method method = (Method)injectionPoint.getMember();
         return method.getAnnotation(annoClass);
      }

      if (injectionType == ElementType.FIELD)
      {
         Class clazz = injectionPoint.getMember().getDeclaringClass();
         return clazz.getAnnotation(annoClass);
      }

      return null;
   }

   WebClientSpec createWebClientSpec(InjectionPoint injectionPoint,
                                     ElementType injectionType)
   {
      Annotation initialPage = getAnnotation(InitialPage.class, injectionPoint, injectionType);
      if (initialPage == null) throw new IllegalArgumentException("@InitialPage required for injection.  Specify at class level for field injection or at method level for method injection.");

      Browser browser = null;
      Annotation browserAnno = getAnnotation(org.jboss.jsfunit.cdi.BrowserVersion.class, injectionPoint, injectionType);
      if (browserAnno == null) browser = Browser.DEFAULT;
      if (browserAnno != null) browser = ((org.jboss.jsfunit.cdi.BrowserVersion)browserAnno).value();

      String host = null;
      int port = 0;

      Proxy proxyAnno = (Proxy)getAnnotation(Proxy.class, injectionPoint, injectionType);
      if (proxyAnno != null)
      {
         host = proxyAnno.host();
         port = proxyAnno.port();
      }

      WebClientSpec wcSpec = new WebClientSpec(((InitialPage)initialPage).value(), browser.getVersion(), host, port);

      BasicAuthentication basicAuthAnno = (BasicAuthentication)getAnnotation(BasicAuthentication.class, injectionPoint, injectionType);
      FormAuthentication formAuthAnno = (FormAuthentication)getAnnotation(FormAuthentication.class, injectionPoint, injectionType);
      InitialRequest initReqAnno = (InitialRequest)getAnnotation(InitialRequest.class, injectionPoint, injectionType);
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
         wcSpec.setInitialRequestStrategy(new FormAuthenticationStrategy(userName, password, submitComponent, userNameComponent, passwordComponent));
      }

      if (initReqAnno != null)
      {
        Class clazz = initReqAnno.value();
        InitialRequestStrategy initReq = (InitialRequestStrategy)Environment.newInstance(clazz);
        wcSpec.setInitialRequestStrategy(initReq);
      }

      setCookies(wcSpec, injectionPoint, injectionType);

      return wcSpec;
   }

   private void setCookies(WebClientSpec wcSpec,
                           InjectionPoint injectionPoint,
                           ElementType injectionType)
   {
      Cookies cookiesAnno = (Cookies)getAnnotation(Cookies.class, injectionPoint, injectionType);
      if (cookiesAnno == null) return;

      String[] names = cookiesAnno.names();
      String[] values = cookiesAnno.values();
      if (names.length != values.length) throw new IllegalArgumentException("'names' and 'values' must have equal number of elements in @Cookies annotation.");

      for (int i=0; i < names.length; i++)
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
            if (injectionType == ElementType.METHOD) throw new IllegalArgumentException("Only one of @BasicAuthentication, @FormAuthentication, or @InitialRequest is allowed per method.");
            if (injectionType == ElementType.FIELD) throw new IllegalArgumentException("Only one of @BasicAuthentication, @FormAuthentication, or @InitialRequest is allowed at the class level.");
         }
         if (anno != null) found = true;
      }
   }

   JSFSession createJSFSession(InjectionPoint injectionPoint, 
                               ElementType injectionType,
                               HttpSession httpSession) throws IOException
   {
      JSFSession jsfSession = new JSFSession(createWebClientSpec(injectionPoint, injectionType));

      if (injectionType == ElementType.FIELD) httpSession.setAttribute(JSFSESSION_FIELD_INJECTED, jsfSession);
      if (injectionType == ElementType.METHOD) httpSession.setAttribute(JSFSESSION_METHOD_INJECTED, jsfSession);

      return jsfSession;
   }

   @Produces
   JSFSession findJSFSession(InjectionPoint injectionPoint) throws IOException
   {
      ElementType injectionType = getElementType(injectionPoint);
      HttpSession httpSession = WebConversationFactory.getSessionFromThreadLocal();
      JSFSession jsfSession = null;
      if (injectionType == ElementType.FIELD) jsfSession = (JSFSession)httpSession.getAttribute(JSFSESSION_FIELD_INJECTED);
      if (injectionType == ElementType.METHOD) jsfSession = (JSFSession)httpSession.getAttribute(JSFSESSION_METHOD_INJECTED);

      if (jsfSession != null) return jsfSession;
      jsfSession = createJSFSession(injectionPoint, injectionType, httpSession);
      return jsfSession;
   }

   @Produces
   JSFClientSession getJSFClientSession(InjectionPoint injectionPoint) throws IOException
   {
      return findJSFSession(injectionPoint).getJSFClientSession();
   }

   @Produces
   JSFServerSession getJSFServerSession(InjectionPoint injectionPoint) throws IOException
   {
      return findJSFSession(injectionPoint).getJSFServerSession();
   }

   private ElementType getElementType(InjectionPoint injectionPoint)
   {
      Member member = injectionPoint.getMember();
      if (member instanceof Method) return ElementType.METHOD;
      if (member instanceof Field) return ElementType.FIELD;

      throw new IllegalArgumentException("JSFUnit artifacts are only injectable at method or field.  Found InjectionPoint Member "
              + injectionPoint.getMember());
   }
}