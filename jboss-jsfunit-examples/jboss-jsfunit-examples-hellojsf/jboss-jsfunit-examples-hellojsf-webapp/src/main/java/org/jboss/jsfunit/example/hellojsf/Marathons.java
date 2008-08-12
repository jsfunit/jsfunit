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

package org.jboss.jsfunit.example.hellojsf;

import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 * Aggregator of Marathon objects.
 *
 * @author Stan Silvert
 */
public class Marathons
{
   
   private List marathonList = new ArrayList();
   private String selectedMarathon = "Not Selected";

   public Marathons()
   {
      marathonList.add(new Marathon("BAA Boston Marathon", "Boston, MA"));
      marathonList.add(new Marathon("BoA Chicago Marathon", "Chicago, IL"));
      marathonList.add(new Marathon("ING New York Marathon", "New York, NY"));
      marathonList.add(new Marathon("Flora London Marathon", "London, UK"));
      marathonList.add(new Marathon("Berlin Marathon", "Berlin, DE"));
      marathonList.add(new Marathon("Olympic Marathon", "Beijing, CN"));
   }
   
   public List getList()
   {
      return this.marathonList;
   }
   
   public String select()
   {
      FacesContext ctx = FacesContext.getCurrentInstance();
      this.selectedMarathon = (String)ctx.getExternalContext().getRequestParameterMap().get("selectedName");
        
      return null;
   }
   
   public String invalidateSession()
   {
      FacesContext ctx = FacesContext.getCurrentInstance();
      HttpSession session = (HttpSession)ctx.getExternalContext().getSession(true);
      session.invalidate();
      return null;
   }
   
   public String getSelectedMarathon()
   {
      return this.selectedMarathon;
   }
}
