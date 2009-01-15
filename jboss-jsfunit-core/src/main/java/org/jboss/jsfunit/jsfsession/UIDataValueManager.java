/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.jsfunit.jsfsession;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.ValueHolder;

/**
 * This class provides a way to get the value from a component that is
 * inside a UIData.  Before a value can be returned, the proper row indexes
 * must be set on the UIData.
 *
 * @author Stan Silvert
 * @since 1.0
 */
class UIDataValueManager 
{
    // key = a UIData ancestor of the component
    // value = the row id needed to get the component's value
    private Map<UIData, Integer> uiDataRowMap = new HashMap<UIData, Integer>();
    
    private ValueHolder component;

    UIDataValueManager(UIData parent, ValueHolder component)
    {
       this.component = component;
       
       // find all UIData ancestors
       UIComponent uiComponent = parent;
       do
       {
          if (uiComponent instanceof UIData)
          {
             UIData uiData = (UIData)uiComponent;
            // System.out.println("$$$ Adding " + uiData.getClientId(javax.faces.context.FacesContext.getCurrentInstance()) 
            //                    + " to uiDataRowMap with index=" + uiData.getRowIndex());
             uiDataRowMap.put(uiData, uiData.getRowIndex());
          }
          uiComponent = uiComponent.getParent();
       }
       while (uiComponent != null);
    }
    
    Object getValue()
    {
       Map<UIData, Integer> savedRowIndexes = new HashMap<UIData, Integer>();
       
       // save current row and set row needed to get value
       for (Iterator<UIData> i = uiDataRowMap.keySet().iterator(); i.hasNext();)
       {
          UIData uiData = i.next();
          savedRowIndexes.put(uiData, uiData.getRowIndex());
          uiData.setRowIndex(uiDataRowMap.get(uiData));
       }
       
       Object value = component.getValue();
       
       // restore rows
       for (Iterator<UIData> i = savedRowIndexes.keySet().iterator(); i.hasNext();)
       {
          UIData uiData = i.next();
          uiData.setRowIndex(savedRowIndexes.get(uiData));
       }
       
       return value;
    }
}