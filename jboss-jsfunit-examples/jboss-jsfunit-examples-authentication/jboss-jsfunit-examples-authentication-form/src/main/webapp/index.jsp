<%--
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
 */
--%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<HTML>

<f:view>  
   <h1><h:outputText value="JSFUnit  Form Authentication Demo Application" id="title"/></h1>
    
   <h:outputLink id="securedPageLink" value="secured-page.faces">
      <h:outputText value="Click here to go to the secured page"/>
   </h:outputLink>   

   <br/><br/>
   <h2><h:outputText value="Sample JSFUnit Tests You Can Run Against This Application"/></h2>
   
   <h:panelGrid columns="3" border="1" cellpadding="2" id="panelGrid">
      <h:outputText value="FormAuthenticationTest"/>
      <h:outputLink id="RunFormAuthenticationTest" 
                  value="ServletTestRunner?suite=org.jboss.jsfunit.example.authentication.form.FormAuthenticationTest&xsl=cactus-report.xsl">
          <h:outputText value="Run Test"/>
      </h:outputLink>
      <h:outputLink id="SourceFormcAuthenticationTest" 
                  value="FormAuthenticationTest.java">
          <h:outputText value="View Source"/>
      </h:outputLink>

   </h:panelGrid>
      
</f:view>

</HTML>
