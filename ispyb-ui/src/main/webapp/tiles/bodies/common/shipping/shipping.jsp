<%--------------------------------------------------------------------------------------------------
This file is part of ISPyB.

ISPyB is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

ISPyB is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.

Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, A. De Maria Antolinos
--------------------------------------------------------------------------------------------------%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.application-servers.com/layout" prefix="layout" %>

<%@page import="java.util.Calendar,java.util.GregorianCalendar"%>
<%@page import="ispyb.common.util.Constants"%>

<layout:skin includeScript="true" />

<layout:panel key="Search Shipping" align="left" styleClass="PANEL">
<layout:form action="/reader/genericShippingAction.do" reqCode="search">
	
	<layout:grid cols="1" styleClass="SEARCH_GRID">	
		<layout:column>
					<layout:text key="Shipment label :" property="shippingName" styleClass="FIELD" mode="E,E,E"/>
					<layout:space/>
					
					<layout:date key="Creation date between DD-MM-YYYY :" property="creationDateStart" styleClass="FIELD" mode="E,E,E"
						patternKey="<%=Constants.DATE_FORMAT%>" 
						startYear="<%=(new Integer((new GregorianCalendar()).get(Calendar.YEAR) - 2)).toString()%>" 
						endYear="<%=(new Integer((new GregorianCalendar()).get(Calendar.YEAR))).toString()%>"/>	
					<layout:date key="and DD-MM-YYYY :" property="creationDateEnd" styleClass="FIELD" mode="E,E,E"
						patternKey="<%=Constants.DATE_FORMAT%>" 
						startYear="<%=(new Integer((new GregorianCalendar()).get(Calendar.YEAR) - 2)).toString()%>" 
						endYear="<%=(new Integer((new GregorianCalendar()).get(Calendar.YEAR))).toString()%>"/>
					<layout:space/>
					
					<logic:notEqual name="genericViewShippingForm" property="role" value="<%=Constants.ROLE_USER%>">
						<layout:text key="Main proposer name :" property="mainProposer" styleClass="FIELD" mode="E,E,E"/>
						<layout:space/>
					</logic:notEqual>
					
					<logic:notEqual name="genericViewShippingForm" property="role" value="<%=Constants.ROLE_USER%>">
					<logic:notEqual name="genericViewShippingForm" property="role" value="<%=Constants.ROLE_STORE%>">
						<layout:text key="Proposal number <i>(Ex:MX415)</i> :" property="proposalCodeNumber"   styleClass="FIELD" mode="E,E,E"/>
					</logic:notEqual>
					</logic:notEqual>
					
		</layout:column>
			
		<layout:row>
			<layout:reset/>
			<layout:submit><layout:message key="Search"/></layout:submit>
		</layout:row>
	</layout:grid>
	
	
</layout:form>	
</layout:panel>
			<h4>You may use the * character to do a search with an incomplete field.</h4>

