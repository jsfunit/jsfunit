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

import javax.faces.component.UIData;
import javax.faces.component.ValueHolder;

/**
 * This class provides a way to get the value from a component that is
 * inside a UIData.  Before a value can be returned, the proper row index
 * must be set on the UIData.
 *
 * @author Stan Silvert
 */
class UIDataValueManager 
{

    private UIData parent;
    private ValueHolder component;
    private int row;

    UIDataValueManager(UIData parent, ValueHolder component, int row)
    {
       this.parent = parent;
       this.component = component;
       this.row = row;
    }
    
    Object getValue()
    {
       int currentRow = parent.getRowIndex();
       parent.setRowIndex(row);
       Object value = component.getValue();
       parent.setRowIndex(currentRow);
       return value;
    }
}