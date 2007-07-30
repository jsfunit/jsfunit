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
   <h1><h:outputText value="JSFUnit HelloJSF Demo Application" id="title"/></h1>
   
   <h:form id="form1">    

      <h:outputText value="Enter your name:" rendered="#{empty foo.text}" id="prompt"/>
      <h:outputText value="Hello #{foo.text}" rendered="#{!empty foo.text}" id="greeting"/><br/>

      <h:inputText value="#{foo.text}" id="input_foo_text">          
        <f:validateLength minimum="2"/>
      </h:inputText>
      <h:message for="input_foo_text" styleClass="errorMessage"/>
      <br/>
      <h:outputText id="funchecktext" value="Uncheck this box just for fun  "/>
      <h:selectBooleanCheckbox id="funcheck" value="#{checkbox.funCheck}"/>
      <br/><br/>
      <h:commandButton value="Hello" action="/index.jsp" id="submit_button"/>
      <h:commandButton value="Goodbye" action="/finalgreeting.jsp" id="goodbye_button"/>

   </h:form>
   
   <br/><br/>
   <h2><h:outputText value="Sample JSFUnit Tests You Can Run Against This Application"/></h2>
   
   <h:panelGrid columns="3" border="1" cellpadding="2" id="panelGrid">
      <h:outputText value="SimplifiedHelloJSFIntegrationTest"/>
      <h:outputLink id="RunSimplifiedHelloJSFIntegrationTest" 
                  value="ServletTestRunner?suite=org.jboss.jsfunit.example.hellojsf.SimplifiedHelloJSFIntegrationTest&xsl=cactus-report.xsl">
          <h:outputText value="Run Test"/>
      </h:outputLink>
      <h:outputLink id="SourceSimplifiedHelloJSFIntegrationTest" 
                  value="SimplifiedHelloJSFIntegrationTest.java">
          <h:outputText value="View Source"/>
      </h:outputLink>

      <h:outputText value="HelloJSFIntegrationTest"/>
      <h:outputLink id="RunHelloJSFIntegrationTest" 
                  value="ServletTestRunner?suite=org.jboss.jsfunit.example.hellojsf.HelloJSFIntegrationTest&xsl=cactus-report.xsl">
          <h:outputText value="Run Test"/>
      </h:outputLink>
      <h:outputLink id="SourceHelloJSFIntegrationTest" 
                  value="HelloJSFIntegrationTest.java">
          <h:outputText value="View Source"/>
      </h:outputLink>

      <h:outputText value="FacadeAPITest"/>
      <h:outputLink id="RunFacadeAPITest" 
                  value="ServletTestRunner?suite=org.jboss.jsfunit.example.hellojsf.FacadeAPITest&xsl=cactus-report.xsl">
          <h:outputText value="Run Test"/>
      </h:outputLink>
      <h:outputLink id="SourceFacadeAPITest" 
                  value="FacadeAPITest.java">
          <h:outputText value="View Source"/>
      </h:outputLink>

   </h:panelGrid>
      
</f:view>

</HTML>
