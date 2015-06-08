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

<%	
	String targetLabContact = request.getContextPath() + "/reader/createLabContactAction.do?reqCode=readOnly";
%>

<jsp:useBean id="viewLabContactAction" scope="request" class="ispyb.client.common.labcontact.ViewLabContactAction"/>

<bean:define name="viewLabContactForm" property="listOfLabContacts" id="myList" type="java.util.List"	toScope="request"/>

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>


<%-- To not show a empty table when no lab-contacts exists --%>	
<logic:empty name="myList">
	<h4>No&nbsp;lab-contact&nbsp;have&nbsp;been&nbsp;found</h4>
</logic:empty>


<logic:notEmpty name="myList">
	<layout:grid cols="1"  borderSpacing="10">
	
		<%-- Page --%>
		<layout:panel key="All Lab-contacts" align="left" styleClass="PANEL">
		<layout:collection 	name="viewLabContactForm" property="listOfLabContacts"
							styleClass="LIST" styleClass2="LIST2"
							id="currInfo" >
	
			<layout:collectionItem title="Proposal" sortable="true">
				<bean:write name="currInfo" property="proposalVO.code"/><bean:write name="currInfo" property="proposalVO.number"/>
			</layout:collectionItem>
			
			<layout:collectionItem title="Card name"      property="cardName"
				sortable="true" href="<%=targetLabContact%>"  paramId="labContactId" paramProperty="labContactId" />
			
			<layout:collectionItem title="Contact name"       property="personVO.familyName"      sortable="true"/>
			<layout:collectionItem title="Contact first name" property="personVO.givenName"       sortable="true"/>
			<layout:collectionItem title="Contact phone"      property="personVO.phoneNumber"/>
												
			
		</layout:collection>
		</layout:panel>
	
	</layout:grid>
</logic:notEmpty>

