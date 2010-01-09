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
package org.jboss.jsfunit.analysis.model;

import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;

/**
 * A TestPropertyResolver.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
@SuppressWarnings("deprecation")
public class TestPropertyResolver extends PropertyResolver
{

   @Override
   public Class getType(Object arg0, Object arg1) throws EvaluationException, PropertyNotFoundException
   {
      return null;
   }

   @Override
   public Class getType(Object arg0, int arg1) throws EvaluationException, PropertyNotFoundException
   {
      return null;
   }

   @Override
   public Object getValue(Object arg0, Object arg1) throws EvaluationException, PropertyNotFoundException
   {
      return null;
   }

   @Override
   public Object getValue(Object arg0, int arg1) throws EvaluationException, PropertyNotFoundException
   {
      return null;
   }

   @Override
   public boolean isReadOnly(Object arg0, Object arg1) throws EvaluationException, PropertyNotFoundException
   {
      return false;
   }

   @Override
   public boolean isReadOnly(Object arg0, int arg1) throws EvaluationException, PropertyNotFoundException
   {
      return false;
   }

   @Override
   public void setValue(Object arg0, Object arg1, Object arg2) throws EvaluationException, PropertyNotFoundException
   {
   }

   @Override
   public void setValue(Object arg0, int arg1, Object arg2) throws EvaluationException, PropertyNotFoundException
   {
   }

}
