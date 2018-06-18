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

<%@ page isELIgnored="false" %>

<%	
	String targetSuggestCourrier = "/user/getShipmentCourrierSuggestAction";
	String targetLabContactForm  = request.getContextPath() + "/user/createLabContactAction.do?reqCode=displaySelectedPerson";
	String targetDummyLabContactForm  = request.getContextPath() + "/user/createLabContactAction.do?reqCode=createDummyLabContact";
%>


<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>


<%-- Errors --%>
<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />

<%-- Page --%>
<layout:grid cols="1"  borderSpacing="5">

<%-- Form to find a scientist ------------------------------------------------------- --%>
<layout:panel key="New/Edit LabContact" align="left" styleClass="PANEL">
	<layout:form action="/user/createLabContactAction.do" reqCode="login">	
	    <layout:text key="shippingId"	name="viewLabContactForm" property="shippingId"  mode="H,H,H"/>
		<layout:grid cols="1" styleClass="SEARCH_GRID">	
			<layout:column>
				<layout:text key="Scientist name" property="name"	 styleClass="FIELD"	mode="E,E,E" isRequired="false" />
				<layout:text key="Scientist firstname" property="firstName"	 styleClass="FIELD"	mode="E,E,E" isRequired="false" />
			</layout:column>
			<layout:space/>
			<layout:row styleClass="rowAlignCenter">
				<layout:submit reqCode="login"><layout:message key="Search contact"/></layout:submit>
				<logic:notEmpty name="viewLabContactForm" property="shippingId">
					<layout:submit reqCode="backToShipment"><layout:message key="Back to shipment"/></layout:submit>
				</logic:notEmpty>
			</layout:row>
			
			
		</layout:grid>
	</layout:form>	
</layout:panel>

<%-- LIST OF SCIENTISTS ----------------------------------------------------------------
   (list visible when there is more than 2 match for the current familyName and firstName)
 --%>
<bean:define name="viewLabContactForm" property="listOfScientists" id="myList" type="java.util.List"	toScope="request"/>
<bean:define name="viewLabContactForm" property="shippingId" id="shippingId" type="java.lang.String"	toScope="request"/>
<logic:notEmpty name="myList">
	
	<layout:panel key="Select the scientist contact" align="left" styleClass="PANEL">
		
		<layout:collection 	name="viewLabContactForm"  property="listOfScientists"
							styleClass="LIST" styleClass2="LIST2" id="currInfo" indexId="index" >
				
			<layout:collectionItem title="Scientist name" 		property="person.familyName" sortable="true" />
			<layout:collectionItem title="Scientist first name" property="person.givenName"  sortable="true" />
			<layout:collectionItem title="Lab name"          	property="laboratory.name"   sortable="true" />
			<layout:collectionItem title="Action">
				<%
					ispyb.client.common.labcontact.ScientistInfosBean currInfo = (ispyb.client.common.labcontact.ScientistInfosBean)pageContext.getAttribute("currInfo");
							String url = targetLabContactForm;
							url += "&amp;personId="  + ((currInfo.getPerson().getPersonId()    !=null)? currInfo.getPerson().getPersonId()    :new Integer(0));
							url += "&amp;familyName="+ ((currInfo.getPerson().getFamilyName()  !=null)? currInfo.getPerson().getFamilyName()  :"");
							url += "&amp;firstName=" + ((currInfo.getPerson().getGivenName()   !=null)? currInfo.getPerson().getGivenName()   :"");
							url += "&amp;phone="     + ((currInfo.getPerson().getPhoneNumber() !=null)? currInfo.getPerson().getPhoneNumber() :"");
							url += "&amp;fax="       + ((currInfo.getPerson().getFaxNumber()   !=null)? currInfo.getPerson().getFaxNumber()   :"");
							url += "&amp;email="     + ((currInfo.getPerson().getEmailAddress()!=null)? currInfo.getPerson().getEmailAddress():"");
							
							url += "&amp;laboratoryName="   + ((currInfo.getLaboratory().getName()        !=null)? currInfo.getLaboratory().getName()        :"");
							url += "&amp;laboratoryAddress="+ ((currInfo.getLaboratory().getAddress()     !=null)? java.net.URLEncoder.encode(currInfo.getLaboratory().getAddress(), "UTF-8"):"");
							url += "&amp;shippingId="+shippingId;
				%>
				<a href="<%=url%>" class="LIST2">Select</a>
			</layout:collectionItem>

		</layout:collection>
		
	</layout:panel>
</logic:notEmpty>

<%-- Acknowledge Action --%>
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />
</layout:grid>

