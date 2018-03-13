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
<%@ page isELIgnored="false" %>
<%@page import="ispyb.common.util.Constants"%>

<%	
//<layout:text key="person ID" 	property="labContact.personVO.personId"   styleClass="FIELD"	mode="H,H,H"/>
//<layout:text key="proposal ID" 	property="labContact.proposalVO.proposalId" styleClass="FIELD"	mode="H,H,H"/>
	String targetSuggestCourrier 		= "/user/getShipmentCourrierSuggestAction";
	String returnCourier = "Courier company <u>for return</u> (if " + Constants.SITE_NAME +" sends a dewar back)";
%>

<script TYPE="text/javascript">
	function alertCourierAccountsDetails(defaultCourierCompany, courierAccount){
		var msg = "Please make sure that\n";
		var fieldsMissing = false;
		var defaultCourierCompany = "";
		if(document.getElementById('suggestCourrier') != null){
			defaultCourierCompany =document.getElementById('suggestCourrier').value;
		}
		var courierAccount = "";
		if(document.getElementById('courierAccount') != null){
		courierAccount = document.getElementById('courierAccount').value;
		}
		if(defaultCourierCompany == null || defaultCourierCompany == ""){
			fieldsMissing = true;
			msg += "- Courier company for return\n";			
		}
		if(courierAccount == null || courierAccount == ""){
			fieldsMissing = true;
			msg += "- Courier account \n";			
		}
		if(fieldsMissing == true){
			msg += "is provided as it is important for the safe return of your dewar.";
			alert(msg);	
		}
		return false;
	}
</script> 

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>


<%-- Errors --%>
<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />

<%-- Page --%>
<layout:grid cols="1"  borderSpacing="5">
<%-- LabContact --%>
<layout:panel key="New/Edit LabContact" align="left" styleClass="PANEL">
<h3><font color=orange>Note that your changes in the lab contact name and address may be updated by the labcontact details as entered in the User Portal.<br>
If you want to make them permanent, then update the User Portal account. </font></h3>
	<layout:form action="/user/createLabContactAction.do" reqCode="save">
	<layout:text key="shippingId"	name="viewLabContactForm" property="shippingId"  mode="H,H,H"/>	
		<layout:grid cols="1" styleClass="SEARCH_GRID">	
			<layout:column>
			
			<layout:panel key="Lab-contact card" width="100%" align="left" styleClass="PANEL">
				<layout:text key="labContact ID" property="labContact.labContactId" styleClass="FIELD"	mode="H,H,H"/>
				
				
				<layout:text key="Card name" 	property="labContact.cardName"   styleClass="FIELD"	mode="E,E,I" isRequired="true" size="40"/> 
			</layout:panel>
			
			<layout:panel key="Contact person info" width="100%" align="left" styleClass="PANEL">
				<layout:text key="person ID" 	property="person.personId"		styleClass="FIELD"	mode="H,H,H"/>
				<layout:text key="person login"	property="person.login"			styleClass="FIELD"	mode="H,H,H"/>
				<c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
					<layout:text key="Family name" 	property="person.familyName"   styleClass="FIELD" mode="E,E,I" isRequired="true"/>
					<layout:text key="First name" 	property="person.givenName"    styleClass="FIELD" mode="E,E,I" isRequired="true"/>
				</c:if>
				<c:if test="${SITE_ATTRIBUTE ne 'MAXIV'}">
					<layout:text key="Family name" 	property="person.familyName"   styleClass="FIELD" mode="I,I,I" isRequired="true"/>
					<layout:text key="First name" 	property="person.givenName"    styleClass="FIELD" mode="I,I,I" isRequired="true"/>
				</c:if>
				<layout:text key="Telephone" 	property="person.phoneNumber"  styleClass="FIELD" mode="E,E,I"/>
				<layout:text key="Fax" 			property="person.faxNumber"    styleClass="FIELD" mode="E,E,I"/>
				<layout:text key="Email" 		property="person.emailAddress" styleClass="FIELD" mode="E,E,I"/>
			</layout:panel>
			
			<layout:panel key="Laboratory info" width="100%" align="left"          styleClass="PANEL">
				<layout:text key="laboratoryID"	property="laboratory.laboratoryId" styleClass="FIELD"	mode="H,H,H"/>
				<layout:text key="Lab name" 	property="laboratory.name" 		styleClass="FIELD"	mode="E,E,I" isRequired="true" size="59"/>
				<layout:textarea key="Lab address (*)" 	property="laboratory.address" 	styleClass="FIELD"	mode="E,E,I" isRequired="true" size="53" rows="4"/>
				<TR><TD></TD><TD class="FIELD"><FONT color="red">(*) address must fit in the text box without scrolling</FONT></TD></TR>
			</layout:panel>
			
			<%--
			<layout:panel key="Default info" width="100%" align="left" styleClass="PANEL">
				<layout:suggest key="<%=returnCourier%>" property="labContact.defaultCourrierCompany" styleClass="FIELD"	mode="E,E,I" styleId="suggestCourrier" suggestAction="<%=targetSuggestCourrier%>"/>
				<layout:text key="Courier account" property="labContact.courierAccount"   styleClass="FIELD"	mode="E,E,I"/>
				<layout:text key="Billing reference" property="labContact.billingReference"   styleClass="FIELD"	mode="E,E,I"/>					 	
				<layout:text key="Average Customs value of a dewar (Euro)" property="labContact.dewarAvgCustomsValue"   styleClass="FIELD"	mode="E,E,I"/>
				<layout:text key="Average Transport value of a dewar (Euro)" property="labContact.dewarAvgTransportValue" styleClass="FIELD"	mode="E,E,I"/>
			</layout:panel>
			
			--%>
			
			<jsp:include page="editCourierAccountDetails.jsp" flush="true" />
			
			</layout:column>
			
			<layout:space/>
			<layout:row styleClass="rowAlignCenter">
					<layout:submit reqCode="save" mode="D,D,N" onclick="alertCourierAccountsDetails();"><layout:message key="Save card" /></layout:submit>
				<logic:notEmpty name="viewLabContactForm" property="shippingId">
					<layout:submit reqCode="backToShipment"><layout:message key="Back to shipment"/></layout:submit>
				</logic:notEmpty>
			</layout:row>
		</layout:grid>
	</layout:form>	
</layout:panel>

<%-- Acknowledge Action --%>
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />
</layout:grid>

