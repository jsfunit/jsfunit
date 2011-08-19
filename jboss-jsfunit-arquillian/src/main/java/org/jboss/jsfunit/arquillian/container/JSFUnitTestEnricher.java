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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.jboss.arquillian.test.spi.TestEnricher;
import org.jboss.jsfunit.api.JSFUnitResource;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;

/**
 * JSFUnitTestEnricher
 * 
 * 
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @author <a href="http://community.jboss.org/people/spinner)">Jose Rodolfo freitas</a>
 * @version $Revision: $
 */
public class JSFUnitTestEnricher implements TestEnricher {
    // -------------------------------------------------------------------------------------||
    // Contract - TestEnricher ------------------------------------------------------------||
    // -------------------------------------------------------------------------------------||

    @Override
    public void enrich(Object testCase) {
        List<Field> annotatedFields = SecurityActions.getFieldsWithAnnotation(testCase.getClass(), JSFUnitResource.class);

        try {
            for (Field field : annotatedFields) {
                Object value = null;

                if (field.getType() == JSFSession.class) {
                    value = JSFUnitSessionFactory.findJSFSession(field);
                } else if (field.getType() == JSFClientSession.class) {
                    value = JSFUnitSessionFactory.getJSFClientSession(field);
                } else if (field.getType() == JSFServerSession.class) {
                    value = JSFUnitSessionFactory.getJSFServerSession(field);
                }

                field.set(testCase, value);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not inject members", e);
        }
    }

    @Override
    public Object[] resolve(Method method) {
        Object[] values = new Object[method.getParameterTypes().length];

        try {
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
                if (values[i] != null) {
                    continue; // someone else has set this value, possible the CDIEnricher
                }
                Class<?> parameterType = parameterTypes[i];

                Object value = null;
                if (parameterType == JSFSession.class) {
                    value = JSFUnitSessionFactory.findJSFSession(method);
                } else if (parameterType == JSFClientSession.class) {
                    value = JSFUnitSessionFactory.getJSFClientSession(method);
                } else if (parameterType == JSFServerSession.class) {
                    value = JSFUnitSessionFactory.getJSFServerSession(method);
                }
                values[i] = value;
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not inject method parameters", e);
        }
        return values;
    }

}
