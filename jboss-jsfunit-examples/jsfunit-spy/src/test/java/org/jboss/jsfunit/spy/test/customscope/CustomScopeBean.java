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

package org.jboss.jsfunit.spy.test.customscope;

/**
 * Backing bean for custom scope create/destroy UI.
 *
 * @author Stan Silvert
 * @since 1.1
 */
public class CustomScopeBean 
{
   public static final String NAME1 = "my scope1";
   public static final String NAME2 = "my scope2";
   
   private String scopeAction = "";
    
   private MyCustomScope customScope1;
   private MyCustomScope customScope2;

   public String getScopeAction()
   {
      return scopeAction;
   }

   public void setScopeAction(String scopeAction)
   {
      this.scopeAction = scopeAction;
   }
    
   public String doScopeAction()
   {
      if (this.scopeAction.equals("create1")) createCustomScope1();
      if (this.scopeAction.equals("create2")) createCustomScope2();
      if (this.scopeAction.equals("destroy1")) destroyCustomScope1();
      if (this.scopeAction.equals("destroy2")) destroyCustomScope2();
      return null;
   }
   
   private void createCustomScope1()
   {
      this.customScope1 = new MyCustomScope(NAME1);
      customScope1.put("key1", "value1");
      customScope1.put("key2", "value2");
   }
   
   private void createCustomScope2()
   {
      this.customScope2 = new MyCustomScope(NAME2);
      customScope2.put("key1", "value1");
      customScope2.put("key2", "value2");
   }
   
   private void destroyCustomScope1()
   {
      if (this.customScope1 != null) this.customScope1.destroy();
   }
   
   private void destroyCustomScope2()
   {
      if (this.customScope2 != null) this.customScope2.destroy();
   }

}
