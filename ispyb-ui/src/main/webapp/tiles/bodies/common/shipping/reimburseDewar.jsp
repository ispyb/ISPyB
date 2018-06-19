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

<%	
	String targetViewDewars			= request.getContextPath() + "/menuSelected.do?leftMenuId=8&topMenuId=5&targetUrl=/user/viewDewarAction.do?reqCode=display";
	String selectedDewarTitle		= 	"<a href=\"" + targetViewDewars + "\">" +
										"<img src=\"" + request.getContextPath() + "/images/up.gif\" border=0 onmouseover=\"return overlib('View Dewars');\" onmouseout=\"return nd();\">" +
										"</a>" +
										"Selected Dewar";
	String dewarPanelTitle			= "All Components in Shipment : ";
	String containerPanelTitle		= "All Containers in Dewar : ";
%>

<layout:skin includeScript="true"/>
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<%-- Retrieve a few things from the Beans --%>
<logic:present name="breadCrumbsForm" property="selectedShipping" scope="session">	
	<bean:define name="breadCrumbsForm" property="selectedShipping.shippingName" id="selectedShippingCode" 	type="java.lang.String"/>
	<% dewarPanelTitle += selectedShippingCode; %>
</logic:present>

<logic:present name="breadCrumbsForm" property="selectedDewar" scope="session">	
	<bean:define name="breadCrumbsForm" property="selectedDewar.code" id="selectedDewarCode" 	type="java.lang.String"/>
	<% 
	dewarPanelTitle 		= "Selected Component : " + selectedDewarCode;
	containerPanelTitle		+= selectedDewarCode;
	%>
</logic:present>

<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />
<%-------------------------%>

<%-- Errors --%>
<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />


<%-- Page --%>
<layout:grid cols="1"  borderSpacing="10">
	<%-- Dewar --%>
	<layout:panel key="Acknowledge the conditions and set the reimbursement status" align="left" styleClass="PANEL">
		<layout:form action="/user/viewDewarAction.do" reqCode="setReimbursed">	
		<bean:define name="viewDewarForm" property="fedexCode" id="fedexCode" 	type="java.lang.String"/>
		<layout:grid cols="1"  borderSpacing="10">
				<layout:column>				
				<p><center><b>Acknowledge the conditions and set the reimbursement status</b></center>
				<br><center><b>Declaration</b></center>
				<br> By setting this dewar to reimbursed, the labels that will be generated for sending the dewar by courier will use the ESRF account.
				<br> You MUST NOT use this account to ship more than the allowed number of dewars, or any other equipment for this or any other experiment.
				<br> Any abuse of this account will immediately result in your proposal being refused access to the dewar reimbursement procedure, and eventually to the ESRF beamlines.
				<br>				
				<br>For ESRF reimbursement, you MUST: 
				<br>
				<br>- Copy and paste the following information into the courier service request form:
				<br>&nbsp;&nbsp;-> FedEx Account Number : <b><%=Constants.SHIPPING_DELIVERY_AGENT_FEDEX_ACCOUNT%></b>
				<br>&nbsp;&nbsp;-> Your Reference : <b><%=fedexCode%></b>
				<br>
				<br>- Tick the 'Include a return label' box.	
				<br>
				<br>
				<br> Please click on the following checkbox if you agree with these conditions and you wish to have this dewar automatically reimbursed by the ESRF.				
				</p>								
				<layout:space/>
						<layout:checkbox key="I agree, please set this dewar to reimbursed" value="1" property="info.isReimbursed" styleClass="FIELD" mode="E,E,E"/>	
				<layout:space/>								
				</layout:column> 
			</layout:grid>
				
					<layout:submit reqCode="setReimbursed"><layout:message key="Save"/></layout:submit>
			
		</layout:form>
		
	</layout:panel>
	<layout:space/>
</layout:grid>
																	 
			
