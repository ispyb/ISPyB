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
<%@page import="ispyb.common.util.Constants"%>

<%@ page isELIgnored="false" %>

<%@page import="java.util.Calendar,java.util.GregorianCalendar"%>

<%  
	String shippingPanelTitle = "Submit Shipping : ";
	String targetViewShippings		= request.getContextPath() + "/reader/genericShippingAction.do?reqCode=display";	
	String targetViewDewars			= request.getContextPath() + "/user/viewDewarAction.do?reqCode=display";
	String targetSuggestCourrier 		= "/user/getShipmentCourrierSuggestAction";
	String expectedString 			= "Expected " + Constants.SITE_NAME + " arrival date";
%>

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>

<%	
	String warningMsg = "Warning: Once you've sent the shipment to "+Constants.ESRF_DLS+", you won't be able to modify "+
		"the content of the shipment (dewars, containers, samples,..) and the information above. You will still be able "+
		"to change sessions, add comments and modify Lab-Contacts.";
%>


<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />
<%-------------------------%>

<%-------------------------------------------------------- "View all" links ------------------------------------------------------%>
<layout:row>

		<layout:link href="<%=targetViewShippings%>" styleClass="PANEL_BREAD_CRUMBS">
			<img src="<%=request.getContextPath()%>/images/up.gif" border=0 onmouseover="return overlib('View All Shipments');" onmouseout="return nd();">
			View all Shipments
		</layout:link>

		&nbsp;&nbsp;&nbsp;&nbsp;
		
		<layout:link href="<%=targetViewDewars%>" styleClass="PANEL_BREAD_CRUMBS">
			<img src="<%=request.getContextPath()%>/images/up.gif" border="0" onmouseover="return overlib('View All Dewars in Shipment');" onmouseout="return nd();">
			View all Components in this Shipment
		</layout:link>
	
</layout:row>


<%-- Errors --%>
<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />


<%-- Retrieve a few things from the Beans --%>
<logic:present name="breadCrumbsForm" property="selectedShipping" scope="session">	
	<bean:define name="breadCrumbsForm" property="selectedShipping.shippingName" id="selectedShippingCode" 	type="java.lang.String"/>
	<% shippingPanelTitle += "<i>" +selectedShippingCode+ "</i>"; %>
</logic:present>


<%-- Page --%>
<layout:grid cols="1"  borderSpacing="5">
<%-- Shipping --%>
<layout:panel key="<%=shippingPanelTitle%>" align="left" styleClass="PANEL" width="350">
	<layout:form action="/user/submitShippingAction.do" reqCode="submit">	
		<layout:grid cols="1" styleClass="SEARCH_GRID">	
			<layout:column>
				<layout:text key="Shipping id"			property="shipping.shippingId" 		styleClass="FIELD"	mode="H,H,H"/>
			
				<layout:text key="Creation date"		property="shipping.creationDateStr"	styleClass="FIELD"	mode="I,I,I" />
				<layout:space/>
				
				<layout:suggest key="Courier company" 	property="shipping.deliveryAgentAgentName" styleClass="FIELD"	mode="E,E,I" styleId="suggestCourrier" suggestAction="<%=targetSuggestCourrier%>"/>
				<layout:space/>
				
				<layout:collection 	name="submitShippingForm" property="listDewars" id="dewar" indexId="index">
					<layout:collectionItem>
					<layout:grid cols="1" styleClass="SEARCH_GRID">	
						<layout:row>
							<layout:column>
								<p class="FIELD_BOLD">Tracking number for <i>${dewar.code}</i></p>
							</layout:column>
							<layout:column>
								<html:text size="10" property="trackingNumbers[${index}]" value="${dewar.trackingNumberToSynchrotron}" />
							</layout:column>	
						</layout:row>	
					</layout:grid>	
					</layout:collectionItem>
				</layout:collection>
				
				<layout:space/>
				<layout:date key="Sending Date (DD-MM-YYYY)" property="sendingDate" styleClass="FIELD" mode="E,E,E" isRequired="true"
						patternKey="<%=Constants.DATE_FORMAT%>" 
						startYear="<%=(new Integer((new GregorianCalendar()).get(Calendar.YEAR) - 2)).toString()%>" 
						endYear="<%=(new Integer((new GregorianCalendar()).get(Calendar.YEAR))).toString()%>"/>	
						
				<layout:date key="<%=expectedString%>" property="expectedEsrfArrivalDate" styleClass="FIELD" mode="E,E,E" isRequired="true"
						patternKey="<%=Constants.DATE_FORMAT%>" 
						startYear="<%=(new Integer((new GregorianCalendar()).get(Calendar.YEAR) - 2)).toString()%>" 
						endYear="<%=(new Integer((new GregorianCalendar()).get(Calendar.YEAR))).toString()%>"/>	
				
				<layout:space/>
				<layout:text key="Shipment status"			property="shipping.shippingStatus" 		styleClass="FIELD"	mode="I,I,I" value="<%=Constants.SHIPPING_STATUS_SENT_TO_ESRF%>"/>
				
			</layout:column>
			<layout:space/>
			<layout:row styleClass="rowAlignCenter">
				<layout:submit reqCode="submit"><layout:message key="Save"/></layout:submit>
			</layout:row>
			
			<layout:space/>
			<layout:message styleClass="TEXT_INFO" key="<%=warningMsg%>"/>
		</layout:grid>
	</layout:form>	
</layout:panel>

<%-- Acknowledge Action --%>
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />
</layout:grid>

