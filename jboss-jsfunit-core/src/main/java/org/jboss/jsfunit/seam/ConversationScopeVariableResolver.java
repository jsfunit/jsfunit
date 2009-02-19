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

package org.jboss.jsfunit.seam;

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.VariableResolver;

/**
 * VariableResolver for Seam conversation scope objects that JSFUnit stashes
 * in the HttpSession.  This is implemented as a Variableresolver instead of
 * an ELResolver because we want to maintain compatibility with JSF 1.1.
 *
 * @author Stan Silvert
 * @since 1.0
 */
public class ConversationScopeVariableResolver extends VariableResolver
{
   public static final String SEAM_CONVERSATION_EL_IDENTIFIER = "seamconversation";
   
   private VariableResolver delegate;
   
   private boolean isSeamPresent = SeamUtil.isSeamPresent();
   
   /**
    * Create a new instance of ConversationScopeVariableResolver.
    * 
    * @param delegate The next VariableResolver in the resolver chain.
    */
   public ConversationScopeVariableResolver(VariableResolver delegate)
   {
      this.delegate = delegate;
   }

   /**
    * See javadoc for VariableResolver.
    * 
    * @see javax.faces.el.VariableResolver#resolveVariable(javax.faces.context.FacesContext, java.lang.String) 
    */
   public Object resolveVariable(FacesContext facesContext, String name) throws EvaluationException 
   {
      if (isSeamPresent && SEAM_CONVERSATION_EL_IDENTIFIER.equals(name)) {
         return ConversationScope.convCache(facesContext);
      }
         
      return delegate.resolveVariable(facesContext, name);
   }

}