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
package org.jboss.jsfunit.spy.data;

import javax.faces.application.FacesMessage;

/**
 * Read-only FacesMessage adds the Client Id (if any) that generated
 * the message.
 *
 * @author Stan Silvert
 * @since 1.1
 */
public class SpyFacesMessage
{

   private String clientId;
   private String severity;
   private String summary;
   private String detail;

   SpyFacesMessage(String clientId, FacesMessage facesMessage)
   {
      if (facesMessage == null)
      {
         throw new NullPointerException("facesMessage can not be nuill");
      }
      this.clientId = clientId;
      this.severity = facesMessage.getSeverity().toString();
      this.summary = facesMessage.getSummary();
      this.detail = facesMessage.getDetail();
   }

   public String getClientId()
   {
      return clientId;
   }

   public String getDetail()
   {
      return detail;
   }

   public String getSeverity()
   {
      return severity;
   }

   public String getSummary()
   {
      return summary;
   }
}
