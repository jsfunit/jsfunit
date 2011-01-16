<%--
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
--%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<HTML>

<f:view>  
   <h1><h:outputText value="Major Marathons" id="title"/></h1>
   
   <h:form id="form1">    

      <table>
         <tr><th>Marathon</th> <th>Location</th> <th>Select</th></tr>
         <c:forEach var="marathon" items="#{marathons.list}">
            <tr>
              <td><h:outputText value="#{marathon.name}" /></td>
              <td><h:outputText value="#{marathon.location}" /></td>
              <td>
                 <h:commandLink id="marathonSelect" onclick="var foo='bar';" action="#{marathons.select}">
                    <f:param name="selectedName" value="#{marathon.name}"/>
                    <h:outputText id="selectText" value="Select"/>
                 </h:commandLink>
              </td>
            </tr>
         </c:forEach>
      </table>
      <br/><b>
         <h:outputText value="Selected Marathon: #{marathons.selectedMarathon}"/>
      </b>
   </h:form>
   
      
</f:view>

</HTML>
