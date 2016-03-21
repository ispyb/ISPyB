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

<%@page import="java.util.Calendar,java.util.GregorianCalendar"%>

<%
    //String targetCreateLabContact = request.getContextPath() + "/user/loginLabContactAction.do?reqCode=loginDisplay";
	String targetUpdateDB 	= request.getContextPath() + "/updateDB.do?reqCode=updateProposal"; 
%>

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />
<%-------------------------%>

<%-- Errors --%>
<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />

<%-- Page --%>
<layout:grid cols="1"  borderSpacing="5">
<c:if test="${SITE_ATTRIBUTE eq 'DLS'}">
	<%-- DLS Warning --%>
	<layout:panel key="Please note" align="left" styleClass="PANEL">
	<font color="red">Please contact your designated point of contact before using this functionality.</font>
	</layout:panel>
</c:if>
<%-- Shipping --%>

<layout:panel key="New/Edit Shipment" align="left" styleClass="PANEL">
	<layout:form action="/user/createShippingAction.do" reqCode="save">	
		<layout:grid cols="1" styleClass="SEARCH_GRID">	
			<layout:column>
			
				<layout:panel key="Details" align="left" styleClass="PANEL" width="100%">
					<layout:text key="Creation date"	property="info.creationDateStr"	styleClass="FIELD"	mode="I,I,E"/>
					<layout:space/>
					<layout:text key="Shipment label"	property="info.shippingName"	styleClass="FIELD"	mode="E,I,I" isRequired="true"/>
					<layout:text key="Number of dewars"	 property="nbDewars" styleClass="FIELD"	mode="E,I,I" isRequired="true" tooltip="# of dewar in Shipment." />
					<layout:text key="Number of other components <br/><i>(i.e. toolbox, laser...)</i>"	property="nbOtherComponents" styleClass="FIELD"	mode="E,I,I" isRequired="true" tooltip="# of items in Shipment. i.e. toolboxes, laser, ..." />		
					<layout:space/>
					
					<logic:notEmpty name="viewShippingForm" property="listSessions">
						<layout:select size="6" multiple="true" key="Beamline / Experiment"    property="experimentsScheduled" styleClass="FIELD" mode="E,E,I" isRequired="true" tooltip="Select experiment(s) for which this shipment is meant. (Use Shift/Ctrl to select several)">
							<bean:define id="listAvailSessions" name="viewShippingForm" property="listSessions" type="java.util.ArrayList"/>
							<layout:options collection="listAvailSessions" property="sessionId"  labelProperty="sessionDescription"/>
						</layout:select>
					</logic:notEmpty>
					<logic:empty name="viewShippingForm" property="listSessions">
						<tr><th class="FIELD">Beamline / Experiment</th><td class="FIELD"><font color="red">No <%=Constants.SESSION_VISIT%> available</font></td></tr>
					</logic:empty>					
						<tr><td class="FIELD" >--> If you do not find your latest sessions </td>
						<td class="FIELD" >
							<a href="<%=targetUpdateDB%>" >update ISPyB database.</a>
						</td>
						</tr>
					<layout:space/>
					
					<layout:textarea key="Comments" 			property="info.comments" 			styleClass="OPTIONAL"	mode="E,E,I"	size="25" rows="4"/>
					<layout:text key="Shipment status"			property="info.shippingStatus" 		styleClass="FIELD"	mode="I,I,I" />
				</layout:panel>
				
				<layout:panel key="Lab-contacts" align="left" styleClass="PANEL">
					<bean:define id="listDefaultCourierCompany" name="viewShippingForm" property="listDefaultCourierCompany"></bean:define>
					<bean:define id="listCourierAccount" name="viewShippingForm" property="listCourierAccount"></bean:define>
					<bean:define id="listBillingReference" name="viewShippingForm" property="listBillingReference"></bean:define>
					<bean:define id="listDewarAvgCustomsValue" name="viewShippingForm" property="listDewarAvgCustomsValue"></bean:define>
					<bean:define id="listDewarAvgTransportValue" name="viewShippingForm" property="listDewarAvgTransportValue"></bean:define>
					
					
					<layout:select onchange="<%= \"sendingLabContactHasChanged(\"+listDefaultCourierCompany+\", \"+listCourierAccount+\", \"+listBillingReference+\", \"+listDewarAvgCustomsValue+\", \"+listDewarAvgTransportValue+\");\"%>" key="Lab-Contact for sending" 	property="sendingLabContactId"  styleClass="COMBO" mode="E,E,I" isRequired="true">
						<bean:define id="listLabContacts" name="viewShippingForm" property="listLabContacts" type="java.util.ArrayList"/>
						<layout:option key="select a contact card" value="0"/>
						<layout:options collection="listLabContacts" property="labContactId"  labelProperty="cardName"/>
					</layout:select>
					<layout:link title="Lab-Contact card" href="javascript:document.viewShippingForm.elements['reqCode'].value='editLabContact';document.viewShippingForm.submit();" styleClass="FIELD">
						Creation/Edition of a Lab-Contact card
					</layout:link>
					
					<layout:checkbox styleId="checkboxSameReturnAdress"  key="Return adress is identical as sending adress (Y/N)" 	onclick="<%= \"setSameLabContactForReturn2(\"+listDefaultCourierCompany+\", \"+listCourierAccount+\", \"+listBillingReference+\", \"+listDewarAvgCustomsValue+\", \"+listDewarAvgTransportValue+\");\"%>" property="isIdenticalReturnAddress"	 styleClass="FIELD" mode="E,E,I"/>
					
					<layout:select onchange="<%= \"returnLabContactHasChanged(\"+listDefaultCourierCompany+\", \"+listCourierAccount+\", \"+listBillingReference+\", \"+listDewarAvgCustomsValue+\", \"+listDewarAvgTransportValue+\");\"%>" key="If No, Lab-Contact for Return" 	property="returnLabContactId" styleClass="COMBO"	mode="E,E,I" isRequired="true">
						<bean:define id="listLabContacts" name="viewShippingForm" property="listLabContacts" type="java.util.ArrayList"/>
						<layout:option key="select a contact card" value="0"/>
						<layout:options collection="listLabContacts" property="labContactId"  labelProperty="cardName"/>
					</layout:select>
					
					<jsp:include page="../labcontact/editCourierAccountDetails.jsp" flush="true" />
				</layout:panel>
			</layout:column>
			
			<layout:space/>
			<layout:row styleClass="rowAlignCenter">
				<layout:submit reqCode="save" onclick="alertCourierAccountsDetails();"><layout:message key="Save"/></layout:submit>
				<layout:submit reqCode="saveAndFill" onclick="alertCourierAccountsDetails();"><layout:message key="Save and Edit"/></layout:submit>
			</layout:row>
		</layout:grid>
	</layout:form>	
