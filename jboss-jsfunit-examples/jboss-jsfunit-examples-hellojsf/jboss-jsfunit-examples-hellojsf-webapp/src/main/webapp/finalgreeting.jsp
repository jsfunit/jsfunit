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
   
      <h:outputText value="Bye #{foo.text}. I enjoyed our chat." id="finalgreeting"/><br/>
      <h:form id="form2">
         <h:commandButton value="Go Back to Start" action="/index.jsp" id="go_back_button"/><br/>
         <h:commandLink value="Go Back to Start Using h:commandLink" action="/index.jsp" id="go_back_link"/><br/>
         <h:commandLink value="Stay here Using h:commandLink" action="/finalgreeting.jsp" id="stay_here_link">
            <f:param id="name" name="name" value="#{foo.text}"/>
         </h:commandLink>
      </h:form>
</f:view>

</HTML>
