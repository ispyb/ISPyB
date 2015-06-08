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

<%@page import="ispyb.common.util.Constants"%>

<%	
	String targetSuggestCourrier 		= "/user/getShipmentCourrierSuggestAction";
	String returnCourier = "Courier company <u>for return</u> (if " + Constants.SITE_NAME +" sends a dewar back)";
%>

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>


<%-- Courier Accounts details --%>
<layout:panel key="Courier accounts details for return" width="100%" align="left" styleClass="PANEL">
	<tr><td class="FIELD"><font color="orange">These informations are relevant for <u>all</u> shipments.</font></td></tr>
	<layout:suggest key="<%=returnCourier%>" property="labContact.defaultCourrierCompany" styleClass="FIELD"	mode="E,E,I" styleId="suggestCourrier" suggestAction="<%=targetSuggestCourrier%>"/>
	<layout:text key="Courier account" property="labContact.courierAccount"   styleClass="FIELD" styleId="courierAccount"	mode="E,E,I"/>
	<layout:text key="Billing reference" property="labContact.billingReference"   styleClass="FIELD"	styleId="billingReference" mode="E,E,I"/>					 	
	<layout:text key="Average Customs value of a dewar (Euro)" property="labContact.dewarAvgCustomsValue"  styleId="dewarAvgCustomsValue" styleClass="FIELD"	mode="E,E,I"/>
	<layout:text key="Average Transport value of a dewar (Euro)" property="labContact.dewarAvgTransportValue" styleId="dewarAvgTransportValue" styleClass="FIELD"	mode="E,E,I"/>
</layout:panel>