</layout:panel>

<%-- Acknowledge Action --%>
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />
</layout:grid>

<script language="JavaScript" type="text/javascript">
<!--
var sendingLabContact = document.getElementsByName("sendingLabContactId")[0];
var returnLabContact = document.getElementsByName("returnLabContactId")[0];
var checkboxSameReturnLabContact = document.getElementsByName("isIdenticalReturnAddress")[0];

//save original style
var fontWeight_safe = returnLabContact.style.fontWeight;
var returnBorder_safe = returnLabContact.style.border;
var sendingBorder_safe = sendingLabContact.style.border;

function sendingLabContactHasChanged(listDefaultCourierCompany,listCourierAccount, listBillingReference , listDewarAvgCustomsValue, listDewarAvgTransportValue) {
	//reset border
	sendingLabContact.style.border = sendingBorder_safe;
	//check if return labContact must equal to the sending labContact
	if (checkboxSameReturnLabContact.checked) {
		//update return labContact field
		returnLabContact.value = sendingLabContact.value;
		returnLabContactHasChanged(listDefaultCourierCompany,listCourierAccount, listBillingReference , listDewarAvgCustomsValue, listDewarAvgTransportValue);
	}
}

//Controle les champs : sendingLabContact et returnLabContact
function setSameLabContactForReturn() {
	//si on a pas choisi le sendingLabContact
	if (sendingLabContact.value == "") {
		//on autorise pas de cocher la checkbox 
		//checkboxSameReturnAdress.checked = false;
		document.getElementById("checkboxSameReturnAdress").checked = false;
		sendingLabContact.style.border = "1px solid red"; 
	}
	else {
		sendingLabContact.style.border = sendingBorder_safe;
	}
//	if (checkboxSameReturnAdress.checked) {
	if(document.getElementById("checkboxSameReturnAdress").checked){
		//same address
		returnLabContact.value = sendingLabContact.value;
		returnLabContact.readonly = true;
		returnLabContact.disabled = true;
		//modify the style
		returnLabContact.style.fontWeight = "bold";
		returnLabContact.style.border = "0px solid #000"; 
	}
	else {
		//not same address for return
		returnLabContact.value = "";
		returnLabContact.readonly = false;
		returnLabContact.disabled = false;
		//reset the original style
		returnLabContact.style.fontWeight = fontWeight_safe;
		returnLabContact.style.border = returnBorder_safe;
	}
}

function setSameLabContactForReturn2(listDefaultCourierCompany,listCourierAccount, listBillingReference , listDewarAvgCustomsValue, listDewarAvgTransportValue) {
	setSameLabContactForReturn();
	returnLabContactHasChanged(listDefaultCourierCompany,listCourierAccount, listBillingReference , listDewarAvgCustomsValue, listDewarAvgTransportValue);
}

window.onload=setSameLabContactForReturn;

// return labcontact changed => courier accounts details changed
function returnLabContactHasChanged(listDefaultCourierCompany,listCourierAccount, listBillingReference , listDewarAvgCustomsValue, listDewarAvgTransportValue) {
	var id = returnLabContact.selectedIndex;
	var defaultCourierCompany = "";
	var courierAccount = "";
	var billingReference = "";
	var dewarAvgCustomsValue = "";
	var dewarAvgTransportValue = "";
	if(id > 1){
		defaultCourierCompany = listDefaultCourierCompany[id-1];
		courierAccount = listCourierAccount[id-1];
		billingReference = listBillingReference[id-1];
		dewarAvgCustomsValue = listDewarAvgCustomsValue[id-1];
		dewarAvgTransportValue = listDewarAvgTransportValue[id-1];
	}
	document.getElementById('suggestCourrier').value = defaultCourierCompany;
	document.getElementById('courierAccount').value = courierAccount;
	document.getElementById('billingReference').value = billingReference;
	document.getElementById('dewarAvgCustomsValue').value = dewarAvgCustomsValue;
	document.getElementById('dewarAvgTransportValue').value = dewarAvgTransportValue;
	return false;
}

// alert if courier accounts details are not completed
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
//-->
</script>
