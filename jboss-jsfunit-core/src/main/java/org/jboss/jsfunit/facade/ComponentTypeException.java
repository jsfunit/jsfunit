/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.jsfunit.facade;

import javax.faces.component.UIComponent;

/**
 * This class provides an exception for methods that expect a componentID to
 * map to a particular JSF component type.
 *
 * @author Stan Silvert
 */
public class ComponentTypeException extends RuntimeException
{
   
   private ComponentTypeException(String methodName,
                                  String componentID,
                                  Class expectedType,
                                  UIComponent component)
   {
      super(methodName + " is only valid on components of type " + expectedType.getName() + ". '" 
            + componentID + "' is of type " + component.getClass().getName() + ".");
   }
   
   /**
    * Check to see if the componentID references a JSF component of the given
    * type.  If not, throw the ComponentTypeException.
    *
    * @param methodName The name of the method that accepts componentID as a
    *                   parameter.
    * @param componentID The componentID passed to the calling method.
    * @param expectedType The JSF component type expected.
    * @param clientIDs The clientIDs from the latest request.
    *
    * @throws ComponentTypeException if componentID does not reference a JSF
    *         component of the expected type.
    */
   public static void check(String methodName, 
                            String componentID,
                            Class expectedType, 
                            ClientIDs clientIDs)
   {
      UIComponent component = clientIDs.findComponent(componentID);
      if (expectedType.isAssignableFrom(component.getClass())) return;
      throw new ComponentTypeException(methodName, componentID, expectedType, component);
   }
}
