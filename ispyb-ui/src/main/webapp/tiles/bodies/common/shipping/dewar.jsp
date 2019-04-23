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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@page import="java.util.Calendar,java.util.GregorianCalendar"%>
<%@page import="ispyb.common.util.Constants"%>
<%@ page isELIgnored="false" %>
<layout:skin includeScript="true" />

<layout:panel key="Search Dewars" align="left" styleClass="PANEL">
	<layout:form action="/reader/genericDewarAction.do" reqCode="search">
		<layout:grid cols="1" styleClass="SEARCH_GRID">
			<layout:column>
					<layout:text key="Label" 		property="dewarName" 	styleClass="FIELD" 	mode="E,E,E"/>
					<layout:text key="Comments" 	property="comments" 	styleClass="FIELD"	mode="E,E,E"/>

					<c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
						<logic:equal name="genericViewDewarForm" property="role" value="<%=Constants.ROLE_MANAGER%>">
							<layout:text key="Barcode" 				property="barCode" 	 	styleClass="FIELD"	mode="E,E,E"/>
						</logic:equal>
					</c:if>
					<logic:equal name="genericViewDewarForm" property="role" value="<%=Constants.ROLE_STORE%>">
						<layout:text key="Barcode" 				property="barCode" 	 	styleClass="FIELD"	mode="E,E,E"/>
					</logic:equal>

					<layout:space/>
					
					<layout:date key="Experiment date between DD-MM-YYYY :" property="experimentDateStart" styleClass="FIELD" mode="E,E,E"
						patternKey="<%=Constants.DATE_FORMAT%>" 
						startYear="<%=(new Integer((new GregorianCalendar()).get(Calendar.YEAR) - 2)).toString()%>" 
						endYear="<%=(new Integer((new GregorianCalendar()).get(Calendar.YEAR))).toString()%>"/>	
					<layout:date key="and DD-MM-YYYY :" property="experimentDateEnd" styleClass="FIELD" mode="E,E,E"
						patternKey="<%=Constants.DATE_FORMAT%>" 
						startYear="<%=(new Integer((new GregorianCalendar()).get(Calendar.YEAR) - 2)).toString()%>" 
						endYear="<%=(new Integer((new GregorianCalendar()).get(Calendar.YEAR))).toString()%>"/>
					<layout:space/>
					
					<layout:select key="Status" property="dewarStatus" styleClass="FIELD"	mode="E,E,I" >
						<layout:option key="" 		 value="" />
						<layout:option key="<%=Constants.SHIPPING_STATUS_OPENED%>" 		 value="<%=Constants.SHIPPING_STATUS_OPENED%>" />
						<layout:option key="<%=Constants.SHIPPING_STATUS_READY_TO_GO%>" 		 value="<%=Constants.SHIPPING_STATUS_READY_TO_GO%>" />
						<layout:option key="<%=Constants.SHIPPING_STATUS_SENT_TO_ESRF%>" value="<%=Constants.SHIPPING_STATUS_SENT_TO_ESRF%>" />
						<layout:option key="<%=Constants.SHIPPING_STATUS_AT_ESRF%>" 	 value="<%=Constants.SHIPPING_STATUS_AT_ESRF%>" />
						<layout:option key="<%=Constants.SHIPPING_STATUS_SENT_TO_USER%>" value="<%=Constants.SHIPPING_STATUS_SENT_TO_USER%>" />
					</layout:select>
					<layout:select key="Location" property="storageLocation" styleClass="FIELD"	mode="E,E,I" >
						<layout:option key="" 		 	value="" />
						<layout:option key="<%=Constants.DEWAR_STORES_IN%>"  value="<%=Constants.DEWAR_STORES_IN%>" />
						<c:if test="${SITE_ATTRIBUTE eq 'ESRF'}">
							<layout:option key="<%=Constants.DEWAR_ID14%>" 		 value="<%=Constants.DEWAR_ID14%>" />
							<layout:option key="<%=Constants.DEWAR_ID23%>" 		 value="<%=Constants.DEWAR_ID23%>" />
							<layout:option key="<%=Constants.DEWAR_ID29%>" 		 value="<%=Constants.DEWAR_ID29%>" />
						</c:if>
						<c:if test="${SITE_ATTRIBUTE eq 'EMBL'}">
							<layout:option key="<%=Constants.DEWAR_P12%>" 		 value="<%=Constants.DEWAR_P12%>" />
							<layout:option key="<%=Constants.DEWAR_P13%>" 		 value="<%=Constants.DEWAR_P13%>" />
							<layout:option key="<%=Constants.DEWAR_P14%>" 		 value="<%=Constants.DEWAR_P14%>" />
							<layout:option key="<%=Constants.DEWAR_PE2%>" 		 value="<%=Constants.DEWAR_PE2%>" />
						</c:if>		
						<c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
							<layout:option key="<%=Constants.DEWAR_I9113%>" 		 value="<%=Constants.DEWAR_I9113%>" />
						</c:if>
						<layout:option key="<%=Constants.DEWAR_STORES_OUT%>" value="<%=Constants.DEWAR_STORES_OUT%>" />
					</layout:select>
					
			</layout:column>
			
			<layout:row>
				<layout:reset/>
				<layout:submit><layout:message key="Search"/></layout:submit>
			</layout:row>
		</layout:grid>
		
		
	</layout:form>
</layout:panel>
				<h4>You may use the * character to do a search with an incomplete field.</h4>

