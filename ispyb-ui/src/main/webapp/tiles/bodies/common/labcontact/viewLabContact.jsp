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
<%@page import="ispyb.server.common.vos.proposals.Person3VO"%>
<%@page import="ispyb.server.common.vos.proposals.LabContact3VO"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.application-servers.com/layout" prefix="layout" %>
<%@ page isELIgnored="false" %>
<%	
	String targetDeleteLabContact 	= request.getContextPath() + "/user/viewLabContactAction.do?reqCode=deleteLabContact";
	String targetUpdateLabContact 	= request.getContextPath() + "/user/createLabContactAction.do?reqCode=updateDisplay";
%>

<jsp:useBean id="viewLabContactAction" scope="request" class="ispyb.client.common.labcontact.ViewLabContactAction"/>

<bean:define name="viewLabContactForm" property="listOfLabContacts" id="myList" type="java.util.List"	toScope="request"/>

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<%-- Messages --%>
<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />

<%-- To not show a empty table when no lab-contacts exists --%>	
<logic:empty name="myList">
           <h4>No&nbsp;lab-contact&nbsp;have&nbsp;been&nbsp;found</h4>
</logic:empty>


<logic:notEmpty name="myList">	
	<layout:grid cols="1"  borderSpacing="10">
	
		<%-- Page --%>
		<layout:panel key="All Lab-contacts" align="left" styleClass="PANEL">
		<layout:collection 	name="viewLabContactForm" 
							property="listOfLabContacts"
							styleClass="LIST" styleClass2="LIST2"
							id="currInfo"
							indexId="index">
	
			<layout:collectionItem title="Card name"         property="cardName" 	
				sortable="true" href="<%=targetUpdateLabContact%>" paramId="labContactId" paramProperty="labContactId" />
			
			<layout:collectionItem title="Lab name"          property="personVO.laboratoryVO.name" sortable="true"/>
			<layout:collectionItem title="Lab address">
				<%	
				
				LabContact3VO labcontact = (LabContact3VO)pageContext.getAttribute("currInfo");
				Person3VO person = (Person3VO)labcontact.getPersonVO();
					
					String address = person.getLaboratoryVO().getAddress();
					if (address == null) {
						address = "";
					}
					else {
						address = address.replaceAll("\n", "<br>");
					}
				%>
				<%=address%>
			</layout:collectionItem>
			<layout:collectionItem title="Contact name"      property="personVO.familyName"      sortable="true"/>
			<layout:collectionItem title="Contact first name" property="personVO.givenName"       sortable="true"/>
			<layout:collectionItem title="Contact phone"     property="personVO.phoneNumber"/>
					
			<%-- Actions --%>												
			<layout:collectionItem title="Edit / Remove" width="120">
				<%-- Edit --%>
				<html:link href="<%=targetUpdateLabContact%>" paramName="currInfo" paramId="labContactId" paramProperty="labContactId">
				<img src="<%=request.getContextPath()%>/images/Edit_16x16_01.png" border="0" onmouseover="return overlib('Edit LabContact');" onmouseout="return nd();"/>
				</html:link>
				<%-- Delete only if not linked to a shipment--%>
				<logic:equal name="viewLabContactForm" property="listLabContactDeleted[${index}]" value="0">
					<html:link href="<%=targetDeleteLabContact%>" paramName="currInfo" paramId="labContactId" paramProperty="labContactId" onclick="return window.confirm('Do you really want to delete this lab-contact?');">
						<img src="<%=request.getContextPath()%>/images/cancel.png" border=0 onmouseover="return overlib('Delete the LabContact');" onmouseout="return nd();">
					</html:link>
				</logic:equal>
			</layout:collectionItem>										
			
		</layout:collection>
		</layout:panel>
	
	</layout:grid>
</logic:notEmpty>

